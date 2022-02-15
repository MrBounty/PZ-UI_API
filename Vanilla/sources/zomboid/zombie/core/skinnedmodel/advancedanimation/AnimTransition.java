package zombie.core.skinnedmodel.advancedanimation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class AnimTransition {
   public String m_Target;
   public String m_AnimName;
   public float m_SyncAdjustTime = 0.0F;
   public float m_blendInTime = Float.POSITIVE_INFINITY;
   public float m_blendOutTime = Float.POSITIVE_INFINITY;
   public float m_speedScale = Float.POSITIVE_INFINITY;
   public List m_Conditions = new ArrayList();
}
