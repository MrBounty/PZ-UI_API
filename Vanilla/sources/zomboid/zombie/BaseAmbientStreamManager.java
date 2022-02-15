package zombie;

import zombie.iso.RoomDef;

public abstract class BaseAmbientStreamManager {
   public abstract void stop();

   public abstract void doAlarm(RoomDef var1);

   public abstract void doGunEvent();

   public abstract void init();

   public abstract void addBlend(String var1, float var2, boolean var3, boolean var4, boolean var5, boolean var6);

   protected abstract void addRandomAmbient();

   public abstract void doOneShotAmbients();

   public abstract void update();

   public abstract void addAmbient(String var1, int var2, int var3, int var4, float var5);

   public abstract void addAmbientEmitter(float var1, float var2, int var3, String var4);

   public abstract void addDaytimeAmbientEmitter(float var1, float var2, int var3, String var4);
}
