package zombie.core.skinnedmodel;

import zombie.characters.IsoGameCharacter;
import zombie.core.textures.TextureDraw;
import zombie.iso.IsoMovingObject;
import zombie.popman.ObjectPool;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class ModelCameraRenderData extends TextureDraw.GenericDrawer {
   private ModelCamera m_camera;
   private float m_angle;
   private boolean m_bUseWorldIso;
   private float m_x;
   private float m_y;
   private float m_z;
   private boolean m_bInVehicle;
   public static final ObjectPool s_pool = new ObjectPool(ModelCameraRenderData::new);

   public ModelCameraRenderData init(ModelCamera var1, ModelManager.ModelSlot var2) {
      IsoMovingObject var3 = var2.model.object;
      IsoGameCharacter var4 = (IsoGameCharacter)Type.tryCastTo(var3, IsoGameCharacter.class);
      this.m_camera = var1;
      this.m_x = var3.x;
      this.m_y = var3.y;
      this.m_z = var3.z;
      if (var4 == null) {
         this.m_angle = 0.0F;
         this.m_bInVehicle = false;
         this.m_bUseWorldIso = !BaseVehicle.RENDER_TO_TEXTURE;
      } else {
         this.m_bInVehicle = var4.isSeatedInVehicle();
         if (this.m_bInVehicle) {
            this.m_angle = 0.0F;
            BaseVehicle var5 = var4.getVehicle();
            this.m_x = var5.x;
            this.m_y = var5.y;
            this.m_z = var5.z;
         } else {
            this.m_angle = var4.getAnimationPlayer().getRenderedAngle();
         }

         this.m_bUseWorldIso = true;
      }

      return this;
   }

   public ModelCameraRenderData init(ModelCamera var1, float var2, boolean var3, float var4, float var5, float var6, boolean var7) {
      this.m_camera = var1;
      this.m_angle = var2;
      this.m_bUseWorldIso = var3;
      this.m_x = var4;
      this.m_y = var5;
      this.m_z = var6;
      this.m_bInVehicle = var7;
      return this;
   }

   public void render() {
      this.m_camera.m_useAngle = this.m_angle;
      this.m_camera.m_bUseWorldIso = this.m_bUseWorldIso;
      this.m_camera.m_x = this.m_x;
      this.m_camera.m_y = this.m_y;
      this.m_camera.m_z = this.m_z;
      this.m_camera.m_bInVehicle = this.m_bInVehicle;
      ModelCamera.instance = this.m_camera;
   }

   public void postRender() {
      s_pool.release((Object)this);
   }
}
