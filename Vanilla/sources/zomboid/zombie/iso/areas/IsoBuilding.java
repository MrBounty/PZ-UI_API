package zombie.iso.areas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.SurvivorDesc;
import zombie.core.Rand;
import zombie.core.opengl.RenderSettings;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemType;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.LotHeader;
import zombie.iso.RoomDef;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;

public final class IsoBuilding extends IsoArea {
   public Rectangle bounds;
   public final Vector Exits = new Vector();
   public boolean IsResidence = true;
   public final ArrayList container = new ArrayList();
   public final Vector Rooms = new Vector();
   public final Vector Windows = new Vector();
   public int ID = 0;
   public static int IDMax = 0;
   public int safety = 0;
   public int transparentWalls = 0;
   private boolean isToxic = false;
   public static float PoorBuildingScore = 10.0F;
   public static float GoodBuildingScore = 100.0F;
   public int scoreUpdate = -1;
   public BuildingDef def;
   public boolean bSeenInside = false;
   public ArrayList lights = new ArrayList();
   static ArrayList tempo = new ArrayList();
   static ArrayList tempContainer = new ArrayList();
   static ArrayList RandomContainerChoices = new ArrayList();
   static ArrayList windowchoices = new ArrayList();

   public int getRoomsNumber() {
      return this.Rooms.size();
   }

   public IsoBuilding() {
      this.ID = IDMax++;
      this.scoreUpdate = -120 + Rand.Next(120);
   }

   public int getID() {
      return this.ID;
   }

   public void TriggerAlarm() {
   }

   public IsoBuilding(IsoCell var1) {
      this.ID = IDMax++;
      this.scoreUpdate = -120 + Rand.Next(120);
   }

   public boolean ContainsAllItems(Stack var1) {
      return false;
   }

   public float ScoreBuildingPersonSpecific(SurvivorDesc var1, boolean var2) {
      float var3 = 0.0F;
      var3 += (float)(this.Rooms.size() * 5);
      var3 += (float)(this.Exits.size() * 15);
      var3 -= (float)(this.transparentWalls * 10);

      for(int var4 = 0; var4 < this.container.size(); ++var4) {
         ItemContainer var5 = (ItemContainer)this.container.get(var4);
         var3 += (float)(var5.Items.size() * 3);
      }

      BuildingScore var8;
      if (!IsoWorld.instance.CurrentCell.getBuildingScores().containsKey(this.ID)) {
         var8 = new BuildingScore(this);
         var8.building = this;
         IsoWorld.instance.CurrentCell.getBuildingScores().put(this.ID, var8);
         this.ScoreBuildingGeneral(var8);
      }

      var8 = (BuildingScore)IsoWorld.instance.CurrentCell.getBuildingScores().get(this.ID);
      var3 += (var8.defense + var8.food + (float)var8.size + var8.weapons + var8.wood) * 10.0F;
      int var9 = -10000;
      int var6 = -10000;
      if (!this.Exits.isEmpty()) {
         IsoRoomExit var7 = (IsoRoomExit)this.Exits.get(0);
         var9 = var7.x;
         var6 = var7.y;
      }

      float var10 = IsoUtils.DistanceManhatten(var1.getInstance().getX(), var1.getInstance().getY(), (float)var9, (float)var6);
      if (var10 > 0.0F) {
         if (var2) {
            var3 *= var10 * 0.5F;
         } else {
            var3 /= var10 * 0.5F;
         }
      }

      return var3;
   }

   public BuildingDef getDef() {
      return this.def;
   }

   public void update() {
      if (!this.Exits.isEmpty()) {
         byte var1 = 0;
         int var2 = 0;

         for(int var3 = 0; var3 < this.Rooms.size(); ++var3) {
            IsoRoom var4 = (IsoRoom)this.Rooms.get(var3);
            if (var4.layer == 0) {
               for(int var5 = 0; var5 < var4.TileList.size(); ++var5) {
                  ++var2;
                  IsoGridSquare var6 = (IsoGridSquare)var4.TileList.get(var5);
               }
            }
         }

         if (var2 == 0) {
            ++var2;
         }

         int var7 = (int)((float)var1 / (float)var2);
         --this.scoreUpdate;
         if (this.scoreUpdate <= 0) {
            this.scoreUpdate += 120;
            BuildingScore var8 = null;
            if (IsoWorld.instance.CurrentCell.getBuildingScores().containsKey(this.ID)) {
               var8 = (BuildingScore)IsoWorld.instance.CurrentCell.getBuildingScores().get(this.ID);
            } else {
               var8 = new BuildingScore(this);
               var8.building = this;
            }

            var8 = this.ScoreBuildingGeneral(var8);
            var8.defense += (float)(var7 * 10);
            this.safety = var7;
            IsoWorld.instance.CurrentCell.getBuildingScores().put(this.ID, var8);
         }

      }
   }

