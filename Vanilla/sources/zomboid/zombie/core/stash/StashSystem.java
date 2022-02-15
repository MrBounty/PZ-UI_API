package zombie.core.stash;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.VirtualZombieManager;
import zombie.ZombieSpawnRecorder;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.MapItem;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTrap;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.ui.UIFont;
import zombie.util.Type;
import zombie.worldMap.symbols.WorldMapBaseSymbol;

public final class StashSystem {
   public static ArrayList allStashes;
   public static ArrayList possibleStashes;
   public static ArrayList buildingsToDo;
   private static final ArrayList possibleTrap = new ArrayList();
   private static ArrayList alreadyReadMap = new ArrayList();

   public static void init() {
      if (possibleStashes == null) {
         initAllStashes();
         buildingsToDo = new ArrayList();
         possibleTrap.add("Base.FlameTrapSensorV1");
         possibleTrap.add("Base.SmokeBombSensorV1");
         possibleTrap.add("Base.NoiseTrapSensorV1");
         possibleTrap.add("Base.NoiseTrapSensorV2");
         possibleTrap.add("Base.AerosolbombSensorV1");
      }

   }

   public static void initAllStashes() {
      allStashes = new ArrayList();
      possibleStashes = new ArrayList();
      KahluaTable var0 = (KahluaTable)LuaManager.env.rawget("StashDescriptions");
      KahluaTableIterator var1 = var0.iterator();

      while(var1.advance()) {
         KahluaTableImpl var3 = (KahluaTableImpl)var1.getValue();
         Stash var2 = new Stash(var3.rawgetStr("name"));
         var2.load(var3);
         allStashes.add(var2);
      }

   }

   public static void checkStashItem(InventoryItem var0) {
      if (!GameClient.bClient && !possibleStashes.isEmpty()) {
         int var1 = 60;
         if (var0.getStashChance() > 0) {
            var1 = var0.getStashChance();
         }

         switch(SandboxOptions.instance.AnnotatedMapChance.getValue()) {
         case 1:
            return;
         case 2:
            var1 += 15;
            break;
         case 3:
            var1 += 10;
         case 4:
         default:
            break;
         case 5:
            var1 -= 10;
            break;
         case 6:
            var1 -= 20;
         }

         if (Rand.Next(100) <= 100 - var1) {
            ArrayList var2 = new ArrayList();

            for(int var3 = 0; var3 < allStashes.size(); ++var3) {
               Stash var4 = (Stash)allStashes.get(var3);
               if (var4.item.equals(var0.getFullType()) && checkSpecificSpawnProperties(var4, var0)) {
                  boolean var5 = false;

                  for(int var6 = 0; var6 < possibleStashes.size(); ++var6) {
                     StashBuilding var7 = (StashBuilding)possibleStashes.get(var6);
                     if (var7.stashName.equals(var4.name)) {
                        var5 = true;
                        break;
                     }
                  }

                  if (var5) {
                     var2.add(var4);
                  }
               }
            }

            if (!var2.isEmpty()) {
               Stash var8 = (Stash)var2.get(Rand.Next(0, var2.size()));
               doStashItem(var8, var0);
            }
         }
      }
   }

   public static void doStashItem(Stash var0, InventoryItem var1) {
      if (var0.customName != null) {
         var1.setName(var0.customName);
      }

      if ("Map".equals(var0.type)) {
         MapItem var2 = (MapItem)Type.tryCastTo(var1, MapItem.class);
         if (var2 == null) {
            throw new IllegalArgumentException(var1 + " is not a MapItem");
         }

         if (var0.annotations != null) {
            for(int var3 = 0; var3 < var0.annotations.size(); ++var3) {
               StashAnnotation var4 = (StashAnnotation)var0.annotations.get(var3);
               if (var4.symbol != null) {
                  var2.getSymbols().addTexture(var4.symbol, var4.x, var4.y, 0.5F, 0.5F, WorldMapBaseSymbol.DEFAULT_SCALE, var4.r, var4.g, var4.b, 1.0F);
               } else if (var4.text != null && !GameServer.bServer) {
                  var2.getSymbols().addUntranslatedText(var4.text, UIFont.Handwritten, var4.x, var4.y, var4.r, var4.g, var4.b, 1.0F);
               }
            }
         }

         removeFromPossibleStash(var0);
         var1.setStashMap(var0.name);
      }

   }

