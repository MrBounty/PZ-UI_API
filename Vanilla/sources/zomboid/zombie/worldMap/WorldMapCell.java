package zombie.worldMap;

import java.util.ArrayList;
import java.util.Iterator;

public final class WorldMapCell {
   public int m_x;
   public int m_y;
   public final ArrayList m_features = new ArrayList();

   public void hitTest(float var1, float var2, ArrayList var3) {
      var1 -= (float)(this.m_x * 300);
      var2 -= (float)(this.m_y * 300);

      for(int var4 = 0; var4 < this.m_features.size(); ++var4) {
         WorldMapFeature var5 = (WorldMapFeature)this.m_features.get(var4);
         if (var5.containsPoint(var1, var2)) {
            var3.add(var5);
         }
      }

   }

   public void dispose() {
      Iterator var1 = this.m_features.iterator();

      while(var1.hasNext()) {
         WorldMapFeature var2 = (WorldMapFeature)var1.next();
         var2.dispose();
      }

      this.m_features.clear();
   }
}
