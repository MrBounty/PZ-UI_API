package de.jarnbjo.vorbis;

import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.util.io.ByteArrayBitInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class VorbisStream {
   private LogicalOggStream oggStream;
   private IdentificationHeader identificationHeader;
   private CommentHeader commentHeader;
   private SetupHeader setupHeader;
   private AudioPacket lastAudioPacket;
   private AudioPacket nextAudioPacket;
   private LinkedList audioPackets = new LinkedList();
   private byte[] currentPcm;
   private int currentPcmIndex;
   private int currentPcmLimit;
   private static final int IDENTIFICATION_HEADER = 1;
   private static final int COMMENT_HEADER = 3;
   private static final int SETUP_HEADER = 5;
   private int bitIndex = 0;
   private byte lastByte = 0;
   private boolean initialized = false;
   private Object streamLock = new Object();
   private int pageCounter = 0;
   private int currentBitRate = 0;
   private long currentGranulePosition;
   public static final int BIG_ENDIAN = 0;
   public static final int LITTLE_ENDIAN = 1;

   public VorbisStream() {
   }

   public VorbisStream(LogicalOggStream var1) throws VorbisFormatException, IOException {
      this.oggStream = var1;

      for(int var2 = 0; var2 < 3; ++var2) {
         ByteArrayBitInputStream var3 = new ByteArrayBitInputStream(var1.getNextOggPacket());
         int var4 = var3.getInt(8);
         switch(var4) {
         case 1:
            this.identificationHeader = new IdentificationHeader(var3);
         case 2:
         case 4:
         default:
            break;
         case 3:
            this.commentHeader = new CommentHeader(var3);
            break;
         case 5:
            this.setupHeader = new SetupHeader(this, var3);
         }
      }

      if (this.identificationHeader == null) {
         throw new VorbisFormatException("The file has no identification header.");
      } else if (this.commentHeader == null) {
         throw new VorbisFormatException("The file has no commentHeader.");
      } else if (this.setupHeader == null) {
         throw new VorbisFormatException("The file has no setup header.");
      } else {
         this.currentPcm = new byte[this.identificationHeader.getChannels() * this.identificationHeader.getBlockSize1() * 2];
      }
   }

   public IdentificationHeader getIdentificationHeader() {
      return this.identificationHeader;
   }

   public CommentHeader getCommentHeader() {
      return this.commentHeader;
   }

   protected SetupHeader getSetupHeader() {
      return this.setupHeader;
   }

   public boolean isOpen() {
      return this.oggStream.isOpen();
   }

   public void close() throws IOException {
      this.oggStream.close();
   }

   public int readPcm(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this.streamLock) {
         int var5 = this.identificationHeader.getChannels();
         if (this.lastAudioPacket == null) {
            this.lastAudioPacket = this.getNextAudioPacket();
         }

         if (this.currentPcm == null || this.currentPcmIndex >= this.currentPcmLimit) {
            AudioPacket var6 = this.getNextAudioPacket();

            try {
               var6.getPcm(this.lastAudioPacket, this.currentPcm);
               this.currentPcmLimit = var6.getNumberOfSamples() * this.identificationHeader.getChannels() * 2;
            } catch (ArrayIndexOutOfBoundsException var10) {
               return 0;
            }

            this.currentPcmIndex = 0;
            this.lastAudioPacket = var6;
         }

         int var12 = 0;
         boolean var7 = false;
         int var8 = 0;

         int var13;
         for(var13 = this.currentPcmIndex; var13 < this.currentPcmLimit && var8 < var3; ++var13) {
            var1[var2 + var8++] = this.currentPcm[var13];
            ++var12;
         }

         this.currentPcmIndex = var13;
         return var12;
      }
   }

   private AudioPacket getNextAudioPacket() throws VorbisFormatException, IOException {
      ++this.pageCounter;
      byte[] var1 = this.oggStream.getNextOggPacket();
      AudioPacket var2 = null;

      while(var2 == null) {
         try {
            var2 = new AudioPacket(this, new ByteArrayBitInputStream(var1));
         } catch (ArrayIndexOutOfBoundsException var4) {
         }
      }

      this.currentGranulePosition += (long)var2.getNumberOfSamples();
      this.currentBitRate = var1.length * 8 * this.identificationHeader.getSampleRate() / var2.getNumberOfSamples();
      return var2;
   }

   public long getCurrentGranulePosition() {
      return this.currentGranulePosition;
   }

   public int getCurrentBitRate() {
      return this.currentBitRate;
   }

   public byte[] processPacket(byte[] var1) throws VorbisFormatException, IOException {
      if (var1.length == 0) {
         throw new VorbisFormatException("Cannot decode a vorbis packet with length = 0");
      } else if ((var1[0] & 1) == 1) {
         ByteArrayBitInputStream var6 = new ByteArrayBitInputStream(var1);
         switch(var6.getInt(8)) {
         case 1:
            this.identificationHeader = new IdentificationHeader(var6);
         case 2:
         case 4:
         default:
            break;
         case 3:
            this.commentHeader = new CommentHeader(var6);
            break;
         case 5:
            this.setupHeader = new SetupHeader(this, var6);
         }

         return null;
      } else if (this.identificationHeader != null && this.commentHeader != null && this.setupHeader != null) {
         AudioPacket var2 = new AudioPacket(this, new ByteArrayBitInputStream(var1));
         this.currentGranulePosition += (long)var2.getNumberOfSamples();
         if (this.lastAudioPacket == null) {
            this.lastAudioPacket = var2;
            return null;
         } else {
            byte[] var3 = new byte[this.identificationHeader.getChannels() * var2.getNumberOfSamples() * 2];

            try {
               var2.getPcm(this.lastAudioPacket, var3);
            } catch (IndexOutOfBoundsException var5) {
               Arrays.fill(var3, (byte)0);
            }

            this.lastAudioPacket = var2;
            return var3;
         }
      } else {
         throw new VorbisFormatException("Cannot decode audio packet before all three header packets have been decoded.");
      }
   }
}
