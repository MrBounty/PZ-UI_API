package zombie.scripting.objects;

import java.util.HashMap;
import java.util.Iterator;
import zombie.core.math.PZMath;
import zombie.scripting.ScriptParser;

public final class SoundTimelineScript extends BaseScriptObject {
   private String eventName;
   private HashMap positionByName = new HashMap();

   public void Load(String var1, String var2) {
      this.eventName = var1;
      ScriptParser.Block var3 = ScriptParser.parse(var2);
      var3 = (ScriptParser.Block)var3.children.get(0);
      Iterator var4 = var3.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         this.positionByName.put(var6, PZMath.tryParseInt(var7, 0));
      }

   }

   public String getEventName() {
      return this.eventName;
   }

   public int getPosition(String var1) {
      return this.positionByName.containsKey(var1) ? (Integer)this.positionByName.get(var1) : -1;
   }

   public void reset() {
      this.positionByName.clear();
   }
}
