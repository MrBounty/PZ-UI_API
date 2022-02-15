package zombie.ai.sadisticAIDirector;

import zombie.GameTime;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;

public final class SleepingEventData {
   protected int forceWakeUpTime = -1;
   protected boolean zombiesIntruders = true;
   protected int nightmareWakeUp = -1;
   protected IsoWindow weakestWindow = null;
   protected IsoDoor openDoor = null;
   protected boolean bRaining = false;
   protected boolean bWasRainingAtStart = false;
   protected double rainTimeStartHours = -1.0D;
   protected float sleepingTime = 8.0F;

   public void reset() {
      this.forceWakeUpTime = -1;
      this.zombiesIntruders = false;
      this.nightmareWakeUp = -1;
      this.openDoor = null;
      this.weakestWindow = null;
      this.bRaining = false;
      this.bWasRainingAtStart = false;
      this.rainTimeStartHours = -1.0D;
      this.sleepingTime = 8.0F;
   }

   public double getHoursSinceRainStarted() {
      return GameTime.getInstance().getWorldAgeHours() - this.rainTimeStartHours;
   }
}
