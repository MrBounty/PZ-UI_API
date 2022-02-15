package zombie.characters;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.GameWindow;
import zombie.chat.ChatSettings;
import zombie.chat.defaultChats.FactionChat;
import zombie.core.Rand;
import zombie.core.network.ByteBufferWriter;
import zombie.core.textures.ColorInfo;
import zombie.network.GameClient;
import zombie.network.ServerOptions;
import zombie.network.chat.ChatServer;

public final class Faction {
   private String name;
   private String owner;
   private String tag;
   private ColorInfo tagColor;
   private final ArrayList players = new ArrayList();
   public static ArrayList factions = new ArrayList();

   public Faction() {
   }

   public Faction(String var1, String var2) {
      this.setName(var1);
      this.setOwner(var2);
      this.tagColor = new ColorInfo(Rand.Next(0.3F, 1.0F), Rand.Next(0.3F, 1.0F), Rand.Next(0.3F, 1.0F), 1.0F);
   }

   public static Faction createFaction(String var0, String var1) {
      if (!factionExist(var0)) {
         Faction var2 = new Faction(var0, var1);
         factions.add(var2);
         if (GameClient.bClient) {
            GameClient.sendFaction(var2, false);
         }

         return var2;
      } else {
         return null;
      }
   }

   public static ArrayList getFactions() {
      return factions;
   }

   public static boolean canCreateFaction(IsoPlayer var0) {
      boolean var1 = ServerOptions.instance.Faction.getValue();
      if (var1 && ServerOptions.instance.FactionDaySurvivedToCreate.getValue() > 0 && var0.getHoursSurvived() / 24.0D < (double)ServerOptions.instance.FactionDaySurvivedToCreate.getValue()) {
         var1 = false;
      }

      return var1;
   }

   public boolean canCreateTag() {
      return this.players.size() + 1 >= ServerOptions.instance.FactionPlayersRequiredForTag.getValue();
   }

