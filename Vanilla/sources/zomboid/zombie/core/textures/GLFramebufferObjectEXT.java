package zombie.core.textures;

import org.lwjgl.opengl.EXTFramebufferObject;

public final class GLFramebufferObjectEXT implements IGLFramebufferObject {
   public int GL_FRAMEBUFFER() {
      return 36160;
   }

   public int GL_RENDERBUFFER() {
      return 36161;
   }

   public int GL_COLOR_ATTACHMENT0() {
      return 36064;
   }

   public int GL_DEPTH_ATTACHMENT() {
      return 36096;
   }

   public int GL_STENCIL_ATTACHMENT() {
      return 36128;
   }

   public int GL_DEPTH_STENCIL() {
      return 34041;
   }

   public int GL_DEPTH24_STENCIL8() {
      return 35056;
   }

   public int GL_FRAMEBUFFER_COMPLETE() {
      return 36053;
   }

   public int GL_FRAMEBUFFER_UNDEFINED() {
      return 0;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT() {
      return 36054;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT() {
      return 36055;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS() {
      return 36057;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_FORMATS() {
      return 36058;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER() {
      return 36059;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER() {
      return 36060;
   }

   public int GL_FRAMEBUFFER_UNSUPPORTED() {
      return 36061;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE() {
      return 0;
   }

   public int glGenFramebuffers() {
      return EXTFramebufferObject.glGenFramebuffersEXT();
   }

   public void glBindFramebuffer(int var1, int var2) {
      EXTFramebufferObject.glBindFramebufferEXT(var1, var2);
   }

   public void glFramebufferTexture2D(int var1, int var2, int var3, int var4, int var5) {
      EXTFramebufferObject.glFramebufferTexture2DEXT(var1, var2, var3, var4, var5);
   }

   public int glGenRenderbuffers() {
      return EXTFramebufferObject.glGenRenderbuffersEXT();
   }

   public void glBindRenderbuffer(int var1, int var2) {
      EXTFramebufferObject.glBindRenderbufferEXT(var1, var2);
   }

   public void glRenderbufferStorage(int var1, int var2, int var3, int var4) {
      EXTFramebufferObject.glRenderbufferStorageEXT(var1, var2, var3, var4);
   }

   public void glFramebufferRenderbuffer(int var1, int var2, int var3, int var4) {
      EXTFramebufferObject.glFramebufferRenderbufferEXT(var1, var2, var3, var4);
   }

   public int glCheckFramebufferStatus(int var1) {
      return EXTFramebufferObject.glCheckFramebufferStatusEXT(var1);
   }

   public void glDeleteFramebuffers(int var1) {
      EXTFramebufferObject.glDeleteFramebuffersEXT(var1);
   }

   public void glDeleteRenderbuffers(int var1) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(var1);
   }
}
