package zombie.iso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import zombie.SandboxOptions;
import zombie.core.Rand;

public final class IsoMetaChunk {
   public static final float zombiesMinPerChunk = 0.06F;
   public static final float zombiesFullPerChunk = 12.0F;
   private int ZombieIntensity = 0;
   private IsoMetaGrid.Zone[] zones;
   private int zonesSize;
   private RoomDef[] rooms;
   private int roomsSize;

   public float getZombieIntensity(boolean var1) {
      float var2 = (float)this.ZombieIntensity;
      float var3 = var2 / 255.0F;
      if (SandboxOptions.instance.Distribution.getValue() == 2) {
         var2 = 128.0F;
         var3 = 0.5F;
      }

      var2 *= 0.5F;
      if (SandboxOptions.instance.Zombies.getValue() == 1) {
         var2 *= 4.0F;
      } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
         var2 *= 3.0F;
      } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
         var2 *= 2.0F;
      } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
         var2 *= 0.35F;
      } else if (SandboxOptions.instance.Zombies.getValue() == 6) {
         var2 = 0.0F;
      }

      var3 = var2 / 255.0F;
      float var4 = 11.94F;
      var4 *= var3;
      var2 = 0.06F + var4;
      if (!var1) {
         return var2;
      } else {
         float var5 = var3 * 10.0F;
         if (Rand.Next(3) == 0) {
            return 0.0F;
         } else {
            var5 *= 0.5F;
            int var6 = 1000;
            if (SandboxOptions.instance.Zombies.getValue() == 1) {
               var6 = (int)((float)var6 / 2.0F);
            } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
               var6 = (int)((float)var6 / 1.7F);
            } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
               var6 = (int)((float)var6 / 1.5F);
            } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
               var6 = (int)((float)var6 * 1.5F);
            }

            if ((float)Rand.Next(var6) < var5 && IsoWorld.getZombiesEnabled()) {
               var2 = 120.0F;
               if (var2 > 12.0F) {
                  var2 = 12.0F;
               }
            }

            return var2;
         }
      }
   }

   public float getZombieIntensity() {
      return this.getZombieIntensity(true);
   }

   public void setZombieIntensity(int var1) {
      if (var1 >= 0) {
         this.ZombieIntensity = var1;
      }

   }

   public float getLootZombieIntensity() {
      float var1 = (float)this.ZombieIntensity;
      float var2 = var1 / 255.0F;
      var2 = var1 / 255.0F;
      float var3 = 11.94F;
      var3 *= var2;
      var1 = 0.06F + var3;
      float var4 = var2 * 10.0F;
      var2 = var2 * var2 * var2;
      if ((float)Rand.Next(300) <= var4) {
         var1 = 120.0F;
      }

      return IsoWorld.getZombiesDisabled() ? 400.0F : var1;
   }

   public int getUnadjustedZombieIntensity() {
      return this.ZombieIntensity;
   }

   public void addZone(IsoMetaGrid.Zone var1) {
      if (this.zones == null) {
         this.zones = new IsoMetaGrid.Zone[8];
      }

      if (this.zonesSize == this.zones.length) {
         IsoMetaGrid.Zone[] var2 = new IsoMetaGrid.Zone[this.zones.length + 8];
         System.arraycopy(this.zones, 0, var2, 0, this.zonesSize);
         this.zones = var2;
      }

      this.zones[this.zonesSize++] = var1;
   }

   public void removeZone(IsoMetaGrid.Zone var1) {
      if (this.zones != null) {
         for(int var2 = 0; var2 < this.zonesSize; ++var2) {
            if (this.zones[var2] == var1) {
               while(var2 < this.zonesSize - 1) {
                  this.zones[var2] = this.zones[var2 + 1];
                  ++var2;
               }

               this.zones[this.zonesSize - 1] = null;
               --this.zonesSize;
               break;
            }
         }

      }
   }

   public IsoMetaGrid.Zone getZone(int var1) {
      return var1 >= 0 && var1 < this.zonesSize ? this.zones[var1] : null;
   }

   public IsoMetaGrid.Zone getZoneAt(int var1, int var2, int var3) {
      if (this.zones != null && this.zonesSize > 0) {
         IsoMetaGrid.Zone var4 = null;

         for(int var5 = this.zonesSize - 1; var5 >= 0; --var5) {
            IsoMetaGrid.Zone var6 = this.zones[var5];
            if (var6.contains(var1, var2, var3)) {
               if (var6.isPreferredZoneForSquare) {
                  return var6;
               }

               if (var4 == null) {
                  var4 = var6;
               }
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   public ArrayList getZonesAt(int var1, int var2, int var3, ArrayList var4) {
      for(int var5 = 0; var5 < this.zonesSize; ++var5) {
         IsoMetaGrid.Zone var6 = this.zones[var5];
         if (var6.contains(var1, var2, var3)) {
            var4.add(var6);
         }
      }

      return var4;
   }

   public void getZonesUnique(Set var1) {
      for(int var2 = 0; var2 < this.zonesSize; ++var2) {
         IsoMetaGrid.Zone var3 = this.zones[var2];
         var1.add(var3);
      }

   }

   public void getZonesIntersecting(int var1, int var2, int var3, int var4, int var5, ArrayList var6) {
      for(int var7 = 0; var7 < this.zonesSize; ++var7) {
         IsoMetaGrid.Zone var8 = this.zones[var7];
         if (!var6.contains(var8) && var8.intersects(var1, var2, var3, var4, var5)) {
            var6.add(var8);
         }
      }

   }

   public void clearZones() {
      if (this.zones != null) {
         for(int var1 = 0; var1 < this.zones.length; ++var1) {
            this.zones[var1] = null;
         }
      }

      this.zones = null;
      this.zonesSize = 0;
   }

   public void clearRooms() {
      if (this.rooms != null) {
         for(int var1 = 0; var1 < this.rooms.length; ++var1) {
            this.rooms[var1] = null;
         }
      }

      this.rooms = null;
      this.roomsSize = 0;
   }

   public int numZones() {
      return this.zonesSize;
   }

   public void addRoom(RoomDef var1) {
      if (this.rooms == null) {
         this.rooms = new RoomDef[8];
      }

      if (this.roomsSize == this.rooms.length) {
         RoomDef[] var2 = new RoomDef[this.rooms.length + 8];
         System.arraycopy(this.rooms, 0, var2, 0, this.roomsSize);
         this.rooms = var2;
      }

      this.rooms[this.roomsSize++] = var1;
   }

   public RoomDef getRoomAt(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.roomsSize; ++var4) {
         RoomDef var5 = this.rooms[var4];
         if (!var5.isEmptyOutside() && var5.level == var3) {
            for(int var6 = 0; var6 < var5.rects.size(); ++var6) {
               RoomDef.RoomRect var7 = (RoomDef.RoomRect)var5.rects.get(var6);
               if (var7.x <= var1 && var7.y <= var2 && var1 < var7.getX2() && var2 < var7.getY2()) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public RoomDef getEmptyOutsideAt(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.roomsSize; ++var4) {
         RoomDef var5 = this.rooms[var4];
         if (var5.isEmptyOutside() && var5.level == var3) {
            for(int var6 = 0; var6 < var5.rects.size(); ++var6) {
               RoomDef.RoomRect var7 = (RoomDef.RoomRect)var5.rects.get(var6);
               if (var7.x <= var1 && var7.y <= var2 && var1 < var7.getX2() && var2 < var7.getY2()) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public int getNumRooms() {
      return this.roomsSize;
   }

   public void getRoomsIntersecting(int var1, int var2, int var3, int var4, ArrayList var5) {
      for(int var6 = 0; var6 < this.roomsSize; ++var6) {
         RoomDef var7 = this.rooms[var6];
         if (!var7.isEmptyOutside() && !var5.contains(var7) && var7.intersects(var1, var2, var3, var4)) {
            var5.add(var7);
         }
      }

   }

   public void Dispose() {
      if (this.rooms != null) {
         Arrays.fill(this.rooms, (Object)null);
      }

      if (this.zones != null) {
         Arrays.fill(this.zones, (Object)null);
      }

   }
}
