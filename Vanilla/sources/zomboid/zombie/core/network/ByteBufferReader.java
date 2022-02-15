package zombie.core.network;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public final class ByteBufferReader {
   public ByteBuffer bb;

   public ByteBufferReader(ByteBuffer var1) {
      this.bb = var1;
   }

   public boolean getBoolean() {
      return this.bb.get() != 0;
   }

   public byte getByte() {
      return this.bb.get();
   }

   public char getChar() {
      return this.bb.getChar();
   }

   public double getDouble() {
      return this.bb.getDouble();
   }

   public float getFloat() {
      return this.bb.getFloat();
   }

   public int getInt() {
      return this.bb.getInt();
   }

   public long getLong() {
      return this.bb.getLong();
   }

   public short getShort() {
      return this.bb.getShort();
   }

   public String getUTF() {
      short var1 = this.bb.getShort();
      byte[] var2 = new byte[var1];
      this.bb.get(var2);

      try {
         return new String(var2, "UTF-8");
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException("Bad encoding!");
      }
   }
}
