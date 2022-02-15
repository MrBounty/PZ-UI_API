package com.sixlegs.png;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class ImageDataInputStream extends InputStream {
   private final PngInputStream in;
   private final StateMachine machine;
   private final byte[] onebyte = new byte[1];
   private boolean done;

   public ImageDataInputStream(PngInputStream var1, StateMachine var2) {
      this.in = var1;
      this.machine = var2;
   }

   public int read() throws IOException {
      return this.read(this.onebyte, 0, 1) == -1 ? -1 : 255 & this.onebyte[0];
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.done) {
         return -1;
      } else {
         try {
            int var4 = 0;

            while(var4 != var3 && !this.done) {
               while(var4 != var3 && this.in.getRemaining() > 0) {
                  int var5 = Math.min(var3 - var4, this.in.getRemaining());
                  this.in.readFully(var1, var2 + var4, var5);
                  var4 += var5;
               }

               if (this.in.getRemaining() <= 0) {
                  this.in.endChunk(this.machine.getType());
                  this.machine.nextState(this.in.startChunk());
                  this.done = this.machine.getType() != 1229209940;
               }
            }

            return var4;
         } catch (EOFException var6) {
            this.done = true;
            return -1;
         }
      }
   }
}