   public static void prepareBuildingStash(String var0) {
      if (var0 != null) {
         Stash var1 = getStash(var0);
         if (var1 != null && !alreadyReadMap.contains(var0)) {
            alreadyReadMap.add(var0);
            buildingsToDo.add(new StashBuilding(var1.name, var1.buildingX, var1.buildingY));
            RoomDef var2 = IsoWorld.instance.getMetaGrid().getRoomAt(var1.buildingX, var1.buildingY, 0);
            if (var2 != null && var2.getBuilding() != null && var2.getBuilding().isFullyStreamedIn()) {
               doBuildingStash(var2.getBuilding());
            }
         }

      }
   }

   private static boolean checkSpecificSpawnProperties(Stash var0, InventoryItem var1) {
      if (!var0.spawnOnlyOnZed || var1.getContainer() != null && var1.getContainer().getParent() instanceof IsoDeadBody) {
         return (var0.minDayToSpawn <= -1 || GameTime.instance.getDaysSurvived() >= var0.minDayToSpawn) && (var0.maxDayToSpawn <= -1 || GameTime.instance.getDaysSurvived() <= var0.maxDayToSpawn);
      } else {
         return false;
      }
   }

   private static void removeFromPossibleStash(Stash var0) {
      for(int var1 = 0; var1 < possibleStashes.size(); ++var1) {
         StashBuilding var2 = (StashBuilding)possibleStashes.get(var1);
         if (var2.buildingX == var0.buildingX && var2.buildingY == var0.buildingY) {
            possibleStashes.remove(var1);
            --var1;
         }
      }

   }

