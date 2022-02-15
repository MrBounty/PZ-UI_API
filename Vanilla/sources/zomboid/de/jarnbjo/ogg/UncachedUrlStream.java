package de.jarnbjo.ogg;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class UncachedUrlStream implements PhysicalOggStream {
   private boolean closed = false;
   private URLConnection source;
   private InputStream sourceStream;
   private Object drainLock = new Object();
   private LinkedList pageCache = new LinkedList();
   private long numberOfSamples = -1L;
   private HashMap logicalStreams = new HashMap();
   private UncachedUrlStream.LoaderThread loaderThread;
   private static final int PAGECACHE_SIZE = 10;

   public UncachedUrlStream(URL var1) throws OggFormatException, IOException {
      this.source = var1.openConnection();
      this.sourceStream = this.source.getInputStream();
      this.loaderThread = new UncachedUrlStream.LoaderThread(this.sourceStream, this.pageCache);
      (new Thread(this.loaderThread)).start();

      while(!this.loaderThread.isBosDone() || this.pageCache.size() < 10) {
         try {
            Thread.sleep(200L);
         } catch (InterruptedException var3) {
         }
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
      this.sourceStream.close();
   }

   public OggPage getOggPage(int var1) throws IOException {
      while(this.pageCache.size() == 0) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var4) {
         }
      }

      synchronized(this.drainLock) {
         return (OggPage)this.pageCache.removeFirst();
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

   public class LoaderThread implements Runnable {
      private InputStream source;
      private LinkedList pageCache;
      private RandomAccessFile drain;
      private byte[] memoryCache;
      private boolean bosDone = false;
      private int pageNumber;

      public LoaderThread(InputStream var2, LinkedList var3) {
         this.source = var2;
         this.pageCache = var3;
      }

      public void run() {
         try {
            boolean var1 = false;
            byte[] var2 = new byte[8192];

            while(!var1) {
               OggPage var3 = OggPage.create(this.source);
               synchronized(UncachedUrlStream.this.drainLock) {
                  this.pageCache.add(var3);
               }

               if (!var3.isBos()) {
                  this.bosDone = true;
               }

               if (var3.isEos()) {
                  var1 = true;
               }

               LogicalOggStreamImpl var4 = (LogicalOggStreamImpl)UncachedUrlStream.this.getLogicalStream(var3.getStreamSerialNumber());
               if (var4 == null) {
                  var4 = new LogicalOggStreamImpl(UncachedUrlStream.this, var3.getStreamSerialNumber());
                  UncachedUrlStream.this.logicalStreams.put(new Integer(var3.getStreamSerialNumber()), var4);
                  var4.checkFormat(var3);
               }

               ++this.pageNumber;

               while(this.pageCache.size() > 10) {
                  try {
                     Thread.sleep(200L);
                  } catch (InterruptedException var6) {
                  }
               }
            }
         } catch (EndOfOggStreamException var8) {
         } catch (IOException var9) {
            var9.printStackTrace();
         }

      }

      public boolean isBosDone() {
         return this.bosDone;
      }
   }
}
