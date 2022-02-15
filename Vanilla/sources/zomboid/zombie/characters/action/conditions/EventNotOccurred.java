package zombie.characters.action.conditions;

import org.w3c.dom.Element;
import zombie.characters.action.ActionContext;
import zombie.characters.action.IActionCondition;

public final class EventNotOccurred implements IActionCondition {
   public String eventName;

   public String getDescription() {
      return "EventNotOccurred(" + this.eventName + ")";
   }

   private boolean load(Element var1) {
      this.eventName = var1.getTextContent().toLowerCase();
      return true;
   }

   public boolean passes(ActionContext var1, int var2) {
      return !var1.hasEventOccurred(this.eventName, var2);
   }

   public IActionCondition clone() {
      return null;
   }

   public static class Factory implements IActionCondition.IFactory {
      public IActionCondition create(Element var1) {
         EventNotOccurred var2 = new EventNotOccurred();
         return var2.load(var1) ? var2 : null;
      }
   }
}
