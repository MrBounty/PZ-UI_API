package org.luaj.kahluafork.compiler;

import java.util.Hashtable;
import se.krka.kahlua.vm.KahluaException;
import se.krka.kahlua.vm.Prototype;

public class FuncState {
   private static final Object NULL_OBJECT = new Object();
   public String[] locvars;
   public String[] upvalues;
   public int linedefined;
   public int lastlinedefined;
   public int isVararg;
   Prototype f;
   Hashtable htable;
   FuncState prev;
   LexState ls;
   BlockCnt bl;
   int pc;
   int lasttarget;
   int jpc;
   int freereg;
   int nk;
   int np;
   int nlocvars;
   int nactvar;
   int[] upvalues_k = new int[60];
   int[] upvalues_info = new int[60];
   short[] actvar = new short[200];
   int[] actvarline = new int[200];
   public static String currentFile;
   public static String currentfullFile;
   public static final int MAXSTACK = 250;
   static final int LUAI_MAXUPVALUES = 60;
   static final int LUAI_MAXVARS = 200;
   static final int OpArgN = 0;
   static final int OpArgU = 1;
   static final int OpArgR = 2;
   static final int OpArgK = 3;
   public static final int LUA_MULTRET = -1;
   public static final int VARARG_HASARG = 1;
   public static final int VARARG_ISVARARG = 2;
   public static final int VARARG_NEEDSARG = 4;
   public static final int iABC = 0;
   public static final int iABx = 1;
   public static final int iAsBx = 2;
   public static final int SIZE_C = 9;
   public static final int SIZE_B = 9;
   public static final int SIZE_Bx = 18;
   public static final int SIZE_A = 8;
   public static final int SIZE_OP = 6;
   public static final int POS_OP = 0;
   public static final int POS_A = 6;
   public static final int POS_C = 14;
   public static final int POS_B = 23;
   public static final int POS_Bx = 14;
   public static final int MAX_OP = 63;
   public static final int MAXARG_A = 255;
   public static final int MAXARG_B = 511;
   public static final int MAXARG_C = 511;
   public static final int MAXARG_Bx = 262143;
   public static final int MAXARG_sBx = 131071;
   public static final int MASK_OP = 63;
   public static final int MASK_A = 16320;
   public static final int MASK_B = -8388608;
   public static final int MASK_C = 8372224;
   public static final int MASK_Bx = -16384;
   public static final int MASK_NOT_OP = -64;
   public static final int MASK_NOT_A = -16321;
   public static final int MASK_NOT_B = 8388607;
   public static final int MASK_NOT_C = -8372225;
   public static final int MASK_NOT_Bx = 16383;
   public static final int BITRK = 256;
   public static final int MAXINDEXRK = 255;
   public static final int NO_REG = 255;
   public static final int OP_MOVE = 0;
   public static final int OP_LOADK = 1;
   public static final int OP_LOADBOOL = 2;
   public static final int OP_LOADNIL = 3;
   public static final int OP_GETUPVAL = 4;
   public static final int OP_GETGLOBAL = 5;
   public static final int OP_GETTABLE = 6;
   public static final int OP_SETGLOBAL = 7;
   public static final int OP_SETUPVAL = 8;
   public static final int OP_SETTABLE = 9;
   public static final int OP_NEWTABLE = 10;
   public static final int OP_SELF = 11;
   public static final int OP_ADD = 12;
   public static final int OP_SUB = 13;
   public static final int OP_MUL = 14;
   public static final int OP_DIV = 15;
   public static final int OP_MOD = 16;
   public static final int OP_POW = 17;
   public static final int OP_UNM = 18;
   public static final int OP_NOT = 19;
   public static final int OP_LEN = 20;
   public static final int OP_CONCAT = 21;
   public static final int OP_JMP = 22;
   public static final int OP_EQ = 23;
   public static final int OP_LT = 24;
   public static final int OP_LE = 25;
   public static final int OP_TEST = 26;
   public static final int OP_TESTSET = 27;
   public static final int OP_CALL = 28;
   public static final int OP_TAILCALL = 29;
   public static final int OP_RETURN = 30;
   public static final int OP_FORLOOP = 31;
   public static final int OP_FORPREP = 32;
   public static final int OP_TFORLOOP = 33;
   public static final int OP_SETLIST = 34;
   public static final int OP_CLOSE = 35;
   public static final int OP_CLOSURE = 36;
   public static final int OP_VARARG = 37;
   public static final int NUM_OPCODES = 38;
   public static final int[] luaP_opmodes = new int[]{96, 113, 84, 96, 80, 113, 108, 49, 16, 60, 84, 108, 124, 124, 124, 124, 124, 124, 96, 96, 96, 104, 34, 188, 188, 188, 228, 228, 84, 84, 16, 98, 98, 132, 20, 0, 81, 80};
   public static final int LFIELDS_PER_FLUSH = 50;