   public void AddRoom(IsoRoom var1) {
      this.Rooms.add(var1);
      if (this.bounds == null) {
         this.bounds = (Rectangle)var1.bounds.clone();
      }

      if (var1 != null && var1.bounds != null) {
         this.bounds.add(var1.bounds);
      }

   }

   public void CalculateExits() {
      Iterator var1 = this.Rooms.iterator();

      while(var1.hasNext()) {
         IsoRoom var2 = (IsoRoom)var1.next();
         Iterator var3 = var2.Exits.iterator();

         while(var3.hasNext()) {
            IsoRoomExit var4 = (IsoRoomExit)var3.next();
            if (var4.To.From == null && var2.layer == 0) {
               this.Exits.add(var4);
            }
         }
      }

   }

   public void CalculateWindows() {
      Iterator var1 = this.Rooms.iterator();

      label118:
      while(var1.hasNext()) {
         IsoRoom var2 = (IsoRoom)var1.next();
         Iterator var3 = var2.TileList.iterator();

         while(true) {
            IsoGridSquare var6;
            IsoObject var8;
            int var9;
            do {
               if (!var3.hasNext()) {
                  continue label118;
               }

               IsoGridSquare var4 = (IsoGridSquare)var3.next();
               IsoGridSquare var5 = var4.getCell().getGridSquare(var4.getX(), var4.getY() + 1, var4.getZ());
               var6 = var4.getCell().getGridSquare(var4.getX() + 1, var4.getY(), var4.getZ());
               if (var4.getProperties().Is(IsoFlagType.collideN) && var4.getProperties().Is(IsoFlagType.transparentN)) {
                  ++var2.transparentWalls;
                  ++this.transparentWalls;
               }

               if (var4.getProperties().Is(IsoFlagType.collideW) && var4.getProperties().Is(IsoFlagType.transparentW)) {
                  ++var2.transparentWalls;
                  ++this.transparentWalls;
               }

               boolean var7;
               if (var5 != null) {
                  var7 = var5.getRoom() != null;
                  if (var5.getRoom() != null && var5.getRoom().building != var2.building) {
                     var7 = false;
                  }

                  if (var5.getProperties().Is(IsoFlagType.collideN) && var5.getProperties().Is(IsoFlagType.transparentN) && !var7) {
                     ++var2.transparentWalls;
                     ++this.transparentWalls;
                  }
               }

               if (var6 != null) {
                  var7 = var6.getRoom() != null;
                  if (var6.getRoom() != null && var6.getRoom().building != var2.building) {
                     var7 = false;
                  }

                  if (var6.getProperties().Is(IsoFlagType.collideW) && var6.getProperties().Is(IsoFlagType.transparentW) && !var7) {
                     ++var2.transparentWalls;
                     ++this.transparentWalls;
                  }
               }

               for(var9 = 0; var9 < var4.getSpecialObjects().size(); ++var9) {
                  var8 = (IsoObject)var4.getSpecialObjects().get(var9);
                  if (var8 instanceof IsoWindow) {
                     this.Windows.add((IsoWindow)var8);
                  }
               }

               if (var5 != null) {
                  for(var9 = 0; var9 < var5.getSpecialObjects().size(); ++var9) {
                     var8 = (IsoObject)var5.getSpecialObjects().get(var9);
                     if (var8 instanceof IsoWindow) {
                        this.Windows.add((IsoWindow)var8);
                     }
                  }
               }
            } while(var6 == null);

            for(var9 = 0; var9 < var6.getSpecialObjects().size(); ++var9) {
               var8 = (IsoObject)var6.getSpecialObjects().get(var9);
               if (var8 instanceof IsoWindow) {
                  this.Windows.add((IsoWindow)var8);
               }
            }
         }
      }

   }

