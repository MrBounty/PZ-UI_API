package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaException;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;
import zombie.Lua.LuaManager;

public final class StringLib implements JavaFunction {
   private static final int SUB = 0;
   private static final int CHAR = 1;
   private static final int BYTE = 2;
   private static final int LOWER = 3;
   private static final int UPPER = 4;
   private static final int REVERSE = 5;
   private static final int FORMAT = 6;
   private static final int FIND = 7;
   private static final int MATCH = 8;
   private static final int GSUB = 9;
   private static final int TRIM = 10;
   private static final int SPLIT = 11;
   private static final int SORT = 12;
   private static final int CONTAINS = 13;
   private static final int NUM_FUNCTIONS = 14;
   private static final boolean[] SPECIALS = new boolean[256];
   private static final int LUA_MAXCAPTURES = 32;
   private static final char L_ESC = '%';
   private static final int CAP_UNFINISHED = -1;
   private static final int CAP_POSITION = -2;
   private static final String[] names;
   private static final StringLib[] functions;
   private static final Class STRING_CLASS;
   private final int methodId;
   private static final char[] digits;

   public StringLib(int var1) {
      this.methodId = var1;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();

      for(int var3 = 0; var3 < 14; ++var3) {
         var2.rawset(names[var3], functions[var3]);
      }

      var2.rawset("__index", var2);
      KahluaTable var4 = KahluaUtil.getClassMetatables(var0, var1);
      var4.rawset(STRING_CLASS, var2);
      var1.rawset("string", var2);
   }

