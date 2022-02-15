package org.luaj.kahluafork.compiler;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import se.krka.kahlua.vm.KahluaException;
import se.krka.kahlua.vm.Prototype;
import zombie.core.Core;

public class LexState {
   public int nCcalls;
   protected static final String RESERVED_LOCAL_VAR_FOR_CONTROL = "(for control)";
   protected static final String RESERVED_LOCAL_VAR_FOR_STATE = "(for state)";
   protected static final String RESERVED_LOCAL_VAR_FOR_GENERATOR = "(for generator)";
   protected static final String RESERVED_LOCAL_VAR_FOR_STEP = "(for step)";
   protected static final String RESERVED_LOCAL_VAR_FOR_LIMIT = "(for limit)";
   protected static final String RESERVED_LOCAL_VAR_FOR_INDEX = "(for index)";
   protected static final String[] RESERVED_LOCAL_VAR_KEYWORDS = new String[]{"(for control)", "(for generator)", "(for index)", "(for limit)", "(for state)", "(for step)"};
   private static final Hashtable RESERVED_LOCAL_VAR_KEYWORDS_TABLE = new Hashtable();
   private static final int EOZ = -1;
   private static final int MAXSRC = 80;
   private static final int MAX_INT = 2147483645;
   private static final int UCHAR_MAX = 255;
   private static final int LUAI_MAXCCALLS = 200;
   static final int NO_JUMP = -1;
   static final int OPR_ADD = 0;
   static final int OPR_SUB = 1;
   static final int OPR_MUL = 2;
   static final int OPR_DIV = 3;
   static final int OPR_MOD = 4;
   static final int OPR_POW = 5;
   static final int OPR_CONCAT = 6;
   static final int OPR_NE = 7;
   static final int OPR_EQ = 8;
   static final int OPR_LT = 9;
   static final int OPR_LE = 10;
   static final int OPR_GT = 11;
   static final int OPR_GE = 12;
   static final int OPR_AND = 13;
   static final int OPR_OR = 14;
   static final int OPR_NOBINOPR = 15;
   static final int OPR_MINUS = 0;
   static final int OPR_NOT = 1;
   static final int OPR_LEN = 2;
   static final int OPR_NOUNOPR = 3;
   static final int VVOID = 0;
   static final int VNIL = 1;
   static final int VTRUE = 2;
   static final int VFALSE = 3;
   static final int VK = 4;
   static final int VKNUM = 5;
   static final int VLOCAL = 6;
   static final int VUPVAL = 7;
   static final int VGLOBAL = 8;
   static final int VINDEXED = 9;
   static final int VJMP = 10;
   static final int VRELOCABLE = 11;
   static final int VNONRELOC = 12;
   static final int VCALL = 13;
   static final int VVARARG = 14;
   int current;
   int linenumber;
   int lastline;
   final Token t = new Token();
   final Token lookahead = new Token();
   FuncState fs;
   Reader z;
   byte[] buff;
   int nbuff;
   String source;
   static final String[] luaX_tokens;
   static final int TK_AND = 257;
   static final int TK_BREAK = 258;
   static final int TK_DO = 259;
   static final int TK_ELSE = 260;
   static final int TK_ELSEIF = 261;
   static final int TK_END = 262;
   static final int TK_FALSE = 263;
   static final int TK_FOR = 264;
   static final int TK_FUNCTION = 265;
   static final int TK_IF = 266;
   static final int TK_IN = 267;
   static final int TK_LOCAL = 268;
   static final int TK_NIL = 269;
   static final int TK_NOT = 270;
   static final int TK_OR = 271;
   static final int TK_REPEAT = 272;
   static final int TK_RETURN = 273;
   static final int TK_THEN = 274;
   static final int TK_TRUE = 275;
   static final int TK_UNTIL = 276;
   static final int TK_WHILE = 277;
   static final int TK_CONCAT = 278;
   static final int TK_DOTS = 279;
   static final int TK_EQ = 280;
   static final int TK_GE = 281;
   static final int TK_LE = 282;
   static final int TK_NE = 283;
   static final int TK_NUMBER = 284;
   static final int TK_NAME = 285;
   static final int TK_STRING = 286;
   static final int TK_EOS = 287;
   static final int FIRST_RESERVED = 257;
   static final int NUM_RESERVED = 21;
   static final Hashtable RESERVED;
   static final int[] priorityLeft;
   static final int[] priorityRight;
   static final int UNARY_PRIORITY = 8;

   private static final String LUA_QS(String var0) {
      return "'" + var0 + "'";
   }

   private static final String LUA_QL(Object var0) {
      return LUA_QS(String.valueOf(var0));
   }

   public static boolean isReservedKeyword(String var0) {
      return RESERVED_LOCAL_VAR_KEYWORDS_TABLE.containsKey(var0);
   }

   private boolean isalnum(int var1) {
      return var1 >= 48 && var1 <= 57 || var1 >= 97 && var1 <= 122 || var1 >= 65 && var1 <= 90 || var1 == 95;
   }

   private boolean isalpha(int var1) {
      return var1 >= 97 && var1 <= 122 || var1 >= 65 && var1 <= 90;
   }

   private boolean isdigit(int var1) {
      return var1 >= 48 && var1 <= 57;
   }

   private boolean isspace(int var1) {
      return var1 <= 32;
   }

