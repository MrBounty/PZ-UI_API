package se.krka.kahlua.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.stdlib.CoroutineLib;
import se.krka.kahlua.stdlib.OsLib;
import se.krka.kahlua.stdlib.RandomLib;
import se.krka.kahlua.stdlib.StringLib;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.test.UserdataArray;
import se.krka.kahlua.threading.BlockingKahluaThread;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;

public class J2SEPlatform implements Platform {
   private static J2SEPlatform INSTANCE = new J2SEPlatform();

   public static J2SEPlatform getInstance() {
      return INSTANCE;
   }

   public double pow(double var1, double var3) {
      return Math.pow(var1, var3);
   }

   public KahluaTable newTable() {
      return new KahluaTableImpl(new LinkedHashMap());
   }

   public KahluaTable newEnvironment() {
      KahluaTable var1 = this.newTable();
      this.setupEnvironment(var1);
      return var1;
   }

   public void setupEnvironment(KahluaTable var1) {
      var1.wipe();
      var1.rawset("_G", var1);
      var1.rawset("_VERSION", "Kahlua kahlua.major.kahlua.minor.kahlua.fix for Lua lua.version (J2SE)");
      MathLib.register(this, var1);
      BaseLib.register(var1);
      RandomLib.register(this, var1);
      UserdataArray.register(this, var1);
      StringLib.register(this, var1);
      CoroutineLib.register(this, var1);
      OsLib.register(this, var1);
      TableLib.register(this, var1);
      LuaCompiler.register(var1);
      KahluaThread var2 = this.setupWorkerThread(var1);
      KahluaUtil.setupLibrary(var1, var2, "/stdlib");
      File var3 = (new File("serialize.lua")).getAbsoluteFile();

      try {
         FileInputStream var4 = new FileInputStream(var3);

         try {
            LuaClosure var5 = LuaCompiler.loadis((InputStream)var4, "serialize.lua", var1);
            var2.call(var5, (Object)null, (Object)null, (Object)null);
         } catch (Throwable var8) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var4.close();
      } catch (IOException var9) {
         throw new RuntimeException(var9);
      }
   }

   private KahluaThread setupWorkerThread(KahluaTable var1) {
      BlockingKahluaThread var2 = new BlockingKahluaThread(this, var1);
      KahluaUtil.setWorkerThread(var1, var2);
      return var2;
   }
}
