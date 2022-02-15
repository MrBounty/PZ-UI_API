package zombie.worldMap;

import java.util.ArrayList;
import zombie.popman.ObjectPool;
import zombie.worldMap.styles.WorldMapStyleLayer;

public final class WorldMapRenderLayer {
   WorldMapStyleLayer m_styleLayer;
   final ArrayList m_features = new ArrayList();
   static ObjectPool s_pool = new ObjectPool(WorldMapRenderLayer::new);
}
