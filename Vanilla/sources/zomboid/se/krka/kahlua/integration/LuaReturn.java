package se.krka.kahlua.integration;

import java.util.AbstractList;

public abstract class LuaReturn extends AbstractList {
   protected final Object[] returnValues;

   protected LuaReturn(Object[] var1) {
      this.returnValues = var1;
   }

   public abstract boolean isSuccess();

   public abstract Object getErrorObject();

   public abstract String getErrorString();

   public abstract String getLuaStackTrace();

   public abstract RuntimeException getJavaException();

   public Object getFirst() {
      return this.get(0);
   }

   public Object getSecond() {
      return this.get(1);
   }

   public Object getThird() {
      return this.get(2);
   }

   public Object get(int var1) {
      int var2 = this.size();
      if (var1 >= 0 && var1 < var2) {
         return this.returnValues[var1 + 1];
      } else {
         throw new IndexOutOfBoundsException("The index " + var1 + " is outside the bounds [0, " + var2 + ")");
      }
   }

   public int size() {
      return this.returnValues.length - 1;
   }

   public static LuaReturn createReturn(Object[] var0) {
      Boolean var1 = (Boolean)var0[0];
      return (LuaReturn)(var1 ? new LuaSuccess(var0) : new LuaFail(var0));
   }
}
