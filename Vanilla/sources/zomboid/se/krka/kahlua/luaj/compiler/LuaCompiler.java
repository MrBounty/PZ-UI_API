package se.krka.kahlua.luaj.compiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.luaj.kahluafork.compiler.LexState;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;

public class LuaCompiler implements JavaFunction {
   public static boolean rewriteEvents = false;
   private final int index;
   private static final int LOADSTRING = 0;
   private static final int LOADSTREAM = 1;
   private static final String[] names = new String[]{"loadstring", "loadstream"};
   private static final LuaCompiler[] functions;

   private LuaCompiler(int var1) {
      this.index = var1;
   }

   public static void register(KahluaTable var0) {
      for(int var1 = 0; var1 < names.length; ++var1) {
         var0.rawset(names[var1], functions[var1]);
      }

   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return this.loadstring(var1, var2);
      case 1:
         return loadstream(var1, var2);
      default:
         return 0;
      }
   }

   public static int loadstream(LuaCallFrame var0, int var1) {
      try {
         KahluaUtil.luaAssert(var1 >= 2, "not enough arguments");
         Object var2 = var0.get(0);
         KahluaUtil.luaAssert(var2 != null, "No input given");
         String var3 = (String)var0.get(1);
         if (var2 instanceof Reader) {
            return var0.push(loadis((Reader)((Reader)var2), var3, (String)null, var0.getEnvironment()));
         } else if (var2 instanceof InputStream) {
            return var0.push(loadis((InputStream)((InputStream)var2), var3, (String)null, var0.getEnvironment()));
         } else {
            KahluaUtil.fail("Invalid type to loadstream: " + var2.getClass());
            return 0;
         }
      } catch (RuntimeException var4) {
         return var0.push((Object)null, var4.getMessage());
      } catch (IOException var5) {
         return var0.push((Object)null, var5.getMessage());
      }
   }

   private int loadstring(LuaCallFrame var1, int var2) {
      try {
         KahluaUtil.luaAssert(var2 >= 1, "not enough arguments");
         String var3 = (String)var1.get(0);
         KahluaUtil.luaAssert(var3 != null, "No source given");
         String var4 = null;
         if (var2 >= 2) {
            var4 = (String)var1.get(1);
         }

         return var1.push(loadstring(var3, var4, var1.getEnvironment()));
      } catch (RuntimeException var5) {
         return var1.push((Object)null, var5.getMessage());
      } catch (IOException var6) {
         return var1.push((Object)null, var6.getMessage());
      }
   }

   public static LuaClosure loadis(InputStream var0, String var1, KahluaTable var2) throws IOException {
      return loadis((InputStream)var0, var1, (String)null, var2);
   }

   public static LuaClosure loadis(Reader var0, String var1, KahluaTable var2) throws IOException {
      return loadis((Reader)var0, var1, (String)null, var2);
   }

   public static LuaClosure loadstring(String var0, String var1, KahluaTable var2) throws IOException {
      return loadis((InputStream)(new ByteArrayInputStream(var0.getBytes("UTF-8"))), var1, var0, var2);
   }

   private static LuaClosure loadis(Reader var0, String var1, String var2, KahluaTable var3) throws IOException {
      return new LuaClosure(LexState.compile(var0.read(), var0, var1, var2), var3);
   }

   private static LuaClosure loadis(InputStream var0, String var1, String var2, KahluaTable var3) throws IOException {
      return loadis((Reader)(new InputStreamReader(var0)), var1, var2, var3);
   }

   static {
      functions = new LuaCompiler[names.length];

      for(int var0 = 0; var0 < names.length; ++var0) {
         functions[var0] = new LuaCompiler(var0);
      }

   }
}
