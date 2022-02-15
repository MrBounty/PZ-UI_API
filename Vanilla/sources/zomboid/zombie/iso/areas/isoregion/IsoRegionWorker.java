package zombie.iso.areas.isoregion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import zombie.GameWindow;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.ThreadGroups;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoWorld;
import zombie.iso.areas.isoregion.data.DataChunk;
import zombie.iso.areas.isoregion.data.DataRoot;
import zombie.iso.areas.isoregion.jobs.JobApplyChanges;
import zombie.iso.areas.isoregion.jobs.JobChunkUpdate;
import zombie.iso.areas.isoregion.jobs.JobServerSendFullData;
import zombie.iso.areas.isoregion.jobs.JobSquareUpdate;
import zombie.iso.areas.isoregion.jobs.RegionJob;
import zombie.iso.areas.isoregion.jobs.RegionJobManager;
import zombie.iso.areas.isoregion.jobs.RegionJobType;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;

public final class IsoRegionWorker {
   private Thread thread;
   private boolean bFinished;
   protected static final AtomicBoolean isRequestingBufferSwap = new AtomicBoolean(false);
   private static IsoRegionWorker instance;
   private DataRoot rootBuffer = new DataRoot();
   private List discoveredChunks = new ArrayList();
   private final List threadDiscoveredChunks = new ArrayList();
   private int lastThreadDiscoveredChunksSize = 0;
   private final ConcurrentLinkedQueue jobQueue = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue jobOutgoingQueue = new ConcurrentLinkedQueue();
   private final List jobBatchedProcessing = new ArrayList();
   private final ConcurrentLinkedQueue finishedJobQueue = new ConcurrentLinkedQueue();
   private static final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

   protected IsoRegionWorker() {
      instance = this;
   }

   protected void create() {
      if (this.thread == null) {
         this.bFinished = false;
         this.thread = new Thread(ThreadGroups.Workers, () -> {
            while(!this.bFinished) {
               try {
                  this.thread_main_loop();
               } catch (Exception var2) {
                  var2.printStackTrace();
               }
            }

         });
         this.thread.setPriority(5);
         this.thread.setDaemon(true);
         this.thread.setName("IsoRegionWorker");
         this.thread.setUncaughtExceptionHandler(GameWindow::uncaughtException);
         this.thread.start();
      }
   }

   protected void stop() {
      if (this.thread != null) {
         if (this.thread != null) {
            this.bFinished = true;

            while(true) {
               if (!this.thread.isAlive()) {
                  this.thread = null;
                  break;
               }
            }
         }

         if (this.jobQueue.size() > 0) {
            DebugLog.IsoRegion.warn("IsoRegionWorker -> JobQueue has items remaining");
         }

         if (this.jobBatchedProcessing.size() > 0) {
            DebugLog.IsoRegion.warn("IsoRegionWorker -> JobBatchedProcessing has items remaining");
         }

         this.jobQueue.clear();
         this.jobOutgoingQueue.clear();
         this.jobBatchedProcessing.clear();
         this.finishedJobQueue.clear();
         this.rootBuffer = null;
         this.discoveredChunks = null;
      }
   }

   protected void EnqueueJob(RegionJob var1) {
      this.jobQueue.add(var1);
   }

   protected void ApplyChunkChanges() {
      this.ApplyChunkChanges(true);
   }

   protected void ApplyChunkChanges(boolean var1) {
      JobApplyChanges var2 = RegionJobManager.allocApplyChanges(var1);
      this.jobQueue.add(var2);
   }

