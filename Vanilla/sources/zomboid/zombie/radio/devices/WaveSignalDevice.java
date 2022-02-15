package zombie.radio.devices;

import zombie.iso.IsoGridSquare;

public interface WaveSignalDevice {
   DeviceData getDeviceData();

   void setDeviceData(DeviceData var1);

   float getDelta();

   void setDelta(float var1);

   IsoGridSquare getSquare();

   float getX();

   float getY();

   float getZ();

   void AddDeviceText(String var1, float var2, float var3, float var4, String var5, int var6);

   boolean HasPlayerInRange();
}
