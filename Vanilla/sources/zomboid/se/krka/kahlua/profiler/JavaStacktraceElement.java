package se.krka.kahlua.profiler;

import se.krka.kahlua.vm.JavaFunction;

public class JavaStacktraceElement implements StacktraceElement {
   private final JavaFunction javaFunction;

   public JavaStacktraceElement(JavaFunction var1) {
      this.javaFunction = var1;
   }

   public String name() {
      return this.javaFunction.toString();
   }

   public String type() {
      return "java";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof JavaStacktraceElement)) {
         return false;
      } else {
         JavaStacktraceElement var2 = (JavaStacktraceElement)var1;
         return this.javaFunction == var2.javaFunction;
      }
   }

   public int hashCode() {
      return this.javaFunction.hashCode();
   }
}
