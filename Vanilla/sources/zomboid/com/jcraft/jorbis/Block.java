package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;

public class Block {
   int eofflag;
   int floor_bits;
   int glue_bits;
   long granulepos;
   int lW;
   int mode;
   int nW;
   Buffer opb = new Buffer();
   float[][] pcm = new float[0][];
   int pcmend;
   int res_bits;
   long sequence;
   int time_bits;
   DspState vd;
   int W;

   public static String asdsadsa(String var0, byte[] var1, int var2) {
      var0 = var0 + Integer.toString((var1[var2] & 255) + 256, 16).substring(1);
      return var0;
   }

   public Block(DspState var1) {
      this.vd = var1;
      if (var1.analysisp != 0) {
         this.opb.writeinit();
      }

   }

   public int clear() {
      if (this.vd != null && this.vd.analysisp != 0) {
         this.opb.writeclear();
      }

      return 0;
   }

   public void init(DspState var1) {
      this.vd = var1;
   }

   public int synthesis(Packet var1) {
      Info var2 = this.vd.vi;
      this.opb.readinit(var1.packet_base, var1.packet, var1.bytes);
      if (this.opb.read(1) != 0) {
         return -1;
      } else {
         int var3 = this.opb.read(this.vd.modebits);
         if (var3 == -1) {
            return -1;
         } else {
            this.mode = var3;
            this.W = var2.mode_param[this.mode].blockflag;
            if (this.W != 0) {
               this.lW = this.opb.read(1);
               this.nW = this.opb.read(1);
               if (this.nW == -1) {
                  return -1;
               }
            } else {
               this.lW = 0;
               this.nW = 0;
            }

            this.granulepos = var1.granulepos;
            this.sequence = var1.packetno - 3L;
            this.eofflag = var1.e_o_s;
            this.pcmend = var2.blocksizes[this.W];
            if (this.pcm.length < var2.channels) {
               this.pcm = new float[var2.channels][];
            }

            int var4;
            for(var4 = 0; var4 < var2.channels; ++var4) {
               if (this.pcm[var4] != null && this.pcm[var4].length >= this.pcmend) {
                  for(int var5 = 0; var5 < this.pcmend; ++var5) {
                     this.pcm[var4][var5] = 0.0F;
                  }
               } else {
                  this.pcm[var4] = new float[this.pcmend];
               }
            }

            var4 = var2.map_type[var2.mode_param[this.mode].mapping];
            return FuncMapping.mapping_P[var4].inverse(this, this.vd.mode[this.mode]);
         }
      }
   }
}
