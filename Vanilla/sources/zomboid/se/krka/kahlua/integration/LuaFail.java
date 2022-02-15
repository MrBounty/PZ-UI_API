package se.krka.kahlua.integration;

import se.krka.kahlua.vm.KahluaUtil;

public class LuaFail extends LuaReturn {
   LuaFail(Object[] var1) {
      super(var1);
   }

   public boolean isSuccess() {
      return false;
   }

   public Object getErrorObject() {
      return this.returnValues.length >= 2 ? this.returnValues[1] : null;
   }

   public String getErrorString() {
      return this.returnValues.length >= 2 && this.returnValues[1] != null ? KahluaUtil.rawTostring(this.returnValues[1]) : "";
   }

   public String getLuaStackTrace() {
      return this.returnValues.length >= 3 && this.returnValues[2] instanceof String ? (String)this.returnValues[2] : "";
   }

   public RuntimeException getJavaException() {
      return this.returnValues.length >= 4 && this.returnValues[3] instanceof RuntimeException ? (RuntimeException)this.returnValues[3] : null;
   }

   public int size() {
      return 0;
   }

   public String toString() {
      String var10000 = this.getErrorString();
      return var10000 + "\n" + this.getLuaStackTrace();
   }
}
