package zombie.core.opengl;

import zombie.core.SpriteRenderer;
import zombie.util.Type;

public final class GLState {
   public static final GLState.CAlphaFunc AlphaFunc = new GLState.CAlphaFunc();
   public static final GLState.CAlphaTest AlphaTest = new GLState.CAlphaTest();
   public static final GLState.CBlendFunc BlendFunc = new GLState.CBlendFunc();
   public static final GLState.CBlendFuncSeparate BlendFuncSeparate = new GLState.CBlendFuncSeparate();
   public static final GLState.CColorMask ColorMask = new GLState.CColorMask();
   public static final GLState.CStencilFunc StencilFunc = new GLState.CStencilFunc();
   public static final GLState.CStencilMask StencilMask = new GLState.CStencilMask();
   public static final GLState.CStencilOp StencilOp = new GLState.CStencilOp();
   public static final GLState.CStencilTest StencilTest = new GLState.CStencilTest();

   public static void startFrame() {
      AlphaFunc.setDirty();
      AlphaTest.setDirty();
      BlendFunc.setDirty();
      BlendFuncSeparate.setDirty();
      ColorMask.setDirty();
      StencilFunc.setDirty();
      StencilMask.setDirty();
      StencilOp.setDirty();
      StencilTest.setDirty();
   }

   public static final class CAlphaFunc extends GLState.BaseIntFloat {
      void Set(GLState.CIntFloatValue var1) {
         SpriteRenderer.instance.glAlphaFunc(var1.a, var1.b);
      }
   }

   public static final class CAlphaTest extends GLState.BaseBoolean {
      void Set(GLState.CBooleanValue var1) {
         if (var1.value) {
            SpriteRenderer.instance.glEnable(3008);
         } else {
            SpriteRenderer.instance.glDisable(3008);
         }

      }
   }

   public static final class CBlendFunc extends GLState.Base2Ints {
      void Set(GLState.C2IntsValue var1) {
         SpriteRenderer.instance.glBlendFunc(var1.a, var1.b);
      }
   }

   public static final class CBlendFuncSeparate extends GLState.Base4Ints {
      void Set(GLState.C4IntsValue var1) {
         SpriteRenderer.instance.glBlendFuncSeparate(var1.a, var1.b, var1.c, var1.d);
      }
   }

   public static final class CColorMask extends GLState.Base4Booleans {
      void Set(GLState.C4BooleansValue var1) {
         SpriteRenderer.instance.glColorMask(var1.a ? 1 : 0, var1.b ? 1 : 0, var1.c ? 1 : 0, var1.d ? 1 : 0);
      }
   }

   public static final class CStencilFunc extends GLState.Base3Ints {
      void Set(GLState.C3IntsValue var1) {
         SpriteRenderer.instance.glStencilFunc(var1.a, var1.b, var1.c);
      }
   }

   public static final class CStencilMask extends GLState.BaseInt {
      void Set(GLState.CIntValue var1) {
         SpriteRenderer.instance.glStencilMask(var1.value);
      }
   }

   public static final class CStencilOp extends GLState.Base3Ints {
      void Set(GLState.C3IntsValue var1) {
         SpriteRenderer.instance.glStencilOp(var1.a, var1.b, var1.c);
      }
   }

   public static final class CStencilTest extends GLState.BaseBoolean {
      void Set(GLState.CBooleanValue var1) {
         if (var1.value) {
            SpriteRenderer.instance.glEnable(2960);
         } else {
            SpriteRenderer.instance.glDisable(2960);
         }

      }
   }

   public abstract static class Base4Ints extends IOpenGLState {
      GLState.C4IntsValue defaultValue() {
         return new GLState.C4IntsValue();
      }
   }

   public abstract static class Base3Ints extends IOpenGLState {
      GLState.C3IntsValue defaultValue() {
         return new GLState.C3IntsValue();
      }
   }

   public abstract static class Base2Ints extends IOpenGLState {
      GLState.C2IntsValue defaultValue() {
         return new GLState.C2IntsValue();
      }
   }

   public abstract static class BaseInt extends IOpenGLState {
      GLState.CIntValue defaultValue() {
         return new GLState.CIntValue();
      }
   }

   public abstract static class BaseIntFloat extends IOpenGLState {
      GLState.CIntFloatValue defaultValue() {
         return new GLState.CIntFloatValue();
      }
   }

   public abstract static class Base4Booleans extends IOpenGLState {
      GLState.C4BooleansValue defaultValue() {
         return new GLState.C4BooleansValue();
      }
   }

   public abstract static class BaseBoolean extends IOpenGLState {
      GLState.CBooleanValue defaultValue() {
         return new GLState.CBooleanValue(true);
      }
   }

