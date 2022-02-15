package zombie.characters.AttachedItems;

public final class AttachedModelName {
   public String attachmentName;
   public String modelName;
   public float bloodLevel;

   public AttachedModelName(AttachedModelName var1) {
      this.attachmentName = var1.attachmentName;
      this.modelName = var1.modelName;
      this.bloodLevel = var1.bloodLevel;
   }

   public AttachedModelName(String var1, String var2, float var3) {
      this.attachmentName = var1;
      this.modelName = var2;
      this.bloodLevel = var3;
   }
}
