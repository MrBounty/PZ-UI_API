package zombie.core.textures;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.ZomboidFileSystem;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.bucket.BucketManager;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.RenderThread;
import zombie.core.utils.BooleanGrid;
import zombie.core.utils.ImageUtils;
import zombie.core.utils.WrappedBuffer;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.fileSystem.FileSystem;
import zombie.interfaces.IDestroyable;
import zombie.interfaces.ITexture;
import zombie.iso.Vector2;
import zombie.iso.objects.ObjectRenderEffects;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.util.StringUtils;
import zombie.util.Type;

public class Texture extends Asset implements IDestroyable, ITexture, Serializable {
   public static final HashSet nullTextures = new HashSet();
   private static final long serialVersionUID = 7472363451408935314L;
   private static final ObjectRenderEffects objRen = ObjectRenderEffects.alloc();
   public static int BindCount = 0;
   public static boolean bDoingQuad = false;
   public static float lr;
   public static float lg;
   public static float lb;
   public static float la;
   public static int lastlastTextureID = -2;
   public static int totalTextureID = 0;
   private static Texture white = null;
   private static Texture errorTexture = null;
   private static Texture mipmap = null;
   public static int lastTextureID = -1;
   public static boolean WarnFailFindTexture = true;
   private static final HashMap textures = new HashMap();
   private static final HashMap s_sharedTextureTable = new HashMap();
   private static final HashMap steamAvatarMap = new HashMap();
   public boolean flip;
   public float offsetX;
   public float offsetY;
   public boolean bindAlways;
   public float xEnd;
   public float yEnd;
   public float xStart;
   public float yStart;
   protected TextureID dataid;
   protected Mask mask;
   protected String name;
   protected boolean solid;
   protected int width;
   protected int height;
   protected int heightOrig;
   protected int widthOrig;
   private int realWidth;
   private int realHeight;
   private boolean destroyed;
   private Texture splitIconTex;
   private int splitX;
   private int splitY;
   private int splitW;
   private int splitH;
   protected FileSystem.SubTexture subTexture;
   public Texture.TextureAssetParams assetParams;
   private static final ThreadLocal pngSize = ThreadLocal.withInitial(PNGSize::new);
   public static final AssetType ASSET_TYPE = new AssetType("Texture");

   public Texture(AssetPath var1, AssetManager var2, Texture.TextureAssetParams var3) {
      super(var1, var2);
      this.flip = false;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.bindAlways = false;
      this.xEnd = 1.0F;
      this.yEnd = 1.0F;
      this.xStart = 0.0F;
      this.yStart = 0.0F;
      this.realWidth = 0;
      this.realHeight = 0;
      this.destroyed = false;
      this.splitX = -1;
      this.assetParams = var3;
      this.name = var1 == null ? null : var1.getPath();
      if (var3 != null && var3.subTexture != null) {
         FileSystem.SubTexture var4 = var3.subTexture;
         this.splitX = var4.m_info.x;
         this.splitY = var4.m_info.y;
         this.splitW = var4.m_info.w;
         this.splitH = var4.m_info.h;
         this.width = this.splitW;
         this.height = this.splitH;
         this.offsetX = (float)var4.m_info.ox;
         this.offsetY = (float)var4.m_info.oy;
         this.widthOrig = var4.m_info.fx;
         this.heightOrig = var4.m_info.fy;
         this.name = var4.m_info.name;
         this.subTexture = var4;
      }

      TextureID.TextureIDAssetParams var9 = new TextureID.TextureIDAssetParams();
      if (this.assetParams != null && this.assetParams.subTexture != null) {
         var9.subTexture = this.assetParams.subTexture;
         String var5 = var9.subTexture.m_pack_name;
         String var6 = var9.subTexture.m_page_name;
         FileSystem var7 = this.getAssetManager().getOwner().getFileSystem();
         var9.flags = var7.getTexturePackFlags(var5);
         var9.flags |= var7.getTexturePackAlpha(var5, var6) ? 8 : 0;
         AssetPath var8 = new AssetPath("@pack@/" + var5 + "/" + var6);
         this.dataid = (TextureID)TextureIDAssetManager.instance.load(var8, var9);
      } else {
         if (this.assetParams == null) {
            var9.flags |= TextureID.bUseCompressionOption ? 4 : 0;
         } else {
            var9.flags = this.assetParams.flags;
         }

         this.dataid = (TextureID)this.getAssetManager().getOwner().get(TextureID.ASSET_TYPE).load(this.getPath(), var9);
      }

      this.onCreated(Asset.State.EMPTY);
      if (this.dataid != null) {
         this.addDependency(this.dataid);
      }

   }

   public Texture(TextureID var1, String var2) {
      super((AssetPath)null, TextureAssetManager.instance);
      this.flip = false;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.bindAlways = false;
      this.xEnd = 1.0F;
      this.yEnd = 1.0F;
      this.xStart = 0.0F;
      this.yStart = 0.0F;
      this.realWidth = 0;
      this.realHeight = 0;
      this.destroyed = false;
      this.splitX = -1;
      this.dataid = var1;
      ++this.dataid.referenceCount;
      if (var1.isReady()) {
         this.solid = this.dataid.solid;
         this.width = var1.width;
         this.height = var1.height;
         this.xEnd = (float)this.width / (float)var1.widthHW;
         this.yEnd = (float)this.height / (float)var1.heightHW;
      } else {
         assert false;
      }

      this.name = var2;
      this.assetParams = null;
      this.onCreated(var1.getState());
      this.addDependency(var1);
   }

   public Texture(String var1) throws Exception {
      this(new TextureID(var1), var1);
      this.setUseAlphaChannel(true);
   }

