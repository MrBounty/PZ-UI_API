package zombie.iso;

import fmod.fmod.Audio;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.joml.Vector2f;
import zombie.CollisionManager;
import zombie.GameTime;
import zombie.MovingObjectUpdateScheduler;
import zombie.SoundManager;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.State;
import zombie.ai.astar.Mover;
import zombie.ai.states.AttackState;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.CollideWithWallState;
import zombie.ai.states.CrawlingZombieTurnState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.WalkTowardState;
import zombie.ai.states.ZombieFallDownState;
import zombie.ai.states.ZombieIdleState;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.textures.ColorInfo;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.WeaponType;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.isoregion.regions.IWorldRegion;
import zombie.iso.objects.IsoMolotovCocktail;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.RenderEffectType;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.popman.ZombiePopulationManager;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;

public class IsoMovingObject extends IsoObject implements Mover {
   public static IsoMovingObject.TreeSoundManager treeSoundMgr = new IsoMovingObject.TreeSoundManager();
   public static final int MAX_ZOMBIES_EATING = 3;
   private static int IDCount = 0;
   private static final Vector2 tempo = new Vector2();
   public boolean noDamage = false;
   public IsoGridSquare last = null;
   public float lx;
   public float ly;
   public float lz;
   public float nx;
   public float ny;
   public float x;
   public float y;
   public float z;
   public IsoSpriteInstance def = null;
   protected IsoGridSquare current = null;
   protected Vector2 hitDir = new Vector2();
   protected int ID = 0;
   protected IsoGridSquare movingSq = null;
   protected boolean solid = true;
   protected float width = 0.24F;
   protected boolean shootable = true;
   protected boolean Collidable = true;
   protected float scriptnx = 0.0F;
   protected float scriptny = 0.0F;
   protected String ScriptModule = "none";
   protected Vector2 movementLastFrame = new Vector2();
   protected float weight = 1.0F;
   boolean bOnFloor = false;
   private boolean closeKilled = false;
   private String collideType = null;
   private float lastCollideTime = 0.0F;
   private int TimeSinceZombieAttack = 1000000;
   private boolean collidedE = false;
   private boolean collidedN = false;
   private IsoObject CollidedObject = null;
   private boolean collidedS = false;
   private boolean collidedThisFrame = false;
   private boolean collidedW = false;
   private boolean CollidedWithDoor = false;
   private boolean collidedWithVehicle = false;
   private boolean destroyed = false;
   private boolean firstUpdate = true;
   private float impulsex = 0.0F;
   private float impulsey = 0.0F;
   private float limpulsex = 0.0F;
   private float limpulsey = 0.0F;
   private float hitForce = 0.0F;
   private float hitFromAngle;
   private int PathFindIndex = -1;
   private float StateEventDelayTimer = 0.0F;
   private Thumpable thumpTarget = null;
   private boolean bAltCollide = false;
   private IsoZombie lastTargettedBy = null;
   private float feelersize = 0.5F;
   public final boolean[] bOutline = new boolean[4];
   public final ColorInfo[] outlineColor = new ColorInfo[4];
   private final ArrayList eatingZombies = new ArrayList();
   private boolean zombiesDontAttack = false;

   public IsoMovingObject(IsoCell var1) {
      this.sprite = IsoSprite.CreateSprite(IsoSpriteManager.instance);
      if (var1 != null) {
         this.ID = IDCount++;
         if (this.getCell().isSafeToAdd()) {
            this.getCell().getObjectList().add(this);
         } else {
            this.getCell().getAddList().add(this);
         }

      }
   }

   public IsoMovingObject(IsoCell var1, boolean var2) {
      this.ID = IDCount++;
      this.sprite = IsoSprite.CreateSprite(IsoSpriteManager.instance);
      if (var2) {
         if (this.getCell().isSafeToAdd()) {
            this.getCell().getObjectList().add(this);
         } else {
            this.getCell().getAddList().add(this);
         }
      }

   }

   public IsoMovingObject(IsoCell var1, IsoGridSquare var2, IsoSprite var3, boolean var4) {
      this.ID = IDCount++;
      this.sprite = var3;
      if (var4) {
         if (this.getCell().isSafeToAdd()) {
            this.getCell().getObjectList().add(this);
         } else {
            this.getCell().getAddList().add(this);
         }
      }

   }

   public IsoMovingObject() {
      this.ID = IDCount++;
      this.getCell().getAddList().add(this);
   }

   public static int getIDCount() {
      return IDCount;
   }

   public static void setIDCount(int var0) {
      IDCount = var0;
   }

   public IsoBuilding getBuilding() {
      if (this.current == null) {
         return null;
      } else {
         IsoRoom var1 = this.current.getRoom();
         return var1 == null ? null : var1.building;
      }
   }

   public IWorldRegion getMasterRegion() {
      return this.current != null ? this.current.getIsoWorldRegion() : null;
   }

   public float getWeight() {
      return this.weight;
   }

   public void setWeight(float var1) {
      this.weight = var1;
   }

   public float getWeight(float var1, float var2) {
      return this.weight;
   }

   public void onMouseRightClick(int var1, int var2) {
      if (this.square.getZ() == (int)IsoPlayer.getInstance().getZ() && this.DistToProper(IsoPlayer.getInstance()) <= 2.0F) {
         IsoPlayer.getInstance().setDragObject(this);
      }

   }

   public String getObjectName() {
      return "IsoMovingObject";
   }

   public void onMouseRightReleased() {
   }

   public void collideWith(IsoObject var1) {
      if (this instanceof IsoGameCharacter && var1 instanceof IsoGameCharacter) {
         LuaEventManager.triggerEvent("OnCharacterCollide", this, var1);
      } else {
         LuaEventManager.triggerEvent("OnObjectCollide", this, var1);
      }

   }

   public void doStairs() {
      if (this.current != null) {
         if (this.last != null) {
            if (!(this instanceof IsoPhysicsObject)) {
               IsoGridSquare var1 = this.current;
               if (var1.z > 0 && (var1.Has(IsoObjectType.stairsTN) || var1.Has(IsoObjectType.stairsTW)) && this.z - (float)((int)this.z) < 0.1F) {
                  IsoGridSquare var2 = IsoWorld.instance.CurrentCell.getGridSquare(var1.x, var1.y, var1.z - 1);
                  if (var2 != null && (var2.Has(IsoObjectType.stairsTN) || var2.Has(IsoObjectType.stairsTW))) {
                     var1 = var2;
                  }
               }

               if (this instanceof IsoGameCharacter && (this.last.Has(IsoObjectType.stairsTN) || this.last.Has(IsoObjectType.stairsTW))) {
                  this.z = (float)Math.round(this.z);
               }

               float var4 = this.z;
               if (var1.HasStairs()) {
                  var4 = var1.getApparentZ(this.x - (float)var1.getX(), this.y - (float)var1.getY());
               }

               if (this instanceof IsoGameCharacter) {
                  State var3 = ((IsoGameCharacter)this).getCurrentState();
                  if (var3 == ClimbOverFenceState.instance() || var3 == ClimbThroughWindowState.instance()) {
                     if (var1.HasStairs() && this.z > var4) {
                        this.z = Math.max(var4, this.z - 0.075F * GameTime.getInstance().getMultiplier());
                     }

                     return;
                  }
               }

               if (Math.abs(var4 - this.z) < 0.95F) {
                  this.z = var4;
               }

            }
         }
      }
   }

   public int getID() {
      return this.ID;
   }

   public void setID(int var1) {
      this.ID = var1;
   }

   public int getPathFindIndex() {
      return this.PathFindIndex;
   }

