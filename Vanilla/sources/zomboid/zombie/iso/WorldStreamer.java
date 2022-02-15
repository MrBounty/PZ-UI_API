package zombie.iso;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import zombie.ChunkMapFilenames;
import zombie.GameWindow;
import zombie.SystemDisabler;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.ThreadGroups;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.erosion.categories.ErosionCategory;
import zombie.gameStates.GameLoadingState;
import zombie.network.ChunkChecksum;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.savefile.PlayerDB;
import zombie.vehicles.VehiclesDB2;

public final class WorldStreamer {
   static final WorldStreamer.ChunkComparator comp = new WorldStreamer.ChunkComparator();
   private static final int CRF_CANCEL = 1;
   private static final int CRF_CANCEL_SENT = 2;
   private static final int CRF_DELETE = 4;
   private static final int CRF_TIMEOUT = 8;
   private static final int CRF_RECEIVED = 16;
   private static final int BLOCK_SIZE = 1024;
   public static WorldStreamer instance = new WorldStreamer();
   private final ConcurrentLinkedQueue jobQueue = new ConcurrentLinkedQueue();
   private final Stack jobList = new Stack();
   private final ConcurrentLinkedQueue chunkRequests0 = new ConcurrentLinkedQueue();
   private final ArrayList chunkRequests1 = new ArrayList();
   private final ArrayList pendingRequests = new ArrayList();
   private final ArrayList pendingRequests1 = new ArrayList();
   private final ConcurrentLinkedQueue sentRequests = new ConcurrentLinkedQueue();
   private final CRC32 crc32 = new CRC32();
   private final ConcurrentLinkedQueue freeBuffers = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue waitingToSendQ = new ConcurrentLinkedQueue();
   private final ArrayList tempRequests = new ArrayList();
   private final Inflater decompressor = new Inflater();
   private final byte[] readBuf = new byte[1024];
   private final ConcurrentLinkedQueue waitingToCancelQ = new ConcurrentLinkedQueue();
   public Thread worldStreamer;
   public boolean bFinished = false;
   private IsoChunk chunkHeadMain;
   private int requestNumber;
   private boolean bCompare = false;
   private boolean NetworkFileDebug;
   private ByteBuffer inMemoryZip;
   private boolean requestingLargeArea = false;
   private volatile int largeAreaDownloads;
   private ByteBuffer bb1 = ByteBuffer.allocate(5120);
   private ByteBuffer bb2 = ByteBuffer.allocate(5120);

   private int bufferSize(int var1) {
      return (var1 + 1024 - 1) / 1024 * 1024;
   }

   private ByteBuffer ensureCapacity(ByteBuffer var1, int var2) {
      if (var1 == null) {
         return ByteBuffer.allocate(this.bufferSize(var2));
      } else if (var1.capacity() < var2) {
         ByteBuffer var3 = ByteBuffer.allocate(this.bufferSize(var2));
         return var3.put(var1.array(), 0, var1.position());
      } else {
         return var1;
      }
   }

   private ByteBuffer getByteBuffer(int var1) {
      ByteBuffer var2 = (ByteBuffer)this.freeBuffers.poll();
      if (var2 == null) {
         return ByteBuffer.allocate(this.bufferSize(var1));
      } else {
         var2.clear();
         return this.ensureCapacity(var2, var1);
      }
   }

   private void releaseBuffer(ByteBuffer var1) {
      this.freeBuffers.add(var1);
   }

   private void sendRequests() throws IOException {
      if (!this.chunkRequests1.isEmpty()) {
         if (!this.requestingLargeArea || this.pendingRequests1.size() <= 20) {
            long var1 = System.currentTimeMillis();
            WorldStreamer.ChunkRequest var3 = null;
            WorldStreamer.ChunkRequest var4 = null;

            for(int var5 = this.chunkRequests1.size() - 1; var5 >= 0; --var5) {
               IsoChunk var6 = (IsoChunk)this.chunkRequests1.get(var5);
               WorldStreamer.ChunkRequest var7 = WorldStreamer.ChunkRequest.alloc();
               var7.chunk = var6;
               var7.requestNumber = this.requestNumber++;
               var7.time = var1;
               var7.crc = ChunkChecksum.getChecksum(var6.wx, var6.wy);
               if (var3 == null) {
                  var3 = var7;
               } else {
                  var4.next = var7;
               }

               var7.next = null;
               var4 = var7;
               this.pendingRequests1.add(var7);
               this.chunkRequests1.remove(var5);
               if (this.requestingLargeArea && this.pendingRequests1.size() >= 40) {
                  break;
               }
            }

            this.waitingToSendQ.add(var3);
         }
      }
   }

