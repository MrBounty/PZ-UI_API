package zombie.core.skinnedmodel.population;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import zombie.ZomboidFileSystem;
import zombie.characters.HairOutfitDefinitions;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.gameStates.ChooseGameInfo;

@XmlRootElement
public class BeardStyles {
   @XmlElement(
      name = "style"
   )
   public final ArrayList m_Styles = new ArrayList();
   @XmlTransient
   public static BeardStyles instance;

   public static void init() {
      String var10000 = ZomboidFileSystem.instance.base.getAbsolutePath();
      instance = Parse(var10000 + File.separator + ZomboidFileSystem.processFilePath("media/hairStyles/beardStyles.xml", File.separatorChar));
      if (instance != null) {
         instance.m_Styles.add(0, new BeardStyle());
         Iterator var0 = ZomboidFileSystem.instance.getModIDs().iterator();

         while(true) {
            String var1;
            BeardStyles var4;
            do {
               ChooseGameInfo.Mod var2;
               do {
                  if (!var0.hasNext()) {
                     return;
                  }

                  var1 = (String)var0.next();
                  var2 = ChooseGameInfo.getAvailableModDetails(var1);
               } while(var2 == null);

               String var3 = ZomboidFileSystem.instance.getModDir(var1);
               var4 = Parse(var3 + File.separator + ZomboidFileSystem.processFilePath("media/hairStyles/beardStyles.xml", File.separatorChar));
            } while(var4 == null);

            Iterator var5 = var4.m_Styles.iterator();

            while(var5.hasNext()) {
               BeardStyle var6 = (BeardStyle)var5.next();
               BeardStyle var7 = instance.FindStyle(var6.name);
               if (var7 == null) {
                  instance.m_Styles.add(var6);
               } else {
                  if (DebugLog.isEnabled(DebugType.Clothing)) {
                     DebugLog.Clothing.println("mod \"%s\" overrides beard \"%s\"", var1, var6.name);
                  }

                  int var8 = instance.m_Styles.indexOf(var7);
                  instance.m_Styles.set(var8, var6);
               }
            }
         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.m_Styles.clear();
         instance = null;
      }
   }

   public static BeardStyles Parse(String var0) {
      try {
         return parse(var0);
      } catch (FileNotFoundException var2) {
      } catch (IOException | JAXBException var3) {
         ExceptionLogger.logException(var3);
      }

      return null;
   }

   public static BeardStyles parse(String var0) throws JAXBException, IOException {
      FileInputStream var1 = new FileInputStream(var0);

      BeardStyles var4;
      try {
         JAXBContext var2 = JAXBContext.newInstance(new Class[]{BeardStyles.class});
         Unmarshaller var3 = var2.createUnmarshaller();
         var4 = (BeardStyles)var3.unmarshal(var1);
      } catch (Throwable var6) {
         try {
            var1.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var1.close();
      return var4;
   }

   public BeardStyle FindStyle(String var1) {
      for(int var2 = 0; var2 < this.m_Styles.size(); ++var2) {
         BeardStyle var3 = (BeardStyle)this.m_Styles.get(var2);
         if (var3.name.equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public String getRandomStyle(String var1) {
      return HairOutfitDefinitions.instance.getRandomBeard(var1, this.m_Styles);
   }

   public BeardStyles getInstance() {
      return instance;
   }

   public ArrayList getAllStyles() {
      return this.m_Styles;
   }
}
