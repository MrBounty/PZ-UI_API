package zombie.characters.action.conditions;

import org.w3c.dom.Element;
import zombie.characters.action.ActionContext;
import zombie.characters.action.IActionCondition;
import zombie.core.Core;
import zombie.core.skinnedmodel.advancedanimation.IAnimatable;
import zombie.core.skinnedmodel.advancedanimation.IAnimationVariableSource;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.util.StringUtils;

public final class CharacterVariableCondition implements IActionCondition {
   private CharacterVariableCondition.Operator op;
   private Object lhsValue;
   private Object rhsValue;

   private static Object parseValue(String var0, boolean var1) {
      if (var0.length() <= 0) {
         return var0;
      } else {
         char var2 = var0.charAt(0);
         int var4;
         char var5;
         if (var2 != '-' && var2 != '+' && (var2 < '0' || var2 > '9')) {
            if (!var0.equalsIgnoreCase("true") && !var0.equalsIgnoreCase("yes")) {
               if (!var0.equalsIgnoreCase("false") && !var0.equalsIgnoreCase("no")) {
                  if (var1) {
                     if (var2 != '\'' && var2 != '"') {
                        return new CharacterVariableCondition.CharacterVariableLookup(var0);
                     } else {
                        StringBuilder var8 = new StringBuilder(var0.length() - 2);

                        for(var4 = 1; var4 < var0.length(); ++var4) {
                           var5 = var0.charAt(var4);
                           switch(var5) {
                           case '"':
                           case '\'':
                              if (var5 == var2) {
                                 return var8.toString();
                              }
                           default:
                              var8.append(var5);
                              break;
                           case '\\':
                              var8.append(var0.charAt(var4));
                           }
                        }

                        return var8.toString();
                     }
                  } else {
                     return var0;
                  }
               } else {
                  return false;
               }
            } else {
               return true;
            }
         } else {
            int var3 = 0;
            if (var2 >= '0' && var2 <= '9') {
               var3 = var2 - 48;
            }

            for(var4 = 1; var4 < var0.length(); ++var4) {
               var5 = var0.charAt(var4);
               if (var5 >= '0' && var5 <= '9') {
                  var3 = var3 * 10 + (var5 - 48);
               } else if (var5 != ',') {
                  if (var5 != '.') {
                     return var0;
                  }

                  ++var4;
                  break;
               }
            }

            if (var4 == var0.length()) {
               return var3;
            } else {
               float var9 = (float)var3;

               for(float var6 = 10.0F; var4 < var0.length(); ++var4) {
                  char var7 = var0.charAt(var4);
                  if (var7 >= '0' && var7 <= '9') {
                     var9 += (float)(var7 - 48) / var6;
                     var6 *= 10.0F;
                  } else if (var7 != ',') {
                     return var0;
                  }
               }

               if (var2 == '-') {
                  var9 *= -1.0F;
               }

               return var9;
            }
         }
      }
   }

   private boolean load(Element var1) {
      String var2 = var1.getNodeName();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1971989233:
         if (var2.equals("gtrEqual")) {
            var3 = 8;
         }
         break;
      case -1295482945:
         if (var2.equals("equals")) {
            var3 = 5;
         }
         break;
      case -1180085800:
         if (var2.equals("isTrue")) {
            var3 = 0;
         }
         break;
      case 102693:
         if (var2.equals("gtr")) {
            var3 = 3;
         }
         break;
      case 3318169:
         if (var2.equals("less")) {
            var3 = 4;
         }
         break;
      case 341896475:
         if (var2.equals("lessEqual")) {
            var3 = 7;
         }
         break;
      case 881486962:
         if (var2.equals("notEquals")) {
            var3 = 6;
         }
         break;
      case 950484197:
         if (var2.equals("compare")) {
            var3 = 2;
         }
         break;
      case 2058602009:
         if (var2.equals("isFalse")) {
            var3 = 1;
         }
      }

