package zombie.iso.areas;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.Translator;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerOptions;
import zombie.network.chat.ChatServer;

public class SafeHouse {
   private int x = 0;
   private int y = 0;
   private int w = 0;
   private int h = 0;
   private static int diffError = 2;
   private String owner = null;
   private ArrayList players = new ArrayList();
   private long lastVisited = 0L;
   private String title = "Safehouse";
   private int playerConnected = 0;
   private int openTimer = 0;
   private final String id;
   public ArrayList playersRespawn = new ArrayList();
   private static final ArrayList safehouseList = new ArrayList();

   public static void init() {
      safehouseList.clear();
   }

   public static SafeHouse addSafeHouse(int var0, int var1, int var2, int var3, String var4, boolean var5) {
      SafeHouse var6 = new SafeHouse(var0, var1, var2, var3, var4);
      var6.setOwner(var4);
      var6.setLastVisited(Calendar.getInstance().getTimeInMillis());
      var6.addPlayer(var4);
      safehouseList.add(var6);
      if (GameServer.bServer) {
         DebugLog.log("safehouse: added " + var0 + "," + var1 + "," + var2 + "," + var3 + " owner=" + var4);
      }

      if (GameClient.bClient && !var5) {
         GameClient.sendSafehouse(var6, false);
      }

      updateSafehousePlayersConnected();
      if (GameClient.bClient) {
         LuaEventManager.triggerEvent("OnSafehousesChanged");
      }

      return var6;
   }

   public static SafeHouse addSafeHouse(IsoGridSquare var0, IsoPlayer var1) {
      String var2 = canBeSafehouse(var0, var1);
      return var2 != null && !"".equals(var2) ? null : addSafeHouse(var0.getBuilding().def.getX() - diffError, var0.getBuilding().def.getY() - diffError, var0.getBuilding().def.getW() + diffError * 2, var0.getBuilding().def.getH() + diffError * 2, var1.getUsername(), false);
   }

