package zombie.core.fonts;

import gnu.trove.list.array.TShortArrayList;
import gnu.trove.map.hash.TShortObjectHashMap;
import gnu.trove.procedure.TShortObjectProcedure;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import zombie.ZomboidFileSystem;
import zombie.asset.Asset;
import zombie.asset.AssetStateObserver;
import zombie.core.Color;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.util.StringUtils;

public final class AngelCodeFont implements Font, AssetStateObserver {
   private static final int DISPLAY_LIST_CACHE_SIZE = 200;
   private static final int MAX_CHAR = 255;
   private int baseDisplayListID = -1;
   public AngelCodeFont.CharDef[] chars;
   private boolean displayListCaching = false;
   private AngelCodeFont.DisplayList eldestDisplayList;
   private int eldestDisplayListID;
   private final LinkedHashMap displayLists = new LinkedHashMap(200, 1.0F, true) {
      protected boolean removeEldestEntry(Entry var1) {
         AngelCodeFont.this.eldestDisplayList = (AngelCodeFont.DisplayList)var1.getValue();
         AngelCodeFont.this.eldestDisplayListID = AngelCodeFont.this.eldestDisplayList.id;
         return false;
      }
   };
   private Texture fontImage;
   private int lineHeight;
   private HashMap pages = new HashMap();
   private File fntFile;
   public static int xoff = 0;
   public static int yoff = 0;
   public static Color curCol = null;
   public static float curR = 0.0F;
   public static float curG = 0.0F;
   public static float curB = 0.0F;
   public static float curA = 0.0F;
   private static float s_scale = 0.0F;
   private static char[] data = new char[256];

   public AngelCodeFont(String var1, Texture var2) throws FileNotFoundException {
      this.fontImage = var2;
      String var3 = var1;
      FileInputStream var4 = new FileInputStream(new File(var1));
      if (var1.startsWith("/")) {
         var3 = var1.substring(1);
      }

      int var5;
      while((var5 = var3.indexOf("\\")) != -1) {
         String var10000 = var3.substring(0, var5);
         var3 = var10000 + "/" + var3.substring(var5 + 1);
      }

      this.parseFnt(var4);
   }

   public AngelCodeFont(String var1, String var2) throws FileNotFoundException {
      if (!StringUtils.isNullOrWhitespace(var2)) {
         byte var3 = 0;
         int var6 = var3 | (TextureID.bUseCompression ? 4 : 0);
         this.fontImage = Texture.getSharedTexture(var2, var6);
         if (this.fontImage != null && !this.fontImage.isReady()) {
            this.fontImage.getObserverCb().add(this);
         }
      }

      String var7 = var1;
      FileInputStream var4 = null;
      if (var1.startsWith("/")) {
         var7 = var1.substring(1);
      }

      int var5;
      while((var5 = var7.indexOf("\\")) != -1) {
         String var10000 = var7.substring(0, var5);
         var7 = var10000 + "/" + var7.substring(var5 + 1);
      }

      this.fntFile = new File(ZomboidFileSystem.instance.getString(var7));
      var4 = new FileInputStream(ZomboidFileSystem.instance.getString(var7));
      this.parseFnt(var4);
   }

   public void drawString(float var1, float var2, String var3) {
      this.drawString(var1, var2, var3, Color.white);
   }

   public void drawString(float var1, float var2, String var3, Color var4) {
      this.drawString(var1, var2, var3, var4, 0, var3.length() - 1);
   }

   public void drawString(float var1, float var2, String var3, float var4, float var5, float var6, float var7) {
      this.drawString(var1, var2, var3, var4, var5, var6, var7, 0, var3.length() - 1);
   }

   public void drawString(float var1, float var2, float var3, String var4, float var5, float var6, float var7, float var8) {
      this.drawString(var1, var2, var3, var4, var5, var6, var7, var8, 0, var4.length() - 1);
   }

