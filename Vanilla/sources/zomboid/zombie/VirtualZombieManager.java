package zombie;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.ZombiesZoneDefinition;
import zombie.characters.action.ActionGroup;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaChunk;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.Vector2;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoFireManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.popman.NetworkZombieSimulator;
import zombie.popman.ZombiePopulationManager;
import zombie.vehicles.PolygonalMap2;

public final class VirtualZombieManager {
   private final ArrayDeque ReusableZombies = new ArrayDeque();
   private final HashSet ReusableZombieSet = new HashSet();
   private final ArrayList ReusedThisFrame = new ArrayList();
   private final ArrayList RecentlyRemoved = new ArrayList();
   public static VirtualZombieManager instance = new VirtualZombieManager();
   public int MaxRealZombies = 1;
   private final ArrayList m_tempZombies = new ArrayList();
   public final ArrayList choices = new ArrayList();
   private final ArrayList bestchoices = new ArrayList();
   HandWeapon w = null;

   public boolean removeZombieFromWorld(IsoZombie var1) {
      boolean var2 = var1.getCurrentSquare() != null;
      var1.getEmitter().unregister();
      var1.removeFromWorld();
      var1.removeFromSquare();
      return var2;
   }

   private void reuseZombie(IsoZombie var1) {
      if (var1 != null) {
         assert !IsoWorld.instance.CurrentCell.getObjectList().contains(var1);

         assert !IsoWorld.instance.CurrentCell.getZombieList().contains(var1);

         assert var1.getCurrentSquare() == null || !var1.getCurrentSquare().getMovingObjects().contains(var1);

         if (!this.isReused(var1)) {
            NetworkZombieSimulator.getInstance().remove(var1);
            var1.resetForReuse();
            this.addToReusable(var1);
         }
      }
   }

   public void addToReusable(IsoZombie var1) {
      if (var1 != null && !this.ReusableZombieSet.contains(var1)) {
         this.ReusableZombies.addLast(var1);
         this.ReusableZombieSet.add(var1);
      }

   }

   public boolean isReused(IsoZombie var1) {
      return this.ReusableZombieSet.contains(var1);
   }

   public void init() {
      if (!GameClient.bClient) {
         IsoZombie var1 = null;
         if (!IsoWorld.getZombiesDisabled()) {
            for(int var2 = 0; var2 < this.MaxRealZombies; ++var2) {
               var1 = new IsoZombie(IsoWorld.instance.CurrentCell);
               var1.getEmitter().unregister();
               this.addToReusable(var1);
            }

         }
      }
   }

   public void Reset() {
      this.bestchoices.clear();
      this.choices.clear();
      this.ReusableZombies.clear();
      this.ReusableZombieSet.clear();
      this.ReusedThisFrame.clear();
   }

   public void update() {
      long var1 = System.currentTimeMillis();

      int var3;
      IsoZombie var4;
      for(var3 = this.RecentlyRemoved.size() - 1; var3 >= 0; --var3) {
         var4 = (IsoZombie)this.RecentlyRemoved.get(var3);
         var4.updateEmitter();
         if (var1 - var4.removedFromWorldMS > 5000L) {
            if (var4.vocalEvent != 0L) {
               var4.getEmitter().stopSound(var4.vocalEvent);
               var4.vocalEvent = 0L;
            }

            var4.getEmitter().stopAll();
            this.RecentlyRemoved.remove(var3);
            this.ReusedThisFrame.add(var4);
         }
      }

      if (!GameClient.bClient && !GameServer.bServer) {
         for(var3 = 0; var3 < IsoWorld.instance.CurrentCell.getZombieList().size(); ++var3) {
            var4 = (IsoZombie)IsoWorld.instance.CurrentCell.getZombieList().get(var3);
            if (!var4.KeepItReal && var4.getCurrentSquare() == null) {
               var4.removeFromWorld();
               var4.removeFromSquare();

               assert this.ReusedThisFrame.contains(var4);

               assert !IsoWorld.instance.CurrentCell.getZombieList().contains(var4);

               --var3;
            }
         }

         for(var3 = 0; var3 < this.ReusedThisFrame.size(); ++var3) {
            var4 = (IsoZombie)this.ReusedThisFrame.get(var3);
            this.reuseZombie(var4);
         }

         this.ReusedThisFrame.clear();
      } else {
         for(var3 = 0; var3 < this.ReusedThisFrame.size(); ++var3) {
            var4 = (IsoZombie)this.ReusedThisFrame.get(var3);
            this.reuseZombie(var4);
         }

         this.ReusedThisFrame.clear();
      }
   }

