package zombie.worldMap.symbols;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.iso.IsoUtils;
import zombie.network.GameServer;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.vehicles.BaseVehicle;
import zombie.worldMap.UIWorldMap;

public class WorldMapSymbols {
   public static final int SAVEFILE_VERSION = 1;
   public final float MIN_VISIBLE_ZOOM = 14.5F;
   public static final float COLLAPSED_RADIUS = 3.0F;
   private final ArrayList m_symbols = new ArrayList();
   private final WorldMapSymbolCollisions m_collision = new WorldMapSymbolCollisions();
   private float m_layoutWorldScale = 0.0F;
   private final Quaternionf m_layoutRotation = new Quaternionf();
   private boolean m_layoutIsometric = true;
   private boolean m_layoutMiniMapSymbols = false;

   public WorldMapTextSymbol addTranslatedText(String var1, UIFont var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.addText(var1, true, var2, var3, var4, 0.0F, 0.0F, WorldMapBaseSymbol.DEFAULT_SCALE, var5, var6, var7, var8);
   }

   public WorldMapTextSymbol addUntranslatedText(String var1, UIFont var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.addText(var1, false, var2, var3, var4, 0.0F, 0.0F, WorldMapBaseSymbol.DEFAULT_SCALE, var5, var6, var7, var8);
   }

   public WorldMapTextSymbol addText(String var1, boolean var2, UIFont var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      WorldMapTextSymbol var13 = new WorldMapTextSymbol(this);
      var13.m_text = var1;
      var13.m_translated = var2;
      var13.m_font = var3;
      var13.m_x = var4;
      var13.m_y = var5;
      if (!GameServer.bServer) {
         var13.m_width = (float)TextManager.instance.MeasureStringX(var3, var13.getTranslatedText());
         var13.m_height = (float)TextManager.instance.getFontHeight(var3);
      }

      var13.m_anchorX = PZMath.clamp(var6, 0.0F, 1.0F);
      var13.m_anchorY = PZMath.clamp(var7, 0.0F, 1.0F);
      var13.m_scale = var8;
      var13.m_r = var9;
      var13.m_g = var10;
      var13.m_b = var11;
      var13.m_a = var12;
      this.m_symbols.add(var13);
      this.m_layoutWorldScale = 0.0F;
      return var13;
   }

   public WorldMapTextureSymbol addTexture(String var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return this.addTexture(var1, var2, var3, 0.0F, 0.0F, WorldMapBaseSymbol.DEFAULT_SCALE, var4, var5, var6, var7);
   }

   public WorldMapTextureSymbol addTexture(String var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      WorldMapTextureSymbol var11 = new WorldMapTextureSymbol(this);
      var11.setSymbolID(var1);
      MapSymbolDefinitions.MapSymbolDefinition var12 = MapSymbolDefinitions.getInstance().getSymbolById(var1);
      if (var12 == null) {
         var11.m_width = 18.0F;
         var11.m_height = 18.0F;
      } else {
         var11.m_texture = GameServer.bServer ? null : Texture.getSharedTexture(var12.getTexturePath());
         var11.m_width = (float)var12.getWidth();
         var11.m_height = (float)var12.getHeight();
      }

      if (var11.m_texture == null && !GameServer.bServer) {
         var11.m_texture = Texture.getErrorTexture();
      }

      var11.m_x = var2;
      var11.m_y = var3;
      var11.m_anchorX = PZMath.clamp(var4, 0.0F, 1.0F);
      var11.m_anchorY = PZMath.clamp(var5, 0.0F, 1.0F);
      var11.m_scale = var6;
      var11.m_r = var7;
      var11.m_g = var8;
      var11.m_b = var9;
      var11.m_a = var10;
      this.m_symbols.add(var11);
      this.m_layoutWorldScale = 0.0F;
      return var11;
   }

   public void removeSymbolByIndex(int var1) {
      WorldMapBaseSymbol var2 = (WorldMapBaseSymbol)this.m_symbols.remove(var1);
      var2.release();
   }

   public void clear() {
      for(int var1 = 0; var1 < this.m_symbols.size(); ++var1) {
         ((WorldMapBaseSymbol)this.m_symbols.get(var1)).release();
      }

      this.m_symbols.clear();
      this.m_layoutWorldScale = 0.0F;
   }

   public void invalidateLayout() {
      this.m_layoutWorldScale = 0.0F;
   }

   public void render(UIWorldMap var1) {
      float var2 = var1.getAPI().worldOriginX();
      float var3 = var1.getAPI().worldOriginY();
      this.checkLayout(var1);
      if (Core.bDebug) {
      }

      boolean var4 = false;

      for(int var5 = 0; var5 < this.m_symbols.size(); ++var5) {
         WorldMapBaseSymbol var6 = (WorldMapBaseSymbol)this.m_symbols.get(var5);
         if (this.isSymbolVisible(var1, var6)) {
            float var7 = var2 + var6.m_layoutX;
            float var8 = var3 + var6.m_layoutY;
            if (!(var7 + var6.widthScaled(var1) <= 0.0F) && !((double)var7 >= var1.getWidth()) && !(var8 + var6.heightScaled(var1) <= 0.0F) && !((double)var8 >= var1.getHeight())) {
               if (var4) {
                  var1.DrawTextureScaledColor((Texture)null, (double)var7, (double)var8, (double)var6.widthScaled(var1), (double)var6.heightScaled(var1), 1.0D, 1.0D, 1.0D, 0.3D);
               }

               var6.render(var1, var2, var3);
            }
         }
      }

   }