   public static Prototype compile(int var0, Reader var1, String var2, String var3) {
      if (var2 != null) {
         var3 = var2;
      } else {
         var2 = "stdin";
         var3 = "[string \"" + trim(var3, 80) + "\"]";
      }

      LexState var4 = new LexState(var1, var0, var3);
      FuncState var5 = new FuncState(var4);
      var5.isVararg = 2;
      var5.f.name = var2;
      var4.next();
      var4.chunk();
      var4.check(287);
      var4.close_func();
      FuncState._assert(var5.prev == null);
      FuncState._assert(var5.f.numUpvalues == 0);
      FuncState._assert(var4.fs == null);
      return var5.f;
   }

   public LexState(Reader var1, int var2, String var3) {
      this.z = var1;
      this.buff = new byte[32];
      this.lookahead.token = 287;
      this.fs = null;
      this.linenumber = 1;
      this.lastline = 1;
      this.source = var3;
      this.nbuff = 0;
      this.current = var2;
      this.skipShebang();
   }

   void nextChar() {
      try {
         this.current = this.z.read();
      } catch (IOException var2) {
         var2.printStackTrace();
         this.current = -1;
      }

   }

   boolean currIsNewline() {
      return this.current == 10 || this.current == 13;
   }

   void save_and_next() {
      this.save(this.current);
      this.nextChar();
   }

   void save(int var1) {
      if (this.buff == null || this.nbuff + 1 > this.buff.length) {
         this.buff = FuncState.realloc(this.buff, this.nbuff * 2 + 1);
      }

      this.buff[this.nbuff++] = (byte)var1;
   }

   String token2str(int var1) {
      if (var1 < 257) {
         return iscntrl(var1) ? "char(" + var1 + ")" : String.valueOf((char)var1);
      } else {
         return luaX_tokens[var1 - 257];
      }
   }

   private static boolean iscntrl(int var0) {
      return var0 < 32;
   }

   String txtToken(int var1) {
      switch(var1) {
      case 284:
      case 285:
      case 286:
         return new String(this.buff, 0, this.nbuff);
      default:
         return this.token2str(var1);
      }
   }

   void lexerror(String var1, int var2) {
      String var3 = this.source;
      String var4;
      if (var2 != 0) {
         var4 = var3 + ":" + this.linenumber + ": " + var1 + " near `" + this.txtToken(var2) + "`";
      } else {
         var4 = var3 + ":" + this.linenumber + ": " + var1;
      }

      throw new KahluaException(var4);
   }

   private static String trim(String var0, int var1) {
      if (var0.length() > var1) {
         String var10000 = var0.substring(0, var1 - 3);
         return var10000 + "...";
      } else {
         return var0;
      }
   }

   void syntaxerror(String var1) {
      this.lexerror(var1, this.t.token);
   }

   String newstring(byte[] var1, int var2, int var3) {
      try {
         String var4 = new String(var1, var2, var3, "UTF-8");
         return var4;
      } catch (UnsupportedEncodingException var5) {
         return null;
      }
   }

   void inclinenumber() {
      int var1 = this.current;
      FuncState._assert(this.currIsNewline());
      this.nextChar();
      if (this.currIsNewline() && this.current != var1) {
         this.nextChar();
      }

      if (++this.linenumber >= 2147483645) {
         this.syntaxerror("chunk has too many lines");
      }

   }

   private void skipShebang() {
      if (this.current == 35) {
         while(!this.currIsNewline() && this.current != -1) {
            this.nextChar();
         }
      }

   }

   boolean check_next(String var1) {
      if (var1.indexOf(this.current) < 0) {
         return false;
      } else {
         this.save_and_next();
         return true;
      }
   }

   void str2d(String var1, Token var2) {
      try {
         double var3;
         if (var1.startsWith("0x")) {
            var3 = (double)Long.parseLong(var1.substring(2), 16);
         } else {
            var3 = Double.parseDouble(var1);
         }

         var2.r = var3;
      } catch (NumberFormatException var5) {
         this.lexerror("malformed number", 284);
      }

   }

   void read_numeral(Token var1) {
      FuncState._assert(this.isdigit(this.current));

      do {
         do {
            this.save_and_next();
         } while(this.isdigit(this.current));
      } while(this.current == 46);

      if (this.check_next("Ee")) {
         this.check_next("+-");
      }

      while(this.isalnum(this.current) || this.current == 95) {
         this.save_and_next();
      }

      String var2 = new String(this.buff, 0, this.nbuff);
      this.str2d(var2, var1);
   }

   int skip_sep() {
      int var1 = 0;
      int var2 = this.current;
      FuncState._assert(var2 == 91 || var2 == 93);
      this.save_and_next();

      while(this.current == 61) {
         this.save_and_next();
         ++var1;
      }

      return this.current == var2 ? var1 : -var1 - 1;
   }

   void read_long_string(Token var1, int var2) {
      int var3 = 0;
      this.save_and_next();
      if (this.currIsNewline()) {
         this.inclinenumber();
      }

      boolean var4 = false;

      while(!var4) {
         switch(this.current) {
         case -1:
            this.lexerror(var1 != null ? "unfinished long string" : "unfinished long comment", 287);
            break;
         case 10:
         case 13:
            this.save(10);
            this.inclinenumber();
            if (var1 == null) {
               this.nbuff = 0;
            }
            break;
         case 91:
            if (this.skip_sep() == var2) {
               this.save_and_next();
               ++var3;
            }
            break;
         case 93:
            if (this.skip_sep() == var2) {
               this.save_and_next();
               var4 = true;
            }
            break;
         default:
            if (var1 != null) {
               this.save_and_next();
            } else {
               this.nextChar();
            }
         }
      }

      if (var1 != null) {
         var1.ts = this.newstring(this.buff, 2 + var2, this.nbuff - 2 * (2 + var2));
      }

   }

