package zombie.vehicles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatElement;
import zombie.chat.ChatElementOwner;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Drainable;
import zombie.iso.IsoGridSquare;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.radio.devices.DeviceData;
import zombie.radio.devices.WaveSignalDevice;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.UIFont;

public final class VehiclePart implements ChatElementOwner, WaveSignalDevice {
   protected BaseVehicle vehicle;
   protected boolean bCreated;
   protected String partId;
   protected VehicleScript.Part scriptPart;
   protected ItemContainer container;
   protected InventoryItem item;
   protected KahluaTable modData;
   protected float lastUpdated = -1.0F;
   protected short updateFlags;
   protected VehiclePart parent;
   protected VehicleDoor door;
   protected VehicleWindow window;
   protected ArrayList children;
   protected String category;
   protected int condition = -1;
   protected boolean specificItem = true;
   protected float wheelFriction = 0.0F;
   protected int mechanicSkillInstaller = 0;
   private float suspensionDamping = 0.0F;
   private float suspensionCompression = 0.0F;
   private float engineLoudness = 0.0F;
   protected VehicleLight light;
   protected DeviceData deviceData;
   protected ChatElement chatElement;
   protected boolean hasPlayerInRange;

   public VehiclePart(BaseVehicle var1) {
      this.vehicle = var1;
   }

   public BaseVehicle getVehicle() {
      return this.vehicle;
   }

   public void setScriptPart(VehicleScript.Part var1) {
      this.scriptPart = var1;
   }

   public VehicleScript.Part getScriptPart() {
      return this.scriptPart;
   }

   public ItemContainer getItemContainer() {
      return this.container;
   }

   public void setItemContainer(ItemContainer var1) {
      if (var1 != null) {
         var1.parent = this.getVehicle();
         var1.vehiclePart = this;
      }

      this.container = var1;
   }

   public boolean hasModData() {
      return this.modData != null && !this.modData.isEmpty();
   }

   public KahluaTable getModData() {
      if (this.modData == null) {
         this.modData = LuaManager.platform.newTable();
      }

      return this.modData;
   }

   public float getLastUpdated() {
      return this.lastUpdated;
   }

   public void setLastUpdated(float var1) {
      this.lastUpdated = var1;
   }

   public String getId() {
      return this.scriptPart == null ? this.partId : this.scriptPart.id;
   }

   public int getIndex() {
      return this.vehicle.parts.indexOf(this);
   }

   public String getArea() {
      return this.scriptPart == null ? null : this.scriptPart.area;
   }

   public ArrayList getItemType() {
      return this.scriptPart == null ? null : this.scriptPart.itemType;
   }

   public KahluaTable getTable(String var1) {
      if (this.scriptPart != null && this.scriptPart.tables != null) {
         KahluaTable var2 = (KahluaTable)this.scriptPart.tables.get(var1);
         return var2 == null ? null : LuaManager.copyTable(var2);
      } else {
         return null;
      }
   }

   public InventoryItem getInventoryItem() {
      return this.item;
   }

   public void setInventoryItem(InventoryItem var1, int var2) {
      this.item = var1;
      this.doInventoryItemStats(var1, var2);
      this.getVehicle().updateTotalMass();
      this.getVehicle().bDoDamageOverlay = true;
      if (this.scriptPart != null && this.scriptPart.itemType != null && !this.scriptPart.itemType.isEmpty() && this.scriptPart.models != null) {
         for(int var3 = 0; var3 < this.scriptPart.models.size(); ++var3) {
            VehicleScript.Model var4 = (VehicleScript.Model)this.scriptPart.models.get(var3);
            this.vehicle.setModelVisible(this, var4, var1 != null);
         }
      }

      this.getVehicle().updatePartStats();
      this.getVehicle().updateBulletStats();
   }

   public void setInventoryItem(InventoryItem var1) {
      this.setInventoryItem(var1, 0);
   }