   FuncState(LexState var1) {
      Prototype var2 = new Prototype();
      if (var1.fs != null) {
         var2.name = var1.fs.f.name;
      }

      this.f = var2;
      this.prev = var1.fs;
      this.ls = var1;
      var1.fs = this;
      this.pc = 0;
      this.lasttarget = -1;
      this.jpc = -1;
      this.freereg = 0;
      this.nk = 0;
      this.np = 0;
      this.nlocvars = 0;
      this.nactvar = 0;
      this.bl = null;
      var2.maxStacksize = 2;
      this.htable = new Hashtable();
   }

   FuncState(LexState var1, String var2) {
      Prototype var3 = new Prototype();
      var3.name = var2;
      this.f = var3;
      this.prev = var1.fs;
      this.ls = var1;
      var1.fs = this;
      this.pc = 0;
      this.lasttarget = -1;
      this.jpc = -1;
      this.freereg = 0;
      this.nk = 0;
      this.np = 0;
      this.nlocvars = 0;
      this.nactvar = 0;
      this.bl = null;
      var3.maxStacksize = 2;
      this.htable = new Hashtable();
   }

   InstructionPtr getcodePtr(ExpDesc var1) {
      return new InstructionPtr(this.f.code, var1.info);
   }

   int getcode(ExpDesc var1) {
      return this.f.code[var1.info];
   }

   int codeAsBx(int var1, int var2, int var3) {
      return this.codeABx(var1, var2, var3 + 131071);
   }

   void setmultret(ExpDesc var1) {
      this.setreturns(var1, -1);
   }

   String getlocvar(int var1) {
      return this.locvars[this.actvar[var1]];
   }

   void checklimit(int var1, int var2, String var3) {
      if (var1 > var2) {
         this.errorlimit(var2, var3);
      }

   }

   void errorlimit(int var1, String var2) {
      String var3 = this.linedefined == 0 ? "main function has more than " + var1 + " " + var2 : "function at line " + this.linedefined + " has more than " + var1 + " " + var2;
      this.ls.lexerror(var3, 0);
   }

   int indexupvalue(String var1, ExpDesc var2) {
      for(int var3 = 0; var3 < this.f.numUpvalues; ++var3) {
         if (this.upvalues_k[var3] == var2.k && this.upvalues_info[var3] == var2.info) {
            _assert(this.upvalues[var3].equals(var1));
            return var3;
         }
      }

      this.checklimit(this.f.numUpvalues + 1, 60, "upvalues");
      if (this.upvalues == null || this.f.numUpvalues + 1 > this.upvalues.length) {
         this.upvalues = realloc(this.upvalues, this.f.numUpvalues * 2 + 1);
      }

      _assert(var2.k == 6 || var2.k == 7);
      int var4 = this.f.numUpvalues++;
      this.upvalues[var4] = var1;
      this.upvalues_k[var4] = var2.k;
      this.upvalues_info[var4] = var2.info;
      return var4;
   }

   int searchvar(String var1) {
      for(int var2 = this.nactvar - 1; var2 >= 0; --var2) {
         if (var1.equals(this.getlocvar(var2))) {
            return var2;
         }
      }

      return -1;
   }

   void markupval(int var1) {
      BlockCnt var2;
      for(var2 = this.bl; var2 != null && var2.nactvar > var1; var2 = var2.previous) {
      }

      if (var2 != null) {
         var2.upval = true;
      }

   }

