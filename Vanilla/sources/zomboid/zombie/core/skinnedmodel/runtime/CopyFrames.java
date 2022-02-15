package zombie.core.skinnedmodel.runtime;

import java.util.Iterator;
import java.util.List;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.scripting.ScriptParser;

public final class CopyFrames implements IRuntimeAnimationCommand {
   protected int m_frame;
   protected int m_FPS = 30;
   protected String m_source;
   protected int m_sourceFrame1;
   protected int m_sourceFrame2;
   protected int m_sourceFPS = 30;

   public void parse(ScriptParser.Block var1) {
      Iterator var2 = var1.values.iterator();

      while(var2.hasNext()) {
         ScriptParser.Value var3 = (ScriptParser.Value)var2.next();
         String var4 = var3.getKey().trim();
         String var5 = var3.getValue().trim();
         if ("source".equalsIgnoreCase(var4)) {
            this.m_source = var5;
         } else if ("frame".equalsIgnoreCase(var4)) {
            this.m_frame = PZMath.tryParseInt(var5, 1);
         } else if ("sourceFrame1".equalsIgnoreCase(var4)) {
            this.m_sourceFrame1 = PZMath.tryParseInt(var5, 1);
         } else if ("sourceFrame2".equalsIgnoreCase(var4)) {
            this.m_sourceFrame2 = PZMath.tryParseInt(var5, 1);
         }
      }

   }

   public void exec(List var1) {
      AnimationClip var2 = ModelManager.instance.getAnimationClip(this.m_source);

      for(int var3 = 0; var3 < 60; ++var3) {
         Keyframe[] var4 = var2.getBoneFramesAt(var3);
         if (var4.length != 0) {
            for(int var5 = this.m_sourceFrame1; var5 <= this.m_sourceFrame2; ++var5) {
               Keyframe var6 = var4[0];
               Keyframe var7 = new Keyframe();
               var7.Bone = var6.Bone;
               var7.BoneName = var6.BoneName;
               var7.Time = (float)(this.m_frame - 1 + (var5 - this.m_sourceFrame1)) / (float)this.m_FPS;
               var7.Position = KeyframeUtil.GetKeyFramePosition(var4, (float)(var5 - 1) / (float)this.m_sourceFPS, (double)var2.Duration);
               var7.Rotation = KeyframeUtil.GetKeyFrameRotation(var4, (float)(var5 - 1) / (float)this.m_sourceFPS, (double)var2.Duration);
               var7.Scale = var6.Scale;
               var1.add(var7);
            }
         }
      }

   }
}
