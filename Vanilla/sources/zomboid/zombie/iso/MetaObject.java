package zombie.iso;

public final class MetaObject {
   int type;
   int x;
   int y;
   RoomDef def;
   boolean bUsed = false;

   public MetaObject(int var1, int var2, int var3, RoomDef var4) {
      this.type = var1;
      this.x = var2;
      this.y = var3;
      this.def = var4;
   }

   public RoomDef getRoom() {
      return this.def;
   }

   public boolean getUsed() {
      return this.bUsed;
   }

   public void setUsed(boolean var1) {
      this.bUsed = var1;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getType() {
      return this.type;
   }
}
