package fmod.fmod;

public final class FMOD_STUDIO_PARAMETER_DESCRIPTION {
   public final String name;
   public final FMOD_STUDIO_PARAMETER_ID id;
   public final int flags;
   public final int globalIndex;

   public FMOD_STUDIO_PARAMETER_DESCRIPTION(String var1, FMOD_STUDIO_PARAMETER_ID var2, int var3, int var4) {
      this.name = var1;
      this.id = var2;
      this.flags = var3;
      this.globalIndex = var4;
   }

   public boolean isGlobal() {
      return (this.flags & FMOD_STUDIO_PARAMETER_FLAGS.FMOD_STUDIO_PARAMETER_GLOBAL.bit) != 0;
   }
}
