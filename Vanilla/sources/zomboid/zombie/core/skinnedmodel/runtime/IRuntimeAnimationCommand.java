package zombie.core.skinnedmodel.runtime;

import java.util.List;
import zombie.scripting.ScriptParser;

public interface IRuntimeAnimationCommand {
   void parse(ScriptParser.Block var1);

   void exec(List var1);
}
