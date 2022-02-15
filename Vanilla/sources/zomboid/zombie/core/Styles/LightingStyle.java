package zombie.core.Styles;

import zombie.IndieGL;

public final class LightingStyle extends AbstractStyle {
   private static final long serialVersionUID = 1L;
   public static final LightingStyle instance = new LightingStyle();

   public void setupState() {
      IndieGL.glBlendFuncA(0, 768);
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
