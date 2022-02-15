package zombie.iso.objects;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.SystemDisabler;
import zombie.WorldSoundManager;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.states.ThumpState;
import zombie.audio.parameters.ParameterMeleeHitSurface;
import zombie.characters.BaseCharacterSoundEmitter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.core.Translator;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.HandWeapon;
import zombie.iso.BrokenFences;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.LosUtil;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;
import zombie.util.Type;
import zombie.util.io.BitHeader;
import zombie.util.io.BitHeaderRead;
import zombie.util.io.BitHeaderWrite;
import zombie.world.WorldDictionary;

public class IsoThumpable extends IsoObject implements BarricadeAble, Thumpable {
   private KahluaTable table;
   private KahluaTable modData;
   public Boolean isDoor = false;
   public Boolean isDoorFrame = false;
   public String breakSound = "BreakObject";
   private boolean isCorner = false;
   private boolean isFloor = false;
   private boolean blockAllTheSquare = false;
   public boolean Locked = false;
   public int MaxHealth = 500;
   public int Health = 500;
   public int PushedMaxStrength = 0;
   public int PushedStrength = 0;
   IsoSprite closedSprite;
   public boolean north = false;
   private int thumpDmg = 8;
   private float crossSpeed = 1.0F;
   public boolean open = false;
   IsoSprite openSprite;
   private boolean destroyed = false;
   private boolean canBarricade = false;
   public boolean canPassThrough = false;
   private boolean isStairs = false;
   private boolean isContainer = false;
   private boolean dismantable = false;
   private boolean canBePlastered = false;
   private boolean paintable = false;
   private boolean isThumpable = true;
   private boolean isHoppable = false;
   private int lightSourceRadius = -1;
   private int lightSourceLife = -1;
   private int lightSourceXOffset = 0;
   private int lightSourceYOffset = 0;
   private boolean lightSourceOn = false;
   private IsoLightSource lightSource = null;
   private String lightSourceFuel = null;
   private float lifeLeft = -1.0F;
   private float lifeDelta = 0.0F;
   private boolean haveFuel = false;
   private float updateAccumulator = 0.0F;
   private float lastUpdateHours = -1.0F;
   public int keyId = -1;
   private boolean lockedByKey = false;
   public boolean lockedByPadlock = false;
   private boolean canBeLockByPadlock = false;
   public int lockedByCode = 0;
   public int OldNumPlanks = 0;
   public String thumpSound = "ZombieThumpGeneric";
   public static final Vector2 tempo = new Vector2();

   public KahluaTable getModData() {
      if (this.modData == null) {
         this.modData = LuaManager.platform.newTable();
      }

      return this.modData;
   }

   public void setModData(KahluaTable var1) {
      this.modData = var1;
   }

   public boolean hasModData() {
      return this.modData != null && !this.modData.isEmpty();
   }

   public boolean isCanPassThrough() {
      return this.canPassThrough;
   }

   public void setCanPassThrough(boolean var1) {
      this.canPassThrough = var1;
   }

   public boolean isBlockAllTheSquare() {
      return this.blockAllTheSquare;
   }

   public void setBlockAllTheSquare(boolean var1) {
      this.blockAllTheSquare = var1;
   }

   public void setIsDismantable(boolean var1) {
      this.dismantable = var1;
   }

   public boolean isDismantable() {
      return this.dismantable;
   }

   public float getCrossSpeed() {
      return this.crossSpeed;
   }

   public void setCrossSpeed(float var1) {
      this.crossSpeed = var1;
   }

   public void setIsFloor(boolean var1) {
      this.isFloor = var1;
   }

   public boolean isCorner() {
      return this.isCorner;
   }

   public boolean isFloor() {
      return this.isFloor;
   }

   public void setIsContainer(boolean var1) {
      this.isContainer = var1;
      if (var1) {
         this.container = new ItemContainer("crate", this.square, this);
         if (this.sprite.getProperties().Is("ContainerCapacity")) {
            this.container.Capacity = Integer.parseInt(this.sprite.getProperties().Val("ContainerCapacity"));
         }

         this.container.setExplored(true);
      }

   }

   public void setIsStairs(boolean var1) {
      this.isStairs = var1;
   }

   public boolean isStairs() {
      return this.isStairs;
   }

   public boolean isWindow() {
      return this.sprite != null && (this.sprite.getProperties().Is(IsoFlagType.WindowN) || this.sprite.getProperties().Is(IsoFlagType.WindowW));
   }

   public String getObjectName() {
      return "Thumpable";
   }

   public IsoThumpable(IsoCell var1) {
      super(var1);
   }

   public void setCorner(boolean var1) {
      this.isCorner = var1;
   }

   public void setCanBarricade(boolean var1) {
      this.canBarricade = var1;
   }

   public boolean getCanBarricade() {
      return this.canBarricade;
   }

   public void setHealth(int var1) {
      this.Health = var1;
   }

   public int getHealth() {
      return this.Health;
   }

   public void setMaxHealth(int var1) {
      this.MaxHealth = var1;
   }

   public int getMaxHealth() {
      return this.MaxHealth;
   }

   public void setThumpDmg(Integer var1) {
      this.thumpDmg = var1;
   }

   public int getThumpDmg() {
      return this.thumpDmg;
   }

   public void setBreakSound(String var1) {
      this.breakSound = var1;
   }

   public String getBreakSound() {
      return this.breakSound;
   }

   public boolean isDoor() {
      return this.isDoor;
   }

   public boolean getNorth() {
      return this.north;
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      if (this.square == null) {
         return var1.set(0.0F, 0.0F);
      } else if (!this.isDoor && !this.isDoorFrame && !this.isWindow() && !this.isHoppable && (this.getProperties() == null || !this.getProperties().Is(IsoFlagType.collideN) && !this.getProperties().Is(IsoFlagType.collideW))) {
         return var1.set(this.getX() + 0.5F, this.getY() + 0.5F);
      } else {
         return this.north ? var1.set(this.getX() + 0.5F, this.getY()) : var1.set(this.getX(), this.getY() + 0.5F);
      }
   }

   public boolean isDoorFrame() {
      return this.isDoorFrame;
   }

   public void setIsDoor(boolean var1) {
      this.isDoor = var1;
   }

   public void setIsDoorFrame(boolean var1) {
      this.isDoorFrame = var1;
   }

   public void setSprite(String var1) {
      this.closedSprite = IsoSpriteManager.instance.getSprite(var1);
      this.sprite = this.closedSprite;
   }

   public void setSpriteFromName(String var1) {
      this.sprite = IsoSpriteManager.instance.getSprite(var1);
   }

   public void setClosedSprite(IsoSprite var1) {
      this.closedSprite = var1;
      this.sprite = this.closedSprite;
   }

   public void setOpenSprite(IsoSprite var1) {
      this.openSprite = var1;
   }

   public IsoThumpable(IsoCell var1, IsoGridSquare var2, String var3, String var4, boolean var5, KahluaTable var6) {
      this.OutlineOnMouseover = true;
      this.PushedMaxStrength = this.PushedStrength = 2500;
      this.openSprite = IsoSpriteManager.instance.getSprite(var4);
      this.closedSprite = IsoSpriteManager.instance.getSprite(var3);
      this.table = var6;
      this.sprite = this.closedSprite;
      this.square = var2;
      this.north = var5;
   }

