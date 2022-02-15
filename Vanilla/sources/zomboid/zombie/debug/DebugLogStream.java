package zombie.debug;

import java.io.PrintStream;
import zombie.util.StringUtils;

public final class DebugLogStream extends PrintStream {
   private final PrintStream m_wrappedStream;
   private final PrintStream m_wrappedWarnStream;
   private final PrintStream m_wrappedErrStream;
   private final IDebugLogFormatter m_formatter;
   public static final String s_prefixErr = "ERROR: ";
   public static final String s_prefixWarn = "WARN : ";
   public static final String s_prefixOut = "LOG  : ";
   public static final String s_prefixDebug = "DEBUG: ";

   public DebugLogStream(PrintStream var1, PrintStream var2, PrintStream var3, IDebugLogFormatter var4) {
      super(var1);
      this.m_wrappedStream = var1;
      this.m_wrappedWarnStream = var2;
      this.m_wrappedErrStream = var3;
      this.m_formatter = var4;
   }

   private void write(PrintStream var1, String var2) {
      String var3 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var2);
      if (var3 != null) {
         var1.print(var3);
      }

   }

   private void writeln(PrintStream var1, String var2) {
      this.writeln(var1, LogSeverity.General, "LOG  : ", var2);
   }

   private void writeln(PrintStream var1, String var2, Object var3) {
      this.writeln(var1, LogSeverity.General, "LOG  : ", var2, var3);
   }

   private void writeln(PrintStream var1, LogSeverity var2, String var3, String var4) {
      String var5 = this.m_formatter.format(var2, var3, "", var4);
      if (var5 != null) {
         var1.println(var5);
      }

   }

   private void writeln(PrintStream var1, LogSeverity var2, String var3, String var4, Object var5) {
      String var6 = this.m_formatter.format(var2, var3, "", var4, var5);
      if (var6 != null) {
         var1.println(var6);
      }

   }

   public static String generateCallerPrefix() {
      StackTraceElement var0 = tryGetCallerTraceElement(4);
      return var0 == null ? "(UnknownStack)" : getStackTraceElementString(var0, false);
   }

   public static StackTraceElement tryGetCallerTraceElement(int var0) {
      StackTraceElement[] var1 = Thread.currentThread().getStackTrace();
      if (var1.length <= var0) {
         return null;
      } else {
         StackTraceElement var2 = var1[var0];
         return var2;
      }
   }

   public static String getStackTraceElementString(StackTraceElement var0, boolean var1) {
      if (var0 == null) {
         return "(UnknownStack)";
      } else {
         String var2 = getUnqualifiedClassName(var0.getClassName());
         String var3 = var0.getMethodName();
         int var4 = var0.getLineNumber();
         String var5;
         if (var0.isNativeMethod()) {
            var5 = " (Native Method)";
         } else if (var1 && var4 > -1) {
            var5 = " line:" + var4;
         } else {
            var5 = "";
         }

         String var6 = var2 + "." + var3 + var5;
         return var6;
      }
   }

   public static String getTopStackTraceString(Throwable var0) {
      if (var0 == null) {
         return "Null Exception";
      } else {
         StackTraceElement[] var1 = var0.getStackTrace();
         if (var1 != null && var1.length != 0) {
            StackTraceElement var2 = var1[0];
            return getStackTraceElementString(var2, true);
         } else {
            return "No Stack Trace Available";
         }
      }
   }

   public void printStackTrace() {
      this.printStackTrace(0);
   }

   public void printStackTrace(int var1) {
      StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
      int var3 = var1 == 0 ? var2.length : Math.min(var1, var2.length);

      for(int var4 = 0; var4 < var3; ++var4) {
         StackTraceElement var5 = var2[var4];
         this.println("\t" + var5.toString());
      }

   }

   private static String getUnqualifiedClassName(String var0) {
      String var1 = var0;
      int var2 = var0.lastIndexOf(46);
      if (var2 > -1 && var2 < var0.length() - 1) {
         var1 = var0.substring(var2 + 1);
      }

      return var1;
   }

   public void debugln(String var1) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var2 = generateCallerPrefix();
         String var3 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var2, 36) + "> ", "%s", var1);
         this.m_wrappedStream.println(var3);
      }

   }

   public void debugln(String var1, Object var2) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var3 = generateCallerPrefix();
         String var4 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var3, 36) + "> ", var1, var2);
         this.m_wrappedStream.println(var4);
      }

   }

   public void debugln(String var1, Object var2, Object var3) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var4 = generateCallerPrefix();
         String var5 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var4, 36) + "> ", var1, var2, var3);
         this.m_wrappedStream.println(var5);
      }

   }

   public void debugln(String var1, Object var2, Object var3, Object var4) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var5 = generateCallerPrefix();
         String var6 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var5, 36) + "> ", var1, var2, var3, var4);
         this.m_wrappedStream.println(var6);
      }

   }

   public void debugln(String var1, Object var2, Object var3, Object var4, Object var5) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var6 = generateCallerPrefix();
         String var7 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var6, 36) + "> ", var1, var2, var3, var4, var5);
         this.m_wrappedStream.println(var7);
      }

   }

   public void debugln(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var7 = generateCallerPrefix();
         String var8 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var7, 36) + "> ", var1, var2, var3, var4, var5, var6);
         this.m_wrappedStream.println(var8);
      }

   }

   public void debugln(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      if (this.m_formatter.isLogEnabled(LogSeverity.General)) {
         String var8 = generateCallerPrefix();
         String var9 = this.m_formatter.format(LogSeverity.General, "DEBUG: ", StringUtils.leftJustify(var8, 36) + "> ", var1, var2, var3, var4, var5, var6, var7);
         this.m_wrappedStream.println(var9);
      }

   }

   public void print(boolean var1) {
      this.write(this.m_wrappedStream, var1 ? "true" : "false");
   }

   public void print(char var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(int var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(long var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(float var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(double var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(String var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public void print(Object var1) {
      this.write(this.m_wrappedStream, String.valueOf(var1));
   }

   public PrintStream printf(String var1, Object... var2) {
      this.write(this.m_wrappedStream, String.format(var1, var2));
      return this;
   }

   public void println() {
      this.writeln(this.m_wrappedStream, "");
   }

   public void println(boolean var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(char var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(int var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(long var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(float var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(double var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(char[] var1) {
      this.writeln(this.m_wrappedStream, "%s", String.valueOf(var1));
   }

   public void println(String var1) {
      this.writeln(this.m_wrappedStream, "%s", var1);
   }

   public void println(Object var1) {
      this.writeln(this.m_wrappedStream, "%s", var1);
   }

   public void println(String var1, Object var2) {
      String var3 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2);
      if (var3 != null) {
         this.m_wrappedStream.println(var3);
      }

   }

   public void println(String var1, Object var2, Object var3) {
      String var4 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3);
      if (var4 != null) {
         this.m_wrappedStream.println(var4);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4) {
      String var5 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4);
      if (var5 != null) {
         this.m_wrappedStream.println(var5);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5) {
      String var6 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5);
      if (var6 != null) {
         this.m_wrappedStream.println(var6);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      String var7 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5, var6);
      if (var7 != null) {
         this.m_wrappedStream.println(var7);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      String var8 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5, var6, var7);
      if (var8 != null) {
         this.m_wrappedStream.println(var8);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      String var9 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 != null) {
         this.m_wrappedStream.println(var9);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      String var10 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5, var6, var7, var8, var9);
      if (var10 != null) {
         this.m_wrappedStream.println(var10);
      }

   }

   public void println(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10) {
      String var11 = this.m_formatter.format(LogSeverity.General, "LOG  : ", "", var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      if (var11 != null) {
         this.m_wrappedStream.println(var11);
      }

   }

   public void error(Object var1) {
      PrintStream var10001 = this.m_wrappedErrStream;
      LogSeverity var10002 = LogSeverity.Error;
      String var10004 = generateCallerPrefix();
      this.writeln(var10001, var10002, "ERROR: ", var10004 + "> " + String.valueOf(var1));
   }

   public void error(String var1, Object... var2) {
      PrintStream var10001 = this.m_wrappedErrStream;
      LogSeverity var10002 = LogSeverity.Error;
      String var10004 = generateCallerPrefix();
      this.writeln(var10001, var10002, "ERROR: ", var10004 + "> " + String.format(var1, var2));
   }

   public void warn(Object var1) {
      PrintStream var10001 = this.m_wrappedWarnStream;
      LogSeverity var10002 = LogSeverity.Warning;
      String var10004 = generateCallerPrefix();
      this.writeln(var10001, var10002, "WARN : ", var10004 + "> " + String.valueOf(var1));
   }

   public void warn(String var1, Object... var2) {
      PrintStream var10001 = this.m_wrappedWarnStream;
      LogSeverity var10002 = LogSeverity.Warning;
      String var10004 = generateCallerPrefix();
      this.writeln(var10001, var10002, "WARN : ", var10004 + "> " + String.format(var1, var2));
   }

   public void printUnitTest(String var1, boolean var2, Object... var3) {
      if (!var2) {
         this.error(var1 + ", fail", var3);
      } else {
         this.println(var1 + ", pass", var3);
      }

   }

   public void printException(Throwable var1, String var2, LogSeverity var3) {
      this.printException(var1, var2, generateCallerPrefix(), var3);
   }

   public void printException(Throwable var1, String var2, String var3, LogSeverity var4) {
      if (var1 == null) {
         this.warn("Null exception passed.");
      } else {
         String var5;
         PrintStream var6;
         boolean var7;
         switch(var4) {
         case Trace:
         case General:
            var5 = "LOG  : ";
            var6 = this.m_wrappedStream;
            var7 = false;
            break;
         case Warning:
            var5 = "WARN : ";
            var6 = this.m_wrappedWarnStream;
            var7 = false;
            break;
         default:
            this.error("Unhandled LogSeverity: %s. Defaulted to Error.", String.valueOf(var4));
         case Error:
            var5 = "ERROR: ";
            var6 = this.m_wrappedErrStream;
            var7 = true;
         }

         if (var2 != null) {
            this.writeln(var6, var4, var5, String.format("%s> Exception thrown %s at %s. Message: %s", var3, var1.toString(), getTopStackTraceString(var1), var2));
         } else {
            this.writeln(var6, var4, var5, String.format("%s> Exception thrown %s at %s.", var3, var1.toString(), getTopStackTraceString(var1)));
         }

         if (var7) {
            this.error("Stack trace:");
            var1.printStackTrace(var6);
         }

      }
   }
}
