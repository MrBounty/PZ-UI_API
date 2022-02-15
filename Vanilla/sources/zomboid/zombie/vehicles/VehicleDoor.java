package zombie.vehicles;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.scripting.objects.VehicleScript;

public final class VehicleDoor {
   protected VehiclePart part;
   protected boolean open;
   protected boolean locked;
   protected boolean lockBroken;

   public VehicleDoor(VehiclePart var1) {
      this.part = var1;
   }

   public void init(VehicleScript.Door var1) {
      this.open = false;
      this.locked = false;
      this.lockBroken = false;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean var1) {
      this.open = var1;
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(boolean var1) {
      this.locked = var1;
   }

   public boolean isLockBroken() {
      return this.lockBroken;
   }

   public void setLockBroken(boolean var1) {
      this.lockBroken = var1;
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.put((byte)(this.open ? 1 : 0));
      var1.put((byte)(this.locked ? 1 : 0));
      var1.put((byte)(this.lockBroken ? 1 : 0));
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.open = var1.get() == 1;
      this.locked = var1.get() == 1;
      this.lockBroken = var1.get() == 1;
   }
}
