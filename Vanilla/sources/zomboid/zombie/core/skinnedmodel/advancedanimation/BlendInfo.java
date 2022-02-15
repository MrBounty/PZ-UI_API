package zombie.core.skinnedmodel.advancedanimation;

public class BlendInfo {
   public String name;
   public BlendType Type;
   public String mulDec;
   public String mulInc;
   public float dec;
   public float inc;

   public static class BlendInstance {
      public float current = -1.0F;
      public float target;
      BlendInfo info;

      public String GetDebug() {
         String var1 = "Blend: " + this.info.name;
         switch(this.info.Type) {
         case Linear:
            var1 = var1 + ", Linear ";
            break;
         case InverseExponential:
            var1 = var1 + ", InverseExponential ";
         }

         var1 = var1 + ", Current " + this.current;
         return var1;
      }

      public BlendInstance(BlendInfo var1) {
         this.info = var1;
      }

      public void set(float var1) {
         this.target = var1;
         if (this.current == -1.0F) {
            this.current = this.target;
         }

      }

      public void update() {
         float var1 = 0.0F;
         float var2;
         if (this.current < this.target) {
            var2 = 1.0F;
            switch(this.info.Type) {
            case InverseExponential:
               var2 = this.current / 1.0F;
               var2 = 1.0F - var2;
               if (var2 < 0.1F) {
                  var2 = 0.1F;
               }
            case Linear:
            default:
               var1 = this.info.inc * var2;
               this.current += var1;
               if (this.current > this.target) {
                  this.current = this.target;
               }
            }
         } else if (this.current > this.target) {
            var2 = 1.0F;
            switch(this.info.Type) {
            case InverseExponential:
               var2 = this.current / 1.0F;
               var2 = 1.0F - var2;
               if (var2 < 0.1F) {
                  var2 = 0.1F;
               }
            case Linear:
            default:
               var1 = -this.info.dec * var2;
               this.current += var1;
               if (this.current < this.target) {
                  this.current = this.target;
               }
            }
         }

      }
   }
}