   public IsoThumpable(IsoCell var1, IsoGridSquare var2, String var3, boolean var4, KahluaTable var5) {
      this.OutlineOnMouseover = true;
      this.PushedMaxStrength = this.PushedStrength = 2500;
      this.closedSprite = IsoSpriteManager.instance.getSprite(var3);
      this.table = var5;
      this.sprite = this.closedSprite;
      this.square = var2;
      this.north = var4;
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      BitHeaderRead var4 = BitHeader.allocRead(BitHeader.HeaderSize.Long, var1);
      this.OutlineOnMouseover = true;
      this.PushedMaxStrength = this.PushedStrength = 2500;
      if (!var4.equals(0)) {
         this.open = var4.hasFlags(1);
         this.Locked = var4.hasFlags(2);
         this.north = var4.hasFlags(4);
         if (var4.hasFlags(8)) {
            this.MaxHealth = var1.getInt();
         }

         if (var4.hasFlags(16)) {
            this.Health = var1.getInt();
         } else {
            this.Health = this.MaxHealth;
         }

         if (var4.hasFlags(32)) {
            this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var4.hasFlags(64)) {
            this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         }

         if (var4.hasFlags(128)) {
            this.thumpDmg = var1.getInt();
         }

         this.isDoor = var4.hasFlags(512);
         this.isDoorFrame = var4.hasFlags(1024);
         this.isCorner = var4.hasFlags(2048);
         this.isStairs = var4.hasFlags(4096);
         this.isContainer = var4.hasFlags(8192);
         this.isFloor = var4.hasFlags(16384);
         this.canBarricade = var4.hasFlags(32768);
         this.canPassThrough = var4.hasFlags(65536);
         this.dismantable = var4.hasFlags(131072);
         this.canBePlastered = var4.hasFlags(262144);
         this.paintable = var4.hasFlags(524288);
         if (var4.hasFlags(1048576)) {
            this.crossSpeed = var1.getFloat();
         }

         if (var4.hasFlags(2097152)) {
            if (this.table == null) {
               this.table = LuaManager.platform.newTable();
            }

            this.table.load(var1, var2);
         }

         if (var4.hasFlags(4194304)) {
            if (this.modData == null) {
               this.modData = LuaManager.platform.newTable();
            }

            this.modData.load(var1, var2);
         }

         this.blockAllTheSquare = var4.hasFlags(8388608);
         this.isThumpable = var4.hasFlags(16777216);
         this.isHoppable = var4.hasFlags(33554432);
         if (var4.hasFlags(67108864)) {
            this.setLightSourceLife(var1.getInt());
         }

         if (var4.hasFlags(134217728)) {
            this.setLightSourceRadius(var1.getInt());
         }

         if (var4.hasFlags(268435456)) {
            this.setLightSourceXOffset(var1.getInt());
         }

         if (var4.hasFlags(536870912)) {
            this.setLightSourceYOffset(var1.getInt());
         }

         if (var4.hasFlags(1073741824)) {
            this.setLightSourceFuel(WorldDictionary.getItemTypeFromID(var1.getShort()));
         }

         if (var4.hasFlags(2147483648L)) {
            this.setLifeDelta(var1.getFloat());
         }

         if (var4.hasFlags(4294967296L)) {
            this.setLifeLeft(var1.getFloat());
         }

         if (var4.hasFlags(8589934592L)) {
            this.keyId = var1.getInt();
         }

         this.lockedByKey = var4.hasFlags(17179869184L);
         this.lockedByPadlock = var4.hasFlags(34359738368L);
         this.canBeLockByPadlock = var4.hasFlags(68719476736L);
         if (var4.hasFlags(137438953472L)) {
            this.lockedByCode = var1.getInt();
         }

         if (var4.hasFlags(274877906944L)) {
            this.thumpSound = GameWindow.ReadString(var1);
            if ("thumpa2".equals(this.thumpSound)) {
               this.thumpSound = "ZombieThumpGeneric";
            }

            if ("metalthump".equals(this.thumpSound)) {
               this.thumpSound = "ZombieThumpMetal";
            }
         }

         if (var4.hasFlags(549755813888L)) {
            this.lastUpdateHours = var1.getFloat();
         }

         if (var2 >= 183) {
            if (var4.hasFlags(1099511627776L)) {
               this.haveFuel = true;
            }

            if (var4.hasFlags(2199023255552L)) {
               this.lightSourceOn = true;
            }
         }
      }

      var4.release();
      if (this.getLightSourceFuel() != null) {
         boolean var5 = this.isLightSourceOn();
         this.createLightSource(this.getLightSourceRadius(), this.getLightSourceXOffset(), this.getLightSourceYOffset(), 0, this.getLightSourceLife(), this.getLightSourceFuel(), (InventoryItem)null, (IsoGameCharacter)null);
         if (this.lightSource != null) {
            this.getLightSource().setActive(var5);
         }

         this.setLightSourceOn(var5);
      }

      if (SystemDisabler.doObjectStateSyncEnable && GameClient.bClient) {
         GameClient.instance.objectSyncReq.putRequestLoad(this.square);
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      BitHeaderWrite var3 = BitHeader.allocWrite(BitHeader.HeaderSize.Long, var1);
      if (this.open) {
         var3.addFlags(1);
      }

      if (this.Locked) {
         var3.addFlags(2);
      }

      if (this.north) {
         var3.addFlags(4);
      }

      if (this.MaxHealth != 500) {
         var3.addFlags(8);
         var1.putInt(this.MaxHealth);
      }

      if (this.Health != this.MaxHealth) {
         var3.addFlags(16);
         var1.putInt(this.Health);
      }

      if (this.closedSprite != null) {
         var3.addFlags(32);
         var1.putInt(this.closedSprite.ID);
      }

      if (this.openSprite != null) {
         var3.addFlags(64);
         var1.putInt(this.openSprite.ID);
      }

      if (this.thumpDmg != 8) {
         var3.addFlags(128);
         var1.putInt(this.thumpDmg);
      }

      if (this.isDoor) {
         var3.addFlags(512);
      }

      if (this.isDoorFrame) {
         var3.addFlags(1024);
      }

      if (this.isCorner) {
         var3.addFlags(2048);
      }

      if (this.isStairs) {
         var3.addFlags(4096);
      }

      if (this.isContainer) {
         var3.addFlags(8192);
      }

      if (this.isFloor) {
         var3.addFlags(16384);
      }

      if (this.canBarricade) {
         var3.addFlags(32768);
      }

      if (this.canPassThrough) {
         var3.addFlags(65536);
      }

      if (this.dismantable) {
         var3.addFlags(131072);
      }

      if (this.canBePlastered) {
         var3.addFlags(262144);
      }

      if (this.paintable) {
         var3.addFlags(524288);
      }

      if (this.crossSpeed != 1.0F) {
         var3.addFlags(1048576);
         var1.putFloat(this.crossSpeed);
      }

      if (this.table != null && !this.table.isEmpty()) {
         var3.addFlags(2097152);
         this.table.save(var1);
      }

      if (this.modData != null && !this.modData.isEmpty()) {
         var3.addFlags(4194304);
         this.modData.save(var1);
      }

      if (this.blockAllTheSquare) {
         var3.addFlags(8388608);
      }

      if (this.isThumpable) {
         var3.addFlags(16777216);
      }

      if (this.isHoppable) {
         var3.addFlags(33554432);
      }

      if (this.getLightSourceLife() != -1) {
         var3.addFlags(67108864);
         var1.putInt(this.getLightSourceLife());
      }

      if (this.getLightSourceRadius() != -1) {
         var3.addFlags(134217728);
         var1.putInt(this.getLightSourceRadius());
      }

      if (this.getLightSourceXOffset() != 0) {
         var3.addFlags(268435456);
         var1.putInt(this.getLightSourceXOffset());
      }

      if (this.getLightSourceYOffset() != 0) {
         var3.addFlags(536870912);
         var1.putInt(this.getLightSourceYOffset());
      }

      if (this.getLightSourceFuel() != null) {
         var3.addFlags(1073741824);
         var1.putShort(WorldDictionary.getItemRegistryID(this.getLightSourceFuel()));
      }

      if (this.getLifeDelta() != 0.0F) {
         var3.addFlags(2147483648L);
         var1.putFloat(this.getLifeDelta());
      }

      if (this.getLifeLeft() != -1.0F) {
         var3.addFlags(4294967296L);
         var1.putFloat(this.getLifeLeft());
      }

      if (this.keyId != -1) {
         var3.addFlags(8589934592L);
         var1.putInt(this.keyId);
      }

      if (this.isLockedByKey()) {
         var3.addFlags(17179869184L);
      }

      if (this.isLockedByPadlock()) {
         var3.addFlags(34359738368L);
      }

      if (this.canBeLockByPadlock()) {
         var3.addFlags(68719476736L);
      }

      if (this.getLockedByCode() != 0) {
         var3.addFlags(137438953472L);
         var1.putInt(this.getLockedByCode());
      }

      if (!this.thumpSound.equals("ZombieThumbGeneric")) {
         var3.addFlags(274877906944L);
         GameWindow.WriteString(var1, this.thumpSound);
      }

      if (this.lastUpdateHours != -1.0F) {
         var3.addFlags(549755813888L);
         var1.putFloat(this.lastUpdateHours);
      }

      if (this.haveFuel) {
         var3.addFlags(1099511627776L);
      }

      if (this.lightSourceOn) {
         var3.addFlags(2199023255552L);
      }

      var3.write();
      var3.release();
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public boolean IsOpen() {
      return this.open;
   }

   public boolean IsStrengthenedByPushedItems() {
      return false;
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      return false;
   }

   public boolean TestPathfindCollide(IsoMovingObject var1, IsoGridSquare var2, IsoGridSquare var3) {
      boolean var4 = this.north;
      if (var1 instanceof IsoSurvivor && ((IsoSurvivor)var1).getInventory().contains("Hammer")) {
         return false;
      } else if (this.open) {
         return false;
      } else {
         if (var2 == this.square) {
            if (var4 && var3.getY() < var2.getY()) {
               return true;
            }

            if (!var4 && var3.getX() < var2.getX()) {
               return true;
            }
         } else {
            if (var4 && var3.getY() > var2.getY()) {
               return true;
            }

            if (!var4 && var3.getX() > var2.getX()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean TestCollide(IsoMovingObject var1, IsoGridSquare var2, IsoGridSquare var3) {
      if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isNoClip()) {
         return false;
      } else {
         boolean var4 = this.north;
         if (this.open) {
            return false;
         } else if (this.blockAllTheSquare) {
            if (var2 != this.square) {
               if (var1 != null) {
                  var1.collideWith(this);
               }

               return true;
            } else {
               return false;
            }
         } else {
            if (var2 == this.square) {
               if (var4 && var3.getY() < var2.getY()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough && !this.isStairs && !this.isCorner) {
                     return true;
                  }
               }

               if (!var4 && var3.getX() < var2.getX()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough && !this.isStairs && !this.isCorner) {
                     return true;
                  }
               }
            } else {
               if (var4 && var3.getY() > var2.getY()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough && !this.isStairs && !this.isCorner) {
                     return true;
                  }
               }

               if (!var4 && var3.getX() > var2.getX()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough && !this.isStairs && !this.isCorner) {
                     return true;
                  }
               }
            }

            if (this.isCorner) {
               if (var3.getY() < var2.getY() && var3.getX() < var2.getX()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough) {
                     return true;
                  }
               }

               if (var3.getY() > var2.getY() && var3.getX() > var2.getX()) {
                  if (var1 != null) {
                     var1.collideWith(this);
                  }

                  if (!this.canPassThrough) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      if (this.canPassThrough) {
         return IsoObject.VisionResult.NoEffect;
      } else {
         boolean var3 = this.north;
         if (this.open) {
            var3 = !var3;
         }

         if (var2.getZ() != var1.getZ()) {
            return IsoObject.VisionResult.NoEffect;
         } else {
            boolean var4 = this.sprite != null && this.sprite.getProperties().Is("doorTrans");
            if (var1 == this.square) {
               if (var3 && var2.getY() < var1.getY()) {
                  if (var4) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  if (this.isWindow()) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  return IsoObject.VisionResult.Blocked;
               }

               if (!var3 && var2.getX() < var1.getX()) {
                  if (var4) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  if (this.isWindow()) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  return IsoObject.VisionResult.Blocked;
               }
            } else {
               if (var3 && var2.getY() > var1.getY()) {
                  if (var4) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  if (this.isWindow()) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  return IsoObject.VisionResult.Blocked;
               }

               if (!var3 && var2.getX() > var1.getX()) {
                  if (var4) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  if (this.isWindow()) {
                     return IsoObject.VisionResult.Unblocked;
                  }

                  return IsoObject.VisionResult.Blocked;
               }
            }

            return IsoObject.VisionResult.NoEffect;
         }
      }
   }

   public void Thump(IsoMovingObject var1) {
      if (SandboxOptions.instance.Lore.ThumpOnConstruction.getValue()) {
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

         boolean var6 = BrokenFences.getInstance().isBreakableObject(this);
         if (var1 instanceof IsoZombie) {
            if (((IsoZombie)var1).cognition == 1 && this.isDoor() && !this.IsOpen() && !this.isLocked()) {
               this.ToggleDoor((IsoGameCharacter)var1);
               return;
            }

            int var3 = var1.getCurrentSquare().getMovingObjects().size();
            if (var1.getCurrentSquare().getW() != null) {
               var3 += var1.getCurrentSquare().getW().getMovingObjects().size();
            }

            if (var1.getCurrentSquare().getE() != null) {
               var3 += var1.getCurrentSquare().getE().getMovingObjects().size();
            }

            if (var1.getCurrentSquare().getS() != null) {
               var3 += var1.getCurrentSquare().getS().getMovingObjects().size();
            }

            if (var1.getCurrentSquare().getN() != null) {
               var3 += var1.getCurrentSquare().getN().getMovingObjects().size();
            }

            int var4 = this.thumpDmg;
            int var5;
            if (var3 >= var4) {
               var5 = 1 * ThumpState.getFastForwardDamageMultiplier();
               this.Health -= var5;
            } else {
               this.partialThumpDmg += (float)var3 / (float)var4 * (float)ThumpState.getFastForwardDamageMultiplier();
               if ((int)this.partialThumpDmg > 0) {
                  var5 = (int)this.partialThumpDmg;
                  this.Health -= var5;
                  this.partialThumpDmg -= (float)var5;
               }
            }

            WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, true, 4.0F, 15.0F);
            if (this.isDoor()) {
               this.setRenderEffect(RenderEffectType.Hit_Door, true);
            }
         }

         if (this.Health <= 0) {
            ((IsoGameCharacter)var1).getEmitter().playSound(this.breakSound, this);
            if (GameServer.bServer) {
               GameServer.PlayWorldSoundServer(this.breakSound, false, var1.getCurrentSquare(), 0.2F, 20.0F, 1.1F, true);
            }

            WorldSoundManager.instance.addSound((Object)null, this.square.getX(), this.square.getY(), this.square.getZ(), 10, 20, true, 4.0F, 15.0F);
            var1.setThumpTarget((Thumpable)null);
            if (IsoDoor.destroyDoubleDoor(this)) {
               return;
            }

            if (IsoDoor.destroyGarageDoor(this)) {
               return;
            }

            if (var6) {
               PropertyContainer var7 = this.getProperties();
               IsoDirections var8;
               if (var7.Is(IsoFlagType.collideN) && var7.Is(IsoFlagType.collideW)) {
                  var8 = var1.getY() >= this.getY() ? IsoDirections.N : IsoDirections.S;
               } else if (var7.Is(IsoFlagType.collideN)) {
                  var8 = var1.getY() >= this.getY() ? IsoDirections.N : IsoDirections.S;
               } else {
                  var8 = var1.getX() >= this.getX() ? IsoDirections.W : IsoDirections.E;
               }

               BrokenFences.getInstance().destroyFence(this, var8);
               return;
            }

            this.destroy();
         }

      }
   }

   public Thumpable getThumpableFor(IsoGameCharacter var1) {
      if (this.isDoor() || this.isWindow()) {
         IsoBarricade var2 = this.getBarricadeForCharacter(var1);
         if (var2 != null) {
            return var2;
         }

         var2 = this.getBarricadeOppositeCharacter(var1);
         if (var2 != null) {
            return var2;
         }
      }

      boolean var4 = this.isThumpable;
      boolean var3 = var1 instanceof IsoZombie && ((IsoZombie)var1).isCrawling();
      if (!var4 && var3 && BrokenFences.getInstance().isBreakableObject(this)) {
         var4 = true;
      }

      if (!var4 && var3 && this.isHoppable()) {
         var4 = true;
      }

      if (var4 && !this.isDestroyed()) {
         if ((!this.isDoor() || !this.IsOpen()) && !this.isWindow()) {
            return !var3 && this.isHoppable() ? null : this;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public float getThumpCondition() {
      return (float)PZMath.clamp(this.Health, 0, this.MaxHealth) / (float)this.MaxHealth;
   }

   public void WeaponHit(IsoGameCharacter var1, HandWeapon var2) {
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (GameClient.bClient) {
         if (var3 != null) {
            GameClient.instance.sendWeaponHit(var3, var2, this);
         }

         if (this.isDoor()) {
            this.setRenderEffect(RenderEffectType.Hit_Door, true);
         }

      } else {
         Thumpable var4 = this.getThumpableFor(var1);
         if (var4 != null) {
            if (var4 instanceof IsoBarricade) {
               ((IsoBarricade)var4).WeaponHit(var1, var2);
            } else {
               this.Damage(var2.getDoorDamage());
               if (var2.getDoorHitSound() != null) {
                  if (var3 != null) {
                     String var5 = this.getSoundPrefix();
                     byte var6 = -1;
                     switch(var5.hashCode()) {
                     case -1326475798:
                        if (var5.equals("PrisonMetalDoor")) {
                           var6 = 3;
                        }
                        break;
                     case -247340139:
                        if (var5.equals("GarageDoor")) {
                           var6 = 0;
                        }
                        break;
                     case 228116188:
                        if (var5.equals("SlidingGlassDoor")) {
                           var6 = 4;
                        }
                        break;
                     case 945260341:
                        if (var5.equals("MetalDoor")) {
                           var6 = 1;
                        }
                        break;
                     case 945336402:
                        if (var5.equals("MetalGate")) {
                           var6 = 2;
                        }
                     }

                     switch(var6) {
                     case 0:
                     case 1:
                     case 2:
                     case 3:
                        var3.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Metal);
                        break;
                     case 4:
                        var3.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Glass);
                        break;
                     default:
                        var3.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Wood);
                     }
                  }

                  var1.getEmitter().playSound(var2.getDoorHitSound(), this);
                  if (GameServer.bServer) {
                     GameServer.PlayWorldSoundServer(var2.getDoorHitSound(), false, this.getSquare(), 0.2F, 20.0F, 1.0F, false);
                  }
               }

               WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, false, 0.0F, 15.0F);
               if (this.isDoor()) {
                  this.setRenderEffect(RenderEffectType.Hit_Door, true);
               }

               if (!this.IsStrengthenedByPushedItems() && this.Health <= 0 || this.IsStrengthenedByPushedItems() && this.Health <= -this.PushedMaxStrength) {
                  var1.getEmitter().playSound(this.breakSound, this);
                  WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, false, 0.0F, 15.0F);
                  if (GameClient.bClient) {
                     GameClient.instance.sendClientCommandV((IsoPlayer)null, "object", "OnDestroyIsoThumpable", "x", (int)this.getX(), "y", (int)this.getY(), "z", (int)this.getZ(), "index", this.getObjectIndex());
                  }

                  LuaEventManager.triggerEvent("OnDestroyIsoThumpable", this, (Object)null);
                  if (IsoDoor.destroyDoubleDoor(this)) {
                     return;
                  }

                  if (IsoDoor.destroyGarageDoor(this)) {
                     return;
                  }

                  this.destroyed = true;
                  if (this.getObjectIndex() != -1) {
                     this.square.transmitRemoveItemFromSquare(this);
                  }
               }

            }
         }
      }
   }

   public IsoGridSquare getOtherSideOfDoor(IsoGameCharacter var1) {
      if (this.north) {
         return var1.getCurrentSquare().getRoom() == this.square.getRoom() ? IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX(), this.square.getY() - 1, this.square.getZ()) : IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX(), this.square.getY(), this.square.getZ());
      } else {
         return var1.getCurrentSquare().getRoom() == this.square.getRoom() ? IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX() - 1, this.square.getY(), this.square.getZ()) : IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX(), this.square.getY(), this.square.getZ());
      }
   }

   public void ToggleDoorActual(IsoGameCharacter var1) {
      if (this.isBarricaded()) {
         if (var1 != null) {
            this.playDoorSound(var1.getEmitter(), "Blocked");
            var1.setHaloNote(Translator.getText("IGUI_PlayerText_DoorBarricaded"), 255, 255, 255, 256.0F);
            this.setRenderEffect(RenderEffectType.Hit_Door, true);
         }

      } else if (this.isLockedByKey() && var1 instanceof IsoPlayer && var1.getCurrentSquare().Is(IsoFlagType.exterior) && var1.getInventory().haveThisKeyId(this.getKeyId()) == null) {
         this.playDoorSound(var1.getEmitter(), "Locked");
         this.setRenderEffect(RenderEffectType.Hit_Door, true);
      } else {
         if (this.isLockedByKey() && var1 instanceof IsoPlayer && var1.getInventory().haveThisKeyId(this.getKeyId()) != null) {
            this.playDoorSound(var1.getEmitter(), "Unlock");
            this.setIsLocked(false);
            this.setLockedByKey(false);
         }

         this.DirtySlice();
         this.square.InvalidateSpecialObjectPaths();
         if (this.Locked && var1 instanceof IsoPlayer && var1.getCurrentSquare().Is(IsoFlagType.exterior) && !this.open) {
            this.playDoorSound(var1.getEmitter(), "Locked");
            this.setRenderEffect(RenderEffectType.Hit_Door, true);
         } else {
            if (var1 instanceof IsoPlayer) {
            }

            for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
               LosUtil.cachecleared[var2] = true;
            }

            IsoGridSquare.setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
            if (this.getSprite().getProperties().Is("DoubleDoor")) {
               if (IsoDoor.isDoubleDoorObstructed(this)) {
                  if (var1 != null) {
                     this.playDoorSound(var1.getEmitter(), "Blocked");
                     var1.setHaloNote(Translator.getText("IGUI_PlayerText_DoorBlocked"), 255, 255, 255, 256.0F);
                  }

               } else {
                  boolean var3 = this.open;
                  IsoDoor.toggleDoubleDoor(this, true);
                  if (var3 != this.open) {
                     this.playDoorSound(var1.getEmitter(), this.open ? "Open" : "Close");
                  }

               }
            } else if (this.isObstructed()) {
               if (var1 != null) {
                  this.playDoorSound(var1.getEmitter(), "Blocked");
                  var1.setHaloNote(Translator.getText("IGUI_PlayerText_DoorBlocked"), 255, 255, 255, 256.0F);
               }

            } else {
               this.sprite = this.closedSprite;
               this.open = !this.open;
               this.setLockedByKey(false);
               if (this.open) {
                  this.playDoorSound(var1.getEmitter(), "Open");
                  this.sprite = this.openSprite;
               } else {
                  this.playDoorSound(var1.getEmitter(), "Close");
               }

               this.square.RecalcProperties();
               this.syncIsoObject(false, (byte)(this.open ? 1 : 0), (UdpConnection)null, (ByteBuffer)null);
               LuaEventManager.triggerEvent("OnContainerUpdate");
            }
         }
      }
   }

   public void ToggleDoor(IsoGameCharacter var1) {
      this.ToggleDoorActual(var1);
   }

   public void ToggleDoorSilent() {
      if (!this.isBarricaded()) {
         this.square.InvalidateSpecialObjectPaths();

         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            LosUtil.cachecleared[var1] = true;
         }

         IsoGridSquare.setRecalcLightTime(-1);
         this.open = !this.open;
         this.sprite = this.closedSprite;
         if (this.open) {
            this.sprite = this.openSprite;
         }

      }
   }

   public boolean isObstructed() {
      return IsoDoor.isDoorObstructed(this);
   }

   public boolean haveSheetRope() {
      return IsoWindow.isTopOfSheetRopeHere(this.square, this.north);
   }

   public int countAddSheetRope() {
      return !this.isHoppable() && !this.isWindow() ? 0 : IsoWindow.countAddSheetRope(this.square, this.north);
   }

   public boolean canAddSheetRope() {
      return !this.isHoppable() && !this.isWindow() ? false : IsoWindow.canAddSheetRope(this.square, this.north);
   }

   public boolean addSheetRope(IsoPlayer var1, String var2) {
      return !this.canAddSheetRope() ? false : IsoWindow.addSheetRope(var1, this.square, this.north, var2);
   }

   public boolean removeSheetRope(IsoPlayer var1) {
      return this.haveSheetRope() ? IsoWindow.removeSheetRope(var1, this.square, this.north) : false;
   }

   public void createLightSource(int var1, int var2, int var3, int var4, int var5, String var6, InventoryItem var7, IsoGameCharacter var8) {
      this.setLightSourceXOffset(var2);
      this.setLightSourceYOffset(var3);
      this.setLightSourceRadius(var1);
      this.setLightSourceFuel(var6);
      if (var7 != null) {
         if (!(var7 instanceof DrainableComboItem)) {
            this.setLifeLeft(1.0F);
            this.setHaveFuel(true);
         } else {
            this.setLifeLeft(((DrainableComboItem)var7).getUsedDelta());
            this.setLifeDelta(((DrainableComboItem)var7).getUseDelta());
            this.setHaveFuel(!"Base.Torch".equals(var7.getFullType()) || ((DrainableComboItem)var7).getUsedDelta() > 0.0F);
         }

         var8.removeFromHands(var7);
         IsoWorldInventoryObject var9 = var7.getWorldItem();
         if (var9 != null) {
            if (var9.getSquare() != null) {
               var9.getSquare().transmitRemoveItemFromSquare(var9);
               LuaEventManager.triggerEvent("OnContainerUpdate");
            }
         } else if (var7.getContainer() != null) {
            var7.getContainer().Remove(var7);
         }
      }

      this.setLightSourceOn(this.haveFuel);
      if (this.lightSource != null) {
         this.lightSource.setActive(this.isLightSourceOn());
      }

   }

   public InventoryItem insertNewFuel(InventoryItem var1, IsoGameCharacter var2) {
      if (var1 != null) {
         InventoryItem var3 = this.removeCurrentFuel(var2);
         if (var2 != null) {
            var2.removeFromHands(var1);
            var2.getInventory().Remove(var1);
         }

         if (var1 instanceof DrainableComboItem) {
            this.setLifeLeft(((DrainableComboItem)var1).getUsedDelta());
            this.setLifeDelta(((DrainableComboItem)var1).getUseDelta());
         } else {
            this.setLifeLeft(1.0F);
         }

         this.setHaveFuel(true);
         this.toggleLightSource(true);
         return var3;
      } else {
         return null;
      }
   }

   public InventoryItem removeCurrentFuel(IsoGameCharacter var1) {
      if (this.haveFuel()) {
         InventoryItem var2 = InventoryItemFactory.CreateItem(this.getLightSourceFuel());
         if (var2 instanceof DrainableComboItem) {
            ((DrainableComboItem)var2).setUsedDelta(this.getLifeLeft());
         }

         if (var1 != null) {
            var1.getInventory().AddItem(var2);
         }

         this.setLifeLeft(0.0F);
         this.setLifeDelta(-1.0F);
         this.toggleLightSource(false);
         this.setHaveFuel(false);
         return var2;
      } else {
         return null;
      }
   }

   private int calcLightSourceX() {
      int var1 = (int)this.getX();
      int var2 = (int)this.getY();
      if (this.lightSourceXOffset != 0) {
         for(int var3 = 1; var3 <= Math.abs(this.lightSourceXOffset); ++var3) {
            int var4 = this.lightSourceXOffset > 0 ? 1 : -1;
            LosUtil.TestResults var5 = LosUtil.lineClear(this.getCell(), (int)this.getX(), (int)this.getY(), (int)this.getZ(), var1 + var4, var2, (int)this.getZ(), false);
            if (var5 == LosUtil.TestResults.Blocked || var5 == LosUtil.TestResults.ClearThroughWindow) {
               break;
            }

            var1 += var4;
         }
      }

      return var1;
   }

   private int calcLightSourceY() {
      int var1 = (int)this.getX();
      int var2 = (int)this.getY();
      if (this.lightSourceYOffset != 0) {
         for(int var3 = 1; var3 <= Math.abs(this.lightSourceYOffset); ++var3) {
            int var4 = this.lightSourceYOffset > 0 ? 1 : -1;
            LosUtil.TestResults var5 = LosUtil.lineClear(this.getCell(), (int)this.getX(), (int)this.getY(), (int)this.getZ(), var1, var2 + var4, (int)this.getZ(), false);
            if (var5 == LosUtil.TestResults.Blocked || var5 == LosUtil.TestResults.ClearThroughWindow) {
               break;
            }

            var2 += var4;
         }
      }

      return var2;
   }

   public void update() {
      if (this.getObjectIndex() != -1) {
         int var3;
         if (!GameServer.bServer) {
            if (this.lightSource != null && !this.lightSource.isInBounds()) {
               this.lightSource = null;
            }

            byte var1;
            int var2;
            int var4;
            if (this.lightSourceFuel != null && !this.lightSourceFuel.isEmpty() && this.lightSource == null && this.square != null) {
               var1 = 0;
               var2 = this.calcLightSourceX();
               var3 = this.calcLightSourceY();
               if (IsoWorld.instance.CurrentCell.isInChunkMap(var2, var3)) {
                  var4 = this.getLightSourceLife();
                  this.setLightSource(new IsoLightSource(var2, var3, (int)this.getZ() + var1, 1.0F, 1.0F, 1.0F, this.lightSourceRadius, var4 > 0 ? var4 : -1));
                  this.lightSource.setActive(this.isLightSourceOn());
                  IsoWorld.instance.getCell().getLamppostPositions().add(this.getLightSource());
               }
            }

            if (this.lightSource != null && this.lightSource.isActive()) {
               var1 = 0;
               var2 = this.calcLightSourceX();
               var3 = this.calcLightSourceY();
               if (var2 != this.lightSource.x || var3 != this.lightSource.y) {
                  this.getCell().removeLamppost(this.lightSource);
                  var4 = this.getLightSourceLife();
                  this.setLightSource(new IsoLightSource(var2, var3, (int)this.getZ() + var1, 1.0F, 1.0F, 1.0F, this.lightSourceRadius, var4 > 0 ? var4 : -1));
                  this.lightSource.setActive(this.isLightSourceOn());
                  IsoWorld.instance.getCell().getLamppostPositions().add(this.getLightSource());
               }
            }
         }

         if (this.getLifeLeft() > -1.0F) {
            float var5 = (float)GameTime.getInstance().getWorldAgeHours();
            if (this.lastUpdateHours == -1.0F) {
               this.lastUpdateHours = var5;
            } else if (this.lastUpdateHours > var5) {
               this.lastUpdateHours = var5;
            }

            float var6 = var5 - this.lastUpdateHours;
            this.lastUpdateHours = var5;
            if (this.isLightSourceOn()) {
               this.updateAccumulator += var6;
               var3 = (int)Math.floor((double)(this.updateAccumulator / 0.004166667F));
               if (var3 > 0) {
                  this.updateAccumulator -= 0.004166667F * (float)var3;
                  this.setLifeLeft(this.getLifeLeft() - this.getLifeDelta() * (float)var3);
                  if (this.getLifeLeft() <= 0.0F) {
                     this.setLifeLeft(0.0F);
                     this.toggleLightSource(false);
                  }
               }
            } else {
               this.updateAccumulator = 0.0F;
            }
         }

         this.checkHaveElectricity();
      }
   }

   void Damage(int var1) {
      if (this.isThumpable()) {
         this.DirtySlice();
         this.Health -= var1;
      }
   }

   public void destroy() {
      if (!this.destroyed) {
         if (this.getObjectIndex() != -1) {
            if (GameClient.bClient) {
               GameClient.instance.sendClientCommandV((IsoPlayer)null, "object", "OnDestroyIsoThumpable", "x", this.square.getX(), "y", this.square.getY(), "z", this.square.getZ(), "index", this.getObjectIndex());
            }

            LuaEventManager.triggerEvent("OnDestroyIsoThumpable", this, (Object)null);
            this.Health = 0;
            this.destroyed = true;
            if (this.getObjectIndex() != -1) {
               this.square.transmitRemoveItemFromSquare(this);
            }

         }
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
      return this.canBarricade;
   }

   public IsoBarricade getBarricadeForCharacter(IsoGameCharacter var1) {
      return IsoBarricade.GetBarricadeForCharacter(this, var1);
   }

   public IsoBarricade getBarricadeOppositeCharacter(IsoGameCharacter var1) {
      return IsoBarricade.GetBarricadeOppositeCharacter(this, var1);
   }

   public void setIsDoor(Boolean var1) {
      this.isDoor = var1;
   }

   public KahluaTable getTable() {
      return this.table;
   }

   public void setTable(KahluaTable var1) {
      this.table = var1;
   }

   public boolean canBePlastered() {
      return this.canBePlastered;
   }

   public void setCanBePlastered(boolean var1) {
      this.canBePlastered = var1;
   }

   public boolean isPaintable() {
      return this.paintable;
   }

   public void setPaintable(boolean var1) {
      this.paintable = var1;
   }

   public boolean isLocked() {
      return this.Locked;
   }

   public void setIsLocked(boolean var1) {
      this.Locked = var1;
   }

   public boolean isThumpable() {
      return this.isBarricaded() ? true : this.isThumpable;
   }

   public void setIsThumpable(boolean var1) {
      this.isThumpable = var1;
   }

   public void setIsHoppable(boolean var1) {
      this.setHoppable(var1);
   }

   public IsoSprite getOpenSprite() {
      return this.openSprite;
   }

   public boolean isHoppable() {
      if (this.isDoor() && !this.IsOpen() && this.closedSprite != null) {
         PropertyContainer var1 = this.closedSprite.getProperties();
         return var1.Is(IsoFlagType.HoppableN) || var1.Is(IsoFlagType.HoppableW);
      } else {
         return this.sprite == null || !this.sprite.getProperties().Is(IsoFlagType.HoppableN) && !this.sprite.getProperties().Is(IsoFlagType.HoppableW) ? this.isHoppable : true;
      }
   }

   public void setHoppable(boolean var1) {
      this.isHoppable = var1;
   }

   public int getLightSourceRadius() {
      return this.lightSourceRadius;
   }

   public void setLightSourceRadius(int var1) {
      this.lightSourceRadius = var1;
   }

   public int getLightSourceXOffset() {
      return this.lightSourceXOffset;
   }

   public void setLightSourceXOffset(int var1) {
      this.lightSourceXOffset = var1;
   }

   public int getLightSourceYOffset() {
      return this.lightSourceYOffset;
   }

   public void setLightSourceYOffset(int var1) {
      this.lightSourceYOffset = var1;
   }

   public int getLightSourceLife() {
      return this.lightSourceLife;
   }

   public void setLightSourceLife(int var1) {
      this.lightSourceLife = var1;
   }

   public boolean isLightSourceOn() {
      return this.lightSourceOn;
   }

   public void setLightSourceOn(boolean var1) {
      this.lightSourceOn = var1;
   }

   public IsoLightSource getLightSource() {
      return this.lightSource;
   }

   public void setLightSource(IsoLightSource var1) {
      this.lightSource = var1;
   }

   public void toggleLightSource(boolean var1) {
      this.setLightSourceOn(var1);
      if (this.lightSource != null) {
         this.getLightSource().setActive(var1);
         IsoGridSquare.setRecalcLightTime(-1);
         GameTime.instance.lightSourceUpdate = 100.0F;
      }
   }

   public String getLightSourceFuel() {
      return this.lightSourceFuel;
   }

   public void setLightSourceFuel(String var1) {
      if (var1 != null && var1.isEmpty()) {
         var1 = null;
      }

      this.lightSourceFuel = var1;
   }

   public float getLifeLeft() {
      return this.lifeLeft;
   }

   public void setLifeLeft(float var1) {
      this.lifeLeft = var1;
   }

   public float getLifeDelta() {
      return this.lifeDelta;
   }

   public void setLifeDelta(float var1) {
      this.lifeDelta = var1;
   }

   public boolean haveFuel() {
      return this.haveFuel;
   }

   public void setHaveFuel(boolean var1) {
      this.haveFuel = var1;
   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      byte var2 = (byte)this.square.getObjects().indexOf(this);
      var1.putByte(var2);
      var1.putByte((byte)1);
      if (this.getSprite() != null && this.getSprite().getProperties().Is("DoubleDoor")) {
         var1.putByte((byte)5);
      } else {
         if (this.open) {
            var1.putByte((byte)1);
         } else if (this.lockedByKey) {
            var1.putByte((byte)3);
         } else {
            var1.putByte((byte)4);
         }

      }
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      if (this.square == null) {
         System.out.println("ERROR: " + this.getClass().getSimpleName() + " square is null");
      } else if (this.getObjectIndex() == -1) {
         PrintStream var10000 = System.out;
         String var10001 = this.getClass().getSimpleName();
         var10000.println("ERROR: " + var10001 + " not found on square " + this.square.getX() + "," + this.square.getY() + "," + this.square.getZ());
      } else if (this.isDoor()) {
         short var5 = -1;
         if ((GameServer.bServer || GameClient.bClient) && var4 != null) {
            var5 = var4.getShort();
         }

         if (GameClient.bClient && !var1) {
            var5 = IsoPlayer.getInstance().getOnlineID();
            ByteBufferWriter var11 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SyncIsoObject.doPacket(var11);
            this.syncIsoObjectSend(var11);
            var11.putShort(var5);
            PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
         } else {
            UdpConnection var7;
            ByteBufferWriter var8;
            Iterator var9;
            if (GameServer.bServer && !var1) {
               var9 = GameServer.udpEngine.connections.iterator();

               while(var9.hasNext()) {
                  var7 = (UdpConnection)var9.next();
                  var8 = var7.startPacket();
                  PacketTypes.PacketType.SyncIsoObject.doPacket(var8);
                  this.syncIsoObjectSend(var8);
                  var8.putShort(var5);
                  PacketTypes.PacketType.SyncIsoObject.send(var7);
               }
            } else if (var1) {
               if (GameClient.bClient && var5 != -1) {
                  IsoPlayer var6 = (IsoPlayer)GameClient.IDToPlayerMap.get(var5);
                  if (var6 != null) {
                     var6.networkAI.setNoCollision(1000L);
                  }
               }

               if (var2 == 1) {
                  this.open = true;
                  this.sprite = this.openSprite;
                  this.Locked = false;
               } else if (var2 == 0) {
                  this.open = false;
                  this.sprite = this.closedSprite;
               } else if (var2 == 3) {
                  this.Locked = true;
                  this.lockedByKey = true;
                  this.open = false;
                  this.sprite = this.closedSprite;
               } else if (var2 == 4) {
                  this.Locked = false;
                  this.lockedByKey = false;
                  this.open = false;
                  this.sprite = this.closedSprite;
               }

               if (GameServer.bServer) {
                  var9 = GameServer.udpEngine.connections.iterator();

                  while(var9.hasNext()) {
                     var7 = (UdpConnection)var9.next();
                     if (var3 != null && var7.getConnectedGUID() != var3.getConnectedGUID()) {
                        var8 = var7.startPacket();
                        PacketTypes.PacketType.SyncIsoObject.doPacket(var8);
                        this.syncIsoObjectSend(var8);
                        var8.putShort(var5);
                        PacketTypes.PacketType.SyncIsoObject.send(var7);
                     }
                  }
               }

               if (var2 == 5) {
                  IsoDoor.toggleDoubleDoor(this, false);
                  if (GameServer.bServer) {
                     ServerMap.instance.physicsCheck(this.square.getX(), this.square.getY());
                  }
               }

               this.square.InvalidateSpecialObjectPaths();
               this.square.RecalcProperties();
               this.square.RecalcAllWithNeighbours(true);

               for(int var10 = 0; var10 < IsoPlayer.numPlayers; ++var10) {
                  LosUtil.cachecleared[var10] = true;
               }

               IsoGridSquare.setRecalcLightTime(-1);
               GameTime.instance.lightSourceUpdate = 100.0F;
               LuaEventManager.triggerEvent("OnContainerUpdate");
            }
         }

      }
   }

   public void addToWorld() {
      super.addToWorld();
      this.getCell().addToProcessIsoObject(this);
   }

   public void removeFromWorld() {
      if (this.lightSource != null) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.lightSource);
      }

      super.removeFromWorld();
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      super.saveChange(var1, var2, var3);
      if ("lightSource".equals(var1)) {
         var3.put((byte)(this.lightSourceOn ? 1 : 0));
         var3.put((byte)(this.haveFuel ? 1 : 0));
         var3.putFloat(this.lifeLeft);
         var3.putFloat(this.lifeDelta);
      } else if ("paintable".equals(var1)) {
         var3.put((byte)(this.isPaintable() ? 1 : 0));
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      super.loadChange(var1, var2);
      if ("lightSource".equals(var1)) {
         boolean var3 = var2.get() == 1;
         this.haveFuel = var2.get() == 1;
         this.lifeLeft = var2.getFloat();
         this.lifeDelta = var2.getFloat();
         if (var3 != this.lightSourceOn) {
            this.toggleLightSource(var3);
         }
      } else if ("paintable".equals(var1)) {
         this.setPaintable(var2.get() == 1);
      }

   }

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

   public IsoGridSquare getInsideSquare() {
      return this.north ? this.square.getCell().getGridSquare(this.square.getX(), this.square.getY() - 1, this.square.getZ()) : this.square.getCell().getGridSquare(this.square.getX() - 1, this.square.getY(), this.square.getZ());
   }

   public IsoGridSquare getOppositeSquare() {
      return this.getInsideSquare();
   }

   public boolean isAdjacentToSquare(IsoGridSquare var1) {
      IsoGridSquare var2 = this.getSquare();
      if (var2 != null && var1 != null) {
         int var3 = var2.x - var1.x;
         int var4 = var2.y - var1.y;
         int var5 = var2.x;
         int var6 = var2.x;
         int var7 = var2.y;
         int var8 = var2.y;
         IsoGridSquare var9 = var2;
         switch(this.getSpriteEdge(false)) {
         case N:
            --var5;
            ++var6;
            --var7;
            if (var4 == 1) {
               var9 = var2.getAdjacentSquare(IsoDirections.N);
            }
            break;
         case S:
            --var5;
            ++var6;
            ++var8;
            if (var4 == -1) {
               var9 = var2.getAdjacentSquare(IsoDirections.S);
            }
            break;
         case W:
            --var7;
            ++var8;
            --var5;
            if (var3 == 1) {
               var9 = var2.getAdjacentSquare(IsoDirections.W);
            }
            break;
         case E:
            --var7;
            ++var8;
            ++var6;
            if (var3 == -1) {
               var9 = var2.getAdjacentSquare(IsoDirections.E);
            }
            break;
         default:
            return false;
         }

         if (var1.x >= var5 && var1.x <= var6 && var1.y >= var7 && var1.y <= var8) {
            return !var9.isSomethingTo(var1);
         } else {
            return false;
         }
      } else {
         return false;
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

   public void addSheet(IsoGameCharacter var1) {
      IsoGridSquare var2 = this.getIndoorSquare();
      IsoObjectType var3;
      if (this.north) {
         var3 = IsoObjectType.curtainN;
         if (var2 != this.square) {
            var3 = IsoObjectType.curtainS;
         }
      } else {
         var3 = IsoObjectType.curtainW;
         if (var2 != this.square) {
            var3 = IsoObjectType.curtainE;
         }
      }

      if (var1 != null) {
         if (this.north) {
            if (var1.getY() < this.getY()) {
               var2 = this.getCell().getGridSquare((double)this.getX(), (double)(this.getY() - 1.0F), (double)this.getZ());
               var3 = IsoObjectType.curtainS;
            } else {
               var2 = this.getSquare();
               var3 = IsoObjectType.curtainN;
            }
         } else if (var1.getX() < this.getX()) {
            var2 = this.getCell().getGridSquare((double)(this.getX() - 1.0F), (double)this.getY(), (double)this.getZ());
            var3 = IsoObjectType.curtainE;
         } else {
            var2 = this.getSquare();
            var3 = IsoObjectType.curtainW;
         }
      }

      if (var2 != null) {
         if (var2.getCurtain(var3) == null) {
            if (var2 != null) {
               int var4 = 16;
               if (var3 == IsoObjectType.curtainE) {
                  ++var4;
               }

               if (var3 == IsoObjectType.curtainS) {
                  var4 += 3;
               }

               if (var3 == IsoObjectType.curtainN) {
                  var4 += 2;
               }

               var4 += 4;
               IsoCurtain var5 = new IsoCurtain(this.getCell(), var2, "fixtures_windows_curtains_01_" + var4, this.north);
               var2.AddSpecialTileObject(var5);
               if (GameServer.bServer) {
                  var5.transmitCompleteItemToClients();
                  var1.sendObjectChange("removeOneOf", new Object[]{"type", "Sheet"});
               } else {
                  var1.getInventory().RemoveOneOf("Sheet");
               }
            }

         }
      }
   }

   public IsoGridSquare getIndoorSquare() {
      if (this.square.getRoom() != null) {
         return this.square;
      } else {
         IsoGridSquare var1;
         if (this.north) {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX(), this.square.getY() - 1, this.square.getZ());
         } else {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.square.getX() - 1, this.square.getY(), this.square.getZ());
         }

         if (var1 != null && var1.getFloor() != null) {
            if (var1.getRoom() != null) {
               return var1;
            } else if (this.square.getFloor() == null) {
               return var1;
            } else {
               String var2 = var1.getFloor().getSprite().getName();
               return var2 != null && var2.startsWith("carpentry_02_") ? var1 : this.square;
            }
         } else {
            return this.square;
         }
      }
   }

   public int getKeyId() {
      return this.keyId;
   }

   public void setKeyId(int var1, boolean var2) {
      if (var2 && this.keyId != var1 && GameClient.bClient) {
         this.keyId = var1;
         this.syncIsoThumpable();
      } else {
         this.keyId = var1;
      }

   }

   public void setKeyId(int var1) {
      this.setKeyId(var1, true);
   }

   public boolean isLockedByKey() {
      return this.lockedByKey;
   }

   public void setLockedByKey(boolean var1) {
      boolean var2 = var1 != this.lockedByKey;
      this.lockedByKey = var1;
      this.setIsLocked(var1);
      if (!GameServer.bServer && var2) {
         if (var1) {
            this.syncIsoObject(false, (byte)3, (UdpConnection)null, (ByteBuffer)null);
         } else {
            this.syncIsoObject(false, (byte)4, (UdpConnection)null, (ByteBuffer)null);
         }
      }

   }

   public boolean isLockedByPadlock() {
      return this.lockedByPadlock;
   }

   public void syncIsoThumpable() {
      ByteBufferWriter var1 = GameClient.connection.startPacket();
      PacketTypes.PacketType.SyncThumpable.doPacket(var1);
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      byte var2 = (byte)this.square.getObjects().indexOf(this);
      if (var2 == -1) {
         PrintStream var10000 = System.out;
         int var10001 = this.square.getX();
         var10000.println("ERROR: Thumpable door not found on square " + var10001 + ", " + this.square.getY() + ", " + this.square.getZ());
         GameClient.connection.cancelPacket();
      } else {
         var1.putByte(var2);
         var1.putInt(this.getLockedByCode());
         var1.putByte((byte)(this.lockedByPadlock ? 1 : 0));
         var1.putInt(this.getKeyId());
         PacketTypes.PacketType.SyncThumpable.send(GameClient.connection);
      }
   }

   public void setLockedByPadlock(boolean var1) {
      if (this.lockedByPadlock != var1 && GameClient.bClient) {
         this.lockedByPadlock = var1;
         this.syncIsoThumpable();
      } else {
         this.lockedByPadlock = var1;
      }

   }

   public boolean canBeLockByPadlock() {
      return this.canBeLockByPadlock;
   }

   public void setCanBeLockByPadlock(boolean var1) {
      this.canBeLockByPadlock = var1;
   }

   public int getLockedByCode() {
      return this.lockedByCode;
   }

   public void setLockedByCode(int var1) {
      if (this.lockedByCode != var1 && GameClient.bClient) {
         this.lockedByCode = var1;
         this.syncIsoThumpable();
      } else {
         this.lockedByCode = var1;
      }

   }

   public boolean isLockedToCharacter(IsoGameCharacter var1) {
      if (GameClient.bClient && var1 instanceof IsoPlayer && !((IsoPlayer)var1).accessLevel.equals("")) {
         return false;
      } else if (this.getLockedByCode() > 0) {
         return true;
      } else {
         return this.isLockedByPadlock() && (var1.getInventory() == null || var1.getInventory().haveThisKeyId(this.getKeyId()) == null);
      }
   }

   public boolean canClimbOver(IsoGameCharacter var1) {
      if (this.square == null) {
         return false;
      } else if (!this.isHoppable()) {
         return false;
      } else {
         return var1 == null || IsoWindow.canClimbThroughHelper(var1, this.getSquare(), this.getOppositeSquare(), this.north);
      }
   }

   public boolean canClimbThrough(IsoGameCharacter var1) {
      if (this.square == null) {
         return false;
      } else if (!this.isWindow()) {
         return false;
      } else if (this.isBarricaded()) {
         return false;
      } else {
         return var1 == null || IsoWindow.canClimbThroughHelper(var1, this.getSquare(), this.getOppositeSquare(), this.north);
      }
   }

   public String getThumpSound() {
      return this.thumpSound;
   }

   public void setThumpSound(String var1) {
      this.thumpSound = var1;
   }

   public IsoObject getRenderEffectMaster() {
      int var1 = IsoDoor.getDoubleDoorIndex(this);
      IsoObject var2;
      if (var1 != -1) {
         var2 = null;
         if (var1 == 2) {
            var2 = IsoDoor.getDoubleDoorObject(this, 1);
         } else if (var1 == 3) {
            var2 = IsoDoor.getDoubleDoorObject(this, 4);
         }

         if (var2 != null) {
            return var2;
         }
      } else {
         var2 = IsoDoor.getGarageDoorFirst(this);
         if (var2 != null) {
            return var2;
         }
      }

      return this;
   }

   public IsoDirections getSpriteEdge(boolean var1) {
      if (!this.isDoor() && !this.isWindow()) {
         return null;
      } else if (this.open && !var1) {
         PropertyContainer var2 = this.getProperties();
         if (var2 != null && var2.Is(IsoFlagType.attachedE)) {
            return IsoDirections.E;
         } else if (var2 != null && var2.Is(IsoFlagType.attachedS)) {
            return IsoDirections.S;
         } else {
            return this.north ? IsoDirections.W : IsoDirections.N;
         }
      } else {
         return this.north ? IsoDirections.N : IsoDirections.W;
      }
   }

   private String getSoundPrefix() {
      if (this.closedSprite == null) {
         return "WoodDoor";
      } else {
         PropertyContainer var1 = this.closedSprite.getProperties();
         return var1.Is("DoorSound") ? var1.Val("DoorSound") : "WoodDoor";
      }
   }

   private void playDoorSound(BaseCharacterSoundEmitter var1, String var2) {
      var1.playSound(this.getSoundPrefix() + var2, this);
   }
}
