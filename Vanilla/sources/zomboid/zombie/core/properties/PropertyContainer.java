package zombie.core.properties;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.TIntSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import zombie.core.TilePropertyAliasMap;
import zombie.core.Collections.NonBlockingHashMap;
import zombie.iso.SpriteDetails.IsoFlagType;

public final class PropertyContainer {
   private long SpriteFlags1 = 0L;
   private long SpriteFlags2 = 0L;
   private final TIntIntHashMap Properties = new TIntIntHashMap();
   private int[] keyArray;
   public static NonBlockingHashMap test = new NonBlockingHashMap();
   public static List sorted = Collections.synchronizedList(new ArrayList());
   private byte Surface;
   private byte SurfaceFlags;
   private short StackReplaceTileOffset;
   private static final byte SURFACE_VALID = 1;
   private static final byte SURFACE_ISOFFSET = 2;
   private static final byte SURFACE_ISTABLE = 4;
   private static final byte SURFACE_ISTABLETOP = 8;

   public void CreateKeySet() {
      TIntSet var1 = this.Properties.keySet();
      this.keyArray = var1.toArray();
   }

   public void AddProperties(PropertyContainer var1) {
      if (var1.keyArray != null) {
         for(int var2 = 0; var2 < var1.keyArray.length; ++var2) {
            int var3 = var1.keyArray[var2];
            this.Properties.put(var3, var1.Properties.get(var3));
         }

         this.SpriteFlags1 |= var1.SpriteFlags1;
         this.SpriteFlags2 |= var1.SpriteFlags2;
      }
   }

   public void Clear() {
      this.SpriteFlags1 = 0L;
      this.SpriteFlags2 = 0L;
      this.Properties.clear();
      this.SurfaceFlags &= -2;
   }

   public boolean Is(IsoFlagType var1) {
      long var2 = var1.index() / 64 == 0 ? this.SpriteFlags1 : this.SpriteFlags2;
      return (var2 & 1L << var1.index() % 64) != 0L;
   }

   public boolean Is(Double var1) {
      return this.Is(IsoFlagType.fromIndex(var1.intValue()));
   }

   public void Set(String var1, String var2) {
      this.Set(var1, var2, true);
   }

   public void Set(String var1, String var2, boolean var3) {
      if (var1 != null) {
         if (var3) {
            IsoFlagType var4 = IsoFlagType.FromString(var1);
            if (var4 != IsoFlagType.MAX) {
               this.Set(var4);
               return;
            }
         }

         int var6 = TilePropertyAliasMap.instance.getIDFromPropertyName(var1);
         if (var6 != -1) {
            int var5 = TilePropertyAliasMap.instance.getIDFromPropertyValue(var6, var2);
            this.SurfaceFlags &= -2;
            this.Properties.put(var6, var5);
         }
      }
   }

   public void Set(IsoFlagType var1) {
      if (var1.index() / 64 == 0) {
         this.SpriteFlags1 |= 1L << var1.index() % 64;
      } else {
         this.SpriteFlags2 |= 1L << var1.index() % 64;
      }

   }

   public void Set(IsoFlagType var1, String var2) {
      this.Set(var1);
   }

   public void UnSet(String var1) {
      int var2 = TilePropertyAliasMap.instance.getIDFromPropertyName(var1);
      this.Properties.remove(var2);
   }

   public void UnSet(IsoFlagType var1) {
      if (var1.index() / 64 == 0) {
         this.SpriteFlags1 &= ~(1L << var1.index() % 64);
      } else {
         this.SpriteFlags2 &= ~(1L << var1.index() % 64);
      }

   }

   public String Val(String var1) {
      int var2 = TilePropertyAliasMap.instance.getIDFromPropertyName(var1);
      return !this.Properties.containsKey(var2) ? null : TilePropertyAliasMap.instance.getPropertyValueString(var2, this.Properties.get(var2));
   }

   public boolean Is(String var1) {
      int var2 = TilePropertyAliasMap.instance.getIDFromPropertyName(var1);
      return this.Properties.containsKey(var2);
   }

   public ArrayList getFlagsList() {
      ArrayList var1 = new ArrayList();

      int var2;
      for(var2 = 0; var2 < 64; ++var2) {
         if ((this.SpriteFlags1 & 1L << var2) != 0L) {
            var1.add(IsoFlagType.fromIndex(var2));
         }
      }

      for(var2 = 0; var2 < 64; ++var2) {
         if ((this.SpriteFlags2 & 1L << var2) != 0L) {
            var1.add(IsoFlagType.fromIndex(64 + var2));
         }
      }

      return var1;
   }

   public ArrayList getPropertyNames() {
      ArrayList var1 = new ArrayList();
      TIntSet var2 = this.Properties.keySet();
      var2.forEach((var1x) -> {
         var1.add(((TilePropertyAliasMap.TileProperty)TilePropertyAliasMap.instance.Properties.get(var1x)).propertyName);
         return true;
      });
      Collections.sort(var1);
      return var1;
   }

   private void initSurface() {
      if ((this.SurfaceFlags & 1) == 0) {
         this.Surface = 0;
         this.StackReplaceTileOffset = 0;
         this.SurfaceFlags = 1;
         this.Properties.forEachEntry((var1, var2) -> {
            TilePropertyAliasMap.TileProperty var3 = (TilePropertyAliasMap.TileProperty)TilePropertyAliasMap.instance.Properties.get(var1);
            String var4 = var3.propertyName;
            String var5 = (String)var3.possibleValues.get(var2);
            if ("Surface".equals(var4) && var5 != null) {
               try {
                  int var6 = Integer.parseInt(var5);
                  if (var6 >= 0 && var6 <= 128) {
                     this.Surface = (byte)var6;
                  }
               } catch (NumberFormatException var8) {
               }
            } else if ("IsSurfaceOffset".equals(var4)) {
               this.SurfaceFlags = (byte)(this.SurfaceFlags | 2);
            } else if ("IsTable".equals(var4)) {
               this.SurfaceFlags = (byte)(this.SurfaceFlags | 4);
            } else if ("IsTableTop".equals(var4)) {
               this.SurfaceFlags = (byte)(this.SurfaceFlags | 8);
            } else if ("StackReplaceTileOffset".equals(var4)) {
               try {
                  this.StackReplaceTileOffset = (short)Integer.parseInt(var5);
               } catch (NumberFormatException var7) {
               }
            }

            return true;
         });
      }
   }

   public int getSurface() {
      this.initSurface();
      return this.Surface;
   }

   public boolean isSurfaceOffset() {
      this.initSurface();
      return (this.SurfaceFlags & 2) != 0;
   }

   public boolean isTable() {
      this.initSurface();
      return (this.SurfaceFlags & 4) != 0;
   }

   public boolean isTableTop() {
      this.initSurface();
      return (this.SurfaceFlags & 8) != 0;
   }

   public int getStackReplaceTileOffset() {
      this.initSurface();
      return this.StackReplaceTileOffset;
   }

   public static class MostTested {
      public IsoFlagType flag;
      public int count;
   }

   private static class ProfileEntryComparitor implements Comparator {
      public ProfileEntryComparitor() {
      }

      public int compare(Object var1, Object var2) {
         double var3 = (double)((PropertyContainer.MostTested)var1).count;
         double var5 = (double)((PropertyContainer.MostTested)var2).count;
         if (var3 > var5) {
            return -1;
         } else {
            return var5 > var3 ? 1 : 0;
         }
      }
   }
}
