package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class VorbisFile {
   static final int CHUNKSIZE = 8500;
   static final int SEEK_SET = 0;
   static final int SEEK_CUR = 1;
   static final int SEEK_END = 2;
   static final int OV_FALSE = -1;
   static final int OV_EOF = -2;
   static final int OV_HOLE = -3;
   static final int OV_EREAD = -128;
   static final int OV_EFAULT = -129;
   static final int OV_EIMPL = -130;
   static final int OV_EINVAL = -131;
   static final int OV_ENOTVORBIS = -132;
   static final int OV_EBADHEADER = -133;
   static final int OV_EVERSION = -134;
   static final int OV_ENOTAUDIO = -135;
   static final int OV_EBADPACKET = -136;
   static final int OV_EBADLINK = -137;
   static final int OV_ENOSEEK = -138;
   float bittrack;
   int current_link;
   int current_serialno;
   long[] dataoffsets;
   InputStream datasource;
   boolean decode_ready = false;
   long end;
   int links;
   long offset;
   long[] offsets;
   StreamState os = new StreamState();
   SyncState oy = new SyncState();
   long pcm_offset;
   long[] pcmlengths;
   float samptrack;
   boolean seekable = false;
   int[] serialnos;
   Comment[] vc;
   DspState vd = new DspState();
   Block vb;
   Info[] vi;

   public VorbisFile(String var1) throws JOrbisException {
      this.vb = new Block(this.vd);
      VorbisFile.SeekableInputStream var2 = null;

      try {
         var2 = new VorbisFile.SeekableInputStream(var1);
         int var3 = this.open(var2, (byte[])null, 0);
         if (var3 == -1) {
            throw new JOrbisException("VorbisFile: open return -1");
         }
      } catch (Exception var11) {
         throw new JOrbisException("VorbisFile: " + var11.toString());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var10) {
               var10.printStackTrace();
            }
         }

      }

   }

   public VorbisFile(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      this.vb = new Block(this.vd);
      int var4 = this.open(var1, var2, var3);
      if (var4 == -1) {
      }

   }

   static int fseek(InputStream var0, long var1, int var3) {
      if (var0 instanceof VorbisFile.SeekableInputStream) {
         VorbisFile.SeekableInputStream var4 = (VorbisFile.SeekableInputStream)var0;

         try {
            if (var3 == 0) {
               var4.seek(var1);
            } else if (var3 == 2) {
               var4.seek(var4.getLength() - var1);
            }
         } catch (Exception var6) {
         }

         return 0;
      } else {
         try {
            if (var3 == 0) {
               var0.reset();
            }

            var0.skip(var1);
            return 0;
         } catch (Exception var7) {
            return -1;
         }
      }
   }

   static long ftell(InputStream var0) {
      try {
         if (var0 instanceof VorbisFile.SeekableInputStream) {
            VorbisFile.SeekableInputStream var1 = (VorbisFile.SeekableInputStream)var0;
            return var1.tell();
         }
      } catch (Exception var2) {
      }

      return 0L;
   }

   public int bitrate(int var1) {
      if (var1 >= this.links) {
         return -1;
      } else if (!this.seekable && var1 != 0) {
         return this.bitrate(0);
      } else if (var1 >= 0) {
         if (this.seekable) {
            return (int)Math.rint((double)((float)((this.offsets[var1 + 1] - this.dataoffsets[var1]) * 8L) / this.time_total(var1)));
         } else if (this.vi[var1].bitrate_nominal > 0) {
            return this.vi[var1].bitrate_nominal;
         } else if (this.vi[var1].bitrate_upper > 0) {
            return this.vi[var1].bitrate_lower > 0 ? (this.vi[var1].bitrate_upper + this.vi[var1].bitrate_lower) / 2 : this.vi[var1].bitrate_upper;
         } else {
            return -1;
         }
      } else {
         long var2 = 0L;

         for(int var4 = 0; var4 < this.links; ++var4) {
            var2 += (this.offsets[var4 + 1] - this.dataoffsets[var4]) * 8L;
         }

         return (int)Math.rint((double)((float)var2 / this.time_total(-1)));
      }
   }

   public int bitrate_instant() {
      int var1 = this.seekable ? this.current_link : 0;
      if (this.samptrack == 0.0F) {
         return -1;
      } else {
         int var2 = (int)((double)(this.bittrack / this.samptrack * (float)this.vi[var1].rate) + 0.5D);
         this.bittrack = 0.0F;
         this.samptrack = 0.0F;
         return var2;
      }
   }

   public void close() throws IOException {
      this.datasource.close();
   }

   public Comment[] getComment() {
      return this.vc;
   }

   public Comment getComment(int var1) {
      if (this.seekable) {
         if (var1 < 0) {
            return this.decode_ready ? this.vc[this.current_link] : null;
         } else {
            return var1 >= this.links ? null : this.vc[var1];
         }
      } else {
         return this.decode_ready ? this.vc[0] : null;
      }
   }

   public Info[] getInfo() {
      return this.vi;
   }

   public Info getInfo(int var1) {
      if (this.seekable) {
         if (var1 < 0) {
            return this.decode_ready ? this.vi[this.current_link] : null;
         } else {
            return var1 >= this.links ? null : this.vi[var1];
         }
      } else {
         return this.decode_ready ? this.vi[0] : null;
      }
   }

   public int pcm_seek(long var1) {
      boolean var3 = true;
      long var4 = this.pcm_total(-1);
      if (!this.seekable) {
         return -1;
      } else if (var1 >= 0L && var1 <= var4) {
         int var19;
         for(var19 = this.links - 1; var19 >= 0; --var19) {
            var4 -= this.pcmlengths[var19];
            if (var1 >= var4) {
               break;
            }
         }

         long var6 = var1 - var4;
         long var8 = this.offsets[var19 + 1];
         long var10 = this.offsets[var19];
         int var12 = (int)var10;
         Page var13 = new Page();

         while(var10 < var8) {
            long var14;
            if (var8 - var10 < 8500L) {
               var14 = var10;
            } else {
               var14 = (var8 + var10) / 2L;
            }

            this.seek_helper(var14);
            int var16 = this.get_next_page(var13, var8 - var14);
            if (var16 == -1) {
               var8 = var14;
            } else {
               long var17 = var13.granulepos();
               if (var17 < var6) {
                  var12 = var16;
                  var10 = this.offset;
               } else {
                  var8 = var14;
               }
            }
         }

         if (this.raw_seek(var12) != 0) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
         } else if (this.pcm_offset >= var1) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
         } else if (var1 > this.pcm_total(-1)) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
         } else {
            while(this.pcm_offset < var1) {
               int var20 = (int)(var1 - this.pcm_offset);
               float[][][] var7 = new float[1][][];
               int[] var21 = new int[this.getInfo(-1).channels];
               int var9 = this.vd.synthesis_pcmout(var7, var21);
               if (var9 > var20) {
                  var9 = var20;
               }

               this.vd.synthesis_read(var9);
               this.pcm_offset += (long)var9;
               if (var9 < var20 && this.process_packet(1) == 0) {
                  this.pcm_offset = this.pcm_total(-1);
               }
            }

            return 0;
         }
      } else {
         this.pcm_offset = -1L;
         this.decode_clear();
         return -1;
      }
   }

   public long pcm_tell() {
      return this.pcm_offset;
   }

   public long pcm_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return this.pcmlengths[var1];
         } else {
            long var2 = 0L;

            for(int var4 = 0; var4 < this.links; ++var4) {
               var2 += this.pcm_total(var4);
            }

            return var2;
         }
      } else {
         return -1L;
      }
   }

   public int raw_seek(int var1) {
      if (!this.seekable) {
         return -1;
      } else if (var1 >= 0 && (long)var1 <= this.offsets[this.links]) {
         this.pcm_offset = -1L;
         this.decode_clear();
         this.seek_helper((long)var1);
         switch(this.process_packet(1)) {
         case -1:
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
         case 0:
            this.pcm_offset = this.pcm_total(-1);
            return 0;
         default:
            while(true) {
               switch(this.process_packet(0)) {
               case -1:
                  this.pcm_offset = -1L;
                  this.decode_clear();
                  return -1;
               case 0:
                  return 0;
               }
            }
         }
      } else {
         this.pcm_offset = -1L;
         this.decode_clear();
         return -1;
      }
   }

   public long raw_tell() {
      return this.offset;
   }

   public long raw_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return this.offsets[var1 + 1] - this.offsets[var1];
         } else {
            long var2 = 0L;

            for(int var4 = 0; var4 < this.links; ++var4) {
               var2 += this.raw_total(var4);
            }

            return var2;
         }
      } else {
         return -1L;
      }
   }

   public boolean seekable() {
      return this.seekable;
   }

   public int serialnumber(int var1) {
      if (var1 >= this.links) {
         return -1;
      } else if (!this.seekable && var1 >= 0) {
         return this.serialnumber(-1);
      } else {
         return var1 < 0 ? this.current_serialno : this.serialnos[var1];
      }
   }

   public int streams() {
      return this.links;
   }

   public float time_tell() {
      int var1 = -1;
      long var2 = 0L;
      float var4 = 0.0F;
      if (this.seekable) {
         var2 = this.pcm_total(-1);
         var4 = this.time_total(-1);

         for(var1 = this.links - 1; var1 >= 0; --var1) {
            var2 -= this.pcmlengths[var1];
            var4 -= this.time_total(var1);
            if (this.pcm_offset >= var2) {
               break;
            }
         }
      }

      return var4 + (float)(this.pcm_offset - var2) / (float)this.vi[var1].rate;
   }

   public float time_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return (float)this.pcmlengths[var1] / (float)this.vi[var1].rate;
         } else {
            float var2 = 0.0F;

            for(int var3 = 0; var3 < this.links; ++var3) {
               var2 += this.time_total(var3);
            }

            return var2;
         }
      } else {
         return -1.0F;
      }
   }

   int bisect_forward_serialno(long var1, long var3, long var5, int var7, int var8) {
      long var9 = var5;
      long var11 = var5;
      Page var13 = new Page();

      int var14;
      while(var3 < var9) {
         long var15;
         if (var9 - var3 < 8500L) {
            var15 = var3;
         } else {
            var15 = (var3 + var9) / 2L;
         }

         this.seek_helper(var15);
         var14 = this.get_next_page(var13, -1L);
         if (var14 == -128) {
            return -128;
         }

         if (var14 >= 0 && var13.serialno() == var7) {
            var3 = (long)(var14 + var13.header_len + var13.body_len);
         } else {
            var9 = var15;
            if (var14 >= 0) {
               var11 = (long)var14;
            }
         }
      }

      this.seek_helper(var11);
      var14 = this.get_next_page(var13, -1L);
      if (var14 == -128) {
         return -128;
      } else {
         if (var3 < var5 && var14 != -1) {
            var14 = this.bisect_forward_serialno(var11, this.offset, var5, var13.serialno(), var8 + 1);
            if (var14 == -128) {
               return -128;
            }
         } else {
            this.links = var8 + 1;
            this.offsets = new long[var8 + 2];
            this.offsets[var8 + 1] = var3;
         }

         this.offsets[var8] = var1;
         return 0;
      }
   }

   int clear() {
      this.vb.clear();
      this.vd.clear();
      this.os.clear();
      if (this.vi != null && this.links != 0) {
         for(int var1 = 0; var1 < this.links; ++var1) {
            this.vi[var1].clear();
            this.vc[var1].clear();
         }

         this.vi = null;
         this.vc = null;
      }

      if (this.dataoffsets != null) {
         this.dataoffsets = null;
      }

      if (this.pcmlengths != null) {
         this.pcmlengths = null;
      }

      if (this.serialnos != null) {
         this.serialnos = null;
      }

      if (this.offsets != null) {
         this.offsets = null;
      }

      this.oy.clear();
      return 0;
   }

   void decode_clear() {
      this.os.clear();
      this.vd.clear();
      this.vb.clear();
      this.decode_ready = false;
      this.bittrack = 0.0F;
      this.samptrack = 0.0F;
   }

   int fetch_headers(Info var1, Comment var2, int[] var3, Page var4) {
      Page var5 = new Page();
      Packet var6 = new Packet();
      if (var4 == null) {
         int var7 = this.get_next_page(var5, 8500L);
         if (var7 == -128) {
            return -128;
         }

         if (var7 < 0) {
            return -132;
         }

         var4 = var5;
      }

      if (var3 != null) {
         var3[0] = var4.serialno();
      }

      this.os.init(var4.serialno());
      var1.init();
      var2.init();
      int var8 = 0;

      do {
         if (var8 >= 3) {
            return 0;
         }

         this.os.pagein(var4);

         while(var8 < 3) {
            int var9 = this.os.packetout(var6);
            if (var9 == 0) {
               break;
            }

            if (var9 == -1) {
               var1.clear();
               var2.clear();
               this.os.clear();
               return -1;
            }

            if (var1.synthesis_headerin(var2, var6) != 0) {
               var1.clear();
               var2.clear();
               this.os.clear();
               return -1;
            }

            ++var8;
         }
      } while(var8 >= 3 || this.get_next_page(var4, 1L) >= 0);

      var1.clear();
      var2.clear();
      this.os.clear();
      return -1;
   }

   int host_is_big_endian() {
      return 1;
   }

   int open(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      return this.open_callbacks(var1, var2, var3);
   }

   int open_callbacks(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      this.datasource = var1;
      this.oy.init();
      if (var2 != null) {
         int var5 = this.oy.buffer(var3);
         System.arraycopy(var2, 0, this.oy.data, var5, var3);
         this.oy.wrote(var3);
      }

      int var4;
      if (var1 instanceof VorbisFile.SeekableInputStream) {
         var4 = this.open_seekable();
      } else {
         var4 = this.open_nonseekable();
      }

      if (var4 != 0) {
         this.datasource = null;
         this.clear();
      }

      return var4;
   }

   int open_nonseekable() {
      this.links = 1;
      this.vi = new Info[this.links];
      this.vi[0] = new Info();
      this.vc = new Comment[this.links];
      this.vc[0] = new Comment();
      int[] var1 = new int[1];
      if (this.fetch_headers(this.vi[0], this.vc[0], var1, (Page)null) == -1) {
         return -1;
      } else {
         this.current_serialno = var1[0];
         this.make_decode_ready();
         return 0;
      }
   }

   int open_seekable() throws JOrbisException {
      Info var1 = new Info();
      Comment var2 = new Comment();
      Page var8 = new Page();
      int[] var9 = new int[1];
      int var6 = this.fetch_headers(var1, var2, var9, (Page)null);
      int var3 = var9[0];
      int var7 = (int)this.offset;
      this.os.clear();
      if (var6 == -1) {
         return -1;
      } else if (var6 < 0) {
         return var6;
      } else {
         this.seekable = true;
         fseek(this.datasource, 0L, 2);
         this.offset = ftell(this.datasource);
         long var4 = this.offset;
         var4 = (long)this.get_prev_page(var8);
         if (var8.serialno() != var3) {
            if (this.bisect_forward_serialno(0L, 0L, var4 + 1L, var3, 0) < 0) {
               this.clear();
               return -128;
            }
         } else if (this.bisect_forward_serialno(0L, var4, var4 + 1L, var3, 0) < 0) {
            this.clear();
            return -128;
         }

         this.prefetch_all_headers(var1, var2, var7);
         return 0;
      }
   }

   void prefetch_all_headers(Info var1, Comment var2, int var3) throws JOrbisException {
      Page var4 = new Page();
      this.vi = new Info[this.links];
      this.vc = new Comment[this.links];
      this.dataoffsets = new long[this.links];
      this.pcmlengths = new long[this.links];
      this.serialnos = new int[this.links];

      label38:
      for(int var6 = 0; var6 < this.links; ++var6) {
         if (var1 != null && var2 != null && var6 == 0) {
            this.vi[var6] = var1;
            this.vc[var6] = var2;
            this.dataoffsets[var6] = (long)var3;
         } else {
            this.seek_helper(this.offsets[var6]);
            this.vi[var6] = new Info();
            this.vc[var6] = new Comment();
            if (this.fetch_headers(this.vi[var6], this.vc[var6], (int[])null, (Page)null) == -1) {
               this.dataoffsets[var6] = -1L;
            } else {
               this.dataoffsets[var6] = this.offset;
               this.os.clear();
            }
         }

         long var7 = this.offsets[var6 + 1];
         this.seek_helper(var7);

         do {
            int var5 = this.get_prev_page(var4);
            if (var5 == -1) {
               this.vi[var6].clear();
               this.vc[var6].clear();
               continue label38;
            }
         } while(var4.granulepos() == -1L);

         this.serialnos[var6] = var4.serialno();
         this.pcmlengths[var6] = var4.granulepos();
      }

   }

   int process_packet(int var1) {
      Page var2 = new Page();

      while(true) {
         if (this.decode_ready) {
            Packet var3 = new Packet();
            int var4 = this.os.packetout(var3);
            if (var4 > 0) {
               long var5 = var3.granulepos;
               if (this.vb.synthesis(var3) == 0) {
                  int var7 = this.vd.synthesis_pcmout((float[][][])null, (int[])null);
                  this.vd.synthesis_blockin(this.vb);
                  this.samptrack += (float)(this.vd.synthesis_pcmout((float[][][])null, (int[])null) - var7);
                  this.bittrack += (float)(var3.bytes * 8);
                  if (var5 != -1L && var3.e_o_s == 0) {
                     var7 = this.seekable ? this.current_link : 0;
                     int var8 = this.vd.synthesis_pcmout((float[][][])null, (int[])null);
                     var5 -= (long)var8;

                     for(int var9 = 0; var9 < var7; ++var9) {
                        var5 += this.pcmlengths[var9];
                     }

                     this.pcm_offset = var5;
                  }

                  return 1;
               }
            }
         }

         if (var1 == 0) {
            return 0;
         }

         if (this.get_next_page(var2, -1L) < 0) {
            return 0;
         }

         this.bittrack += (float)(var2.header_len * 8);
         if (this.decode_ready && this.current_serialno != var2.serialno()) {
            this.decode_clear();
         }

         if (!this.decode_ready) {
            if (!this.seekable) {
               int[] var12 = new int[1];
               int var13 = this.fetch_headers(this.vi[0], this.vc[0], var12, var2);
               this.current_serialno = var12[0];
               if (var13 != 0) {
                  return var13;
               }

               ++this.current_link;
               boolean var11 = false;
            } else {
               this.current_serialno = var2.serialno();

               int var10;
               for(var10 = 0; var10 < this.links && this.serialnos[var10] != this.current_serialno; ++var10) {
               }

               if (var10 == this.links) {
                  return -1;
               }

               this.current_link = var10;
               this.os.init(this.current_serialno);
               this.os.reset();
            }

            this.make_decode_ready();
         }

         this.os.pagein(var2);
      }
   }

   int read(byte[] var1, int var2, int var3, int var4, int var5, int[] var6) {
      int var7 = this.host_is_big_endian();
      int var8 = 0;

      while(true) {
         if (this.decode_ready) {
            float[][][] var10 = new float[1][][];
            int[] var11 = new int[this.getInfo(-1).channels];
            int var12 = this.vd.synthesis_pcmout(var10, var11);
            float[][] var9 = var10[0];
            if (var12 != 0) {
               int var13 = this.getInfo(-1).channels;
               int var14 = var4 * var13;
               if (var12 > var2 / var14) {
                  var12 = var2 / var14;
               }

               int var15;
               int var16;
               int var17;
               int var18;
               if (var4 == 1) {
                  var16 = var5 != 0 ? 0 : 128;

                  for(var17 = 0; var17 < var12; ++var17) {
                     for(var18 = 0; var18 < var13; ++var18) {
                        var15 = (int)((double)var9[var18][var11[var18] + var17] * 128.0D + 0.5D);
                        if (var15 > 127) {
                           var15 = 127;
                        } else if (var15 < -128) {
                           var15 = -128;
                        }

                        var1[var8++] = (byte)(var15 + var16);
                     }
                  }
               } else {
                  var16 = var5 != 0 ? 0 : '耀';
                  if (var7 == var3) {
                     int var19;
                     int var20;
                     if (var5 != 0) {
                        for(var17 = 0; var17 < var13; ++var17) {
                           var18 = var11[var17];
                           var19 = var17;

                           for(var20 = 0; var20 < var12; ++var20) {
                              var15 = (int)((double)var9[var17][var18 + var20] * 32768.0D + 0.5D);
                              if (var15 > 32767) {
                                 var15 = 32767;
                              } else if (var15 < -32768) {
                                 var15 = -32768;
                              }

                              var1[var19] = (byte)(var15 >>> 8);
                              var1[var19 + 1] = (byte)var15;
                              var19 += var13 * 2;
                           }
                        }
                     } else {
                        for(var17 = 0; var17 < var13; ++var17) {
                           float[] var21 = var9[var17];
                           var19 = var17;

                           for(var20 = 0; var20 < var12; ++var20) {
                              var15 = (int)((double)var21[var20] * 32768.0D + 0.5D);
                              if (var15 > 32767) {
                                 var15 = 32767;
                              } else if (var15 < -32768) {
                                 var15 = -32768;
                              }

                              var1[var19] = (byte)(var15 + var16 >>> 8);
                              var1[var19 + 1] = (byte)(var15 + var16);
                              var19 += var13 * 2;
                           }
                        }
                     }
                  } else if (var3 != 0) {
                     for(var17 = 0; var17 < var12; ++var17) {
                        for(var18 = 0; var18 < var13; ++var18) {
                           var15 = (int)((double)var9[var18][var17] * 32768.0D + 0.5D);
                           if (var15 > 32767) {
                              var15 = 32767;
                           } else if (var15 < -32768) {
                              var15 = -32768;
                           }

                           var15 += var16;
                           var1[var8++] = (byte)(var15 >>> 8);
                           var1[var8++] = (byte)var15;
                        }
                     }
                  } else {
                     for(var17 = 0; var17 < var12; ++var17) {
                        for(var18 = 0; var18 < var13; ++var18) {
                           var15 = (int)((double)var9[var18][var17] * 32768.0D + 0.5D);
                           if (var15 > 32767) {
                              var15 = 32767;
                           } else if (var15 < -32768) {
                              var15 = -32768;
                           }

                           var15 += var16;
                           var1[var8++] = (byte)var15;
                           var1[var8++] = (byte)(var15 >>> 8);
                        }
                     }
                  }
               }

               this.vd.synthesis_read(var12);
               this.pcm_offset += (long)var12;
               if (var6 != null) {
                  var6[0] = this.current_link;
               }

               return var12 * var14;
            }
         }

         switch(this.process_packet(1)) {
         case -1:
            return -1;
         case 0:
            return 0;
         }
      }
   }

   int time_seek(float var1) {
      boolean var2 = true;
      long var3 = this.pcm_total(-1);
      float var5 = this.time_total(-1);
      if (!this.seekable) {
         return -1;
      } else if (!(var1 < 0.0F) && !(var1 > var5)) {
         int var8;
         for(var8 = this.links - 1; var8 >= 0; --var8) {
            var3 -= this.pcmlengths[var8];
            var5 -= this.time_total(var8);
            if (var1 >= var5) {
               break;
            }
         }

         long var6 = (long)((float)var3 + (var1 - var5) * (float)this.vi[var8].rate);
         return this.pcm_seek(var6);
      } else {
         this.pcm_offset = -1L;
         this.decode_clear();
         return -1;
      }
   }

   private int get_data() {
      int var1 = this.oy.buffer(8500);
      byte[] var2 = this.oy.data;
      boolean var3 = false;

      int var6;
      try {
         var6 = this.datasource.read(var2, var1, 8500);
      } catch (Exception var5) {
         return -128;
      }

      this.oy.wrote(var6);
      if (var6 == -1) {
         var6 = 0;
      }

      return var6;
   }

   private int get_next_page(Page var1, long var2) {
      if (var2 > 0L) {
         var2 += this.offset;
      }

      while(var2 <= 0L || this.offset < var2) {
         int var4 = this.oy.pageseek(var1);
         if (var4 < 0) {
            this.offset -= (long)var4;
         } else {
            int var5;
            if (var4 != 0) {
               var5 = (int)this.offset;
               this.offset += (long)var4;
               return var5;
            }

            if (var2 == 0L) {
               return -1;
            }

            var5 = this.get_data();
            if (var5 == 0) {
               return -2;
            }

            if (var5 < 0) {
               return -128;
            }
         }
      }

      return -1;
   }

   private int get_prev_page(Page var1) throws JOrbisException {
      long var2 = this.offset;
      int var5 = -1;

      label37:
      do {
         int var4;
         while(var5 == -1) {
            var2 -= 8500L;
            if (var2 < 0L) {
               var2 = 0L;
            }

            this.seek_helper(var2);

            while(this.offset < var2 + 8500L) {
               var4 = this.get_next_page(var1, var2 + 8500L - this.offset);
               if (var4 == -128) {
                  return -128;
               }

               if (var4 < 0) {
                  continue label37;
               }

               var5 = var4;
            }
         }

         this.seek_helper((long)var5);
         var4 = this.get_next_page(var1, 8500L);
         if (var4 < 0) {
            return -129;
         }

         return var5;
      } while(var5 != -1);

      throw new JOrbisException();
   }

   private int make_decode_ready() {
      if (this.decode_ready) {
         System.exit(1);
      }

      this.vd.synthesis_init(this.vi[0]);
      this.vb.init(this.vd);
      this.decode_ready = true;
      return 0;
   }

   private void seek_helper(long var1) {
      fseek(this.datasource, var1, 0);
      this.offset = var1;
      this.oy.reset();
   }

   class SeekableInputStream extends InputStream {
      final String mode = "r";
      RandomAccessFile raf = null;

      SeekableInputStream(String var2) throws IOException {
         this.raf = new RandomAccessFile(var2, "r");
      }

      public int available() throws IOException {
         return this.raf.length() == this.raf.getFilePointer() ? 0 : 1;
      }

      public void close() throws IOException {
         this.raf.close();
      }

      public long getLength() throws IOException {
         return this.raf.length();
      }

      public synchronized void mark(int var1) {
      }

      public boolean markSupported() {
         return false;
      }

      public int read() throws IOException {
         return this.raf.read();
      }

      public int read(byte[] var1) throws IOException {
         return this.raf.read(var1);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return this.raf.read(var1, var2, var3);
      }

      public synchronized void reset() throws IOException {
      }

      public void seek(long var1) throws IOException {
         this.raf.seek(var1);
      }

      public long skip(long var1) throws IOException {
         return (long)this.raf.skipBytes((int)var1);
      }

      public long tell() throws IOException {
         return this.raf.getFilePointer();
      }
   }
}
