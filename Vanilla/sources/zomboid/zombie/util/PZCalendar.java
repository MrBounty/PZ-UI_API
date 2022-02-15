package zombie.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class PZCalendar {
   private final Calendar calendar;

   public static PZCalendar getInstance() {
      return new PZCalendar(Calendar.getInstance());
   }

   public PZCalendar(Calendar var1) {
      Objects.requireNonNull(var1);
      this.calendar = var1;
   }

   public void set(int var1, int var2, int var3, int var4, int var5) {
      this.calendar.set(var1, var2, var3, var4, var5);
   }

   public void setTimeInMillis(long var1) {
      this.calendar.setTimeInMillis(var1);
   }

   public int get(int var1) {
      return this.calendar.get(var1);
   }

   public final Date getTime() {
      return this.calendar.getTime();
   }

   public long getTimeInMillis() {
      return this.calendar.getTimeInMillis();
   }

   public boolean isLeapYear(int var1) {
      return ((GregorianCalendar)this.calendar).isLeapYear(var1);
   }
}