   public Texture(String var1, BufferedInputStream var2, boolean var3, Texture.PZFileformat var4) {
      this(new TextureID(var2, var1, var3, var4), var1);
      if (var3 && this.dataid.mask != null) {
         this.createMask(this.dataid.mask);
         this.dataid.mask = null;
         this.dataid.data = null;
      }

   }

   public Texture(String var1, BufferedInputStream var2, boolean var3) throws Exception {
      this(new TextureID(var2, var1, var3), var1);
      if (var3) {
         this.createMask(this.dataid.mask);
         this.dataid.mask = null;
         this.dataid.data = null;
      }

   }

   public Texture(String var1, boolean var2, boolean var3) throws Exception {
      this(new TextureID(var1), var1);
      this.setUseAlphaChannel(var3);
      if (var2) {
         this.dataid.data = null;
      }

   }

   public Texture(String var1, String var2) {
      this(new TextureID(var1, var2), var1);
      this.setUseAlphaChannel(true);
   }

   public Texture(String var1, int[] var2) {
      this(new TextureID(var1, var2), var1);
      if (var1.contains("drag")) {
         boolean var3 = false;
      }

      this.setUseAlphaChannel(true);
   }

   public Texture(String var1, boolean var2) throws Exception {
      this(new TextureID(var1), var1);
      this.setUseAlphaChannel(var2);
   }

   public Texture(int var1, int var2, String var3, int var4) {
      this(new TextureID(var1, var2, var4), var3);
   }

   public Texture(int var1, int var2, int var3) {
      this((TextureID)(new TextureID(var1, var2, var3)), (String)null);
   }

   public Texture(String var1, int var2, int var3, int var4) throws Exception {
      this(new TextureID(var1, var2, var3, var4), var1);
   }

   public Texture(Texture var1) {
      this(var1.dataid, var1.name + "(copy)");
      this.width = var1.width;
      this.height = var1.height;
      this.name = var1.name;
      this.xStart = var1.xStart;
      this.yStart = var1.yStart;
      this.xEnd = var1.xEnd;
      this.yEnd = var1.yEnd;
      this.solid = var1.solid;
   }

   public Texture() {
      super((AssetPath)null, TextureAssetManager.instance);
      this.flip = false;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.bindAlways = false;
      this.xEnd = 1.0F;
      this.yEnd = 1.0F;
      this.xStart = 0.0F;
      this.yStart = 0.0F;
      this.realWidth = 0;
      this.realHeight = 0;
      this.destroyed = false;
      this.splitX = -1;
      this.assetParams = null;
      this.onCreated(Asset.State.EMPTY);
   }

   public static String processFilePath(String var0) {
      var0 = var0.replaceAll("\\\\", "/");
      return var0;
   }

   public static void bindNone() {
      IndieGL.glDisable(3553);
      lastTextureID = -1;
      --BindCount;
   }

   public static Texture getWhite() {
      if (white == null) {
         white = new Texture(32, 32, "white", 0);
         RenderThread.invokeOnRenderContext(() -> {
            GL11.glBindTexture(3553, lastTextureID = white.getID());
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            ByteBuffer var0 = MemoryUtil.memAlloc(white.width * white.height * 4);

            for(int var1 = 0; var1 < white.width * white.height * 4; ++var1) {
               var0.put((byte)-1);
            }

            var0.flip();
            GL11.glTexImage2D(3553, 0, 6408, white.width, white.height, 0, 6408, 5121, var0);
            MemoryUtil.memFree(var0);
         });
         s_sharedTextureTable.put("white.png", white);
         s_sharedTextureTable.put("media/white.png", white);
         s_sharedTextureTable.put("media/ui/white.png", white);
      }

      return white;
   }

   public static Texture getErrorTexture() {
      if (errorTexture == null) {
         errorTexture = new Texture(32, 32, "EngineErrorTexture", 0);
         RenderThread.invokeOnRenderContext(() -> {
            GL11.glBindTexture(3553, lastTextureID = errorTexture.getID());
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            byte var0 = 4;
            ByteBuffer var1 = MemoryUtil.memAlloc(errorTexture.width * errorTexture.height * var0);
            var1.position(errorTexture.width * errorTexture.height * var0);
            int var2 = errorTexture.width * var0;
            boolean var3 = true;
            boolean var4 = var3;
            byte var5 = 8;
            int var6 = errorTexture.width / var5;

            for(int var7 = 0; var7 < var5 * var5; ++var7) {
               int var8 = var7 / var5;
               int var9 = var7 % var5;
               if (var8 > 0 && var9 == 0) {
                  var3 = !var3;
                  var4 = var3;
               }

               int var10 = var4 ? -16776961 : -1;
               var4 = !var4;

               for(int var11 = 0; var11 < var6; ++var11) {
                  for(int var12 = 0; var12 < var6; ++var12) {
                     var1.putInt((var8 * var6 + var11) * var2 + (var9 * var6 + var12) * var0, var10);
                  }
               }
            }

            var1.flip();
            GL11.glTexImage2D(3553, 0, 6408, errorTexture.width, errorTexture.height, 0, 6408, 5121, var1);
            MemoryUtil.memFree(var1);
         });
         s_sharedTextureTable.put("EngineErrorTexture.png", errorTexture);
      }

      return errorTexture;
   }

   private static void initEngineMipmapTextureLevel(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ByteBuffer var7 = MemoryUtil.memAlloc(var1 * var2 * 4);
      MemoryUtil.memSet(var7, 255);

      for(int var8 = 0; var8 < var1 * var2; ++var8) {
         var7.put((byte)(var3 & 255));
         var7.put((byte)(var4 & 255));
         var7.put((byte)(var5 & 255));
         var7.put((byte)(var6 & 255));
      }

      var7.flip();
      GL11.glTexImage2D(3553, var0, 6408, var1, var2, 0, 6408, 5121, var7);
      MemoryUtil.memFree(var7);
   }

