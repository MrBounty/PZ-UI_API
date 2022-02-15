package zombie.profanity.locales;

import java.util.regex.Matcher;
import zombie.profanity.Phonizer;

public class LocaleEnglish extends Locale {
   public LocaleEnglish(String var1) {
      super(var1);
   }

   protected void Init() {
      this.storeVowelsAmount = 3;
      this.addFilterRawWord("ass");
      this.addPhonizer(new Phonizer("strt", "(?<strt>^(?:KN|GN|PN|AE|WR))") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, var1.group(this.getName()).toString().substring(1, 2));
            }

         }
      });
      this.addPhonizer(new Phonizer("dropY", "(?<dropY>(?<=M)B$)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "");
            }

         }
      });
      this.addPhonizer(new Phonizer("dropB", "(?<dropB>(?<=M)B$)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "");
            }

         }
      });
      this.addPhonizer(new Phonizer("z", "(?<z>Z)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "S");
            }

         }
      });
      this.addPhonizer(new Phonizer("ck", "(?<ck>CK)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "K");
            }

         }
      });
      this.addPhonizer(new Phonizer("q", "(?<q>Q)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "K");
            }

         }
      });
      this.addPhonizer(new Phonizer("v", "(?<v>V)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "F");
            }

         }
      });
      this.addPhonizer(new Phonizer("xS", "(?<xS>^X)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "S");
            }

         }
      });
      this.addPhonizer(new Phonizer("xKS", "(?<xKS>(?<=\\w)X)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "KS");
            }

         }
      });
      this.addPhonizer(new Phonizer("ph", "(?<ph>PH)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "F");
            }

         }
      });
      this.addPhonizer(new Phonizer("c", "(?<c>C(?=[AUOIE]))") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "K");
            }

         }
      });
   }
}
