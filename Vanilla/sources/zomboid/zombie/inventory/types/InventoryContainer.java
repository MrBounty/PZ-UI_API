package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.IsoGameCharacter;
import zombie.core.Translator;
import zombie.core.math.PZMath;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;

public final class InventoryContainer extends InventoryItem {
   ItemContainer container = new ItemContainer();
   int capacity = 0;
   int weightReduction = 0;
   private String CanBeEquipped = "";

   public InventoryContainer(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.container.containingItem = this;
      this.container.type = var3;
      this.container.inventoryContainer = this;
   }

   public boolean IsInventoryContainer() {
      return true;
   }

   public int getSaveType() {
      return Item.Type.Container.ordinal();
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Container";
   }

   public ItemContainer getInventory() {
      return this.container;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putInt(this.container.ID);
      var1.putInt(this.weightReduction);
      this.container.save(var1);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      int var3 = var1.getInt();
      this.setWeightReduction(var1.getInt());
      if (this.container == null) {
         this.container = new ItemContainer();
      }

      this.container.clear();
      this.container.containingItem = this;
      this.container.setWeightReduction(this.weightReduction);
      this.container.Capacity = this.capacity;
      this.container.ID = var3;
      this.container.load(var1, var2);
      this.synchWithVisual();
   }

   public int getCapacity() {
      return this.container.getCapacity();
   }

   public void setCapacity(int var1) {
      this.capacity = var1;
      if (this.container == null) {
         this.container = new ItemContainer();
      }

      this.container.Capacity = var1;
   }

   public float getInventoryWeight() {
      if (this.getInventory() == null) {
         return 0.0F;
      } else {
         float var1 = 0.0F;
         ArrayList var2 = this.getInventory().getItems();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            InventoryItem var4 = (InventoryItem)var2.get(var3);
            if (this.isEquipped()) {
               var1 += var4.getEquippedWeight();
            } else {
               var1 += var4.getUnequippedWeight();
            }
         }

         return var1;
      }
   }

   public int getEffectiveCapacity(IsoGameCharacter var1) {
      return this.container.getEffectiveCapacity(var1);
   }

   public int getWeightReduction() {
      return this.weightReduction;
   }

   public void setWeightReduction(int var1) {
      var1 = Math.min(var1, 100);
      var1 = Math.max(var1, 0);
      this.weightReduction = var1;
      this.container.setWeightReduction(var1);
   }

   public void updateAge() {
      ArrayList var1 = this.getInventory().getItems();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         ((InventoryItem)var1.get(var2)).updateAge();
      }

   }

   public void DoTooltip(ObjectTooltip var1) {
      var1.render();
      super.DoTooltip(var1);
      int var2 = var1.getHeight().intValue();
      var2 -= var1.padBottom;
      if (var1.getWidth() < 160.0D) {
         var1.setWidth(160.0D);
      }

      if (!this.getItemContainer().getItems().isEmpty()) {
         int var3 = 5;
         var2 += 4;
         HashSet var4 = new HashSet();

         for(int var5 = this.getItemContainer().getItems().size() - 1; var5 >= 0; --var5) {
            InventoryItem var6 = (InventoryItem)this.getItemContainer().getItems().get(var5);
            if (var6.getName() != null) {
               if (var4.contains(var6.getName())) {
                  continue;
               }

               var4.add(var6.getName());
            }

            var1.DrawTextureScaledAspect(var6.getTex(), (double)var3, (double)var2, 16.0D, 16.0D, 1.0D, 1.0D, 1.0D, 1.0D);
            var3 += 17;
            if ((float)(var3 + 16) > var1.width - (float)var1.padRight) {
               break;
            }
         }

         var2 += 16;
      }

      var2 += var1.padBottom;
      var1.setHeight((double)var2);
   }

   public void DoTooltip(ObjectTooltip var1, ObjectTooltip.Layout var2) {
      float var4 = 0.0F;
      float var5 = 0.6F;
      float var6 = 0.0F;
      float var7 = 0.7F;
      ObjectTooltip.LayoutItem var3;
      if (this.getEffectiveCapacity(var1.getCharacter()) != 0) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_container_Capacity") + ":", 1.0F, 1.0F, 1.0F, 1.0F);
         var3.setValueRightNoPlus(this.getEffectiveCapacity(var1.getCharacter()));
      }

      if (this.getWeightReduction() != 0) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_container_Weight_Reduction") + ":", 1.0F, 1.0F, 1.0F, 1.0F);
         var3.setValueRightNoPlus(this.getWeightReduction());
      }

      if (this.getBloodClothingType() != null) {
         float var8 = this.getBloodLevel();
         if (var8 != 0.0F) {
            var3 = var2.addItem();
            var3.setLabel(Translator.getText("Tooltip_clothing_bloody") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
            var3.setProgress(var8, var4, var5, var6, var7);
         }
      }

   }

   public void setBloodLevel(float var1) {
      ArrayList var2 = BloodClothingType.getCoveredParts(this.getBloodClothingType());

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         this.setBlood((BloodBodyPartType)var2.get(var3), PZMath.clamp(var1, 0.0F, 100.0F));
      }

   }

   public float getBloodLevel() {
      ArrayList var1 = BloodClothingType.getCoveredParts(this.getBloodClothingType());
      float var2 = 0.0F;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2 += this.getBlood((BloodBodyPartType)var1.get(var3));
      }

      return var2;
   }

   public void setCanBeEquipped(String var1) {
      this.CanBeEquipped = var1 == null ? "" : var1;
   }

   public String canBeEquipped() {
      return this.CanBeEquipped;
   }

   public ItemContainer getItemContainer() {
      return this.container;
   }

   public void setItemContainer(ItemContainer var1) {
      this.container = var1;
   }

   public float getContentsWeight() {
      return this.getInventory().getContentsWeight();
   }

   public float getEquippedWeight() {
      float var1 = 1.0F;
      if (this.getWeightReduction() > 0) {
         var1 = 1.0F - (float)this.getWeightReduction() / 100.0F;
      }

      return this.getActualWeight() * 0.3F + this.getContentsWeight() * var1;
   }

   public String getClothingExtraSubmenu() {
      return this.ScriptItem.clothingExtraSubmenu;
   }
}