   void read_string(int var1, Token var2) {
      this.save_and_next();

      while(true) {
         byte var3;
         label53:
         while(true) {
            while(this.current != var1) {
               switch(this.current) {
               case -1:
                  this.lexerror("unfinished string", 287);
                  break;
               case 10:
               case 13:
                  this.lexerror("unfinished string", 286);
                  break;
               case 92:
                  this.nextChar();
                  int var4;
                  int var5;
                  switch(this.current) {
                  case -1:
                     continue;
                  case 10:
                  case 13:
                     this.save(10);
                     this.inclinenumber();
                     continue;
                  case 97:
                     var3 = 7;
                     break label53;
                  case 98:
                     var3 = 8;
                     break label53;
                  case 102:
                     var3 = 12;
                     break label53;
                  case 110:
                     var3 = 10;
                     break label53;
                  case 114:
                     var3 = 13;
                     break label53;
                  case 116:
                     var3 = 9;
                     break label53;
                  case 118:
                     var3 = 11;
                     break label53;
                  default:
                     if (!this.isdigit(this.current)) {
                        this.save_and_next();
                        continue;
                     }

                     var4 = 0;
                     var5 = 0;
                  }

                  do {
                     var5 = 10 * var5 + (this.current - 48);
                     this.nextChar();
                     ++var4;
                  } while(var4 < 3 && this.isdigit(this.current));

                  if (var5 > 255) {
                     this.lexerror("escape sequence too large", 286);
                  }

                  this.save(var5);
                  break;
               default:
                  this.save_and_next();
               }
            }

            this.save_and_next();
            var2.ts = this.newstring(this.buff, 1, this.nbuff - 2);
            return;
         }

         this.save(var3);
         this.nextChar();
      }
   }

   int llex(Token var1) {
      this.nbuff = 0;

      label103:
      while(true) {
         int var2;
         switch(this.current) {
         case -1:
            return 287;
         case 10:
         case 13:
            this.inclinenumber();
            break;
         case 34:
         case 39:
            this.read_string(this.current, var1);
            return 286;
         case 45:
            this.nextChar();
            if (this.current != 45) {
               return 45;
            }

            this.nextChar();
            if (this.current == 91) {
               var2 = this.skip_sep();
               this.nbuff = 0;
               if (var2 >= 0) {
                  this.read_long_string((Token)null, var2);
                  this.nbuff = 0;
                  break;
               }
            }

            while(true) {
               if (this.currIsNewline() || this.current == -1) {
                  continue label103;
               }

               this.nextChar();
            }
         case 46:
            this.save_and_next();
            if (this.check_next(".")) {
               if (this.check_next(".")) {
                  return 279;
               }

               return 278;
            }

            if (!this.isdigit(this.current)) {
               return 46;
            }

            this.read_numeral(var1);
            return 284;
         case 60:
            this.nextChar();
            if (this.current != 61) {
               return 60;
            }

            this.nextChar();
            return 282;
         case 62:
            this.nextChar();
            if (this.current != 61) {
               return 62;
            }

            this.nextChar();
            return 281;
         case 91:
            var2 = this.skip_sep();
            if (var2 >= 0) {
               this.read_long_string(var1, var2);
               return 286;
            }

            if (var2 == -1) {
               return 91;
            }

            this.lexerror("invalid long string delimiter", 286);
         case 61:
            this.nextChar();
            if (this.current != 61) {
               return 61;
            }

            this.nextChar();
            return 280;
         case 126:
            this.nextChar();
            if (this.current != 61) {
               return 126;
            }

            this.nextChar();
            return 283;
         default:
            if (!this.isspace(this.current)) {
               if (this.isdigit(this.current)) {
                  this.read_numeral(var1);
                  return 284;
               }

               if (!this.isalpha(this.current) && this.current != 95) {
                  var2 = this.current;
                  this.nextChar();
                  return var2;
               }

               do {
                  do {
                     this.save_and_next();
                  } while(this.isalnum(this.current));
               } while(this.current == 95);

               String var3 = this.newstring(this.buff, 0, this.nbuff);
               if (RESERVED.containsKey(var3)) {
                  return (Integer)RESERVED.get(var3);
               }

               var1.ts = var3;
               return 285;
            }

            FuncState._assert(!this.currIsNewline());
            this.nextChar();
         }
      }
   }

   void next() {
      this.lastline = this.linenumber;
      if (this.lookahead.token != 287) {
         this.t.set(this.lookahead);
         this.lookahead.token = 287;
      } else {
         this.t.token = this.llex(this.t);
      }

   }

   void lookahead() {
      FuncState._assert(this.lookahead.token == 287);
      this.lookahead.token = this.llex(this.lookahead);
   }

   boolean hasmultret(int var1) {
      return var1 == 13 || var1 == 14;
   }

   void error_expected(int var1) {
      String var10001 = this.token2str(var1);
      this.syntaxerror(LUA_QS(var10001) + " expected");
   }

   boolean testnext(int var1) {
      if (this.t.token == var1) {
         this.next();
         return true;
      } else {
         return false;
      }
   }

