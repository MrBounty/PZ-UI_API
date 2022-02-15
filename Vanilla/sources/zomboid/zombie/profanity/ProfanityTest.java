package zombie.profanity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import zombie.ZomboidFileSystem;
import zombie.profanity.locales.Locale;

public class ProfanityTest {
   public static void runTest() {
      ProfanityFilter var0 = ProfanityFilter.getInstance();
      System.out.println("");
      loadDictionary();
      testString(1, "profane stuff:  f u c k. sex xex h4rd ÃŸhit knight hello, @ $ $ H O L E   ass-hole f-u-c-k f_u_c_k_ @$$h0le fu'ckeerr: sdsi: KUNT as'as!! ffffuuuccckkkerrr");
   }

   public static void testString(int var0, String var1) {
      ProfanityFilter var2 = ProfanityFilter.getInstance();
      String var3 = "";
      System.out.println("Benchmarking " + var0 + " iterations: ");
      System.out.println("Original: " + var1);
      long var4 = System.nanoTime();

      for(int var6 = 0; var6 < var0; ++var6) {
         var3 = var2.filterString(var1);
      }

      long var10 = System.nanoTime();
      long var8 = var10 - var4;
      System.out.println("Done, time spent: " + (float)var8 / 1.0E9F + " seconds");
      System.out.println("Result: " + var3);
      System.out.println("");
   }

   public static void loadDictionary() {
      System.out.println("");
      System.out.println("Dictionary: ");
      long var0 = System.nanoTime();
      ProfanityFilter var2 = ProfanityFilter.getInstance();

      try {
         File var3 = ZomboidFileSystem.instance.getMediaFile("profanity" + File.separator + "Dictionary.txt");
         FileReader var4 = new FileReader(var3);
         BufferedReader var5 = new BufferedReader(var4);
         new StringBuffer();
         int var9 = 0;
         int var10 = 0;

         String var7;
         PrintStream var10000;
         for(Locale var11 = var2.getLocale(); (var7 = var5.readLine()) != null; ++var9) {
            String var8 = var11.returnMatchSetForWord(var7);
            if (var8 != null) {
               var10000 = System.out;
               String var10001 = var7.trim();
               var10000.println("Found match: " + var10001 + ", Phonized: " + var11.returnPhonizedWord(var7.trim()) + ", Set: " + var8);
               ++var10;
            }
         }

         var4.close();
         var10000 = System.out;
         int var15 = var2.getFilterWordsCount();
         var10000.println("Profanity filter tested " + var15 + " blacklisted words against " + var9 + " words from dictionary.");
         System.out.println("Found " + var10 + " matches.");
      } catch (IOException var12) {
         var12.printStackTrace();
      }

      long var13 = System.nanoTime();
      long var14 = var13 - var0;
      System.out.println("Done, time spent: " + (float)var14 / 1.0E9F + " seconds");
      System.out.println("");
   }
}
