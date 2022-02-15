package zombie.worldMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.SandboxOptions;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.ShaderProgram;
import zombie.core.textures.TextureID;
import zombie.core.utils.ImageUtils;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.SliceY;
import zombie.iso.Vector2;
import zombie.worldMap.styles.WorldMapStyleLayer;

public class WorldMapVisited {
   private static WorldMapVisited instance;
   private int m_minX;
   private int m_minY;
   private int m_maxX;
   private int m_maxY;
   byte[] m_visited;
   boolean m_changed = false;
   int m_changeX1 = 0;
   int m_changeY1 = 0;
   int m_changeX2 = 0;
   int m_changeY2 = 0;
   private final int[] m_updateMinX = new int[4];
   private final int[] m_updateMinY = new int[4];
   private final int[] m_updateMaxX = new int[4];
   private final int[] m_updateMaxY = new int[4];
   private static final int TEXTURE_BPP = 4;
   private TextureID m_textureID;
   private int m_textureW = 0;
   private int m_textureH = 0;
   private ByteBuffer m_textureBuffer;
   private boolean m_textureChanged = false;
   private final WorldMapStyleLayer.RGBAf m_color = (new WorldMapStyleLayer.RGBAf()).init(0.85882354F, 0.84313726F, 0.7529412F, 1.0F);
   private final WorldMapStyleLayer.RGBAf m_gridColor;
   private boolean m_mainMenu;
   private static ShaderProgram m_shaderProgram;
   private static ShaderProgram m_gridShaderProgram;
   static final int UNITS_PER_CELL = 10;
   static final int SQUARES_PER_CELL = 300;
   static final int SQUARES_PER_UNIT = 30;
   static final int TEXTURE_PAD = 1;
   static final int BIT_VISITED = 1;
   static final int BIT_KNOWN = 2;
   Vector2 m_vector2;

   public WorldMapVisited() {
      this.m_gridColor = (new WorldMapStyleLayer.RGBAf()).init(this.m_color.r * 0.85F, this.m_color.g * 0.85F, this.m_color.b * 0.85F, 1.0F);
      this.m_mainMenu = false;
      this.m_vector2 = new Vector2();
      Arrays.fill(this.m_updateMinX, -1);
      Arrays.fill(this.m_updateMinY, -1);
      Arrays.fill(this.m_updateMaxX, -1);
      Arrays.fill(this.m_updateMaxY, -1);
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      if (var1 > var3 || var2 > var4) {
         var4 = 0;
         var2 = 0;
         var3 = 0;
         var1 = 0;
         this.m_mainMenu = true;
      }

      this.m_minX = var1;
      this.m_minY = var2;
      this.m_maxX = var3;
      this.m_maxY = var4;
      this.m_changed = true;
      this.m_changeX1 = 0;
      this.m_changeY1 = 0;
      this.m_changeX2 = this.getWidthInCells() * 10 - 1;
      this.m_changeY2 = this.getHeightInCells() * 10 - 1;
      this.m_visited = new byte[this.getWidthInCells() * 10 * this.getHeightInCells() * 10];
      this.m_textureW = this.calcTextureWidth();
      this.m_textureH = this.calcTextureHeight();
      this.m_textureBuffer = BufferUtils.createByteBuffer(this.m_textureW * this.m_textureH * 4);
      this.m_textureBuffer.limit(this.m_textureBuffer.capacity());
      int var5 = SandboxOptions.getInstance().Map.MapAllKnown.getValue() ? 0 : -1;
      byte var6 = -1;
      byte var7 = -1;
      byte var8 = -1;

      for(int var9 = 0; var9 < this.m_textureBuffer.limit(); var9 += 4) {
         this.m_textureBuffer.put(var9, (byte)var5);
         this.m_textureBuffer.put(var9 + 1, var6);
         this.m_textureBuffer.put(var9 + 2, var7);
         this.m_textureBuffer.put(var9 + 3, var8);
      }

      this.m_textureID = new TextureID(this.m_textureW, this.m_textureH, 0);
   }

