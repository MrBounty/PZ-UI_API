package zombie.core;

import java.io.PrintStream;

public final class ProxyPrintStream extends PrintStream {
   private PrintStream fileStream = null;
   private PrintStream systemStream = null;

   public ProxyPrintStream(PrintStream var1, PrintStream var2) {
      super(var1);
      this.systemStream = var1;
      this.fileStream = var2;
   }

   public void print(String var1) {
      this.systemStream.print(var1);
      this.fileStream.print(var1);
      this.fileStream.flush();
   }

   public void println(String var1) {
      this.systemStream.println(var1);
      this.fileStream.println(var1);
      this.fileStream.flush();
   }

   public void println(Object var1) {
      this.systemStream.println(var1);
      this.fileStream.println(var1);
      this.fileStream.flush();
   }

   public void write(byte[] var1, int var2, int var3) {
      this.systemStream.write(var1, var2, var3);
      this.fileStream.write(var1, var2, var3);
      this.fileStream.flush();
   }

   public void flush() {
      this.systemStream.flush();
      this.fileStream.flush();
   }
}
