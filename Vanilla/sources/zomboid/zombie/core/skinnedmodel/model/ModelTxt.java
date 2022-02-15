package zombie.core.skinnedmodel.model;

import java.util.ArrayList;
import java.util.HashMap;

public final class ModelTxt {
   boolean bStatic;
   boolean bReverse;
   VertexBufferObject.VertexArray vertices;
   int[] elements;
   HashMap boneIndices = new HashMap();
   ArrayList SkeletonHierarchy = new ArrayList();
   ArrayList bindPose = new ArrayList();
   ArrayList skinOffsetMatrices = new ArrayList();
   ArrayList invBindPose = new ArrayList();
   HashMap clips = new HashMap();
}
