package zombie.core.Styles;

public abstract class AbstractStyle implements Style {
   private static final long serialVersionUID = 1L;

   public boolean getRenderSprite() {
      return false;
   }

   public AlphaOp getAlphaOp() {
      return null;
   }

   public int getStyleID() {
      return this.hashCode();
   }

   public void resetState() {
   }

   public void setupState() {
   }

   public GeometryData build() {
      return null;
   }

   public void render(int var1, int var2) {
   }
}
