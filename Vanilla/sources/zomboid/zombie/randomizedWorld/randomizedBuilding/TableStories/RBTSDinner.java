package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSDinner extends RBTableStoryBase {
   private boolean hasPlate = true;
   private String plate = null;
   private String foodType = "Base.Chicken";

   public RBTSDinner() {
      this.need2Tables = true;
      this.chance = 10;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      if (this.table2 != null) {
         int var2;
         if (this.westTable) {
            this.generateFood();
            if (this.hasPlate) {
               this.addWorldItem(this.plate, this.table1.getSquare(), 0.6875F, 0.8437F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(80, 105));
            }

            this.addWorldItem(this.foodType, this.table1.getSquare(), 0.6875F, 0.8437F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(80, 105));
            this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table1.getSquare(), 0.406F, 0.656F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table1.getSquare(), 0.3359F, 0.9765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
            this.addWorldItem("Base.Fork", this.table1.getSquare(), 0.851F, 0.96265F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
            this.generateFood();
            if (this.hasPlate) {
               this.addWorldItem(this.plate, this.table1.getSquare(), 0.5519F, 0.4531F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(240, 285));
            }

            this.addWorldItem(this.foodType, this.table1.getSquare(), 0.5519F, 0.4531F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(240, 285));
            this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table1.getSquare(), 0.523F, 0.57F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table1.getSquare(), 0.305F, 0.2656F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
            this.addWorldItem("Base.Fork", this.table1.getSquare(), 0.704F, 0.265F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
            if (Rand.Next(100) < 70) {
               this.generateFood();
               if (this.hasPlate) {
                  this.addWorldItem(this.plate, this.table2.getSquare(), 0.6484F, 0.8353F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(80, 105));
               }

               this.addWorldItem(this.foodType, this.table2.getSquare(), 0.6484F, 0.8353F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(80, 105));
               this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table2.getSquare(), 0.429F, 0.632F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               this.addWorldItem("Base.Fork", this.table2.getSquare(), 0.234F, 0.92F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
               this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table2.getSquare(), 0.851F, 0.96265F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
            }

            if (Rand.Next(100) < 50) {
               this.generateFood();
               if (this.hasPlate) {
                  this.addWorldItem(this.plate, this.table2.getSquare(), 0.5859F, 0.3941F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(240, 285));
               }

               this.addWorldItem(this.foodType, this.table2.getSquare(), 0.5859F, 0.3941F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(240, 285));
               this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table2.getSquare(), 0.71F, 0.539F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               this.addWorldItem("Base.Fork", this.table2.getSquare(), 0.195F, 0.21F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
               this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table2.getSquare(), 0.851F, 0.3F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
            }

            if (Rand.NextBool(2)) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.Salt" : "Base.Pepper", this.table1.getSquare(), 0.984F, 0.734F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            var2 = Rand.Next(0, 5);
            switch(var2) {
            case 0:
               this.addWorldItem("Base.Mustard", this.table2.getSquare(), 0.46F, 0.664F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 1:
               this.addWorldItem("Base.Ketchup", this.table2.getSquare(), 0.46F, 0.664F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 2:
               this.addWorldItem("Base.Marinara", this.table2.getSquare(), 0.46F, 0.664F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            var2 = Rand.Next(0, 4);
            switch(var2) {
            case 0:
               this.addWorldItem("Base.Wine", this.table2.getSquare(), 0.228F, 0.593F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 1:
               this.addWorldItem("Base.Wine2", this.table2.getSquare(), 0.228F, 0.593F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 2:
               this.addWorldItem("Base.WaterBottleFull", this.table2.getSquare(), 0.228F, 0.593F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 3:
               this.addWorldItem("Base.BeerBottle", this.table2.getSquare(), 0.228F, 0.593F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            if (Rand.NextBool(3)) {
               var2 = Rand.Next(0, 5);
               switch(var2) {
               case 0:
                  this.addWorldItem("Base.WineEmpty", this.table1.getSquare(), 0.96F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 1:
                  this.addWorldItem("Base.WineEmpty2", this.table1.getSquare(), 0.96F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 2:
                  this.addWorldItem("Base.WaterBottleEmpty", this.table1.getSquare(), 0.96F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 3:
                  this.addWorldItem("Base.BeerEmpty", this.table1.getSquare(), 0.96F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 4:
                  this.addWorldItem("Base.BeerCan", this.table1.getSquare(), 0.96F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               }
            }
         } else {
            this.generateFood();
            if (this.hasPlate) {
               this.addWorldItem(this.plate, this.table1.getSquare(), 0.343F, 0.562F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            }

            this.addWorldItem(this.foodType, this.table1.getSquare(), 0.343F, 0.562F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(0, 15));
            this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table1.getSquare(), 0.469F, 0.469F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table1.getSquare(), 0.281F, 0.843F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.addWorldItem("Base.Fork", this.table1.getSquare(), 0.234F, 0.298F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.generateFood();
            if (this.hasPlate) {
               this.addWorldItem(this.plate, this.table1.getSquare(), 0.867F, 0.695F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            }

            this.addWorldItem(this.foodType, this.table1.getSquare(), 0.867F, 0.695F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(165, 185));
            this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table1.getSquare(), 0.648F, 0.383F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table1.getSquare(), 0.945F, 0.304F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            this.addWorldItem("Base.Fork", this.table1.getSquare(), 0.945F, 0.96F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            if (Rand.Next(100) < 70) {
               this.generateFood();
               if (this.hasPlate) {
                  this.addWorldItem(this.plate, this.table2.getSquare(), 0.35F, 0.617F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               }

               this.addWorldItem(this.foodType, this.table2.getSquare(), 0.35F, 0.617F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(0, 15));
               this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table2.getSquare(), 0.46F, 0.476F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table2.getSquare(), 0.265F, 0.828F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               this.addWorldItem("Base.Fork", this.table2.getSquare(), 0.25F, 0.34F, this.table2.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            }

            if (Rand.Next(100) < 50) {
               this.generateFood();
               if (this.hasPlate) {
                  this.addWorldItem(this.plate, this.table2.getSquare(), 0.89F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
               }

               this.addWorldItem(this.foodType, this.table2.getSquare(), 0.89F, 0.6F, this.table1.getSurfaceOffsetNoTable() / 96.0F + 0.01F, Rand.Next(165, 185));
               this.addWorldItem(Rand.NextBool(2) ? "Base.GlassWine" : "Base.GlassTumbler", this.table2.getSquare(), 0.648F, 0.638F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               this.addWorldItem(Rand.NextBool(3) ? "Base.KitchenKnife" : "Base.ButterKnife", this.table2.getSquare(), 0.937F, 0.281F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
               this.addWorldItem("Base.Fork", this.table2.getSquare(), 0.945F, 0.835F, this.table2.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            }

            if (Rand.NextBool(2)) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.Salt" : "Base.Pepper", this.table1.getSquare(), 0.656F, 0.718F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            var2 = Rand.Next(0, 5);
            switch(var2) {
            case 0:
               this.addWorldItem("Base.Mustard", this.table2.getSquare(), 0.61F, 0.328F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 1:
               this.addWorldItem("Base.Ketchup", this.table2.getSquare(), 0.61F, 0.328F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 2:
               this.addWorldItem("Base.Marinara", this.table2.getSquare(), 0.61F, 0.328F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            var2 = Rand.Next(0, 4);
            switch(var2) {
            case 0:
               this.addWorldItem("Base.Wine", this.table2.getSquare(), 0.468F, 0.09F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 1:
               this.addWorldItem("Base.Wine2", this.table2.getSquare(), 0.468F, 0.09F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 2:
               this.addWorldItem("Base.WaterBottleFull", this.table2.getSquare(), 0.468F, 0.09F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               break;
            case 3:
               this.addWorldItem("Base.BeerBottle", this.table2.getSquare(), 0.468F, 0.09F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            }

            if (Rand.NextBool(3)) {
               var2 = Rand.Next(0, 5);
               switch(var2) {
               case 0:
                  this.addWorldItem("Base.WineEmpty", this.table2.getSquare(), 0.851F, 0.15F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 1:
                  this.addWorldItem("Base.WineEmpty2", this.table2.getSquare(), 0.851F, 0.15F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 2:
                  this.addWorldItem("Base.WaterBottleEmpty", this.table2.getSquare(), 0.851F, 0.15F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 3:
                  this.addWorldItem("Base.BeerEmpty", this.table2.getSquare(), 0.851F, 0.15F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 4:
                  this.addWorldItem("Base.BeerCan", this.table2.getSquare(), 0.851F, 0.15F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               }
            }
         }
      }

   }

   public void generateFood() {
      this.foodType = "Base.Chicken";
      if (Rand.NextBool(4)) {
         this.foodType = "Base.TVDinner";
         this.hasPlate = false;
      } else {
         this.hasPlate = true;
         int var1;
         if (this.plate == null) {
            var1 = Rand.Next(0, 4);
            switch(var1) {
            case 0:
               this.plate = "Base.Plate";
               break;
            case 1:
               this.plate = "Base.PlateBlue";
               break;
            case 2:
               this.plate = "Base.PlateOrange";
               break;
            case 3:
               this.plate = "Base.PlateFancy";
            }
         }

         var1 = Rand.Next(0, 4);
         switch(var1) {
         case 0:
            this.foodType = "Base.Chicken";
            break;
         case 1:
            this.foodType = "Base.PorkChop";
            break;
         case 2:
            this.foodType = "Base.Steak";
            break;
         case 3:
            this.foodType = "Base.Salmon";
         }
      }

   }
}