   void check(int var1) {
      if (this.t.token != var1) {
         this.error_expected(var1);
      }

   }

   void checknext(int var1) {
      this.check(var1);
      this.next();
   }

   void check_condition(boolean var1, String var2) {
      if (!var1) {
         this.syntaxerror(var2);
      }

   }

   void check_match(int var1, int var2, int var3) {
      if (!this.testnext(var1)) {
         if (var3 == this.linenumber) {
            this.error_expected(var1);
         } else {
            String var10001 = LUA_QS(this.token2str(var1));
            this.syntaxerror(var10001 + " expected (to close " + LUA_QS(this.token2str(var2)) + " at line " + var3 + ")");
         }
      }

   }

   String str_checkname() {
      this.check(285);
      String var1 = this.t.ts;
      this.next();
      return var1;
   }

   void codestring(ExpDesc var1, String var2) {
      var1.init(4, this.fs.stringK(var2));
   }

   void checkname(ExpDesc var1) {
      this.codestring(var1, this.str_checkname());
   }

   int registerlocalvar(String var1) {
      FuncState var2 = this.fs;
      if (var2.locvars == null || var2.nlocvars + 1 > var2.locvars.length) {
         var2.locvars = FuncState.realloc(var2.locvars, var2.nlocvars * 2 + 1);
      }

      var2.locvars[var2.nlocvars] = var1;
      return var2.nlocvars++;
   }

   void new_localvarliteral(String var1, int var2) {
      this.new_localvar(var1, var2);
   }

   void new_localvar(String var1, int var2, int var3) {
      FuncState var4 = this.fs;
      var4.checklimit(var4.nactvar + var2 + 1, 200, "local variables");
      var4.actvar[var4.nactvar + var2] = (short)this.registerlocalvar(var1);
      if (Core.bDebug) {
         var4.actvarline[var4.actvar[var4.nactvar + var2]] = this.linenumber;
      }

   }

   void new_localvar(String var1, int var2) {
      FuncState var3 = this.fs;
      var3.checklimit(var3.nactvar + var2 + 1, 200, "local variables");
      var3.actvar[var3.nactvar + var2] = (short)this.registerlocalvar(var1);
      if (Core.bDebug) {
         var3.actvarline[var3.actvar[var3.nactvar + var2]] = this.linenumber;
      }

   }

   void adjustlocalvars(int var1) {
      FuncState var2 = this.fs;
      var2.nactvar += var1;
   }

   void removevars(int var1) {
      FuncState var2 = this.fs;
      var2.nactvar = var1;
   }

   void singlevar(ExpDesc var1) {
      String var2 = this.str_checkname();
      FuncState var3 = this.fs;
      if (var3.singlevaraux(var2, var1, 1) == 8) {
         var1.info = var3.stringK(var2);
      }

   }

   void adjust_assign(int var1, int var2, ExpDesc var3) {
      FuncState var4 = this.fs;
      int var5 = var1 - var2;
      if (this.hasmultret(var3.k)) {
         ++var5;
         if (var5 < 0) {
            var5 = 0;
         }

         var4.setreturns(var3, var5);
         if (var5 > 1) {
            var4.reserveregs(var5 - 1);
         }
      } else {
         if (var3.k != 0) {
            var4.exp2nextreg(var3);
         }

         if (var5 > 0) {
            int var6 = var4.freereg;
            var4.reserveregs(var5);
            var4.nil(var6, var5);
         }
      }

   }

   void enterlevel() {
      if (++this.nCcalls > 200) {
         this.lexerror("chunk has too many syntax levels", 0);
      }

   }

   void leavelevel() {
      --this.nCcalls;
   }

   void pushclosure(FuncState var1, ExpDesc var2) {
      FuncState var3 = this.fs;
      Prototype var4 = var3.f;
      if (var4.prototypes == null || var3.np + 1 > var4.prototypes.length) {
         var4.prototypes = FuncState.realloc(var4.prototypes, var3.np * 2 + 1);
      }

      var4.prototypes[var3.np++] = var1.f;
      var2.init(11, var3.codeABx(36, 0, var3.np - 1));

      for(int var5 = 0; var5 < var1.f.numUpvalues; ++var5) {
         int var6 = var1.upvalues_k[var5] == 6 ? 0 : 4;
         var3.codeABC(var6, 0, var1.upvalues_info[var5], 0);
      }

   }

   void close_func() {
      FuncState var1 = this.fs;
      Prototype var2 = var1.f;
      var2.isVararg = var1.isVararg != 0;
      this.removevars(0);
      var1.ret(0, 0);
      var2.code = FuncState.realloc(var2.code, var1.pc);
      var2.lines = FuncState.realloc(var2.lines, var1.pc);
      var2.constants = FuncState.realloc(var2.constants, var1.nk);
      var2.prototypes = FuncState.realloc(var2.prototypes, var1.np);
      var1.locvars = FuncState.realloc(var1.locvars, var1.nlocvars);
      if (Core.bDebug) {
         var2.locvars = var1.locvars;
         var2.locvarlines = var1.actvarline;
      }

      var1.upvalues = FuncState.realloc(var1.upvalues, var2.numUpvalues);
      FuncState._assert(var1.bl == null);
      this.fs = var1.prev;
   }

   void field(ExpDesc var1) {
      FuncState var2 = this.fs;
      ExpDesc var3 = new ExpDesc();
      var2.exp2anyreg(var1);
      this.next();
      this.checkname(var3);
      var2.indexed(var1, var3);
   }

