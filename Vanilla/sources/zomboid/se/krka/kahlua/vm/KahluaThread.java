package se.krka.kahlua.vm;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.stdlib.BaseLib;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.gameStates.IngameState;
import zombie.ui.UIManager;

public class KahluaThread {
   private static final int FIELDS_PER_FLUSH = 50;
   private static final int OP_MOVE = 0;
   private static final int OP_LOADK = 1;
   private static final int OP_LOADBOOL = 2;
   private static final int OP_LOADNIL = 3;
   private static final int OP_GETUPVAL = 4;
   private static final int OP_GETGLOBAL = 5;
   private static final int OP_GETTABLE = 6;
   private static final int OP_SETGLOBAL = 7;
   private static final int OP_SETUPVAL = 8;
   private static final int OP_SETTABLE = 9;
   private static final int OP_NEWTABLE = 10;
   private static final int OP_SELF = 11;
   private static final int OP_ADD = 12;
   private static final int OP_SUB = 13;
   private static final int OP_MUL = 14;
   private static final int OP_DIV = 15;
   private static final int OP_MOD = 16;
   private static final int OP_POW = 17;
   private static final int OP_UNM = 18;
   private static final int OP_NOT = 19;
   private static final int OP_LEN = 20;
   private static final int OP_CONCAT = 21;
   private static final int OP_JMP = 22;
   private static final int OP_EQ = 23;
   private static final int OP_LT = 24;
   private static final int OP_LE = 25;
   private static final int OP_TEST = 26;
   private static final int OP_TESTSET = 27;
   private static final int OP_CALL = 28;
   private static final int OP_TAILCALL = 29;
   private static final int OP_RETURN = 30;
   private static final int OP_FORLOOP = 31;
   private static final int OP_FORPREP = 32;
   private static final int OP_TFORLOOP = 33;
   private static final int OP_SETLIST = 34;
   private static final int OP_CLOSE = 35;
   private static final int OP_CLOSURE = 36;
   private static final int OP_VARARG = 37;
   private static final int MAX_INDEX_RECURSION = 100;
   private static final String[] meta_ops = new String[38];
   public static LuaCallFrame LastCallFrame;
   private final Coroutine rootCoroutine;
   public Coroutine currentCoroutine;
   private boolean doProfiling;
   private final PrintStream out;
   private final Platform platform;
   public boolean bStep;
   public String currentfile;
   public int currentLine;
   public int lastLine;
   public int lastCallFrame;
   public boolean bReset;
   public ArrayList profileEntries;
   public HashMap profileEntryMap;
   public static int m_error_count;
   public static final ArrayList m_errors_list;
   private final StringBuilder m_stringBuilder;
   private final StringWriter m_stringWriter;
   private final PrintWriter m_printWriter;
   HashMap BreakpointMap;
   HashMap BreakpointDataMap;
   HashMap BreakpointReadDataMap;
   public boolean bStepInto;

   public Coroutine getCurrentCoroutine() {
      return this.currentCoroutine;
   }

   public KahluaThread(Platform var1, KahluaTable var2) {
      this(System.out, var1, var2);
   }

   public KahluaThread(PrintStream var1, Platform var2, KahluaTable var3) {
      this.doProfiling = false;
      this.bStep = false;
      this.bReset = false;
      this.profileEntries = new ArrayList();
      this.profileEntryMap = new HashMap();
      this.m_stringBuilder = new StringBuilder();
      this.m_stringWriter = new StringWriter();
      this.m_printWriter = new PrintWriter(this.m_stringWriter);
      this.BreakpointMap = new HashMap();
      this.BreakpointDataMap = new HashMap();
      this.BreakpointReadDataMap = new HashMap();
      this.bStepInto = false;
      this.platform = var2;
      this.out = var1;
      this.rootCoroutine = new Coroutine(var2, var3, this);
      this.currentCoroutine = this.rootCoroutine;
   }

   public int call(int var1) {
      int var2 = this.currentCoroutine.getTop();
      int var3 = var2 - var1 - 1;
      Object var4 = this.currentCoroutine.objectStack[var3];
      if (var4 == null) {
         throw new RuntimeException("tried to call nil");
      } else {
         try {
            if (var4 instanceof JavaFunction) {
               return this.callJava((JavaFunction)var4, var3 + 1, var3, var1);
            }
         } catch (Exception var7) {
            String var10002 = var7.getClass().getName();
            throw new RuntimeException(var10002 + " " + var7.getMessage() + " in " + (JavaFunction)var4);
         }

         if (!(var4 instanceof LuaClosure)) {
            throw new RuntimeException("tried to call a non-function");
         } else {
            LuaCallFrame var5 = this.currentCoroutine.pushNewCallFrame((LuaClosure)var4, (JavaFunction)null, var3 + 1, var3, var1, false, false);
            var5.init();
            this.luaMainloop();
            int var6 = this.currentCoroutine.getTop() - var3;
            this.currentCoroutine.stackTrace = "";
            return var6;
         }
      }
   }

   private int callJava(JavaFunction var1, int var2, int var3, int var4) {
      Coroutine var5 = this.currentCoroutine;
      LuaCallFrame var6 = var5.pushNewCallFrame((LuaClosure)null, var1, var2, var3, var4, false, false);
      int var7 = var1.call(var6, var4);
      int var8 = var6.getTop();
      int var9 = var8 - var7;
      int var10 = var3 - var2;
      var6.stackCopy(var9, var10, var7);
      var6.setTop(var7 + var10);
      var5.popCallFrame();
      return var7;
   }

   private final Object prepareMetatableCall(Object var1) {
      if (!(var1 instanceof JavaFunction) && !(var1 instanceof LuaClosure)) {
         Object var2 = this.getMetaOp(var1, "__call");
         return var2;
      } else {
         return var1;
      }
   }

   public boolean isCurrent(String var1, int var2) {
      return var2 == this.currentLine;
   }

