package zombie.core.skinnedmodel.advancedanimation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import zombie.core.logger.ExceptionLogger;
import zombie.core.skinnedmodel.animation.BoneAxis;
import zombie.core.skinnedmodel.model.jassimp.JAssImpImporter;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

@XmlRootElement
public final class AnimNode {
   private static final Comparator s_eventsComparator = (var0, var1) -> {
      return Float.compare(var0.m_TimePc, var1.m_TimePc);
   };
   public String m_Name = "";
   public int m_Priority = 5;
   public String m_AnimName = "";
   public String m_DeferredBoneName = "";
   public BoneAxis m_deferredBoneAxis;
   public boolean m_useDeferedRotation;
   public boolean m_Looped;
   public float m_BlendTime;
   public float m_BlendOutTime;
   public boolean m_StopAnimOnExit;
   public boolean m_EarlyTransitionOut;
   public String m_SpeedScale;
   public String m_SpeedScaleVariable;
   public float m_SpeedScaleRandomMultiplierMin;
   public float m_SpeedScaleRandomMultiplierMax;
   @XmlTransient
   private float m_SpeedScaleF;
   public float m_randomAdvanceFraction;
   public float m_maxTorsoTwist;
   public String m_Scalar;
   public String m_Scalar2;
   public boolean m_AnimReverse;
   public boolean m_SyncTrackingEnabled;
   public List m_2DBlends;
   public List m_Conditions;
   public List m_Events;
   public List m_2DBlendTri;
   public List m_Transitions;
   public List m_SubStateBoneWeights;
   @XmlTransient
   public Anim2DBlendPicker m_picker;
   @XmlTransient
   public AnimState m_State;
   @XmlTransient
   private AnimTransition m_transitionOut;

   public AnimNode() {
      this.m_deferredBoneAxis = BoneAxis.Y;
      this.m_useDeferedRotation = false;
      this.m_Looped = true;
      this.m_BlendTime = 0.0F;
      this.m_BlendOutTime = -1.0F;
      this.m_StopAnimOnExit = false;
      this.m_EarlyTransitionOut = false;
      this.m_SpeedScale = "1.00";
      this.m_SpeedScaleVariable = null;
      this.m_SpeedScaleRandomMultiplierMin = 1.0F;
      this.m_SpeedScaleRandomMultiplierMax = 1.0F;
      this.m_SpeedScaleF = Float.POSITIVE_INFINITY;
      this.m_randomAdvanceFraction = 0.0F;
      this.m_maxTorsoTwist = 15.0F;
      this.m_Scalar = "";
      this.m_Scalar2 = "";
      this.m_AnimReverse = false;
      this.m_SyncTrackingEnabled = true;
      this.m_2DBlends = new ArrayList();
      this.m_Conditions = new ArrayList();
      this.m_Events = new ArrayList();
      this.m_2DBlendTri = new ArrayList();
      this.m_Transitions = new ArrayList();
      this.m_SubStateBoneWeights = new ArrayList();
      this.m_State = null;
   }