   public static Texture getEngineMipmapTexture() {
      if (mipmap == null) {
         mipmap = new Texture(256, 256, "EngineMipmapTexture", 0);
         mipmap.dataid.setMinFilter(9984);
         RenderThread.invokeOnRenderContext(() -> {
            GL11.glBindTexture(3553, lastTextureID = mipmap.getID());
            GL11.glTexParameteri(3553, 10241, 9984);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 33085, 6);
            initEngineMipmapTextureLevel(0, mipmap.width, mipmap.height, 255, 0, 0, 255);
            initEngineMipmapTextureLevel(1, mipmap.width / 2, mipmap.height / 2, 0, 255, 0, 255);
            initEngineMipmapTextureLevel(2, mipmap.width / 4, mipmap.height / 4, 0, 0, 255, 255);
            initEngineMipmapTextureLevel(3, mipmap.width / 8, mipmap.height / 8, 255, 255, 0, 255);
            initEngineMipmapTextureLevel(4, mipmap.width / 16, mipmap.height / 16, 255, 0, 255, 255);
            initEngineMipmapTextureLevel(5, mipmap.width / 32, mipmap.height / 32, 0, 0, 0, 255);
            initEngineMipmapTextureLevel(6, mipmap.width / 64, mipmap.height / 64, 255, 255, 255, 255);
         });
      }