   public void setPathFindIndex(int var1) {
      this.PathFindIndex = var1;
   }

   public float getScreenX() {
      return IsoUtils.XToScreen(this.x, this.y, this.z, 0);
   }

   public float getScreenY() {
      return IsoUtils.YToScreen(this.x, this.y, this.z, 0);
   }

   public Thumpable getThumpTarget() {
      return this.thumpTarget;
   }

   public void setThumpTarget(Thumpable var1) {
      this.thumpTarget = var1;
   }

   public Vector2 getVectorFromDirection(Vector2 var1) {
      return getVectorFromDirection(var1, this.dir);
   }

   public static Vector2 getVectorFromDirection(Vector2 var0, IsoDirections var1) {
      if (var0 == null) {
         DebugLog.General.warn("Supplied vector2 is null. Cannot be processed. Using fail-safe fallback.");
         var0 = new Vector2();
      }

      var0.x = 0.0F;
      var0.y = 0.0F;
      switch(var1) {
      case S:
         var0.x = 0.0F;
         var0.y = 1.0F;
         break;
      case N:
         var0.x = 0.0F;
         var0.y = -1.0F;
         break;
      case E:
         var0.x = 1.0F;
         var0.y = 0.0F;
         break;
      case W:
         var0.x = -1.0F;
         var0.y = 0.0F;
         break;
      case NW:
         var0.x = -1.0F;
         var0.y = -1.0F;
         break;
      case NE:
         var0.x = 1.0F;
         var0.y = -1.0F;
         break;
      case SW:
         var0.x = -1.0F;
         var0.y = 1.0F;
         break;
      case SE:
         var0.x = 1.0F;
         var0.y = 1.0F;
      }

      var0.normalize();
      return var0;
   }

   public Vector3 getPosition(Vector3 var1) {
      var1.set(this.getX(), this.getY(), this.getZ());
      return var1;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float var1) {
      this.x = var1;
      this.nx = var1;
      this.scriptnx = var1;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float var1) {
      this.y = var1;
      this.ny = var1;
      this.scriptny = var1;
   }

   public float getZ() {
      return this.z;
   }

   public void setZ(float var1) {
      this.z = var1;
      this.lz = var1;
   }

   public IsoGridSquare getSquare() {
      return this.current != null ? this.current : this.square;
   }

   public IsoBuilding getCurrentBuilding() {
      if (this.current == null) {
         return null;
      } else {
         return this.current.getRoom() == null ? null : this.current.getRoom().building;
      }
   }

   public float Hit(HandWeapon var1, IsoGameCharacter var2, float var3, boolean var4, float var5) {
      return 0.0F;
   }

