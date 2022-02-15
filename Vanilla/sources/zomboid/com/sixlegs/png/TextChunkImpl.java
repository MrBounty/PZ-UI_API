package com.sixlegs.png;

class TextChunkImpl implements TextChunk {
   private final String keyword;
   private final String text;
   private final String language;
   private final String translated;
   private final int type;

   public TextChunkImpl(String var1, String var2, String var3, String var4, int var5) {
      this.keyword = var1;
      this.text = var2;
      this.language = var3;
      this.translated = var4;
      this.type = var5;
   }

   public String getKeyword() {
      return this.keyword;
   }

   public String getTranslatedKeyword() {
      return this.translated;
   }

   public String getLanguage() {
      return this.language;
   }

   public String getText() {
      return this.text;
   }

   public int getType() {
      return this.type;
   }
}
