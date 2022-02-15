package zombie.profanity;

import java.util.regex.Matcher;

public class Phonizer {
   private String name;
   private String regex;

   public Phonizer(String var1, String var2) {
      this.name = var1;
      this.regex = var2;
   }

   public String getName() {
      return this.name;
   }

   public String getRegex() {
      return this.regex;
   }

   public void execute(Matcher var1, StringBuffer var2) {
      if (var1.group(this.name) != null) {
         var1.appendReplacement(var2, "${" + this.name + "}");
      }

   }
}