   private void thread_main_loop() throws InterruptedException, IsoRegionException {
      IsoRegions.PRINT_D = DebugLog.isEnabled(DebugType.IsoRegion);

      for(RegionJob var1 = (RegionJob)this.jobQueue.poll(); var1 != null; var1 = (RegionJob)this.jobQueue.poll()) {
         switch(var1.getJobType()) {
         case ServerSendFullData:
            if (!GameServer.bServer) {
               break;
            }

            UdpConnection var7 = ((JobServerSendFullData)var1).getTargetConn();
            if (var7 == null) {
               if (Core.bDebug) {
                  throw new IsoRegionException("IsoRegion: Server send full data target connection == null");
               }

               IsoRegions.warn("IsoRegion: Server send full data target connection == null");
               break;
            }

            IsoRegions.log("IsoRegion: Server Send Full Data to " + var7.idStr);
            ArrayList var3 = new ArrayList();
            this.rootBuffer.getAllChunks(var3);
            JobChunkUpdate var4 = RegionJobManager.allocChunkUpdate();
            var4.setTargetConn(var7);
            Iterator var5 = var3.iterator();

            DataChunk var6;
            for(; var5.hasNext(); var4.addChunkFromDataChunk(var6)) {
               var6 = (DataChunk)var5.next();
               if (!var4.canAddChunk()) {
                  this.jobOutgoingQueue.add(var4);
                  var4 = RegionJobManager.allocChunkUpdate();
                  var4.setTargetConn(var7);
               }
            }

            if (var4.getChunkCount() > 0) {
               this.jobOutgoingQueue.add(var4);
            } else {
               RegionJobManager.release(var4);
            }

            this.finishedJobQueue.add(var1);
            break;
         case DebugResetAllData:
            IsoRegions.log("IsoRegion: Debug Reset All Data");

            for(int var2 = 0; var2 < 2; ++var2) {
               this.rootBuffer.resetAllData();
               if (var2 == 0) {
                  isRequestingBufferSwap.set(true);

                  while(isRequestingBufferSwap.get() && !this.bFinished) {
                     Thread.sleep(5L);
                  }
               }
            }

            this.finishedJobQueue.add(var1);
            break;
         case SquareUpdate:
         case ChunkUpdate:
         case ApplyChanges:
            IsoRegions.log("IsoRegion: Queueing " + var1.getJobType() + " for batched processing.");
            this.jobBatchedProcessing.add(var1);
            if (var1.getJobType() == RegionJobType.ApplyChanges) {
               this.thread_run_batched_jobs();
               this.jobBatchedProcessing.clear();
            }
            break;
         default:
            this.finishedJobQueue.add(var1);
         }
      }

      Thread.sleep(20L);
   }

   private void thread_run_batched_jobs() throws InterruptedException {
      IsoRegions.log("IsoRegion: Apply changes -> Batched processing " + this.jobBatchedProcessing.size() + " jobs.");

      for(int var1 = 0; var1 < 2; ++var1) {
         for(int var2 = 0; var2 < this.jobBatchedProcessing.size(); ++var2) {
            RegionJob var3 = (RegionJob)this.jobBatchedProcessing.get(var2);
            switch(var3.getJobType()) {
            case SquareUpdate:
               JobSquareUpdate var11 = (JobSquareUpdate)var3;
               this.rootBuffer.updateExistingSquare(var11.getWorldSquareX(), var11.getWorldSquareY(), var11.getWorldSquareZ(), var11.getNewSquareFlags());
               break;
            case ChunkUpdate:
               JobChunkUpdate var10 = (JobChunkUpdate)var3;
               var10.readChunksPacket(this.rootBuffer, this.threadDiscoveredChunks);
               break;
            case ApplyChanges:
               this.rootBuffer.processDirtyChunks();
               if (var1 == 0) {
                  isRequestingBufferSwap.set(true);

                  while(isRequestingBufferSwap.get()) {
                     Thread.sleep(5L);
                  }
               } else {
                  JobApplyChanges var4 = (JobApplyChanges)var3;
                  if (!GameClient.bClient && var4.isSaveToDisk()) {
                     for(int var5 = this.jobBatchedProcessing.size() - 1; var5 >= 0; --var5) {
                        RegionJob var6 = (RegionJob)this.jobBatchedProcessing.get(var5);
                        if (var6.getJobType() == RegionJobType.ChunkUpdate || var6.getJobType() == RegionJobType.SquareUpdate) {
                           JobChunkUpdate var7;
                           if (var6.getJobType() == RegionJobType.SquareUpdate) {
                              JobSquareUpdate var8 = (JobSquareUpdate)var6;
                              this.rootBuffer.select.reset(var8.getWorldSquareX(), var8.getWorldSquareY(), var8.getWorldSquareZ(), true, false);
                              var7 = RegionJobManager.allocChunkUpdate();
                              var7.addChunkFromDataChunk(this.rootBuffer.select.chunk);
                           } else {
                              this.jobBatchedProcessing.remove(var5);
                              var7 = (JobChunkUpdate)var6;
                           }

                           var7.saveChunksToDisk();
                           if (GameServer.bServer) {
                              this.jobOutgoingQueue.add(var7);
                           }
                        }
                     }

                     if (this.threadDiscoveredChunks.size() > 0 && this.threadDiscoveredChunks.size() > this.lastThreadDiscoveredChunksSize && !Core.getInstance().isNoSave()) {
                        IsoRegions.log("IsoRegion: Apply changes -> Saving header file to disk.");
                        File var12 = IsoRegions.getHeaderFile();

                        try {
                           DataOutputStream var13 = new DataOutputStream(new FileOutputStream(var12));
                           var13.writeInt(186);
                           var13.writeInt(this.threadDiscoveredChunks.size());
                           Iterator var14 = this.threadDiscoveredChunks.iterator();

                           while(var14.hasNext()) {
                              Integer var15 = (Integer)var14.next();
                              var13.writeInt(var15);
                           }

                           var13.flush();
                           var13.close();
                           this.lastThreadDiscoveredChunksSize = this.threadDiscoveredChunks.size();
                        } catch (Exception var9) {
                           DebugLog.log(var9.getMessage());
                           var9.printStackTrace();
                        }
                     }
                  }

                  this.finishedJobQueue.addAll(this.jobBatchedProcessing);
               }
            }
         }
      }

   }

