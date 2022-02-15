package zombie.iso;

import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import zombie.iso.areas.IsoBuilding;

public class IsoGridOcclusionData {
   public static final int MAXBUILDINGOCCLUDERS = 3;
   private static final THashSet _leftBuildings = new THashSet(3);
   private static final THashSet _rightBuildings = new THashSet(3);
   private static final THashSet _allBuildings = new THashSet(3);
   private static int _ObjectEpoch = 0;
   private final ArrayList _leftBuildingsArray = new ArrayList(3);
   private final ArrayList _rightBuildingsArray = new ArrayList(3);
   private final ArrayList _allBuildingsArray = new ArrayList(3);
   private IsoGridSquare _ownerSquare = null;
   private boolean _bSoftInitialized = false;
   private boolean _bLeftOccludedByOrphanStructures = false;
   private boolean _bRightOccludedByOrphanStructures = false;
   private int _objectEpoch = -1;

   public IsoGridOcclusionData(IsoGridSquare var1) {
      this._ownerSquare = var1;
   }

   public static void SquareChanged() {
      ++_ObjectEpoch;
      if (_ObjectEpoch < 0) {
         _ObjectEpoch = 0;
      }

   }

   public void Reset() {
      this._bSoftInitialized = false;
      this._bLeftOccludedByOrphanStructures = false;
      this._bRightOccludedByOrphanStructures = false;
      this._allBuildingsArray.clear();
      this._leftBuildingsArray.clear();
      this._rightBuildingsArray.clear();
      this._objectEpoch = -1;
   }

   public boolean getCouldBeOccludedByOrphanStructures(IsoGridOcclusionData.OcclusionFilter var1) {
      if (this._objectEpoch != _ObjectEpoch) {
         if (this._bSoftInitialized) {
            this.Reset();
         }

         this._objectEpoch = _ObjectEpoch;
      }

      if (!this._bSoftInitialized) {
         this.LazyInitializeSoftOccluders();
      }

      if (var1 == IsoGridOcclusionData.OcclusionFilter.Left) {
         return this._bLeftOccludedByOrphanStructures;
      } else if (var1 == IsoGridOcclusionData.OcclusionFilter.Right) {
         return this._bRightOccludedByOrphanStructures;
      } else {
         return this._bLeftOccludedByOrphanStructures || this._bRightOccludedByOrphanStructures;
      }
   }

   public ArrayList getBuildingsCouldBeOccluders(IsoGridOcclusionData.OcclusionFilter var1) {
      if (this._objectEpoch != _ObjectEpoch) {
         if (this._bSoftInitialized) {
            this.Reset();
         }

         this._objectEpoch = _ObjectEpoch;
      }

      if (!this._bSoftInitialized) {
         this.LazyInitializeSoftOccluders();
      }

      if (var1 == IsoGridOcclusionData.OcclusionFilter.Left) {
         return this._leftBuildingsArray;
      } else {
         return var1 == IsoGridOcclusionData.OcclusionFilter.Right ? this._rightBuildingsArray : this._allBuildingsArray;
      }
   }

   private void LazyInitializeSoftOccluders() {
      boolean var1 = false;
      int var2 = this._ownerSquare.getX();
      int var3 = this._ownerSquare.getY();
      int var4 = this._ownerSquare.getZ();
      _allBuildings.clear();
      _leftBuildings.clear();
      _rightBuildings.clear();
      var1 |= this.GetBuildingFloorsProjectedOnSquare(_allBuildings, var2, var3, var4);
      var1 |= this.GetBuildingFloorsProjectedOnSquare(_allBuildings, var2 + 1, var3 + 1, var4);
      var1 |= this.GetBuildingFloorsProjectedOnSquare(_allBuildings, var2 + 2, var3 + 2, var4);
      var1 |= this.GetBuildingFloorsProjectedOnSquare(_allBuildings, var2 + 3, var3 + 3, var4);
      this._bLeftOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_leftBuildings, var2, var3 + 1, var4);
      this._bLeftOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_leftBuildings, var2 + 1, var3 + 2, var4);
      this._bLeftOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_leftBuildings, var2 + 2, var3 + 3, var4);
      this._bRightOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_rightBuildings, var2 + 1, var3, var4);
      this._bRightOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_rightBuildings, var2 + 2, var3 + 1, var4);
      this._bRightOccludedByOrphanStructures |= this.GetBuildingFloorsProjectedOnSquare(_rightBuildings, var2 + 3, var3 + 2, var4);
      this._bLeftOccludedByOrphanStructures |= var1;
      _leftBuildings.addAll(_allBuildings);
      this._bRightOccludedByOrphanStructures |= var1;
      _rightBuildings.addAll(_allBuildings);
      _allBuildings.clear();
      _allBuildings.addAll(_leftBuildings);
      _allBuildings.addAll(_rightBuildings);
      this._leftBuildingsArray.addAll(_leftBuildings);
      this._rightBuildingsArray.addAll(_rightBuildings);
      this._allBuildingsArray.addAll(_allBuildings);
      this._bSoftInitialized = true;
   }

   private boolean GetBuildingFloorsProjectedOnSquare(THashSet var1, int var2, int var3, int var4) {
      boolean var5 = false;
      int var6 = var2;
      int var7 = var3;

      for(int var8 = var4; var8 < IsoCell.MaxHeight; var7 += 3) {
         IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var6, var7, var8);
         if (var9 != null) {
            IsoBuilding var10 = var9.getBuilding();
            if (var10 == null) {
               var10 = var9.roofHideBuilding;
            }

            if (var10 != null) {
               var1.add(var10);
            }

            for(int var11 = var8 - 1; var11 >= 0 && var10 == null; --var11) {
               IsoGridSquare var12 = IsoWorld.instance.CurrentCell.getGridSquare(var6, var7, var11);
               if (var12 != null) {
                  var10 = var12.getBuilding();
                  if (var10 == null) {
                     var10 = var12.roofHideBuilding;
                  }

                  if (var10 != null) {
                     var1.add(var10);
                  }
               }
            }

            if (var10 == null && !var5 && var9.getZ() != 0 && var9.getPlayerBuiltFloor() != null) {
               var5 = true;
            }
         }

         ++var8;
         var6 += 3;
      }

      return var5;
   }

   public static enum OcclusionFilter {
      Left,
      Right,
      All;

      // $FF: synthetic method
      private static IsoGridOcclusionData.OcclusionFilter[] $values() {
         return new IsoGridOcclusionData.OcclusionFilter[]{Left, Right, All};
      }
   }

   public static enum OccluderType {
      Unknown,
      NotFull,
      Full;

      // $FF: synthetic method
      private static IsoGridOcclusionData.OccluderType[] $values() {
         return new IsoGridOcclusionData.OccluderType[]{Unknown, NotFull, Full};
      }
   }
}