      switch(var3) {
      case 0:
         this.op = CharacterVariableCondition.Operator.Equal;
         this.lhsValue = new CharacterVariableCondition.CharacterVariableLookup(var1.getTextContent().trim());
         this.rhsValue = true;
         return true;
      case 1:
         this.op = CharacterVariableCondition.Operator.Equal;
         this.lhsValue = new CharacterVariableCondition.CharacterVariableLookup(var1.getTextContent().trim());
         this.rhsValue = false;
         return true;
      case 2:
         String var4 = var1.getAttribute("op").trim();
         byte var5 = -1;
         switch(var4.hashCode()) {
         case 60:
            if (var4.equals("<")) {
               var5 = 5;
            }
            break;
         case 61:
            if (var4.equals("=")) {
               var5 = 1;
            }
            break;
         case 62:
            if (var4.equals(">")) {
               var5 = 6;
            }
            break;
         case 1084:
            if (var4.equals("!=")) {
               var5 = 3;
            }
            break;
         case 1921:
            if (var4.equals("<=")) {
               var5 = 7;
            }
            break;
         case 1922:
            if (var4.equals("<>")) {
               var5 = 4;
            }
            break;
         case 1952:
            if (var4.equals("==")) {
               var5 = 2;
            }
            break;
         case 1983:
            if (var4.equals(">=")) {
               var5 = 8;
            }
         }

         switch(var5) {
         case 1:
         case 2:
            this.op = CharacterVariableCondition.Operator.Equal;
            break;
         case 3:
         case 4:
            this.op = CharacterVariableCondition.Operator.NotEqual;
            break;
         case 5:
            this.op = CharacterVariableCondition.Operator.Less;
            break;
         case 6:
            this.op = CharacterVariableCondition.Operator.Greater;
            break;
         case 7:
            this.op = CharacterVariableCondition.Operator.LessEqual;
            break;
         case 8:
            this.op = CharacterVariableCondition.Operator.GreaterEqual;
            break;
         default:
            return false;
         }

         this.loadCompareValues(var1);
         return true;
      case 3:
         this.op = CharacterVariableCondition.Operator.Greater;
         this.loadCompareValues(var1);
         return true;
      case 4:
         this.op = CharacterVariableCondition.Operator.Less;
         this.loadCompareValues(var1);
         return true;
      case 5:
         this.op = CharacterVariableCondition.Operator.Equal;
         this.loadCompareValues(var1);
         return true;
      case 6:
         this.op = CharacterVariableCondition.Operator.NotEqual;
         this.loadCompareValues(var1);
         return true;
      case 7:
         this.op = CharacterVariableCondition.Operator.LessEqual;
         this.loadCompareValues(var1);
         return true;
      case 8:
         this.op = CharacterVariableCondition.Operator.GreaterEqual;
         this.loadCompareValues(var1);
         return true;
      default:
         return false;
      }
   }

   private void loadCompareValues(Element var1) {
      String var2 = var1.getAttribute("a").trim();
      String var3 = var1.getAttribute("b").trim();
      this.lhsValue = parseValue(var2, true);
      this.rhsValue = parseValue(var3, false);
   }

   private static Object resolveValue(Object var0, IAnimationVariableSource var1) {
      if (var0 instanceof CharacterVariableCondition.CharacterVariableLookup) {
         String var2 = var1.getVariableString(((CharacterVariableCondition.CharacterVariableLookup)var0).variableName);
         return var2 != null ? parseValue(var2, false) : null;
      } else {
         return var0;
      }
   }

   private boolean resolveCompareTo(int var1) {
      switch(this.op) {
      case Equal:
         return var1 == 0;
      case NotEqual:
         return var1 != 0;
      case Less:
         return var1 < 0;
      case LessEqual:
         return var1 <= 0;
      case Greater:
         return var1 > 0;
      case GreaterEqual:
         return var1 >= 0;
      default:
         return false;
      }
   }

   public boolean passes(ActionContext var1, int var2) {
      IAnimatable var3 = var1.getOwner();
      Object var4 = resolveValue(this.lhsValue, var3);
      Object var5 = resolveValue(this.rhsValue, var3);
      boolean var6;
      if (var4 == null && var5 instanceof String && StringUtils.isNullOrEmpty((String)var5)) {
         if (this.op == CharacterVariableCondition.Operator.Equal) {
            return true;
         }

         if (this.op == CharacterVariableCondition.Operator.NotEqual) {
            return false;
         }

         var6 = true;
      }

      if (var4 != null && var5 != null) {
         if (var4.getClass().equals(var5.getClass())) {
            if (var4 instanceof String) {
               return this.resolveCompareTo(((String)var4).compareTo((String)var5));
            }

            if (var4 instanceof Integer) {
               return this.resolveCompareTo(((Integer)var4).compareTo((Integer)var5));
            }

            if (var4 instanceof Float) {
               return this.resolveCompareTo(((Float)var4).compareTo((Float)var5));
            }

            if (var4 instanceof Boolean) {
               return this.resolveCompareTo(((Boolean)var4).compareTo((Boolean)var5));
            }
         }

         var6 = var4 instanceof Integer;
         boolean var7 = var4 instanceof Float;
         boolean var8 = var5 instanceof Integer;
         boolean var9 = var5 instanceof Float;
         if ((var6 || var7) && (var8 || var9)) {
            boolean var10 = this.lhsValue instanceof CharacterVariableCondition.CharacterVariableLookup;
            boolean var11 = this.rhsValue instanceof CharacterVariableCondition.CharacterVariableLookup;
            float var14;
            float var15;
            if (var10 == var11) {
               var14 = var7 ? (Float)var4 : (float)(Integer)var4;
               var15 = var9 ? (Float)var5 : (float)(Integer)var5;
               return this.resolveCompareTo(Float.compare(var14, var15));
            } else {
               int var12;
               int var13;
               if (var10) {
                  if (var9) {
                     var14 = var7 ? (Float)var4 : (float)(Integer)var4;
                     var15 = (Float)var5;
                     return this.resolveCompareTo(Float.compare(var14, var15));
                  } else {
                     var12 = var7 ? (int)(Float)var4 : (Integer)var4;
                     var13 = (Integer)var5;
                     return this.resolveCompareTo(Integer.compare(var12, var13));
                  }
               } else if (var7) {
                  var14 = (Float)var4;
                  var15 = var9 ? (Float)var5 : (float)(Integer)var5;
                  return this.resolveCompareTo(Float.compare(var14, var15));
               } else {
                  var12 = (Integer)var4;
                  var13 = var9 ? (int)(Float)var5 : (Integer)var5;
                  return this.resolveCompareTo(Integer.compare(var12, var13));
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public IActionCondition clone() {
      return this;
   }

   private static String getOpString(CharacterVariableCondition.Operator var0) {
      switch(var0) {
      case Equal:
         return " == ";
      case NotEqual:
         return " != ";
      case Less:
         return " < ";
      case LessEqual:
         return " <= ";
      case Greater:
         return " > ";
      case GreaterEqual:
         return " >=";
      default:
         return " ?? ";
      }
   }

   private static String valueToString(Object var0) {
      return var0 instanceof String ? "\"" + (String)var0 + "\"" : var0.toString();
   }

   public String getDescription() {
      String var10000 = valueToString(this.lhsValue);
      return var10000 + getOpString(this.op) + valueToString(this.rhsValue);
   }

   private static class CharacterVariableLookup {
      public String variableName;

      public CharacterVariableLookup(String var1) {
         this.variableName = var1;
         if (Core.bDebug) {
            AnimatorDebugMonitor.registerVariable(var1);
         }

      }

      public String toString() {
         return this.variableName;
      }
   }

   static enum Operator {
      Equal,
      NotEqual,
      Less,
      Greater,
      LessEqual,
      GreaterEqual;

      // $FF: synthetic method
      private static CharacterVariableCondition.Operator[] $values() {
         return new CharacterVariableCondition.Operator[]{Equal, NotEqual, Less, Greater, LessEqual, GreaterEqual};
      }
   }

   public static class Factory implements IActionCondition.IFactory {
      public IActionCondition create(Element var1) {
         CharacterVariableCondition var2 = new CharacterVariableCondition();
         return var2.load(var1) ? var2 : null;
      }
   }
}
