package zombie.core.textures;

import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjglx.opengl.OpenGLException;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.PZGLUtil;
import zombie.core.utils.ImageUtils;

public final class TextureCombiner {
   public static final TextureCombiner instance = new TextureCombiner();
   public static int count = 0;
   private TextureFBO fbo;
   private final float m_coordinateSpaceMax = 256.0F;
   private final ArrayList fboPool = new ArrayList();

   public void init() throws Exception {
   }

   public void combineStart() {
      this.clear();
      count = 33984;
      GL13.glEnable(3042);
      GL13.glEnable(3553);
      GL13.glTexEnvi(8960, 8704, 7681);
   }

   public void combineEnd() {
      GL13.glActiveTexture(33984);
   }

   public void clear() {
      for(int var1 = 33985; var1 <= count; ++var1) {
         GL13.glActiveTexture(var1);
         GL13.glDisable(3553);
      }

      GL13.glActiveTexture(33984);
   }

   public void overlay(Texture var1) {
      GL13.glActiveTexture(count);
      GL13.glEnable(3553);
      GL13.glEnable(3042);
      var1.bind();
      if (count > 33984) {
         GL13.glTexEnvi(8960, 8704, 34160);
         GL13.glTexEnvi(8960, 34161, 34165);
         GL13.glTexEnvi(8960, 34176, 34168);
         GL13.glTexEnvi(8960, 34177, 5890);
         GL13.glTexEnvi(8960, 34178, 34168);
         GL13.glTexEnvi(8960, 34192, 768);
         GL13.glTexEnvi(8960, 34193, 768);
         GL13.glTexEnvi(8960, 34194, 770);
         GL13.glTexEnvi(8960, 34162, 34165);
         GL13.glTexEnvi(8960, 34184, 34168);
         GL13.glTexEnvi(8960, 34185, 5890);
         GL13.glTexEnvi(8960, 34186, 34168);
         GL13.glTexEnvi(8960, 34200, 770);
         GL13.glTexEnvi(8960, 34201, 770);
         GL13.glTexEnvi(8960, 34202, 770);
      }

      ++count;
   }

   public Texture combine(Texture var1, Texture var2) throws Exception {
      Core.getInstance().DoStartFrameStuff(var1.width, var2.width, 1.0F, 0);
      Texture var3 = new Texture(var1.width, var2.height, 16);
      if (this.fbo == null) {
         this.fbo = new TextureFBO(var3);
      } else {
         this.fbo.setTexture(var3);
      }

      GL13.glActiveTexture(33984);
      GL13.glEnable(3553);
      GL13.glBindTexture(3553, var1.getID());
      this.fbo.startDrawing(true, true);
      GL13.glBegin(7);
      GL13.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL13.glTexCoord2f(0.0F, 0.0F);
      GL13.glVertex2d(0.0D, 0.0D);
      GL13.glTexCoord2f(0.0F, 1.0F);
      GL13.glVertex2d(0.0D, (double)var1.height);
      GL13.glTexCoord2f(1.0F, 1.0F);
      GL13.glVertex2d((double)var1.width, (double)var1.height);
      GL13.glTexCoord2f(1.0F, 0.0F);
      GL13.glVertex2d((double)var1.width, 0.0D);
      GL13.glEnd();
      GL13.glBindTexture(3553, var2.getID());
      GL13.glBegin(7);
      GL13.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL13.glTexCoord2f(0.0F, 0.0F);
      GL13.glVertex2d(0.0D, 0.0D);
      GL13.glTexCoord2f(0.0F, 1.0F);
      GL13.glVertex2d(0.0D, (double)var1.height);
      GL13.glTexCoord2f(1.0F, 1.0F);
      GL13.glVertex2d((double)var1.width, (double)var1.height);
      GL13.glTexCoord2f(1.0F, 0.0F);
      GL13.glVertex2d((double)var1.width, 0.0D);
      GL13.glEnd();
      this.fbo.endDrawing();
      Core.getInstance().DoEndFrameStuff(var1.width, var2.width);
      return var3;
   }

   public static int[] flipPixels(int[] var0, int var1, int var2) {
      int[] var3 = null;
      if (var0 != null) {
         var3 = new int[var1 * var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
               var3[(var2 - var4 - 1) * var1 + var5] = var0[var4 * var1 + var5];
            }
         }
      }

