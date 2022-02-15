package zombie.radio.StorySounds;

import java.util.ArrayList;
import zombie.core.Color;

public final class EventSound {
   protected String name;
   protected Color color;
   protected ArrayList dataPoints;
   protected ArrayList storySounds;

   public EventSound() {
      this("Unnamed");
   }

   public EventSound(String var1) {
      this.color = new Color(1.0F, 1.0F, 1.0F);
      this.dataPoints = new ArrayList();
      this.storySounds = new ArrayList();
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public ArrayList getDataPoints() {
      return this.dataPoints;
   }

   public void setDataPoints(ArrayList var1) {
      this.dataPoints = var1;
   }

   public ArrayList getStorySounds() {
      return this.storySounds;
   }

   public void setStorySounds(ArrayList var1) {
      this.storySounds = var1;
   }
}