   int singlevaraux(String var1, ExpDesc var2, int var3) {
      int var4 = this.searchvar(var1);
      if (var4 >= 0) {
         var2.init(6, var4);
         if (var3 == 0) {
            this.markupval(var4);
         }

         return 6;
      } else if (this.prev == null) {
         var2.init(8, 255);
         return 8;
      } else if (this.prev.singlevaraux(var1, var2, 0) == 8) {
         return 8;
      } else {
         var2.info = this.indexupvalue(var1, var2);
         var2.k = 7;
         return 7;
      }
   }

   void enterblock(BlockCnt var1, boolean var2) {
      var1.breaklist = -1;
      var1.isbreakable = var2;
      var1.nactvar = this.nactvar;
      var1.upval = false;
      var1.previous = this.bl;
      this.bl = var1;
      _assert(this.freereg == this.nactvar);
   }

   void leaveblock() {
      BlockCnt var1 = this.bl;
      this.bl = var1.previous;
      this.ls.removevars(var1.nactvar);
      if (var1.upval) {
         this.codeABC(35, var1.nactvar, 0, 0);
      }

      _assert(!var1.isbreakable || !var1.upval);
      _assert(var1.nactvar == this.nactvar);
      this.freereg = this.nactvar;
      this.patchtohere(var1.breaklist);
   }

   void closelistfield(ConsControl var1) {
      if (var1.v.k != 0) {
         this.exp2nextreg(var1.v);
         var1.v.k = 0;
         if (var1.tostore == 50) {
            this.setlist(var1.t.info, var1.na, var1.tostore);
            var1.tostore = 0;
         }

      }
   }

   boolean hasmultret(int var1) {
      return var1 == 13 || var1 == 14;
   }

   void lastlistfield(ConsControl var1) {
      if (var1.tostore != 0) {
         if (this.hasmultret(var1.v.k)) {
            this.setmultret(var1.v);
            this.setlist(var1.t.info, var1.na, -1);
            --var1.na;
         } else {
            if (var1.v.k != 0) {
               this.exp2nextreg(var1.v);
            }

            this.setlist(var1.t.info, var1.na, var1.tostore);
         }

      }
   }

   void nil(int var1, int var2) {
      if (this.pc > this.lasttarget) {
         if (this.pc == 0) {
            if (var1 >= this.nactvar) {
               return;
            }
         } else {
            InstructionPtr var3 = new InstructionPtr(this.f.code, this.pc - 1);
            if (GET_OPCODE(var3.get()) == 3) {
               int var4 = GETARG_A(var3.get());
               int var5 = GETARG_B(var3.get());
               if (var4 <= var1 && var1 <= var5 + 1) {
                  if (var1 + var2 - 1 > var5) {
                     SETARG_B(var3, var1 + var2 - 1);
                  }

                  return;
               }
            }
         }
      }

      this.codeABC(3, var1, var1 + var2 - 1, 0);
   }

   int jump() {
      int var1 = this.jpc;
      this.jpc = -1;
      int var2 = this.codeAsBx(22, 0, -1);
      var2 = this.concat(var2, var1);
      return var2;
   }

   void ret(int var1, int var2) {
      this.codeABC(30, var1, var2 + 1, 0);
   }

   int condjump(int var1, int var2, int var3, int var4) {
      this.codeABC(var1, var2, var3, var4);
      return this.jump();
   }

   void fixjump(int var1, int var2) {
      InstructionPtr var3 = new InstructionPtr(this.f.code, var1);
      int var4 = var2 - (var1 + 1);
      _assert(var2 != -1);
      if (Math.abs(var4) > 131071) {
         this.ls.syntaxerror("control structure too long");
      }

      SETARG_sBx(var3, var4);
   }

   int getlabel() {
      this.lasttarget = this.pc;
      return this.pc;
   }

   int getjump(int var1) {
      int var2 = GETARG_sBx(this.f.code[var1]);
      return var2 == -1 ? -1 : var1 + 1 + var2;
   }

   InstructionPtr getjumpcontrol(int var1) {
      InstructionPtr var2 = new InstructionPtr(this.f.code, var1);
      return var1 >= 1 && testTMode(GET_OPCODE(var2.code[var2.idx - 1])) ? new InstructionPtr(var2.code, var2.idx - 1) : var2;
   }

   boolean need_value(int var1) {
      while(var1 != -1) {
         int var2 = this.getjumpcontrol(var1).get();
         if (GET_OPCODE(var2) != 27) {
            return true;
         }

         var1 = this.getjump(var1);
      }

      return false;
   }

