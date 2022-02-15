package zombie.randomizedWorld.randomizedBuilding;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import se.krka.kahlua.vm.KahluaTable;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoRadio;
import zombie.iso.objects.IsoStove;
import zombie.iso.objects.IsoTelevision;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameServer;
import zombie.randomizedWorld.randomizedBuilding.TableStories.RBTableStoryBase;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBandPractice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBathroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBedroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBleach;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSCorpsePsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSDeadDrunk;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSFootballNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunmanInBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunslinger;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHenDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHockeyPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHouseParty;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPokerNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPoliceAtHouse;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscape;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscapeWithPolice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSkeletonPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSpecificProfession;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStagDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStudentNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSuicidePact;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSTinFoilHat;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombieLockedBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombiesEating;
import zombie.randomizedWorld.randomizedDeadSurvivor.RandomizedDeadSurvivorBase;

public final class RBBasic extends RandomizedBuildingBase {
   private final ArrayList specificProfessionDistribution = new ArrayList();
   private final Map specificProfessionRoomDistribution = new HashMap();
   private static final HashMap kitchenSinkItems = new HashMap();
   private static final HashMap kitchenCounterItems = new HashMap();
   private static final HashMap kitchenStoveItems = new HashMap();
   private static final HashMap bathroomSinkItems = new HashMap();
   private final ArrayList coldFood = new ArrayList();
   private final Map plankStash = new HashMap();
   private final ArrayList deadSurvivorsStory = new ArrayList();
   private int totalChanceRDS = 0;
   private static final HashMap rdsMap = new HashMap();
   private static final ArrayList uniqueRDSSpawned = new ArrayList();
   private ArrayList tablesDone = new ArrayList();
   private boolean doneTable = false;

   public void randomizeBuilding(BuildingDef var1) {
      this.tablesDone = new ArrayList();
      boolean var2 = Rand.Next(100) <= 20;
      ArrayList var3 = new ArrayList();
      String var4 = (String)this.specificProfessionDistribution.get(Rand.Next(0, this.specificProfessionDistribution.size()));
      ItemPickerJava.ItemPickerRoom var5 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(var4);
      IsoCell var6 = IsoWorld.instance.CurrentCell;
      boolean var7 = Rand.NextBool(9);

      int var8;
      for(var8 = var1.x - 1; var8 < var1.x2 + 1; ++var8) {
         for(int var9 = var1.y - 1; var9 < var1.y2 + 1; ++var9) {
            for(int var10 = 0; var10 < 8; ++var10) {
               IsoGridSquare var11 = var6.getGridSquare(var8, var9, var10);
               if (var11 != null) {
                  if (var7 && var11.getFloor() != null && this.plankStash.containsKey(var11.getFloor().getSprite().getName())) {
                     IsoThumpable var12 = new IsoThumpable(var11.getCell(), var11, (String)this.plankStash.get(var11.getFloor().getSprite().getName()), false, (KahluaTable)null);
                     var12.setIsThumpable(false);
                     var12.container = new ItemContainer("plankstash", var11, var12);
                     var11.AddSpecialObject(var12);
                     var11.RecalcAllWithNeighbours(true);
                     var7 = false;
                  }

                  for(int var17 = 0; var17 < var11.getObjects().size(); ++var17) {
                     IsoObject var13 = (IsoObject)var11.getObjects().get(var17);
                     if (Rand.Next(100) <= 65 && var13 instanceof IsoDoor && !((IsoDoor)var13).isExteriorDoor((IsoGameCharacter)null)) {
                        ((IsoDoor)var13).ToggleDoorSilent();
                        ((IsoDoor)var13).syncIsoObject(true, (byte)1, (UdpConnection)null, (ByteBuffer)null);
                     }

                     if (var13 instanceof IsoWindow) {
                        IsoWindow var14 = (IsoWindow)var13;
                        if (Rand.NextBool(80)) {
                           var1.bAlarmed = false;
                           var14.ToggleWindow((IsoGameCharacter)null);
                        }

                        IsoCurtain var15 = var14.HasCurtains();
                        if (var15 != null && Rand.NextBool(15)) {
                           var15.ToggleDoorSilent();
                        }
                     }

                     if (var2 && Rand.Next(100) <= 70 && var13.getContainer() != null && var11.getRoom() != null && var11.getRoom().getName() != null && ((String)this.specificProfessionRoomDistribution.get(var4)).contains(var11.getRoom().getName()) && var5.Containers.containsKey(var13.getContainer().getType())) {
                        var13.getContainer().clear();
                        var3.add(var13.getContainer());
                        var13.getContainer().setExplored(true);
                     }

                     if (Rand.Next(100) < 15 && var13.getContainer() != null && var13.getContainer().getType().equals("stove")) {
                        InventoryItem var18 = var13.getContainer().AddItem((String)this.coldFood.get(Rand.Next(0, this.coldFood.size())));
                        var18.setCooked(true);
                        var18.setAutoAge();
                     }

                     if (!this.tablesDone.contains(var13) && var13.getProperties().isTable() && var13.getProperties().getSurface() == 34 && var13.getContainer() == null && !this.doneTable) {
                        this.checkForTableSpawn(var1, var13);
                     }
                  }

                  if (var11.getRoom() != null && "kitchen".equals(var11.getRoom().getName())) {
                     this.doKitchenStuff(var11);
                  }

                  if (var11.getRoom() != null && "bathroom".equals(var11.getRoom().getName())) {
                     this.doBathroomStuff(var11);
                  }

                  if (var11.getRoom() != null && "bedroom".equals(var11.getRoom().getName())) {
                     this.doBedroomStuff(var11);
                  }

                  if (var11.getRoom() != null && "livingroom".equals(var11.getRoom().getName())) {
                     this.doLivingRoomStuff(var11);
                  }
               }
            }
         }
      }

      for(var8 = 0; var8 < var3.size(); ++var8) {
         ItemContainer var16 = (ItemContainer)var3.get(var8);
         ItemPickerJava.fillContainerType(var5, var16, "", (IsoGameCharacter)null);
         ItemPickerJava.updateOverlaySprite(var16.getParent());
         if (GameServer.bServer) {
            GameServer.sendItemsInContainer(var16.getParent(), var16);
         }
      }

      if (!var2 && Rand.Next(100) < 25) {
         this.addRandomDeadSurvivorStory(var1);
         var1.setAllExplored(true);
         var1.bAlarmed = false;
      }

      this.doneTable = false;
   }

