package zombie.characters.WornItems;

import java.util.ArrayList;

public final class BodyLocationGroup {
   protected final String id;
   protected final ArrayList locations = new ArrayList();

   public BodyLocationGroup(String var1) {
      if (var1 == null) {
         throw new NullPointerException("id is null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("id is empty");
      } else {
         this.id = var1;
      }
   }

   public BodyLocation getLocation(String var1) {
      for(int var2 = 0; var2 < this.locations.size(); ++var2) {
         BodyLocation var3 = (BodyLocation)this.locations.get(var2);
         if (var3.isID(var1)) {
            return var3;
         }
      }

      return null;
   }

   public BodyLocation getLocationNotNull(String var1) {
      BodyLocation var2 = this.getLocation(var1);
      if (var2 == null) {
         throw new RuntimeException("unknown location \"" + var1 + "\"");
      } else {
         return var2;
      }
   }

   public BodyLocation getOrCreateLocation(String var1) {
      BodyLocation var2 = this.getLocation(var1);
      if (var2 == null) {
         var2 = new BodyLocation(this, var1);
         this.locations.add(var2);
      }

      return var2;
   }

   public BodyLocation getLocationByIndex(int var1) {
      return var1 >= 0 && var1 < this.size() ? (BodyLocation)this.locations.get(var1) : null;
   }

   public int size() {
      return this.locations.size();
   }

   public void setExclusive(String var1, String var2) {
      BodyLocation var3 = this.getLocationNotNull(var1);
      BodyLocation var4 = this.getLocationNotNull(var2);
      var3.setExclusive(var2);
      var4.setExclusive(var1);
   }

   public boolean isExclusive(String var1, String var2) {
      BodyLocation var3 = this.getLocationNotNull(var1);
      this.checkValid(var2);
      return var3.exclusive.contains(var2);
   }

   public void setHideModel(String var1, String var2) {
      BodyLocation var3 = this.getLocationNotNull(var1);
      this.checkValid(var2);
      var3.setHideModel(var2);
   }

   public boolean isHideModel(String var1, String var2) {
      BodyLocation var3 = this.getLocationNotNull(var1);
      this.checkValid(var2);
      return var3.isHideModel(var2);
   }

   public int indexOf(String var1) {
      for(int var2 = 0; var2 < this.locations.size(); ++var2) {
         BodyLocation var3 = (BodyLocation)this.locations.get(var2);
         if (var3.isID(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public void checkValid(String var1) {
      if (var1 == null) {
         throw new NullPointerException("locationId is null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("locationId is empty");
      } else if (this.indexOf(var1) == -1) {
         throw new RuntimeException("unknown location \"" + var1 + "\"");
      }
   }

   public void setMultiItem(String var1, boolean var2) {
      BodyLocation var3 = this.getLocationNotNull(var1);
      var3.setMultiItem(var2);
   }

   public boolean isMultiItem(String var1) {
      BodyLocation var2 = this.getLocationNotNull(var1);
      return var2.isMultiItem();
   }

   public ArrayList getAllLocations() {
      return this.locations;
   }
}
