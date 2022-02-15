package se.krka.kahlua.vm;

import java.util.ArrayList;
import zombie.Lua.LuaManager;
import zombie.core.Core;

public class Coroutine {
   private final Platform platform;
   private KahluaThread thread;
   private Coroutine parent;
   public KahluaTable environment;
   public String stackTrace;
   private final ArrayList liveUpvalues;
   private static final int MAX_STACK_SIZE = 3000;
   private static final int INITIAL_STACK_SIZE = 1000;
   private static final int MAX_CALL_FRAME_STACK_SIZE = 1000;
   private static final int INITIAL_CALL_FRAME_STACK_SIZE = 200;
   public Object[] objectStack;
   private int top;
   private LuaCallFrame[] callFrameStack;
   private int callFrameTop;

   public Coroutine() {
      this.stackTrace = "";
      this.liveUpvalues = new ArrayList();
      this.platform = null;
   }

   public Coroutine getParent() {
      return this.parent;
   }

   public Coroutine(Platform var1, KahluaTable var2, KahluaThread var3) {
      this.stackTrace = "";
      this.liveUpvalues = new ArrayList();
      this.platform = var1;
      this.environment = var2;
      this.thread = var3;
      this.objectStack = new Object[1000];
      this.callFrameStack = new LuaCallFrame[200];
   }

   public Coroutine(Platform var1, KahluaTable var2) {
      this(var1, var2, (KahluaThread)null);
   }

   public final LuaCallFrame pushNewCallFrame(LuaClosure var1, JavaFunction var2, int var3, int var4, int var5, boolean var6, boolean var7) {
      this.setCallFrameStackTop(this.callFrameTop + 1);
      LuaCallFrame var8 = this.currentCallFrame();
      var8.setup(var1, var2, var3, var4, var5, var6, var7);
      return var8;
   }

   public void popCallFrame() {
      if (this.isDead()) {
         throw new RuntimeException("Stack underflow");
      } else {
         this.setCallFrameStackTop(this.callFrameTop - 1);
      }
   }

   private final void ensureCallFrameStackSize(int var1) {
      if (var1 > 1000) {
         throw new RuntimeException("Stack overflow");
      } else {
         int var2 = this.callFrameStack.length;

         int var3;
         for(var3 = var2; var3 <= var1; var3 = 2 * var3) {
         }

         if (var3 > var2) {
            LuaCallFrame[] var4 = new LuaCallFrame[var3];
            System.arraycopy(this.callFrameStack, 0, var4, 0, var2);
            this.callFrameStack = var4;
         }

      }
   }

   public final void setCallFrameStackTop(int var1) {
      if (var1 > this.callFrameTop) {
         this.ensureCallFrameStackSize(var1);
      } else {
         this.callFrameStackClear(var1, this.callFrameTop - 1);
      }

      this.callFrameTop = var1;
   }

   private void callFrameStackClear(int var1, int var2) {
      for(; var1 <= var2; ++var1) {
         LuaCallFrame var3 = this.callFrameStack[var1];
         if (var3 != null) {
            this.callFrameStack[var1].closure = null;
            this.callFrameStack[var1].javaFunction = null;
         }
      }

   }

   private final void ensureStacksize(int var1) {
      if (var1 > 3000) {
         throw new RuntimeException("Stack overflow");
      } else {
         int var2 = this.objectStack.length;

         int var3;
         for(var3 = var2; var3 <= var1; var3 = 2 * var3) {
         }

         if (var3 > var2) {
            Object[] var4 = new Object[var3];
            System.arraycopy(this.objectStack, 0, var4, 0, var2);
            this.objectStack = var4;
         }

      }
   }

   public final void setTop(int var1) {
      if (this.top < var1) {
         this.ensureStacksize(var1);
      } else {
         this.stackClear(var1, this.top - 1);
      }

      this.top = var1;
   }

   public final void stackCopy(int var1, int var2, int var3) {
      if (var3 > 0 && var1 != var2) {
         System.arraycopy(this.objectStack, var1, this.objectStack, var2, var3);
         LuaCallFrame var4 = this.getParentNoAssert(1);
         if (Core.bDebug && var4 != null && var4.closure != null && var4.pc > 0) {
            for(int var5 = var2; var5 < var2 + var3; ++var5) {
               int var6 = var4.closure.prototype.lines[var4.pc - 1];
               boolean var7 = var4.closure.prototype.lines[var4.pc] != var6;
               if (this.thread == LuaManager.thread && var4.closure.prototype.locvarlines != null) {
                  while(var6 > var4.closure.prototype.locvarlines[var4.localsAssigned] && var4.closure.prototype.locvarlines[var4.localsAssigned] != 0) {
                     ++var4.localsAssigned;
                  }
               }

               if (var7 && this.thread == LuaManager.thread && var4.closure.prototype.locvarlines != null && var4.closure.prototype.locvarlines[var4.localsAssigned] == var6) {
                  int var8 = var4.localsAssigned++;
                  String var9 = var4.closure.prototype.locvars[var8];
                  var4.setLocalVarToStack(var9, var5);
               }
            }
         }
      }

   }

   public final void stackClear(int var1, int var2) {
      while(var1 <= var2) {
         this.objectStack[var1] = null;
         ++var1;
      }

   }

