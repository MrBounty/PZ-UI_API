package zombie.core;

public final class Language {
   private final int index;
   private final String name;
   private final String text;
   private final String charset;
   private final String base;
   private final boolean azerty;

   Language(int var1, String var2, String var3, String var4, String var5, boolean var6) {
      this.index = var1;
      this.name = var2;
      this.text = var3;
      this.charset = var4;
      this.base = var5;
      this.azerty = var6;
   }

   public int index() {
      return this.index;
   }

   public String name() {
      return this.name;
   }

   public String text() {
      return this.text;
   }

   public String charset() {
      return this.charset;
   }

   public String base() {
      return this.base;
   }

   public boolean isAzerty() {
      return this.azerty;
   }

   public String toString() {
      return this.name;
   }

   public static Language fromIndex(int var0) {
      return Languages.instance.getByIndex(var0);
   }

   public static Language FromString(String var0) {
      Language var1 = Languages.instance.getByName(var0);
      if (var1 == null) {
         var1 = Languages.instance.getDefaultLanguage();
      }

      return var1;
   }
}
