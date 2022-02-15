package zombie.characters.AttachedItems;

import java.util.ArrayList;
import zombie.inventory.types.HandWeapon;
import zombie.util.StringUtils;
import zombie.util.Type;

public final class AttachedModelNames {
   protected AttachedLocationGroup group;
   protected final ArrayList models = new ArrayList();

   AttachedLocationGroup getGroup() {
      return this.group;
   }

   public void copyFrom(AttachedModelNames var1) {
      this.models.clear();

      for(int var2 = 0; var2 < var1.models.size(); ++var2) {
         AttachedModelName var3 = (AttachedModelName)var1.models.get(var2);
         this.models.add(new AttachedModelName(var3));
      }

   }

   public void initFrom(AttachedItems var1) {
      this.group = var1.getGroup();
      this.models.clear();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         AttachedItem var3 = var1.get(var2);
         String var4 = var3.getItem().getStaticModel();
         if (!StringUtils.isNullOrWhitespace(var4)) {
            String var5 = this.group.getLocation(var3.getLocation()).getAttachmentName();
            HandWeapon var6 = (HandWeapon)Type.tryCastTo(var3.getItem(), HandWeapon.class);
            float var7 = var6 == null ? 0.0F : var6.getBloodLevel();
            this.models.add(new AttachedModelName(var5, var4, var7));
         }
      }

   }

   public int size() {
      return this.models.size();
   }

   public AttachedModelName get(int var1) {
      return (AttachedModelName)this.models.get(var1);
   }

   public void clear() {
      this.models.clear();
   }
}
