package zombie.core.skinnedmodel.model;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjglx.BufferUtils;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.skinnedmodel.Vector3;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.textures.Texture;
import zombie.creative.creativerects.OpenSimplexNoise;
import zombie.iso.IsoCamera;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;

public final class HeightTerrain {
   private final ByteBuffer buffer;
   public VertexBufferObject vb;
   public static float isoAngle = 62.65607F;
   public static float scale = 0.047085002F;
   OpenSimplexNoise noise = new OpenSimplexNoise((long)Rand.Next(10000000));
   static float[] lightAmbient = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] lightDiffuse = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] lightPosition = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] specular = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] shininess = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] emission = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] ambient = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static float[] diffuse = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   static ByteBuffer temp = ByteBuffer.allocateDirect(16);

   public HeightTerrain(int var1, int var2) {
      ArrayList var5 = new ArrayList();
      int var6 = var1 * var2;
      int var7 = var1;
      int var8 = var2;
      ArrayList var9 = new ArrayList();
      Vector2 var10 = new Vector2(2.0F, 0.0F);
      boolean var11 = false;

      int var12;
      int var13;
      float var14;
      VertexPositionNormalTangentTextureSkin var15;
      for(var12 = 0; var12 < var7; ++var12) {
         for(var13 = 0; var13 < var8; ++var13) {
            var14 = (float)this.calc((float)var12, (float)var13);
            var14 *= 1.0F;
            ++var14;
            var15 = null;
            var15 = new VertexPositionNormalTangentTextureSkin();
            var15.Position = new Vector3();
            var15.Position.set((float)(-var12), var14 * 30.0F, (float)(-var13));
            var15.Normal = new Vector3();
            var15.Normal.set(0.0F, 1.0F, 0.0F);
            var15.Normal.normalize();
            var15.TextureCoordinates = new Vector2();
            var15.TextureCoordinates = new Vector2((float)var12 / (float)(var7 - 1) * 16.0F, (float)var13 / (float)(var8 - 1) * 16.0F);
            var5.add(var15);
         }
      }

      int var27 = 0;

      for(var12 = 0; var12 < var7; ++var12) {
         for(var13 = 0; var13 < var8; ++var13) {
            var14 = (float)this.calc((float)var12, (float)var13);
            var14 *= 1.0F;
            ++var14;
            var14 = Math.max(0.0F, var14);
            var14 = Math.min(1.0F, var14);
            var15 = null;
            var15 = (VertexPositionNormalTangentTextureSkin)var5.get(var27);
            Vector3 var16 = new Vector3();
            Vector3 var17 = new Vector3();
            float var18 = (float)this.calc((float)(var12 + 1), (float)var13);
            var18 *= 1.0F;
            ++var18;
            float var19 = (float)this.calc((float)(var12 - 1), (float)var13);
            var19 *= 1.0F;
            ++var19;
            float var20 = (float)this.calc((float)var12, (float)(var13 + 1));
            var20 *= 1.0F;
            ++var20;
            float var21 = (float)this.calc((float)var12, (float)(var13 - 1));
            var21 *= 1.0F;
            ++var21;
            float var22 = var18 * 700.0F;
            float var23 = var19 * 700.0F;
            float var24 = var20 * 700.0F;
            float var25 = var21 * 700.0F;
            var16.set(var10.x, var10.y, var22 - var23);
            var17.set(var10.y, var10.x, var24 - var25);
            var16.normalize();
            var17.normalize();
            Vector3 var26 = var16.cross(var17);
            var15.Normal.x(var26.x());
            var15.Normal.y(var26.z());
            var15.Normal.z(var26.y());
            var15.Normal.normalize();
            PrintStream var10000 = System.out;
            float var10001 = var15.Normal.x();
            var10000.println(var10001 + " , " + var15.Normal.y() + ", " + var15.Normal.z());
            var15.Normal.normalize();
            ++var27;
         }
      }

      var27 = 0;

      for(var12 = 0; var12 < var8 - 1; ++var12) {
         if ((var12 & 1) == 0) {
            for(var13 = 0; var13 < var7; ++var13) {
               var9.add(var13 + (var12 + 1) * var7);
               var9.add(var13 + var12 * var7);
               ++var27;
               ++var27;
            }
         } else {
            for(var13 = var7 - 1; var13 > 0; --var13) {
               var9.add(var13 - 1 + var12 * var7);
               var9.add(var13 + (var12 + 1) * var7);
               ++var27;
               ++var27;
            }
         }
      }

      if ((var7 & 1) > 0 && var8 > 2) {
         var9.add((var8 - 1) * var7);
         ++var27;
      }

      this.vb = new VertexBufferObject();
      ByteBuffer var28 = BufferUtils.createByteBuffer(var5.size() * 36);

      for(var13 = 0; var13 < var5.size(); ++var13) {
         VertexPositionNormalTangentTextureSkin var31 = (VertexPositionNormalTangentTextureSkin)var5.get(var13);
         var28.putFloat(var31.Position.x());
         var28.putFloat(var31.Position.y());
         var28.putFloat(var31.Position.z());
         var28.putFloat(var31.Normal.x());
         var28.putFloat(var31.Normal.y());
         var28.putFloat(var31.Normal.z());
         byte var30 = -1;
         var28.putInt(var30);
         var28.putFloat(var31.TextureCoordinates.x);
         var28.putFloat(var31.TextureCoordinates.y);
      }

      var28.flip();
      int[] var29 = new int[var9.size()];

      for(int var33 = 0; var33 < var9.size(); ++var33) {
         Integer var32 = (Integer)var9.get(var9.size() - 1 - var33);
         var29[var33] = var32;
      }

      this.vb._handle = this.vb.LoadSoftwareVBO(var28, this.vb._handle, var29);
      this.buffer = var28;
   }

   double calcTerrain(float var1, float var2) {
      var1 *= 10.0F;
      var2 *= 10.0F;
      double var3 = this.noise.eval((double)(var1 / 900.0F), (double)(var2 / 600.0F), 0.0D);
      var3 += this.noise.eval((double)(var1 / 600.0F), (double)(var2 / 600.0F), 0.0D) / 4.0D;
      var3 += (this.noise.eval((double)(var1 / 300.0F), (double)(var2 / 300.0F), 0.0D) + 1.0D) / 8.0D;
      var3 += (this.noise.eval((double)(var1 / 150.0F), (double)(var2 / 150.0F), 0.0D) + 1.0D) / 16.0D;
      var3 += (this.noise.eval((double)(var1 / 75.0F), (double)(var2 / 75.0F), 0.0D) + 1.0D) / 32.0D;
      double var5 = (this.noise.eval((double)var1, (double)var2, 0.0D) + 1.0D) / 2.0D;
      double var10000 = var5 * ((this.noise.eval((double)var1, (double)var2, 0.0D) + 1.0D) / 2.0D);
      return var3;
   }

   double calc(float var1, float var2) {
      return this.calcTerrain(var1, var2);
   }

   public void pushView(int var1, int var2, int var3) {
      GL11.glDepthMask(false);
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      float var4 = 0.6F;
      byte var5 = 0;
      byte var6 = 0;
      int var7 = var5 + IsoCamera.getOffscreenWidth(IsoPlayer.getPlayerIndex());
      int var8 = var6 + IsoCamera.getOffscreenHeight(IsoPlayer.getPlayerIndex());
      double var9 = (double)IsoUtils.XToIso((float)var5, (float)var6, 0.0F);
      double var11 = (double)IsoUtils.YToIso(0.0F, 0.0F, 0.0F);
      double var13 = (double)IsoUtils.XToIso((float)Core.getInstance().getOffscreenWidth(IsoPlayer.getPlayerIndex()), 0.0F, 0.0F);
      double var15 = (double)IsoUtils.YToIso((float)var7, (float)var6, 0.0F);
      double var17 = (double)IsoUtils.XToIso((float)var7, (float)var8, 0.0F);
      double var19 = (double)IsoUtils.YToIso((float)Core.getInstance().getOffscreenWidth(IsoPlayer.getPlayerIndex()), (float)Core.getInstance().getOffscreenHeight(IsoPlayer.getPlayerIndex()), 6.0F);
      double var21 = (double)IsoUtils.XToIso(-128.0F, (float)Core.getInstance().getOffscreenHeight(IsoPlayer.getPlayerIndex()), 6.0F);
      double var23 = (double)IsoUtils.YToIso((float)var5, (float)var8, 0.0F);
      double var10000 = var17 - var9;
      var10000 = var23 - var15;
      double var25 = (double)((float)Math.abs(Core.getInstance().getOffscreenWidth(0)) / 1920.0F);
      double var27 = (double)((float)Math.abs(Core.getInstance().getOffscreenHeight(0)) / 1080.0F);
      GL11.glLoadIdentity();
      GL11.glOrtho(-var25 / 2.0D, var25 / 2.0D, -var27 / 2.0D, var27 / 2.0D, -10.0D, 10.0D);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glScaled((double)scale, (double)scale, (double)scale);
      GL11.glRotatef(isoAngle, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslated((double)(IsoWorld.instance.CurrentCell.ChunkMap[0].getWidthInTiles() / 2), 0.0D, (double)(IsoWorld.instance.CurrentCell.ChunkMap[0].getWidthInTiles() / 2));
      GL11.glDepthRange(-100.0D, 100.0D);
   }

   public void popView() {
      GL11.glEnable(3008);
      GL11.glDepthFunc(519);
      GL11.glDepthMask(false);
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
   }

   public void render() {
      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glDisable(2884);
      GL11.glEnable(2929);
      GL11.glDepthFunc(519);
      GL11.glColorMask(true, true, true, true);
      GL11.glAlphaFunc(519, 0.0F);
      GL11.glDepthFunc(519);
      GL11.glDepthRange(-10.0D, 10.0D);
      GL11.glEnable(2903);
      GL11.glEnable(2896);
      GL11.glEnable(16384);
      GL11.glEnable(16385);
      GL11.glEnable(2929);
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glAlphaFunc(519, 0.0F);
      GL11.glDisable(3089);
      this.doLighting();
      GL11.glDisable(2929);
      GL11.glEnable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glCullFace(1029);
      this.pushView(IsoPlayer.getInstance().getCurrentSquare().getChunk().wx / 30 * 300, IsoPlayer.getInstance().getCurrentSquare().getChunk().wy / 30 * 300, 0);
      Texture.getSharedTexture("media/textures/grass.png").bind();
      this.vb.DrawStrip((Shader)null);
      this.popView();
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glDisable(2929);
      GL11.glEnable(6144);
      if (PerformanceSettings.ModelLighting) {
         GL11.glDisable(2903);
         GL11.glDisable(2896);
         GL11.glDisable(16384);
         GL11.glDisable(16385);
      }

      GL11.glDepthRange(0.0D, 100.0D);
      SpriteRenderer.ringBuffer.restoreVBOs = true;
      GL11.glEnable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glEnable(3553);
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
   }

   private void doLighting() {
      temp.order(ByteOrder.nativeOrder());
      temp.clear();
      GL11.glColorMaterial(1032, 5634);
      GL11.glDisable(2903);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2896);
      GL11.glEnable(16384);
      GL11.glDisable(16385);
      lightAmbient[0] = 0.7F;
      lightAmbient[1] = 0.7F;
      lightAmbient[2] = 0.7F;
      lightAmbient[3] = 0.5F;
      lightDiffuse[0] = 0.5F;
      lightDiffuse[1] = 0.5F;
      lightDiffuse[2] = 0.5F;
      lightDiffuse[3] = 1.0F;
      Vector3 var1 = new Vector3(1.0F, 1.0F, 1.0F);
      var1.normalize();
      lightPosition[0] = -var1.x();
      lightPosition[1] = var1.y();
      lightPosition[2] = -var1.z();
      lightPosition[3] = 0.0F;
      GL11.glLightfv(16384, 4608, temp.asFloatBuffer().put(lightAmbient).flip());
      GL11.glLightfv(16384, 4609, temp.asFloatBuffer().put(lightDiffuse).flip());
      GL11.glLightfv(16384, 4611, temp.asFloatBuffer().put(lightPosition).flip());
      GL11.glLightf(16384, 4615, 0.0F);
      GL11.glLightf(16384, 4616, 0.0F);
      GL11.glLightf(16384, 4617, 0.0F);
      specular[0] = 0.0F;
      specular[1] = 0.0F;
      specular[2] = 0.0F;
      specular[3] = 0.0F;
      GL11.glMaterialfv(1032, 4610, temp.asFloatBuffer().put(specular).flip());
      GL11.glMaterialfv(1032, 5633, temp.asFloatBuffer().put(specular).flip());
      GL11.glMaterialfv(1032, 5632, temp.asFloatBuffer().put(specular).flip());
      ambient[0] = 0.6F;
      ambient[1] = 0.6F;
      ambient[2] = 0.6F;
      ambient[3] = 1.0F;
      diffuse[0] = 0.6F;
      diffuse[1] = 0.6F;
      diffuse[2] = 0.6F;
      diffuse[3] = 0.6F;
      GL11.glMaterialfv(1032, 4608, temp.asFloatBuffer().put(ambient).flip());
      GL11.glMaterialfv(1032, 4609, temp.asFloatBuffer().put(diffuse).flip());
   }
}
