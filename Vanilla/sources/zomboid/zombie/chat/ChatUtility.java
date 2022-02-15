package zombie.chat;

import java.util.ArrayList;
import java.util.HashMap;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.raknet.UdpConnection;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerOptions;
import zombie.network.chat.ChatType;

public final class ChatUtility {
   private static final boolean useEuclidean = true;
   private static final HashMap allowedChatIcons = new HashMap();
   private static final HashMap allowedChatIconsFull = new HashMap();
   private static final StringBuilder builder = new StringBuilder();
   private static final StringBuilder builderTest = new StringBuilder();

   private ChatUtility() {
   }

   public static float getScrambleValue(IsoObject var0, IsoPlayer var1, float var2) {
      return getScrambleValue(var0.getX(), var0.getY(), var0.getZ(), var0.getSquare(), var1, var2);
   }

   public static float getScrambleValue(float var0, float var1, float var2, IsoGridSquare var3, IsoPlayer var4, float var5) {
      float var6 = 1.0F;
      boolean var7 = false;
      boolean var8 = false;
      if (var3 != null && var4.getSquare() != null) {
         if (var4.getBuilding() != null && var3.getBuilding() != null && var4.getBuilding() == var3.getBuilding()) {
            if (var4.getSquare().getRoom() == var3.getRoom()) {
               var6 = (float)((double)var6 * 2.0D);
               var8 = true;
            } else if (Math.abs(var4.getZ() - var2) < 1.0F) {
               var6 = (float)((double)var6 * 2.0D);
            }
         } else if (var4.getBuilding() != null || var3.getBuilding() != null) {
            var6 = (float)((double)var6 * 0.5D);
            var7 = true;
         }

         if (Math.abs(var4.getZ() - var2) >= 1.0F) {
            var6 = (float)((double)var6 - (double)var6 * (double)Math.abs(var4.getZ() - var2) * 0.25D);
            var7 = true;
         }
      }

      float var9 = var5 * var6;
      float var10 = 1.0F;
      if (var6 > 0.0F && playerWithinBounds(var0, var1, var4, var9)) {
         float var11 = getDistance(var0, var1, var4);
         if (var11 >= 0.0F && var11 < var9) {
            float var12 = var9 * 0.6F;
            if (!var8 && (var7 || !(var11 < var12))) {
               if (var9 - var12 != 0.0F) {
                  var10 = (var11 - var12) / (var9 - var12);
                  if (var10 < 0.2F) {
                     var10 = 0.2F;
                  }
               }
            } else {
               var10 = 0.0F;
            }
         }
      }

      return var10;
   }

   public static boolean playerWithinBounds(IsoObject var0, IsoObject var1, float var2) {
      return playerWithinBounds(var0.getX(), var0.getY(), var1, var2);
   }

   public static boolean playerWithinBounds(float var0, float var1, IsoObject var2, float var3) {
      if (var2 == null) {
         return false;
      } else {
         return var2.getX() > var0 - var3 && var2.getX() < var0 + var3 && var2.getY() > var1 - var3 && var2.getY() < var1 + var3;
      }
   }

   public static float getDistance(IsoObject var0, IsoPlayer var1) {
      return var1 == null ? -1.0F : (float)Math.sqrt(Math.pow((double)(var0.getX() - var1.x), 2.0D) + Math.pow((double)(var0.getY() - var1.y), 2.0D));
   }

   public static float getDistance(float var0, float var1, IsoPlayer var2) {
      return var2 == null ? -1.0F : (float)Math.sqrt(Math.pow((double)(var0 - var2.x), 2.0D) + Math.pow((double)(var1 - var2.y), 2.0D));
   }

