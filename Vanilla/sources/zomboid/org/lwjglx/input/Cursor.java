package org.lwjglx.input;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjglx.BufferUtils;
import org.lwjglx.LWJGLException;

public class Cursor {
   public static final int CURSOR_ONE_BIT_TRANSPARENCY = 1;
   public static final int CURSOR_8_BIT_ALPHA = 2;
   public static final int CURSOR_ANIMATION = 4;
   private long cursorHandle;

   public Cursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException {
      if (var5 != 1) {
         System.out.println("ANIMATED CURSORS NOT YET SUPPORTED IN LWJGLX");
      } else {
         IntBuffer var8 = BufferUtils.createIntBuffer(var6.limit());
         flipImages(var1, var2, var5, var6, var8);
         ByteBuffer var9 = convertARGBIntBuffertoRGBAByteBuffer(var1, var2, var8);
         GLFWImage var10 = GLFWImage.malloc();
         var10.width(var1);
         var10.height(var2);
         var10.pixels(var9);
         this.cursorHandle = GLFW.glfwCreateCursor(var10, var3, var4);
         if (this.cursorHandle == 0L) {
            throw new RuntimeException("Error creating GLFW cursor");
         }
      }
   }

   private static ByteBuffer convertARGBIntBuffertoRGBAByteBuffer(int var0, int var1, IntBuffer var2) {
      ByteBuffer var3 = BufferUtils.createByteBuffer(var0 * var1 * 4);

      for(int var4 = 0; var4 < var2.limit(); ++var4) {
         int var5 = var2.get(var4);
         byte var6 = (byte)(var5 >>> 24);
         byte var7 = (byte)(var5 >>> 16);
         byte var8 = (byte)(var5 >>> 8);
         byte var9 = (byte)var5;
         var3.put(var9);
         var3.put(var8);
         var3.put(var7);
         var3.put(var6);
      }

      var3.flip();
      return var3;
   }

   public static int getMinCursorSize() {
      return 1;
   }

   public static int getMaxCursorSize() {
      return 512;
   }

   public static int getCapabilities() {
      return 2;
   }

   private static void flipImages(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      for(int var5 = 0; var5 < var2; ++var5) {
         int var6 = var5 * var0 * var1;
         flipImage(var0, var1, var6, var3, var4);
      }

   }

   private static void flipImage(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      for(int var5 = 0; var5 < var1 >> 1; ++var5) {
         int var6 = var5 * var0 + var2;
         int var7 = (var1 - var5 - 1) * var0 + var2;

         for(int var8 = 0; var8 < var0; ++var8) {
            int var9 = var6 + var8;
            int var10 = var7 + var8;
            int var11 = var3.get(var9 + var3.position());
            var4.put(var9, var3.get(var10 + var3.position()));
            var4.put(var10, var11);
         }
      }

   }

   public long getHandle() {
      return this.cursorHandle;
   }

   public void destroy() {
      GLFW.glfwDestroyCursor(this.cursorHandle);
   }
}
