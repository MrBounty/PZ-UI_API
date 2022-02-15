package org.luaj.kahluafork.compiler;

class InstructionPtr {
   final int[] code;
   final int idx;

   InstructionPtr(int[] var1, int var2) {
      this.code = var1;
      this.idx = var2;
   }

   int get() {
      return this.code[this.idx];
   }

   void set(int var1) {
      this.code[this.idx] = var1;
   }
}
