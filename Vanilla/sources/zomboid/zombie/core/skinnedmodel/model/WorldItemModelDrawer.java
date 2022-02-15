package zombie.core.skinnedmodel.model;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import zombie.core.Core;
import zombie.core.ImmutableColor;
import zombie.core.SpriteRenderer;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.util.StringUtils;

public final class WorldItemModelDrawer extends TextureDraw.GenericDrawer {
   private static final ObjectPool s_modelDrawerPool = new ObjectPool(WorldItemModelDrawer::new);
   private static final ColorInfo tempColorInfo = new ColorInfo();
   private static final Matrix4f s_attachmentXfrm = new Matrix4f();
   private static final ImmutableColor ROTTEN_FOOD_COLOR = new ImmutableColor(0.5F, 0.5F, 0.5F);
   private boolean makeDisapear = false;
   public static boolean NEW_WAY = true;
   private Model m_model;
   private float m_hue;
   private float m_tintR;
   private float m_tintG;
   private float m_tintB;
   private float m_x;
   private float m_y;
   private float m_z;
   private final Vector3f m_angle = new Vector3f();
   private final Matrix4f m_transform = new Matrix4f();
   private float m_ambientR;
   private float m_ambientG;
   private float m_ambientB;
   private float alpha = 1.0F;
   static Vector3f temprot = new Vector3f(0.0F, 5.0F, -2.0F);

   public static boolean renderMain(InventoryItem var0, IsoGridSquare var1, float var2, float var3, float var4, float var5) {
      return renderMain(var0, var1, var2, var3, var4, var5, -1.0F);
   }

