package fmod.fmod;

public enum FMOD_STUDIO_PARAMETER_FLAGS {
   FMOD_STUDIO_PARAMETER_READONLY(1),
   FMOD_STUDIO_PARAMETER_AUTOMATIC(2),
   FMOD_STUDIO_PARAMETER_GLOBAL(4),
   FMOD_STUDIO_PARAMETER_DISCRETE(8);

   public final int bit;

   private FMOD_STUDIO_PARAMETER_FLAGS(int var3) {
      this.bit = var3;
   }

   // $FF: synthetic method
   private static FMOD_STUDIO_PARAMETER_FLAGS[] $values() {
      return new FMOD_STUDIO_PARAMETER_FLAGS[]{FMOD_STUDIO_PARAMETER_READONLY, FMOD_STUDIO_PARAMETER_AUTOMATIC, FMOD_STUDIO_PARAMETER_GLOBAL, FMOD_STUDIO_PARAMETER_DISCRETE};
   }
}