   boolean patchtestreg(int var1, int var2) {
      InstructionPtr var3 = this.getjumpcontrol(var1);
      if (GET_OPCODE(var3.get()) != 27) {
         return false;
      } else {
         if (var2 != 255 && var2 != GETARG_B(var3.get())) {
            SETARG_A(var3, var2);
         } else {
            var3.set(CREATE_ABC(26, GETARG_B(var3.get()), 0, GETARG_C(var3.get())));
         }

         return true;
      }
   }

   void removevalues(int var1) {
      while(var1 != -1) {
         this.patchtestreg(var1, 255);
         var1 = this.getjump(var1);
      }

   }

   void patchlistaux(int var1, int var2, int var3, int var4) {
      int var5;
      for(; var1 != -1; var1 = var5) {
         var5 = this.getjump(var1);
         if (this.patchtestreg(var1, var3)) {
            this.fixjump(var1, var2);
         } else {
            this.fixjump(var1, var4);
         }
      }

   }

   void dischargejpc() {
      this.patchlistaux(this.jpc, this.pc, 255, this.pc);
      this.jpc = -1;
   }

   void patchlist(int var1, int var2) {
      if (var2 == this.pc) {
         this.patchtohere(var1);
      } else {
         _assert(var2 < this.pc);
         this.patchlistaux(var1, var2, 255, var2);
      }

   }

   void patchtohere(int var1) {
      this.getlabel();
      this.jpc = this.concat(this.jpc, var1);
   }

   int concat(int var1, int var2) {
      if (var2 == -1) {
         return var1;
      } else {
         if (var1 == -1) {
            var1 = var2;
         } else {
            int var3;
            int var4;
            for(var3 = var1; (var4 = this.getjump(var3)) != -1; var3 = var4) {
            }

            this.fixjump(var3, var2);
         }

         return var1;
      }
   }

   void checkstack(int var1) {
      int var2 = this.freereg + var1;
      if (var2 > this.f.maxStacksize) {
         if (var2 >= 250) {
            this.ls.syntaxerror("function or expression too complex");
         }

         this.f.maxStacksize = var2;
      }

   }

   void reserveregs(int var1) {
      this.checkstack(var1);
      this.freereg += var1;
   }

   void freereg(int var1) {
      if (!ISK(var1) && var1 >= this.nactvar) {
         --this.freereg;
         _assert(var1 == this.freereg);
      }

   }

   void freeexp(ExpDesc var1) {
      if (var1.k == 12) {
         this.freereg(var1.info);
      }

   }

   int addk(Object var1) {
      int var2;
      if (this.htable.containsKey(var1)) {
         var2 = (Integer)this.htable.get(var1);
      } else {
         var2 = this.nk;
         this.htable.put(var1, new Integer(var2));
         Prototype var3 = this.f;
         if (var3.constants == null || this.nk + 1 >= var3.constants.length) {
            var3.constants = realloc(var3.constants, this.nk * 2 + 1);
         }

         if (var1 == NULL_OBJECT) {
            var1 = null;
         }

         var3.constants[this.nk++] = var1;
      }

      return var2;
   }

   int stringK(String var1) {
      return this.addk(var1);
   }

   int numberK(double var1) {
      return this.addk(new Double(var1));
   }

   int boolK(boolean var1) {
      return this.addk(var1 ? Boolean.TRUE : Boolean.FALSE);
   }

   int nilK() {
      return this.addk(NULL_OBJECT);
   }

   void setreturns(ExpDesc var1, int var2) {
      if (var1.k == 13) {
         SETARG_C(this.getcodePtr(var1), var2 + 1);
      } else if (var1.k == 14) {
         SETARG_B(this.getcodePtr(var1), var2 + 1);
         SETARG_A(this.getcodePtr(var1), this.freereg);
         this.reserveregs(1);
      }

   }

   void setoneret(ExpDesc var1) {
      if (var1.k == 13) {
         var1.k = 12;
         var1.info = GETARG_A(this.getcode(var1));
      } else if (var1.k == 14) {
         SETARG_B(this.getcodePtr(var1), 2);
         var1.k = 11;
      }

   }

