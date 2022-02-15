package zombie.iso;

public class IsoDirectionSet {
   public int set = 0;

   public static IsoDirections rotate(IsoDirections var0, int var1) {
      var1 += var0.index();
      var1 %= 8;
      return IsoDirections.fromIndex(var1);
   }

   public IsoDirections getNext() {
      for(int var1 = 0; var1 < 8; ++var1) {
         int var2 = 1 << var1;
         if ((this.set & var2) != 0) {
            this.set ^= var2;
            return IsoDirections.fromIndex(var1);
         }
      }

      return IsoDirections.Max;
   }
}
