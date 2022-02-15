package zombie.debug;

class GenericDebugLogFormatter implements IDebugLogFormatter {
   private final DebugType debugType;

   public GenericDebugLogFormatter(DebugType var1) {
      this.debugType = var1;
   }

   public boolean isLogEnabled(LogSeverity var1) {
      return DebugLog.isLogEnabled(var1, this.debugType);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public String format(LogSeverity var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13) {
      return DebugLog.formatString(this.debugType, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }
}