   public static AnimNode Parse(String var0) {
      try {
         AnimNode var1 = (AnimNode)PZXmlUtil.parse(AnimNode.class, var0);
         if (var1.m_2DBlendTri.size() > 0) {
            var1.m_picker = new Anim2DBlendPicker();
            var1.m_picker.SetPickTriangles(var1.m_2DBlendTri);
         }

         PZArrayUtil.forEach(var1.m_Events, (var0x) -> {
            if ("SetVariable".equalsIgnoreCase(var0x.m_EventName)) {
               String[] var1 = var0x.m_ParameterValue.split("=");
               if (var1.length == 2) {
                  var0x.m_SetVariable1 = var1[0];
                  var0x.m_SetVariable2 = var1[1];
               }
            }

         });
         var1.m_Events.sort(s_eventsComparator);

         try {
            var1.m_SpeedScaleF = Float.parseFloat(var1.m_SpeedScale);
         } catch (NumberFormatException var4) {
            var1.m_SpeedScaleVariable = var1.m_SpeedScale;
         }

         if (var1.m_SubStateBoneWeights.isEmpty()) {
            var1.m_SubStateBoneWeights.add(new AnimBoneWeight("Bip01_Spine1", 0.5F));
            var1.m_SubStateBoneWeights.add(new AnimBoneWeight("Bip01_Neck", 1.0F));
            var1.m_SubStateBoneWeights.add(new AnimBoneWeight("Bip01_BackPack", 1.0F));
            var1.m_SubStateBoneWeights.add(new AnimBoneWeight("Bip01_Prop1", 1.0F));
            var1.m_SubStateBoneWeights.add(new AnimBoneWeight("Bip01_Prop2", 1.0F));
         }

         int var2;
         for(var2 = 0; var2 < var1.m_SubStateBoneWeights.size(); ++var2) {
            AnimBoneWeight var3 = (AnimBoneWeight)var1.m_SubStateBoneWeights.get(var2);
            var3.boneName = JAssImpImporter.getSharedString(var3.boneName, "AnimBoneWeight.boneName");
         }

         var1.m_transitionOut = null;

         for(var2 = 0; var2 < var1.m_Transitions.size(); ++var2) {
            AnimTransition var6 = (AnimTransition)var1.m_Transitions.get(var2);
            if (StringUtils.isNullOrWhitespace(var6.m_Target)) {
               var1.m_transitionOut = var6;
            }
         }

         return var1;
      } catch (PZXmlParserException var5) {
         System.err.println("AnimNode.Parse threw an exception reading file: " + var0);
         ExceptionLogger.logException(var5);
         return null;
      }
   }

   public boolean checkConditions(IAnimationVariableSource var1) {
      List var2 = this.m_Conditions;
      return AnimCondition.pass(var1, var2);
   }

   public float getSpeedScale(IAnimationVariableSource var1) {
      return this.m_SpeedScaleF != Float.POSITIVE_INFINITY ? this.m_SpeedScaleF : var1.getVariableFloat(this.m_SpeedScale, 1.0F);
   }

   public boolean isIdleAnim() {
      return this.m_Name.contains("Idle");
   }

   public AnimTransition findTransitionTo(IAnimationVariableSource var1, String var2) {
      AnimTransition var3 = null;
      int var4 = 0;

      for(int var5 = this.m_Transitions.size(); var4 < var5; ++var4) {
         AnimTransition var6 = (AnimTransition)this.m_Transitions.get(var4);
         if (StringUtils.equalsIgnoreCase(var6.m_Target, var2) && AnimCondition.pass(var1, var6.m_Conditions)) {
            var3 = var6;
            break;
         }
      }

      return var3;
   }

   public String toString() {
      return String.format("AnimNode{ Name: %s, AnimName: %s, Conditions: %s }", this.m_Name, this.m_AnimName, this.getConditionsString());
   }

   public String getConditionsString() {
      return PZArrayUtil.arrayToString(this.m_Conditions, AnimCondition::getConditionString, "( ", " )", ", ");
   }

   public boolean isAbstract() {
      if (!StringUtils.isNullOrWhitespace(this.m_AnimName)) {
         return false;
      } else {
         return this.m_2DBlends.isEmpty();
      }
   }

   public float getBlendOutTime() {
      if (this.m_transitionOut != null) {
         return this.m_transitionOut.m_blendOutTime;
      } else {
         return this.m_BlendOutTime >= 0.0F ? this.m_BlendOutTime : this.m_BlendTime;
      }
   }

   public String getDeferredBoneName() {
      return StringUtils.isNullOrWhitespace(this.m_DeferredBoneName) ? "Translation_Data" : this.m_DeferredBoneName;
   }

   public BoneAxis getDeferredBoneAxis() {
      return this.m_deferredBoneAxis;
   }

   public int getPriority() {
      return this.m_Priority;
   }
}
