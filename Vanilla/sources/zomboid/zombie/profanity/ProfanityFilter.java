package zombie.profanity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.profanity.locales.Locale;
import zombie.profanity.locales.LocaleChinese;
import zombie.profanity.locales.LocaleEnglish;
import zombie.profanity.locales.LocaleGerman;

public class ProfanityFilter {
   public static boolean DEBUG = false;
   private Map locales = new HashMap();
   private Locale locale;
   private Locale localeDefault;
   private Pattern prePattern;
   private boolean enabled = true;
   public static String LOCALES_DIR;
   private static ProfanityFilter instance;

   public static ProfanityFilter getInstance() {
      if (instance == null) {
         instance = new ProfanityFilter();
      }

      return instance;
   }

   private ProfanityFilter() {
      this.addLocale(new LocaleEnglish("EN"), true);
      this.addLocale(new LocaleGerman("GER"));
      this.addLocale(new LocaleChinese("CHIN"));
      this.prePattern = Pattern.compile("(?<spaced>(?:(?:\\s|\\W)[\\w\\$@](?=\\s|\\W)){2,20})|(?<word>[\\w'\\$@_-]+)");
   }

   public static void printDebug(String var0) {
      if (DEBUG) {
         System.out.println(var0);
      }

   }

   public void enable(boolean var1) {
      this.enabled = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public int getFilterWordsCount() {
      return this.locale != null ? this.locale.getFilterWordsCount() : 0;
   }

   public void addLocale(Locale var1) {
      this.addLocale(var1, false);
   }

   public void addLocale(Locale var1, boolean var2) {
      this.locales.put(var1.getID(), var1);
      if (var2) {
         this.locale = var1;
         this.localeDefault = var1;
      }

   }

   public Locale getLocale() {
      return this.locale;
   }

   public void addWhiteListWord(String var1) {
      if (this.locale != null) {
         this.locale.addWhiteListWord(var1);
      }

   }

   public void removeWhiteListWord(String var1) {
      if (this.locale != null) {
         this.locale.removeWhiteListWord(var1);
      }

   }

   public void addFilterWord(String var1) {
      if (this.locale != null) {
         this.locale.addFilterWord(var1);
      }

   }

   public void removeFilterWord(String var1) {
      if (this.locale != null) {
         this.locale.removeFilterWord(var1);
      }

   }

   public void setLocale(String var1) {
      if (this.locales.containsKey(var1)) {
         this.locale = (Locale)this.locales.get(var1);
      } else {
         this.locale = this.localeDefault;
      }

   }

   public String filterString(String var1) {
      if (this.enabled && this.locale != null && var1 != null && this.locale.getFilterWordsCount() > 0) {
         try {
            StringBuffer var2 = new StringBuffer();
            Matcher var3 = this.prePattern.matcher(var1);

            while(var3.find()) {
               if (var3.group("word") != null) {
                  var3.appendReplacement(var2, Matcher.quoteReplacement(this.locale.filterWord(var3.group("word"), true)));
               } else if (var3.group("spaced") != null) {
                  Locale var10002 = this.locale;
                  String var10003 = var3.group("spaced");
                  var3.appendReplacement(var2, Matcher.quoteReplacement(" " + var10002.filterWord(var10003.replaceAll("\\s+", ""))));
               }
            }

            var3.appendTail(var2);
            return var2.toString();
         } catch (Exception var4) {
            System.out.println("Profanity failed for: " + var1);
         }
      }

      return var1;
   }

   public String validateString(String var1) {
      return this.validateString(var1, true, true, true);
   }

   public String validateString(String var1, boolean var2, boolean var3, boolean var4) {
      if (this.enabled && this.locale != null && var1 != null && this.locale.getFilterWordsCount() > 0) {
         try {
            String var5 = null;
            boolean var6 = false;
            StringBuilder var7 = new StringBuilder();
            Matcher var8 = this.prePattern.matcher(var1);

            while(true) {
               while(var8.find()) {
                  if (var2 && var8.group("word") != null) {
                     var5 = this.locale.validateWord(var8.group("word"), var3);
                     if (var5 != null) {
                        if (var6) {
                           var7.append(", ");
                        }

                        var7.append(var5);
                        var6 = true;
                     }
                  } else if (var4 && var8.group("spaced") != null) {
                     var5 = this.locale.validateWord(var8.group("spaced").replaceAll("\\s+", ""), false);
                     if (var5 != null) {
                        if (var6) {
                           var7.append(", ");
                        }

                        var7.append(var5);
                        var6 = true;
                     }
                  }
               }

               return var6 ? var7.toString() : null;
            }
         } catch (Exception var9) {
            System.out.println("Profanity validate string failed for: " + var1);
            var9.printStackTrace();
         }
      }

      return "Failed to parse string :(.";
   }

   static {
      LOCALES_DIR = "media" + File.separator + "profanity" + File.separator + "locales" + File.separator;
   }
}