   void dischargevars(ExpDesc var1) {
      switch(var1.k) {
      case 6:
         var1.k = 12;
         break;
      case 7:
         var1.info = this.codeABC(4, 0, var1.info, 0);
         var1.k = 11;
         break;
      case 8:
         var1.info = this.codeABx(5, 0, var1.info);
         var1.k = 11;
         break;
      case 9:
         this.freereg(var1.aux);
         this.freereg(var1.info);
         var1.info = this.codeABC(6, 0, var1.info, var1.aux);
         var1.k = 11;
      case 10:
      case 11:
      case 12:
      default:
         break;
      case 13:
      case 14:
         this.setoneret(var1);
      }

   }

   int code_label(int var1, int var2, int var3) {
      this.getlabel();
      return this.codeABC(2, var1, var2, var3);
   }

   void discharge2reg(ExpDesc var1, int var2) {
      this.dischargevars(var1);
      switch(var1.k) {
      case 1:
         this.nil(var2, 1);
         break;
      case 2:
      case 3:
         this.codeABC(2, var2, var1.k == 2 ? 1 : 0, 0);
         break;
      case 4:
         this.codeABx(1, var2, var1.info);
         break;
      case 5:
         this.codeABx(1, var2, this.numberK(var1.nval()));
         break;
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      default:
         _assert(var1.k == 0 || var1.k == 10);
         return;
      case 11:
         InstructionPtr var3 = this.getcodePtr(var1);
         SETARG_A(var3, var2);
         break;
      case 12:
         if (var2 != var1.info) {
            this.codeABC(0, var2, var1.info, 0);
         }
      }

      var1.info = var2;
      var1.k = 12;
   }

   void discharge2anyreg(ExpDesc var1) {
      if (var1.k != 12) {
         this.reserveregs(1);
         this.discharge2reg(var1, this.freereg - 1);
      }

   }

   void exp2reg(ExpDesc var1, int var2) {
      this.discharge2reg(var1, var2);
      if (var1.k == 10) {
         var1.t = this.concat(var1.t, var1.info);
      }

      if (var1.hasjumps()) {
         int var4 = -1;
         int var5 = -1;
         if (this.need_value(var1.t) || this.need_value(var1.f)) {
            int var6 = var1.k == 10 ? -1 : this.jump();
            var4 = this.code_label(var2, 0, 1);
            var5 = this.code_label(var2, 1, 0);
            this.patchtohere(var6);
         }

         int var3 = this.getlabel();
         this.patchlistaux(var1.f, var3, var2, var4);
         this.patchlistaux(var1.t, var3, var2, var5);
      }

      var1.f = var1.t = -1;
      var1.info = var2;
      var1.k = 12;
   }

   void exp2nextreg(ExpDesc var1) {
      this.dischargevars(var1);
      this.freeexp(var1);
      this.reserveregs(1);
      this.exp2reg(var1, this.freereg - 1);
   }

   int exp2anyreg(ExpDesc var1) {
      this.dischargevars(var1);
      if (var1.k == 12) {
         if (!var1.hasjumps()) {
            return var1.info;
         }

         if (var1.info >= this.nactvar) {
            this.exp2reg(var1, var1.info);
            return var1.info;
         }
      }

      this.exp2nextreg(var1);
      return var1.info;
   }

   void exp2val(ExpDesc var1) {
      if (var1.hasjumps()) {
         this.exp2anyreg(var1);
      } else {
         this.dischargevars(var1);
      }

   }

   int exp2RK(ExpDesc var1) {
      this.exp2val(var1);
      switch(var1.k) {
      case 1:
      case 2:
      case 3:
      case 5:
         if (this.nk <= 255) {
            var1.info = var1.k == 1 ? this.nilK() : (var1.k == 5 ? this.numberK(var1.nval()) : this.boolK(var1.k == 2));
            var1.k = 4;
            return RKASK(var1.info);
         }
         break;
      case 4:
         if (var1.info <= 255) {
            return RKASK(var1.info);
         }
      }

      return this.exp2anyreg(var1);
   }

