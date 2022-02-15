package zombie.iso;

import java.nio.ByteBuffer;

public final class SliceY {
   public static final ByteBuffer SliceBuffer = ByteBuffer.allocate(10485760);
   public static final Object SliceBufferLock = "SliceY SliceBufferLock";
}
