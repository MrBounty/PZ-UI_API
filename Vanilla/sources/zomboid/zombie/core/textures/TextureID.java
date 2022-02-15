package zombie.core.textures;

import java.io.BufferedInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjglx.BufferUtils;
import zombie.IndieGL;
import zombie.SystemDisabler;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.RenderThread;
import zombie.core.utils.BooleanGrid;
import zombie.core.utils.DirectBufferAllocator;
import zombie.core.utils.WrappedBuffer;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.fileSystem.FileSystem;
import zombie.interfaces.IDestroyable;

public final class TextureID extends Asset implements IDestroyable, Serializable {
   private static final long serialVersionUID = 4409253583065563738L;
   public static long totalGraphicMemory = 0L;
   public static boolean UseFiltering = false;
   public static boolean bUseCompression = true;
   public static boolean bUseCompressionOption = true;
   public static float totalMemUsed = 0.0F;
   private static boolean FREE_MEMORY = true;
   private static final HashMap TextureIDMap = new HashMap();
   protected String pathFileName;
   protected boolean solid;
   protected int width;
   protected int widthHW;
   protected int height;
   protected int heightHW;
   protected transient ImageData data;
   protected transient int id = -1;
   private int m_glMagFilter = -1;
   private int m_glMinFilter = -1;
   ArrayList alphaList;
   int referenceCount = 0;
   BooleanGrid mask;
   protected int flags = 0;
   public TextureID.TextureIDAssetParams assetParams;
   public static final IntBuffer deleteTextureIDS = BufferUtils.createIntBuffer(20);
   public static final AssetType ASSET_TYPE = new AssetType("TextureID");

   public TextureID(AssetPath var1, AssetManager var2, TextureID.TextureIDAssetParams var3) {
      super(var1, var2);
      this.assetParams = var3;
      this.flags = var3 == null ? 0 : this.assetParams.flags;
   }

   protected TextureID() {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.onCreated(Asset.State.READY);
   }

   public TextureID(int var1, int var2, int var3) {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = new TextureID.TextureIDAssetParams();
      this.assetParams.flags = var3;
      if ((var3 & 16) != 0) {
         if ((var3 & 4) != 0) {
            DebugLog.General.warn("FBO incompatible with COMPRESS");
            TextureID.TextureIDAssetParams var10000 = this.assetParams;
            var10000.flags &= -5;
         }

         this.data = new ImageData(var1, var2, (WrappedBuffer)null);
      } else {
         this.data = new ImageData(var1, var2);
      }

      this.width = this.data.getWidth();
      this.height = this.data.getHeight();
      this.widthHW = this.data.getWidthHW();
      this.heightHW = this.data.getHeightHW();
      this.solid = this.data.isSolid();
      RenderThread.queueInvokeOnRenderContext(() -> {
         this.createTexture(false);
      });
      this.onCreated(Asset.State.READY);
   }

   public TextureID(ImageData var1) {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.data = var1;
      RenderThread.invokeOnRenderContext(this::createTexture);
      this.onCreated(Asset.State.READY);
   }

   public TextureID(String var1, String var2) {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.data = new ImageData(var1, var2);
      this.pathFileName = var1;
      RenderThread.invokeOnRenderContext(this::createTexture);
      this.onCreated(Asset.State.READY);
   }

   public TextureID(String var1, int[] var2) {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.data = new ImageData(var1, var2);
      this.pathFileName = var1;
      RenderThread.invokeOnRenderContext(this::createTexture);
      this.onCreated(Asset.State.READY);
   }

   public TextureID(String var1, int var2, int var3, int var4) throws Exception {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      int var5;
      while((var5 = var1.indexOf("\\")) != -1) {
         String var10000 = var1.substring(0, var5);
         var1 = var10000 + "/" + var1.substring(var5 + 1);
      }

      (this.data = new ImageData(var1)).makeTransp((byte)var2, (byte)var3, (byte)var4);
      if (this.alphaList == null) {
         this.alphaList = new ArrayList();
      }

      this.alphaList.add(new AlphaColorIndex(var2, var3, var4, 0));
      this.pathFileName = var1;
      RenderThread.invokeOnRenderContext(this::createTexture);
      this.onCreated(Asset.State.READY);
   }

