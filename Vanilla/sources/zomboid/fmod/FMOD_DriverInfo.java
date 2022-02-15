package fmod;

public class FMOD_DriverInfo {
   public int id;
   public String name;
   public int guid;
   public int systemrate;
   public int speakermode;
   public int speakerchannels;

   public FMOD_DriverInfo() {
      this.id = -1;
      this.name = "";
      this.guid = 0;
      this.systemrate = 8000;
      this.speakermode = 0;
      this.speakerchannels = 1;
   }

   public FMOD_DriverInfo(int var1, String var2) {
      this.id = var1;
      this.name = var2;
      this.guid = 0;
      this.systemrate = 8000;
      this.speakermode = 0;
      this.speakerchannels = 1;
   }
}
