package zombie.characters;

public final class ZombieFootstepManager extends BaseZombieSoundManager {
   public static final ZombieFootstepManager instance = new ZombieFootstepManager();

   public ZombieFootstepManager() {
      super(40, 500);
   }

   public void playSound(IsoZombie var1) {
      var1.getEmitter().playFootsteps("ZombieFootstepsCombined", var1.getFootstepVolume());
   }

   public void postUpdate() {
   }
}
