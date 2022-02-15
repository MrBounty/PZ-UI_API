package zombie.scripting.objects;

import java.util.Arrays;
import zombie.characters.skills.PerkFactory;
import zombie.debug.DebugLog;

public class MovableRecipe extends Recipe {
   private boolean isValid = false;
   private String worldSprite = "";
   private PerkFactory.Perk xpPerk;
   private Recipe.Source primaryTools;
   private Recipe.Source secondaryTools;

   public MovableRecipe() {
      this.xpPerk = PerkFactory.Perks.MAX;
      this.AnimNode = "Disassemble";
      this.removeResultItem = true;
      this.AllowDestroyedItem = false;
      this.name = "Disassemble Movable";
      this.setCanBeDoneFromFloor(false);
   }

   public void setResult(String var1, int var2) {
      Recipe.Result var3 = new Recipe.Result();
      var3.count = var2;
      if (var1.contains(".")) {
         var3.type = var1.split("\\.")[1];
         var3.module = var1.split("\\.")[0];
      } else {
         DebugLog.log("MovableRecipe invalid result item. item = " + var1);
      }

      this.Result = var3;
   }

   public void setSource(String var1) {
      Recipe.Source var2 = new Recipe.Source();
      var2.getItems().add(var1);
      this.Source.add(var2);
   }

   public void setTool(String var1, boolean var2) {
      Recipe.Source var3 = new Recipe.Source();
      var3.keep = true;
      if (var1.contains("/")) {
         var1 = var1.replaceFirst("keep ", "").trim();
         var3.getItems().addAll(Arrays.asList(var1.split("/")));
      } else {
         var3.getItems().add(var1);
      }

      if (var2) {
         this.primaryTools = var3;
      } else {
         this.secondaryTools = var3;
      }

      this.Source.add(var3);
   }

   public Recipe.Source getPrimaryTools() {
      return this.primaryTools;
   }

   public Recipe.Source getSecondaryTools() {
      return this.secondaryTools;
   }

   public void setRequiredSkill(PerkFactory.Perk var1, int var2) {
      Recipe.RequiredSkill var3 = new Recipe.RequiredSkill(var1, var2);
      this.skillRequired.add(var3);
   }

   public void setXpPerk(PerkFactory.Perk var1) {
      this.xpPerk = var1;
   }

   public PerkFactory.Perk getXpPerk() {
      return this.xpPerk;
   }

   public boolean hasXpPerk() {
      return this.xpPerk != PerkFactory.Perks.MAX;
   }

   public void setOnCreate(String var1) {
      this.LuaCreate = var1;
   }

   public void setOnXP(String var1) {
      this.LuaGiveXP = var1;
   }

   public void setTime(float var1) {
      this.TimeToMake = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getWorldSprite() {
      return this.worldSprite;
   }

   public void setWorldSprite(String var1) {
      this.worldSprite = var1;
   }

   public boolean isValid() {
      return this.isValid;
   }

   public void setValid(boolean var1) {
      this.isValid = var1;
   }
}
