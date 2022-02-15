package zombie.core.Styles;

import org.lwjgl.opengl.GL14;

public final class UIFBOStyle extends AbstractStyle {
   public static final UIFBOStyle instance = new UIFBOStyle();

   public void setupState() {
      GL14.glBlendFuncSeparate(770, 771, 1, 771);
   }

   public AlphaOp getAlphaOp() {
      return AlphaOp.KEEP;
   }

   public int getStyleID() {
      return 1;
   }

   public boolean getRenderSprite() {
      return true;
   }
}
