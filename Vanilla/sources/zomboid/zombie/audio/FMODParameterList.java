package zombie.audio;

import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import java.util.ArrayList;

public final class FMODParameterList {
   public final ArrayList parameterList = new ArrayList();
   public final FMODParameter[] parameterArray = new FMODParameter[96];

   public void add(FMODParameter var1) {
      this.parameterList.add(var1);
      if (var1.getParameterDescription() != null) {
         this.parameterArray[var1.getParameterDescription().globalIndex] = var1;
      }

   }

   public FMODParameter get(FMOD_STUDIO_PARAMETER_DESCRIPTION var1) {
      return var1 == null ? null : this.parameterArray[var1.globalIndex];
   }

   public void update() {
      for(int var1 = 0; var1 < this.parameterList.size(); ++var1) {
         ((FMODParameter)this.parameterList.get(var1)).update();
      }

   }
}
