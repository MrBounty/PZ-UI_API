package fmod.fmod;

import java.util.BitSet;
import zombie.audio.FMODParameterList;
import zombie.audio.GameSoundClip;

public interface IFMODParameterUpdater {
   FMODParameterList getFMODParameters();

   void startEvent(long var1, GameSoundClip var3, BitSet var4);

   void updateEvent(long var1, GameSoundClip var3);

   void stopEvent(long var1, GameSoundClip var3, BitSet var4);
}
