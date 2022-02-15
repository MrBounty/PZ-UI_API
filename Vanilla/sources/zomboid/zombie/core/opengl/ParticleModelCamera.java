package zombie.core.opengl;

import zombie.core.Core;
import zombie.core.skinnedmodel.ModelCamera;

public final class ParticleModelCamera extends ModelCamera {
   public void Begin() {
      Core.getInstance().DoPushIsoParticleStuff(this.m_x, this.m_y, this.m_z);
   }

   public void End() {
      Core.getInstance().DoPopIsoStuff();
   }
}
