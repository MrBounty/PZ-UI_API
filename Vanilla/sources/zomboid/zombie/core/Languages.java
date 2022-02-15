package zombie.core;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;
import zombie.gameStates.ChooseGameInfo;
import zombie.util.Lambda;
import zombie.util.list.PZArrayUtil;

public final class Languages {
   public static final Languages instance = new Languages();
   private final ArrayList m_languages = new ArrayList();
   private Language m_defaultLanguage = new Language(0, "EN", "English", "UTF-8", (String)null, false);

   public Languages() {
      this.m_languages.add(this.m_defaultLanguage);
   }

   public void init() {
      this.m_languages.clear();
      this.m_defaultLanguage = new Language(0, "EN", "English", "UTF-8", (String)null, false);
      this.m_languages.add(this.m_defaultLanguage);
      this.loadTranslateDirectory(ZomboidFileSystem.instance.getMediaPath("lua/shared/Translate"));
      Iterator var1 = ZomboidFileSystem.instance.getModIDs().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         ChooseGameInfo.Mod var3 = ChooseGameInfo.getAvailableModDetails(var2);
         if (var3 != null) {
            File var4 = new File(var3.getDir(), "media/lua/shared/Translate");
            if (var4.isDirectory()) {
               this.loadTranslateDirectory(var4.getAbsolutePath());
            }
         }
      }

   }

   public Language getDefaultLanguage() {
      return this.m_defaultLanguage;
   }

   public int getNumLanguages() {
      return this.m_languages.size();
   }

   public Language getByIndex(int var1) {
      return var1 >= 0 && var1 < this.m_languages.size() ? (Language)this.m_languages.get(var1) : null;
   }

   public Language getByName(String var1) {
      return (Language)PZArrayUtil.find((List)this.m_languages, Lambda.predicate(var1, (var0, var1x) -> {
         return var0.name().equalsIgnoreCase(var1x);
      }));
   }

   public int getIndexByName(String var1) {
      return PZArrayUtil.indexOf((List)this.m_languages, Lambda.predicate(var1, (var0, var1x) -> {
         return var0.name().equalsIgnoreCase(var1x);
      }));
   }

   private void loadTranslateDirectory(String var1) {
      Filter var2 = (var0) -> {
         return Files.isDirectory(var0, new LinkOption[0]) && Files.exists(var0.resolve("language.txt"), new LinkOption[0]);
      };
      Path var3 = FileSystems.getDefault().getPath(var1);
      if (Files.exists(var3, new LinkOption[0])) {
         try {
            DirectoryStream var4 = Files.newDirectoryStream(var3, var2);

            try {
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  Path var6 = (Path)var5.next();
                  LanguageFileData var7 = this.loadLanguageDirectory(var6.toAbsolutePath());
                  if (var7 != null) {
                     int var8 = this.getIndexByName(var7.name);
                     Language var9;
                     if (var8 == -1) {
                        var9 = new Language(this.m_languages.size(), var7.name, var7.text, var7.charset, var7.base, var7.azerty);
                        this.m_languages.add(var9);
                     } else {
                        var9 = new Language(var8, var7.name, var7.text, var7.charset, var7.base, var7.azerty);
                        this.m_languages.set(var8, var9);
                        if (var7.name.equals(this.m_defaultLanguage.name())) {
                           this.m_defaultLanguage = var9;
                        }
                     }
                  }
               }
            } catch (Throwable var11) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
         }

      }
   }

   private LanguageFileData loadLanguageDirectory(Path var1) {
      String var2 = var1.getFileName().toString();
      LanguageFileData var3 = new LanguageFileData();
      var3.name = var2;
      LanguageFile var4 = new LanguageFile();
      String var5 = var1.resolve("language.txt").toString();
      return !var4.read(var5, var3) ? null : var3;
   }
}
