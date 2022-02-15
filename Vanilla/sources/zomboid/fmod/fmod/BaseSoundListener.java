package fmod.fmod;

public abstract class BaseSoundListener {
   public int index;
   public float x;
   public float y;
   public float z;

   public BaseSoundListener(int var1) {
      this.index = var1;
   }

   public void setPos(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public abstract void tick();
}