   void storevar(ExpDesc var1, ExpDesc var2) {
      int var3;
      switch(var1.k) {
      case 6:
         this.freeexp(var2);
         this.exp2reg(var2, var1.info);
         return;
      case 7:
         var3 = this.exp2anyreg(var2);
         this.codeABC(8, var3, var1.info, 0);
         break;
      case 8:
         var3 = this.exp2anyreg(var2);
         this.codeABx(7, var3, var1.info);
         break;
      case 9:
         var3 = this.exp2RK(var2);
         this.codeABC(9, var1.info, var1.aux, var3);
         break;
      default:
         _assert(false);
      }

      this.freeexp(var2);
   }

   void self(ExpDesc var1, ExpDesc var2) {
      this.exp2anyreg(var1);
      this.freeexp(var1);
      int var3 = this.freereg;
      this.reserveregs(2);
      this.codeABC(11, var3, var1.info, this.exp2RK(var2));
      this.freeexp(var2);
      var1.info = var3;
      var1.k = 12;
   }

   void invertjump(ExpDesc var1) {
      InstructionPtr var2 = this.getjumpcontrol(var1.info);
      _assert(testTMode(GET_OPCODE(var2.get())) && GET_OPCODE(var2.get()) != 27 && GET_OPCODE(var2.get()) != 26);
      int var3 = GETARG_A(var2.get());
      int var4 = var3 != 0 ? 0 : 1;
      SETARG_A(var2, var4);
   }

   int jumponcond(ExpDesc var1, int var2) {
      if (var1.k == 11) {
         int var3 = this.getcode(var1);
         if (GET_OPCODE(var3) == 19) {
            --this.pc;
            return this.condjump(26, GETARG_B(var3), 0, var2 != 0 ? 0 : 1);
         }
      }

      this.discharge2anyreg(var1);
      this.freeexp(var1);
      return this.condjump(27, 255, var1.info, var2);
   }

   void goiftrue(ExpDesc var1) {
      this.dischargevars(var1);
      int var2;
      switch(var1.k) {
      case 2:
      case 4:
      case 5:
         var2 = -1;
         break;
      case 3:
         var2 = this.jump();
         break;
      case 6:
      case 7:
      case 8:
      case 9:
      default:
         var2 = this.jumponcond(var1, 0);
         break;
      case 10:
         this.invertjump(var1);
         var2 = var1.info;
      }

      var1.f = this.concat(var1.f, var2);
      this.patchtohere(var1.t);
      var1.t = -1;
   }

   void goiffalse(ExpDesc var1) {
      this.dischargevars(var1);
      int var2;
      switch(var1.k) {
      case 1:
      case 3:
         var2 = -1;
         break;
      case 2:
         var2 = this.jump();
         break;
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      default:
         var2 = this.jumponcond(var1, 1);
         break;
      case 10:
         var2 = var1.info;
      }

      var1.t = this.concat(var1.t, var2);
      this.patchtohere(var1.f);
      var1.f = -1;
   }

   void codenot(ExpDesc var1) {
      this.dischargevars(var1);
      switch(var1.k) {
      case 1:
      case 3:
         var1.k = 2;
         break;
      case 2:
      case 4:
      case 5:
         var1.k = 3;
         break;
      case 6:
      case 7:
      case 8:
      case 9:
      default:
         _assert(false);
         break;
      case 10:
         this.invertjump(var1);
         break;
      case 11:
      case 12:
         this.discharge2anyreg(var1);
         this.freeexp(var1);
         var1.info = this.codeABC(19, 0, var1.info, 0);
         var1.k = 11;
      }

      int var2 = var1.f;
      var1.f = var1.t;
      var1.t = var2;
      this.removevalues(var1.f);
      this.removevalues(var1.t);
   }

   void indexed(ExpDesc var1, ExpDesc var2) {
      var1.aux = this.exp2RK(var2);
      var1.k = 9;
   }

