package zombie.characters.AttachedItems;

public final class AttachedLocation {
   protected final AttachedLocationGroup group;
   protected final String id;
   protected String attachmentName;

   public AttachedLocation(AttachedLocationGroup var1, String var2) {
      if (var2 == null) {
         throw new NullPointerException("id is null");
      } else if (var2.isEmpty()) {
         throw new IllegalArgumentException("id is empty");
      } else {
         this.group = var1;
         this.id = var2;
      }
   }

   public void setAttachmentName(String var1) {
      if (this.id == null) {
         throw new NullPointerException("attachmentName is null");
      } else if (this.id.isEmpty()) {
         throw new IllegalArgumentException("attachmentName is empty");
      } else {
         this.attachmentName = var1;
      }
   }

   public String getAttachmentName() {
      return this.attachmentName;
   }

   public String getId() {
      return this.id;
   }
}