   public IsoZombie createRealZombieAlways(int var1, boolean var2) {
      return this.createRealZombieAlways(var1, var2, 0);
   }

   public IsoZombie createRealZombieAlways(int var1, int var2, boolean var3) {
      int var4 = PersistentOutfits.instance.getOutfit(var1);
      return this.createRealZombieAlways(var2, var3, var4);
   }

   public IsoZombie createRealZombieAlways(int var1, boolean var2, int var3) {
      IsoZombie var4 = null;
      if (!SystemDisabler.doZombieCreation) {
         return null;
      } else if (this.choices != null && !this.choices.isEmpty()) {
         IsoGridSquare var5 = (IsoGridSquare)this.choices.get(Rand.Next(this.choices.size()));
         if (var5 == null) {
            return null;
         } else {
            if (this.w == null) {
               this.w = (HandWeapon)InventoryItemFactory.CreateItem("Base.Axe");
            }

            if ((GameServer.bServer || GameClient.bClient) && var3 == 0) {
               var3 = ZombiesZoneDefinition.pickPersistentOutfit(var5);
            }

            if (this.ReusableZombies.isEmpty()) {
               var4 = new IsoZombie(IsoWorld.instance.CurrentCell);
               var4.bDressInRandomOutfit = var3 == 0;
               var4.setPersistentOutfitID(var3);
               IsoWorld.instance.CurrentCell.getObjectList().add(var4);
            } else {
               var4 = (IsoZombie)this.ReusableZombies.removeFirst();
               this.ReusableZombieSet.remove(var4);
               var4.getHumanVisual().clear();
               var4.clearAttachedItems();
               var4.clearItemsToSpawnAtDeath();
               var4.bDressInRandomOutfit = var3 == 0;
               var4.setPersistentOutfitID(var3);
               var4.setSitAgainstWall(false);
               var4.setOnDeathDone(false);
               var4.setOnKillDone(false);
               var4.setDoDeathSound(true);
               var4.setHitTime(0);
               var4.setFallOnFront(false);
               var4.setFakeDead(false);
               var4.setReanimatedPlayer(false);
               var4.setStateMachineLocked(false);
               Vector2 var6 = var4.dir.ToVector();
               var6.x += (float)Rand.Next(200) / 100.0F - 0.5F;
               var6.y += (float)Rand.Next(200) / 100.0F - 0.5F;
               var6.normalize();
               var4.setForwardDirection(var6);
               IsoWorld.instance.CurrentCell.getObjectList().add(var4);
               var4.walkVariant = "ZombieWalk";
               var4.DoZombieStats();
               if (var4.isOnFire()) {
                  IsoFireManager.RemoveBurningCharacter(var4);
                  var4.setOnFire(false);
               }

               if (var4.AttachedAnimSprite != null) {
                  var4.AttachedAnimSprite.clear();
               }

               var4.thumpFlag = 0;
               var4.thumpSent = false;
               var4.mpIdleSound = false;
               var4.soundSourceTarget = null;
               var4.soundAttract = 0.0F;
               var4.soundAttractTimeout = 0.0F;
               var4.bodyToEat = null;
               var4.eatBodyTarget = null;
               var4.atlasTex = null;
               var4.clearVariables();
               var4.setStaggerBack(false);
               var4.setKnockedDown(false);
               var4.setKnifeDeath(false);
               var4.setJawStabAttach(false);
               var4.setCrawler(false);
               var4.initializeStates();
               var4.actionContext.setGroup(ActionGroup.getActionGroup("zombie"));
               var4.advancedAnimator.OnAnimDataChanged(false);
               var4.setDefaultState();
               var4.getAnimationPlayer().resetBoneModelTransforms();
            }

            var4.dir = IsoDirections.fromIndex(var1);
            var4.setForwardDirection(var4.dir.ToVector());
            var4.getInventory().setExplored(false);
            if (var2) {
               var4.bDressInRandomOutfit = true;
            }

            var4.target = null;
            var4.TimeSinceSeenFlesh = 100000.0F;
            if (!var4.isFakeDead()) {
               if (SandboxOptions.instance.Lore.Toughness.getValue() == 1) {
                  var4.setHealth(3.5F + Rand.Next(0.0F, 0.3F));
               }

               if (SandboxOptions.instance.Lore.Toughness.getValue() == 2) {
                  var4.setHealth(1.5F + Rand.Next(0.0F, 0.3F));
               }

               if (SandboxOptions.instance.Lore.Toughness.getValue() == 3) {
                  var4.setHealth(0.5F + Rand.Next(0.0F, 0.3F));
               }

               if (SandboxOptions.instance.Lore.Toughness.getValue() == 4) {
                  var4.setHealth(Rand.Next(0.5F, 3.5F) + Rand.Next(0.0F, 0.3F));
               }
            } else {
               var4.setHealth(0.5F + Rand.Next(0.0F, 0.3F));
            }

            float var11 = (float)Rand.Next(0, 1000);
            float var7 = (float)Rand.Next(0, 1000);
            var11 /= 1000.0F;
            var7 /= 1000.0F;
            var11 += (float)var5.getX();
            var7 += (float)var5.getY();
            var4.setCurrent(var5);
            var4.setMovingSquareNow();
            var4.setX(var11);
            var4.setY(var7);
            var4.setZ((float)var5.getZ());
            if ((GameClient.bClient || GameServer.bServer) && var4.networkAI != null) {
               var4.networkAI.reset();
            }

            var4.upKillCount = true;
            if (var2) {
               var4.setDir(IsoDirections.fromIndex(Rand.Next(8)));
               var4.setForwardDirection(var4.dir.ToVector());
               var4.setFakeDead(false);
               var4.setHealth(0.0F);
               var4.upKillCount = false;
               var4.DoZombieInventory();
               new IsoDeadBody(var4, true);
               return var4;
            } else {
               synchronized(IsoWorld.instance.CurrentCell.getZombieList()) {
                  var4.getEmitter().register();
                  IsoWorld.instance.CurrentCell.getZombieList().add(var4);
                  if (GameClient.bClient) {
                     var4.bRemote = true;
                  }

                  if (GameServer.bServer) {
                     var4.OnlineID = ServerMap.instance.getUniqueZombieId();
                     if (var4.OnlineID == -1) {
                        IsoWorld.instance.CurrentCell.getZombieList().remove(var4);
                        IsoWorld.instance.CurrentCell.getObjectList().remove(var4);
                        this.ReusedThisFrame.add(var4);
                        return null;
                     }

                     ServerMap.instance.ZombieMap.put(var4.OnlineID, var4);
                  }

                  return var4;
               }
            }
         }
      } else {
         return null;
      }
   }

