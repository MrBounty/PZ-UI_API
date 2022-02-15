package zombie.debug.options;

public interface IDebugOptionGroup extends IDebugOption {
   Iterable getChildren();

   void addChild(IDebugOption var1);

   void onChildAdded(IDebugOption var1);

   void onDescendantAdded(IDebugOption var1);
}