   private void doLivingRoomStuff(IsoGridSquare var1) {
      IsoObject var2 = null;
      boolean var3 = false;

      int var4;
      for(var4 = 0; var4 < var1.getObjects().size(); ++var4) {
         IsoObject var5 = (IsoObject)var1.getObjects().get(var4);
         if (Rand.NextBool(5) && var5.getProperties().Val("BedType") == null && var5.getSurfaceOffsetNoTable() > 0.0F && var5.getSurfaceOffsetNoTable() < 30.0F && !(var5 instanceof IsoRadio)) {
            var2 = var5;
         }

         if (var5 instanceof IsoRadio || var5 instanceof IsoTelevision) {
            var3 = true;
            break;
         }
      }

      if (!var3 && var2 != null) {
         var4 = Rand.Next(0, 6);
         String var7 = "Base.TVRemote";
         switch(var4) {
         case 0:
            var7 = "Base.TVRemote";
            break;
         case 1:
            var7 = "Base.TVMagazine";
            break;
         case 2:
            var7 = "Base.Newspaper";
            break;
         case 3:
            var7 = "Base.VideoGame";
            break;
         case 4:
            var7 = "Base.Mugl";
            break;
         case 5:
            var7 = "Base.Headphones";
         }

         IsoDirections var6 = this.getFacing(var2.getSprite());
         if (var6 != null) {
            if (var6 == IsoDirections.E) {
               this.addWorldItem(var7, var1, 0.4F, Rand.Next(0.34F, 0.74F), var2.getSurfaceOffsetNoTable() / 96.0F);
            }

            if (var6 == IsoDirections.W) {
               this.addWorldItem(var7, var1, 0.64F, Rand.Next(0.34F, 0.74F), var2.getSurfaceOffsetNoTable() / 96.0F);
            }

            if (var6 == IsoDirections.N) {
               this.addWorldItem(var7, var1, Rand.Next(0.44F, 0.64F), 0.67F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }

            if (var6 == IsoDirections.S) {
               this.addWorldItem(var7, var1, Rand.Next(0.44F, 0.64F), 0.42F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }
      }

   }

   private void doBedroomStuff(IsoGridSquare var1) {
      for(int var2 = 0; var2 < var1.getObjects().size(); ++var2) {
         IsoObject var3 = (IsoObject)var1.getObjects().get(var2);
         if (var3.getSprite() == null || var3.getSprite().getName() == null) {
            return;
         }

         int var4;
         if (Rand.NextBool(7) && var3.getSprite().getName().contains("bedding") && var3.getProperties().Val("BedType") != null) {
            var4 = Rand.Next(0, 14);
            switch(var4) {
            case 0:
               this.addWorldItem("Shirt_FormalTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 1:
               this.addWorldItem("Shirt_FormalWhite_ShortSleeveTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem("Tshirt_DefaultDECAL_TINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem("Tshirt_PoloStripedTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem("Tshirt_PoloTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 5:
               this.addWorldItem("Jacket_WhiteTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 6:
               this.addWorldItem("Jumper_DiamondPatternTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 7:
               this.addWorldItem("Jumper_TankTopDiamondTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 8:
               this.addWorldItem("HoodieDOWN_WhiteTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 9:
               this.addWorldItem("Trousers_DefaultTEXTURE_TINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 10:
               this.addWorldItem("Trousers_WhiteTINT", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 11:
               this.addWorldItem("Trousers_Denim", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 12:
               this.addWorldItem("Trousers_Padded", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 13:
               this.addWorldItem("TrousersMesh_DenimLight", var1, 0.6F, 0.6F, var3.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (Rand.NextBool(7) && var3.getContainer() != null && "sidetable".equals(var3.getContainer().getType())) {
            var4 = Rand.Next(0, 4);
            String var5 = "Base.Book";
            switch(var4) {
            case 0:
               var5 = "Base.Book";
               break;
            case 1:
               var5 = "Base.Notebook";
               break;
            case 2:
               var5 = "Base.VideoGame";
               break;
            case 3:
               var5 = "Base.CDPlayer";
            }

            IsoDirections var6 = this.getFacing(var3.getSprite());
            if (var6 != null) {
               if (var6 == IsoDirections.E) {
                  this.addWorldItem(var5, var1, 0.42F, Rand.Next(0.34F, 0.74F), var3.getSurfaceOffsetNoTable() / 96.0F);
               }

               if (var6 == IsoDirections.W) {
                  this.addWorldItem(var5, var1, 0.64F, Rand.Next(0.34F, 0.74F), var3.getSurfaceOffsetNoTable() / 96.0F);
               }

               if (var6 == IsoDirections.N) {
                  this.addWorldItem(var5, var1, Rand.Next(0.44F, 0.64F), 0.67F, var3.getSurfaceOffsetNoTable() / 96.0F);
               }

               if (var6 == IsoDirections.S) {
                  this.addWorldItem(var5, var1, Rand.Next(0.44F, 0.64F), 0.42F, var3.getSurfaceOffsetNoTable() / 96.0F);
               }
            }

            return;
         }
      }

   }

   private void doKitchenStuff(IsoGridSquare var1) {
      boolean var2 = false;
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.getObjects().size(); ++var4) {
         IsoObject var5 = (IsoObject)var1.getObjects().get(var4);
         if (var5.getSprite() == null || var5.getSprite().getName() == null) {
            return;
         }

         IsoDirections var6;
         if (!var2 && var5.getSprite().getName().contains("sink") && Rand.NextBool(4)) {
            var6 = this.getFacing(var5.getSprite());
            if (var6 != null) {
               this.generateSinkClutter(var6, var5, var1, kitchenSinkItems);
               var2 = true;
            }
         } else if (!var3 && var5.getContainer() != null && "counter".equals(var5.getContainer().getType()) && Rand.NextBool(6)) {
            boolean var9 = true;

            for(int var7 = 0; var7 < var1.getObjects().size(); ++var7) {
               IsoObject var8 = (IsoObject)var1.getObjects().get(var7);
               if (var8.getSprite() != null && var8.getSprite().getName() != null && var8.getSprite().getName().contains("sink") || var8 instanceof IsoStove || var8 instanceof IsoRadio) {
                  var9 = false;
                  break;
               }
            }

            if (var9) {
               IsoDirections var10 = this.getFacing(var5.getSprite());
               if (var10 != null) {
                  this.generateCounterClutter(var10, var5, var1, kitchenCounterItems);
                  var3 = true;
               }
            }
         } else if (var5 instanceof IsoStove && var5.getContainer() != null && "stove".equals(var5.getContainer().getType()) && Rand.NextBool(4)) {
            var6 = this.getFacing(var5.getSprite());
            if (var6 != null) {
               this.generateKitchenStoveClutter(var6, var5, var1);
            }
         }
      }

   }

   private void doBathroomStuff(IsoGridSquare var1) {
      boolean var2 = false;
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.getObjects().size(); ++var4) {
         IsoObject var5 = (IsoObject)var1.getObjects().get(var4);
         if (var5.getSprite() == null || var5.getSprite().getName() == null) {
            return;
         }

         if (!var2 && !var3 && var5.getSprite().getName().contains("sink") && Rand.NextBool(5) && var5.getSurfaceOffsetNoTable() > 0.0F) {
            IsoDirections var9 = this.getFacing(var5.getSprite());
            if (var9 != null) {
               this.generateSinkClutter(var9, var5, var1, bathroomSinkItems);
               var2 = true;
            }
         } else if (!var2 && !var3 && var5.getContainer() != null && "counter".equals(var5.getContainer().getType()) && Rand.NextBool(5)) {
            boolean var6 = true;

            for(int var7 = 0; var7 < var1.getObjects().size(); ++var7) {
               IsoObject var8 = (IsoObject)var1.getObjects().get(var7);
               if (var8.getSprite() != null && var8.getSprite().getName() != null && var8.getSprite().getName().contains("sink") || var8 instanceof IsoStove || var8 instanceof IsoRadio) {
                  var6 = false;
                  break;
               }
            }

            if (var6) {
               IsoDirections var10 = this.getFacing(var5.getSprite());
               if (var10 != null) {
                  this.generateCounterClutter(var10, var5, var1, bathroomSinkItems);
                  var3 = true;
               }
            }
         }
      }

   }

   private void generateKitchenStoveClutter(IsoDirections var1, IsoObject var2, IsoGridSquare var3) {
      int var4 = Rand.Next(1, 3);
      String var5 = (String)kitchenStoveItems.get(Rand.Next(1, kitchenStoveItems.size()));
      if (var1 == IsoDirections.W) {
         switch(var4) {
         case 1:
            this.addWorldItem(var5, var3, 0.5703125F, 0.8046875F, var2.getSurfaceOffsetNoTable() / 96.0F);
            break;
         case 2:
            this.addWorldItem(var5, var3, 0.5703125F, 0.2578125F, var2.getSurfaceOffsetNoTable() / 96.0F);
         }
      }

      if (var1 == IsoDirections.E) {
         switch(var4) {
         case 1:
            this.addWorldItem(var5, var3, 0.5F, 0.7890625F, var2.getSurfaceOffsetNoTable() / 96.0F);
            break;
         case 2:
            this.addWorldItem(var5, var3, 0.5F, 0.1875F, var2.getSurfaceOffsetNoTable() / 96.0F);
         }
      }

      if (var1 == IsoDirections.S) {
         switch(var4) {
         case 1:
            this.addWorldItem(var5, var3, 0.3125F, 0.53125F, var2.getSurfaceOffsetNoTable() / 96.0F);
            break;
         case 2:
            this.addWorldItem(var5, var3, 0.875F, 0.53125F, var2.getSurfaceOffsetNoTable() / 96.0F);
         }
      }

      if (var1 == IsoDirections.N) {
         switch(var4) {
         case 1:
            this.addWorldItem(var5, var3, 0.3203F, 0.523475F, var2.getSurfaceOffsetNoTable() / 96.0F);
            break;
         case 2:
            this.addWorldItem(var5, var3, 0.8907F, 0.523475F, var2.getSurfaceOffsetNoTable() / 96.0F);
         }
      }

   }

   private void generateCounterClutter(IsoDirections var1, IsoObject var2, IsoGridSquare var3, HashMap var4) {
      int var5 = Math.min(5, var4.size() + 1);
      int var6 = Rand.Next(1, var5);
      ArrayList var7 = new ArrayList();

      int var9;
      for(int var8 = 0; var8 < var6; ++var8) {
         var9 = Rand.Next(1, 5);
         boolean var10 = false;

         while(!var10) {
            if (!var7.contains(var9)) {
               var7.add(var9);
               var10 = true;
            } else {
               var9 = Rand.Next(1, 5);
            }
         }

         if (var7.size() == 4) {
         }
      }

      ArrayList var13 = new ArrayList();

      for(var9 = 0; var9 < var7.size(); ++var9) {
         int var14 = (Integer)var7.get(var9);
         int var11 = Rand.Next(1, var4.size() + 1);
         String var12 = null;

         while(var12 == null) {
            var12 = (String)var4.get(var11);
            if (var13.contains(var12)) {
               var12 = null;
               var11 = Rand.Next(1, var4.size() + 1);
            }
         }

         var13.add(var12);
         if (var1 == IsoDirections.S) {
            switch(var14) {
            case 1:
               this.addWorldItem(var12, var3, 0.138F, Rand.Next(0.2F, 0.523F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem(var12, var3, 0.383F, Rand.Next(0.2F, 0.523F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem(var12, var3, 0.633F, Rand.Next(0.2F, 0.523F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem(var12, var3, 0.78F, Rand.Next(0.2F, 0.523F), var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.N) {
            switch(var14) {
            case 1:
               var3.AddWorldInventoryItem(var12, 0.133F, Rand.Next(0.53125F, 0.9375F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               var3.AddWorldInventoryItem(var12, 0.38F, Rand.Next(0.53125F, 0.9375F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               var3.AddWorldInventoryItem(var12, 0.625F, Rand.Next(0.53125F, 0.9375F), var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               var3.AddWorldInventoryItem(var12, 0.92F, Rand.Next(0.53125F, 0.9375F), var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.E) {
            switch(var14) {
            case 1:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.226F, 0.593F), 0.14F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.226F, 0.593F), 0.33F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.226F, 0.593F), 0.64F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.226F, 0.593F), 0.92F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.W) {
            switch(var14) {
            case 1:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.5859375F, 0.9F), 0.21875F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.5859375F, 0.9F), 0.421875F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.5859375F, 0.9F), 0.71F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               var3.AddWorldInventoryItem(var12, Rand.Next(0.5859375F, 0.9F), 0.9175F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }
      }

   }

   private void generateSinkClutter(IsoDirections var1, IsoObject var2, IsoGridSquare var3, HashMap var4) {
      int var5 = Math.min(5, var4.size() + 1);
      int var6 = Rand.Next(1, var5);
      ArrayList var7 = new ArrayList();

      int var9;
      for(int var8 = 0; var8 < var6; ++var8) {
         var9 = Rand.Next(1, 5);
         boolean var10 = false;

         while(!var10) {
            if (!var7.contains(var9)) {
               var7.add(var9);
               var10 = true;
            } else {
               var9 = Rand.Next(1, 5);
            }
         }

         if (var7.size() == 4) {
         }
      }

      ArrayList var13 = new ArrayList();

      for(var9 = 0; var9 < var7.size(); ++var9) {
         int var14 = (Integer)var7.get(var9);
         int var11 = Rand.Next(1, var4.size() + 1);
         String var12 = null;

         while(var12 == null) {
            var12 = (String)var4.get(var11);
            if (var13.contains(var12)) {
               var12 = null;
               var11 = Rand.Next(1, var4.size() + 1);
            }
         }

         var13.add(var12);
         if (var1 == IsoDirections.S) {
            switch(var14) {
            case 1:
               this.addWorldItem(var12, var3, 0.71875F, 0.125F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem(var12, var3, 0.0935F, 0.21875F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem(var12, var3, 0.1328125F, 0.589375F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem(var12, var3, 0.7890625F, 0.589375F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.N) {
            switch(var14) {
            case 1:
               this.addWorldItem(var12, var3, 0.921875F, 0.921875F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem(var12, var3, 0.1640625F, 0.8984375F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem(var12, var3, 0.021875F, 0.5F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem(var12, var3, 0.8671875F, 0.5F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.E) {
            switch(var14) {
            case 1:
               this.addWorldItem(var12, var3, 0.234375F, 0.859375F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem(var12, var3, 0.59375F, 0.875F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem(var12, var3, 0.53125F, 0.125F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem(var12, var3, 0.210937F, 0.1328125F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }

         if (var1 == IsoDirections.W) {
            switch(var14) {
            case 1:
               this.addWorldItem(var12, var3, 0.515625F, 0.109375F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 2:
               this.addWorldItem(var12, var3, 0.578125F, 0.890625F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 3:
               this.addWorldItem(var12, var3, 0.8828125F, 0.8984375F, var2.getSurfaceOffsetNoTable() / 96.0F);
               break;
            case 4:
               this.addWorldItem(var12, var3, 0.8671875F, 0.1653125F, var2.getSurfaceOffsetNoTable() / 96.0F);
            }
         }
      }

   }

   private IsoDirections getFacing(IsoSprite var1) {
      if (var1 != null && var1.getProperties().Is("Facing")) {
         String var2 = var1.getProperties().Val("Facing");
         byte var4 = -1;
         switch(var2.hashCode()) {
         case 69:
            if (var2.equals("E")) {
               var4 = 3;
            }
            break;
         case 78:
            if (var2.equals("N")) {
               var4 = 0;
            }
            break;
         case 83:
            if (var2.equals("S")) {
               var4 = 1;
            }
            break;
         case 87:
            if (var2.equals("W")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
            return IsoDirections.N;
         case 1:
            return IsoDirections.S;
         case 2:
            return IsoDirections.W;
         case 3:
            return IsoDirections.E;
         }
      }

      return null;
   }

   private void checkForTableSpawn(BuildingDef var1, IsoObject var2) {
      if (Rand.NextBool(10)) {
         RBTableStoryBase var3 = RBTableStoryBase.getRandomStory(var2.getSquare(), var2);
         if (var3 != null) {
            var3.randomizeBuilding(var1);
            this.doneTable = true;
         }
      }

   }

   private IsoObject checkForTable(IsoGridSquare var1, IsoObject var2) {
      if (!this.tablesDone.contains(var2) && var1 != null) {
         for(int var3 = 0; var3 < var1.getObjects().size(); ++var3) {
            IsoObject var4 = (IsoObject)var1.getObjects().get(var3);
            if (!this.tablesDone.contains(var4) && var4.getProperties().isTable() && var4.getProperties().getSurface() == 34 && var4.getContainer() == null && var4 != var2) {
               return var4;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void doProfessionStory(BuildingDef var1, String var2) {
      this.spawnItemsInContainers(var1, var2, 70);
   }

   private void addRandomDeadSurvivorStory(BuildingDef var1) {
      this.initRDSMap(var1);
      int var2 = Rand.Next(this.totalChanceRDS);
      Iterator var3 = rdsMap.keySet().iterator();
      int var4 = 0;

      while(var3.hasNext()) {
         RandomizedDeadSurvivorBase var5 = (RandomizedDeadSurvivorBase)var3.next();
         var4 += (Integer)rdsMap.get(var5);
         if (var2 < var4) {
            var5.randomizeDeadSurvivor(var1);
            if (var5.isUnique()) {
               getUniqueRDSSpawned().add(var5.getName());
            }
            break;
         }
      }

   }

   private void initRDSMap(BuildingDef var1) {
      this.totalChanceRDS = 0;
      rdsMap.clear();

      for(int var2 = 0; var2 < this.deadSurvivorsStory.size(); ++var2) {
         RandomizedDeadSurvivorBase var3 = (RandomizedDeadSurvivorBase)this.deadSurvivorsStory.get(var2);
         if (var3.isValid(var1, false) && var3.isTimeValid(false) && (var3.isUnique() && !getUniqueRDSSpawned().contains(var3.getName()) || !var3.isUnique())) {
            this.totalChanceRDS += ((RandomizedDeadSurvivorBase)this.deadSurvivorsStory.get(var2)).getChance();
            rdsMap.put((RandomizedDeadSurvivorBase)this.deadSurvivorsStory.get(var2), ((RandomizedDeadSurvivorBase)this.deadSurvivorsStory.get(var2)).getChance());
         }
      }

   }

   public void doRandomDeadSurvivorStory(BuildingDef var1, RandomizedDeadSurvivorBase var2) {
      var2.randomizeDeadSurvivor(var1);
   }

   public RBBasic() {
      this.name = "RBBasic";
      this.deadSurvivorsStory.add(new RDSBleach());
      this.deadSurvivorsStory.add(new RDSGunslinger());
      this.deadSurvivorsStory.add(new RDSGunmanInBathroom());
      this.deadSurvivorsStory.add(new RDSZombieLockedBathroom());
      this.deadSurvivorsStory.add(new RDSDeadDrunk());
      this.deadSurvivorsStory.add(new RDSSpecificProfession());
      this.deadSurvivorsStory.add(new RDSZombiesEating());
      this.deadSurvivorsStory.add(new RDSBandPractice());
      this.deadSurvivorsStory.add(new RDSBathroomZed());
      this.deadSurvivorsStory.add(new RDSBedroomZed());
      this.deadSurvivorsStory.add(new RDSFootballNight());
      this.deadSurvivorsStory.add(new RDSHenDo());
      this.deadSurvivorsStory.add(new RDSStagDo());
      this.deadSurvivorsStory.add(new RDSStudentNight());
      this.deadSurvivorsStory.add(new RDSPokerNight());
      this.deadSurvivorsStory.add(new RDSSuicidePact());
      this.deadSurvivorsStory.add(new RDSPrisonEscape());
      this.deadSurvivorsStory.add(new RDSPrisonEscapeWithPolice());
      this.deadSurvivorsStory.add(new RDSSkeletonPsycho());
      this.deadSurvivorsStory.add(new RDSCorpsePsycho());
      this.deadSurvivorsStory.add(new RDSPoliceAtHouse());
      this.deadSurvivorsStory.add(new RDSHouseParty());
      this.deadSurvivorsStory.add(new RDSTinFoilHat());
      this.deadSurvivorsStory.add(new RDSHockeyPsycho());
      this.specificProfessionDistribution.add("Carpenter");
      this.specificProfessionDistribution.add("Electrician");
      this.specificProfessionDistribution.add("Farmer");
      this.specificProfessionDistribution.add("Nurse");
      this.specificProfessionRoomDistribution.put("Carpenter", "kitchen");
      this.specificProfessionRoomDistribution.put("Electrician", "kitchen");
      this.specificProfessionRoomDistribution.put("Farmer", "kitchen");
      this.specificProfessionRoomDistribution.put("Nurse", "kitchen");
      this.specificProfessionRoomDistribution.put("Nurse", "bathroom");
      this.coldFood.add("Base.Chicken");
      this.coldFood.add("Base.Steak");
      this.coldFood.add("Base.PorkChop");
      this.coldFood.add("Base.MuttonChop");
      this.coldFood.add("Base.MeatPatty");
      this.coldFood.add("Base.FishFillet");
      this.coldFood.add("Base.Salmon");
      this.plankStash.put("floors_interior_tilesandwood_01_40", "floors_interior_tilesandwood_01_56");
      this.plankStash.put("floors_interior_tilesandwood_01_41", "floors_interior_tilesandwood_01_57");
      this.plankStash.put("floors_interior_tilesandwood_01_42", "floors_interior_tilesandwood_01_58");
      this.plankStash.put("floors_interior_tilesandwood_01_43", "floors_interior_tilesandwood_01_59");
      this.plankStash.put("floors_interior_tilesandwood_01_44", "floors_interior_tilesandwood_01_60");
      this.plankStash.put("floors_interior_tilesandwood_01_45", "floors_interior_tilesandwood_01_61");
      this.plankStash.put("floors_interior_tilesandwood_01_46", "floors_interior_tilesandwood_01_62");
      this.plankStash.put("floors_interior_tilesandwood_01_47", "floors_interior_tilesandwood_01_63");
      this.plankStash.put("floors_interior_tilesandwood_01_52", "floors_interior_tilesandwood_01_68");
      kitchenSinkItems.put(1, "Soap2");
      kitchenSinkItems.put(2, "CleaningLiquid2");
      kitchenSinkItems.put(3, "Sponge");
      kitchenCounterItems.put(1, "Dogfood");
      kitchenCounterItems.put(2, "CannedCorn");
      kitchenCounterItems.put(3, "CannedPeas");
      kitchenCounterItems.put(4, "CannedPotato2");
      kitchenCounterItems.put(5, "CannedSardines");
      kitchenCounterItems.put(6, "CannedTomato2");
      kitchenCounterItems.put(7, "CannedCarrots2");
      kitchenCounterItems.put(8, "CannedChili");
      kitchenCounterItems.put(9, "CannedBolognese");
      kitchenCounterItems.put(10, "TinOpener");
      kitchenCounterItems.put(11, "WaterBottleFull");
      kitchenCounterItems.put(12, "Cereal");
      kitchenCounterItems.put(13, "CerealBowl");
      kitchenCounterItems.put(14, "Spoon");
      kitchenCounterItems.put(15, "Fork");
      kitchenCounterItems.put(16, "KitchenKnife");
      kitchenCounterItems.put(17, "ButterKnife");
      kitchenCounterItems.put(18, "BreadKnife");
      kitchenCounterItems.put(19, "DishCloth");
      kitchenCounterItems.put(20, "RollingPin");
      kitchenCounterItems.put(21, "EmptyJar");
      kitchenCounterItems.put(22, "Bowl");
      kitchenCounterItems.put(23, "MugWhite");
      kitchenCounterItems.put(24, "MugRed");
      kitchenCounterItems.put(25, "Mugl");
      kitchenCounterItems.put(26, "WaterPot");
      kitchenCounterItems.put(27, "WaterSaucepan");
      kitchenCounterItems.put(28, "PotOfSoup");
      kitchenCounterItems.put(29, "StewBowl");
      kitchenCounterItems.put(30, "SoupBowl");
      kitchenCounterItems.put(31, "WaterSaucepanPasta");
      kitchenCounterItems.put(32, "WaterSaucepanRice");
      kitchenStoveItems.put(1, "WaterSaucepanRice");
      kitchenStoveItems.put(2, "WaterSaucepanPasta");
      kitchenStoveItems.put(3, "WaterPot");
      kitchenStoveItems.put(4, "PotOfSoup");
      kitchenStoveItems.put(5, "WaterSaucepan");
      kitchenStoveItems.put(6, "PotOfStew");
      kitchenStoveItems.put(7, "PastaPot");
      kitchenStoveItems.put(8, "RicePot");
      bathroomSinkItems.put(1, "Comb");
      bathroomSinkItems.put(2, "Cologne");
      bathroomSinkItems.put(3, "Antibiotics");
      bathroomSinkItems.put(4, "Bandage");
      bathroomSinkItems.put(5, "Pills");
      bathroomSinkItems.put(6, "PillsAntiDep");
      bathroomSinkItems.put(7, "PillsBeta");
      bathroomSinkItems.put(8, "PillsSleepingTablets");
      bathroomSinkItems.put(9, "PillsVitamins");
      bathroomSinkItems.put(10, "Lipstick");
      bathroomSinkItems.put(11, "MakeupEyeshadow");
      bathroomSinkItems.put(12, "MakeupFoundation");
      bathroomSinkItems.put(13, "Perfume");
      bathroomSinkItems.put(14, "Razor");
      bathroomSinkItems.put(15, "Toothbrush");
      bathroomSinkItems.put(16, "Toothpaste");
      bathroomSinkItems.put(17, "Tweezers");
   }

   public ArrayList getSurvivorStories() {
      return this.deadSurvivorsStory;
   }

   public ArrayList getSurvivorProfession() {
      return this.specificProfessionDistribution;
   }

   public static ArrayList getUniqueRDSSpawned() {
      return uniqueRDSSpawned;
   }
}
