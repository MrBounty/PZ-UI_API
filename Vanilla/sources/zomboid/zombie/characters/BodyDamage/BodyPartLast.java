package zombie.characters.BodyDamage;

public class BodyPartLast {
   private boolean bandaged;
   private boolean bitten;
   private boolean scratched;
   private boolean cut = false;

   public boolean bandaged() {
      return this.bandaged;
   }

   public boolean bitten() {
      return this.bitten;
   }

   public boolean scratched() {
      return this.scratched;
   }

   public boolean isCut() {
      return this.cut;
   }

   public void copy(BodyPart var1) {
      this.bandaged = var1.bandaged();
      this.bitten = var1.bitten();
      this.scratched = var1.scratched();
      this.cut = var1.isCut();
   }
}