      return var3;
   }

   private TextureCombiner.CombinerFBO getFBO(int var1, int var2) {
      for(int var3 = 0; var3 < this.fboPool.size(); ++var3) {
         TextureCombiner.CombinerFBO var4 = (TextureCombiner.CombinerFBO)this.fboPool.get(var3);
         if (var4.fbo.getWidth() == var1 && var4.fbo.getHeight() == var2) {
            return var4;
         }
      }

      return null;
   }

   private Texture createTexture(int var1, int var2) {
      TextureCombiner.CombinerFBO var3 = this.getFBO(var1, var2);
      Texture var4;
      if (var3 == null) {
         var3 = new TextureCombiner.CombinerFBO();
         var4 = new Texture(var1, var2, 16);
         var3.fbo = new TextureFBO(var4);
         this.fboPool.add(var3);
      } else {
         var4 = var3.textures.isEmpty() ? new Texture(var1, var2, 16) : (Texture)var3.textures.pop();
         var4.bind();
         GL11.glTexImage2D(3553, 0, 6408, var4.getWidthHW(), var4.getHeightHW(), 0, 6408, 5121, (IntBuffer)null);
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10241, 9729);
         var4.dataid.setMinFilter(9729);
         Texture.lastTextureID = 0;
         GL13.glBindTexture(3553, 0);
         var3.fbo.setTexture(var4);
      }

      this.fbo = var3.fbo;
      return var4;
   }

   public void releaseTexture(Texture var1) {
      TextureCombiner.CombinerFBO var2 = this.getFBO(var1.getWidth(), var1.getHeight());
      if (var2 != null && var2.textures.size() < 100) {
         var2.textures.push(var1);
      } else {
         var1.destroy();
      }

   }

   public Texture combine(ArrayList var1) throws Exception, OpenGLException {
      PZGLUtil.checkGLErrorThrow("Enter");
      int var2 = getResultingWidth(var1);
      int var3 = getResultingHeight(var1);
      Texture var4 = this.createTexture(var2, var3);
      GL13.glPushAttrib(24576);
      GL11.glDisable(3089);
      GL11.glDisable(2960);
      this.fbo.startDrawing(true, true);
      PZGLUtil.checkGLErrorThrow("FBO.startDrawing %s", this.fbo);
      Core.getInstance().DoStartFrameStuffSmartTextureFx(var2, var3, -1);
      PZGLUtil.checkGLErrorThrow("Core.DoStartFrameStuffFx w:%d, h:%d", var2, var3);

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         TextureCombinerCommand var6 = (TextureCombinerCommand)var1.get(var5);
         if (var6.shader != null) {
            var6.shader.Start();
         }

         GL13.glActiveTexture(33984);
         GL11.glEnable(3553);
         Texture var7 = var6.tex == null ? Texture.getErrorTexture() : var6.tex;
         var7.bind();
         if (var6.mask != null) {
            GL13.glActiveTexture(33985);
            GL13.glEnable(3553);
            int var8 = Texture.lastTextureID;
            if (var6.mask.getTextureId() != null) {
               var6.mask.getTextureId().setMagFilter(9728);
               var6.mask.getTextureId().setMinFilter(9728);
            }

            var6.mask.bind();
            Texture.lastTextureID = var8;
         } else {
            GL13.glActiveTexture(33985);
            GL13.glDisable(3553);
         }

         if (var6.shader != null) {
            if (var6.shaderParams != null) {
               ArrayList var12 = var6.shaderParams;

               for(int var9 = 0; var9 < var12.size(); ++var9) {
                  TextureCombinerShaderParam var10 = (TextureCombinerShaderParam)var12.get(var9);
                  float var11 = Rand.Next(var10.min, var10.max);
                  var6.shader.setValue(var10.name, var11);
               }
            }

            var6.shader.setValue("DIFFUSE", var7, 0);
            if (var6.mask != null) {
               var6.shader.setValue("MASK", var6.mask, 1);
            }
         }

         GL13.glBlendFunc(var6.blendSrc, var6.blendDest);
         if (var6.x != -1) {
            float var13 = (float)var2 / 256.0F;
            float var14 = (float)var3 / 256.0F;
            GL13.glBegin(7);
            GL13.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL13.glTexCoord2f(0.0F, 1.0F);
            GL13.glVertex2d((double)((float)var6.x * var13), (double)((float)var6.y * var14));
            GL13.glTexCoord2f(0.0F, 0.0F);
            GL13.glVertex2d((double)((float)var6.x * var13), (double)((float)(var6.y + var6.h) * var14));
            GL13.glTexCoord2f(1.0F, 0.0F);
            GL13.glVertex2d((double)((float)(var6.x + var6.w) * var13), (double)((float)(var6.y + var6.h) * var14));
            GL13.glTexCoord2f(1.0F, 1.0F);
            GL13.glVertex2d((double)((float)(var6.x + var6.w) * var13), (double)((float)var6.y * var14));
            GL13.glEnd();
         } else {
            GL13.glBegin(7);
            GL13.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL13.glTexCoord2f(0.0F, 1.0F);
            GL13.glVertex2d(0.0D, 0.0D);
            GL13.glTexCoord2f(0.0F, 0.0F);
            GL13.glVertex2d(0.0D, (double)var3);
            GL13.glTexCoord2f(1.0F, 0.0F);
            GL13.glVertex2d((double)var2, (double)var3);
            GL13.glTexCoord2f(1.0F, 1.0F);
            GL13.glVertex2d((double)var2, 0.0D);
            GL13.glEnd();
         }

         if (var6.shader != null) {
            var6.shader.End();
         }

         PZGLUtil.checkGLErrorThrow("TextureCombinerCommand[%d}: %s", var5, var6);
      }

      Core.getInstance().DoEndFrameStuffFx(var2, var3, -1);
      this.fbo.releaseTexture();
      this.fbo.endDrawing();
      PZGLUtil.checkGLErrorThrow("FBO.endDrawing: %s", this.fbo);
      GL13.glBlendFunc(770, 771);
      GL13.glActiveTexture(33985);
      GL13.glDisable(3553);
      if (Core.OptionModelTextureMipmaps) {
      }

      GL13.glActiveTexture(33984);
      Texture.lastTextureID = 0;
      GL13.glBindTexture(3553, 0);
      SpriteRenderer.ringBuffer.restoreBoundTextures = true;
      GL13.glPopAttrib();
      PZGLUtil.checkGLErrorThrow("Exit.");
      return var4;
   }

   public static int getResultingHeight(ArrayList var0) {
      if (var0.isEmpty()) {
         return 32;
      } else {
         TextureCombinerCommand var1 = findDominantCommand(var0, Comparator.comparingInt((var0x) -> {
            return var0x.tex.height;
         }));
         if (var1 == null) {
            return 32;
         } else {
            Texture var2 = var1.tex;
            return ImageUtils.getNextPowerOfTwoHW(var2.height);
         }
      }
   }

   public static int getResultingWidth(ArrayList var0) {
      if (var0.isEmpty()) {
         return 32;
      } else {
         TextureCombinerCommand var1 = findDominantCommand(var0, Comparator.comparingInt((var0x) -> {
            return var0x.tex.width;
         }));
         if (var1 == null) {
            return 32;
         } else {
            Texture var2 = var1.tex;
            return ImageUtils.getNextPowerOfTwoHW(var2.width);
         }
      }
   }

   private static TextureCombinerCommand findDominantCommand(ArrayList var0, Comparator var1) {
      TextureCombinerCommand var2 = null;
      int var3 = var0.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TextureCombinerCommand var5 = (TextureCombinerCommand)var0.get(var4);
         if (var5.tex != null && (var2 == null || var1.compare(var5, var2) > 0)) {
            var2 = var5;
         }
      }

      return var2;
   }

   private void createMipMaps(Texture var1) {
      if (GL.getCapabilities().OpenGL30) {
         GL13.glActiveTexture(33984);
         var1.bind();
         GL30.glGenerateMipmap(3553);
         short var2 = 9987;
         GL11.glTexParameteri(3553, 10241, var2);
         var1.dataid.setMinFilter(var2);
      }
   }

   private static final class CombinerFBO {
      TextureFBO fbo;
      final ArrayDeque textures = new ArrayDeque();
   }
}
