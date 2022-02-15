package zombie.profanity.locales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.ZomboidFileSystem;
import zombie.profanity.Phonizer;
import zombie.profanity.ProfanityFilter;

public abstract class Locale {
   protected String id;
   protected int storeVowelsAmount = 3;
   protected String phoneticRules = "";
   protected Map phonizers = new HashMap();
   protected Map filterWords = new HashMap();
   protected List filterWordsRaw = new ArrayList();
   protected List filterContains = new ArrayList();
   protected ArrayList whitelistWords = new ArrayList();
   protected Pattern pattern;
   private Pattern preProcessLeet = Pattern.compile("(?<leet>[\\$@34701])\\k<leet>*|(?<nonWord>[^A-Z\\s\\$@34701]+)");
   private Pattern preProcessDoubles = Pattern.compile("(?<doublechar>[A-Z])\\k<doublechar>+");
   private Pattern preProcessVowels = Pattern.compile("(?<vowel>[AOUIE])");

   protected Locale(String var1) {
      this.id = var1;
      this.Init();
      this.finalizeData();
      this.loadFilterWords();
      this.loadFilterContains();
      this.loadWhiteListWords();
      ProfanityFilter.printDebug("Done init locale: " + this.id);
   }

   public String getID() {
      return this.id;
   }

   public String getPhoneticRules() {
      return this.phoneticRules;
   }

   public int getFilterWordsCount() {
      return this.filterWords.size();
   }

   protected abstract void Init();

   public void addWhiteListWord(String var1) {
      var1 = var1.toUpperCase().trim();
      if (!this.whitelistWords.contains(var1)) {
         this.whitelistWords.add(var1);
      }

   }

   public void removeWhiteListWord(String var1) {
      var1 = var1.toUpperCase().trim();
      if (this.whitelistWords.contains(var1)) {
         this.whitelistWords.remove(var1);
      }

   }

   public boolean isWhiteListedWord(String var1) {
      return this.whitelistWords.contains(var1.toUpperCase().trim());
   }

   public void addFilterWord(String var1) {
      String var2 = this.phonizeWord(var1);
      if (var2.length() > 2) {
         String var3 = "";
         if (this.filterWords.containsKey(var2)) {
            var3 = var3 + (String)this.filterWords.get(var2) + ",";
         }

         ProfanityFilter.printDebug("Adding word: " + var1 + ", Phonized: " + var2);
         this.filterWords.put(var2, var3 + var1.toLowerCase());
      } else {
         ProfanityFilter.printDebug("Refusing word: " + var1 + ", Phonized: " + var2 + ", null or phonized < 2 characters");
      }

   }

   public void removeFilterWord(String var1) {
      String var2 = this.phonizeWord(var1);
      if (this.filterWords.containsKey(var2)) {
         this.filterWords.remove(var2);
      }

   }

   public void addFilterContains(String var1) {
      if (var1 != null && !var1.isEmpty() && !this.filterContains.contains(var1.toUpperCase())) {
         this.filterContains.add(var1.toUpperCase());
      }

   }

   public void removeFilterContains(String var1) {
      this.filterContains.remove(var1.toUpperCase());
   }

   public void addFilterRawWord(String var1) {
      if (var1 != null && !var1.isEmpty() && !this.filterWordsRaw.contains(var1.toUpperCase())) {
         this.filterWordsRaw.add(var1.toUpperCase());
      }

   }

   public void removeFilterWordRaw(String var1) {
      this.filterWordsRaw.remove(var1.toUpperCase());
   }

   protected String repeatString(int var1, char var2) {
      char[] var3 = new char[var1];
      Arrays.fill(var3, var2);
      return new String(var3);
   }

