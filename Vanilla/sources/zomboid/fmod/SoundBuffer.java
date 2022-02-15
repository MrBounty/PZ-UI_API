package fmod;

import zombie.debug.DebugLog;

public class SoundBuffer {
   public int Buf_Size;
   public int Buf_Read;
   public int Buf_Write;
   private short[] intdata;
   private int delay;

   public SoundBuffer(int var1) {
      this.Buf_Size = var1;
      this.Buf_Read = 0;
      this.Buf_Write = 0;
      this.delay = 1;
      this.intdata = new short[var1];
   }

   public void get(long var1, short[] var3) {
      int var6 = this.Buf_Write - this.Buf_Read;
      if (var6 < 0) {
         var6 += this.Buf_Size;
      }

      int var5;
      if ((long)var6 < var1) {
         for(var5 = 0; (long)var5 < var1 - 1L; ++var5) {
            var3[var5] = 0;
         }

      } else {
         int var4;
         if ((long)var6 > var1 * (long)this.delay * 2L) {
            if ((long)this.delay * var1 * 3L < (long)this.Buf_Size) {
               ++this.delay;
            }

            DebugLog.log("[SoundBuffer] correct: delay: " + this.delay);
            this.Buf_Read = (int)((long)this.Buf_Write - var1 * (long)this.delay);
            if (this.Buf_Read < 0) {
               this.Buf_Read += this.Buf_Size;
            }

            var5 = 0;

            for(var4 = this.Buf_Read; (long)var5 < var1 * 2L; var4 = (var4 + 1) % this.Buf_Size) {
               this.intdata[var4] = 0;
               ++var5;
            }

         } else {
            var5 = 0;

            for(var4 = this.Buf_Read; (long)var5 < var1 - 1L && var4 != this.Buf_Write; var4 = (var4 + 1) % this.Buf_Size) {
               var3[var5] = this.intdata[var4];
               ++var5;
            }

            this.Buf_Read = var4;
         }
      }
   }

   public void push(long var1, short[] var3) {
      boolean var6 = false;
      int var5 = 0;

      int var4;
      for(var4 = this.Buf_Write; (long)var5 < var1 - 1L; var4 = (var4 + 1) % this.Buf_Size) {
         this.intdata[var4] = var3[var5];
         if (var3[var5] != 0) {
            var6 = true;
         }

         ++var5;
      }

      if (var6) {
         this.Buf_Write = var4;
      }

   }

   public void push(long var1, byte[] var3) {
      boolean var6 = false;
      int var5 = 0;

      int var4;
      for(var4 = this.Buf_Write; (long)var5 < var1 - 1L; var4 = (var4 + 1) % this.Buf_Size) {
         this.intdata[var4] = (short)(var3[var5 + 1] * 256 + var3[var5]);
         if (var3[var5] != 0) {
            var6 = true;
         }

         var5 += 2;
      }

      if (var6) {
         this.Buf_Write = var4;
      }

   }

   public short[] buf() {
      return this.intdata;
   }
}
