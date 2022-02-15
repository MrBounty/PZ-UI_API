package zombie.radio.script;

public final class RadioScriptEntry {
   private int chanceMin;
   private int chanceMax;
   private String scriptName;
   private int Delay;

   public RadioScriptEntry(String var1, int var2) {
      this(var1, var2, 0, 100);
   }

   public RadioScriptEntry(String var1, int var2, int var3, int var4) {
      this.chanceMin = 0;
      this.chanceMax = 100;
      this.scriptName = "";
      this.Delay = 0;
      this.scriptName = var1;
      this.setChanceMin(var3);
      this.setChanceMax(var4);
      this.setDelay(var2);
   }

   public void setChanceMin(int var1) {
      this.chanceMin = var1 < 0 ? 0 : (var1 > 100 ? 100 : var1);
   }

   public int getChanceMin() {
      return this.chanceMin;
   }

   public void setChanceMax(int var1) {
      this.chanceMax = var1 < 0 ? 0 : (var1 > 100 ? 100 : var1);
   }

   public int getChanceMax() {
      return this.chanceMax;
   }

   public String getScriptName() {
      return this.scriptName;
   }

   public void setScriptName(String var1) {
      this.scriptName = var1;
   }

   public int getDelay() {
      return this.Delay;
   }

   public void setDelay(int var1) {
      this.Delay = var1 >= 0 ? var1 : 0;
   }
}