   public int getMinX() {
      return this.m_minX;
   }

   public int getMinY() {
      return this.m_minY;
   }

   private int getWidthInCells() {
      return this.m_maxX - this.m_minX + 1;
   }

   private int getHeightInCells() {
      return this.m_maxY - this.m_minY + 1;
   }

   private int calcTextureWidth() {
      return ImageUtils.getNextPowerOfTwo(this.getWidthInCells() * 10 + 2);
   }

   private int calcTextureHeight() {
      return ImageUtils.getNextPowerOfTwo(this.getHeightInCells() * 10 + 2);
   }

   public void setKnownInCells(int var1, int var2, int var3, int var4) {
      this.setFlags(var1 * 300, var2 * 300, (var3 + 1) * 300, (var4 + 1) * 300, 2);
   }

   public void clearKnownInCells(int var1, int var2, int var3, int var4) {
      this.clearFlags(var1 * 300, var2 * 300, (var3 + 1) * 300, (var4 + 1) * 300, 2);
   }

   public void setVisitedInCells(int var1, int var2, int var3, int var4) {
      this.setFlags(var1 * 300, var2 * 300, var3 * 300, var4 * 300, 1);
   }

   public void clearVisitedInCells(int var1, int var2, int var3, int var4) {
      this.clearFlags(var1 * 300, var2 * 300, var3 * 300, var4 * 300, 1);
   }

   public void setKnownInSquares(int var1, int var2, int var3, int var4) {
      this.setFlags(var1, var2, var3, var4, 2);
   }

   public void clearKnownInSquares(int var1, int var2, int var3, int var4) {
      this.clearFlags(var1, var2, var3, var4, 2);
   }

   public void setVisitedInSquares(int var1, int var2, int var3, int var4) {
      this.setFlags(var1, var2, var3, var4, 1);
   }

   public void clearVisitedInSquares(int var1, int var2, int var3, int var4) {
      this.clearFlags(var1, var2, var3, var4, 1);
   }

   private void updateVisitedTexture() {
      this.m_textureID.bind();
      GL11.glTexImage2D(3553, 0, 6408, this.m_textureW, this.m_textureH, 0, 6408, 5121, this.m_textureBuffer);
   }

   public void renderMain() {
      this.m_textureChanged |= this.updateTextureData(this.m_textureBuffer, this.m_textureW);
   }

   private void initShader() {
      m_shaderProgram = ShaderProgram.createShaderProgram("worldMapVisited", false, true);
      if (m_shaderProgram.isCompiled()) {
      }

   }

   public void render(float var1, float var2, int var3, int var4, int var5, int var6, float var7, boolean var8) {
      if (!this.m_mainMenu) {
         GL13.glActiveTexture(33984);
         GL13.glClientActiveTexture(33984);
         GL11.glEnable(3553);
         if (this.m_textureChanged) {
            this.m_textureChanged = false;
            this.updateVisitedTexture();
         }

         this.m_textureID.bind();
         int var9 = var8 ? 9729 : 9728;
         GL11.glTexParameteri(3553, 10241, var9);
         GL11.glTexParameteri(3553, 10240, var9);
         GL11.glEnable(3042);
         GL11.glTexEnvi(8960, 8704, 8448);
         GL11.glColor4f(this.m_color.r, this.m_color.g, this.m_color.b, this.m_color.a);
         if (m_shaderProgram == null) {
            this.initShader();
         }

         if (m_shaderProgram.isCompiled()) {
            m_shaderProgram.Start();
            float var10 = (float)(1 + (var3 - this.m_minX) * 10) / (float)this.m_textureW;
            float var11 = (float)(1 + (var4 - this.m_minY) * 10) / (float)this.m_textureH;
            float var12 = (float)(1 + (var5 + 1 - this.m_minX) * 10) / (float)this.m_textureW;
            float var13 = (float)(1 + (var6 + 1 - this.m_minY) * 10) / (float)this.m_textureH;
            float var14 = (float)((var3 - this.m_minX) * 300) * var7;
            float var15 = (float)((var4 - this.m_minY) * 300) * var7;
            float var16 = (float)((var5 + 1 - this.m_minX) * 300) * var7;
            float var17 = (float)((var6 + 1 - this.m_minY) * 300) * var7;
            GL11.glBegin(7);
            GL11.glTexCoord2f(var10, var11);
            GL11.glVertex2f(var1 + var14, var2 + var15);
            GL11.glTexCoord2f(var12, var11);
            GL11.glVertex2f(var1 + var16, var2 + var15);
            GL11.glTexCoord2f(var12, var13);
            GL11.glVertex2f(var1 + var16, var2 + var17);
            GL11.glTexCoord2f(var10, var13);
            GL11.glVertex2f(var1 + var14, var2 + var17);
            GL11.glEnd();
            m_shaderProgram.End();
         }
      }
   }

