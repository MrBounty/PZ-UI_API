package zombie.iso;

import gnu.trove.list.array.TIntArrayList;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MapGroups;
import zombie.SandboxOptions;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.characters.Faction;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.stash.StashSystem;
import zombie.debug.DebugLog;
import zombie.gameStates.ChooseGameInfo;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.areas.SafeHouse;
import zombie.iso.objects.IsoMannequin;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.randomizedWorld.randomizedBuilding.RBBasic;
import zombie.randomizedWorld.randomizedZoneStory.RandomizedZoneStoryBase;
import zombie.util.BufferedRandomAccessFile;
import zombie.util.SharedStrings;
import zombie.util.Type;
import zombie.vehicles.Clipper;
import zombie.vehicles.ClipperOffset;
import zombie.vehicles.PolygonalMap2;

public final class IsoMetaGrid {
   private static final int NUM_LOADER_THREADS = 8;
   private static ArrayList s_PreferredZoneTypes = new ArrayList();
   private static Clipper s_clipper = null;
   private static ClipperOffset s_clipperOffset = null;
   private static ByteBuffer s_clipperBuffer = null;
   static Rectangle a = new Rectangle();
   static Rectangle b = new Rectangle();
   static ArrayList roomChoices = new ArrayList(50);
   private final ArrayList tempRooms = new ArrayList();
   private final ArrayList tempZones1 = new ArrayList();
   private final ArrayList tempZones2 = new ArrayList();
   private final IsoMetaGrid.MetaGridLoaderThread[] threads = new IsoMetaGrid.MetaGridLoaderThread[8];
   public int minX = 10000000;
   public int minY = 10000000;
   public int maxX = -10000000;
   public int maxY = -10000000;
   public final ArrayList Zones = new ArrayList();
   public final ArrayList Buildings = new ArrayList();
   public final ArrayList VehiclesZones = new ArrayList();
   public IsoMetaCell[][] Grid;
   public final ArrayList MetaCharacters = new ArrayList();
   final ArrayList HighZombieList = new ArrayList();
   private int width;
   private int height;
   private final SharedStrings sharedStrings = new SharedStrings();
   private long createStartTime;

   public void AddToMeta(IsoGameCharacter var1) {
      IsoWorld.instance.CurrentCell.Remove(var1);
      if (!this.MetaCharacters.contains(var1)) {
         this.MetaCharacters.add(var1);
      }

   }

   public void RemoveFromMeta(IsoPlayer var1) {
      this.MetaCharacters.remove(var1);
      if (!IsoWorld.instance.CurrentCell.getObjectList().contains(var1)) {
         IsoWorld.instance.CurrentCell.getObjectList().add(var1);
      }

   }

   public int getMinX() {
      return this.minX;
   }

   public int getMinY() {
      return this.minY;
   }

   public int getMaxX() {
      return this.maxX;
   }

   public int getMaxY() {
      return this.maxY;
   }

   public IsoMetaGrid.Zone getZoneAt(int var1, int var2, int var3) {
      IsoMetaChunk var4 = this.getChunkDataFromTile(var1, var2);
      return var4 != null ? var4.getZoneAt(var1, var2, var3) : null;
   }

   public ArrayList getZonesAt(int var1, int var2, int var3) {
      return this.getZonesAt(var1, var2, var3, new ArrayList());
   }

   public ArrayList getZonesAt(int var1, int var2, int var3, ArrayList var4) {
      IsoMetaChunk var5 = this.getChunkDataFromTile(var1, var2);
      return var5 != null ? var5.getZonesAt(var1, var2, var3, var4) : var4;
   }

   public ArrayList getZonesIntersecting(int var1, int var2, int var3, int var4, int var5) {
      ArrayList var6 = new ArrayList();
      return this.getZonesIntersecting(var1, var2, var3, var4, var5, var6);
   }

   public ArrayList getZonesIntersecting(int var1, int var2, int var3, int var4, int var5, ArrayList var6) {
      for(int var7 = var2 / 300; var7 <= (var2 + var5) / 300; ++var7) {
         for(int var8 = var1 / 300; var8 <= (var1 + var4) / 300; ++var8) {
            if (var8 >= this.minX && var8 <= this.maxX && var7 >= this.minY && var7 <= this.maxY && this.Grid[var8 - this.minX][var7 - this.minY] != null) {
               this.Grid[var8 - this.minX][var7 - this.minY].getZonesIntersecting(var1, var2, var3, var4, var5, var6);
            }
         }
      }

      return var6;
   }

