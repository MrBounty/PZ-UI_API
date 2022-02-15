package zombie.audio;

import fmod.javafmod;

public abstract class FMODGlobalParameter extends FMODParameter {
   public FMODGlobalParameter(String var1) {
      super(var1);
      if (this.getParameterDescription() != null && !this.getParameterDescription().isGlobal()) {
         boolean var2 = true;
      }

   }

   public void setCurrentValue(float var1) {
      javafmod.FMOD_Studio_System_SetParameterByID(this.getParameterID(), var1, false);
   }

   public void startEventInstance(long var1) {
   }

   public void stopEventInstance(long var1) {
   }
}
