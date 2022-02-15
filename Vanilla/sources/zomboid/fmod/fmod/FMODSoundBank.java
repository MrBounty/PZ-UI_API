package fmod.fmod;

import fmod.javafmod;
import java.security.InvalidParameterException;
import java.util.HashMap;
import zombie.audio.BaseSoundBank;
import zombie.core.Core;

public class FMODSoundBank extends BaseSoundBank {
   public HashMap voiceMap = new HashMap();
   public HashMap footstepMap = new HashMap();

   private void check(String var1) {
      if (Core.bDebug && javafmod.FMOD_Studio_System_GetEvent("event:/" + var1) < 0L) {
         System.out.println("MISSING in .bank " + var1);
      }

   }

   public void addVoice(String var1, String var2, float var3) {
      FMODVoice var4 = new FMODVoice(var2, var3);
      this.voiceMap.put(var1, var4);
   }

   public void addFootstep(String var1, String var2, String var3, String var4, String var5) {
      FMODFootstep var6 = new FMODFootstep(var2, var3, var4, var5);
      this.footstepMap.put(var1, var6);
   }

   public FMODVoice getVoice(String var1) {
      return this.voiceMap.containsKey(var1) ? (FMODVoice)this.voiceMap.get(var1) : null;
   }

   public FMODFootstep getFootstep(String var1) {
      if (this.footstepMap.containsKey(var1)) {
         return (FMODFootstep)this.footstepMap.get(var1);
      } else {
         throw new InvalidParameterException("Footstep not loaded: " + var1);
      }
   }
}
