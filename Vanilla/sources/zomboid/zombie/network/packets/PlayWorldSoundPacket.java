package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.SoundManager;
import zombie.core.network.ByteBufferWriter;

public class PlayWorldSoundPacket implements INetworkPacket {
   String name;
   int x;
   int y;
   byte z;

   public void set(String var1, int var2, int var3, byte var4) {
      this.name = var1;
      this.x = var2;
      this.y = var3;
      this.z = var4;
   }

   public void process() {
      SoundManager.instance.PlayWorldSoundImpl(this.name, false, this.x, this.y, this.z, 1.0F, 20.0F, 2.0F, false);
   }

   public String getName() {
      return this.name;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void parse(ByteBuffer var1) {
      this.x = var1.getInt();
      this.y = var1.getInt();
      this.z = var1.get();
      this.name = GameWindow.ReadString(var1);
   }

   public void write(ByteBufferWriter var1) {
      var1.putInt(this.x);
      var1.putInt(this.y);
      var1.putByte(this.z);
      var1.putUTF(this.name);
   }

   public boolean isConsistent() {
      return this.name != null && !this.name.isEmpty();
   }

   public int getPacketSizeBytes() {
      return 12 + this.name.length();
   }

   public String getDescription() {
      return "\n\tPlayWorldSoundPacket [name=" + this.name + " | x=" + this.x + " | y=" + this.y + " | z=" + this.z + " ]";
   }
}
