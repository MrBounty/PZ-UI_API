package zombie.util;

public final class PZXmlParserException extends Exception {
   public PZXmlParserException() {
   }

   public PZXmlParserException(String var1) {
      super(var1);
   }

   public PZXmlParserException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public PZXmlParserException(Throwable var1) {
      super(var1);
   }

   public String toString() {
      String var1 = super.toString();
      String var2 = var1;
      Throwable var3 = this.getCause();
      if (var3 != null) {
         var2 = var1 + System.lineSeparator() + "  Caused by:" + System.lineSeparator() + "    " + var3.toString();
      }

      return var2;
   }
}