   public void FillContainers() {
      boolean var1 = false;
      Iterator var2 = this.Rooms.iterator();

      while(var2.hasNext()) {
         IsoRoom var3 = (IsoRoom)var2.next();
         if (var3.RoomDef != null && var3.RoomDef.contains("tutorial")) {
            var1 = true;
         }

         if (!var3.TileList.isEmpty()) {
            IsoGridSquare var4 = (IsoGridSquare)var3.TileList.get(0);
            if (var4.getX() < 74 && var4.getY() < 32) {
               var1 = true;
            }
         }

         if (var3.RoomDef.contains("shop")) {
            this.IsResidence = false;
         }

         Iterator var8 = var3.TileList.iterator();

         while(var8.hasNext()) {
            IsoGridSquare var5 = (IsoGridSquare)var8.next();

            for(int var6 = 0; var6 < var5.getObjects().size(); ++var6) {
               IsoObject var7 = (IsoObject)var5.getObjects().get(var6);
               if (var7.hasWater()) {
                  var3.getWaterSources().add(var7);
               }

               if (var7.container != null) {
                  this.container.add(var7.container);
                  var3.Containers.add(var7.container);
               }
            }

            if (var5.getProperties().Is(IsoFlagType.bed)) {
               var3.Beds.add(var5);
            }
         }
      }

   }

   public ItemContainer getContainerWith(ItemType var1) {
      Iterator var2 = this.Rooms.iterator();

      while(var2.hasNext()) {
         IsoRoom var3 = (IsoRoom)var2.next();
         Iterator var4 = var3.Containers.iterator();

         while(var4.hasNext()) {
            ItemContainer var5 = (ItemContainer)var4.next();
            if (var5.HasType(var1)) {
               return var5;
            }
         }
      }

      return null;
   }

   public IsoRoom getRandomRoom() {
      if (this.Rooms.size() == 0) {
         return null;
      } else {
         IsoRoom var1 = (IsoRoom)this.Rooms.get(Rand.Next(this.Rooms.size()));
         return var1;
      }
   }

   private BuildingScore ScoreBuildingGeneral(BuildingScore var1) {
      var1.food = 0.0F;
      var1.defense = 0.0F;
      var1.weapons = 0.0F;
      var1.wood = 0.0F;
      var1.building = this;
      var1.size = 0;
      var1.defense += (float)((this.Exits.size() - 1) * 140);
      var1.defense -= (float)(this.transparentWalls * 40);
      var1.size = this.Rooms.size() * 10;
      var1.size += this.container.size() * 10;
      return var1;
   }

   public IsoGridSquare getFreeTile() {
      IsoGridSquare var1 = null;

      do {
         IsoRoom var2 = (IsoRoom)this.Rooms.get(Rand.Next(this.Rooms.size()));
         var1 = var2.getFreeTile();
      } while(var1 == null);

      return var1;
   }

   public boolean hasWater() {
      Iterator var1 = this.Rooms.iterator();

      while(var1 != null && var1.hasNext()) {
         IsoRoom var2 = (IsoRoom)var1.next();
         if (!var2.WaterSources.isEmpty()) {
            IsoObject var3 = null;

            for(int var4 = 0; var4 < var2.WaterSources.size(); ++var4) {
               if (((IsoObject)var2.WaterSources.get(var4)).hasWater()) {
                  var3 = (IsoObject)var2.WaterSources.get(var4);
                  break;
               }
            }

            if (var3 != null) {
               return true;
            }
         }
      }

      return false;
   }

   public void CreateFrom(BuildingDef var1, LotHeader var2) {
      for(int var3 = 0; var3 < var1.rooms.size(); ++var3) {
         IsoRoom var4 = var2.getRoom(((RoomDef)var1.rooms.get(var3)).ID);
         var4.building = this;
         this.Rooms.add(var4);
      }

   }

