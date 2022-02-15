package zombie.worldMap;

import gnu.trove.list.array.TIntArrayList;
import zombie.core.math.PZMath;

public final class WorldMapPoints extends TIntArrayList {
   int m_minX;
   int m_minY;
   int m_maxX;
   int m_maxY;

   public int numPoints() {
      return this.size() / 2;
   }

   public int getX(int var1) {
      return this.get(var1 * 2);
   }

   public int getY(int var1) {
      return this.get(var1 * 2 + 1);
   }

   public void calculateBounds() {
      this.m_minX = this.m_minY = Integer.MAX_VALUE;
      this.m_maxX = this.m_maxY = Integer.MIN_VALUE;

      for(int var1 = 0; var1 < this.numPoints(); ++var1) {
         int var2 = this.getX(var1);
         int var3 = this.getY(var1);
         this.m_minX = PZMath.min(this.m_minX, var2);
         this.m_minY = PZMath.min(this.m_minY, var3);
         this.m_maxX = PZMath.max(this.m_maxX, var2);
         this.m_maxY = PZMath.max(this.m_maxY, var3);
      }

   }

   public boolean isClockwise() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.numPoints(); ++var2) {
         int var3 = this.getX(var2);
         int var4 = this.getY(var2);
         int var5 = this.getX((var2 + 1) % this.numPoints());
         int var6 = this.getY((var2 + 1) % this.numPoints());
         var1 += (float)((var5 - var3) * (var6 + var4));
      }

      return (double)var1 > 0.0D;
   }
}