   public static final class CIntFloatValue implements IOpenGLState.Value {
      int a;
      float b;

      public GLState.CIntFloatValue set(int var1, float var2) {
         this.a = var1;
         this.b = var2;
         return this;
      }

      public boolean equals(Object var1) {
         GLState.CIntFloatValue var2 = (GLState.CIntFloatValue)Type.tryCastTo(var1, GLState.CIntFloatValue.class);
         return var2 != null && var2.a == this.a && var2.b == this.b;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         GLState.CIntFloatValue var2 = (GLState.CIntFloatValue)var1;
         this.a = var2.a;
         this.b = var2.b;
         return this;
      }
   }

   public static final class C4IntsValue implements IOpenGLState.Value {
      int a;
      int b;
      int c;
      int d;

      public GLState.C4IntsValue set(int var1, int var2, int var3, int var4) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
         this.d = var4;
         return this;
      }

      public boolean equals(Object var1) {
         GLState.C4IntsValue var2 = (GLState.C4IntsValue)Type.tryCastTo(var1, GLState.C4IntsValue.class);
         return var2 != null && var2.a == this.a && var2.b == this.b && var2.c == this.c && var2.d == this.d;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         GLState.C4IntsValue var2 = (GLState.C4IntsValue)var1;
         this.a = var2.a;
         this.b = var2.b;
         this.c = var2.c;
         this.d = var2.d;
         return this;
      }
   }

   public static final class C3IntsValue implements IOpenGLState.Value {
      int a;
      int b;
      int c;

      public GLState.C3IntsValue set(int var1, int var2, int var3) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
         return this;
      }

      public boolean equals(Object var1) {
         GLState.C3IntsValue var2 = (GLState.C3IntsValue)Type.tryCastTo(var1, GLState.C3IntsValue.class);
         return var2 != null && var2.a == this.a && var2.b == this.b && var2.c == this.c;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         GLState.C3IntsValue var2 = (GLState.C3IntsValue)var1;
         this.a = var2.a;
         this.b = var2.b;
         this.c = var2.c;
         return this;
      }
   }

   public static final class C2IntsValue implements IOpenGLState.Value {
      int a;
      int b;

      public GLState.C2IntsValue set(int var1, int var2) {
         this.a = var1;
         this.b = var2;
         return this;
      }

      public boolean equals(Object var1) {
         GLState.C2IntsValue var2 = (GLState.C2IntsValue)Type.tryCastTo(var1, GLState.C2IntsValue.class);
         return var2 != null && var2.a == this.a && var2.b == this.b;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         GLState.C2IntsValue var2 = (GLState.C2IntsValue)var1;
         this.a = var2.a;
         this.b = var2.b;
         return this;
      }
   }

   public static class CIntValue implements IOpenGLState.Value {
      int value;

      public GLState.CIntValue set(int var1) {
         this.value = var1;
         return this;
      }

      public boolean equals(Object var1) {
         return var1 instanceof GLState.CIntValue && ((GLState.CIntValue)var1).value == this.value;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         this.value = ((GLState.CIntValue)var1).value;
         return this;
      }
   }

   public static final class C4BooleansValue implements IOpenGLState.Value {
      boolean a;
      boolean b;
      boolean c;
      boolean d;

      public GLState.C4BooleansValue set(boolean var1, boolean var2, boolean var3, boolean var4) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
         this.d = var4;
         return this;
      }

      public boolean equals(Object var1) {
         GLState.C4BooleansValue var2 = (GLState.C4BooleansValue)Type.tryCastTo(var1, GLState.C4BooleansValue.class);
         return var2 != null && var2.a == this.a && var2.b == this.b && var2.c == this.c;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         GLState.C4BooleansValue var2 = (GLState.C4BooleansValue)var1;
         this.a = var2.a;
         this.b = var2.b;
         this.c = var2.c;
         this.d = var2.d;
         return this;
      }
   }

   public static class CBooleanValue implements IOpenGLState.Value {
      public static final GLState.CBooleanValue TRUE = new GLState.CBooleanValue(true);
      public static final GLState.CBooleanValue FALSE = new GLState.CBooleanValue(false);
      boolean value;

      CBooleanValue(boolean var1) {
         this.value = var1;
      }

      public boolean equals(Object var1) {
         return var1 instanceof GLState.CBooleanValue && ((GLState.CBooleanValue)var1).value == this.value;
      }

      public IOpenGLState.Value set(IOpenGLState.Value var1) {
         this.value = ((GLState.CBooleanValue)var1).value;
         return this;
      }
   }
}
