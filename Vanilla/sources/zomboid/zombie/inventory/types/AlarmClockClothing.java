package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.ai.sadisticAIDirector.SleepingEvent;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.core.utils.OnceEvery;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemSoundManager;
import zombie.inventory.ItemType;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;

public final class AlarmClockClothing extends Clothing {
   private int alarmHour = -1;
   private int alarmMinutes = -1;
   private boolean alarmSet = false;
   private long ringSound;
   private double ringSince = -1.0D;
   private int forceDontRing = -1;
   private String alarmSound = "AlarmClockLoop";
   private int soundRadius = 40;
   private boolean isDigital = true;
   public static short PacketPlayer = 1;
   public static short PacketWorld = 2;
   private static final OnceEvery sendEvery = new OnceEvery(2.0F);

   public AlarmClockClothing(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.cat = ItemType.AlarmClockClothing;
      if (this.fullType.contains("Classic")) {
         this.isDigital = false;
      }

      this.randomizeAlarm();
   }

   public AlarmClockClothing(String var1, String var2, String var3, Item var4, String var5, String var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.cat = ItemType.AlarmClockClothing;
      if (this.fullType.contains("Classic")) {
         this.isDigital = false;
      }

      this.randomizeAlarm();
   }

   private void randomizeAlarm() {
      if (!Core.bLastStand) {
         if (this.isDigital()) {
            this.alarmHour = Rand.Next(0, 23);
            this.alarmMinutes = (int)Math.floor((double)(Rand.Next(0, 59) / 10)) * 10;
            this.alarmSet = Rand.Next(15) == 1;
         }
      }
   }

   public IsoGridSquare getAlarmSquare() {
      IsoGridSquare var1 = null;
      ItemContainer var2 = this.getOutermostContainer();
      if (var2 != null) {
         var1 = var2.getSourceGrid();
         if (var1 == null && var2.parent != null) {
            var1 = var2.parent.square;
         }

         InventoryItem var3 = var2.containingItem;
         if (var1 == null && var3 != null && var3.getWorldItem() != null) {
            var1 = var3.getWorldItem().getSquare();
         }
      }

      if (var1 == null && this.getWorldItem() != null && this.getWorldItem().getWorldObjectIndex() != -1) {
         var1 = this.getWorldItem().square;
      }

      return var1;
   }

   public boolean shouldUpdateInWorld() {
      return this.alarmSet;
   }

   public void update() {
      if (this.alarmSet) {
         int var1 = GameTime.instance.getMinutes() / 10 * 10;
         if (!this.isRinging() && this.forceDontRing != var1 && this.alarmHour == GameTime.instance.getHour() && this.alarmMinutes == var1) {
            this.ringSince = GameTime.getInstance().getWorldAgeHours();
         }

         if (this.isRinging()) {
            double var2 = GameTime.getInstance().getWorldAgeHours();
            if (this.ringSince > var2) {
               this.ringSince = var2;
            }

            IsoGridSquare var4 = this.getAlarmSquare();
            if (var4 != null && !(this.ringSince + 0.5D < var2)) {
               if (!GameClient.bClient && var4 != null) {
                  WorldSoundManager.instance.addSoundRepeating((Object)null, var4.getX(), var4.getY(), var4.getZ(), this.getSoundRadius(), 3, false);
               }
            } else {
               this.stopRinging();
            }

            if (!GameServer.bServer && this.isRinging()) {
               ItemSoundManager.addItem(this);
            }
         }

         if (this.forceDontRing != var1) {
            this.forceDontRing = -1;
         }
      }

   }

