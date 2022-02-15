package zombie.radio.media;

import java.util.ArrayList;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Translator;
import zombie.debug.DebugLog;

public class MediaData {
   private final String id;
   private final String itemDisplayName;
   private String title;
   private String subtitle;
   private String author;
   private String extra;
   private short index;
   private String category;
   private final int spawning;
   private ArrayList lines = new ArrayList();

   public MediaData(String var1, String var2, int var3) {
      this.itemDisplayName = var2;
      this.id = var1;
      this.spawning = var3;
      if (Core.bDebug) {
         if (var2 == null) {
            throw new RuntimeException("ItemDisplayName may not be null.");
         }

         if (var1 == null) {
            throw new RuntimeException("Id may not be null.");
         }
      }

   }

   public void addLine(String var1, float var2, float var3, float var4, String var5) {
      MediaData.MediaLineData var6 = new MediaData.MediaLineData(var1, var2, var3, var4, var5);
      this.lines.add(var6);
   }

   public int getLineCount() {
      return this.lines.size();
   }

   public String getTranslatedItemDisplayName() {
      return Translator.getText(this.itemDisplayName);
   }

   public boolean hasTitle() {
      return this.title != null;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public String getTitleEN() {
      return this.title != null ? Translator.getTextMediaEN(this.title) : null;
   }

   public String getTranslatedTitle() {
      return this.title != null ? Translator.getText(this.title) : null;
   }

   public boolean hasSubTitle() {
      return this.subtitle != null;
   }

   public void setSubtitle(String var1) {
      this.subtitle = var1;
   }

   public String getSubtitleEN() {
      return this.subtitle != null ? Translator.getTextMediaEN(this.subtitle) : null;
   }

   public String getTranslatedSubTitle() {
      return this.subtitle != null ? Translator.getText(this.subtitle) : null;
   }

   public boolean hasAuthor() {
      return this.author != null;
   }

   public void setAuthor(String var1) {
      this.author = var1;
   }

   public String getAuthorEN() {
      return this.author != null ? Translator.getTextMediaEN(this.author) : null;
   }

   public String getTranslatedAuthor() {
      return this.author != null ? Translator.getText(this.author) : null;
   }

   public boolean hasExtra() {
      return this.extra != null;
   }

   public void setExtra(String var1) {
      this.extra = var1;
   }

   public String getExtraEN() {
      return this.extra != null ? Translator.getTextMediaEN(this.extra) : null;
   }

   public String getTranslatedExtra() {
      return this.extra != null ? Translator.getText(this.extra) : null;
   }

   public String getId() {
      return this.id;
   }

   public short getIndex() {
      return this.index;
   }

   protected void setIndex(short var1) {
      this.index = var1;
   }

   public String getCategory() {
      return this.category;
   }

   protected void setCategory(String var1) {
      this.category = var1;
   }

   public int getSpawning() {
      return this.spawning;
   }

   public byte getMediaType() {
      if (this.category == null) {
         String var10000 = this.itemDisplayName != null ? this.itemDisplayName : "unknown";
         DebugLog.log("Warning MediaData has no category set, mediadata = " + var10000);
      }

      return RecordedMedia.getMediaTypeForCategory(this.category);
   }

   public MediaData.MediaLineData getLine(int var1) {
      return var1 >= 0 && var1 < this.lines.size() ? (MediaData.MediaLineData)this.lines.get(var1) : null;
   }

   public class MediaLineData {
      private final String text;
      private final Color color;
      private final String codes;

      public MediaLineData(String var2, float var3, float var4, float var5, String var6) {
         this.text = var2;
         this.codes = var6;
         if (var3 == 0.0F && var4 == 0.0F && var5 == 0.0F) {
            var3 = 1.0F;
            var4 = 1.0F;
            var5 = 1.0F;
         }

         this.color = new Color(var3, var4, var5);
      }

      public String getTranslatedText() {
         return Translator.getText(this.text);
      }

      public Color getColor() {
         return this.color;
      }

      public float getR() {
         return this.color.r;
      }

      public float getG() {
         return this.color.g;
      }

      public float getB() {
         return this.color.b;
      }

      public String getCodes() {
         return this.codes;
      }

      public String getTextGuid() {
         return this.text;
      }
   }
}
