package zombie.core;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.core.math.PZMath;

public final class GameVersion {
   private final int m_major;
   private final int m_minor;
   private final String m_suffix;
   private final String m_string;

   public GameVersion(int var1, int var2, String var3) {
      if (var1 < 0) {
         throw new IllegalArgumentException("major version must be greater than zero");
      } else if (var2 >= 0 && var2 <= 999) {
         this.m_major = var1;
         this.m_minor = var2;
         this.m_suffix = var3;
         this.m_string = String.format(Locale.ENGLISH, "%d.%d%s", this.m_major, this.m_minor, this.m_suffix == null ? "" : this.m_suffix);
      } else {
         throw new IllegalArgumentException("minor version must be from 0 to 999");
      }
   }

   public int getMajor() {
      return this.m_major;
   }

   public int getMinor() {
      return this.m_minor;
   }

   public String getSuffix() {
      return this.m_suffix;
   }

   public int getInt() {
      return this.m_major * 1000 + this.m_minor;
   }

   public boolean isGreaterThan(GameVersion var1) {
      return this.getInt() > var1.getInt();
   }

   public boolean isGreaterThanOrEqualTo(GameVersion var1) {
      return this.getInt() >= var1.getInt();
   }

   public boolean isLessThan(GameVersion var1) {
      return this.getInt() < var1.getInt();
   }

   public boolean isLessThanOrEqualTo(GameVersion var1) {
      return this.getInt() <= var1.getInt();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof GameVersion)) {
         return false;
      } else {
         GameVersion var2 = (GameVersion)var1;
         return var2.m_major == this.m_major && var2.m_minor == this.m_minor;
      }
   }

   public String toString() {
      return this.m_string;
   }

   public static GameVersion parse(String var0) {
      Matcher var1 = Pattern.compile("([0-9]+)\\.([0-9]+)(.*)").matcher(var0);
      if (var1.matches()) {
         int var2 = PZMath.tryParseInt(var1.group(1), 0);
         int var3 = PZMath.tryParseInt(var1.group(2), 0);
         String var4 = var1.group(3);
         return new GameVersion(var2, var3, var4);
      } else {
         throw new IllegalArgumentException("invalid game version \"" + var0 + "\"");
      }
   }
}
