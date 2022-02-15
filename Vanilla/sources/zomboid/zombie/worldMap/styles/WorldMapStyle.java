package zombie.worldMap.styles;

import java.util.ArrayList;

public final class WorldMapStyle {
   public final ArrayList m_layers = new ArrayList();

   public void copyFrom(WorldMapStyle var1) {
      this.m_layers.clear();
      this.m_layers.addAll(var1.m_layers);
   }
}
