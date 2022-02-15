package zombie.randomizedWorld.randomizedBuilding;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;

public final class RBSchool extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      IsoCell var2 = IsoWorld.instance.CurrentCell;

      for(int var3 = var1.x - 1; var3 < var1.x2 + 1; ++var3) {
         for(int var4 = var1.y - 1; var4 < var1.y2 + 1; ++var4) {
            for(int var5 = 0; var5 < 8; ++var5) {
               IsoGridSquare var6 = var2.getGridSquare(var3, var4, var5);
               if (var6 != null && this.roomValid(var6)) {
                  int var7;
                  for(var7 = 0; var7 < var6.getObjects().size(); ++var7) {
                     IsoObject var8 = (IsoObject)var6.getObjects().get(var7);
                     if (Rand.NextBool(3) && this.isTableFor3DItems(var8, var6)) {
                        int var9 = Rand.Next(0, 8);
                        switch(var9) {
                        case 0:
                           var6.AddWorldInventoryItem("Pen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 1:
                           var6.AddWorldInventoryItem("Pencil", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 2:
                           var6.AddWorldInventoryItem("Crayons", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 3:
                           var6.AddWorldInventoryItem("RedPen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 4:
                           var6.AddWorldInventoryItem("BluePen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 5:
                           var6.AddWorldInventoryItem("Eraser", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                        }

                        int var10 = Rand.Next(0, 6);
                        switch(var10) {
                        case 0:
                           var6.AddWorldInventoryItem("Doodle", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 1:
                           var6.AddWorldInventoryItem("Book", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 2:
                           var6.AddWorldInventoryItem("Notebook", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                           break;
                        case 3:
                           var6.AddWorldInventoryItem("SheetPaper2", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), var8.getSurfaceOffsetNoTable() / 96.0F);
                        }
                     }
                  }

                  if (var6.getRoom() != null && "classroom".equals(var6.getRoom().getName())) {
                     if (Rand.NextBool(50)) {
                        var7 = Rand.Next(0, 10);
                        switch(var7) {
                        case 0:
                           var6.AddWorldInventoryItem("Doodle", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 1:
                           var6.AddWorldInventoryItem("Book", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 2:
                           var6.AddWorldInventoryItem("Notebook", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 3:
                           var6.AddWorldInventoryItem("SheetPaper2", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 4:
                           var6.AddWorldInventoryItem("Pen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 5:
                           var6.AddWorldInventoryItem("Pencil", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 6:
                           var6.AddWorldInventoryItem("Crayons", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 7:
                           var6.AddWorldInventoryItem("RedPen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 8:
                           var6.AddWorldInventoryItem("BluePen", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                           break;
                        case 9:
                           var6.AddWorldInventoryItem("Eraser", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                        }
                     }

                     if (Rand.NextBool(120)) {
                        var6.AddWorldInventoryItem("Bag_Schoolbag", Rand.Next(0.4F, 0.8F), Rand.Next(0.4F, 0.8F), 0.0F);
                     }
                  }
               }
            }
         }
      }

   }

   public boolean roomValid(IsoGridSquare var1) {
      return var1.getRoom() != null && "classroom".equals(var1.getRoom().getName());
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return var1.getRoom("classroom") != null || var2;
   }

   public RBSchool() {
      this.name = "School";
      this.setAlwaysDo(true);
   }
}