   public static boolean isAlreadyInFaction(String var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         Faction var2 = (Faction)factions.get(var1);
         if (var2.getOwner().equals(var0)) {
            return true;
         }

         for(int var3 = 0; var3 < var2.getPlayers().size(); ++var3) {
            if (((String)var2.getPlayers().get(var3)).equals(var0)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isAlreadyInFaction(IsoPlayer var0) {
      return isAlreadyInFaction(var0.getUsername());
   }

   public void removePlayer(String var1) {
      this.getPlayers().remove(var1);
      if (GameClient.bClient) {
         GameClient.sendFaction(this, false);
      }

   }

   public static boolean factionExist(String var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         if (((Faction)factions.get(var1)).getName().equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public static boolean tagExist(String var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         if (((Faction)factions.get(var1)).getTag() != null && ((Faction)factions.get(var1)).getTag().equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public static Faction getPlayerFaction(IsoPlayer var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         Faction var2 = (Faction)factions.get(var1);
         if (var2.getOwner().equals(var0.getUsername())) {
            return var2;
         }

         for(int var3 = 0; var3 < var2.getPlayers().size(); ++var3) {
            if (((String)var2.getPlayers().get(var3)).equals(var0.getUsername())) {
               return var2;
            }
         }
      }

      return null;
   }

   public static Faction getPlayerFaction(String var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         Faction var2 = (Faction)factions.get(var1);
         if (var2.getOwner().equals(var0)) {
            return var2;
         }

         for(int var3 = 0; var3 < var2.getPlayers().size(); ++var3) {
            if (((String)var2.getPlayers().get(var3)).equals(var0)) {
               return var2;
            }
         }
      }

      return null;
   }

   public static Faction getFaction(String var0) {
      for(int var1 = 0; var1 < factions.size(); ++var1) {
         if (((Faction)factions.get(var1)).getName().equals(var0)) {
            return (Faction)factions.get(var1);
         }
      }

      return null;
   }

   public void removeFaction() {
      getFactions().remove(this);
      if (GameClient.bClient) {
         GameClient.sendFaction(this, true);
      }

   }

   public void syncFaction() {
      if (GameClient.bClient) {
         GameClient.sendFaction(this, false);
      }

   }

   public boolean isOwner(String var1) {
      return this.getOwner().equals(var1);
   }

   public boolean isPlayerMember(IsoPlayer var1) {
      return this.isMember(var1.getUsername());
   }

   public boolean isMember(String var1) {
      for(int var2 = 0; var2 < this.getPlayers().size(); ++var2) {
         if (((String)this.getPlayers().get(var2)).equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public void writeToBuffer(ByteBufferWriter var1, boolean var2) {
      var1.putUTF(this.getName());
      var1.putUTF(this.getOwner());
      var1.putInt(this.getPlayers().size());
      if (this.getTag() != null) {
         var1.putByte((byte)1);
         var1.putUTF(this.getTag());
         var1.putFloat(this.getTagColor().r);
         var1.putFloat(this.getTagColor().g);
         var1.putFloat(this.getTagColor().b);
      } else {
         var1.putByte((byte)0);
      }

      Iterator var3 = this.getPlayers().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.putUTF(var4);
      }

      var1.putBoolean(var2);
   }

   public void save(ByteBuffer var1) {
      GameWindow.WriteString(var1, this.getName());
      GameWindow.WriteString(var1, this.getOwner());
      var1.putInt(this.getPlayers().size());
      if (this.getTag() != null) {
         var1.put((byte)1);
         GameWindow.WriteString(var1, this.getTag());
         var1.putFloat(this.getTagColor().r);
         var1.putFloat(this.getTagColor().g);
         var1.putFloat(this.getTagColor().b);
      } else {
         var1.put((byte)0);
      }

      Iterator var2 = this.getPlayers().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
      }

   }

   public void load(ByteBuffer var1, int var2) {
      this.setName(GameWindow.ReadString(var1));
      this.setOwner(GameWindow.ReadString(var1));
      int var3 = var1.getInt();
      if (var1.get() == 1) {
         this.setTag(GameWindow.ReadString(var1));
         this.setTagColor(new ColorInfo(var1.getFloat(), var1.getFloat(), var1.getFloat(), 1.0F));
      } else {
         this.setTagColor(new ColorInfo(Rand.Next(0.3F, 1.0F), Rand.Next(0.3F, 1.0F), Rand.Next(0.3F, 1.0F), 1.0F));
      }

      for(int var4 = 0; var4 < var3; ++var4) {
         this.getPlayers().add(GameWindow.ReadString(var1));
      }

      if (ChatServer.isInited()) {
         FactionChat var6 = ChatServer.getInstance().createFactionChat(this.getName());
         ChatSettings var5 = FactionChat.getDefaultSettings();
         var5.setFontColor(this.tagColor.r, this.tagColor.g, this.tagColor.b, this.tagColor.a);
         var6.setSettings(var5);
      }

   }

   public void addPlayer(String var1) {
      for(int var2 = 0; var2 < factions.size(); ++var2) {
         Faction var3 = (Faction)factions.get(var2);
         if (var3.getOwner().equals(var1)) {
            return;
         }

         for(int var4 = 0; var4 < var3.getPlayers().size(); ++var4) {
            if (((String)var3.getPlayers().get(var4)).equals(var1)) {
               return;
            }
         }
      }

      this.players.add(var1);
      if (GameClient.bClient) {
         GameClient.sendFaction(this, false);
      }

   }

   public ArrayList getPlayers() {
      return this.players;
   }

   public ColorInfo getTagColor() {
      return this.tagColor;
   }

   public void setTagColor(ColorInfo var1) {
      if (var1.r < 0.19F) {
         var1.r = 0.19F;
      }

      if (var1.g < 0.19F) {
         var1.g = 0.19F;
      }

      if (var1.b < 0.19F) {
         var1.b = 0.19F;
      }

      this.tagColor = var1;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String var1) {
      if (this.owner == null) {
         this.owner = var1;
      } else {
         if (!this.isMember(this.owner)) {
            this.getPlayers().add(this.owner);
            this.getPlayers().remove(var1);
         }

         this.owner = var1;
      }
   }
}
