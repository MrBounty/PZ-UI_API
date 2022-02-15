package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.core.Color;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemType;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteGrid;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;

public class Moveable extends InventoryItem {
   protected String worldSprite = "";
   private boolean isLight = false;
   private boolean lightUseBattery = false;
   private boolean lightHasBattery = false;
   private String lightBulbItem = "Base.LightBulb";
   private float lightPower = 0.0F;
   private float lightDelta = 2.5E-4F;
   private float lightR = 1.0F;
   private float lightG = 1.0F;
   private float lightB = 1.0F;
   private boolean isMultiGridAnchor = false;
   private IsoSpriteGrid spriteGrid;
   private String customNameFull = "Moveable Object";
   private String movableFullName = "Moveable Object";
   protected boolean canBeDroppedOnFloor = false;
   private boolean hasReadWorldSprite = false;
   protected String customItem = null;

   public Moveable(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.Moveable;
   }

   public Moveable(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.Moveable;
   }

   public String getName() {
      if ("Moveable Object".equals(this.movableFullName)) {
         return this.name;
      } else if (this.movableFullName.equals(this.name)) {
         return Translator.getMoveableDisplayName(this.customNameFull);
      } else {
         String var10000 = Translator.getMoveableDisplayName(this.movableFullName);
         return var10000 + this.customNameFull.substring(this.movableFullName.length());
      }
   }

   public String getDisplayName() {
      return this.getName();
   }

   public boolean CanBeDroppedOnFloor() {
      if (this.worldSprite != null && this.spriteGrid != null) {
         IsoSprite var1 = IsoSpriteManager.instance.getSprite(this.worldSprite);
         PropertyContainer var2 = var1.getProperties();
         return this.canBeDroppedOnFloor || !var2.Is("ForceSingleItem");
      } else {
         return this.canBeDroppedOnFloor;
      }
   }

   public String getMovableFullName() {
      return this.movableFullName;
   }

   public String getCustomNameFull() {
      return this.customNameFull;
   }

   public boolean isMultiGridAnchor() {
      return this.isMultiGridAnchor;
   }

   public IsoSpriteGrid getSpriteGrid() {
      return this.spriteGrid;
   }

   public String getWorldSprite() {
      return this.worldSprite;
   }

   public boolean ReadFromWorldSprite(String var1) {
      if (var1 == null) {
         return false;
      } else if (this.hasReadWorldSprite && this.worldSprite != null && this.worldSprite.equalsIgnoreCase(var1)) {
         return true;
      } else {
         this.customItem = null;

         try {
            IsoSprite var2 = IsoSpriteManager.instance.getSprite(var1);
            if (var2 != null) {
               PropertyContainer var3 = var2.getProperties();
               if (var3.Is("IsMoveAble")) {
                  if (var3.Is("CustomItem")) {
                     this.customItem = var3.Val("CustomItem");
                     Item var11 = ScriptManager.instance.FindItem(this.customItem);
                     if (var11 != null) {
                        this.Weight = this.ActualWeight = var11.ActualWeight;
                     }

                     this.worldSprite = var1;
                     if (var2.getSpriteGrid() != null) {
                        this.spriteGrid = var2.getSpriteGrid();
                        int var12 = var2.getSpriteGrid().getSpriteIndex(var2);
                        this.isMultiGridAnchor = var12 == 0;
                     }

                     return true;
                  }

                  this.isLight = var3.Is("lightR");
                  this.worldSprite = var1;
                  float var4 = 1.0F;
                  if (var3.Is("PickUpWeight")) {
                     var4 = Float.parseFloat(var3.Val("PickUpWeight")) / 10.0F;
                  }

                  this.Weight = var4;
                  this.ActualWeight = var4;
                  this.setCustomWeight(true);
                  String var5 = "Moveable Object";
                  if (var3.Is("CustomName")) {
                     if (var3.Is("GroupName")) {
                        String var10000 = var3.Val("GroupName");
                        var5 = var10000 + " " + var3.Val("CustomName");
                     } else {
                        var5 = var3.Val("CustomName");
                     }
                  }

                  this.movableFullName = var5;
                  this.name = var5;
                  this.customNameFull = var5;
                  if (var2.getSpriteGrid() != null) {
                     this.spriteGrid = var2.getSpriteGrid();
                     int var13 = var2.getSpriteGrid().getSpriteIndex(var2);
                     int var14 = var2.getSpriteGrid().getSpriteCount();
                     this.isMultiGridAnchor = var13 == 0;
                     if (!var3.Is("ForceSingleItem")) {
                        this.name = this.name + " (" + (var13 + 1) + "/" + var14 + ")";
                     } else {
                        this.name = this.name + " (1/1)";
                     }

                     this.customNameFull = this.name;
                     Texture var8 = null;
                     String var9 = "Item_Flatpack";
                     if (var9 != null) {
                        var8 = Texture.getSharedTexture(var9);
                        this.setColor(new Color(Rand.Next(0.7F, 1.0F), Rand.Next(0.7F, 1.0F), Rand.Next(0.7F, 1.0F)));
                     }

                     if (var8 == null) {
                        var8 = Texture.getSharedTexture("media/inventory/Question_On.png");
                     }

                     this.setTexture(var8);
                  } else if (this.texture == null || this.texture.getName() == null || this.texture.getName().equals("Item_Moveable_object") || this.texture.getName().equals("Question_On")) {
                     Texture var6 = null;
                     String var7 = null;
                     var7 = var1;
                     if (var1 != null) {
                        var6 = Texture.getSharedTexture(var1);
                        if (var6 != null) {
                           var6 = var6.splitIcon();
                        }
                     }

                     if (var6 == null) {
                        if (!var3.Is("MoveType")) {
                           var7 = "Item_Moveable_object";
                        } else if (var3.Val("MoveType").equals("WallObject")) {
                           var7 = "Item_Moveable_wallobject";
                        } else if (var3.Val("MoveType").equals("WindowObject")) {
                           var7 = "Item_Moveable_windowobject";
                        } else if (var3.Val("MoveType").equals("Window")) {
                           var7 = "Item_Moveable_window";
                        } else if (var3.Val("MoveType").equals("FloorTile")) {
                           var7 = "Item_Moveable_floortile";
                        } else if (var3.Val("MoveType").equals("FloorRug")) {
                           var7 = "Item_Moveable_floorrug";
                        } else if (var3.Val("MoveType").equals("Vegitation")) {
                           var7 = "Item_Moveable_vegitation";
                        }

                        if (var7 != null) {
                           var6 = Texture.getSharedTexture(var7);
                        }
                     }

                     if (var6 == null) {
                        var6 = Texture.getSharedTexture("media/inventory/Question_On.png");
                     }

                     this.setTexture(var6);
                  }

                  this.hasReadWorldSprite = true;
                  return true;
               }
            }
         } catch (Exception var10) {
            System.out.println("Error in Moveable item: " + var10.getMessage());
         }

         System.out.println("Warning: Moveable not valid");
         return false;
      }
   }

