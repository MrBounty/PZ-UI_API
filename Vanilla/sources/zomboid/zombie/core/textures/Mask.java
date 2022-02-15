package zombie.core.textures;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import zombie.core.opengl.RenderThread;
import zombie.core.utils.BooleanGrid;
import zombie.core.utils.WrappedBuffer;
import zombie.interfaces.ITexture;

public final class Mask implements Serializable, Cloneable {
   private static final long serialVersionUID = -5679205580926696806L;
   private boolean full;
   private int height;
   BooleanGrid mask;
   private int width;

   protected Mask() {
   }

   public Mask(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      this.mask = new BooleanGrid(var1, var2);
      this.full();
   }

   public Mask(Texture var1, Texture var2, int var3, int var4, int var5, int var6) {
      if (var1.getMask() != null) {
         var5 = var2.getWidth();
         var6 = var2.getHeight();
         var2.setMask(this);
         this.mask = new BooleanGrid(var5, var6);

         for(int var7 = var3; var7 < var3 + var5; ++var7) {
            for(int var8 = var4; var8 < var4 + var6; ++var8) {
               this.mask.setValue(var7 - var3, var8 - var4, var1.getMask().mask.getValue(var7, var8));
            }
         }

      }
   }

   public Mask(Mask var1, int var2, int var3, int var4, int var5) {
      this.width = var4;
      this.height = var5;
      this.mask = new BooleanGrid(var4, var5);

      for(int var6 = 0; var6 < var4; ++var6) {
         for(int var7 = 0; var7 < var5; ++var7) {
            this.mask.setValue(var6, var7, var1.mask.getValue(var2 + var6, var3 + var7));
         }
      }

   }

   public Mask(boolean[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.width = var6;
      this.height = var7;
      this.mask = new BooleanGrid(var6, var7);

      for(int var8 = 0; var8 < var6; ++var8) {
         for(int var9 = 0; var9 < var7; ++var9) {
            this.mask.setValue(var8, var9, var1[var4 + var8 + (var5 + var9) * var2]);
         }
      }

   }

   public Mask(BooleanGrid var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.width = var6;
      this.height = var7;
      this.mask = new BooleanGrid(var6, var7);

      for(int var8 = 0; var8 < var6; ++var8) {
         for(int var9 = 0; var9 < var7; ++var9) {
            this.mask.setValue(var8, var9, var1.getValue(var4 + var8, var5 + var9));
         }
      }

   }

   protected Mask(Texture var1, WrappedBuffer var2) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      int var3 = var1.getWidthHW();
      int var4 = var1.getHeightHW();
      int var5 = (int)(var1.getXStart() * (float)var3);
      int var6 = (int)(var1.getXEnd() * (float)var3);
      int var7 = (int)(var1.getYStart() * (float)var4);
      int var8 = (int)(var1.getYEnd() * (float)var4);
      this.mask = new BooleanGrid(this.width, this.height);
      var1.setMask(this);
      ByteBuffer var9 = var2.getBuffer();
      var9.rewind();

      for(int var11 = 0; var11 < var1.getHeightHW(); ++var11) {
         for(int var12 = 0; var12 < var1.getWidthHW(); ++var12) {
            var9.get();
            var9.get();
            var9.get();
            byte var10 = var9.get();
            if (var12 >= var5 && var12 < var6 && var11 >= var7 && var11 < var8) {
               if (var10 == 0) {
                  this.mask.setValue(var12 - var5, var11 - var7, false);
                  this.full = false;
               } else {
                  if (var10 < 127) {
                     this.mask.setValue(var12 - var5, var11 - var7, true);
                  }

                  this.mask.setValue(var12 - var5, var11 - var7, true);
               }
            }

            if (var11 >= var8) {
               break;
            }
         }
      }

      var2.dispose();
   }

   public Mask(ITexture var1, boolean[] var2) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      int var3 = var1.getWidthHW();
      int var10000 = (int)(var1.getXStart() * (float)var3);
      var10000 = (int)(var1.getXEnd() * (float)var3);
      int var6 = (int)(var1.getYStart() * (float)(var3 = var1.getHeightHW()));
      var10000 = (int)(var1.getYEnd() * (float)var3);
      var1.setMask(this);
      this.mask = new BooleanGrid(this.width, this.height);

      for(int var8 = 0; var8 < var1.getHeight(); ++var8) {
         for(int var9 = 0; var9 < var1.getWidth(); ++var9) {
            this.mask.setValue(var9, var8, var2[var8 * var1.getWidth() + var9]);
         }
      }

   }

   public Mask(ITexture var1, BooleanGrid var2) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      var1.setMask(this);
      this.mask = new BooleanGrid(this.width, this.height);

      for(int var3 = 0; var3 < var1.getHeight(); ++var3) {
         for(int var4 = 0; var4 < var1.getWidth(); ++var4) {
            this.mask.setValue(var4, var3, var2.getValue(var4, var3));
         }
      }

   }

   public Mask(ITexture var1) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      int var2 = var1.getWidthHW();
      int var3 = (int)(var1.getXStart() * (float)var2);
      int var4 = (int)(var1.getXEnd() * (float)var2);
      int var5 = (int)(var1.getYStart() * (float)(var2 = var1.getHeightHW()));
      int var6 = (int)(var1.getYEnd() * (float)var2);
      var1.setMask(this);
      this.mask = new BooleanGrid(this.width, this.height);
      RenderThread.invokeOnRenderContext(() -> {
         WrappedBuffer var6x = var1.getData();
         ByteBuffer var7 = var6x.getBuffer();
         var7.rewind();

         for(int var9 = 0; var9 < var1.getHeightHW(); ++var9) {
            for(int var10 = 0; var10 < var1.getWidthHW(); ++var10) {
               var7.get();
               var7.get();
               var7.get();
               byte var8 = var7.get();
               if (var10 >= var3 && var10 < var4 && var9 >= var5 && var9 < var6) {
                  if (var8 == 0) {
                     this.mask.setValue(var10 - var3, var9 - var5, false);
                     this.full = false;
                  } else {
                     if (var8 < 127) {
                        this.mask.setValue(var10 - var3, var9 - var5, true);
                     } else {
                        boolean var11 = false;
                     }

                     this.mask.setValue(var10 - var3, var9 - var5, true);
                  }
               }

               if (var9 >= var6) {
                  break;
               }
            }
         }

         var6x.dispose();
      });
   }

   public Mask(Mask var1) {
      this.width = var1.width;
      this.height = var1.height;
      this.full = var1.full;

      try {
         this.mask = var1.mask.clone();
      } catch (CloneNotSupportedException var3) {
         var3.printStackTrace(System.err);
      }

   }

   public Object clone() {
      return new Mask(this);
   }

   public void full() {
      this.mask.fill();
      this.full = true;
   }

   public void set(int var1, int var2, boolean var3) {
      this.mask.setValue(var1, var2, var3);
      if (!var3 && this.full) {
         this.full = false;
      }

   }

   public boolean get(int var1, int var2) {
      return this.full ? true : this.mask.getValue(var1, var2);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.width = var1.readInt();
      this.height = var1.readInt();
      this.full = var1.readBoolean();
      if (var1.readBoolean()) {
         this.mask = (BooleanGrid)var1.readObject();
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeInt(this.width);
      var1.writeInt(this.height);
      var1.writeBoolean(this.full);
      if (this.mask != null) {
         var1.writeBoolean(true);
         var1.writeObject(this.mask);
      } else {
         var1.writeBoolean(false);
      }

   }

   public void save(String var1) {
   }
}
