package zombie.iso.SpriteDetails;

import java.util.HashMap;

public enum IsoObjectType {
   normal(0),
   jukebox(1),
   wall(2),
   stairsTW(3),
   stairsTN(4),
   stairsMW(5),
   stairsMN(6),
   stairsBW(7),
   stairsBN(8),
   UNUSED9(9),
   UNUSED10(10),
   doorW(11),
   doorN(12),
   lightswitch(13),
   radio(14),
   curtainN(15),
   curtainS(16),
   curtainW(17),
   curtainE(18),
   doorFrW(19),
   doorFrN(20),
   tree(21),
   windowFN(22),
   windowFW(23),
   UNUSED24(24),
   WestRoofB(25),
   WestRoofM(26),
   WestRoofT(27),
   isMoveAbleObject(28),
   MAX(29);

   private final int index;
   private static final HashMap fromStringMap = new HashMap();

   private IsoObjectType(int var3) {
      this.index = var3;
   }

   public int index() {
      return this.index;
   }

   public static IsoObjectType fromIndex(int var0) {
      return ((IsoObjectType[])IsoObjectType.class.getEnumConstants())[var0];
   }

   public static IsoObjectType FromString(String var0) {
      IsoObjectType var1 = (IsoObjectType)fromStringMap.get(var0);
      return var1 == null ? MAX : var1;
   }

   // $FF: synthetic method
   private static IsoObjectType[] $values() {
      return new IsoObjectType[]{normal, jukebox, wall, stairsTW, stairsTN, stairsMW, stairsMN, stairsBW, stairsBN, UNUSED9, UNUSED10, doorW, doorN, lightswitch, radio, curtainN, curtainS, curtainW, curtainE, doorFrW, doorFrN, tree, windowFN, windowFW, UNUSED24, WestRoofB, WestRoofM, WestRoofT, isMoveAbleObject, MAX};
   }

   static {
      IsoObjectType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         IsoObjectType var3 = var0[var2];
         if (var3 == MAX) {
            break;
         }

         fromStringMap.put(var3.name(), var3);
      }

   }
}
