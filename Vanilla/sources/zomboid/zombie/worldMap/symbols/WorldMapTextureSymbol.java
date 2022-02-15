package zombie.worldMap.symbols;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import zombie.GameWindow;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.worldMap.UIWorldMap;

public final class WorldMapTextureSymbol extends WorldMapBaseSymbol {
   private String m_symbolID;
   Texture m_texture;

   public WorldMapTextureSymbol(WorldMapSymbols var1) {
      super(var1);
   }

   public void setSymbolID(String var1) {
      this.m_symbolID = var1;
   }

   public String getSymbolID() {
      return this.m_symbolID;
   }

   public void checkTexture() {
      if (this.m_texture == null) {
         MapSymbolDefinitions.MapSymbolDefinition var1 = MapSymbolDefinitions.getInstance().getSymbolById(this.getSymbolID());
         if (var1 == null) {
            this.m_width = 18.0F;
            this.m_height = 18.0F;
         } else {
            this.m_texture = Texture.getSharedTexture(var1.getTexturePath());
            this.m_width = (float)var1.getWidth();
            this.m_height = (float)var1.getHeight();
         }

         if (this.m_texture == null) {
            this.m_texture = Texture.getErrorTexture();
         }

      }
   }

   public WorldMapSymbols.WorldMapSymbolType getType() {
      return WorldMapSymbols.WorldMapSymbolType.Texture;
   }

   public void layout(UIWorldMap var1, WorldMapSymbolCollisions var2, float var3, float var4) {
      this.checkTexture();
      super.layout(var1, var2, var3, var4);
   }

   public void save(ByteBuffer var1) throws IOException {
      super.save(var1);
      GameWindow.WriteString(var1, this.m_symbolID);
   }

   public void load(ByteBuffer var1, int var2, int var3) throws IOException {
      super.load(var1, var2, var3);
      this.m_symbolID = GameWindow.ReadString(var1);
   }

   public void render(UIWorldMap var1, float var2, float var3) {
      if (this.m_collided) {
         this.renderCollided(var1, var2, var3);
      } else {
         this.checkTexture();
         float var4 = var2 + this.m_layoutX;
         float var5 = var3 + this.m_layoutY;
         if (this.m_scale > 0.0F) {
            float var6 = this.getDisplayScale(var1);
            SpriteRenderer.instance.m_states.getPopulatingActiveState().render(this.m_texture, (float)var1.getAbsoluteX().intValue() + var4, (float)var1.getAbsoluteY().intValue() + var5, (float)this.m_texture.getWidth() * var6, (float)this.m_texture.getHeight() * var6, this.m_r, this.m_g, this.m_b, this.m_a, (Consumer)null);
         } else {
            var1.DrawTextureColor(this.m_texture, (double)var4, (double)var5, (double)this.m_r, (double)this.m_g, (double)this.m_b, (double)this.m_a);
         }
      }

   }

   public void release() {
      this.m_symbolID = null;
      this.m_texture = null;
   }
}
