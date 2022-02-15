package se.krka.kahlua.vm;

public class KahluaException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   public Object errorMessage;

   public KahluaException(Object var1) {
      this.errorMessage = var1;
   }

   public String getMessage() {
      return this.errorMessage == null ? "nil" : this.errorMessage.toString();
   }
}
