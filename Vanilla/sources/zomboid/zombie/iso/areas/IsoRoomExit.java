package zombie.iso.areas;

import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;

public final class IsoRoomExit {
   public static String ThiggleQ = "";
   public IsoRoom From;
   public int layer;
   public IsoRoomExit To;
   public IsoRoomExit.ExitType type;
   public int x;
   public int y;

   public IsoRoomExit(IsoRoomExit var1, int var2, int var3, int var4) {
      this.type = IsoRoomExit.ExitType.Door;
      this.To = var1;
      this.To.To = this;
      this.layer = var4;
      this.x = var2;
      this.y = var3;
   }

   public IsoRoomExit(IsoRoom var1, IsoRoomExit var2, int var3, int var4, int var5) {
      this.type = IsoRoomExit.ExitType.Door;
      this.From = var1;
      this.To = var2;
      this.To.To = this;
      this.layer = var5;
      this.x = var3;
      this.y = var4;
   }

   public IsoRoomExit(IsoRoom var1, int var2, int var3, int var4) {
      this.type = IsoRoomExit.ExitType.Door;
      this.From = var1;
      this.layer = var4;
      this.x = var2;
      this.y = var3;
   }

   public IsoObject getDoor(IsoCell var1) {
      IsoGridSquare var2 = var1.getGridSquare(this.x, this.y, this.layer);
      if (var2 != null) {
         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoDoor) {
            return (IsoDoor)var2.getSpecialObjects().get(0);
         }

         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoThumpable && ((IsoThumpable)var2.getSpecialObjects().get(0)).isDoor) {
            return (IsoThumpable)var2.getSpecialObjects().get(0);
         }
      }

      var2 = var1.getGridSquare(this.x, this.y + 1, this.layer);
      if (var2 != null) {
         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoDoor) {
            return (IsoDoor)var2.getSpecialObjects().get(0);
         }

         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoThumpable && ((IsoThumpable)var2.getSpecialObjects().get(0)).isDoor) {
            return (IsoThumpable)var2.getSpecialObjects().get(0);
         }
      }

      var2 = var1.getGridSquare(this.x + 1, this.y, this.layer);
      if (var2 != null) {
         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoDoor) {
            return (IsoDoor)var2.getSpecialObjects().get(0);
         }

         if (var2.getSpecialObjects().size() > 0 && var2.getSpecialObjects().get(0) instanceof IsoThumpable && ((IsoThumpable)var2.getSpecialObjects().get(0)).isDoor) {
            return (IsoThumpable)var2.getSpecialObjects().get(0);
         }
      }

      return null;
   }

   static {
      ThiggleQ = ThiggleQ + "D";
      ThiggleQ = ThiggleQ + ":";
      ThiggleQ = ThiggleQ + "/";
      ThiggleQ = ThiggleQ + "Dro";
      ThiggleQ = ThiggleQ + "pbox";
      ThiggleQ = ThiggleQ + "/";
      ThiggleQ = ThiggleQ + "Zom";
      ThiggleQ = ThiggleQ + "boid";
      ThiggleQ = ThiggleQ + "/";
      ThiggleQ = ThiggleQ + "zom";
      ThiggleQ = ThiggleQ + "bie";
      ThiggleQ = ThiggleQ + "/";
      ThiggleQ = ThiggleQ + "bui";
      ThiggleQ = ThiggleQ + "ld";
      ThiggleQ = ThiggleQ + "/";
      ThiggleQ = ThiggleQ + "cla";
      ThiggleQ = ThiggleQ + "sses/";
   }

   public static enum ExitType {
      Door,
      Window;

      // $FF: synthetic method
      private static IsoRoomExit.ExitType[] $values() {
         return new IsoRoomExit.ExitType[]{Door, Window};
      }
   }
}
