package zombie.iso.areas;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.GameWindow;
import zombie.network.GameClient;

public final class NonPvpZone {
   private int x;
   private int y;
   private int x2;
   private int y2;
   private int size;
   private String title;
   public static final ArrayList nonPvpZoneList = new ArrayList();

   public NonPvpZone() {
   }

   public NonPvpZone(String var1, int var2, int var3, int var4, int var5) {
      int var6;
      if (var2 > var4) {
         var6 = var4;
         var4 = var2;
         var2 = var6;
      }

      if (var3 > var5) {
         var6 = var5;
         var5 = var3;
         var3 = var6;
      }

      this.setX(var2);
      this.setX2(var4);
      this.setY(var3);
      this.setY2(var5);
      this.title = var1;
      this.size = Math.abs(var2 - var4 + (var3 - var5));
      this.syncNonPvpZone(false);
   }

   public static NonPvpZone addNonPvpZone(String var0, int var1, int var2, int var3, int var4) {
      NonPvpZone var5 = new NonPvpZone(var0, var1, var2, var3, var4);
      nonPvpZoneList.add(var5);
      return var5;
   }

   public static void removeNonPvpZone(String var0, boolean var1) {
      NonPvpZone var2 = getZoneByTitle(var0);
      if (var2 != null) {
         nonPvpZoneList.remove(var2);
         if (!var1) {
            var2.syncNonPvpZone(true);
         }
      }

   }

   public static NonPvpZone getZoneByTitle(String var0) {
      for(int var1 = 0; var1 < nonPvpZoneList.size(); ++var1) {
         NonPvpZone var2 = (NonPvpZone)nonPvpZoneList.get(var1);
         if (var2.getTitle().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static NonPvpZone getNonPvpZone(int var0, int var1) {
      for(int var2 = 0; var2 < nonPvpZoneList.size(); ++var2) {
         NonPvpZone var3 = (NonPvpZone)nonPvpZoneList.get(var2);
         if (var0 >= var3.getX() && var0 < var3.getX2() && var1 >= var3.getY() && var1 < var3.getY2()) {
            return var3;
         }
      }

      return null;
   }

   public static ArrayList getAllZones() {
      return nonPvpZoneList;
   }

   public void syncNonPvpZone(boolean var1) {
      if (GameClient.bClient) {
         GameClient.sendNonPvpZone(this, var1);
      }

   }

   public void save(ByteBuffer var1) {
      var1.putInt(this.getX());
      var1.putInt(this.getY());
      var1.putInt(this.getX2());
      var1.putInt(this.getY2());
      var1.putInt(this.getSize());
      GameWindow.WriteString(var1, this.getTitle());
   }

   public void load(ByteBuffer var1, int var2) {
      this.setX(var1.getInt());
      this.setY(var1.getInt());
      this.setX2(var1.getInt());
      this.setY2(var1.getInt());
      this.setSize(var1.getInt());
      this.setTitle(GameWindow.ReadString(var1));
   }

   public int getX() {
      return this.x;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getX2() {
      return this.x2;
   }

   public void setX2(int var1) {
      this.x2 = var1;
   }

   public int getY2() {
      return this.y2;
   }

   public void setY2(int var1) {
      this.y2 = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int var1) {
      this.size = var1;
   }
}