   private final void luaMainloop() {
      LuaCallFrame var1 = this.currentCoroutine.currentCallFrame();
      LuaClosure var2 = var1.closure;
      Prototype var3 = var2.prototype;
      int[] var4 = var3.code;
      int var5 = var1.returnBase;
      String var6 = "";
      long var7 = System.nanoTime();
      if (this.doProfiling && Core.bDebug && this == LuaManager.thread) {
         Coroutine var9 = this.getCurrentCoroutine();
         String var10 = var9.objectStack[0].toString();
         String var10000 = var9.getThread().currentfile;
         String var11 = var10000 + " " + var10.substring(0, var10.indexOf(":"));
         var6 = var11;
      }

      boolean var27 = true;

      label938:
      do {
         if (this.bReset) {
            long var54 = System.nanoTime();
            this.DoProfileTiming(var6, var7, var54);
            return;
         }

         if (Core.bDebug && this == LuaManager.thread) {
            Coroutine var28 = this.getCurrentCoroutine();
            if (var28 != null) {
               this.lastLine = this.currentLine;
               LuaCallFrame var30 = var28.currentCallFrame();
               if (var30.closure != null) {
                  this.currentfile = var30.closure.prototype.filename;
                  this.currentLine = var30.closure.prototype.lines[var30.pc];
                  if (this.bStep && this.currentLine != this.lastLine) {
                     if (this.bStepInto) {
                        this.bStep = false;
                        UIManager.debugBreakpoint(var30.closure.prototype.filename, (long)this.currentLine - 1L);
                        this.lastCallFrame = var28.getCallframeTop();
                        var27 = true;
                     } else if (var28.getCallframeTop() <= this.lastCallFrame) {
                        this.bStep = false;
                        this.lastCallFrame = var28.getCallframeTop();
                        UIManager.debugBreakpoint(var30.closure.prototype.filename, (long)this.currentLine - 1L);
                        var27 = true;
                     }
                  }

                  if (this.BreakpointMap.containsKey(var30.closure.prototype.filename)) {
                     ArrayList var12 = (ArrayList)this.BreakpointMap.get(var30.closure.prototype.filename);
                     if (var12.contains((long)var30.closure.prototype.lines[var30.pc]) && (var30.pc == 0 || var30.closure.prototype.lines[var30.pc - 1] != var30.closure.prototype.lines[var30.pc])) {
                        UIManager.debugBreakpoint(var30.closure.prototype.filename, (long)var30.closure.prototype.lines[var30.pc]);
                     }
                  }
               }
            }
         }

         var27 = true;

         try {
            if (this.bStep) {
               boolean var35 = false;
            }

            int var36 = var4[var1.pc++];
            int var14 = var36 & 63;
            int var17;
            int var18;
            Object var19;
            int var22;
            int var29;
            int var33;
            int var34;
            int var37;
            Object var38;
            double var39;
            int var43;
            double var44;
            Object var45;
            Double var46;
            Object var47;
            Object var48;
            double var49;
            int var51;
            boolean var52;
            boolean var53;
            String var55;
            Object var56;
            long var57;
            int var58;
            String var59;
            String var60;
            Object var64;
            UpValue var69;
            boolean var76;
            switch(var14) {
            case 0:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var1.set(var29, var1.get(var34));
               var27 = false;
               break;
            case 1:
               var29 = getA8(var36);
               var34 = getBx(var36);
               if (Core.bDebug) {
                  var37 = var1.closure.prototype.lines[var1.pc - 1];
                  var76 = var1.closure.prototype.lines[var1.pc] != var37;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var37 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var76 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var37) {
                     var17 = var1.localsAssigned++;
                     var55 = var1.closure.prototype.locvars[var17];
                     var1.setLocalVarToStack(var55, var1.localBase + var29);
                  }
               }

               var1.set(var29, var3.constants[var34]);
               break;
            case 2:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               Boolean var74 = var34 == 0 ? Boolean.FALSE : Boolean.TRUE;
               if (Core.bDebug) {
                  var43 = var1.closure.prototype.lines[var1.pc - 1];
                  var53 = var1.closure.prototype.lines[var1.pc] != var43;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var43 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var53 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var43) {
                     var18 = var1.localsAssigned++;
                     var59 = var1.closure.prototype.locvars[var18];
                     if (var59.equals("group")) {
                        var52 = false;
                     }

                     var1.setLocalVarToStack(var59, var1.localBase + var29);
                  }
               }

               var1.set(var29, var74);
               if (var33 != 0) {
                  ++var1.pc;
               }
               break;
            case 3:
               var29 = getA8(var36);
               var34 = getB9(var36);
               if (Core.bDebug) {
                  var37 = var1.closure.prototype.lines[var1.pc - 1];
                  var76 = var1.closure.prototype.lines[var1.pc] != var37;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var37 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var76 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var37) {
                     var17 = var1.localsAssigned++;
                     var55 = var1.closure.prototype.locvars[var17];
                     var1.setLocalVarToStack(var55, var1.localBase + var29);
                  }
               }

               var1.stackClear(var29, var34);
               break;
            case 4:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var69 = var2.upvalues[var34];
               if (Core.bDebug) {
                  var43 = var1.closure.prototype.lines[var1.pc - 1];
                  var53 = var1.closure.prototype.lines[var1.pc] != var43;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var43 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var53 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var43) {
                     var18 = var1.localsAssigned++;
                     var59 = var1.closure.prototype.locvars[var18];
                     if (var59.equals("group")) {
                        var52 = false;
                     }

                     var1.setLocalVarToStack(var59, var1.localBase + var29);
                  }
               }