   public void updateSound(BaseSoundEmitter var1) {
      assert !GameServer.bServer;

      IsoGridSquare var2 = this.getAlarmSquare();
      if (var2 != null) {
         var1.setPos((float)var2.x + 0.5F, (float)var2.y + 0.5F, (float)var2.z);
         if (!var1.isPlaying(this.ringSound)) {
            if (this.alarmSound == null || "".equals(this.alarmSound)) {
               this.alarmSound = "AlarmClockLoop";
            }

            this.ringSound = var1.playSoundImpl(this.alarmSound, var2);
         }

         if (GameClient.bClient && sendEvery.Check() && this.isInLocalPlayerInventory()) {
            GameClient.instance.sendWorldSound((Object)null, var2.x, var2.y, var2.z, this.getSoundRadius(), 3, false, 0.0F, 1.0F);
         }

         this.wakeUpPlayers(var2);
      }
   }

   private void wakeUpPlayers(IsoGridSquare var1) {
      if (!GameServer.bServer) {
         int var2 = this.getSoundRadius();
         int var3 = Math.max(var1.getZ() - 3, 0);
         int var4 = Math.min(var1.getZ() + 3, 8);

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null && !var6.isDead() && var6.getCurrentSquare() != null && !var6.Traits.Deaf.isSet()) {
               IsoGridSquare var7 = var6.getCurrentSquare();
               if (var7.z >= var3 && var7.z < var4) {
                  float var8 = IsoUtils.DistanceToSquared((float)var1.x, (float)var1.y, (float)var7.x, (float)var7.y);
                  if (var6.Traits.HardOfHearing.isSet()) {
                     var8 *= 4.5F;
                  }

                  if (!(var8 > (float)(var2 * var2))) {
                     this.wakeUp(var6);
                  }
               }
            }
         }

      }
   }

   private void wakeUp(IsoPlayer var1) {
      if (var1.Asleep) {
         SoundManager.instance.setMusicWakeState(var1, "WakeNormal");
         SleepingEvent.instance.wakeUp(var1);
      }

   }

   public boolean isRinging() {
      return this.ringSince >= 0.0D;
   }

   public boolean finishupdate() {
      return !this.alarmSet;
   }

   public void DoTooltip(ObjectTooltip var1, ObjectTooltip.Layout var2) {
      ObjectTooltip.LayoutItem var3 = var2.addItem();
      var3.setLabel(Translator.getText("IGUI_CurrentTime"), 1.0F, 1.0F, 0.8F, 1.0F);
      int var4 = GameTime.instance.getMinutes() / 10 * 10;
      var3.setValue(GameTime.getInstance().getHour() + ":" + (var4 == 0 ? "00" : var4), 1.0F, 1.0F, 0.8F, 1.0F);
      if (this.alarmSet) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("IGUI_AlarmIsSetFor"), 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValue(this.alarmHour + ":" + (this.alarmMinutes == 0 ? "00" : this.alarmMinutes), 1.0F, 1.0F, 0.8F, 1.0F);
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putInt(this.alarmHour);
      var1.putInt(this.alarmMinutes);
      var1.put((byte)(this.alarmSet ? 1 : 0));
      var1.putFloat((float)this.ringSince);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      this.alarmHour = var1.getInt();
      this.alarmMinutes = var1.getInt();
      this.alarmSet = var1.get() == 1;
      this.ringSince = (double)var1.getFloat();
      this.ringSound = -1L;
   }

   public int getSaveType() {
      return Item.Type.AlarmClock.ordinal();
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "AlarmClock";
   }

   public void setAlarmSet(boolean var1) {
      this.stopRinging();
      this.alarmSet = var1;
      this.ringSound = -1L;
      if (var1) {
         IsoWorld.instance.CurrentCell.addToProcessItems((InventoryItem)this);
         IsoWorldInventoryObject var2 = this.getWorldItem();
         if (var2 != null && var2.getSquare() != null) {
            IsoCell var3 = IsoWorld.instance.getCell();
            if (!var3.getProcessWorldItems().contains(var2)) {
               var3.getProcessWorldItems().add(var2);
            }
         }
      } else {
         IsoWorld.instance.CurrentCell.addToProcessItemsRemove((InventoryItem)this);
      }

   }

   public boolean isAlarmSet() {
      return this.alarmSet;
   }

   public void setHour(int var1) {
      this.alarmHour = var1;
      this.forceDontRing = -1;
   }

   public void setMinute(int var1) {
      this.alarmMinutes = var1;
      this.forceDontRing = -1;
   }

   public int getHour() {
      return this.alarmHour;
   }

   public int getMinute() {
      return this.alarmMinutes;
   }

   public void syncAlarmClock() {
      IsoPlayer var1 = this.getOwnerPlayer(this.container);
      if (var1 != null) {
         this.syncAlarmClock_Player(var1);
      }

      if (this.worldItem != null) {
         this.syncAlarmClock_World();
      }

   }

   private IsoPlayer getOwnerPlayer(ItemContainer var1) {
      if (var1 == null) {
         return null;
      } else {
         IsoObject var2 = var1.getParent();
         return var2 instanceof IsoPlayer ? (IsoPlayer)var2 : null;
      }
   }

   public void syncAlarmClock_Player(IsoPlayer var1) {
      if (GameClient.bClient) {
         ByteBufferWriter var2 = GameClient.connection.startPacket();
         PacketTypes.PacketType.SyncAlarmClock.doPacket(var2);
         var2.putShort(PacketPlayer);
         var2.putShort(var1.OnlineID);
         var2.putInt(this.id);
         var2.putByte((byte)0);
         var2.putInt(this.alarmHour);
         var2.putInt(this.alarmMinutes);
         var2.putByte((byte)(this.alarmSet ? 1 : 0));
         PacketTypes.PacketType.SyncAlarmClock.send(GameClient.connection);
      }

   }

   public void syncAlarmClock_World() {
      if (GameClient.bClient) {
         ByteBufferWriter var1 = GameClient.connection.startPacket();
         PacketTypes.PacketType.SyncAlarmClock.doPacket(var1);
         var1.putShort(PacketWorld);
         var1.putInt(this.worldItem.square.getX());
         var1.putInt(this.worldItem.square.getY());
         var1.putInt(this.worldItem.square.getZ());
         var1.putInt(this.id);
         var1.putByte((byte)0);
         var1.putInt(this.alarmHour);
         var1.putInt(this.alarmMinutes);
         var1.putByte((byte)(this.alarmSet ? 1 : 0));
         PacketTypes.PacketType.SyncAlarmClock.send(GameClient.connection);
      }

   }

   public void syncStopRinging() {
      if (GameClient.bClient) {
         ByteBufferWriter var1 = GameClient.connection.startPacket();
         PacketTypes.PacketType.SyncAlarmClock.doPacket(var1);
         IsoPlayer var2 = this.getOwnerPlayer(this.container);
         if (var2 != null) {
            var1.putShort(PacketPlayer);
            var1.putShort(var2.OnlineID);
         } else if (this.getWorldItem() != null) {
            var1.putShort(PacketWorld);
            var1.putInt(this.worldItem.square.getX());
            var1.putInt(this.worldItem.square.getY());
            var1.putInt(this.worldItem.square.getZ());
         } else {
            assert false;
         }

         var1.putInt(this.id);
         var1.putByte((byte)1);
         PacketTypes.PacketType.SyncAlarmClock.send(GameClient.connection);
      }
   }

   public void stopRinging() {
      if (this.ringSound != -1L) {
         this.ringSound = -1L;
      }

      ItemSoundManager.removeItem(this);
      this.ringSince = -1.0D;
      this.forceDontRing = GameTime.instance.getMinutes() / 10 * 10;
   }

   public String getAlarmSound() {
      return this.alarmSound;
   }

   public void setAlarmSound(String var1) {
      this.alarmSound = var1;
   }

   public int getSoundRadius() {
      return this.soundRadius;
   }

   public void setSoundRadius(int var1) {
      this.soundRadius = var1;
   }

   public boolean isDigital() {
      return this.isDigital;
   }
}
