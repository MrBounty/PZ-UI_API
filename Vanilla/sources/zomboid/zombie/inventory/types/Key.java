package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemType;
import zombie.scripting.objects.Item;

public final class Key extends InventoryItem {
   private int keyId = -1;
   private boolean padlock = false;
   private int numberOfKey = 0;
   private boolean digitalPadlock = false;
   public static final Key[] highlightDoor = new Key[4];

   public Key(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.Key;
   }

   public int getSaveType() {
      return Item.Type.Key.ordinal();
   }

   public void takeKeyId() {
      if (this.getContainer() != null && this.getContainer().getSourceGrid() != null && this.getContainer().getSourceGrid().getBuilding() != null && this.getContainer().getSourceGrid().getBuilding().def != null) {
         this.setKeyId(this.getContainer().getSourceGrid().getBuilding().def.getKeyId());
      }

   }

   public static void setHighlightDoors(int var0, InventoryItem var1) {
      if (var1 instanceof Key && !((Key)var1).isPadlock() && !((Key)var1).isDigitalPadlock()) {
         highlightDoor[var0] = (Key)var1;
      } else {
         highlightDoor[var0] = null;
      }

   }

   public int getKeyId() {
      return this.keyId;
   }

   public void setKeyId(int var1) {
      this.keyId = var1;
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Key";
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putInt(this.getKeyId());
      var1.put((byte)this.numberOfKey);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      this.setKeyId(var1.getInt());
      this.numberOfKey = var1.get();
   }

   public boolean isPadlock() {
      return this.padlock;
   }

   public void setPadlock(boolean var1) {
      this.padlock = var1;
   }

   public int getNumberOfKey() {
      return this.numberOfKey;
   }

   public void setNumberOfKey(int var1) {
      this.numberOfKey = var1;
   }

   public boolean isDigitalPadlock() {
      return this.digitalPadlock;
   }

   public void setDigitalPadlock(boolean var1) {
      this.digitalPadlock = var1;
   }
}