   public static boolean renderMain(InventoryItem var0, IsoGridSquare var1, float var2, float var3, float var4, float var5, float var6) {
      if (var0 != null && var1 != null) {
         Core.getInstance();
         if (!Core.Option3DGroundItem) {
            return false;
         } else {
            ModelScript var7;
            String var8;
            String var9;
            String var10;
            Model var14;
            WorldItemModelDrawer var15;
            if (!StringUtils.isNullOrEmpty(var0.getWorldStaticItem())) {
               var7 = ScriptManager.instance.getModelScript(var0.getWorldStaticItem());
               if (var7 != null) {
                  var8 = var7.getMeshName();
                  var9 = var7.getTextureName();
                  var10 = var7.getShaderName();
                  ImmutableColor var11 = ImmutableColor.white;
                  float var12 = 1.0F;
                  if (var0 instanceof Food) {
                     ModelScript var13;
                     if (((Food)var0).isCooked()) {
                        var13 = ScriptManager.instance.getModelScript(var0.getWorldStaticItem() + "Cooked");
                        if (var13 != null) {
                           var9 = var13.getTextureName();
                           var8 = var13.getMeshName();
                           var10 = var13.getShaderName();
                           var7 = var13;
                        }
                     }

                     if (((Food)var0).isBurnt()) {
                        var13 = ScriptManager.instance.getModelScript(var0.getWorldStaticItem() + "Burnt");
                        if (var13 != null) {
                           var9 = var13.getTextureName();
                           var8 = var13.getMeshName();
                           var10 = var13.getShaderName();
                           var7 = var13;
                        }
                     }

                     if (((Food)var0).isRotten()) {
                        var13 = ScriptManager.instance.getModelScript(var0.getWorldStaticItem() + "Rotten");
                        if (var13 != null) {
                           var9 = var13.getTextureName();
                           var8 = var13.getMeshName();
                           var10 = var13.getShaderName();
                           var7 = var13;
                        } else {
                           var11 = ROTTEN_FOOD_COLOR;
                        }
                     }
                  }

                  if (var0 instanceof Clothing || var0.getClothingItem() != null) {
                     var9 = var7.getTextureName(true);
                     var11 = var0.getVisual().getTint(var0.getClothingItem());
                     if (var9 == null) {
                        if (!var0.getClothingItem().textureChoices.isEmpty()) {
                           var9 = (String)var0.getClothingItem().textureChoices.get(var0.getVisual().getTextureChoice());
                        } else {
                           var9 = (String)var0.getClothingItem().m_BaseTextures.get(var0.getVisual().getBaseTexture());
                        }
                     }
                  }

                  boolean var25 = var7.bStatic;
                  var14 = ModelManager.instance.tryGetLoadedModel(var8, var9, var25, var10, true);
                  if (var14 == null) {
                     ModelManager.instance.loadAdditionalModel(var8, var9, var25, var10);
                  }

                  var14 = ModelManager.instance.getLoadedModel(var8, var9, var25, var10);
                  if (var14 != null && var14.isReady()) {
                     var15 = (WorldItemModelDrawer)s_modelDrawerPool.alloc();
                     var15.init(var0, var1, var2, var3, var4, var14, var12, var11, var5);
                     if (var7.scale != 1.0F) {
                        var15.m_transform.scale(var7.scale);
                     }

                     if (var0.worldScale != 1.0F) {
                        var15.m_transform.scale(var7.scale * var0.worldScale);
                     }

                     var15.m_angle.x = 0.0F;
                     if (var6 < 0.0F) {
                        var15.m_angle.y = (float)var0.worldZRotation;
                     } else {
                        var15.m_angle.y = var6;
                     }

                     var15.m_angle.z = 0.0F;
                     if (Core.bDebug) {
                     }

                     SpriteRenderer.instance.drawGeneric(var15);
                     return true;
                  }
               }
            } else if (var0 instanceof Clothing) {
               ClothingItem var18 = var0.getClothingItem();
               ItemVisual var19 = var0.getVisual();
               if (var18 != null && var19 != null && "Bip01_Head".equalsIgnoreCase(var18.m_AttachBone) && (!((Clothing)var0).isCosmetic() || "Eyes".equals(var0.getBodyLocation()))) {
                  boolean var20 = false;
                  var10 = var18.getModel(var20);
                  if (!StringUtils.isNullOrWhitespace(var10)) {
                     String var21 = var19.getTextureChoice(var18);
                     boolean var22 = var18.m_Static;
                     String var26 = var18.m_Shader;
                     var14 = ModelManager.instance.tryGetLoadedModel(var10, var21, var22, var26, false);
                     if (var14 == null) {
                        ModelManager.instance.loadAdditionalModel(var10, var21, var22, var26);
                     }

                     var14 = ModelManager.instance.getLoadedModel(var10, var21, var22, var26);
                     if (var14 != null && var14.isReady()) {
                        var15 = (WorldItemModelDrawer)s_modelDrawerPool.alloc();
                        float var30 = var19.getHue(var18);
                        ImmutableColor var17 = var19.getTint(var18);
                        var15.init(var0, var1, var2, var3, var4, var14, var30, var17, var5);
                        if (NEW_WAY) {
                           var15.m_angle.x = 180.0F + var5;
                           if (var6 < 0.0F) {
                              var15.m_angle.y = (float)var0.worldZRotation;
                           } else {
                              var15.m_angle.y = var6;
                           }

                           var15.m_angle.z = -90.0F;
                           if (Core.bDebug) {
                           }

                           var15.m_transform.translate(-0.08F, 0.0F, 0.05F);
                        }

                        SpriteRenderer.instance.drawGeneric(var15);
                        return true;
                     }
                  }
               }
            }

            if (var0 instanceof HandWeapon) {
               var7 = ScriptManager.instance.getModelScript(var0.getStaticModel());
               if (var7 != null) {
                  var8 = var7.getMeshName();
                  var9 = var7.getTextureName();
                  var10 = var7.getShaderName();
                  boolean var23 = var7.bStatic;
                  Model var24 = ModelManager.instance.tryGetLoadedModel(var8, var9, var23, var10, false);
                  if (var24 == null) {
                     ModelManager.instance.loadAdditionalModel(var8, var9, var23, var10);
                  }

                  var24 = ModelManager.instance.getLoadedModel(var8, var9, var23, var10);
                  if (var24 != null && var24.isReady()) {
                     WorldItemModelDrawer var27 = (WorldItemModelDrawer)s_modelDrawerPool.alloc();
                     float var28 = 1.0F;
                     ImmutableColor var29 = ImmutableColor.white;
                     var27.init(var0, var1, var2, var3, var4, var24, var28, var29, var5);
                     if (var7.scale != 1.0F) {
                        var27.m_transform.scale(var7.scale);
                     }

                     if (var0.worldScale != 1.0F) {
                        var27.m_transform.scale(var7.scale * var0.worldScale);
                     }

                     var27.m_angle.x = 0.0F;
                     if (!NEW_WAY) {
                        var27.m_angle.y = 180.0F;
                     }

                     if (NEW_WAY) {
                        s_attachmentXfrm.identity();
                        s_attachmentXfrm.rotateXYZ(0.0F, 3.1415927F, 1.5707964F);
                        s_attachmentXfrm.invert();
                        var27.m_transform.mul((Matrix4fc)s_attachmentXfrm);
                     }

                     ModelAttachment var16 = var7.getAttachmentById("world");
                     if (var16 != null) {
                        ModelInstanceRenderData.makeAttachmentTransform(var16, s_attachmentXfrm);
                        s_attachmentXfrm.invert();
                        var27.m_transform.mul((Matrix4fc)s_attachmentXfrm);
                     }

                     if (var6 < 0.0F) {
                        var27.m_angle.y = (float)var0.worldZRotation;
                     } else {
                        var27.m_angle.y = var6;
                     }

                     SpriteRenderer.instance.drawGeneric(var27);
                     return true;
                  }
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private void init(InventoryItem var1, IsoGridSquare var2, float var3, float var4, float var5, Model var6, float var7, ImmutableColor var8, float var9) {
      this.m_model = var6;
      this.m_tintR = var8.r;
      this.m_tintG = var8.g;
      this.m_tintB = var8.b;
      this.m_hue = var7;
      this.m_x = var3;
      this.m_y = var4;
      this.m_z = var5;
      this.m_transform.rotationZ((90.0F + var9) * 0.017453292F);
      if (var1 instanceof Clothing) {
         float var10 = -0.08F;
         float var11 = 0.05F;
         this.m_transform.translate(var10, 0.0F, var11);
      }

      this.m_angle.x = 0.0F;
      this.m_angle.y = 525.0F;
      this.m_angle.z = 0.0F;
      if (NEW_WAY) {
         this.m_transform.identity();
         this.m_angle.y = 0.0F;
         if (var6.Mesh != null && var6.Mesh.isReady() && var6.Mesh.m_transform != null) {
            var6.Mesh.m_transform.transpose();
            this.m_transform.mul((Matrix4fc)var6.Mesh.m_transform);
            var6.Mesh.m_transform.transpose();
         }
      }

      var2.interpolateLight(tempColorInfo, this.m_x % 1.0F, this.m_y % 1.0F);
      if (GameServer.bServer && ServerGUI.isCreated()) {
         tempColorInfo.set(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.m_ambientR = tempColorInfo.r;
      this.m_ambientG = tempColorInfo.g;
      this.m_ambientB = tempColorInfo.b;
   }

   public void render() {
      if (this.m_model.bStatic) {
         Model var1 = this.m_model;
         if (var1.Effect == null) {
            var1.CreateShader("basicEffect");
         }

         Shader var2 = var1.Effect;
         if (var2 != null && var1.Mesh != null && var1.Mesh.isReady()) {
            this.alpha = 1.0F;
            GL11.glPushAttrib(1048575);
            GL11.glPushClientAttrib(-1);
            Core.getInstance().DoPushIsoStuff(this.m_x, this.m_y, this.m_z, 0.0F, false);
            GL11.glRotated(-180.0D, 0.0D, 1.0D, 0.0D);
            GL11.glRotated((double)this.m_angle.x, 1.0D, 0.0D, 0.0D);
            GL11.glRotated((double)this.m_angle.y, 0.0D, 1.0D, 0.0D);
            GL11.glRotated((double)this.m_angle.z, 0.0D, 0.0D, 1.0D);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthFunc(513);
            GL11.glDepthMask(true);
            GL11.glDepthRange(0.0D, 1.0D);
            GL11.glEnable(2929);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            var2.Start();
            if (var1.tex != null) {
               var2.setTexture(var1.tex, "Texture", 0);
            }

            var2.setDepthBias(0.0F);
            var2.setAmbient(this.m_ambientR * 0.4F, this.m_ambientG * 0.4F, this.m_ambientB * 0.4F);
            var2.setLightingAmount(1.0F);
            var2.setHueShift(this.m_hue);
            var2.setTint(this.m_tintR, this.m_tintG, this.m_tintB);
            var2.setAlpha(this.alpha);

            for(int var3 = 0; var3 < 5; ++var3) {
               var2.setLight(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, 0.0F, 0.0F, 0.0F, (IsoMovingObject)null);
            }

            Vector3f var5 = temprot;
            var5.x = 0.0F;
            var5.y = 5.0F;
            var5.z = -2.0F;
            var5.rotateY((float)Math.toRadians((double)this.m_angle.y));
            float var4 = 1.5F;
            var2.setLight(4, var5.x, var5.z, var5.y, this.m_ambientR / 4.0F * var4, this.m_ambientG / 4.0F * var4, this.m_ambientB / 4.0F * var4, 5000.0F, Float.NaN, 0.0F, 0.0F, 0.0F, (IsoMovingObject)null);
            var2.setTransformMatrix(this.m_transform, false);
            var1.Mesh.Draw(var2);
            var2.End();
            if (Core.bDebug && DebugOptions.instance.ModelRenderAxis.getValue()) {
               Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 0.5F, 1.0F);
            }

            Core.getInstance().DoPopIsoStuff();
            GL11.glPopAttrib();
            GL11.glPopClientAttrib();
            Texture.lastTextureID = -1;
            SpriteRenderer.ringBuffer.restoreBoundTextures = true;
            SpriteRenderer.ringBuffer.restoreVBOs = true;
         }
      }
   }

   public void postRender() {
      s_modelDrawerPool.release((Object)this);
   }
}
