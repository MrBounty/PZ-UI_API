package zombie.debug;

public interface IDebugLogFormatter {
   boolean isLogEnabled(LogSeverity var1);

   String format(LogSeverity var1, String var2, String var3, String var4);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12);

   String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13);
}
