package zombie.core.opengl;

import org.joml.Math;
import org.lwjgl.opengl.GL11;
import zombie.core.Core;
import zombie.core.skinnedmodel.ModelCamera;

public final class CharacterModelCamera extends ModelCamera {
   public static final CharacterModelCamera instance = new CharacterModelCamera();

   public void Begin() {
      if (this.m_bUseWorldIso) {
         Core.getInstance().DoPushIsoStuff(this.m_x, this.m_y, this.m_z, this.m_useAngle, this.m_bInVehicle);
         GL11.glDepthMask(this.bDepthMask);
      } else {
         short var1 = 1024;
         short var2 = 1024;
         float var3 = 42.75F;
         float var4 = 0.0F;
         float var5 = -0.45F;
         float var6 = 0.0F;
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         float var7 = (float)var1 / (float)var2;
         boolean var8 = false;
         if (var8) {
            GL11.glOrtho((double)(-var3 * var7), (double)(var3 * var7), (double)var3, (double)(-var3), -100.0D, 100.0D);
         } else {
            GL11.glOrtho((double)(-var3 * var7), (double)(var3 * var7), (double)(-var3), (double)var3, -100.0D, 100.0D);
         }

         float var9 = Math.sqrt(2048.0F);
         GL11.glScalef(-var9, var9, var9);
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(var4, var5, var6);
         GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotated(Math.toDegrees((double)this.m_useAngle) + 45.0D, 0.0D, 1.0D, 0.0D);
         GL11.glDepthRange(0.0D, 1.0D);
         GL11.glDepthMask(this.bDepthMask);
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
