package zombie.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.core.raknet.UdpConnection;

public class ClientChunkRequest {
   public ArrayList chunks = new ArrayList(20);
   private static final ConcurrentLinkedQueue freeChunks = new ConcurrentLinkedQueue();
   public static final ConcurrentLinkedQueue freeBuffers = new ConcurrentLinkedQueue();
   public boolean largeArea = false;
   int minX;
   int maxX;
   int minY;
   int maxY;

   public ClientChunkRequest.Chunk getChunk() {
      ClientChunkRequest.Chunk var1 = (ClientChunkRequest.Chunk)freeChunks.poll();
      if (var1 == null) {
         var1 = new ClientChunkRequest.Chunk();
      }

      return var1;
   }

   public void releaseChunk(ClientChunkRequest.Chunk var1) {
      this.releaseBuffer(var1);
      freeChunks.add(var1);
   }

   public void getByteBuffer(ClientChunkRequest.Chunk var1) {
      var1.bb = (ByteBuffer)freeBuffers.poll();
      if (var1.bb == null) {
         var1.bb = ByteBuffer.allocate(16384);
      } else {
         var1.bb.clear();
      }

   }

   public void releaseBuffer(ClientChunkRequest.Chunk var1) {
      if (var1.bb != null) {
         freeBuffers.add(var1.bb);
         var1.bb = null;
      }

   }

   public void releaseBuffers() {
      for(int var1 = 0; var1 < this.chunks.size(); ++var1) {
         ((ClientChunkRequest.Chunk)this.chunks.get(var1)).bb = null;
      }

   }

   public void unpack(ByteBuffer var1, UdpConnection var2) {
      int var3;
      for(var3 = 0; var3 < this.chunks.size(); ++var3) {
         this.releaseBuffer((ClientChunkRequest.Chunk)this.chunks.get(var3));
      }

      freeChunks.addAll(this.chunks);
      this.chunks.clear();
      var3 = var1.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         ClientChunkRequest.Chunk var5 = this.getChunk();
         var5.requestNumber = var1.getInt();
         var5.wx = var1.getInt();
         var5.wy = var1.getInt();
         var5.crc = var1.getLong();
         this.chunks.add(var5);
      }

      this.largeArea = false;
   }

   public void unpackLargeArea(ByteBuffer var1, UdpConnection var2) {
      int var3;
      for(var3 = 0; var3 < this.chunks.size(); ++var3) {
         this.releaseBuffer((ClientChunkRequest.Chunk)this.chunks.get(var3));
      }

      freeChunks.addAll(this.chunks);
      this.chunks.clear();
      this.minX = var1.getInt();
      this.minY = var1.getInt();
      this.maxX = var1.getInt();
      this.maxY = var1.getInt();

      for(var3 = this.minX; var3 < this.maxX; ++var3) {
         for(int var4 = this.minY; var4 < this.maxY; ++var4) {
            ClientChunkRequest.Chunk var5 = this.getChunk();
            var5.requestNumber = var1.getInt();
            var5.wx = var3;
            var5.wy = var4;
            var5.crc = 0L;
            this.releaseBuffer(var5);
            this.chunks.add(var5);
         }
      }

      this.largeArea = true;
   }

   public static final class Chunk {
      public int requestNumber;
      public int wx;
      public int wy;
      public long crc;
      public ByteBuffer bb;
   }
}
