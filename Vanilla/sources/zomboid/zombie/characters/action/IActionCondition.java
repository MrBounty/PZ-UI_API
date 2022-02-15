package zombie.characters.action;

import java.util.HashMap;
import org.w3c.dom.Element;

public interface IActionCondition {
   HashMap s_factoryMap = new HashMap();

   String getDescription();

   boolean passes(ActionContext var1, int var2);

   IActionCondition clone();

   static IActionCondition createInstance(Element var0) {
      IActionCondition.IFactory var1 = (IActionCondition.IFactory)s_factoryMap.get(var0.getNodeName());
      return var1 != null ? var1.create(var0) : null;
   }

   static void registerFactory(String var0, IActionCondition.IFactory var1) {
      s_factoryMap.put(var0, var1);
   }

   public interface IFactory {
      IActionCondition create(Element var1);
   }
}
