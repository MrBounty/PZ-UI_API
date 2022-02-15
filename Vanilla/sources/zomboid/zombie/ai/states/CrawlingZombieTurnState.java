package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoDirections;
import zombie.iso.Vector2;

public final class CrawlingZombieTurnState extends State {
   private static final CrawlingZombieTurnState _instance = new CrawlingZombieTurnState();
   private static final Vector2 tempVector2_1 = new Vector2();
   private static final Vector2 tempVector2_2 = new Vector2();

   public static CrawlingZombieTurnState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      ((IsoZombie)var1).AllowRepathDelay = 0.0F;
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("TurnSome")) {
         Vector2 var3 = tempVector2_1.set(var1.dir.ToVector());
         Vector2 var4 = "left".equalsIgnoreCase(var2.m_ParameterValue) ? IsoDirections.fromIndex(var1.dir.index() + 1).ToVector() : IsoDirections.fromIndex(var1.dir.index() - 1).ToVector();
         Vector2 var5 = PZMath.lerp(tempVector2_2, var3, var4, var2.m_TimePc);
         var1.setForwardDirection(var5);
      } else {
         if (var2.m_EventName.equalsIgnoreCase("TurnComplete")) {
            if ("left".equalsIgnoreCase(var2.m_ParameterValue)) {
               var1.dir = IsoDirections.fromIndex(var1.dir.index() + 1);
            } else {
               var1.dir = IsoDirections.fromIndex(var1.dir.index() - 1);
            }

            var1.getVectorFromDirection(var1.getForwardDirection());
         }

      }
   }

   public static boolean calculateDir(IsoGameCharacter var0, IsoDirections var1) {
      if (var1.index() > var0.dir.index()) {
         return var1.index() - var0.dir.index() <= 4;
      } else {
         return var1.index() - var0.dir.index() < -4;
      }
   }
}