      return mipmap;
   }

   public static void clearTextures() {
      textures.clear();
   }

   public static Texture getSharedTexture(String var0) {
      byte var1 = 0;
      int var2 = var1 | (TextureID.bUseCompression ? 4 : 0);
      return getSharedTexture(var0, var2);
   }

   public static Texture getSharedTexture(String var0, int var1) {
      if (GameServer.bServer && !ServerGUI.isCreated()) {
         return null;
      } else {
         try {
            return getSharedTextureInternal(var0, var1);
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public static Texture trygetTexture(String var0) {
      if (GameServer.bServer && !ServerGUI.isCreated()) {
         return null;
      } else {
         Texture var1 = getSharedTexture(var0);
         if (var1 == null) {
            String var2 = "media/textures/" + var0;
            if (!var0.endsWith(".png")) {
               var2 = var2 + ".png";
            }

            var1 = (Texture)s_sharedTextureTable.get(var2);
            if (var1 != null) {
               return var1;
            }

            String var3 = ZomboidFileSystem.instance.getString(var2);
            if (!var3.equals(var2)) {
               byte var4 = 0;
               int var6 = var4 | (TextureID.bUseCompression ? 4 : 0);
               Texture.TextureAssetParams var5 = new Texture.TextureAssetParams();
               var5.flags = var6;
               var1 = (Texture)TextureAssetManager.instance.load(new AssetPath(var3), var5);
               BucketManager.Shared().AddTexture(var2, var1);
               setSharedTextureInternal(var2, var1);
            }
         }

         return var1;
      }
   }

   private static void onTextureFileChanged(String var0) {
      DebugLog.General.println("Texture.onTextureFileChanged> " + var0);
   }

   public static void onTexturePacksChanged() {
      nullTextures.clear();
      s_sharedTextureTable.clear();
   }

   private static void setSharedTextureInternal(String var0, Texture var1) {
      s_sharedTextureTable.put(var0, var1);
   }

   private static Texture getSharedTextureInternal(String var0, int var1) {
      if (GameServer.bServer && !ServerGUI.isCreated()) {
         return null;
      } else if (nullTextures.contains(var0)) {
         return null;
      } else {
         Texture var2 = (Texture)s_sharedTextureTable.get(var0);
         if (var2 != null) {
            return var2;
         } else {
            Texture var4;
            Texture.TextureAssetParams var6;
            if (!var0.endsWith(".txt")) {
               String var3 = var0;
               if (var0.endsWith(".pcx") || var0.endsWith(".png")) {
                  var3 = var0.substring(0, var0.lastIndexOf("."));
               }

               var3 = var3.substring(var0.lastIndexOf("/") + 1);
               var4 = TexturePackPage.getTexture(var3);
               if (var4 != null) {
                  setSharedTextureInternal(var0, var4);
                  return var4;
               }

               FileSystem.SubTexture var5 = (FileSystem.SubTexture)GameWindow.texturePackTextures.get(var3);
               if (var5 != null) {
                  var6 = new Texture.TextureAssetParams();
                  var6.subTexture = var5;
                  String var15 = "@pack/" + var5.m_pack_name + "/" + var5.m_page_name + "/" + var5.m_info.name;
                  Texture var8 = (Texture)TextureAssetManager.instance.load(new AssetPath(var15), var6);
                  if (var8 == null) {
                     nullTextures.add(var0);
                  } else {
                     setSharedTextureInternal(var0, var8);
                  }

                  return var8;
               }
            }

            if (TexturePackPage.subTextureMap.containsKey(var0)) {
               return (Texture)TexturePackPage.subTextureMap.get(var0);
            } else {
               FileSystem.SubTexture var9 = (FileSystem.SubTexture)GameWindow.texturePackTextures.get(var0);
               if (var9 != null) {
                  Texture.TextureAssetParams var11 = new Texture.TextureAssetParams();
                  var11.subTexture = var9;
                  String var13 = "@pack/" + var9.m_pack_name + "/" + var9.m_page_name + "/" + var9.m_info.name;
                  Texture var14 = (Texture)TextureAssetManager.instance.load(new AssetPath(var13), var11);
                  if (var14 == null) {
                     nullTextures.add(var0);
                  } else {
                     setSharedTextureInternal(var0, var14);
                  }

                  return var14;
               } else if (BucketManager.Shared().HasTexture(var0)) {
                  var4 = BucketManager.Shared().getTexture(var0);
                  setSharedTextureInternal(var0, var4);
                  return var4;
               } else if (StringUtils.endsWithIgnoreCase(var0, ".pcx")) {
                  nullTextures.add(var0);
                  return null;
               } else if (var0.lastIndexOf(46) == -1) {
                  nullTextures.add(var0);
                  return null;
               } else {
                  String var10 = ZomboidFileSystem.instance.getString(var0);
                  boolean var12 = var10 != var0;
                  if (!var12 && !(new File(var10)).exists()) {
                     nullTextures.add(var0);
                     return null;
                  } else {
                     var6 = new Texture.TextureAssetParams();
                     var6.flags = var1;
                     Texture var7 = (Texture)TextureAssetManager.instance.load(new AssetPath(var10), var6);
                     BucketManager.Shared().AddTexture(var0, var7);
                     setSharedTextureInternal(var0, var7);
                     return var7;
                  }
               }
            }
         }
      }
   }

   public static Texture getSharedTexture(String var0, String var1) {
      if (BucketManager.Shared().HasTexture(var0 + var1)) {
         return BucketManager.Shared().getTexture(var0 + var1);
      } else {
         Texture var2 = new Texture(var0, var1);
         BucketManager.Shared().AddTexture(var0 + var1, var2);
         return var2;
      }
   }

   public static Texture getSharedTexture(String var0, int[] var1, String var2) {
      if (BucketManager.Shared().HasTexture(var0 + var2)) {
         return BucketManager.Shared().getTexture(var0 + var2);
      } else {
         Texture var3 = new Texture(var0, var1);
         BucketManager.Shared().AddTexture(var0 + var2, var3);
         return var3;
      }
   }

   public static Texture getTexture(String var0) {
      if (!var0.contains(".txt")) {
         String var1 = var0.replace(".png", "");
         var1 = var1.replace(".pcx", "");
         var1 = var1.substring(var0.lastIndexOf("/") + 1);
         Texture var2 = TexturePackPage.getTexture(var1);
         if (var2 != null) {
            return var2;
         }
      }

      if (BucketManager.Active().HasTexture(var0)) {
         return BucketManager.Active().getTexture(var0);
      } else {
         try {
            Texture var4 = new Texture(var0);
            BucketManager.Active().AddTexture(var0, var4);
            return var4;
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public static Texture getSteamAvatar(long var0) {
      if (steamAvatarMap.containsKey(var0)) {
         return (Texture)steamAvatarMap.get(var0);
      } else {
         TextureID var2 = TextureID.createSteamAvatar(var0);
         if (var2 == null) {
            return null;
         } else {
            Texture var3 = new Texture(var2, "SteamAvatar" + SteamUtils.convertSteamIDToString(var0));
            steamAvatarMap.put(var0, var3);
            return var3;
         }
      }
   }

   public static void steamAvatarChanged(long var0) {
      Texture var2 = (Texture)steamAvatarMap.get(var0);
      if (var2 != null) {
         steamAvatarMap.remove(var0);
      }

   }

   public static void forgetTexture(String var0) {
      BucketManager.Shared().forgetTexture(var0);
      s_sharedTextureTable.remove(var0);
   }

   public static void reload(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         Texture var1 = (Texture)s_sharedTextureTable.get(var0);
         if (var1 == null) {
            var1 = (Texture)Type.tryCastTo((Asset)TextureAssetManager.instance.getAssetTable().get(var0), Texture.class);
            if (var1 == null) {
               return;
            }
         }

         var1.reloadFromFile(var0);
      }
   }

   public static int[] flipPixels(int[] var0, int var1, int var2) {
      int[] var3 = null;
      if (var0 != null) {
         var3 = new int[var1 * var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
               var3[(var2 - var4 - 1) * var1 + var5] = var0[var4 * var1 + var5];
            }
         }
      }

      return var3;
   }

   public void reloadFromFile(String var1) {
      if (this.dataid != null) {
         TextureID.TextureIDAssetParams var5 = new TextureID.TextureIDAssetParams();
         var5.flags = this.dataid.flags;
         this.dataid.getAssetManager().reload(this.dataid, var5);
      } else if (var1 != null && !var1.isEmpty()) {
         File var2 = new File(var1);
         if (var2.exists()) {
            try {
               ImageData var3 = new ImageData(var2.getAbsolutePath());
               if (var3.getWidthHW() != this.getWidthHW() || var3.getHeightHW() != this.getHeightHW()) {
                  return;
               }

               RenderThread.invokeOnRenderContext(var3, (var1x) -> {
                  GL11.glBindTexture(3553, lastTextureID = this.dataid.id);
                  short var2 = 6408;
                  GL11.glTexImage2D(3553, 0, var2, this.getWidthHW(), this.getHeightHW(), 0, 6408, 5121, var1x.getData().getBuffer());
               });
            } catch (Throwable var4) {
               ExceptionLogger.logException(var4, var1);
            }

         }
      }
   }

   public void bind() {
      this.bind(3553);
   }

   public void bind(int var1) {
      if (!this.isDestroyed() && this.isValid() && this.isReady()) {
         if (this.bindAlways) {
            this.dataid.bindalways();
         } else {
            this.dataid.bind();
         }

      } else {
         getErrorTexture().bind(var1);
      }
   }

   public void copyMaskRegion(Texture var1, int var2, int var3, int var4, int var5) {
      if (var1.getMask() != null) {
         new Mask(var1, this, var2, var3, var4, var5);
      }
   }

   public void createMask() {
      new Mask(this);
   }

   public void createMask(boolean[] var1) {
      new Mask(this, var1);
   }

   public void createMask(BooleanGrid var1) {
      new Mask(this, var1);
   }

   public void createMask(WrappedBuffer var1) {
      new Mask(this, var1);
   }

   public void destroy() {
      if (!this.destroyed) {
         if (this.dataid != null && --this.dataid.referenceCount == 0) {
            if (lastTextureID == this.dataid.id) {
               lastTextureID = -1;
            }

            this.dataid.destroy();
         }

         this.destroyed = true;
      }
   }

   public boolean equals(Texture var1) {
      return var1.xStart == this.xStart && var1.xEnd == this.xEnd && var1.yStart == this.yStart && var1.yEnd == this.yEnd && var1.width == this.width && var1.height == this.height && var1.solid == this.solid && (this.dataid == null || var1.dataid == null || var1.dataid.pathFileName == null || this.dataid.pathFileName == null || var1.dataid.pathFileName.equals(this.dataid.pathFileName));
   }

   public WrappedBuffer getData() {
      return this.dataid.getData();
   }

   public void setData(ByteBuffer var1) {
      this.dataid.setData(var1);
   }

   public int getHeight() {
      if (!this.isReady() && this.height <= 0 && !(this instanceof SmartTexture)) {
         this.syncReadSize();
      }

      return this.height;
   }

   public void setHeight(int var1) {
      this.height = var1;
   }

   public int getHeightHW() {
      if (!this.isReady() && this.height <= 0 && !(this instanceof SmartTexture)) {
         this.syncReadSize();
      }

      return this.dataid.heightHW;
   }

   public int getHeightOrig() {
      return this.heightOrig == 0 ? this.getHeight() : this.heightOrig;
   }

   public int getID() {
      return this.dataid.id;
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask var1) {
      this.mask = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      if (var1 != null) {
         if (var1.equals(this.name)) {
            if (!textures.containsKey(var1)) {
               textures.put(var1, this);
            }

         } else {
            if (textures.containsKey(var1)) {
            }

            if (textures.containsKey(this.name)) {
               textures.remove(this.name);
            }

            this.name = var1;
            textures.put(var1, this);
         }
      }
   }

   public TextureID getTextureId() {
      return this.dataid;
   }

   public boolean getUseAlphaChannel() {
      return !this.solid;
   }

   public void setUseAlphaChannel(boolean var1) {
      this.dataid.solid = this.solid = !var1;
   }

   public int getWidth() {
      if (!this.isReady() && this.width <= 0 && !(this instanceof SmartTexture)) {
         this.syncReadSize();
      }

      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public int getWidthHW() {
      if (!this.isReady() && this.width <= 0 && !(this instanceof SmartTexture)) {
         this.syncReadSize();
      }

      return this.dataid.widthHW;
   }

   public int getWidthOrig() {
      return this.widthOrig == 0 ? this.getWidth() : this.widthOrig;
   }

   public float getXEnd() {
      return this.xEnd;
   }

   public float getXStart() {
      return this.xStart;
   }

   public float getYEnd() {
      return this.yEnd;
   }

   public float getYStart() {
      return this.yStart;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public void setOffsetX(int var1) {
      this.offsetX = (float)var1;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public void setOffsetY(int var1) {
      this.offsetY = (float)var1;
   }

   public boolean isCollisionable() {
      return this.mask != null;
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public boolean isSolid() {
      return this.solid;
   }

   public boolean isValid() {
      return this.dataid != null;
   }

   public void makeTransp(int var1, int var2, int var3) {
      this.setAlphaForeach(var1, var2, var3, 0);
   }

   public void render(float var1, float var2, float var3, float var4) {
      this.render(var1, var2, var3, var4, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
   }

   public void render(float var1, float var2) {
      this.render(var1, var2, (float)this.width, (float)this.height, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
   }

   public void render(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Consumer var9) {
      var1 += this.offsetX;
      var2 += this.offsetY;
      SpriteRenderer.instance.render(this, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void render(ObjectRenderEffects var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      float var11 = this.offsetX + var2;
      float var12 = this.offsetY + var3;
      objRen.x1 = (double)var11 + var1.x1 * (double)var4;
      objRen.y1 = (double)var12 + var1.y1 * (double)var5;
      objRen.x2 = (double)(var11 + var4) + var1.x2 * (double)var4;
      objRen.y2 = (double)var12 + var1.y2 * (double)var5;
      objRen.x3 = (double)(var11 + var4) + var1.x3 * (double)var4;
      objRen.y3 = (double)(var12 + var5) + var1.y3 * (double)var5;
      objRen.x4 = (double)var11 + var1.x4 * (double)var4;
      objRen.y4 = (double)(var12 + var5) + var1.y4 * (double)var5;
      SpriteRenderer.instance.render(this, objRen.x1, objRen.y1, objRen.x2, objRen.y2, objRen.x3, objRen.y3, objRen.x4, objRen.y4, var6, var7, var8, var9, var10);
   }

   public void rendershader2(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8, float var9, float var10, float var11, float var12) {
      if (var12 != 0.0F) {
         float var13 = (float)var5 / (float)this.getWidthHW();
         float var14 = (float)var6 / (float)this.getHeightHW();
         float var15 = (float)(var5 + var7) / (float)this.getWidthHW();
         float var16 = (float)(var6 + var8) / (float)this.getHeightHW();
         if (this.flip) {
            float var17 = var15;
            var15 = var13;
            var13 = var17;
            var1 += (float)this.widthOrig - this.offsetX - (float)this.width;
            var2 += this.offsetY;
         } else {
            var1 += this.offsetX;
            var2 += this.offsetY;
         }

         if (var9 > 1.0F) {
            var9 = 1.0F;
         }

         if (var10 > 1.0F) {
            var10 = 1.0F;
         }

         if (var11 > 1.0F) {
            var11 = 1.0F;
         }

         if (var12 > 1.0F) {
            var12 = 1.0F;
         }

         if (var9 < 0.0F) {
            var9 = 0.0F;
         }

         if (var10 < 0.0F) {
            var10 = 0.0F;
         }

         if (var11 < 0.0F) {
            var11 = 0.0F;
         }

         if (var12 < 0.0F) {
            var12 = 0.0F;
         }

         if (!(var1 + var3 <= 0.0F)) {
            if (!(var2 + var4 <= 0.0F)) {
               if (!(var1 >= (float)Core.getInstance().getScreenWidth())) {
                  if (!(var2 >= (float)Core.getInstance().getScreenHeight())) {
                     lr = var9;
                     lg = var10;
                     lb = var11;
                     la = var12;
                     SpriteRenderer.instance.render(this, var1, var2, var3, var4, var9, var10, var11, var12, var13, var16, var15, var16, var15, var14, var13, var14);
                  }
               }
            }
         }
      }
   }

   public void renderdiamond(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8) {
      SpriteRenderer.instance.render((Texture)null, var1, var2, var1 + var3 / 2.0F, var2 - var4 / 2.0F, var1 + var3, var2, var1 + var3 / 2.0F, var2 + var4 / 2.0F, var5, var6, var7, var8);
   }

   public void renderwallnw(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      lr = -1.0F;
      lg = -1.0F;
      lb = -1.0F;
      la = -1.0F;
      if (this.flip) {
         var1 += (float)this.widthOrig - this.offsetX - (float)this.width;
         var2 += this.offsetY;
      } else {
         var1 += this.offsetX;
         var2 += this.offsetY;
      }

      int var11 = Core.TileScale;
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingOldDebug.getValue()) {
         var8 = -65536;
         var6 = -65536;
         var7 = -65536;
         var5 = -65536;
      }

      float var12 = var1 - var3 / 2.0F - 0.0F;
      float var13 = var2 - (float)(96 * var11) + var4 / 2.0F - 1.0F - 0.0F;
      float var14 = var1 + 0.0F;
      float var15 = var2 - (float)(96 * var11) - 2.0F - 0.0F;
      float var16 = var1 + 0.0F;
      float var17 = var2 + 4.0F + 0.0F;
      float var18 = var1 - var3 / 2.0F - 0.0F;
      float var19 = var2 + var4 / 2.0F + 4.0F + 0.0F;
      SpriteRenderer.instance.render(this, var12, var13, var14, var15, var16, var17, var18, var19, var8, var7, var5, var6);
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingOldDebug.getValue()) {
         var10 = -256;
         var9 = -256;
         var7 = -256;
         var5 = -256;
      }

      var12 = var1 - 0.0F;
      var13 = var2 - (float)(96 * var11) - 0.0F;
      var14 = var1 + var3 / 2.0F + 0.0F;
      var15 = var2 - (float)(96 * var11) + var4 / 2.0F - 0.0F;
      var16 = var1 + var3 / 2.0F + 0.0F;
      var17 = var2 + var4 / 2.0F + 5.0F + 0.0F;
      var18 = var1 - 0.0F;
      var19 = var2 + 5.0F + 0.0F;
      SpriteRenderer.instance.render(this, var12, var13, var14, var15, var16, var17, var18, var19, var7, var10, var9, var5);
   }

   public void renderwallw(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8) {
      lr = -1.0F;
      lg = -1.0F;
      lb = -1.0F;
      la = -1.0F;
      if (this.flip) {
         var1 += (float)this.widthOrig - this.offsetX - (float)this.width;
         var2 += this.offsetY;
      } else {
         var1 += this.offsetX;
         var2 += this.offsetY;
      }

      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingOldDebug.getValue()) {
         var6 = -16711936;
         var5 = -16711936;
         var8 = -16728064;
         var7 = -16728064;
      }

      int var9 = Core.TileScale;
      float var10 = var1 - var3 / 2.0F - 0.0F;
      float var11 = var2 - (float)(96 * var9) + var4 / 2.0F - 1.0F - 0.0F;
      float var12 = var1 + (float)var9 + 0.0F;
      float var13 = var2 - (float)(96 * var9) - 3.0F - 0.0F;
      float var14 = var1 + (float)var9 + 0.0F;
      float var15 = var2 + 3.0F + 0.0F;
      float var16 = var1 - var3 / 2.0F - 0.0F;
      float var17 = var2 + var4 / 2.0F + 4.0F + 0.0F;
      SpriteRenderer.instance.render(this, var10, var11, var12, var13, var14, var15, var16, var17, var8, var7, var5, var6);
   }

   public void renderwalln(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8) {
      lr = -1.0F;
      lg = -1.0F;
      lb = -1.0F;
      la = -1.0F;
      if (this.flip) {
         var1 += (float)this.widthOrig - this.offsetX - (float)this.width;
         var2 += this.offsetY;
      } else {
         var1 += this.offsetX;
         var2 += this.offsetY;
      }

      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingOldDebug.getValue()) {
         var6 = -16776961;
         var5 = -16776961;
         var8 = -16777024;
         var7 = -16777024;
      }

      int var9 = Core.TileScale;
      float var10 = var1 - 6.0F - 0.0F;
      float var11 = var2 - (float)(96 * var9) - 3.0F - 0.0F;
      float var12 = var1 + var3 / 2.0F + 0.0F;
      float var13 = var2 - (float)(96 * var9) + var4 / 2.0F - 0.0F;
      float var14 = var1 + var3 / 2.0F + 0.0F;
      float var15 = var2 + var4 / 2.0F + 5.0F + 0.0F;
      float var16 = var1 - 6.0F - 0.0F;
      float var17 = var2 + 2.0F + 0.0F;
      SpriteRenderer.instance.render(this, var10, var11, var12, var13, var14, var15, var16, var17, var7, var8, var6, var5);
   }

   public void renderstrip(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8, Consumer var9) {
      try {
         if (var8 <= 0.0F) {
            return;
         }

         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         if (var6 > 1.0F) {
            var6 = 1.0F;
         }

         if (var7 > 1.0F) {
            var7 = 1.0F;
         }

         if (var8 > 1.0F) {
            var8 = 1.0F;
         }

         if (var5 < 0.0F) {
            var5 = 0.0F;
         }

         if (var6 < 0.0F) {
            var6 = 0.0F;
         }

         if (var7 < 0.0F) {
            var7 = 0.0F;
         }

         if (var8 < 0.0F) {
            var8 = 0.0F;
         }

         float var10 = this.getXStart();
         float var11 = this.getYStart();
         float var12 = this.getXEnd();
         float var13 = this.getYEnd();
         if (this.flip) {
            var1 = (int)((float)var1 + ((float)this.widthOrig - this.offsetX - (float)this.width));
            var2 = (int)((float)var2 + this.offsetY);
         } else {
            var1 = (int)((float)var1 + this.offsetX);
            var2 = (int)((float)var2 + this.offsetY);
         }

         SpriteRenderer.instance.renderi(this, var1, var2, var3, var4, var5, var6, var7, var8, var9);
      } catch (Exception var15) {
         bDoingQuad = false;
         Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var15);
      }

   }

   public void setAlphaForeach(int var1, int var2, int var3, int var4) {
      ImageData var5 = this.getTextureId().getImageData();
      if (var5 != null) {
         var5.makeTransp((byte)var1, (byte)var2, (byte)var3, (byte)var4);
      } else {
         WrappedBuffer var6 = this.getData();
         this.setData(ImageUtils.makeTransp(var6.getBuffer(), var1, var2, var3, var4, this.getWidthHW(), this.getHeightHW()));
         var6.dispose();
      }

      AlphaColorIndex var7 = new AlphaColorIndex(var1, var2, var3, var4);
      if (this.dataid.alphaList == null) {
         this.dataid.alphaList = new ArrayList();
      }

      if (!this.dataid.alphaList.contains(var7)) {
         this.dataid.alphaList.add(var7);
      }

   }

   public void setCustomizedTexture() {
      this.dataid.pathFileName = null;
   }

   public void setNameOnly(String var1) {
      this.name = var1;
   }

   public void setRegion(int var1, int var2, int var3, int var4) {
      if (var1 >= 0 && var1 <= this.getWidthHW()) {
         if (var2 >= 0 && var2 <= this.getHeightHW()) {
            if (var3 > 0) {
               if (var4 > 0) {
                  if (var3 + var1 > this.getWidthHW()) {
                     var3 = this.getWidthHW() - var1;
                  }

                  if (var4 > this.getHeightHW()) {
                     var4 = this.getHeightHW() - var2;
                  }

                  this.xStart = (float)var1 / (float)this.getWidthHW();
                  this.yStart = (float)var2 / (float)this.getHeightHW();
                  this.xEnd = (float)(var1 + var3) / (float)this.getWidthHW();
                  this.yEnd = (float)(var2 + var4) / (float)this.getHeightHW();
                  this.width = var3;
                  this.height = var4;
               }
            }
         }
      }
   }

   public Texture splitIcon() {
      if (this.splitIconTex == null) {
         if (!this.dataid.isReady()) {
            this.splitIconTex = new Texture();
            this.splitIconTex.name = this.name + "_Icon";
            this.splitIconTex.dataid = this.dataid;
            ++this.splitIconTex.dataid.referenceCount;
            this.splitIconTex.splitX = this.splitX;
            this.splitIconTex.splitY = this.splitY;
            this.splitIconTex.splitW = this.splitW;
            this.splitIconTex.splitH = this.splitH;
            this.splitIconTex.width = this.width;
            this.splitIconTex.height = this.height;
            this.splitIconTex.offsetX = 0.0F;
            this.splitIconTex.offsetY = 0.0F;
            this.splitIconTex.widthOrig = 0;
            this.splitIconTex.heightOrig = 0;
            this.splitIconTex.addDependency(this.dataid);
            setSharedTextureInternal(this.splitIconTex.name, this.splitIconTex);
            return this.splitIconTex;
         }

         this.splitIconTex = new Texture(this.getTextureId(), this.name + "_Icon");
         float var1 = this.xStart * (float)this.getWidthHW();
         float var2 = this.yStart * (float)this.getHeightHW();
         float var3 = this.xEnd * (float)this.getWidthHW() - var1;
         float var4 = this.yEnd * (float)this.getHeightHW() - var2;
         this.splitIconTex.setRegion((int)var1, (int)var2, (int)var3, (int)var4);
         this.splitIconTex.offsetX = 0.0F;
         this.splitIconTex.offsetY = 0.0F;
         setSharedTextureInternal(this.name + "_Icon", this.splitIconTex);
      }

      return this.splitIconTex;
   }

   public Texture split(int var1, int var2, int var3, int var4) {
      Texture var5 = new Texture(this.getTextureId(), this.name + "_" + var1 + "_" + var2);
      this.splitX = var1;
      this.splitY = var2;
      this.splitW = var3;
      this.splitH = var4;
      if (this.getTextureId().isReady()) {
         var5.setRegion(var1, var2, var3, var4);
      } else {
         assert false;
      }

      return var5;
   }

   public Texture split(String var1, int var2, int var3, int var4, int var5) {
      Texture var6 = new Texture(this.getTextureId(), var1);
      var6.setRegion(var2, var3, var4, var5);
      return var6;
   }

   public Texture[] split(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      Texture[] var9 = new Texture[var3 * var4];

      for(int var10 = 0; var10 < var3; ++var10) {
         for(int var11 = 0; var11 < var4; ++var11) {
            var9[var11 + var10 * var4] = new Texture(this.getTextureId(), this.name + "_" + var3 + "_" + var4);
            var9[var11 + var10 * var4].setRegion(var1 + var11 * var5 + var7 * var11, var2 + var10 * var6 + var8 * var10, var5, var6);
            var9[var11 + var10 * var4].copyMaskRegion(this, var1 + var11 * var5 + var7 * var11, var2 + var10 * var6 + var8 * var10, var5, var6);
         }
      }

      return var9;
   }

   public Texture[][] split2D(int[] var1, int[] var2) {
      if (var1 != null && var2 != null) {
         Texture[][] var3 = new Texture[var1.length][var2.length];
         float var8 = 0.0F;
         float var6 = 0.0F;
         float var5 = 0.0F;

         for(int var9 = 0; var9 < var2.length; ++var9) {
            var6 += var8;
            var8 = (float)var2[var9] / (float)this.getHeightHW();
            var5 = 0.0F;

            for(int var10 = 0; var10 < var1.length; ++var10) {
               float var7 = (float)var1[var10] / (float)this.getWidthHW();
               Texture var4 = var3[var10][var9] = new Texture(this);
               var4.width = var1[var10];
               var4.height = var2[var9];
               var4.xStart = var5;
               var4.xEnd = var5 += var7;
               var4.yStart = var6;
               var4.yEnd = var6 + var8;
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{ name:\"" + this.name + "\", w:" + this.getWidth() + ", h:" + this.getHeight() + " }";
   }

   public void saveMask(String var1) {
      this.mask.save(var1);
   }

   public void save(String var1) {
      GL11.glPixelStorei(3333, 1);
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = true;
      ByteBuffer var7 = null;
      var7 = this.getData().getBuffer();
      int[] var8 = new int[var2 * var3];
      File var10 = new File(var1);

      for(int var11 = 0; var11 < var8.length; ++var11) {
         int var9 = var11 * 4;
         var8[var11] = (var7.get(var9 + 3) & 255) << 24 | (var7.get(var9) & 255) << 16 | (var7.get(var9 + 1) & 255) << 8 | (var7.get(var9 + 2) & 255) << 0;
      }

      var7 = null;
      BufferedImage var14 = new BufferedImage(var2, var3, 2);
      var14.setRGB(0, 0, var2, var3, var8, 0, var2);

      try {
         ImageIO.write(var14, "png", var10);
      } catch (IOException var13) {
         var13.printStackTrace();
      }

      GL13.glActiveTexture(33984);
   }

   public void loadMaskRegion(ByteBuffer var1) {
      if (var1 != null) {
         this.mask = new Mask();
         this.mask.mask = new BooleanGrid(this.width, this.height);
         this.mask.mask.LoadFromByteBuffer(var1);
      }
   }

   public void saveMaskRegion(ByteBuffer var1) {
      if (var1 != null) {
         this.mask.mask.PutToByteBuffer(var1);
      }
   }

   public int getRealWidth() {
      return this.realWidth;
   }

   public void setRealWidth(int var1) {
      this.realWidth = var1;
   }

   public int getRealHeight() {
      return this.realHeight;
   }

   public void setRealHeight(int var1) {
      this.realHeight = var1;
   }

   public Vector2 getUVScale(Vector2 var1) {
      var1.set(1.0F, 1.0F);
      if (this.dataid == null) {
         return var1;
      } else {
         if (this.dataid.heightHW != this.dataid.height || this.dataid.widthHW != this.dataid.width) {
            var1.x = (float)this.dataid.width / (float)this.dataid.widthHW;
            var1.y = (float)this.dataid.height / (float)this.dataid.heightHW;
         }

         return var1;
      }
   }

   private void syncReadSize() {
      PNGSize var1 = (PNGSize)pngSize.get();
      var1.readSize(this.name);
      this.width = var1.width;
      this.height = var1.height;
   }

   public AssetType getType() {
      return ASSET_TYPE;
   }

   public void onBeforeReady() {
      if (this.assetParams != null) {
         this.assetParams.subTexture = null;
         this.assetParams = null;
      }

      this.solid = this.dataid.solid;
      if (this.splitX == -1) {
         this.width = this.dataid.width;
         this.height = this.dataid.height;
         this.xEnd = (float)this.width / (float)this.dataid.widthHW;
         this.yEnd = (float)this.height / (float)this.dataid.heightHW;
         if (this.dataid.mask != null) {
            this.createMask(this.dataid.mask);
         }
      } else {
         this.setRegion(this.splitX, this.splitY, this.splitW, this.splitH);
         if (this.dataid.mask != null) {
            this.mask = new Mask(this.dataid.mask, this.dataid.width, this.dataid.height, this.splitX, this.splitY, this.splitW, this.splitH);
         }
      }

   }

   public static void collectAllIcons(HashMap var0, HashMap var1) {
      Iterator var2 = s_sharedTextureTable.entrySet().iterator();

      while(true) {
         Entry var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (Entry)var2.next();
         } while(!((String)var3.getKey()).startsWith("media/ui/Container_") && !((String)var3.getKey()).startsWith("Item_"));

         String var4 = "";
         if (((String)var3.getKey()).startsWith("Item_")) {
            var4 = ((String)var3.getKey()).replaceFirst("Item_", "");
         } else if (((String)var3.getKey()).startsWith("media/ui/Container_")) {
            var4 = ((String)var3.getKey()).replaceFirst("media/ui/Container_", "");
            var4 = var4.replaceAll("\\.png", "");
            String var10000 = var4.toLowerCase();
            DebugLog.log("Adding " + var10000 + ", value = " + (String)var3.getKey());
         }

         var0.put(var4.toLowerCase(), var4);
         var1.put(var4.toLowerCase(), (String)var3.getKey());
      }
   }

   public static final class TextureAssetParams extends AssetManager.AssetParams {
      int flags = 0;
      FileSystem.SubTexture subTexture;
   }

   public static enum PZFileformat {
      PNG,
      DDS;

      // $FF: synthetic method
      private static Texture.PZFileformat[] $values() {
         return new Texture.PZFileformat[]{PNG, DDS};
      }
   }
}
