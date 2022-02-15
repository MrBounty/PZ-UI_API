package de.jarnbjo.ogg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class BasicStream implements PhysicalOggStream {
   private boolean closed = false;
   private InputStream sourceStream;
   private Object drainLock = new Object();
   private LinkedList pageCache = new LinkedList();
   private long numberOfSamples = -1L;
   private int position = 0;
   private HashMap logicalStreams = new HashMap();
   private OggPage firstPage;
   int pageNumber = 2;

   public BasicStream(InputStream var1) throws OggFormatException, IOException {
      this.firstPage = OggPage.create(var1);
      this.position += this.firstPage.getTotalLength();
      LogicalOggStreamImpl var2 = new LogicalOggStreamImpl(this, this.firstPage.getStreamSerialNumber());
      this.logicalStreams.put(new Integer(this.firstPage.getStreamSerialNumber()), var2);
      var2.checkFormat(this.firstPage);
   }

   public Collection getLogicalStreams() {
      return this.logicalStreams.values();
   }

   public boolean isOpen() {
      return !this.closed;
   }

   public void close() throws IOException {
      this.closed = true;
      this.sourceStream.close();
   }

   public int getContentLength() {
      return -1;
   }

   public int getPosition() {
      return this.position;
   }

   public OggPage getOggPage(int var1) throws IOException {
      OggPage var2;
      if (this.firstPage != null) {
         var2 = this.firstPage;
         this.firstPage = null;
         return var2;
      } else {
         var2 = OggPage.create(this.sourceStream);
         this.position += var2.getTotalLength();
         return var2;
      }
   }

   private LogicalOggStream getLogicalStream(int var1) {
      return (LogicalOggStream)this.logicalStreams.get(new Integer(var1));
   }

   public void setTime(long var1) throws IOException {
      throw new UnsupportedOperationException("Method not supported by this class");
   }

   public boolean isSeekable() {
      return false;
   }
}