   public static SafeHouse hasSafehouse(String var0) {
      for(int var1 = 0; var1 < safehouseList.size(); ++var1) {
         SafeHouse var2 = (SafeHouse)safehouseList.get(var1);
         if (var2.getPlayers().contains(var0) || var2.getOwner().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static SafeHouse hasSafehouse(IsoPlayer var0) {
      return hasSafehouse(var0.getUsername());
   }

   public static void updateSafehousePlayersConnected() {
      SafeHouse var0 = null;

      label28:
      for(int var1 = 0; var1 < safehouseList.size(); ++var1) {
         var0 = (SafeHouse)safehouseList.get(var1);
         var0.setPlayerConnected(0);
         Iterator var2 = GameClient.IDToPlayerMap.values().iterator();

         while(true) {
            IsoPlayer var3;
            do {
               if (!var2.hasNext()) {
                  continue label28;
               }

               var3 = (IsoPlayer)var2.next();
            } while(!var0.getPlayers().contains(var3.getUsername()) && !var0.getOwner().equals(var3.getUsername()));

            var0.setPlayerConnected(var0.getPlayerConnected() + 1);
         }
      }

   }

   public static SafeHouse getSafeHouse(IsoGridSquare var0) {
      return var0.getBuilding() != null && var0.getBuilding().def != null ? isSafeHouse(var0, (String)null, false) : null;
   }

   public static SafeHouse getSafeHouse(int var0, int var1, int var2, int var3) {
      SafeHouse var4 = null;

      for(int var5 = 0; var5 < safehouseList.size(); ++var5) {
         var4 = (SafeHouse)safehouseList.get(var5);
         if (var0 == var4.getX() && var2 == var4.getW() && var1 == var4.getY() && var3 == var4.getH()) {
            return var4;
         }
      }

      return null;
   }

   public static SafeHouse isSafeHouse(IsoGridSquare var0, String var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         if (GameClient.bClient) {
            IsoPlayer var3 = GameClient.instance.getPlayerFromUsername(var1);
            if (var3 != null && !var3.accessLevel.equals("")) {
               return null;
            }
         }

         SafeHouse var6 = null;
         boolean var4 = false;

         for(int var5 = 0; var5 < safehouseList.size(); ++var5) {
            var6 = (SafeHouse)safehouseList.get(var5);
            if (var0.getX() >= var6.getX() && var0.getX() < var6.getX2() && var0.getY() >= var6.getY() && var0.getY() < var6.getY2()) {
               var4 = true;
               break;
            }
         }

         if (var4 && var2 && ServerOptions.instance.DisableSafehouseWhenPlayerConnected.getValue() && (var6.getPlayerConnected() > 0 || var6.getOpenTimer() > 0)) {
            return null;
         } else {
            return !var4 || (var1 == null || var6 == null || var6.getPlayers().contains(var1) || var6.getOwner().equals(var1)) && var1 != null ? null : var6;
         }
      }
   }

   public static void clearSafehouseList() {
      safehouseList.clear();
   }

   public boolean playerAllowed(IsoPlayer var1) {
      return this.players.contains(var1.getUsername()) || this.owner.equals(var1.getUsername()) || !var1.accessLevel.equals("");
   }

   public boolean playerAllowed(String var1) {
      return this.players.contains(var1) || this.owner.equals(var1);
   }

   public void addPlayer(String var1) {
      if (!this.players.contains(var1)) {
         this.players.add(var1);
         if (GameClient.bClient) {
            GameClient.sendSafehouse(this, false);
         }

         updateSafehousePlayersConnected();
      }

   }

   public void removePlayer(String var1) {
      if (this.players.contains(var1)) {
         this.players.remove(var1);
         this.playersRespawn.remove(var1);
         if (GameClient.bClient) {
            GameClient.sendSafehouse(this, false);
         }
      }

   }

   public void syncSafehouse() {
      if (GameClient.bClient) {
         GameClient.sendSafehouse(this, false);
      }

   }

   public void removeSafeHouse(IsoPlayer var1) {
      this.removeSafeHouse(var1, false);
   }

   public void removeSafeHouse(IsoPlayer var1, boolean var2) {
      if (var1 == null || var1.getUsername().equals(this.getOwner()) || !var1.accessLevel.equals("admin") && !var1.accessLevel.equals("moderator") || var2) {
         if (GameClient.bClient) {
            GameClient.sendSafehouse(this, true);
         }

         if (GameServer.bServer) {
            GameServer.sendSafehouse(this, true, (UdpConnection)null);
         }

         getSafehouseList().remove(this);
         int var10000 = this.x;
         DebugLog.log("safehouse: removed " + var10000 + "," + this.y + "," + this.w + "," + this.h + " owner=" + this.getOwner());
         if (GameClient.bClient) {
            LuaEventManager.triggerEvent("OnSafehousesChanged");
         }

      }
   }

   public void save(ByteBuffer var1) {
      var1.putInt(this.getX());
      var1.putInt(this.getY());
      var1.putInt(this.getW());
      var1.putInt(this.getH());
      GameWindow.WriteString(var1, this.getOwner());
      var1.putInt(this.getPlayers().size());
      Iterator var2 = this.getPlayers().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
      }

      var1.putLong(this.getLastVisited());
      GameWindow.WriteString(var1, this.getTitle());
      var1.putInt(this.playersRespawn.size());

      for(int var4 = 0; var4 < this.playersRespawn.size(); ++var4) {
         GameWindow.WriteString(var1, (String)this.playersRespawn.get(var4));
      }

   }

   public static SafeHouse load(ByteBuffer var0, int var1) {
      SafeHouse var2 = new SafeHouse(var0.getInt(), var0.getInt(), var0.getInt(), var0.getInt(), GameWindow.ReadString(var0));
      int var3 = var0.getInt();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         var2.addPlayer(GameWindow.ReadString(var0));
      }

      var2.setLastVisited(var0.getLong());
      if (var1 >= 101) {
         var2.setTitle(GameWindow.ReadString(var0));
      }

      if (ChatServer.isInited()) {
         ChatServer.getInstance().createSafehouseChat(var2.getId());
      }

      safehouseList.add(var2);
      if (var1 >= 177) {
         var4 = var0.getInt();

         for(int var5 = 0; var5 < var4; ++var5) {
            var2.playersRespawn.add(GameWindow.ReadString(var0));
         }
      }

      return var2;
   }

