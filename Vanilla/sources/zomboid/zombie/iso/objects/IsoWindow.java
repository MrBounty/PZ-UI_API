package zombie.iso.objects;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.AmbientStreamManager;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.WorldSoundManager;
import zombie.Lua.LuaEventManager;
import zombie.ai.states.ThumpState;
import zombie.audio.parameters.ParameterMeleeHitSurface;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoLivingCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.SafeHouse;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerOptions;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PolygonalMap2;

public class IsoWindow extends IsoObject implements BarricadeAble, Thumpable {
   public int Health = 75;
   public int MaxHealth = 75;
   public IsoWindow.WindowType type;
   IsoSprite closedSprite;
   IsoSprite smashedSprite;
   public boolean north;
   public boolean Locked;
   public boolean PermaLocked;
   public boolean open;
   IsoSprite openSprite;
   private boolean destroyed;
   private boolean glassRemoved;
   private IsoSprite glassRemovedSprite;
   public int OldNumPlanks;

   public IsoCurtain HasCurtains() {
      IsoGridSquare var1 = this.getOppositeSquare();
      if (var1 != null) {
         IsoCurtain var2 = var1.getCurtain(this.getNorth() ? IsoObjectType.curtainS : IsoObjectType.curtainE);
         if (var2 != null) {
            return var2;
         }
      }

      return this.getSquare().getCurtain(this.getNorth() ? IsoObjectType.curtainN : IsoObjectType.curtainW);
   }

   public IsoGridSquare getIndoorSquare() {
      if (this.square.getRoom() != null) {
         return this.square;
      } else {
         IsoGridSquare var1;
         if (this.north) {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX(), this.square.getY() - 1, this.square.getZ());
            if (var1 != null && var1.getRoom() != null) {
               return var1;
            }
         } else {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX() - 1, this.square.getY(), this.square.getZ());
            if (var1 != null && var1.getRoom() != null) {
               return var1;
            }
         }

