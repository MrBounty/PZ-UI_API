package zombie.core.Styles;

public interface Style {
   void setupState();

   void resetState();

   int getStyleID();

   AlphaOp getAlphaOp();

   boolean getRenderSprite();

   GeometryData build();

   void render(int var1, int var2);
}