   public static String canBeSafehouse(IsoGridSquare var0, IsoPlayer var1) {
      if (!GameClient.bClient && !GameServer.bServer) {
         return null;
      } else if (!ServerOptions.instance.PlayerSafehouse.getValue() && !ServerOptions.instance.AdminSafehouse.getValue()) {
         return null;
      } else if (ServerOptions.instance.PlayerSafehouse.getValue() && hasSafehouse(var1) != null) {
         return Translator.getText("IGUI_Safehouse_AlreadyHaveSafehouse");
      } else {
         int var2 = ServerOptions.instance.SafehouseDaySurvivedToClaim.getValue();
         if (!ServerOptions.instance.PlayerSafehouse.getValue() && ServerOptions.instance.AdminSafehouse.getValue() && GameClient.bClient) {
            if (!var1.accessLevel.equals("admin") && !var1.accessLevel.equals("moderator")) {
               return null;
            }

            var2 = 0;
         }

         if (var2 > 0 && var1.getHoursSurvived() < (double)(var2 * 24)) {
            return Translator.getText("IGUI_Safehouse_DaysSurvivedToClaim", var2);
         } else {
            KahluaTable var9;
            if (GameClient.bClient) {
               KahluaTableIterator var3 = GameClient.instance.getServerSpawnRegions().iterator();
               IsoGridSquare var4 = null;

               while(var3.advance()) {
                  KahluaTable var5 = (KahluaTable)var3.getValue();
                  KahluaTableIterator var6 = ((KahluaTableImpl)var5.rawget("points")).iterator();

                  while(var6.advance()) {
                     KahluaTable var7 = (KahluaTable)var6.getValue();
                     KahluaTableIterator var8 = var7.iterator();

                     while(var8.advance()) {
                        var9 = (KahluaTable)var8.getValue();
                        Double var10 = (Double)var9.rawget("worldX");
                        Double var11 = (Double)var9.rawget("worldY");
                        Double var12 = (Double)var9.rawget("posX");
                        Double var13 = (Double)var9.rawget("posY");
                        var4 = IsoWorld.instance.getCell().getGridSquare(var12 + var10 * 300.0D, var13 + var11 * 300.0D, 0.0D);
                        if (var4 != null && var4.getBuilding() != null && var4.getBuilding().getDef() != null) {
                           BuildingDef var14 = var4.getBuilding().getDef();
                           if (var0.getX() >= var14.getX() && var0.getX() < var14.getX2() && var0.getY() >= var14.getY() && var0.getY() < var14.getY2()) {
                              return Translator.getText("IGUI_Safehouse_IsSpawnPoint");
                           }
                        }
                     }
                  }
               }
            }

            boolean var15 = true;
            boolean var16 = false;
            boolean var17 = false;
            boolean var18 = false;
            boolean var19 = false;
            BuildingDef var20 = var0.getBuilding().getDef();
            if (var0.getBuilding().Rooms != null) {
               Iterator var21 = var0.getBuilding().Rooms.iterator();

               while(var21.hasNext()) {
                  IsoRoom var23 = (IsoRoom)var21.next();
                  if (var23.getName().equals("kitchen")) {
                     var17 = true;
                  }

                  if (var23.getName().equals("bedroom") || var23.getName().equals("livingroom")) {
                     var18 = true;
                  }

                  if (var23.getName().equals("bathroom")) {
                     var19 = true;
                  }
               }
            }

            var9 = null;

            for(int var24 = var20.getX() - diffError; var24 < var20.getX2() + diffError; ++var24) {
               for(int var25 = var20.getY() - diffError; var25 < var20.getY2() + diffError; ++var25) {
                  IsoGridSquare var22 = var0.getCell().getGridSquare(var24, var25, 0);
                  if (var22 != null) {
                     for(int var26 = 0; var26 < var22.getMovingObjects().size(); ++var26) {
                        IsoMovingObject var27 = (IsoMovingObject)var22.getMovingObjects().get(var26);
                        if (var27 != var1) {
                           var15 = false;
                           break;
                        }

                        if (!var27.getSquare().Is(IsoFlagType.exterior)) {
                           var16 = true;
                        }
                     }
                  }
               }

               if (!var15) {
                  break;
               }
            }

            if (var15 && var16) {
               return !var18 ? Translator.getText("IGUI_Safehouse_NotHouse") : "";
            } else {
               return Translator.getText("IGUI_Safehouse_SomeoneInside");
            }
         }
      }
   }

   public void kickOutOfSafehouse(IsoPlayer var1) {
      if (var1.getAccessLevel().equals("None")) {
         GameClient.sendTeleport(var1, (float)(this.x - 1), (float)(this.y - 1), 0.0F);
      }

   }

   public SafeHouse alreadyHaveSafehouse(String var1) {
      return ServerOptions.instance.PlayerSafehouse.getValue() ? hasSafehouse(var1) : null;
   }

   public SafeHouse alreadyHaveSafehouse(IsoPlayer var1) {
      return ServerOptions.instance.PlayerSafehouse.getValue() ? hasSafehouse(var1) : null;
   }