   public static UdpConnection findConnection(short var0) {
      UdpConnection var1 = null;
      if (GameServer.udpEngine != null) {
         for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
            UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);

            for(int var4 = 0; var4 < var3.playerIDs.length; ++var4) {
               if (var3.playerIDs[var4] == var0) {
                  var1 = var3;
                  break;
               }
            }
         }
      }

      if (var1 == null) {
         DebugLog.log("Connection with PlayerID ='" + var0 + "' not found!");
      }

      return var1;
   }

   public static UdpConnection findConnection(String var0) {
      UdpConnection var1 = null;
      if (GameServer.udpEngine != null) {
         for(int var2 = 0; var2 < GameServer.udpEngine.connections.size() && var1 == null; ++var2) {
            UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);

            for(int var4 = 0; var4 < var3.players.length; ++var4) {
               if (var3.players[var4] != null && var3.players[var4].username.equalsIgnoreCase(var0)) {
                  var1 = var3;
                  break;
               }
            }
         }
      }

      if (var1 == null) {
         DebugLog.log("Player with nickname = '" + var0 + "' not found!");
      }

      return var1;
   }

   public static IsoPlayer findPlayer(int var0) {
      IsoPlayer var1 = null;
      if (GameServer.udpEngine != null) {
         for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
            UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);

            for(int var4 = 0; var4 < var3.playerIDs.length; ++var4) {
               if (var3.playerIDs[var4] == var0) {
                  var1 = var3.players[var4];
                  break;
               }
            }
         }
      }

      if (var1 == null) {
         DebugLog.log("Player with PlayerID ='" + var0 + "' not found!");
      }

      return var1;
   }

   public static String findPlayerName(int var0) {
      return findPlayer(var0).getUsername();
   }

   public static IsoPlayer findPlayer(String var0) {
      IsoPlayer var1 = null;
      if (GameClient.bClient) {
         var1 = GameClient.instance.getPlayerFromUsername(var0);
      } else if (GameServer.bServer) {
         var1 = GameServer.getPlayerByUserName(var0);
      }

      if (var1 == null) {
         DebugLog.log("Player with nickname = '" + var0 + "' not found!");
      }

      return var1;
   }

   public static ArrayList getAllowedChatStreams() {
      String var0 = ServerOptions.getInstance().ChatStreams.getValue();
      var0 = var0.replaceAll("\"", "");
      String[] var1 = var0.split(",");
      ArrayList var2 = new ArrayList();
      var2.add(ChatType.server);
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         byte var8 = -1;
         switch(var6.hashCode()) {
         case 97:
            if (var6.equals("a")) {
               var8 = 2;
            }
            break;
         case 102:
            if (var6.equals("f")) {
               var8 = 6;
            }
            break;
         case 114:
            if (var6.equals("r")) {
               var8 = 1;
            }
            break;
         case 115:
            if (var6.equals("s")) {
               var8 = 0;
            }
            break;
         case 119:
            if (var6.equals("w")) {
               var8 = 3;
            }
            break;
         case 121:
            if (var6.equals("y")) {
               var8 = 4;
            }
            break;
         case 3669:
            if (var6.equals("sh")) {
               var8 = 5;
            }
            break;
         case 96673:
            if (var6.equals("all")) {
               var8 = 7;
            }
         }

         switch(var8) {
         case 0:
            var2.add(ChatType.say);
            break;
         case 1:
            var2.add(ChatType.radio);
            break;
         case 2:
            var2.add(ChatType.admin);
            break;
         case 3:
            var2.add(ChatType.whisper);
            break;
         case 4:
            var2.add(ChatType.shout);
            break;
         case 5:
            var2.add(ChatType.safehouse);
            break;
         case 6:
            var2.add(ChatType.faction);
            break;
         case 7:
            var2.add(ChatType.general);
         }
      }

      return var2;
   }

   public static boolean chatStreamEnabled(ChatType var0) {
      ArrayList var1 = getAllowedChatStreams();
      return var1.contains(var0);
   }

   public static void InitAllowedChatIcons() {
      allowedChatIcons.clear();
      Texture.collectAllIcons(allowedChatIcons, allowedChatIconsFull);
   }

   private static String getColorString(String var0, boolean var1) {
      if (Colors.ColorExists(var0)) {
         Color var6 = Colors.GetColorByName(var0);
         if (var1) {
            float var7 = var6.getRedFloat();
            return var7 + "," + var6.getGreenFloat() + "," + var6.getBlueFloat();
         } else {
            int var10000 = var6.getRed();
            return var10000 + "," + var6.getGreen() + "," + var6.getBlue();
         }
      } else {
         if (var0.length() <= 11 && var0.contains(",")) {
            String[] var2 = var0.split(",");
            if (var2.length == 3) {
               int var3 = parseColorInt(var2[0]);
               int var4 = parseColorInt(var2[1]);
               int var5 = parseColorInt(var2[2]);
               if (var3 != -1 && var4 != -1 && var5 != -1) {
                  if (var1) {
                     return (float)var3 / 255.0F + "," + (float)var4 / 255.0F + "," + (float)var5 / 255.0F;
                  }

                  return var3 + "," + var4 + "," + var5;
               }
            }
         }

         return null;
      }
   }

   private static int parseColorInt(String var0) {
      try {
         int var1 = Integer.parseInt(var0);
         return var1 >= 0 && var1 <= 255 ? var1 : -1;
      } catch (Exception var2) {
         return -1;
      }
   }

   public static String parseStringForChatBubble(String var0) {
      try {
         builder.delete(0, builder.length());
         builderTest.delete(0, builderTest.length());
         var0 = var0.replaceAll("\\[br/]", "");
         var0 = var0.replaceAll("\\[cdt=", "");
         char[] var1 = var0.toCharArray();
         boolean var2 = false;
         boolean var3 = false;
         int var4 = 0;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            char var6 = var1[var5];
            if (var6 != '*') {
               if (var2) {
                  builderTest.append(var6);
               } else {
                  builder.append(var6);
               }
            } else if (!var2) {
               var2 = true;
            } else {
               String var7 = builderTest.toString();
               builderTest.delete(0, builderTest.length());
               String var8 = getColorString(var7, false);
               if (var8 != null) {
                  if (var3) {
                     builder.append("[/]");
                  }

                  builder.append("[col=");
                  builder.append(var8);
                  builder.append(']');
                  var2 = false;
                  var3 = true;
               } else if (var4 < 10 && (var7.equalsIgnoreCase("music") || allowedChatIcons.containsKey(var7.toLowerCase()))) {
                  if (var3) {
                     builder.append("[/]");
                     var3 = false;
                  }

                  builder.append("[img=");
                  builder.append(var7.equalsIgnoreCase("music") ? "music" : (String)allowedChatIcons.get(var7.toLowerCase()));
                  builder.append(']');
                  var2 = false;
                  ++var4;
               } else {
                  builder.append('*');
                  builder.append(var7);
               }
            }
         }

         if (var2) {
            builder.append('*');
            String var10 = builderTest.toString();
            if (var10.length() > 0) {
               builder.append(var10);
            }

            if (var3) {
               builder.append("[/]");
            }
         }

         return builder.toString();
      } catch (Exception var9) {
         var9.printStackTrace();
         return var0;
      }
   }

   public static String parseStringForChatLog(String var0) {
      try {
         builder.delete(0, builder.length());
         builderTest.delete(0, builderTest.length());
         char[] var1 = var0.toCharArray();
         boolean var2 = false;
         boolean var3 = false;
         int var4 = 0;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            char var6 = var1[var5];
            if (var6 != '*') {
               if (var2) {
                  builderTest.append(var6);
               } else {
                  builder.append(var6);
               }
            } else if (!var2) {
               var2 = true;
            } else {
               String var7 = builderTest.toString();
               builderTest.delete(0, builderTest.length());
               String var8 = getColorString(var7, true);
               if (var8 != null) {
                  builder.append(" <RGB:");
                  builder.append(var8);
                  builder.append('>');
                  var2 = false;
                  var3 = true;
               } else {
                  if (var4 < 10 && (var7.equalsIgnoreCase("music") || allowedChatIconsFull.containsKey(var7.toLowerCase()))) {
                     if (var3) {
                        builder.append(" <RGB:");
                        builder.append("1.0,1.0,1.0");
                        builder.append('>');
                        var3 = false;
                     }

                     String var9 = var7.equalsIgnoreCase("music") ? "Icon_music_notes" : (String)allowedChatIconsFull.get(var7.toLowerCase());
                     Texture var10 = Texture.getSharedTexture(var9);
                     if (Texture.getSharedTexture(var9) != null) {
                        int var11 = (int)((float)var10.getWidth() * 0.5F);
                        int var12 = (int)((float)var10.getHeight() * 0.5F);
                        if (var7.equalsIgnoreCase("music")) {
                           var11 = (int)((float)var10.getWidth() * 0.75F);
                           var12 = (int)((float)var10.getHeight() * 0.75F);
                        }

                        builder.append("<IMAGE:");
                        builder.append(var9);
                        builder.append("," + var11 + "," + var12 + ">");
                        var2 = false;
                        ++var4;
                        continue;
                     }
                  }

                  builder.append('*');
                  builder.append(var7);
               }
            }
         }

         if (var2) {
            builder.append('*');
            String var14 = builderTest.toString();
            if (var14.length() > 0) {
               builder.append(var14);
            }
         }

         return builder.toString();
      } catch (Exception var13) {
         var13.printStackTrace();
         return var0;
      }
   }
}
