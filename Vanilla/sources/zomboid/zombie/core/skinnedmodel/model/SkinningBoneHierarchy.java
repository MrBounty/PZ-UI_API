package zombie.core.skinnedmodel.model;

import java.util.Iterator;
import java.util.Map.Entry;
import zombie.util.list.PZArrayUtil;

public final class SkinningBoneHierarchy {
   private boolean m_boneHieararchyValid = false;
   private SkinningBone[] m_allBones = null;
   private SkinningBone[] m_rootBones = null;

   public boolean isValid() {
      return this.m_boneHieararchyValid;
   }

   public void buildBoneHiearchy(SkinningData var1) {
      this.m_rootBones = new SkinningBone[0];
      this.m_allBones = new SkinningBone[var1.numBones()];
      PZArrayUtil.arrayPopulate(this.m_allBones, SkinningBone::new);

      int var4;
      SkinningBone var6;
      for(Iterator var2 = var1.BoneIndices.entrySet().iterator(); var2.hasNext(); var6.Children = new SkinningBone[0]) {
         Entry var3 = (Entry)var2.next();
         var4 = (Integer)var3.getValue();
         String var5 = (String)var3.getKey();
         var6 = this.m_allBones[var4];
         var6.Index = var4;
         var6.Name = var5;
      }

      for(int var7 = 0; var7 < var1.numBones(); ++var7) {
         SkinningBone var8 = this.m_allBones[var7];
         var4 = var1.getParentBoneIdx(var7);
         if (var4 > -1) {
            var8.Parent = this.m_allBones[var4];
            var8.Parent.Children = (SkinningBone[])PZArrayUtil.add(var8.Parent.Children, var8);
         } else {
            this.m_rootBones = (SkinningBone[])PZArrayUtil.add(this.m_rootBones, var8);
         }
      }

      this.m_boneHieararchyValid = true;
   }

   public int numRootBones() {
      return this.m_rootBones.length;
   }

   public SkinningBone getBoneAt(int var1) {
      return this.m_allBones[var1];
   }

   public SkinningBone getRootBoneAt(int var1) {
      return this.m_rootBones[var1];
   }
}
