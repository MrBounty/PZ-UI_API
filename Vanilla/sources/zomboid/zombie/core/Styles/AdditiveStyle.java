package zombie.core.Styles;

import zombie.IndieGL;

public final class AdditiveStyle extends AbstractStyle {
   private static final long serialVersionUID = 1L;
   public static final AdditiveStyle instance = new AdditiveStyle();

   public void setupState() {
      IndieGL.glBlendFuncA(1, 771);
   }

   public void resetState() {
      IndieGL.glBlendFuncA(770, 771);
   }

   public AlphaOp getAlphaOp() {
      return AlphaOp.KEEP;
   }

   public int getStyleID() {
      return 3;
   }

   public boolean getRenderSprite() {
      return true;
   }
}
