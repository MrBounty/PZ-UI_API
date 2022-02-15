package zombie.core.Styles;

import zombie.IndieGL;

public final class TransparentStyle extends AbstractStyle {
   private static final long serialVersionUID = 1L;
   public static final TransparentStyle instance = new TransparentStyle();

   public void setupState() {
      IndieGL.glBlendFuncA(770, 771);
   }

   public void resetState() {
   }

   public AlphaOp getAlphaOp() {
      return AlphaOp.KEEP;
   }

   public int getStyleID() {
      return 2;
   }

   public boolean getRenderSprite() {
      return true;
   }
}
