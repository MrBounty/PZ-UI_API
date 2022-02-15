package de.jarnbjo.ogg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LogicalOggStreamImpl implements LogicalOggStream {
   private PhysicalOggStream source;
   private int serialNumber;
   private ArrayList pageNumberMapping = new ArrayList();
   private ArrayList granulePositions = new ArrayList();
   private int pageIndex = 0;
   private OggPage currentPage;
   private int currentSegmentIndex;
   private boolean open = true;
   private String format = "application/octet-stream";

   public LogicalOggStreamImpl(PhysicalOggStream var1, int var2) {
      this.source = var1;
      this.serialNumber = var2;
   }

   public void addPageNumberMapping(int var1) {
      this.pageNumberMapping.add(new Integer(var1));
   }

   public void addGranulePosition(long var1) {
      this.granulePositions.add(new Long(var1));
   }

   public synchronized void reset() throws OggFormatException, IOException {
      this.currentPage = null;
      this.currentSegmentIndex = 0;
      this.pageIndex = 0;
   }

   public synchronized OggPage getNextOggPage() throws EndOfOggStreamException, OggFormatException, IOException {
      if (this.source.isSeekable()) {
         this.currentPage = this.source.getOggPage((Integer)this.pageNumberMapping.get(this.pageIndex++));
      } else {
         this.currentPage = this.source.getOggPage(-1);
      }

      return this.currentPage;
   }

   public synchronized byte[] getNextOggPacket() throws EndOfOggStreamException, OggFormatException, IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      boolean var2 = false;
      if (this.currentPage == null) {
         this.currentPage = this.getNextOggPage();
      }

      int var5;
      do {
         if (this.currentSegmentIndex >= this.currentPage.getSegmentOffsets().length) {
            this.currentSegmentIndex = 0;
            if (this.currentPage.isEos()) {
               throw new EndOfOggStreamException();
            }

            if (this.source.isSeekable() && this.pageNumberMapping.size() <= this.pageIndex) {
               while(this.pageNumberMapping.size() <= this.pageIndex + 10) {
                  try {
                     Thread.sleep(1000L);
                  } catch (InterruptedException var4) {
                  }
               }
            }

            this.currentPage = this.getNextOggPage();
            if (var1.size() == 0 && this.currentPage.isContinued()) {
               boolean var3 = false;

               while(!var3) {
                  if (this.currentPage.getSegmentLengths()[this.currentSegmentIndex++] != 255) {
                     var3 = true;
                  }

                  if (this.currentSegmentIndex > this.currentPage.getSegmentTable().length) {
                     this.currentPage = this.source.getOggPage((Integer)this.pageNumberMapping.get(this.pageIndex++));
                  }
               }
            }
         }

         var5 = this.currentPage.getSegmentLengths()[this.currentSegmentIndex];
         var1.write(this.currentPage.getData(), this.currentPage.getSegmentOffsets()[this.currentSegmentIndex], var5);
         ++this.currentSegmentIndex;
      } while(var5 == 255);

      return var1.toByteArray();
   }

   public boolean isOpen() {
      return this.open;
   }

   public void close() throws IOException {
      this.open = false;
   }

   public long getMaximumGranulePosition() {
      Long var1 = (Long)this.granulePositions.get(this.granulePositions.size() - 1);
      return var1;
   }

   public synchronized long getTime() {
      return this.currentPage != null ? this.currentPage.getAbsoluteGranulePosition() : -1L;
   }

   public synchronized void setTime(long var1) throws IOException {
      boolean var3 = false;

      int var5;
      for(var5 = 0; var5 < this.granulePositions.size(); ++var5) {
         Long var4 = (Long)this.granulePositions.get(var5);
         if (var4 > var1) {
            break;
         }
      }

      this.pageIndex = var5;
      this.currentPage = this.source.getOggPage((Integer)this.pageNumberMapping.get(this.pageIndex++));
      this.currentSegmentIndex = 0;
      boolean var6 = false;

      int var7;
      do {
         if (this.currentSegmentIndex >= this.currentPage.getSegmentOffsets().length) {
            this.currentSegmentIndex = 0;
            if (this.pageIndex >= this.pageNumberMapping.size()) {
               throw new EndOfOggStreamException();
            }

            this.currentPage = this.source.getOggPage((Integer)this.pageNumberMapping.get(this.pageIndex++));
         }

         var7 = this.currentPage.getSegmentLengths()[this.currentSegmentIndex];
         ++this.currentSegmentIndex;
      } while(var7 == 255);

   }

   public void checkFormat(OggPage var1) {
      byte[] var2 = var1.getData();
      if (var2.length >= 7 && var2[1] == 118 && var2[2] == 111 && var2[3] == 114 && var2[4] == 98 && var2[5] == 105 && var2[6] == 115) {
         this.format = "audio/x-vorbis";
      } else if (var2.length >= 7 && var2[1] == 116 && var2[2] == 104 && var2[3] == 101 && var2[4] == 111 && var2[5] == 114 && var2[6] == 97) {
         this.format = "video/x-theora";
      } else if (var2.length == 4 && var2[0] == 102 && var2[1] == 76 && var2[2] == 97 && var2[3] == 67) {
         this.format = "audio/x-flac";
      }

   }

   public String getFormat() {
      return this.format;
   }
}