   protected DataRoot getRootBuffer() {
      return this.rootBuffer;
   }

   protected void setRootBuffer(DataRoot var1) {
      this.rootBuffer = var1;
   }

   protected void load() {
      IsoRegions.log("IsoRegion: Load save map.");
      if (!GameClient.bClient) {
         this.loadSaveMap();
      } else {
         GameClient.sendIsoRegionDataRequest();
      }

   }

   protected void update() {
      for(RegionJob var1 = (RegionJob)this.finishedJobQueue.poll(); var1 != null; var1 = (RegionJob)this.finishedJobQueue.poll()) {
         RegionJobManager.release(var1);
      }

      for(JobChunkUpdate var7 = (JobChunkUpdate)this.jobOutgoingQueue.poll(); var7 != null; var7 = (JobChunkUpdate)this.jobOutgoingQueue.poll()) {
         if (GameServer.bServer) {
            IsoRegions.log("IsoRegion: sending changed datachunks packet.");

            try {
               for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
                  UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);
                  if (var7.getTargetConn() == null || var7.getTargetConn() == var3) {
                     ByteBufferWriter var4 = var3.startPacket();
                     PacketTypes.PacketType.IsoRegionServerPacket.doPacket(var4);
                     ByteBuffer var5 = var4.bb;
                     var5.putLong(System.nanoTime());
                     var7.saveChunksToNetBuffer(var5);
                     PacketTypes.PacketType.IsoRegionServerPacket.send(var3);
                  }
               }
            } catch (Exception var6) {
               DebugLog.log(var6.getMessage());
               var6.printStackTrace();
            }
         }

