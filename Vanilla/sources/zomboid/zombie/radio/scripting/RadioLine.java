package zombie.radio.scripting;

public final class RadioLine {
   private float r;
   private float g;
   private float b;
   private String text;
   private String effects;
   private float airTime;

   public RadioLine(String var1, float var2, float var3, float var4) {
      this(var1, var2, var3, var4, (String)null);
   }

   public RadioLine(String var1, float var2, float var3, float var4, String var5) {
      this.r = 1.0F;
      this.g = 1.0F;
      this.b = 1.0F;
      this.text = "<!text missing!>";
      this.effects = "";
      this.airTime = -1.0F;
      this.text = var1 != null ? var1 : this.text;
      this.r = var2;
      this.g = var3;
      this.b = var4;
      this.effects = var5 != null ? var5 : this.effects;
   }

   public float getR() {
      return this.r;
   }

   public float getG() {
      return this.g;
   }

   public float getB() {
      return this.b;
   }

   public String getText() {
      return this.text;
   }

   public String getEffectsString() {
      return this.effects;
   }

   public boolean isCustomAirTime() {
      return this.airTime > 0.0F;
   }

   public float getAirTime() {
      return this.airTime;
   }

   public void setAirTime(float var1) {
      this.airTime = var1;
   }

   public void setText(String var1) {
      this.text = var1;
   }
}
