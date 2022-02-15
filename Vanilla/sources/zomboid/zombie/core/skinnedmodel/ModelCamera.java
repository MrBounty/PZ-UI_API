package zombie.core.skinnedmodel;

import zombie.core.opengl.IModelCamera;

public abstract class ModelCamera implements IModelCamera {
   public static ModelCamera instance = null;
   public float m_useAngle;
   public boolean m_bUseWorldIso;
   public float m_x;
   public float m_y;
   public float m_z;
   public boolean m_bInVehicle;
   public boolean bDepthMask = true;
}
