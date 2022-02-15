package zombie.characters.AttachedItems;

import java.util.ArrayList;

public final class AttachedLocations {
   protected static final ArrayList groups = new ArrayList();

   public static AttachedLocationGroup getGroup(String var0) {
      for(int var1 = 0; var1 < groups.size(); ++var1) {
         AttachedLocationGroup var2 = (AttachedLocationGroup)groups.get(var1);
         if (var2.id.equals(var0)) {
            return var2;
         }
      }

      AttachedLocationGroup var3 = new AttachedLocationGroup(var0);
      groups.add(var3);
      return var3;
   }

   public static void Reset() {
      groups.clear();
   }
}