   public IsoMetaGrid.VehicleZone getVehicleZoneAt(int var1, int var2, int var3) {
      IsoMetaCell var4 = this.getMetaGridFromTile(var1, var2);
      if (var4 != null && !var4.vehicleZones.isEmpty()) {
         for(int var5 = 0; var5 < var4.vehicleZones.size(); ++var5) {
            IsoMetaGrid.VehicleZone var6 = (IsoMetaGrid.VehicleZone)var4.vehicleZones.get(var5);
            if (var6.z == var3 && var1 >= var6.x && var1 < var6.x + var6.w && var2 >= var6.y && var2 < var6.y + var6.h) {
               return var6;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public BuildingDef getBuildingAt(int var1, int var2) {
      for(int var3 = 0; var3 < this.Buildings.size(); ++var3) {
         BuildingDef var4 = (BuildingDef)this.Buildings.get(var3);
         if (var4.x <= var1 && var4.y <= var2 && var4.getW() > var1 - var4.x && var4.getH() > var2 - var4.y) {
            return var4;
         }
      }

      return null;
   }

   public BuildingDef getBuildingAtRelax(int var1, int var2) {
      for(int var3 = 0; var3 < this.Buildings.size(); ++var3) {
         BuildingDef var4 = (BuildingDef)this.Buildings.get(var3);
         if (var4.x <= var1 + 1 && var4.y <= var2 + 1 && var4.getW() > var1 - var4.x - 1 && var4.getH() > var2 - var4.y - 1) {
            return var4;
         }
      }

      return null;
   }

   public RoomDef getRoomAt(int var1, int var2, int var3) {
      IsoMetaChunk var4 = this.getChunkDataFromTile(var1, var2);
      return var4 != null ? var4.getRoomAt(var1, var2, var3) : null;
   }

   public RoomDef getEmptyOutsideAt(int var1, int var2, int var3) {
      IsoMetaChunk var4 = this.getChunkDataFromTile(var1, var2);
      return var4 != null ? var4.getEmptyOutsideAt(var1, var2, var3) : null;
   }

   public void getRoomsIntersecting(int var1, int var2, int var3, int var4, ArrayList var5) {
      for(int var6 = var2 / 300; var6 <= (var2 + this.height) / 300; ++var6) {
         for(int var7 = var1 / 300; var7 <= (var1 + this.width) / 300; ++var7) {
            if (var7 >= this.minX && var7 <= this.maxX && var6 >= this.minY && var6 <= this.maxY) {
               IsoMetaCell var8 = this.Grid[var7 - this.minX][var6 - this.minY];
               if (var8 != null) {
                  var8.getRoomsIntersecting(var1, var2, var3, var4, var5);
               }
            }
         }
      }

   }

   public int countRoomsIntersecting(int var1, int var2, int var3, int var4) {
      this.tempRooms.clear();

      for(int var5 = var2 / 300; var5 <= (var2 + this.height) / 300; ++var5) {
         for(int var6 = var1 / 300; var6 <= (var1 + this.width) / 300; ++var6) {
            if (var6 >= this.minX && var6 <= this.maxX && var5 >= this.minY && var5 <= this.maxY) {
               IsoMetaCell var7 = this.Grid[var6 - this.minX][var5 - this.minY];
               if (var7 != null) {
                  var7.getRoomsIntersecting(var1, var2, var3, var4, this.tempRooms);
               }
            }
         }
      }

      return this.tempRooms.size();
   }

   public int countNearbyBuildingsRooms(IsoPlayer var1) {
      int var2 = (int)var1.getX() - 20;
      int var3 = (int)var1.getY() - 20;
      byte var4 = 40;
      byte var5 = 40;
      int var6 = this.countRoomsIntersecting(var2, var3, var4, var5);
      return var6;
   }

   private boolean isInside(IsoMetaGrid.Zone var1, BuildingDef var2) {
      a.x = var1.x;
      a.y = var1.y;
      a.width = var1.w;
      a.height = var1.h;
      b.x = var2.x;
      b.y = var2.y;
      b.width = var2.getW();
      b.height = var2.getH();
      return a.contains(b);
   }

   private boolean isAdjacent(IsoMetaGrid.Zone var1, IsoMetaGrid.Zone var2) {
      if (var1 == var2) {
         return false;
      } else {
         a.x = var1.x;
         a.y = var1.y;
         a.width = var1.w;
         a.height = var1.h;
         b.x = var2.x;
         b.y = var2.y;
         b.width = var2.w;
         b.height = var2.h;
         --a.x;
         --a.y;
         Rectangle var10000 = a;
         var10000.width += 2;
         var10000 = a;
         var10000.height += 2;
         --b.x;
         --b.y;
         var10000 = b;
         var10000.width += 2;
         var10000 = b;
         var10000.height += 2;
         return a.intersects(b);
      }
   }

   public IsoMetaGrid.Zone registerZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      return this.registerZone(var1, var2, var3, var4, var5, var6, var7, IsoMetaGrid.ZoneGeometryType.INVALID, (TIntArrayList)null, 0);
   }

   public IsoMetaGrid.Zone registerZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, IsoMetaGrid.ZoneGeometryType var8, TIntArrayList var9, int var10) {
      var1 = this.sharedStrings.get(var1);
      var2 = this.sharedStrings.get(var2);
      IsoMetaGrid.Zone var11 = new IsoMetaGrid.Zone(var1, var2, var3, var4, var5, var6, var7);
      var11.geometryType = var8;
      if (var9 != null) {
         var11.points.addAll(var9);
         var11.polylineWidth = var10;
      }

      var11.isPreferredZoneForSquare = isPreferredZoneForSquare(var2);
      if (var3 >= this.minX * 300 - 100 && var4 >= this.minY * 300 - 100 && var3 + var6 <= (this.maxX + 1) * 300 + 100 && var4 + var7 <= (this.maxY + 1) * 300 + 100 && var5 >= 0 && var5 < 8 && var6 <= 600 && var7 <= 600) {
         this.addZone(var11);
         return var11;
      } else {
         return var11;
      }
   }

   public IsoMetaGrid.Zone registerGeometryZone(String var1, String var2, int var3, String var4, KahluaTable var5, KahluaTable var6) {
      int var7 = Integer.MAX_VALUE;
      int var8 = Integer.MAX_VALUE;
      int var9 = Integer.MIN_VALUE;
      int var10 = Integer.MIN_VALUE;
      TIntArrayList var11 = new TIntArrayList(var5.len());

      for(int var12 = 0; var12 < var5.len(); var12 += 2) {
         Object var13 = var5.rawget(var12 + 1);
         Object var14 = var5.rawget(var12 + 2);
         int var15 = ((Double)var13).intValue();
         int var16 = ((Double)var14).intValue();
         var11.add(var15);
         var11.add(var16);
         var7 = Math.min(var7, var15);
         var8 = Math.min(var8, var16);
         var9 = Math.max(var9, var15);
         var10 = Math.max(var10, var16);
      }

      byte var19 = -1;
      switch(var4.hashCode()) {
      case -397519558:
         if (var4.equals("polygon")) {
            var19 = 1;
         }
         break;
      case 106845584:
         if (var4.equals("point")) {
            var19 = 0;
         }
         break;
      case 561938880:
         if (var4.equals("polyline")) {
            var19 = 2;
         }
      }

      IsoMetaGrid.ZoneGeometryType var10000;
      switch(var19) {
      case 0:
         var10000 = IsoMetaGrid.ZoneGeometryType.Point;
         break;
      case 1:
         var10000 = IsoMetaGrid.ZoneGeometryType.Polygon;
         break;
      case 2:
         var10000 = IsoMetaGrid.ZoneGeometryType.Polyline;
         break;
      default:
         throw new IllegalArgumentException("unknown zone geometry type");
      }

      IsoMetaGrid.ZoneGeometryType var17 = var10000;
      Double var18 = var17 == IsoMetaGrid.ZoneGeometryType.Polyline && var6 != null ? (Double)Type.tryCastTo(var6.rawget("LineWidth"), Double.class) : null;
      if (var18 != null) {
         int[] var20 = new int[4];
         this.calculatePolylineOutlineBounds(var11, var18.intValue(), var20);
         var7 = var20[0];
         var8 = var20[1];
         var9 = var20[2];
         var10 = var20[3];
      }

      IsoMetaGrid.Zone var21;
      if (!var2.equals("Vehicle") && !var2.equals("ParkingStall")) {
         var21 = this.registerZone(var1, var2, var7, var8, var3, var9 - var7 + 1, var10 - var8 + 1, var17, var11, var18 == null ? 0 : var18.intValue());
         var11.clear();
         return var21;
      } else {
         var21 = this.registerVehiclesZone(var1, var2, var7, var8, var3, var9 - var7 + 1, var10 - var8 + 1, var6);
         if (var21 != null) {
            var21.geometryType = var17;
            var21.points.addAll(var11);
         }

         return var21;
      }
   }

   private void calculatePolylineOutlineBounds(TIntArrayList var1, int var2, int[] var3) {
      if (s_clipperOffset == null) {
         s_clipperOffset = new ClipperOffset();
         s_clipperBuffer = ByteBuffer.allocateDirect(2048);
      }

      s_clipperOffset.clear();
      s_clipperBuffer.clear();
      float var4 = var2 % 2 == 0 ? 0.0F : 0.5F;

      int var5;
      for(var5 = 0; var5 < var1.size(); var5 += 2) {
         int var6 = var1.get(var5);
         int var7 = var1.get(var5 + 1);
         s_clipperBuffer.putFloat((float)var6 + var4);
         s_clipperBuffer.putFloat((float)var7 + var4);
      }

      s_clipperBuffer.flip();
      s_clipperOffset.addPath(var1.size() / 2, s_clipperBuffer, ClipperOffset.JoinType.jtMiter.ordinal(), ClipperOffset.EndType.etOpenButt.ordinal());
      s_clipperOffset.execute((double)((float)var2 / 2.0F));
      var5 = s_clipperOffset.getPolygonCount();
      if (var5 < 1) {
         DebugLog.General.warn("Failed to generate polyline outline");
      } else {
         s_clipperBuffer.clear();
         s_clipperOffset.getPolygon(0, s_clipperBuffer);
         short var14 = s_clipperBuffer.getShort();
         float var15 = Float.MAX_VALUE;
         float var8 = Float.MAX_VALUE;
         float var9 = -3.4028235E38F;
         float var10 = -3.4028235E38F;

         for(int var11 = 0; var11 < var14; ++var11) {
            float var12 = s_clipperBuffer.getFloat();
            float var13 = s_clipperBuffer.getFloat();
            var15 = PZMath.min(var15, var12);
            var8 = PZMath.min(var8, var13);
            var9 = PZMath.max(var9, var12);
            var10 = PZMath.max(var10, var13);
         }

         var3[0] = (int)PZMath.floor(var15);
         var3[1] = (int)PZMath.floor(var8);
         var3[2] = (int)PZMath.ceil(var9);
         var3[3] = (int)PZMath.ceil(var10);
      }
   }

   /** @deprecated */
   @Deprecated
   public IsoMetaGrid.Zone registerZoneNoOverlap(String var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      return var3 >= this.minX * 300 - 100 && var4 >= this.minY * 300 - 100 && var3 + var6 <= (this.maxX + 1) * 300 + 100 && var4 + var7 <= (this.maxY + 1) * 300 + 100 && var5 >= 0 && var5 < 8 && var6 <= 600 && var7 <= 600 ? this.registerZone(var1, var2, var3, var4, var5, var6, var7) : null;
   }

   private void addZone(IsoMetaGrid.Zone var1) {
      this.Zones.add(var1);

      for(int var2 = var1.y / 300; var2 <= (var1.y + var1.h) / 300; ++var2) {
         for(int var3 = var1.x / 300; var3 <= (var1.x + var1.w) / 300; ++var3) {
            if (var3 >= this.minX && var3 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && this.Grid[var3 - this.minX][var2 - this.minY] != null) {
               this.Grid[var3 - this.minX][var2 - this.minY].addZone(var1, var3 * 300, var2 * 300);
            }
         }
      }

   }

   public void removeZone(IsoMetaGrid.Zone var1) {
      this.Zones.remove(var1);

      for(int var2 = var1.y / 300; var2 <= (var1.y + var1.h) / 300; ++var2) {
         for(int var3 = var1.x / 300; var3 <= (var1.x + var1.w) / 300; ++var3) {
            if (var3 >= this.minX && var3 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && this.Grid[var3 - this.minX][var2 - this.minY] != null) {
               this.Grid[var3 - this.minX][var2 - this.minY].removeZone(var1);
            }
         }
      }

   }

   public void removeZonesForCell(int var1, int var2) {
      IsoMetaCell var3 = this.getCellData(var1, var2);
      if (var3 != null) {
         ArrayList var4 = this.tempZones1;
         var4.clear();

         int var5;
         for(var5 = 0; var5 < 900; ++var5) {
            var3.ChunkMap[var5].getZonesIntersecting(var1 * 300, var2 * 300, 0, 300, 300, var4);
         }

         for(var5 = 0; var5 < var4.size(); ++var5) {
            IsoMetaGrid.Zone var6 = (IsoMetaGrid.Zone)var4.get(var5);
            ArrayList var7 = this.tempZones2;
            if (var6.difference(var1 * 300, var2 * 300, 0, 300, 300, var7)) {
               this.removeZone(var6);

               for(int var8 = 0; var8 < var7.size(); ++var8) {
                  this.addZone((IsoMetaGrid.Zone)var7.get(var8));
               }
            }
         }

         if (!var3.vehicleZones.isEmpty()) {
            var3.vehicleZones.clear();
         }

         if (!var3.mannequinZones.isEmpty()) {
            var3.mannequinZones.clear();
         }

      }
   }

   public void removeZonesForLotDirectory(String var1) {
      if (!this.Zones.isEmpty()) {
         File var2 = new File(ZomboidFileSystem.instance.getString("media/maps/" + var1 + "/"));
         if (var2.isDirectory()) {
            ChooseGameInfo.Map var3 = ChooseGameInfo.getMapDetails(var1);
            if (var3 != null) {
               String[] var4 = var2.list();
               if (var4 != null) {
                  for(int var5 = 0; var5 < var4.length; ++var5) {
                     String var6 = var4[var5];
                     if (var6.endsWith(".lotheader")) {
                        String[] var7 = var6.split("_");
                        var7[1] = var7[1].replace(".lotheader", "");
                        int var8 = Integer.parseInt(var7[0].trim());
                        int var9 = Integer.parseInt(var7[1].trim());
                        this.removeZonesForCell(var8, var9);
                     }
                  }

               }
            }
         }
      }
   }

   public void processZones() {
      int var1 = 0;

      for(int var2 = this.minX; var2 <= this.maxX; ++var2) {
         for(int var3 = this.minY; var3 <= this.maxY; ++var3) {
            if (this.Grid[var2 - this.minX][var3 - this.minY] != null) {
               for(int var4 = 0; var4 < 30; ++var4) {
                  for(int var5 = 0; var5 < 30; ++var5) {
                     var1 = Math.max(var1, this.Grid[var2 - this.minX][var3 - this.minY].getChunk(var5, var4).numZones());
                  }
               }
            }
         }
      }

      DebugLog.log("Max #ZONES on one chunk is " + var1);
   }

   public IsoMetaGrid.Zone registerVehiclesZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, KahluaTable var8) {
      if (!var2.equals("Vehicle") && !var2.equals("ParkingStall")) {
         return null;
      } else {
         var1 = this.sharedStrings.get(var1);
         var2 = this.sharedStrings.get(var2);
         IsoMetaGrid.VehicleZone var9 = new IsoMetaGrid.VehicleZone(var1, var2, var3, var4, var5, var6, var7, var8);
         this.VehiclesZones.add(var9);
         int var10 = (int)Math.ceil((double)((float)(var9.x + var9.w) / 300.0F));
         int var11 = (int)Math.ceil((double)((float)(var9.y + var9.h) / 300.0F));

         for(int var12 = var9.y / 300; var12 < var11; ++var12) {
            for(int var13 = var9.x / 300; var13 < var10; ++var13) {
               if (var13 >= this.minX && var13 <= this.maxX && var12 >= this.minY && var12 <= this.maxY && this.Grid[var13 - this.minX][var12 - this.minY] != null) {
                  this.Grid[var13 - this.minX][var12 - this.minY].vehicleZones.add(var9);
               }
            }
         }

         return var9;
      }
   }

   public void checkVehiclesZones() {
      int var4 = 0;

      while(var4 < this.VehiclesZones.size()) {
         boolean var1 = true;

         for(int var5 = 0; var5 < var4; ++var5) {
            IsoMetaGrid.Zone var2 = (IsoMetaGrid.Zone)this.VehiclesZones.get(var4);
            IsoMetaGrid.Zone var3 = (IsoMetaGrid.Zone)this.VehiclesZones.get(var5);
            if (var2.getX() == var3.getX() && var2.getY() == var3.getY() && var2.h == var3.h && var2.w == var3.w) {
               var1 = false;
               DebugLog.log("checkVehiclesZones: ERROR! Zone '" + var2.name + "':'" + var2.type + "' (" + var2.x + ", " + var2.y + ") duplicate with Zone '" + var3.name + "':'" + var3.type + "' (" + var3.x + ", " + var3.y + ")");
               break;
            }
         }

         if (var1) {
            ++var4;
         } else {
            this.VehiclesZones.remove(var4);
         }
      }

   }

   public IsoMetaGrid.Zone registerMannequinZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, KahluaTable var8) {
      if (!"Mannequin".equals(var2)) {
         return null;
      } else {
         var1 = this.sharedStrings.get(var1);
         var2 = this.sharedStrings.get(var2);
         IsoMannequin.MannequinZone var9 = new IsoMannequin.MannequinZone(var1, var2, var3, var4, var5, var6, var7, var8);
         int var10 = (int)Math.ceil((double)((float)(var9.x + var9.w) / 300.0F));
         int var11 = (int)Math.ceil((double)((float)(var9.y + var9.h) / 300.0F));

         for(int var12 = var9.y / 300; var12 < var11; ++var12) {
            for(int var13 = var9.x / 300; var13 < var10; ++var13) {
               if (var13 >= this.minX && var13 <= this.maxX && var12 >= this.minY && var12 <= this.maxY && this.Grid[var13 - this.minX][var12 - this.minY] != null) {
                  this.Grid[var13 - this.minX][var12 - this.minY].mannequinZones.add(var9);
               }
            }
         }

         return var9;
      }
   }

