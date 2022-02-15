package zombie.core.skinnedmodel.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.scripting.ScriptParser;
import zombie.scripting.objects.BaseScriptObject;

public final class RuntimeAnimationScript extends BaseScriptObject {
   protected String m_name = this.toString();
   protected final ArrayList m_commands = new ArrayList();

   public void Load(String var1, String var2) {
      this.m_name = var1;
      ScriptParser.Block var3 = ScriptParser.parse(var2);
      var3 = (ScriptParser.Block)var3.children.get(0);
      Iterator var4 = var3.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         if ("xxx".equals(var6)) {
         }
      }

      var4 = var3.children.iterator();

      while(var4.hasNext()) {
         ScriptParser.Block var8 = (ScriptParser.Block)var4.next();
         if ("CopyFrame".equals(var8.type)) {
            CopyFrame var9 = new CopyFrame();
            var9.parse(var8);
            this.m_commands.add(var9);
         } else if ("CopyFrames".equals(var8.type)) {
            CopyFrames var10 = new CopyFrames();
            var10.parse(var8);
            this.m_commands.add(var10);
         }
      }

   }

   public void exec() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.m_commands.iterator();

      while(var2.hasNext()) {
         IRuntimeAnimationCommand var3 = (IRuntimeAnimationCommand)var2.next();
         var3.exec(var1);
      }

      float var4 = 0.0F;

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         var4 = Math.max(var4, ((Keyframe)var1.get(var5)).Time);
      }

      AnimationClip var6 = new AnimationClip(var4, var1, this.m_name, true);
      var1.clear();
      ModelManager.instance.addAnimationClip(var6.Name, var6);
      var1.clear();
   }

   public void reset() {
      this.m_name = this.toString();
      this.m_commands.clear();
   }
}