   public void drawString(float var1, float var2, String var3, Color var4, int var5, int var6) {
      xoff = (int)var1;
      yoff = (int)var2;
      curR = var4.r;
      curG = var4.g;
      curB = var4.b;
      curA = var4.a;
      s_scale = 0.0F;
      Texture.lr = var4.r;
      Texture.lg = var4.g;
      Texture.lb = var4.b;
      Texture.la = var4.a;
      if (this.displayListCaching && var5 == 0 && var6 == var3.length() - 1) {
         AngelCodeFont.DisplayList var7 = (AngelCodeFont.DisplayList)this.displayLists.get(var3);
         if (var7 != null) {
            GL11.glCallList(var7.id);
         } else {
            var7 = new AngelCodeFont.DisplayList();
            var7.text = var3;
            int var8 = this.displayLists.size();
            if (var8 < 200) {
               var7.id = this.baseDisplayListID + var8;
            } else {
               var7.id = this.eldestDisplayListID;
               this.displayLists.remove(this.eldestDisplayList.text);
            }

            this.displayLists.put(var3, var7);
            GL11.glNewList(var7.id, 4865);
            this.render(var3, var5, var6);
            GL11.glEndList();
         }
      } else {
         this.render(var3, var5, var6);
      }

   }

   public void drawString(float var1, float var2, String var3, float var4, float var5, float var6, float var7, int var8, int var9) {
      this.drawString(var1, var2, 0.0F, var3, var4, var5, var6, var7, var8, var9);
   }

   public void drawString(float var1, float var2, float var3, String var4, float var5, float var6, float var7, float var8, int var9, int var10) {
      xoff = (int)var1;
      yoff = (int)var2;
      curR = var5;
      curG = var6;
      curB = var7;
      curA = var8;
      s_scale = var3;
      Texture.lr = var5;
      Texture.lg = var6;
      Texture.lb = var7;
      Texture.la = var8;
      if (this.displayListCaching && var9 == 0 && var10 == var4.length() - 1) {
         AngelCodeFont.DisplayList var11 = (AngelCodeFont.DisplayList)this.displayLists.get(var4);
         if (var11 != null) {
            GL11.glCallList(var11.id);
         } else {
            var11 = new AngelCodeFont.DisplayList();
            var11.text = var4;
            int var12 = this.displayLists.size();
            if (var12 < 200) {
               var11.id = this.baseDisplayListID + var12;
            } else {
               var11.id = this.eldestDisplayListID;
               this.displayLists.remove(this.eldestDisplayList.text);
            }

            this.displayLists.put(var4, var11);
            GL11.glNewList(var11.id, 4865);
            this.render(var4, var9, var10);
            GL11.glEndList();
         }
      } else {
         this.render(var4, var9, var10);
      }

   }

   public int getHeight(String var1) {
      AngelCodeFont.DisplayList var2 = null;
      if (this.displayListCaching) {
         var2 = (AngelCodeFont.DisplayList)this.displayLists.get(var1);
         if (var2 != null && var2.height != null) {
            return var2.height.intValue();
         }
      }

      int var3 = 1;
      int var4 = 0;

      for(int var5 = 0; var5 < var1.length(); ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 == '\n') {
            ++var3;
            var4 = 0;
         } else if (var6 != ' ' && var6 < this.chars.length) {
            AngelCodeFont.CharDef var7 = this.chars[var6];
            if (var7 != null) {
               var4 = Math.max(var7.height + var7.yoffset, var4);
            }
         }
      }

      var4 = var3 * this.getLineHeight();
      if (var2 != null) {
         var2.height = new Short((short)var4);
      }

