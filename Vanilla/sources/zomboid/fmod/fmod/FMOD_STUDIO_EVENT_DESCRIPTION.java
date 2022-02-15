package fmod.fmod;

import java.util.ArrayList;

public final class FMOD_STUDIO_EVENT_DESCRIPTION {
   public final long address;
   public final String path;
   public final FMOD_GUID id;
   public final boolean bHasSustainPoints;
   public final long length;
   public final ArrayList parameters = new ArrayList();

   public FMOD_STUDIO_EVENT_DESCRIPTION(long var1, String var3, FMOD_GUID var4, boolean var5, long var6) {
      this.address = var1;
      this.path = var3;
      this.id = var4;
      this.bHasSustainPoints = var5;
      this.length = var6;
   }

   public boolean hasParameter(FMOD_STUDIO_PARAMETER_DESCRIPTION var1) {
      return this.parameters.contains(var1);
   }
}