   public void setAllExplored(boolean var1) {
      this.def.bAlarmed = false;

      for(int var2 = 0; var2 < this.Rooms.size(); ++var2) {
         IsoRoom var3 = (IsoRoom)this.Rooms.get(var2);
         var3.def.setExplored(var1);

         for(int var4 = var3.def.getX(); var4 <= var3.def.getX2(); ++var4) {
            for(int var5 = var3.def.getY(); var5 <= var3.def.getY2(); ++var5) {
               IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var4, var5, var3.def.level);
               if (var6 != null) {
                  var6.setHourSeenToCurrent();
               }
            }
         }
      }

   }

   public boolean isAllExplored() {
      for(int var1 = 0; var1 < this.Rooms.size(); ++var1) {
         IsoRoom var2 = (IsoRoom)this.Rooms.get(var1);
         if (!var2.def.bExplored) {
            return false;
         }
      }

      return true;
   }

   public void addWindow(IsoWindow var1, boolean var2, IsoGridSquare var3, IsoBuilding var4) {
      this.Windows.add(var1);
      IsoGridSquare var5 = null;
      if (var2) {
         var5 = var1.square;
      } else {
         var5 = var3;
      }

      if (var5 != null) {
         if (var5.getRoom() == null) {
            float var6 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            float var7 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            float var8 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            byte var9 = 7;
            IsoLightSource var10 = new IsoLightSource(var5.getX(), var5.getY(), var5.getZ(), var6, var7, var8, var9, var4);
            this.lights.add(var10);
            IsoWorld.instance.CurrentCell.getLamppostPositions().add(var10);
         }
      }
   }

   public void addWindow(IsoWindow var1, boolean var2) {
      this.addWindow(var1, var2, var1.square, (IsoBuilding)null);
   }

   public void addDoor(IsoDoor var1, boolean var2, IsoGridSquare var3, IsoBuilding var4) {
      IsoGridSquare var5 = null;
      if (var2) {
         var5 = var1.square;
      } else {
         var5 = var3;
      }

      if (var5 != null) {
         if (var5.getRoom() == null) {
            float var6 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            float var7 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            float var8 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            byte var9 = 7;
            IsoLightSource var10 = new IsoLightSource(var5.getX(), var5.getY(), var5.getZ(), var6, var7, var8, var9, var4);
            this.lights.add(var10);
            IsoWorld.instance.CurrentCell.getLamppostPositions().add(var10);
         }
      }
   }

   public void addDoor(IsoDoor var1, boolean var2) {
      this.addDoor(var1, var2, var1.square, (IsoBuilding)null);
   }

   public boolean isResidential() {
      return this.containsRoom("bedroom");
   }

   public boolean containsRoom(String var1) {
      for(int var2 = 0; var2 < this.Rooms.size(); ++var2) {
         if (var1.equals(((IsoRoom)this.Rooms.get(var2)).getName())) {
            return true;
         }
      }

      return false;
   }

   public IsoRoom getRandomRoom(String var1) {
      tempo.clear();

      for(int var2 = 0; var2 < this.Rooms.size(); ++var2) {
         if (var1.equals(((IsoRoom)this.Rooms.get(var2)).getName())) {
            tempo.add((IsoRoom)this.Rooms.get(var2));
         }
      }

      if (tempo.isEmpty()) {
         return null;
      } else {
         return (IsoRoom)tempo.get(Rand.Next(tempo.size()));
      }
   }

   public ItemContainer getRandomContainer(String var1) {
      RandomContainerChoices.clear();
      String[] var2 = null;
      if (var1 != null) {
         var2 = var1.split(",");
      }

      int var3;
      if (var2 != null) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            RandomContainerChoices.add(var2[var3]);
         }
      }

      tempContainer.clear();

      for(var3 = 0; var3 < this.Rooms.size(); ++var3) {
         IsoRoom var4 = (IsoRoom)this.Rooms.get(var3);

         for(int var5 = 0; var5 < var4.Containers.size(); ++var5) {
            ItemContainer var6 = (ItemContainer)var4.Containers.get(var5);
            if (var1 == null || RandomContainerChoices.contains(var6.getType())) {
               tempContainer.add(var6);
            }
         }
      }

      if (tempContainer.isEmpty()) {
         return null;
      } else {
         return (ItemContainer)tempContainer.get(Rand.Next(tempContainer.size()));
      }
   }

   public IsoWindow getRandomFirstFloorWindow() {
      windowchoices.clear();
      windowchoices.addAll(this.Windows);

      for(int var1 = 0; var1 < windowchoices.size(); ++var1) {
         if (((IsoWindow)windowchoices.get(var1)).getZ() > 0.0F) {
            windowchoices.remove(var1);
         }
      }

      if (!windowchoices.isEmpty()) {
         return (IsoWindow)windowchoices.get(Rand.Next(windowchoices.size()));
      } else {
         return null;
      }
   }

   public boolean isToxic() {
      return this.isToxic;
   }

   public void setToxic(boolean var1) {
      this.isToxic = var1;
   }

   public void forceAwake() {
      for(int var1 = this.def.getX(); var1 <= this.def.getX2(); ++var1) {
         for(int var2 = this.def.getY(); var2 <= this.def.getY2(); ++var2) {
            for(int var3 = 0; var3 <= 4; ++var3) {
               IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
               if (var4 != null) {
                  for(int var5 = 0; var5 < var4.getMovingObjects().size(); ++var5) {
                     if (var4.getMovingObjects().get(var5) instanceof IsoGameCharacter) {
                        ((IsoGameCharacter)var4.getMovingObjects().get(var5)).forceAwake();
                     }
                  }
               }
            }
         }
      }

   }
}