   public boolean isLight() {
      return this.isLight;
   }

   public void setLight(boolean var1) {
      this.isLight = var1;
   }

   public boolean isLightUseBattery() {
      return this.lightUseBattery;
   }

   public void setLightUseBattery(boolean var1) {
      this.lightUseBattery = var1;
   }

   public boolean isLightHasBattery() {
      return this.lightHasBattery;
   }

   public void setLightHasBattery(boolean var1) {
      this.lightHasBattery = var1;
   }

   public String getLightBulbItem() {
      return this.lightBulbItem;
   }

   public void setLightBulbItem(String var1) {
      this.lightBulbItem = var1;
   }

   public float getLightPower() {
      return this.lightPower;
   }

   public void setLightPower(float var1) {
      this.lightPower = var1;
   }

   public float getLightDelta() {
      return this.lightDelta;
   }

   public void setLightDelta(float var1) {
      this.lightDelta = var1;
   }

   public float getLightR() {
      return this.lightR;
   }

   public void setLightR(float var1) {
      this.lightR = var1;
   }

   public float getLightG() {
      return this.lightG;
   }

   public void setLightG(float var1) {
      this.lightG = var1;
   }

   public float getLightB() {
      return this.lightB;
   }

   public void setLightB(float var1) {
      this.lightB = var1;
   }

   public int getSaveType() {
      return Item.Type.Moveable.ordinal();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      GameWindow.WriteString(var1, this.worldSprite);
      var1.put((byte)(this.isLight ? 1 : 0));
      if (this.isLight) {
         var1.put((byte)(this.lightUseBattery ? 1 : 0));
         var1.put((byte)(this.lightHasBattery ? 1 : 0));
         var1.put((byte)(this.lightBulbItem != null ? 1 : 0));
         if (this.lightBulbItem != null) {
            GameWindow.WriteString(var1, this.lightBulbItem);
         }

         var1.putFloat(this.lightPower);
         var1.putFloat(this.lightDelta);
         var1.putFloat(this.lightR);
         var1.putFloat(this.lightG);
         var1.putFloat(this.lightB);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      this.worldSprite = GameWindow.ReadString(var1);
      if (!this.ReadFromWorldSprite(this.worldSprite) && this instanceof Radio) {
         String var10000 = this.fullType != null ? this.fullType : "unknown";
         DebugLog.log("Moveable.load -> Radio item = " + var10000);
      }

      if (this.customItem == null && !StringUtils.isNullOrWhitespace(this.worldSprite) && !this.type.equalsIgnoreCase(this.worldSprite)) {
         this.type = this.worldSprite;
         this.fullType = this.module + "." + this.worldSprite;
      }

      this.isLight = var1.get() == 1;
      if (this.isLight) {
         this.lightUseBattery = var1.get() == 1;
         this.lightHasBattery = var1.get() == 1;
         if (var1.get() == 1) {
            this.lightBulbItem = GameWindow.ReadString(var1);
         }

         this.lightPower = var1.getFloat();
         this.lightDelta = var1.getFloat();
         this.lightR = var1.getFloat();
         this.lightG = var1.getFloat();
         this.lightB = var1.getFloat();
      }

   }

   public void setWorldSprite(String var1) {
      this.worldSprite = var1;
   }
}