   public void renderGrid(float var1, float var2, int var3, int var4, int var5, int var6, float var7, float var8) {
      if (!(var8 < 11.0F)) {
         if (m_gridShaderProgram == null) {
            m_gridShaderProgram = ShaderProgram.createShaderProgram("worldMapGrid", false, true);
         }

         if (m_gridShaderProgram.isCompiled()) {
            m_gridShaderProgram.Start();
            float var9 = var1 + (float)(var3 * 300 - this.m_minX * 300) * var7;
            float var10 = var2 + (float)(var4 * 300 - this.m_minY * 300) * var7;
            float var11 = var9 + (float)((var5 - var3 + 1) * 300) * var7;
            float var12 = var10 + (float)((var6 - var4 + 1) * 300) * var7;
            VBOLinesUV var13 = WorldMapRenderer.m_vboLinesUV;
            var13.setMode(1);
            var13.setLineWidth(0.5F);
            var13.startRun(this.m_textureID);
            float var14 = this.m_gridColor.r;
            float var15 = this.m_gridColor.g;
            float var16 = this.m_gridColor.b;
            float var17 = this.m_gridColor.a;
            byte var18 = 1;
            if (var8 < 13.0F) {
               var18 = 8;
            } else if (var8 < 14.0F) {
               var18 = 4;
            } else if (var8 < 15.0F) {
               var18 = 2;
            }

            m_gridShaderProgram.setValue("UVOffset", this.m_vector2.set(0.5F / (float)this.m_textureW, 0.0F));

            int var19;
            for(var19 = var3 * 10; var19 <= (var5 + 1) * 10; var19 += var18) {
               var13.reserve(2);
               var13.addElement(var1 + (float)(var19 * 30 - this.m_minX * 300) * var7, var10, 0.0F, (float)(1 + var19 - this.m_minX * 10) / (float)this.m_textureW, 1.0F / (float)this.m_textureH, var14, var15, var16, var17);
               var13.addElement(var1 + (float)(var19 * 30 - this.m_minX * 300) * var7, var12, 0.0F, (float)(1 + var19 - this.m_minX * 10) / (float)this.m_textureW, (float)(1 + this.getHeightInCells() * 10) / (float)this.m_textureH, var14, var15, var16, var17);
            }

            m_gridShaderProgram.setValue("UVOffset", this.m_vector2.set(0.0F, 0.5F / (float)this.m_textureH));

            for(var19 = var4 * 10; var19 <= (var6 + 1) * 10; var19 += var18) {
               var13.reserve(2);
               var13.addElement(var9, var2 + (float)(var19 * 30 - this.m_minY * 300) * var7, 0.0F, 1.0F / (float)this.m_textureW, (float)(1 + var19 - this.m_minY * 10) / (float)this.m_textureH, var14, var15, var16, var17);
               var13.addElement(var11, var2 + (float)(var19 * 30 - this.m_minY * 300) * var7, 0.0F, (float)(1 + this.getWidthInCells() * 10) / (float)this.m_textureW, (float)(1 + var19 - this.m_minY * 10) / (float)this.m_textureH, var14, var15, var16, var17);
            }

            var13.flush();
            m_gridShaderProgram.End();
         }
      }
   }

