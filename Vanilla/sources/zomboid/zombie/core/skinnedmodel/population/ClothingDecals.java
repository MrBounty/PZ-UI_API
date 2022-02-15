package zombie.core.skinnedmodel.population;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.gameStates.ChooseGameInfo;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.StringUtils;

@XmlRootElement
public class ClothingDecals {
   @XmlElement(
      name = "group"
   )
   public final ArrayList m_Groups = new ArrayList();
   @XmlTransient
   public static ClothingDecals instance;
   private final HashMap m_cachedDecals = new HashMap();

   public static void init() {
      if (instance != null) {
         throw new IllegalStateException("ClothingDecals Already Initialized.");
      } else {
         String var10000 = ZomboidFileSystem.instance.base.getAbsolutePath();
         instance = Parse(var10000 + File.separator + ZomboidFileSystem.processFilePath("media/clothing/clothingDecals.xml", File.separatorChar));
         if (instance != null) {
            Iterator var0 = ZomboidFileSystem.instance.getModIDs().iterator();

            while(true) {
               String var1;
               ClothingDecals var4;
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
                  var4 = Parse(var3 + File.separator + ZomboidFileSystem.processFilePath("media/clothing/clothingDecals.xml", File.separatorChar));
               } while(var4 == null);

               Iterator var5 = var4.m_Groups.iterator();

               while(var5.hasNext()) {
                  ClothingDecalGroup var6 = (ClothingDecalGroup)var5.next();
                  ClothingDecalGroup var7 = instance.FindGroup(var6.m_Name);
                  if (var7 == null) {
                     instance.m_Groups.add(var6);
                  } else {
                     if (DebugLog.isEnabled(DebugType.Clothing)) {
                        DebugLog.Clothing.println("mod \"%s\" overrides decal group \"%s\"", var1, var6.m_Name);
                     }

                     int var8 = instance.m_Groups.indexOf(var7);
                     instance.m_Groups.set(var8, var6);
                  }
               }
            }
         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.m_cachedDecals.clear();
         instance.m_Groups.clear();
         instance = null;
      }
   }

   public static ClothingDecals Parse(String var0) {
      try {
         return parse(var0);
      } catch (FileNotFoundException var2) {
      } catch (JAXBException | IOException var3) {
         ExceptionLogger.logException(var3);
      }

      return null;
   }

   public static ClothingDecals parse(String var0) throws JAXBException, IOException {
      FileInputStream var1 = new FileInputStream(var0);

      ClothingDecals var4;
      try {
         JAXBContext var2 = JAXBContext.newInstance(new Class[]{ClothingDecals.class});
         Unmarshaller var3 = var2.createUnmarshaller();
         var4 = (ClothingDecals)var3.unmarshal(var1);
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

   public ClothingDecal getDecal(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         return null;
      } else {
         ClothingDecals.CachedDecal var2 = (ClothingDecals.CachedDecal)this.m_cachedDecals.get(var1);
         if (var2 == null) {
            var2 = new ClothingDecals.CachedDecal();
            this.m_cachedDecals.put(var1, var2);
         }

         if (var2.m_decal != null) {
            return var2.m_decal;
         } else {
            String var3 = ZomboidFileSystem.instance.getString("media/clothing/clothingDecals/" + var1 + ".xml");

            try {
               var2.m_decal = (ClothingDecal)PZXmlUtil.parse(ClothingDecal.class, var3);
               var2.m_decal.name = var1;
            } catch (PZXmlParserException var5) {
               System.err.println("Failed to load ClothingDecal: " + var3);
               ExceptionLogger.logException(var5);
               return null;
            }

            return var2.m_decal;
         }
      }
   }

   public ClothingDecalGroup FindGroup(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.m_Groups.size(); ++var2) {
            ClothingDecalGroup var3 = (ClothingDecalGroup)this.m_Groups.get(var2);
            if (var3.m_Name.equalsIgnoreCase(var1)) {
               return var3;
            }
         }

         return null;
      }
   }

   public String getRandomDecal(String var1) {
      ClothingDecalGroup var2 = this.FindGroup(var1);
      return var2 == null ? null : var2.getRandomDecal();
   }

   private static final class CachedDecal {
      ClothingDecal m_decal;
   }
}
