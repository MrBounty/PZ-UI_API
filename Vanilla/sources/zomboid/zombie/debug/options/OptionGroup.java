package zombie.debug.options;

import java.util.ArrayList;
import zombie.debug.BooleanDebugOption;

public class OptionGroup implements IDebugOptionGroup {
   public final IDebugOptionGroup Group;
   private IDebugOptionGroup m_parentGroup;
   private final String m_groupName;
   private final ArrayList m_children = new ArrayList();

   public OptionGroup(String var1) {
      this.m_groupName = var1;
      this.Group = this;
   }

   public OptionGroup(IDebugOptionGroup var1, String var2) {
      this.m_groupName = getCombinedName(var1, var2);
      this.Group = this;
      var1.addChild(this);
   }

   public String getName() {
      return this.m_groupName;
   }

   public IDebugOptionGroup getParent() {
      return this.m_parentGroup;
   }

   public void setParent(IDebugOptionGroup var1) {
      this.m_parentGroup = var1;
   }

   public Iterable getChildren() {
      return this.m_children;
   }

   public void addChild(IDebugOption var1) {
      this.m_children.add(var1);
      var1.setParent(this);
      this.onChildAdded(var1);
   }

   public void onChildAdded(IDebugOption var1) {
      this.onDescendantAdded(var1);
   }

   public void onDescendantAdded(IDebugOption var1) {
      if (this.m_parentGroup != null) {
         this.m_parentGroup.onDescendantAdded(var1);
      }

   }

   public static BooleanDebugOption newOption(String var0, boolean var1) {
      return newOptionInternal((IDebugOptionGroup)null, var0, false, var1);
   }

   public static BooleanDebugOption newDebugOnlyOption(String var0, boolean var1) {
      return newOptionInternal((IDebugOptionGroup)null, var0, true, var1);
   }

   public static BooleanDebugOption newOption(IDebugOptionGroup var0, String var1, boolean var2) {
      return newOptionInternal(var0, var1, false, var2);
   }

   public static BooleanDebugOption newDebugOnlyOption(IDebugOptionGroup var0, String var1, boolean var2) {
      return newOptionInternal(var0, var1, true, var2);
   }

   private static BooleanDebugOption newOptionInternal(IDebugOptionGroup var0, String var1, boolean var2, boolean var3) {
      String var4 = getCombinedName(var0, var1);
      BooleanDebugOption var5 = new BooleanDebugOption(var4, var2, var3);
      if (var0 != null) {
         var0.addChild(var5);
      }

      return var5;
   }

   private static String getCombinedName(IDebugOptionGroup var0, String var1) {
      String var2;
      if (var0 != null) {
         var2 = String.format("%s.%s", var0.getName(), var1);
      } else {
         var2 = var1;
      }

      return var2;
   }
}
