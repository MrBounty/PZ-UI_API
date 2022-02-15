package zombie.debug.options;

public interface IDebugOption {
   String getName();

   IDebugOptionGroup getParent();

   void setParent(IDebugOptionGroup var1);
}
