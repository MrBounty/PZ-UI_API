package zombie.core.textures;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public final class PNGSize {
   private static final byte[] SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private static final int IHDR = 1229472850;
   public int width;
   public int height;
   private int bitdepth;
   private int colorType;
   private int bytesPerPixel;
   private InputStream input;
   private final CRC32 crc = new CRC32();
   private final byte[] buffer = new byte[4096];
   private int chunkLength;
   private int chunkType;
   private int chunkRemaining;

   public void readSize(String var1) {
      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               this.readSize((InputStream)var3);
            } catch (Throwable var8) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var3.close();
         } catch (Throwable var9) {
            try {
               var2.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         var2.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public void readSize(InputStream var1) throws IOException {
      this.input = var1;
      this.readFully(this.buffer, 0, SIGNATURE.length);
      if (!this.checkSignature(this.buffer)) {
         throw new IOException("Not a valid PNG file");
      } else {
         this.openChunk(1229472850);
         this.readIHDR();
         this.closeChunk();
      }
   }

   private void readIHDR() throws IOException {
      this.checkChunkLength(13);
      this.readChunk(this.buffer, 0, 13);
      this.width = this.readInt(this.buffer, 0);
      this.height = this.readInt(this.buffer, 4);
      this.bitdepth = this.buffer[8] & 255;
      this.colorType = this.buffer[9] & 255;
   }

   private void openChunk() throws IOException {
      this.readFully(this.buffer, 0, 8);
      this.chunkLength = this.readInt(this.buffer, 0);
      this.chunkType = this.readInt(this.buffer, 4);
      this.chunkRemaining = this.chunkLength;
      this.crc.reset();
      this.crc.update(this.buffer, 4, 4);
   }

   private void openChunk(int var1) throws IOException {
      this.openChunk();
      if (this.chunkType != var1) {
         throw new IOException("Expected chunk: " + Integer.toHexString(var1));
      }
   }

   private void closeChunk() throws IOException {
      if (this.chunkRemaining > 0) {
         this.skip((long)(this.chunkRemaining + 4));
      } else {
         this.readFully(this.buffer, 0, 4);
         int var1 = this.readInt(this.buffer, 0);
         int var2 = (int)this.crc.getValue();
         if (var2 != var1) {
            throw new IOException("Invalid CRC");
         }
      }

      this.chunkRemaining = 0;
      this.chunkLength = 0;
      this.chunkType = 0;
   }

   private void checkChunkLength(int var1) throws IOException {
      if (this.chunkLength != var1) {
         throw new IOException("Chunk has wrong size");
      }
   }

   private int readChunk(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > this.chunkRemaining) {
         var3 = this.chunkRemaining;
      }

      this.readFully(var1, var2, var3);
      this.crc.update(var1, var2, var3);
      this.chunkRemaining -= var3;
      return var3;
   }

   private void readFully(byte[] var1, int var2, int var3) throws IOException {
      do {
         int var4 = this.input.read(var1, var2, var3);
         if (var4 < 0) {
            throw new EOFException();
         }

         var2 += var4;
         var3 -= var4;
      } while(var3 > 0);

   }

   private int readInt(byte[] var1, int var2) {
      return var1[var2] << 24 | (var1[var2 + 1] & 255) << 16 | (var1[var2 + 2] & 255) << 8 | var1[var2 + 3] & 255;
   }

   private void skip(long var1) throws IOException {
      while(var1 > 0L) {
         long var3 = this.input.skip(var1);
         if (var3 < 0L) {
            throw new EOFException();
         }

         var1 -= var3;
      }

   }

   private boolean checkSignature(byte[] var1) {
      for(int var2 = 0; var2 < SIGNATURE.length; ++var2) {
         if (var1[var2] != SIGNATURE[var2]) {
            return false;
         }
      }

      return true;
   }
}
