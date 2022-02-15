package zombie.characters.AttachedItems;

import java.util.ArrayList;

public final class AttachedLocationGroup {
   protected final String id;
   protected final ArrayList locations = new ArrayList();

   public AttachedLocationGroup(String var1) {
      if (var1 == null) {
         throw new NullPointerException("id is null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("id is empty");
      } else {
         this.id = var1;
      }
   }

   public AttachedLocation getLocation(String var1) {
      for(int var2 = 0; var2 < this.locations.size(); ++var2) {
         AttachedLocation var3 = (AttachedLocation)this.locations.get(var2);
         if (var3.id.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public AttachedLocation getOrCreateLocation(String var1) {
      AttachedLocation var2 = this.getLocation(var1);
      if (var2 == null) {
         var2 = new AttachedLocation(this, var1);
         this.locations.add(var2);
      }

      return var2;
   }

   public AttachedLocation getLocationByIndex(int var1) {
      return var1 >= 0 && var1 < this.size() ? (AttachedLocation)this.locations.get(var1) : null;
   }

   public int size() {
      return this.locations.size();
   }

   public int indexOf(String var1) {
      for(int var2 = 0; var2 < this.locations.size(); ++var2) {
         AttachedLocation var3 = (AttachedLocation)this.locations.get(var2);
         if (var3.id.equals(var1)) {
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
         throw new RuntimeException("no such location \"" + var1 + "\"");
      }
   }
}
