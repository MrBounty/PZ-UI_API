package zombie.radio.StorySounds;

import java.util.ArrayList;

public final class StorySoundEvent {
   protected String name;
   protected ArrayList eventSounds;

   public StorySoundEvent() {
      this("Unnamed");
   }

   public StorySoundEvent(String var1) {
      this.eventSounds = new ArrayList();
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public ArrayList getEventSounds() {
      return this.eventSounds;
   }

   public void setEventSounds(ArrayList var1) {
      this.eventSounds = var1;
   }
}