         RegionJobManager.release(var7);
      }

   }

   protected void readServerUpdatePacket(ByteBuffer var1) {
      if (GameClient.bClient) {
         IsoRegions.log("IsoRegion: Receiving changed datachunk packet from server");

         try {
            JobChunkUpdate var2 = RegionJobManager.allocChunkUpdate();
            long var3 = var1.getLong();
            var2.readChunksFromNetBuffer(var1, var3);
            this.EnqueueJob(var2);
            this.ApplyChunkChanges();
         } catch (Exception var5) {
            DebugLog.log(var5.getMessage());
            var5.printStackTrace();
         }
      }

   }

   protected void readClientRequestFullUpdatePacket(ByteBuffer var1, UdpConnection var2) {
      if (GameServer.bServer && var2 != null) {
         IsoRegions.log("IsoRegion: Receiving request full data packet from client");

         try {
            JobServerSendFullData var3 = RegionJobManager.allocServerSendFullData(var2);
            this.EnqueueJob(var3);
         } catch (Exception var4) {
            DebugLog.log(var4.getMessage());
            var4.printStackTrace();
         }
      }

   }

   protected void addDebugResetJob() {
      if (!GameServer.bServer && !GameClient.bClient) {
         this.EnqueueJob(RegionJobManager.allocDebugResetAllData());
      }

   }

   protected void addSquareChangedJob(int var1, int var2, int var3, boolean var4, byte var5) {
      int var6 = var1 / 10;
      int var7 = var2 / 10;
      int var8 = IsoRegions.hash(var6, var7);
      if (this.discoveredChunks.contains(var8)) {
         IsoRegions.log("Update square only, plus any unprocessed chunks in a 7x7 grid.", Colors.Magenta);
         JobSquareUpdate var9 = RegionJobManager.allocSquareUpdate(var1, var2, var3, var5);
         this.EnqueueJob(var9);
         this.readSurroundingChunks(var6, var7, 7, false);
         this.ApplyChunkChanges();
      } else {
         if (var4) {
            return;
         }

         IsoRegions.log("Adding new chunk, plus any unprocessed chunks in a 7x7 grid.", Colors.Magenta);
         this.readSurroundingChunks(var6, var7, 7, true);
      }

   }

   protected void readSurroundingChunks(int var1, int var2, int var3, boolean var4) {
      this.readSurroundingChunks(var1, var2, var3, var4, false);
   }

   protected void readSurroundingChunks(int var1, int var2, int var3, boolean var4, boolean var5) {
      int var6 = 1;
      if (var3 > 0 && var3 <= IsoChunkMap.ChunkGridWidth) {
         var6 = var3 / 2;
         if (var6 + var6 >= IsoChunkMap.ChunkGridWidth) {
            --var6;
         }
      }

      int var7 = var1 - var6;
      int var8 = var2 - var6;
      int var9 = var1 + var6;
      int var10 = var2 + var6;
      JobChunkUpdate var11 = RegionJobManager.allocChunkUpdate();
      boolean var13 = false;

      for(int var14 = var7; var14 <= var9; ++var14) {
         for(int var15 = var8; var15 <= var10; ++var15) {
            IsoChunk var12 = GameServer.bServer ? ServerMap.instance.getChunk(var14, var15) : IsoWorld.instance.getCell().getChunk(var14, var15);
            if (var12 != null) {
               int var16 = IsoRegions.hash(var12.wx, var12.wy);
               if (var5 || !this.discoveredChunks.contains(var16)) {
                  this.discoveredChunks.add(var16);
                  if (!var11.canAddChunk()) {
                     this.EnqueueJob(var11);
                     var11 = RegionJobManager.allocChunkUpdate();
                  }

                  var11.addChunkFromIsoChunk(var12);
                  var13 = true;
               }
            }
         }
      }

      if (var11.getChunkCount() > 0) {
         this.EnqueueJob(var11);
      } else {
         RegionJobManager.release(var11);
      }

      if (var13 && var4) {
         this.ApplyChunkChanges();
      }

   }

   private void loadSaveMap() {
      try {
         boolean var1 = false;
         ArrayList var2 = new ArrayList();
         File var3 = IsoRegions.getHeaderFile();
         if (var3.exists()) {
            DataInputStream var4 = new DataInputStream(new FileInputStream(var3));
            var1 = true;
            int var5 = var4.readInt();
            int var6 = var4.readInt();
            int var8 = 0;

            while(true) {
               if (var8 >= var6) {
                  var4.close();
                  break;
               }

               int var7 = var4.readInt();
               var2.add(var7);
               ++var8;
            }
         }

         File var23 = IsoRegions.getDirectory();
         File[] var24 = var23.listFiles(new FilenameFilter() {
            public boolean accept(File var1, String var2) {
               return var2.startsWith("datachunk_") && var2.endsWith(".bin");
            }
         });
         JobChunkUpdate var25 = RegionJobManager.allocChunkUpdate();
         ByteBuffer var26 = byteBuffer;
         boolean var27 = false;
         if (var24 != null) {
            File[] var9 = var24;
            int var10 = var24.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               File var12 = var9[var11];
               FileInputStream var13 = new FileInputStream(var12);

               try {
                  var26.clear();
                  int var14 = var13.read(var26.array());
                  var26.limit(var14);
                  var26.mark();
                  int var15 = var26.getInt();
                  int var16 = var26.getInt();
                  int var17 = var26.getInt();
                  int var18 = var26.getInt();
                  var26.reset();
                  int var19 = IsoRegions.hash(var17, var18);
                  if (!this.discoveredChunks.contains(var19)) {
                     this.discoveredChunks.add(var19);
                  }

                  if (var2.contains(var19)) {
                     var2.remove(var2.indexOf(var19));
                  } else {
                     IsoRegions.warn("IsoRegion: A chunk save has been found that was not in header known chunks list.");
                  }

                  if (!var25.canAddChunk()) {
                     this.EnqueueJob(var25);
                     var25 = RegionJobManager.allocChunkUpdate();
                  }

                  var25.addChunkFromFile(var26);
                  var27 = true;
               } catch (Throwable var21) {
                  try {
                     var13.close();
                  } catch (Throwable var20) {
                     var21.addSuppressed(var20);
                  }

                  throw var21;
               }

               var13.close();
            }
         }

         if (var25.getChunkCount() > 0) {
            this.EnqueueJob(var25);
         } else {
            RegionJobManager.release(var25);
         }

         if (var27) {
            this.ApplyChunkChanges(false);
         }

         if (var1 && var2.size() > 0) {
            IsoRegions.warn("IsoRegion: " + var2.size() + " previously discovered chunks have not been loaded.");
            throw new IsoRegionException("IsoRegion: " + var2.size() + " previously discovered chunks have not been loaded.");
         }
      } catch (Exception var22) {
         DebugLog.log(var22.getMessage());
         var22.printStackTrace();
      }

   }
}
