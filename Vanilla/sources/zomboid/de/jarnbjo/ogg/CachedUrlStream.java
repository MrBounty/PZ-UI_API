package de.jarnbjo.ogg;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CachedUrlStream implements PhysicalOggStream {
   private boolean closed;
   private URLConnection source;
   private InputStream sourceStream;
   private Object drainLock;
   private RandomAccessFile drain;
   private byte[] memoryCache;
   private ArrayList pageOffsets;
   private ArrayList pageLengths;
   private long numberOfSamples;
   private long cacheLength;
   private HashMap logicalStreams;
   private CachedUrlStream.LoaderThread loaderThread;

   public CachedUrlStream(URL var1) throws OggFormatException, IOException {
      this(var1, (RandomAccessFile)null);
   }

   public CachedUrlStream(URL var1, RandomAccessFile var2) throws OggFormatException, IOException {
      this.closed = false;
      this.drainLock = new Object();
      this.pageOffsets = new ArrayList();
      this.pageLengths = new ArrayList();
      this.numberOfSamples = -1L;
      this.logicalStreams = new HashMap();
      this.source = var1.openConnection();
      if (var2 == null) {
         int var3 = this.source.getContentLength();
         if (var3 == -1) {
            throw new IOException("The URLConncetion's content length must be set when operating with a in-memory cache.");
         }

         this.memoryCache = new byte[var3];
      }

      this.drain = var2;
      this.sourceStream = this.source.getInputStream();
      this.loaderThread = new CachedUrlStream.LoaderThread(this.sourceStream, var2, this.memoryCache);
      (new Thread(this.loaderThread)).start();

      while(!this.loaderThread.isBosDone() || this.pageOffsets.size() < 20) {
         try {
            Thread.sleep(200L);
         } catch (InterruptedException var4) {
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

   public long getCacheLength() {
      return this.cacheLength;
   }

   public OggPage getOggPage(int var1) throws IOException {
      synchronized(this.drainLock) {
         Long var3 = (Long)this.pageOffsets.get(var1);
         Long var4 = (Long)this.pageLengths.get(var1);
         if (var3 != null) {
            if (this.drain != null) {
               this.drain.seek(var3);
               return OggPage.create(this.drain);
            } else {
               byte[] var5 = new byte[var4.intValue()];
               System.arraycopy(this.memoryCache, var3.intValue(), var5, 0, var4.intValue());
               return OggPage.create(var5);
            }
         } else {
            return null;
         }
      }
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

   public class LoaderThread implements Runnable {
      private InputStream source;
      private RandomAccessFile drain;
      private byte[] memoryCache;
      private boolean bosDone = false;
      private int pageNumber;

      public LoaderThread(InputStream var2, RandomAccessFile var3, byte[] var4) {
         this.source = var2;
         this.drain = var3;
         this.memoryCache = var4;
      }

      public void run() {
         try {
            boolean var1 = false;

            OggPage var3;
            for(byte[] var2 = new byte[8192]; !var1; CachedUrlStream.this.cacheLength = var3.getAbsoluteGranulePosition()) {
               var3 = OggPage.create(this.source);
               synchronized(CachedUrlStream.this.drainLock) {
                  int var5 = CachedUrlStream.this.pageOffsets.size();
                  long var6 = var5 > 0 ? (Long)CachedUrlStream.this.pageOffsets.get(var5 - 1) + (Long)CachedUrlStream.this.pageLengths.get(var5 - 1) : 0L;
                  byte[] var8 = var3.getHeader();
                  byte[] var9 = var3.getSegmentTable();
                  byte[] var10 = var3.getData();
                  if (this.drain != null) {
                     this.drain.seek(var6);
                     this.drain.write(var8);
                     this.drain.write(var9);
                     this.drain.write(var10);
                  } else {
                     System.arraycopy(var8, 0, this.memoryCache, (int)var6, var8.length);
                     System.arraycopy(var9, 0, this.memoryCache, (int)var6 + var8.length, var9.length);
                     System.arraycopy(var10, 0, this.memoryCache, (int)var6 + var8.length + var9.length, var10.length);
                  }

                  CachedUrlStream.this.pageOffsets.add(new Long(var6));
                  CachedUrlStream.this.pageLengths.add(new Long((long)(var8.length + var9.length + var10.length)));
               }

               if (!var3.isBos()) {
                  this.bosDone = true;
               }

               if (var3.isEos()) {
                  var1 = true;
               }

               LogicalOggStreamImpl var4 = (LogicalOggStreamImpl)CachedUrlStream.this.getLogicalStream(var3.getStreamSerialNumber());
               if (var4 == null) {
                  var4 = new LogicalOggStreamImpl(CachedUrlStream.this, var3.getStreamSerialNumber());
                  CachedUrlStream.this.logicalStreams.put(new Integer(var3.getStreamSerialNumber()), var4);
                  var4.checkFormat(var3);
               }

               var4.addPageNumberMapping(this.pageNumber);
               var4.addGranulePosition(var3.getAbsoluteGranulePosition());
               ++this.pageNumber;
            }
         } catch (EndOfOggStreamException var13) {
         } catch (IOException var14) {
            var14.printStackTrace();
         }

      }

      public boolean isBosDone() {
         return this.bosDone;
      }
   }
}
