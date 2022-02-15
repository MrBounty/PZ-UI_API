package zombie.iso.areas.isoregion.jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import zombie.core.Core;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.iso.IsoChunk;
import zombie.iso.areas.isoregion.ChunkUpdate;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.data.DataChunk;
import zombie.iso.areas.isoregion.data.DataRoot;
import zombie.network.GameClient;

public class JobChunkUpdate extends RegionJob {
   private ByteBuffer buffer = ByteBuffer.allocate(65536);
   private int chunkCount = 0;
   private int bufferMaxBytes = 0;
   private long netTimeStamp = -1L;
   private UdpConnection targetConn;

   protected JobChunkUpdate() {
      super(RegionJobType.ChunkUpdate);
   }

   protected void reset() {
      this.chunkCount = 0;
      this.bufferMaxBytes = 0;
      this.netTimeStamp = -1L;
      this.targetConn = null;
      this.buffer.clear();
   }

   public UdpConnection getTargetConn() {
      return this.targetConn;
   }

   public void setTargetConn(UdpConnection var1) {
      this.targetConn = var1;
   }

   public int getChunkCount() {
      return this.chunkCount;
   }

   public ByteBuffer getBuffer() {
      return this.buffer;
   }

   public long getNetTimeStamp() {
      return this.netTimeStamp;
   }

   public void setNetTimeStamp(long var1) {
      this.netTimeStamp = var1;
   }

   public boolean readChunksPacket(DataRoot var1, List var2) {
      this.buffer.position(0);
      int var3 = this.buffer.getInt();
      int var4 = this.buffer.getInt();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = this.buffer.getInt();
         int var7 = this.buffer.getInt();
         int var8 = this.buffer.getInt();
         int var9 = this.buffer.getInt();
         var1.select.reset(var8 * 10, var9 * 10, 0, true, false);
         int var10;
         if (GameClient.bClient) {
            if (this.netTimeStamp != -1L && this.netTimeStamp < var1.select.chunk.getLastUpdateStamp()) {
               var10 = this.buffer.position();
               int var11 = this.buffer.getInt();
               this.buffer.position(var10 + var11);
               continue;
            }

            var1.select.chunk.setLastUpdateStamp(this.netTimeStamp);
         } else {
            var10 = IsoRegions.hash(var8, var9);
            if (!var2.contains(var10)) {
               var2.add(var10);
            }
         }

         var1.select.chunk.load(this.buffer, var7, true);
         var1.select.chunk.setDirtyAllActive();
      }

      return true;
   }

   public boolean saveChunksToDisk() {
      if (Core.getInstance().isNoSave()) {
         return true;
      } else if (this.chunkCount <= 0) {
         return false;
      } else {
         this.buffer.position(0);
         int var1 = this.buffer.getInt();
         int var2 = this.buffer.getInt();

         for(int var3 = 0; var3 < var2; ++var3) {
            this.buffer.mark();
            int var4 = this.buffer.getInt();
            int var5 = this.buffer.getInt();
            int var6 = this.buffer.getInt();
            int var7 = this.buffer.getInt();
            this.buffer.reset();
            File var8 = IsoRegions.getChunkFile(var6, var7);

            try {
               FileOutputStream var9 = new FileOutputStream(var8);
               var9.getChannel().truncate(0L);
               var9.write(this.buffer.array(), this.buffer.position(), var4);
               var9.flush();
               var9.close();
            } catch (Exception var10) {
               DebugLog.log(var10.getMessage());
               var10.printStackTrace();
            }

            this.buffer.position(this.buffer.position() + var4);
         }

         return true;
      }
   }

   public boolean saveChunksToNetBuffer(ByteBuffer var1) {
      IsoRegions.log("Server max bytes buffer = " + this.bufferMaxBytes + ", chunks = " + this.chunkCount);
      var1.put(this.buffer.array(), 0, this.bufferMaxBytes);
      return true;
   }

   public boolean readChunksFromNetBuffer(ByteBuffer var1, long var2) {
      this.netTimeStamp = var2;
      var1.mark();
      this.bufferMaxBytes = var1.getInt();
      this.chunkCount = var1.getInt();
      var1.reset();
      IsoRegions.log("Client max bytes buffer = " + this.bufferMaxBytes + ", chunks = " + this.chunkCount);
      this.buffer.position(0);
      this.buffer.put(var1.array(), var1.position(), this.bufferMaxBytes);
      return true;
   }

   public boolean canAddChunk() {
      return this.buffer.position() + 1024 < this.buffer.capacity();
   }

   private int startBufferBlock() {
      if (this.chunkCount == 0) {
         this.buffer.position(0);
         this.buffer.putInt(0);
         this.buffer.putInt(0);
      }

      int var1 = this.buffer.position();
      this.buffer.putInt(0);
      return var1;
   }

   private void endBufferBlock(int var1) {
      this.bufferMaxBytes = this.buffer.position();
      this.buffer.position(var1);
      this.buffer.putInt(this.bufferMaxBytes - var1);
      ++this.chunkCount;
      this.buffer.position(0);
      this.buffer.putInt(this.bufferMaxBytes);
      this.buffer.putInt(this.chunkCount);
      this.buffer.position(this.bufferMaxBytes);
   }

   public boolean addChunkFromDataChunk(DataChunk var1) {
      if (this.buffer.position() + 1024 >= this.buffer.capacity()) {
         return false;
      } else {
         int var2 = this.startBufferBlock();
         this.buffer.putInt(186);
         this.buffer.putInt(var1.getChunkX());
         this.buffer.putInt(var1.getChunkY());
         var1.save(this.buffer);
         this.endBufferBlock(var2);
         return true;
      }
   }

   public boolean addChunkFromIsoChunk(IsoChunk var1) {
      if (this.buffer.position() + 1024 >= this.buffer.capacity()) {
         return false;
      } else {
         int var2 = this.startBufferBlock();
         this.buffer.putInt(186);
         this.buffer.putInt(var1.wx);
         this.buffer.putInt(var1.wy);
         ChunkUpdate.writeIsoChunkIntoBuffer(var1, this.buffer);
         this.endBufferBlock(var2);
         return true;
      }
   }

   public boolean addChunkFromFile(ByteBuffer var1) {
      if (this.buffer.position() + var1.limit() >= this.buffer.capacity()) {
         return false;
      } else {
         var1.getInt();
         int var2 = this.startBufferBlock();
         this.buffer.putInt(var1.getInt());
         this.buffer.putInt(var1.getInt());
         this.buffer.putInt(var1.getInt());
         var1.mark();
         int var3 = var1.getInt();
         var1.reset();
         this.buffer.put(var1.array(), var1.position(), var3);
         this.endBufferBlock(var2);
         return true;
      }
   }
}