   public void save(ByteBuffer var1) {
      this.savePart(var1, 0, false);
      this.savePart(var1, 1, false);
   }

   public void savePart(ByteBuffer var1, int var2, boolean var3) {
      int var4;
      if (var2 == 0) {
         var1.put((byte)77);
         var1.put((byte)69);
         var1.put((byte)84);
         var1.put((byte)65);
         var1.putInt(186);
         var1.putInt(this.Grid.length);
         var1.putInt(this.Grid[0].length);

         for(var4 = 0; var4 < this.Grid.length; ++var4) {
            for(int var5 = 0; var5 < this.Grid[0].length; ++var5) {
               IsoMetaCell var6 = this.Grid[var4][var5];
               int var7 = 0;
               if (var6.info != null) {
                  var7 = var6.info.Rooms.values().size();
               }

               var1.putInt(var7);
               Iterator var8;
               short var11;
               if (var6.info != null) {
                  for(var8 = var6.info.Rooms.entrySet().iterator(); var8.hasNext(); var1.putShort(var11)) {
                     Entry var9 = (Entry)var8.next();
                     RoomDef var10 = (RoomDef)var9.getValue();
                     var1.putInt((Integer)var9.getKey());
                     var11 = 0;
                     if (var10.bExplored) {
                        var11 = (short)(var11 | 1);
                     }

                     if (var10.bLightsActive) {
                        var11 = (short)(var11 | 2);
                     }

                     if (var10.bDoneSpawn) {
                        var11 = (short)(var11 | 4);
                     }

                     if (var10.isRoofFixed()) {
                        var11 = (short)(var11 | 8);
                     }
                  }
               }

               if (var6.info != null) {
                  var1.putInt(var6.info.Buildings.size());
               } else {
                  var1.putInt(0);
               }

               if (var6.info != null) {
                  var8 = var6.info.Buildings.iterator();

                  while(var8.hasNext()) {
                     BuildingDef var12 = (BuildingDef)var8.next();
                     var1.put((byte)(var12.bAlarmed ? 1 : 0));
                     var1.putInt(var12.getKeyId());
                     var1.put((byte)(var12.seen ? 1 : 0));
                     var1.put((byte)(var12.isHasBeenVisited() ? 1 : 0));
                     var1.putInt(var12.lootRespawnHour);
                  }
               }
            }
         }

      } else {
         var1.putInt(SafeHouse.getSafehouseList().size());

         for(var4 = 0; var4 < SafeHouse.getSafehouseList().size(); ++var4) {
            ((SafeHouse)SafeHouse.getSafehouseList().get(var4)).save(var1);
         }

         var1.putInt(NonPvpZone.getAllZones().size());

         for(var4 = 0; var4 < NonPvpZone.getAllZones().size(); ++var4) {
            ((NonPvpZone)NonPvpZone.getAllZones().get(var4)).save(var1);
         }

         var1.putInt(Faction.getFactions().size());

         for(var4 = 0; var4 < Faction.getFactions().size(); ++var4) {
            ((Faction)Faction.getFactions().get(var4)).save(var1);
         }

         if (GameServer.bServer) {
            var4 = var1.position();
            var1.putInt(0);
            StashSystem.save(var1);
            var1.putInt(var4, var1.position());
         } else if (!GameClient.bClient) {
            StashSystem.save(var1);
         }

         var1.putInt(RBBasic.getUniqueRDSSpawned().size());

         for(var4 = 0; var4 < RBBasic.getUniqueRDSSpawned().size(); ++var4) {
            GameWindow.WriteString(var1, (String)RBBasic.getUniqueRDSSpawned().get(var4));
         }

      }
   }