               var1.set(var29, var69.getValue());
               break;
            case 5:
               var29 = getA8(var36);
               var34 = getBx(var36);
               var38 = this.tableget(var2.env, var3.constants[var34]);
               if (Core.bDebug) {
                  var43 = var1.closure.prototype.lines[var1.pc - 1];
                  var53 = var1.closure.prototype.lines[var1.pc] != var43;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var43 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var53 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var43) {
                     var18 = var1.localsAssigned++;
                     var59 = var1.closure.prototype.locvars[var18];
                     if (var59.equals("group")) {
                        var52 = false;
                     }

                     var1.setLocalVarToStack(var59, var1.localBase + var29);
                  }
               }

               var1.set(var29, var38);
               break;
            case 6:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = var1.get(var34);
               var48 = this.getRegisterOrConstant(var1, var33, var3);
               var45 = this.tableget(var38, var48);
               if (Core.bDebug) {
                  var18 = var1.closure.prototype.lines[var1.pc - 1];
                  boolean var73 = var1.closure.prototype.lines[var1.pc] != var18;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var18 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var73 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var18) {
                     var58 = var1.localsAssigned++;
                     String var72 = var1.closure.prototype.locvars[var58];
                     var1.setLocalVarToStack(var72, var1.localBase + var29);
                  }
               }

               var1.set(var29, var45);
               break;
            case 7:
               var29 = getA8(var36);
               var34 = getBx(var36);
               var38 = var1.get(var29);
               var48 = var3.constants[var34];
               if (var38 instanceof LuaClosure && var48 instanceof String) {
                  ((LuaClosure)var38).debugName = var48.toString();
               }

               if (LuaCompiler.rewriteEvents) {
                  var45 = var2.env.rawget(var48);
                  if (var45 instanceof KahluaTable && var45 != var38) {
                     KahluaTableImpl var66 = (KahluaTableImpl)var45;
                     var66.setRewriteTable(var38);
                  }

                  this.tableSet(var2.env, var48, var38);
               } else {
                  this.tableSet(var2.env, var48, var38);
               }
               break;
            case 8:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var69 = var2.upvalues[var34];
               if (Core.bDebug) {
                  var43 = var1.closure.prototype.lines[var1.pc - 1];
                  var53 = var1.closure.prototype.lines[var1.pc] != var43;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var43 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var53 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var43) {
                     var18 = var1.localsAssigned++;
                     var59 = var1.closure.prototype.locvars[var18];
                     var1.setLocalVarToStack(var59, var1.localBase + var29);
                  }
               }

               var69.setValue(var1.get(var29));
               break;
            case 9:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = var1.get(var29);
               var48 = this.getRegisterOrConstant(var1, var34, var3);
               var45 = this.getRegisterOrConstant(var1, var33, var3);
               this.tableSet(var38, var48, var45);
               break;
            case 10:
               var29 = getA8(var36);
               KahluaTable var67 = this.platform.newTable();
               if (Core.bDebug) {
                  var43 = var1.closure.prototype.lines[var1.pc - 1];
                  var53 = var1.closure.prototype.lines[var1.pc] != var43;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var43 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var53 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var43) {
                     var18 = var1.localsAssigned++;
                     var59 = var1.closure.prototype.locvars[var18];
                     var1.setLocalVarToStack(var59, var1.localBase + var29);
                  }
               }

               var1.set(var29, var67);
               break;
            case 11:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = this.getRegisterOrConstant(var1, var33, var3);
               var48 = var1.get(var34);
               LastCallFrame = var1;
               var45 = this.tableget(var48, var38);
               var1.set(var29, var45);
               var1.set(var29 + 1, var48);
               var27 = false;
               break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = this.getRegisterOrConstant(var1, var34, var3);
               var48 = this.getRegisterOrConstant(var1, var33, var3);
               var45 = null;
               var46 = null;
               var19 = null;
               Double var75;
               if ((var75 = KahluaUtil.rawTonumber(var38)) != null && (var46 = KahluaUtil.rawTonumber(var48)) != null) {
                  var19 = this.primitiveMath(var75, var46, var14);
               } else {
                  String var62 = meta_ops[var14];
                  var64 = this.getBinMetaOp(var38, var48, var62);
                  if (var64 == null) {
                     this.doStacktraceProper(var1);
                     String var63 = "unknown";
                     if (var2.debugName != null) {
                        var63 = var2.debugName;
                     } else if (var3.name != null) {
                        var63 = var3.name;
                     }

                     KahluaUtil.fail(var62 + " not defined for operands in " + var63);
                  }

                  var19 = this.call(var64, var38, var48, (Object)null);
               }

               if (Core.bDebug) {
                  var58 = var1.closure.prototype.lines[var1.pc - 1];
                  boolean var71 = var1.closure.prototype.lines[var1.pc] != var58;
                  if (this == LuaManager.thread && var1.closure.prototype.locvarlines != null) {
                     while(var58 > var1.closure.prototype.locvarlines[var1.localsAssigned] && var1.closure.prototype.locvarlines[var1.localsAssigned] != 0) {
                        ++var1.localsAssigned;
                     }
                  }

                  if (var71 && this == LuaManager.thread && var1.closure.prototype.locvarlines != null && var1.closure.prototype.locvarlines[var1.localsAssigned] == var58) {
                     var22 = var1.localsAssigned++;
                     String var23 = var1.closure.prototype.locvars[var22];
                     var1.setLocalVarToStack(var23, var1.localBase + var29);
                  }
               }

               var1.set(var29, var19);
               break;
            case 18:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var38 = var1.get(var34);
               Double var65 = KahluaUtil.rawTonumber(var38);
               if (var65 != null) {
                  var45 = KahluaUtil.toDouble(-KahluaUtil.fromDouble(var65));
               } else {
                  var47 = this.getMetaOp(var38, "__unm");
                  var45 = this.call(var47, var38, (Object)null, (Object)null);
               }

               var1.set(var29, var45);
               break;
            case 19:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var38 = var1.get(var34);
               var1.set(var29, KahluaUtil.toBoolean(!KahluaUtil.boolEval(var38)));
               var27 = false;
               break;
            case 20:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var38 = var1.get(var34);
               if (var38 instanceof KahluaTable) {
                  KahluaTable var70 = (KahluaTable)var38;
                  var48 = KahluaUtil.toDouble((long)var70.len());
               } else if (var38 instanceof String) {
                  var60 = (String)var38;
                  var48 = KahluaUtil.toDouble((long)var60.length());
               } else {
                  var45 = this.getMetaOp(var38, "__len");
                  if (var45 == null) {
                     this.doStacktraceProper(var1);
                  }

                  KahluaUtil.luaAssert(var45 != null, "__len not defined for operand");
                  var48 = this.call(var45, var38, (Object)null, (Object)null);
               }

               var1.set(var29, var48);
               var27 = false;
               break;
            case 21:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var37 = var34;
               var45 = var1.get(var33);
               var43 = var33 - 1;

               while(var37 <= var43) {
                  var55 = KahluaUtil.rawTostring(var45);
                  if (var55 != null) {
                     var51 = 0;

                     for(var58 = var43; var37 <= var58; ++var51) {
                        var64 = var1.get(var58);
                        --var58;
                        if (KahluaUtil.rawTostring(var64) == null) {
                           break;
                        }
                     }

                     if (var51 > 0) {
                        StringBuilder var68 = new StringBuilder();

                        for(var22 = var43 - var51 + 1; var22 <= var43; ++var22) {
                           var68.append(KahluaUtil.rawTostring(var1.get(var22)));
                        }

                        var68.append(var55);
                        var45 = var68.toString();
                        var43 -= var51;
                     }
                  }

                  if (var37 <= var43) {
                     var47 = var1.get(var43);
                     var19 = this.getBinMetaOp(var47, var45, "__concat");
                     if (var19 == null) {
                        KahluaUtil.fail("__concat not defined for operands: " + var47 + " and " + var45);
                     }

                     var45 = this.call(var19, var47, var45, (Object)null);
                     --var43;
                  }
               }

               var1.set(var29, var45);
               var27 = false;
               break;
            case 22:
               var1.pc += getSBx(var36);
               break;
            case 23:
            case 24:
            case 25:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = this.getRegisterOrConstant(var1, var34, var3);
               var48 = this.getRegisterOrConstant(var1, var33, var3);
               if (var38 instanceof Double && var48 instanceof Double) {
                  var44 = KahluaUtil.fromDouble(var38);
                  var49 = KahluaUtil.fromDouble(var48);
                  if (var14 == 23) {
                     if (var44 == var49 == (var29 == 0)) {
                        ++var1.pc;
                     }
                  } else if (var14 == 24) {
                     if (var44 < var49 == (var29 == 0)) {
                        ++var1.pc;
                     }
                  } else if (var44 <= var49 == (var29 == 0)) {
                     ++var1.pc;
                  }
               } else if (var38 instanceof String && var48 instanceof String) {
                  if (var14 == 23) {
                     if (var38.equals(var48) == (var29 == 0)) {
                        ++var1.pc;
                     }
                  } else {
                     var60 = (String)var38;
                     var55 = (String)var48;
                     var51 = var60.compareTo(var55);
                     if (var14 == 24) {
                        if (var51 < 0 == (var29 == 0)) {
                           ++var1.pc;
                        }
                     } else if (var51 <= 0 == (var29 == 0)) {
                        ++var1.pc;
                     }
                  }
               } else {
                  if (var38 == var48 && var14 == 23) {
                     var53 = true;
                  } else {
                     boolean var50 = false;
                     var59 = meta_ops[var14];
                     var56 = this.getCompMetaOp(var38, var48, var59);
                     if (var56 == null && var14 == 25) {
                        var56 = this.getCompMetaOp(var38, var48, "__lt");
                        var64 = var38;
                        var38 = var48;
                        var48 = var64;
                        var50 = true;
                     }

                     if (var56 == null && var14 == 23) {
                        var53 = BaseLib.luaEquals(var38, var48);
                     } else {
                        if (var56 == null) {
                           this.doStacktraceProper(var1);
                           KahluaUtil.fail(var59 + " not defined for operand");
                        }

                        var64 = this.call(var56, var38, var48, (Object)null);
                        var53 = KahluaUtil.boolEval(var64);
                     }

                     if (var50) {
                        var53 = !var53;
                     }
                  }

                  if (var53 == (var29 == 0)) {
                     ++var1.pc;
                  }
               }

               var27 = false;
               break;
            case 26:
               var29 = getA8(var36);
               var33 = getC9(var36);
               var38 = var1.get(var29);
               if (KahluaUtil.boolEval(var38) == (var33 == 0)) {
                  ++var1.pc;
               }
               break;
            case 27:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var38 = var1.get(var34);
               if (KahluaUtil.boolEval(var38) != (var33 == 0)) {
                  var1.set(var29, var38);
               } else {
                  ++var1.pc;
               }
               break;
            case 28:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               var37 = var34 - 1;
               if (var37 != -1) {
                  var1.setTop(var29 + var37 + 1);
               } else {
                  var37 = var1.getTop() - var29 - 1;
               }

               var1.restoreTop = var33 != 0;
               var43 = var1.localBase;
               var17 = var43 + var29 + 1;
               var18 = var43 + var29;
               var19 = var1.get(var29);
               if (var19 == null) {
                  var52 = false;
                  var19 = var1.get(var29);
               }

               if (var19 == null) {
                  this.doStacktraceProper(var1);
                  if (var1.getClosure().debugName != null) {
                     KahluaUtil.fail("Object tried to call nil in " + var1.getClosure().debugName);
                  } else if (var1.getClosure().prototype != null && var1.getClosure().prototype.name != null) {
                     KahluaUtil.fail("Object tried to call nil in " + var1.getClosure().prototype.name);
                  } else {
                     KahluaUtil.fail("Object tried to call nil in unknown");
                  }
               }

               var56 = this.prepareMetatableCall(var19);
               if (var56 == null) {
                  KahluaUtil.fail("Object " + var19 + " did not have __call metatable set");
               }

               if (var56 != var19) {
                  var17 = var18;
                  ++var37;
               }

               if (var56 instanceof LuaClosure) {
                  LuaCallFrame var61 = this.currentCoroutine.pushNewCallFrame((LuaClosure)var56, (JavaFunction)null, var17, var18, var37, true, var1.canYield);
                  var61.init();
                  var1 = var61;
                  var2 = var61.closure;
                  var3 = var2.prototype;
                  var4 = var3.code;
                  var5 = var61.returnBase;
                  break;
               } else {
                  if (!(var56 instanceof JavaFunction)) {
                     throw new RuntimeException("Tried to call a non-function: " + var56);
                  }

                  this.callJava((JavaFunction)var56, var17, var18, var37);
                  var1 = this.currentCoroutine.currentCallFrame();
                  if (var1 != null && !var1.isJava()) {
                     var2 = var1.closure;
                     var3 = var2.prototype;
                     var4 = var3.code;
                     var5 = var1.returnBase;
                     if (var1.restoreTop) {
                        var1.setTop(var3.maxStacksize);
                     }
                     break;
                  }

                  var57 = System.nanoTime();
                  return;
               }
            case 29:
               var37 = var1.localBase;
               this.currentCoroutine.closeUpvalues(var37);
               var29 = getA8(var36);
               var34 = getB9(var36);
               var43 = var34 - 1;
               if (var43 == -1) {
                  var43 = var1.getTop() - var29 - 1;
               }

               var1.restoreTop = false;
               var45 = var1.get(var29);

               try {
                  KahluaUtil.luaAssert(var45 != null, "Tried to call nil");
               } catch (Exception var24) {
                  if (Core.bDebug && UIManager.defaultthread == LuaManager.thread) {
                     UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.thread.currentLine - 1));
                  }

                  this.debugException(var24);
                  this.doStacktraceProper(var1);
                  KahluaUtil.fail("");
               }

               var47 = this.prepareMetatableCall(var45);
               if (var47 == null) {
                  KahluaUtil.fail("Object did not have __call metatable set");
               }

               var51 = var5 + 1;
               if (var47 != var45) {
                  var51 = var5;
                  ++var43;
               }

               this.currentCoroutine.stackCopy(var37 + var29, var5, var43 + 1);
               this.currentCoroutine.setTop(var5 + var43 + 1);
               if (var47 instanceof LuaClosure) {
                  var1.localBase = var51;
                  var1.nArguments = var43;
                  var1.closure = (LuaClosure)var47;
                  var1.init();
               } else {
                  if (!(var47 instanceof JavaFunction)) {
                     KahluaUtil.fail("Tried to call a non-function: " + var47);
                  }

                  Coroutine var20 = this.currentCoroutine;
                  this.callJava((JavaFunction)var47, var51, var5, var43);
                  var1 = this.currentCoroutine.currentCallFrame();
                  var20.popCallFrame();
                  if (var20 != this.currentCoroutine) {
                     if (var20.isDead() && var20 != this.rootCoroutine && this.currentCoroutine.getParent() == var20) {
                        this.currentCoroutine.resume(var20.getParent());
                        var20.destroy();
                        this.currentCoroutine.getParent().currentCallFrame().push(Boolean.TRUE);
                     }

                     var1 = this.currentCoroutine.currentCallFrame();
                     if (var1.isJava()) {
                        var57 = System.nanoTime();
                        return;
                     }
                  } else {
                     if (!var1.fromLua) {
                        var57 = System.nanoTime();
                        return;
                     }

                     var1 = this.currentCoroutine.currentCallFrame();
                     if (var1.restoreTop) {
                        var1.setTop(var1.closure.prototype.maxStacksize);
                     }
                  }
               }

               var2 = var1.closure;
               var3 = var2.prototype;
               var4 = var3.code;
               var5 = var1.returnBase;
               break;
            case 30:
               var29 = getA8(var36);
               var34 = getB9(var36) - 1;
               var37 = var1.localBase;
               this.currentCoroutine.closeUpvalues(var37);
               if (var34 == -1) {
                  var34 = var1.getTop() - var29;
               }

               this.currentCoroutine.stackCopy(var1.localBase + var29, var5, var34);
               this.currentCoroutine.setTop(var5 + var34);
               if (!var1.fromLua) {
                  this.currentCoroutine.popCallFrame();
                  long var42 = System.nanoTime();
                  return;
               }

               if (var1.canYield && this.currentCoroutine.atBottom()) {
                  var1.localBase = var1.returnBase;
                  Coroutine var41 = this.currentCoroutine;
                  Coroutine.yieldHelper(var1, var1, var34);
                  var41.popCallFrame();
                  var1 = this.currentCoroutine.currentCallFrame();
                  if (var1 == null || var1.isJava()) {
                     return;
                  }
               } else {
                  this.currentCoroutine.popCallFrame();
               }

               var1 = this.currentCoroutine.currentCallFrame();
               var2 = var1.closure;
               var3 = var2.prototype;
               var4 = var3.code;
               var5 = var1.returnBase;
               if (var1.restoreTop) {
                  var1.setTop(var3.maxStacksize);
               }
               break;
            case 31:
               Double var21;
               label728: {
                  var29 = getA8(var36);
                  var39 = KahluaUtil.fromDouble(var1.get(var29));
                  var44 = KahluaUtil.fromDouble(var1.get(var29 + 1));
                  var49 = KahluaUtil.fromDouble(var1.get(var29 + 2));
                  var39 += var49;
                  var21 = KahluaUtil.toDouble(var39);
                  var1.set(var29, var21);
                  if (var49 > 0.0D) {
                     if (var39 <= var44) {
                        break label728;
                     }
                  } else if (var39 >= var44) {
                     break label728;
                  }

                  var1.clearFromIndex(var29);
                  break;
               }

               var34 = getSBx(var36);
               var1.pc += var34;
               var1.set(var29 + 3, var21);
               break;
            case 32:
               var29 = getA8(var36);
               var34 = getSBx(var36);
               var39 = KahluaUtil.fromDouble(var1.get(var29));
               var44 = KahluaUtil.fromDouble(var1.get(var29 + 2));
               var1.set(var29, KahluaUtil.toDouble(var39 - var44));
               var1.pc += var34;
               break;
            case 33:
               var29 = getA8(var36);
               var33 = getC9(var36);
               var1.setTop(var29 + 6);
               var1.stackCopy(var29, var29 + 3, 3);
               this.call(2);
               var1.clearFromIndex(var29 + 3 + var33);
               var1.setPrototypeStacksize();
               var38 = var1.get(var29 + 3);
               if (var38 != null) {
                  var1.set(var29 + 2, var38);
               } else {
                  ++var1.pc;
               }
               break;
            case 34:
               var29 = getA8(var36);
               var34 = getB9(var36);
               var33 = getC9(var36);
               if (var34 == 0) {
                  var34 = var1.getTop() - var29 - 1;
               }

               if (var33 == 0) {
                  var33 = var4[var1.pc++];
               }

               var37 = (var33 - 1) * 50;
               KahluaTable var40 = (KahluaTable)var1.get(var29);
               var17 = 1;

               while(true) {
                  if (var17 > var34) {
                     continue label938;
                  }

                  var46 = KahluaUtil.toDouble((long)(var37 + var17));
                  var19 = var1.get(var29 + var17);
                  var40.rawset(var46, var19);
                  ++var17;
               }
            case 35:
               var29 = getA8(var36);
               var1.closeUpvalues(var29);
               break;
            case 36:
               var29 = getA8(var36);
               var34 = getBx(var36);
               Prototype var15 = var3.prototypes[var34];
               LuaClosure var16 = new LuaClosure(var15, var2.env);
               var1.set(var29, var16);
               var17 = var15.numUpvalues;
               var18 = 0;

               while(true) {
                  if (var18 >= var17) {
                     continue label938;
                  }

                  var36 = var4[var1.pc++];
                  var14 = var36 & 63;
                  var34 = getB9(var36);
                  switch(var14) {
                  case 0:
                     var16.upvalues[var18] = var1.findUpvalue(var34);
                     break;
                  case 4:
                     var16.upvalues[var18] = var2.upvalues[var34];
                  }

                  ++var18;
               }
            case 37:
               var29 = getA8(var36);
               var34 = getB9(var36) - 1;
               var1.pushVarargs(var29, var34);
            }
         } catch (RuntimeException var25) {
            if (Core.bDebug && UIManager.defaultthread == LuaManager.thread) {
            }

            if (var25.getMessage() != null) {
               ExceptionLogger.logException(var25);
               this.debugException(var25);
            }

            this.doStacktraceProper(var1);
            KahluaUtil.fail("");
            boolean var31 = true;

            do {
               var1 = this.currentCoroutine.currentCallFrame();
               if (var1 == null) {
                  Coroutine var32 = this.currentCoroutine.getParent();
                  if (var32 != null) {
                     LuaCallFrame var13 = var32.currentCallFrame();
                     var13.push(Boolean.FALSE);
                     var13.push(var25.getMessage());
                     var13.push(this.currentCoroutine.stackTrace);
                     this.currentCoroutine.destroy();
                     this.currentCoroutine = var32;
                     var1 = this.currentCoroutine.currentCallFrame();
                     var2 = var1.closure;
                     var3 = var2.prototype;
                     var4 = var3.code;
                     var5 = var1.returnBase;
                     var31 = false;
                  }
                  break;
               }

               this.currentCoroutine.addStackTrace(var1);
               this.currentCoroutine.popCallFrame();
            } while(var1.fromLua);

            if (var1 != null) {
               var1.closeUpvalues(0);
            }

            if (var31) {
               throw var25;
            }
         } catch (Exception var26) {
            if (Core.bDebug && UIManager.defaultthread == LuaManager.thread) {
               UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.thread.currentLine - 1));
            }

            if (var26.getMessage() != null) {
               System.out.printf(var26.getMessage());
            }
         }
      } while(!this.bReset);

      throw new RuntimeException("lua was reset");
   }

   private void DoProfileTiming(String var1, long var2, long var4) {
      if (this.doProfiling) {
         double var6 = (double)(var4 - var2) / 1000000.0D;
         if (GameWindow.states.current == IngameState.instance) {
            KahluaThread.Entry var8 = null;
            if (this.profileEntryMap.containsKey(var1)) {
               var8 = (KahluaThread.Entry)this.profileEntryMap.get(var1);
            } else {
               var8 = new KahluaThread.Entry();
               this.profileEntryMap.put(var1, var8);
               this.profileEntries.add(var8);
               var8.file = var1;
            }

            var8.time += var6;
            Collections.sort(this.profileEntries, new KahluaThread.ProfileEntryComparitor());
         }
      }
   }

   public StringBuilder startErrorMessage() {
      this.m_stringBuilder.setLength(0);
      return this.m_stringBuilder;
   }

   public void flushErrorMessage() {
      String var1 = this.m_stringBuilder.toString();
      DebugLog.log(var1);

      while(m_errors_list.size() >= 40) {
         m_errors_list.remove(0);
      }

      m_errors_list.add(var1);
      ++m_error_count;
   }

   public void doStacktraceProper(LuaCallFrame var1) {
      if (var1 != null) {
         StringBuilder var2 = this.startErrorMessage();
         var2.append("-----------------------------------------\n");
         var2.append("STACK TRACE\n");
         var2.append("-----------------------------------------\n");
         int var3 = var1.coroutine.getCallframeTop();

         for(int var4 = var3 - 1; var4 >= 0; --var4) {
            LuaCallFrame var5 = var1.coroutine.getCallFrame(var4);
            var2.append(var5.toString2());
            var2.append("\n");
         }

         this.flushErrorMessage();
      }
   }

   public void doStacktraceProper() {
      LuaCallFrame var1 = this.currentCoroutine.currentCallFrame();
      this.doStacktraceProper(var1);
   }

   public void debugException(Exception var1) {
      this.m_stringWriter.getBuffer().setLength(0);
      var1.printStackTrace(this.m_printWriter);
      String var2 = this.m_stringWriter.toString();
      m_errors_list.add(var2);
      ++m_error_count;
   }

   protected Object getMetaOp(Object var1, String var2) {
      KahluaTable var3 = (KahluaTable)this.getmetatable(var1, true);
      return var3 == null ? null : var3.rawget(var2);
   }

   private final Object getCompMetaOp(Object var1, Object var2, String var3) {
      KahluaTable var4 = (KahluaTable)this.getmetatable(var1, true);
      KahluaTable var5 = (KahluaTable)this.getmetatable(var2, true);
      if (var4 != null && var5 != null) {
         Object var6 = var4.rawget(var3);
         Object var7 = var5.rawget(var3);
         return var6 == var7 && var6 != null ? var6 : null;
      } else {
         return null;
      }
   }

   private final Object getBinMetaOp(Object var1, Object var2, String var3) {
      Object var4 = this.getMetaOp(var1, var3);
      return var4 != null ? var4 : this.getMetaOp(var2, var3);
   }

   private final Object getRegisterOrConstant(LuaCallFrame var1, int var2, Prototype var3) {
      int var4 = var2 - 256;
      return var4 < 0 ? var1.get(var2) : var3.constants[var4];
   }

   private static final int getA8(int var0) {
      return var0 >>> 6 & 255;
   }

   private static final int getC9(int var0) {
      return var0 >>> 14 & 511;
   }

   private static final int getB9(int var0) {
      return var0 >>> 23 & 511;
   }

   private static final int getBx(int var0) {
      return var0 >>> 14;
   }

   private static final int getSBx(int var0) {
      return (var0 >>> 14) - 131071;
   }

   private Double primitiveMath(Double var1, Double var2, int var3) {
      double var4 = KahluaUtil.fromDouble(var1);
      double var6 = KahluaUtil.fromDouble(var2);
      double var8 = 0.0D;
      switch(var3) {
      case 12:
         var8 = var4 + var6;
         break;
      case 13:
         var8 = var4 - var6;
         break;
      case 14:
         var8 = var4 * var6;
         break;
      case 15:
         var8 = var4 / var6;
         break;
      case 16:
         if (var6 == 0.0D) {
            var8 = Double.NaN;
         } else {
            int var10 = (int)(var4 / var6);
            var8 = var4 - (double)var10 * var6;
         }
         break;
      case 17:
         var8 = this.platform.pow(var4, var6);
      }

      return KahluaUtil.toDouble(var8);
   }

   public Object call(Object var1, Object var2, Object var3, Object var4) {
      int var5 = this.currentCoroutine.getTop();
      this.currentCoroutine.setTop(var5 + 1 + 3);
      this.currentCoroutine.objectStack[var5] = var1;
      this.currentCoroutine.objectStack[var5 + 1] = var2;
      this.currentCoroutine.objectStack[var5 + 2] = var3;
      this.currentCoroutine.objectStack[var5 + 3] = var4;
      int var6 = this.call(3);
      Object var7 = null;
      if (var6 >= 1) {
         var7 = this.currentCoroutine.objectStack[var5];
      }

      this.currentCoroutine.setTop(var5);
      return var7;
   }

   public Object call(Object var1, Object[] var2) {
      int var3 = this.currentCoroutine.getTop();
      int var4 = var2 == null ? 0 : var2.length;
      this.currentCoroutine.setTop(var3 + 1 + var4);
      this.currentCoroutine.objectStack[var3] = var1;

      int var5;
      for(var5 = 1; var5 <= var4; ++var5) {
         this.currentCoroutine.objectStack[var3 + var5] = var2[var5 - 1];
      }

      var5 = this.call(var4);
      Object var6 = null;
      if (var5 >= 1) {
         var6 = this.currentCoroutine.objectStack[var3];
      }

      this.currentCoroutine.setTop(var3);
      return var6;
   }

   public Object tableget(Object var1, Object var2) {
      Object var3 = var1;

      for(int var4 = 100; var4 > 0; --var4) {
         boolean var5 = var3 instanceof KahluaTable;
         Object var7;
         if (var5) {
            KahluaTable var6 = (KahluaTable)var3;
            var7 = var6.rawget(var2);
            if (var7 != null) {
               return var7;
            }
         }

         Object var8 = this.getMetaOp(var3, "__index");
         if (var8 == null) {
            if (var5) {
               return null;
            }

            StringBuilder var9 = this.startErrorMessage();
            var9.append("-------------------------------------------------------------\n");
            var9.append("attempted index: " + var2 + " of non-table: " + var3 + "\n");
            this.flushErrorMessage();
            this.doStacktraceProper(this.currentCoroutine.currentCallFrame());
            throw new RuntimeException("attempted index: " + var2 + " of non-table: " + var3);
         }

         if (var8 instanceof JavaFunction || var8 instanceof LuaClosure) {
            var7 = this.call(var8, var1, var2, (Object)null);
            return var7;
         }

         var3 = var8;
      }

      throw new RuntimeException("loop in gettable");
   }

   public void tableSet(Object var1, Object var2, Object var3) {
      Object var4 = var1;

      for(int var5 = 100; var5 > 0; --var5) {
         Object var6;
         if (var4 instanceof KahluaTable) {
            KahluaTable var7 = (KahluaTable)var4;
            if (var7.rawget(var2) != null) {
               var7.rawset(var2, var3);
               return;
            }

            var6 = this.getMetaOp(var4, "__newindex");
            if (var6 == null) {
               var7.rawset(var2, var3);
               return;
            }
         } else {
            var6 = this.getMetaOp(var4, "__newindex");
            if (var6 == null) {
               this.doStacktraceProper(this.currentCoroutine.currentCallFrame());
            }

            KahluaUtil.luaAssert(var6 != null, "attempted index of non-table");
         }

         if (var6 instanceof JavaFunction || var6 instanceof LuaClosure) {
            this.call(var6, var1, var2, var3);
            return;
         }

         var4 = var6;
      }

      throw new RuntimeException("loop in settable");
   }

   public void setmetatable(Object var1, KahluaTable var2) {
      KahluaUtil.luaAssert(var1 != null, "Can't set metatable for nil");
      if (var1 instanceof KahluaTable) {
         KahluaTable var3 = (KahluaTable)var1;
         var3.setMetatable(var2);
      } else {
         KahluaUtil.fail("Could not set metatable for object");
      }

   }

   public Object getmetatable(Object var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         KahluaTable var3 = null;
         KahluaTable var4;
         if (var1 instanceof KahluaTable) {
            var4 = (KahluaTable)var1;
            var3 = var4.getMetatable();
         } else if (var3 == null) {
            var4 = KahluaUtil.getClassMetatables(this.platform, this.getEnvironment());
            var3 = (KahluaTable)this.tableget(var4, var1.getClass());
         }

         if (!var2 && var3 != null) {
            Object var5 = var3.rawget("__metatable");
            if (var5 != null) {
               return var5;
            }
         }

         return var3;
      }
   }

   public Object[] pcall(Object var1, Object[] var2) {
      int var3 = var2 == null ? 0 : var2.length;
      Coroutine var4 = this.currentCoroutine;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + var3);
      var4.objectStack[var5] = var1;
      if (var3 > 0) {
         System.arraycopy(var2, 0, var4.objectStack, var5 + 1, var3);
      }

      int var6 = this.pcall(var3);
      KahluaUtil.luaAssert(var4 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      Object[] var7 = null;
      if (var2.length == var6) {
         var7 = var2;
      } else {
         var7 = new Object[var6];
      }

      System.arraycopy(var4.objectStack, var5, var7, 0, var6);
      var4.setTop(var5);
      return var7;
   }

   public void pcallvoid(Object var1, Object[] var2) {
      int var3 = var2 == null ? 0 : var2.length;
      Coroutine var4 = this.currentCoroutine;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + var3);
      var4.objectStack[var5] = var1;
      if (var3 > 0) {
         System.arraycopy(var2, 0, var4.objectStack, var5 + 1, var3);
      }

      this.pcall(var3);
      KahluaUtil.luaAssert(var4 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      var4.setTop(var5);
   }

   public void pcallvoid(Object var1, Object var2) {
      Coroutine var3 = this.currentCoroutine;
      int var4 = var3.getTop();
      var3.setTop(var4 + 1 + 1);
      var3.objectStack[var4] = var1;
      var3.objectStack[var4 + 1] = var2;
      int var5 = this.pcall(1);
      KahluaUtil.luaAssert(var3 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      var3.setTop(var4);
   }

   public void pcallvoid(Object var1, Object var2, Object var3) {
      Coroutine var4 = this.currentCoroutine;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + 2);
      var4.objectStack[var5] = var1;
      var4.objectStack[var5 + 1] = var2;
      var4.objectStack[var5 + 2] = var3;
      int var6 = this.pcall(2);
      KahluaUtil.luaAssert(var4 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      var4.setTop(var5);
   }

   public void pcallvoid(Object var1, Object var2, Object var3, Object var4) {
      Coroutine var5 = this.currentCoroutine;
      int var6 = var5.getTop();
      var5.setTop(var6 + 1 + 3);
      var5.objectStack[var6] = var1;
      var5.objectStack[var6 + 1] = var2;
      var5.objectStack[var6 + 2] = var3;
      var5.objectStack[var6 + 3] = var4;
      int var7 = this.pcall(3);
      KahluaUtil.luaAssert(var5 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      var5.setTop(var6);
   }

   public Boolean pcallBoolean(Object var1, Object var2) {
      Coroutine var3 = this.currentCoroutine;
      int var4 = var3.getTop();
      var3.setTop(var4 + 1 + 1);
      var3.objectStack[var4] = var1;
      var3.objectStack[var4 + 1] = var2;
      int var5 = this.pcall(1);
      KahluaUtil.luaAssert(var3 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      Boolean var6 = null;
      if (var5 > 1) {
         Boolean var7 = (Boolean)var3.objectStack[var4];
         if (var7) {
            Object var8 = var3.objectStack[var4 + 1];
            if (var8 instanceof Boolean) {
               var6 = (Boolean)var8 ? Boolean.TRUE : Boolean.FALSE;
            }
         }
      }

      var3.setTop(var4);
      return var6;
   }

   public Boolean pcallBoolean(Object var1, Object var2, Object var3) {
      Coroutine var4 = this.currentCoroutine;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + 2);
      var4.objectStack[var5] = var1;
      var4.objectStack[var5 + 1] = var2;
      var4.objectStack[var5 + 2] = var3;
      int var6 = this.pcall(2);
      KahluaUtil.luaAssert(var4 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      Boolean var7 = null;
      if (var6 > 1) {
         Boolean var8 = (Boolean)var4.objectStack[var5];
         if (var8) {
            Object var9 = var4.objectStack[var5 + 1];
            if (var9 instanceof Boolean) {
               var7 = (Boolean)var9 ? Boolean.TRUE : Boolean.FALSE;
            }
         }
      }

      var4.setTop(var5);
      return var7;
   }

   public Boolean pcallBoolean(Object var1, Object var2, Object var3, Object var4) {
      Coroutine var5 = this.currentCoroutine;
      int var6 = var5.getTop();
      var5.setTop(var6 + 1 + 3);
      var5.objectStack[var6] = var1;
      var5.objectStack[var6 + 1] = var2;
      var5.objectStack[var6 + 2] = var3;
      var5.objectStack[var6 + 3] = var4;
      int var7 = this.pcall(3);
      KahluaUtil.luaAssert(var5 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      Boolean var8 = null;
      if (var7 > 1) {
         Boolean var9 = (Boolean)var5.objectStack[var6];
         if (var9) {
            Object var10 = var5.objectStack[var6 + 1];
            if (var10 instanceof Boolean) {
               var8 = (Boolean)var10 ? Boolean.TRUE : Boolean.FALSE;
            }
         }
      }

      var5.setTop(var6);
      return var8;
   }

   public Boolean pcallBoolean(Object var1, Object[] var2) {
      int var3 = var2 == null ? 0 : var2.length;
      Coroutine var4 = this.currentCoroutine;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + var3);
      var4.objectStack[var5] = var1;
      if (var3 > 0) {
         System.arraycopy(var2, 0, var4.objectStack, var5 + 1, var3);
      }

      int var6 = this.pcall(var3);
      KahluaUtil.luaAssert(var4 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      Boolean var7 = null;
      if (var6 > 1) {
         Boolean var8 = (Boolean)var4.objectStack[var5];
         if (var8) {
            Object var9 = var4.objectStack[var5 + 1];
            if (var9 instanceof Boolean) {
               var7 = (Boolean)var9 ? Boolean.TRUE : Boolean.FALSE;
            }
         }
      }

      var4.setTop(var5);
      return var7;
   }

   public Object[] pcall(Object var1) {
      return this.pcall(var1, (Object[])null);
   }

   public int pcall(int var1) {
      Coroutine var2 = this.currentCoroutine;
      LuaCallFrame var3 = var2.currentCallFrame();
      var2.stackTrace = "";
      int var4 = var2.getTop() - var1 - 1;

      Object var5;
      Object var6;
      try {
         int var7 = var2.getCallframeTop();
         int var8 = this.call(var1);
         int var9 = var2.getCallframeTop();
         boolean var10;
         if (var7 != var9) {
            var10 = false;
         }

         KahluaUtil.luaAssert(var7 == var9, "error - call stack depth changed.");
         if (var7 != var9) {
            var10 = false;
         }

         int var13 = var4 + var8 + 1;
         var2.setTop(var13);
         var2.stackCopy(var4, var4 + 1, var8);
         var2.objectStack[var4] = Boolean.TRUE;
         return 1 + var8;
      } catch (KahluaException var11) {
         var6 = var11;
         var5 = var11.errorMessage;
      } catch (Throwable var12) {
         var6 = var12;
         String var10000 = var12.getMessage();
         var5 = var10000 + " " + var12.getClass().getName();
      }

      KahluaUtil.luaAssert(var2 == this.currentCoroutine, "Internal Kahlua error - coroutine changed in pcall");
      if (var3 != null) {
         var3.closeUpvalues(0);
      }

      var2.cleanCallFrames(var3);
      if (var5 instanceof String) {
         var5 = (String)var5;
      }

      var2.setTop(var4 + 4);
      var2.objectStack[var4] = Boolean.FALSE;
      var2.objectStack[var4 + 1] = var5;
      var2.objectStack[var4 + 2] = var2.stackTrace;
      var2.objectStack[var4 + 3] = var6;
      var2.stackTrace = "";
      return 4;
   }

   public KahluaTable getEnvironment() {
      return this.currentCoroutine.environment;
   }

   public PrintStream getOut() {
      return this.out;
   }

   public Platform getPlatform() {
      return this.platform;
   }

   public void breakpointToggle(String var1, int var2) {
      ArrayList var3;
      if (!this.BreakpointMap.containsKey(var1)) {
         var3 = new ArrayList();
         this.BreakpointMap.put(var1, var3);
      } else {
         var3 = (ArrayList)this.BreakpointMap.get(var1);
      }

      if (!var3.contains((long)var2)) {
         var3.add((long)var2);
      } else {
         var3.remove((long)var2);
      }

   }

   public boolean hasBreakpoint(String var1, int var2) {
      return this.BreakpointMap.containsKey(var1) && ((ArrayList)this.BreakpointMap.get(var1)).contains((long)var2);
   }

   public void toggleBreakOnChange(KahluaTable var1, Object var2) {
      ArrayList var3;
      if (!this.BreakpointDataMap.containsKey(var1)) {
         var3 = new ArrayList();
         this.BreakpointDataMap.put(var1, var3);
      } else {
         var3 = (ArrayList)this.BreakpointDataMap.get(var1);
      }

      if (!var3.contains(var2)) {
         var3.add(var2);
      } else {
         var3.remove(var2);
      }

   }

   public void toggleBreakOnRead(KahluaTable var1, Object var2) {
      ArrayList var3;
      if (!this.BreakpointReadDataMap.containsKey(var1)) {
         var3 = new ArrayList();
         this.BreakpointReadDataMap.put(var1, var3);
      } else {
         var3 = (ArrayList)this.BreakpointReadDataMap.get(var1);
      }

      if (!var3.contains(var2)) {
         var3.add(var2);
      } else {
         var3.remove(var2);
      }

   }

   public boolean hasDataBreakpoint(KahluaTable var1, Object var2) {
      if (!this.BreakpointDataMap.containsKey(var1)) {
         return false;
      } else {
         ArrayList var3 = (ArrayList)this.BreakpointDataMap.get(var1);
         return var3.contains(var2);
      }
   }

   public boolean hasReadDataBreakpoint(KahluaTable var1, Object var2) {
      if (!this.BreakpointReadDataMap.containsKey(var1)) {
         return false;
      } else {
         ArrayList var3 = (ArrayList)this.BreakpointReadDataMap.get(var1);
         return var3.contains(var2);
      }
   }

   static {
      meta_ops[12] = "__add";
      meta_ops[13] = "__sub";
      meta_ops[14] = "__mul";
      meta_ops[15] = "__div";
      meta_ops[16] = "__mod";
      meta_ops[17] = "__pow";
      meta_ops[23] = "__eq";
      meta_ops[24] = "__lt";
      meta_ops[25] = "__le";
      LastCallFrame = null;
      m_error_count = 0;
      m_errors_list = new ArrayList();
   }

   public static class Entry {
      public String file;
      public double time;
   }

   private static class ProfileEntryComparitor implements Comparator {
      public ProfileEntryComparitor() {
      }

      public int compare(KahluaThread.Entry var1, KahluaThread.Entry var2) {
         double var3 = var1.time;
         double var5 = var2.time;
         if (var3 > var5) {
            return -1;
         } else {
            return var5 > var3 ? 1 : 0;
         }
      }
   }
}
