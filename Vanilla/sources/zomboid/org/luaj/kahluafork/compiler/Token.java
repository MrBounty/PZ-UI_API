package org.luaj.kahluafork.compiler;

public class Token {
   int token;
   double r;
   String ts;

   public void set(Token var1) {
      this.token = var1.token;
      this.r = var1.r;
      this.ts = var1.ts;
   }
}
