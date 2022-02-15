package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.ai.states.ThumpState;
import zombie.audio.parameters.ParameterMeleeHitSurface;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.LosUtil;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.Type;

public class IsoBarricade extends IsoObject implements Thumpable {
   public static final int MAX_PLANKS = 4;
   public static final int PLANK_HEALTH = 1000;
   private int[] plankHealth = new int[4];
   public static final int METAL_HEALTH = 5000;
   public static final int METAL_HEALTH_DAMAGED = 2500;
   private int metalHealth;
   public static final int METAL_BAR_HEALTH = 3000;
   private int metalBarHealth;

   public IsoBarricade(IsoCell var1) {
      super(var1);
   }

   public IsoBarricade(IsoCell var1, IsoGridSquare var2, IsoDirections var3) {
      this.square = var2;
      this.dir = var3;
   }

   public String getObjectName() {
      return "Barricade";
   }

   public void addPlank(IsoGameCharacter var1, InventoryItem var2) {
      if (this.canAddPlank()) {
         int var3 = 1000;
         if (var2 != null) {
            var3 = (int)((float)var2.getCondition() / (float)var2.getConditionMax() * 1000.0F);
         }

         if (var1 != null) {
            var3 = (int)((float)var3 * var1.getBarricadeStrengthMod());
         }

         int var4;
         for(var4 = 0; var4 < 4; ++var4) {
            if (this.plankHealth[var4] <= 0) {
               this.plankHealth[var4] = var3;
               break;
            }
         }

         this.chooseSprite();
         if (!GameServer.bServer) {
            for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
               LosUtil.cachecleared[var4] = true;
            }

            IsoGridSquare.setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
         }

         if (this.square != null) {
            this.square.RecalcProperties();
         }

      }
   }

   public InventoryItem removePlank(IsoGameCharacter var1) {
      if (this.getNumPlanks() <= 0) {
         return null;
      } else {
         InventoryItem var2 = null;

         int var3;
         for(var3 = 3; var3 >= 0; --var3) {
            if (this.plankHealth[var3] > 0) {
               float var4 = Math.min((float)this.plankHealth[var3] / 1000.0F, 1.0F);
               var2 = InventoryItemFactory.CreateItem("Base.Plank");
               var2.setCondition((int)Math.max((float)var2.getConditionMax() * var4, 1.0F));
               this.plankHealth[var3] = 0;
               break;
            }
         }

         if (this.getNumPlanks() <= 0) {
            if (this.square != null) {
               if (GameServer.bServer) {
                  this.square.transmitRemoveItemFromSquare(this);
               } else {
                  this.square.RemoveTileObject(this);
               }
            }
         } else {
            this.chooseSprite();
            if (!GameServer.bServer) {
               for(var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
                  LosUtil.cachecleared[var3] = true;
               }

               IsoGridSquare.setRecalcLightTime(-1);
               GameTime.instance.lightSourceUpdate = 100.0F;
            }

            if (this.square != null) {
               this.square.RecalcProperties();
            }
         }

         return var2;
      }
   }

   public int getNumPlanks() {
      int var1 = 0;

      for(int var2 = 0; var2 < 4; ++var2) {
         if (this.plankHealth[var2] > 0) {
            ++var1;
         }
      }

      return var1;
   }

   public boolean canAddPlank() {
      return !this.isMetal() && this.getNumPlanks() < 4 && !this.isMetalBar();
   }

   public void addMetalBar(IsoGameCharacter var1, InventoryItem var2) {
      if (this.getNumPlanks() <= 0) {
         if (this.metalHealth <= 0) {
            if (this.metalBarHealth <= 0) {
               this.metalBarHealth = 3000;
               if (var2 != null) {
                  this.metalBarHealth = (int)((float)var2.getCondition() / (float)var2.getConditionMax() * 5000.0F);
               }

               if (var1 != null) {
                  this.metalBarHealth = (int)((float)this.metalBarHealth * var1.getMetalBarricadeStrengthMod());
               }

               this.chooseSprite();
               if (!GameServer.bServer) {
                  for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
                     LosUtil.cachecleared[var3] = true;
                  }

                  IsoGridSquare.setRecalcLightTime(-1);
                  GameTime.instance.lightSourceUpdate = 100.0F;
               }

               if (this.square != null) {
                  this.square.RecalcProperties();
               }

            }
         }
      }
   }

   public InventoryItem removeMetalBar(IsoGameCharacter var1) {
      if (this.metalBarHealth <= 0) {
         return null;
      } else {
         float var2 = Math.min((float)this.metalBarHealth / 3000.0F, 1.0F);
         this.metalBarHealth = 0;
         InventoryItem var3 = InventoryItemFactory.CreateItem("Base.MetalBar");
         var3.setCondition((int)Math.max((float)var3.getConditionMax() * var2, 1.0F));
         if (this.square != null) {
            if (GameServer.bServer) {
               this.square.transmitRemoveItemFromSquare(this);
            } else {
               this.square.RemoveTileObject(this);
            }
         }

         return var3;
      }
   }

   public void addMetal(IsoGameCharacter var1, InventoryItem var2) {
      if (this.getNumPlanks() <= 0) {
         if (this.metalHealth <= 0) {
            this.metalHealth = 5000;
            if (var2 != null) {
               this.metalHealth = (int)((float)var2.getCondition() / (float)var2.getConditionMax() * 5000.0F);
            }

            if (var1 != null) {
               this.metalHealth = (int)((float)this.metalHealth * var1.getMetalBarricadeStrengthMod());
            }

            this.chooseSprite();
            if (!GameServer.bServer) {
               for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
                  LosUtil.cachecleared[var3] = true;
               }

               IsoGridSquare.setRecalcLightTime(-1);
               GameTime.instance.lightSourceUpdate = 100.0F;
            }

            if (this.square != null) {
               this.square.RecalcProperties();
            }

         }
      }
   }

   public boolean isMetalBar() {
      return this.metalBarHealth > 0;
   }

   public InventoryItem removeMetal(IsoGameCharacter var1) {
      if (this.metalHealth <= 0) {
         return null;
      } else {
         float var2 = Math.min((float)this.metalHealth / 5000.0F, 1.0F);
         this.metalHealth = 0;
         InventoryItem var3 = InventoryItemFactory.CreateItem("Base.SheetMetal");
         var3.setCondition((int)Math.max((float)var3.getConditionMax() * var2, 1.0F));
         if (this.square != null) {
            if (GameServer.bServer) {
               this.square.transmitRemoveItemFromSquare(this);
            } else {
               this.square.RemoveTileObject(this);
            }
         }

         return var3;
      }
   }

   public boolean isMetal() {
      return this.metalHealth > 0;
   }

   public boolean isBlockVision() {
      return this.isMetal() || this.getNumPlanks() > 2;
   }

   private void chooseSprite() {
      IsoSpriteManager var1 = IsoSpriteManager.instance;
      int var2;
      String var3;
      if (this.metalHealth > 0) {
         var2 = this.metalHealth <= 2500 ? 2 : 0;
         var3 = "constructedobjects_01";
         switch(this.dir) {
         case W:
            this.sprite = var1.getSprite(var3 + "_" + (24 + var2));
            break;
         case N:
            this.sprite = var1.getSprite(var3 + "_" + (25 + var2));
            break;
         case E:
            this.sprite = var1.getSprite(var3 + "_" + (28 + var2));
            break;
         case S:
            this.sprite = var1.getSprite(var3 + "_" + (29 + var2));
            break;
         default:
            this.sprite.LoadFramesNoDirPageSimple("media/ui/missing-tile.png");
         }

      } else if (this.metalBarHealth > 0) {
         String var4 = "constructedobjects_01";
         switch(this.dir) {
         case W:
            this.sprite = var1.getSprite(var4 + "_55");
            break;
         case N:
            this.sprite = var1.getSprite(var4 + "_53");
            break;
         case E:
            this.sprite = var1.getSprite(var4 + "_52");
            break;
         case S:
            this.sprite = var1.getSprite(var4 + "_54");
            break;
         default:
            this.sprite.LoadFramesNoDirPageSimple("media/ui/missing-tile.png");
         }

      } else {
         var2 = this.getNumPlanks();
         if (var2 <= 0) {
            this.sprite = var1.getSprite("media/ui/missing-tile.png");
         } else {
            var3 = "carpentry_01";
            switch(this.dir) {
            case W:
               this.sprite = var1.getSprite(var3 + "_" + (8 + (var2 - 1) * 2));
               break;
            case N:
               this.sprite = var1.getSprite(var3 + "_" + (9 + (var2 - 1) * 2));
               break;
            case E:
               this.sprite = var1.getSprite(var3 + "_" + (0 + (var2 - 1) * 2));
               break;
            case S:
               this.sprite = var1.getSprite(var3 + "_" + (1 + (var2 - 1) * 2));
               break;
            default:
               this.sprite.LoadFramesNoDirPageSimple("media/ui/missing-tile.png");
            }

         }
      }
   }

   public boolean isDestroyed() {
      return this.metalHealth <= 0 && this.getNumPlanks() <= 0 && this.metalBarHealth <= 0;
   }

   public boolean TestCollide(IsoMovingObject var1, IsoGridSquare var2, IsoGridSquare var3) {
      return false;
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      if (this.metalHealth <= 0 && this.getNumPlanks() <= 2) {
         return IsoObject.VisionResult.NoEffect;
      } else {
         if (var1 == this.square) {
            if (this.dir == IsoDirections.N && var2.getY() < var1.getY()) {
               return IsoObject.VisionResult.Blocked;
            }

            if (this.dir == IsoDirections.S && var2.getY() > var1.getY()) {
               return IsoObject.VisionResult.Blocked;
            }

            if (this.dir == IsoDirections.W && var2.getX() < var1.getX()) {
               return IsoObject.VisionResult.Blocked;
            }

            if (this.dir == IsoDirections.E && var2.getX() > var1.getX()) {
               return IsoObject.VisionResult.Blocked;
            }
         } else if (var2 == this.square && var1 != this.square) {
            return this.TestVision(var2, var1);
         }

         return IsoObject.VisionResult.NoEffect;
      }
   }

   public void Thump(IsoMovingObject var1) {
      if (!this.isDestroyed()) {
         if (var1 instanceof IsoZombie) {
            int var2 = this.getNumPlanks();
            boolean var3 = this.metalHealth > 2500;
            int var4 = ThumpState.getFastForwardDamageMultiplier();
            this.Damage(((IsoZombie)var1).strength * var4);
            if (var2 != this.getNumPlanks()) {
               ((IsoGameCharacter)var1).getEmitter().playSound("BreakBarricadePlank");
               if (GameServer.bServer) {
                  GameServer.PlayWorldSoundServer("BreakBarricadePlank", false, var1.getCurrentSquare(), 0.2F, 20.0F, 1.1F, true);
               }
            }

            if (this.isDestroyed()) {
               if (this.getSquare().getBuilding() != null) {
                  this.getSquare().getBuilding().forceAwake();
               }

               this.square.transmitRemoveItemFromSquare(this);
               if (!GameServer.bServer) {
                  this.square.RemoveTileObject(this);
               }
            } else if ((var2 != this.getNumPlanks() || var3 && this.metalHealth < 2500) && GameServer.bServer) {
               this.sendObjectChange("state");
            }

            if (!this.isDestroyed()) {
               this.setRenderEffect(RenderEffectType.Hit_Door, true);
            }

            WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, true, 4.0F, 15.0F);
         }

      }
   }

   public Thumpable getThumpableFor(IsoGameCharacter var1) {
      return this.isDestroyed() ? null : this;
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      if (this.square == null) {
         return var1.set(0.0F, 0.0F);
      } else if (this.dir == IsoDirections.N) {
         return var1.set(this.getX() + 0.5F, this.getY());
      } else if (this.dir == IsoDirections.S) {
         return var1.set(this.getX() + 0.5F, this.getY() + 1.0F);
      } else if (this.dir == IsoDirections.W) {
         return var1.set(this.getX(), this.getY() + 0.5F);
      } else {
         return this.dir == IsoDirections.E ? var1.set(this.getX() + 1.0F, this.getY() + 0.5F) : var1.set(this.getX(), this.getY() + 0.5F);
      }
   }

   public void WeaponHit(IsoGameCharacter var1, HandWeapon var2) {
      if (!this.isDestroyed()) {
         IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
         if (GameClient.bClient) {
            if (var3 != null) {
               GameClient.instance.sendWeaponHit(var3, var2, this);
            }

         } else {
            String var4 = !this.isMetal() && !this.isMetalBar() ? "HitBarricadePlank" : "HitBarricadeMetal";
            if (var3 != null) {
               var3.setMeleeHitSurface(!this.isMetal() && !this.isMetalBar() ? ParameterMeleeHitSurface.Material.Wood : ParameterMeleeHitSurface.Material.Metal);
            }

            SoundManager.instance.PlayWorldSound(var4, false, this.getSquare(), 1.0F, 20.0F, 2.0F, false);
            if (GameServer.bServer) {
               GameServer.PlayWorldSoundServer(var4, false, this.getSquare(), 1.0F, 20.0F, 2.0F, false);
            }

            if (var2 != null) {
               this.Damage(var2.getDoorDamage() * 5);
            } else {
               this.Damage(100);
            }

            WorldSoundManager.instance.addSound(var1, this.square.getX(), this.square.getY(), this.square.getZ(), 20, 20, false, 0.0F, 15.0F);
            if (this.isDestroyed()) {
               if (var1 != null) {
                  String var5 = var4 == "HitBarricadeMetal" ? "BreakBarricadeMetal" : "BreakBarricadePlank";
                  var1.getEmitter().playSound(var5);
                  if (GameServer.bServer) {
                     GameServer.PlayWorldSoundServer(var5, false, var1.getCurrentSquare(), 0.2F, 20.0F, 1.1F, true);
                  }
               }

               this.square.transmitRemoveItemFromSquare(this);
               if (!GameServer.bServer) {
                  this.square.RemoveTileObject(this);
               }
            }

            if (!this.isDestroyed()) {
               this.setRenderEffect(RenderEffectType.Hit_Door, true);
            }

         }
      }
   }

   public void DamageBarricade(int var1) {
      this.Damage(var1);
   }

   public void Damage(int var1) {
      if (!"Tutorial".equals(Core.GameMode)) {
         if (this.metalHealth > 0) {
            this.metalHealth -= var1;
            if (this.metalHealth <= 0) {
               this.metalHealth = 0;
               this.chooseSprite();
            }

         } else if (this.metalBarHealth > 0) {
            this.metalBarHealth -= var1;
            if (this.metalBarHealth <= 0) {
               this.metalBarHealth = 0;
               this.chooseSprite();
            }

         } else {
            for(int var2 = 3; var2 >= 0; --var2) {
               if (this.plankHealth[var2] > 0) {
                  int[] var10000 = this.plankHealth;
                  var10000[var2] -= var1;
                  if (this.plankHealth[var2] <= 0) {
                     this.plankHealth[var2] = 0;
                     this.chooseSprite();
                  }
                  break;
               }
            }

         }
      }
   }

   public float getThumpCondition() {
      if (this.metalHealth > 0) {
         return (float)PZMath.clamp(this.metalHealth, 0, 5000) / 5000.0F;
      } else if (this.metalBarHealth > 0) {
         return (float)PZMath.clamp(this.metalBarHealth, 0, 3000) / 3000.0F;
      } else {
         for(int var1 = 3; var1 >= 0; --var1) {
            if (this.plankHealth[var1] > 0) {
               return (float)PZMath.clamp(this.plankHealth[var1], 0, 1000) / 1000.0F;
            }
         }

         return 0.0F;
      }
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      byte var4 = var1.get();
      this.dir = IsoDirections.fromIndex(var4);
      byte var5 = var1.get();

      for(int var6 = 0; var6 < var5; ++var6) {
         short var7 = var1.getShort();
         if (var6 < 4) {
            this.plankHealth[var6] = var7;
         }
      }

      this.metalHealth = var1.getShort();
      if (var2 >= 90) {
         this.metalBarHealth = var1.getShort();
      }

      this.chooseSprite();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      var1.put((byte)1);
      var1.put(IsoObject.factoryGetClassID(this.getObjectName()));
      var1.put((byte)this.dir.index());
      var1.put((byte)4);

      for(int var3 = 0; var3 < 4; ++var3) {
         var1.putShort((short)this.plankHealth[var3]);
      }

      var1.putShort((short)this.metalHealth);
      var1.putShort((short)this.metalBarHealth);
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      if ("state".equals(var1)) {
         for(int var4 = 0; var4 < 4; ++var4) {
            var3.putShort((short)this.plankHealth[var4]);
         }

         var3.putShort((short)this.metalHealth);
         var3.putShort((short)this.metalBarHealth);
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      if ("state".equals(var1)) {
         int var3;
         for(var3 = 0; var3 < 4; ++var3) {
            this.plankHealth[var3] = var2.getShort();
         }

         this.metalHealth = var2.getShort();
         this.metalBarHealth = var2.getShort();
         this.chooseSprite();
         if (this.square != null) {
            this.square.RecalcProperties();
         }

         for(var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            LosUtil.cachecleared[var3] = true;
         }

         IsoGridSquare.setRecalcLightTime(-1);
         GameTime.instance.lightSourceUpdate = 100.0F;
      }

   }

   public BarricadeAble getBarricadedObject() {
      int var1 = this.getSpecialObjectIndex();
      if (var1 == -1) {
         return null;
      } else {
         ArrayList var2 = this.getSquare().getSpecialObjects();
         boolean var3;
         int var4;
         if (this.getDir() != IsoDirections.W && this.getDir() != IsoDirections.N) {
            if (this.getDir() == IsoDirections.E || this.getDir() == IsoDirections.S) {
               var3 = this.getDir() == IsoDirections.S;
               var4 = this.getSquare().getX() + (this.getDir() == IsoDirections.E ? 1 : 0);
               int var9 = this.getSquare().getY() + (this.getDir() == IsoDirections.S ? 1 : 0);
               IsoGridSquare var6 = this.getCell().getGridSquare((double)var4, (double)var9, (double)this.getZ());
               if (var6 != null) {
                  var2 = var6.getSpecialObjects();

                  for(int var7 = var2.size() - 1; var7 >= 0; --var7) {
                     IsoObject var8 = (IsoObject)var2.get(var7);
                     if (var8 instanceof BarricadeAble && var3 == ((BarricadeAble)var8).getNorth()) {
                        return (BarricadeAble)var8;
                     }
                  }
               }
            }
         } else {
            var3 = this.getDir() == IsoDirections.N;

            for(var4 = var1 - 1; var4 >= 0; --var4) {
               IsoObject var5 = (IsoObject)var2.get(var4);
               if (var5 instanceof BarricadeAble && var3 == ((BarricadeAble)var5).getNorth()) {
                  return (BarricadeAble)var5;
               }
            }
         }

         return null;
      }
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      int var8 = IsoCamera.frameState.playerIndex;
      BarricadeAble var9 = this.getBarricadedObject();
      if (var9 != null && this.square.lighting[var8].targetDarkMulti() <= var9.getSquare().lighting[var8].targetDarkMulti()) {
         var4 = var9.getSquare().lighting[var8].lightInfo();
         this.setTargetAlpha(var8, ((IsoObject)var9).getTargetAlpha(var8));
      }

      super.render(var1, var2, var3, var4, var5, var6, var7);
   }

   public static IsoBarricade GetBarricadeOnSquare(IsoGridSquare var0, IsoDirections var1) {
      if (var0 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var0.getSpecialObjects().size(); ++var2) {
            IsoObject var3 = (IsoObject)var0.getSpecialObjects().get(var2);
            if (var3 instanceof IsoBarricade) {
               IsoBarricade var4 = (IsoBarricade)var3;
               if (var4.getDir() == var1) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public static IsoBarricade GetBarricadeForCharacter(BarricadeAble var0, IsoGameCharacter var1) {
      if (var0 != null && var0.getSquare() != null) {
         if (var1 != null) {
            if (var0.getNorth()) {
               if (var1.getY() < (float)var0.getSquare().getY()) {
                  return GetBarricadeOnSquare(var0.getOppositeSquare(), var0.getNorth() ? IsoDirections.S : IsoDirections.E);
               }
            } else if (var1.getX() < (float)var0.getSquare().getX()) {
               return GetBarricadeOnSquare(var0.getOppositeSquare(), var0.getNorth() ? IsoDirections.S : IsoDirections.E);
            }
         }

         return GetBarricadeOnSquare(var0.getSquare(), var0.getNorth() ? IsoDirections.N : IsoDirections.W);
      } else {
         return null;
      }
   }

   public static IsoBarricade GetBarricadeOppositeCharacter(BarricadeAble var0, IsoGameCharacter var1) {
      if (var0 != null && var0.getSquare() != null) {
         if (var1 != null) {
            if (var0.getNorth()) {
               if (var1.getY() < (float)var0.getSquare().getY()) {
                  return GetBarricadeOnSquare(var0.getSquare(), var0.getNorth() ? IsoDirections.N : IsoDirections.W);
               }
            } else if (var1.getX() < (float)var0.getSquare().getX()) {
               return GetBarricadeOnSquare(var0.getSquare(), var0.getNorth() ? IsoDirections.N : IsoDirections.W);
            }
         }

         return GetBarricadeOnSquare(var0.getOppositeSquare(), var0.getNorth() ? IsoDirections.S : IsoDirections.E);
      } else {
         return null;
      }
   }

   public static IsoBarricade AddBarricadeToObject(BarricadeAble var0, boolean var1) {
      IsoGridSquare var2 = var1 ? var0.getOppositeSquare() : var0.getSquare();
      IsoDirections var3 = null;
      if (var0.getNorth()) {
         var3 = var1 ? IsoDirections.S : IsoDirections.N;
      } else {
         var3 = var1 ? IsoDirections.E : IsoDirections.W;
      }

      if (var2 != null && var3 != null) {
         IsoBarricade var4 = GetBarricadeOnSquare(var2, var3);
         if (var4 != null) {
            return var4;
         } else {
            var4 = new IsoBarricade(IsoWorld.instance.CurrentCell, var2, var3);
            int var5 = -1;

            int var6;
            for(var6 = 0; var6 < var2.getObjects().size(); ++var6) {
               IsoObject var7 = (IsoObject)var2.getObjects().get(var6);
               if (var7 instanceof IsoCurtain) {
                  IsoCurtain var8 = (IsoCurtain)var7;
                  if (var8.getType() == IsoObjectType.curtainW && var3 == IsoDirections.W) {
                     var5 = var6;
                  } else if (var8.getType() == IsoObjectType.curtainN && var3 == IsoDirections.N) {
                     var5 = var6;
                  } else if (var8.getType() == IsoObjectType.curtainE && var3 == IsoDirections.E) {
                     var5 = var6;
                  } else if (var8.getType() == IsoObjectType.curtainS && var3 == IsoDirections.S) {
                     var5 = var6;
                  }

                  if (var5 != -1) {
                     break;
                  }
               }
            }

            var2.AddSpecialObject(var4, var5);

            for(var6 = 0; var6 < IsoPlayer.numPlayers; ++var6) {
               LosUtil.cachecleared[var6] = true;
            }

            IsoGridSquare.setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
            return var4;
         }
      } else {
         return null;
      }
   }

   public static IsoBarricade AddBarricadeToObject(BarricadeAble var0, IsoGameCharacter var1) {
      if (var0 != null && var0.getSquare() != null && var1 != null) {
         boolean var2;
         if (var0.getNorth()) {
            var2 = var1.getY() < (float)var0.getSquare().getY();
            return AddBarricadeToObject(var0, var2);
         } else {
            var2 = var1.getX() < (float)var0.getSquare().getX();
            return AddBarricadeToObject(var0, var2);
         }
      } else {
         return null;
      }
   }
}
