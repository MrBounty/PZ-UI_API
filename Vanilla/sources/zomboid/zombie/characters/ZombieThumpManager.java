package zombie.characters;

import fmod.fmod.FMODManager;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import zombie.core.math.PZMath;
import zombie.iso.IsoObject;

public final class ZombieThumpManager extends BaseZombieSoundManager {
   public static final ZombieThumpManager instance = new ZombieThumpManager();

   public ZombieThumpManager() {
      super(40, 100);
   }

   public void playSound(IsoZombie var1) {
      long var2 = 0L;
      if (var1.thumpFlag == 1) {
         var2 = var1.getEmitter().playSoundImpl("ZombieThumpGeneric", (IsoObject)null);
      } else if (var1.thumpFlag == 2) {
         var1.getEmitter().playSoundImpl("ZombieThumpGeneric", (IsoObject)null);
         var2 = var1.getEmitter().playSoundImpl("ZombieThumpWindow", (IsoObject)null);
      } else if (var1.thumpFlag == 3) {
         var2 = var1.getEmitter().playSoundImpl("ZombieThumpWindow", (IsoObject)null);
      } else if (var1.thumpFlag == 4) {
         var2 = var1.getEmitter().playSoundImpl("ZombieThumpMetal", (IsoObject)null);
      }

      FMOD_STUDIO_PARAMETER_DESCRIPTION var4 = FMODManager.instance.getParameterDescription("ObjectCondition");
      var1.getEmitter().setParameterValue(var2, var4, PZMath.ceil(var1.getThumpCondition() * 100.0F));
   }

   public void postUpdate() {
      for(int var1 = 0; var1 < this.characters.size(); ++var1) {
         ((IsoZombie)this.characters.get(var1)).setThumpFlag(0);
      }

   }
}
