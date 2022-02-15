package zombie.network.packets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class ActionPacket implements INetworkPacket {
   private short id;
   private boolean operation;
   private float reloadSpeed;
   private boolean override;
   private String primary;
   private String secondary;
   private final HashMap variables = new HashMap();
   private IsoGameCharacter character;

   public void set(boolean var1, BaseAction var2) {
      this.character = var2.chr;
      this.id = var2.chr.getOnlineID();
      this.operation = var1;
      this.reloadSpeed = var2.chr.getVariableFloat("ReloadSpeed", 1.0F);
      this.override = var2.overrideHandModels;
      this.primary = var2.getPrimaryHandItem() == null ? var2.getPrimaryHandMdl() : var2.getPrimaryHandItem().getStaticModel();
      this.secondary = var2.getSecondaryHandItem() == null ? var2.getSecondaryHandMdl() : var2.getSecondaryHandItem().getStaticModel();
      Iterator var3 = var2.animVariables.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.variables.put(var4, var2.chr.getVariableString(var4));
      }

      if (this.variables.containsValue("DetachItem") || this.variables.containsValue("AttachItem")) {
         this.variables.put("AttachAnim", var2.chr.getVariableString("AttachAnim"));
      }

      if (this.variables.containsValue("Loot")) {
         this.variables.put("LootPosition", var2.chr.getVariableString("LootPosition"));
      }

   }

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.operation = var1.get() != 0;
      this.reloadSpeed = var1.getFloat();
      this.override = var1.get() != 0;
      this.primary = GameWindow.ReadString(var1);
      this.secondary = GameWindow.ReadString(var1);
      int var2 = var1.getInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.variables.put(GameWindow.ReadString(var1), GameWindow.ReadString(var1));
      }

      if (GameServer.bServer) {
         this.character = (IsoGameCharacter)GameServer.IDToPlayerMap.get(this.id);
      } else if (GameClient.bClient) {
         this.character = (IsoGameCharacter)GameClient.IDToPlayerMap.get(this.id);
      } else {
         this.character = null;
      }

   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putBoolean(this.operation);
      var1.putFloat(this.reloadSpeed);
      var1.putBoolean(this.override);
      var1.putUTF(this.primary);
      var1.putUTF(this.secondary);
      var1.putInt(this.variables.size());
      Iterator var2 = this.variables.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.putUTF((String)var3.getKey());
         var1.putUTF((String)var3.getValue());
      }

   }

   public boolean isConsistent() {
      boolean var1 = this.character instanceof IsoPlayer;
      if (!var1 && Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, "[Action] is not consistent");
      }

      return var1;
   }

   public String getDescription() {
      StringBuilder var1 = (new StringBuilder("[ ")).append("character=").append(this.id);
      if (this.isConsistent()) {
         var1.append(" \"").append(((IsoPlayer)this.character).getUsername()).append("\"");
      }

      var1.append(" | ").append("operation=").append(this.operation ? "start" : "stop").append(" | ").append("variables=").append(this.variables.size()).append(" | ");
      Iterator var2 = this.variables.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append((String)var3.getKey()).append("=").append((String)var3.getValue()).append(" | ");
      }

      var1.append("override=").append(this.override).append(" ").append("primary=\"").append(this.primary == null ? "" : this.primary).append("\" ").append("secondary=\"").append(this.secondary == null ? "" : this.secondary).append("\" ]");
      return var1.toString();
   }

   public boolean isRelevant(UdpConnection var1) {
      return this.isConsistent() && var1.RelevantTo(this.character.getX(), this.character.getY());
   }

   public void process() {
      if (this.isConsistent()) {
         if (this.operation) {
            BaseAction var1 = new BaseAction(this.character);
            this.variables.forEach((var1x, var2) -> {
               if (!"true".equals(var2) && !"false".equals(var2)) {
                  var1.setAnimVariable(var1x, var2);
               } else {
                  var1.setAnimVariable(var1x, Boolean.parseBoolean(var2));
               }

            });
            if ("Reload".equals(this.variables.get("PerformingAction"))) {
               this.character.setVariable("ReloadSpeed", this.reloadSpeed);
            }

            this.character.setVariable("IsPerformingAnAction", true);
            this.character.getNetworkCharacterAI().setAction(var1);
            this.character.getNetworkCharacterAI().setOverride(this.override, this.primary, this.secondary);
            this.character.getNetworkCharacterAI().startAction();
         } else if (this.character.getNetworkCharacterAI().getAction() != null) {
            this.character.getNetworkCharacterAI().stopAction();
         }
      } else {
         DebugLog.Multiplayer.warn("Action error: player id=" + this.id + " not fond");
      }

   }
}
