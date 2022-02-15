package zombie.worldMap.styles;

import java.util.ArrayList;
import zombie.core.textures.Texture;
import zombie.worldMap.WorldMapFeature;

public class WorldMapPolygonStyleLayer extends WorldMapStyleLayer {
   public final ArrayList m_fill = new ArrayList();
   public final ArrayList m_texture = new ArrayList();
   public final ArrayList m_scale = new ArrayList();

   public WorldMapPolygonStyleLayer(String var1) {
      super(var1);
   }

   public String getTypeString() {
      return "Polygon";
   }

   public void render(WorldMapFeature var1, WorldMapStyleLayer.RenderArgs var2) {
      WorldMapStyleLayer.RGBAf var3 = this.evalColor(var2, this.m_fill);
      if (var3.a < 0.01F) {
         WorldMapStyleLayer.RGBAf.s_pool.release((Object)var3);
      } else {
         float var4 = this.evalFloat(var2, this.m_scale);
         Texture var5 = this.evalTexture(var2, this.m_texture);
         if (var5 != null && var5.isReady()) {
            var2.drawer.fillPolygon(var2, var1, var3, var5, var4);
         } else {
            var2.drawer.fillPolygon(var2, var1, var3);
         }

         WorldMapStyleLayer.RGBAf.s_pool.release((Object)var3);
      }
   }
}
