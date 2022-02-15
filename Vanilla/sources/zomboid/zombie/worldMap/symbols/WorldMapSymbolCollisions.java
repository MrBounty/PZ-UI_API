package zombie.worldMap.symbols;

import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TFloatArrayList;

public final class WorldMapSymbolCollisions {
   final TFloatArrayList m_boxes = new TFloatArrayList();
   final TByteArrayList m_collide = new TByteArrayList();

   boolean addBox(float var1, float var2, float var3, float var4, boolean var5) {
      int var6 = this.m_boxes.size() / 4 - 1;
      int var7 = var6 + 1;
      this.m_boxes.add(var1);
      this.m_boxes.add(var2);
      this.m_boxes.add(var1 + var3);
      this.m_boxes.add(var2 + var4);
      this.m_collide.add((byte)(var5 ? 1 : 0));
      if (!var5) {
         return false;
      } else {
         for(int var8 = 0; var8 <= var6; ++var8) {
            if (this.isCollision(var8, var7)) {
               var1 += var3 / 2.0F;
               var2 += var4 / 2.0F;
               this.m_boxes.set(var7 * 4, var1 - 3.0F - 1.0F);
               this.m_boxes.set(var7 * 4 + 1, var2 - 3.0F - 1.0F);
               this.m_boxes.set(var7 * 4 + 2, var1 + 3.0F + 1.0F);
               this.m_boxes.set(var7 * 4 + 3, var2 - 3.0F + 1.0F);
               return true;
            }
         }

         return false;
      }
   }

   boolean isCollision(int var1, int var2) {
      if (this.m_collide.getQuick(var1) != 0 && this.m_collide.getQuick(var2) != 0) {
         var1 *= 4;
         var2 *= 4;
         float var3 = this.m_boxes.get(var1);
         float var4 = this.m_boxes.get(var1 + 1);
         float var5 = this.m_boxes.get(var1 + 2);
         float var6 = this.m_boxes.get(var1 + 3);
         float var7 = this.m_boxes.get(var2);
         float var8 = this.m_boxes.get(var2 + 1);
         float var9 = this.m_boxes.get(var2 + 2);
         float var10 = this.m_boxes.get(var2 + 3);
         return var3 < var9 && var5 > var7 && var4 < var10 && var6 > var8;
      } else {
         return false;
      }
   }

   boolean isCollision(int var1) {
      for(int var2 = 0; var2 < this.m_boxes.size() / 4; ++var2) {
         if (var2 != var1 && this.isCollision(var1, var2)) {
            return true;
         }
      }

      return false;
   }
}
