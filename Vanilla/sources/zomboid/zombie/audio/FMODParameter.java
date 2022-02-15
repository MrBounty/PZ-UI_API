package zombie.audio;

import fmod.fmod.FMODManager;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import fmod.fmod.FMOD_STUDIO_PARAMETER_ID;

public abstract class FMODParameter {
   private final String m_name;
   private final FMOD_STUDIO_PARAMETER_DESCRIPTION m_parameterDescription;
   private float m_currentValue = Float.NaN;

   public FMODParameter(String var1) {
      this.m_name = var1;
      this.m_parameterDescription = FMODManager.instance.getParameterDescription(var1);
   }

   public String getName() {
      return this.m_name;
   }

   public FMOD_STUDIO_PARAMETER_DESCRIPTION getParameterDescription() {
      return this.m_parameterDescription;
   }

   public FMOD_STUDIO_PARAMETER_ID getParameterID() {
      return this.m_parameterDescription == null ? null : this.m_parameterDescription.id;
   }

   public float getCurrentValue() {
      return this.m_currentValue;
   }

   public void update() {
      float var1 = this.calculateCurrentValue();
      if (var1 != this.m_currentValue) {
         this.m_currentValue = var1;
         this.setCurrentValue(this.m_currentValue);
      }
   }

   public void resetToDefault() {
   }

   public abstract float calculateCurrentValue();

   public abstract void setCurrentValue(float var1);

   public abstract void startEventInstance(long var1);

   public abstract void stopEventInstance(long var1);
}
