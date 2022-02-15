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
public class HairStyles {
   @XmlElement(
      name = "male"
   )
   public final ArrayList m_MaleStyles = new ArrayList();
   @XmlElement(
      name = "female"
   )
   public final ArrayList m_FemaleStyles = new ArrayList();
   @XmlTransient
   public static HairStyles instance;

   public static void init() {
      String var10000 = ZomboidFileSystem.instance.base.getAbsolutePath();
      instance = Parse(var10000 + File.separator + ZomboidFileSystem.processFilePath("media/hairStyles/hairStyles.xml", File.separatorChar));
      if (instance != null) {
         Iterator var0 = ZomboidFileSystem.instance.getModIDs().iterator();

         while(true) {
            String var1;
            HairStyles var4;
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
               var4 = Parse(var3 + File.separator + ZomboidFileSystem.processFilePath("media/hairStyles/hairStyles.xml", File.separatorChar));
            } while(var4 == null);

            Iterator var5 = var4.m_FemaleStyles.iterator();

            HairStyle var6;
            HairStyle var7;
            int var8;
            while(var5.hasNext()) {
               var6 = (HairStyle)var5.next();
               var7 = instance.FindFemaleStyle(var6.name);
               if (var7 == null) {
                  instance.m_FemaleStyles.add(var6);
               } else {
                  if (DebugLog.isEnabled(DebugType.Clothing)) {
                     DebugLog.Clothing.println("mod \"%s\" overrides hair \"%s\"", var1, var6.name);
                  }

                  var8 = instance.m_FemaleStyles.indexOf(var7);
                  instance.m_FemaleStyles.set(var8, var6);
               }
            }

            var5 = var4.m_MaleStyles.iterator();

            while(var5.hasNext()) {
               var6 = (HairStyle)var5.next();
               var7 = instance.FindMaleStyle(var6.name);
               if (var7 == null) {
                  instance.m_MaleStyles.add(var6);
               } else {
                  if (DebugLog.isEnabled(DebugType.Clothing)) {
                     DebugLog.Clothing.println("mod \"%s\" overrides hair \"%s\"", var1, var6.name);
                  }

                  var8 = instance.m_MaleStyles.indexOf(var7);
                  instance.m_MaleStyles.set(var8, var6);
               }
            }
         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.m_FemaleStyles.clear();
         instance.m_MaleStyles.clear();
         instance = null;
      }
   }

   public static HairStyles Parse(String var0) {
      try {
         return parse(var0);
      } catch (FileNotFoundException var2) {
      } catch (IOException | JAXBException var3) {
         ExceptionLogger.logException(var3);
      }

      return null;
   }

   public static HairStyles parse(String var0) throws JAXBException, IOException {
      FileInputStream var1 = new FileInputStream(var0);

      HairStyles var4;
      try {
         JAXBContext var2 = JAXBContext.newInstance(new Class[]{HairStyles.class});
         Unmarshaller var3 = var2.createUnmarshaller();
         var4 = (HairStyles)var3.unmarshal(var1);
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

   public HairStyle FindMaleStyle(String var1) {
      return this.FindStyle(this.m_MaleStyles, var1);
   }

   public HairStyle FindFemaleStyle(String var1) {
      return this.FindStyle(this.m_FemaleStyles, var1);
   }

   private HairStyle FindStyle(ArrayList var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         HairStyle var4 = (HairStyle)var1.get(var3);
         if (var4.name.equalsIgnoreCase(var2)) {
            return var4;
         }

         if ("".equals(var2) && var4.name.equalsIgnoreCase("bald")) {
            return var4;
         }
      }

      return null;
   }

   public String getRandomMaleStyle(String var1) {
      return HairOutfitDefinitions.instance.getRandomHaircut(var1, this.m_MaleStyles);
   }

   public String getRandomFemaleStyle(String var1) {
      return HairOutfitDefinitions.instance.getRandomHaircut(var1, this.m_FemaleStyles);
   }

   public HairStyle getAlternateForHat(HairStyle var1, String var2) {
      if (!"nohair".equalsIgnoreCase(var2) && !"nohairnobeard".equalsIgnoreCase(var2)) {
         if (this.m_FemaleStyles.contains(var1)) {
            return this.FindFemaleStyle(var1.getAlternate(var2));
         } else {
            return this.m_MaleStyles.contains(var1) ? this.FindMaleStyle(var1.getAlternate(var2)) : var1;
         }
      } else {
         return null;
      }
   }

   public ArrayList getAllMaleStyles() {
      return this.m_MaleStyles;
   }

   public ArrayList getAllFemaleStyles() {
      return this.m_FemaleStyles;
   }
}
