package org.joml;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Matrix4dStack extends Matrix4d {
   private static final long serialVersionUID = 1L;
   private Matrix4d[] mats;
   private int curr;

   public Matrix4dStack(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("stackSize must be >= 1");
      } else {
         this.mats = new Matrix4d[var1 - 1];

         for(int var2 = 0; var2 < this.mats.length; ++var2) {
            this.mats[var2] = new Matrix4d();
         }

      }
   }

   public Matrix4dStack() {
   }

   public Matrix4dStack clear() {
      this.curr = 0;
      this.identity();
      return this;
   }

   public Matrix4dStack pushMatrix() {
      if (this.curr == this.mats.length) {
         throw new IllegalStateException("max stack size of " + (this.curr + 1) + " reached");
      } else {
         this.mats[this.curr++].set((Matrix4dc)this);
         return this;
      }
   }

   public Matrix4dStack popMatrix() {
      if (this.curr == 0) {
         throw new IllegalStateException("already at the buttom of the stack");
      } else {
         this.set(this.mats[--this.curr]);
         return this;
      }
   }

   public int hashCode() {
      int var1 = super.hashCode();
      var1 = 31 * var1 + this.curr;

      for(int var2 = 0; var2 < this.curr; ++var2) {
         var1 = 31 * var1 + this.mats[var2].hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!super.equals(var1)) {
         return false;
      } else {
         if (var1 instanceof Matrix4dStack) {
            Matrix4dStack var2 = (Matrix4dStack)var1;
            if (this.curr != var2.curr) {
               return false;
            }

            for(int var3 = 0; var3 < this.curr; ++var3) {
               if (!this.mats[var3].equals(var2.mats[var3])) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeInt(this.curr);

      for(int var2 = 0; var2 < this.curr; ++var2) {
         var1.writeObject(this.mats[var2]);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException {
      super.readExternal(var1);
      this.curr = var1.readInt();
      this.mats = new Matrix4dStack[this.curr];

      for(int var2 = 0; var2 < this.curr; ++var2) {
         Matrix4d var3 = new Matrix4d();
         var3.readExternal(var1);
         this.mats[var2] = var3;
      }

   }
}