   public static boolean allowSafeHouse(IsoPlayer var0) {
      boolean var1 = false;
      boolean var2 = (GameClient.bClient || GameServer.bServer) && (ServerOptions.instance.PlayerSafehouse.getValue() || ServerOptions.instance.AdminSafehouse.getValue());
      if (var2) {
         if (ServerOptions.instance.PlayerSafehouse.getValue()) {
            var1 = hasSafehouse(var0) == null;
         }

         if (var1 && ServerOptions.instance.SafehouseDaySurvivedToClaim.getValue() > 0 && var0.getHoursSurvived() / 24.0D < (double)ServerOptions.instance.SafehouseDaySurvivedToClaim.getValue()) {
            var1 = false;
         }

         if (ServerOptions.instance.AdminSafehouse.getValue() && GameClient.bClient) {
            var1 = var0.accessLevel.equals("admin") || var0.accessLevel.equals("moderator");
         }
      }

      return var1;
   }

   public void updateSafehouse(IsoPlayer var1) {
      if (var1 == null || !this.getPlayers().contains(var1.getUsername()) && !this.getOwner().equals(var1.getUsername())) {
         if (ServerOptions.instance.SafeHouseRemovalTime.getValue() > 0 && Calendar.getInstance().getTimeInMillis() - this.getLastVisited() > (long)(3600000 * ServerOptions.instance.SafeHouseRemovalTime.getValue())) {
            boolean var2 = false;
            IsoGridSquare var3 = null;

            for(int var4 = this.getX(); var4 < this.getX2(); ++var4) {
               for(int var5 = this.getY(); var5 < this.getY2(); ++var5) {
                  var3 = IsoWorld.instance.getCell().getGridSquare(var4, var5, 0);
                  if (var3 != null) {
                     for(int var6 = 0; var6 < var3.getMovingObjects().size(); ++var6) {
                        IsoMovingObject var7 = (IsoMovingObject)var3.getMovingObjects().get(var6);
                        if (var7 instanceof IsoPlayer && (this.getPlayers().contains(((IsoPlayer)var7).getUsername()) || this.getOwner().equals(((IsoPlayer)var7).getUsername()))) {
                           var2 = true;
                           break;
                        }
                     }
                  }
               }
            }

            if (var2) {
               this.setLastVisited(Calendar.getInstance().getTimeInMillis());
               return;
            }

            this.removeSafeHouse(var1, true);
         }
      } else {
         this.setLastVisited(Calendar.getInstance().getTimeInMillis());
      }

   }

   public SafeHouse(int var1, int var2, int var3, int var4, String var5) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
      this.players.add(var5);
      this.owner = var5;
      this.id = var1 + "," + var2 + " at " + Calendar.getInstance().getTimeInMillis();
   }

   public String getId() {
      return this.id;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getW() {
      return this.w;
   }

   public void setW(int var1) {
      this.w = var1;
   }

   public int getH() {
      return this.h;
   }

   public void setH(int var1) {
      this.h = var1;
   }

   public int getX2() {
      return this.x + this.w;
   }

   public int getY2() {
      return this.y + this.h;
   }

   public ArrayList getPlayers() {
      return this.players;
   }

   public void setPlayers(ArrayList var1) {
      this.players = var1;
   }

   public static ArrayList getSafehouseList() {
      return safehouseList;
   }

   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String var1) {
      this.owner = var1;
      if (this.players.contains(var1)) {
         this.players.remove(var1);
      }

   }

   public boolean isOwner(IsoPlayer var1) {
      return this.getOwner().equals(var1.getUsername());
   }

   public long getLastVisited() {
      return this.lastVisited;
   }

   public void setLastVisited(long var1) {
      this.lastVisited = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public int getPlayerConnected() {
      return this.playerConnected;
   }

   public void setPlayerConnected(int var1) {
      this.playerConnected = var1;
   }

   public int getOpenTimer() {
      return this.openTimer;
   }

   public void setOpenTimer(int var1) {
      this.openTimer = var1;
   }

   public void setRespawnInSafehouse(boolean var1, String var2) {
      if (var1) {
         this.playersRespawn.add(var2);
      } else {
         this.playersRespawn.remove(var2);
      }

      if (GameClient.bClient) {
         GameClient.sendSafehouse(this, false);
      }

   }

   public boolean isRespawnInSafehouse(String var1) {
      return this.playersRespawn.contains(var1);
   }
}