         return null;
      }
   }

   public IsoGridSquare getAddSheetSquare(IsoGameCharacter var1) {
      if (var1 != null && var1.getCurrentSquare() != null) {
         IsoGridSquare var2 = var1.getCurrentSquare();
         IsoGridSquare var3 = this.getSquare();
         if (this.north) {
            return var2.getY() < var3.getY() ? this.getCell().getGridSquare(var3.x, var3.y - 1, var3.z) : var3;
         } else {
            return var2.getX() < var3.getX() ? this.getCell().getGridSquare(var3.x - 1, var3.y, var3.z) : var3;
         }
      } else {
         return null;
      }
   }

   public void AttackObject(IsoGameCharacter var1) {
      super.AttackObject(var1);
      IsoObject var2 = this.square.getWall(this.north);
      if (var2 != null) {
         var2.AttackObject(var1);
      }

   }

   public IsoGridSquare getInsideSquare() {
      if (this.square == null) {
         return null;
      } else {
         return this.north ? this.getCell().getGridSquare(this.square.getX(), this.square.getY() - 1, this.square.getZ()) : this.getCell().getGridSquare(this.square.getX() - 1, this.square.getY(), this.square.getZ());
      }
   }

   public IsoGridSquare getOppositeSquare() {
      return this.getInsideSquare();
   }

   public IsoWindow(IsoCell var1) {
      super(var1);
      this.type = IsoWindow.WindowType.SinglePane;
      this.north = false;
      this.Locked = false;
      this.PermaLocked = false;
      this.open = false;
      this.destroyed = false;
      this.glassRemoved = false;
   }

   public String getObjectName() {
      return "Window";
   }

   public void WeaponHit(IsoGameCharacter var1, HandWeapon var2) {
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (GameClient.bClient) {
         if (var3 != null) {
            GameClient.instance.sendWeaponHit(var3, var2, this);
         }

      } else {
         Thumpable var4 = this.getThumpableFor(var1);
         if (var4 != null) {
            if (var4 instanceof IsoBarricade) {
               ((IsoBarricade)var4).WeaponHit(var1, var2);
            } else if (var2 == ((IsoLivingCharacter)var1).bareHands) {
               if (var3 != null) {
                  var3.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Glass);
                  var3.getEmitter().playSound(var2.getDoorHitSound(), this);
               }

            } else {
               if (var2 != null) {
                  this.Damage((float)(var2.getDoorDamage() * 5), var1);
               } else {
                  this.Damage(100.0F, var1);
               }

               this.DirtySlice();
               if (var2 != null && var2.getDoorHitSound() != null) {
                  if (var3 != null) {
                     var3.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Glass);
                  }

                  var1.getEmitter().playSound(var2.getDoorHitSound(), this);
                  if (GameServer.bServer) {
                     GameServer.PlayWorldSoundServer(var2.getDoorHitSound(), false, this.getSquare(), 1.0F, 20.0F, 2.0F, false);
                  }
               }

               WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, false, 0.0F, 15.0F);
               if (!this.isDestroyed() && this.Health <= 0) {
                  this.smashWindow();
                  this.addBrokenGlass(var1);
               }

            }
         }
      }
   }

   public void smashWindow(boolean var1, boolean var2) {
      if (!this.destroyed) {
         if (GameClient.bClient && !var1) {
            GameClient.instance.smashWindow(this, 1);
         }

         if (!var1) {
            if (GameServer.bServer) {
               GameServer.PlayWorldSoundServer("SmashWindow", false, this.square, 0.2F, 20.0F, 1.1F, true);
            } else {
               SoundManager.instance.PlayWorldSound("SmashWindow", this.square, 0.2F, 20.0F, 1.0F, true);
            }

            WorldSoundManager.instance.addSound((Object)null, this.square.getX(), this.square.getY(), this.square.getZ(), 10, 20, true, 4.0F, 15.0F);
         }

         this.destroyed = true;
         this.sprite = this.smashedSprite;
         if (var2) {
            this.handleAlarm();
         }

         if (GameServer.bServer && !var1) {
            GameServer.smashWindow(this, 1);
         }

         this.square.InvalidateSpecialObjectPaths();
      }
   }

   public void smashWindow(boolean var1) {
      this.smashWindow(var1, true);
   }

   public void smashWindow() {
      this.smashWindow(false, true);
   }

   public void addBrokenGlass(IsoMovingObject var1) {
      if (var1 != null) {
         if (this.getSquare() != null) {
            if (this.getNorth()) {
               this.addBrokenGlass(var1.getY() >= (float)this.getSquare().getY());
            } else {
               this.addBrokenGlass(var1.getX() >= (float)this.getSquare().getX());
            }

         }
      }
   }

   public void addBrokenGlass(boolean var1) {
      IsoGridSquare var2 = var1 ? this.getOppositeSquare() : this.getSquare();
      if (var2 != null) {
         var2.addBrokenGlass();
      }

   }

   private void handleAlarm() {
      if (!GameClient.bClient) {
         IsoGridSquare var1 = this.getIndoorSquare();
         if (var1 != null) {
            IsoRoom var2 = var1.getRoom();
            RoomDef var3 = var2.def;
            if (var3.building.bAlarmed && !GameClient.bClient) {
               AmbientStreamManager.instance.doAlarm(var3);
            }

         }
      }
   }

   public IsoWindow(IsoCell var1, IsoGridSquare var2, IsoSprite var3, boolean var4) {
      this.type = IsoWindow.WindowType.SinglePane;
      this.north = false;
      this.Locked = false;
      this.PermaLocked = false;
      this.open = false;
      this.destroyed = false;
      this.glassRemoved = false;
      var3.getProperties().UnSet(IsoFlagType.cutN);
      var3.getProperties().UnSet(IsoFlagType.cutW);
      int var5 = 0;
      if (var3.getProperties().Is("OpenTileOffset")) {
         var5 = Integer.parseInt(var3.getProperties().Val("OpenTileOffset"));
      }

      int var6 = 0;
      this.PermaLocked = var3.getProperties().Is("WindowLocked");
      if (var3.getProperties().Is("SmashedTileOffset")) {
         var6 = Integer.parseInt(var3.getProperties().Val("SmashedTileOffset"));
      }

      this.closedSprite = var3;
      if (var4) {
         this.closedSprite.getProperties().Set(IsoFlagType.cutN);
         this.closedSprite.getProperties().Set(IsoFlagType.windowN);
      } else {
         this.closedSprite.getProperties().Set(IsoFlagType.cutW);
         this.closedSprite.getProperties().Set(IsoFlagType.windowW);
      }

      this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var3, var5);
      this.smashedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var3, var6);
      if (this.closedSprite.getProperties().Is("GlassRemovedOffset")) {
         int var7 = Integer.parseInt(this.closedSprite.getProperties().Val("GlassRemovedOffset"));
         this.glassRemovedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, this.closedSprite, var7);
      } else {
         this.glassRemovedSprite = this.smashedSprite;
      }

      if (this.smashedSprite != this.closedSprite && this.smashedSprite != null) {
         this.smashedSprite.AddProperties(this.closedSprite);
         this.smashedSprite.setType(this.closedSprite.getType());
      }

      if (this.openSprite != this.closedSprite && this.openSprite != null) {
         this.openSprite.AddProperties(this.closedSprite);
         this.openSprite.setType(this.closedSprite.getType());
      }

      if (this.glassRemovedSprite != this.closedSprite && this.glassRemovedSprite != null) {
         this.glassRemovedSprite.AddProperties(this.closedSprite);
         this.glassRemovedSprite.setType(this.closedSprite.getType());
      }

      this.sprite = this.closedSprite;
      IsoObject var9 = var2.getWall(var4);
      if (var9 != null) {
         var9.rerouteCollide = this;
      }

      this.square = var2;
      this.north = var4;
      switch(this.type) {
      case SinglePane:
         this.MaxHealth = this.Health = 50;
         break;
      case DoublePane:
         this.MaxHealth = this.Health = 150;
      }

      byte var8 = 69;
      if (SandboxOptions.instance.LockedHouses.getValue() == 1) {
         var8 = -1;
      } else if (SandboxOptions.instance.LockedHouses.getValue() == 2) {
         var8 = 5;
      } else if (SandboxOptions.instance.LockedHouses.getValue() == 3) {
         var8 = 10;
      } else if (SandboxOptions.instance.LockedHouses.getValue() == 4) {
         var8 = 50;
      } else if (SandboxOptions.instance.LockedHouses.getValue() == 5) {
         var8 = 60;
      } else if (SandboxOptions.instance.LockedHouses.getValue() == 6) {
         var8 = 70;
      }

      if (var8 > -1) {
         this.Locked = Rand.Next(100) < var8;
      }

   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public boolean IsOpen() {
      return this.open;
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      return false;
   }

   public boolean TestCollide(IsoMovingObject var1, IsoGridSquare var2, IsoGridSquare var3) {
      if (var2 == this.square) {
         if (this.north && var3.getY() < var2.getY()) {
            if (var1 != null) {
               var1.collideWith(this);
            }

            return true;
         }

         if (!this.north && var3.getX() < var2.getX()) {
            if (var1 != null) {
               var1.collideWith(this);
            }

            return true;
         }
      } else {
         if (this.north && var3.getY() > var2.getY()) {
            if (var1 != null) {
               var1.collideWith(this);
            }

            return true;
         }

         if (!this.north && var3.getX() > var2.getX()) {
            if (var1 != null) {
               var1.collideWith(this);
            }

            return true;
         }
      }

      return false;
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      if (var2.getZ() != var1.getZ()) {
         return IsoObject.VisionResult.NoEffect;
      } else {
         if (var1 == this.square) {
            if (this.north && var2.getY() < var1.getY()) {
               return IsoObject.VisionResult.Unblocked;
            }

            if (!this.north && var2.getX() < var1.getX()) {
               return IsoObject.VisionResult.Unblocked;
            }
         } else {
            if (this.north && var2.getY() > var1.getY()) {
               return IsoObject.VisionResult.Unblocked;
            }

            if (!this.north && var2.getX() > var1.getX()) {
               return IsoObject.VisionResult.Unblocked;
            }
         }

         return IsoObject.VisionResult.NoEffect;
      }
   }

   public void Thump(IsoMovingObject var1) {
      if (var1 instanceof IsoGameCharacter) {
         Thumpable var2 = this.getThumpableFor((IsoGameCharacter)var1);
         if (var2 == null) {
            return;
         }

         if (var2 != this) {
            var2.Thump(var1);
            return;
         }
      }

      if (var1 instanceof IsoZombie) {
         if (((IsoZombie)var1).cognition == 1 && !this.canClimbThrough((IsoZombie)var1) && !this.isInvincible() && (!this.Locked || var1.getCurrentSquare() != null && !var1.getCurrentSquare().Is(IsoFlagType.exterior))) {
            this.ToggleWindow((IsoGameCharacter)var1);
            if (this.canClimbThrough((IsoZombie)var1)) {
               return;
            }
         }

         int var3 = ThumpState.getFastForwardDamageMultiplier();
         this.DirtySlice();
         this.Damage((float)(((IsoZombie)var1).strength * var3), var1);
         WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, true, 4.0F, 15.0F);
      }

      if (!this.isDestroyed() && this.Health <= 0) {
         if (this.getSquare().getBuilding() != null) {
            this.getSquare().getBuilding().forceAwake();
         }

         if (GameServer.bServer) {
            GameServer.smashWindow(this, 1);
            GameServer.PlayWorldSoundServer("SmashWindow", false, var1.getCurrentSquare(), 0.2F, 20.0F, 1.1F, true);
         }

         ((IsoGameCharacter)var1).getEmitter().playSound("SmashWindow", this);
         WorldSoundManager.instance.addSound((Object)null, this.square.getX(), this.square.getY(), this.square.getZ(), 10, 20, true, 4.0F, 15.0F);
         var1.setThumpTarget((Thumpable)null);
         this.destroyed = true;
         this.sprite = this.smashedSprite;
         this.square.InvalidateSpecialObjectPaths();
         this.addBrokenGlass(var1);
         if (var1 instanceof IsoZombie && this.getThumpableFor((IsoZombie)var1) != null) {
            var1.setThumpTarget(this.getThumpableFor((IsoZombie)var1));
         }
      }

   }

   public Thumpable getThumpableFor(IsoGameCharacter var1) {
      IsoBarricade var2 = this.getBarricadeForCharacter(var1);
      if (var2 != null) {
         return var2;
      } else if (!this.isDestroyed() && !this.IsOpen()) {
         return this;
      } else {
         var2 = this.getBarricadeOppositeCharacter(var1);
         return var2 != null ? var2 : null;
      }
   }

   public float getThumpCondition() {
      return (float)PZMath.clamp(this.Health, 0, this.MaxHealth) / (float)this.MaxHealth;
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.open = var1.get() == 1;
      this.north = var1.get() == 1;
      int var4;
      if (var2 >= 87) {
         this.Health = var1.getInt();
      } else {
         var4 = var1.getInt();
         this.Health = var1.getInt();
         int var5 = var1.getInt();
         if (var2 >= 49) {
            short var6 = var1.getShort();
         } else {
            Math.max(var5, var4 * 1000);
         }

         this.OldNumPlanks = var4;
      }

      this.Locked = var1.get() == 1;
      this.PermaLocked = var1.get() == 1;
      this.destroyed = var1.get() == 1;
      if (var2 >= 64) {
         this.glassRemoved = var1.get() == 1;
         if (var1.get() == 1) {
            this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var1.get() == 1) {
            this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var1.get() == 1) {
            this.smashedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var1.get() == 1) {
            this.glassRemovedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }
      } else {
         if (var1.getInt() == 1) {
            this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var1.getInt() == 1) {
            this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var1.getInt() == 1) {
            this.smashedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (this.closedSprite != null) {
            if (this.destroyed && this.closedSprite.getProperties().Is("SmashedTileOffset")) {
               var4 = Integer.parseInt(this.closedSprite.getProperties().Val("SmashedTileOffset"));
               this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, this.closedSprite, -var4);
            }

            if (this.closedSprite.getProperties().Is("GlassRemovedOffset")) {
               var4 = Integer.parseInt(this.closedSprite.getProperties().Val("GlassRemovedOffset"));
               this.glassRemovedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, this.closedSprite, var4);
            }
         }

         if (this.glassRemovedSprite == null) {
            this.glassRemovedSprite = this.smashedSprite != null ? this.smashedSprite : this.closedSprite;
         }
      }

      this.MaxHealth = var1.getInt();
      if (this.closedSprite != null) {
         if (this.north) {
            this.closedSprite.getProperties().Set(IsoFlagType.cutN);
            this.closedSprite.getProperties().Set(IsoFlagType.windowN);
         } else {
            this.closedSprite.getProperties().Set(IsoFlagType.cutW);
            this.closedSprite.getProperties().Set(IsoFlagType.windowW);
         }

         if (this.smashedSprite != this.closedSprite && this.smashedSprite != null) {
            this.smashedSprite.AddProperties(this.closedSprite);
            this.smashedSprite.setType(this.closedSprite.getType());
         }

         if (this.openSprite != this.closedSprite && this.openSprite != null) {
            this.openSprite.AddProperties(this.closedSprite);
            this.openSprite.setType(this.closedSprite.getType());
         }

         if (this.glassRemovedSprite != this.closedSprite && this.glassRemovedSprite != null) {
            this.glassRemovedSprite.AddProperties(this.closedSprite);
            this.glassRemovedSprite.setType(this.closedSprite.getType());
         }
      }

      if (SystemDisabler.doObjectStateSyncEnable && GameClient.bClient) {
         GameClient.instance.objectSyncReq.putRequestLoad(this.square);
      }

   }

   public void addToWorld() {
      super.addToWorld();
      this.getCell().addToWindowList(this);
   }

   public void removeFromWorld() {
      super.removeFromWorld();
      this.getCell().removeFromWindowList(this);
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.open ? 1 : 0));
      var1.put((byte)(this.north ? 1 : 0));
      var1.putInt(this.Health);
      var1.put((byte)(this.Locked ? 1 : 0));
      var1.put((byte)(this.PermaLocked ? 1 : 0));
      var1.put((byte)(this.destroyed ? 1 : 0));
      var1.put((byte)(this.glassRemoved ? 1 : 0));
      if (this.openSprite != null) {
         var1.put((byte)1);
         var1.putInt(this.openSprite.ID);
      } else {
         var1.put((byte)0);
      }

      if (this.closedSprite != null) {
         var1.put((byte)1);
         var1.putInt(this.closedSprite.ID);
      } else {
         var1.put((byte)0);
      }

      if (this.smashedSprite != null) {
         var1.put((byte)1);
         var1.putInt(this.smashedSprite.ID);
      } else {
         var1.put((byte)0);
      }

      if (this.glassRemovedSprite != null) {
         var1.put((byte)1);
         var1.putInt(this.glassRemovedSprite.ID);
      } else {
         var1.put((byte)0);
      }

      var1.putInt(this.MaxHealth);
   }

   public void saveState(ByteBuffer var1) throws IOException {
      var1.put((byte)(this.Locked ? 1 : 0));
   }

   public void loadState(ByteBuffer var1) throws IOException {
      boolean var2 = var1.get() == 1;
      if (var2 != this.Locked) {
         this.Locked = var2;
      }

   }

   public void openCloseCurtain(IsoGameCharacter var1) {
      if (var1 == IsoPlayer.getInstance()) {
         IsoGridSquare var2 = null;
         Object var3 = null;
         IsoDirections var4 = IsoDirections.N;
         IsoGridSquare var5;
         if (this.north) {
            var5 = this.square;
            var4 = IsoDirections.N;
            if (var5.getRoom() == null) {
               var5 = this.getCell().getGridSquare(var5.getX(), var5.getY() - 1, var5.getZ());
               var4 = IsoDirections.S;
            }

            var3 = var2;
            var2 = var5;
         } else {
            var5 = this.square;
            var4 = IsoDirections.W;
            if (var5.getRoom() == null) {
               var5 = this.getCell().getGridSquare(var5.getX() - 1, var5.getY(), var5.getZ());
               var4 = IsoDirections.E;
            }

            var3 = var2;
            var2 = var5;
         }

         int var6;
         if (var2 != null) {
            for(var6 = 0; var6 < var2.getSpecialObjects().size(); ++var6) {
               if (var2.getSpecialObjects().get(var6) instanceof IsoCurtain) {
                  ((IsoCurtain)var2.getSpecialObjects().get(var6)).ToggleDoorSilent();
                  return;
               }
            }
         }

         if (var3 != null) {
            for(var6 = 0; var6 < ((IsoGridSquare)var3).getSpecialObjects().size(); ++var6) {
               if (((IsoGridSquare)var3).getSpecialObjects().get(var6) instanceof IsoCurtain) {
                  ((IsoCurtain)((IsoGridSquare)var3).getSpecialObjects().get(var6)).ToggleDoorSilent();
                  return;
               }
            }
         }
      }

   }

   public void removeSheet(IsoGameCharacter var1) {
      IsoGridSquare var2 = null;
      IsoDirections var3 = IsoDirections.N;
      IsoGridSquare var4;
      if (this.north) {
         var4 = this.square;
         var3 = IsoDirections.N;
         if (var4.getRoom() == null) {
            var4 = this.getCell().getGridSquare(var4.getX(), var4.getY() - 1, var4.getZ());
            var3 = IsoDirections.S;
         }

         var2 = var4;
      } else {
         var4 = this.square;
         var3 = IsoDirections.W;
         if (var4.getRoom() == null) {
            var4 = this.getCell().getGridSquare(var4.getX() - 1, var4.getY(), var4.getZ());
            var3 = IsoDirections.E;
         }

         var2 = var4;
      }

      for(int var6 = 0; var6 < var2.getSpecialObjects().size(); ++var6) {
         IsoObject var5 = (IsoObject)var2.getSpecialObjects().get(var6);
         if (var5 instanceof IsoCurtain) {
            var2.transmitRemoveItemFromSquare(var5);
            if (var1 != null) {
               if (GameServer.bServer) {
                  var1.sendObjectChange("addItemOfType", new Object[]{"type", var5.getName()});
               } else {
                  var1.getInventory().AddItem(var5.getName());
               }
            }
            break;
         }
      }

   }

   public void addSheet(IsoGameCharacter var1) {
      IsoGridSquare var2 = null;
      IsoObjectType var3;
      IsoGridSquare var4;
      if (this.north) {
         var4 = this.square;
         var3 = IsoObjectType.curtainN;
         if (var1 != null) {
            if (var1.getY() < this.getY()) {
               var4 = this.getCell().getGridSquare(var4.getX(), var4.getY() - 1, var4.getZ());
               var3 = IsoObjectType.curtainS;
            }
         } else if (var4.getRoom() == null) {
            var4 = this.getCell().getGridSquare(var4.getX(), var4.getY() - 1, var4.getZ());
            var3 = IsoObjectType.curtainS;
         }

         var2 = var4;
      } else {
         var4 = this.square;
         var3 = IsoObjectType.curtainW;
         if (var1 != null) {
            if (var1.getX() < this.getX()) {
               var4 = this.getCell().getGridSquare(var4.getX() - 1, var4.getY(), var4.getZ());
               var3 = IsoObjectType.curtainE;
            }
         } else if (var4.getRoom() == null) {
            var4 = this.getCell().getGridSquare(var4.getX() - 1, var4.getY(), var4.getZ());
            var3 = IsoObjectType.curtainE;
         }

         var2 = var4;
      }

      if (var2.getCurtain(var3) == null) {
         if (var2 != null) {
            int var6 = 16;
            if (var3 == IsoObjectType.curtainE) {
               ++var6;
            }

            if (var3 == IsoObjectType.curtainS) {
               var6 += 3;
            }

            if (var3 == IsoObjectType.curtainN) {
               var6 += 2;
            }

            var6 += 4;
            IsoCurtain var5 = new IsoCurtain(this.getCell(), var2, "fixtures_windows_curtains_01_" + var6, this.north);
            var2.AddSpecialTileObject(var5);
            if (!var5.open) {
               var5.ToggleDoorSilent();
            }

            if (GameServer.bServer) {
               var5.transmitCompleteItemToClients();
               if (var1 != null) {
                  var1.sendObjectChange("removeOneOf", new Object[]{"type", "Sheet"});
               }
            } else if (var1 != null) {
               var1.getInventory().RemoveOneOf("Sheet");
            }

         }
      }
   }

   public void ToggleWindow(IsoGameCharacter var1) {
      this.DirtySlice();
      IsoGridSquare.setRecalcLightTime(-1);
      if (!this.PermaLocked) {
         if (!this.destroyed) {
            if (var1 == null || this.getBarricadeForCharacter(var1) == null) {
               this.Locked = false;
               this.open = !this.open;
               this.sprite = this.closedSprite;
               this.square.InvalidateSpecialObjectPaths();
               if (this.open) {
                  if (!(var1 instanceof IsoZombie) || SandboxOptions.getInstance().Lore.TriggerHouseAlarm.getValue()) {
                     this.handleAlarm();
                  }

                  this.sprite = this.openSprite;
               }

               this.square.RecalcProperties();
               this.syncIsoObject(false, (byte)(this.open ? 1 : 0), (UdpConnection)null, (ByteBuffer)null);
               PolygonalMap2.instance.squareChanged(this.square);
               LuaEventManager.triggerEvent("OnContainerUpdate");
            }
         }
      }
   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      byte var2 = (byte)this.square.getObjects().indexOf(this);
      var1.putByte(var2);
      var1.putByte((byte)1);
      var1.putByte((byte)(this.open ? 1 : 0));
      var1.putByte((byte)(this.destroyed ? 1 : 0));
      var1.putByte((byte)(this.Locked ? 1 : 0));
      var1.putByte((byte)(this.PermaLocked ? 1 : 0));
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      if (this.square == null) {
         System.out.println("ERROR: " + this.getClass().getSimpleName() + " square is null");
      } else if (this.getObjectIndex() == -1) {
         PrintStream var10000 = System.out;
         String var10001 = this.getClass().getSimpleName();
         var10000.println("ERROR: " + var10001 + " not found on square " + this.square.getX() + "," + this.square.getY() + "," + this.square.getZ());
      } else {
         if (GameClient.bClient && !var1) {
            ByteBufferWriter var12 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SyncIsoObject.doPacket(var12);
            this.syncIsoObjectSend(var12);
            PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
         } else if (GameServer.bServer && !var1) {
            Iterator var11 = GameServer.udpEngine.connections.iterator();

            while(var11.hasNext()) {
               UdpConnection var13 = (UdpConnection)var11.next();
               ByteBufferWriter var14 = var13.startPacket();
               PacketTypes.PacketType.SyncIsoObject.doPacket(var14);
               this.syncIsoObjectSend(var14);
               PacketTypes.PacketType.SyncIsoObject.send(var13);
            }
         } else if (var1) {
            boolean var5 = var4.get() == 1;
            boolean var6 = var4.get() == 1;
            boolean var7 = var4.get() == 1;
            if (var2 == 1) {
               this.open = true;
               this.sprite = this.openSprite;
            } else if (var2 == 0) {
               this.open = false;
               this.sprite = this.closedSprite;
            }

            if (var5) {
               this.destroyed = true;
               this.sprite = this.smashedSprite;
            }

            this.Locked = var6;
            this.PermaLocked = var7;
            if (GameServer.bServer) {
               Iterator var8 = GameServer.udpEngine.connections.iterator();

               while(var8.hasNext()) {
                  UdpConnection var9 = (UdpConnection)var8.next();
                  if (var3 != null && var9.getConnectedGUID() != var3.getConnectedGUID()) {
                     ByteBufferWriter var10 = var9.startPacket();
                     PacketTypes.PacketType.SyncIsoObject.doPacket(var10);
                     this.syncIsoObjectSend(var10);
                     PacketTypes.PacketType.SyncIsoObject.send(var9);
                  }
               }
            }

            this.square.RecalcProperties();
            LuaEventManager.triggerEvent("OnContainerUpdate");
         }

      }
   }

   public static boolean isTopOfSheetRopeHere(IsoGridSquare var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.Is(IsoFlagType.climbSheetTopN) || var0.Is(IsoFlagType.climbSheetTopS) || var0.Is(IsoFlagType.climbSheetTopW) || var0.Is(IsoFlagType.climbSheetTopE);
      }
   }

   public static boolean isTopOfSheetRopeHere(IsoGridSquare var0, boolean var1) {
      if (var0 == null) {
         return false;
      } else {
         if (var1) {
            if (var0.Is(IsoFlagType.climbSheetTopN)) {
               return true;
            }

            if (var0.nav[IsoDirections.N.index()] != null && var0.nav[IsoDirections.N.index()].Is(IsoFlagType.climbSheetTopS)) {
               return true;
            }
         } else {
            if (var0.Is(IsoFlagType.climbSheetTopW)) {
               return true;
            }

            if (var0.nav[IsoDirections.W.index()] != null && var0.nav[IsoDirections.W.index()].Is(IsoFlagType.climbSheetTopE)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean haveSheetRope() {
      return isTopOfSheetRopeHere(this.square, this.north);
   }

   public static boolean isSheetRopeHere(IsoGridSquare var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.Is(IsoFlagType.climbSheetTopW) || var0.Is(IsoFlagType.climbSheetTopN) || var0.Is(IsoFlagType.climbSheetTopE) || var0.Is(IsoFlagType.climbSheetTopS) || var0.Is(IsoFlagType.climbSheetW) || var0.Is(IsoFlagType.climbSheetN) || var0.Is(IsoFlagType.climbSheetE) || var0.Is(IsoFlagType.climbSheetS);
      }
   }

   public static boolean canClimbHere(IsoGridSquare var0) {
      if (var0 == null) {
         return false;
      } else if (var0.getProperties().Is(IsoFlagType.solid)) {
         return false;
      } else if (!var0.Has(IsoObjectType.stairsBN) && !var0.Has(IsoObjectType.stairsMN) && !var0.Has(IsoObjectType.stairsTN)) {
         return !var0.Has(IsoObjectType.stairsBW) && !var0.Has(IsoObjectType.stairsMW) && !var0.Has(IsoObjectType.stairsTW);
      } else {
         return false;
      }
   }

   public static int countAddSheetRope(IsoGridSquare var0, boolean var1) {
      if (isTopOfSheetRopeHere(var0, var1)) {
         return 0;
      } else {
         IsoCell var2 = IsoWorld.instance.CurrentCell;
         if (var0.TreatAsSolidFloor()) {
            IsoGridSquare var3;
            if (var1) {
               var3 = var2.getOrCreateGridSquare((double)var0.getX(), (double)(var0.getY() - 1), (double)var0.getZ());
               if (var3 == null || var3.TreatAsSolidFloor() || isSheetRopeHere(var3) || !canClimbHere(var3)) {
                  return 0;
               }

               var0 = var3;
            } else {
               var3 = var2.getOrCreateGridSquare((double)(var0.getX() - 1), (double)var0.getY(), (double)var0.getZ());
               if (var3 == null || var3.TreatAsSolidFloor() || isSheetRopeHere(var3) || !canClimbHere(var3)) {
                  return 0;
               }

               var0 = var3;
            }
         }

         for(int var4 = 1; var0 != null; ++var4) {
            if (!canClimbHere(var0)) {
               return 0;
            }

            if (var0.TreatAsSolidFloor()) {
               return var4;
            }

            if (var0.getZ() == 0) {
               return var4;
            }

            var0 = var2.getOrCreateGridSquare((double)var0.getX(), (double)var0.getY(), (double)(var0.getZ() - 1));
         }

         return 0;
      }
   }

   public int countAddSheetRope() {
      return countAddSheetRope(this.square, this.north);
   }

   public static boolean canAddSheetRope(IsoGridSquare var0, boolean var1) {
      return countAddSheetRope(var0, var1) != 0;
   }

   public boolean canAddSheetRope() {
      return !this.canClimbThrough((IsoGameCharacter)null) ? false : canAddSheetRope(this.square, this.north);
   }

   public boolean addSheetRope(IsoPlayer var1, String var2) {
      return !this.canAddSheetRope() ? false : addSheetRope(var1, this.square, this.north, var2);
   }

   public static boolean addSheetRope(IsoPlayer var0, IsoGridSquare var1, boolean var2, String var3) {
      boolean var4 = false;
      int var5 = 0;
      byte var6 = 0;
      if (var2) {
         var6 = 1;
      }

      boolean var7 = false;
      boolean var8 = false;
      IsoGridSquare var9 = null;
      IsoGridSquare var10 = null;
      IsoCell var11 = IsoWorld.instance.CurrentCell;
      if (var1.TreatAsSolidFloor()) {
         if (!var2) {
            var9 = var11.getGridSquare(var1.getX() - 1, var1.getY(), var1.getZ());
            if (var9 != null) {
               var8 = true;
               var6 = 3;
            }
         } else {
            var10 = var11.getGridSquare(var1.getX(), var1.getY() - 1, var1.getZ());
            if (var10 != null) {
               var7 = true;
               var6 = 4;
            }
         }
      }

      if (var1.getProperties().Is(IsoFlagType.solidfloor)) {
      }

      while(var1 != null && (GameServer.bServer || var0.getInventory().contains(var3))) {
         String var12 = "crafted_01_" + var6;
         if (var5 > 0) {
            if (var8) {
               var12 = "crafted_01_10";
            } else if (var7) {
               var12 = "crafted_01_13";
            } else {
               var12 = "crafted_01_" + (var6 + 8);
            }
         }

         IsoObject var13 = new IsoObject(var11, var1, var12);
         var13.setName(var3);
         var13.sheetRope = true;
         var1.getObjects().add(var13);
         var13.transmitCompleteItemToClients();
         var1.haveSheetRope = true;
         if (var7 && var5 == 0) {
            var1 = var10;
            var13 = new IsoObject(var11, var10, "crafted_01_5");
            var13.setName(var3);
            var13.sheetRope = true;
            var10.getObjects().add(var13);
            var13.transmitCompleteItemToClients();
         }

         if (var8 && var5 == 0) {
            var1 = var9;
            var13 = new IsoObject(var11, var9, "crafted_01_2");
            var13.setName(var3);
            var13.sheetRope = true;
            var9.getObjects().add(var13);
            var13.transmitCompleteItemToClients();
         }

         var1.RecalcProperties();
         var1.getProperties().UnSet(IsoFlagType.solidtrans);
         if (GameServer.bServer) {
            if (var5 == 0) {
               var0.sendObjectChange("removeOneOf", new Object[]{"type", "Nails"});
            }

            var0.sendObjectChange("removeOneOf", new Object[]{"type", var3});
         } else {
            if (var5 == 0) {
               var0.getInventory().RemoveOneOf("Nails");
            }

            var0.getInventory().RemoveOneOf(var3);
         }

         ++var5;
         if (var4) {
            break;
         }

         var1 = var11.getOrCreateGridSquare((double)var1.getX(), (double)var1.getY(), (double)(var1.getZ() - 1));
         if (var1 != null && var1.TreatAsSolidFloor()) {
            var4 = true;
         }
      }

      return true;
   }

   public boolean removeSheetRope(IsoPlayer var1) {
      return !this.haveSheetRope() ? false : removeSheetRope(var1, this.square, this.north);
   }

   public static boolean removeSheetRope(IsoPlayer var0, IsoGridSquare var1, boolean var2) {
      if (var1 == null) {
         return false;
      } else {
         IsoGridSquare var6 = var1;
         var1.haveSheetRope = false;
         IsoFlagType var3;
         IsoFlagType var4;
         String var5;
         int var7;
         IsoObject var8;
         if (var2) {
            if (var1.Is(IsoFlagType.climbSheetTopN)) {
               var3 = IsoFlagType.climbSheetTopN;
               var4 = IsoFlagType.climbSheetN;
            } else {
               if (var1.nav[IsoDirections.N.index()] == null || !var1.nav[IsoDirections.N.index()].Is(IsoFlagType.climbSheetTopS)) {
                  return false;
               }

               var3 = IsoFlagType.climbSheetTopS;
               var4 = IsoFlagType.climbSheetS;
               var5 = "crafted_01_4";

               for(var7 = 0; var7 < var6.getObjects().size(); ++var7) {
                  var8 = (IsoObject)var6.getObjects().get(var7);
                  if (var8.sprite != null && var8.sprite.getName() != null && var8.sprite.getName().equals(var5)) {
                     var6.transmitRemoveItemFromSquare(var8);
                     break;
                  }
               }

               var6 = var1.nav[IsoDirections.N.index()];
            }
         } else if (var1.Is(IsoFlagType.climbSheetTopW)) {
            var3 = IsoFlagType.climbSheetTopW;
            var4 = IsoFlagType.climbSheetW;
         } else {
            if (var1.nav[IsoDirections.W.index()] == null || !var1.nav[IsoDirections.W.index()].Is(IsoFlagType.climbSheetTopE)) {
               return false;
            }

            var3 = IsoFlagType.climbSheetTopE;
            var4 = IsoFlagType.climbSheetE;
            var5 = "crafted_01_3";

            for(var7 = 0; var7 < var6.getObjects().size(); ++var7) {
               var8 = (IsoObject)var6.getObjects().get(var7);
               if (var8.sprite != null && var8.sprite.getName() != null && var8.sprite.getName().equals(var5)) {
                  var6.transmitRemoveItemFromSquare(var8);
                  break;
               }
            }

            var6 = var1.nav[IsoDirections.W.index()];
         }

         while(var6 != null) {
            boolean var10 = false;

            for(int var11 = 0; var11 < var6.getObjects().size(); ++var11) {
               IsoObject var9 = (IsoObject)var6.getObjects().get(var11);
               if (var9.getProperties() != null && (var9.getProperties().Is(var3) || var9.getProperties().Is(var4))) {
                  var6.transmitRemoveItemFromSquare(var9);
                  if (GameServer.bServer) {
                     if (var0 != null) {
                        var0.sendObjectChange("addItemOfType", new Object[]{"type", var9.getName()});
                     }
                  } else if (var0 != null) {
                     var0.getInventory().AddItem(var9.getName());
                  }

                  var10 = true;
                  break;
               }
            }

            if (!var10 || var6.getZ() == 0) {
               break;
            }

            var6 = var6.getCell().getGridSquare(var6.getX(), var6.getY(), var6.getZ() - 1);
         }

         return true;
      }
   }

   public void Damage(float var1) {
      this.Damage(var1, false);
   }

   public void Damage(float var1, boolean var2) {
      if (!this.isInvincible() && !"Tutorial".equals(Core.GameMode)) {
         this.DirtySlice();
         this.Health = (int)((float)this.Health - var1);
         if (this.Health < 0) {
            this.Health = 0;
         }

         if (!this.isDestroyed() && this.Health == 0) {
            this.smashWindow(false, !var2 || SandboxOptions.getInstance().Lore.TriggerHouseAlarm.getValue());
            if (this.getSquare().getBuilding() != null) {
               this.getSquare().getBuilding().forceAwake();
            }
         }

      }
   }

   public void Damage(float var1, IsoMovingObject var2) {
      if (!this.isInvincible() && !"Tutorial".equals(Core.GameMode)) {
         this.Health = (int)((float)this.Health - var1);
         if (this.Health < 0) {
            this.Health = 0;
         }

         if (!this.isDestroyed() && this.Health == 0) {
            boolean var3 = !(var2 instanceof IsoZombie) || SandboxOptions.getInstance().Lore.TriggerHouseAlarm.getValue();
            this.smashWindow(false, var3);
            this.addBrokenGlass(var2);
         }

      }
   }

   public boolean isLocked() {
      return this.Locked;
   }

   public boolean isSmashed() {
      return this.destroyed;
   }

   public boolean isInvincible() {
      if (this.square != null && this.square.Is(IsoFlagType.makeWindowInvincible)) {
         int var1 = this.getObjectIndex();
         if (var1 != -1) {
            IsoObject[] var2 = (IsoObject[])this.square.getObjects().getElements();
            int var3 = this.square.getObjects().size();

            for(int var4 = 0; var4 < var3; ++var4) {
               if (var4 != var1) {
                  IsoObject var5 = var2[var4];
                  PropertyContainer var6 = var5.getProperties();
                  if (var6 != null && var6.Is(this.getNorth() ? IsoFlagType.cutN : IsoFlagType.cutW) && var6.Is(IsoFlagType.makeWindowInvincible)) {
                     return true;
                  }
               }
            }
         }

         return this.sprite != null && this.sprite.getProperties().Is(IsoFlagType.makeWindowInvincible);
      } else {
         return false;
      }
   }

   public IsoBarricade getBarricadeOnSameSquare() {
      return IsoBarricade.GetBarricadeOnSquare(this.square, this.north ? IsoDirections.N : IsoDirections.W);
   }

   public IsoBarricade getBarricadeOnOppositeSquare() {
      return IsoBarricade.GetBarricadeOnSquare(this.getOppositeSquare(), this.north ? IsoDirections.S : IsoDirections.E);
   }

   public boolean isBarricaded() {
      IsoBarricade var1 = this.getBarricadeOnSameSquare();
      if (var1 == null) {
         var1 = this.getBarricadeOnOppositeSquare();
      }

      return var1 != null;
   }

   public boolean isBarricadeAllowed() {
      return true;
   }

   public IsoBarricade getBarricadeForCharacter(IsoGameCharacter var1) {
      return IsoBarricade.GetBarricadeForCharacter(this, var1);
   }

   public IsoBarricade getBarricadeOppositeCharacter(IsoGameCharacter var1) {
      return IsoBarricade.GetBarricadeOppositeCharacter(this, var1);
   }

   public boolean getNorth() {
      return this.north;
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      if (this.square == null) {
         return var1.set(0.0F, 0.0F);
      } else {
         return this.north ? var1.set(this.getX() + 0.5F, this.getY()) : var1.set(this.getX(), this.getY() + 0.5F);
      }
   }

   public void setIsLocked(boolean var1) {
      this.Locked = var1;
   }

   public IsoSprite getOpenSprite() {
      return this.openSprite;
   }

   public void setOpenSprite(IsoSprite var1) {
      this.openSprite = var1;
   }

   public void setSmashed(boolean var1) {
      if (var1) {
         this.destroyed = true;
         this.sprite = this.smashedSprite;
      } else {
         this.destroyed = false;
         this.sprite = this.open ? this.openSprite : this.closedSprite;
         this.Health = this.MaxHealth;
      }

      this.glassRemoved = false;
   }

   public IsoSprite getSmashedSprite() {
      return this.smashedSprite;
   }

   public void setSmashedSprite(IsoSprite var1) {
      this.smashedSprite = var1;
   }

   public void setPermaLocked(Boolean var1) {
      this.PermaLocked = var1;
   }

   public boolean isPermaLocked() {
      return this.PermaLocked;
   }

   public static boolean canClimbThroughHelper(IsoGameCharacter var0, IsoGridSquare var1, IsoGridSquare var2, boolean var3) {
      IsoGridSquare var4 = var1;
      float var5 = 0.5F;
      float var6 = 0.5F;
      if (var3) {
         if (var0.getY() >= (float)var1.getY()) {
            var4 = var2;
            var6 = 0.7F;
         } else {
            var6 = 0.3F;
         }
      } else if (var0.getX() >= (float)var1.getX()) {
         var4 = var2;
         var5 = 0.7F;
      } else {
         var5 = 0.3F;
      }

      if (var4 == null) {
         return false;
      } else if (var4.isSolid()) {
         return false;
      } else if (var4.Is(IsoFlagType.water)) {
         return false;
      } else if (!var0.canClimbDownSheetRope(var4) && !var4.HasStairsBelow() && !PolygonalMap2.instance.canStandAt((float)var4.x + var5, (float)var4.y + var6, var4.z, (BaseVehicle)null, 19)) {
         return !var4.TreatAsSolidFloor();
      } else {
         return !GameClient.bClient || !(var0 instanceof IsoPlayer) || SafeHouse.isSafeHouse(var4, ((IsoPlayer)var0).getUsername(), true) == null || ServerOptions.instance.SafehouseAllowTrepass.getValue();
      }
   }

   public boolean canClimbThrough(IsoGameCharacter var1) {
      if (this.square != null && !this.isInvincible()) {
         if (this.isBarricaded()) {
            return false;
         } else if (var1 != null && !canClimbThroughHelper(var1, this.getSquare(), this.getOppositeSquare(), this.north)) {
            return false;
         } else {
            IsoGameCharacter var2 = this.getFirstCharacterClosing();
            if (var2 != null && var2.isVariable("CloseWindowOutcome", "success")) {
               return false;
            } else {
               return this.Health > 0 && !this.destroyed ? this.open : true;
            }
         }
      } else {
         return false;
      }
   }

   public IsoGameCharacter getFirstCharacterClimbingThrough() {
      IsoGameCharacter var1 = this.getFirstCharacterClimbingThrough(this.getSquare());
      return var1 != null ? var1 : this.getFirstCharacterClimbingThrough(this.getOppositeSquare());
   }

   public IsoGameCharacter getFirstCharacterClimbingThrough(IsoGridSquare var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var1.getMovingObjects().size(); ++var2) {
            IsoGameCharacter var3 = (IsoGameCharacter)Type.tryCastTo((IsoMovingObject)var1.getMovingObjects().get(var2), IsoGameCharacter.class);
            if (var3 != null && var3.isClimbingThroughWindow(this)) {
               return var3;
            }
         }

         return null;
      }
   }

   public IsoGameCharacter getFirstCharacterClosing() {
      IsoGameCharacter var1 = this.getFirstCharacterClosing(this.getSquare());
      return var1 != null ? var1 : this.getFirstCharacterClosing(this.getOppositeSquare());
   }

   public IsoGameCharacter getFirstCharacterClosing(IsoGridSquare var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var1.getMovingObjects().size(); ++var2) {
            IsoGameCharacter var3 = (IsoGameCharacter)Type.tryCastTo((IsoMovingObject)var1.getMovingObjects().get(var2), IsoGameCharacter.class);
            if (var3 != null && var3.isClosingWindow(this)) {
               return var3;
            }
         }

         return null;
      }
   }

   public boolean isGlassRemoved() {
      return this.glassRemoved;
   }

   public void setGlassRemoved(boolean var1) {
      if (this.destroyed) {
         if (var1) {
            this.sprite = this.glassRemovedSprite;
            this.glassRemoved = true;
         } else {
            this.sprite = this.smashedSprite;
            this.glassRemoved = false;
         }

         if (this.getObjectIndex() != -1) {
            PolygonalMap2.instance.squareChanged(this.square);
         }

      }
   }

   public void removeBrokenGlass() {
      if (GameClient.bClient) {
         GameClient.instance.smashWindow(this, 2);
      } else {
         this.setGlassRemoved(true);
      }

   }

   public IsoBarricade addBarricadesDebug(int var1, boolean var2) {
      IsoGridSquare var3 = this.square.getRoom() == null ? this.square : this.getOppositeSquare();
      boolean var4 = var3 != this.square;
      IsoBarricade var5 = IsoBarricade.AddBarricadeToObject(this, var4);
      if (var5 != null) {
         for(int var6 = 0; var6 < var1; ++var6) {
            if (var2) {
               var5.addMetalBar((IsoGameCharacter)null, (InventoryItem)null);
            } else {
               var5.addPlank((IsoGameCharacter)null, (InventoryItem)null);
            }
         }
      }

      return var5;
   }

   public void addRandomBarricades() {
      IsoGridSquare var1 = this.square.getRoom() == null ? this.square : this.getOppositeSquare();
      if (this.getZ() == 0.0F && var1 != null && var1.getRoom() == null) {
         boolean var2 = var1 != this.square;
         IsoBarricade var3 = IsoBarricade.AddBarricadeToObject(this, var2);
         if (var3 != null) {
            int var4 = Rand.Next(1, 4);

            for(int var5 = 0; var5 < var4; ++var5) {
               var3.addPlank((IsoGameCharacter)null, (InventoryItem)null);
            }

            if (GameServer.bServer) {
               var3.transmitCompleteItemToClients();
            }
         }
      } else {
         this.addSheet((IsoGameCharacter)null);
         this.HasCurtains().ToggleDoor((IsoGameCharacter)null);
      }

   }

   public static enum WindowType {
      SinglePane,
      DoublePane;

      // $FF: synthetic method
      private static IsoWindow.WindowType[] $values() {
         return new IsoWindow.WindowType[]{SinglePane, DoublePane};
      }
   }
}
