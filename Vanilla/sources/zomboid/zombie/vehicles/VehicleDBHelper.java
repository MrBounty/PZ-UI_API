package zombie.vehicles;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import zombie.debug.DebugLog;
import zombie.util.PZSQLUtils;

public final class VehicleDBHelper {
   public static boolean isPlayerAlive(String var0, int var1) {
      File var2 = new File(var0 + File.separator + "map_p.bin");
      if (var2.exists()) {
         return true;
      } else if (var1 == -1) {
         return false;
      } else {
         Connection var3 = null;
         File var4 = new File(var0 + File.separator + "vehicles.db");
         var4.setReadable(true, false);
         if (!var4.exists()) {
            return false;
         } else {
            try {
               var3 = PZSQLUtils.getConnection(var4.getAbsolutePath());
            } catch (Exception var20) {
               DebugLog.log("failed to create vehicles database");
               System.exit(1);
            }

            boolean var5 = false;
            String var6 = "SELECT isDead FROM localPlayers WHERE id=?";
            PreparedStatement var7 = null;

            boolean var9;
            try {
               var7 = var3.prepareStatement(var6);
               var7.setInt(1, var1);
               ResultSet var8 = var7.executeQuery();
               if (var8.next()) {
                  var5 = !var8.getBoolean(1);
               }

               return var5;
            } catch (SQLException var21) {
               var9 = false;
            } finally {
               try {
                  if (var7 != null) {
                     var7.close();
                  }

                  var3.close();
               } catch (SQLException var19) {
                  System.out.println(var19.getMessage());
               }

            }

            return var9;
         }
      }
   }
}
