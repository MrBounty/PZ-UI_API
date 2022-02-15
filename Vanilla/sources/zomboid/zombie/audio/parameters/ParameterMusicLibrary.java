package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.core.Core;

public final class ParameterMusicLibrary extends FMODGlobalParameter {
   public ParameterMusicLibrary() {
      super("MusicLibrary");
   }

   public float calculateCurrentValue() {
      float var10000;
      switch(Core.getInstance().getOptionMusicLibrary()) {
      case 2:
         var10000 = (float)ParameterMusicLibrary.Library.EarlyAccess.label;
         break;
      case 3:
         var10000 = (float)ParameterMusicLibrary.Library.Random.label;
         break;
      default:
         var10000 = (float)ParameterMusicLibrary.Library.Official.label;
      }

      return var10000;
   }

   public static enum Library {
      Official(0),
      EarlyAccess(1),
      Random(2);

      final int label;

      private Library(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterMusicLibrary.Library[] $values() {
         return new ParameterMusicLibrary.Library[]{Official, EarlyAccess, Random};
      }
   }
}
