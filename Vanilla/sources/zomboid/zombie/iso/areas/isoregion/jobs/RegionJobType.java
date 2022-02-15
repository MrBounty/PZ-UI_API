package zombie.iso.areas.isoregion.jobs;

public enum RegionJobType {
   SquareUpdate,
   ChunkUpdate,
   ApplyChanges,
   ServerSendFullData,
   DebugResetAllData;

   // $FF: synthetic method
   private static RegionJobType[] $values() {
      return new RegionJobType[]{SquareUpdate, ChunkUpdate, ApplyChanges, ServerSendFullData, DebugResetAllData};
   }
}
