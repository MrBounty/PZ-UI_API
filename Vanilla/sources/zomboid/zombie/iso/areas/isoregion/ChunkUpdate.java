package zombie.iso.areas.isoregion;

import java.nio.ByteBuffer;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;

public class ChunkUpdate {
   public static void writeIsoChunkIntoBuffer(IsoChunk var0, ByteBuffer var1) {
      if (var0 != null) {
         int var4 = var1.position();
         var1.putInt(0);
         var1.putInt(var0.maxLevel);
         int var5 = (var0.maxLevel + 1) * 100;
         var1.putInt(var5);

         int var6;
         for(var6 = 0; var6 <= var0.maxLevel; ++var6) {
            for(int var7 = 0; var7 < var0.squares[0].length; ++var7) {
               IsoGridSquare var2 = var0.squares[var6][var7];
               byte var3 = IsoRegions.calculateSquareFlags(var2);
               var1.put(var3);
            }
         }

         var6 = var1.position();
         var1.position(var4);
         var1.putInt(var6 - var4);
         var1.position(var6);
      } else {
         var1.putInt(-1);
      }

   }
}
