package se.krka.kahlua.require;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;

public class Require implements JavaFunction {
   private final LuaSourceProvider luaSourceProvider;

   public void install(KahluaTable var1) {
      var1.rawset("require", this);
      var1.rawset(this, new HashMap());
   }

   public Require(LuaSourceProvider var1) {
      this.luaSourceProvider = var1;
   }

   public int call(LuaCallFrame var1, int var2) {
      KahluaTable var3 = var1.getEnvironment();
      Map var4 = (Map)var1.getThread().tableget(var3, this);
      String var5 = KahluaUtil.getStringArg(var1, 1, "require");
      Require.Result var6 = (Require.Result)var4.get(var5);
      if (var6 == null) {
         this.setState(var4, var5, Require.Result.LOADING);
         Reader var7 = this.luaSourceProvider.getLuaSource(var5);
         if (var7 == null) {
            this.error(var4, var5, "Does not exist: " + var5);
         }

         try {
            LuaClosure var8 = LuaCompiler.loadis(var7, var5, var3);
            this.setState(var4, var5, Require.Result.LOADING);
            var1.getThread().call(var8, (Object)null, (Object)null, (Object)null);
            this.setState(var4, var5, Require.Result.LOADED);
            return 0;
         } catch (IOException var10) {
            this.error(var4, var5, "Error in: " + var5 + ": " + var10.getMessage());
         } catch (RuntimeException var11) {
            String var9 = "Error in: " + var5 + ": " + var11.getMessage();
            this.setState(var4, var5, Require.Result.error(var9));
            throw new RuntimeException(var9, var11);
         }
      }

      if (var6 == Require.Result.LOADING) {
         this.error(var4, var5, "Circular dependency found for: " + var5);
      }

      if (var6.state == Require.State.BROKEN) {
         KahluaUtil.fail(var6.errorMessage);
      }

      return 0;
   }

   private void error(Map var1, String var2, String var3) {
      this.setState(var1, var2, Require.Result.error(var3));
      KahluaUtil.fail(var3);
   }

   private void setState(Map var1, String var2, Require.Result var3) {
      var1.put(var2, var3);
   }

   private static class Result {
      public final String errorMessage;
      public final Require.State state;
      public static final Require.Result LOADING;
      public static final Require.Result LOADED;

      private Result(String var1, Require.State var2) {
         this.errorMessage = var1;
         this.state = var2;
      }

      public static Require.Result error(String var0) {
         return new Require.Result(var0, Require.State.BROKEN);
      }

      static {
         LOADING = new Require.Result((String)null, Require.State.LOADING);
         LOADED = new Require.Result((String)null, Require.State.LOADED);
      }
   }

   private static enum State {
      LOADING,
      LOADED,
      BROKEN;

      // $FF: synthetic method
      private static Require.State[] $values() {
         return new Require.State[]{LOADING, LOADED, BROKEN};
      }
   }
}
