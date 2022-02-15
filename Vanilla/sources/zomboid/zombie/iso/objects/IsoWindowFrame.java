package zombie.iso.objects;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.network.GameServer;

public class IsoWindowFrame {
   private static IsoWindowFrame.Direction getDirection(IsoObject var0) {
      if (!(var0 instanceof IsoWindow) && !(var0 instanceof IsoThumpable)) {
         if (var0 != null && var0.getProperties() != null && var0.getObjectIndex() != -1) {
            if (var0.getProperties().Is(IsoFlagType.WindowN)) {
               return IsoWindowFrame.Direction.NORTH;
            } else {
               return var0.getProperties().Is(IsoFlagType.WindowW) ? IsoWindowFrame.Direction.WEST : IsoWindowFrame.Direction.INVALID;
            }
         } else {
            return IsoWindowFrame.Direction.INVALID;
         }
      } else {
         return IsoWindowFrame.Direction.INVALID;
      }
   }

   public static boolean isWindowFrame(IsoObject var0) {
      return getDirection(var0).isValid();
   }

   public static boolean isWindowFrame(IsoObject var0, boolean var1) {
      IsoWindowFrame.Direction var2 = getDirection(var0);
      return var1 && var2 == IsoWindowFrame.Direction.NORTH || !var1 && var2 == IsoWindowFrame.Direction.WEST;
   }

   public static int countAddSheetRope(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      return var1.isValid() ? IsoWindow.countAddSheetRope(var0.getSquare(), var1 == IsoWindowFrame.Direction.NORTH) : 0;
   }

   public static boolean canAddSheetRope(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      return var1.isValid() && IsoWindow.canAddSheetRope(var0.getSquare(), var1 == IsoWindowFrame.Direction.NORTH);
   }

   public static boolean haveSheetRope(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      return var1.isValid() && IsoWindow.isTopOfSheetRopeHere(var0.getSquare(), var1 == IsoWindowFrame.Direction.NORTH);
   }

   public static boolean addSheetRope(IsoObject var0, IsoPlayer var1, String var2) {
      return !canAddSheetRope(var0) ? false : IsoWindow.addSheetRope(var1, var0.getSquare(), getDirection(var0) == IsoWindowFrame.Direction.NORTH, var2);
   }

   public static boolean removeSheetRope(IsoObject var0, IsoPlayer var1) {
      return !haveSheetRope(var0) ? false : IsoWindow.removeSheetRope(var1, var0.getSquare(), getDirection(var0) == IsoWindowFrame.Direction.NORTH);
   }

   public static IsoGridSquare getOppositeSquare(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      if (!var1.isValid()) {
         return null;
      } else {
         boolean var2 = var1 == IsoWindowFrame.Direction.NORTH;
         return var0.getSquare().getAdjacentSquare(var2 ? IsoDirections.N : IsoDirections.W);
      }
   }

   public static IsoGridSquare getIndoorSquare(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      if (!var1.isValid()) {
         return null;
      } else {
         IsoGridSquare var2 = var0.getSquare();
         if (var2.getRoom() != null) {
            return var2;
         } else {
            IsoGridSquare var3 = getOppositeSquare(var0);
            return var3 != null && var3.getRoom() != null ? var3 : null;
         }
      }
   }

   public static IsoCurtain getCurtain(IsoObject var0) {
      IsoWindowFrame.Direction var1 = getDirection(var0);
      if (!var1.isValid()) {
         return null;
      } else {
         boolean var2 = var1 == IsoWindowFrame.Direction.NORTH;
         IsoCurtain var3 = var0.getSquare().getCurtain(var2 ? IsoObjectType.curtainN : IsoObjectType.curtainW);
         if (var3 != null) {
            return var3;
         } else {
            IsoGridSquare var4 = getOppositeSquare(var0);
            return var4 == null ? null : var4.getCurtain(var2 ? IsoObjectType.curtainS : IsoObjectType.curtainE);
         }
      }
   }

   public static IsoGridSquare getAddSheetSquare(IsoObject var0, IsoGameCharacter var1) {
      IsoWindowFrame.Direction var2 = getDirection(var0);
      if (!var2.isValid()) {
         return null;
      } else {
         boolean var3 = var2 == IsoWindowFrame.Direction.NORTH;
         if (var1 != null && var1.getCurrentSquare() != null) {
            IsoGridSquare var4 = var1.getCurrentSquare();
            IsoGridSquare var5 = var0.getSquare();
            if (var3) {
               if (var4.getY() < var5.getY()) {
                  return var5.getAdjacentSquare(IsoDirections.N);
               }
            } else if (var4.getX() < var5.getX()) {
               return var5.getAdjacentSquare(IsoDirections.W);
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   public static void addSheet(IsoObject var0, IsoGameCharacter var1) {
      IsoWindowFrame.Direction var2 = getDirection(var0);
      if (var2.isValid()) {
         boolean var3 = var2 == IsoWindowFrame.Direction.NORTH;
         IsoGridSquare var4 = getIndoorSquare(var0);
         if (var4 == null) {
            var4 = var0.getSquare();
         }

         if (var1 != null) {
            var4 = getAddSheetSquare(var0, var1);
         }

         if (var4 != null) {
            IsoObjectType var5;
            if (var4 == var0.getSquare()) {
               var5 = var3 ? IsoObjectType.curtainN : IsoObjectType.curtainW;
            } else {
               var5 = var3 ? IsoObjectType.curtainS : IsoObjectType.curtainE;
            }

            if (var4.getCurtain(var5) == null) {
               int var6 = 16;
               if (var5 == IsoObjectType.curtainE) {
                  ++var6;
               }

               if (var5 == IsoObjectType.curtainS) {
                  var6 += 3;
               }

               if (var5 == IsoObjectType.curtainN) {
                  var6 += 2;
               }

               var6 += 4;
               IsoCurtain var7 = new IsoCurtain(var0.getCell(), var4, "fixtures_windows_curtains_01_" + var6, var3);
               var4.AddSpecialTileObject(var7);
               if (GameServer.bServer) {
                  var7.transmitCompleteItemToClients();
                  if (var1 != null) {
                     var1.sendObjectChange("removeOneOf", new Object[]{"type", "Sheet"});
                  }
               } else if (var1 != null) {
                  var1.getInventory().RemoveOneOf("Sheet");
               }

            }
         }
      }
   }

   public static boolean canClimbThrough(IsoObject var0, IsoGameCharacter var1) {
      IsoWindowFrame.Direction var2 = getDirection(var0);
      if (!var2.isValid()) {
         return false;
      } else if (var0.getSquare() == null) {
         return false;
      } else {
         IsoWindow var3 = var0.getSquare().getWindow(var2 == IsoWindowFrame.Direction.NORTH);
         if (var3 != null && var3.isBarricaded()) {
            return false;
         } else {
            if (var1 != null) {
               IsoGridSquare var4 = var2 == IsoWindowFrame.Direction.NORTH ? var0.getSquare().nav[IsoDirections.N.index()] : var0.getSquare().nav[IsoDirections.W.index()];
               if (!IsoWindow.canClimbThroughHelper(var1, var0.getSquare(), var4, var2 == IsoWindowFrame.Direction.NORTH)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private static enum Direction {
      INVALID,
      NORTH,
      WEST;

      public boolean isValid() {
         return this != INVALID;
      }

      // $FF: synthetic method
      private static IsoWindowFrame.Direction[] $values() {
         return new IsoWindowFrame.Direction[]{INVALID, NORTH, WEST};
      }
   }
}
