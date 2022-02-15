package zombie.debug;

import zombie.config.BooleanConfigOption;
import zombie.core.Core;
import zombie.debug.options.IDebugOption;
import zombie.debug.options.IDebugOptionGroup;

public class BooleanDebugOption extends BooleanConfigOption implements IDebugOption {
   private IDebugOptionGroup m_parent;
   private final boolean m_debugOnly;

   public BooleanDebugOption(String var1, boolean var2, boolean var3) {
      super(var1, var3);
      this.m_debugOnly = var2;
   }

   public boolean getValue() {
      return !Core.bDebug && this.isDebugOnly() ? super.getDefaultValue() : super.getValue();
   }

   public boolean isDebugOnly() {
      return this.m_debugOnly;
   }

   public IDebugOptionGroup getParent() {
      return this.m_parent;
   }

   public void setParent(IDebugOptionGroup var1) {
      this.m_parent = var1;
   }
}