      return var4;
   }

   public int getLineHeight() {
      return this.lineHeight;
   }

   public int getWidth(String var1) {
      return this.getWidth(var1, 0, var1.length() - 1, false);
   }

   public int getWidth(String var1, boolean var2) {
      return this.getWidth(var1, 0, var1.length() - 1, var2);
   }

   public int getWidth(String var1, int var2, int var3) {
      return this.getWidth(var1, var2, var3, false);
   }

   public int getWidth(String var1, int var2, int var3, boolean var4) {
      AngelCodeFont.DisplayList var5 = null;
      if (this.displayListCaching && var2 == 0 && var3 == var1.length() - 1) {
         var5 = (AngelCodeFont.DisplayList)this.displayLists.get(var1);
         if (var5 != null && var5.width != null) {
            return var5.width.intValue();
         }
      }

      int var6 = var3 - var2 + 1;
      int var7 = 0;
      int var8 = 0;
      AngelCodeFont.CharDef var9 = null;

      for(int var10 = 0; var10 < var6; ++var10) {
         char var11 = var1.charAt(var2 + var10);
         if (var11 == '\n') {
            var8 = 0;
         } else if (var11 < this.chars.length) {
            AngelCodeFont.CharDef var12 = this.chars[var11];
            if (var12 != null) {
               if (var9 != null) {
                  var8 += var9.getKerning(var11);
               }

               var9 = var12;
               if (!var4 && var10 >= var6 - 1) {
                  var8 += var12.width;
               } else {
                  var8 += var12.xadvance;
               }

               var7 = Math.max(var7, var8);
            }
         }
      }

      if (var5 != null) {
         var5.width = new Short((short)var7);
      }

      return var7;
   }

   public int getYOffset(String var1) {
      AngelCodeFont.DisplayList var2 = null;
      if (this.displayListCaching) {
         var2 = (AngelCodeFont.DisplayList)this.displayLists.get(var1);
         if (var2 != null && var2.yOffset != null) {
            return var2.yOffset.intValue();
         }
      }

      int var3 = var1.indexOf(10);
      if (var3 == -1) {
         var3 = var1.length();
      }

      int var4 = 10000;

      for(int var5 = 0; var5 < var3; ++var5) {
         char var6 = var1.charAt(var5);
         AngelCodeFont.CharDef var7 = this.chars[var6];
         if (var7 != null) {
            var4 = Math.min(var7.yoffset, var4);
         }
      }

      if (var2 != null) {
         var2.yOffset = new Short((short)var4);
      }

      return var4;
   }

   private AngelCodeFont.CharDef parseChar(String var1) {
      AngelCodeFont.CharDef var2 = new AngelCodeFont.CharDef();
      StringTokenizer var3 = new StringTokenizer(var1, " =");
      var3.nextToken();
      var3.nextToken();
      var2.id = Integer.parseInt(var3.nextToken());
      if (var2.id < 0) {
         return null;
      } else {
         if (var2.id > 255) {
         }

         var3.nextToken();
         var2.x = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.y = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.width = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.height = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.xoffset = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.yoffset = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.xadvance = Short.parseShort(var3.nextToken());
         var3.nextToken();
         var2.page = Short.parseShort(var3.nextToken());
         Texture var4 = this.fontImage;
         if (this.pages.containsKey(var2.page)) {
            var4 = (Texture)this.pages.get(var2.page);
         }

         if (var4 != null && var4.isReady()) {
            var2.init();
         }

         if (var2.id != 32) {
            this.lineHeight = Math.max(var2.height + var2.yoffset, this.lineHeight);
         }

         return var2;
      }
   }

   private void parseFnt(InputStream var1) {
      if (this.displayListCaching) {
         this.baseDisplayListID = GL11.glGenLists(200);
         if (this.baseDisplayListID == 0) {
            this.displayListCaching = false;
         }
      }

      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(var1));
         String var3 = var2.readLine();
         String var4 = var2.readLine();
         TShortObjectHashMap var5 = new TShortObjectHashMap(64);
         ArrayList var6 = new ArrayList(255);
         int var7 = 0;
         boolean var8 = false;

         AngelCodeFont.CharDef var17;
         while(!var8) {
            String var9 = var2.readLine();
            if (var9 == null) {
               var8 = true;
            } else {
               StringTokenizer var10;
               short var11;
               int var19;
               if (var9.startsWith("page")) {
                  var10 = new StringTokenizer(var9, " =");
                  var10.nextToken();
                  var10.nextToken();
                  var11 = Short.parseShort(var10.nextToken());
                  var10.nextToken();
                  String var12 = var10.nextToken().replace("\"", "");
                  String var10000 = this.fntFile.getParent();
                  var12 = var10000 + File.separatorChar + var12;
                  var12 = var12.replace("\\", "/");
                  byte var13 = 0;
                  var19 = var13 | (TextureID.bUseCompression ? 4 : 0);
                  Texture var14 = Texture.getSharedTexture(var12, var19);
                  if (var14 == null) {
                     System.out.println("AngelCodeFont failed to load page " + var11 + " texture " + var12);
                  } else {
                     this.pages.put(var11, var14);
                     if (!var14.isReady()) {
                        var14.getObserverCb().add(this);
                     }
                  }
               }

               if (!var9.startsWith("chars c") && var9.startsWith("char")) {
                  var17 = this.parseChar(var9);
                  if (var17 != null) {
                     var7 = Math.max(var7, var17.id);
                     var6.add(var17);
                  }
               }

               if (!var9.startsWith("kernings c") && var9.startsWith("kerning")) {
                  var10 = new StringTokenizer(var9, " =");
                  var10.nextToken();
                  var10.nextToken();
                  var11 = Short.parseShort(var10.nextToken());
                  var10.nextToken();
                  int var18 = Integer.parseInt(var10.nextToken());
                  var10.nextToken();
                  var19 = Integer.parseInt(var10.nextToken());
                  TShortArrayList var20 = (TShortArrayList)var5.get(var11);
                  if (var20 == null) {
                     var20 = new TShortArrayList();
                     var5.put(var11, var20);
                  }

                  var20.add((short)var18);
                  var20.add((short)var19);
               }
            }
         }

         this.chars = new AngelCodeFont.CharDef[var7 + 1];

         for(Iterator var16 = var6.iterator(); var16.hasNext(); this.chars[var17.id] = var17) {
            var17 = (AngelCodeFont.CharDef)var16.next();
         }

         var5.forEachEntry(new TShortObjectProcedure() {
            public boolean execute(short var1, TShortArrayList var2) {
               AngelCodeFont.CharDef var3 = AngelCodeFont.this.chars[var1];
               var3.kerningSecond = new short[var2.size() / 2];
               var3.kerningAmount = new short[var2.size() / 2];
               int var4 = 0;

               for(int var5 = 0; var5 < var2.size(); var5 += 2) {
                  var3.kerningSecond[var4] = var2.get(var5);
                  var3.kerningAmount[var4] = var2.get(var5 + 1);
                  ++var4;
               }

               short[] var9 = Arrays.copyOf(var3.kerningSecond, var3.kerningSecond.length);
               short[] var6 = Arrays.copyOf(var3.kerningAmount, var3.kerningAmount.length);
               Arrays.sort(var9);

               for(int var7 = 0; var7 < var9.length; ++var7) {
                  for(int var8 = 0; var8 < var3.kerningSecond.length; ++var8) {
                     if (var3.kerningSecond[var8] == var9[var7]) {
                        var3.kerningAmount[var7] = var6[var8];
                        break;
                     }
                  }
               }

               var3.kerningSecond = var9;
               return true;
            }
         });
         var2.close();
      } catch (IOException var15) {
         var15.printStackTrace();
      }

   }

   private void render(String var1, int var2, int var3) {
      ++var3;
      int var4 = var3 - var2;
      float var5 = 0.0F;
      float var6 = 0.0F;
      AngelCodeFont.CharDef var7 = null;
      if (data.length < var4) {
         data = new char[(var4 + 128 - 1) / 128 * 128];
      }

      var1.getChars(var2, var3, data, 0);

      for(int var8 = 0; var8 < var4; ++var8) {
         char var9 = data[var8];
         if (var9 == '\n') {
            var5 = 0.0F;
            var6 += (float)this.getLineHeight();
         } else if (var9 < this.chars.length) {
            AngelCodeFont.CharDef var10 = this.chars[var9];
            if (var10 != null) {
               if (var7 != null) {
                  if (s_scale > 0.0F) {
                     var5 += (float)var7.getKerning(var9) * s_scale;
                  } else {
                     var5 += (float)var7.getKerning(var9);
                  }
               }

               var7 = var10;
               var10.draw(var5, var6);
               if (s_scale > 0.0F) {
                  var5 += (float)var10.xadvance * s_scale;
               } else {
                  var5 += (float)var10.xadvance;
               }
            }
         }
      }

   }

   public void onStateChanged(Asset.State var1, Asset.State var2, Asset var3) {
      if (var3 == this.fontImage || this.pages.containsValue(var3)) {
         if (var2 == Asset.State.READY) {
            AngelCodeFont.CharDef[] var4 = this.chars;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               AngelCodeFont.CharDef var7 = var4[var6];
               if (var7 != null && var7.image == null) {
                  Texture var8 = this.fontImage;
                  if (this.pages.containsKey(var7.page)) {
                     var8 = (Texture)this.pages.get(var7.page);
                  }

                  if (var3 == var8) {
                     var7.init();
                  }
               }
            }

         }
      }
   }

   public boolean isEmpty() {
      if (this.fontImage != null && this.fontImage.isEmpty()) {
         return true;
      } else {
         Iterator var1 = this.pages.values().iterator();

         Texture var2;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            var2 = (Texture)var1.next();
         } while(!var2.isEmpty());

         return true;
      }
   }

   public void destroy() {
      AngelCodeFont.CharDef[] var1 = this.chars;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         AngelCodeFont.CharDef var4 = var1[var3];
         if (var4 != null) {
            var4.destroy();
         }
      }

      Arrays.fill(this.chars, (Object)null);
      this.pages.clear();
   }

   private static class DisplayList {
      Short height;
      int id;
      String text;
      Short width;
      Short yOffset;
   }

   public class CharDef {
      public short dlIndex;
      public short height;
      public int id;
      public Texture image;
      public short[] kerningSecond;
      public short[] kerningAmount;
      public short width;
      public short x;
      public short xadvance;
      public short xoffset;
      public short y;
      public short yoffset;
      public short page;

      public void draw(float var1, float var2) {
         Texture var3 = this.image;
         if (AngelCodeFont.s_scale > 0.0F) {
            SpriteRenderer.instance.m_states.getPopulatingActiveState().render(var3, var1 + (float)this.xoffset * AngelCodeFont.s_scale + (float)AngelCodeFont.xoff, var2 + (float)this.yoffset * AngelCodeFont.s_scale + (float)AngelCodeFont.yoff, (float)this.width * AngelCodeFont.s_scale, (float)this.height * AngelCodeFont.s_scale, AngelCodeFont.curR, AngelCodeFont.curG, AngelCodeFont.curB, AngelCodeFont.curA, (Consumer)null);
         } else {
            SpriteRenderer.instance.renderi(var3, (int)(var1 + (float)this.xoffset + (float)AngelCodeFont.xoff), (int)(var2 + (float)this.yoffset + (float)AngelCodeFont.yoff), this.width, this.height, AngelCodeFont.curR, AngelCodeFont.curG, AngelCodeFont.curB, AngelCodeFont.curA, (Consumer)null);
         }

      }

      public int getKerning(int var1) {
         if (this.kerningSecond == null) {
            return 0;
         } else {
            int var2 = 0;
            int var3 = this.kerningSecond.length - 1;

            while(var2 <= var3) {
               int var4 = var2 + var3 >>> 1;
               if (this.kerningSecond[var4] < var1) {
                  var2 = var4 + 1;
               } else {
                  if (this.kerningSecond[var4] <= var1) {
                     return this.kerningAmount[var4];
                  }

                  var3 = var4 - 1;
               }
            }

            return 0;
         }
      }

      public void init() {
         Texture var1 = AngelCodeFont.this.fontImage;
         if (AngelCodeFont.this.pages.containsKey(this.page)) {
            var1 = (Texture)AngelCodeFont.this.pages.get(this.page);
         }

         TextureID var10003 = var1.getTextureId();
         String var10004 = var1.getName();
         this.image = new AngelCodeFont.CharDefTexture(var10003, var10004 + "_" + this.x + "_" + this.y);
         this.image.setRegion(this.x + (int)(var1.xStart * (float)var1.getWidthHW()), this.y + (int)(var1.yStart * (float)var1.getHeightHW()), this.width, this.height);
      }

      public void destroy() {
         if (this.image != null && this.image.getTextureId() != null) {
            ((AngelCodeFont.CharDefTexture)this.image).releaseCharDef();
            this.image = null;
         }

      }

      public String toString() {
         return "[CharDef id=" + this.id + " x=" + this.x + " y=" + this.y + "]";
      }
   }

   public static final class CharDefTexture extends Texture {
      public CharDefTexture(TextureID var1, String var2) {
         super(var1, var2);
      }

      public void releaseCharDef() {
         this.removeDependency(this.dataid);
      }
   }
}
