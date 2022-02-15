package zombie.scripting.objects;

import zombie.scripting.ScriptManager;

public final class VehicleTemplate extends BaseScriptObject {
   public String name;
   public String body;
   public VehicleScript script;

   public VehicleTemplate(ScriptModule var1, String var2, String var3) {
      ScriptManager var4 = ScriptManager.instance;
      if (!var4.scriptsWithVehicleTemplates.contains(var4.currentFileName)) {
         var4.scriptsWithVehicleTemplates.add(var4.currentFileName);
      }

      this.module = var1;
      this.name = var2;
      this.body = var3;
   }

   public VehicleScript getScript() {
      if (this.script == null) {
         this.script = new VehicleScript();
         this.script.module = this.getModule();
         this.script.Load(this.name, this.body);
      }

      return this.script;
   }
}
