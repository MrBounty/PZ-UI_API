package com.sixlegs.png;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PngImage implements Transparency {
   private static final PngConfig DEFAULT_CONFIG = (new PngConfig.Builder()).build();
   private final PngConfig config;
   private final Map props;
   private boolean read;

   public PngImage() {
      this(DEFAULT_CONFIG);
   }

   public PngImage(PngConfig var1) {
      this.props = new HashMap();
      this.read = false;
      this.config = var1;
   }

   public PngConfig getConfig() {
      return this.config;
   }

   public BufferedImage read(File var1) throws IOException {
      return this.read(new BufferedInputStream(new FileInputStream(var1)), true);
   }

   public BufferedImage read(InputStream var1, boolean var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("InputStream is null");
      } else {
         this.read = true;
         this.props.clear();
         int var3 = this.config.getReadLimit();
         BufferedImage var4 = null;
         StateMachine var5 = new StateMachine(this);

         try {
            PngInputStream var6 = new PngInputStream(var1);

            int var8;
            for(HashSet var7 = new HashSet(); var5.getState() != 6; var6.endChunk(var8)) {
               var8 = var6.startChunk();
               var5.nextState(var8);

               try {
                  ImageDataInputStream var9;
                  if (var8 == 1229209940) {
                     switch(var3) {
                     case 2:
                        var9 = null;
                        return var9;
                     case 3:
                        break;
                     default:
                        var9 = new ImageDataInputStream(var6, var5);
                        var4 = this.createImage(var9, new Dimension(this.getWidth(), this.getHeight()));

                        while((var8 = var5.getType()) == 1229209940) {
                           skipFully(var9, (long)var6.getRemaining());
                        }
                     }
                  }

                  String var10002;
                  if (!this.isMultipleOK(var8) && !var7.add(Integers.valueOf(var8))) {
                     var10002 = PngConstants.getChunkName(var8);
                     throw new PngException("Multiple " + var10002 + " chunks are not allowed", !PngConstants.isAncillary(var8));
                  }

                  try {
                     this.readChunk(var8, var6, var6.getOffset(), var6.getRemaining());
                  } catch (PngException var15) {
                     throw var15;
                  } catch (IOException var16) {
                     var10002 = PngConstants.getChunkName(var8);
                     throw new PngException("Malformed " + var10002 + " chunk", var16, !PngConstants.isAncillary(var8));
                  }

                  skipFully(var6, (long)var6.getRemaining());
                  if (var8 == 1229472850 && var3 == 1) {
                     var9 = null;
                     return var9;
                  }
               } catch (PngException var17) {
                  if (var17.isFatal()) {
                     throw var17;
                  }

                  skipFully(var6, (long)var6.getRemaining());
                  this.handleWarning(var17);
               }
            }

            BufferedImage var19 = var4;
            return var19;
         } finally {
            if (var2) {
               var1.close();
            }

         }
      }
   }

   protected BufferedImage createImage(InputStream var1, Dimension var2) throws IOException {
      return ImageFactory.createImage(this, var1, var2);
   }

   protected boolean handlePass(BufferedImage var1, int var2) {
      return true;
   }

   protected boolean handleProgress(BufferedImage var1, float var2) {
      return true;
   }

   protected void handleWarning(PngException var1) throws PngException {
      if (this.config.getWarningsFatal()) {
         throw var1;
      }
   }

   public int getWidth() {
      return this.getInt("width");
   }

   public int getHeight() {
      return this.getInt("height");
   }

   public int getBitDepth() {
      return this.getInt("bit_depth");
   }

   public boolean isInterlaced() {
      return this.getInt("interlace") != 0;
   }

   public int getColorType() {
      return this.getInt("color_type");
   }

   public int getTransparency() {
      int var1 = this.getColorType();
      return var1 != 6 && var1 != 4 && !this.props.containsKey("transparency") && !this.props.containsKey("palette_alpha") ? 1 : 3;
   }

   public int getSamples() {
      switch(this.getColorType()) {
      case 2:
         return 3;
      case 3:
      case 5:
      default:
         return 1;
      case 4:
         return 2;
      case 6:
         return 4;
      }
   }

   public float getGamma() {
      this.assertRead();
      return this.props.containsKey("gamma") ? ((Number)this.getProperty("gamma", Number.class, true)).floatValue() : this.config.getDefaultGamma();
   }

   public short[] getGammaTable() {
      this.assertRead();
      return createGammaTable(this.getGamma(), this.config.getDisplayExponent(), this.getBitDepth() == 16 && !this.config.getReduce16());
   }

   static short[] createGammaTable(float var0, float var1, boolean var2) {
      int var3 = 1 << (var2 ? 16 : 8);
      short[] var4 = new short[var3];
      double var5 = 1.0D / ((double)var0 * (double)var1);

      for(int var7 = 0; var7 < var3; ++var7) {
         var4[var7] = (short)((int)(Math.pow((double)var7 / (double)(var3 - 1), var5) * (double)(var3 - 1)));
      }

      return var4;
   }

   public Color getBackground() {
      int[] var1 = (int[])this.getProperty("background_rgb", int[].class, false);
      if (var1 == null) {
         return null;
      } else {
         switch(this.getColorType()) {
         case 0:
         case 4:
            int var4 = var1[0] * 255 / ((1 << this.getBitDepth()) - 1);
            return new Color(var4, var4, var4);
         case 1:
         case 2:
         default:
            if (this.getBitDepth() == 16) {
               return new Color(var1[0] >> 8, var1[1] >> 8, var1[2] >> 8);
            }

            return new Color(var1[0], var1[1], var1[2]);
         case 3:
            byte[] var2 = (byte[])this.getProperty("palette", byte[].class, true);
            int var3 = var1[0] * 3;
            return new Color(255 & var2[var3 + 0], 255 & var2[var3 + 1], 255 & var2[var3 + 2]);
         }
      }
   }

   public Object getProperty(String var1) {
      this.assertRead();
      return this.props.get(var1);
   }

   Object getProperty(String var1, Class var2, boolean var3) {
      this.assertRead();
      Object var4 = this.props.get(var1);
      if (var4 == null) {
         if (var3) {
            throw new IllegalStateException("Image is missing property \"" + var1 + "\"");
         }
      } else if (!var2.isAssignableFrom(var4.getClass())) {
         throw new IllegalStateException("Property \"" + var1 + "\" has type " + var4.getClass().getName() + ", expected " + var2.getName());
      }

      return var4;
   }

   private int getInt(String var1) {
      return ((Number)this.getProperty(var1, Number.class, true)).intValue();
   }

   public Map getProperties() {
      return this.props;
   }

   public TextChunk getTextChunk(String var1) {
      List var2 = (List)this.getProperty("text_chunks", List.class, false);
      if (var1 != null && var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            TextChunk var4 = (TextChunk)var3.next();
            if (var4.getKeyword().equals(var1)) {
               return var4;
            }
         }
      }

      return null;
   }

   protected void readChunk(int var1, DataInput var2, long var3, int var5) throws IOException {
      if (var1 != 1229209940) {
         if (this.config.getReadLimit() == 4 && PngConstants.isAncillary(var1)) {
            switch(var1) {
            case 1732332865:
            case 1951551059:
               break;
            default:
               return;
            }
         }

         RegisteredChunks.read(var1, var2, var5, this);
      }
   }

   protected boolean isMultipleOK(int var1) {
      switch(var1) {
      case 1229209940:
      case 1767135348:
      case 1934642260:
      case 1950701684:
      case 2052348020:
         return true;
      default:
         return false;
      }
   }

   private void assertRead() {
      if (!this.read) {
         throw new IllegalStateException("Image has not been read");
      }
   }

   private static void skipFully(InputStream var0, long var1) throws IOException {
      while(var1 > 0L) {
         long var3 = var0.skip(var1);
         if (var3 == 0L) {
            if (var0.read() == -1) {
               throw new EOFException();
            }

            --var1;
         } else {
            var1 -= var3;
         }
      }

   }
}
