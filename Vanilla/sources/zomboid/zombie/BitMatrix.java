package zombie;

public class BitMatrix {
   public static boolean Is(int var0, int var1, int var2, int var3) {
      return (1 << (var1 + 1) * 9 + (var2 + 1) * 3 + var3 + 1 & var0) == 1 << (var1 + 1) * 9 + (var2 + 1) * 3 + var3 + 1;
   }

   public static int Set(int var0, int var1, int var2, int var3, boolean var4) {
      if (var4) {
         var0 |= 1 << (var1 + 1) * 9 + (var2 + 1) * 3 + var3 + 1;
      } else {
         var0 &= ~(1 << (var1 + 1) * 9 + (var2 + 1) * 3 + var3 + 1);
      }

      return var0;
   }
}
