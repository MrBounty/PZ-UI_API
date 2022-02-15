package zombie.core.opengl;

import java.io.PrintStream;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjglx.opengl.OpenGLException;
import org.lwjglx.opengl.Util;
import zombie.core.skinnedmodel.model.Model;

public class PZGLUtil {
   static int test = 0;

   public static void checkGLErrorThrow(String var0, Object... var1) throws OpenGLException {
      int var2 = GL11.glGetError();
      if (var2 != 0) {
         ++test;
         throw new OpenGLException(createErrorMessage(var2, var0, var1));
      }
   }

   private static String createErrorMessage(int var0, String var1, Object... var2) {
      String var3 = System.lineSeparator();
      return "  GL Error code (" + var0 + ") encountered." + var3 + "  Error translation: " + createErrorMessage(var0) + var3 + "  While performing: " + String.format(var1, var2);
   }

   private static String createErrorMessage(int var0) {
      String var1 = Util.translateGLErrorString(var0);
      return var1 + " (" + var0 + ")";
   }

   public static boolean checkGLError(boolean var0) {
      try {
         Util.checkGLError();
         return true;
      } catch (OpenGLException var2) {
         RenderThread.logGLException(var2, var0);
         return false;
      }
   }

   public static void printGLState(PrintStream var0) {
      int var1 = GL11.glGetInteger(2979);
      var0.println("DEBUG: GL_MODELVIEW_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(2980);
      var0.println("DEBUG: GL_PROJECTION_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(2981);
      var0.println("DEBUG: GL_TEXTURE_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(2992);
      var0.println("DEBUG: GL_ATTRIB_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(2993);
      var0.println("DEBUG: GL_CLIENT_ATTRIB_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3381);
      var0.println("DEBUG: GL_MAX_ATTRIB_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3382);
      var0.println("DEBUG: GL_MAX_MODELVIEW_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3383);
      var0.println("DEBUG: GL_MAX_NAME_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3384);
      var0.println("DEBUG: GL_MAX_PROJECTION_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3385);
      var0.println("DEBUG: GL_MAX_TEXTURE_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3387);
      var0.println("DEBUG: GL_MAX_CLIENT_ATTRIB_STACK_DEPTH= " + var1);
      var1 = GL11.glGetInteger(3440);
      var0.println("DEBUG: GL_NAME_STACK_DEPTH= " + var1);
   }

   public static void loadMatrix(Matrix4f var0) {
      var0.get(Model.m_staticReusableFloatBuffer);
      Model.m_staticReusableFloatBuffer.position(16);
      Model.m_staticReusableFloatBuffer.flip();
      GL11.glLoadMatrixf(Model.m_staticReusableFloatBuffer);
   }

   public static void multMatrix(Matrix4f var0) {
      var0.get(Model.m_staticReusableFloatBuffer);
      Model.m_staticReusableFloatBuffer.position(16);
      Model.m_staticReusableFloatBuffer.flip();
      GL11.glMultMatrixf(Model.m_staticReusableFloatBuffer);
   }

   public static void loadMatrix(int var0, Matrix4f var1) {
      GL11.glMatrixMode(var0);
      loadMatrix(var1);
   }

   public static void multMatrix(int var0, Matrix4f var1) {
      GL11.glMatrixMode(var0);
      multMatrix(var1);
   }

   public static void pushAndLoadMatrix(int var0, Matrix4f var1) {
      GL11.glMatrixMode(var0);
      GL11.glPushMatrix();
      loadMatrix(var1);
   }

   public static void pushAndMultMatrix(int var0, Matrix4f var1) {
      GL11.glMatrixMode(var0);
      GL11.glPushMatrix();
      multMatrix(var1);
   }

   public static void popMatrix(int var0) {
      GL11.glMatrixMode(var0);
      GL11.glPopMatrix();
   }
}