   public void doInventoryItemStats(InventoryItem var1, int var2) {
      if (var1 != null) {
         if (this.isContainer()) {
            if (var1.getMaxCapacity() > 0 && this.getScriptPart().container.conditionAffectsCapacity) {
               this.setContainerCapacity((int)getNumberByCondition((float)var1.getMaxCapacity(), (float)var1.getCondition(), 5.0F));
            } else if (var1.getMaxCapacity() > 0) {
               this.setContainerCapacity(var1.getMaxCapacity());
            }

            this.setContainerContentAmount(var1.getItemCapacity());
         }

         this.setSuspensionCompression(getNumberByCondition(var1.getSuspensionCompression(), (float)var1.getCondition(), 0.6F));
         this.setSuspensionDamping(getNumberByCondition(var1.getSuspensionDamping(), (float)var1.getCondition(), 0.6F));
         if (var1.getEngineLoudness() > 0.0F) {
            this.setEngineLoudness(getNumberByCondition(var1.getEngineLoudness(), (float)var1.getCondition(), 10.0F));
         }

         this.setCondition(var1.getCondition());
         this.setMechanicSkillInstaller(var2);
      } else {
         if (this.scriptPart != null && this.scriptPart.container != null) {
            if (this.scriptPart.container.capacity > 0) {
               this.setContainerCapacity(this.scriptPart.container.capacity);
            } else {
               this.setContainerCapacity(0);
            }
         }

         this.setMechanicSkillInstaller(0);
         this.setContainerContentAmount(0.0F);
         this.setSuspensionCompression(0.0F);
         this.setSuspensionDamping(0.0F);
         this.setWheelFriction(0.0F);
         this.setEngineLoudness(0.0F);
      }

   }

   public void setRandomCondition(InventoryItem var1) {
      VehicleType var2 = VehicleType.getTypeFromName(this.getVehicle().getVehicleType());
      int var3;
      if (this.getVehicle().isGoodCar()) {
         var3 = 100;
         if (var1 != null) {
            var3 = var1.getConditionMax();
         }

         this.setCondition(Rand.Next(var3 - var3 / 3, var3));
         if (var1 != null) {
            var1.setCondition(this.getCondition());
         }

      } else {
         var3 = 100;
         if (var1 != null) {
            var3 = var1.getConditionMax();
         }

         if (var2 != null) {
            var3 = (int)((float)var3 * var2.getRandomBaseVehicleQuality());
         }

         float var4 = 100.0F;
         int var5;
         if (var1 != null) {
            var5 = var1.getChanceToSpawnDamaged();
            if (var2 != null) {
               var5 += var2.chanceToPartDamage;
            }

            if (var5 > 0 && Rand.Next(100) < var5) {
               var4 = (float)Rand.Next(var3 - var3 / 2, var3);
            }
         } else {
            var5 = 30;
            if (var2 != null) {
               var5 += var2.chanceToPartDamage;
            }

            if (Rand.Next(100) < var5) {
               var4 = Rand.Next((float)var3 * 0.5F, (float)var3);
            }
         }

         switch(SandboxOptions.instance.CarGeneralCondition.getValue()) {
         case 1:
            var4 -= Rand.Next(var4 * 0.3F, Rand.Next(var4 * 0.3F, var4 * 0.9F));
            break;
         case 2:
            var4 -= Rand.Next(var4 * 0.1F, var4 * 0.3F);
         case 3:
         default:
            break;
         case 4:
            var4 += Rand.Next(var4 * 0.2F, var4 * 0.4F);
            break;
         case 5:
            var4 += Rand.Next(var4 * 0.5F, var4 * 0.9F);
         }

         var4 = Math.max(0.0F, var4);
         var4 = Math.min(100.0F, var4);
         this.setCondition((int)var4);
         if (var1 != null) {
            var1.setCondition(this.getCondition());
         }

      }
   }

   public void setGeneralCondition(InventoryItem var1, float var2, float var3) {
      byte var4 = 100;
      int var7 = (int)((float)var4 * var2);
      float var5 = 100.0F;
      int var6;
      if (var1 != null) {
         var6 = var1.getChanceToSpawnDamaged();
         var6 = (int)((float)var6 + var3);
         if (var6 > 0 && Rand.Next(100) < var6) {
            var5 = (float)Rand.Next(var7 - var7 / 2, var7);
         }
      } else {
         byte var8 = 30;
         var6 = (int)((float)var8 + var3);
         if (Rand.Next(100) < var6) {
            var5 = Rand.Next((float)var7 * 0.5F, (float)var7);
         }
      }

      switch(SandboxOptions.instance.CarGeneralCondition.getValue()) {
      case 1:
         var5 -= Rand.Next(var5 * 0.3F, Rand.Next(var5 * 0.3F, var5 * 0.9F));
         break;
      case 2:
         var5 -= Rand.Next(var5 * 0.1F, var5 * 0.3F);
      case 3:
      default:
         break;
      case 4:
         var5 += Rand.Next(var5 * 0.2F, var5 * 0.4F);
         break;
      case 5:
         var5 += Rand.Next(var5 * 0.5F, var5 * 0.9F);
      }

      var5 = Math.max(0.0F, var5);
      var5 = Math.min(100.0F, var5);
      this.setCondition((int)var5);
      if (var1 != null) {
         var1.setCondition(this.getCondition());
      }

   }