   public String toString() {
      return names[this.methodId];
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.methodId) {
      case 0:
         return this.sub(var1, var2);
      case 1:
         return this.stringChar(var1, var2);
      case 2:
         return this.stringByte(var1, var2);
      case 3:
         return this.lower(var1, var2);
      case 4:
         return this.upper(var1, var2);
      case 5:
         return this.reverse(var1, var2);
      case 6:
         return this.format(var1, var2);
      case 7:
         return findAux(var1, true);
      case 8:
         return findAux(var1, false);
      case 9:
         return gsub(var1, var2);
      case 10:
         return trim(var1, var2);
      case 11:
         return split(var1, var2);
      case 12:
         return sort(var1, var2);
      case 13:
         return this.contains(var1, var2);
      default:
         return 0;
      }
   }

   private long unsigned(long var1) {
      if (var1 < 0L) {
         var1 += 4294967296L;
      }

      return var1;
   }

   private int format(LuaCallFrame var1, int var2) {
      String var3 = KahluaUtil.getStringArg(var1, 1, names[6]);
      int var4 = var3.length();
      int var5 = 2;
      StringBuilder var6 = new StringBuilder();

      label327:
      for(int var7 = 0; var7 < var4; ++var7) {
         char var8 = var3.charAt(var7);
         if (var8 == '%') {
            ++var7;
            KahluaUtil.luaAssert(var7 < var4, "incomplete option to 'format'");
            var8 = var3.charAt(var7);
            if (var8 == '%') {
               var6.append('%');
            } else {
               boolean var9 = false;
               boolean var10 = false;
               boolean var11 = false;
               boolean var12 = false;
               boolean var13 = false;

               while(true) {
                  switch(var8) {
                  case ' ':
                     var13 = true;
                     break;
                  case '#':
                     var9 = true;
                     break;
                  case '+':
                     var12 = true;
                     break;
                  case '-':
                     var11 = true;
                     break;
                  case '0':
                     var10 = true;
                     break;
                  default:
                     int var14;
                     for(var14 = 0; var8 >= '0' && var8 <= '9'; var8 = var3.charAt(var7)) {
                        var14 = 10 * var14 + var8 - 48;
                        ++var7;
                        KahluaUtil.luaAssert(var7 < var4, "incomplete option to 'format'");
                     }

                     int var15 = 0;
                     boolean var16 = false;
                     if (var8 == '.') {
                        var16 = true;
                        ++var7;
                        KahluaUtil.luaAssert(var7 < var4, "incomplete option to 'format'");

                        for(var8 = var3.charAt(var7); var8 >= '0' && var8 <= '9'; var8 = var3.charAt(var7)) {
                           var15 = 10 * var15 + var8 - 48;
                           ++var7;
                           KahluaUtil.luaAssert(var7 < var4, "incomplete option to 'format'");
                        }
                     }

                     if (var11) {
                        var10 = false;
                     }

                     byte var17 = 10;
                     boolean var18 = false;
                     byte var19 = 6;
                     String var20 = "";
                     switch(var8) {
                     case 'E':
                        var18 = true;
                        break;
                     case 'F':
                     case 'H':
                     case 'I':
                     case 'J':
                     case 'K':
                     case 'L':
                     case 'M':
                     case 'N':
                     case 'O':
                     case 'P':
                     case 'Q':
                     case 'R':
                     case 'S':
                     case 'T':
                     case 'U':
                     case 'V':
                     case 'W':
                     case 'Y':
                     case 'Z':
                     case '[':
                     case '\\':
                     case ']':
                     case '^':
                     case '_':
                     case '`':
                     case 'a':
                     case 'b':
                     case 'h':
                     case 'j':
                     case 'k':
                     case 'l':
                     case 'm':
                     case 'n':
                     case 'p':
                     case 'r':
                     case 't':
                     case 'v':
                     case 'w':
                     default:
                        throw new RuntimeException("invalid option '%" + var8 + "' to 'format'");
                     case 'G':
                        var18 = true;
                        break;
                     case 'X':
                        var17 = 16;
                        var19 = 1;
                        var18 = true;
                        var20 = "0X";
                        break;
                     case 'c':
                        var10 = false;
                        break;
                     case 'd':
                     case 'i':
                        var19 = 1;
                     case 'e':
                     case 'f':
                     case 'g':
                        break;
                     case 'o':
                        var17 = 8;
                        var19 = 1;
                        var20 = "0";
                        break;
                     case 'q':
                        var14 = 0;
                        break;
                     case 's':
                        var10 = false;
                        break;
                     case 'u':
                        var19 = 1;
                        break;
                     case 'x':
                        var17 = 16;
                        var19 = 1;
                        var20 = "0x";
                     }

                     if (!var16) {
                        var15 = var19;
                     }

                     if (var16 && var17 != 10) {
                        var10 = false;
                     }

                     int var21 = var10 ? 48 : 32;
                     int var22 = var6.length();
                     if (!var11) {
                        this.extend(var6, var14, (char)var21);
                     }

                     String var23;
                     int var24;
                     Double var32;
                     int var35;
                     double var37;
                     boolean var38;
                     switch(var8) {
                     case 'E':
                     case 'e':
                     case 'f':
                        var32 = this.getDoubleArg(var1, var5);
                        var38 = var32.isInfinite() || var32.isNaN();
                        var37 = var32;
                        if (KahluaUtil.isNegative(var37)) {
                           if (!var38) {
                              var6.append('-');
                           }

                           var37 = -var37;
                        } else if (var12) {
                           var6.append('+');
                        } else if (var13) {
                           var6.append(' ');
                        }

                        if (var38) {
                           var6.append(KahluaUtil.numberToString(var32));
                        } else if (var8 == 'f') {
                           this.appendPrecisionNumber(var6, var37, var15, var9);
                        } else {
                           this.appendScientificNumber(var6, var37, var15, var9, false);
                        }
                        break;
                     case 'F':
                     case 'H':
                     case 'I':
                     case 'J':
                     case 'K':
                     case 'L':
                     case 'M':
                     case 'N':
                     case 'O':
                     case 'P':
                     case 'Q':
                     case 'R':
                     case 'S':
                     case 'T':
                     case 'U':
                     case 'V':
                     case 'W':
                     case 'Y':
                     case 'Z':
                     case '[':
                     case '\\':
                     case ']':
                     case '^':
                     case '_':
                     case '`':
                     case 'a':
                     case 'b':
                     case 'h':
                     case 'j':
                     case 'k':
                     case 'l':
                     case 'm':
                     case 'n':
                     case 'p':
                     case 'r':
                     case 't':
                     case 'v':
                     case 'w':
                     default:
                        throw new RuntimeException("Internal error");
                     case 'G':
                     case 'g':
                        if (var15 <= 0) {
                           var15 = 1;
                        }

                        var32 = this.getDoubleArg(var1, var5);
                        var38 = var32.isInfinite() || var32.isNaN();
                        var37 = var32;
                        if (KahluaUtil.isNegative(var37)) {
                           if (!var38) {
                              var6.append('-');
                           }

                           var37 = -var37;
                        } else if (var12) {
                           var6.append('+');
                        } else if (var13) {
                           var6.append(' ');
                        }

                        if (var38) {
                           var6.append(KahluaUtil.numberToString(var32));
                        } else {
                           double var27 = roundToSignificantNumbers(var37, var15);
                           if (var27 != 0.0D && (!(var27 >= 1.0E-4D) || !(var27 < (double)KahluaUtil.ipow(10L, var15)))) {
                              this.appendScientificNumber(var6, var27, var15 - 1, var9, true);
                              break;
                           }

                           int var29;
                           if (var27 == 0.0D) {
                              var29 = 1;
                           } else if (Math.floor(var27) == 0.0D) {
                              var29 = 0;
                           } else {
                              double var30 = var27;

                              for(var29 = 1; var30 >= 10.0D; ++var29) {
                                 var30 /= 10.0D;
                              }
                           }

                           this.appendSignificantNumber(var6, var27, var15 - var29, var9);
                        }
                        break;
                     case 'X':
                     case 'o':
                     case 'u':
                     case 'x':
                        long var33 = this.getDoubleArg(var1, var5).longValue();
                        var33 = this.unsigned(var33);
                        if (var9) {
                           if (var17 == 8) {
                              var35 = 0;

                              for(long var26 = var33; var26 > 0L; ++var35) {
                                 var26 /= 8L;
                              }

                              if (var15 <= var35) {
                                 var6.append(var20);
                              }
                           } else if (var17 == 16 && var33 != 0L) {
                              var6.append(var20);
                           }
                        }

                        if (var33 != 0L || var15 > 0) {
                           stringBufferAppend(var6, (double)var33, var17, false, var15);
                        }
                        break;
                     case 'c':
                        var6.append((char)this.getDoubleArg(var1, var5).shortValue());
                        break;
                     case 'd':
                     case 'i':
                        var32 = this.getDoubleArg(var1, var5);
                        long var36 = var32.longValue();
                        if (var36 < 0L) {
                           var6.append('-');
                           var36 = -var36;
                        } else if (var12) {
                           var6.append('+');
                        } else if (var13) {
                           var6.append(' ');
                        }

                        if (var36 != 0L || var15 > 0) {
                           stringBufferAppend(var6, (double)var36, var17, false, var15);
                        }
                        break;
                     case 'q':
                        var23 = this.getStringArg(var1, var5);
                        var6.append('"');

                        for(var24 = 0; var24 < var23.length(); ++var24) {
                           char var25 = var23.charAt(var24);
                           switch(var25) {
                           case '\n':
                              var6.append("\\\n");
                              break;
                           case '\r':
                              var6.append("\\r");
                              break;
                           case '"':
                              var6.append("\\\"");
                              break;
                           case '\\':
                              var6.append("\\");
                              break;
                           default:
                              var6.append(var25);
                           }
                        }

                        var6.append('"');
                        break;
                     case 's':
                        var23 = this.getStringArg(var1, var5);
                        var24 = var23.length();
                        if (var16) {
                           var24 = Math.min(var15, var23.length());
                        }

                        this.append(var6, var23, 0, var24);
                     }

                     int var34;
                     if (var11) {
                        var34 = var6.length();
                        var24 = var14 - (var34 - var22);
                        if (var24 > 0) {
                           this.extend(var6, var24, ' ');
                        }
                     } else {
                        var34 = var6.length();
                        var24 = var34 - var22 - var14;
                        var24 = Math.min(var24, var14);
                        if (var24 > 0) {
                           var6.delete(var22, var22 + var24);
                        }

                        if (var10) {
                           var35 = var22 + (var14 - var24);
                           char var39 = var6.charAt(var35);
                           if (var39 == '+' || var39 == '-' || var39 == ' ') {
                              var6.setCharAt(var35, '0');
                              var6.setCharAt(var22, var39);
                           }
                        }
                     }

                     if (var18) {
                        this.stringBufferUpperCase(var6, var22);
                     }

                     ++var5;
                     continue label327;
                  }

                  ++var7;
                  KahluaUtil.luaAssert(var7 < var4, "incomplete option to 'format'");
                  var8 = var3.charAt(var7);
               }
            }
         } else {
            var6.append(var8);
         }
      }

      var1.push(var6.toString());
      return 1;
   }

   private void append(StringBuilder var1, String var2, int var3, int var4) {
      for(int var5 = var3; var5 < var4; ++var5) {
         var1.append(var2.charAt(var5));
      }

   }

   private void extend(StringBuilder var1, int var2, char var3) {
      int var4 = var1.length();
      var1.setLength(var4 + var2);

      for(int var5 = var2 - 1; var5 >= 0; --var5) {
         var1.setCharAt(var4 + var5, var3);
      }

   }

   private void stringBufferUpperCase(StringBuilder var1, int var2) {
      int var3 = var1.length();

      for(int var4 = var2; var4 < var3; ++var4) {
         char var5 = var1.charAt(var4);
         if (var5 >= 'a' && var5 <= 'z') {
            var1.setCharAt(var4, (char)(var5 - 32));
         }
      }

   }

   private static void stringBufferAppend(StringBuilder var0, double var1, int var3, boolean var4, int var5) {
      int var6;
      for(var6 = var0.length(); var1 > 0.0D || var5 > 0; --var5) {
         double var7 = Math.floor(var1 / (double)var3);
         var0.append(digits[(int)(var1 - var7 * (double)var3)]);
         var1 = var7;
      }

      int var14 = var0.length() - 1;
      if (var6 > var14 && var4) {
         var0.append('0');
      } else {
         int var8 = (1 + var14 - var6) / 2;

         for(int var9 = var8 - 1; var9 >= 0; --var9) {
            int var10 = var6 + var9;
            int var11 = var14 - var9;
            char var12 = var0.charAt(var10);
            char var13 = var0.charAt(var11);
            var0.setCharAt(var10, var13);
            var0.setCharAt(var11, var12);
         }
      }

   }

   private void appendPrecisionNumber(StringBuilder var1, double var2, int var4, boolean var5) {
      var2 = roundToPrecision(var2, var4);
      double var6 = Math.floor(var2);
      double var8 = var2 - var6;

      for(int var10 = 0; var10 < var4; ++var10) {
         var8 *= 10.0D;
      }

      var8 = KahluaUtil.round(var6 + var8) - var6;
      stringBufferAppend(var1, var6, 10, true, 0);
      if (var5 || var4 > 0) {
         var1.append('.');
      }

      stringBufferAppend(var1, var8, 10, false, var4);
   }

   private void appendSignificantNumber(StringBuilder var1, double var2, int var4, boolean var5) {
      double var6 = Math.floor(var2);
      stringBufferAppend(var1, var6, 10, true, 0);
      double var8 = roundToSignificantNumbers(var2 - var6, var4);
      boolean var10 = var6 == 0.0D && var8 != 0.0D;
      int var11 = 0;
      int var12 = var4;

      int var13;
      for(var13 = 0; var13 < var12; ++var13) {
         var8 *= 10.0D;
         if (Math.floor(var8) == 0.0D && var8 != 0.0D) {
            ++var11;
            if (var10) {
               ++var12;
            }
         }
      }

      var8 = KahluaUtil.round(var8);
      if (!var5) {
         while(var8 > 0.0D && var8 % 10.0D == 0.0D) {
            var8 /= 10.0D;
            --var4;
         }
      }

      var1.append('.');
      var13 = var1.length();
      this.extend(var1, var11, '0');
      int var14 = var1.length();
      stringBufferAppend(var1, var8, 10, false, 0);
      int var15 = var1.length();
      int var16 = var15 - var14;
      if (var5 && var16 < var4) {
         int var17 = var4 - var16 - var11;
         this.extend(var1, var17, '0');
      }

      if (!var5 && var13 == var1.length()) {
         var1.delete(var13 - 1, var1.length());
      }

   }

   private void appendScientificNumber(StringBuilder var1, double var2, int var4, boolean var5, boolean var6) {
      int var7 = 0;

      int var8;
      for(var8 = 0; var8 < 2; ++var8) {
         if (var2 >= 1.0D) {
            while(var2 >= 10.0D) {
               var2 /= 10.0D;
               ++var7;
            }
         } else {
            while(var2 > 0.0D && var2 < 1.0D) {
               var2 *= 10.0D;
               --var7;
            }
         }

         var2 = roundToPrecision(var2, var4);
      }

      var8 = Math.abs(var7);
      char var9;
      if (var7 >= 0) {
         var9 = '+';
      } else {
         var9 = '-';
      }

      if (var6) {
         this.appendSignificantNumber(var1, var2, var4, var5);
      } else {
         this.appendPrecisionNumber(var1, var2, var4, var5);
      }

      var1.append('e');
      var1.append(var9);
      stringBufferAppend(var1, (double)var8, 10, true, 2);
   }

   private String getStringArg(LuaCallFrame var1, int var2) {
      return this.getStringArg(var1, var2, names[6]);
   }

   private String getStringArg(LuaCallFrame var1, int var2, String var3) {
      return KahluaUtil.getStringArg(var1, var2, var3);
   }

   private Double getDoubleArg(LuaCallFrame var1, int var2) {
      return this.getDoubleArg(var1, var2, names[6]);
   }

   private Double getDoubleArg(LuaCallFrame var1, int var2, String var3) {
      return KahluaUtil.getNumberArg(var1, var2, var3);
   }

   private int lower(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "not enough arguments");
      String var3 = this.getStringArg(var1, 1, names[3]);
      var1.push(var3.toLowerCase());
      return 1;
   }

   private int upper(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "not enough arguments");
      String var3 = this.getStringArg(var1, 1, names[4]);
      var1.push(var3.toUpperCase());
      return 1;
   }

   private int contains(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "not enough arguments");
      String var3 = this.getStringArg(var1, 1, names[13]);
      String var4 = this.getStringArg(var1, 2, names[13]);
      var1.push(var3.contains(var4));
      return 1;
   }

   private int reverse(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "not enough arguments");
      String var3 = this.getStringArg(var1, 1, names[5]);
      var3 = (new StringBuilder(var3)).reverse().toString();
      var1.push(var3);
      return 1;
   }

   private int stringByte(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "not enough arguments");
      String var3 = this.getStringArg(var1, 1, names[2]);
      int var4 = this.nullDefault(1, KahluaUtil.getOptionalNumberArg(var1, 2));
      int var5 = this.nullDefault(var4, KahluaUtil.getOptionalNumberArg(var1, 3));
      int var6 = var3.length();
      if (var4 < 0) {
         var4 += var6 + 1;
      }

      if (var4 <= 0) {
         var4 = 1;
      }

      if (var5 < 0) {
         var5 += var6 + 1;
      } else if (var5 > var6) {
         var5 = var6;
      }

      int var7 = 1 + var5 - var4;
      if (var7 <= 0) {
         return 0;
      } else {
         var1.setTop(var7);
         int var8 = var4 - 1;

         for(int var9 = 0; var9 < var7; ++var9) {
            char var10 = var3.charAt(var8 + var9);
            var1.set(var9, KahluaUtil.toDouble((long)var10));
         }

         return var7;
      }
   }

   private int nullDefault(int var1, Double var2) {
      return var2 == null ? var1 : var2.intValue();
   }

   private int stringChar(LuaCallFrame var1, int var2) {
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5 = this.getDoubleArg(var1, var4 + 1, names[1]).intValue();
         var3.append((char)var5);
      }

      return var1.push(var3.toString());
   }

   private int sub(LuaCallFrame var1, int var2) {
      String var3 = this.getStringArg(var1, 1, names[0]);
      double var4 = this.getDoubleArg(var1, 2, names[0]);
      double var6 = -1.0D;
      if (var2 >= 3) {
         var6 = this.getDoubleArg(var1, 3, names[0]);
      }

      int var9 = (int)var4;
      int var10 = (int)var6;
      int var11 = var3.length();
      if (var9 < 0) {
         var9 = Math.max(var11 + var9 + 1, 1);
      } else if (var9 == 0) {
         var9 = 1;
      }

      if (var10 < 0) {
         var10 = Math.max(0, var10 + var11 + 1);
      } else if (var10 > var11) {
         var10 = var11;
      }

      if (var9 > var10) {
         return var1.push("");
      } else {
         String var8 = var3.substring(var9 - 1, var10);
         return var1.push(var8);
      }
   }

   public static double roundToPrecision(double var0, int var2) {
      double var3 = (double)KahluaUtil.ipow(10L, var2);
      return KahluaUtil.round(var0 * var3) / var3;
   }

   public static double roundToSignificantNumbers(double var0, int var2) {
      if (var0 == 0.0D) {
         return 0.0D;
      } else if (var0 < 0.0D) {
         return -roundToSignificantNumbers(-var0, var2);
      } else {
         double var3 = (double)KahluaUtil.ipow(10L, var2 - 1);
         double var5 = var3 * 10.0D;

         double var7;
         for(var7 = 1.0D; var7 * var0 < var3; var7 *= 10.0D) {
         }

         while(var7 * var0 >= var5) {
            var7 /= 10.0D;
         }

         return KahluaUtil.round(var0 * var7) / var7;
      }
   }

   private static Object push_onecapture(StringLib.MatchState var0, int var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      if (var1 >= var0.level) {
         if (var1 == 0) {
            String var7 = var2.string.substring(var2.index, var3.index);
            var0.callFrame.push(var7);
            return var7;
         } else {
            throw new RuntimeException("invalid capture index");
         }
      } else {
         int var4 = var0.capture[var1].len;
         if (var4 == -1) {
            throw new RuntimeException("unfinished capture");
         } else if (var4 == -2) {
            Double var8 = new Double((double)(var0.src_init.length() - var0.capture[var1].init.length() + 1));
            var0.callFrame.push(var8);
            return var8;
         } else {
            int var5 = var0.capture[var1].init.index;
            String var6 = var0.capture[var1].init.string.substring(var5, var5 + var4);
            var0.callFrame.push(var6);
            return var6;
         }
      }
   }

   private static int push_captures(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      int var3 = var0.level == 0 && var1 != null ? 1 : var0.level;
      KahluaUtil.luaAssert(var3 <= 32, "too many captures");

      for(int var4 = 0; var4 < var3; ++var4) {
         push_onecapture(var0, var4, var1, var2);
      }

      return var3;
   }

   private static boolean noSpecialChars(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 < 256 && SPECIALS[var2]) {
            return false;
         }
      }

      return true;
   }

   private static int findAux(LuaCallFrame var0, boolean var1) {
      String var2 = var1 ? names[7] : names[8];
      String var3 = KahluaUtil.getStringArg(var0, 1, var2);
      String var4 = KahluaUtil.getStringArg(var0, 2, var2);
      Double var5 = KahluaUtil.getOptionalNumberArg(var0, 3);
      boolean var6 = KahluaUtil.boolEval(KahluaUtil.getOptionalArg(var0, 4));
      int var7 = var5 == null ? 0 : var5.intValue() - 1;
      if (var7 < 0) {
         var7 += var3.length();
         if (var7 < 0) {
            var7 = 0;
         }
      } else if (var7 > var3.length()) {
         var7 = var3.length();
      }

      if (var1 && (var6 || noSpecialChars(var4))) {
         int var14 = var3.indexOf(var4, var7);
         if (var14 > -1) {
            return var0.push(KahluaUtil.toDouble((long)(var14 + 1)), KahluaUtil.toDouble((long)(var14 + var4.length())));
         }
      } else {
         StringLib.StringPointer var8 = new StringLib.StringPointer(var3);
         StringLib.StringPointer var9 = new StringLib.StringPointer(var4);
         boolean var10 = false;
         if (var9.getChar() == '^') {
            var10 = true;
            var9.postIncrString(1);
         }

         StringLib.StringPointer var11 = var8.getClone();
         var11.postIncrString(var7);
         StringLib.MatchState var12 = new StringLib.MatchState(var0, var8.getClone(), var8.getStringLength());

         do {
            var12.level = 0;
            StringLib.StringPointer var13;
            if ((var13 = match(var12, var11, var9)) != null) {
               if (var1) {
                  return var0.push(new Double((double)(var8.length() - var11.length() + 1)), new Double((double)(var8.length() - var13.length()))) + push_captures(var12, (StringLib.StringPointer)null, (StringLib.StringPointer)null);
               }

               return push_captures(var12, var11, var13);
            }
         } while(var11.postIncrStringI(1) < var12.endIndex && !var10);
      }

      return var0.pushNil();
   }

   private static StringLib.StringPointer startCapture(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, int var3) {
      int var5 = var0.level;
      KahluaUtil.luaAssert(var5 < 32, "too many captures");
      var0.capture[var5].init = var1.getClone();
      var0.capture[var5].init.setIndex(var1.getIndex());
      var0.capture[var5].len = var3;
      var0.level = var5 + 1;
      StringLib.StringPointer var4;
      if ((var4 = match(var0, var1, var2)) == null) {
         --var0.level;
      }

      return var4;
   }

   private static int captureToClose(StringLib.MatchState var0) {
      int var1 = var0.level;
      --var1;

      while(var1 >= 0) {
         if (var0.capture[var1].len == -1) {
            return var1;
         }

         --var1;
      }

      throw new RuntimeException("invalid pattern capture");
   }

   private static StringLib.StringPointer endCapture(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      int var3 = captureToClose(var0);
      var0.capture[var3].len = var0.capture[var3].init.length() - var1.length();
      StringLib.StringPointer var4;
      if ((var4 = match(var0, var1, var2)) == null) {
         var0.capture[var3].len = -1;
      }

      return var4;
   }

   private static int checkCapture(StringLib.MatchState var0, int var1) {
      var1 -= 49;
      KahluaUtil.luaAssert(var1 < 0 || var1 >= var0.level || var0.capture[var1].len == -1, "invalid capture index");
      return var1;
   }

   private static StringLib.StringPointer matchCapture(StringLib.MatchState var0, StringLib.StringPointer var1, int var2) {
      var2 = checkCapture(var0, var2);
      int var3 = var0.capture[var2].len;
      if (var0.endIndex - var1.length() >= var3 && var0.capture[var2].init.compareTo(var1, var3) == 0) {
         StringLib.StringPointer var4 = var1.getClone();
         var4.postIncrString(var3);
         return var4;
      } else {
         return null;
      }
   }

   private static StringLib.StringPointer matchBalance(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      KahluaUtil.luaAssert(var2.getChar() != 0 && var2.getChar(1) != 0, "unbalanced pattern");
      StringLib.StringPointer var3 = var1.getClone();
      if (var3.getChar() != var2.getChar()) {
         return null;
      } else {
         char var4 = var2.getChar();
         char var5 = var2.getChar(1);
         int var6 = 1;

         while(var3.preIncrStringI(1) < var0.endIndex) {
            if (var3.getChar() == var5) {
               --var6;
               if (var6 == 0) {
                  StringLib.StringPointer var7 = var3.getClone();
                  var7.postIncrString(1);
                  return var7;
               }
            } else if (var3.getChar() == var4) {
               ++var6;
            }
         }

         return null;
      }
   }

   private static StringLib.StringPointer classEnd(StringLib.StringPointer var0) {
      StringLib.StringPointer var1 = var0.getClone();
      switch(var1.postIncrString(1)) {
      case '%':
         KahluaUtil.luaAssert(var1.getChar() != 0, "malformed pattern (ends with '%')");
         var1.postIncrString(1);
         return var1;
      case '[':
         if (var1.getChar() == '^') {
            var1.postIncrString(1);
         }

         do {
            KahluaUtil.luaAssert(var1.getChar() != 0, "malformed pattern (missing ']')");
            if (var1.postIncrString(1) == '%' && var1.getChar() != 0) {
               var1.postIncrString(1);
            }
         } while(var1.getChar() != ']');

         var1.postIncrString(1);
         return var1;
      default:
         return var1;
      }
   }

   private static boolean singleMatch(char var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      switch(var1.getChar()) {
      case '%':
         return matchClass(var1.getChar(1), var0);
      case '.':
         return true;
      case '[':
         StringLib.StringPointer var3 = var2.getClone();
         var3.postIncrString(-1);
         return matchBracketClass(var0, var1, var3);
      default:
         return var1.getChar() == var0;
      }
   }

   private static StringLib.StringPointer minExpand(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      StringLib.StringPointer var4 = var3.getClone();
      StringLib.StringPointer var5 = var1.getClone();
      var4.postIncrString(1);

      while(true) {
         StringLib.StringPointer var6 = match(var0, var5, var4);
         if (var6 != null) {
            return var6;
         }

         if (var5.getIndex() >= var0.endIndex || !singleMatch(var5.getChar(), var2, var3)) {
            return null;
         }

         var5.postIncrString(1);
      }
   }

   private static StringLib.StringPointer maxExpand(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      int var4;
      for(var4 = 0; var1.getIndex() + var4 < var0.endIndex && singleMatch(var1.getChar(var4), var2, var3); ++var4) {
      }

      while(var4 >= 0) {
         StringLib.StringPointer var5 = var1.getClone();
         var5.postIncrString(var4);
         StringLib.StringPointer var6 = var3.getClone();
         var6.postIncrString(1);
         StringLib.StringPointer var7 = match(var0, var5, var6);
         if (var7 != null) {
            return var7;
         }

         --var4;
      }

      return null;
   }

   private static boolean matchBracketClass(char var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      StringLib.StringPointer var3 = var1.getClone();
      StringLib.StringPointer var4 = var2.getClone();
      boolean var5 = true;
      if (var3.getChar(1) == '^') {
         var5 = false;
         var3.postIncrString(1);
      }

      label38:
      do {
         while(var3.preIncrStringI(1) < var4.getIndex()) {
            if (var3.getChar() == '%') {
               var3.postIncrString(1);
               continue label38;
            }

            if (var3.getChar(1) == '-' && var3.getIndex() + 2 < var4.getIndex()) {
               var3.postIncrString(2);
               if (var3.getChar(-2) <= var0 && var0 <= var3.getChar()) {
                  return var5;
               }
            } else if (var3.getChar() == var0) {
               return var5;
            }
         }

         return !var5;
      } while(!matchClass(var3.getChar(), var0));

      return var5;
   }

   private static StringLib.StringPointer match(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      StringLib.StringPointer var3 = var1.getClone();
      StringLib.StringPointer var4 = var2.getClone();
      boolean var5 = true;
      boolean var6 = false;

      while(var5) {
         StringLib.StringPointer var7;
         StringLib.StringPointer var9;
         var5 = false;
         var6 = false;
         label87:
         switch(var4.getChar()) {
         case '\u0000':
            return var3;
         case '$':
            if (var4.getChar(1) == 0) {
               return var3.getIndex() == var0.endIndex ? var3 : null;
            }
         default:
            var6 = true;
            break;
         case '%':
            switch(var4.getChar(1)) {
            case 'b':
               var7 = var4.getClone();
               var7.postIncrString(2);
               var3 = matchBalance(var0, var3, var7);
               if (var3 == null) {
                  return null;
               }

               var4.postIncrString(4);
               var5 = true;
               continue;
            case 'f':
               var4.postIncrString(2);
               KahluaUtil.luaAssert(var4.getChar() == '[', "missing '[' after '%%f' in pattern");
               var7 = classEnd(var4);
               char var8 = var3.getIndex() == var0.src_init.getIndex() ? 0 : var3.getChar(-1);
               var9 = var7.getClone();
               var9.postIncrString(-1);
               if (!matchBracketClass(var8, var4, var9) && matchBracketClass(var3.getChar(), var4, var9)) {
                  var4 = var7;
                  var5 = true;
                  continue;
               }

               return null;
            default:
               if (Character.isDigit(var4.getChar(1))) {
                  var3 = matchCapture(var0, var3, var4.getChar(1));
                  if (var3 == null) {
                     return null;
                  }

                  var4.postIncrString(2);
                  var5 = true;
                  continue;
               }

               var6 = true;
               break label87;
            }
         case '(':
            var7 = var4.getClone();
            if (var4.getChar(1) == ')') {
               var7.postIncrString(2);
               return startCapture(var0, var3, var7, -2);
            }

            var7.postIncrString(1);
            return startCapture(var0, var3, var7, -1);
         case ')':
            var7 = var4.getClone();
            var7.postIncrString(1);
            return endCapture(var0, var3, var7);
         }

         if (var6) {
            var7 = classEnd(var4);
            boolean var12 = var3.getIndex() < var0.endIndex && singleMatch(var3.getChar(), var4, var7);
            switch(var7.getChar()) {
            case '*':
               return maxExpand(var0, var3, var4, var7);
            case '+':
               var9 = var3.getClone();
               var9.postIncrString(1);
               return var12 ? maxExpand(var0, var9, var4, var7) : null;
            case '-':
               return minExpand(var0, var3, var4, var7);
            case '?':
               StringLib.StringPointer var10 = var3.getClone();
               var10.postIncrString(1);
               StringLib.StringPointer var11 = var7.getClone();
               var11.postIncrString(1);
               if (var12 && (var9 = match(var0, var10, var11)) != null) {
                  return var9;
               }

               var4 = var7;
               var7.postIncrString(1);
               var5 = true;
               break;
            default:
               if (!var12) {
                  return null;
               }

               var3.postIncrString(1);
               var4 = var7;
               var5 = true;
            }
         }
      }

      return null;
   }

   private static boolean matchClass(char var0, char var1) {
      char var3 = Character.toLowerCase(var0);
      boolean var2;
      switch(var3) {
      case 'a':
         var2 = Character.isLowerCase(var1) || Character.isUpperCase(var1);
         break;
      case 'b':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'm':
      case 'n':
      case 'o':
      case 'q':
      case 'r':
      case 't':
      case 'v':
      case 'y':
      default:
         return var0 == var1;
      case 'c':
         var2 = isControl(var1);
         break;
      case 'd':
         var2 = Character.isDigit(var1);
         break;
      case 'l':
         var2 = Character.isLowerCase(var1);
         break;
      case 'p':
         var2 = isPunct(var1);
         break;
      case 's':
         var2 = isSpace(var1);
         break;
      case 'u':
         var2 = Character.isUpperCase(var1);
         break;
      case 'w':
         var2 = Character.isLowerCase(var1) || Character.isUpperCase(var1) || Character.isDigit(var1);
         break;
      case 'x':
         var2 = isHex(var1);
         break;
      case 'z':
         var2 = var1 == 0;
      }

      return var3 == var0 == var2;
   }

   private static boolean isPunct(char var0) {
      return var0 >= '!' && var0 <= '/' || var0 >= ':' && var0 <= '@' || var0 >= '[' && var0 <= '`' || var0 >= '{' && var0 <= '~';
   }

   private static boolean isSpace(char var0) {
      return var0 >= '\t' && var0 <= '\r' || var0 == ' ';
   }

   private static boolean isControl(char var0) {
      return var0 >= 0 && var0 <= 31 || var0 == 127;
   }

   private static boolean isHex(char var0) {
      return var0 >= '0' && var0 <= '9' || var0 >= 'a' && var0 <= 'f' || var0 >= 'A' && var0 <= 'F';
   }

   private static int gsub(LuaCallFrame var0, int var1) {
      String var2 = KahluaUtil.getStringArg(var0, 1, names[9]);
      String var3 = KahluaUtil.getStringArg(var0, 2, names[9]);
      Object var4 = KahluaUtil.getArg(var0, 3, names[9]);
      String var5 = KahluaUtil.rawTostring(var4);
      if (var5 != null) {
         var4 = var5;
      }

      Double var14 = KahluaUtil.getOptionalNumberArg(var0, 4);
      int var6 = var14 == null ? Integer.MAX_VALUE : var14.intValue();
      StringLib.StringPointer var7 = new StringLib.StringPointer(var3);
      StringLib.StringPointer var8 = new StringLib.StringPointer(var2);
      boolean var9 = false;
      if (var7.getChar() == '^') {
         var9 = true;
         var7.postIncrString(1);
      }

      if (!(var4 instanceof Double) && !(var4 instanceof String) && !(var4 instanceof LuaClosure) && !(var4 instanceof JavaFunction) && !(var4 instanceof KahluaTable)) {
         KahluaUtil.fail("string/function/table expected, got " + var4);
      }

      StringLib.MatchState var10 = new StringLib.MatchState(var0, var8.getClone(), var8.length());
      int var11 = 0;
      StringBuilder var12 = new StringBuilder();

      while(var11 < var6) {
         var10.level = 0;
         StringLib.StringPointer var13 = match(var10, var8, var7);
         if (var13 != null) {
            ++var11;
            addValue(var10, var4, var12, var8, var13);
         }

         if (var13 != null && var13.getIndex() > var8.getIndex()) {
            var8.setIndex(var13.getIndex());
         } else {
            if (var8.getIndex() >= var10.endIndex) {
               break;
            }

            var12.append(var8.postIncrString(1));
         }

         if (var9) {
            break;
         }
      }

      return var0.push(var12.append(var8.getString()).toString(), new Double((double)var11));
   }

   private static int trim(LuaCallFrame var0, int var1) {
      String var2 = KahluaUtil.getStringArg(var0, 1, names[10]);
      return var0.push(var2.trim());
   }

   private static int split(LuaCallFrame var0, int var1) {
      String var2 = KahluaUtil.getStringArg(var0, 1, names[11]);
      String var3 = KahluaUtil.getStringArg(var0, 2, names[11]);
      String[] var4 = var2.split(var3);
      KahluaTable var5 = LuaManager.platform.newTable();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var5.rawset(var6 + 1, var4[var6]);
      }

      return var0.push(var5);
   }

   private static int sort(LuaCallFrame var0, int var1) {
      String var2 = KahluaUtil.getStringArg(var0, 1, names[12]);
      String var3 = KahluaUtil.getStringArg(var0, 2, names[12]);
      return var0.push(var2.compareTo(var3) > 0);
   }

   private static void addValue(StringLib.MatchState var0, Object var1, StringBuilder var2, StringLib.StringPointer var3, StringLib.StringPointer var4) {
      String var5 = KahluaUtil.rawTostring(var1);
      if (var5 != null) {
         var2.append(addString(var0, var5, var3, var4));
      } else {
         Object var6 = var0.getCapture(0);
         String var7;
         if (var6 != null) {
            var7 = KahluaUtil.rawTostring(var6);
         } else {
            var7 = var3.getStringSubString(var4.getIndex() - var3.getIndex());
         }

         Object var8 = null;
         if (var1 instanceof KahluaTable) {
            var8 = ((KahluaTable)var1).rawget(var7);
         } else {
            var8 = var0.callFrame.getThread().call(var1, var7, (Object)null, (Object)null);
         }

         if (var8 == null) {
            var8 = var7;
         }

         var2.append(KahluaUtil.rawTostring(var8));
      }

   }

   private static String addString(StringLib.MatchState var0, String var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      StringLib.StringPointer var4 = new StringLib.StringPointer(var1);
      StringBuilder var5 = new StringBuilder();

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         char var7 = var4.getChar(var6);
         if (var7 != '%') {
            var5.append(var7);
         } else {
            ++var6;
            var7 = var4.getChar(var6);
            if (!Character.isDigit(var7)) {
               var5.append(var7);
            } else if (var7 == '0') {
               int var8 = var2.getStringLength() - var3.length();
               var5.append(var2.getStringSubString(var8));
            } else {
               Object var9 = var0.getCapture(var7 - 49);
               if (var9 == null) {
                  throw new KahluaException("invalid capture index");
               }

               var5.append(KahluaUtil.tostring(var9, (KahluaThread)null));
            }
         }
      }

      return var5.toString();
   }

   static {
      String var0 = "^$*+?.([%-";

      for(int var1 = 0; var1 < var0.length(); ++var1) {
         SPECIALS[var0.charAt(var1)] = true;
      }

      STRING_CLASS = "".getClass();
      names = new String[14];
      names[0] = "sub";
      names[1] = "char";
      names[2] = "byte";
      names[3] = "lower";
      names[4] = "upper";
      names[5] = "reverse";
      names[6] = "format";
      names[7] = "find";
      names[8] = "match";
      names[9] = "gsub";
      names[10] = "trim";
      names[11] = "split";
      names[12] = "sort";
      names[13] = "contains";
      functions = new StringLib[14];

      for(int var2 = 0; var2 < 14; ++var2) {
         functions[var2] = new StringLib(var2);
      }

      digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   }

   public static class MatchState {
      public final LuaCallFrame callFrame;
      public final StringLib.StringPointer src_init;
      public final int endIndex;
      public final StringLib.MatchState.Capture[] capture;
      public int level;

      public MatchState(LuaCallFrame var1, StringLib.StringPointer var2, int var3) {
         this.callFrame = var1;
         this.src_init = var2;
         this.endIndex = var3;
         this.capture = new StringLib.MatchState.Capture[32];

         for(int var4 = 0; var4 < 32; ++var4) {
            this.capture[var4] = new StringLib.MatchState.Capture();
         }

      }

      public Object getCapture(int var1) {
         if (var1 >= this.level) {
            return null;
         } else {
            return this.capture[var1].len == -2 ? new Double((double)(this.src_init.length() - this.capture[var1].init.length() + 1)) : this.capture[var1].init.getStringSubString(this.capture[var1].len);
         }
      }

      public static class Capture {
         public StringLib.StringPointer init;
         public int len;
      }
   }

   public static class StringPointer {
      private final String string;
      private int index = 0;

      public StringPointer(String var1) {
         this.string = var1;
      }

      public StringPointer(String var1, int var2) {
         this.string = var1;
         this.index = var2;
      }

      public StringLib.StringPointer getClone() {
         return new StringLib.StringPointer(this.string, this.index);
      }

      public int getIndex() {
         return this.index;
      }

      public void setIndex(int var1) {
         this.index = var1;
      }

      public String getString() {
         return this.index == 0 ? this.string : this.string.substring(this.index);
      }

      public int getStringLength() {
         return this.getStringLength(0);
      }

      public int getStringLength(int var1) {
         return this.string.length() - (this.index + var1);
      }

      public String getStringSubString(int var1) {
         return this.string.substring(this.index, this.index + var1);
      }

      public char getChar() {
         return this.getChar(0);
      }

      public char getChar(int var1) {
         int var2 = this.index + var1;
         return var2 >= this.string.length() ? '\u0000' : this.string.charAt(var2);
      }

      public int length() {
         return this.string.length() - this.index;
      }

      public int postIncrStringI(int var1) {
         int var2 = this.index;
         this.index += var1;
         return var2;
      }

      public int preIncrStringI(int var1) {
         this.index += var1;
         return this.index;
      }

      public char postIncrString(int var1) {
         char var2 = this.getChar();
         this.index += var1;
         return var2;
      }

      public int compareTo(StringLib.StringPointer var1, int var2) {
         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.getChar(var3) - var1.getChar(var3);
            if (var4 != 0) {
               return var4;
            }
         }

         return 0;
      }
   }
}
