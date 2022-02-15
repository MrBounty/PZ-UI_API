package se.krka.kahlua.integration;

public class LuaSuccess extends LuaReturn {
   LuaSuccess(Object[] var1) {
      super(var1);
   }

   public boolean isSuccess() {
      return true;
   }

   public Object getErrorObject() {
      throw new UnsupportedOperationException("Not valid when isSuccess is true");
   }

   public String getErrorString() {
      throw new UnsupportedOperationException("Not valid when isSuccess is true");
   }

   public String getLuaStackTrace() {
      throw new UnsupportedOperationException("Not valid when isSuccess is true");
   }

   public RuntimeException getJavaException() {
      throw new UnsupportedOperationException("Not valid when isSuccess is true");
   }
}
