package se.krka.kahlua.stdlib;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public class OsLib implements JavaFunction {
   private static final int DATE = 0;
   private static final int DIFFTIME = 1;
   private static final int TIME = 2;
   private static final int NUM_FUNCS = 3;
   private static final String[] funcnames = new String[3];
   private static final OsLib[] funcs;
   private static final String TABLE_FORMAT = "*t";
   private static final String DEFAULT_FORMAT = "%c";
   private static final String YEAR = "year";
   private static final String MONTH = "month";
   private static final String DAY = "day";
   private static final String HOUR = "hour";
   private static final String MIN = "min";
   private static final String SEC = "sec";
   private static final String WDAY = "wday";
   private static final String YDAY = "yday";
   private static final Object MILLISECOND;
   private static TimeZone tzone;
   public static final int TIME_DIVIDEND = 1000;
   public static final double TIME_DIVIDEND_INVERTED = 0.001D;
   private static final int MILLIS_PER_DAY = 86400000;
   private static final int MILLIS_PER_WEEK = 604800000;
   private int methodId;
   private static String[] shortDayNames;
   private static String[] longDayNames;
   private static String[] shortMonthNames;
   private static String[] longMonthNames;

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();

      for(int var3 = 0; var3 < 3; ++var3) {
         var2.rawset(funcnames[var3], funcs[var3]);
      }

      var1.rawset("os", var2);
   }

   private OsLib(int var1) {
      this.methodId = var1;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.methodId) {
      case 0:
         return this.date(var1, var2);
      case 1:
         return this.difftime(var1, var2);
      case 2:
         return this.time(var1, var2);
      default:
         throw new RuntimeException("Undefined method called on os.");
      }
   }

   private int time(LuaCallFrame var1, int var2) {
      if (var2 == 0) {
         double var3 = (double)System.currentTimeMillis() * 0.001D;
         var1.push(KahluaUtil.toDouble(var3));
      } else {
         KahluaTable var6 = (KahluaTable)KahluaUtil.getArg(var1, 1, "time");
         double var4 = (double)getDateFromTable(var6).getTime() * 0.001D;
         var1.push(KahluaUtil.toDouble(var4));
      }

      return 1;
   }

   private int difftime(LuaCallFrame var1, int var2) {
      double var3 = KahluaUtil.getDoubleArg(var1, 1, "difftime");
      double var5 = KahluaUtil.getDoubleArg(var1, 2, "difftime");
      var1.push(KahluaUtil.toDouble(var3 - var5));
      return 1;
   }

   private int date(LuaCallFrame var1, int var2) {
      Platform var3 = var1.getPlatform();
      if (var2 == 0) {
         return var1.push(getdate("%c", var3));
      } else {
         String var4 = KahluaUtil.getStringArg(var1, 1, "date");
         if (var2 == 1) {
            return var1.push(getdate(var4, var3));
         } else {
            double var5 = KahluaUtil.getDoubleArg(var1, 2, "date");
            long var7 = (long)(var5 * 1000.0D);
            return var1.push(getdate(var4, var7, var3));
         }
      }
   }

   public static Object getdate(String var0, Platform var1) {
      return getdate(var0, Calendar.getInstance().getTime().getTime(), var1);
   }

   public static Object getdate(String var0, long var1, Platform var3) {
      Calendar var4 = null;
      int var5 = 0;
      if (var0.charAt(var5) == '!') {
         var4 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         ++var5;
      } else {
         var4 = Calendar.getInstance(tzone);
      }

      var4.setTime(new Date(var1));
      if (var4 == null) {
         return null;
      } else {
         return var0.substring(var5, 2 + var5).equals("*t") ? getTableFromDate(var4, var3) : formatTime(var0.substring(var5), var4);
      }
   }

   public static String formatTime(String var0, Calendar var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         if (var0.charAt(var3) == '%' && var3 + 1 != var0.length()) {
            ++var3;
            var2.append(strftime(var0.charAt(var3), var1));
         } else {
            var2.append(var0.charAt(var3));
         }
      }

      return var2.toString();
   }

   private static String format2Digits(int var0) {
      String var1 = Integer.toString(var0);
      if (var0 < 10) {
         var1 = "0" + var1;
      }

      return var1;
   }

   private static String strftime(char var0, Calendar var1) {
      switch(var0) {
      case 'A':
         return longDayNames[var1.get(7) - 1];
      case 'B':
         return longMonthNames[var1.get(2)];
      case 'C':
         return Integer.toString(var1.get(1) / 100);
      case 'D':
         return formatTime("%m/%d/%y", var1);
      case 'E':
      case 'F':
      case 'G':
      case 'J':
      case 'K':
      case 'L':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'T':
      case 'X':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'f':
      case 'g':
      case 'i':
      case 'k':
      case 'l':
      case 'o':
      case 'q':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'x':
      default:
         return null;
      case 'H':
         return format2Digits(var1.get(11));
      case 'I':
         return format2Digits(var1.get(10));
      case 'M':
         return format2Digits(var1.get(12));
      case 'R':
         return formatTime("%H:%M", var1);
      case 'S':
         return format2Digits(var1.get(13));
      case 'U':
         return Integer.toString(getWeekOfYear(var1, true, false));
      case 'V':
         return Integer.toString(getWeekOfYear(var1, false, true));
      case 'W':
         return Integer.toString(getWeekOfYear(var1, false, false));
      case 'Y':
         return Integer.toString(var1.get(1));
      case 'Z':
         return var1.getTimeZone().getID();
      case 'a':
         return shortDayNames[var1.get(7) - 1];
      case 'b':
         return shortMonthNames[var1.get(2)];
      case 'c':
         return var1.getTime().toString();
      case 'd':
         return format2Digits(var1.get(5));
      case 'e':
         return var1.get(5) < 10 ? " " + strftime('d', var1) : strftime('d', var1);
      case 'h':
         return strftime('b', var1);
      case 'j':
         return Integer.toString(getDayOfYear(var1));
      case 'm':
         return format2Digits(var1.get(2) + 1);
      case 'n':
         return "\n";
      case 'p':
         return var1.get(9) == 0 ? "AM" : "PM";
      case 'r':
         return formatTime("%I:%M:%S %p", var1);
      case 'w':
         return Integer.toString(var1.get(7) - 1);
      case 'y':
         return Integer.toString(var1.get(1) % 100);
      }
   }

   public static KahluaTable getTableFromDate(Calendar var0, Platform var1) {
      KahluaTable var2 = var1.newTable();
      var2.rawset("year", KahluaUtil.toDouble((long)var0.get(1)));
      var2.rawset("month", KahluaUtil.toDouble((long)(var0.get(2) + 1)));
      var2.rawset("day", KahluaUtil.toDouble((long)var0.get(5)));
      var2.rawset("hour", KahluaUtil.toDouble((long)var0.get(11)));
      var2.rawset("min", KahluaUtil.toDouble((long)var0.get(12)));
      var2.rawset("sec", KahluaUtil.toDouble((long)var0.get(13)));
      var2.rawset("wday", KahluaUtil.toDouble((long)var0.get(7)));
      var2.rawset("yday", KahluaUtil.toDouble((long)getDayOfYear(var0)));
      var2.rawset(MILLISECOND, KahluaUtil.toDouble((long)var0.get(14)));
      return var2;
   }

   public static Date getDateFromTable(KahluaTable var0) {
      Calendar var1 = Calendar.getInstance(tzone);
      var1.set(1, (int)KahluaUtil.fromDouble(var0.rawget("year")));
      var1.set(2, (int)KahluaUtil.fromDouble(var0.rawget("month")) - 1);
      var1.set(5, (int)KahluaUtil.fromDouble(var0.rawget("day")));
      Object var2 = var0.rawget("hour");
      Object var3 = var0.rawget("min");
      Object var4 = var0.rawget("sec");
      Object var5 = var0.rawget(MILLISECOND);
      if (var2 != null) {
         var1.set(11, (int)KahluaUtil.fromDouble(var2));
      } else {
         var1.set(11, 0);
      }

      if (var3 != null) {
         var1.set(12, (int)KahluaUtil.fromDouble(var3));
      } else {
         var1.set(12, 0);
      }

      if (var4 != null) {
         var1.set(13, (int)KahluaUtil.fromDouble(var4));
      } else {
         var1.set(13, 0);
      }

      if (var5 != null) {
         var1.set(14, (int)KahluaUtil.fromDouble(var5));
      } else {
         var1.set(14, 0);
      }

      return var1.getTime();
   }

   public static int getDayOfYear(Calendar var0) {
      Calendar var1 = Calendar.getInstance(var0.getTimeZone());
      var1.setTime(var0.getTime());
      var1.set(2, 0);
      var1.set(5, 1);
      long var2 = var0.getTime().getTime() - var1.getTime().getTime();
      return (int)Math.ceil((double)var2 / 8.64E7D);
   }

   public static int getWeekOfYear(Calendar var0, boolean var1, boolean var2) {
      Calendar var3 = Calendar.getInstance(var0.getTimeZone());
      var3.setTime(var0.getTime());
      var3.set(2, 0);
      var3.set(5, 1);
      int var4 = var3.get(7);
      if (var1 && var4 != 1) {
         var3.set(5, 7 - var4 + 1);
      } else if (var4 != 2) {
         var3.set(5, 7 - var4 + 1 + 1);
      }

      long var5 = var0.getTime().getTime() - var3.getTime().getTime();
      int var7 = (int)(var5 / 604800000L);
      if (var2 && 7 - var4 >= 4) {
         ++var7;
      }

      return var7;
   }

   static {
      funcnames[0] = "date";
      funcnames[1] = "difftime";
      funcnames[2] = "time";
      funcs = new OsLib[3];

      for(int var0 = 0; var0 < 3; ++var0) {
         funcs[var0] = new OsLib(var0);
      }

      MILLISECOND = "milli";
      tzone = TimeZone.getTimeZone("UTC");
      shortDayNames = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
      longDayNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
      shortMonthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
      longMonthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
   }
}
