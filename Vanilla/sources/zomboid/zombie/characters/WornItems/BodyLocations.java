package zombie.characters.WornItems;

import java.util.ArrayList;

public final class BodyLocations {
   protected static final ArrayList groups = new ArrayList();

   public static BodyLocationGroup getGroup(String var0) {
      for(int var1 = 0; var1 < groups.size(); ++var1) {
         BodyLocationGroup var2 = (BodyLocationGroup)groups.get(var1);
         if (var2.id.equals(var0)) {
            return var2;
         }
      }

      BodyLocationGroup var3 = new BodyLocationGroup(var0);
      groups.add(var3);
      return var3;
   }

   public static void Reset() {
      groups.clear();
   }
}