   void checkLayout(UIWorldMap var1) {
      Quaternionf var2 = ((Quaternionf)((BaseVehicle.QuaternionfObjectPool)BaseVehicle.TL_quaternionf_pool.get()).alloc()).setFromUnnormalized((Matrix4fc)var1.getAPI().getRenderer().getModelViewMatrix());
      if (this.m_layoutWorldScale == var1.getAPI().getWorldScale() && this.m_layoutIsometric == var1.getAPI().getBoolean("Isometric") && this.m_layoutMiniMapSymbols == var1.getAPI().getBoolean("MiniMapSymbols") && this.m_layoutRotation.equals(var2)) {
         ((BaseVehicle.QuaternionfObjectPool)BaseVehicle.TL_quaternionf_pool.get()).release(var2);
      } else {
         this.m_layoutWorldScale = var1.getAPI().getWorldScale();
         this.m_layoutIsometric = var1.getAPI().getBoolean("Isometric");
         this.m_layoutMiniMapSymbols = var1.getAPI().getBoolean("MiniMapSymbols");
         this.m_layoutRotation.set((Quaternionfc)var2);
         ((BaseVehicle.QuaternionfObjectPool)BaseVehicle.TL_quaternionf_pool.get()).release(var2);
         float var3 = var1.getAPI().worldOriginX();
         float var4 = var1.getAPI().worldOriginY();
         this.m_collision.m_boxes.clear();
         boolean var5 = false;

         int var6;
         WorldMapBaseSymbol var7;
         for(var6 = 0; var6 < this.m_symbols.size(); ++var6) {
            var7 = (WorldMapBaseSymbol)this.m_symbols.get(var6);
            var7.layout(var1, this.m_collision, var3, var4);
            var5 |= var7.m_collided;
         }

         if (var5) {
            for(var6 = 0; var6 < this.m_symbols.size(); ++var6) {
               var7 = (WorldMapBaseSymbol)this.m_symbols.get(var6);
               if (!var7.m_collided && this.m_collision.isCollision(var6)) {
                  var7.m_collided = true;
               }
            }
         }

      }
   }

   public int getSymbolCount() {
      return this.m_symbols.size();
   }

   public WorldMapBaseSymbol getSymbolByIndex(int var1) {
      return (WorldMapBaseSymbol)this.m_symbols.get(var1);
   }

   boolean isSymbolVisible(UIWorldMap var1, WorldMapBaseSymbol var2) {
      return var2.isVisible() && (var2.m_scale <= 0.0F || var1.getAPI().getZoomF() >= 14.5F);
   }

   int hitTest(UIWorldMap var1, float var2, float var3) {
      var2 -= var1.getAPI().worldOriginX();
      var3 -= var1.getAPI().worldOriginY();
      this.checkLayout(var1);
      float var4 = Float.MAX_VALUE;
      int var5 = -1;

      for(int var6 = 0; var6 < this.m_symbols.size(); ++var6) {
         WorldMapBaseSymbol var7 = (WorldMapBaseSymbol)this.m_symbols.get(var6);
         if (this.isSymbolVisible(var1, var7)) {
            float var8 = var7.m_layoutX;
            float var9 = var7.m_layoutY;
            float var10 = var8 + var7.widthScaled(var1);
            float var11 = var9 + var7.heightScaled(var1);
            if (var7.m_collided) {
               var8 += var7.widthScaled(var1) / 2.0F - 1.5F;
               var9 += var7.heightScaled(var1) / 2.0F - 1.5F;
               var10 = var8 + 6.0F;
               var11 = var9 + 6.0F;
               float var12 = IsoUtils.DistanceToSquared((var8 + var10) / 2.0F, (var9 + var11) / 2.0F, var2, var3);
               if (var12 < var4) {
                  var4 = var12;
                  var5 = var6;
               }
            }

            if (var2 >= var8 && var2 < var10 && var3 >= var9 && var3 < var11) {
               return var6;
            }
         }
      }

      if (var5 != -1 && var4 < 100.0F) {
         return var5;
      } else {
         return -1;
      }
   }

   public boolean getMiniMapSymbols() {
      return this.m_layoutMiniMapSymbols;
   }

   public float getLayoutWorldScale() {
      return this.m_layoutWorldScale;
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.putShort((short)1);
      var1.putInt(this.m_symbols.size());

      for(int var2 = 0; var2 < this.m_symbols.size(); ++var2) {
         WorldMapBaseSymbol var3 = (WorldMapBaseSymbol)this.m_symbols.get(var2);
         var1.put((byte)var3.getType().index());
         var3.save(var1);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      short var3 = var1.getShort();
      if (var3 >= 1 && var3 <= 1) {
         int var4 = var1.getInt();

         for(int var5 = 0; var5 < var4; ++var5) {
            byte var6 = var1.get();
            if (var6 == WorldMapSymbols.WorldMapSymbolType.Text.index()) {
               WorldMapTextSymbol var7 = new WorldMapTextSymbol(this);
               var7.load(var1, var2, var3);
               this.m_symbols.add(var7);
            } else {
               if (var6 != WorldMapSymbols.WorldMapSymbolType.Texture.index()) {
                  throw new IOException("unknown map symbol type " + var6);
               }

               WorldMapTextureSymbol var8 = new WorldMapTextureSymbol(this);
               var8.load(var1, var2, var3);
               this.m_symbols.add(var8);
            }
         }

      } else {
         throw new IOException("unknown map symbols version " + var3);
      }
   }

   public static enum WorldMapSymbolType {
      NONE(-1),
      Text(0),
      Texture(1);

      private final byte m_type;

      private WorldMapSymbolType(int var3) {
         this.m_type = (byte)var3;
      }

      int index() {
         return this.m_type;
      }

      // $FF: synthetic method
      private static WorldMapSymbols.WorldMapSymbolType[] $values() {
         return new WorldMapSymbols.WorldMapSymbolType[]{NONE, Text, Texture};
      }
   }
}