   boolean constfolding(int var1, ExpDesc var2, ExpDesc var3) {
      if (var2.isnumeral() && var3.isnumeral()) {
         double var4 = var2.nval();
         double var6 = var3.nval();
         double var8;
         switch(var1) {
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
            var8 = var4 % var6;
            break;
         case 17:
            return false;
         case 18:
            var8 = -var4;
            break;
         case 19:
         default:
            _assert(false);
            return false;
         case 20:
            return false;
         }

         if (!Double.isNaN(var8) && !Double.isInfinite(var8)) {
            var2.setNval(var8);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   void codearith(int var1, ExpDesc var2, ExpDesc var3) {
      if (!this.constfolding(var1, var2, var3)) {
         int var4 = var1 != 18 && var1 != 20 ? this.exp2RK(var3) : 0;
         int var5 = this.exp2RK(var2);
         if (var5 > var4) {
            this.freeexp(var2);
            this.freeexp(var3);
         } else {
            this.freeexp(var3);
            this.freeexp(var2);
         }

         var2.info = this.codeABC(var1, 0, var5, var4);
         var2.k = 11;
      }
   }

   void codecomp(int var1, int var2, ExpDesc var3, ExpDesc var4) {
      int var5 = this.exp2RK(var3);
      int var6 = this.exp2RK(var4);
      this.freeexp(var4);
      this.freeexp(var3);
      if (var2 == 0 && var1 != 23) {
         int var7 = var5;
         var5 = var6;
         var6 = var7;
         var2 = 1;
      }

      var3.info = this.condjump(var1, var2, var5, var6);
      var3.k = 10;
   }

   void prefix(int var1, ExpDesc var2) {
      ExpDesc var3 = new ExpDesc();
      var3.init(5, 0);
      switch(var1) {
      case 0:
         if (var2.k == 4) {
            this.exp2anyreg(var2);
         }

         this.codearith(18, var2, var3);
         break;
      case 1:
         this.codenot(var2);
         break;
      case 2:
         this.exp2anyreg(var2);
         this.codearith(20, var2, var3);
         break;
      default:
         _assert(false);
      }

   }

   void infix(int var1, ExpDesc var2) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
         if (!var2.isnumeral()) {
            this.exp2RK(var2);
         }
         break;
      case 6:
         this.exp2nextreg(var2);
         break;
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      default:
         this.exp2RK(var2);
         break;
      case 13:
         this.goiftrue(var2);
         break;
      case 14:
         this.goiffalse(var2);
      }

   }

   void posfix(int var1, ExpDesc var2, ExpDesc var3) {
      switch(var1) {
      case 0:
         this.codearith(12, var2, var3);
         break;
      case 1:
         this.codearith(13, var2, var3);
         break;
      case 2:
         this.codearith(14, var2, var3);
         break;
      case 3:
         this.codearith(15, var2, var3);
         break;
      case 4:
         this.codearith(16, var2, var3);
         break;
      case 5:
         this.codearith(17, var2, var3);
         break;
      case 6:
         this.exp2val(var3);
         if (var3.k == 11 && GET_OPCODE(this.getcode(var3)) == 21) {
            _assert(var2.info == GETARG_B(this.getcode(var3)) - 1);
            this.freeexp(var2);
            SETARG_B(this.getcodePtr(var3), var2.info);
            var2.k = 11;
            var2.info = var3.info;
         } else {
            this.exp2nextreg(var3);
            this.codearith(21, var2, var3);
         }
         break;
      case 7:
         this.codecomp(23, 0, var2, var3);
         break;
      case 8:
         this.codecomp(23, 1, var2, var3);
         break;
      case 9:
         this.codecomp(24, 1, var2, var3);
         break;
      case 10:
         this.codecomp(25, 1, var2, var3);
         break;
      case 11:
         this.codecomp(24, 0, var2, var3);
         break;
      case 12:
         this.codecomp(25, 0, var2, var3);
         break;
      case 13:
         _assert(var2.t == -1);
         this.dischargevars(var3);
         var3.f = this.concat(var3.f, var2.f);
         var2.setvalue(var3);
         break;
      case 14:
         _assert(var2.f == -1);
         this.dischargevars(var3);
         var3.t = this.concat(var3.t, var2.t);
         var2.setvalue(var3);
         break;
      default:
         _assert(false);
      }

   }

   void fixline(int var1) {
      this.f.lines[this.pc - 1] = var1;
   }

   int code(int var1, int var2) {
      Prototype var3 = this.f;
      this.dischargejpc();
      if (var3.code == null || this.pc + 1 > var3.code.length) {
         var3.code = realloc(var3.code, this.pc * 2 + 1);
      }

      var3.code[this.pc] = var1;
      if (var3.lines == null || this.pc + 1 > var3.lines.length) {
         var3.lines = realloc(var3.lines, this.pc * 2 + 1);
      }

      var3.lines[this.pc] = var2;
      var3.file = currentFile;
      var3.filename = currentfullFile;
      return this.pc++;
   }

   int codeABC(int var1, int var2, int var3, int var4) {
      _assert(getOpMode(var1) == 0);
      _assert(getBMode(var1) != 0 || var3 == 0);
      _assert(getCMode(var1) != 0 || var4 == 0);
      return this.code(CREATE_ABC(var1, var2, var3, var4), this.ls.lastline);
   }

   int codeABx(int var1, int var2, int var3) {
      _assert(getOpMode(var1) == 1 || getOpMode(var1) == 2);
      _assert(getCMode(var1) == 0);
      return this.code(CREATE_ABx(var1, var2, var3), this.ls.lastline);
   }

   void setlist(int var1, int var2, int var3) {
      int var4 = (var2 - 1) / 50 + 1;
      int var5 = var3 == -1 ? 0 : var3;
      _assert(var3 != 0);
      if (var4 <= 511) {
         this.codeABC(34, var1, var5, var4);
      } else {
         this.codeABC(34, var1, var5, 0);
         this.code(var4, this.ls.lastline);
      }

      this.freereg = var1 + 1;
   }

   protected static void _assert(boolean var0) {
      if (!var0) {
         throw new KahluaException("compiler assert failed");
      }
   }

   static void SET_OPCODE(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -64 | var1 << 0 & 63);
   }

