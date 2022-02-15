package zombie.network;

public class Userlog {
   private String username;
   private String type;
   private String text;
   private String issuedBy;
   private int amount;

   public Userlog(String var1, String var2, String var3, String var4, int var5) {
      this.username = var1;
      this.type = var2;
      this.text = var3;
      this.issuedBy = var4;
      this.amount = var5;
   }

   public String getUsername() {
      return this.username;
   }

   public String getType() {
      return this.type;
   }

   public String getText() {
      return this.text;
   }

   public String getIssuedBy() {
      return this.issuedBy;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int var1) {
      this.amount = var1;
   }

   public static enum UserlogType {
      AdminLog(0),
      Kicked(1),
      Banned(2),
      DupeItem(3),
      LuaChecksum(4),
      WarningPoint(5);

      private int index;

      private UserlogType(int var3) {
         this.index = var3;
      }

      public int index() {
         return this.index;
      }

      public static Userlog.UserlogType fromIndex(int var0) {
         return ((Userlog.UserlogType[])Userlog.UserlogType.class.getEnumConstants())[var0];
      }

      public static Userlog.UserlogType FromString(String var0) {
         return valueOf(var0);
      }

      // $FF: synthetic method
      private static Userlog.UserlogType[] $values() {
         return new Userlog.UserlogType[]{AdminLog, Kicked, Banned, DupeItem, LuaChecksum, WarningPoint};
      }
   }
}
