package zombie.iso;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import zombie.GameTime;

public class IsoFloorBloodSplat {
   public static final float FADE_HOURS = 72.0F;
   public static HashMap SpriteMap = new HashMap();
   public static String[] FloorBloodTypes = new String[]{"blood_floor_small_01", "blood_floor_small_02", "blood_floor_small_03", "blood_floor_small_04", "blood_floor_small_05", "blood_floor_small_06", "blood_floor_small_07", "blood_floor_small_08", "blood_floor_med_01", "blood_floor_med_02", "blood_floor_med_03", "blood_floor_med_04", "blood_floor_med_05", "blood_floor_med_06", "blood_floor_med_07", "blood_floor_med_08", "blood_floor_large_01", "blood_floor_large_02", "blood_floor_large_03", "blood_floor_large_04", "blood_floor_large_05"};
   public float x;
   public float y;
   public float z;
   public int Type;
   public float worldAge;
   public int index;
   public int fade;
   IsoChunk chunk;

   public IsoFloorBloodSplat() {
   }

   public IsoFloorBloodSplat(float var1, float var2, float var3, int var4, float var5) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.Type = var4;
      this.worldAge = var5;
   }

   public void save(ByteBuffer var1) {
      int var2 = (int)(this.x / 10.0F * 255.0F);
      if (var2 < 0) {
         var2 = 0;
      }

      if (var2 > 255) {
         var2 = 255;
      }

      int var3 = (int)(this.y / 10.0F * 255.0F);
      if (var3 < 0) {
         var3 = 0;
      }

      if (var3 > 255) {
         var3 = 255;
      }

      int var4 = (int)(this.z / 8.0F * 255.0F);
      if (var4 < 0) {
         var4 = 0;
      }

      if (var4 > 255) {
         var4 = 255;
      }

      var1.put((byte)var2);
      var1.put((byte)var3);
      var1.put((byte)var4);
      var1.put((byte)this.Type);
      var1.putFloat(this.worldAge);
      var1.put((byte)this.index);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      if (var2 >= 65) {
         this.x = (float)(var1.get() & 255) / 255.0F * 10.0F;
         this.y = (float)(var1.get() & 255) / 255.0F * 10.0F;
         this.z = (float)(var1.get() & 255) / 255.0F * 8.0F;
         this.Type = var1.get();
         this.worldAge = var1.getFloat();
         if (var2 >= 73) {
            this.index = var1.get();
         }
      } else {
         this.x = var1.getFloat();
         this.y = var1.getFloat();
         this.z = var1.getFloat();
         this.Type = var1.getInt();
         this.worldAge = (float)GameTime.getInstance().getWorldAgeHours();
      }

   }
}
