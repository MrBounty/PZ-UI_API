package zombie.util.util;

public class Display {
   private static final String displayChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#¤%&/()=?'@£${[]}+|^~*-_.:,;<>\\";

   public static String display(int var0) {
      return String.valueOf(var0);
   }

   static String hexChar(char var0) {
      String var1 = Integer.toHexString(var0);
      switch(var1.length()) {
      case 1:
         return "\\u000" + var1;
      case 2:
         return "\\u00" + var1;
      case 3:
         return "\\u0" + var1;
      case 4:
         return "\\u" + var1;
      default:
         throw new RuntimeException("Internal error");
      }
   }
}
