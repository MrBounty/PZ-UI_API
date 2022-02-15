package zombie.radio.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.radio.globals.CompareResult;

public final class ExitOptionOld {
   private String parentScript;
   private String name;
   private ConditionContainer condition;
   private List scriptEntries = new ArrayList();

   public ExitOptionOld(String var1, String var2) {
      this.parentScript = var1 != null ? var1 : "Noname";
      this.name = var2 != null ? var2 : "Noname";
   }

   public void setCondition(ConditionContainer var1) {
      this.condition = var1;
   }

   public void addScriptEntry(RadioScriptEntry var1) {
      if (var1 != null) {
         this.scriptEntries.add(var1);
      } else {
         DebugLog.log(DebugType.Radio, "Error trying to add 'null' scriptentry in script: " + this.parentScript + ", exitoption: " + this.name);
      }

   }

   public RadioScriptEntry evaluate() {
      CompareResult var1 = CompareResult.True;
      if (this.condition != null) {
         var1 = this.condition.Evaluate();
      }

      if (var1.equals(CompareResult.True)) {
         if (this.scriptEntries != null && this.scriptEntries.size() > 0) {
            int var2 = Rand.Next(100);
            Iterator var3 = this.scriptEntries.iterator();

            while(var3.hasNext()) {
               RadioScriptEntry var4 = (RadioScriptEntry)var3.next();
               if (var4 != null) {
                  System.out.println("ScriptEntry " + var4.getScriptName());
                  System.out.println("Chance: " + var2 + " Min: " + var4.getChanceMin() + " Max: " + var4.getChanceMax());
                  if (var2 >= var4.getChanceMin() && var2 < var4.getChanceMax()) {
                     return var4;
                  }
               }
            }
         }
      } else if (var1.equals(CompareResult.Invalid)) {
         System.out.println("Error occured evaluating condition: " + this.parentScript + ", exitoption: " + this.name);
         DebugLog.log(DebugType.Radio, "Error occured evaluating condition: " + this.parentScript + ", exitoption: " + this.name);
      }

      return null;
   }
}
