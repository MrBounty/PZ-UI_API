package zombie.network;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import se.krka.kahlua.vm.KahluaTable;

public class DBSchema {
   private HashMap schema = new HashMap();
   private KahluaTable fullTable;

   public DBSchema(Connection var1) {
      try {
         DatabaseMetaData var2 = var1.getMetaData();
         String[] var3 = new String[]{"TABLE"};
         ResultSet var4 = var2.getTables((String)null, (String)null, (String)null, var3);

         while(true) {
            String var5;
            do {
               if (!var4.next()) {
                  return;
               }

               var5 = var4.getString(3);
            } while(var5.startsWith("SQLITE_"));

            ResultSet var6 = var2.getColumns((String)null, (String)null, var5, (String)null);
            HashMap var7 = new HashMap();

            while(var6.next()) {
               String var8 = var6.getString(4);
               if (!var8.equals("world") && !var8.equals("moderator") && !var8.equals("admin") && !var8.equals("password") && !var8.equals("encryptedPwd") && !var8.equals("pwdEncryptType") && !var8.equals("transactionID")) {
                  var7.put(var8, var6.getString(6));
               }
            }

            this.schema.put(var5, var7);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }

   public KahluaTable getFullTable() {
      return this.fullTable;
   }

   public void setFullTable(KahluaTable var1) {
      this.fullTable = var1;
   }

   public HashMap getSchema() {
      return this.schema;
   }
}
