package se.krka.kahlua.vm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class KahluaArray implements KahluaTable {
   private KahluaTable metatable;
   private Object[] data = new Object[16];
   private int len = 0;
   private boolean recalculateLen;

   public String getString(String var1) {
      return (String)this.rawget(var1);
   }

   public int size() {
      return this.len();
   }

   public int len() {
      if (this.recalculateLen) {
         int var1 = this.len - 1;

         for(Object[] var2 = this.data; var1 >= 0 && var2[var1] == null; --var1) {
         }

         this.len = var1 + 1;
         this.recalculateLen = false;
      }

      return this.len;
   }

   public KahluaTableIterator iterator() {
      return new KahluaTableIterator() {
         private Double curKey;
         private Object curValue;
         private int index = 1;

         public int call(LuaCallFrame var1, int var2) {
            return this.advance() ? var1.push(this.getKey(), this.getValue()) : 0;
         }

         public boolean advance() {
            while(this.index <= KahluaArray.this.len()) {
               Object var1 = KahluaArray.this.rawget(this.index);
               if (var1 != null) {
                  int var2 = this.index++;
                  this.curKey = KahluaUtil.toDouble((long)var2);
                  this.curValue = var1;
                  return true;
               }

               ++this.index;
            }

            return false;
         }

         public Object getKey() {
            return this.curKey;
         }

         public Object getValue() {
            return this.curValue;
         }
      };
   }

   public boolean isEmpty() {
      return this.len() == 0;
   }

   public void wipe() {
      for(int var1 = 0; var1 < this.data.length; ++var1) {
         this.data[var1] = null;
      }

      this.len = 0;
   }

   public Object rawget(int var1) {
      return var1 >= 1 && var1 <= this.len ? this.data[var1 - 1] : null;
   }

   public void rawset(int var1, Object var2) {
      if (var1 <= 0) {
         KahluaUtil.fail("Index out of range: " + var1);
      }

      if (var1 >= this.len) {
         if (var2 == null) {
            if (var1 == this.len) {
               this.data[var1 - 1] = var2;
               this.recalculateLen = true;
            }

            return;
         }

         if (this.data.length < var1) {
            int var3 = 2 * var1;
            int var4 = var3 - 1;
            Object[] var5 = new Object[var4];
            System.arraycopy(this.data, 0, var5, 0, this.len);
            this.data = var5;
         }

         this.len = var1;
      }

      this.data[var1 - 1] = var2;
   }

   private int getKeyIndex(Object var1) {
      if (var1 instanceof Double) {
         Double var2 = (Double)var1;
         return var2.intValue();
      } else {
         return -1;
      }
   }

   public Object rawget(Object var1) {
      int var2 = this.getKeyIndex(var1);
      return this.rawget(var2);
   }

   public void rawset(Object var1, Object var2) {
      int var3 = this.getKeyIndex(var1);
      if (var3 == -1) {
         KahluaUtil.fail("Invalid table key: " + var1);
      }

      this.rawset(var3, var2);
   }

   public Object next(Object var1) {
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = this.getKeyIndex(var1);
         if (var2 <= 0 || var2 > this.len) {
            KahluaUtil.fail("invalid key to 'next'");
            return null;
         }
      }

      while(var2 < this.len) {
         if (this.data[var2] != null) {
            return KahluaUtil.toDouble((long)(var2 + 1));
         }

         ++var2;
      }

      return null;
   }

   public KahluaTable getMetatable() {
      return this.metatable;
   }

   public void setMetatable(KahluaTable var1) {
      this.metatable = var1;
   }

   public Class getJavaClass() {
      return null;
   }

   public void save(ByteBuffer var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void load(ByteBuffer var1, int var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void save(DataOutputStream var1) throws IOException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void load(DataInputStream var1, int var2) throws IOException {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