   void yindex(ExpDesc var1) {
      this.next();
      this.expr(var1);
      this.fs.exp2val(var1);
      this.checknext(93);
   }

   void recfield(ConsControl var1) {
      FuncState var2 = this.fs;
      int var3 = this.fs.freereg;
      ExpDesc var4 = new ExpDesc();
      ExpDesc var5 = new ExpDesc();
      if (this.t.token == 285) {
         var2.checklimit(var1.nh, 2147483645, "items in a constructor");
         this.checkname(var4);
      } else {
         this.yindex(var4);
      }

      ++var1.nh;
      this.checknext(61);
      int var6 = var2.exp2RK(var4);
      this.expr(var5);
      var2.codeABC(9, var1.t.info, var6, var2.exp2RK(var5));
      var2.freereg = var3;
   }

   void listfield(ConsControl var1) {
      this.expr(var1.v);
      this.fs.checklimit(var1.na, 2147483645, "items in a constructor");
      ++var1.na;
      ++var1.tostore;
   }

   void constructor(ExpDesc var1) {
      FuncState var2 = this.fs;
      int var3 = this.linenumber;
      int var4 = var2.codeABC(10, 0, 0, 0);
      ConsControl var5 = new ConsControl();
      var5.na = var5.nh = var5.tostore = 0;
      var5.t = var1;
      var1.init(11, var4);
      var5.v.init(0, 0);
      var2.exp2nextreg(var1);
      this.checknext(123);

      do {
         FuncState._assert(var5.v.k == 0 || var5.tostore > 0);
         if (this.t.token == 125) {
            break;
         }

         var2.closelistfield(var5);
         switch(this.t.token) {
         case 91:
            this.recfield(var5);
            break;
         case 285:
            this.lookahead();
            if (this.lookahead.token != 61) {
               this.listfield(var5);
            } else {
               this.recfield(var5);
            }
            break;
         default:
            this.listfield(var5);
         }
      } while(this.testnext(44) || this.testnext(59));

      this.check_match(125, 123, var3);
      var2.lastlistfield(var5);
      InstructionPtr var6 = new InstructionPtr(var2.f.code, var4);
      FuncState.SETARG_B(var6, luaO_int2fb(var5.na));
      FuncState.SETARG_C(var6, luaO_int2fb(var5.nh));
   }

   static int luaO_int2fb(int var0) {
      int var1;
      for(var1 = 0; var0 >= 16; ++var1) {
         var0 = var0 + 1 >> 1;
      }

      return var0 < 8 ? var0 : var1 + 1 << 3 | var0 - 8;
   }

   void parlist() {
      FuncState var1 = this.fs;
      Prototype var2 = var1.f;
      int var3 = 0;
      var1.isVararg = 0;
      if (this.t.token != 41) {
         do {
            switch(this.t.token) {
            case 279:
               this.next();
               var1.isVararg |= 2;
               break;
            case 285:
               this.new_localvar(this.str_checkname(), var3++);
               break;
            default:
               this.syntaxerror("<name> or " + LUA_QL("...") + " expected");
            }
         } while(var1.isVararg == 0 && this.testnext(44));
      }

      this.adjustlocalvars(var3);
      var2.numParams = var1.nactvar - (var1.isVararg & 1);
      var1.reserveregs(var1.nactvar);
   }

   void body(ExpDesc var1, boolean var2, int var3) {
      FuncState var4 = new FuncState(this, this.t.ts);
      var4.linedefined = var3;
      this.checknext(40);
      if (var2) {
         this.new_localvarliteral("self", 0);
         this.adjustlocalvars(1);
      }

      this.parlist();
      this.checknext(41);
      this.chunk();
      var4.lastlinedefined = this.linenumber;
      this.check_match(262, 265, var3);
      this.close_func();
      this.pushclosure(var4, var1);
   }

   int explist1(ExpDesc var1) {
      int var2 = 1;
      this.expr(var1);

      while(this.testnext(44)) {
         this.fs.exp2nextreg(var1);
         this.expr(var1);
         ++var2;
      }

      return var2;
   }

   void funcargs(ExpDesc var1) {
      FuncState var2 = this.fs;
      ExpDesc var3 = new ExpDesc();
      int var6 = this.linenumber;
      switch(this.t.token) {
      case 40:
         if (var6 != this.lastline) {
            this.syntaxerror("ambiguous syntax (function call x new statement)");
         }

         this.next();
         if (this.t.token == 41) {
            var3.k = 0;
         } else {
            this.explist1(var3);
            var2.setmultret(var3);
         }

         this.check_match(41, 40, var6);
         break;
      case 123:
         this.constructor(var3);
         break;
      case 286:
         this.codestring(var3, this.t.ts);
         this.next();
         break;
      default:
         this.syntaxerror("function arguments expected");
         return;
      }

      FuncState._assert(var1.k == 12);
      int var4 = var1.info;
      int var5;
      if (this.hasmultret(var3.k)) {
         var5 = -1;
      } else {
         if (var3.k != 0) {
            var2.exp2nextreg(var3);
         }

         var5 = var2.freereg - (var4 + 1);
      }

      var1.init(13, var2.codeABC(28, var4, var5 + 1, 2));
      var2.fixline(var6);
      var2.freereg = var4 + 1;
   }

