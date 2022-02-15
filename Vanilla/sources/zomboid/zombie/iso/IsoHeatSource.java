package zombie.iso;

import zombie.characters.IsoPlayer;

public class IsoHeatSource {
   private int x;
   private int y;
   private int z;
   private int radius;
   private int temperature;

   public IsoHeatSource(int var1, int var2, int var3, int var4, int var5) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.radius = var4;
      this.temperature = var5;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int var1) {
      this.radius = var1;
   }

   public int getTemperature() {
      return this.temperature;
   }

   public void setTemperature(int var1) {
      this.temperature = var1;
   }

   public boolean isInBounds(int var1, int var2, int var3, int var4) {
      return this.x >= var1 && this.x < var3 && this.y >= var2 && this.y < var4;
   }

   public boolean isInBounds() {
      IsoChunkMap[] var1 = IsoWorld.instance.CurrentCell.ChunkMap;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (!var1[var2].ignore) {
            int var3 = var1[var2].getWorldXMinTiles();
            int var4 = var1[var2].getWorldXMaxTiles();
            int var5 = var1[var2].getWorldYMinTiles();
            int var6 = var1[var2].getWorldYMaxTiles();
            if (this.isInBounds(var3, var5, var4, var6)) {
               return true;
            }
         }
      }

      return false;
   }
}