   public TextureID(String var1) throws Exception {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      if (var1.toLowerCase().contains(".pcx")) {
         this.data = new ImageData(var1, var1);
      } else {
         this.data = new ImageData(var1);
      }

      if (this.data.getHeight() != -1) {
         this.pathFileName = var1;
         RenderThread.invokeOnRenderContext(this::createTexture);
         this.onCreated(Asset.State.READY);
      }
   }

   public TextureID(BufferedInputStream var1, String var2, boolean var3, Texture.PZFileformat var4) {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.data = new ImageData(var1, var3, var4);
      if (this.data.id != -1) {
         this.id = this.data.id;
         this.width = this.data.getWidth();
         this.height = this.data.getHeight();
         this.widthHW = this.data.getWidthHW();
         this.heightHW = this.data.getHeightHW();
         this.solid = this.data.isSolid();
      } else {
         if (var3) {
            this.mask = this.data.mask;
            this.data.mask = null;
         }

         this.createTexture();
      }

      this.pathFileName = var2;
      this.onCreated(Asset.State.READY);
   }

   public TextureID(BufferedInputStream var1, String var2, boolean var3) throws Exception {
      super((AssetPath)null, TextureIDAssetManager.instance);
      this.assetParams = null;
      this.data = new ImageData(var1, var3);
      if (var3) {
         this.mask = this.data.mask;
         this.data.mask = null;
      }

      this.pathFileName = var2;
      RenderThread.invokeOnRenderContext(this::createTexture);
      this.onCreated(Asset.State.READY);
   }

   public static TextureID createSteamAvatar(long var0) {
      ImageData var2 = ImageData.createSteamAvatar(var0);
      if (var2 == null) {
         return null;
      } else {
         TextureID var3 = new TextureID(var2);
         return var3;
      }
   }

   public int getID() {
      return this.id;
   }

   public boolean bind() {
      if (this.id == -1 && this.data == null) {
         Texture.getErrorTexture().bind();
         return true;
      } else {
         this.debugBoundTexture();
         return this.id != -1 && this.id == Texture.lastTextureID ? false : this.bindalways();
      }
   }

   public boolean bindalways() {
      this.bindInternal();
      return true;
   }

   private void bindInternal() {
      if (this.id == -1) {
         this.generateHwId(this.data != null && this.data.data != null);
      }

      this.assignFilteringFlags();
      Texture.lastlastTextureID = Texture.lastTextureID;
      Texture.lastTextureID = this.id;
      ++Texture.BindCount;
   }

   private void debugBoundTexture() {
      if (DebugOptions.instance.Checks.BoundTextures.getValue() && Texture.lastTextureID != -1) {
         int var1 = GL11.glGetInteger(34016);
         if (var1 == 33984) {
            int var2 = GL11.glGetInteger(32873);
            if (var2 != Texture.lastTextureID) {
               String var3 = null;
               Iterator var4 = TextureIDAssetManager.instance.getAssetTable().values().iterator();

               while(var4.hasNext()) {
                  Asset var5 = (Asset)var4.next();
                  TextureID var6 = (TextureID)var5;
                  if (var6.id == Texture.lastTextureID) {
                     var3 = var6.getPath().getPath();
                     break;
                  }
               }

               DebugLog.General.error("Texture.lastTextureID %d != GL_TEXTURE_BINDING_2D %d name=%s", Texture.lastTextureID, var2, var3);
            }
         }
      }

   }

   public void destroy() {
      assert Thread.currentThread() == RenderThread.RenderThread;

      if (this.id != -1) {
         if (deleteTextureIDS.position() == deleteTextureIDS.capacity()) {
            deleteTextureIDS.flip();
            GL11.glDeleteTextures(deleteTextureIDS);
            deleteTextureIDS.clear();
         }

         deleteTextureIDS.put(this.id);
         this.id = -1;
      }
   }

