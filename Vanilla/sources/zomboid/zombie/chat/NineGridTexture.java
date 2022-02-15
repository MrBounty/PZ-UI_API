package zombie.chat;

import java.util.function.Consumer;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;

public class NineGridTexture {
   private Texture topLeft;
   private Texture topMid;
   private Texture topRight;
   private Texture left;
   private Texture mid;
   private Texture right;
   private Texture botLeft;
   private Texture botMid;
   private Texture botRight;
   private int outer;

   public NineGridTexture(String var1, int var2) {
      this.outer = var2;
      this.topLeft = Texture.getSharedTexture(var1 + "_topleft");
      this.topMid = Texture.getSharedTexture(var1 + "_topmid");
      this.topRight = Texture.getSharedTexture(var1 + "_topright");
      this.left = Texture.getSharedTexture(var1 + "_left");
      this.mid = Texture.getSharedTexture(var1 + "_mid");
      this.right = Texture.getSharedTexture(var1 + "_right");
      this.botLeft = Texture.getSharedTexture(var1 + "_botleft");
      this.botMid = Texture.getSharedTexture(var1 + "_botmid");
      this.botRight = Texture.getSharedTexture(var1 + "_botright");
   }

   public void renderInnerBased(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8) {
      var2 += 5;
      var4 -= 7;
      SpriteRenderer.instance.renderi(this.topLeft, var1 - this.outer, var2 - this.outer, this.outer, this.outer, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.topMid, var1, var2 - this.outer, var3, this.outer, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.topRight, var1 + var3, var2 - this.outer, this.outer, this.outer, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.left, var1 - this.outer, var2, this.outer, var4, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.mid, var1, var2, var3, var4, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.right, var1 + var3, var2, this.outer, var4, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.botLeft, var1 - this.outer, var2 + var4, this.outer, this.outer, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.botMid, var1, var2 + var4, var3, this.outer, var5, var6, var7, var8, (Consumer)null);
      SpriteRenderer.instance.renderi(this.botRight, var1 + var3, var2 + var4, this.outer, this.outer, var5, var6, var7, var8, (Consumer)null);
   }
}
