package zombie.characters;

import zombie.iso.Vector2;

public class NetworkCharacter {
   float minMovement;
   float maxMovement;
   long deltaTime;
   public final NetworkCharacter.Transform transform = new NetworkCharacter.Transform();
   final Vector2 movement = new Vector2();
   final NetworkCharacter.Point d1 = new NetworkCharacter.Point();
   final NetworkCharacter.Point d2 = new NetworkCharacter.Point();

   public NetworkCharacter() {
      this.minMovement = 0.075F;
      this.maxMovement = 0.5F;
      this.deltaTime = 10L;
   }

   NetworkCharacter(float var1, float var2, long var3) {
      this.minMovement = var1;
      this.maxMovement = var2;
      this.deltaTime = var3;
   }

   public void updateTransform(float var1, float var2, float var3, float var4) {
      this.transform.position.x = var1;
      this.transform.position.y = var2;
      this.transform.rotation.x = var3;
      this.transform.rotation.y = var4;
   }

   public void updateInterpolationPoint(int var1, float var2, float var3, float var4, float var5) {
      if (this.d2.t == 0) {
         this.updateTransform(var2, var3, var4, var5);
      }

      this.d2.t = var1;
      this.d2.px = var2;
      this.d2.py = var3;
      this.d2.rx = var4;
      this.d2.ry = var5;
   }

   public void updatePointInternal(float var1, float var2, float var3, float var4) {
      this.d1.px = var1;
      this.d1.py = var2;
      this.d1.rx = var3;
      this.d1.ry = var4;
   }

   public void updateExtrapolationPoint(int var1, float var2, float var3, float var4, float var5) {
      if (var1 > this.d1.t) {
         this.d2.t = this.d1.t;
         this.d2.px = this.d1.px;
         this.d2.py = this.d1.py;
         this.d2.rx = this.d1.rx;
         this.d2.ry = this.d1.ry;
         this.d1.t = var1;
         this.d1.px = var2;
         this.d1.py = var3;
         this.d1.rx = var4;
         this.d1.ry = var5;
      }

   }

   void extrapolate(int var1) {
      float var2 = (float)(var1 - this.d1.t) / (float)(this.d1.t - this.d2.t);
      float var3 = this.d1.px - this.d2.px;
      float var4 = this.d1.py - this.d2.py;
      this.movement.x = var3 * var2;
      this.movement.y = var4 * var2;
      if (var3 > this.minMovement || var4 > this.minMovement || -var3 > this.minMovement || -var4 > this.minMovement) {
         this.transform.moving = true;
         this.transform.rotation.x = this.movement.x;
         this.transform.rotation.y = this.movement.y;
         this.transform.rotation.normalize();
      }

      this.transform.position.x = this.d1.px + this.movement.x;
      this.transform.position.y = this.d1.py + this.movement.y;
      this.transform.operation = NetworkCharacter.Operation.EXTRAPOLATION;
   }

   void extrapolateInternal(int var1, float var2, float var3) {
      float var4 = (float)(var1 - this.d1.t) / (float)(var1 - this.d1.t);
      float var5 = var2 - this.d1.px;
      float var6 = var3 - this.d1.py;
      this.movement.x = var5 * var4;
      this.movement.y = var6 * var4;
      if (this.movement.getLength() > this.maxMovement) {
         this.movement.setLength(this.maxMovement);
      }

      if (var5 > this.minMovement || var6 > this.minMovement || -var5 > this.minMovement || -var6 > this.minMovement) {
         this.transform.moving = true;
         this.transform.rotation.x = this.movement.x;
         this.transform.rotation.y = this.movement.y;
         this.transform.rotation.normalize();
      }

      this.transform.position.x = var2 + this.movement.x;
      this.transform.position.y = var3 + this.movement.y;
      this.transform.operation = NetworkCharacter.Operation.EXTRAPOLATION;
   }

   void interpolate(int var1) {
      float var2 = (float)(var1 - this.d1.t) / (float)(this.d2.t - this.d1.t);
      float var3 = this.d2.px - this.d1.px;
      float var4 = this.d2.py - this.d1.py;
      this.movement.x = var3 * var2;
      this.movement.y = var4 * var2;
      if (this.movement.getLength() > this.maxMovement) {
         this.movement.setLength(this.maxMovement);
      }

      if (var3 > this.minMovement || var4 > this.minMovement || -var3 > this.minMovement || -var4 > this.minMovement) {
         this.transform.moving = true;
         this.transform.rotation.x = this.movement.x;
         this.transform.rotation.y = this.movement.y;
         this.transform.rotation.normalize();
      }

      this.transform.position.x = this.d1.px + this.movement.x;
      this.transform.position.y = this.d1.py + this.movement.y;
      this.transform.operation = NetworkCharacter.Operation.INTERPOLATION;
   }

   public NetworkCharacter.Transform predict(int var1, int var2, float var3, float var4, float var5, float var6) {
      this.transform.moving = false;
      this.transform.operation = NetworkCharacter.Operation.NONE;
      this.transform.time = var2 + var1;
      this.updateExtrapolationPoint(var2, var3, var4, var5, var6);
      if (this.d1.t != 0 && this.d2.t != 0) {
         this.extrapolate(var1 + var2);
      } else {
         this.updateTransform(var3, var4, var5, var6);
      }

      return this.transform;
   }

   public NetworkCharacter.Transform reconstruct(int var1, float var2, float var3, float var4, float var5) {
      this.transform.moving = false;
      this.transform.operation = NetworkCharacter.Operation.NONE;
      if (this.d2.t != 0) {
         if ((long)var1 + this.deltaTime <= (long)this.d2.t) {
            this.updatePointInternal(var2, var3, var4, var5);
            if (this.d1.t != 0 && this.d1.t != var1) {
               this.interpolate(var1);
            }

            this.d1.t = var1;
         } else if (var1 > this.d2.t && var1 < this.d2.t + 2000) {
            this.extrapolateInternal(var1, var2, var3);
            this.updatePointInternal(var2, var3, var4, var5);
            this.d1.t = var1;
         }
      }

      return this.transform;
   }

   public void checkReset(int var1) {
      if (var1 > 2000 + this.d2.t) {
         this.reset();
      }

   }

   public void checkResetPlayer(int var1) {
      if (var1 > 2000 + this.d1.t) {
         this.reset();
      }

   }

   public void reset() {
      this.d1.t = 0;
      this.d1.px = 0.0F;
      this.d1.py = 0.0F;
      this.d1.rx = 0.0F;
      this.d1.ry = 0.0F;
      this.d2.t = 0;
      this.d2.px = 0.0F;
      this.d2.py = 0.0F;
      this.d2.rx = 0.0F;
      this.d2.ry = 0.0F;
   }

   public static class Transform {
      public Vector2 position = new Vector2();
      public Vector2 rotation = new Vector2();
      public NetworkCharacter.Operation operation;
      public boolean moving;
      public int time;

      public Transform() {
         this.operation = NetworkCharacter.Operation.NONE;
         this.moving = false;
         this.time = 0;
      }
   }

   static class Point {
      public float px = 0.0F;
      public float py = 0.0F;
      public float rx = 0.0F;
      public float ry = 0.0F;
      public int t = 0;
   }

   public static enum Operation {
      INTERPOLATION,
      EXTRAPOLATION,
      NONE;

      // $FF: synthetic method
      private static NetworkCharacter.Operation[] $values() {
         return new NetworkCharacter.Operation[]{INTERPOLATION, EXTRAPOLATION, NONE};
      }
   }
}
