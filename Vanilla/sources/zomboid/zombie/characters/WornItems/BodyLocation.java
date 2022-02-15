package zombie.characters.WornItems;

import java.util.ArrayList;

public final class BodyLocation {
   protected final BodyLocationGroup group;
   protected final String id;
   protected final ArrayList aliases = new ArrayList();
   protected final ArrayList exclusive = new ArrayList();
   protected final ArrayList hideModel = new ArrayList();
   protected boolean bMultiItem = false;

   public BodyLocation(BodyLocationGroup var1, String var2) {
      this.checkId(var2, "id");
      this.group = var1;
      this.id = var2;
   }

   public BodyLocation addAlias(String var1) {
      this.checkId(var1, "alias");
      if (this.aliases.contains(var1)) {
         return this;
      } else {
         this.aliases.add(var1);
         return this;
      }
   }

   public BodyLocation setExclusive(String var1) {
      this.checkId(var1, "otherId");
      if (this.aliases.contains(var1)) {
         return this;
      } else if (this.exclusive.contains(var1)) {
         return this;
      } else {
         this.exclusive.add(var1);
         return this;
      }
   }

   public BodyLocation setHideModel(String var1) {
      this.checkId(var1, "otherId");
      if (this.hideModel.contains(var1)) {
         return this;
      } else {
         this.hideModel.add(var1);
         return this;
      }
   }

   public boolean isMultiItem() {
      return this.bMultiItem;
   }

   public BodyLocation setMultiItem(boolean var1) {
      this.bMultiItem = var1;
      return this;
   }

   public boolean isHideModel(String var1) {
      return this.hideModel.contains(var1);
   }

   public boolean isExclusive(String var1) {
      return this.group.isExclusive(this.id, var1);
   }

   public boolean isID(String var1) {
      return this.id.equals(var1) || this.aliases.contains(var1);
   }

   private void checkId(String var1, String var2) {
      if (var1 == null) {
         throw new NullPointerException(var2 + " is null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException(var2 + " is empty");
      }
   }

   public String getId() {
      return this.id;
   }
}