   public void load() {
      File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_meta.bin");

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  int var5 = var3.read(SliceY.SliceBuffer.array());
                  SliceY.SliceBuffer.limit(var5);
                  this.load(SliceY.SliceBuffer);
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
      } catch (FileNotFoundException var12) {
      } catch (Exception var13) {
         ExceptionLogger.logException(var13);
      }

   }

   public void load(ByteBuffer var1) {
      var1.mark();
      byte var3 = var1.get();
      byte var4 = var1.get();
      byte var5 = var1.get();
      byte var6 = var1.get();
      int var2;
      if (var3 == 77 && var4 == 69 && var5 == 84 && var6 == 65) {
         var2 = var1.getInt();
      } else {
         var2 = 33;
         var1.reset();
      }

      int var7 = var1.getInt();
      int var8 = var1.getInt();
      if (var7 != this.Grid.length || var8 != this.Grid[0].length) {
         DebugLog.log("map_meta.bin world size (" + var7 + "x" + var8 + ") does not match the current map size (" + this.Grid.length + "x" + this.Grid[0].length + ")");
         var7 = Math.min(var7, this.Grid.length);
         var8 = Math.min(var8, this.Grid[0].length);
      }

      int var9 = 0;
      int var10 = 0;

      int var11;
      int var12;
      IsoMetaCell var13;
      int var14;
      int var15;
      int var16;
      for(var11 = 0; var11 < var7; ++var11) {
         for(var12 = 0; var12 < var8; ++var12) {
            var13 = this.Grid[var11][var12];
            var14 = var1.getInt();

            boolean var17;
            boolean var19;
            boolean var20;
            for(var15 = 0; var15 < var14; ++var15) {
               var16 = var1.getInt();
               var17 = false;
               boolean var18 = false;
               var19 = false;
               var20 = false;
               if (var2 >= 160) {
                  short var21 = var1.getShort();
                  var17 = (var21 & 1) != 0;
                  var18 = (var21 & 2) != 0;
                  var19 = (var21 & 4) != 0;
                  var20 = (var21 & 8) != 0;
               } else {
                  var17 = var1.get() == 1;
                  if (var2 >= 34) {
                     var18 = var1.get() == 1;
                  } else {
                     var18 = Rand.Next(2) == 0;
                  }
               }

               if (var13.info != null) {
                  RoomDef var29 = (RoomDef)var13.info.Rooms.get(var16);
                  if (var29 != null) {
                     var29.setExplored(var17);
                     var29.bLightsActive = var18;
                     var29.bDoneSpawn = var19;
                     var29.setRoofFixed(var20);
                  } else {
                     DebugLog.log("ERROR: invalid room ID #" + var16 + " in cell " + var11 + "," + var12 + " while reading map_meta.bin");
                  }
               }
            }

            var15 = var1.getInt();
            var9 += var15;

            for(var16 = 0; var16 < var15; ++var16) {
               var17 = var1.get() == 1;
               int var28 = var2 >= 57 ? var1.getInt() : -1;
               var19 = var2 >= 74 ? var1.get() == 1 : false;
               var20 = var2 >= 107 ? var1.get() == 1 : false;
               if (var2 >= 111 && var2 < 121) {
                  var1.getInt();
               } else {
                  boolean var10000 = false;
               }

               int var22 = var2 >= 125 ? var1.getInt() : 0;
               if (var13.info != null && var16 < var13.info.Buildings.size()) {
                  BuildingDef var23 = (BuildingDef)var13.info.Buildings.get(var16);
                  if (var17) {
                     ++var10;
                  }

                  var23.bAlarmed = var17;
                  var23.setKeyId(var28);
                  if (var2 >= 74) {
                     var23.seen = var19;
                  }

                  var23.hasBeenVisited = var20;
                  var23.lootRespawnHour = var22;
               }
            }
         }
      }

      if (var2 <= 112) {
         this.Zones.clear();

         for(var11 = 0; var11 < this.height; ++var11) {
            for(var12 = 0; var12 < this.width; ++var12) {
               var13 = this.Grid[var12][var11];
               if (var13 != null) {
                  for(var14 = 0; var14 < 30; ++var14) {
                     for(var15 = 0; var15 < 30; ++var15) {
                        var13.ChunkMap[var15 + var14 * 30].clearZones();
                     }
                  }
               }
            }
         }

         this.loadZone(var1, var2);
      }

      SafeHouse.clearSafehouseList();
      var11 = var1.getInt();

      for(var12 = 0; var12 < var11; ++var12) {
         SafeHouse.load(var1, var2);
      }

      NonPvpZone.nonPvpZoneList.clear();
      var12 = var1.getInt();

      int var24;
      for(var24 = 0; var24 < var12; ++var24) {
         NonPvpZone var25 = new NonPvpZone();
         var25.load(var1, var2);
         NonPvpZone.getAllZones().add(var25);
      }

      Faction.factions = new ArrayList();
      var24 = var1.getInt();

      for(var14 = 0; var14 < var24; ++var14) {
         Faction var26 = new Faction();
         var26.load(var1, var2);
         Faction.getFactions().add(var26);
      }

      if (GameServer.bServer) {
         var14 = var1.getInt();
         StashSystem.load(var1, var2);
      } else if (GameClient.bClient) {
         var14 = var1.getInt();
         var1.position(var14);
      } else {
         StashSystem.load(var1, var2);
      }

      ArrayList var27 = RBBasic.getUniqueRDSSpawned();
      var27.clear();
      var15 = var1.getInt();

      for(var16 = 0; var16 < var15; ++var16) {
         var27.add(GameWindow.ReadString(var1));
      }

   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public IsoMetaCell getCellData(int var1, int var2) {
      return var1 - this.minX >= 0 && var2 - this.minY >= 0 && var1 - this.minX < this.width && var2 - this.minY < this.height ? this.Grid[var1 - this.minX][var2 - this.minY] : null;
   }

   public IsoMetaCell getCellDataAbs(int var1, int var2) {
      return this.Grid[var1][var2];
   }

   public IsoMetaCell getCurrentCellData() {
      int var1 = IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldX;
      int var2 = IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldY;
      float var3 = (float)var1;
      float var4 = (float)var2;
      var3 /= 30.0F;
      var4 /= 30.0F;
      if (var3 < 0.0F) {
         var3 = (float)((int)var3 - 1);
      }

      if (var4 < 0.0F) {
         var4 = (float)((int)var4 - 1);
      }

      var1 = (int)var3;
      var2 = (int)var4;
      return this.getCellData(var1, var2);
   }

   public IsoMetaCell getMetaGridFromTile(int var1, int var2) {
      int var3 = var1 / 300;
      int var4 = var2 / 300;
      return this.getCellData(var3, var4);
   }

   public IsoMetaChunk getCurrentChunkData() {
      int var1 = IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldX;
      int var2 = IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldY;
      float var3 = (float)var1;
      float var4 = (float)var2;
      var3 /= 30.0F;
      var4 /= 30.0F;
      if (var3 < 0.0F) {
         var3 = (float)((int)var3 - 1);
      }

      if (var4 < 0.0F) {
         var4 = (float)((int)var4 - 1);
      }

      var1 = (int)var3;
      var2 = (int)var4;
      return this.getCellData(var1, var2).getChunk(IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldX - var1 * 30, IsoWorld.instance.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldY - var2 * 30);
   }

   public IsoMetaChunk getChunkData(int var1, int var2) {
      float var5 = (float)var1;
      float var6 = (float)var2;
      var5 /= 30.0F;
      var6 /= 30.0F;
      if (var5 < 0.0F) {
         var5 = (float)((int)var5 - 1);
      }

      if (var6 < 0.0F) {
         var6 = (float)((int)var6 - 1);
      }

      int var3 = (int)var5;
      int var4 = (int)var6;
      IsoMetaCell var7 = this.getCellData(var3, var4);
      return var7 == null ? null : var7.getChunk(var1 - var3 * 30, var2 - var4 * 30);
   }

   public IsoMetaChunk getChunkDataFromTile(int var1, int var2) {
      int var3 = var1 / 10;
      int var4 = var2 / 10;
      var3 -= this.minX * 30;
      var4 -= this.minY * 30;
      int var5 = var3 / 30;
      int var6 = var4 / 30;
      var3 += this.minX * 30;
      var4 += this.minY * 30;
      var5 += this.minX;
      var6 += this.minY;
      IsoMetaCell var7 = this.getCellData(var5, var6);
      return var7 == null ? null : var7.getChunk(var3 - var5 * 30, var4 - var6 * 30);
   }

   public boolean isValidSquare(int var1, int var2) {
      if (var1 < this.minX * 300) {
         return false;
      } else if (var1 >= (this.maxX + 1) * 300) {
         return false;
      } else if (var2 < this.minY * 300) {
         return false;
      } else {
         return var2 < (this.maxY + 1) * 300;
      }
   }

   public boolean isValidChunk(int var1, int var2) {
      var1 *= 10;
      var2 *= 10;
      if (var1 < this.minX * 300) {
         return false;
      } else if (var1 >= (this.maxX + 1) * 300) {
         return false;
      } else if (var2 < this.minY * 300) {
         return false;
      } else if (var2 >= (this.maxY + 1) * 300) {
         return false;
      } else {
         return this.Grid[var1 / 300 - this.minX][var2 / 300 - this.minY].info != null;
      }
   }

   public void Create() {
      this.CreateStep1();
      this.CreateStep2();
   }

   public void CreateStep1() {
      this.minX = 10000000;
      this.minY = 10000000;
      this.maxX = -10000000;
      this.maxY = -10000000;
      IsoLot.InfoHeaders.clear();
      IsoLot.InfoHeaderNames.clear();
      IsoLot.InfoFileNames.clear();
      long var1 = System.currentTimeMillis();
      DebugLog.log("IsoMetaGrid.Create: begin scanning directories");
      ArrayList var4 = this.getLotDirectories();
      DebugLog.log("Looking in these map folders:");
      Iterator var5 = var4.iterator();

      String var6;
      while(var5.hasNext()) {
         var6 = (String)var5.next();
         var6 = ZomboidFileSystem.instance.getString("media/maps/" + var6 + "/");
         File var10000 = new File(var6);
         DebugLog.log("    " + var10000.getAbsolutePath());
      }

      DebugLog.log("<End of map-folders list>");
      var5 = var4.iterator();

      while(true) {
         File var3;
         do {
            if (!var5.hasNext()) {
               if (this.maxX >= this.minX && this.maxY >= this.minY) {
                  this.Grid = new IsoMetaCell[this.maxX - this.minX + 1][this.maxY - this.minY + 1];
                  this.width = this.maxX - this.minX + 1;
                  this.height = this.maxY - this.minY + 1;
                  long var14 = System.currentTimeMillis() - var1;
                  DebugLog.log("IsoMetaGrid.Create: finished scanning directories in " + (float)var14 / 1000.0F + " seconds");
                  DebugLog.log("IsoMetaGrid.Create: begin loading");
                  this.createStartTime = System.currentTimeMillis();

                  for(int var15 = 0; var15 < 8; ++var15) {
                     IsoMetaGrid.MetaGridLoaderThread var16 = new IsoMetaGrid.MetaGridLoaderThread(this.minY + var15);
                     var16.setDaemon(true);
                     var16.setName("MetaGridLoaderThread" + var15);
                     var16.start();
                     this.threads[var15] = var16;
                  }

                  return;
               }

               throw new IllegalStateException("Failed to find any .lotheader files");
            }

            var6 = (String)var5.next();
            var3 = new File(ZomboidFileSystem.instance.getString("media/maps/" + var6 + "/"));
         } while(!var3.isDirectory());

         ChooseGameInfo.Map var7 = ChooseGameInfo.getMapDetails(var6);
         String[] var8 = var3.list();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            if (!IsoLot.InfoFileNames.containsKey(var8[var9])) {
               HashMap var17;
               String var10001;
               String var10002;
               if (var8[var9].endsWith(".lotheader")) {
                  String[] var10 = var8[var9].split("_");
                  var10[1] = var10[1].replace(".lotheader", "");
                  int var11 = Integer.parseInt(var10[0].trim());
                  int var12 = Integer.parseInt(var10[1].trim());
                  if (var11 < this.minX) {
                     this.minX = var11;
                  }

                  if (var12 < this.minY) {
                     this.minY = var12;
                  }

                  if (var11 > this.maxX) {
                     this.maxX = var11;
                  }

                  if (var12 > this.maxY) {
                     this.maxY = var12;
                  }

                  var17 = IsoLot.InfoFileNames;
                  var10001 = var8[var9];
                  var10002 = var3.getAbsolutePath();
                  var17.put(var10001, var10002 + File.separator + var8[var9]);
                  LotHeader var13 = new LotHeader();
                  var13.bFixed2x = var7.isFixed2x();
                  IsoLot.InfoHeaders.put(var8[var9], var13);
                  IsoLot.InfoHeaderNames.add(var8[var9]);
               } else if (var8[var9].endsWith(".lotpack")) {
                  var17 = IsoLot.InfoFileNames;
                  var10001 = var8[var9];
                  var10002 = var3.getAbsolutePath();
                  var17.put(var10001, var10002 + File.separator + var8[var9]);
               } else if (var8[var9].startsWith("chunkdata_")) {
                  var17 = IsoLot.InfoFileNames;
                  var10001 = var8[var9];
                  var10002 = var3.getAbsolutePath();
                  var17.put(var10001, var10002 + File.separator + var8[var9]);
               }
            }
         }
      }
   }

   public void CreateStep2() {
      boolean var1 = true;

      while(true) {
         int var2;
         while(var1) {
            var1 = false;

            for(var2 = 0; var2 < 8; ++var2) {
               if (this.threads[var2].isAlive()) {
                  var1 = true;

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var5) {
                  }
                  break;
               }
            }
         }

         for(var2 = 0; var2 < 8; ++var2) {
            this.threads[var2].postLoad();
            this.threads[var2] = null;
         }

         for(var2 = 0; var2 < this.Buildings.size(); ++var2) {
            BuildingDef var3 = (BuildingDef)this.Buildings.get(var2);
            if (!Core.GameMode.equals("LastStand") && var3.rooms.size() > 2) {
               int var4 = 11;
               if (SandboxOptions.instance.getElecShutModifier() > -1 && GameTime.instance.NightsSurvived < SandboxOptions.instance.getElecShutModifier()) {
                  var4 = 9;
               }

               if (SandboxOptions.instance.Alarm.getValue() == 1) {
                  var4 = -1;
               } else if (SandboxOptions.instance.Alarm.getValue() == 2) {
                  var4 += 5;
               } else if (SandboxOptions.instance.Alarm.getValue() == 3) {
                  var4 += 3;
               } else if (SandboxOptions.instance.Alarm.getValue() == 5) {
                  var4 -= 3;
               } else if (SandboxOptions.instance.Alarm.getValue() == 6) {
                  var4 -= 5;
               }

               if (var4 > -1) {
                  var3.bAlarmed = Rand.Next(var4) == 0;
               }
            }
         }

         long var6 = System.currentTimeMillis() - this.createStartTime;
         DebugLog.log("IsoMetaGrid.Create: finished loading in " + (float)var6 / 1000.0F + " seconds");
         return;
      }
   }

   public void Dispose() {
      if (this.Grid != null) {
         for(int var1 = 0; var1 < this.Grid.length; ++var1) {
            IsoMetaCell[] var2 = this.Grid[var1];

            for(int var3 = 0; var3 < var2.length; ++var3) {
               IsoMetaCell var4 = var2[var3];
               if (var4 != null) {
                  var4.Dispose();
               }
            }

            Arrays.fill(var2, (Object)null);
         }

         Arrays.fill(this.Grid, (Object)null);
         this.Grid = null;
         Iterator var5 = this.Buildings.iterator();

         while(var5.hasNext()) {
            BuildingDef var6 = (BuildingDef)var5.next();
            var6.Dispose();
         }

         this.Buildings.clear();
         this.VehiclesZones.clear();
         var5 = this.Zones.iterator();

         while(var5.hasNext()) {
            IsoMetaGrid.Zone var7 = (IsoMetaGrid.Zone)var5.next();
            var7.Dispose();
         }

         this.Zones.clear();
         this.sharedStrings.clear();
      }
   }

   public Vector2 getRandomIndoorCoord() {
      return null;
   }

   public RoomDef getRandomRoomBetweenRange(float var1, float var2, float var3, float var4) {
      RoomDef var5 = null;
      float var6 = 0.0F;
      roomChoices.clear();
      LotHeader var7 = null;

      for(int var8 = 0; var8 < IsoLot.InfoHeaderNames.size(); ++var8) {
         var7 = (LotHeader)IsoLot.InfoHeaders.get(IsoLot.InfoHeaderNames.get(var8));
         if (!var7.RoomList.isEmpty()) {
            for(int var9 = 0; var9 < var7.RoomList.size(); ++var9) {
               var5 = (RoomDef)var7.RoomList.get(var9);
               var6 = IsoUtils.DistanceManhatten(var1, var2, (float)var5.x, (float)var5.y);
               if (var6 > var3 && var6 < var4) {
                  roomChoices.add(var5);
               }
            }
         }
      }

      if (!roomChoices.isEmpty()) {
         return (RoomDef)roomChoices.get(Rand.Next(roomChoices.size()));
      } else {
         return null;
      }
   }

   public RoomDef getRandomRoomNotInRange(float var1, float var2, int var3) {
      RoomDef var4 = null;

      do {
         LotHeader var5 = null;

         do {
            var5 = (LotHeader)IsoLot.InfoHeaders.get(IsoLot.InfoHeaderNames.get(Rand.Next(IsoLot.InfoHeaderNames.size())));
         } while(var5.RoomList.isEmpty());

         var4 = (RoomDef)var5.RoomList.get(Rand.Next(var5.RoomList.size()));
      } while(var4 == null || IsoUtils.DistanceManhatten(var1, var2, (float)var4.x, (float)var4.y) < (float)var3);

      return var4;
   }

   public void save() {
      try {
         File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_meta.bin");
         FileOutputStream var2 = new FileOutputStream(var1);

         try {
            BufferedOutputStream var3 = new BufferedOutputStream(var2);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  this.save(SliceY.SliceBuffer);
                  var3.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
               }
            } catch (Throwable var15) {
               try {
                  var3.close();
               } catch (Throwable var10) {
                  var15.addSuppressed(var10);
               }

               throw var15;
            }

            var3.close();
         } catch (Throwable var16) {
            try {
               var2.close();
            } catch (Throwable var9) {
               var16.addSuppressed(var9);
            }

            throw var16;
         }

         var2.close();
         File var18 = ZomboidFileSystem.instance.getFileInCurrentSave("map_zone.bin");
         FileOutputStream var19 = new FileOutputStream(var18);

         try {
            BufferedOutputStream var4 = new BufferedOutputStream(var19);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  this.saveZone(SliceY.SliceBuffer);
                  var4.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
               }
            } catch (Throwable var12) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var12.addSuppressed(var8);
               }

               throw var12;
            }

            var4.close();
         } catch (Throwable var13) {
            try {
               var19.close();
            } catch (Throwable var7) {
               var13.addSuppressed(var7);
            }

            throw var13;
         }

         var19.close();
      } catch (Exception var17) {
         ExceptionLogger.logException(var17);
      }

   }

   public void loadZones() {
      File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_zone.bin");

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  int var5 = var3.read(SliceY.SliceBuffer.array());
                  SliceY.SliceBuffer.limit(var5);
                  this.loadZone(SliceY.SliceBuffer, -1);
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
      } catch (FileNotFoundException var12) {
      } catch (Exception var13) {
         ExceptionLogger.logException(var13);
      }

   }

   public void loadZone(ByteBuffer var1, int var2) {
      if (var2 == -1) {
         byte var3 = var1.get();
         byte var4 = var1.get();
         byte var5 = var1.get();
         byte var6 = var1.get();
         if (var3 != 90 || var4 != 79 || var5 != 78 || var6 != 69) {
            DebugLog.log("ERROR: expected 'ZONE' at start of map_zone.bin");
            return;
         }

         var2 = var1.getInt();
      }

      int var22 = this.Zones.size();
      if (!GameServer.bServer && var2 >= 34 || GameServer.bServer && var2 >= 36) {
         Iterator var23 = this.Zones.iterator();

         while(var23.hasNext()) {
            IsoMetaGrid.Zone var25 = (IsoMetaGrid.Zone)var23.next();
            var25.Dispose();
         }

         this.Zones.clear();

         int var7;
         int var8;
         for(int var24 = 0; var24 < this.height; ++var24) {
            for(int var27 = 0; var27 < this.width; ++var27) {
               IsoMetaCell var29 = this.Grid[var27][var24];
               if (var29 != null) {
                  for(var7 = 0; var7 < 30; ++var7) {
                     for(var8 = 0; var8 < 30; ++var8) {
                        var29.ChunkMap[var8 + var7 * 30].clearZones();
                     }
                  }
               }
            }
         }

         IsoMetaGrid.ZoneGeometryType[] var26 = IsoMetaGrid.ZoneGeometryType.values();
         TIntArrayList var28 = new TIntArrayList();
         String var9;
         int var10;
         int var12;
         int var13;
         int var14;
         int var16;
         int var30;
         int var39;
         if (var2 >= 141) {
            var30 = var1.getInt();
            HashMap var31 = new HashMap();

            for(var8 = 0; var8 < var30; ++var8) {
               var9 = GameWindow.ReadStringUTF(var1);
               var31.put(var8, var9);
            }

            var8 = var1.getInt();
            DebugLog.log("loading " + var8 + " zones from map_zone.bin");

            int var33;
            String var35;
            for(var33 = 0; var33 < var8; ++var33) {
               String var34 = (String)var31.get(Integer.valueOf(var1.getShort()));
               var35 = (String)var31.get(Integer.valueOf(var1.getShort()));
               var12 = var1.getInt();
               var13 = var1.getInt();
               byte var37 = var1.get();
               int var15 = var1.getInt();
               var16 = var1.getInt();
               IsoMetaGrid.ZoneGeometryType var38 = IsoMetaGrid.ZoneGeometryType.INVALID;
               var28.clear();
               var39 = 0;
               if (var2 >= 185) {
                  byte var19 = var1.get();
                  if (var19 < 0 || var19 >= var26.length) {
                     var19 = 0;
                  }

                  var38 = var26[var19];
                  if (var38 != IsoMetaGrid.ZoneGeometryType.INVALID) {
                     if (var2 >= 186 && var38 == IsoMetaGrid.ZoneGeometryType.Polyline) {
                        var39 = PZMath.clamp(var1.get(), 0, 255);
                     }

                     short var20 = var1.getShort();

                     for(int var21 = 0; var21 < var20; ++var21) {
                        var28.add(var1.getInt());
                     }
                  }
               }

               int var40 = var1.getInt();
               IsoMetaGrid.Zone var41 = this.registerZone(var34, var35, var12, var13, var37, var15, var16, var38, var38 == IsoMetaGrid.ZoneGeometryType.INVALID ? null : var28, var39);
               var41.hourLastSeen = var40;
               var41.haveConstruction = var1.get() == 1;
               var41.lastActionTimestamp = var1.getInt();
               var41.setOriginalName((String)var31.get(Integer.valueOf(var1.getShort())));
               var41.id = var1.getDouble();
            }

            var33 = var1.getInt();

            for(var10 = 0; var10 < var33; ++var10) {
               var35 = GameWindow.ReadString(var1);
               ArrayList var36 = new ArrayList();
               var13 = var1.getInt();

               for(var14 = 0; var14 < var13; ++var14) {
                  var36.add(var1.getDouble());
               }

               IsoWorld.instance.getSpawnedZombieZone().put(var35, var36);
            }

            return;
         }

         var30 = var1.getInt();
         DebugLog.log("loading " + var30 + " zones from map_zone.bin");
         if (var2 <= 112 && var30 > var22 * 2) {
            DebugLog.log("ERROR: seems like too many zones in map_zone.bin");
            return;
         }

         for(var7 = 0; var7 < var30; ++var7) {
            String var32 = GameWindow.ReadString(var1);
            var9 = GameWindow.ReadString(var1);
            var10 = var1.getInt();
            int var11 = var1.getInt();
            var12 = var1.getInt();
            var13 = var1.getInt();
            var14 = var1.getInt();
            if (var2 < 121) {
               var1.getInt();
            } else {
               boolean var10000 = false;
            }

            var16 = var2 < 68 ? var1.getShort() : var1.getInt();
            IsoMetaGrid.Zone var17 = this.registerZone(var32, var9, var10, var11, var12, var13, var14);
            var17.hourLastSeen = var16;
            if (var2 >= 35) {
               boolean var18 = var1.get() == 1;
               var17.haveConstruction = var18;
            }

            if (var2 >= 41) {
               var17.lastActionTimestamp = var1.getInt();
            }

            if (var2 >= 98) {
               var17.setOriginalName(GameWindow.ReadString(var1));
            }

            if (var2 >= 110 && var2 < 121) {
               var39 = var1.getInt();
            }

            var17.id = var1.getDouble();
         }
      }

   }

   public void saveZone(ByteBuffer var1) {
      var1.put((byte)90);
      var1.put((byte)79);
      var1.put((byte)78);
      var1.put((byte)69);
      var1.putInt(186);
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < this.Zones.size(); ++var3) {
         IsoMetaGrid.Zone var4 = (IsoMetaGrid.Zone)this.Zones.get(var3);
         var2.add(var4.getName());
         var2.add(var4.getOriginalName());
         var2.add(var4.getType());
      }

      ArrayList var9 = new ArrayList(var2);
      HashMap var10 = new HashMap();

      int var5;
      for(var5 = 0; var5 < var9.size(); ++var5) {
         var10.put((String)var9.get(var5), var5);
      }

      if (var9.size() > 32767) {
         throw new IllegalStateException("IsoMetaGrid.saveZone() string table is too large");
      } else {
         var1.putInt(var9.size());

         for(var5 = 0; var5 < var9.size(); ++var5) {
            GameWindow.WriteString(var1, (String)var9.get(var5));
         }

         var1.putInt(this.Zones.size());

         for(var5 = 0; var5 < this.Zones.size(); ++var5) {
            IsoMetaGrid.Zone var6 = (IsoMetaGrid.Zone)this.Zones.get(var5);
            var1.putShort(((Integer)var10.get(var6.getName())).shortValue());
            var1.putShort(((Integer)var10.get(var6.getType())).shortValue());
            var1.putInt(var6.x);
            var1.putInt(var6.y);
            var1.put((byte)var6.z);
            var1.putInt(var6.w);
            var1.putInt(var6.h);
            var1.put((byte)var6.geometryType.ordinal());
            if (!var6.isRectangle()) {
               if (var6.isPolyline()) {
                  var1.put((byte)var6.polylineWidth);
               }

               var1.putShort((short)var6.points.size());

               for(int var7 = 0; var7 < var6.points.size(); ++var7) {
                  var1.putInt(var6.points.get(var7));
               }
            }

            var1.putInt(var6.hourLastSeen);
            var1.put((byte)(var6.haveConstruction ? 1 : 0));
            var1.putInt(var6.lastActionTimestamp);
            var1.putShort(((Integer)var10.get(var6.getOriginalName())).shortValue());
            var1.putDouble(var6.id);
         }

         var2.clear();
         var9.clear();
         var10.clear();
         var1.putInt(IsoWorld.instance.getSpawnedZombieZone().size());
         Iterator var12 = IsoWorld.instance.getSpawnedZombieZone().keySet().iterator();

         while(var12.hasNext()) {
            String var11 = (String)var12.next();
            ArrayList var13 = (ArrayList)IsoWorld.instance.getSpawnedZombieZone().get(var11);
            GameWindow.WriteString(var1, var11);
            var1.putInt(var13.size());

            for(int var8 = 0; var8 < var13.size(); ++var8) {
               var1.putDouble((Double)var13.get(var8));
            }
         }

      }
   }

   private void getLotDirectories(String var1, ArrayList var2) {
      if (!var2.contains(var1)) {
         ChooseGameInfo.Map var3 = ChooseGameInfo.getMapDetails(var1);
         if (var3 != null) {
            var2.add(var1);
            Iterator var4 = var3.getLotDirectories().iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               this.getLotDirectories(var5, var2);
            }

         }
      }
   }

   public ArrayList getLotDirectories() {
      if (GameClient.bClient) {
         Core.GameMap = GameClient.GameMap;
      }

      if (GameServer.bServer) {
         Core.GameMap = GameServer.GameMap;
      }

      if (Core.GameMap.equals("DEFAULT")) {
         MapGroups var1 = new MapGroups();
         var1.createGroups();
         if (var1.getNumberOfGroups() != 1) {
            throw new RuntimeException("GameMap is DEFAULT but there are multiple worlds to choose from");
         }

         var1.setWorld(0);
      }

      ArrayList var5 = new ArrayList();
      if (Core.GameMap.contains(";")) {
         String[] var2 = Core.GameMap.split(";");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].trim();
            if (!var4.isEmpty() && !var5.contains(var4)) {
               var5.add(var4);
            }
         }
      } else {
         this.getLotDirectories(Core.GameMap, var5);
      }

      return var5;
   }

   public static boolean isPreferredZoneForSquare(String var0) {
      return s_PreferredZoneTypes.contains(var0);
   }

   static {
      s_PreferredZoneTypes.add("DeepForest");
      s_PreferredZoneTypes.add("Farm");
      s_PreferredZoneTypes.add("FarmLand");
      s_PreferredZoneTypes.add("Forest");
      s_PreferredZoneTypes.add("Vegitation");
      s_PreferredZoneTypes.add("Nav");
      s_PreferredZoneTypes.add("TownZone");
      s_PreferredZoneTypes.add("TrailerPark");
   }

   private final class MetaGridLoaderThread extends Thread {
      final SharedStrings sharedStrings = new SharedStrings();
      final ArrayList Buildings = new ArrayList();
      final ArrayList tempRooms = new ArrayList();
      int wY;

      MetaGridLoaderThread(int var2) {
         this.wY = var2;
      }

      public void run() {
         try {
            this.runInner();
         } catch (Exception var2) {
            ExceptionLogger.logException(var2);
         }

      }

      void runInner() {
         for(int var1 = this.wY; var1 <= IsoMetaGrid.this.maxY; var1 += 8) {
            for(int var2 = IsoMetaGrid.this.minX; var2 <= IsoMetaGrid.this.maxX; ++var2) {
               this.loadCell(var2, var1);
            }
         }

      }

      void loadCell(int var1, int var2) {
         IsoMetaCell var3 = new IsoMetaCell(var1, var2);
         IsoMetaGrid.this.Grid[var1 - IsoMetaGrid.this.minX][var2 - IsoMetaGrid.this.minY] = var3;
         String var4 = var1 + "_" + var2 + ".lotheader";
         if (IsoLot.InfoFileNames.containsKey(var4)) {
            LotHeader var5 = (LotHeader)IsoLot.InfoHeaders.get(var4);
            if (var5 != null) {
               File var6 = new File((String)IsoLot.InfoFileNames.get(var4));
               if (var6.exists()) {
                  var3.info = var5;

                  try {
                     BufferedRandomAccessFile var7 = new BufferedRandomAccessFile(var6.getAbsolutePath(), "r", 4096);

                     try {
                        var5.version = IsoLot.readInt(var7);
                        int var8 = IsoLot.readInt(var7);

                        int var9;
                        for(var9 = 0; var9 < var8; ++var9) {
                           String var10 = IsoLot.readString(var7);
                           var5.tilesUsed.add(this.sharedStrings.get(var10.trim()));
                        }

                        var7.read();
                        var5.width = IsoLot.readInt(var7);
                        var5.height = IsoLot.readInt(var7);
                        var5.levels = IsoLot.readInt(var7);
                        var9 = IsoLot.readInt(var7);

                        int var13;
                        int var14;
                        int var22;
                        for(var22 = 0; var22 < var9; ++var22) {
                           String var11 = IsoLot.readString(var7);
                           RoomDef var12 = new RoomDef(var22, this.sharedStrings.get(var11));
                           var12.level = IsoLot.readInt(var7);
                           var13 = IsoLot.readInt(var7);

                           for(var14 = 0; var14 < var13; ++var14) {
                              RoomDef.RoomRect var15 = new RoomDef.RoomRect(IsoLot.readInt(var7) + var1 * 300, IsoLot.readInt(var7) + var2 * 300, IsoLot.readInt(var7), IsoLot.readInt(var7));
                              var12.rects.add(var15);
                           }

                           var12.CalculateBounds();
                           var5.Rooms.put(var12.ID, var12);
                           var5.RoomList.add(var12);
                           var3.addRoom(var12, var1 * 300, var2 * 300);
                           var14 = IsoLot.readInt(var7);

                           for(int var26 = 0; var26 < var14; ++var26) {
                              int var16 = IsoLot.readInt(var7);
                              int var17 = IsoLot.readInt(var7);
                              int var18 = IsoLot.readInt(var7);
                              var12.objects.add(new MetaObject(var16, var17 + var1 * 300 - var12.x, var18 + var2 * 300 - var12.y, var12));
                           }

                           var12.bLightsActive = Rand.Next(2) == 0;
                        }

                        var22 = IsoLot.readInt(var7);

                        int var23;
                        for(var23 = 0; var23 < var22; ++var23) {
                           BuildingDef var24 = new BuildingDef();
                           var13 = IsoLot.readInt(var7);
                           var24.ID = var23;

                           for(var14 = 0; var14 < var13; ++var14) {
                              RoomDef var28 = (RoomDef)var5.Rooms.get(IsoLot.readInt(var7));
                              var28.building = var24;
                              if (var28.isEmptyOutside()) {
                                 var24.emptyoutside.add(var28);
                              } else {
                                 var24.rooms.add(var28);
                              }
                           }

                           var24.CalculateBounds(this.tempRooms);
                           var5.Buildings.add(var24);
                           this.Buildings.add(var24);
                        }

                        for(var23 = 0; var23 < 30; ++var23) {
                           for(int var25 = 0; var25 < 30; ++var25) {
                              var13 = var7.read();
                              IsoMetaChunk var27 = var3.getChunk(var23, var25);
                              var27.setZombieIntensity(var13);
                           }
                        }
                     } catch (Throwable var20) {
                        try {
                           var7.close();
                        } catch (Throwable var19) {
                           var20.addSuppressed(var19);
                        }

                        throw var20;
                     }

                     var7.close();
                  } catch (Exception var21) {
                     DebugLog.log("ERROR loading " + var6.getAbsolutePath());
                     ExceptionLogger.logException(var21);
                  }

               }
            }
         }
      }

      void postLoad() {
         IsoMetaGrid.this.Buildings.addAll(this.Buildings);
         this.Buildings.clear();
         this.sharedStrings.clear();
         this.tempRooms.clear();
      }
   }

   public static class Zone {
      public Double id = 0.0D;
      public int hourLastSeen = 0;
      public int lastActionTimestamp = 0;
      public boolean haveConstruction = false;
      public final HashMap spawnedZombies = new HashMap();
      public String zombiesTypeToSpawn = null;
      public Boolean spawnSpecialZombies = null;
      public String name;
      public String type;
      public int x;
      public int y;
      public int z;
      public int w;
      public int h;
      public IsoMetaGrid.ZoneGeometryType geometryType;
      public final TIntArrayList points;
      public int polylineWidth;
      public float[] polylineOutlinePoints;
      public float[] triangles;
      public int pickedXForZoneStory;
      public int pickedYForZoneStory;
      public RandomizedZoneStoryBase pickedRZStory;
      private String originalName;
      public boolean isPreferredZoneForSquare;
      static final PolygonalMap2.LiangBarsky LIANG_BARSKY = new PolygonalMap2.LiangBarsky();
      static final Vector2 L_lineSegmentIntersects = new Vector2();

      public Zone(String var1, String var2, int var3, int var4, int var5, int var6, int var7) {
         this.geometryType = IsoMetaGrid.ZoneGeometryType.INVALID;
         this.points = new TIntArrayList();
         this.polylineWidth = 0;
         this.isPreferredZoneForSquare = false;
         this.id = (double)Rand.Next(9999999) + 100000.0D;
         this.originalName = var1;
         this.name = var1;
         this.type = var2;
         this.x = var3;
         this.y = var4;
         this.z = var5;
         this.w = var6;
         this.h = var7;
      }

      public void setX(int var1) {
         this.x = var1;
      }

      public void setY(int var1) {
         this.y = var1;
      }

      public void setW(int var1) {
         this.w = var1;
      }

      public void setH(int var1) {
         this.h = var1;
      }

      public boolean isPoint() {
         return this.geometryType == IsoMetaGrid.ZoneGeometryType.Point;
      }

      public boolean isPolygon() {
         return this.geometryType == IsoMetaGrid.ZoneGeometryType.Polygon;
      }

      public boolean isPolyline() {
         return this.geometryType == IsoMetaGrid.ZoneGeometryType.Polyline;
      }

      public boolean isRectangle() {
         return this.geometryType == IsoMetaGrid.ZoneGeometryType.INVALID;
      }

      public void setPickedXForZoneStory(int var1) {
         this.pickedXForZoneStory = var1;
      }

      public void setPickedYForZoneStory(int var1) {
         this.pickedYForZoneStory = var1;
      }

      public float getHoursSinceLastSeen() {
         return (float)GameTime.instance.getWorldAgeHours() - (float)this.hourLastSeen;
      }

      public void setHourSeenToCurrent() {
         this.hourLastSeen = (int)GameTime.instance.getWorldAgeHours();
      }

      public void setHaveConstruction(boolean var1) {
         this.haveConstruction = var1;
         if (GameClient.bClient) {
            ByteBufferWriter var2 = GameClient.connection.startPacket();
            PacketTypes.PacketType.ConstructedZone.doPacket(var2);
            var2.putInt(this.x);
            var2.putInt(this.y);
            var2.putInt(this.z);
            PacketTypes.PacketType.ConstructedZone.send(GameClient.connection);
         }

      }

      public boolean haveCons() {
         return this.haveConstruction;
      }

      public int getZombieDensity() {
         IsoMetaChunk var1 = IsoWorld.instance.MetaGrid.getChunkDataFromTile(this.x, this.y);
         return var1 != null ? var1.getUnadjustedZombieIntensity() : 0;
      }

      public boolean contains(int var1, int var2, int var3) {
         if (var3 != this.z) {
            return false;
         } else if (var1 >= this.x && var1 < this.x + this.w) {
            if (var2 >= this.y && var2 < this.y + this.h) {
               if (this.isPoint()) {
                  return false;
               } else if (this.isPolyline()) {
                  if (this.polylineWidth > 0) {
                     this.checkPolylineOutline();
                     return this.isPointInPolyline_WindingNumber((float)var1 + 0.5F, (float)var2 + 0.5F, 0) == IsoMetaGrid.Zone.PolygonHit.Inside;
                  } else {
                     return false;
                  }
               } else if (this.isPolygon()) {
                  return this.isPointInPolygon_WindingNumber((float)var1 + 0.5F, (float)var2 + 0.5F, 0) == IsoMetaGrid.Zone.PolygonHit.Inside;
               } else {
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public boolean intersects(int var1, int var2, int var3, int var4, int var5) {
         if (this.z != var3) {
            return false;
         } else if (var1 + var4 > this.x && var1 < this.x + this.w) {
            if (var2 + var5 > this.y && var2 < this.y + this.h) {
               if (this.isPolygon()) {
                  return this.polygonRectIntersect(var1, var2, var4, var5);
               } else if (this.isPolyline()) {
                  if (this.polylineWidth > 0) {
                     this.checkPolylineOutline();
                     return this.polylineOutlineRectIntersect(var1, var2, var4, var5);
                  } else {
                     for(int var6 = 0; var6 < this.points.size() - 2; var6 += 2) {
                        int var7 = this.points.getQuick(var6);
                        int var8 = this.points.getQuick(var6 + 1);
                        int var9 = this.points.getQuick(var6 + 2);
                        int var10 = this.points.getQuick(var6 + 3);
                        if (LIANG_BARSKY.lineRectIntersect((float)var7, (float)var8, (float)(var9 - var7), (float)(var10 - var8), (float)var1, (float)var2, (float)(var1 + var4), (float)(var2 + var5))) {
                           return true;
                        }
                     }

                     return false;
                  }
               } else {
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public boolean difference(int var1, int var2, int var3, int var4, int var5, ArrayList var6) {
         var6.clear();
         if (!this.intersects(var1, var2, var3, var4, var5)) {
            return false;
         } else if (this.isRectangle()) {
            int var14;
            int var15;
            if (this.x < var1) {
               var14 = Math.max(var2, this.y);
               var15 = Math.min(var2 + var5, this.y + this.h);
               var6.add(new IsoMetaGrid.Zone(this.name, this.type, this.x, var14, var3, var1 - this.x, var15 - var14));
            }

            if (var1 + var4 < this.x + this.w) {
               var14 = Math.max(var2, this.y);
               var15 = Math.min(var2 + var5, this.y + this.h);
               var6.add(new IsoMetaGrid.Zone(this.name, this.type, var1 + var4, var14, var3, this.x + this.w - (var1 + var4), var15 - var14));
            }

            if (this.y < var2) {
               var6.add(new IsoMetaGrid.Zone(this.name, this.type, this.x, this.y, var3, this.w, var2 - this.y));
            }

            if (var2 + var5 < this.y + this.h) {
               var6.add(new IsoMetaGrid.Zone(this.name, this.type, this.x, var2 + var5, var3, this.w, this.y + this.h - (var2 + var5)));
            }

            return true;
         } else {
            if (this.isPolygon()) {
               if (IsoMetaGrid.s_clipper == null) {
                  IsoMetaGrid.s_clipper = new Clipper();
                  IsoMetaGrid.s_clipperBuffer = ByteBuffer.allocateDirect(2048);
               }

               Clipper var7 = IsoMetaGrid.s_clipper;
               ByteBuffer var8 = IsoMetaGrid.s_clipperBuffer;
               var8.clear();

               int var9;
               for(var9 = 0; var9 < this.points.size(); var9 += 2) {
                  var8.putFloat((float)this.points.getQuick(var9));
                  var8.putFloat((float)this.points.getQuick(var9 + 1));
               }

               var7.clear();
               var7.addPath(this.points.size() / 2, var8, false);
               var7.clipAABB((float)var1, (float)var2, (float)(var1 + var4), (float)(var2 + var5));
               var9 = var7.generatePolygons();

               for(int var10 = 0; var10 < var9; ++var10) {
                  var8.clear();
                  var7.getPolygon(var10, var8);
                  short var11 = var8.getShort();
                  if (var11 < 3) {
                     var8.position(var8.position() + var11 * 4 * 2);
                  } else {
                     IsoMetaGrid.Zone var12 = new IsoMetaGrid.Zone(this.name, this.type, this.x, this.y, this.z, this.w, this.h);
                     var12.geometryType = IsoMetaGrid.ZoneGeometryType.Polygon;

                     for(int var13 = 0; var13 < var11; ++var13) {
                        var12.points.add((int)var8.getFloat());
                        var12.points.add((int)var8.getFloat());
                     }

                     var6.add(var12);
                  }
               }
            }

            if (this.isPolyline()) {
            }

            return true;
         }
      }

      public IsoGridSquare getRandomSquareInZone() {
         return IsoWorld.instance.getCell().getGridSquare(Rand.Next(this.x, this.x + this.w), Rand.Next(this.y, this.y + this.h), this.z);
      }

      public IsoGridSquare getRandomUnseenSquareInZone() {
         return null;
      }

      public void addSquare(IsoGridSquare var1) {
      }

      public ArrayList getSquares() {
         return null;
      }

      public void removeSquare(IsoGridSquare var1) {
      }

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public String getType() {
         return this.type;
      }

      public void setType(String var1) {
         this.type = var1;
      }

      public int getLastActionTimestamp() {
         return this.lastActionTimestamp;
      }

      public void setLastActionTimestamp(int var1) {
         this.lastActionTimestamp = var1;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getHeight() {
         return this.h;
      }

      public int getWidth() {
         return this.w;
      }

      public void sendToServer() {
         if (GameClient.bClient) {
            GameClient.registerZone(this, true);
         }

      }

      public String getOriginalName() {
         return this.originalName;
      }

      public void setOriginalName(String var1) {
         this.originalName = var1;
      }

      public int getClippedSegmentOfPolyline(int var1, int var2, int var3, int var4, double[] var5) {
         if (!this.isPolyline()) {
            return -1;
         } else {
            float var6 = this.polylineWidth % 2 == 0 ? 0.0F : 0.5F;

            for(int var7 = 0; var7 < this.points.size() - 2; var7 += 2) {
               int var8 = this.points.getQuick(var7);
               int var9 = this.points.getQuick(var7 + 1);
               int var10 = this.points.getQuick(var7 + 2);
               int var11 = this.points.getQuick(var7 + 3);
               if (LIANG_BARSKY.lineRectIntersect((float)var8 + var6, (float)var9 + var6, (float)(var10 - var8), (float)(var11 - var9), (float)var1, (float)var2, (float)var3, (float)var4, var5)) {
                  return var7 / 2;
               }
            }

            return -1;
         }
      }

      private void checkPolylineOutline() {
         if (this.polylineOutlinePoints == null) {
            if (this.isPolyline()) {
               if (this.polylineWidth > 0) {
                  if (IsoMetaGrid.s_clipperOffset == null) {
                     IsoMetaGrid.s_clipperOffset = new ClipperOffset();
                     IsoMetaGrid.s_clipperBuffer = ByteBuffer.allocateDirect(2048);
                  }

                  ClipperOffset var1 = IsoMetaGrid.s_clipperOffset;
                  ByteBuffer var2 = IsoMetaGrid.s_clipperBuffer;
                  var1.clear();
                  var2.clear();
                  float var3 = this.polylineWidth % 2 == 0 ? 0.0F : 0.5F;

                  int var4;
                  int var6;
                  for(var4 = 0; var4 < this.points.size(); var4 += 2) {
                     int var5 = this.points.get(var4);
                     var6 = this.points.get(var4 + 1);
                     var2.putFloat((float)var5 + var3);
                     var2.putFloat((float)var6 + var3);
                  }

                  var2.flip();
                  var1.addPath(this.points.size() / 2, var2, ClipperOffset.JoinType.jtMiter.ordinal(), ClipperOffset.EndType.etOpenButt.ordinal());
                  var1.execute((double)((float)this.polylineWidth / 2.0F));
                  var4 = var1.getPolygonCount();
                  if (var4 < 1) {
                     DebugLog.General.warn("Failed to generate polyline outline");
                  } else {
                     var2.clear();
                     var1.getPolygon(0, var2);
                     short var7 = var2.getShort();
                     this.polylineOutlinePoints = new float[var7 * 2];

                     for(var6 = 0; var6 < var7; ++var6) {
                        this.polylineOutlinePoints[var6 * 2] = var2.getFloat();
                        this.polylineOutlinePoints[var6 * 2 + 1] = var2.getFloat();
                     }

                  }
               }
            }
         }
      }

      float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
         return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      }

      IsoMetaGrid.Zone.PolygonHit isPointInPolygon_WindingNumber(float var1, float var2, int var3) {
         int var4 = 0;

         for(int var5 = 0; var5 < this.points.size(); var5 += 2) {
            int var6 = this.points.getQuick(var5);
            int var7 = this.points.getQuick(var5 + 1);
            int var8 = this.points.getQuick((var5 + 2) % this.points.size());
            int var9 = this.points.getQuick((var5 + 3) % this.points.size());
            if ((float)var7 <= var2) {
               if ((float)var9 > var2 && this.isLeft((float)var6, (float)var7, (float)var8, (float)var9, var1, var2) > 0.0F) {
                  ++var4;
               }
            } else if ((float)var9 <= var2 && this.isLeft((float)var6, (float)var7, (float)var8, (float)var9, var1, var2) < 0.0F) {
               --var4;
            }
         }

         return var4 == 0 ? IsoMetaGrid.Zone.PolygonHit.Outside : IsoMetaGrid.Zone.PolygonHit.Inside;
      }

      IsoMetaGrid.Zone.PolygonHit isPointInPolyline_WindingNumber(float var1, float var2, int var3) {
         int var4 = 0;
         float[] var5 = this.polylineOutlinePoints;
         if (var5 == null) {
            return IsoMetaGrid.Zone.PolygonHit.Outside;
         } else {
            for(int var6 = 0; var6 < var5.length; var6 += 2) {
               float var7 = var5[var6];
               float var8 = var5[var6 + 1];
               float var9 = var5[(var6 + 2) % var5.length];
               float var10 = var5[(var6 + 3) % var5.length];
               if (var8 <= var2) {
                  if (var10 > var2 && this.isLeft(var7, var8, var9, var10, var1, var2) > 0.0F) {
                     ++var4;
                  }
               } else if (var10 <= var2 && this.isLeft(var7, var8, var9, var10, var1, var2) < 0.0F) {
                  --var4;
               }
            }

            return var4 == 0 ? IsoMetaGrid.Zone.PolygonHit.Outside : IsoMetaGrid.Zone.PolygonHit.Inside;
         }
      }

      boolean polygonRectIntersect(int var1, int var2, int var3, int var4) {
         if (this.x >= var1 && this.x + this.w <= var1 + var3 && this.y >= var2 && this.y + this.h <= var2 + var4) {
            return true;
         } else {
            return this.lineSegmentIntersects((float)var1, (float)var2, (float)(var1 + var3), (float)var2) || this.lineSegmentIntersects((float)(var1 + var3), (float)var2, (float)(var1 + var3), (float)(var2 + var4)) || this.lineSegmentIntersects((float)(var1 + var3), (float)(var2 + var4), (float)var1, (float)(var2 + var4)) || this.lineSegmentIntersects((float)var1, (float)(var2 + var4), (float)var1, (float)var2);
         }
      }

      boolean lineSegmentIntersects(float var1, float var2, float var3, float var4) {
         L_lineSegmentIntersects.set(var3 - var1, var4 - var2);
         float var5 = L_lineSegmentIntersects.getLength();
         L_lineSegmentIntersects.normalize();
         float var6 = L_lineSegmentIntersects.x;
         float var7 = L_lineSegmentIntersects.y;

         for(int var8 = 0; var8 < this.points.size(); var8 += 2) {
            float var9 = (float)this.points.getQuick(var8);
            float var10 = (float)this.points.getQuick(var8 + 1);
            float var11 = (float)this.points.getQuick((var8 + 2) % this.points.size());
            float var12 = (float)this.points.getQuick((var8 + 3) % this.points.size());
            float var17 = var1 - var9;
            float var18 = var2 - var10;
            float var19 = var11 - var9;
            float var20 = var12 - var10;
            float var21 = 1.0F / (var20 * var6 - var19 * var7);
            float var22 = (var19 * var18 - var20 * var17) * var21;
            if (var22 >= 0.0F && var22 <= var5) {
               float var23 = (var18 * var6 - var17 * var7) * var21;
               if (var23 >= 0.0F && var23 <= 1.0F) {
                  return true;
               }
            }
         }

         if (this.isPointInPolygon_WindingNumber((var1 + var3) / 2.0F, (var2 + var4) / 2.0F, 0) != IsoMetaGrid.Zone.PolygonHit.Outside) {
            return true;
         } else {
            return false;
         }
      }

      boolean polylineOutlineRectIntersect(int var1, int var2, int var3, int var4) {
         if (this.polylineOutlinePoints == null) {
            return false;
         } else if (this.x >= var1 && this.x + this.w <= var1 + var3 && this.y >= var2 && this.y + this.h <= var2 + var4) {
            return true;
         } else {
            return this.polylineOutlineSegmentIntersects((float)var1, (float)var2, (float)(var1 + var3), (float)var2) || this.polylineOutlineSegmentIntersects((float)(var1 + var3), (float)var2, (float)(var1 + var3), (float)(var2 + var4)) || this.polylineOutlineSegmentIntersects((float)(var1 + var3), (float)(var2 + var4), (float)var1, (float)(var2 + var4)) || this.polylineOutlineSegmentIntersects((float)var1, (float)(var2 + var4), (float)var1, (float)var2);
         }
      }

      boolean polylineOutlineSegmentIntersects(float var1, float var2, float var3, float var4) {
         L_lineSegmentIntersects.set(var3 - var1, var4 - var2);
         float var5 = L_lineSegmentIntersects.getLength();
         L_lineSegmentIntersects.normalize();
         float var6 = L_lineSegmentIntersects.x;
         float var7 = L_lineSegmentIntersects.y;
         float[] var8 = this.polylineOutlinePoints;

         for(int var9 = 0; var9 < var8.length; var9 += 2) {
            float var10 = var8[var9];
            float var11 = var8[var9 + 1];
            float var12 = var8[(var9 + 2) % var8.length];
            float var13 = var8[(var9 + 3) % var8.length];
            float var18 = var1 - var10;
            float var19 = var2 - var11;
            float var20 = var12 - var10;
            float var21 = var13 - var11;
            float var22 = 1.0F / (var21 * var6 - var20 * var7);
            float var23 = (var20 * var19 - var21 * var18) * var22;
            if (var23 >= 0.0F && var23 <= var5) {
               float var24 = (var19 * var6 - var18 * var7) * var22;
               if (var24 >= 0.0F && var24 <= 1.0F) {
                  return true;
               }
            }
         }

         if (this.isPointInPolyline_WindingNumber((var1 + var3) / 2.0F, (var2 + var4) / 2.0F, 0) != IsoMetaGrid.Zone.PolygonHit.Outside) {
            return true;
         } else {
            return false;
         }
      }

      private boolean isClockwise() {
         if (!this.isPolygon()) {
            return false;
         } else {
            float var1 = 0.0F;

            for(int var2 = 0; var2 < this.points.size(); var2 += 2) {
               int var3 = this.points.getQuick(var2);
               int var4 = this.points.getQuick(var2 + 1);
               int var5 = this.points.getQuick((var2 + 2) % this.points.size());
               int var6 = this.points.getQuick((var2 + 3) % this.points.size());
               var1 += (float)((var5 - var3) * (var6 + var4));
            }

            return (double)var1 > 0.0D;
         }
      }

      public float[] getPolygonTriangles() {
         if (this.triangles != null) {
            return this.triangles;
         } else if (!this.isPolygon()) {
            return null;
         } else {
            if (IsoMetaGrid.s_clipper == null) {
               IsoMetaGrid.s_clipper = new Clipper();
               IsoMetaGrid.s_clipperBuffer = ByteBuffer.allocateDirect(2048);
            }

            Clipper var1 = IsoMetaGrid.s_clipper;
            ByteBuffer var2 = IsoMetaGrid.s_clipperBuffer;
            var2.clear();
            int var3;
            if (this.isClockwise()) {
               for(var3 = this.points.size() - 1; var3 > 0; var3 -= 2) {
                  var2.putFloat((float)this.points.getQuick(var3 - 1));
                  var2.putFloat((float)this.points.getQuick(var3));
               }
            } else {
               for(var3 = 0; var3 < this.points.size(); var3 += 2) {
                  var2.putFloat((float)this.points.getQuick(var3));
                  var2.putFloat((float)this.points.getQuick(var3 + 1));
               }
            }

            var1.clear();
            var1.addPath(this.points.size() / 2, var2, false);
            var3 = var1.generatePolygons();
            if (var3 < 1) {
               return null;
            } else {
               var2.clear();
               int var4 = var1.triangulate(0, var2);
               this.triangles = new float[var4 * 2];

               for(int var5 = 0; var5 < var4; ++var5) {
                  this.triangles[var5 * 2] = var2.getFloat();
                  this.triangles[var5 * 2 + 1] = var2.getFloat();
               }

               return this.triangles;
            }
         }
      }

      public float[] getPolylineOutlineTriangles() {
         if (this.triangles != null) {
            return this.triangles;
         } else if (this.isPolyline() && this.polylineWidth > 0) {
            this.checkPolylineOutline();
            float[] var1 = this.polylineOutlinePoints;
            if (var1 == null) {
               return null;
            } else {
               if (IsoMetaGrid.s_clipper == null) {
                  IsoMetaGrid.s_clipper = new Clipper();
                  IsoMetaGrid.s_clipperBuffer = ByteBuffer.allocateDirect(2048);
               }

               Clipper var2 = IsoMetaGrid.s_clipper;
               ByteBuffer var3 = IsoMetaGrid.s_clipperBuffer;
               var3.clear();
               int var4;
               if (this.isClockwise()) {
                  for(var4 = var1.length - 1; var4 > 0; var4 -= 2) {
                     var3.putFloat(var1[var4 - 1]);
                     var3.putFloat(var1[var4]);
                  }
               } else {
                  for(var4 = 0; var4 < var1.length; var4 += 2) {
                     var3.putFloat(var1[var4]);
                     var3.putFloat(var1[var4 + 1]);
                  }
               }

               var2.clear();
               var2.addPath(var1.length / 2, var3, false);
               var4 = var2.generatePolygons();
               if (var4 < 1) {
                  return null;
               } else {
                  var3.clear();
                  int var5 = var2.triangulate(0, var3);
                  this.triangles = new float[var5 * 2];

                  for(int var6 = 0; var6 < var5; ++var6) {
                     this.triangles[var6 * 2] = var3.getFloat();
                     this.triangles[var6 * 2 + 1] = var3.getFloat();
                  }

                  return this.triangles;
               }
            }
         } else {
            return null;
         }
      }

      public void Dispose() {
         this.pickedRZStory = null;
         this.points.clear();
         this.polylineOutlinePoints = null;
         this.spawnedZombies.clear();
         this.triangles = null;
      }

      private static enum PolygonHit {
         OnEdge,
         Inside,
         Outside;

         // $FF: synthetic method
         private static IsoMetaGrid.Zone.PolygonHit[] $values() {
            return new IsoMetaGrid.Zone.PolygonHit[]{OnEdge, Inside, Outside};
         }
      }
   }

   public static final class VehicleZone extends IsoMetaGrid.Zone {
      public static final short VZF_FaceDirection = 1;
      public IsoDirections dir;
      public short flags;

      public VehicleZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, KahluaTable var8) {
         super(var1, var2, var3, var4, var5, var6, var7);
         this.dir = IsoDirections.Max;
         this.flags = 0;
         if (var8 != null) {
            Object var9 = var8.rawget("Direction");
            if (var9 instanceof String) {
               this.dir = IsoDirections.valueOf((String)var9);
            }

            var9 = var8.rawget("FaceDirection");
            if (var9 == Boolean.TRUE) {
               this.flags = (short)(this.flags | 1);
            }
         }

      }

      public boolean isFaceDirection() {
         return (this.flags & 1) != 0;
      }
   }

   public static enum ZoneGeometryType {
      INVALID,
      Point,
      Polyline,
      Polygon;

      // $FF: synthetic method
      private static IsoMetaGrid.ZoneGeometryType[] $values() {
         return new IsoMetaGrid.ZoneGeometryType[]{INVALID, Point, Polyline, Polygon};
      }
   }

   public static final class Trigger {
      public BuildingDef def;
      public int triggerRange;
      public int zombieExclusionRange;
      public String type;
      public boolean triggered = false;
      public KahluaTable data;

      public Trigger(BuildingDef var1, int var2, int var3, String var4) {
         this.def = var1;
         this.triggerRange = var2;
         this.zombieExclusionRange = var3;
         this.type = var4;
         this.data = LuaManager.platform.newTable();
      }

      public KahluaTable getModData() {
         return this.data;
      }
   }
}