   public static void doBuildingStash(BuildingDef var0) {
      if (buildingsToDo == null) {
         init();
      }

      for(int var1 = 0; var1 < buildingsToDo.size(); ++var1) {
         StashBuilding var2 = (StashBuilding)buildingsToDo.get(var1);
         if (var2.buildingX > var0.x && var2.buildingX < var0.x2 && var2.buildingY > var0.y && var2.buildingY < var0.y2) {
            if (var0.hasBeenVisited) {
               buildingsToDo.remove(var1);
               --var1;
            } else {
               Stash var3 = getStash(var2.stashName);
               if (var3 != null) {
                  ItemPickerJava.ItemPickerRoom var4 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(var3.spawnTable);
                  var0.setAllExplored(true);
                  doSpecificBuildingProperties(var3, var0);

                  for(int var5 = var0.x - 1; var5 < var0.x2 + 1; ++var5) {
                     for(int var6 = var0.y - 1; var6 < var0.y2 + 1; ++var6) {
                        for(int var7 = 0; var7 < 8; ++var7) {
                           IsoGridSquare var8 = IsoWorld.instance.CurrentCell.getGridSquare(var5, var6, var7);
                           if (var8 != null) {
                              for(int var9 = 0; var9 < var8.getObjects().size(); ++var9) {
                                 IsoObject var10 = (IsoObject)var8.getObjects().get(var9);
                                 if (var10.getContainer() != null && var8.getRoom() != null && var8.getRoom().getBuilding().getDef() == var0 && var8.getRoom().getName() != null && var4.Containers.containsKey(var10.getContainer().getType())) {
                                    ItemPickerJava.ItemPickerRoom var11 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(var8.getRoom().getName());
                                    boolean var12 = false;
                                    if (var11 == null || !var11.Containers.containsKey(var10.getContainer().getType())) {
                                       var10.getContainer().clear();
                                       var12 = true;
                                    }

                                    ItemPickerJava.fillContainerType(var4, var10.getContainer(), "", (IsoGameCharacter)null);
                                    ItemPickerJava.updateOverlaySprite(var10);
                                    if (var12) {
                                       var10.getContainer().setExplored(true);
                                    }
                                 }

                                 BarricadeAble var13 = (BarricadeAble)Type.tryCastTo(var10, BarricadeAble.class);
                                 if (var3.barricades > -1 && var13 != null && var13.isBarricadeAllowed() && Rand.Next(100) < var3.barricades) {
                                    if (var10 instanceof IsoDoor) {
                                       ((IsoDoor)var10).addRandomBarricades();
                                    } else if (var10 instanceof IsoWindow) {
                                       ((IsoWindow)var10).addRandomBarricades();
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  buildingsToDo.remove(var1);
                  --var1;
               }
            }
         }
      }

   }

   private static void doSpecificBuildingProperties(Stash var0, BuildingDef var1) {
      int var6;
      if (var0.containers != null) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var0.containers.size(); ++var3) {
            StashContainer var4 = (StashContainer)var0.containers.get(var3);
            IsoGridSquare var5 = null;
            if (!"all".equals(var4.room)) {
               for(var6 = 0; var6 < var1.rooms.size(); ++var6) {
                  RoomDef var7 = (RoomDef)var1.rooms.get(var6);
                  if (var4.room.equals(var7.name)) {
                     var2.add(var7);
                  }
               }
            } else if (var4.contX > -1 && var4.contY > -1 && var4.contZ > -1) {
               var5 = IsoWorld.instance.getCell().getGridSquare(var4.contX, var4.contY, var4.contZ);
            } else {
               var5 = var1.getFreeSquareInRoom();
            }

            if (!var2.isEmpty()) {
               RoomDef var15 = (RoomDef)var2.get(Rand.Next(0, var2.size()));
               var5 = var15.getFreeSquare();
            }

            if (var5 != null) {
               if (var4.containerItem != null && !var4.containerItem.isEmpty()) {
                  ItemPickerJava.ItemPickerRoom var18 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(var0.spawnTable);
                  if (var18 == null) {
                     DebugLog.log("Container distribution " + var0.spawnTable + " not found");
                     return;
                  }

                  InventoryItem var19 = InventoryItemFactory.CreateItem(var4.containerItem);
                  if (var19 == null) {
                     DebugLog.General.error("Item " + var4.containerItem + " Doesn't exist.");
                     return;
                  }

                  ItemPickerJava.ItemPickerContainer var8 = (ItemPickerJava.ItemPickerContainer)var18.Containers.get(var19.getType());
                  ItemPickerJava.rollContainerItem((InventoryContainer)var19, (IsoGameCharacter)null, var8);
                  var5.AddWorldInventoryItem(var19, 0.0F, 0.0F, 0.0F);
               } else {
                  IsoThumpable var17 = new IsoThumpable(var5.getCell(), var5, var4.containerSprite, false, (KahluaTable)null);
                  var17.setIsThumpable(false);
                  var17.container = new ItemContainer(var4.containerType, var5, var17);
                  var5.AddSpecialObject(var17);
                  var5.RecalcAllWithNeighbours(true);
               }
            } else {
               DebugLog.log("No free room was found to spawn special container for stash " + var0.name);
            }
         }
      }

      int var9;
      if (var0.minTrapToSpawn > -1) {
         for(var9 = var0.minTrapToSpawn; var9 < var0.maxTrapToSpawn; ++var9) {
            IsoGridSquare var10 = var1.getFreeSquareInRoom();
            if (var10 != null) {
               HandWeapon var12 = (HandWeapon)InventoryItemFactory.CreateItem((String)possibleTrap.get(Rand.Next(0, possibleTrap.size())));
               if (GameServer.bServer) {
                  GameServer.AddExplosiveTrap(var12, var10, var12.getSensorRange() > 0);
               } else {
                  IsoTrap var14 = new IsoTrap(var12, var10.getCell(), var10);
                  var10.AddTileObject(var14);
               }
            }
         }
      }

      if (var0.zombies > -1) {
         for(var9 = 0; var9 < var1.rooms.size(); ++var9) {
            RoomDef var11 = (RoomDef)var1.rooms.get(var9);
            if (IsoWorld.getZombiesEnabled()) {
               byte var13 = 1;
               int var16 = 0;

               for(var6 = 0; var6 < var11.area; ++var6) {
                  if (Rand.Next(100) < var0.zombies) {
                     ++var16;
                  }
               }

               if (SandboxOptions.instance.Zombies.getValue() == 1) {
                  var16 += 4;
               } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
                  var16 += 3;
               } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
                  var16 += 2;
               } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
                  var16 -= 4;
               }

               if (var16 > var11.area / 2) {
                  var16 = var11.area / 2;
               }

               if (var16 < var13) {
                  var16 = var13;
               }

               ArrayList var20 = VirtualZombieManager.instance.addZombiesToMap(var16, var11, false);
               ZombieSpawnRecorder.instance.record(var20, "StashSystem");
            }
         }
      }

   }

   public static Stash getStash(String var0) {
      for(int var1 = 0; var1 < allStashes.size(); ++var1) {
         Stash var2 = (Stash)allStashes.get(var1);
         if (var2.name.equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static void visitedBuilding(BuildingDef var0) {
      if (!GameClient.bClient) {
         for(int var1 = 0; var1 < possibleStashes.size(); ++var1) {
            StashBuilding var2 = (StashBuilding)possibleStashes.get(var1);
            if (var2.buildingX > var0.x && var2.buildingX < var0.x2 && var2.buildingY > var0.y && var2.buildingY < var0.y2) {
               possibleStashes.remove(var1);
               --var1;
            }
         }

      }
   }

   public static void load(ByteBuffer var0, int var1) {
      init();
      alreadyReadMap = new ArrayList();
      possibleStashes = new ArrayList();
      buildingsToDo = new ArrayList();
      int var2 = var0.getInt();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         possibleStashes.add(new StashBuilding(GameWindow.ReadString(var0), var0.getInt(), var0.getInt()));
      }

      var3 = var0.getInt();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         buildingsToDo.add(new StashBuilding(GameWindow.ReadString(var0), var0.getInt(), var0.getInt()));
      }

      if (var1 >= 109) {
         var4 = var0.getInt();

         for(int var5 = 0; var5 < var4; ++var5) {
            alreadyReadMap.add(GameWindow.ReadString(var0));
         }
      }

   }

   public static void save(ByteBuffer var0) {
      if (allStashes != null) {
         var0.putInt(possibleStashes.size());

         int var1;
         StashBuilding var2;
         for(var1 = 0; var1 < possibleStashes.size(); ++var1) {
            var2 = (StashBuilding)possibleStashes.get(var1);
            GameWindow.WriteString(var0, var2.stashName);
            var0.putInt(var2.buildingX);
            var0.putInt(var2.buildingY);
         }

         var0.putInt(buildingsToDo.size());

         for(var1 = 0; var1 < buildingsToDo.size(); ++var1) {
            var2 = (StashBuilding)buildingsToDo.get(var1);
            GameWindow.WriteString(var0, var2.stashName);
            var0.putInt(var2.buildingX);
            var0.putInt(var2.buildingY);
         }

         var0.putInt(alreadyReadMap.size());

         for(var1 = 0; var1 < alreadyReadMap.size(); ++var1) {
            GameWindow.WriteString(var0, (String)alreadyReadMap.get(var1));
         }

      }
   }

   public static ArrayList getPossibleStashes() {
      return possibleStashes;
   }

   public static void reinit() {
      possibleStashes = null;
      alreadyReadMap = new ArrayList();
      init();
   }

   public static void Reset() {
      allStashes = null;
      possibleStashes = null;
      buildingsToDo = null;
      possibleTrap.clear();
      alreadyReadMap.clear();
   }
}