   public void updateMain() {
      UdpConnection var1 = GameClient.connection;
      if (this.chunkHeadMain != null) {
         this.chunkRequests0.add(this.chunkHeadMain);
         this.chunkHeadMain = null;
      }

      this.tempRequests.clear();

      WorldStreamer.ChunkRequest var2;
      WorldStreamer.ChunkRequest var3;
      for(var2 = (WorldStreamer.ChunkRequest)this.waitingToSendQ.poll(); var2 != null; var2 = (WorldStreamer.ChunkRequest)this.waitingToSendQ.poll()) {
         for(; var2 != null; var2 = var3) {
            var3 = var2.next;
            if ((var2.flagsWS & 1) != 0) {
               var2.flagsUDP |= 16;
            } else {
               this.tempRequests.add(var2);
            }
         }
      }

      WorldStreamer.ChunkRequest var4;
      ByteBufferWriter var6;
      int var7;
      if (!this.tempRequests.isEmpty()) {
         var6 = var1.startPacket();
         PacketTypes.PacketType.RequestZipList.doPacket(var6);
         var6.putInt(this.tempRequests.size());

         for(var7 = 0; var7 < this.tempRequests.size(); ++var7) {
            var4 = (WorldStreamer.ChunkRequest)this.tempRequests.get(var7);
            var6.putInt(var4.requestNumber);
            var6.putInt(var4.chunk.wx);
            var6.putInt(var4.chunk.wy);
            var6.putLong(var4.crc);
            if (this.NetworkFileDebug) {
               DebugLog.log(DebugType.NetworkFileDebug, "requested " + var4.chunk.wx + "," + var4.chunk.wy + " crc=" + var4.crc);
            }
         }

         PacketTypes.PacketType.RequestZipList.send(var1);

         for(var7 = 0; var7 < this.tempRequests.size(); ++var7) {
            var4 = (WorldStreamer.ChunkRequest)this.tempRequests.get(var7);
            this.sentRequests.add(var4);
         }
      }

      this.tempRequests.clear();

      for(var2 = (WorldStreamer.ChunkRequest)this.waitingToCancelQ.poll(); var2 != null; var2 = (WorldStreamer.ChunkRequest)this.waitingToCancelQ.poll()) {
         this.tempRequests.add(var2);
      }

      if (!this.tempRequests.isEmpty()) {
         var6 = var1.startPacket();
         PacketTypes.PacketType.NotRequiredInZip.doPacket(var6);

         try {
            var6.putInt(this.tempRequests.size());

            for(var7 = 0; var7 < this.tempRequests.size(); ++var7) {
               var4 = (WorldStreamer.ChunkRequest)this.tempRequests.get(var7);
               if (this.NetworkFileDebug) {
                  DebugLog.log(DebugType.NetworkFileDebug, "cancelled " + var4.chunk.wx + "," + var4.chunk.wy);
               }

               var6.putInt(var4.requestNumber);
               var4.flagsMain |= 2;
            }

            PacketTypes.PacketType.NotRequiredInZip.send(var1);
         } catch (Exception var5) {
            var5.printStackTrace();
            var1.cancelPacket();
         }
      }

   }

