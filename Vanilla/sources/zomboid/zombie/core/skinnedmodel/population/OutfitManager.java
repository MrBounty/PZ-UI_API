package zombie.core.skinnedmodel.population;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import zombie.DebugFileWatcher;
import zombie.PredicatedFileWatcher;
import zombie.ZomboidFileSystem;
import zombie.asset.AssetPath;
import zombie.core.Rand;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.gameStates.ChooseGameInfo;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

@XmlRootElement
public class OutfitManager {
   public ArrayList m_MaleOutfits = new ArrayList();
   public ArrayList m_FemaleOutfits = new ArrayList();
   @XmlTransient
   public static OutfitManager instance;
   @XmlTransient
   private final Hashtable m_cachedClothingItems = new Hashtable();
   @XmlTransient
   private final ArrayList m_clothingItemListeners = new ArrayList();
   @XmlTransient
   private final TreeMap m_femaleOutfitMap;
   @XmlTransient
   private final TreeMap m_maleOutfitMap;

   public OutfitManager() {
      this.m_femaleOutfitMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.m_maleOutfitMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
   }

   public static void init() {
      if (instance != null) {
         throw new IllegalStateException("OutfitManager Already Initialized.");
      } else {
         instance = tryParse("game", "media/clothing/clothing.xml");
         if (instance != null) {
            instance.loaded();
         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.unload();
         instance = null;
      }
   }

   private void loaded() {
      Iterator var1 = ZomboidFileSystem.instance.getModIDs().iterator();

      while(true) {
         String var2;
         OutfitManager var4;
         do {
            ChooseGameInfo.Mod var3;
            do {
               if (!var1.hasNext()) {
                  DebugFileWatcher.instance.add(new PredicatedFileWatcher("media/clothing/clothing.xml", (var0) -> {
                     onClothingXmlFileChanged();
                  }));
                  this.loadAllClothingItems();
                  var1 = this.m_MaleOutfits.iterator();

                  Outfit var8;
                  Iterator var9;
                  ClothingItemReference var10;
                  while(var1.hasNext()) {
                     var8 = (Outfit)var1.next();
                     var8.m_Immutable = true;

                     for(var9 = var8.m_items.iterator(); var9.hasNext(); var10.m_Immutable = true) {
                        var10 = (ClothingItemReference)var9.next();
                     }
                  }

                  var1 = this.m_FemaleOutfits.iterator();

                  while(var1.hasNext()) {
                     var8 = (Outfit)var1.next();
                     var8.m_Immutable = true;

                     for(var9 = var8.m_items.iterator(); var9.hasNext(); var10.m_Immutable = true) {
                        var10 = (ClothingItemReference)var9.next();
                     }
                  }

                  Collections.shuffle(this.m_MaleOutfits);
                  Collections.shuffle(this.m_FemaleOutfits);
                  return;
               }

               var2 = (String)var1.next();
               var3 = ChooseGameInfo.getAvailableModDetails(var2);
            } while(var3 == null);

            var4 = tryParse(var2, "media/clothing/clothing.xml");
         } while(var4 == null);

         Iterator var5;
         Outfit var6;
         Outfit var7;
         for(var5 = var4.m_MaleOutfits.iterator(); var5.hasNext(); this.m_maleOutfitMap.put(var6.m_Name, var6)) {
            var6 = (Outfit)var5.next();
            var7 = this.FindMaleOutfit(var6.m_Name);
            if (var7 == null) {
               this.m_MaleOutfits.add(var6);
            } else {
               if (DebugLog.isEnabled(DebugType.Clothing)) {
                  DebugLog.Clothing.println("mod \"%s\" overrides male outfit \"%s\"", var2, var6.m_Name);
               }

               this.m_MaleOutfits.set(this.m_MaleOutfits.indexOf(var7), var6);
            }
         }

         for(var5 = var4.m_FemaleOutfits.iterator(); var5.hasNext(); this.m_femaleOutfitMap.put(var6.m_Name, var6)) {
            var6 = (Outfit)var5.next();
            var7 = this.FindFemaleOutfit(var6.m_Name);
            if (var7 == null) {
               this.m_FemaleOutfits.add(var6);
            } else {
               if (DebugLog.isEnabled(DebugType.Clothing)) {
                  DebugLog.Clothing.println("mod \"%s\" overrides female outfit \"%s\"", var2, var6.m_Name);
               }

               this.m_FemaleOutfits.set(this.m_FemaleOutfits.indexOf(var7), var6);
            }
         }
      }
   }

   private static void onClothingXmlFileChanged() {
      DebugLog.Clothing.println("OutfitManager.onClothingXmlFileChanged> Detected change in media/clothing/clothing.xml");
      Reload();
   }

   public static void Reload() {
      DebugLog.Clothing.println("Reloading OutfitManager");
      OutfitManager var0 = instance;
      instance = tryParse("game", "media/clothing/clothing.xml");
      if (instance != null) {
         instance.loaded();
      }

      if (var0 != null && instance != null) {
         instance.onReloaded(var0);
      }

   }

   private void onReloaded(OutfitManager var1) {
      PZArrayUtil.copy(this.m_clothingItemListeners, var1.m_clothingItemListeners);
      var1.unload();
      this.loadAllClothingItems();
   }

   private void unload() {
      Iterator var1 = this.m_cachedClothingItems.values().iterator();

      while(var1.hasNext()) {
         OutfitManager.ClothingItemEntry var2 = (OutfitManager.ClothingItemEntry)var1.next();
         DebugFileWatcher.instance.remove(var2.m_fileWatcher);
      }

      this.m_cachedClothingItems.clear();
      this.m_clothingItemListeners.clear();
   }

   public void addClothingItemListener(IClothingItemListener var1) {
      if (var1 != null) {
         if (!this.m_clothingItemListeners.contains(var1)) {
            this.m_clothingItemListeners.add(var1);
         }
      }
   }

   public void removeClothingItemListener(IClothingItemListener var1) {
      this.m_clothingItemListeners.remove(var1);
   }

   private void invokeClothingItemChangedEvent(String var1) {
      Iterator var2 = this.m_clothingItemListeners.iterator();

      while(var2.hasNext()) {
         IClothingItemListener var3 = (IClothingItemListener)var2.next();
         var3.clothingItemChanged(var1);
      }

   }

   public Outfit GetRandomOutfit(boolean var1) {
      Outfit var2;
      if (var1) {
         var2 = (Outfit)PZArrayUtil.pickRandom((List)this.m_FemaleOutfits);
      } else {
         var2 = (Outfit)PZArrayUtil.pickRandom((List)this.m_MaleOutfits);
      }

      return var2;
   }

   public Outfit GetRandomNonProfessionalOutfit(boolean var1) {
      int var10000 = Rand.Next(5);
      String var2 = "Generic0" + (var10000 + 1);
      if (Rand.NextBool(4)) {
         int var3;
         if (var1) {
            var3 = Rand.Next(3);
            switch(var3) {
            case 0:
               var2 = "Mannequin1";
               break;
            case 1:
               var2 = "Mannequin2";
               break;
            case 2:
               var2 = "Classy";
            }
         } else {
            var3 = Rand.Next(3);
            switch(var3) {
            case 0:
               var2 = "Classy";
               break;
            case 1:
               var2 = "Tourist";
               break;
            case 2:
               var2 = "MallSecurity";
            }
         }
      }

      return this.GetSpecificOutfit(var1, var2);
   }

   public Outfit GetSpecificOutfit(boolean var1, String var2) {
      Outfit var3;
      if (var1) {
         var3 = this.FindFemaleOutfit(var2);
      } else {
         var3 = this.FindMaleOutfit(var2);
      }

      return var3;
   }

   private static OutfitManager tryParse(String var0, String var1) {
      try {
         return parse(var0, var1);
      } catch (PZXmlParserException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private static OutfitManager parse(String var0, String var1) throws PZXmlParserException {
      if ("game".equals(var0)) {
         String var10000 = ZomboidFileSystem.instance.base.getAbsolutePath();
         var1 = var10000 + File.separator + ZomboidFileSystem.processFilePath(var1, File.separatorChar);
      } else {
         String var2 = ZomboidFileSystem.instance.getModDir(var0);
         var1 = var2 + File.separator + ZomboidFileSystem.processFilePath(var1, File.separatorChar);
      }

      if (!(new File(var1)).exists()) {
         return null;
      } else {
         OutfitManager var3 = (OutfitManager)PZXmlUtil.parse(OutfitManager.class, var1);
         if (var3 != null) {
            PZArrayUtil.forEach((List)var3.m_MaleOutfits, (var1x) -> {
               var1x.setModID(var0);
            });
            PZArrayUtil.forEach((List)var3.m_FemaleOutfits, (var1x) -> {
               var1x.setModID(var0);
            });
            PZArrayUtil.forEach((List)var3.m_MaleOutfits, (var1x) -> {
               var3.m_maleOutfitMap.put(var1x.m_Name, var1x);
            });
            PZArrayUtil.forEach((List)var3.m_FemaleOutfits, (var1x) -> {
               var3.m_femaleOutfitMap.put(var1x.m_Name, var1x);
            });
         }

         return var3;
      }
   }

   private static void tryWrite(OutfitManager var0, String var1) {
      try {
         write(var0, var1);
      } catch (IOException | JAXBException var3) {
         var3.printStackTrace();
      }

   }

   private static void write(OutfitManager var0, String var1) throws IOException, JAXBException {
      FileOutputStream var2 = new FileOutputStream(var1);

      try {
         JAXBContext var3 = JAXBContext.newInstance(new Class[]{OutfitManager.class});
         Marshaller var4 = var3.createMarshaller();
         var4.setProperty("jaxb.formatted.output", Boolean.TRUE);
         var4.marshal(var0, var2);
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
   }

   public Outfit FindMaleOutfit(String var1) {
      return (Outfit)this.m_maleOutfitMap.get(var1);
   }

   public Outfit FindFemaleOutfit(String var1) {
      return (Outfit)this.m_femaleOutfitMap.get(var1);
   }

   private Outfit FindOutfit(ArrayList var1, String var2) {
      Outfit var3 = null;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         Outfit var5 = (Outfit)var1.get(var4);
         if (var5.m_Name.equalsIgnoreCase(var2)) {
            var3 = var5;
            break;
         }
      }

      return var3;
   }

   public ClothingItem getClothingItem(String var1) {
      String var2 = ZomboidFileSystem.instance.getFilePathFromGuid(var1);
      if (var2 == null) {
         return null;
      } else {
         OutfitManager.ClothingItemEntry var3 = (OutfitManager.ClothingItemEntry)this.m_cachedClothingItems.get(var1);
         if (var3 == null) {
            var3 = new OutfitManager.ClothingItemEntry();
            var3.m_filePath = var2;
            var3.m_guid = var1;
            var3.m_item = null;
            this.m_cachedClothingItems.put(var1, var3);
         }

         if (var3.m_item != null) {
            var3.m_item.m_GUID = var1;
            return var3.m_item;
         } else {
            try {
               String var4 = ZomboidFileSystem.instance.resolveFileOrGUID(var2);
               var3.m_item = (ClothingItem)ClothingItemAssetManager.instance.load(new AssetPath(var4));
               var3.m_item.m_Name = this.extractClothingItemName(var2);
               var3.m_item.m_GUID = var1;
            } catch (Exception var6) {
               System.err.println("Failed to load ClothingItem: " + var2);
               ExceptionLogger.logException(var6);
               return null;
            }

            if (var3.m_fileWatcher == null) {
               String var5 = var3.m_filePath;
               if (!var1.startsWith("game-")) {
                  var5 = ZomboidFileSystem.instance.getString(var5);
               }

               var3.m_fileWatcher = new PredicatedFileWatcher(var5, (var2x) -> {
                  this.onClothingItemFileChanged(var3);
               });
               DebugFileWatcher.instance.add(var3.m_fileWatcher);
            }

            return var3.m_item;
         }
      }
   }

   private String extractClothingItemName(String var1) {
      String var2 = StringUtils.trimPrefix(var1, "media/clothing/clothingItems/");
      var2 = StringUtils.trimSuffix(var2, ".xml");
      return var2;
   }

   private void onClothingItemFileChanged(OutfitManager.ClothingItemEntry var1) {
      ClothingItemAssetManager.instance.reload(var1.m_item);
   }

   public void onClothingItemStateChanged(ClothingItem var1) {
      if (var1.isReady()) {
         this.invokeClothingItemChangedEvent(var1.m_GUID);
      }

   }

   public void loadAllClothingItems() {
      ArrayList var1 = ScriptManager.instance.getAllItems();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Item var3 = (Item)var1.get(var2);
         String var4;
         if (var3.replacePrimaryHand != null) {
            var4 = ZomboidFileSystem.instance.getGuidFromFilePath("media/clothing/clothingItems/" + var3.replacePrimaryHand.clothingItemName + ".xml");
            if (var4 != null) {
               var3.replacePrimaryHand.clothingItem = this.getClothingItem(var4);
            }
         }

         if (var3.replaceSecondHand != null) {
            var4 = ZomboidFileSystem.instance.getGuidFromFilePath("media/clothing/clothingItems/" + var3.replaceSecondHand.clothingItemName + ".xml");
            if (var4 != null) {
               var3.replaceSecondHand.clothingItem = this.getClothingItem(var4);
            }
         }

         if (!StringUtils.isNullOrWhitespace(var3.getClothingItem())) {
            var4 = ZomboidFileSystem.instance.getGuidFromFilePath("media/clothing/clothingItems/" + var3.getClothingItem() + ".xml");
            if (var4 != null) {
               ClothingItem var5 = this.getClothingItem(var4);
               var3.setClothingItemAsset(var5);
            }
         }
      }

   }

   public boolean isLoadingClothingItems() {
      Iterator var1 = this.m_cachedClothingItems.values().iterator();

      OutfitManager.ClothingItemEntry var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (OutfitManager.ClothingItemEntry)var1.next();
      } while(!var2.m_item.isEmpty());

      return true;
   }

   public void debugOutfits() {
      this.debugOutfits(this.m_FemaleOutfits);
      this.debugOutfits(this.m_MaleOutfits);
   }

   private void debugOutfits(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Outfit var3 = (Outfit)var2.next();
         this.debugOutfit(var3);
      }

   }

   private void debugOutfit(Outfit var1) {
      String var2 = null;
      Iterator var3 = var1.m_items.iterator();

      while(var3.hasNext()) {
         ClothingItemReference var4 = (ClothingItemReference)var3.next();
         ClothingItem var5 = this.getClothingItem(var4.itemGUID);
         if (var5 != null && !var5.isEmpty()) {
            String var6 = ScriptManager.instance.getItemTypeForClothingItem(var5.m_Name);
            if (var6 != null) {
               Item var7 = ScriptManager.instance.getItem(var6);
               if (var7 != null && var7.getType() == Item.Type.Container) {
                  String var8 = StringUtils.isNullOrWhitespace(var7.getBodyLocation()) ? var7.CanBeEquipped : var7.getBodyLocation();
                  if (var2 != null && var2.equals(var8)) {
                     DebugLog.Clothing.warn("outfit \"%s\" has multiple bags", var1.m_Name);
                  }

                  var2 = var8;
               }
            }
         }
      }

   }

   private static final class ClothingItemEntry {
      public ClothingItem m_item;
      public String m_guid;
      public String m_filePath;
      public PredicatedFileWatcher m_fileWatcher;
   }
}