   public static float getNumberByCondition(float var0, float var1, float var2) {
      var1 += 20.0F * (100.0F - var1) / 100.0F;
      float var3 = var1 / 100.0F;
      return (float)Math.round(Math.max(var2, var0 * var3) * 100.0F) / 100.0F;
   }

   public boolean isContainer() {
      if (this.scriptPart == null) {
         return false;
      } else {
         return this.scriptPart.container != null;
      }
   }

   public int getContainerCapacity() {
      return this.getContainerCapacity((IsoGameCharacter)null);
   }

   public int getContainerCapacity(IsoGameCharacter var1) {
      if (!this.isContainer()) {
         return 0;
      } else if (this.getItemContainer() != null) {
         return var1 == null ? this.getItemContainer().getCapacity() : this.getItemContainer().getEffectiveCapacity(var1);
      } else if (this.getInventoryItem() != null) {
         return this.scriptPart.container.conditionAffectsCapacity ? (int)getNumberByCondition((float)this.getInventoryItem().getMaxCapacity(), (float)this.getCondition(), 5.0F) : this.getInventoryItem().getMaxCapacity();
      } else {
         return this.scriptPart.container.capacity;
      }
   }

   public void setContainerCapacity(int var1) {
      if (this.isContainer()) {
         if (this.getItemContainer() != null) {
            this.getItemContainer().Capacity = var1;
         }

      }
   }

   public String getContainerContentType() {
      return !this.isContainer() ? null : this.scriptPart.container.contentType;
   }

   public float getContainerContentAmount() {
      if (!this.isContainer()) {
         return 0.0F;
      } else {
         if (this.hasModData()) {
            Object var1 = this.getModData().rawget("contentAmount");
            if (var1 instanceof Double) {
               return ((Double)var1).floatValue();
            }
         }

         return 0.0F;
      }
   }

   public void setContainerContentAmount(float var1) {
      this.setContainerContentAmount(var1, false, false);
   }

   public void setContainerContentAmount(float var1, boolean var2, boolean var3) {
      if (this.isContainer()) {
         int var4 = this.scriptPart.container.capacity;
         if (this.getInventoryItem() != null) {
            var4 = this.getInventoryItem().getMaxCapacity();
         }

         if (!var2) {
            var1 = Math.min(var1, (float)var4);
         }

         var1 = Math.max(var1, 0.0F);
         this.getModData().rawset("contentAmount", (double)var1);
         if (this.getInventoryItem() != null) {
            this.getInventoryItem().setItemCapacity(var1);
         }

         if (!var3) {
            this.getVehicle().updateTotalMass();
         }

      }
   }

   public int getContainerSeatNumber() {
      return !this.isContainer() ? -1 : this.scriptPart.container.seat;
   }

   public String getLuaFunction(String var1) {
      return this.scriptPart != null && this.scriptPart.luaFunctions != null ? (String)this.scriptPart.luaFunctions.get(var1) : null;
   }

