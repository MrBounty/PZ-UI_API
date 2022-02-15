package fmod;

public class FMODSoundBuffer {
   private long sound;
   private byte[] buf1;
   private Long buf1size;
   private Long vadStatus;
   private Long loudness;
   private boolean intError;

   public FMODSoundBuffer(long var1) {
      this.sound = var1;
      this.buf1 = new byte[2048];
      this.buf1size = new Long(0L);
      this.vadStatus = new Long(0L);
      this.loudness = new Long(0L);
      this.intError = false;
   }

   public boolean pull(long var1) {
      int var3 = javafmod.FMOD_Sound_GetData(this.sound, this.buf1, this.buf1size, this.vadStatus, this.loudness);
      this.intError = var3 == -1;
      return var3 == 0;
   }

   public byte[] buf() {
      return this.buf1;
   }

   public long get_size() {
      return this.buf1size;
   }

   public long get_vad() {
      return this.vadStatus;
   }

   public long get_loudness() {
      return this.loudness;
   }

   public boolean get_interror() {
      return this.intError;
   }
}
