package zombie.profanity.locales;

import java.util.regex.Matcher;
import zombie.profanity.Phonizer;

public class LocaleGerman extends LocaleEnglish {
   public LocaleGerman(String var1) {
      super(var1);
   }

   protected void Init() {
      this.storeVowelsAmount = 3;
      super.Init();
      this.addPhonizer(new Phonizer("ringelS", "(?<ringelS>ÃŸ)") {
         public void execute(Matcher var1, StringBuffer var2) {
            if (var1.group(this.getName()) != null) {
               var1.appendReplacement(var2, "S");
            }

         }
      });
   }
}