   private IsoGridSquare pickEatingZombieSquare(float var1, float var2, float var3, float var4, int var5) {
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3, (double)var4, (double)var5);
      if (var6 != null && this.canSpawnAt(var6.x, var6.y, var6.z) && !var6.HasStairs()) {
         return PolygonalMap2.instance.lineClearCollide(var1, var2, var3, var4, var5, (IsoMovingObject)null, false, true) ? null : var6;
      } else {
         return null;
      }
   }

   public void createEatingZombies(IsoDeadBody var1, int var2) {
      if (!IsoWorld.getZombiesDisabled()) {
         for(int var3 = 0; var3 < var2; ++var3) {
            float var4 = var1.x;
            float var5 = var1.y;
            switch(var3) {
            case 0:
               var4 -= 0.5F;
               break;
            case 1:
               var4 += 0.5F;
               break;
            case 2:
               var5 -= 0.5F;
               break;
            case 3:
               var5 += 0.5F;
            }

            IsoGridSquare var6 = this.pickEatingZombieSquare(var1.x, var1.y, var4, var5, (int)var1.z);
            if (var6 != null) {
               this.choices.clear();
               this.choices.add(var6);
               IsoZombie var7 = this.createRealZombieAlways(1, false);
               if (var7 != null) {
                  ZombieSpawnRecorder.instance.record(var7, "createEatingZombies");
                  var7.bDressInRandomOutfit = true;
                  var7.setX(var4);
                  var7.setY(var5);
                  var7.setZ(var1.z);
                  var7.faceLocationF(var1.x, var1.y);
                  var7.setEatBodyTarget(var1, true);
               }
            }
         }

      }
   }

   private IsoZombie createRealZombie(int var1, boolean var2) {
      return GameClient.bClient ? null : this.createRealZombieAlways(var1, var2);
   }

   public void AddBloodToMap(int var1, IsoChunk var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         IsoGridSquare var4 = null;
         int var5 = 0;

         int var7;
         do {
            int var6 = Rand.Next(10);
            var7 = Rand.Next(10);
            var4 = var2.getGridSquare(var6, var7, 0);
            ++var5;
         } while(var5 < 100 && (var4 == null || !var4.isFree(false)));

         if (var4 != null) {
            byte var10 = 5;
            if (Rand.Next(10) == 0) {
               var10 = 10;
            }

            if (Rand.Next(40) == 0) {
               var10 = 20;
            }

            for(var7 = 0; var7 < var10; ++var7) {
               float var8 = (float)Rand.Next(3000) / 1000.0F;
               float var9 = (float)Rand.Next(3000) / 1000.0F;
               --var8;
               --var9;
               var2.addBloodSplat((float)var4.getX() + var8, (float)var4.getY() + var9, (float)var4.getZ(), Rand.Next(12) + 8);
            }
         }
      }

   }

   public ArrayList addZombiesToMap(int var1, RoomDef var2) {
      return this.addZombiesToMap(var1, var2, true);
   }

   public ArrayList addZombiesToMap(int var1, RoomDef var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      if ("Tutorial".equals(Core.GameMode)) {
         return var4;
      } else {
         this.choices.clear();
         this.bestchoices.clear();
         IsoGridSquare var5 = null;

         int var6;
         int var7;
         for(var6 = 0; var6 < var2.rects.size(); ++var6) {
            var7 = var2.level;
            RoomDef.RoomRect var8 = (RoomDef.RoomRect)var2.rects.get(var6);

            for(int var9 = var8.x; var9 < var8.getX2(); ++var9) {
               for(int var10 = var8.y; var10 < var8.getY2(); ++var10) {
                  var5 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var10, var7);
                  if (var5 != null && this.canSpawnAt(var9, var10, var7)) {
                     this.choices.add(var5);
                     boolean var11 = false;

                     for(int var12 = 0; var12 < IsoPlayer.numPlayers; ++var12) {
                        if (IsoPlayer.players[var12] != null && var5.isSeen(var12)) {
                           var11 = true;
                        }
                     }

                     if (!var11) {
                        this.bestchoices.add(var5);
                     }
                  }
               }
            }
         }

         var1 = Math.min(var1, this.choices.size());
         if (!this.bestchoices.isEmpty()) {
            this.choices.addAll(this.bestchoices);
            this.choices.addAll(this.bestchoices);
         }

         for(var6 = 0; var6 < var1; ++var6) {
            if (!this.choices.isEmpty()) {
               var2.building.bAlarmed = false;
               var7 = Rand.Next(8);
               byte var13 = 4;
               IsoZombie var14 = this.createRealZombie(var7, var3 ? Rand.Next(var13) == 0 : false);
               if (var14 != null && var14.getSquare() != null) {
                  if (!GameServer.bServer) {
                     var14.bDressInRandomOutfit = true;
                  }

                  var14.setX((float)((int)var14.getX()) + (float)Rand.Next(2, 8) / 10.0F);
                  var14.setY((float)((int)var14.getY()) + (float)Rand.Next(2, 8) / 10.0F);
                  this.choices.remove(var14.getSquare());
                  this.choices.remove(var14.getSquare());
                  this.choices.remove(var14.getSquare());
                  var4.add(var14);
               }
            } else {
               System.out.println("No choices for zombie.");
            }
         }

         this.bestchoices.clear();
         this.choices.clear();
         return var4;
      }
   }

   public void tryAddIndoorZombies(RoomDef var1, boolean var2) {
   }

   private void addIndoorZombies(int var1, RoomDef var2, boolean var3) {
      this.choices.clear();
      this.bestchoices.clear();
      IsoGridSquare var4 = null;

      int var5;
      int var6;
      for(var5 = 0; var5 < var2.rects.size(); ++var5) {
         var6 = var2.level;
         RoomDef.RoomRect var7 = (RoomDef.RoomRect)var2.rects.get(var5);

         for(int var8 = var7.x; var8 < var7.getX2(); ++var8) {
            for(int var9 = var7.y; var9 < var7.getY2(); ++var9) {
               var4 = IsoWorld.instance.CurrentCell.getGridSquare(var8, var9, var6);
               if (var4 != null && this.canSpawnAt(var8, var9, var6)) {
                  this.choices.add(var4);
               }
            }
         }
      }

      var1 = Math.min(var1, this.choices.size());
      if (!this.bestchoices.isEmpty()) {
         this.choices.addAll(this.bestchoices);
         this.choices.addAll(this.bestchoices);
      }

      for(var5 = 0; var5 < var1; ++var5) {
         if (!this.choices.isEmpty()) {
            var2.building.bAlarmed = false;
            var6 = Rand.Next(8);
            byte var10 = 4;
            IsoZombie var11 = this.createRealZombie(var6, var3 ? Rand.Next(var10) == 0 : false);
            if (var11 != null && var11.getSquare() != null) {
               ZombieSpawnRecorder.instance.record(var11, "addIndoorZombies");
               var11.bIndoorZombie = true;
               var11.setX((float)((int)var11.getX()) + (float)Rand.Next(2, 8) / 10.0F);
               var11.setY((float)((int)var11.getY()) + (float)Rand.Next(2, 8) / 10.0F);
               this.choices.remove(var11.getSquare());
               this.choices.remove(var11.getSquare());
               this.choices.remove(var11.getSquare());
            }
         } else {
            System.out.println("No choices for zombie.");
         }
      }

      this.bestchoices.clear();
      this.choices.clear();
   }

   public void addIndoorZombiesToChunk(IsoChunk var1, IsoRoom var2, int var3, ArrayList var4) {
      if (var3 > 0) {
         float var5 = var2.getRoomDef().getAreaOverlapping(var1);
         int var6 = (int)Math.ceil((double)((float)var3 * var5));
         if (var6 > 0) {
            this.choices.clear();
            int var7 = var2.def.level;

            int var8;
            for(var8 = 0; var8 < var2.rects.size(); ++var8) {
               RoomDef.RoomRect var9 = (RoomDef.RoomRect)var2.rects.get(var8);
               int var10 = Math.max(var1.wx * 10, var9.x);
               int var11 = Math.max(var1.wy * 10, var9.y);
               int var12 = Math.min((var1.wx + 1) * 10, var9.x + var9.w);
               int var13 = Math.min((var1.wy + 1) * 10, var9.y + var9.h);

               for(int var14 = var10; var14 < var12; ++var14) {
                  for(int var15 = var11; var15 < var13; ++var15) {
                     IsoGridSquare var16 = var1.getGridSquare(var14 - var1.wx * 10, var15 - var1.wy * 10, var7);
                     if (var16 != null && this.canSpawnAt(var14, var15, var7)) {
                        this.choices.add(var16);
                     }
                  }
               }
            }

            if (!this.choices.isEmpty()) {
               var2.def.building.bAlarmed = false;
               var6 = Math.min(var6, this.choices.size());

               for(var8 = 0; var8 < var6; ++var8) {
                  IsoZombie var17 = this.createRealZombie(Rand.Next(8), false);
                  if (var17 != null && var17.getSquare() != null) {
                     if (!GameServer.bServer) {
                        var17.bDressInRandomOutfit = true;
                     }

                     var17.setX((float)((int)var17.getX()) + (float)Rand.Next(2, 8) / 10.0F);
                     var17.setY((float)((int)var17.getY()) + (float)Rand.Next(2, 8) / 10.0F);
                     this.choices.remove(var17.getSquare());
                     var4.add(var17);
                  }
               }

               this.choices.clear();
            }
         }
      }
   }

   public void addIndoorZombiesToChunk(IsoChunk var1, IsoRoom var2) {
      if (var2.def.spawnCount == -1) {
         var2.def.spawnCount = this.getZombieCountForRoom(var2);
      }

      this.m_tempZombies.clear();
      this.addIndoorZombiesToChunk(var1, var2, var2.def.spawnCount, this.m_tempZombies);
      ZombieSpawnRecorder.instance.record(this.m_tempZombies, "addIndoorZombiesToChunk");
   }

   public void addDeadZombiesToMap(int var1, RoomDef var2) {
      boolean var3 = false;
      this.choices.clear();
      this.bestchoices.clear();
      IsoGridSquare var4 = null;

      int var5;
      int var6;
      for(var5 = 0; var5 < var2.rects.size(); ++var5) {
         var6 = var2.level;
         RoomDef.RoomRect var7 = (RoomDef.RoomRect)var2.rects.get(var5);

         for(int var8 = var7.x; var8 < var7.getX2(); ++var8) {
            for(int var9 = var7.y; var9 < var7.getY2(); ++var9) {
               var4 = IsoWorld.instance.CurrentCell.getGridSquare(var8, var9, var6);
               if (var4 != null && var4.isFree(false)) {
                  this.choices.add(var4);
                  if (!GameServer.bServer) {
                     boolean var10 = false;

                     for(int var11 = 0; var11 < IsoPlayer.numPlayers; ++var11) {
                        if (IsoPlayer.players[var11] != null && var4.isSeen(var11)) {
                           var10 = true;
                        }
                     }

                     if (!var10) {
                        this.bestchoices.add(var4);
                     }
                  }
               }
            }
         }
      }

      var1 = Math.min(var1, this.choices.size());
      if (!this.bestchoices.isEmpty()) {
         this.choices.addAll(this.bestchoices);
         this.choices.addAll(this.bestchoices);
      }

      for(var5 = 0; var5 < var1; ++var5) {
         if (!this.choices.isEmpty()) {
            var6 = Rand.Next(8);
            this.createRealZombie(var6, true);
         }
      }

      this.bestchoices.clear();
      this.choices.clear();
   }

   public void RemoveZombie(IsoZombie var1) {
      if (var1.isReanimatedPlayer()) {
         if (var1.vocalEvent != 0L) {
            var1.getEmitter().stopSound(var1.vocalEvent);
            var1.vocalEvent = 0L;
         }

         ReanimatedPlayers.instance.removeReanimatedPlayerFromWorld(var1);
      } else {
         if (var1.isDead()) {
            if (!this.RecentlyRemoved.contains(var1)) {
               var1.removedFromWorldMS = System.currentTimeMillis();
               this.RecentlyRemoved.add(var1);
            }
         } else if (!this.ReusedThisFrame.contains(var1)) {
            this.ReusedThisFrame.add(var1);
         }

      }
   }

   public void createHordeFromTo(float var1, float var2, float var3, float var4, int var5) {
      ZombiePopulationManager.instance.createHordeFromTo((int)var1, (int)var2, (int)var3, (int)var4, var5);
   }

   public IsoZombie createRealZombie(float var1, float var2, float var3) {
      this.choices.clear();
      this.choices.add(IsoWorld.instance.CurrentCell.getGridSquare((double)var1, (double)var2, (double)var3));
      if (!this.choices.isEmpty()) {
         int var4 = Rand.Next(8);
         return this.createRealZombie(var4, true);
      } else {
         return null;
      }
   }

   public IsoZombie createRealZombieNow(float var1, float var2, float var3) {
      this.choices.clear();
      IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare((double)var1, (double)var2, (double)var3);
      if (var4 == null) {
         return null;
      } else {
         this.choices.add(var4);
         if (!this.choices.isEmpty()) {
            int var5 = Rand.Next(8);
            return this.createRealZombie(var5, false);
         } else {
            return null;
         }
      }
   }

   private int getZombieCountForRoom(IsoRoom var1) {
      if (IsoWorld.getZombiesDisabled()) {
         return 0;
      } else if (GameClient.bClient) {
         return 0;
      } else if (Core.bLastStand) {
         return 0;
      } else {
         int var2 = 7;
         if (SandboxOptions.instance.Zombies.getValue() == 1) {
            var2 = 3;
         } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
            var2 = 4;
         } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
            var2 = 6;
         } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
            var2 = 15;
         }

         float var3 = 0.0F;
         IsoMetaChunk var4 = IsoWorld.instance.getMetaChunk(var1.def.x / 10, var1.def.y / 10);
         if (var4 != null) {
            var3 = var4.getLootZombieIntensity();
            if (var3 > 4.0F) {
               var2 = (int)((float)var2 - (var3 / 2.0F - 2.0F));
            }
         }

         if (var1.def.getArea() > 100) {
            var2 -= 2;
         }

         var2 = Math.max(2, var2);
         int var5;
         if (var1.getBuilding() != null) {
            var5 = var1.def.getArea();
            if (var1.getBuilding().getRoomsNumber() > 100 && var5 >= 20) {
               int var6 = var1.getBuilding().getRoomsNumber() - 95;
               if (var6 > 20) {
                  var6 = 20;
               }

               if (SandboxOptions.instance.Zombies.getValue() == 1) {
                  var6 += 10;
               } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
                  var6 += 7;
               } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
                  var6 += 5;
               } else if (SandboxOptions.instance.Zombies.getValue() == 4) {
                  var6 -= 10;
               }

               if (var5 < 30) {
                  var6 -= 6;
               }

               if (var5 < 50) {
                  var6 -= 10;
               }

               if (var5 < 70) {
                  var6 -= 13;
               }

               return Rand.Next(var6, var6 + 10);
            }
         }

         if (Rand.Next(var2) == 0) {
            byte var7 = 1;
            var5 = (int)((float)var7 + (var3 / 2.0F - 2.0F));
            if (var1.def.getArea() < 30) {
               var5 -= 4;
            }

            if (var1.def.getArea() > 85) {
               var5 += 2;
            }

            if (var1.getBuilding().getRoomsNumber() < 7) {
               var5 -= 2;
            }

            if (SandboxOptions.instance.Zombies.getValue() == 1) {
               var5 += 3;
            } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
               var5 += 2;
            } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
               ++var5;
            } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
               var5 -= 2;
            }

            var5 = Math.max(0, var5);
            var5 = Math.min(7, var5);
            return Rand.Next(var5, var5 + 2);
         } else {
            return 0;
         }
      }
   }

   public void roomSpotted(IsoRoom var1) {
      if (!GameClient.bClient) {
         var1.def.forEachChunk((var0, var1x) -> {
            var1x.addSpawnedRoom(var0.ID);
         });
         if (var1.def.spawnCount == -1) {
            var1.def.spawnCount = this.getZombieCountForRoom(var1);
         }

         if (var1.def.spawnCount > 0) {
            if (var1.getBuilding().getDef().isFullyStreamedIn()) {
               ArrayList var2 = this.addZombiesToMap(var1.def.spawnCount, var1.def, false);
               ZombieSpawnRecorder.instance.record(var2, "roomSpotted");
            } else {
               this.m_tempZombies.clear();
               var1.def.forEachChunk((var2x, var3) -> {
                  this.addIndoorZombiesToChunk(var3, var1, var1.def.spawnCount, this.m_tempZombies);
               });
               ZombieSpawnRecorder.instance.record(this.m_tempZombies, "roomSpotted");
            }

         }
      }
   }

   private boolean isBlockedInAllDirections(int var1, int var2, int var3) {
      IsoGridSquare var4 = GameServer.bServer ? ServerMap.instance.getGridSquare(var1, var2, var3) : IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         boolean var5 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)0, (int)1) && var4.nav[IsoDirections.N.index()] != null;
         boolean var6 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)2, (int)1) && var4.nav[IsoDirections.S.index()] != null;
         boolean var7 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)0, (int)1, (int)1) && var4.nav[IsoDirections.W.index()] != null;
         boolean var8 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)2, (int)1, (int)1) && var4.nav[IsoDirections.E.index()] != null;
         return var5 && var6 && var7 && var8;
      }
   }

   private boolean canPathOnlyN(int var1, int var2, int var3) {
      IsoGridSquare var4 = GameServer.bServer ? ServerMap.instance.getGridSquare(var1, var2, var3) : IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         boolean var5 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)0, (int)1) && var4.nav[IsoDirections.N.index()] != null;
         boolean var6 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)2, (int)1) && var4.nav[IsoDirections.S.index()] != null;
         boolean var7 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)0, (int)1, (int)1) && var4.nav[IsoDirections.W.index()] != null;
         boolean var8 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)2, (int)1, (int)1) && var4.nav[IsoDirections.E.index()] != null;
         return !var5 && var6 && var7 && var8;
      }
   }

   private boolean canPathOnlyS(int var1, int var2, int var3) {
      IsoGridSquare var4 = GameServer.bServer ? ServerMap.instance.getGridSquare(var1, var2, var3) : IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         boolean var5 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)0, (int)1) && var4.nav[IsoDirections.N.index()] != null;
         boolean var6 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)2, (int)1) && var4.nav[IsoDirections.S.index()] != null;
         boolean var7 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)0, (int)1, (int)1) && var4.nav[IsoDirections.W.index()] != null;
         boolean var8 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)2, (int)1, (int)1) && var4.nav[IsoDirections.E.index()] != null;
         return var5 && !var6 && var7 && var8;
      }
   }

   private boolean canPathOnlyW(int var1, int var2, int var3) {
      IsoGridSquare var4 = GameServer.bServer ? ServerMap.instance.getGridSquare(var1, var2, var3) : IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         boolean var5 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)0, (int)1) && var4.nav[IsoDirections.N.index()] != null;
         boolean var6 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)2, (int)1) && var4.nav[IsoDirections.S.index()] != null;
         boolean var7 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)0, (int)1, (int)1) && var4.nav[IsoDirections.W.index()] != null;
         boolean var8 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)2, (int)1, (int)1) && var4.nav[IsoDirections.E.index()] != null;
         return var5 && var6 && !var7 && var8;
      }
   }

   private boolean canPathOnlyE(int var1, int var2, int var3) {
      IsoGridSquare var4 = GameServer.bServer ? ServerMap.instance.getGridSquare(var1, var2, var3) : IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         boolean var5 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)0, (int)1) && var4.nav[IsoDirections.N.index()] != null;
         boolean var6 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)1, (int)2, (int)1) && var4.nav[IsoDirections.S.index()] != null;
         boolean var7 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)0, (int)1, (int)1) && var4.nav[IsoDirections.W.index()] != null;
         boolean var8 = IsoGridSquare.getMatrixBit(var4.pathMatrix, (int)2, (int)1, (int)1) && var4.nav[IsoDirections.E.index()] != null;
         return var5 && var6 && var7 && !var8;
      }
   }

   public boolean canSpawnAt(int var1, int var2, int var3) {
      IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var4 != null && var4.isFree(false)) {
         if (this.isBlockedInAllDirections(var1, var2, var3)) {
            return false;
         } else if (this.canPathOnlyE(var1, var2, var3) && this.canPathOnlyW(var1 + 1, var2, var3)) {
            return false;
         } else if (this.canPathOnlyE(var1 - 1, var2, var3) && this.canPathOnlyW(var1, var2, var3)) {
            return false;
         } else if (this.canPathOnlyS(var1, var2, var3) && this.canPathOnlyN(var1, var2 + 1, var3)) {
            return false;
         } else {
            return !this.canPathOnlyS(var1, var2 - 1, var3) || !this.canPathOnlyN(var1, var2, var3);
         }
      } else {
         return false;
      }
   }

   public int reusableZombiesSize() {
      return this.ReusableZombies.size();
   }
}
