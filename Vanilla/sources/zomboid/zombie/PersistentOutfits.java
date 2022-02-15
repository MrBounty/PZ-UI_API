package zombie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.characters.ZombiesZoneDefinition;
import zombie.characters.AttachedItems.AttachedWeaponDefinitions;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.logger.ExceptionLogger;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.iso.IsoWorld;
import zombie.iso.SliceY;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.randomizedWorld.randomizedVehicleStory.RandomizedVehicleStoryBase;
import zombie.scripting.objects.Item;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public class PersistentOutfits {
   public static final PersistentOutfits instance = new PersistentOutfits();
   public static final int INVALID_ID = 0;
   public static final int FEMALE_BIT = Integer.MIN_VALUE;
   public static final int NO_HAT_BIT = 32768;
   private static final int FILE_VERSION_1 = 1;
   private static final int FILE_VERSION_LATEST = 1;
   private static final byte[] FILE_MAGIC = new byte[]{80, 83, 84, 90};
   private static final int NUM_SEEDS = 500;
   private final long[] m_seeds = new long[500];
   private final ArrayList m_outfitNames = new ArrayList();
   private final PersistentOutfits.DataList m_all = new PersistentOutfits.DataList();
   private final PersistentOutfits.DataList m_female = new PersistentOutfits.DataList();
   private final PersistentOutfits.DataList m_male = new PersistentOutfits.DataList();
   private final TreeMap m_outfitToData;
   private final TreeMap m_outfitToFemale;
   private final TreeMap m_outfitToMale;
   private static final ItemVisuals tempItemVisuals = new ItemVisuals();

   public PersistentOutfits() {
      this.m_outfitToData = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.m_outfitToFemale = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.m_outfitToMale = new TreeMap(String.CASE_INSENSITIVE_ORDER);
   }

   public void init() {
      this.m_all.clear();
      this.m_female.clear();
      this.m_male.clear();
      this.m_outfitToData.clear();
      this.m_outfitToFemale.clear();
      this.m_outfitToMale.clear();
      this.m_outfitNames.clear();
      if (!GameClient.bClient) {
         for(int var1 = 0; var1 < 500; ++var1) {
            this.m_seeds[var1] = (long)Rand.Next(Integer.MAX_VALUE);
         }
      }

      this.initOutfitList(OutfitManager.instance.m_FemaleOutfits, true);
      this.initOutfitList(OutfitManager.instance.m_MaleOutfits, false);
      this.registerCustomOutfits();
      if (!GameClient.bClient) {
         this.load();
         this.save();
      }
   }

   private void initOutfitList(ArrayList var1, boolean var2) {
      ArrayList var3 = new ArrayList(var1);
      var3.sort((var0, var1x) -> {
         return var0.m_Name.compareTo(var1x.m_Name);
      });
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Outfit var5 = (Outfit)var4.next();
         this.initOutfit(var5.m_Name, var2, true, PersistentOutfits::ApplyOutfit);
      }

   }

   private void initOutfit(String var1, boolean var2, boolean var3, PersistentOutfits.IOutfitter var4) {
      TreeMap var5 = var2 ? this.m_outfitToFemale : this.m_outfitToMale;
      PersistentOutfits.Data var6 = (PersistentOutfits.Data)this.m_outfitToData.get(var1);
      if (var6 == null) {
         var6 = new PersistentOutfits.Data();
         var6.m_index = (short)this.m_all.size();
         var6.m_outfitName = var1;
         var6.m_useSeed = var3;
         var6.m_outfitter = var4;
         this.m_outfitNames.add(var1);
         this.m_outfitToData.put(var1, var6);
         this.m_all.add(var6);
      }

      PersistentOutfits.DataList var7 = var2 ? this.m_female : this.m_male;
      var7.add(var6);
      var5.put(var1, var6);
   }

   private void registerCustomOutfits() {
      ArrayList var1 = IsoWorld.instance.getRandomizedVehicleStoryList();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         RandomizedVehicleStoryBase var3 = (RandomizedVehicleStoryBase)var1.get(var2);
         var3.registerCustomOutfits();
      }

      ZombiesZoneDefinition.registerCustomOutfits();
      if (GameServer.bServer || GameClient.bClient) {
         this.registerOutfitter("ReanimatedPlayer", false, SharedDescriptors::ApplyReanimatedPlayerOutfit);
      }

   }

   public ArrayList getOutfitNames() {
      return this.m_outfitNames;
   }

   public int pickRandomFemale() {
      if (this.m_female.isEmpty()) {
         return 0;
      } else {
         String var1 = ((PersistentOutfits.Data)PZArrayUtil.pickRandom((List)this.m_female)).m_outfitName;
         return this.pickOutfitFemale(var1);
      }
   }

   public int pickRandomMale() {
      if (this.m_male.isEmpty()) {
         return 0;
      } else {
         String var1 = ((PersistentOutfits.Data)PZArrayUtil.pickRandom((List)this.m_male)).m_outfitName;
         return this.pickOutfitMale(var1);
      }
   }

   public int pickOutfitFemale(String var1) {
      PersistentOutfits.Data var2 = (PersistentOutfits.Data)this.m_outfitToFemale.get(var1);
      if (var2 == null) {
         return 0;
      } else {
         short var3 = (short)var2.m_index;
         short var4 = var2.m_useSeed ? (short)Rand.Next(500) : 0;
         return Integer.MIN_VALUE | var3 << 16 | var4 + 1;
      }
   }

   public int pickOutfitMale(String var1) {
      PersistentOutfits.Data var2 = (PersistentOutfits.Data)this.m_outfitToMale.get(var1);
      if (var2 == null) {
         return 0;
      } else {
         short var3 = (short)var2.m_index;
         short var4 = var2.m_useSeed ? (short)Rand.Next(500) : 0;
         return var3 << 16 | var4 + 1;
      }
   }

   public int pickOutfit(String var1, boolean var2) {
      return var2 ? this.pickOutfitFemale(var1) : this.pickOutfitMale(var1);
   }

   public int getOutfit(int var1) {
      if (var1 == 0) {
         return 0;
      } else {
         int var2 = var1 & Integer.MIN_VALUE;
         var1 &= Integer.MAX_VALUE;
         int var3 = var1 & '耀';
         var1 &= -32769;
         short var4 = (short)(var1 >> 16);
         short var5 = (short)(var1 & '\uffff');
         if (var4 >= 0 && var4 < this.m_all.size()) {
            PersistentOutfits.Data var6 = (PersistentOutfits.Data)this.m_all.get(var4);
            if (var6.m_useSeed && (var5 < 1 || var5 > 500)) {
               var5 = (short)(Rand.Next(500) + 1);
            }

            return var2 | var3 | var4 << 16 | var5;
         } else {
            return 0;
         }
      }
   }

   public void save() {
      if (!Core.getInstance().isNoSave()) {
         File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("z_outfits.bin");

         try {
            FileOutputStream var2 = new FileOutputStream(var1);

            try {
               BufferedOutputStream var3 = new BufferedOutputStream(var2);

               try {
                  synchronized(SliceY.SliceBufferLock) {
                     SliceY.SliceBuffer.clear();
                     ByteBuffer var5 = SliceY.SliceBuffer;
                     this.save(var5);
                     var3.write(var5.array(), 0, var5.position());
                  }
               } catch (Throwable var10) {
                  try {
                     var3.close();
                  } catch (Throwable var8) {
                     var10.addSuppressed(var8);
                  }

                  throw var10;
               }

               var3.close();
            } catch (Throwable var11) {
               try {
                  var2.close();
               } catch (Throwable var7) {
                  var11.addSuppressed(var7);
               }

               throw var11;
            }

            var2.close();
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
         }

      }
   }

   public void save(ByteBuffer var1) {
      var1.put(FILE_MAGIC);
      var1.putInt(1);
      var1.putShort((short)500);

      for(int var2 = 0; var2 < 500; ++var2) {
         var1.putLong(this.m_seeds[var2]);
      }

   }

   public void load() {
      File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("z_outfits.bin");

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  ByteBuffer var5 = SliceY.SliceBuffer;
                  int var6 = var3.read(var5.array());
                  var5.limit(var6);
                  this.load(var5);
               }
            } catch (Throwable var11) {
               try {
                  var3.close();
               } catch (Throwable var9) {
                  var11.addSuppressed(var9);
               }

               throw var11;
            }

            var3.close();
         } catch (Throwable var12) {
            try {
               var2.close();
            } catch (Throwable var8) {
               var12.addSuppressed(var8);
            }

            throw var12;
         }

         var2.close();
      } catch (FileNotFoundException var13) {
      } catch (Exception var14) {
         ExceptionLogger.logException(var14);
      }

   }

   public void load(ByteBuffer var1) throws IOException {
      byte[] var2 = new byte[4];
      var1.get(var2);
      if (!Arrays.equals(var2, FILE_MAGIC)) {
         throw new IOException("not magic");
      } else {
         int var3 = var1.getInt();
         if (var3 >= 1 && var3 <= 1) {
            short var4 = var1.getShort();

            for(int var5 = 0; var5 < var4; ++var5) {
               if (var5 < 500) {
                  this.m_seeds[var5] = var1.getLong();
               }
            }

         }
      }
   }

   public void registerOutfitter(String var1, boolean var2, PersistentOutfits.IOutfitter var3) {
      this.initOutfit(var1, true, var2, var3);
      this.initOutfit(var1, false, var2, var3);
   }

   private static void ApplyOutfit(int var0, String var1, IsoGameCharacter var2) {
      instance.applyOutfit(var0, var1, var2);
   }

   private void applyOutfit(int var1, String var2, IsoGameCharacter var3) {
      boolean var4 = (var1 & Integer.MIN_VALUE) != 0;
      var1 &= Integer.MAX_VALUE;
      short var5 = (short)(var1 >> 16);
      PersistentOutfits.Data var6 = (PersistentOutfits.Data)this.m_all.get(var5);
      IsoZombie var7 = (IsoZombie)Type.tryCastTo(var3, IsoZombie.class);
      if (var7 != null) {
         var7.setFemaleEtc(var4);
      }

      var3.dressInNamedOutfit(var6.m_outfitName);
      if (var7 != null && var3.doDirtBloodEtc) {
         AttachedWeaponDefinitions.instance.addRandomAttachedWeapon(var7);
         var7.addRandomBloodDirtHolesEtc();
      }

      this.removeFallenHat(var1, var3);
   }

   public boolean isHatFallen(IsoGameCharacter var1) {
      return this.isHatFallen(var1.getPersistentOutfitID());
   }

   public boolean isHatFallen(int var1) {
      return (var1 & '耀') != 0;
   }

   public void setFallenHat(IsoGameCharacter var1, boolean var2) {
      int var3 = var1.getPersistentOutfitID();
      if (var3 != 0) {
         if (var2) {
            var3 |= 32768;
         } else {
            var3 &= -32769;
         }

         var1.setPersistentOutfitID(var3, var1.isPersistentOutfitInit());
      }
   }

   public boolean removeFallenHat(int var1, IsoGameCharacter var2) {
      if ((var1 & '耀') == 0) {
         return false;
      } else if (var2.isUsingWornItems()) {
         return false;
      } else {
         boolean var3 = false;
         var2.getItemVisuals(tempItemVisuals);

         for(int var4 = 0; var4 < tempItemVisuals.size(); ++var4) {
            ItemVisual var5 = (ItemVisual)tempItemVisuals.get(var4);
            Item var6 = var5.getScriptItem();
            if (var6 != null && var6.getChanceToFall() > 0) {
               var2.getItemVisuals().remove(var5);
               var3 = true;
            }
         }

         return var3;
      }
   }

   public void dressInOutfit(IsoGameCharacter var1, int var2) {
      var2 = this.getOutfit(var2);
      if (var2 != 0) {
         int var3 = var2 & 2147450879;
         short var4 = (short)(var3 >> 16);
         short var5 = (short)(var3 & '\uffff');
         PersistentOutfits.Data var6 = (PersistentOutfits.Data)this.m_all.get(var4);
         if (var6.m_useSeed) {
            OutfitRNG.setSeed(this.m_seeds[var5 - 1]);
         }

         var6.m_outfitter.accept(var2, var6.m_outfitName, var1);
      }
   }

   private static final class DataList extends ArrayList {
   }

   public interface IOutfitter {
      void accept(int var1, String var2, IsoGameCharacter var3);
   }

   private static final class Data {
      int m_index;
      String m_outfitName;
      boolean m_useSeed = true;
      PersistentOutfits.IOutfitter m_outfitter;
   }
}
