package zombie.ui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatElement;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.textures.Texture;
import zombie.network.GameServer;

public final class TextDrawObject {
   private String[] validImages;
   private String[] validFonts;
   private final ArrayList lines;
   private int width;
   private int height;
   private int maxCharsLine;
   private UIFont defaultFontEnum;
   private AngelCodeFont defaultFont;
   private String original;
   private String unformatted;
   private TextDrawObject.DrawLine currentLine;
   private TextDrawObject.DrawElement currentElement;
   private boolean hasOpened;
   private boolean drawBackground;
   private boolean allowImages;
   private boolean allowChatIcons;
   private boolean allowColors;
   private boolean allowFonts;
   private boolean allowBBcode;
   private boolean allowAnyImage;
   private boolean allowLineBreaks;
   private boolean equalizeLineHeights;
   private boolean enabled;
   private int visibleRadius;
   private float scrambleVal;
   private float outlineR;
   private float outlineG;
   private float outlineB;
   private float outlineA;
   private float defaultR;
   private float defaultG;
   private float defaultB;
   private float defaultA;
   private int hearRange;
   private float internalClock;
   private String customTag;
   private int customImageMaxDim;
   private TextDrawHorizontal defaultHorz;
   private int drawMode;
   private static ArrayList renderBatch = new ArrayList();
   private static ArrayDeque renderBatchPool = new ArrayDeque();
   private String elemText;

   public TextDrawObject() {
      this(255, 255, 255, true, true, true, true, true, false);
   }

