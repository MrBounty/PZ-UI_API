package zombie.core.skinnedmodel.advancedanimation;

import zombie.characters.action.ActionContext;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.core.skinnedmodel.model.ModelInstance;

public interface IAnimatable extends IAnimationVariableSource {
   ActionContext getActionContext();

   AnimationPlayer getAnimationPlayer();

   AnimationPlayerRecorder getAnimationPlayerRecorder();

   boolean isAnimationRecorderActive();

   AdvancedAnimator getAdvancedAnimator();

   ModelInstance getModelInstance();

   String GetAnimSetName();

   String getUID();

   default short getOnlineID() {
      return -1;
   }
}