   void prefixexp(ExpDesc var1) {
      switch(this.t.token) {
      case 40:
         int var2 = this.linenumber;
         this.next();
         this.expr(var1);
         this.check_match(41, 40, var2);
         this.fs.dischargevars(var1);
         return;
      case 285:
         this.singlevar(var1);
         return;
      default:
         this.syntaxerror("unexpected symbol");
      }
   }

   void primaryexp(ExpDesc var1) {
      FuncState var2 = this.fs;
      this.prefixexp(var1);

      while(true) {
         ExpDesc var3;
         switch(this.t.token) {
         case 40:
         case 123:
         case 286:
            var2.exp2nextreg(var1);
            this.funcargs(var1);
            break;
         case 46:
            this.field(var1);
            break;
         case 58:
            var3 = new ExpDesc();
            this.next();
            this.checkname(var3);
            var2.self(var1, var3);
            this.funcargs(var1);
            break;
         case 91:
            var3 = new ExpDesc();
            var2.exp2anyreg(var1);
            this.yindex(var3);
            var2.indexed(var1, var3);
            break;
         default:
            return;
         }
      }
   }

   void simpleexp(ExpDesc var1) {
      switch(this.t.token) {
      case 123:
         this.constructor(var1);
         return;
      case 263:
         var1.init(3, 0);
         break;
      case 265:
         this.next();
         this.body(var1, false, this.linenumber);
         return;
      case 269:
         var1.init(1, 0);
         break;
      case 275:
         var1.init(2, 0);
         break;
      case 279:
         FuncState var2 = this.fs;
         this.check_condition(var2.isVararg != 0, "cannot use " + LUA_QL("...") + " outside a vararg function");
         var2.isVararg &= -5;
         var1.init(14, var2.codeABC(37, 0, 1, 0));
         break;
      case 284:
         var1.init(5, 0);
         var1.setNval(this.t.r);
         break;
      case 286:
         this.codestring(var1, this.t.ts);
         break;
      default:
         this.primaryexp(var1);
         return;
      }

      this.next();
   }

   int getunopr(int var1) {
      switch(var1) {
      case 35:
         return 2;
      case 45:
         return 0;
      case 270:
         return 1;
      default:
         return 3;
      }
   }

   int getbinopr(int var1) {
      switch(var1) {
      case 37:
         return 4;
      case 42:
         return 2;
      case 43:
         return 0;
      case 45:
         return 1;
      case 47:
         return 3;
      case 60:
         return 9;
      case 62:
         return 11;
      case 94:
         return 5;
      case 257:
         return 13;
      case 271:
         return 14;
      case 278:
         return 6;
      case 280:
         return 8;
      case 281:
         return 12;
      case 282:
         return 10;
      case 283:
         return 7;
      default:
         return 15;
      }
   }

   int subexpr(ExpDesc var1, int var2) {
      this.enterlevel();
      int var4 = this.getunopr(this.t.token);
      if (var4 != 3) {
         this.next();
         this.subexpr(var1, 8);
         this.fs.prefix(var4, var1);
      } else {
         this.simpleexp(var1);
      }

      int var3;
      int var6;
      for(var3 = this.getbinopr(this.t.token); var3 != 15 && priorityLeft[var3] > var2; var3 = var6) {
         ExpDesc var5 = new ExpDesc();
         this.next();
         this.fs.infix(var3, var1);
         var6 = this.subexpr(var5, priorityRight[var3]);
         this.fs.posfix(var3, var1, var5);
      }

      this.leavelevel();
      return var3;
   }

   void expr(ExpDesc var1) {
      this.subexpr(var1, 0);
   }

   boolean block_follow(int var1) {
      switch(var1) {
      case 260:
      case 261:
      case 262:
      case 276:
      case 287:
         return true;
      default:
         return false;
      }
   }

   void block() {
      FuncState var1 = this.fs;
      BlockCnt var2 = new BlockCnt();
      var1.enterblock(var2, false);
      this.chunk();
      FuncState._assert(var2.breaklist == -1);
      var1.leaveblock();
   }

   void check_conflict(LHS_assign var1, ExpDesc var2) {
      FuncState var3 = this.fs;
      int var4 = var3.freereg;

      boolean var5;
      for(var5 = false; var1 != null; var1 = var1.prev) {
         if (var1.v.k == 9) {
            if (var1.v.info == var2.info) {
               var5 = true;
               var1.v.info = var4;
            }

            if (var1.v.aux == var2.info) {
               var5 = true;
               var1.v.aux = var4;
            }
         }
      }

      if (var5) {
         var3.codeABC(0, var3.freereg, var2.info, 0);
         var3.reserveregs(1);
      }

   }

   void assignment(LHS_assign var1, int var2) {
      ExpDesc var3 = new ExpDesc();
      this.check_condition(6 <= var1.v.k && var1.v.k <= 9, "syntax error");
      if (this.testnext(44)) {
         LHS_assign var4 = new LHS_assign();
         var4.prev = var1;
         this.primaryexp(var4.v);
         if (var4.v.k == 6) {
            this.check_conflict(var1, var4.v);
         }

         this.assignment(var4, var2 + 1);
      } else {
         this.checknext(61);
         int var5 = this.explist1(var3);
         if (var5 == var2) {
            this.fs.setoneret(var3);
            this.fs.storevar(var1.v, var3);
            return;
         }

         this.adjust_assign(var2, var5, var3);
         if (var5 > var2) {
            FuncState var10000 = this.fs;
            var10000.freereg -= var5 - var2;
         }
      }

      var3.init(12, this.fs.freereg - 1);
      this.fs.storevar(var1.v, var3);
   }

