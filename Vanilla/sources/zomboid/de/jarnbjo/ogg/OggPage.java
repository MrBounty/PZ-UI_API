package de.jarnbjo.ogg;

import de.jarnbjo.util.io.ByteArrayBitInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import zombie.debug.DebugLog;

public class OggPage {
   private int version;
   private boolean continued;
   private boolean bos;
   private boolean eos;
   private long absoluteGranulePosition;
   private int streamSerialNumber;
   private int pageSequenceNumber;
   private int pageCheckSum;
   private int[] segmentOffsets;
   private int[] segmentLengths;
   private int totalLength;
   private byte[] header;
   private byte[] segmentTable;
   private byte[] data;

   protected OggPage() {
   }

   private OggPage(int var1, boolean var2, boolean var3, boolean var4, long var5, int var7, int var8, int var9, int[] var10, int[] var11, int var12, byte[] var13, byte[] var14, byte[] var15) {
      this.version = var1;
      this.continued = var2;
      this.bos = var3;
      this.eos = var4;
      this.absoluteGranulePosition = var5;
      this.streamSerialNumber = var7;
      this.pageSequenceNumber = var8;
      this.pageCheckSum = var9;
      this.segmentOffsets = var10;
      this.segmentLengths = var11;
      this.totalLength = var12;
      this.header = var13;
      this.segmentTable = var14;
      this.data = var15;
   }

   public static OggPage create(RandomAccessFile var0) throws IOException, EndOfOggStreamException, OggFormatException {
      return create(var0, false);
   }

   public static OggPage create(RandomAccessFile var0, boolean var1) throws IOException, EndOfOggStreamException, OggFormatException {
      return create((Object)var0, var1);
   }

   public static OggPage create(InputStream var0) throws IOException, EndOfOggStreamException, OggFormatException {
      return create(var0, false);
   }

   public static OggPage create(InputStream var0, boolean var1) throws IOException, EndOfOggStreamException, OggFormatException {
      return create((Object)var0, var1);
   }

   public static OggPage create(byte[] var0) throws IOException, EndOfOggStreamException, OggFormatException {
      return create(var0, false);
   }

   public static OggPage create(byte[] var0, boolean var1) throws IOException, EndOfOggStreamException, OggFormatException {
      return create((Object)var0, var1);
   }

   private static OggPage create(Object var0, boolean var1) throws IOException, EndOfOggStreamException, OggFormatException {
      try {
         int var2 = 27;
         byte[] var3 = new byte[27];
         if (var0 instanceof RandomAccessFile) {
            RandomAccessFile var4 = (RandomAccessFile)var0;
            if (var4.getFilePointer() == var4.length()) {
               return null;
            }

            var4.readFully(var3);
         } else if (var0 instanceof InputStream) {
            readFully((InputStream)var0, var3);
         } else if (var0 instanceof byte[]) {
            System.arraycopy((byte[])var0, 0, var3, 0, 27);
         }

         ByteArrayBitInputStream var25 = new ByteArrayBitInputStream(var3);
         int var5 = var25.getInt(32);
         if (var5 != 1399285583) {
            String var6;
            for(var6 = Integer.toHexString(var5); var6.length() < 8; var6 = "0" + var6) {
            }

            String var10000 = var6.substring(6, 8);
            var6 = var10000 + var6.substring(4, 6) + var6.substring(2, 4) + var6.substring(0, 2);
            char var7 = (char)Integer.valueOf(var6.substring(0, 2), 16);
            char var8 = (char)Integer.valueOf(var6.substring(2, 4), 16);
            char var9 = (char)Integer.valueOf(var6.substring(4, 6), 16);
            char var10 = (char)Integer.valueOf(var6.substring(6, 8), 16);
            DebugLog.log("Ogg packet header is 0x" + var6 + " (" + var7 + var8 + var9 + var10 + "), should be 0x4f676753 (OggS)");
         }

         int var26 = var25.getInt(8);
         byte var27 = (byte)var25.getInt(8);
         boolean var28 = (var27 & 1) != 0;
         boolean var29 = (var27 & 2) != 0;
         boolean var30 = (var27 & 4) != 0;
         long var11 = var25.getLong(64);
         int var13 = var25.getInt(32);
         int var14 = var25.getInt(32);
         int var15 = var25.getInt(32);
         int var16 = var25.getInt(8);
         int[] var17 = new int[var16];
         int[] var18 = new int[var16];
         int var19 = 0;
         byte[] var20 = new byte[var16];
         byte[] var21 = new byte[1];

         for(int var22 = 0; var22 < var16; ++var22) {
            int var23 = 0;
            if (var0 instanceof RandomAccessFile) {
               var23 = ((RandomAccessFile)var0).readByte() & 255;
            } else if (var0 instanceof InputStream) {
               var23 = ((InputStream)var0).read();
            } else if (var0 instanceof byte[]) {
               byte var33 = ((byte[])var0)[var2++];
               var23 = var33 & 255;
            }

            var20[var22] = (byte)var23;
            var18[var22] = var23;
            var17[var22] = var19;
            var19 += var23;
         }

         byte[] var32 = null;
         if (!var1) {
            var32 = new byte[var19];
            if (var0 instanceof RandomAccessFile) {
               ((RandomAccessFile)var0).readFully(var32);
            } else if (var0 instanceof InputStream) {
               readFully((InputStream)var0, var32);
            } else if (var0 instanceof byte[]) {
               System.arraycopy(var0, var2, var32, 0, var19);
            }
         }

         return new OggPage(var26, var28, var29, var30, var11, var13, var14, var15, var17, var18, var19, var3, var20, var32);
      } catch (EOFException var24) {
         throw new EndOfOggStreamException();
      }
   }

   private static void readFully(InputStream var0, byte[] var1) throws IOException {
      int var3;
      for(int var2 = 0; var2 < var1.length; var2 += var3) {
         var3 = var0.read(var1, var2, var1.length - var2);
         if (var3 == -1) {
            throw new EndOfOggStreamException();
         }
      }

   }

   public long getAbsoluteGranulePosition() {
      return this.absoluteGranulePosition;
   }

   public int getStreamSerialNumber() {
      return this.streamSerialNumber;
   }

   public int getPageSequenceNumber() {
      return this.pageSequenceNumber;
   }

   public int getPageCheckSum() {
      return this.pageCheckSum;
   }

   public int getTotalLength() {
      return this.data != null ? 27 + this.segmentTable.length + this.data.length : this.totalLength;
   }

   public byte[] getData() {
      return this.data;
   }

   public byte[] getHeader() {
      return this.header;
   }

   public byte[] getSegmentTable() {
      return this.segmentTable;
   }

   public int[] getSegmentOffsets() {
      return this.segmentOffsets;
   }

   public int[] getSegmentLengths() {
      return this.segmentLengths;
   }

   public boolean isContinued() {
      return this.continued;
   }

   public boolean isFresh() {
      return !this.continued;
   }

   public boolean isBos() {
      return this.bos;
   }

   public boolean isEos() {
      return this.eos;
   }
}
