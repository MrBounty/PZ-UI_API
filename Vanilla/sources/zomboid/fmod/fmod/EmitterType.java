package fmod.fmod;

public enum EmitterType {
   Footstep,
   Voice,
   Extra;

   // $FF: synthetic method
   private static EmitterType[] $values() {
      return new EmitterType[]{Footstep, Voice, Extra};
   }
}
