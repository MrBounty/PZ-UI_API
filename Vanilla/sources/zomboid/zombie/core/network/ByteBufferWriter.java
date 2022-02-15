package zombie.core.network;

import java.nio.ByteBuffer;
import zombie.GameWindow;

public final class ByteBufferWriter {
   public ByteBuffer bb;

   public ByteBufferWriter(ByteBuffer var1) {
      this.bb = var1;
   }

   public void putBoolean(boolean var1) {
      this.bb.put((byte)(var1 ? 1 : 0));
   }

   public void putByte(byte var1) {
      this.bb.put(var1);
   }

   public void putChar(char var1) {
      this.bb.putChar(var1);
   }

   public void putDouble(double var1) {
      this.bb.putDouble(var1);
   }

   public void putFloat(float var1) {
      this.bb.putFloat(var1);
   }

   public void putInt(int var1) {
      this.bb.putInt(var1);
   }

   public void putLong(long var1) {
      this.bb.putLong(var1);
   }

   public void putShort(short var1) {
      this.bb.putShort(var1);
   }

   public void putUTF(String var1) {
      GameWindow.WriteStringUTF(this.bb, var1);
   }
}
