package zombie.radio.devices;

public final class PresetEntry {
   public String name = "New preset";
   public int frequency = 93200;

   public PresetEntry() {
   }

   public PresetEntry(String var1, int var2) {
      this.name = var1;
      this.frequency = var2;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public int getFrequency() {
      return this.frequency;
   }

   public void setFrequency(int var1) {
      this.frequency = var1;
   }
}
