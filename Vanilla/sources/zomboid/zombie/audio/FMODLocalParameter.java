package zombie.audio;

import fmod.javafmod;
import gnu.trove.list.array.TLongArrayList;

public class FMODLocalParameter extends FMODParameter {
   private final TLongArrayList m_instances = new TLongArrayList();

   public FMODLocalParameter(String var1) {
      super(var1);
      if (this.getParameterDescription() != null && this.getParameterDescription().isGlobal()) {
         boolean var2 = true;
      }

   }

   public float calculateCurrentValue() {
      return 0.0F;
   }

   public void setCurrentValue(float var1) {
      for(int var2 = 0; var2 < this.m_instances.size(); ++var2) {
         long var3 = this.m_instances.get(var2);
         javafmod.FMOD_Studio_EventInstance_SetParameterByID(var3, this.getParameterID(), var1, false);
      }

   }

   public void startEventInstance(long var1) {
      this.m_instances.add(var1);
      javafmod.FMOD_Studio_EventInstance_SetParameterByID(var1, this.getParameterID(), this.getCurrentValue(), false);
   }

   public void stopEventInstance(long var1) {
      this.m_instances.remove(var1);
   }
}
