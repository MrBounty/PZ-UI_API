package zombie.randomizedWorld.randomizedBuilding.TableStories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import zombie.core.Rand;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.randomizedWorld.randomizedBuilding.RandomizedBuildingBase;

public class RBTableStoryBase extends RandomizedBuildingBase {
   public static ArrayList allStories = new ArrayList();
   public static int totalChance = 0;
   protected int chance = 0;
   protected ArrayList rooms = new ArrayList();
   protected boolean need2Tables = false;
   protected boolean ignoreAgainstWall = false;
   protected IsoObject table2 = null;
   protected IsoObject table1 = null;
   protected boolean westTable = false;
   private static final HashMap rbtsmap = new HashMap();
   public ArrayList fullTableMap = new ArrayList();

   public static void initStories(IsoGridSquare var0, IsoObject var1) {
      if (allStories.isEmpty()) {
         allStories.add(new RBTSBreakfast());
         allStories.add(new RBTSDinner());
         allStories.add(new RBTSSoup());
         allStories.add(new RBTSSewing());
         allStories.add(new RBTSElectronics());
         allStories.add(new RBTSFoodPreparation());
         allStories.add(new RBTSButcher());
         allStories.add(new RBTSSandwich());
         allStories.add(new RBTSDrink());
      }

      totalChance = 0;
      rbtsmap.clear();

      for(int var2 = 0; var2 < allStories.size(); ++var2) {
         RBTableStoryBase var3 = (RBTableStoryBase)allStories.get(var2);
         if (var3.isValid(var0, var1, false) && var3.isTimeValid(false)) {
            totalChance += var3.chance;
            rbtsmap.put(var3, var3.chance);
         }
      }

   }

   public static RBTableStoryBase getRandomStory(IsoGridSquare var0, IsoObject var1) {
      initStories(var0, var1);
      int var2 = Rand.Next(totalChance);
      Iterator var3 = rbtsmap.keySet().iterator();
      int var4 = 0;

      RBTableStoryBase var5;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var5 = (RBTableStoryBase)var3.next();
         var4 += (Integer)rbtsmap.get(var5);
      } while(var2 >= var4);

      var5.table1 = var1;
      return var5;
   }

   public boolean isValid(IsoGridSquare var1, IsoObject var2, boolean var3) {
      if (var3) {
         return true;
      } else if (this.rooms != null && var1.getRoom() != null && !this.rooms.contains(var1.getRoom().getName())) {
         return false;
      } else {
         if (this.need2Tables) {
            this.table2 = this.getSecondTable(var2);
            if (this.table2 == null) {
               return false;
            }
         }

         return !this.ignoreAgainstWall || !var1.getWallFull();
      }
   }

   public IsoObject getSecondTable(IsoObject var1) {
      this.westTable = true;
      IsoObject var2 = null;
      IsoCell var3 = IsoWorld.instance.CurrentCell;
      IsoGridSquare var4 = var3.getGridSquare((double)((int)var1.getX()), (double)((int)var1.getY()), (double)var1.getZ());
      if (this.ignoreAgainstWall && var4.getWallFull()) {
         return null;
      } else {
         var4 = var3.getGridSquare((double)((int)var1.getX() - 1), (double)((int)var1.getY()), (double)var1.getZ());
         var2 = this.checkForTable(var4, var1);
         var4 = var3.getGridSquare((double)((int)var1.getX() + 1), (double)((int)var1.getY()), (double)var1.getZ());
         if (var2 == null) {
            var2 = this.checkForTable(var4, var1);
         }

         if (var2 == null) {
            this.westTable = false;
         }

         var4 = var3.getGridSquare((double)((int)var1.getX()), (double)((int)var1.getY() - 1), (double)var1.getZ());
         if (var2 == null) {
            var2 = this.checkForTable(var4, var1);
         }

         var4 = var3.getGridSquare((double)((int)var1.getX()), (double)((int)var1.getY() + 1), (double)var1.getZ());
         if (var2 == null) {
            var2 = this.checkForTable(var4, var1);
         }

         if (var2 != null) {
            var3.getGridSquare((double)((int)var2.getX()), (double)((int)var2.getY()), (double)var2.getZ());
            if (this.ignoreAgainstWall && (var4.getWall(true) != null || var4.getWall(false) != null)) {
               return null;
            }
         }

         return var2;
      }
   }

   private IsoObject checkForTable(IsoGridSquare var1, IsoObject var2) {
      for(int var3 = 0; var3 < var1.getObjects().size(); ++var3) {
         IsoObject var4 = (IsoObject)var1.getObjects().get(var3);
         if (var4.getProperties().isTable() && var4.getProperties().getSurface() == 34 && var4.getContainer() == null && var4 != var2) {
            return var4;
         }
      }

      return null;
   }
}
