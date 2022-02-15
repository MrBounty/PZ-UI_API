package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.BaseCharacterSoundEmitter;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.network.packets.hit.MovingObject;

public class PlaySoundPacket implements INetworkPacket {
   String name;
   MovingObject object = new MovingObject();
   boolean loop;

   public void set(String var1, boolean var2, IsoMovingObject var3) {
      this.name = var1;
      this.loop = var2;
      this.object.setMovingObject(var3);
   }

   public void process() {
      IsoMovingObject var1 = this.object.getMovingObject();
      if (var1 instanceof IsoGameCharacter) {
         BaseCharacterSoundEmitter var2 = ((IsoGameCharacter)var1).getEmitter();
         if (!this.loop) {
            var2.playSoundImpl(this.name, (IsoObject)null);
         }
      } else {
         BaseSoundEmitter var3 = var1.emitter;
         if (var3 == null) {
            var3 = IsoWorld.instance.getFreeEmitter(var1.x, var1.y, var1.z);
            IsoWorld.instance.takeOwnershipOfEmitter(var3);
            var1.emitter = var3;
         }

         if (!this.loop) {
            var3.playSoundImpl(this.name, (IsoObject)null);
         } else {
            var3.playSoundLoopedImpl(this.name);
         }

         var3.tick();
      }

   }

   public String getName() {
      return this.name;
   }

   public IsoMovingObject getMovingObject() {
      return this.object.getMovingObject();
   }

   public void parse(ByteBuffer var1) {
      this.object.parse(var1);
      this.name = GameWindow.ReadString(var1);
      this.loop = var1.get() == 1;
   }

   public void write(ByteBufferWriter var1) {
      this.object.write(var1);
      var1.putUTF(this.name);
      var1.putByte((byte)(this.loop ? 1 : 0));
   }

   public boolean isConsistent() {
      return this.name != null && !this.name.isEmpty();
   }

   public int getPacketSizeBytes() {
      return 12 + this.name.length();
   }

   public String getDescription() {
      String var10000 = this.name;
      return "\n\tPlaySoundPacket [name=" + var10000 + " | object=" + this.object.getDescription() + " | loop=" + this.loop + " ]";
   }
}