   public void freeMemory() {
      this.data = null;
   }

   public WrappedBuffer getData() {
      this.bind();
      WrappedBuffer var1 = DirectBufferAllocator.allocate(this.heightHW * this.widthHW * 4);
      GL11.glGetTexImage(3553, 0, 6408, 5121, var1.getBuffer());
      Texture.lastTextureID = 0;
      GL11.glBindTexture(3553, 0);
      return var1;
   }

   public void setData(ByteBuffer var1) {
      if (var1 == null) {
         this.freeMemory();
      } else {
         this.bind();
         GL11.glTexSubImage2D(3553, 0, 0, 0, this.widthHW, this.heightHW, 6408, 5121, var1);
         if (this.data != null) {
            MipMapLevel var2 = this.data.getData();
            ByteBuffer var3 = var2.getBuffer();
            var1.flip();
            var3.clear();
            var3.put(var1);
            var3.flip();
         }

      }
   }

   public ImageData getImageData() {
      return this.data;
   }

   public void setImageData(ImageData var1) {
      this.data = var1;
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      this.widthHW = var1.getWidthHW();
      this.heightHW = var1.getHeightHW();
      if (var1.mask != null) {
         this.mask = var1.mask;
         var1.mask = null;
      }

      RenderThread.queueInvokeOnRenderContext(this::createTexture);
   }

   public String getPathFileName() {
      return this.pathFileName;
   }

   public boolean isDestroyed() {
      return this.id == -1;
   }

   public boolean isSolid() {
      return this.solid;
   }

   private void createTexture() {
      if (this.data != null) {
         this.createTexture(true);
      }
   }

   private void createTexture(boolean var1) {
      if (this.id == -1) {
         this.width = this.data.getWidth();
         this.height = this.data.getHeight();
         this.widthHW = this.data.getWidthHW();
         this.heightHW = this.data.getHeightHW();
         this.solid = this.data.isSolid();
         this.generateHwId(var1);
      }
   }

