package zombie.worldMap.symbols;

import java.util.ArrayList;
import java.util.Objects;
import zombie.Lua.LuaManager;
import zombie.ui.UIFont;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.worldMap.UIWorldMap;

public class WorldMapSymbolsV1 {
   private static final Pool s_textPool = new Pool(WorldMapSymbolsV1.WorldMapTextSymbolV1::new);
   private static final Pool s_texturePool = new Pool(WorldMapSymbolsV1.WorldMapTextureSymbolV1::new);
   private final UIWorldMap m_ui;
   private final WorldMapSymbols m_uiSymbols;
   private final ArrayList m_symbols = new ArrayList();

   public WorldMapSymbolsV1(UIWorldMap var1, WorldMapSymbols var2) {
      Objects.requireNonNull(var1);
      this.m_ui = var1;
      this.m_uiSymbols = var2;
      this.reinit();
   }

   public WorldMapSymbolsV1.WorldMapTextSymbolV1 addTranslatedText(String var1, UIFont var2, float var3, float var4) {
      WorldMapTextSymbol var5 = this.m_uiSymbols.addTranslatedText(var1, var2, var3, var4, 1.0F, 1.0F, 1.0F, 1.0F);
      WorldMapSymbolsV1.WorldMapTextSymbolV1 var6 = ((WorldMapSymbolsV1.WorldMapTextSymbolV1)s_textPool.alloc()).init(this, var5);
      this.m_symbols.add(var6);
      return var6;
   }

   public WorldMapSymbolsV1.WorldMapTextSymbolV1 addUntranslatedText(String var1, UIFont var2, float var3, float var4) {
      WorldMapTextSymbol var5 = this.m_uiSymbols.addUntranslatedText(var1, var2, var3, var4, 1.0F, 1.0F, 1.0F, 1.0F);
      WorldMapSymbolsV1.WorldMapTextSymbolV1 var6 = ((WorldMapSymbolsV1.WorldMapTextSymbolV1)s_textPool.alloc()).init(this, var5);
      this.m_symbols.add(var6);
      return var6;
   }

   public WorldMapSymbolsV1.WorldMapTextureSymbolV1 addTexture(String var1, float var2, float var3) {
      WorldMapTextureSymbol var4 = this.m_uiSymbols.addTexture(var1, var2, var3, 1.0F, 1.0F, 1.0F, 1.0F);
      WorldMapSymbolsV1.WorldMapTextureSymbolV1 var5 = ((WorldMapSymbolsV1.WorldMapTextureSymbolV1)s_texturePool.alloc()).init(this, var4);
      this.m_symbols.add(var5);
      return var5;
   }

   public int hitTest(float var1, float var2) {
      return this.m_uiSymbols.hitTest(this.m_ui, var1, var2);
   }

   public int getSymbolCount() {
      return this.m_symbols.size();
   }

   public WorldMapSymbolsV1.WorldMapBaseSymbolV1 getSymbolByIndex(int var1) {
      return (WorldMapSymbolsV1.WorldMapBaseSymbolV1)this.m_symbols.get(var1);
   }

   public void removeSymbolByIndex(int var1) {
      this.m_uiSymbols.removeSymbolByIndex(var1);
      ((WorldMapSymbolsV1.WorldMapBaseSymbolV1)this.m_symbols.remove(var1)).release();
   }

   public void clear() {
      this.m_uiSymbols.clear();
      this.reinit();
   }

   void reinit() {
      int var1;
      for(var1 = 0; var1 < this.m_symbols.size(); ++var1) {
         ((WorldMapSymbolsV1.WorldMapBaseSymbolV1)this.m_symbols.get(var1)).release();
      }

      this.m_symbols.clear();

      for(var1 = 0; var1 < this.m_uiSymbols.getSymbolCount(); ++var1) {
         WorldMapBaseSymbol var2 = this.m_uiSymbols.getSymbolByIndex(var1);
         WorldMapTextSymbol var3 = (WorldMapTextSymbol)Type.tryCastTo(var2, WorldMapTextSymbol.class);
         if (var3 != null) {
            WorldMapSymbolsV1.WorldMapTextSymbolV1 var4 = ((WorldMapSymbolsV1.WorldMapTextSymbolV1)s_textPool.alloc()).init(this, var3);
            this.m_symbols.add(var4);
         }

         WorldMapTextureSymbol var6 = (WorldMapTextureSymbol)Type.tryCastTo(var2, WorldMapTextureSymbol.class);
         if (var6 != null) {
            WorldMapSymbolsV1.WorldMapTextureSymbolV1 var5 = ((WorldMapSymbolsV1.WorldMapTextureSymbolV1)s_texturePool.alloc()).init(this, var6);
            this.m_symbols.add(var5);
         }
      }

   }