   private void destroy() {
      if (this.m_textureID != null) {
         TextureID var10000 = this.m_textureID;
         Objects.requireNonNull(var10000);
         RenderThread.invokeOnRenderContext(var10000::destroy);
      }

      this.m_textureBuffer = null;
      this.m_visited = null;
   }

   public void save(ByteBuffer var1) {
      var1.putInt(this.m_minX);
      var1.putInt(this.m_minY);
      var1.putInt(this.m_maxX);
      var1.putInt(this.m_maxY);
      var1.putInt(10);
      var1.put(this.m_visited);
   }

   public void load(ByteBuffer var1, int var2) {
      int var3 = var1.getInt();
      int var4 = var1.getInt();
      int var5 = var1.getInt();
      int var6 = var1.getInt();
      int var7 = var1.getInt();
      if (var3 == this.m_minX && var4 == this.m_minY && var5 == this.m_maxX && var6 == this.m_maxY && var7 == 10) {
         var1.get(this.m_visited);
      }
   }

   public void save() throws IOException {
      ByteBuffer var1 = SliceY.SliceBuffer;
      var1.clear();
      var1.putInt(186);
      this.save(var1);
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var2 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "map_visited.bin");
      FileOutputStream var3 = new FileOutputStream(var2);

      try {
         BufferedOutputStream var4 = new BufferedOutputStream(var3);

         try {
            var4.write(var1.array(), 0, var1.position());
         } catch (Throwable var9) {
            try {
               var4.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var4.close();
      } catch (Throwable var10) {
         try {
            var3.close();
         } catch (Throwable var7) {
            var10.addSuppressed(var7);
         }

         throw var10;
      }

      var3.close();
   }

   public void load() throws IOException {
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var1 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "map_visited.bin");

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               ByteBuffer var4 = SliceY.SliceBuffer;
               var4.clear();
               int var5 = var3.read(var4.array());
               var4.limit(var5);
               int var6 = var4.getInt();
               this.load(var4, var6);
            } catch (Throwable var9) {
               try {
                  var3.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var3.close();
         } catch (Throwable var10) {
            try {
               var2.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var2.close();
      } catch (FileNotFoundException var11) {
      }

   }

   private void setFlags(int var1, int var2, int var3, int var4, int var5) {
      var1 -= this.m_minX * 300;
      var2 -= this.m_minY * 300;
      var3 -= this.m_minX * 300;
      var4 -= this.m_minY * 300;
      int var6 = this.getWidthInCells();
      int var7 = this.getHeightInCells();
      var1 = PZMath.clamp(var1, 0, var6 * 300 - 1);
      var2 = PZMath.clamp(var2, 0, var7 * 300 - 1);
      var3 = PZMath.clamp(var3, 0, var6 * 300 - 1);
      var4 = PZMath.clamp(var4, 0, var7 * 300 - 1);
      if (var1 != var3 && var2 != var4) {
         int var8 = var1 / 30;
         int var9 = var3 / 30;
         int var10 = var2 / 30;
         int var11 = var4 / 30;
         if (var3 % 30 == 0) {
            --var9;
         }

         if (var4 % 30 == 0) {
            --var11;
         }

         boolean var12 = false;
         int var13 = var6 * 10;

         for(int var14 = var10; var14 <= var11; ++var14) {
            for(int var15 = var8; var15 <= var9; ++var15) {
               byte var16 = this.m_visited[var15 + var14 * var13];
               if ((var16 & var5) != var5) {
                  this.m_visited[var15 + var14 * var13] = (byte)(var16 | var5);
                  var12 = true;
               }
            }
         }

         if (var12) {
            this.m_changed = true;
            this.m_changeX1 = PZMath.min(this.m_changeX1, var8);
            this.m_changeY1 = PZMath.min(this.m_changeY1, var10);
            this.m_changeX2 = PZMath.max(this.m_changeX2, var9);
            this.m_changeY2 = PZMath.max(this.m_changeY2, var11);
         }

      }
   }

   private void clearFlags(int var1, int var2, int var3, int var4, int var5) {
      var1 -= this.m_minX * 300;
      var2 -= this.m_minY * 300;
      var3 -= this.m_minX * 300;
      var4 -= this.m_minY * 300;
      int var6 = this.getWidthInCells();
      int var7 = this.getHeightInCells();
      var1 = PZMath.clamp(var1, 0, var6 * 300 - 1);
      var2 = PZMath.clamp(var2, 0, var7 * 300 - 1);
      var3 = PZMath.clamp(var3, 0, var6 * 300 - 1);
      var4 = PZMath.clamp(var4, 0, var7 * 300 - 1);
      if (var1 != var3 && var2 != var4) {
         int var8 = var1 / 30;
         int var9 = var3 / 30;
         int var10 = var2 / 30;
         int var11 = var4 / 30;
         if (var3 % 30 == 0) {
            --var9;
         }

         if (var4 % 30 == 0) {
            --var11;
         }

         boolean var12 = false;
         int var13 = var6 * 10;

         for(int var14 = var10; var14 <= var11; ++var14) {
            for(int var15 = var8; var15 <= var9; ++var15) {
               byte var16 = this.m_visited[var15 + var14 * var13];
               if ((var16 & var5) != var5) {
                  this.m_visited[var15 + var14 * var13] = (byte)(var16 & ~var5);
                  var12 = true;
               }
            }
         }

         if (var12) {
            this.m_changed = true;
            this.m_changeX1 = PZMath.min(this.m_changeX1, var8);
            this.m_changeY1 = PZMath.min(this.m_changeY1, var10);
            this.m_changeX2 = PZMath.max(this.m_changeX2, var9);
            this.m_changeY2 = PZMath.max(this.m_changeY2, var11);
         }

      }
   }

   private boolean updateTextureData(ByteBuffer var1, int var2) {
      if (!this.m_changed) {
         return false;
      } else {
         this.m_changed = false;
         byte var3 = 4;
         int var4 = this.getWidthInCells() * 10;

         for(int var5 = this.m_changeY1; var5 <= this.m_changeY2; ++var5) {
            var1.position((1 + this.m_changeX1) * var3 + (1 + var5) * var2 * var3);

            for(int var6 = this.m_changeX1; var6 <= this.m_changeX2; ++var6) {
               byte var7 = this.m_visited[var6 + var5 * var4];
               var1.put((byte)((var7 & 2) != 0 ? 0 : -1));
               var1.put((byte)((var7 & 1) != 0 ? 0 : -1));
               var1.put((byte)-1);
               var1.put((byte)-1);
            }
         }

         var1.position(0);
         this.m_changeX1 = Integer.MAX_VALUE;
         this.m_changeY1 = Integer.MAX_VALUE;
         this.m_changeX2 = Integer.MIN_VALUE;
         this.m_changeY2 = Integer.MIN_VALUE;
         return true;
      }
   }

   void setUnvisitedRGBA(float var1, float var2, float var3, float var4) {
      this.m_color.init(var1, var2, var3, var4);
   }

   void setUnvisitedGridRGBA(float var1, float var2, float var3, float var4) {
      this.m_gridColor.init(var1, var2, var3, var4);
   }

   boolean hasFlags(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      var1 -= this.m_minX * 300;
      var2 -= this.m_minY * 300;
      var3 -= this.m_minX * 300;
      var4 -= this.m_minY * 300;
      int var7 = this.getWidthInCells();
      int var8 = this.getHeightInCells();
      var1 = PZMath.clamp(var1, 0, var7 * 300 - 1);
      var2 = PZMath.clamp(var2, 0, var8 * 300 - 1);
      var3 = PZMath.clamp(var3, 0, var7 * 300 - 1);
      var4 = PZMath.clamp(var4, 0, var8 * 300 - 1);
      if (var1 != var3 && var2 != var4) {
         int var9 = var1 / 30;
         int var10 = var3 / 30;
         int var11 = var2 / 30;
         int var12 = var4 / 30;
         if (var3 % 30 == 0) {
            --var10;
         }

         if (var4 % 30 == 0) {
            --var12;
         }

         int var13 = var7 * 10;

         for(int var14 = var11; var14 <= var12; ++var14) {
            for(int var15 = var9; var15 <= var10; ++var15) {
               byte var16 = this.m_visited[var15 + var14 * var13];
               if (var6) {
                  if ((var16 & var5) != 0) {
                     return true;
                  }
               } else if ((var16 & var5) != var5) {
                  return false;
               }
            }
         }

         return !var6;
      } else {
         return false;
      }
   }

   boolean isCellVisible(int var1, int var2) {
      return this.hasFlags(var1 * 300, var2 * 300, (var1 + 1) * 300, (var2 + 1) * 300, 3, true);
   }

   public static WorldMapVisited getInstance() {
      IsoMetaGrid var0 = IsoWorld.instance.getMetaGrid();
      if (var0 == null) {
         throw new NullPointerException("IsoWorld.instance.MetaGrid is null");
      } else {
         if (instance == null) {
            instance = new WorldMapVisited();
            instance.setBounds(var0.getMinX(), var0.getMinY(), var0.getMaxX(), var0.getMaxY());

            try {
               instance.load();
               if (SandboxOptions.getInstance().Map.MapAllKnown.getValue()) {
                  instance.setKnownInCells(var0.getMinX(), var0.getMinY(), var0.getMaxX(), var0.getMaxY());
               }
            } catch (Throwable var2) {
               ExceptionLogger.logException(var2);
            }
         }

         return instance;
      }
   }

   public static void update() {
      if (IsoWorld.instance != null) {
         WorldMapVisited var0 = getInstance();
         if (var0 != null) {
            for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
               IsoPlayer var2 = IsoPlayer.players[var1];
               if (var2 != null && !var2.isDead()) {
                  byte var3 = 25;
                  int var4 = ((int)var2.x - var3) / 30;
                  int var5 = ((int)var2.y - var3) / 30;
                  int var6 = ((int)var2.x + var3) / 30;
                  int var7 = ((int)var2.y + var3) / 30;
                  if (((int)var2.x + var3) % 30 == 0) {
                     --var6;
                  }

                  if (((int)var2.y + var3) % 30 == 0) {
                     --var7;
                  }

                  if (var4 != var0.m_updateMinX[var1] || var5 != var0.m_updateMinY[var1] || var6 != var0.m_updateMaxX[var1] || var7 != var0.m_updateMaxY[var1]) {
                     var0.m_updateMinX[var1] = var4;
                     var0.m_updateMinY[var1] = var5;
                     var0.m_updateMaxX[var1] = var6;
                     var0.m_updateMaxY[var1] = var7;
                     var0.setFlags((int)var2.x - var3, (int)var2.y - var3, (int)var2.x + var3, (int)var2.y + var3, 3);
                  }
               }
            }

         }
      }
   }

   public static void SaveAll() {
      WorldMapVisited var0 = instance;
      if (var0 != null) {
         try {
            var0.save();
         } catch (Exception var2) {
            ExceptionLogger.logException(var2);
         }
      }

   }

   public static void Reset() {
      WorldMapVisited var0 = instance;
      if (var0 != null) {
         var0.destroy();
         instance = null;
      }

   }
}
