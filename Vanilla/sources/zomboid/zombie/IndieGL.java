package zombie;

import java.util.Stack;
import org.lwjgl.opengl.GL11;
import zombie.core.SpriteRenderer;
import zombie.core.math.Vector4;
import zombie.core.opengl.GLState;
import zombie.core.opengl.Shader;
import zombie.core.opengl.ShaderProgram;
import zombie.core.textures.Texture;
import zombie.iso.IsoCamera;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.util.Lambda;
import zombie.util.lambda.Invokers;

public final class IndieGL {
   public static int nCount = 0;
   private static final GLState.CIntValue tempInt = new GLState.CIntValue();
   private static final GLState.C2IntsValue temp2Ints = new GLState.C2IntsValue();
   private static final GLState.C3IntsValue temp3Ints = new GLState.C3IntsValue();
   private static final GLState.C4IntsValue temp4Ints = new GLState.C4IntsValue();
   private static final GLState.C4BooleansValue temp4Booleans = new GLState.C4BooleansValue();
   private static final GLState.CIntFloatValue tempIntFloat = new GLState.CIntFloatValue();
   private static final Stack m_shaderStack = new Stack();

   public static void glBlendFunc(int var0, int var1) {
      if (SpriteRenderer.instance != null && SpriteRenderer.GL_BLENDFUNC_ENABLED) {
         GLState.BlendFuncSeparate.set(temp4Ints.set(var0, var1, var0, var1));
      }

   }

   public static void glBlendFuncSeparate(int var0, int var1, int var2, int var3) {
      if (SpriteRenderer.instance != null && SpriteRenderer.GL_BLENDFUNC_ENABLED) {
         GLState.BlendFuncSeparate.set(temp4Ints.set(var0, var1, var2, var3));
      }

   }

   public static void StartShader(Shader var0) {
      int var1 = IsoCamera.frameState.playerIndex;
      StartShader(var0, var1);
   }

   public static void StartShader(Shader var0, int var1) {
      if (var0 != null) {
         StartShader(var0.getID(), var1);
      } else {
         EndShader();
      }

   }

   public static void StartShader(int var0) {
      int var1 = IsoCamera.frameState.playerIndex;
      StartShader(var0, var1);
   }

   public static void StartShader(int var0, int var1) {
      SpriteRenderer.instance.StartShader(var0, var1);
   }

   public static void EndShader() {
      SpriteRenderer.instance.EndShader();
   }

   public static void pushShader(Shader var0) {
      int var1 = IsoCamera.frameState.playerIndex;
      m_shaderStack.push(ShaderStackEntry.alloc(var0, var1));
      StartShader(var0, var1);
   }

   public static void popShader(Shader var0) {
      if (m_shaderStack.isEmpty()) {
         throw new RuntimeException("Push/PopShader mismatch. Cannot pop. Stack is empty.");
      } else if (((ShaderStackEntry)m_shaderStack.peek()).getShader() != var0) {
         throw new RuntimeException("Push/PopShader mismatch. The popped shader != the pushed shader.");
      } else {
         ShaderStackEntry var1 = (ShaderStackEntry)m_shaderStack.pop();
         var1.release();
         if (m_shaderStack.isEmpty()) {
            EndShader();
         } else {
            ShaderStackEntry var2 = (ShaderStackEntry)m_shaderStack.peek();
            StartShader(var2.getShader(), var2.getPlayerIndex());
         }
      }
   }

   public static void bindShader(Shader var0, Runnable var1) {
      pushShader(var0);

      try {
         var1.run();
      } finally {
         popShader(var0);
      }

   }

   public static void bindShader(Shader var0, Object var1, Invokers.Params1.ICallback var2) {
      Lambda.capture(var0, var1, var2, (var0x, var1x, var2x, var3) -> {
         bindShader(var1x, var0x.invoker(var2x, var3));
      });
   }

   public static void bindShader(Shader var0, Object var1, Object var2, Invokers.Params2.ICallback var3) {
      Lambda.capture(var0, var1, var2, var3, (var0x, var1x, var2x, var3x, var4) -> {
         bindShader(var1x, var0x.invoker(var2x, var3x, var4));
      });
   }

