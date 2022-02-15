package zombie.savefile;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import zombie.ZomboidFileSystem;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.util.PZSQLUtils;
import zombie.vehicles.VehicleDBHelper;

public final class PlayerDBHelper {
   public static Connection create() {
      Connection var0 = null;
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var1 = var10000 + Core.GameSaveWorld;
      File var2 = new File(var1);
      if (!var2.exists()) {
         var2.mkdirs();
      }

      File var3 = new File(var1 + File.separator + "players.db");
      var3.setReadable(true, false);
      var3.setExecutable(true, false);
      var3.setWritable(true, false);
      Statement var4;
      if (!var3.exists()) {
         try {
            var3.createNewFile();
            var0 = PZSQLUtils.getConnection(var3.getAbsolutePath());
            var4 = var0.createStatement();
            var4.executeUpdate("CREATE TABLE localPlayers (id   INTEGER PRIMARY KEY NOT NULL,name STRING,wx    INTEGER,wy    INTEGER,x    FLOAT,y    FLOAT,z    FLOAT,worldversion    INTEGER,data BLOB,isDead BOOLEAN);");
            var4.executeUpdate("CREATE TABLE networkPlayers (id   INTEGER PRIMARY KEY NOT NULL,world TEXT,username TEXT,playerIndex   INTEGER,name STRING,x    FLOAT,y    FLOAT,z    FLOAT,worldversion    INTEGER,data BLOB,isDead BOOLEAN);");
            var4.executeUpdate("CREATE INDEX inpusername ON networkPlayers (username);");
            var4.close();
         } catch (Exception var8) {
            ExceptionLogger.logException(var8);
            DebugLog.log("failed to create players database");
            System.exit(1);
         }
      }

      if (var0 == null) {
         try {
            var0 = PZSQLUtils.getConnection(var3.getAbsolutePath());
         } catch (Exception var7) {
            ExceptionLogger.logException(var7);
            DebugLog.log("failed to create players database");
            System.exit(1);
         }
      }

      try {
         var4 = var0.createStatement();
         var4.executeQuery("PRAGMA JOURNAL_MODE=TRUNCATE;");
         var4.close();
      } catch (Exception var6) {
         ExceptionLogger.logException(var6);
         DebugLog.log("failed to config players.db");
         System.exit(1);
      }

      try {
         var0.setAutoCommit(false);
      } catch (SQLException var5) {
         DebugLog.log("failed to setAutoCommit for players.db");
      }

      return var0;
   }

   public static void rollback(Connection var0) {
      if (var0 != null) {
         try {
            var0.rollback();
         } catch (SQLException var2) {
            ExceptionLogger.logException(var2);
         }

      }
   }

   public static boolean isPlayerAlive(String var0, int var1) {
      if (Core.getInstance().isNoSave()) {
         return false;
      } else {
         File var2 = new File(var0 + File.separator + "map_p.bin");
         if (var2.exists()) {
            return true;
         } else if (VehicleDBHelper.isPlayerAlive(var0, var1)) {
            return true;
         } else if (var1 == -1) {
            return false;
         } else {
            try {
               File var3 = new File(var0 + File.separator + "players.db");
               if (!var3.exists()) {
                  return false;
               } else {
                  var3.setReadable(true, false);
                  Connection var4 = PZSQLUtils.getConnection(var3.getAbsolutePath());

                  boolean var8;
                  label117: {
                     try {
                        String var5 = "SELECT isDead FROM localPlayers WHERE id=?";
                        PreparedStatement var6 = var4.prepareStatement(var5);

                        label104: {
                           try {
                              var6.setInt(1, var1);
                              ResultSet var7 = var6.executeQuery();
                              if (!var7.next()) {
                                 break label104;
                              }

                              var8 = !var7.getBoolean(1);
                           } catch (Throwable var11) {
                              if (var6 != null) {
                                 try {
                                    var6.close();
                                 } catch (Throwable var10) {
                                    var11.addSuppressed(var10);
                                 }
                              }

                              throw var11;
                           }

                           if (var6 != null) {
                              var6.close();
                           }
                           break label117;
                        }

                        if (var6 != null) {
                           var6.close();
                        }
                     } catch (Throwable var12) {
                        if (var4 != null) {
                           try {
                              var4.close();
                           } catch (Throwable var9) {
                              var12.addSuppressed(var9);
                           }
                        }

                        throw var12;
                     }

                     if (var4 != null) {
                        var4.close();
                     }

                     return false;
                  }

                  if (var4 != null) {
                     var4.close();
                  }

                  return var8;
               }
            } catch (Throwable var13) {
               ExceptionLogger.logException(var13);
               return false;
            }
         }
      }
   }