   private void loadReceivedChunks() throws DataFormatException, IOException {
      boolean var1 = false;
      int var2 = 0;
      int var3 = 0;

      for(int var4 = 0; var4 < this.pendingRequests1.size(); ++var4) {
         WorldStreamer.ChunkRequest var5 = (WorldStreamer.ChunkRequest)this.pendingRequests1.get(var4);
         if ((var5.flagsUDP & 16) != 0) {
            if (var1) {
               ++var2;
               if ((var5.flagsWS & 1) != 0) {
                  ++var3;
               }
            }

            if ((var5.flagsWS & 1) == 0 || (var5.flagsMain & 2) != 0) {
               this.pendingRequests1.remove(var4--);
               ChunkSaveWorker.instance.Update(var5.chunk);
               if ((var5.flagsUDP & 4) != 0) {
                  File var6 = ChunkMapFilenames.instance.getFilename(var5.chunk.wx, var5.chunk.wy);
                  if (var6.exists()) {
                     if (this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, "deleting map_" + var5.chunk.wx + "_" + var5.chunk.wy + ".bin because it doesn't exist on the server");
                     }

                     var6.delete();
                     ChunkChecksum.setChecksum(var5.chunk.wx, var5.chunk.wy, 0L);
                  }
               }

               ByteBuffer var10 = (var5.flagsWS & 1) != 0 ? null : var5.bb;
               if (var10 != null) {
                  try {
                     var10 = this.decompress(var10);
                  } catch (DataFormatException var9) {
                     DebugLog.General.error("WorldStreamer.loadReceivedChunks: Error while the chunk (" + var5.chunk.wx + ", " + var5.chunk.wy + ") was decompressing");
                     this.chunkRequests1.add(var5.chunk);
                     continue;
                  }

                  if (this.bCompare) {
                     File var7 = ChunkMapFilenames.instance.getFilename(var5.chunk.wx, var5.chunk.wy);
                     if (var7.exists()) {
                        this.compare(var5, var10, var7);
                     }
                  }
               }

               if ((var5.flagsWS & 8) == 0) {
                  if ((var5.flagsWS & 1) == 0 && !var5.chunk.refs.isEmpty()) {
                     if (var10 != null) {
                        var10.position(0);
                     }

                     this.DoChunk(var5.chunk, var10);
                  } else {
                     if (this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, var5.chunk.wx + "_" + var5.chunk.wy + " refs.isEmpty() SafeWrite=" + (var10 != null));
                     }

                     if (var10 != null) {
                        long var11 = ChunkChecksum.getChecksumIfExists(var5.chunk.wx, var5.chunk.wy);
                        this.crc32.reset();
                        this.crc32.update(var10.array(), 0, var10.position());
                        if (var11 != this.crc32.getValue()) {
                           ChunkChecksum.setChecksum(var5.chunk.wx, var5.chunk.wy, this.crc32.getValue());
                           IsoChunk.SafeWrite("map_", var5.chunk.wx, var5.chunk.wy, var10);
                        }
                     }

                     var5.chunk.resetForStore();

                     assert !IsoChunkMap.chunkStore.contains(var5.chunk);

                     IsoChunkMap.chunkStore.add(var5.chunk);
                  }
               }

               if (var5.bb != null) {
                  this.releaseBuffer(var5.bb);
               }

               WorldStreamer.ChunkRequest.release(var5);
            }
         }
      }

      if (var1 && (var2 != 0 || var3 != 0 || !this.pendingRequests1.isEmpty())) {
         DebugLog.log("nReceived=" + var2 + " nCancel=" + var3 + " nPending=" + this.pendingRequests1.size());
      }

   }

   private ByteBuffer decompress(ByteBuffer var1) throws DataFormatException {
      this.decompressor.reset();
      this.decompressor.setInput(var1.array(), 0, var1.position());
      int var2 = 0;
      if (this.inMemoryZip != null) {
         this.inMemoryZip.clear();
      }

      while(!this.decompressor.finished()) {
         int var3 = this.decompressor.inflate(this.readBuf);
         if (var3 != 0) {
            this.inMemoryZip = this.ensureCapacity(this.inMemoryZip, var2 + var3);
            this.inMemoryZip.put(this.readBuf, 0, var3);
            var2 += var3;
         } else if (!this.decompressor.finished()) {
            throw new DataFormatException();
         }
      }

      this.inMemoryZip.limit(this.inMemoryZip.position());
      return this.inMemoryZip;
   }

   private void threadLoop() throws DataFormatException, InterruptedException, IOException {
      IsoChunk var1;
      IsoChunk var2;
      if (GameClient.bClient && !SystemDisabler.doWorldSyncEnable) {
         this.NetworkFileDebug = DebugType.Do(DebugType.NetworkFileDebug);

         for(var1 = (IsoChunk)this.chunkRequests0.poll(); var1 != null; var1 = (IsoChunk)this.chunkRequests0.poll()) {
            while(var1 != null) {
               var2 = var1.next;
               this.chunkRequests1.add(var1);
               var1 = var2;
            }
         }

         if (!this.chunkRequests1.isEmpty()) {
            comp.init();
            Collections.sort(this.chunkRequests1, comp);
            this.sendRequests();
         }

         this.loadReceivedChunks();
         this.cancelOutOfBoundsRequests();
         this.resendTimedOutRequests();
      }

      for(var1 = (IsoChunk)this.jobQueue.poll(); var1 != null; var1 = (IsoChunk)this.jobQueue.poll()) {
         if (this.jobList.contains(var1)) {
            DebugLog.log("Ignoring duplicate chunk added to WorldStreamer.jobList");
         } else {
            this.jobList.add(var1);
         }
      }

      if (this.jobList.isEmpty()) {
         ChunkSaveWorker.instance.Update((IsoChunk)null);
         if (ChunkSaveWorker.instance.bSaving) {
            return;
         }

         if (!this.pendingRequests1.isEmpty()) {
            Thread.sleep(20L);
            return;
         }

         Thread.sleep(140L);
      } else {
         int var3 = this.jobList.size() - 1;

         while(true) {
            if (var3 < 0) {
               boolean var4 = !this.jobList.isEmpty();
               var2 = null;
               if (var4) {
                  comp.init();
                  Collections.sort(this.jobList, comp);
                  var2 = (IsoChunk)this.jobList.remove(this.jobList.size() - 1);
               }

               ChunkSaveWorker.instance.Update(var2);
               if (var2 != null) {
                  if (var2.refs.isEmpty()) {
                     var2.resetForStore();

                     assert !IsoChunkMap.chunkStore.contains(var2);

                     IsoChunkMap.chunkStore.add(var2);
                  } else {
                     this.DoChunk(var2, (ByteBuffer)null);
                  }
               }

               if (var4 || ChunkSaveWorker.instance.bSaving) {
                  return;
               }
               break;
            }

            var2 = (IsoChunk)this.jobList.get(var3);
            if (var2.refs.isEmpty()) {
               this.jobList.remove(var3);
               var2.resetForStore();

               assert !IsoChunkMap.chunkStore.contains(var2);

               IsoChunkMap.chunkStore.add(var2);
            }

            --var3;
         }
      }

      if (!GameClient.bClient && !GameWindow.bLoadedAsClient && PlayerDB.isAvailable()) {
         PlayerDB.getInstance().updateWorldStreamer();
      }

      VehiclesDB2.instance.updateWorldStreamer();
      if (IsoPlayer.getInstance() != null) {
         Thread.sleep(140L);
      } else {
         Thread.sleep(0L);
      }

   }

   public void create() {
      if (this.worldStreamer == null) {
         if (!GameServer.bServer) {
            this.bFinished = false;
            this.worldStreamer = new Thread(ThreadGroups.Workers, () -> {
               while(!this.bFinished) {
                  try {
                     this.threadLoop();
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }
               }

            });
            this.worldStreamer.setPriority(5);
            this.worldStreamer.setDaemon(true);
            this.worldStreamer.setName("World Streamer");
            this.worldStreamer.setUncaughtExceptionHandler(GameWindow::uncaughtException);
            this.worldStreamer.start();
         }
      }
   }

   public void addJob(IsoChunk var1, int var2, int var3, boolean var4) {
      if (!GameServer.bServer) {
         var1.wx = var2;
         var1.wy = var3;
         if (GameClient.bClient && !SystemDisabler.doWorldSyncEnable && var4) {
            var1.next = this.chunkHeadMain;
            this.chunkHeadMain = var1;
         } else {
            assert !this.jobQueue.contains(var1);

            assert !this.jobList.contains(var1);

            this.jobQueue.add(var1);
         }
      }
   }

   public void DoChunk(IsoChunk var1, ByteBuffer var2) {
      if (!GameServer.bServer) {
         this.DoChunkAlways(var1, var2);
      }
   }

   public void DoChunkAlways(IsoChunk var1, ByteBuffer var2) {
      if (Core.bDebug && DebugOptions.instance.WorldStreamerSlowLoad.getValue()) {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException var5) {
         }
      }

      if (var1 != null) {
         try {
            if (!var1.LoadOrCreate(var1.wx, var1.wy, var2)) {
               if (GameClient.bClient) {
                  ChunkChecksum.setChecksum(var1.wx, var1.wy, 0L);
               }

               var1.Blam(var1.wx, var1.wy);
               if (!var1.LoadBrandNew(var1.wx, var1.wy)) {
                  return;
               }
            }

            if (var2 == null) {
               VehiclesDB2.instance.loadChunk(var1);
            }
         } catch (Exception var6) {
            DebugLog.General.error("Exception thrown while trying to load chunk: " + var1.wx + ", " + var1.wy);
            var6.printStackTrace();
            if (GameClient.bClient) {
               ChunkChecksum.setChecksum(var1.wx, var1.wy, 0L);
            }

            var1.Blam(var1.wx, var1.wy);
            if (!var1.LoadBrandNew(var1.wx, var1.wy)) {
               return;
            }
         }

         if (var1.jobType != IsoChunk.JobType.Convert && var1.jobType != IsoChunk.JobType.SoftReset) {
            try {
               if (!var1.refs.isEmpty()) {
                  var1.loadInWorldStreamerThread();
               }
            } catch (Exception var4) {
               var4.printStackTrace();
            }

            IsoChunk.loadGridSquare.add(var1);
         } else {
            var1.doLoadGridsquare();
            var1.bLoaded = true;
         }

      }
   }

   public void addJobInstant(IsoChunk var1, int var2, int var3, int var4, int var5) {
      if (!GameServer.bServer) {
         var1.wx = var4;
         var1.wy = var5;

         try {
            this.DoChunkAlways(var1, (ByteBuffer)null);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public void addJobConvert(IsoChunk var1, int var2, int var3, int var4, int var5) {
      if (!GameServer.bServer) {
         var1.wx = var4;
         var1.wy = var5;
         var1.jobType = IsoChunk.JobType.Convert;

         try {
            this.DoChunk(var1, (ByteBuffer)null);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public void addJobWipe(IsoChunk var1, int var2, int var3, int var4, int var5) {
      var1.wx = var4;
      var1.wy = var5;
      var1.jobType = IsoChunk.JobType.SoftReset;

      try {
         this.DoChunkAlways(var1, (ByteBuffer)null);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public boolean isBusy() {
      if (GameClient.bClient && (!this.chunkRequests0.isEmpty() || !this.chunkRequests1.isEmpty() || this.chunkHeadMain != null || !this.waitingToSendQ.isEmpty() || !this.waitingToCancelQ.isEmpty() || !this.sentRequests.isEmpty() || !this.pendingRequests.isEmpty() || !this.pendingRequests1.isEmpty())) {
         return true;
      } else {
         return !this.jobQueue.isEmpty() || !this.jobList.isEmpty();
      }
   }

   public void stop() {
      DebugLog.log("EXITDEBUG: WorldStreamer.stop 1");
      if (this.worldStreamer != null) {
         this.bFinished = true;
         DebugLog.log("EXITDEBUG: WorldStreamer.stop 2");

         while(this.worldStreamer.isAlive()) {
         }

         DebugLog.log("EXITDEBUG: WorldStreamer.stop 3");
         this.worldStreamer = null;
         this.jobList.clear();
         this.jobQueue.clear();
         DebugLog.log("EXITDEBUG: WorldStreamer.stop 4");
         ChunkSaveWorker.instance.SaveNow();
         ChunkChecksum.Reset();
         DebugLog.log("EXITDEBUG: WorldStreamer.stop 5");
      }
   }

   public void quit() {
      this.stop();
   }

   public void requestLargeAreaZip(int var1, int var2, int var3) throws IOException {
      ByteBufferWriter var4 = GameClient.connection.startPacket();
      PacketTypes.PacketType.RequestLargeAreaZip.doPacket(var4);
      var4.putInt(var1);
      var4.putInt(var2);
      var4.putInt(IsoChunkMap.ChunkGridWidth);
      PacketTypes.PacketType.RequestLargeAreaZip.send(GameClient.connection);
      this.requestingLargeArea = true;
      this.largeAreaDownloads = 0;
      GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_RequestMapData");
      int var5 = 0;
      int var6 = var1 - var3;
      int var7 = var2 - var3;
      int var8 = var1 + var3;
      int var9 = var2 + var3;

      for(int var10 = var7; var10 <= var9; ++var10) {
         for(int var11 = var6; var11 <= var8; ++var11) {
            if (IsoWorld.instance.MetaGrid.isValidChunk(var11, var10)) {
               IsoChunk var12 = (IsoChunk)IsoChunkMap.chunkStore.poll();
               if (var12 == null) {
                  var12 = new IsoChunk(IsoWorld.instance.CurrentCell);
               }

               this.addJob(var12, var11, var10, true);
               ++var5;
            }
         }
      }

      DebugLog.log("Requested " + var5 + " chunks from the server");
      long var23 = System.currentTimeMillis();
      long var24 = var23;
      int var14 = 0;
      int var15 = 0;

      while(this.isBusy()) {
         long var16 = System.currentTimeMillis();
         if (var16 - var24 > 60000L) {
            GameLoadingState.mapDownloadFailed = true;
            throw new IOException("map download from server timed out");
         }

         int var18 = this.largeAreaDownloads;
         GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_DownloadedMapData", var18, var5);
         long var19 = var16 - var23;
         if (var19 / 1000L > (long)var14) {
            DebugLog.log("Received " + var18 + " / " + var5 + " chunks");
            var14 = (int)(var19 / 1000L);
         }

         if (var15 < var18) {
            var24 = var16;
            var15 = var18;
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var22) {
         }
      }

      DebugLog.log("Received " + this.largeAreaDownloads + " / " + var5 + " chunks");
      this.requestingLargeArea = false;
   }

   private void cancelOutOfBoundsRequests() {
      if (!this.requestingLargeArea) {
         for(int var1 = 0; var1 < this.pendingRequests1.size(); ++var1) {
            WorldStreamer.ChunkRequest var2 = (WorldStreamer.ChunkRequest)this.pendingRequests1.get(var1);
            if ((var2.flagsWS & 1) == 0 && var2.chunk.refs.isEmpty()) {
               var2.flagsWS |= 1;
               this.waitingToCancelQ.add(var2);
            }
         }

      }
   }

   private void resendTimedOutRequests() {
      long var1 = System.currentTimeMillis();

      for(int var3 = 0; var3 < this.pendingRequests1.size(); ++var3) {
         WorldStreamer.ChunkRequest var4 = (WorldStreamer.ChunkRequest)this.pendingRequests1.get(var3);
         if ((var4.flagsWS & 1) == 0 && var4.time + 8000L < var1) {
            if (this.NetworkFileDebug) {
               DebugLog.log(DebugType.NetworkFileDebug, "chunk request timed out " + var4.chunk.wx + "," + var4.chunk.wy);
            }

            this.chunkRequests1.add(var4.chunk);
            var4.flagsWS |= 9;
            var4.flagsMain |= 2;
         }
      }

   }

   public void receiveChunkPart(ByteBuffer var1) {
      for(WorldStreamer.ChunkRequest var2 = (WorldStreamer.ChunkRequest)this.sentRequests.poll(); var2 != null; var2 = (WorldStreamer.ChunkRequest)this.sentRequests.poll()) {
         this.pendingRequests.add(var2);
      }

      int var10 = var1.getInt();
      int var3 = var1.getInt();
      int var4 = var1.getInt();
      int var5 = var1.getInt();
      int var6 = var1.getInt();
      int var7 = var1.getInt();

      for(int var8 = 0; var8 < this.pendingRequests.size(); ++var8) {
         WorldStreamer.ChunkRequest var9 = (WorldStreamer.ChunkRequest)this.pendingRequests.get(var8);
         if ((var9.flagsWS & 1) != 0) {
            this.pendingRequests.remove(var8--);
            var9.flagsUDP |= 16;
         } else if (var9.requestNumber == var10) {
            if (var9.bb == null) {
               var9.bb = this.getByteBuffer(var5);
            }

            System.arraycopy(var1.array(), var1.position(), var9.bb.array(), var6, var7);
            if (var9.partsReceived == null) {
               var9.partsReceived = new boolean[var3];
            }

            var9.partsReceived[var4] = true;
            if (var9.isReceived()) {
               if (this.NetworkFileDebug) {
                  DebugLog.log(DebugType.NetworkFileDebug, "received all parts for " + var9.chunk.wx + "," + var9.chunk.wy);
               }

               var9.bb.position(var5);
               this.pendingRequests.remove(var8);
               var9.flagsUDP |= 16;
               if (this.requestingLargeArea) {
                  ++this.largeAreaDownloads;
               }
            }
            break;
         }
      }

   }

   public void receiveNotRequired(ByteBuffer var1) {
      for(WorldStreamer.ChunkRequest var2 = (WorldStreamer.ChunkRequest)this.sentRequests.poll(); var2 != null; var2 = (WorldStreamer.ChunkRequest)this.sentRequests.poll()) {
         this.pendingRequests.add(var2);
      }

      int var8 = var1.getInt();

      for(int var3 = 0; var3 < var8; ++var3) {
         int var4 = var1.getInt();
         boolean var5 = var1.get() == 1;

         for(int var6 = 0; var6 < this.pendingRequests.size(); ++var6) {
            WorldStreamer.ChunkRequest var7 = (WorldStreamer.ChunkRequest)this.pendingRequests.get(var6);
            if ((var7.flagsWS & 1) != 0) {
               this.pendingRequests.remove(var6--);
               var7.flagsUDP |= 16;
            } else if (var7.requestNumber == var4) {
               if (this.NetworkFileDebug) {
                  DebugLog.log(DebugType.NetworkFileDebug, "NotRequiredInZip " + var7.chunk.wx + "," + var7.chunk.wy + " delete=" + !var5);
               }

               if (!var5) {
                  var7.flagsUDP |= 4;
               }

               this.pendingRequests.remove(var6);
               var7.flagsUDP |= 16;
               if (this.requestingLargeArea) {
                  ++this.largeAreaDownloads;
               }
               break;
            }
         }
      }

   }

   private void compare(WorldStreamer.ChunkRequest var1, ByteBuffer var2, File var3) throws IOException {
      IsoChunk var4 = (IsoChunk)IsoChunkMap.chunkStore.poll();
      if (var4 == null) {
         var4 = new IsoChunk(IsoWorld.instance.getCell());
      }

      var4.wx = var1.chunk.wx;
      var4.wy = var1.chunk.wy;
      IsoChunk var5 = (IsoChunk)IsoChunkMap.chunkStore.poll();
      if (var5 == null) {
         var5 = new IsoChunk(IsoWorld.instance.getCell());
      }

      var5.wx = var1.chunk.wx;
      var5.wy = var1.chunk.wy;
      int var6 = var2.position();
      var2.position(0);
      var4.LoadFromBuffer(var1.chunk.wx, var1.chunk.wy, var2);
      var2.position(var6);
      this.crc32.reset();
      this.crc32.update(var2.array(), 0, var6);
      long var10000 = this.crc32.getValue();
      DebugLog.log("downloaded crc=" + var10000 + " on-disk crc=" + ChunkChecksum.getChecksumIfExists(var1.chunk.wx, var1.chunk.wy));
      var5.LoadFromDisk();
      DebugLog.log("downloaded size=" + var6 + " on-disk size=" + var3.length());
      this.compareChunks(var4, var5);
      var4.resetForStore();

      assert !IsoChunkMap.chunkStore.contains(var4);

      IsoChunkMap.chunkStore.add(var4);
      var5.resetForStore();

      assert !IsoChunkMap.chunkStore.contains(var5);

      IsoChunkMap.chunkStore.add(var5);
   }

   private void compareChunks(IsoChunk var1, IsoChunk var2) {
      DebugLog.log("comparing " + var1.wx + "," + var1.wy);

      try {
         this.compareErosion(var1, var2);
         if (var1.lootRespawnHour != var2.lootRespawnHour) {
            DebugLog.log("lootRespawnHour " + var1.lootRespawnHour + " != " + var2.lootRespawnHour);
         }

         for(int var3 = 0; var3 < 10; ++var3) {
            for(int var4 = 0; var4 < 10; ++var4) {
               IsoGridSquare var5 = var1.getGridSquare(var4, var3, 0);
               IsoGridSquare var6 = var2.getGridSquare(var4, var3, 0);
               this.compareSquares(var5, var6);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private void compareErosion(IsoChunk var1, IsoChunk var2) {
      if (var1.getErosionData().init != var2.getErosionData().init) {
         boolean var10000 = var1.getErosionData().init;
         DebugLog.log("init " + var10000 + " != " + var2.getErosionData().init);
      }

      int var3;
      if (var1.getErosionData().eTickStamp != var2.getErosionData().eTickStamp) {
         var3 = var1.getErosionData().eTickStamp;
         DebugLog.log("eTickStamp " + var3 + " != " + var2.getErosionData().eTickStamp);
      }

      float var4;
      if (var1.getErosionData().moisture != var2.getErosionData().moisture) {
         var4 = var1.getErosionData().moisture;
         DebugLog.log("moisture " + var4 + " != " + var2.getErosionData().moisture);
      }

      if (var1.getErosionData().minerals != var2.getErosionData().minerals) {
         var4 = var1.getErosionData().minerals;
         DebugLog.log("minerals " + var4 + " != " + var2.getErosionData().minerals);
      }

      if (var1.getErosionData().epoch != var2.getErosionData().epoch) {
         var3 = var1.getErosionData().epoch;
         DebugLog.log("epoch " + var3 + " != " + var2.getErosionData().epoch);
      }

      if (var1.getErosionData().soil != var2.getErosionData().soil) {
         var3 = var1.getErosionData().soil;
         DebugLog.log("soil " + var3 + " != " + var2.getErosionData().soil);
      }

   }

   private void compareSquares(IsoGridSquare var1, IsoGridSquare var2) {
      if (var1 != null && var2 != null) {
         try {
            this.bb1.clear();
            var1.save(this.bb1, (ObjectOutputStream)null);
            this.bb1.flip();
            this.bb2.clear();
            var2.save(this.bb2, (ObjectOutputStream)null);
            this.bb2.flip();
            if (this.bb1.compareTo(this.bb2) != 0) {
               boolean var3 = true;
               int var4 = -1;
               int var5;
               if (this.bb1.limit() == this.bb2.limit()) {
                  for(var5 = 0; var5 < this.bb1.limit(); ++var5) {
                     if (this.bb1.get(var5) != this.bb2.get(var5)) {
                        var4 = var5;
                        break;
                     }
                  }

                  for(var5 = 0; var5 < var1.getErosionData().regions.size(); ++var5) {
                     if (((ErosionCategory.Data)var1.getErosionData().regions.get(var5)).dispSeason != ((ErosionCategory.Data)var2.getErosionData().regions.get(var5)).dispSeason) {
                        int var10000 = ((ErosionCategory.Data)var1.getErosionData().regions.get(var5)).dispSeason;
                        DebugLog.log("season1=" + var10000 + " season2=" + ((ErosionCategory.Data)var2.getErosionData().regions.get(var5)).dispSeason);
                        var3 = false;
                     }
                  }
               }

               DebugLog.log("square " + var1.x + "," + var1.y + " mismatch at " + var4 + " seasonMatch=" + var3 + " #regions=" + var1.getErosionData().regions.size());
               IsoObject var6;
               String var9;
               if (var1.getObjects().size() == var2.getObjects().size()) {
                  for(var5 = 0; var5 < var1.getObjects().size(); ++var5) {
                     var6 = (IsoObject)var1.getObjects().get(var5);
                     IsoObject var7 = (IsoObject)var2.getObjects().get(var5);
                     this.bb1.clear();
                     var6.save(this.bb1);
                     this.bb1.flip();
                     this.bb2.clear();
                     var7.save(this.bb2);
                     this.bb2.flip();
                     if (this.bb1.compareTo(this.bb2) != 0) {
                        var9 = var6.getClass().getName();
                        DebugLog.log("  1: " + var9 + " " + var6.getName() + " " + (var6.sprite == null ? "no sprite" : var6.sprite.name));
                        var9 = var7.getClass().getName();
                        DebugLog.log("  2: " + var9 + " " + var7.getName() + " " + (var7.sprite == null ? "no sprite" : var7.sprite.name));
                     }
                  }
               } else {
                  for(var5 = 0; var5 < var1.getObjects().size(); ++var5) {
                     var6 = (IsoObject)var1.getObjects().get(var5);
                     var9 = var6.getClass().getName();
                     DebugLog.log("  " + var9 + " " + var6.getName() + " " + (var6.sprite == null ? "no sprite" : var6.sprite.name));
                  }
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      } else {
         if (var1 != null || var2 != null) {
            DebugLog.log("one square is null, the other isn't");
         }

      }
   }

   private static final class ChunkRequest {
      static final ArrayDeque pool = new ArrayDeque();
      IsoChunk chunk;
      int requestNumber;
      boolean[] partsReceived = null;
      long crc;
      ByteBuffer bb;
      transient int flagsMain;
      transient int flagsUDP;
      transient int flagsWS;
      long time;
      WorldStreamer.ChunkRequest next;

      boolean isReceived() {
         if (this.partsReceived == null) {
            return false;
         } else {
            for(int var1 = 0; var1 < this.partsReceived.length; ++var1) {
               if (!this.partsReceived[var1]) {
                  return false;
               }
            }

            return true;
         }
      }

      static WorldStreamer.ChunkRequest alloc() {
         return pool.isEmpty() ? new WorldStreamer.ChunkRequest() : (WorldStreamer.ChunkRequest)pool.pop();
      }

      static void release(WorldStreamer.ChunkRequest var0) {
         var0.chunk = null;
         var0.partsReceived = null;
         var0.bb = null;
         var0.flagsMain = 0;
         var0.flagsUDP = 0;
         var0.flagsWS = 0;
         pool.push(var0);
      }
   }

   private static class ChunkComparator implements Comparator {
      private Vector2[] pos = new Vector2[4];

      public ChunkComparator() {
         for(int var1 = 0; var1 < 4; ++var1) {
            this.pos[var1] = new Vector2();
         }

      }

      public void init() {
         for(int var1 = 0; var1 < 4; ++var1) {
            Vector2 var2 = this.pos[var1];
            var2.x = var2.y = -1.0F;
            IsoPlayer var3 = IsoPlayer.players[var1];
            if (var3 != null) {
               if (var3.lx == var3.x && var3.ly == var3.y) {
                  var2.x = var3.x;
                  var2.y = var3.y;
               } else {
                  var2.x = var3.x - var3.lx;
                  var2.y = var3.y - var3.ly;
                  var2.normalize();
                  var2.setLength(10.0F);
                  var2.x += var3.x;
                  var2.y += var3.y;
               }
            }
         }

      }

      public int compare(IsoChunk var1, IsoChunk var2) {
         float var3 = Float.MAX_VALUE;
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < 4; ++var5) {
            if (this.pos[var5].x != -1.0F || this.pos[var5].y != -1.0F) {
               float var6 = this.pos[var5].x;
               float var7 = this.pos[var5].y;
               var3 = Math.min(var3, IsoUtils.DistanceTo(var6, var7, (float)(var1.wx * 10 + 5), (float)(var1.wy * 10 + 5)));
               var4 = Math.min(var4, IsoUtils.DistanceTo(var6, var7, (float)(var2.wx * 10 + 5), (float)(var2.wy * 10 + 5)));
            }
         }

         if (var3 < var4) {
            return 1;
         } else if (var3 > var4) {
            return -1;
         } else {
            return 0;
         }
      }
   }
}
