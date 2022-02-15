package de.jarnbjo.util.io;

import java.io.IOException;

public final class HuffmanNode {
   private HuffmanNode parent;
   private int depth;
   protected HuffmanNode o0;
   protected HuffmanNode o1;
   protected Integer value;
   private boolean full;

   public HuffmanNode() {
      this((HuffmanNode)null);
   }

   protected HuffmanNode(HuffmanNode var1) {
      this.depth = 0;
      this.full = false;
      this.parent = var1;
      if (var1 != null) {
         this.depth = var1.getDepth() + 1;
      }

   }

   protected HuffmanNode(HuffmanNode var1, int var2) {
      this(var1);
      this.value = new Integer(var2);
      this.full = true;
   }

   protected int read(BitInputStream var1) throws IOException {
      HuffmanNode var2;
      for(var2 = this; var2.value == null; var2 = var1.getBit() ? var2.o1 : var2.o0) {
      }

      return var2.value;
   }

   protected HuffmanNode get0() {
      return this.o0 == null ? this.set0(new HuffmanNode(this)) : this.o0;
   }

   protected HuffmanNode get1() {
      return this.o1 == null ? this.set1(new HuffmanNode(this)) : this.o1;
   }

   protected Integer getValue() {
      return this.value;
   }

   private HuffmanNode getParent() {
      return this.parent;
   }

   protected int getDepth() {
      return this.depth;
   }

   private boolean isFull() {
      return this.full ? true : (this.full = this.o0 != null && this.o0.isFull() && this.o1 != null && this.o1.isFull());
   }

   private HuffmanNode set0(HuffmanNode var1) {
      return this.o0 = var1;
   }

   private HuffmanNode set1(HuffmanNode var1) {
      return this.o1 = var1;
   }

   private void setValue(Integer var1) {
      this.full = true;
      this.value = var1;
   }

   public boolean setNewValue(int var1, int var2) {
      if (this.isFull()) {
         return false;
      } else if (var1 == 1) {
         if (this.o0 == null) {
            this.set0(new HuffmanNode(this, var2));
            return true;
         } else if (this.o1 == null) {
            this.set1(new HuffmanNode(this, var2));
            return true;
         } else {
            return false;
         }
      } else {
         return this.get0().setNewValue(var1 - 1, var2) ? true : this.get1().setNewValue(var1 - 1, var2);
      }
   }
}