   static void SETARG_A(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -16321 | var1 << 6 & 16320);
   }

   static void SETARG_B(InstructionPtr var0, int var1) {
      var0.set(var0.get() & 8388607 | var1 << 23 & -8388608);
   }

   static void SETARG_C(InstructionPtr var0, int var1) {
      var0.set(var0.get() & -8372225 | var1 << 14 & 8372224);
   }

   static void SETARG_Bx(InstructionPtr var0, int var1) {
      var0.set(var0.get() & 16383 | var1 << 14 & -16384);
   }

   static void SETARG_sBx(InstructionPtr var0, int var1) {
      SETARG_Bx(var0, var1 + 131071);
   }

   static int CREATE_ABC(int var0, int var1, int var2, int var3) {
      return var0 << 0 & 63 | var1 << 6 & 16320 | var2 << 23 & -8388608 | var3 << 14 & 8372224;
   }

   static int CREATE_ABx(int var0, int var1, int var2) {
      return var0 << 0 & 63 | var1 << 6 & 16320 | var2 << 14 & -16384;
   }

   static Object[] realloc(Object[] var0, int var1) {
      Object[] var2 = new Object[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static String[] realloc(String[] var0, int var1) {
      String[] var2 = new String[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static Prototype[] realloc(Prototype[] var0, int var1) {
      Prototype[] var2 = new Prototype[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static int[] realloc(int[] var0, int var1) {
      int[] var2 = new int[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   static byte[] realloc(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      if (var0 != null) {
         System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      }

      return var2;
   }

   public static int GET_OPCODE(int var0) {
      return var0 >> 0 & 63;
   }

   public static int GETARG_A(int var0) {
      return var0 >> 6 & 255;
   }

   public static int GETARG_B(int var0) {
      return var0 >> 23 & 511;
   }

   public static int GETARG_C(int var0) {
      return var0 >> 14 & 511;
   }

   public static int GETARG_Bx(int var0) {
      return var0 >> 14 & 262143;
   }

   public static int GETARG_sBx(int var0) {
      return (var0 >> 14 & 262143) - 131071;
   }

   public static boolean ISK(int var0) {
      return 0 != (var0 & 256);
   }

   public static int INDEXK(int var0) {
      return var0 & -257;
   }

   public static int RKASK(int var0) {
      return var0 | 256;
   }

   public static int getOpMode(int var0) {
      return luaP_opmodes[var0] & 3;
   }

   public static int getBMode(int var0) {
      return luaP_opmodes[var0] >> 4 & 3;
   }

   public static int getCMode(int var0) {
      return luaP_opmodes[var0] >> 2 & 3;
   }

   public static boolean testTMode(int var0) {
      return 0 != (luaP_opmodes[var0] & 128);
   }
}