   private void generateHwId(boolean var1) {
      this.id = GL11.glGenTextures();
      ++Texture.totalTextureID;
      GL11.glBindTexture(3553, Texture.lastTextureID = this.id);
      SpriteRenderer.ringBuffer.restoreBoundTextures = true;
      int var2;
      if (this.assetParams == null) {
         var2 = bUseCompressionOption ? 4 : 0;
      } else {
         var2 = this.assetParams.flags;
      }

      boolean var3 = (var2 & 1) != 0;
      boolean var4 = (var2 & 2) != 0;
      boolean var5 = (var2 & 16) != 0;
      boolean var6 = (var2 & 64) != 0 && !var5 && var1;
      boolean var7 = (var2 & 4) != 0;
      char var8;
      if (var7 && GL.getCapabilities().GL_ARB_texture_compression) {
         var8 = '蓮';
      } else {
         var8 = 6408;
      }

      this.m_glMagFilter = var4 ? 9728 : 9729;
      this.m_glMinFilter = var6 ? 9987 : (var3 ? 9728 : 9729);
      GL11.glTexParameteri(3553, 10241, this.m_glMinFilter);
      GL11.glTexParameteri(3553, 10240, this.m_glMagFilter);
      if ((var2 & 32) != 0) {
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      if (var1) {
         if (var6) {
            PZGLUtil.checkGLErrorThrow("TextureID.mipMaps.start");
            int var9 = this.data.getMipMapCount();
            int var10 = PZMath.min(0, var9 - 1);
            int var11 = var9;

            for(int var12 = var10; var12 < var11; ++var12) {
               MipMapLevel var13 = this.data.getMipMapData(var12);
               int var14 = var13.width;
               int var15 = var13.height;
               totalMemUsed += (float)var13.getDataSize();
               GL11.glTexImage2D(3553, var12 - var10, var8, var14, var15, 0, 6408, 5121, var13.getBuffer());
               PZGLUtil.checkGLErrorThrow("TextureID.mipMaps[%d].end", var12);
            }

            PZGLUtil.checkGLErrorThrow("TextureID.mipMaps.end");
         } else {
            PZGLUtil.checkGLErrorThrow("TextureID.noMips.start");
            totalMemUsed += (float)(this.widthHW * this.heightHW * 4);
            GL11.glTexImage2D(3553, 0, var8, this.widthHW, this.heightHW, 0, 6408, 5121, this.data.getData().getBuffer());
            PZGLUtil.checkGLErrorThrow("TextureID.noMips.end");
         }
      } else {
         GL11.glTexImage2D(3553, 0, var8, this.widthHW, this.heightHW, 0, 6408, 5121, (ByteBuffer)null);
         totalMemUsed += (float)(this.widthHW * this.heightHW * 4);
      }

      if (FREE_MEMORY) {
         if (this.data != null) {
            this.data.dispose();
         }

         this.data = null;
         if (this.assetParams != null) {
            this.assetParams.subTexture = null;
            this.assetParams = null;
         }
      }

      TextureIDMap.put(this.id, this.pathFileName);
      if (SystemDisabler.doEnableDetectOpenGLErrorsInTexture) {
         PZGLUtil.checkGLErrorThrow("generateHwId id:%d pathFileName:%s", this.id, this.pathFileName);
      }

   }

   private void assignFilteringFlags() {
      GL11.glBindTexture(3553, this.id);
      if (this.width == 1 && this.height == 1) {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      } else {
         GL11.glTexParameteri(3553, 10241, this.m_glMinFilter);
         GL11.glTexParameteri(3553, 10240, this.m_glMagFilter);
         if ((this.flags & 64) != 0 && DebugOptions.instance.IsoSprite.NearestMagFilterAtMinZoom.getValue() && this.isMinZoomLevel() && this.m_glMagFilter != 9728) {
            GL11.glTexParameteri(3553, 10240, 9728);
         }

         if (DebugOptions.instance.IsoSprite.ForceLinearMagFilter.getValue() && this.m_glMagFilter != 9729) {
            GL11.glTexParameteri(3553, 10240, 9729);
         }

         if (DebugOptions.instance.IsoSprite.ForceNearestMagFilter.getValue() && this.m_glMagFilter != 9728) {
            GL11.glTexParameteri(3553, 10240, 9728);
         }

         if (DebugOptions.instance.IsoSprite.ForceNearestMipMapping.getValue() && this.m_glMinFilter == 9987) {
            GL11.glTexParameteri(3553, 10241, 9986);
         }

         if (DebugOptions.instance.IsoSprite.TextureWrapClampToEdge.getValue()) {
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
         }

         if (DebugOptions.instance.IsoSprite.TextureWrapRepeat.getValue()) {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
         }

         if (SystemDisabler.doEnableDetectOpenGLErrorsInTexture) {
            PZGLUtil.checkGLErrorThrow("assignFilteringFlags id:%d pathFileName:%s", this.id, this.pathFileName);
         }

      }
   }

   public void setMagFilter(int var1) {
      this.m_glMagFilter = var1;
   }

   public void setMinFilter(int var1) {
      this.m_glMinFilter = var1;
   }

   public boolean hasMipMaps() {
      return this.m_glMinFilter == 9987;
   }

   private boolean isMaxZoomLevel() {
      return IndieGL.isMaxZoomLevel();
   }

   private boolean isMinZoomLevel() {
      return IndieGL.isMinZoomLevel();
   }

   public void setAssetParams(AssetManager.AssetParams var1) {
      this.assetParams = (TextureID.TextureIDAssetParams)var1;
      this.flags = this.assetParams == null ? 0 : this.assetParams.flags;
   }

   public AssetType getType() {
      return ASSET_TYPE;
   }

   public static final class TextureIDAssetParams extends AssetManager.AssetParams {
      FileSystem.SubTexture subTexture;
      int flags = 0;
   }
}
