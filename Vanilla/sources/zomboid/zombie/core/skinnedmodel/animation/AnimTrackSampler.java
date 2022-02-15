package zombie.core.skinnedmodel.animation;

import org.lwjgl.util.vector.Matrix4f;

public interface AnimTrackSampler {
   float getTotalTime();

   boolean isLooped();

   void moveToTime(float var1);

   float getCurrentTime();

   void getBoneMatrix(int var1, Matrix4f var2);

   int getNumBones();
}
