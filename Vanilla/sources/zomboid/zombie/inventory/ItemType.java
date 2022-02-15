package zombie.inventory;

public enum ItemType {
   None(0),
   Weapon(1),
   Food(2),
   Literature(3),
   Drainable(4),
   Clothing(5),
   Key(6),
   KeyRing(7),
   Moveable(8),
   AlarmClock(9),
   AlarmClockClothing(10);

   private int index;

   private ItemType(int var3) {
      this.index = var3;
   }

   public int index() {
      return this.index;
   }

   public static ItemType fromIndex(int var0) {
      return ((ItemType[])ItemType.class.getEnumConstants())[var0];
   }

   // $FF: synthetic method
   private static ItemType[] $values() {
      return new ItemType[]{None, Weapon, Food, Literature, Drainable, Clothing, Key, KeyRing, Moveable, AlarmClock, AlarmClockClothing};
   }
}
