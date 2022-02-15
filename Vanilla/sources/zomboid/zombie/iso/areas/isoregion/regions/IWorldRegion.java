package zombie.iso.areas.isoregion.regions;

import java.util.ArrayList;

public interface IWorldRegion {
   ArrayList getDebugConnectedNeighborCopy();

   ArrayList getNeighbors();

   boolean isFogMask();

   boolean isPlayerRoom();

   boolean isFullyRoofed();

   int getRoofCnt();

   ArrayList getDebugIsoChunkRegionCopy();
}
