package zombie.worldMap.symbols;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.core.Translator;
import zombie.network.GameServer;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.worldMap.UIWorldMap;

public final class WorldMapTextSymbol extends WorldMapBaseSymbol {
   String m_text;
   boolean m_translated = false;
   UIFont m_font;

   public WorldMapTextSymbol(WorldMapSymbols var1) {
      super(var1);
      this.m_font = UIFont.Handwritten;
   }

   public void setTranslatedText(String var1) {
      this.m_text = var1;
      this.m_translated = true;
      if (!GameServer.bServer) {
         this.m_width = (float)TextManager.instance.MeasureStringX(this.m_font, this.getTranslatedText());
         this.m_height = (float)TextManager.instance.getFontHeight(this.m_font);
      }
   }

   public void setUntranslatedText(String var1) {
      this.m_text = var1;
      this.m_translated = false;
      if (!GameServer.bServer) {
         this.m_width = (float)TextManager.instance.MeasureStringX(this.m_font, this.getTranslatedText());
         this.m_height = (float)TextManager.instance.getFontHeight(this.m_font);
      }
   }

   public String getTranslatedText() {
      return this.m_translated ? this.m_text : Translator.getText(this.m_text);
   }

   public String getUntranslatedText() {
      return this.m_translated ? null : this.m_text;
   }

   public WorldMapSymbols.WorldMapSymbolType getType() {
      return WorldMapSymbols.WorldMapSymbolType.Text;
   }

   public boolean isVisible() {
      return this.m_owner.getMiniMapSymbols() ? false : super.isVisible();
   }

   public void save(ByteBuffer var1) throws IOException {
      super.save(var1);
      GameWindow.WriteString(var1, this.m_text);
      var1.put((byte)(this.m_translated ? 1 : 0));
   }

   public void load(ByteBuffer var1, int var2, int var3) throws IOException {
      super.load(var1, var2, var3);
      this.m_text = GameWindow.ReadString(var1);
      this.m_translated = var1.get() == 1;
   }

   public void render(UIWorldMap var1, float var2, float var3) {
      if (this.m_width == 0.0F || this.m_height == 0.0F) {
         this.m_width = (float)TextManager.instance.MeasureStringX(this.m_font, this.getTranslatedText());
         this.m_height = (float)TextManager.instance.getFontHeight(this.m_font);
      }

      if (this.m_collided) {
         this.renderCollided(var1, var2, var3);
      } else {
         float var4 = var2 + this.m_layoutX;
         float var5 = var3 + this.m_layoutY;
         if (this.m_scale > 0.0F) {
            var1.DrawText(this.m_font, this.getTranslatedText(), (double)var4, (double)var5, (double)this.getDisplayScale(var1), (double)this.m_r, (double)this.m_g, (double)this.m_b, (double)this.m_a);
         } else {
            var1.DrawText(this.m_font, this.getTranslatedText(), (double)var4, (double)var5, (double)this.m_r, (double)this.m_g, (double)this.m_b, (double)this.m_a);
         }
      }

   }

   public void release() {
      this.m_text = null;
   }
}
