package zombie.core.skinnedmodel.visual;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.core.skinnedmodel.population.ClothingItem;

public final class ItemVisuals extends ArrayList {
   public void save(ByteBuffer var1) throws IOException {
      var1.putShort((short)this.size());

      for(int var2 = 0; var2 < this.size(); ++var2) {
         ((ItemVisual)this.get(var2)).save(var1);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.clear();
      short var3 = var1.getShort();

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemVisual var5 = new ItemVisual();
         var5.load(var1, var2);
         this.add(var5);
      }

   }

   public ItemVisual findHat() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         ItemVisual var2 = (ItemVisual)this.get(var1);
         ClothingItem var3 = var2.getClothingItem();
         if (var3 != null && var3.isHat()) {
            return var2;
         }
      }

      return null;
   }

   public ItemVisual findMask() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         ItemVisual var2 = (ItemVisual)this.get(var1);
         ClothingItem var3 = var2.getClothingItem();
         if (var3 != null && var3.isMask()) {
            return var2;
         }
      }

      return null;
   }
}
