package zombie;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;
import zombie.vehicles.BaseVehicle;

public final class MovingObjectUpdateScheduler {
   public static final MovingObjectUpdateScheduler instance = new MovingObjectUpdateScheduler();
   final MovingObjectUpdateSchedulerUpdateBucket fullSimulation = new MovingObjectUpdateSchedulerUpdateBucket(1);
   final MovingObjectUpdateSchedulerUpdateBucket halfSimulation = new MovingObjectUpdateSchedulerUpdateBucket(2);
   final MovingObjectUpdateSchedulerUpdateBucket quarterSimulation = new MovingObjectUpdateSchedulerUpdateBucket(4);
   final MovingObjectUpdateSchedulerUpdateBucket eighthSimulation = new MovingObjectUpdateSchedulerUpdateBucket(8);
   final MovingObjectUpdateSchedulerUpdateBucket sixteenthSimulation = new MovingObjectUpdateSchedulerUpdateBucket(16);
   long frameCounter;
   private boolean isEnabled = true;

   public long getFrameCounter() {
      return this.frameCounter;
   }

   public void startFrame() {
      ++this.frameCounter;
      this.fullSimulation.clear();
      this.halfSimulation.clear();
      this.quarterSimulation.clear();
      this.eighthSimulation.clear();
      this.sixteenthSimulation.clear();
      ArrayList var1 = IsoWorld.instance.getCell().getObjectList();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         IsoMovingObject var3 = (IsoMovingObject)var1.get(var2);
         if (!GameServer.bServer || !(var3 instanceof IsoZombie)) {
            boolean var4 = false;
            boolean var5 = false;
            float var6 = 1.0E8F;
            boolean var7 = false;

            int var8;
            for(var8 = 0; var8 < IsoPlayer.numPlayers; ++var8) {
               IsoPlayer var9 = IsoPlayer.players[var8];
               if (var9 != null) {
                  if (var3.getCurrentSquare() == null) {
                     var3.setCurrent(IsoWorld.instance.getCell().getGridSquare((double)var3.x, (double)var3.y, (double)var3.z));
                  }

                  if (var9 == var3) {
                     var7 = true;
                  }

                  if (var3.getCurrentSquare() != null) {
                     if (var3.getCurrentSquare().isCouldSee(var8)) {
                        var4 = true;
                     }

                     if (var3.getCurrentSquare().isCanSee(var8)) {
                        var5 = true;
                     }

                     float var10 = var3.DistTo(var9);
                     if (var10 < var6) {
                        var6 = var10;
                     }
                  }
               }
            }

            var8 = 3;
            if (!var5) {
               --var8;
            }

            if (!var4 && var6 > 10.0F) {
               --var8;
            }

            if (var6 > 30.0F) {
               --var8;
            }

            if (var6 > 60.0F) {
               --var8;
            }

            if (var6 > 80.0F) {
               --var8;
            }

            if (var3 instanceof IsoPlayer) {
               var8 = 3;
            }

            if (var3 instanceof BaseVehicle) {
               var8 = 3;
            }

            if (GameServer.bServer) {
               var8 = 3;
            }

            if (var7) {
               var8 = 3;
            }

            if (!this.isEnabled) {
               var8 = 3;
            }

            if (var8 == 3) {
               this.fullSimulation.add(var3);
            }

            if (var8 == 2) {
               this.halfSimulation.add(var3);
            }

            if (var8 == 1) {
               this.quarterSimulation.add(var3);
            }

            if (var8 == 0) {
               this.eighthSimulation.add(var3);
            }

            if (var8 < 0) {
               this.sixteenthSimulation.add(var3);
            }
         }
      }

   }

   public void update() {
      GameTime.getInstance().PerObjectMultiplier = 1.0F;
      this.fullSimulation.update((int)this.frameCounter);
      this.halfSimulation.update((int)this.frameCounter);
      this.quarterSimulation.update((int)this.frameCounter);
      this.eighthSimulation.update((int)this.frameCounter);
      this.sixteenthSimulation.update((int)this.frameCounter);
   }

   public void postupdate() {
      GameTime.getInstance().PerObjectMultiplier = 1.0F;
      this.fullSimulation.postupdate((int)this.frameCounter);
      this.halfSimulation.postupdate((int)this.frameCounter);
      this.quarterSimulation.postupdate((int)this.frameCounter);
      this.eighthSimulation.postupdate((int)this.frameCounter);
      this.sixteenthSimulation.postupdate((int)this.frameCounter);
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(boolean var1) {
      this.isEnabled = var1;
   }

   public void removeObject(IsoMovingObject var1) {
      this.fullSimulation.removeObject(var1);
      this.halfSimulation.removeObject(var1);
      this.quarterSimulation.removeObject(var1);
      this.eighthSimulation.removeObject(var1);
      this.sixteenthSimulation.removeObject(var1);
   }

   public ArrayList getBucket() {
      return this.fullSimulation.getBucket((int)this.frameCounter);
   }
}
