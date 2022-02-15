package zombie.iso.areas.isoregion.jobs;

import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.core.Core;
import zombie.core.raknet.UdpConnection;

public final class RegionJobManager {
   private static final ConcurrentLinkedQueue poolSquareUpdate = new ConcurrentLinkedQueue();
   private static final ConcurrentLinkedQueue poolChunkUpdate = new ConcurrentLinkedQueue();
   private static final ConcurrentLinkedQueue poolApplyChanges = new ConcurrentLinkedQueue();
   private static final ConcurrentLinkedQueue poolServerSendFullData = new ConcurrentLinkedQueue();
   private static final ConcurrentLinkedQueue poolDebugResetAllData = new ConcurrentLinkedQueue();

   public static JobSquareUpdate allocSquareUpdate(int var0, int var1, int var2, byte var3) {
      JobSquareUpdate var4 = (JobSquareUpdate)poolSquareUpdate.poll();
      if (var4 == null) {
         var4 = new JobSquareUpdate();
      }

      var4.worldSquareX = var0;
      var4.worldSquareY = var1;
      var4.worldSquareZ = var2;
      var4.newSquareFlags = var3;
      return var4;
   }

   public static JobChunkUpdate allocChunkUpdate() {
      JobChunkUpdate var0 = (JobChunkUpdate)poolChunkUpdate.poll();
      if (var0 == null) {
         var0 = new JobChunkUpdate();
      }

      return var0;
   }

   public static JobApplyChanges allocApplyChanges(boolean var0) {
      JobApplyChanges var1 = (JobApplyChanges)poolApplyChanges.poll();
      if (var1 == null) {
         var1 = new JobApplyChanges();
      }

      var1.saveToDisk = var0;
      return var1;
   }

   public static JobServerSendFullData allocServerSendFullData(UdpConnection var0) {
      JobServerSendFullData var1 = (JobServerSendFullData)poolServerSendFullData.poll();
      if (var1 == null) {
         var1 = new JobServerSendFullData();
      }

      var1.targetConn = var0;
      return var1;
   }

   public static JobDebugResetAllData allocDebugResetAllData() {
      JobDebugResetAllData var0 = (JobDebugResetAllData)poolDebugResetAllData.poll();
      if (var0 == null) {
         var0 = new JobDebugResetAllData();
      }

      return var0;
   }

   public static void release(RegionJob var0) {
      var0.reset();
      switch(var0.getJobType()) {
      case SquareUpdate:
         poolSquareUpdate.add((JobSquareUpdate)var0);
         break;
      case ApplyChanges:
         poolApplyChanges.add((JobApplyChanges)var0);
         break;
      case ChunkUpdate:
         poolChunkUpdate.add((JobChunkUpdate)var0);
         break;
      case ServerSendFullData:
         poolServerSendFullData.add((JobServerSendFullData)var0);
         break;
      case DebugResetAllData:
         poolDebugResetAllData.add((JobDebugResetAllData)var0);
         break;
      default:
         if (Core.bDebug) {
            throw new RuntimeException("No pooling for this job type?");
         }
      }

   }
}