   public static void bindShader(Shader var0, Object var1, Object var2, Object var3, Invokers.Params3.ICallback var4) {
      Lambda.capture(var0, var1, var2, var3, var4, (var0x, var1x, var2x, var3x, var4x, var5) -> {
         bindShader(var1x, var0x.invoker(var2x, var3x, var4x, var5));
      });
   }

   public static void bindShader(Shader var0, Object var1, Object var2, Object var3, Object var4, Invokers.Params4.ICallback var5) {
      Lambda.capture(var0, var1, var2, var3, var4, var5, (var0x, var1x, var2x, var3x, var4x, var5x, var6) -> {
         bindShader(var1x, var0x.invoker(var2x, var3x, var4x, var5x, var6));
      });
   }

   private static ShaderProgram.Uniform getShaderUniform(Shader var0, String var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         ShaderProgram var3 = var0.getProgram();
         if (var3 == null) {
            return null;
         } else {
            ShaderProgram.Uniform var4 = var3.getUniform(var1, var2, false);
            return var4;
         }
      }
   }

   public static void shaderSetSamplerUnit(Shader var0, String var1, int var2) {
      ShaderProgram.Uniform var3 = getShaderUniform(var0, var1, 35678);
      if (var3 != null) {
         var3.sampler = var2;
         ShaderUpdate1i(var0.getID(), var3.loc, var2);
      }

   }

   public static void shaderSetValue(Shader var0, String var1, float var2) {
      ShaderProgram.Uniform var3 = getShaderUniform(var0, var1, 5126);
      if (var3 != null) {
         ShaderUpdate1f(var0.getID(), var3.loc, var2);
      }

   }

   public static void shaderSetValue(Shader var0, String var1, int var2) {
      ShaderProgram.Uniform var3 = getShaderUniform(var0, var1, 5124);
      if (var3 != null) {
         ShaderUpdate1i(var0.getID(), var3.loc, var2);
      }

   }

   public static void shaderSetValue(Shader var0, String var1, Vector2 var2) {
      shaderSetVector2(var0, var1, var2.x, var2.y);
   }

   public static void shaderSetValue(Shader var0, String var1, Vector3 var2) {
      shaderSetVector3(var0, var1, var2.x, var2.y, var2.z);
   }

   public static void shaderSetValue(Shader var0, String var1, Vector4 var2) {
      shaderSetVector4(var0, var1, var2.x, var2.y, var2.z, var2.w);
   }

   public static void shaderSetVector2(Shader var0, String var1, float var2, float var3) {
      ShaderProgram.Uniform var4 = getShaderUniform(var0, var1, 35664);
      if (var4 != null) {
         ShaderUpdate2f(var0.getID(), var4.loc, var2, var3);
      }

   }

   public static void shaderSetVector3(Shader var0, String var1, float var2, float var3, float var4) {
      ShaderProgram.Uniform var5 = getShaderUniform(var0, var1, 35665);
      if (var5 != null) {
         ShaderUpdate3f(var0.getID(), var5.loc, var2, var3, var4);
      }

   }

   public static void shaderSetVector4(Shader var0, String var1, float var2, float var3, float var4, float var5) {
      ShaderProgram.Uniform var6 = getShaderUniform(var0, var1, 35666);
      if (var6 != null) {
         ShaderUpdate4f(var0.getID(), var6.loc, var2, var3, var4, var5);
      }

   }

   public static void ShaderUpdate1i(int var0, int var1, int var2) {
      SpriteRenderer.instance.ShaderUpdate1i(var0, var1, var2);
   }

   public static void ShaderUpdate1f(int var0, int var1, float var2) {
      SpriteRenderer.instance.ShaderUpdate1f(var0, var1, var2);
   }

   public static void ShaderUpdate2f(int var0, int var1, float var2, float var3) {
      SpriteRenderer.instance.ShaderUpdate2f(var0, var1, var2, var3);
   }

   public static void ShaderUpdate3f(int var0, int var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate3f(var0, var1, var2, var3, var4);
   }

   public static void ShaderUpdate4f(int var0, int var1, float var2, float var3, float var4, float var5) {
      SpriteRenderer.instance.ShaderUpdate4f(var0, var1, var2, var3, var4, var5);
   }

   public static void glBlendFuncA(int var0, int var1) {
      GL11.glBlendFunc(var0, var1);
   }

   public static void glEnable(int var0) {
      SpriteRenderer.instance.glEnable(var0);
   }

   public static void glDoStartFrame(int var0, int var1, float var2, int var3) {
      glDoStartFrame(var0, var1, var2, var3, false);
   }

   public static void glDoStartFrame(int var0, int var1, float var2, int var3, boolean var4) {
      SpriteRenderer.instance.glDoStartFrame(var0, var1, var2, var3, var4);
   }

   public static void glDoEndFrame() {
      SpriteRenderer.instance.glDoEndFrame();
   }

   public static void glColorMask(boolean var0, boolean var1, boolean var2, boolean var3) {
      GLState.ColorMask.set(temp4Booleans.set(var0, var1, var2, var3));
   }

   public static void glColorMaskA(boolean var0, boolean var1, boolean var2, boolean var3) {
      GL11.glColorMask(var0, var0, var3, var3);
   }

   public static void glEnableA(int var0) {
      GL11.glEnable(var0);
   }

   public static void glAlphaFunc(int var0, float var1) {
      GLState.AlphaFunc.set(tempIntFloat.set(var0, var1));
   }

   public static void glAlphaFuncA(int var0, float var1) {
      GL11.glAlphaFunc(var0, var1);
   }

   public static void glStencilFunc(int var0, int var1, int var2) {
      GLState.StencilFunc.set(temp3Ints.set(var0, var1, var2));
   }

   public static void glStencilFuncA(int var0, int var1, int var2) {
      GL11.glStencilFunc(var0, var1, var2);
   }

   public static void glStencilOp(int var0, int var1, int var2) {
      GLState.StencilOp.set(temp3Ints.set(var0, var1, var2));
   }

   public static void glStencilOpA(int var0, int var1, int var2) {
      GL11.glStencilOp(var0, var1, var2);
   }

   public static void glTexParameteri(int var0, int var1, int var2) {
      SpriteRenderer.instance.glTexParameteri(var0, var1, var2);
   }

   public static void glTexParameteriActual(int var0, int var1, int var2) {
      GL11.glTexParameteri(var0, var1, var2);
   }

   public static void glStencilMask(int var0) {
      GLState.StencilMask.set(tempInt.set(var0));
   }

   public static void glStencilMaskA(int var0) {
      GL11.glStencilMask(var0);
   }

   public static void glDisable(int var0) {
      SpriteRenderer.instance.glDisable(var0);
   }

   public static void glClear(int var0) {
      SpriteRenderer.instance.glClear(var0);
   }

   public static void glClearA(int var0) {
      GL11.glClear(var0);
   }

   public static void glDisableA(int var0) {
      GL11.glDisable(var0);
   }

   public static void glLoadIdentity() {
      SpriteRenderer.instance.glLoadIdentity();
   }

   public static void glBind(Texture var0) {
      SpriteRenderer.instance.glBind(var0.getID());
   }

   public static void enableAlphaTest() {
      GLState.AlphaTest.set(GLState.CBooleanValue.TRUE);
   }

   public static void disableAlphaTest() {
      GLState.AlphaTest.set(GLState.CBooleanValue.FALSE);
   }

   public static void enableStencilTest() {
      GLState.StencilTest.set(GLState.CBooleanValue.TRUE);
   }

   public static void disableStencilTest() {
      GLState.StencilTest.set(GLState.CBooleanValue.FALSE);
   }

   public static boolean isMaxZoomLevel() {
      return SpriteRenderer.instance.isMaxZoomLevel();
   }

   public static boolean isMinZoomLevel() {
      return SpriteRenderer.instance.isMinZoomLevel();
   }
}
