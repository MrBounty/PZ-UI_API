package zombie.core.skinnedmodel.model;

import java.util.Arrays;
import org.joml.Vector3f;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.textures.Texture;
import zombie.iso.IsoLightSource;

public final class VehicleModelInstance extends ModelInstance {
   public Texture textureRust = null;
   public Texture textureMask = null;
   public Texture textureLights = null;
   public Texture textureDamage1Overlay = null;
   public Texture textureDamage1Shell = null;
   public Texture textureDamage2Overlay = null;
   public Texture textureDamage2Shell = null;
   public final float[] textureUninstall1 = new float[16];
   public final float[] textureUninstall2 = new float[16];
   public final float[] textureLightsEnables1 = new float[16];
   public final float[] textureLightsEnables2 = new float[16];
   public final float[] textureDamage1Enables1 = new float[16];
   public final float[] textureDamage1Enables2 = new float[16];
   public final float[] textureDamage2Enables1 = new float[16];
   public final float[] textureDamage2Enables2 = new float[16];
   public final float[] matrixBlood1Enables1 = new float[16];
   public final float[] matrixBlood1Enables2 = new float[16];
   public final float[] matrixBlood2Enables1 = new float[16];
   public final float[] matrixBlood2Enables2 = new float[16];
   public float textureRustA = 0.0F;
   public float refWindows = 0.5F;
   public float refBody = 0.4F;
   public final Vector3f painColor = new Vector3f(0.0F, 0.5F, 0.5F);
   private IsoLightSource[] m_lights = new IsoLightSource[3];
   public final Object m_lightsLock = "Model Lights Lock";

   public void reset() {
      super.reset();
      Arrays.fill(this.textureUninstall1, 0.0F);
      Arrays.fill(this.textureUninstall2, 0.0F);
      Arrays.fill(this.textureLightsEnables1, 0.0F);
      Arrays.fill(this.textureLightsEnables2, 0.0F);
      Arrays.fill(this.textureDamage1Enables1, 0.0F);
      Arrays.fill(this.textureDamage1Enables2, 0.0F);
      Arrays.fill(this.textureDamage2Enables1, 0.0F);
      Arrays.fill(this.textureDamage2Enables2, 0.0F);
      Arrays.fill(this.matrixBlood1Enables1, 0.0F);
      Arrays.fill(this.matrixBlood1Enables2, 0.0F);
      Arrays.fill(this.matrixBlood2Enables1, 0.0F);
      Arrays.fill(this.matrixBlood2Enables2, 0.0F);
      this.textureRustA = 0.0F;
      this.refWindows = 0.5F;
      this.refBody = 0.4F;
      this.painColor.set(0.0F, 0.5F, 0.5F);
      Arrays.fill(this.m_lights, (Object)null);
   }

   public void setLights(IsoLightSource[] var1) {
      this.m_lights = var1;
   }

   public IsoLightSource[] getLights() {
      return this.m_lights;
   }

   public void UpdateLights() {
      synchronized(this.m_lightsLock) {
         ModelManager.instance.getClosestThreeLights(this.object, this.m_lights);
      }
   }
}
