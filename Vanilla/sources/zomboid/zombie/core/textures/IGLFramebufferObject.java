package zombie.core.textures;

public interface IGLFramebufferObject {
   int GL_FRAMEBUFFER();

   int GL_RENDERBUFFER();

   int GL_COLOR_ATTACHMENT0();

   int GL_DEPTH_ATTACHMENT();

   int GL_STENCIL_ATTACHMENT();

   int GL_DEPTH_STENCIL();

   int GL_DEPTH24_STENCIL8();

   int GL_FRAMEBUFFER_COMPLETE();

   int GL_FRAMEBUFFER_UNDEFINED();

   int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT();

   int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT();

   int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS();

   int GL_FRAMEBUFFER_INCOMPLETE_FORMATS();

   int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER();

   int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER();

   int GL_FRAMEBUFFER_UNSUPPORTED();

   int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE();

   int glGenFramebuffers();

   void glBindFramebuffer(int var1, int var2);

   void glFramebufferTexture2D(int var1, int var2, int var3, int var4, int var5);

   int glGenRenderbuffers();

   void glBindRenderbuffer(int var1, int var2);

   void glRenderbufferStorage(int var1, int var2, int var3, int var4);

   void glFramebufferRenderbuffer(int var1, int var2, int var3, int var4);

   int glCheckFramebufferStatus(int var1);

   void glDeleteFramebuffers(int var1);

   void glDeleteRenderbuffers(int var1);
}
