package zombie;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoDeadBody;
import zombie.util.Type;

public final class MovingObjectUpdateSchedulerUpdateBucket {
   public int frameMod;
   ArrayList[] buckets;

   public MovingObjectUpdateSchedulerUpdateBucket(int var1) {
      this.init(var1);
   }

   public void init(int var1) {
      this.frameMod = var1;
      this.buckets = new ArrayList[var1];

      for(int var2 = 0; var2 < this.buckets.length; ++var2) {
         this.buckets[var2] = new ArrayList();
      }

   }

   public void clear() {
      for(int var1 = 0; var1 < this.buckets.length; ++var1) {
         ArrayList var2 = this.buckets[var1];
         var2.clear();
      }

   }

   public void remove(IsoMovingObject var1) {
      for(int var2 = 0; var2 < this.buckets.length; ++var2) {
         ArrayList var3 = this.buckets[var2];
         var3.remove(var1);
      }

   }

   public void add(IsoMovingObject var1) {
      int var2 = var1.getID() % this.frameMod;
      this.buckets[var2].add(var1);
   }

   public void update(int var1) {
      GameTime.getInstance().PerObjectMultiplier = (float)this.frameMod;
      ArrayList var2 = this.buckets[var1 % this.frameMod];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)var2.get(var3);
         if (var4 instanceof IsoDeadBody) {
            IsoWorld.instance.getCell().getRemoveList().add(var4);
         } else {
            IsoZombie var5 = (IsoZombie)Type.tryCastTo(var4, IsoZombie.class);
            if (var5 != null && VirtualZombieManager.instance.isReused(var5)) {
               DebugLog.log(DebugType.Zombie, "REUSABLE ZOMBIE IN MovingObjectUpdateSchedulerUpdateBucket IGNORED " + var4);
            } else {
               var4.preupdate();
               var4.update();
            }
         }
      }

      GameTime.getInstance().PerObjectMultiplier = 1.0F;
   }

   public void postupdate(int var1) {
      GameTime.getInstance().PerObjectMultiplier = (float)this.frameMod;
      ArrayList var2 = this.buckets[var1 % this.frameMod];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)var2.get(var3);
         IsoZombie var5 = (IsoZombie)Type.tryCastTo(var4, IsoZombie.class);
         if (var5 != null && VirtualZombieManager.instance.isReused(var5)) {
            DebugLog.log(DebugType.Zombie, "REUSABLE ZOMBIE IN MovingObjectUpdateSchedulerUpdateBucket IGNORED " + var4);
         } else {
            var4.postupdate();
         }
      }

      GameTime.getInstance().PerObjectMultiplier = 1.0F;
   }

   public void removeObject(IsoMovingObject var1) {
      for(int var2 = 0; var2 < this.buckets.length; ++var2) {
         ArrayList var3 = this.buckets[var2];
         var3.remove(var1);
      }

   }

   public ArrayList getBucket(int var1) {
      return this.buckets[var1 % this.frameMod];
   }
}
