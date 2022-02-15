package zombie.network;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import org.lwjglx.BufferUtils;
import zombie.ChunkMapFilenames;
import zombie.core.logger.LoggerManager;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoChunk;

public class PlayerDownloadServer {
   private PlayerDownloadServer.WorkerThread workerThread;
   public int port;
   private UdpConnection connection;
   private boolean NetworkFileDebug;
   private final CRC32 crc32 = new CRC32();
   private ByteBuffer bb = ByteBuffer.allocate(1000000);
   private ByteBuffer sb = BufferUtils.createByteBuffer(1000000);
   private ByteBufferWriter bbw;
   private final ArrayList ccrWaiting;

   public PlayerDownloadServer(UdpConnection var1, int var2) {
      this.bbw = new ByteBufferWriter(this.bb);
      this.ccrWaiting = new ArrayList();
      this.connection = var1;
      this.port = var2;
      this.workerThread = new PlayerDownloadServer.WorkerThread();
      this.workerThread.setDaemon(true);
      this.workerThread.setName("PlayerDownloadServer" + var2);
      this.workerThread.start();
   }

   public void destroy() {
      this.workerThread.putCommand(PlayerDownloadServer.EThreadCommand.Quit, (ClientChunkRequest)null);

      while(this.workerThread.isAlive()) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var2) {
         }
      }

      this.workerThread = null;
   }

   public void startConnectionTest() {
   }

   public void receiveRequestArray(ByteBuffer var1) throws Exception {
      ClientChunkRequest var2 = (ClientChunkRequest)this.workerThread.freeRequests.poll();
      if (var2 == null) {
         var2 = new ClientChunkRequest();
      }

      var2.largeArea = false;
      this.ccrWaiting.add(var2);
      int var3 = var1.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var2.chunks.size() >= 20) {
            var2 = (ClientChunkRequest)this.workerThread.freeRequests.poll();
            if (var2 == null) {
               var2 = new ClientChunkRequest();
            }

            var2.largeArea = false;
            this.ccrWaiting.add(var2);
         }

         ClientChunkRequest.Chunk var5 = var2.getChunk();
         var5.requestNumber = var1.getInt();
         var5.wx = var1.getInt();
         var5.wy = var1.getInt();
         var5.crc = var1.getLong();
         var2.chunks.add(var5);
      }

   }

   public void receiveRequestLargeArea(ByteBuffer var1) {
      ClientChunkRequest var2 = new ClientChunkRequest();
      var2.unpackLargeArea(var1, this.connection);

      for(int var3 = 0; var3 < var2.chunks.size(); ++var3) {
         ClientChunkRequest.Chunk var4 = (ClientChunkRequest.Chunk)var2.chunks.get(var3);
         IsoChunk var5 = ServerMap.instance.getChunk(var4.wx, var4.wy);
         if (var5 != null) {
            var2.getByteBuffer(var4);

            try {
               var5.SaveLoadedChunk(var4, this.crc32);
            } catch (Exception var7) {
               var7.printStackTrace();
               LoggerManager.getLogger("map").write(var7);
               var2.releaseBuffer(var4);
            }
         }
      }

      this.workerThread.putCommand(PlayerDownloadServer.EThreadCommand.RequestLargeArea, var2);
   }

   public void receiveCancelRequest(ByteBuffer var1) {
      int var2 = var1.getInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1.getInt();
         this.workerThread.cancelQ.add(var4);
      }

   }

   public void update() {
      this.NetworkFileDebug = DebugType.Do(DebugType.NetworkFileDebug);
      if (this.workerThread.bReady) {
         this.removeOlderDuplicateRequests();
         if (this.ccrWaiting.isEmpty()) {
            if (this.workerThread.cancelQ.isEmpty() && !this.workerThread.cancelled.isEmpty()) {
               this.workerThread.cancelled.clear();
            }

         } else {
            ClientChunkRequest var1 = (ClientChunkRequest)this.ccrWaiting.remove(0);

            for(int var2 = 0; var2 < var1.chunks.size(); ++var2) {
               ClientChunkRequest.Chunk var3 = (ClientChunkRequest.Chunk)var1.chunks.get(var2);
               if (this.workerThread.isRequestCancelled(var3)) {
                  var1.chunks.remove(var2--);
                  var1.releaseChunk(var3);
               } else {
                  IsoChunk var4 = ServerMap.instance.getChunk(var3.wx, var3.wy);
                  if (var4 != null) {
                     try {
                        var1.getByteBuffer(var3);
                        var4.SaveLoadedChunk(var3, this.crc32);
                     } catch (Exception var6) {
                        var6.printStackTrace();
                        LoggerManager.getLogger("map").write(var6);
                        this.workerThread.sendNotRequired(var3, false);
                        var1.chunks.remove(var2--);
                        var1.releaseChunk(var3);
                     }
                  }
               }
            }

            if (var1.chunks.isEmpty()) {
               this.workerThread.freeRequests.add(var1);
            } else {
               this.workerThread.bReady = false;
               this.workerThread.putCommand(PlayerDownloadServer.EThreadCommand.RequestZipArray, var1);
            }
         }
      }
   }

   private void removeOlderDuplicateRequests() {
      for(int var1 = this.ccrWaiting.size() - 1; var1 >= 0; --var1) {
         ClientChunkRequest var2 = (ClientChunkRequest)this.ccrWaiting.get(var1);

         for(int var3 = 0; var3 < var2.chunks.size(); ++var3) {
            ClientChunkRequest.Chunk var4 = (ClientChunkRequest.Chunk)var2.chunks.get(var3);
            if (this.workerThread.isRequestCancelled(var4)) {
               var2.chunks.remove(var3--);
               var2.releaseChunk(var4);
            } else {
               for(int var5 = var1 - 1; var5 >= 0; --var5) {
                  ClientChunkRequest var6 = (ClientChunkRequest)this.ccrWaiting.get(var5);
                  if (this.cancelDuplicateChunk(var6, var4.wx, var4.wy)) {
                  }
               }
            }
         }

         if (var2.chunks.isEmpty()) {
            this.ccrWaiting.remove(var1);
            this.workerThread.freeRequests.add(var2);
         }
      }

   }

   private boolean cancelDuplicateChunk(ClientChunkRequest var1, int var2, int var3) {
      for(int var4 = 0; var4 < var1.chunks.size(); ++var4) {
         ClientChunkRequest.Chunk var5 = (ClientChunkRequest.Chunk)var1.chunks.get(var4);
         if (this.workerThread.isRequestCancelled(var5)) {
            var1.chunks.remove(var4--);
            var1.releaseChunk(var5);
         } else if (var5.wx == var2 && var5.wy == var3) {
            this.workerThread.sendNotRequired(var5, false);
            var1.chunks.remove(var4);
            var1.releaseChunk(var5);
            return true;
         }
      }

      return false;
   }

   private void sendPacket(PacketTypes.PacketType var1) {
      this.bb.flip();
      this.sb.put(this.bb);
      this.sb.flip();
      this.connection.getPeer().SendRaw(this.sb, var1.PacketPriority, var1.PacketReliability, (byte)0, this.connection.getConnectedGUID(), false);
      this.sb.clear();
   }

   private ByteBufferWriter startPacket() {
      this.bb.clear();
      return this.bbw;
   }

   private final class WorkerThread extends Thread {
      boolean bQuit;
      volatile boolean bReady = true;
      final LinkedBlockingQueue commandQ = new LinkedBlockingQueue();
      final ConcurrentLinkedQueue freeRequests = new ConcurrentLinkedQueue();
      final ConcurrentLinkedQueue cancelQ = new ConcurrentLinkedQueue();
      final ArrayList cancelled = new ArrayList();
      final CRC32 crcMaker = new CRC32();
      static final int chunkSize = 1000;
      private byte[] inMemoryZip = new byte[20480];
      private final Deflater compressor = new Deflater();

      public void run() {
         while(!this.bQuit) {
            try {
               this.runInner();
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }

      }

      private void runInner() throws InterruptedException, IOException {
         MPStatistic.getInstance().PlayerDownloadServer.End();
         PlayerDownloadServer.WorkerThreadCommand var1 = (PlayerDownloadServer.WorkerThreadCommand)this.commandQ.take();
         MPStatistic.getInstance().PlayerDownloadServer.Start();
         switch(var1.e) {
         case RequestLargeArea:
            try {
               this.sendLargeArea(var1.ccr);
               break;
            } finally {
               this.bReady = true;
            }
         case RequestZipArray:
            try {
               this.sendArray(var1.ccr);
               break;
            } finally {
               this.bReady = true;
            }
         case Quit:
            this.bQuit = true;
         }

      }

      void putCommand(PlayerDownloadServer.EThreadCommand var1, ClientChunkRequest var2) {
         PlayerDownloadServer.WorkerThreadCommand var3 = new PlayerDownloadServer.WorkerThreadCommand();
         var3.e = var1;
         var3.ccr = var2;

         while(true) {
            try {
               this.commandQ.put(var3);
               return;
            } catch (InterruptedException var5) {
            }
         }
      }

      private int compressChunk(ClientChunkRequest.Chunk var1) {
         this.compressor.reset();
         this.compressor.setInput(var1.bb.array(), 0, var1.bb.limit());
         this.compressor.finish();
         if ((double)this.inMemoryZip.length < (double)var1.bb.limit() * 1.5D) {
            this.inMemoryZip = new byte[(int)((double)var1.bb.limit() * 1.5D)];
         }

         return this.compressor.deflate(this.inMemoryZip, 0, this.inMemoryZip.length, 3);
      }

      private void sendChunk(ClientChunkRequest.Chunk var1) {
         try {
            long var2 = (long)this.compressChunk(var1);
            long var4 = var2 / 1000L;
            if (var2 % 1000L != 0L) {
               ++var4;
            }

            long var6 = 0L;

            for(int var8 = 0; (long)var8 < var4; ++var8) {
               long var9 = var2 - var6 > 1000L ? 1000L : var2 - var6;
               ByteBufferWriter var11 = PlayerDownloadServer.this.startPacket();
               PacketTypes.PacketType.SentChunk.doPacket(var11);
               var11.putInt(var1.requestNumber);
               var11.putInt((int)var4);
               var11.putInt(var8);
               var11.putInt((int)var2);
               var11.putInt((int)var6);
               var11.putInt((int)var9);
               var11.bb.put(this.inMemoryZip, (int)var6, (int)var9);
               PlayerDownloadServer.this.sendPacket(PacketTypes.PacketType.SentChunk);
               var6 += var9;
            }
         } catch (Exception var12) {
            var12.printStackTrace();
            this.sendNotRequired(var1, false);
         }

      }

      private void sendNotRequired(ClientChunkRequest.Chunk var1, boolean var2) {
         ByteBufferWriter var3 = PlayerDownloadServer.this.startPacket();
         PacketTypes.PacketType.NotRequiredInZip.doPacket(var3);
         var3.putInt(1);
         var3.putInt(var1.requestNumber);
         var3.putByte((byte)(var2 ? 1 : 0));
         PlayerDownloadServer.this.sendPacket(PacketTypes.PacketType.NotRequiredInZip);
      }

      private void sendLargeArea(ClientChunkRequest var1) throws IOException {
         for(int var2 = 0; var2 < var1.chunks.size(); ++var2) {
            ClientChunkRequest.Chunk var3 = (ClientChunkRequest.Chunk)var1.chunks.get(var2);
            int var4 = var3.wx;
            int var5 = var3.wy;
            if (var3.bb != null) {
               var3.bb.limit(var3.bb.position());
               var3.bb.position(0);
               this.sendChunk(var3);
               var1.releaseBuffer(var3);
            } else {
               File var6 = ChunkMapFilenames.instance.getFilename(var4, var5);
               if (var6.exists()) {
                  var1.getByteBuffer(var3);
                  var3.bb = IsoChunk.SafeRead("map_", var4, var5, var3.bb);
                  this.sendChunk(var3);
                  var1.releaseBuffer(var3);
               }
            }
         }

         ClientChunkRequest.freeBuffers.clear();
         var1.chunks.clear();
      }

      private void sendArray(ClientChunkRequest var1) throws IOException {
         int var2;
         for(var2 = 0; var2 < var1.chunks.size(); ++var2) {
            ClientChunkRequest.Chunk var3 = (ClientChunkRequest.Chunk)var1.chunks.get(var2);
            if (!this.isRequestCancelled(var3)) {
               int var4 = var3.wx;
               int var5 = var3.wy;
               long var6 = var3.crc;
               if (var3.bb != null) {
                  boolean var12 = true;
                  if (var3.crc != 0L) {
                     this.crcMaker.reset();
                     this.crcMaker.update(var3.bb.array(), 0, var3.bb.position());
                     var12 = var3.crc != this.crcMaker.getValue();
                     if (var12 && PlayerDownloadServer.this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": crc server=" + this.crcMaker.getValue() + " client=" + var3.crc);
                     }
                  }

                  if (var12) {
                     if (PlayerDownloadServer.this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=true loaded=true");
                     }

                     var3.bb.limit(var3.bb.position());
                     var3.bb.position(0);
                     this.sendChunk(var3);
                  } else {
                     if (PlayerDownloadServer.this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=false loaded=true");
                     }

                     this.sendNotRequired(var3, true);
                  }

                  var1.releaseBuffer(var3);
               } else {
                  File var8 = ChunkMapFilenames.instance.getFilename(var4, var5);
                  if (var8.exists()) {
                     long var9 = ChunkChecksum.getChecksum(var4, var5);
                     if (var9 != 0L && var9 == var3.crc) {
                        if (PlayerDownloadServer.this.NetworkFileDebug) {
                           DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=false loaded=false file=true");
                        }

                        this.sendNotRequired(var3, true);
                     } else {
                        var1.getByteBuffer(var3);
                        var3.bb = IsoChunk.SafeRead("map_", var4, var5, var3.bb);
                        boolean var11 = true;
                        if (var3.crc != 0L) {
                           this.crcMaker.reset();
                           this.crcMaker.update(var3.bb.array(), 0, var3.bb.limit());
                           var11 = var3.crc != this.crcMaker.getValue();
                        }

                        if (var11) {
                           if (PlayerDownloadServer.this.NetworkFileDebug) {
                              DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=true loaded=false file=true");
                           }

                           this.sendChunk(var3);
                        } else {
                           if (PlayerDownloadServer.this.NetworkFileDebug) {
                              DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=false loaded=false file=true");
                           }

                           this.sendNotRequired(var3, true);
                        }

                        var1.releaseBuffer(var3);
                     }
                  } else {
                     if (PlayerDownloadServer.this.NetworkFileDebug) {
                        DebugLog.log(DebugType.NetworkFileDebug, var4 + "," + var5 + ": send=false loaded=false file=false");
                     }

                     this.sendNotRequired(var3, var6 == 0L);
                  }
               }
            }
         }

         for(var2 = 0; var2 < var1.chunks.size(); ++var2) {
            var1.releaseChunk((ClientChunkRequest.Chunk)var1.chunks.get(var2));
         }

         var1.chunks.clear();
         this.freeRequests.add(var1);
      }

      private boolean isRequestCancelled(ClientChunkRequest.Chunk var1) {
         for(Integer var2 = (Integer)this.cancelQ.poll(); var2 != null; var2 = (Integer)this.cancelQ.poll()) {
            this.cancelled.add(var2);
         }

         for(int var4 = 0; var4 < this.cancelled.size(); ++var4) {
            Integer var3 = (Integer)this.cancelled.get(var4);
            if (var3 == var1.requestNumber) {
               if (PlayerDownloadServer.this.NetworkFileDebug) {
                  DebugLog.log(DebugType.NetworkFileDebug, "cancelled request #" + var3);
               }

               this.cancelled.remove(var4);
               return true;
            }
         }

         return false;
      }
   }

   private static enum EThreadCommand {
      RequestLargeArea,
      RequestZipArray,
      Quit;

      // $FF: synthetic method
      private static PlayerDownloadServer.EThreadCommand[] $values() {
         return new PlayerDownloadServer.EThreadCommand[]{RequestLargeArea, RequestZipArray, Quit};
      }
   }

   private static final class WorkerThreadCommand {
      PlayerDownloadServer.EThreadCommand e;
      ClientChunkRequest ccr;
   }
}