   public void Move(Vector2 var1) {
      this.nx += var1.x * GameTime.instance.getMultiplier();
      this.ny += var1.y * GameTime.instance.getMultiplier();
      if (this instanceof IsoPlayer) {
         this.current = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)this.y, (double)((int)this.z));
      }

   }

   public void MoveUnmodded(Vector2 var1) {
      this.nx += var1.x;
      this.ny += var1.y;
      if (this instanceof IsoPlayer) {
         this.current = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)this.y, (double)((int)this.z));
      }

   }

   public boolean isCharacter() {
      return this instanceof IsoGameCharacter;
   }

   public float DistTo(int var1, int var2) {
      return IsoUtils.DistanceManhatten((float)var1, (float)var2, this.x, this.y);
   }

   public float DistTo(IsoMovingObject var1) {
      return IsoUtils.DistanceManhatten(this.x, this.y, var1.x, var1.y);
   }

   public float DistToProper(IsoObject var1) {
      return IsoUtils.DistanceTo(this.x, this.y, var1.getX(), var1.getY());
   }

   public float DistToSquared(IsoMovingObject var1) {
      return IsoUtils.DistanceToSquared(this.x, this.y, var1.x, var1.y);
   }

   public float DistToSquared(float var1, float var2) {
      return IsoUtils.DistanceToSquared(var1, var2, this.x, this.y);
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      float var4 = var1.getFloat();
      float var5 = var1.getFloat();
      this.x = this.lx = this.nx = this.scriptnx = var1.getFloat() + (float)(IsoWorld.saveoffsetx * 300);
      this.y = this.ly = this.ny = this.scriptny = var1.getFloat() + (float)(IsoWorld.saveoffsety * 300);
      this.z = this.lz = var1.getFloat();
      this.dir = IsoDirections.fromIndex(var1.getInt());
      if (var1.get() != 0) {
         if (this.table == null) {
            this.table = LuaManager.platform.newTable();
         }

         this.table.load(var1, var2);
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      var1.put((byte)(this.Serialize() ? 1 : 0));
      var1.put(IsoObject.factoryGetClassID(this.getObjectName()));
      var1.putFloat(this.offsetX);
      var1.putFloat(this.offsetY);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putFloat(this.z);
      var1.putInt(this.dir.index());
      if (this.table != null && !this.table.isEmpty()) {
         var1.put((byte)1);
         this.table.save(var1);
      } else {
         var1.put((byte)0);
      }

   }

   public void removeFromWorld() {
      IsoCell var1 = this.getCell();
      if (var1.isSafeToAdd()) {
         var1.getObjectList().remove(this);
         var1.getRemoveList().remove(this);
      } else {
         var1.getRemoveList().add(this);
      }

      var1.getAddList().remove(this);
      MovingObjectUpdateScheduler.instance.removeObject(this);
      super.removeFromWorld();
   }

   public void removeFromSquare() {
      if (this.current != null) {
         this.current.getMovingObjects().remove(this);
      }

      if (this.last != null) {
         this.last.getMovingObjects().remove(this);
      }

      if (this.movingSq != null) {
         this.movingSq.getMovingObjects().remove(this);
      }

      this.current = this.last = this.movingSq = null;
      if (this.square != null) {
         this.square.getStaticMovingObjects().remove(this);
      }

      super.removeFromSquare();
   }

   public IsoGridSquare getFuturWalkedSquare() {
      if (this.current != null) {
         IsoGridSquare var1 = this.getFeelerTile(this.feelersize);
         if (var1 != null && var1 != this.current) {
            return var1;
         }
      }

      return null;
   }

   public float getGlobalMovementMod() {
      return this.getGlobalMovementMod(true);
   }

   public float getGlobalMovementMod(boolean var1) {
      if (this.current != null && this.z - (float)((int)this.z) < 0.5F) {
         if (this.current.Has(IsoObjectType.tree) || this.current.getProperties() != null && this.current.getProperties().Is("Bush")) {
            if (var1) {
               this.doTreeNoises();
            }

            for(int var2 = 1; var2 < this.current.getObjects().size(); ++var2) {
               IsoObject var3 = (IsoObject)this.current.getObjects().get(var2);
               if (var3 instanceof IsoTree) {
                  var3.setRenderEffect(RenderEffectType.Vegetation_Rustle);
               } else if (var3.getProperties() != null && var3.getProperties().Is("Bush")) {
                  var3.setRenderEffect(RenderEffectType.Vegetation_Rustle);
               }
            }
         }

         IsoGridSquare var5 = this.getFeelerTile(this.feelersize);
         if (var5 != null && var5 != this.current && (var5.Has(IsoObjectType.tree) || var5.getProperties() != null && var5.getProperties().Is("Bush"))) {
            if (var1) {
               this.doTreeNoises();
            }

            for(int var6 = 1; var6 < var5.getObjects().size(); ++var6) {
               IsoObject var4 = (IsoObject)var5.getObjects().get(var6);
               if (var4 instanceof IsoTree) {
                  var4.setRenderEffect(RenderEffectType.Vegetation_Rustle);
               } else if (var4.getSprite() != null && var4.getProperties().Is("Bush")) {
                  var4.setRenderEffect(RenderEffectType.Vegetation_Rustle);
               }
            }
         }
      }

      return this.current != null && this.current.HasStairs() ? 0.75F : 1.0F;
   }

   private void doTreeNoises() {
      if (!GameServer.bServer) {
         if (!(this instanceof IsoPhysicsObject)) {
            if (this.current != null) {
               if (Rand.Next(Rand.AdjustForFramerate(50)) == 0) {
                  treeSoundMgr.addSquare(this.current);
               }
            }
         }
      }
   }

   public void postupdate() {
      this.slideAwayFromWalls();
      boolean var1;
      if (this instanceof IsoZombie && GameServer.bServer && ((IsoZombie)this).getStateMachine().getCurrent() != ZombieIdleState.instance()) {
         var1 = false;
      }

      if (this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         IsoPlayer.setInstance((IsoPlayer)this);
         IsoCamera.CamCharacter = (IsoPlayer)this;
      }

      this.ensureOnTile();
      if (this.lastTargettedBy != null && this.lastTargettedBy.isDead()) {
         this.lastTargettedBy = null;
      }

      if (this.lastTargettedBy != null && this.TimeSinceZombieAttack > 120) {
         this.lastTargettedBy = null;
      }

      ++this.TimeSinceZombieAttack;
      if (this instanceof IsoPlayer) {
         var1 = false;
         ((IsoPlayer)this).setLastCollidedW(this.collidedW);
         ((IsoPlayer)this).setLastCollidedN(this.collidedN);
         IsoPlayer var2 = (IsoPlayer)this;
      }

      if (!this.destroyed) {
         this.collidedThisFrame = false;
         this.collidedN = false;
         this.collidedS = false;
         this.collidedW = false;
         this.collidedE = false;
         this.CollidedWithDoor = false;
         this.last = this.current;
         this.CollidedObject = null;
         this.nx += this.impulsex;
         this.ny += this.impulsey;
         if (this.nx < 0.0F) {
            this.nx = 0.0F;
         }

         if (this.ny < 0.0F) {
            this.ny = 0.0F;
         }

         tempo.set(this.nx - this.x, this.ny - this.y);
         if (tempo.getLength() > 1.0F) {
            tempo.normalize();
            this.nx = this.x + tempo.getX();
            this.ny = this.y + tempo.getY();
         }

         this.impulsex = 0.0F;
         this.impulsey = 0.0F;
         if (this instanceof IsoZombie && (int)this.z == 0 && this.getCurrentBuilding() == null && !this.isInLoadedArea((int)this.nx, (int)this.ny) && (((IsoZombie)this).getCurrentState() == PathFindState.instance() || ((IsoZombie)this).getCurrentState() == WalkTowardState.instance())) {
            ZombiePopulationManager.instance.virtualizeZombie((IsoZombie)this);
         } else {
            float var12 = this.nx;
            float var13 = this.ny;
            this.collidedWithVehicle = false;
            if (this instanceof IsoGameCharacter && !this.isOnFloor() && ((IsoGameCharacter)this).getVehicle() == null && this.isCollidable() && (!(this instanceof IsoPlayer) || !((IsoPlayer)this).isNoClip())) {
               Vector2f var3 = PolygonalMap2.instance.resolveCollision((IsoGameCharacter)this, this.nx, this.ny, IsoMovingObject.L_postUpdate.vector2f);
               if (var3.x != this.nx || var3.y != this.ny) {
                  this.nx = var3.x;
                  this.ny = var3.y;
                  this.collidedWithVehicle = true;
               }
            }

            float var14 = this.nx;
            float var4 = this.ny;
            float var5 = 0.0F;
            boolean var6 = false;
            float var8;
            if (this.Collidable) {
               if (this.bAltCollide) {
                  this.DoCollide(2);
               } else {
                  this.DoCollide(1);
               }

               if (this.collidedN || this.collidedS) {
                  this.ny = this.ly;
                  this.DoCollideNorS();
               }

               if (this.collidedW || this.collidedE) {
                  this.nx = this.lx;
                  this.DoCollideWorE();
               }

               if (this.bAltCollide) {
                  this.DoCollide(1);
               } else {
                  this.DoCollide(2);
               }

               this.bAltCollide = !this.bAltCollide;
               if (this.collidedN || this.collidedS) {
                  this.ny = this.ly;
                  this.DoCollideNorS();
                  var6 = true;
               }

               if (this.collidedW || this.collidedE) {
                  this.nx = this.lx;
                  this.DoCollideWorE();
                  var6 = true;
               }

               var5 = Math.abs(this.nx - this.lx) + Math.abs(this.ny - this.ly);
               float var7 = this.nx;
               var8 = this.ny;
               this.nx = var14;
               this.ny = var4;
               if (this.Collidable && var6) {
                  if (this.bAltCollide) {
                     this.DoCollide(2);
                  } else {
                     this.DoCollide(1);
                  }

                  if (this.collidedN || this.collidedS) {
                     this.ny = this.ly;
                     this.DoCollideNorS();
                  }

                  if (this.collidedW || this.collidedE) {
                     this.nx = this.lx;
                     this.DoCollideWorE();
                  }

                  if (this.bAltCollide) {
                     this.DoCollide(1);
                  } else {
                     this.DoCollide(2);
                  }

                  if (this.collidedN || this.collidedS) {
                     this.ny = this.ly;
                     this.DoCollideNorS();
                     var6 = true;
                  }

                  if (this.collidedW || this.collidedE) {
                     this.nx = this.lx;
                     this.DoCollideWorE();
                     var6 = true;
                  }

                  if (Math.abs(this.nx - this.lx) + Math.abs(this.ny - this.ly) < var5) {
                     this.nx = var7;
                     this.ny = var8;
                  }
               }
            }

            if (this.collidedThisFrame) {
               this.current = this.last;
            }

            this.checkHitWall();
            IsoPlayer var15 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
            if (var15 != null && !var15.isCurrentState(CollideWithWallState.instance()) && !this.collidedN && !this.collidedS && !this.collidedW && !this.collidedE) {
               this.setCollideType((String)null);
            }

            var8 = this.nx - this.x;
            float var9 = this.ny - this.y;
            float var10 = !(Math.abs(var8) > 0.0F) && !(Math.abs(var9) > 0.0F) ? 0.0F : this.getGlobalMovementMod();
            if (Math.abs(var8) > 0.01F || Math.abs(var9) > 0.01F) {
               var8 *= var10;
               var9 *= var10;
            }

            this.x += var8;
            this.y += var9;
            this.doStairs();
            this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, (int)this.z);
            if (this.current == null) {
               for(int var11 = (int)this.z; var11 >= 0; --var11) {
                  this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, var11);
                  if (this.current != null) {
                     break;
                  }
               }

               if (this.current == null && this.last != null) {
                  this.current = this.last;
                  this.x = this.nx = this.scriptnx = (float)this.current.getX() + 0.5F;
                  this.y = this.ny = this.scriptny = (float)this.current.getY() + 0.5F;
               }
            }

            if (this.movingSq != null) {
               this.movingSq.getMovingObjects().remove(this);
               this.movingSq = null;
            }

            if (this.current != null && !this.current.getMovingObjects().contains(this)) {
               this.current.getMovingObjects().add(this);
               this.movingSq = this.current;
            }

            this.ensureOnTile();
            this.square = this.current;
            this.scriptnx = this.nx;
            this.scriptny = this.ny;
            this.firstUpdate = false;
         }
      }
   }

   public void ensureOnTile() {
      if (this.current == null) {
         if (!(this instanceof IsoPlayer)) {
            if (this instanceof IsoSurvivor) {
               IsoWorld.instance.CurrentCell.Remove(this);
               IsoWorld.instance.CurrentCell.getSurvivorList().remove(this);
            }

            return;
         }

         boolean var1 = true;
         boolean var2 = false;
         if (this.last != null && (this.last.Has(IsoObjectType.stairsTN) || this.last.Has(IsoObjectType.stairsTW))) {
            this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, (int)this.z + 1);
            var1 = false;
         }

         if (this.current == null) {
            this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, (int)this.z);
            return;
         }

         if (var1) {
            this.x = this.nx = this.scriptnx = (float)this.current.getX() + 0.5F;
            this.y = this.ny = this.scriptny = (float)this.current.getY() + 0.5F;
         }

         this.z = (float)this.current.getZ();
      }

   }

   public void preupdate() {
      this.nx = this.x;
      this.ny = this.y;
   }

   public void renderlast() {
      this.bOutline[IsoCamera.frameState.playerIndex] = false;
   }

   public void spotted(IsoMovingObject var1, boolean var2) {
   }

   public void update() {
      if (this.def == null) {
         this.def = IsoSpriteInstance.get(this.sprite);
      }

      this.movementLastFrame.x = this.x - this.lx;
      this.movementLastFrame.y = this.y - this.ly;
      this.lx = this.x;
      this.ly = this.y;
      this.lz = this.z;
      this.square = this.current;
      if (this.sprite != null) {
         this.sprite.update(this.def);
      }

      this.StateEventDelayTimer -= GameTime.instance.getMultiplier();
   }

   private void Collided() {
      this.collidedThisFrame = true;
   }

   public int compareToY(IsoMovingObject var1) {
      if (this.sprite == null && var1.sprite == null) {
         return 0;
      } else if (this.sprite != null && var1.sprite == null) {
         return -1;
      } else if (this.sprite == null) {
         return 1;
      } else {
         float var2 = IsoUtils.YToScreen(this.x, this.y, this.z, 0);
         float var3 = IsoUtils.YToScreen(var1.x, var1.y, var1.z, 0);
         if ((double)var2 > (double)var3) {
            return 1;
         } else {
            return (double)var2 < (double)var3 ? -1 : 0;
         }
      }
   }

   public float distToNearestCamCharacter() {
      float var1 = Float.MAX_VALUE;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         IsoPlayer var3 = IsoPlayer.players[var2];
         if (var3 != null) {
            var1 = Math.min(var1, this.DistTo(var3));
         }
      }

      return var1;
   }

   public boolean isSolidForSeparate() {
      if (this instanceof IsoZombieGiblets) {
         return false;
      } else if (this.current == null) {
         return false;
      } else if (!this.solid) {
         return false;
      } else {
         return !this.isOnFloor();
      }
   }

   public boolean isPushableForSeparate() {
      return true;
   }

   public boolean isPushedByForSeparate(IsoMovingObject var1) {
      return true;
   }

   public void separate() {
      if (this.isSolidForSeparate()) {
         if (this.isPushableForSeparate()) {
            IsoGameCharacter var1 = (IsoGameCharacter)Type.tryCastTo(this, IsoGameCharacter.class);
            IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
            if (this.z < 0.0F) {
               this.z = 0.0F;
            }

            for(int var3 = 0; var3 <= 8; ++var3) {
               IsoGridSquare var4 = var3 == 8 ? this.current : this.current.nav[var3];
               if (var4 != null && !var4.getMovingObjects().isEmpty() && (var4 == this.current || !this.current.isBlockedTo(var4))) {
                  for(int var5 = 0; var5 < var4.getMovingObjects().size(); ++var5) {
                     IsoMovingObject var6 = (IsoMovingObject)var4.getMovingObjects().get(var5);
                     if (var6 != this && var6.isSolidForSeparate() && !(Math.abs(this.z - var6.z) > 0.3F)) {
                        IsoGameCharacter var7 = (IsoGameCharacter)Type.tryCastTo(var6, IsoGameCharacter.class);
                        IsoPlayer var8 = (IsoPlayer)Type.tryCastTo(var6, IsoPlayer.class);
                        float var9 = this.width + var6.width;
                        Vector2 var10 = tempo;
                        var10.x = this.nx - var6.nx;
                        var10.y = this.ny - var6.ny;
                        float var11 = var10.getLength();
                        if (var1 == null || var7 == null && !(var6 instanceof BaseVehicle)) {
                           if (var11 < var9) {
                              CollisionManager.instance.AddContact(this, var6);
                           }

                           return;
                        }

                        if (var7 != null) {
                           if (var11 < var9 + 16.0F && var2 != null && var2.getBumpedChr() != var6 && var2.getBeenSprintingFor() >= 70.0F && WeaponType.getWeaponType((IsoGameCharacter)var2) == WeaponType.spear) {
                              var2.reportEvent("ChargeSpearConnect");
                              var2.setAttackType("charge");
                              var2.attackStarted = true;
                              var2.setVariable("StartedAttackWhileSprinting", true);
                              return;
                           }

                           if (!(var11 >= var9)) {
                              boolean var12 = false;
                              if (var2 != null && var2.getVariableFloat("WalkSpeed", 0.0F) > 0.2F && var2.runningTime > 0.5F && var2.getBumpedChr() != var6) {
                                 var12 = true;
                              }

                              if (GameClient.bClient && var2 != null && var7 instanceof IsoPlayer && !ServerOptions.getInstance().PlayerBumpPlayer.getValue()) {
                                 var12 = false;
                              }

                              if (var12 && !"charge".equals(var2.getAttackType())) {
                                 boolean var13 = !this.isOnFloor() && (var1.getBumpedChr() != null || (System.currentTimeMillis() - var2.getLastBump()) / 100L < 15L || var2.isSprinting()) && (var8 == null || !var8.isNPC());
                                 if (var13) {
                                    ++var1.bumpNbr;
                                    int var14 = 10 - var1.bumpNbr * 3;
                                    var14 += var1.getPerkLevel(PerkFactory.Perks.Fitness);
                                    var14 += var1.getPerkLevel(PerkFactory.Perks.Strength);
                                    if (var1.Traits.Clumsy.isSet()) {
                                       var14 -= 5;
                                    }

                                    if (var1.Traits.Graceful.isSet()) {
                                       var14 += 5;
                                    }

                                    if (var1.Traits.VeryUnderweight.isSet()) {
                                       var14 -= 8;
                                    }

                                    if (var1.Traits.Underweight.isSet()) {
                                       var14 -= 4;
                                    }

                                    if (var1.Traits.Obese.isSet()) {
                                       var14 -= 8;
                                    }

                                    if (var1.Traits.Overweight.isSet()) {
                                       var14 -= 4;
                                    }

                                    BodyPart var15 = var1.getBodyDamage().getBodyPart(BodyPartType.Torso_Lower);
                                    if (var15.getAdditionalPain(true) > 20.0F) {
                                       var14 = (int)((float)var14 - (var15.getAdditionalPain(true) - 20.0F) / 20.0F);
                                    }

                                    var14 = Math.min(80, var14);
                                    var14 = Math.max(1, var14);
                                    if (Rand.Next(var14) == 0 || var1.isSprinting()) {
                                       var1.setVariable("BumpDone", false);
                                       var1.setBumpFall(true);
                                       var1.setVariable("TripObstacleType", "zombie");
                                    }
                                 } else {
                                    var1.bumpNbr = 0;
                                 }

                                 var1.setLastBump(System.currentTimeMillis());
                                 var1.setBumpedChr(var7);
                                 var1.setBumpType(this.getBumpedType(var7));
                                 boolean var17 = var1.isBehind(var7);
                                 String var16 = var1.getBumpType();
                                 if (var17) {
                                    if (var16.equals("left")) {
                                       var16 = "right";
                                    } else {
                                       var16 = "left";
                                    }
                                 }

                                 var7.setBumpType(var16);
                                 var7.setHitFromBehind(var17);
                                 if (var13 | GameClient.bClient) {
                                    var1.actionContext.reportEvent("wasBumped");
                                 }
                              }

                              if (GameServer.bServer || this.distToNearestCamCharacter() < 60.0F) {
                                 if (this.isPushedByForSeparate(var6)) {
                                    var10.setLength((var11 - var9) / 8.0F);
                                    this.nx -= var10.x;
                                    this.ny -= var10.y;
                                 }

                                 this.collideWith(var6);
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public String getBumpedType(IsoGameCharacter var1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      String var4 = "left";
      if (this.dir == IsoDirections.S || this.dir == IsoDirections.SE || this.dir == IsoDirections.SW) {
         if (var2 < 0.0F) {
            var4 = "left";
         } else {
            var4 = "right";
         }
      }

      if (this.dir == IsoDirections.N || this.dir == IsoDirections.NE || this.dir == IsoDirections.NW) {
         if (var2 > 0.0F) {
            var4 = "left";
         } else {
            var4 = "right";
         }
      }

      if (this.dir == IsoDirections.E) {
         if (var3 > 0.0F) {
            var4 = "left";
         } else {
            var4 = "right";
         }
      }

      if (this.dir == IsoDirections.W) {
         if (var3 < 0.0F) {
            var4 = "left";
         } else {
            var4 = "right";
         }
      }

      return var4;
   }

   private void slideAwayFromWalls() {
      if (this.current != null) {
         IsoZombie var1 = (IsoZombie)Type.tryCastTo(this, IsoZombie.class);
         if (var1 != null && (this.isOnFloor() || var1.isKnockedDown())) {
            if (!var1.isCrawling() || var1.getPath2() == null && !var1.isMoving()) {
               if (!var1.isCurrentState(ClimbOverFenceState.instance()) && !var1.isCurrentState(ClimbThroughWindowState.instance())) {
                  if (var1.hasAnimationPlayer() && var1.getAnimationPlayer().isReady()) {
                     Vector3 var2 = IsoMovingObject.L_slideAwayFromWalls.vector3;
                     Model.BoneToWorldCoords((IsoGameCharacter)var1, var1.getAnimationPlayer().getSkinningBoneIndex("Bip01_Head", -1), var2);
                     if (Core.bDebug && DebugOptions.instance.CollideWithObstaclesRenderRadius.getValue()) {
                        LineDrawer.DrawIsoCircle(var2.x, var2.y, this.z, 0.3F, 16, 1.0F, 1.0F, 0.0F, 1.0F);
                     }

                     Vector2 var3 = IsoMovingObject.L_slideAwayFromWalls.vector2.set(var2.x - this.x, var2.y - this.y);
                     var3.normalize();
                     var2.x += var3.x * 0.3F;
                     var2.y += var3.y * 0.3F;
                     float var5;
                     if (var1.isKnockedDown() && (var1.isCurrentState(ZombieFallDownState.instance()) || var1.isCurrentState(StaggerBackState.instance()))) {
                        Vector2f var4 = PolygonalMap2.instance.resolveCollision(var1, var2.x, var2.y, IsoMovingObject.L_slideAwayFromWalls.vector2f);
                        if (var4.x != var2.x || var4.y != var2.y) {
                           var5 = GameTime.getInstance().getMultiplier() / 5.0F;
                           this.nx += (var4.x - var2.x) * var5;
                           this.ny += (var4.y - var2.y) * var5;
                           return;
                        }
                     }

                     if ((int)var2.x != this.current.x || (int)var2.y != this.current.y) {
                        IsoGridSquare var6 = this.getCell().getGridSquare((int)var2.x, (int)var2.y, (int)this.z);
                        if (var6 != null) {
                           if (this.current.testCollideAdjacent(this, var6.x - this.current.x, var6.y - this.current.y, 0)) {
                              var5 = GameTime.getInstance().getMultiplier() / 5.0F;
                              if (var6.x < this.current.x) {
                                 this.nx += ((float)this.current.x - var2.x) * var5;
                              } else if (var6.x > this.current.x) {
                                 this.nx += ((float)var6.x - var2.x) * var5;
                              }

                              if (var6.y < this.current.y) {
                                 this.ny += ((float)this.current.y - var2.y) * var5;
                              } else if (var6.y > this.current.y) {
                                 this.ny += ((float)var6.y - var2.y) * var5;
                              }

                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean DoCollide(int var1) {
      IsoGameCharacter var2 = (IsoGameCharacter)Type.tryCastTo(this, IsoGameCharacter.class);
      this.current = this.getCell().getGridSquare((int)this.nx, (int)this.ny, (int)this.z);
      int var3;
      int var4;
      int var5;
      if (this instanceof IsoMolotovCocktail) {
         for(var3 = (int)this.z; var3 > 0; --var3) {
            for(var4 = -1; var4 <= 1; ++var4) {
               for(var5 = -1; var5 <= 1; ++var5) {
                  IsoGridSquare var6 = this.getCell().createNewGridSquare((int)this.nx + var5, (int)this.ny + var4, var3, false);
                  if (var6 != null) {
                     var6.RecalcAllWithNeighbours(true);
                  }
               }
            }
         }
      }

      if (this.current != null) {
         if (!this.current.TreatAsSolidFloor()) {
            this.current = this.getCell().getGridSquare((int)this.nx, (int)this.ny, (int)this.z);
         }

         if (this.current == null) {
            return false;
         }

         this.current = this.getCell().getGridSquare((int)this.nx, (int)this.ny, (int)this.z);
      }

      if (this.current != this.last && this.last != null && this.current != null) {
         if (var2 != null && var2.getCurrentState() != null && var2.getCurrentState().isIgnoreCollide(var2, this.last.x, this.last.y, this.last.z, this.current.x, this.current.y, this.current.z)) {
            return false;
         }

         if (this == IsoCamera.CamCharacter) {
            IsoWorld.instance.CurrentCell.lightUpdateCount = 10;
         }

         var3 = this.current.getX() - this.last.getX();
         var4 = this.current.getY() - this.last.getY();
         var5 = this.current.getZ() - this.last.getZ();
         boolean var9 = false;
         if (this.last.testCollideAdjacent(this, var3, var4, var5) || this.current == null) {
            var9 = true;
         }

         if (var9) {
            if (this.last.getX() < this.current.getX()) {
               this.collidedE = true;
            }

            if (this.last.getX() > this.current.getX()) {
               this.collidedW = true;
            }

            if (this.last.getY() < this.current.getY()) {
               this.collidedS = true;
            }

            if (this.last.getY() > this.current.getY()) {
               this.collidedN = true;
            }

            this.current = this.last;
            this.checkBreakHoppable();
            this.checkHitHoppable();
            if (var1 == 2) {
               if ((this.collidedS || this.collidedN) && (this.collidedE || this.collidedW)) {
                  this.collidedS = false;
                  this.collidedN = false;
               }
            } else if (var1 == 1 && (this.collidedS || this.collidedN) && (this.collidedE || this.collidedW)) {
               this.collidedW = false;
               this.collidedE = false;
            }

            this.Collided();
            return true;
         }
      } else if (this.nx != this.lx || this.ny != this.ly) {
         if (this instanceof IsoZombie && Core.GameMode.equals("Tutorial")) {
            return true;
         }

         if (this.current == null) {
            if (this.nx < this.lx) {
               this.collidedW = true;
            }

            if (this.nx > this.lx) {
               this.collidedE = true;
            }

            if (this.ny < this.ly) {
               this.collidedN = true;
            }

            if (this.ny > this.ly) {
               this.collidedS = true;
            }

            this.nx = this.lx;
            this.ny = this.ly;
            this.current = this.last;
            this.Collided();
            return true;
         }

         if (var2 != null && var2.getPath2() != null) {
            PathFindBehavior2 var7 = var2.getPathFindBehavior2();
            if ((int)var7.getTargetX() == (int)this.x && (int)var7.getTargetY() == (int)this.y && (int)var7.getTargetZ() == (int)this.z) {
               return false;
            }
         }

         IsoGridSquare var8 = this.getFeelerTile(this.feelersize);
         if (var2 != null) {
            if (var2.isClimbing()) {
               var8 = this.current;
            }

            if (var8 != null && var8 != this.current && var2.getPath2() != null && !var2.getPath2().crossesSquare(var8.x, var8.y, var8.z)) {
               var8 = this.current;
            }
         }

         if (var8 != null && var8 != this.current && this.current != null) {
            if (var2 != null && var2.getCurrentState() != null && var2.getCurrentState().isIgnoreCollide(var2, this.current.x, this.current.y, this.current.z, var8.x, var8.y, var8.z)) {
               return false;
            }

            if (this.current.testCollideAdjacent(this, var8.getX() - this.current.getX(), var8.getY() - this.current.getY(), var8.getZ() - this.current.getZ())) {
               if (this.last != null) {
                  if (this.current.getX() < var8.getX()) {
                     this.collidedE = true;
                  }

                  if (this.current.getX() > var8.getX()) {
                     this.collidedW = true;
                  }

                  if (this.current.getY() < var8.getY()) {
                     this.collidedS = true;
                  }

                  if (this.current.getY() > var8.getY()) {
                     this.collidedN = true;
                  }

                  this.checkBreakHoppable();
                  this.checkHitHoppable();
                  if (var1 == 2 && (this.collidedS || this.collidedN) && (this.collidedE || this.collidedW)) {
                     this.collidedS = false;
                     this.collidedN = false;
                  }

                  if (var1 == 1 && (this.collidedS || this.collidedN) && (this.collidedE || this.collidedW)) {
                     this.collidedW = false;
                     this.collidedE = false;
                  }
               }

               this.Collided();
               return true;
            }
         }
      }

      return false;
   }

   private void checkHitHoppable() {
      IsoZombie var1 = (IsoZombie)Type.tryCastTo(this, IsoZombie.class);
      if (var1 != null && !var1.bCrawling) {
         if (!var1.isCurrentState(AttackState.instance()) && !var1.isCurrentState(StaggerBackState.instance()) && !var1.isCurrentState(ClimbOverFenceState.instance()) && !var1.isCurrentState(ClimbThroughWindowState.instance())) {
            if (this.collidedW && !this.collidedN && !this.collidedS && this.last.Is(IsoFlagType.HoppableW)) {
               var1.climbOverFence(IsoDirections.W);
            }

            if (this.collidedN && !this.collidedE && !this.collidedW && this.last.Is(IsoFlagType.HoppableN)) {
               var1.climbOverFence(IsoDirections.N);
            }

            IsoGridSquare var2;
            if (this.collidedS && !this.collidedE && !this.collidedW) {
               var2 = this.last.nav[IsoDirections.S.index()];
               if (var2 != null && var2.Is(IsoFlagType.HoppableN)) {
                  var1.climbOverFence(IsoDirections.S);
               }
            }

            if (this.collidedE && !this.collidedN && !this.collidedS) {
               var2 = this.last.nav[IsoDirections.E.index()];
               if (var2 != null && var2.Is(IsoFlagType.HoppableW)) {
                  var1.climbOverFence(IsoDirections.E);
               }
            }

         }
      }
   }

   private void checkBreakHoppable() {
      IsoZombie var1 = (IsoZombie)Type.tryCastTo(this, IsoZombie.class);
      if (var1 != null && var1.bCrawling) {
         if (!var1.isCurrentState(AttackState.instance()) && !var1.isCurrentState(StaggerBackState.instance()) && !var1.isCurrentState(CrawlingZombieTurnState.instance())) {
            IsoDirections var2 = IsoDirections.Max;
            if (this.collidedW && !this.collidedN && !this.collidedS) {
               var2 = IsoDirections.W;
            }

            if (this.collidedN && !this.collidedE && !this.collidedW) {
               var2 = IsoDirections.N;
            }

            if (this.collidedS && !this.collidedE && !this.collidedW) {
               var2 = IsoDirections.S;
            }

            if (this.collidedE && !this.collidedN && !this.collidedS) {
               var2 = IsoDirections.E;
            }

            if (var2 != IsoDirections.Max) {
               IsoObject var3 = this.last.getHoppableTo(this.last.getAdjacentSquare(var2));
               IsoThumpable var4 = (IsoThumpable)Type.tryCastTo(var3, IsoThumpable.class);
               if (var4 != null && !var4.isThumpable()) {
                  var1.setThumpTarget(var4);
               } else if (var3 != null && var3.getThumpableFor(var1) != null) {
                  var1.setThumpTarget(var3);
               }

            }
         }
      }
   }

   private void checkHitWall() {
      if (this.collidedN || this.collidedS || this.collidedE || this.collidedW) {
         if (this.current != null) {
            IsoPlayer var1 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
            if (var1 != null) {
               if (StringUtils.isNullOrEmpty(this.getCollideType())) {
                  boolean var2 = false;
                  int var3 = this.current.getWallType();
                  if ((var3 & 1) != 0 && this.collidedN && this.getDir() == IsoDirections.N) {
                     var2 = true;
                  }

                  if ((var3 & 2) != 0 && this.collidedS && this.getDir() == IsoDirections.S) {
                     var2 = true;
                  }

                  if ((var3 & 4) != 0 && this.collidedW && this.getDir() == IsoDirections.W) {
                     var2 = true;
                  }

                  if ((var3 & 8) != 0 && this.collidedE && this.getDir() == IsoDirections.E) {
                     var2 = true;
                  }

                  if (this.checkVaultOver()) {
                     var2 = false;
                  }

                  if (var2 && var1.isSprinting() && var1.isLocalPlayer()) {
                     this.setCollideType("wall");
                     var1.getActionContext().reportEvent("collideWithWall");
                     this.lastCollideTime = 70.0F;
                  }

               }
            }
         }
      }
   }

   private boolean checkVaultOver() {
      IsoPlayer var1 = (IsoPlayer)this;
      if (!var1.isCurrentState(ClimbOverFenceState.instance()) && !var1.isIgnoreAutoVault()) {
         if (!var1.IsRunning() && !var1.isSprinting() && var1.isLocalPlayer()) {
            return false;
         } else {
            IsoDirections var2 = this.getDir();
            IsoGridSquare var3 = this.current.getAdjacentSquare(IsoDirections.SE);
            if (var2 == IsoDirections.SE && var3 != null && var3.Is(IsoFlagType.HoppableN) && var3.Is(IsoFlagType.HoppableW)) {
               return false;
            } else {
               IsoGridSquare var4 = this.current;
               if (this.collidedS) {
                  var4 = this.current.getAdjacentSquare(IsoDirections.S);
               } else if (this.collidedE) {
                  var4 = this.current.getAdjacentSquare(IsoDirections.E);
               }

               if (var4 == null) {
                  return false;
               } else {
                  boolean var5 = false;
                  if (this.current.getProperties().Is(IsoFlagType.HoppableN) && this.collidedN && !this.collidedW && !this.collidedE && (var2 == IsoDirections.NW || var2 == IsoDirections.N || var2 == IsoDirections.NE)) {
                     var2 = IsoDirections.N;
                     var5 = true;
                  }

                  if (var4.getProperties().Is(IsoFlagType.HoppableN) && this.collidedS && !this.collidedW && !this.collidedE && (var2 == IsoDirections.SW || var2 == IsoDirections.S || var2 == IsoDirections.SE)) {
                     var2 = IsoDirections.S;
                     var5 = true;
                  }

                  if (this.current.getProperties().Is(IsoFlagType.HoppableW) && this.collidedW && !this.collidedN && !this.collidedS && (var2 == IsoDirections.NW || var2 == IsoDirections.W || var2 == IsoDirections.SW)) {
                     var2 = IsoDirections.W;
                     var5 = true;
                  }

                  if (var4.getProperties().Is(IsoFlagType.HoppableW) && this.collidedE && !this.collidedN && !this.collidedS && (var2 == IsoDirections.NE || var2 == IsoDirections.E || var2 == IsoDirections.SE)) {
                     var2 = IsoDirections.E;
                     var5 = true;
                  }

                  if (var5 && var1.isSafeToClimbOver(var2)) {
                     ClimbOverFenceState.instance().setParams(var1, var2);
                     var1.getActionContext().reportEvent("EventClimbFence");
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public void setMovingSquareNow() {
      if (this.movingSq != null) {
         this.movingSq.getMovingObjects().remove(this);
         this.movingSq = null;
      }

      if (this.current != null && !this.current.getMovingObjects().contains(this)) {
         this.current.getMovingObjects().add(this);
         this.movingSq = this.current;
      }

   }

   public IsoGridSquare getFeelerTile(float var1) {
      Vector2 var2 = tempo;
      var2.x = this.nx - this.lx;
      var2.y = this.ny - this.ly;
      var2.setLength(var1);
      return this.getCell().getGridSquare((int)(this.x + var2.x), (int)(this.y + var2.y), (int)this.z);
   }

   public void DoCollideNorS() {
      this.ny = this.ly;
   }

   public void DoCollideWorE() {
      this.nx = this.lx;
   }

   public int getTimeSinceZombieAttack() {
      return this.TimeSinceZombieAttack;
   }

   public void setTimeSinceZombieAttack(int var1) {
      this.TimeSinceZombieAttack = var1;
   }

   public boolean isCollidedE() {
      return this.collidedE;
   }

   public void setCollidedE(boolean var1) {
      this.collidedE = var1;
   }

   public boolean isCollidedN() {
      return this.collidedN;
   }

   public void setCollidedN(boolean var1) {
      this.collidedN = var1;
   }

   public IsoObject getCollidedObject() {
      return this.CollidedObject;
   }

   public void setCollidedObject(IsoObject var1) {
      this.CollidedObject = var1;
   }

   public boolean isCollidedS() {
      return this.collidedS;
   }

   public void setCollidedS(boolean var1) {
      this.collidedS = var1;
   }

   public boolean isCollidedThisFrame() {
      return this.collidedThisFrame;
   }

   public void setCollidedThisFrame(boolean var1) {
      this.collidedThisFrame = var1;
   }

   public boolean isCollidedW() {
      return this.collidedW;
   }

   public void setCollidedW(boolean var1) {
      this.collidedW = var1;
   }

   public boolean isCollidedWithDoor() {
      return this.CollidedWithDoor;
   }

   public void setCollidedWithDoor(boolean var1) {
      this.CollidedWithDoor = var1;
   }

   public boolean isCollidedWithVehicle() {
      return this.collidedWithVehicle;
   }

   public IsoGridSquare getCurrentSquare() {
      return this.current;
   }

   public IsoMetaGrid.Zone getCurrentZone() {
      return this.current != null ? this.current.getZone() : null;
   }

   public void setCurrent(IsoGridSquare var1) {
      this.current = var1;
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void setDestroyed(boolean var1) {
      this.destroyed = var1;
   }

   public boolean isFirstUpdate() {
      return this.firstUpdate;
   }

   public void setFirstUpdate(boolean var1) {
      this.firstUpdate = var1;
   }

   public Vector2 getHitDir() {
      return this.hitDir;
   }

   public void setHitDir(Vector2 var1) {
      this.hitDir.set(var1);
   }

   public float getImpulsex() {
      return this.impulsex;
   }

   public void setImpulsex(float var1) {
      this.impulsex = var1;
   }

   public float getImpulsey() {
      return this.impulsey;
   }

   public void setImpulsey(float var1) {
      this.impulsey = var1;
   }

   public float getLimpulsex() {
      return this.limpulsex;
   }

   public void setLimpulsex(float var1) {
      this.limpulsex = var1;
   }

   public float getLimpulsey() {
      return this.limpulsey;
   }

   public void setLimpulsey(float var1) {
      this.limpulsey = var1;
   }

   public float getHitForce() {
      return this.hitForce;
   }

   public void setHitForce(float var1) {
      this.hitForce = var1;
   }

   public float getHitFromAngle() {
      return this.hitFromAngle;
   }

   public void setHitFromAngle(float var1) {
      this.hitFromAngle = var1;
   }

   public IsoGridSquare getLastSquare() {
      return this.last;
   }

   public void setLast(IsoGridSquare var1) {
      this.last = var1;
   }

   public float getLx() {
      return this.lx;
   }

   public void setLx(float var1) {
      this.lx = var1;
   }

   public float getLy() {
      return this.ly;
   }

   public void setLy(float var1) {
      this.ly = var1;
   }

   public float getLz() {
      return this.lz;
   }

   public void setLz(float var1) {
      this.lz = var1;
   }

   public float getNx() {
      return this.nx;
   }

   public void setNx(float var1) {
      this.nx = var1;
   }

   public float getNy() {
      return this.ny;
   }

   public void setNy(float var1) {
      this.ny = var1;
   }

   public boolean getNoDamage() {
      return this.noDamage;
   }

   public void setNoDamage(boolean var1) {
      this.noDamage = var1;
   }

   public boolean isSolid() {
      return this.solid;
   }

   public void setSolid(boolean var1) {
      this.solid = var1;
   }

   public float getStateEventDelayTimer() {
      return this.StateEventDelayTimer;
   }

   public void setStateEventDelayTimer(float var1) {
      this.StateEventDelayTimer = var1;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float var1) {
      this.width = var1;
   }

   public boolean isbAltCollide() {
      return this.bAltCollide;
   }

   public void setbAltCollide(boolean var1) {
      this.bAltCollide = var1;
   }

   public boolean isShootable() {
      return this.shootable;
   }

   public void setShootable(boolean var1) {
      this.shootable = var1;
   }

   public IsoZombie getLastTargettedBy() {
      return this.lastTargettedBy;
   }

   public void setLastTargettedBy(IsoZombie var1) {
      this.lastTargettedBy = var1;
   }

   public boolean isCollidable() {
      return this.Collidable;
   }

   public void setCollidable(boolean var1) {
      this.Collidable = var1;
   }

   public float getScriptnx() {
      return this.scriptnx;
   }

   public void setScriptnx(float var1) {
      this.scriptnx = var1;
   }

   public float getScriptny() {
      return this.scriptny;
   }

   public void setScriptny(float var1) {
      this.scriptny = var1;
   }

   public String getScriptModule() {
      return this.ScriptModule;
   }

   public void setScriptModule(String var1) {
      this.ScriptModule = var1;
   }

   public Vector2 getMovementLastFrame() {
      return this.movementLastFrame;
   }

   public void setMovementLastFrame(Vector2 var1) {
      this.movementLastFrame = var1;
   }

   public float getFeelersize() {
      return this.feelersize;
   }

   public void setFeelersize(float var1) {
      this.feelersize = var1;
   }

   public byte canHaveMultipleHits() {
      byte var1 = 0;
      ArrayList var2 = IsoWorld.instance.CurrentCell.getObjectList();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)var2.get(var3);
         IsoPlayer var5 = (IsoPlayer)Type.tryCastTo(var4, IsoPlayer.class);
         if (var5 != null) {
            HandWeapon var6 = (HandWeapon)Type.tryCastTo(var5.getPrimaryHandItem(), HandWeapon.class);
            if (var6 == null || var5.bDoShove || var5.isForceShove()) {
               var6 = var5.bareHands;
            }

            float var7 = IsoUtils.DistanceTo(var5.x, var5.y, this.x, this.y);
            float var8 = var6.getMaxRange() * var6.getRangeMod(var5) + 2.0F;
            if (!(var7 > var8)) {
               float var9 = var5.getDotWithForwardDirection(this.x, this.y);
               if (!((double)var7 > 2.5D) || !(var9 < 0.1F)) {
                  LosUtil.TestResults var10 = LosUtil.lineClear(var5.getCell(), (int)var5.getX(), (int)var5.getY(), (int)var5.getZ(), (int)this.getX(), (int)this.getY(), (int)this.getZ(), false);
                  if (var10 != LosUtil.TestResults.Blocked && var10 != LosUtil.TestResults.ClearThroughClosedDoor) {
                     ++var1;
                     if (var1 >= 2) {
                        return var1;
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public boolean isOnFloor() {
      return this.bOnFloor;
   }

   public void setOnFloor(boolean var1) {
      this.bOnFloor = var1;
   }

   public void Despawn() {
   }

   public boolean isCloseKilled() {
      return this.closeKilled;
   }

   public void setCloseKilled(boolean var1) {
      this.closeKilled = var1;
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      var1.set(this.getX(), this.getY());
      return var1;
   }

   private boolean isInLoadedArea(int var1, int var2) {
      int var3;
      if (GameServer.bServer) {
         for(var3 = 0; var3 < ServerMap.instance.LoadedCells.size(); ++var3) {
            ServerMap.ServerCell var4 = (ServerMap.ServerCell)ServerMap.instance.LoadedCells.get(var3);
            if (var1 >= var4.WX * 50 && var1 < (var4.WX + 1) * 50 && var2 >= var4.WY * 50 && var2 < (var4.WY + 1) * 50) {
               return true;
            }
         }
      } else {
         for(var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            IsoChunkMap var5 = IsoWorld.instance.CurrentCell.ChunkMap[var3];
            if (!var5.ignore && var1 >= var5.getWorldXMinTiles() && var1 < var5.getWorldXMaxTiles() && var2 >= var5.getWorldYMinTiles() && var2 < var5.getWorldYMaxTiles()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isCollided() {
      return !StringUtils.isNullOrWhitespace(this.getCollideType());
   }

   public String getCollideType() {
      return this.collideType;
   }

   public void setCollideType(String var1) {
      this.collideType = var1;
   }

   public float getLastCollideTime() {
      return this.lastCollideTime;
   }

   public void setLastCollideTime(float var1) {
      this.lastCollideTime = var1;
   }

   public ArrayList getEatingZombies() {
      return this.eatingZombies;
   }

   public void setEatingZombies(ArrayList var1) {
      this.eatingZombies.clear();
      this.eatingZombies.addAll(var1);
   }

   public boolean isEatingOther(IsoMovingObject var1) {
      return var1 == null ? false : var1.eatingZombies.contains(this);
   }

   public float getDistanceSq(IsoMovingObject var1) {
      float var2 = this.x - var1.x;
      float var3 = this.y - var1.y;
      var2 *= var2;
      var3 *= var3;
      return var2 + var3;
   }

   public void setZombiesDontAttack(boolean var1) {
      this.zombiesDontAttack = var1;
   }

   public boolean isZombiesDontAttack() {
      return this.zombiesDontAttack;
   }

   public static class TreeSoundManager {
      private ArrayList squares = new ArrayList();
      private long[] soundTime = new long[6];
      private Comparator comp = (var1, var2) -> {
         float var3 = this.getClosestListener((float)var1.x + 0.5F, (float)var1.y + 0.5F, (float)var1.z);
         float var4 = this.getClosestListener((float)var2.x + 0.5F, (float)var2.y + 0.5F, (float)var2.z);
         if (var3 > var4) {
            return 1;
         } else {
            return var3 < var4 ? -1 : 0;
         }
      };

      public void addSquare(IsoGridSquare var1) {
         if (!this.squares.contains(var1)) {
            this.squares.add(var1);
         }

      }

      public void update() {
         if (!this.squares.isEmpty()) {
            Collections.sort(this.squares, this.comp);
            long var1 = System.currentTimeMillis();

            for(int var3 = 0; var3 < this.soundTime.length && var3 < this.squares.size(); ++var3) {
               IsoGridSquare var4 = (IsoGridSquare)this.squares.get(var3);
               if (!(this.getClosestListener((float)var4.x + 0.5F, (float)var4.y + 0.5F, (float)var4.z) > 20.0F)) {
                  int var5 = this.getFreeSoundSlot(var1);
                  if (var5 == -1) {
                     break;
                  }

                  Audio var6 = null;
                  float var7 = 0.05F;
                  float var8 = 16.0F;
                  float var9 = 0.29999998F;
                  if (GameClient.bClient) {
                     var6 = SoundManager.instance.PlayWorldSoundImpl("Bushes", false, var4.getX(), var4.getY(), var4.getZ(), var7, var8, var9, false);
                  } else {
                     BaseSoundEmitter var10 = IsoWorld.instance.getFreeEmitter((float)var4.x + 0.5F, (float)var4.y + 0.5F, (float)var4.z);
                     if (var10.playSound("Bushes") != 0L) {
                        this.soundTime[var5] = var1;
                     }
                  }

                  if (var6 != null) {
                     this.soundTime[var5] = var1;
                  }
               }
            }

            this.squares.clear();
         }
      }

      private float getClosestListener(float var1, float var2, float var3) {
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null && var6.getCurrentSquare() != null) {
               float var7 = var6.getX();
               float var8 = var6.getY();
               float var9 = var6.getZ();
               float var10 = IsoUtils.DistanceTo(var7, var8, var9 * 3.0F, var1, var2, var3 * 3.0F);
               if (var6.Traits.HardOfHearing.isSet()) {
                  var10 *= 4.5F;
               }

               if (var10 < var4) {
                  var4 = var10;
               }
            }
         }

         return var4;
      }

      private int getFreeSoundSlot(long var1) {
         long var3 = Long.MAX_VALUE;
         int var5 = -1;

         for(int var6 = 0; var6 < this.soundTime.length; ++var6) {
            if (this.soundTime[var6] < var3) {
               var3 = this.soundTime[var6];
               var5 = var6;
            }
         }

         if (var1 - var3 < 1000L) {
            return -1;
         } else {
            return var5;
         }
      }
   }

   private static final class L_postUpdate {
      static final Vector2f vector2f = new Vector2f();
   }

   private static final class L_slideAwayFromWalls {
      static final Vector2f vector2f = new Vector2f();
      static final Vector2 vector2 = new Vector2();
      static final Vector3 vector3 = new Vector3();
   }
}