   public TextDrawObject(int var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, true, true, true, true, false);
   }

   public TextDrawObject(int var1, int var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      this.validImages = new String[]{"Icon_music_notes", "media/ui/CarKey.png", "media/ui/ArrowUp.png", "media/ui/ArrowDown.png"};
      this.validFonts = new String[]{"Small", "Dialogue", "Medium", "Code", "Large", "Massive"};
      this.lines = new ArrayList();
      this.width = 0;
      this.height = 0;
      this.maxCharsLine = -1;
      this.defaultFontEnum = UIFont.Dialogue;
      this.defaultFont = null;
      this.original = "";
      this.unformatted = "";
      this.hasOpened = false;
      this.drawBackground = false;
      this.allowImages = true;
      this.allowChatIcons = true;
      this.allowColors = true;
      this.allowFonts = true;
      this.allowBBcode = true;
      this.allowAnyImage = false;
      this.allowLineBreaks = true;
      this.equalizeLineHeights = false;
      this.enabled = true;
      this.visibleRadius = -1;
      this.scrambleVal = 0.0F;
      this.outlineR = 0.0F;
      this.outlineG = 0.0F;
      this.outlineB = 0.0F;
      this.outlineA = 1.0F;
      this.defaultR = 1.0F;
      this.defaultG = 1.0F;
      this.defaultB = 1.0F;
      this.defaultA = 1.0F;
      this.hearRange = -1;
      this.internalClock = 0.0F;
      this.customTag = "default";
      this.customImageMaxDim = 18;
      this.defaultHorz = TextDrawHorizontal.Center;
      this.drawMode = 0;
      this.setSettings(var4, var5, var6, var7, var8, var9);
      this.setDefaultColors(var1, var2, var3);
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setVisibleRadius(int var1) {
      this.visibleRadius = var1;
   }

   public int getVisibleRadius() {
      return this.visibleRadius;
   }

   public void setDrawBackground(boolean var1) {
      this.drawBackground = var1;
   }

   public void setAllowImages(boolean var1) {
      this.allowImages = var1;
   }

   public void setAllowChatIcons(boolean var1) {
      this.allowChatIcons = var1;
   }

   public void setAllowColors(boolean var1) {
      this.allowColors = var1;
   }

   public void setAllowFonts(boolean var1) {
      this.allowFonts = var1;
   }

   public void setAllowBBcode(boolean var1) {
      this.allowBBcode = var1;
   }

   public void setAllowAnyImage(boolean var1) {
      this.allowAnyImage = var1;
   }

   public void setAllowLineBreaks(boolean var1) {
      this.allowLineBreaks = var1;
   }

   public void setEqualizeLineHeights(boolean var1) {
      this.equalizeLineHeights = var1;
      this.calculateDimensions();
   }

   public void setSettings(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      this.allowImages = var2;
      this.allowChatIcons = var3;
      this.allowColors = var4;
      this.allowFonts = var5;
      this.allowBBcode = var1;
      this.equalizeLineHeights = var6;
   }

   public void setCustomTag(String var1) {
      this.customTag = var1;
   }

   public String getCustomTag() {
      return this.customTag;
   }

   public void setValidImages(String[] var1) {
      this.validImages = var1;
   }

   public void setValidFonts(String[] var1) {
      this.validFonts = var1;
   }

   public void setMaxCharsPerLine(int var1) {
      if (var1 > 0) {
         this.ReadString(this.original, var1);
      }
   }

   public void setCustomImageMaxDimensions(int var1) {
      if (var1 >= 1) {
         this.customImageMaxDim = var1;
         this.calculateDimensions();
      }
   }

   public void setOutlineColors(int var1, int var2, int var3) {
      this.setOutlineColors((float)var1 / 255.0F, (float)var2 / 255.0F, (float)var3 / 255.0F, 1.0F);
   }

   public void setOutlineColors(int var1, int var2, int var3, int var4) {
      this.setOutlineColors((float)var1 / 255.0F, (float)var2 / 255.0F, (float)var3 / 255.0F, (float)var4 / 255.0F);
   }

   public void setOutlineColors(float var1, float var2, float var3) {
      this.setOutlineColors(var1, var2, var3, 1.0F);
   }

   public void setOutlineColors(float var1, float var2, float var3, float var4) {
      this.outlineR = var1;
      this.outlineG = var2;
      this.outlineB = var3;
      this.outlineA = var4;
   }

   public void setDefaultColors(int var1, int var2, int var3) {
      this.setDefaultColors((float)var1 / 255.0F, (float)var2 / 255.0F, (float)var3 / 255.0F, 1.0F);
   }

   public void setDefaultColors(int var1, int var2, int var3, int var4) {
      this.setDefaultColors((float)var1 / 255.0F, (float)var2 / 255.0F, (float)var3 / 255.0F, (float)var4 / 255.0F);
   }

   public void setDefaultColors(float var1, float var2, float var3) {
      this.setDefaultColors(var1, var2, var3, 1.0F);
   }

   public void setDefaultColors(float var1, float var2, float var3, float var4) {
      this.defaultR = var1;
      this.defaultG = var2;
      this.defaultB = var3;
      this.defaultA = var4;
   }

   public void setHorizontalAlign(String var1) {
      if (var1.equals("left")) {
         this.defaultHorz = TextDrawHorizontal.Left;
      } else if (var1.equals("center")) {
         this.defaultHorz = TextDrawHorizontal.Center;
      }

      if (var1.equals("right")) {
         this.defaultHorz = TextDrawHorizontal.Right;
      }

   }

   public void setHorizontalAlign(TextDrawHorizontal var1) {
      this.defaultHorz = var1;
   }

   public TextDrawHorizontal getHorizontalAlign() {
      return this.defaultHorz;
   }

   public String getOriginal() {
      return this.original;
   }

   public String getUnformatted() {
      if (!(this.scrambleVal > 0.0F)) {
         return this.unformatted;
      } else {
         String var1 = "";
         Iterator var2 = this.lines.iterator();

         while(var2.hasNext()) {
            TextDrawObject.DrawLine var3 = (TextDrawObject.DrawLine)var2.next();
            Iterator var4 = var3.elements.iterator();

            while(var4.hasNext()) {
               TextDrawObject.DrawElement var5 = (TextDrawObject.DrawElement)var4.next();
               if (!var5.isImage) {
                  var1 = var1 + var5.scrambleText;
               }
            }
         }

         return var1;
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public UIFont getDefaultFontEnum() {
      return this.defaultFontEnum;
   }

   public boolean isNullOrZeroLength() {
      return this.original == null || this.original.length() == 0;
   }

   public float getInternalClock() {
      return this.internalClock;
   }

   public void setInternalTickClock(float var1) {
      if (var1 > 0.0F) {
         this.internalClock = var1;
      }

   }

   public float updateInternalTickClock() {
      return this.updateInternalTickClock(1.25F * GameTime.getInstance().getMultiplier());
   }

   public float updateInternalTickClock(float var1) {
      if (this.internalClock <= 0.0F) {
         return 0.0F;
      } else {
         this.internalClock -= var1;
         if (this.internalClock <= 0.0F) {
            this.internalClock = 0.0F;
         }

         return this.internalClock;
      }
   }

   public void setScrambleVal(float var1) {
      if (this.scrambleVal != var1) {
         this.scrambleVal = var1;
         if (this.scrambleVal > 0.0F) {
            Iterator var2 = this.lines.iterator();

            while(var2.hasNext()) {
               TextDrawObject.DrawLine var3 = (TextDrawObject.DrawLine)var2.next();
               Iterator var4 = var3.elements.iterator();

               while(var4.hasNext()) {
                  TextDrawObject.DrawElement var5 = (TextDrawObject.DrawElement)var4.next();
                  if (!var5.isImage) {
                     var5.scrambleText(this.scrambleVal);
                  }
               }
            }
         }
      }

   }

   public float getScrambleVal() {
      return this.scrambleVal;
   }

   public void setHearRange(int var1) {
      if (var1 < 0) {
         this.hearRange = 0;
      } else {
         this.hearRange = var1;
      }

   }

   public int getHearRange() {
      return this.hearRange;
   }

   private boolean isValidFont(String var1) {
      String[] var2 = this.validFonts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var1.equals(var5) && UIFont.FromString(var1) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean isValidImage(String var1) {
      String[] var2 = this.validImages;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var1.equals(var5)) {
            return true;
         }
      }

      return false;
   }

   private int tryColorInt(String var1) {
      if (var1.length() > 0 && var1.length() <= 3) {
         try {
            int var2 = Integer.parseInt(var1);
            return var2 >= 0 && var2 < 256 ? var2 : -1;
         } catch (NumberFormatException var3) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private String readTagValue(char[] var1, int var2) {
      if (var1[var2] == '=') {
         String var3 = "";

         for(int var4 = var2 + 1; var4 < var1.length; ++var4) {
            char var5 = var1[var4];
            if (var5 == ']') {
               return var3;
            }

            var3 = var3 + var5;
         }
      }

      return null;
   }

   public void Clear() {
      this.original = "";
      this.unformatted = "";
      this.reset();
   }

   private void reset() {
      this.lines.clear();
      this.currentLine = new TextDrawObject.DrawLine();
      this.lines.add(this.currentLine);
      this.currentElement = new TextDrawObject.DrawElement();
      this.currentLine.addElement(this.currentElement);
      this.enabled = true;
      this.scrambleVal = 0.0F;
   }

   private void addNewLine() {
      this.currentLine = new TextDrawObject.DrawLine();
      this.lines.add(this.currentLine);
      this.currentElement = this.currentElement.softclone();
      this.currentLine.addElement(this.currentElement);
   }

   private void addText(String var1) {
      this.currentElement.addText(var1);
      TextDrawObject.DrawLine var10000 = this.currentLine;
      var10000.charW += var1.length();
   }

   private void addWord(String var1) {
      if (this.maxCharsLine > 0 && this.currentLine.charW + var1.length() >= this.maxCharsLine) {
         for(int var2 = 0; var2 < var1.length() / this.maxCharsLine + 1; ++var2) {
            int var3 = var2 * this.maxCharsLine;
            int var4 = var3 + this.maxCharsLine < var1.length() ? var3 + this.maxCharsLine : var1.length();
            if (var1.substring(var3, var4).length() > 0) {
               if (var2 > 0 || this.currentLine.charW != 0) {
                  this.addNewLine();
               }

               this.addText(var1.substring(var3, var4));
            }
         }
      } else {
         this.addText(var1);
      }

   }

   private void addNewElement() {
      if (this.currentElement.text.length() == 0) {
         this.currentElement.reset();
      } else {
         this.currentElement = new TextDrawObject.DrawElement();
         this.currentLine.addElement(this.currentElement);
      }

   }

   private int readTag(char[] var1, int var2, String var3) {
      String var4;
      if (this.allowFonts && var3.equals("fnt")) {
         var4 = this.readTagValue(var1, var2);
         if (var4 != null && this.isValidFont(var4)) {
            this.addNewElement();
            this.currentElement.f = UIFont.FromString(var4);
            this.currentElement.useFont = true;
            this.currentElement.font = TextManager.instance.getFontFromEnum(this.currentElement.f);
            this.hasOpened = true;
            return var2 + var4.length() + 1;
         }
      } else {
         int var7;
         int var8;
         if ((this.allowImages || this.allowChatIcons) && var3.equals("img")) {
            var4 = this.readTagValue(var1, var2);
            if (var4 != null && var4.trim().length() > 0) {
               this.addNewElement();
               int var13 = var4.length();
               String[] var11 = var4.split(",");
               if (var11.length > 1) {
                  var4 = var11[0];
               }

               this.currentElement.isImage = true;
               this.currentElement.text = var4.trim();
               if (this.currentElement.text.equals("music")) {
                  this.currentElement.text = "Icon_music_notes";
               }

               if (this.allowChatIcons && this.isValidImage(this.currentElement.text)) {
                  this.currentElement.tex = Texture.getSharedTexture(this.currentElement.text);
                  this.currentElement.isTextImage = true;
               } else if (this.allowImages) {
                  this.currentElement.tex = Texture.getSharedTexture("Item_" + this.currentElement.text);
                  if (this.currentElement.tex == null) {
                     this.currentElement.tex = Texture.getSharedTexture("media/ui/Container_" + this.currentElement.text);
                  }

                  if (this.currentElement.tex != null) {
                     this.currentElement.isTextImage = false;
                     this.currentElement.text = "Item_" + this.currentElement.text;
                  }
               }

               if (this.allowAnyImage && this.currentElement.tex == null) {
                  this.currentElement.tex = Texture.getSharedTexture(this.currentElement.text);
                  if (this.currentElement.tex != null) {
                     this.currentElement.isTextImage = false;
                  }
               }

               if (var11.length == 4) {
                  var7 = this.tryColorInt(var11[1]);
                  var8 = this.tryColorInt(var11[2]);
                  int var9 = this.tryColorInt(var11[3]);
                  if (var7 != -1 && var8 != -1 && var9 != -1) {
                     this.currentElement.useColor = true;
                     this.currentElement.R = (float)var7 / 255.0F;
                     this.currentElement.G = (float)var8 / 255.0F;
                     this.currentElement.B = (float)var9 / 255.0F;
                  }
               }

               this.addNewElement();
               return var2 + var13 + 1;
            }
         } else if (this.allowColors && var3.equals("col")) {
            var4 = this.readTagValue(var1, var2);
            if (var4 != null) {
               String[] var12 = var4.split(",");
               if (var12.length == 3) {
                  int var6 = this.tryColorInt(var12[0]);
                  var7 = this.tryColorInt(var12[1]);
                  var8 = this.tryColorInt(var12[2]);
                  if (var6 != -1 && var7 != -1 && var8 != -1) {
                     this.addNewElement();
                     this.currentElement.useColor = true;
                     this.currentElement.R = (float)var6 / 255.0F;
                     this.currentElement.G = (float)var7 / 255.0F;
                     this.currentElement.B = (float)var8 / 255.0F;
                     this.hasOpened = true;
                     return var2 + var4.length() + 1;
                  }
               }
            }
         } else if (var3.equals("cdt")) {
            var4 = this.readTagValue(var1, var2);
            if (var4 != null) {
               float var5 = this.internalClock;

               try {
                  var5 = Float.parseFloat(var4);
                  var5 *= 60.0F;
               } catch (NumberFormatException var10) {
                  var10.printStackTrace();
               }

               this.internalClock = var5;
               return var2 + var4.length() + 1;
            }
         }
      }

      return -1;
   }

   public void setDefaultFont(UIFont var1) {
      if (!var1.equals(this.defaultFontEnum)) {
         this.ReadString(var1, this.original, this.maxCharsLine);
      }

   }

   private void setDefaultFontInternal(UIFont var1) {
      if (this.defaultFont == null || !var1.equals(this.defaultFontEnum)) {
         this.defaultFontEnum = var1;
         this.defaultFont = TextManager.instance.getFontFromEnum(var1);
      }

   }

   public void ReadString(String var1) {
      this.ReadString(this.defaultFontEnum, var1, this.maxCharsLine);
   }

   public void ReadString(String var1, int var2) {
      this.ReadString(this.defaultFontEnum, var1, var2);
   }

   public void ReadString(UIFont var1, String var2, int var3) {
      if (var2 == null) {
         var2 = "";
      }

      this.reset();
      this.setDefaultFontInternal(var1);
      if (this.defaultFont != null) {
         this.maxCharsLine = var3;
         this.original = var2;
         char[] var4 = var2.toCharArray();
         this.hasOpened = false;
         String var5 = "";

         for(int var6 = 0; var6 < var4.length; ++var6) {
            char var7 = var4[var6];
            if (this.allowBBcode && var7 == '[') {
               if (var5.length() > 0) {
                  this.addWord(var5);
                  var5 = "";
               }

               if (var6 + 4 < var4.length) {
                  String var8 = (var4[var6 + 1] + var4[var6 + 2] + var4[var6 + 3]).toLowerCase();
                  if (this.allowLineBreaks && var8.equals("br/")) {
                     this.addNewLine();
                     var6 += 4;
                     continue;
                  }

                  if (!this.hasOpened) {
                     int var9 = this.readTag(var4, var6 + 4, var8);
                     if (var9 >= 0) {
                        var6 = var9;
                        continue;
                     }
                  }
               }

               if (this.hasOpened && var6 + 2 < var4.length && var4[var6 + 1] == '/' && var4[var6 + 2] == ']') {
                  this.hasOpened = false;
                  this.addNewElement();
                  var6 += 2;
                  continue;
               }
            }

            if (Character.isWhitespace(var7) && var6 > 0 && !Character.isWhitespace(var4[var6 - 1])) {
               this.addWord(var5);
               var5 = "";
            }

            var5 = var5 + var7;
            this.unformatted = this.unformatted + var7;
         }

         if (var5.length() > 0) {
            this.addWord(var5);
         }

         this.calculateDimensions();
      }
   }

   public void calculateDimensions() {
      this.width = 0;
      this.height = 0;
      int var1 = 0;

      int var2;
      TextDrawObject.DrawLine var3;
      for(var2 = 0; var2 < this.lines.size(); ++var2) {
         var3 = (TextDrawObject.DrawLine)this.lines.get(var2);
         var3.h = 0;
         var3.w = 0;

         for(int var4 = 0; var4 < var3.elements.size(); ++var4) {
            TextDrawObject.DrawElement var5 = (TextDrawObject.DrawElement)var3.elements.get(var4);
            var5.w = 0;
            var5.h = 0;
            if (var5.isImage && var5.tex != null) {
               if (var5.isTextImage) {
                  var5.w = var5.tex.getWidth();
                  var5.h = var5.tex.getHeight();
               } else {
                  var5.w = (int)((float)var5.tex.getWidth() * 0.75F);
                  var5.h = (int)((float)var5.tex.getHeight() * 0.75F);
               }
            } else if (var5.useFont && var5.font != null) {
               var5.w = var5.font.getWidth(var5.text);
               var5.h = var5.font.getHeight(var5.text);
            } else if (this.defaultFont != null) {
               var5.w = this.defaultFont.getWidth(var5.text);
               var5.h = this.defaultFont.getHeight(var5.text);
            }

            var3.w += var5.w;
            if (var5.h > var3.h) {
               var3.h = var5.h;
            }
         }

         if (var3.w > this.width) {
            this.width = var3.w;
         }

         this.height += var3.h;
         if (var3.h > var1) {
            var1 = var3.h;
         }
      }

      if (this.equalizeLineHeights) {
         this.height = 0;

         for(var2 = 0; var2 < this.lines.size(); ++var2) {
            var3 = (TextDrawObject.DrawLine)this.lines.get(var2);
            var3.h = var1;
            this.height += var1;
         }
      }

   }

   public void Draw(double var1, double var3) {
      this.Draw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)this.defaultA, false);
   }

   public void Draw(double var1, double var3, boolean var5) {
      this.Draw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)this.defaultA, var5);
   }

   public void Draw(double var1, double var3, boolean var5, float var6) {
      this.Draw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)var6, var5);
   }

   public void Draw(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      this.Draw(this.defaultHorz, var1, var3, var5, var7, var9, var11, var13);
   }

   public void Draw(TextDrawHorizontal var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      this.DrawRaw(var1, var2, var4, (float)var6, (float)var8, (float)var10, (float)var12, var14);
   }

   public void AddBatchedDraw(double var1, double var3) {
      this.AddBatchedDraw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)this.defaultA, false);
   }

   public void AddBatchedDraw(double var1, double var3, boolean var5) {
      this.AddBatchedDraw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)this.defaultA, var5);
   }

   public void AddBatchedDraw(double var1, double var3, boolean var5, float var6) {
      this.AddBatchedDraw(this.defaultHorz, var1, var3, (double)this.defaultR, (double)this.defaultG, (double)this.defaultB, (double)var6, var5);
   }

   public void AddBatchedDraw(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      this.AddBatchedDraw(this.defaultHorz, var1, var3, var5, var7, var9, var11, var13);
   }

   public void AddBatchedDraw(TextDrawHorizontal var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
      if (!GameServer.bServer) {
         TextDrawObject.RenderBatch var15 = renderBatchPool.isEmpty() ? new TextDrawObject.RenderBatch() : (TextDrawObject.RenderBatch)renderBatchPool.pop();
         var15.playerNum = IsoPlayer.getPlayerIndex();
         var15.element = this;
         var15.horz = var1;
         var15.x = var2;
         var15.y = var4;
         var15.r = (float)var6;
         var15.g = (float)var8;
         var15.b = (float)var10;
         var15.a = (float)var12;
         var15.drawOutlines = var14;
         renderBatch.add(var15);
      }
   }

   public static void RenderBatch(int var0) {
      if (renderBatch.size() > 0) {
         for(int var1 = 0; var1 < renderBatch.size(); ++var1) {
            TextDrawObject.RenderBatch var2 = (TextDrawObject.RenderBatch)renderBatch.get(var1);
            if (var2.playerNum == var0) {
               var2.element.DrawRaw(var2.horz, var2.x, var2.y, var2.r, var2.g, var2.b, var2.a, var2.drawOutlines);
               renderBatchPool.add(var2);
               renderBatch.remove(var1--);
            }
         }
      }

   }

   public static void NoRender(int var0) {
      for(int var1 = 0; var1 < renderBatch.size(); ++var1) {
         TextDrawObject.RenderBatch var2 = (TextDrawObject.RenderBatch)renderBatch.get(var1);
         if (var2.playerNum == var0) {
            renderBatchPool.add(var2);
            renderBatch.remove(var1--);
         }
      }

   }

   public void DrawRaw(TextDrawHorizontal var1, double var2, double var4, float var6, float var7, float var8, float var9, boolean var10) {
      double var11 = var2;
      double var13 = var4;
      double var15 = 0.0D;
      int var17 = Core.getInstance().getScreenWidth();
      int var18 = Core.getInstance().getScreenHeight();
      byte var19 = 20;
      if (var1 == TextDrawHorizontal.Center) {
         var11 = var2 - (double)(this.getWidth() / 2);
      } else if (var1 == TextDrawHorizontal.Right) {
         var11 = var2 - (double)this.getWidth();
      }

      if (!(var11 - (double)var19 >= (double)var17) && !(var11 + (double)this.getWidth() + (double)var19 <= 0.0D) && !(var4 - (double)var19 >= (double)var18) && !(var4 + (double)this.getHeight() + (double)var19 <= 0.0D)) {
         if (this.drawBackground && ChatElement.backdropTexture != null) {
            ChatElement.backdropTexture.renderInnerBased((int)var11, (int)var4, this.getWidth(), this.getHeight(), 0.0F, 0.0F, 0.0F, 0.4F * var9);
         }

         float var20 = this.outlineA;
         if (var10 && var9 < 1.0F) {
            var20 = this.outlineA * var9;
         }

         for(int var21 = 0; var21 < this.lines.size(); ++var21) {
            TextDrawObject.DrawLine var22 = (TextDrawObject.DrawLine)this.lines.get(var21);
            var11 = var2;
            if (var1 == TextDrawHorizontal.Center) {
               var11 = var2 - (double)(var22.w / 2);
            } else if (var1 == TextDrawHorizontal.Right) {
               var11 = var2 - (double)var22.w;
            }

            for(int var23 = 0; var23 < var22.elements.size(); ++var23) {
               TextDrawObject.DrawElement var24 = (TextDrawObject.DrawElement)var22.elements.get(var23);
               var15 = (double)(var22.h / 2 - var24.h / 2);
               this.elemText = this.scrambleVal > 0.0F ? var24.scrambleText : var24.text;
               if (var24.isImage && var24.tex != null) {
                  if (var10 && var24.isTextImage) {
                     SpriteRenderer.instance.renderi(var24.tex, (int)(var11 - 1.0D), (int)(var13 + var15 - 1.0D), var24.w, var24.h, this.outlineR, this.outlineG, this.outlineB, var20, (Consumer)null);
                     SpriteRenderer.instance.renderi(var24.tex, (int)(var11 + 1.0D), (int)(var13 + var15 + 1.0D), var24.w, var24.h, this.outlineR, this.outlineG, this.outlineB, var20, (Consumer)null);
                     SpriteRenderer.instance.renderi(var24.tex, (int)(var11 - 1.0D), (int)(var13 + var15 + 1.0D), var24.w, var24.h, this.outlineR, this.outlineG, this.outlineB, var20, (Consumer)null);
                     SpriteRenderer.instance.renderi(var24.tex, (int)(var11 + 1.0D), (int)(var13 + var15 - 1.0D), var24.w, var24.h, this.outlineR, this.outlineG, this.outlineB, var20, (Consumer)null);
                  }

                  if (var24.useColor) {
                     SpriteRenderer.instance.renderi(var24.tex, (int)var11, (int)(var13 + var15), var24.w, var24.h, var24.R, var24.G, var24.B, var9, (Consumer)null);
                  } else if (var24.isTextImage) {
                     SpriteRenderer.instance.renderi(var24.tex, (int)var11, (int)(var13 + var15), var24.w, var24.h, var6, var7, var8, var9, (Consumer)null);
                  } else {
                     SpriteRenderer.instance.renderi(var24.tex, (int)var11, (int)(var13 + var15), var24.w, var24.h, 1.0F, 1.0F, 1.0F, var9, (Consumer)null);
                  }
               } else if (var24.useFont && var24.font != null) {
                  if (var10) {
                     var24.font.drawString((float)(var11 - 1.0D), (float)(var13 + var15 - 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     var24.font.drawString((float)(var11 + 1.0D), (float)(var13 + var15 + 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     var24.font.drawString((float)(var11 - 1.0D), (float)(var13 + var15 + 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     var24.font.drawString((float)(var11 + 1.0D), (float)(var13 + var15 - 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                  }

                  var24.font.drawString((float)var11, (float)(var13 + var15), this.elemText, var6, var7, var8, var9);
               } else if (this.defaultFont != null) {
                  if (var10) {
                     this.defaultFont.drawString((float)(var11 - 1.0D), (float)(var13 + var15 - 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     this.defaultFont.drawString((float)(var11 + 1.0D), (float)(var13 + var15 + 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     this.defaultFont.drawString((float)(var11 - 1.0D), (float)(var13 + var15 + 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                     this.defaultFont.drawString((float)(var11 + 1.0D), (float)(var13 + var15 - 1.0D), this.elemText, this.outlineR, this.outlineG, this.outlineB, var20);
                  }

                  if (var24.useColor) {
                     this.defaultFont.drawString((float)var11, (float)(var13 + var15), this.elemText, var24.R, var24.G, var24.B, var9);
                  } else {
                     this.defaultFont.drawString((float)var11, (float)(var13 + var15), this.elemText, var6, var7, var8, var9);
                  }
               }

               var11 += (double)var24.w;
            }

            var13 += (double)var22.h;
         }

      }
   }

   private static final class DrawLine {
      private final ArrayList elements = new ArrayList();
      private int h = 0;
      private int w = 0;
      private int charW = 0;

      private void addElement(TextDrawObject.DrawElement var1) {
         this.elements.add(var1);
      }
   }

   private static final class DrawElement {
      private String text = "";
      private String scrambleText = "";
      private float currentScrambleVal = 0.0F;
      private UIFont f;
      private AngelCodeFont font;
      private float R;
      private float G;
      private float B;
      private int w;
      private int h;
      private boolean isImage;
      private boolean useFont;
      private boolean useColor;
      private Texture tex;
      private boolean isTextImage;
      private int charWidth;

      private DrawElement() {
         this.f = UIFont.AutoNormSmall;
         this.font = null;
         this.R = 1.0F;
         this.G = 1.0F;
         this.B = 1.0F;
         this.w = 0;
         this.h = 0;
         this.isImage = false;
         this.useFont = false;
         this.useColor = false;
         this.tex = null;
         this.isTextImage = false;
         this.charWidth = 0;
      }

      private void reset() {
         this.text = "";
         this.scrambleText = "";
         this.f = UIFont.AutoNormSmall;
         this.font = null;
         this.R = 1.0F;
         this.G = 1.0F;
         this.B = 1.0F;
         this.w = 0;
         this.h = 0;
         this.isImage = false;
         this.useFont = false;
         this.useColor = false;
         this.tex = null;
         this.isTextImage = false;
         this.charWidth = 0;
      }

      private void addText(String var1) {
         this.text = this.text + var1;
         this.charWidth = this.text.length();
      }

      private void scrambleText(float var1) {
         if (var1 != this.currentScrambleVal) {
            this.currentScrambleVal = var1;
            int var2 = (int)(var1 * 100.0F);
            String[] var3 = this.text.split("\\s+");
            this.scrambleText = "";
            String[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               int var8 = Rand.Next(100);
               if (var8 > var2) {
                  this.scrambleText = this.scrambleText + var7 + " ";
               } else {
                  char[] var9 = new char[var7.length()];
                  Arrays.fill(var9, ".".charAt(0));
                  String var10001 = this.scrambleText;
                  this.scrambleText = var10001 + new String(var9) + " ";
               }
            }
         }

      }

      private void trim() {
         this.text = this.text.trim();
      }

      private TextDrawObject.DrawElement softclone() {
         TextDrawObject.DrawElement var1 = new TextDrawObject.DrawElement();
         if (this.useColor) {
            var1.R = this.R;
            var1.G = this.G;
            var1.B = this.B;
            var1.useColor = this.useColor;
         }

         if (this.useFont) {
            var1.f = this.f;
            var1.font = this.font;
            var1.useFont = this.useFont;
         }

         return var1;
      }
   }

   private static final class RenderBatch {
      int playerNum;
      TextDrawObject element;
      TextDrawHorizontal horz;
      double x;
      double y;
      float r;
      float g;
      float b;
      float a;
      boolean drawOutlines;
   }
}
