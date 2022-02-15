package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.Coroutine;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;

public class CoroutineLib implements JavaFunction {
   private static final int CREATE = 0;
   private static final int RESUME = 1;
   private static final int YIELD = 2;
   private static final int STATUS = 3;
   private static final int RUNNING = 4;
   private static final int NUM_FUNCTIONS = 5;
   private static final String[] names = new String[5];
   private static final Class COROUTINE_CLASS = (new Coroutine()).getClass();
   private final int index;
   private static final CoroutineLib[] functions;

   public String toString() {
      String var10000 = names[this.index];
      return "coroutine." + var10000;
   }

   public CoroutineLib(int var1) {
      this.index = var1;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();

      for(int var3 = 0; var3 < 5; ++var3) {
         var2.rawset(names[var3], functions[var3]);
      }

      var2.rawset("__index", var2);
      KahluaTable var4 = KahluaUtil.getClassMetatables(var0, var1);
      var4.rawset(COROUTINE_CLASS, var2);
      var1.rawset("coroutine", var2);
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return this.create(var1, var2);
      case 1:
         return this.resume(var1, var2);
      case 2:
         return yield_int(var1, var2);
      case 3:
         return this.status(var1, var2);
      case 4:
         return this.running(var1, var2);
      default:
         return 0;
      }
   }

   private int running(LuaCallFrame var1, int var2) {
      Coroutine var3 = var1.coroutine;
      if (var3.getStatus() != "normal") {
         var3 = null;
      }

      return var1.push(var3);
   }

   private int status(LuaCallFrame var1, int var2) {
      Coroutine var3 = this.getCoroutine(var1, "status");
      return var1.coroutine == var3 ? var1.push("running") : var1.push(var3.getStatus());
   }

   private int resume(LuaCallFrame var1, int var2) {
      Coroutine var3 = this.getCoroutine(var1, "resume");
      String var4 = var3.getStatus();
      if (var4 != "suspended") {
         KahluaUtil.fail("Can not resume coroutine that is in status: " + var4);
      }

      Coroutine var5 = var1.coroutine;
      var3.resume(var5);
      LuaCallFrame var6 = var3.currentCallFrame();
      if (var6.nArguments == -1) {
         var6.setTop(0);
      }

      for(int var7 = 1; var7 < var2; ++var7) {
         var6.push(var1.get(var7));
      }

      if (var6.nArguments == -1) {
         var6.nArguments = var2 - 1;
         var6.init();
      }

      var1.getThread().currentCoroutine = var3;
      return 0;
   }

   private static int yield_int(LuaCallFrame var0, int var1) {
      Coroutine var2 = var0.coroutine;
      Coroutine var3 = var2.getParent();
      KahluaUtil.luaAssert(var3 != null, "Can not yield outside of a coroutine");
      LuaCallFrame var4 = var2.getCallFrame(-2);
      Coroutine.yieldHelper(var4, var0, var1);
      return 0;
   }

   private int create(LuaCallFrame var1, int var2) {
      LuaClosure var3 = this.getFunction(var1, "create");
      Coroutine var4 = new Coroutine(var1.getPlatform(), var1.getEnvironment());
      var4.pushNewCallFrame(var3, (JavaFunction)null, 0, 0, -1, true, true);
      var1.push(var4);
      return 1;
   }

   private LuaClosure getFunction(LuaCallFrame var1, String var2) {
      Object var3 = KahluaUtil.getArg(var1, 1, var2);
      KahluaUtil.luaAssert(var3 instanceof LuaClosure, "argument must be a lua function");
      LuaClosure var4 = (LuaClosure)var3;
      return var4;
   }

   private Coroutine getCoroutine(LuaCallFrame var1, String var2) {
      Object var3 = KahluaUtil.getArg(var1, 1, var2);
      KahluaUtil.luaAssert(var3 instanceof Coroutine, "argument must be a coroutine");
      Coroutine var4 = (Coroutine)var3;
      return var4;
   }

   static {
      names[0] = "create";
      names[1] = "resume";
      names[2] = "yield";
      names[3] = "status";
      names[4] = "running";
      functions = new CoroutineLib[5];

      for(int var0 = 0; var0 < 5; ++var0) {
         functions[var0] = new CoroutineLib(var0);
      }

   }
}