   int cond() {
      ExpDesc var1 = new ExpDesc();
      this.expr(var1);
      if (var1.k == 1) {
         var1.k = 3;
      }

      this.fs.goiftrue(var1);
      return var1.f;
   }

   void breakstat() {
      FuncState var1 = this.fs;
      BlockCnt var2 = var1.bl;

      boolean var3;
      for(var3 = false; var2 != null && !var2.isbreakable; var2 = var2.previous) {
         var3 |= var2.upval;
      }

      if (var2 == null) {
         this.syntaxerror("no loop to break");
      }

      if (var3) {
         var1.codeABC(35, var2.nactvar, 0, 0);
      }

      var2.breaklist = var1.concat(var2.breaklist, var1.jump());
   }

   void whilestat(int var1) {
      FuncState var2 = this.fs;
      BlockCnt var5 = new BlockCnt();
      this.next();
      int var3 = var2.getlabel();
      int var4 = this.cond();
      var2.enterblock(var5, true);
      this.checknext(259);
      this.block();
      var2.patchlist(var2.jump(), var3);
      this.check_match(262, 277, var1);
      var2.leaveblock();
      var2.patchtohere(var4);
   }

   void repeatstat(int var1) {
      FuncState var3 = this.fs;
      int var4 = var3.getlabel();
      BlockCnt var5 = new BlockCnt();
      BlockCnt var6 = new BlockCnt();
      var3.enterblock(var5, true);
      var3.enterblock(var6, false);
      this.next();
      this.chunk();
      this.check_match(276, 272, var1);
      int var2 = this.cond();
      if (!var6.upval) {
         var3.leaveblock();
         var3.patchlist(var2, var4);
      } else {
         this.breakstat();
         var3.patchtohere(var2);
         var3.leaveblock();
         var3.patchlist(var3.jump(), var4);
      }

      var3.leaveblock();
   }

   int exp1() {
      ExpDesc var1 = new ExpDesc();
      this.expr(var1);
      int var2 = var1.k;
      this.fs.exp2nextreg(var1);
      return var2;
   }

   void forbody(int var1, int var2, int var3, boolean var4) {
      BlockCnt var5 = new BlockCnt();
      FuncState var6 = this.fs;
      this.adjustlocalvars(3);
      this.checknext(259);
      int var7 = var4 ? var6.codeAsBx(32, var1, -1) : var6.jump();
      var6.enterblock(var5, false);
      this.adjustlocalvars(var3);
      var6.reserveregs(var3);
      this.block();
      var6.leaveblock();
      var6.patchtohere(var7);
      int var8 = var4 ? var6.codeAsBx(31, var1, -1) : var6.codeABC(33, var1, 0, var3);
      var6.fixline(var2);
      var6.patchlist(var4 ? var8 : var6.jump(), var7 + 1);
   }

   void fornum(String var1, int var2) {
      FuncState var3 = this.fs;
      int var4 = var3.freereg;
      this.new_localvarliteral("(for index)", 0);
      this.new_localvarliteral("(for limit)", 1);
      this.new_localvarliteral("(for step)", 2);
      this.new_localvar(var1, 3);
      this.checknext(61);
      this.exp1();
      this.checknext(44);
      this.exp1();
      if (this.testnext(44)) {
         this.exp1();
      } else {
         var3.codeABx(1, var3.freereg, var3.numberK(1.0D));
         var3.reserveregs(1);
      }

      this.forbody(var4, var2, 1, true);
   }

   void forlist(String var1) {
      FuncState var2 = this.fs;
      ExpDesc var3 = new ExpDesc();
      byte var4 = 0;
      int var6 = var2.freereg;
      int var7 = var4 + 1;
      this.new_localvarliteral("(for generator)", var4);
      this.new_localvarliteral("(for state)", var7++);
      this.new_localvarliteral("(for control)", var7++);
      this.new_localvar(var1, var7++);

      while(this.testnext(44)) {
         this.new_localvar(this.str_checkname(), var7++);
      }

      this.checknext(267);
      int var5 = this.linenumber;
      this.adjust_assign(3, this.explist1(var3), var3);
      var2.checkstack(3);
      this.forbody(var6, var5, var7 - 3, false);
   }

   void forstat(int var1) {
      FuncState var2 = this.fs;
      BlockCnt var4 = new BlockCnt();
      var2.enterblock(var4, true);
      this.next();
      String var3 = this.str_checkname();
      switch(this.t.token) {
      case 44:
      case 267:
         this.forlist(var3);
         break;
      case 61:
         this.fornum(var3, var1);
         break;
      default:
         String var10001 = LUA_QL("=");
         this.syntaxerror(var10001 + " or " + LUA_QL("in") + " expected");
      }

      this.check_match(262, 264, var1);
      var2.leaveblock();
   }

   int test_then_block() {
      this.next();
      int var1 = this.cond();
      this.checknext(274);
      this.block();
      return var1;
   }

   void ifstat(int var1) {
      FuncState var2 = this.fs;
      int var4 = -1;

      int var3;
      for(var3 = this.test_then_block(); this.t.token == 261; var3 = this.test_then_block()) {
         var4 = var2.concat(var4, var2.jump());
         var2.patchtohere(var3);
      }

      if (this.t.token == 260) {
         var4 = var2.concat(var4, var2.jump());
         var2.patchtohere(var3);
         this.next();
         this.block();
      } else {
         var4 = var2.concat(var4, var3);
      }

      var2.patchtohere(var4);
      this.check_match(262, 266, var1);
   }