   public final void closeUpvalues(int var1) {
      int var2 = this.liveUpvalues.size();

      while(true) {
         --var2;
         if (var2 < 0) {
            return;
         }

         UpValue var3 = (UpValue)this.liveUpvalues.get(var2);
         if (var3.getIndex() < var1) {
            return;
         }

         var3.close();
         this.liveUpvalues.remove(var2);
      }
   }

   public final UpValue findUpvalue(int var1) {
      int var2 = this.liveUpvalues.size();

      UpValue var3;
      int var4;
      do {
         --var2;
         if (var2 < 0) {
            break;
         }

         var3 = (UpValue)this.liveUpvalues.get(var2);
         var4 = var3.getIndex();
         if (var4 == var1) {
            return var3;
         }
      } while(var4 >= var1);

      var3 = new UpValue(this, var1);
      this.liveUpvalues.add(var2 + 1, var3);
      return var3;
   }

   public Object getObjectFromStack(int var1) {
      return this.objectStack[var1];
   }

   public int getObjectStackSize() {
      return this.top;
   }

   public LuaCallFrame getParentCallframe() {
      int var1 = this.callFrameTop - 1;
      return var1 < 0 ? null : this.callFrameStack[var1];
   }

   public final LuaCallFrame currentCallFrame() {
      if (this.isDead()) {
         return null;
      } else {
         LuaCallFrame var1 = this.callFrameStack[this.callFrameTop - 1];
         if (var1 == null) {
            var1 = new LuaCallFrame(this);
            this.callFrameStack[this.callFrameTop - 1] = var1;
         }

         return var1;
      }
   }

   public int getTop() {
      return this.top;
   }

   public LuaCallFrame getParent(int var1) {
      KahluaUtil.luaAssert(var1 >= 0, "Level must be non-negative");
      int var2 = this.callFrameTop - var1 - 1;
      KahluaUtil.luaAssert(var2 >= 0, "Level too high");
      return this.callFrameStack[var2];
   }

   public LuaCallFrame getParentNoAssert(int var1) {
      int var2 = this.callFrameTop - var1 - 1;
      return var2 < 0 ? null : this.callFrameStack[var2];
   }

   public String getCurrentStackTrace(int var1, int var2, int var3) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 < 0) {
         var2 = 0;
      }

      StringBuilder var4 = new StringBuilder();

      for(int var5 = this.callFrameTop - 1 - var1; var5 >= var3 && var2-- > 0; --var5) {
         var4.append(this.getStackTrace(this.callFrameStack[var5]));
      }

      return var4.toString();
   }

   public void cleanCallFrames(LuaCallFrame var1) {
      while(true) {
         LuaCallFrame var2 = this.currentCallFrame();
         if (var2 == null || var2 == var1) {
            return;
         }

         this.addStackTrace(var2);
         this.popCallFrame();
      }
   }

   public void addStackTrace(LuaCallFrame var1) {
      String var10001 = this.stackTrace;
      this.stackTrace = var10001 + this.getStackTrace(var1);
   }

   private String getStackTrace(LuaCallFrame var1) {
      if (var1.isLua()) {
         int[] var2 = var1.closure.prototype.lines;
         if (var2 != null) {
            int var3 = var1.pc - 1;
            if (var3 >= 0 && var3 < var2.length) {
               return "at " + var1.closure.prototype + ":" + var2[var3] + "\n";
            }
         }

         return "";
      } else {
         return "at " + var1.javaFunction + "\n";
      }
   }

   public boolean isDead() {
      return this.callFrameTop == 0;
   }

   public Platform getPlatform() {
      return this.platform;
   }

   public String getStatus() {
      if (this.parent == null) {
         return this.isDead() ? "dead" : "suspended";
      } else {
         return "normal";
      }
   }

   public boolean atBottom() {
      return this.callFrameTop == 1;
   }

   public int getCallframeTop() {
      return this.callFrameTop;
   }

   public LuaCallFrame[] getCallframeStack() {
      return this.callFrameStack;
   }

   public LuaCallFrame getCallFrame(int var1) {
      if (var1 < 0) {
         var1 += this.callFrameTop;
      }

      return this.callFrameStack[var1];
   }

   public static void yieldHelper(LuaCallFrame var0, LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var0.canYield, "Can not yield outside of a coroutine");
      Coroutine var3 = var0.coroutine;
      KahluaThread var4 = var3.getThread();
      Coroutine var5 = var3.parent;
      KahluaUtil.luaAssert(var5 != null, "Internal error, coroutine must be running");
      KahluaUtil.luaAssert(var3 == var4.currentCoroutine, "Internal error, must yield current thread");
      var3.destroy();
      LuaCallFrame var6 = var5.currentCallFrame();
      int var7;
      if (var6 == null) {
         var5.setTop(var2 + 1);
         var5.objectStack[0] = Boolean.TRUE;

         for(var7 = 0; var7 < var2; ++var7) {
            var5.objectStack[var7 + 1] = var1.get(var7);
         }
      } else {
         var6.push(Boolean.TRUE);

         for(var7 = 0; var7 < var2; ++var7) {
            Object var8 = var1.get(var7);
            var6.push(var8);
         }
      }

      var4.currentCoroutine = var5;
   }

   public void resume(Coroutine var1) {
      this.parent = var1;
      this.thread = var1.thread;
   }

   public KahluaThread getThread() {
      return this.thread;
   }

   public void destroy() {
      this.parent = null;
      this.thread = null;
   }
}