   protected VehicleScript.Model getScriptModelById(String var1) {
      if (this.scriptPart != null && this.scriptPart.models != null) {
         for(int var2 = 0; var2 < this.scriptPart.models.size(); ++var2) {
            VehicleScript.Model var3 = (VehicleScript.Model)this.scriptPart.models.get(var2);
            if (var1.equals(var3.id)) {
               return var3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void setModelVisible(String var1, boolean var2) {
      VehicleScript.Model var3 = this.getScriptModelById(var1);
      if (var3 != null) {
         this.vehicle.setModelVisible(this, var3, var2);
      }
   }

   public VehiclePart getParent() {
      return this.parent;
   }

   public void addChild(VehiclePart var1) {
      if (this.children == null) {
         this.children = new ArrayList();
      }

      this.children.add(var1);
   }

   public int getChildCount() {
      return this.children == null ? 0 : this.children.size();
   }

   public VehiclePart getChild(int var1) {
      return this.children != null && var1 >= 0 && var1 < this.children.size() ? (VehiclePart)this.children.get(var1) : null;
   }

   public VehicleDoor getDoor() {
      return this.door;
   }

   public VehicleWindow getWindow() {
      return this.window;
   }

   public VehiclePart getChildWindow() {
      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         VehiclePart var2 = this.getChild(var1);
         if (var2.getWindow() != null) {
            return var2;
         }
      }

      return null;
   }

   public VehicleWindow findWindow() {
      VehiclePart var1 = this.getChildWindow();
      return var1 == null ? null : var1.getWindow();
   }

   public VehicleScript.Anim getAnimById(String var1) {
      if (this.scriptPart != null && this.scriptPart.anims != null) {
         for(int var2 = 0; var2 < this.scriptPart.anims.size(); ++var2) {
            VehicleScript.Anim var3 = (VehicleScript.Anim)this.scriptPart.anims.get(var2);
            if (var3.id.equals(var1)) {
               return var3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void save(ByteBuffer var1) throws IOException {
      GameWindow.WriteStringUTF(var1, this.getId());
      var1.put((byte)(this.bCreated ? 1 : 0));
      var1.putFloat(this.lastUpdated);
      if (this.getInventoryItem() == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.getInventoryItem().saveWithSize(var1, false);
      }

      if (this.getItemContainer() == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.getItemContainer().save(var1);
      }

      if (this.hasModData() && !this.getModData().isEmpty()) {
         var1.put((byte)1);
         this.getModData().save(var1);
      } else {
         var1.put((byte)0);
      }

      if (this.getDeviceData() == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.getDeviceData().save(var1, false);
      }

      if (this.light == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.light.save(var1);
      }

      if (this.door == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.door.save(var1);
      }

      if (this.window == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.window.save(var1);
      }

      var1.putInt(this.condition);
      var1.putFloat(this.wheelFriction);
      var1.putInt(this.mechanicSkillInstaller);
      var1.putFloat(this.suspensionCompression);
      var1.putFloat(this.suspensionDamping);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.partId = GameWindow.ReadStringUTF(var1);
      this.bCreated = var1.get() == 1;
      this.lastUpdated = var1.getFloat();
      if (var1.get() == 1) {
         InventoryItem var3 = InventoryItem.loadItem(var1, var2);
         this.item = var3;
      }

      if (var1.get() == 1) {
         if (this.container == null) {
            this.container = new ItemContainer();
            this.container.parent = this.getVehicle();
            this.container.vehiclePart = this;
         }

         this.container.getItems().clear();
         this.container.ID = 0;
         this.container.load(var1, var2);
      }

      if (var1.get() == 1) {
         this.getModData().load(var1, var2);
      }

      if (var1.get() == 1) {
         if (this.getDeviceData() == null) {
            this.createSignalDevice();
         }

         this.getDeviceData().load(var1, var2, false);
      }

      if (var1.get() == 1) {
         if (this.light == null) {
            this.light = new VehicleLight();
         }

         this.light.load(var1, var2);
      }

      if (var1.get() == 1) {
         if (this.door == null) {
            this.door = new VehicleDoor(this);
         }

         this.door.load(var1, var2);
      }

      if (var1.get() == 1) {
         if (this.window == null) {
            this.window = new VehicleWindow(this);
         }

         this.window.load(var1, var2);
      }

      if (var2 >= 116) {
         this.setCondition(var1.getInt());
      }

      if (var2 >= 118) {
         this.setWheelFriction(var1.getFloat());
         this.setMechanicSkillInstaller(var1.getInt());
      }

      if (var2 >= 119) {
         this.setSuspensionCompression(var1.getFloat());
         this.setSuspensionDamping(var1.getFloat());
      }

   }

   public int getWheelIndex() {
      if (this.scriptPart != null && this.scriptPart.wheel != null) {
         for(int var1 = 0; var1 < this.vehicle.script.getWheelCount(); ++var1) {
            VehicleScript.Wheel var2 = this.vehicle.script.getWheel(var1);
            if (this.scriptPart.wheel.equals(var2.id)) {
               return var1;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void createSpotLight(float var1, float var2, float var3, float var4, float var5, int var6) {
      this.light = this.light == null ? new VehicleLight() : this.light;
      this.light.offset.set(var1, var2, 0.0F);
      this.light.dist = var3;
      this.light.intensity = var4;
      this.light.dot = var5;
      this.light.focusing = var6;
   }

   public VehicleLight getLight() {
      return this.light;
   }

   public float getLightDistance() {
      return this.light == null ? 0.0F : 8.0F + 16.0F * (float)this.getCondition() / 100.0F;
   }

   public float getLightIntensity() {
      return this.light == null ? 0.0F : 0.5F + 0.25F * (float)this.getCondition() / 100.0F;
   }

   public float getLightFocusing() {
      return this.light == null ? 0.0F : (float)(10 + (int)(90.0F * (1.0F - (float)this.getCondition() / 100.0F)));
   }

   public void setLightActive(boolean var1) {
      if (this.light != null && this.light.active != var1) {
         this.light.active = var1;
         if (GameServer.bServer) {
            BaseVehicle var10000 = this.vehicle;
            var10000.updateFlags = (short)(var10000.updateFlags | 8);
         }

      }
   }

   public DeviceData createSignalDevice() {
      if (this.deviceData == null) {
         this.deviceData = new DeviceData(this);
      }

      if (this.chatElement == null) {
         this.chatElement = new ChatElement(this, 5, "device");
      }

      return this.deviceData;
   }

   public boolean hasDevicePower() {
      return this.vehicle.getBatteryCharge() > 0.0F;
   }

   public DeviceData getDeviceData() {
      return this.deviceData;
   }

   public void setDeviceData(DeviceData var1) {
      if (var1 == null) {
         var1 = new DeviceData(this);
      }

      this.deviceData = var1;
      this.deviceData.setParent(this);
   }

   public float getDelta() {
      return this.deviceData != null ? this.deviceData.getPower() : 0.0F;
   }

   public void setDelta(float var1) {
      if (this.deviceData != null) {
         this.deviceData.setPower(var1);
      }

   }

   public float getX() {
      return this.vehicle.getX();
   }

   public float getY() {
      return this.vehicle.getY();
   }

   public float getZ() {
      return this.vehicle.getZ();
   }

   public IsoGridSquare getSquare() {
      return this.vehicle.getSquare();
   }

   public void AddDeviceText(String var1, float var2, float var3, float var4, String var5, int var6) {
      if (this.deviceData != null && this.deviceData.getIsTurnedOn()) {
         this.deviceData.doReceiveSignal(var6);
         if (this.deviceData.getDeviceVolume() > 0.0F) {
            this.chatElement.addChatLine(var1, var2, var3, var4, UIFont.Medium, (float)this.deviceData.getDeviceVolumeRange(), "default", true, true, true, true, true, true);
            if (var5 != null) {
               LuaEventManager.triggerEvent("OnDeviceText", var5, this.getX(), this.getY(), this.getZ(), var1, this);
            }
         }
      }

   }

   public boolean HasPlayerInRange() {
      return this.hasPlayerInRange;
   }

   private boolean playerWithinBounds(IsoPlayer var1, float var2) {
      if (var1 != null && !var1.isDead()) {
         return (var1.getX() > this.getX() - var2 || this.getX() < this.getX() + var2) && (var1.getY() > this.getY() - var2 || this.getY() < this.getY() + var2);
      } else {
         return false;
      }
   }

   public void updateSignalDevice() {
      if (this.deviceData != null) {
         if (GameClient.bClient) {
            this.deviceData.updateSimple();
         } else {
            this.deviceData.update(true, this.hasPlayerInRange);
         }

         if (!GameServer.bServer) {
            this.hasPlayerInRange = false;
            if (this.deviceData.getIsTurnedOn()) {
               for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
                  IsoPlayer var2 = IsoPlayer.players[var1];
                  if (this.playerWithinBounds(var2, (float)this.deviceData.getDeviceVolumeRange() * 0.6F)) {
                     this.hasPlayerInRange = true;
                     break;
                  }
               }
            }

            this.chatElement.setHistoryRange((float)this.deviceData.getDeviceVolumeRange() * 0.6F);
            this.chatElement.update();
         } else {
            this.hasPlayerInRange = false;
         }

      }
   }

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String var1) {
      this.category = var1;
   }

   public int getCondition() {
      return this.condition;
   }

   public void setCondition(int var1) {
      var1 = Math.min(100, var1);
      var1 = Math.max(0, var1);
      if (this.getVehicle().getDriver() != null) {
         if (this.condition > 60 && var1 < 60 && var1 > 40) {
            LuaEventManager.triggerEvent("OnVehicleDamageTexture", this.getVehicle().getDriver());
         }

         if (this.condition > 40 && var1 < 40) {
            LuaEventManager.triggerEvent("OnVehicleDamageTexture", this.getVehicle().getDriver());
         }
      }

      this.condition = var1;
      if (this.getInventoryItem() != null) {
         this.getInventoryItem().setCondition(var1);
      }

      this.getVehicle().bDoDamageOverlay = true;
      if ("lightbar".equals(this.getId())) {
         this.getVehicle().lightbarLightsMode.set(0);
         this.getVehicle().setLightbarSirenMode(0);
      }

   }

   public void damage(int var1) {
      if (this.getWindow() != null) {
         this.getWindow().damage(var1);
      } else {
         this.setCondition(this.getCondition() - var1);
         this.getVehicle().transmitPartCondition(this);
      }
   }

   public boolean isSpecificItem() {
      return this.specificItem;
   }

   public void setSpecificItem(boolean var1) {
      this.specificItem = var1;
   }

   public float getWheelFriction() {
      return this.wheelFriction;
   }

   public void setWheelFriction(float var1) {
      this.wheelFriction = var1;
   }

   public int getMechanicSkillInstaller() {
      return this.mechanicSkillInstaller;
   }

   public void setMechanicSkillInstaller(int var1) {
      this.mechanicSkillInstaller = var1;
   }

   public float getSuspensionDamping() {
      return this.suspensionDamping;
   }

   public void setSuspensionDamping(float var1) {
      this.suspensionDamping = var1;
   }

   public float getSuspensionCompression() {
      return this.suspensionCompression;
   }

   public void setSuspensionCompression(float var1) {
      this.suspensionCompression = var1;
   }

   public float getEngineLoudness() {
      return this.engineLoudness;
   }

   public void setEngineLoudness(float var1) {
      this.engineLoudness = var1;
   }

   public void repair() {
      VehicleScript var1 = this.vehicle.getScript();
      float var2 = this.getContainerContentAmount();
      int var5;
      if (this.getItemType() != null && !this.getItemType().isEmpty() && this.getInventoryItem() == null) {
         String var3 = (String)this.getItemType().get(Rand.Next(this.getItemType().size()));
         if (var3 != null && !var3.isEmpty()) {
            InventoryItem var4 = InventoryItemFactory.CreateItem(var3);
            if (var4 != null) {
               this.setInventoryItem(var4);
               if (var4.getMaxCapacity() > 0) {
                  var4.setItemCapacity((float)var4.getMaxCapacity());
               }

               this.vehicle.transmitPartItem(this);
               var5 = this.getWheelIndex();
               if (var5 != -1) {
                  this.vehicle.setTireRemoved(var5, false);
                  this.setModelVisible("InflatedTirePlusWheel", true);
               }
            }
         }
      }

      if (this.getDoor() != null && this.getDoor().isLockBroken()) {
         this.getDoor().setLockBroken(false);
         this.vehicle.transmitPartDoor(this);
      }

      if (this.getCondition() != 100) {
         this.setCondition(100);
         if (this.getInventoryItem() != null) {
            this.doInventoryItemStats(this.getInventoryItem(), this.getMechanicSkillInstaller());
         }

         this.vehicle.transmitPartCondition(this);
      }

      if (this.isContainer() && this.getItemContainer() == null && var2 != (float)this.getContainerCapacity()) {
         this.setContainerContentAmount((float)this.getContainerCapacity());
         this.vehicle.transmitPartModData(this);
      }

      if (this.getInventoryItem() instanceof Drainable && ((Drainable)this.getInventoryItem()).getUsedDelta() < 1.0F) {
         ((Drainable)this.getInventoryItem()).setUsedDelta(1.0F);
         this.vehicle.transmitPartUsedDelta(this);
      }

      if ("Engine".equalsIgnoreCase(this.getId())) {
         byte var6 = 100;
         int var7 = (int)((double)var1.getEngineLoudness() * SandboxOptions.getInstance().ZombieAttractionMultiplier.getValue());
         var5 = (int)var1.getEngineForce();
         this.vehicle.setEngineFeature(var6, var7, var5);
         this.vehicle.transmitEngine();
      }

      this.vehicle.updatePartStats();
      this.vehicle.updateBulletStats();
   }
}