   void localfunc() {
      ExpDesc var1 = new ExpDesc();
      ExpDesc var2 = new ExpDesc();
      FuncState var3 = this.fs;
      this.new_localvar(this.str_checkname(), 0);
      var1.init(6, var3.freereg);
      var3.reserveregs(1);
      this.adjustlocalvars(1);
      this.body(var2, false, this.linenumber);
      var3.storevar(var1, var2);
   }

   void localstat(int var1) {
      int var2 = 0;
      ExpDesc var4 = new ExpDesc();

      do {
         this.new_localvar(this.str_checkname(), var2++, var1);
      } while(this.testnext(44));

      int var3;
      if (this.testnext(61)) {
         var3 = this.explist1(var4);
      } else {
         var4.k = 0;
         var3 = 0;
      }

      this.adjust_assign(var2, var3, var4);
      this.adjustlocalvars(var2);
   }

   boolean funcname(ExpDesc var1) {
      boolean var2 = false;
      this.singlevar(var1);

      while(this.t.token == 46) {
         this.field(var1);
      }

      if (this.t.token == 58) {
         var2 = true;
         this.field(var1);
      }

      return var2;
   }

   void funcstat(int var1) {
      ExpDesc var3 = new ExpDesc();
      ExpDesc var4 = new ExpDesc();
      this.next();
      boolean var2 = this.funcname(var3);
      this.body(var4, var2, var1);
      this.fs.storevar(var3, var4);
      this.fs.fixline(var1);
   }

   void exprstat() {
      FuncState var1 = this.fs;
      LHS_assign var2 = new LHS_assign();
      this.primaryexp(var2.v);
      if (var2.v.k == 13) {
         FuncState.SETARG_C(var1.getcodePtr(var2.v), 1);
      } else {
         var2.prev = null;
         this.assignment(var2, 1);
      }

   }

   void retstat() {
      FuncState var1 = this.fs;
      ExpDesc var2 = new ExpDesc();
      this.next();
      int var3;
      int var4;
      if (!this.block_follow(this.t.token) && this.t.token != 59) {
         var4 = this.explist1(var2);
         if (this.hasmultret(var2.k)) {
            var1.setmultret(var2);
            if (var2.k == 13 && var4 == 1) {
               FuncState.SET_OPCODE(var1.getcodePtr(var2), 29);
               FuncState._assert(FuncState.GETARG_A(var1.getcode(var2)) == var1.nactvar);
            }

            var3 = var1.nactvar;
            var4 = -1;
         } else if (var4 == 1) {
            var3 = var1.exp2anyreg(var2);
         } else {
            var1.exp2nextreg(var2);
            var3 = var1.nactvar;
            FuncState._assert(var4 == var1.freereg - var3);
         }
      } else {
         var4 = 0;
         var3 = 0;
      }

      var1.ret(var3, var4);
   }

   boolean statement() {
      int var1 = this.linenumber;
      switch(this.t.token) {
      case 258:
         this.next();
         this.breakstat();
         return true;
      case 259:
         this.next();
         this.block();
         this.check_match(262, 259, var1);
         return false;
      case 260:
      case 261:
      case 262:
      case 263:
      case 267:
      case 269:
      case 270:
      case 271:
      case 274:
      case 275:
      case 276:
      default:
         this.exprstat();
         return false;
      case 264:
         this.forstat(var1);
         return false;
      case 265:
         this.funcstat(var1);
         return false;
      case 266:
         this.ifstat(var1);
         return false;
      case 268:
         this.next();
         if (this.testnext(265)) {
            this.localfunc();
         } else {
            this.localstat(var1);
         }

         return false;
      case 272:
         this.repeatstat(var1);
         return false;
      case 273:
         this.retstat();
         return true;
      case 277:
         this.whilestat(var1);
         return false;
      }
   }

   void chunk() {
      boolean var1 = false;
      this.enterlevel();

      while(!var1 && !this.block_follow(this.t.token)) {
         var1 = this.statement();
         this.testnext(59);
         FuncState._assert(this.fs.f.maxStacksize >= this.fs.freereg && this.fs.freereg >= this.fs.nactvar);
         this.fs.freereg = this.fs.nactvar;
      }

      this.leavelevel();
   }

   static {
      int var0;
      for(var0 = 0; var0 < RESERVED_LOCAL_VAR_KEYWORDS.length; ++var0) {
         RESERVED_LOCAL_VAR_KEYWORDS_TABLE.put(RESERVED_LOCAL_VAR_KEYWORDS[var0], Boolean.TRUE);
      }

      luaX_tokens = new String[]{"and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while", "..", "...", "==", ">=", "<=", "~=", "<number>", "<name>", "<string>", "<eof>"};
      RESERVED = new Hashtable();

      for(var0 = 0; var0 < 21; ++var0) {
         String var1 = luaX_tokens[var0];
         RESERVED.put(var1, new Integer(257 + var0));
      }

      priorityLeft = new int[]{6, 6, 7, 7, 7, 10, 5, 3, 3, 3, 3, 3, 3, 2, 1};
      priorityRight = new int[]{6, 6, 7, 7, 7, 9, 4, 3, 3, 3, 3, 3, 3, 2, 1};
   }
}
