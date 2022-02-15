package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.SandboxOptions;
import zombie.VirtualZombieManager;
import zombie.ZombieSpawnRecorder;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.SurvivorDesc;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.WeaponPart;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.SpawnPoints;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.randomizedWorld.RandomizedWorldBase;

public class RandomizedBuildingBase extends RandomizedWorldBase {
   private int chance = 0;
   private static int totalChance = 0;
   private static HashMap rbMap = new HashMap();
   protected static final int KBBuildingX = 10744;
   protected static final int KBBuildingY = 9409;
   private boolean alwaysDo = false;
   private static HashMap weaponsList = new HashMap();

   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
   }

   public void init() {
      if (weaponsList.isEmpty()) {
         weaponsList.put("Base.Shotgun", "Base.ShotgunShellsBox");
         weaponsList.put("Base.Pistol", "Base.Bullets9mmBox");
         weaponsList.put("Base.Pistol2", "Base.Bullets45Box");
         weaponsList.put("Base.Pistol3", "Base.Bullets44Box");
         weaponsList.put("Base.VarmintRifle", "Base.223Box");
         weaponsList.put("Base.HuntingRifle", "Base.308Box");
      }
   }

   public static void initAllRBMapChance() {
      for(int var0 = 0; var0 < IsoWorld.instance.getRandomizedBuildingList().size(); ++var0) {
         totalChance += ((RandomizedBuildingBase)IsoWorld.instance.getRandomizedBuildingList().get(var0)).getChance();
         rbMap.put((RandomizedBuildingBase)IsoWorld.instance.getRandomizedBuildingList().get(var0), ((RandomizedBuildingBase)IsoWorld.instance.getRandomizedBuildingList().get(var0)).getChance());
      }

   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      this.debugLine = "";
      if (GameClient.bClient) {
         return false;
      } else if (var1.isAllExplored() && !var2) {
         return false;
      } else {
         if (!GameServer.bServer) {
            if (!var2 && IsoPlayer.getInstance().getSquare() != null && IsoPlayer.getInstance().getSquare().getBuilding() != null && IsoPlayer.getInstance().getSquare().getBuilding().def == var1) {
               this.customizeStartingHouse(IsoPlayer.getInstance().getSquare().getBuilding().def);
               return false;
            }
         } else if (!var2) {
            for(int var3 = 0; var3 < GameServer.Players.size(); ++var3) {
               IsoPlayer var4 = (IsoPlayer)GameServer.Players.get(var3);
               if (var4.getSquare() != null && var4.getSquare().getBuilding() != null && var4.getSquare().getBuilding().def == var1) {
                  return false;
               }
            }
         }

         boolean var8 = false;
         boolean var9 = false;
         boolean var5 = false;

         for(int var6 = 0; var6 < var1.rooms.size(); ++var6) {
            RoomDef var7 = (RoomDef)var1.rooms.get(var6);
            if ("bedroom".equals(var7.name)) {
               var8 = true;
            }

            if ("kitchen".equals(var7.name) || "livingroom".equals(var7.name)) {
               var9 = true;
            }

            if ("bathroom".equals(var7.name)) {
               var5 = true;
            }
         }

         if (!var8) {
            this.debugLine = this.debugLine + "no bedroom ";
         }

         if (!var5) {
            this.debugLine = this.debugLine + "no bathroom ";
         }

         if (!var9) {
            this.debugLine = this.debugLine + "no living room or kitchen ";
         }

         return var8 && var5 && var9;
      }
   }

   private void customizeStartingHouse(BuildingDef var1) {
   }

   public int getMinimumDays() {
      return this.minimumDays;
   }

   public void setMinimumDays(int var1) {
      this.minimumDays = var1;
   }

   public int getMinimumRooms() {
      return this.minimumRooms;
   }

   public void setMinimumRooms(int var1) {
      this.minimumRooms = var1;
   }

   public static void ChunkLoaded(IsoBuilding var0) {
      if (!GameClient.bClient && var0.def != null && !var0.def.seen && var0.def.isFullyStreamedIn()) {
         if (GameServer.bServer && GameServer.Players.isEmpty()) {
            return;
         }

         for(int var1 = 0; var1 < var0.Rooms.size(); ++var1) {
            if (((IsoRoom)var0.Rooms.get(var1)).def.bExplored) {
               return;
            }
         }

         ArrayList var5 = new ArrayList();

         for(int var2 = 0; var2 < IsoWorld.instance.getRandomizedBuildingList().size(); ++var2) {
            RandomizedBuildingBase var3 = (RandomizedBuildingBase)IsoWorld.instance.getRandomizedBuildingList().get(var2);
            if (var3.isAlwaysDo() && var3.isValid(var0.def, false)) {
               var5.add(var3);
            }
         }

         var0.def.seen = true;
         if (var0.def.x == 10744 && var0.def.y == 9409 && Rand.Next(100) < 31) {
            RBKateAndBaldspot var8 = new RBKateAndBaldspot();
            var8.randomizeBuilding(var0.def);
            return;
         }

         RandomizedBuildingBase var6;
         if (!var5.isEmpty()) {
            var6 = (RandomizedBuildingBase)var5.get(Rand.Next(0, var5.size()));
            if (var6 != null) {
               var6.randomizeBuilding(var0.def);
               return;
            }
         }

         if (GameServer.bServer && SpawnPoints.instance.isSpawnBuilding(var0.getDef())) {
            return;
         }

         var6 = IsoWorld.instance.getRBBasic();
         if ("Tutorial".equals(Core.GameMode)) {
            return;
         }

         try {
            int var7 = 10;
            switch(SandboxOptions.instance.SurvivorHouseChance.getValue()) {
            case 1:
               return;
            case 2:
               var7 -= 5;
            case 3:
            default:
               break;
            case 4:
               var7 += 5;
               break;
            case 5:
               var7 += 10;
               break;
            case 6:
               var7 += 20;
            }

            if (Rand.Next(100) <= var7) {
               if (totalChance == 0) {
                  initAllRBMapChance();
               }

               var6 = getRandomStory();
               if (var6 == null) {
                  return;
               }
            }

            if (var6.isValid(var0.def, false) && var6.isTimeValid(false)) {
               var6.randomizeBuilding(var0.def);
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public int getChance() {
      return this.chance;
   }

   public void setChance(int var1) {
      this.chance = var1;
   }

   public boolean isAlwaysDo() {
      return this.alwaysDo;
   }

   public void setAlwaysDo(boolean var1) {
      this.alwaysDo = var1;
   }

   private static RandomizedBuildingBase getRandomStory() {
      int var0 = Rand.Next(totalChance);
      Iterator var1 = rbMap.keySet().iterator();
      int var2 = 0;

      RandomizedBuildingBase var3;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var3 = (RandomizedBuildingBase)var1.next();
         var2 += (Integer)rbMap.get(var3);
      } while(var0 >= var2);

      return var3;
   }

   public ArrayList addZombiesOnSquare(int var1, String var2, Integer var3, IsoGridSquare var4) {
      if (!IsoWorld.getZombiesDisabled() && !"Tutorial".equals(Core.GameMode)) {
         ArrayList var5 = new ArrayList();

         for(int var6 = 0; var6 < var1; ++var6) {
            VirtualZombieManager.instance.choices.clear();
            VirtualZombieManager.instance.choices.add(var4);
            IsoZombie var7 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
            if (var7 != null) {
               if ("Kate".equals(var2) || "Bob".equals(var2) || "Raider".equals(var2)) {
                  var7.doDirtBloodEtc = false;
               }

               if (var3 != null) {
                  var7.setFemaleEtc(Rand.Next(100) < var3);
               }

               if (var2 != null) {
                  var7.dressInPersistentOutfit(var2);
                  var7.bDressInRandomOutfit = false;
               } else {
                  var7.bDressInRandomOutfit = true;
               }

               var5.add(var7);
            }
         }

         ZombieSpawnRecorder.instance.record(var5, this.getClass().getSimpleName());
         return var5;
      } else {
         return null;
      }
   }

   public ArrayList addZombies(BuildingDef var1, int var2, String var3, Integer var4, RoomDef var5) {
      boolean var6 = var5 == null;
      ArrayList var7 = new ArrayList();
      if (!IsoWorld.getZombiesDisabled() && !"Tutorial".equals(Core.GameMode)) {
         if (var5 == null) {
            var5 = this.getRandomRoom(var1, 6);
         }

         int var8 = 2;
         int var9 = var5.area / 2;
         if (var2 == 0) {
            if (SandboxOptions.instance.Zombies.getValue() == 1) {
               var9 += 4;
            } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
               var9 += 3;
            } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
               var9 += 2;
            } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
               var9 -= 4;
            }

            if (var9 > 8) {
               var9 = 8;
            }

            if (var9 < var8) {
               var9 = var8 + 1;
            }
         } else {
            var8 = var2;
            var9 = var2;
         }

         int var10 = Rand.Next(var8, var9);

         for(int var11 = 0; var11 < var10; ++var11) {
            IsoGridSquare var12 = getRandomSpawnSquare(var5);
            if (var12 == null) {
               break;
            }

            VirtualZombieManager.instance.choices.clear();
            VirtualZombieManager.instance.choices.add(var12);
            IsoZombie var13 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
            if (var13 != null) {
               if (var4 != null) {
                  var13.setFemaleEtc(Rand.Next(100) < var4);
               }

               if (var3 != null) {
                  var13.dressInPersistentOutfit(var3);
                  var13.bDressInRandomOutfit = false;
               } else {
                  var13.bDressInRandomOutfit = true;
               }

               var7.add(var13);
               if (var6) {
                  var5 = this.getRandomRoom(var1, 6);
               }
            }
         }

         ZombieSpawnRecorder.instance.record(var7, this.getClass().getSimpleName());
         return var7;
      } else {
         return var7;
      }
   }

   public HandWeapon addRandomRangedWeapon(ItemContainer var1, boolean var2, boolean var3, boolean var4) {
      if (weaponsList == null || weaponsList.isEmpty()) {
         this.init();
      }

      ArrayList var5 = new ArrayList(weaponsList.keySet());
      String var6 = (String)var5.get(Rand.Next(0, var5.size()));
      HandWeapon var7 = this.addWeapon(var6, var2);
      if (var7 == null) {
         return null;
      } else {
         if (var3) {
            var1.addItem(InventoryItemFactory.CreateItem((String)weaponsList.get(var6)));
         }

         if (var4) {
            KahluaTable var8 = (KahluaTable)LuaManager.env.rawget("WeaponUpgrades");
            if (var8 == null) {
               return null;
            }

            KahluaTable var9 = (KahluaTable)var8.rawget(var7.getType());
            if (var9 == null) {
               return null;
            }

            int var10 = Rand.Next(1, var9.len() + 1);

            for(int var11 = 1; var11 <= var10; ++var11) {
               int var12 = Rand.Next(var9.len()) + 1;
               WeaponPart var13 = (WeaponPart)InventoryItemFactory.CreateItem((String)var9.rawget(var12));
               var7.attachWeaponPart(var13);
            }
         }

         return var7;
      }
   }

   public void spawnItemsInContainers(BuildingDef var1, String var2, int var3) {
      ArrayList var4 = new ArrayList();
      ItemPickerJava.ItemPickerRoom var5 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(var2);
      IsoCell var6 = IsoWorld.instance.CurrentCell;

      int var7;
      for(var7 = var1.x - 1; var7 < var1.x2 + 1; ++var7) {
         for(int var8 = var1.y - 1; var8 < var1.y2 + 1; ++var8) {
            for(int var9 = 0; var9 < 8; ++var9) {
               IsoGridSquare var10 = var6.getGridSquare(var7, var8, var9);
               if (var10 != null) {
                  for(int var11 = 0; var11 < var10.getObjects().size(); ++var11) {
                     IsoObject var12 = (IsoObject)var10.getObjects().get(var11);
                     if (Rand.Next(100) <= var3 && var12.getContainer() != null && var10.getRoom() != null && var10.getRoom().getName() != null && var5.Containers.containsKey(var12.getContainer().getType())) {
                        var12.getContainer().clear();
                        var4.add(var12.getContainer());
                        var12.getContainer().setExplored(true);
                     }
                  }
               }
            }
         }
      }

      for(var7 = 0; var7 < var4.size(); ++var7) {
         ItemContainer var13 = (ItemContainer)var4.get(var7);
         ItemPickerJava.fillContainerType(var5, var13, "", (IsoGameCharacter)null);
         ItemPickerJava.updateOverlaySprite(var13.getParent());
         if (GameServer.bServer) {
            GameServer.sendItemsInContainer(var13.getParent(), var13);
         }
      }

   }

   protected void removeAllZombies(BuildingDef var1) {
      for(int var2 = var1.x - 1; var2 < var1.x + var1.x2 + 1; ++var2) {
         for(int var3 = var1.y - 1; var3 < var1.y + var1.y2 + 1; ++var3) {
            for(int var4 = 0; var4 < 8; ++var4) {
               IsoGridSquare var5 = this.getSq(var2, var3, var4);
               if (var5 != null) {
                  for(int var6 = 0; var6 < var5.getMovingObjects().size(); ++var6) {
                     var5.getMovingObjects().remove(var6);
                     --var6;
                  }
               }
            }
         }
      }

   }

   public IsoWindow getWindow(IsoGridSquare var1) {
      for(int var2 = 0; var2 < var1.getObjects().size(); ++var2) {
         IsoObject var3 = (IsoObject)var1.getObjects().get(var2);
         if (var3 instanceof IsoWindow) {
            return (IsoWindow)var3;
         }
      }

      return null;
   }

   public IsoDoor getDoor(IsoGridSquare var1) {
      for(int var2 = 0; var2 < var1.getObjects().size(); ++var2) {
         IsoObject var3 = (IsoObject)var1.getObjects().get(var2);
         if (var3 instanceof IsoDoor) {
            return (IsoDoor)var3;
         }
      }

      return null;
   }

   public void addBarricade(IsoGridSquare var1, int var2) {
      for(int var3 = 0; var3 < var1.getObjects().size(); ++var3) {
         IsoObject var4 = (IsoObject)var1.getObjects().get(var3);
         IsoGridSquare var5;
         boolean var6;
         IsoBarricade var7;
         int var8;
         if (var4 instanceof IsoDoor) {
            if (!((IsoDoor)var4).isBarricadeAllowed()) {
               continue;
            }

            var5 = var1.getRoom() == null ? var1 : ((IsoDoor)var4).getOppositeSquare();
            if (var5 != null && var5.getRoom() == null) {
               var6 = var5 != var1;
               var7 = IsoBarricade.AddBarricadeToObject((IsoDoor)var4, var6);
               if (var7 != null) {
                  for(var8 = 0; var8 < var2; ++var8) {
                     var7.addPlank((IsoGameCharacter)null, (InventoryItem)null);
                  }

                  if (GameServer.bServer) {
                     var7.transmitCompleteItemToClients();
                  }
               }
            }
         }

         if (var4 instanceof IsoWindow && ((IsoWindow)var4).isBarricadeAllowed()) {
            var5 = var1.getRoom() == null ? var1 : ((IsoWindow)var4).getOppositeSquare();
            var6 = var5 != var1;
            var7 = IsoBarricade.AddBarricadeToObject((IsoWindow)var4, var6);
            if (var7 != null) {
               for(var8 = 0; var8 < var2; ++var8) {
                  var7.addPlank((IsoGameCharacter)null, (InventoryItem)null);
               }

               if (GameServer.bServer) {
                  var7.transmitCompleteItemToClients();
               }
            }
         }
      }

   }

   public InventoryItem addWorldItem(String var1, IsoGridSquare var2, float var3, float var4, float var5) {
      return this.addWorldItem(var1, var2, var3, var4, var5, 0);
   }

   public InventoryItem addWorldItem(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6) {
      if (var1 != null && var2 != null) {
         InventoryItem var7 = InventoryItemFactory.CreateItem(var1);
         if (var7 != null) {
            var7.setAutoAge();
            var7.setWorldZRotation(var6);
            if (var7 instanceof HandWeapon) {
               var7.setCondition(Rand.Next(2, var7.getConditionMax()));
            }

            return var2.AddWorldInventoryItem(var7, var3, var4, var5);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public InventoryItem addWorldItem(String var1, IsoGridSquare var2, IsoObject var3) {
      if (var1 != null && var2 != null) {
         float var4 = 0.0F;
         if (var3 != null) {
            var4 = var3.getSurfaceOffsetNoTable() / 96.0F;
         }

         InventoryItem var5 = InventoryItemFactory.CreateItem(var1);
         if (var5 != null) {
            var5.setAutoAge();
            return var2.AddWorldInventoryItem(var5, Rand.Next(0.3F, 0.9F), Rand.Next(0.3F, 0.9F), var4);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean isTableFor3DItems(IsoObject var1, IsoGridSquare var2) {
      return var1.getSurfaceOffsetNoTable() > 0.0F && var1.getContainer() == null && var2.getProperties().Val("waterAmount") == null && !var1.hasWater() && var1.getProperties().Val("BedType") == null;
   }

   public static final class HumanCorpse extends IsoGameCharacter implements IHumanVisual {
      final HumanVisual humanVisual = new HumanVisual(this);
      final ItemVisuals itemVisuals = new ItemVisuals();
      public boolean isSkeleton = false;

      public HumanCorpse(IsoCell var1, float var2, float var3, float var4) {
         super(var1, var2, var3, var4);
         var1.getObjectList().remove(this);
         var1.getAddList().remove(this);
      }

      public void dressInNamedOutfit(String var1) {
         this.getHumanVisual().dressInNamedOutfit(var1, this.itemVisuals);
         this.getHumanVisual().synchWithOutfit(this.getHumanVisual().getOutfit());
      }

      public HumanVisual getHumanVisual() {
         return this.humanVisual;
      }

      public HumanVisual getVisual() {
         return this.humanVisual;
      }

      public void Dressup(SurvivorDesc var1) {
         this.wornItems.setFromItemVisuals(this.itemVisuals);
         this.wornItems.addItemsToItemContainer(this.inventory);
      }

      public boolean isSkeleton() {
         return this.isSkeleton;
      }
   }
}
