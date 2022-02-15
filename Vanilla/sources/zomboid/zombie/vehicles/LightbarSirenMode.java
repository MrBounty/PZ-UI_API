package zombie.vehicles;

import zombie.scripting.objects.VehicleScript;

public final class LightbarSirenMode {
   private int mode = 0;
   private final int modeMax = 3;

   public int get() {
      return this.mode;
   }

   public void set(int var1) {
      if (var1 > 3) {
         this.mode = 3;
      } else if (var1 < 0) {
         this.mode = 0;
      } else {
         this.mode = var1;
      }
   }

   public boolean isEnable() {
      return this.mode != 0;
   }

   public String getSoundName(VehicleScript.LightBar var1) {
      if (this.isEnable()) {
         if (this.mode == 1) {
            return var1.soundSiren0;
         }

         if (this.mode == 2) {
            return var1.soundSiren1;
         }

         if (this.mode == 3) {
            return var1.soundSiren2;
         }
      }

      return "";
   }
}
