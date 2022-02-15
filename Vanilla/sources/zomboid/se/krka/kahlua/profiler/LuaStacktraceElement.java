package se.krka.kahlua.profiler;

import se.krka.kahlua.vm.Prototype;

public class LuaStacktraceElement implements StacktraceElement {
   private final int pc;
   private final Prototype prototype;

   public LuaStacktraceElement(int var1, Prototype var2) {
      this.pc = var1;
      this.prototype = var2;
   }

   public int getLine() {
      return this.pc >= 0 && this.pc < this.prototype.lines.length ? this.prototype.lines[this.pc] : 0;
   }

   public String getSource() {
      return this.prototype.name;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof LuaStacktraceElement)) {
         return false;
      } else {
         LuaStacktraceElement var2 = (LuaStacktraceElement)var1;
         if (this.getLine() != var2.getLine()) {
            return false;
         } else {
            return this.prototype.equals(var2.prototype);
         }
      }
   }

   public int hashCode() {
      int var1 = this.getLine();
      var1 = 31 * var1 + this.prototype.hashCode();
      return var1;
   }

   public String toString() {
      return this.name();
   }

   public String name() {
      String var10000 = this.getSource();
      return var10000 + ":" + this.getLine();
   }

   public String type() {
      return "lua";
   }
}
