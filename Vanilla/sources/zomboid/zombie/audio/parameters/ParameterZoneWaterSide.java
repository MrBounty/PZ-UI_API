package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.math.PZMath;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoWorld;

public final class ParameterZoneWaterSide extends FMODGlobalParameter {
   private int m_playerX = -1;
   private int m_playerY = -1;
   private int m_distance = 40;

   public ParameterZoneWaterSide() {
      super("ZoneWaterSide");
   }

   public float calculateCurrentValue() {
      IsoGameCharacter var1 = this.getCharacter();
      if (var1 == null) {
         return 40.0F;
      } else {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         if (var2 != this.m_playerX || var3 != this.m_playerY) {
            this.m_playerX = var2;
            this.m_playerY = var3;
            this.m_distance = this.calculate(var1);
            if (this.m_distance < 40) {
               this.m_distance = PZMath.clamp(this.m_distance - 5, 0, 40);
            }
         }

         return (float)this.m_distance;
      }
   }

   private int calculate(IsoGameCharacter var1) {
      if (IsoWorld.instance != null && IsoWorld.instance.CurrentCell != null && IsoWorld.instance.CurrentCell.ChunkMap[0] != null) {
         IsoChunkMap var2 = IsoWorld.instance.CurrentCell.ChunkMap[0];
         float var3 = Float.MAX_VALUE;

         for(int var4 = 0; var4 < IsoChunkMap.ChunkGridWidth; ++var4) {
            for(int var5 = 0; var5 < IsoChunkMap.ChunkGridWidth; ++var5) {
               IsoChunk var6 = var2.getChunk(var5, var4);
               if (var6 != null && var6.getNumberOfWaterTiles() == 100) {
                  float var7 = (float)(var6.wx * 10) + 5.0F;
                  float var8 = (float)(var6.wy * 10) + 5.0F;
                  float var9 = var1.x - var7;
                  float var10 = var1.y - var8;
                  if (var9 * var9 + var10 * var10 < var3) {
                     var3 = var9 * var9 + var10 * var10;
                  }
               }
            }
         }

         return (int)PZMath.clamp(PZMath.sqrt(var3), 0.0F, 40.0F);
      } else {
         return 40;
      }
   }

   private IsoGameCharacter getCharacter() {
      IsoPlayer var1 = null;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         IsoPlayer var3 = IsoPlayer.players[var2];
         if (var3 != null && (var1 == null || var1.isDead() && var3.isAlive())) {
            var1 = var3;
         }
      }

      return var1;
   }
}
