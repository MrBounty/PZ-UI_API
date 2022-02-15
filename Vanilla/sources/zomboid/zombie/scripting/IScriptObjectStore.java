package zombie.scripting;

import zombie.scripting.objects.Item;
import zombie.scripting.objects.Recipe;

public interface IScriptObjectStore {
   Item getItem(String var1);

   Recipe getRecipe(String var1);
}
