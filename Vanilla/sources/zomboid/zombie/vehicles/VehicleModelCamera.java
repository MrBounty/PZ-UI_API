package zombie.vehicles;

import org.joml.Math;
import org.lwjgl.opengl.GL11;
import zombie.core.Core;
import zombie.core.skinnedmodel.ModelCamera;

public final class VehicleModelCamera extends ModelCamera {
   public static final VehicleModelCamera instance = new VehicleModelCamera();

   public void Begin() {
      if (this.m_bUseWorldIso) {
         Core.getInstance().DoPushIsoStuff(this.m_x, this.m_y, this.m_z, this.m_useAngle, true);
         GL11.glDepthMask(this.bDepthMask);
      } else {
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glOrtho(-192.0D, 192.0D, -192.0D, 192.0D, -1000.0D, 1000.0D);
         float var1 = Math.sqrt(2048.0F);
         GL11.glScalef(-var1, var1, var1);
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      }
   }

   public void End() {
      if (this.m_bUseWorldIso) {
         Core.getInstance().DoPopIsoStuff();
      } else {
         GL11.glDepthFunc(519);
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
      }
   }
}