   public static void setExposed(LuaManager.Exposer var0) {
      var0.setExposed(WorldMapSymbolsV1.class);
      var0.setExposed(WorldMapSymbolsV1.WorldMapTextSymbolV1.class);
      var0.setExposed(WorldMapSymbolsV1.WorldMapTextureSymbolV1.class);
   }

   public static class WorldMapTextSymbolV1 extends WorldMapSymbolsV1.WorldMapBaseSymbolV1 {
      WorldMapTextSymbol m_textSymbol;

      WorldMapSymbolsV1.WorldMapTextSymbolV1 init(WorldMapSymbolsV1 var1, WorldMapTextSymbol var2) {
         super.init(var1, var2);
         this.m_textSymbol = var2;
         return this;
      }

      public void setTranslatedText(String var1) {
         if (!StringUtils.isNullOrWhitespace(var1)) {
            this.m_textSymbol.setTranslatedText(var1);
            this.m_owner.m_uiSymbols.invalidateLayout();
         }
      }

      public void setUntranslatedText(String var1) {
         if (!StringUtils.isNullOrWhitespace(var1)) {
            this.m_textSymbol.setUntranslatedText(var1);
            this.m_owner.m_uiSymbols.invalidateLayout();
         }
      }

      public String getTranslatedText() {
         return this.m_textSymbol.getTranslatedText();
      }

      public String getUntranslatedText() {
         return this.m_textSymbol.getUntranslatedText();
      }

      public boolean isText() {
         return true;
      }
   }

   public static class WorldMapTextureSymbolV1 extends WorldMapSymbolsV1.WorldMapBaseSymbolV1 {
      WorldMapTextureSymbol m_textureSymbol;

      WorldMapSymbolsV1.WorldMapTextureSymbolV1 init(WorldMapSymbolsV1 var1, WorldMapTextureSymbol var2) {
         super.init(var1, var2);
         this.m_textureSymbol = var2;
         return this;
      }

      public String getSymbolID() {
         return this.m_textureSymbol.getSymbolID();
      }

      public boolean isTexture() {
         return true;
      }
   }

   protected static class WorldMapBaseSymbolV1 extends PooledObject {
      WorldMapSymbolsV1 m_owner;
      WorldMapBaseSymbol m_symbol;

      WorldMapSymbolsV1.WorldMapBaseSymbolV1 init(WorldMapSymbolsV1 var1, WorldMapBaseSymbol var2) {
         this.m_owner = var1;
         this.m_symbol = var2;
         return this;
      }

      public float getWorldX() {
         return this.m_symbol.m_x;
      }

      public float getWorldY() {
         return this.m_symbol.m_y;
      }

      public float getDisplayX() {
         return this.m_symbol.m_layoutX + this.m_owner.m_ui.getAPIv1().worldOriginX();
      }

      public float getDisplayY() {
         return this.m_symbol.m_layoutY + this.m_owner.m_ui.getAPIv1().worldOriginY();
      }

      public float getDisplayWidth() {
         return this.m_symbol.widthScaled(this.m_owner.m_ui);
      }

      public float getDisplayHeight() {
         return this.m_symbol.heightScaled(this.m_owner.m_ui);
      }

      public void setAnchor(float var1, float var2) {
         this.m_symbol.setAnchor(var1, var2);
      }

      public void setPosition(float var1, float var2) {
         this.m_symbol.setPosition(var1, var2);
         this.m_owner.m_uiSymbols.invalidateLayout();
      }

      public void setCollide(boolean var1) {
         this.m_symbol.setCollide(var1);
      }

      public void setVisible(boolean var1) {
         this.m_symbol.setVisible(var1);
      }

      public boolean isVisible() {
         return this.m_symbol.isVisible();
      }

      public void setRGBA(float var1, float var2, float var3, float var4) {
         this.m_symbol.setRGBA(var1, var2, var3, var4);
      }

      public float getRed() {
         return this.m_symbol.m_r;
      }

      public float getGreen() {
         return this.m_symbol.m_g;
      }

      public float getBlue() {
         return this.m_symbol.m_b;
      }

      public float getAlpha() {
         return this.m_symbol.m_a;
      }

      public void setScale(float var1) {
         this.m_symbol.setScale(var1);
      }

      public boolean isText() {
         return false;
      }

      public boolean isTexture() {
         return false;
      }
   }
}
