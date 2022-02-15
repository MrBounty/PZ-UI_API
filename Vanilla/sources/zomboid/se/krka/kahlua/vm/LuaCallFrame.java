package se.krka.kahlua.vm;

import java.util.ArrayList;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.utils.HashMap;

public class LuaCallFrame {
   private final Platform platform;
   public final Coroutine coroutine;
   public LuaClosure closure;
   public JavaFunction javaFunction;
   public int pc;
   public int localBase;
   int returnBase;
   public int nArguments;
   boolean fromLua;
   public boolean canYield;
   boolean restoreTop;
   public int localsAssigned = 0;
   public HashMap LocalVarToStackMap = new HashMap();
   public HashMap LocalStackToVarMap = new HashMap();
   public ArrayList LocalVarNames = new ArrayList();

   public LuaCallFrame(Coroutine var1) {
      this.coroutine = var1;
      this.platform = var1.getPlatform();
   }

   public String getFilename() {
      return this.closure != null ? this.closure.prototype.filename : null;
   }

   public final void set(int var1, Object var2) {
      this.coroutine.objectStack[this.localBase + var1] = var2;
   }

   public final Object get(int var1) {
      return this.coroutine.objectStack[this.localBase + var1];
   }

   public int push(Object var1) {
      int var2 = this.getTop();
      this.setTop(var2 + 1);
      this.set(var2, var1);
      return 1;
   }

   public int push(Object var1, Object var2) {
      int var3 = this.getTop();
      this.setTop(var3 + 2);
      this.set(var3, var1);
      this.set(var3 + 1, var2);
      return 2;
   }

   public int pushNil() {
      return this.push((Object)null);
   }

   public final void stackCopy(int var1, int var2, int var3) {
      this.coroutine.stackCopy(this.localBase + var1, this.localBase + var2, var3);
   }

   public void stackClear(int var1, int var2) {
      while(var1 <= var2) {
         this.coroutine.objectStack[this.localBase + var1] = null;
         ++var1;
      }

   }

   public void clearFromIndex(int var1) {
      if (this.getTop() < var1) {
         this.setTop(var1);
      }

      this.stackClear(var1, this.getTop() - 1);
   }

   public final void setTop(int var1) {
      this.coroutine.setTop(this.localBase + var1);
   }

   public void closeUpvalues(int var1) {
      this.coroutine.closeUpvalues(this.localBase + var1);
   }

   public UpValue findUpvalue(int var1) {
      return this.coroutine.findUpvalue(this.localBase + var1);
   }

   public int getTop() {
      return this.coroutine.getTop() - this.localBase;
   }

   public void init() {
      if (this.isLua()) {
         this.pc = 0;
         if (this.closure.prototype.isVararg) {
            this.localBase += this.nArguments;
            this.setTop(this.closure.prototype.maxStacksize);
            int var1 = Math.min(this.nArguments, this.closure.prototype.numParams);
            this.stackCopy(-this.nArguments, 0, var1);
         } else {
            this.setTop(this.closure.prototype.maxStacksize);
            this.stackClear(this.closure.prototype.numParams, this.nArguments);
         }
      }

   }

   public void setPrototypeStacksize() {
      if (this.isLua()) {
         this.setTop(this.closure.prototype.maxStacksize);
      }

   }

   public void pushVarargs(int var1, int var2) {
      int var3 = this.closure.prototype.numParams;
      int var4 = this.nArguments - var3;
      if (var4 < 0) {
         var4 = 0;
      }

      if (var2 == -1) {
         var2 = var4;
         this.setTop(var1 + var4);
      }

      if (var4 > var2) {
         var4 = var2;
      }

      this.stackCopy(-this.nArguments + var3, var1, var4);
      int var5 = var2 - var4;
      if (var5 > 0) {
         this.stackClear(var1 + var4, var1 + var2 - 1);
      }

   }

   public KahluaTable getEnvironment() {
      return this.isLua() ? this.closure.env : this.coroutine.environment;
   }

   public boolean isJava() {
      return !this.isLua();
   }

   public boolean isLua() {
      return this.closure != null;
   }

   public String toString2() {
      if (this.closure != null) {
         return this.closure.toString2(this.pc);
      } else {
         return this.javaFunction != null ? "Callframe at: " + this.javaFunction.toString() : super.toString();
      }
   }

   public String toString() {
      if (this.closure != null) {
         return "Callframe at: " + this.closure.toString();
      } else {
         return this.javaFunction != null ? "Callframe at: " + this.javaFunction.toString() : super.toString();
      }
   }

   public Platform getPlatform() {
      return this.platform;
   }

   void setup(LuaClosure var1, JavaFunction var2, int var3, int var4, int var5, boolean var6, boolean var7) {
      this.localBase = var3;
      this.returnBase = var4;
      this.nArguments = var5;
      this.fromLua = var6;
      this.canYield = var7;
      this.closure = var1;
      this.javaFunction = var2;
      LuaCallFrame var8 = this;
      this.localsAssigned = 0;
      this.LocalVarToStackMap.clear();
      this.LocalStackToVarMap.clear();
      this.LocalVarNames.clear();
      if (Core.bDebug && this != null && this.closure != null && this.getThread() == LuaManager.thread) {
         for(int var9 = var3; var9 < var3 + var5; ++var9) {
            int var10 = var8.closure.prototype.lines[0];
            if (var8.closure.prototype.locvarlines != null && var8.closure.prototype.locvarlines[var8.localsAssigned] < var10 && var8.closure.prototype.locvarlines[var8.localsAssigned] != 0) {
               int var11 = var8.localsAssigned++;
               String var12 = var8.closure.prototype.locvars[var11];
               if (var12.equals("group")) {
                  boolean var13 = false;
               }

               var8.setLocalVarToStack(var12, var9);
            }
         }
      }

   }

   public KahluaThread getThread() {
      return this.coroutine.getThread();
   }

   public LuaClosure getClosure() {
      return this.closure;
   }

   public void setLocalVarToStack(String var1, int var2) {
      this.LocalVarToStackMap.put(var1, var2);
      this.LocalStackToVarMap.put(var2, var1);
      this.LocalVarNames.add(var1);
   }

   public String getNameOfStack(int var1) {
      return this.LocalStackToVarMap.get(var1) instanceof String ? (String)this.LocalStackToVarMap.get(var1) : "";
   }

   public void printoutLocalVars() {
   }
}
