package com.sixlegs.png;

import java.io.IOException;
import java.lang.reflect.Method;

public class PngException extends IOException {
   private static final Method initCause = getInitCause();
   private final boolean fatal;

   private static Method getInitCause() {
      try {
         return PngException.class.getMethod("initCause", Throwable.class);
      } catch (Exception var1) {
         return null;
      }
   }

   PngException(String var1, boolean var2) {
      this(var1, (Throwable)null, var2);
   }

   PngException(String var1, Throwable var2, boolean var3) {
      super(var1);
      this.fatal = var3;
      if (var2 != null && initCause != null) {
         try {
            initCause.invoke(this, var2);
         } catch (RuntimeException var5) {
            throw var5;
         } catch (Exception var6) {
            throw new IllegalStateException("Error invoking initCause: " + var6.getMessage());
         }
      }

   }

   public boolean isFatal() {
      return this.fatal;
   }
}
