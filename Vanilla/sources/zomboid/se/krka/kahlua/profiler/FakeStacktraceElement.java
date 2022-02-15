package se.krka.kahlua.profiler;

public class FakeStacktraceElement implements StacktraceElement {
   private final String name;
   private final String type;

   public FakeStacktraceElement(String var1, String var2) {
      this.name = var1;
      this.type = var2;
   }

   public String name() {
      return this.name;
   }

   public String type() {
      return this.type;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof FakeStacktraceElement)) {
         return false;
      } else {
         FakeStacktraceElement var2 = (FakeStacktraceElement)var1;
         if (!this.name.equals(var2.name)) {
            return false;
         } else {
            return this.type.equals(var2.type);
         }
      }
   }

   public int hashCode() {
      int var1 = this.name.hashCode();
      var1 = 31 * var1 + this.type.hashCode();
      return var1;
   }

   public String toString() {
      return this.name;
   }
}
