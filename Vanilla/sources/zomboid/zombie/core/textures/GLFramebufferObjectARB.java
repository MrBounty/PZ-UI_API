package zombie.core.textures;

import org.lwjgl.opengl.ARBFramebufferObject;

public final class GLFramebufferObjectARB implements IGLFramebufferObject {
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
      return 33305;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT() {
      return 36054;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT() {
      return 36055;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS() {
      return 0;
   }

   public int GL_FRAMEBUFFER_INCOMPLETE_FORMATS() {
      return 0;
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
      return 36182;
   }

   public int glGenFramebuffers() {
      return ARBFramebufferObject.glGenFramebuffers();
   }

   public void glBindFramebuffer(int var1, int var2) {
      ARBFramebufferObject.glBindFramebuffer(var1, var2);
   }

   public void glFramebufferTexture2D(int var1, int var2, int var3, int var4, int var5) {
      ARBFramebufferObject.glFramebufferTexture2D(var1, var2, var3, var4, var5);
   }

   public int glGenRenderbuffers() {
      return ARBFramebufferObject.glGenRenderbuffers();
   }

   public void glBindRenderbuffer(int var1, int var2) {
      ARBFramebufferObject.glBindRenderbuffer(var1, var2);
   }

   public void glRenderbufferStorage(int var1, int var2, int var3, int var4) {
      ARBFramebufferObject.glRenderbufferStorage(var1, var2, var3, var4);
   }

   public void glFramebufferRenderbuffer(int var1, int var2, int var3, int var4) {
      ARBFramebufferObject.glFramebufferRenderbuffer(var1, var2, var3, var4);
   }

   public int glCheckFramebufferStatus(int var1) {
      return ARBFramebufferObject.glCheckFramebufferStatus(var1);
   }

   public void glDeleteFramebuffers(int var1) {
      ARBFramebufferObject.glDeleteFramebuffers(var1);
   }

   public void glDeleteRenderbuffers(int var1) {
      ARBFramebufferObject.glDeleteRenderbuffers(var1);
   }
}
