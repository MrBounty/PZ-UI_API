package zombie.network;

import java.nio.ByteBuffer;
import zombie.iso.IsoCell;
import zombie.iso.IsoObject;

public class WorldItemTypes {
   public static IsoObject createFromBuffer(ByteBuffer var0) {
      IsoObject var1 = null;
      var1 = IsoObject.factoryFromFileInput((IsoCell)null, (ByteBuffer)var0);
      return var1;
   }
}
