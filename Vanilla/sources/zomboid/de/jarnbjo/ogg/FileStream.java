package de.jarnbjo.ogg;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class FileStream implements PhysicalOggStream {
   private boolean closed = false;
   private RandomAccessFile source;
   private long[] pageOffsets;
   private long numberOfSamples = -1L;
   private HashMap logicalStreams = new HashMap();

   public FileStream(RandomAccessFile var1) throws OggFormatException, IOException {
      this.source = var1;
      ArrayList var2 = new ArrayList();
      int var3 = 0;

      try {
         while(true) {
            var2.add(new Long(this.source.getFilePointer()));
            OggPage var4 = this.getNextPage(var3 > 0);
            if (var4 == null) {
               break;
            }

            LogicalOggStreamImpl var5 = (LogicalOggStreamImpl)this.getLogicalStream(var4.getStreamSerialNumber());
            if (var5 == null) {
               var5 = new LogicalOggStreamImpl(this, var4.getStreamSerialNumber());
               this.logicalStreams.put(new Integer(var4.getStreamSerialNumber()), var5);
            }

            if (var3 == 0) {
               var5.checkFormat(var4);
            }

            var5.addPageNumberMapping(var3);
            var5.addGranulePosition(var4.getAbsoluteGranulePosition());
            if (var3 > 0) {
               this.source.seek(this.source.getFilePointer() + (long)var4.getTotalLength());
            }

            ++var3;
         }
      } catch (EndOfOggStreamException var6) {
      } catch (IOException var7) {
         throw var7;
      }

      this.source.seek(0L);
      this.pageOffsets = new long[var2.size()];
      int var8 = 0;

      for(Iterator var9 = var2.iterator(); var9.hasNext(); this.pageOffsets[var8++] = (Long)var9.next()) {
      }

   }

   public Collection getLogicalStreams() {
      return this.logicalStreams.values();
   }

   public boolean isOpen() {
      return !this.closed;
   }

   public void close() throws IOException {
      this.closed = true;
      this.source.close();
   }

   private OggPage getNextPage() throws EndOfOggStreamException, IOException, OggFormatException {
      return this.getNextPage(false);
   }

   private OggPage getNextPage(boolean var1) throws EndOfOggStreamException, IOException, OggFormatException {
      return OggPage.create(this.source, var1);
   }

   public OggPage getOggPage(int var1) throws IOException {
      this.source.seek(this.pageOffsets[var1]);
      return OggPage.create(this.source);
   }

   private LogicalOggStream getLogicalStream(int var1) {
      return (LogicalOggStream)this.logicalStreams.get(new Integer(var1));
   }

   public void setTime(long var1) throws IOException {
      Iterator var3 = this.logicalStreams.values().iterator();

      while(var3.hasNext()) {
         LogicalOggStream var4 = (LogicalOggStream)var3.next();
         var4.setTime(var1);
      }

   }

   public boolean isSeekable() {
      return true;
   }
}
