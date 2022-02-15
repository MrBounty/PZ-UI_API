package zombie.vehicles;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.joml.Vector3f;

public final class VehicleLight {
   public boolean active;
   public final Vector3f offset = new Vector3f();
   public float dist = 16.0F;
   public float intensity = 1.0F;
   public float dot = 0.96F;
   public int focusing = 0;

   public boolean getActive() {
      return this.active;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }

   /** @deprecated */
   @Deprecated
   public int getFocusing() {
      return this.focusing;
   }

   public float getIntensity() {
      return this.intensity;
   }

   /** @deprecated */
   @Deprecated
   public float getDistanization() {
      return this.dist;
   }

   /** @deprecated */
   @Deprecated
   public boolean canFocusingUp() {
      return this.focusing != 0;
   }

   /** @deprecated */
   @Deprecated
   public boolean canFocusingDown() {
      return this.focusing != 1;
   }

   /** @deprecated */
   @Deprecated
   public void setFocusingUp() {
      if (this.focusing != 0) {
         if (this.focusing < 4) {
            this.focusing = 4;
         } else if (this.focusing < 10) {
            this.focusing = 10;
         } else if (this.focusing < 30) {
            this.focusing = 30;
         } else if (this.focusing < 100) {
            this.focusing = 100;
         } else {
            this.focusing = 0;
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void setFocusingDown() {
      if (this.focusing != 1) {
         if (this.focusing == 0) {
            this.focusing = 100;
         } else if (this.focusing > 30) {
            this.focusing = 30;
         } else if (this.focusing > 10) {
            this.focusing = 10;
         } else if (this.focusing > 4) {
            this.focusing = 4;
         } else {
            this.focusing = 1;
         }

      }
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.put((byte)(this.active ? 1 : 0));
      var1.putFloat(this.offset.x);
      var1.putFloat(this.offset.y);
      var1.putFloat(this.intensity);
      var1.putFloat(this.dist);
      var1.putInt(this.focusing);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.active = var1.get() == 1;
      if (var2 >= 135) {
         this.offset.x = var1.getFloat();
         this.offset.y = var1.getFloat();
         this.intensity = var1.getFloat();
         this.dist = var1.getFloat();
         this.focusing = var1.getInt();
      }

   }
}