   public static ArrayList getPlayers(String var0) throws SQLException {
      ArrayList var1 = new ArrayList();
      if (Core.getInstance().isNoSave()) {
         return var1;
      } else {
         File var2 = new File(var0 + File.separator + "players.db");
         if (!var2.exists()) {
            return var1;
         } else {
            var2.setReadable(true, false);
            Connection var3 = PZSQLUtils.getConnection(var2.getAbsolutePath());

            try {
               String var4 = "SELECT id, name, isDead FROM localPlayers";
               PreparedStatement var5 = var3.prepareStatement(var4);

               try {
                  ResultSet var6 = var5.executeQuery();

                  while(var6.next()) {
                     int var7 = var6.getInt(1);
                     String var8 = var6.getString(2);
                     boolean var9 = var6.getBoolean(3);
                     var1.add(BoxedStaticValues.toDouble((double)var7));
                     var1.add(var8);
                     var1.add(var9 ? Boolean.TRUE : Boolean.FALSE);
                  }
               } catch (Throwable var12) {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }
                  }

                  throw var12;
               }

               if (var5 != null) {
                  var5.close();
               }
            } catch (Throwable var13) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }
               }

               throw var13;
            }

            if (var3 != null) {
               var3.close();
            }

            return var1;
         }
      }
   }

   public static void setPlayer1(String var0, int var1) throws SQLException {
      if (!Core.getInstance().isNoSave()) {
         if (var1 != 1) {
            File var2 = new File(var0 + File.separator + "players.db");
            if (var2.exists()) {
               var2.setReadable(true, false);
               Connection var3 = PZSQLUtils.getConnection(var2.getAbsolutePath());

               label182: {
                  label183: {
                     label184: {
                        try {
                           boolean var4 = false;
                           boolean var5 = false;
                           int var6 = -1;
                           int var7 = -1;
                           String var8 = "SELECT id FROM localPlayers";
                           PreparedStatement var9 = var3.prepareStatement(var8);

                           int var11;
                           try {
                              for(ResultSet var10 = var9.executeQuery(); var10.next(); var7 = Math.max(var7, var11)) {
                                 var11 = var10.getInt(1);
                                 if (var11 == 1) {
                                    var4 = true;
                                 } else if (var6 == -1 || var6 > var11) {
                                    var6 = var11;
                                 }

                                 if (var11 == var1) {
                                    var5 = true;
                                 }
                              }
                           } catch (Throwable var20) {
                              if (var9 != null) {
                                 try {
                                    var9.close();
                                 } catch (Throwable var16) {
                                    var20.addSuppressed(var16);
                                 }
                              }

                              throw var20;
                           }

                           if (var9 != null) {
                              var9.close();
                           }

                           if (var1 <= 0) {
                              if (!var4) {
                                 break label182;
                              }

                              var8 = "UPDATE localPlayers SET id=? WHERE id=?";
                              var9 = var3.prepareStatement(var8);

                              try {
                                 var9.setInt(1, var7 + 1);
                                 var9.setInt(2, 1);
                                 var9.executeUpdate();
                              } catch (Throwable var18) {
                                 if (var9 != null) {
                                    try {
                                       var9.close();
                                    } catch (Throwable var13) {
                                       var18.addSuppressed(var13);
                                    }
                                 }

                                 throw var18;
                              }

                              if (var9 != null) {
                                 var9.close();
                              }
                              break label183;
                           }

                           if (!var5) {
                              break label184;
                           }

                           if (var4) {
                              var8 = "UPDATE localPlayers SET id=? WHERE id=?";
                              var9 = var3.prepareStatement(var8);

                              try {
                                 var9.setInt(1, var7 + 1);
                                 var9.setInt(2, 1);
                                 var9.executeUpdate();
                                 var9.setInt(1, 1);
                                 var9.setInt(2, var1);
                                 var9.executeUpdate();
                                 var9.setInt(1, var1);
                                 var9.setInt(2, var7 + 1);
                                 var9.executeUpdate();
                              } catch (Throwable var17) {
                                 if (var9 != null) {
                                    try {
                                       var9.close();
                                    } catch (Throwable var14) {
                                       var17.addSuppressed(var14);
                                    }
                                 }

                                 throw var17;
                              }

                              if (var9 != null) {
                                 var9.close();
                              }
                           } else {
                              var8 = "UPDATE localPlayers SET id=? WHERE id=?";
                              var9 = var3.prepareStatement(var8);

                              try {
                                 var9.setInt(1, 1);
                                 var9.setInt(2, var1);
                                 var9.executeUpdate();
                              } catch (Throwable var19) {
                                 if (var9 != null) {
                                    try {
                                       var9.close();
                                    } catch (Throwable var15) {
                                       var19.addSuppressed(var15);
                                    }
                                 }

                                 throw var19;
                              }

                              if (var9 != null) {
                                 var9.close();
                              }
                           }
                        } catch (Throwable var21) {
                           if (var3 != null) {
                              try {
                                 var3.close();
                              } catch (Throwable var12) {
                                 var21.addSuppressed(var12);
                              }
                           }

                           throw var21;
                        }

                        if (var3 != null) {
                           var3.close();
                        }

                        return;
                     }

                     if (var3 != null) {
                        var3.close();
                     }

                     return;
                  }

                  if (var3 != null) {
                     var3.close();
                  }

                  return;
               }

               if (var3 != null) {
                  var3.close();
               }

            }
         }
      }
   }
}