   protected boolean containsIgnoreCase(String var1, String var2) {
      if (var1 != null && var2 != null) {
         int var3 = var2.length();
         if (var3 == 0) {
            return true;
         } else {
            for(int var4 = var1.length() - var3; var4 >= 0; --var4) {
               if (var1.regionMatches(true, var4, var2, 0, var3)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public String filterWord(String var1) {
      return this.filterWord(var1, false);
   }

   public String filterWord(String var1, boolean var2) {
      if (this.isWhiteListedWord(var1)) {
         return var1;
      } else {
         String var3 = this.phonizeWord(var1);
         if (this.filterWords.containsKey(var3)) {
            return (new String(new char[var1.length()])).replace('\u0000', '*');
         } else {
            if (this.filterWordsRaw.size() > 0) {
               for(int var4 = 0; var4 < this.filterWordsRaw.size(); ++var4) {
                  if (var1.equalsIgnoreCase((String)this.filterWordsRaw.get(var4))) {
                     return (new String(new char[var1.length()])).replace('\u0000', '*');
                  }
               }
            }

            if (var2) {
               for(int var5 = 0; var5 < this.filterContains.size(); ++var5) {
                  String var6 = (String)this.filterContains.get(var5);
                  if (this.containsIgnoreCase(var1, var6)) {
                     var1 = var1.replaceAll("(?i)" + Pattern.quote(var6), this.repeatString(var6.length(), '*'));
                  }
               }
            }

            return var1;
         }
      }
   }

   public String validateWord(String var1, boolean var2) {
      if (this.isWhiteListedWord(var1)) {
         return null;
      } else {
         String var3 = this.phonizeWord(var1);
         if (this.filterWords.containsKey(var3)) {
            return var1;
         } else {
            if (this.filterWordsRaw.size() > 0) {
               for(int var4 = 0; var4 < this.filterWordsRaw.size(); ++var4) {
                  if (var1.equalsIgnoreCase((String)this.filterWordsRaw.get(var4))) {
                     return var1;
                  }
               }
            }

            if (var2) {
               for(int var5 = 0; var5 < this.filterContains.size(); ++var5) {
                  String var6 = (String)this.filterContains.get(var5);
                  if (this.containsIgnoreCase(var1, var6)) {
                     return var6.toLowerCase();
                  }
               }
            }

            return null;
         }
      }
   }

   public String returnMatchSetForWord(String var1) {
      String var2 = this.phonizeWord(var1);
      return this.filterWords.containsKey(var2) ? (String)this.filterWords.get(var2) : null;
   }

   public String returnPhonizedWord(String var1) {
      return this.phonizeWord(var1);
   }

   protected String phonizeWord(String var1) {
      var1 = var1.toUpperCase().trim();
      if (this.whitelistWords.contains(var1)) {
         return var1;
      } else {
         var1 = this.preProcessWord(var1);
         if (this.phonizers.size() <= 0) {
            return var1;
         } else {
            Matcher var2 = this.pattern.matcher(var1);
            StringBuffer var3 = new StringBuffer();

            while(true) {
               while(var2.find()) {
                  Iterator var4 = this.phonizers.entrySet().iterator();

                  while(var4.hasNext()) {
                     Entry var5 = (Entry)var4.next();
                     if (var2.group((String)var5.getKey()) != null) {
                        ((Phonizer)var5.getValue()).execute(var2, var3);
                        break;
                     }
                  }
               }

               var2.appendTail(var3);
               return var3.toString();
            }
         }
      }
   }

   private String preProcessWord(String var1) {
      Matcher var2 = this.preProcessLeet.matcher(var1);
      StringBuffer var3 = new StringBuffer();

      while(var2.find()) {
         if (var2.group("leet") != null) {
            String var4 = var2.group("leet").toString();
            byte var5 = -1;
            switch(var4.hashCode()) {
            case 36:
               if (var4.equals("$")) {
                  var5 = 0;
               }
               break;
            case 48:
               if (var4.equals("0")) {
                  var5 = 5;
               }
               break;
            case 49:
               if (var4.equals("1")) {
                  var5 = 6;
               }
               break;
            case 51:
               if (var4.equals("3")) {
                  var5 = 3;
               }
               break;
            case 52:
               if (var4.equals("4")) {
                  var5 = 1;
               }
               break;
            case 55:
               if (var4.equals("7")) {
                  var5 = 4;
               }
               break;
            case 64:
               if (var4.equals("@")) {
                  var5 = 2;
               }
            }

            switch(var5) {
            case 0:
               var2.appendReplacement(var3, "S");
               break;
            case 1:
            case 2:
               var2.appendReplacement(var3, "A");
               break;
            case 3:
               var2.appendReplacement(var3, "E");
               break;
            case 4:
               var2.appendReplacement(var3, "T");
               break;
            case 5:
               var2.appendReplacement(var3, "O");
               break;
            case 6:
               var2.appendReplacement(var3, "I");
            }
         } else if (var2.group("nonWord") != null) {
            var2.appendReplacement(var3, "");
         }
      }

      var2.appendTail(var3);
      var2 = this.preProcessDoubles.matcher(var3.toString());
      var3.delete(0, var3.capacity());

      while(var2.find()) {
         if (var2.group("doublechar") != null) {
            var2.appendReplacement(var3, "${doublechar}");
         }
      }

      var2.appendTail(var3);
      var2 = this.preProcessVowels.matcher(var3.toString());
      var3.delete(0, var3.capacity());
      int var6 = 0;

      while(var2.find()) {
         if (var2.group("vowel") != null) {
            if (var6 < this.storeVowelsAmount) {
               var2.appendReplacement(var3, "${vowel}");
               ++var6;
            } else {
               var2.appendReplacement(var3, "");
            }
         }
      }

      var2.appendTail(var3);
      return var3.toString();
   }

   protected void addPhonizer(Phonizer var1) {
      if (var1 != null && !this.phonizers.containsKey(var1.getName())) {
         this.phonizers.put(var1.getName(), var1);
      }

   }

   protected void finalizeData() {
      this.phoneticRules = "";
      int var1 = this.phonizers.size();
      int var2 = 0;
      Iterator var3 = this.phonizers.values().iterator();

      while(var3.hasNext()) {
         Phonizer var4 = (Phonizer)var3.next();
         String var10001 = this.phoneticRules;
         this.phoneticRules = var10001 + var4.getRegex();
         ++var2;
         if (var2 < var1) {
            this.phoneticRules = this.phoneticRules + "|";
         }
      }

      ProfanityFilter.printDebug("PhoneticRules: " + this.phoneticRules);
      this.pattern = Pattern.compile(this.phoneticRules);
   }

   protected void loadFilterWords() {
      try {
         String var1 = ZomboidFileSystem.instance.getString(ProfanityFilter.LOCALES_DIR + "blacklist_" + this.id + ".txt");
         File var2 = new File(var1);
         FileReader var3 = new FileReader(var2);
         BufferedReader var4 = new BufferedReader(var3);

         String var5;
         int var6;
         for(var6 = 0; (var5 = var4.readLine()) != null; ++var6) {
            this.addFilterWord(var5);
         }

         var3.close();
         ProfanityFilter.printDebug("BlackList, " + var6 + " added.");
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   protected void loadFilterContains() {
      try {
         String var1 = ZomboidFileSystem.instance.getString(ProfanityFilter.LOCALES_DIR + "blacklist_contains_" + this.id + ".txt");
         File var2 = new File(var1);
         FileReader var3 = new FileReader(var2);
         BufferedReader var4 = new BufferedReader(var3);
         int var6 = 0;

         String var5;
         while((var5 = var4.readLine()) != null) {
            if (!var5.startsWith("//")) {
               this.addFilterContains(var5);
               ++var6;
            }
         }

         var3.close();
         ProfanityFilter.printDebug("BlackList contains, " + var6 + " added.");
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   protected void loadWhiteListWords() {
      try {
         String var1 = ZomboidFileSystem.instance.getString(ProfanityFilter.LOCALES_DIR + "whitelist_" + this.id + ".txt");
         File var2 = new File(var1);
         FileReader var3 = new FileReader(var2);
         BufferedReader var4 = new BufferedReader(var3);

         String var5;
         int var6;
         for(var6 = 0; (var5 = var4.readLine()) != null; ++var6) {
            this.addWhiteListWord(var5);
         }

         var3.close();
         ProfanityFilter.printDebug("WhiteList, " + var6 + " added.");
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }
}
