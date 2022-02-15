package zombie.core.znet;

import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.network.ServerOptions;

public class PortMapper {
   private static String externalAddress = null;

   public static void startup() {
   }

   public static void shutdown() {
      _cleanup();
   }

   public static boolean discover() {
      _discover();
      return _igd_found();
   }

   public static boolean igdFound() {
      return _igd_found();
   }

   public static boolean addMapping(int var0, int var1, String var2, String var3, int var4) {
      return addMapping(var0, var1, var2, var3, var4, false);
   }

   public static boolean addMapping(int var0, int var1, String var2, String var3, int var4, boolean var5) {
      boolean var6 = _add_mapping(var0, var1, var2, var3, var4, var5);
      if (!var6 && var4 != 0 && ServerOptions.instance.UPnPZeroLeaseTimeFallback.getValue()) {
         DebugLog.log(DebugType.Network, "Failed to add port mapping, retrying with zero lease time");
         var6 = _add_mapping(var0, var1, var2, var3, 0, var5);
      }

      return var6;
   }

   public static boolean removeMapping(int var0, String var1) {
      return _remove_mapping(var0, var1);
   }

   public static void fetchMappings() {
      _fetch_mappings();
   }

   public static int numMappings() {
      return _num_mappings();
   }

   public static PortMappingEntry getMapping(int var0) {
      return _get_mapping(var0);
   }

   public static String getGatewayInfo() {
      return _get_gateway_info();
   }

   public static synchronized String getExternalAddress(boolean var0) {
      if (var0 || externalAddress == null) {
         externalAddress = _get_external_address();
      }

      return externalAddress;
   }

   public static String getExternalAddress() {
      return getExternalAddress(false);
   }

   private static native void _discover();

   private static native void _cleanup();

   private static native boolean _igd_found();

   private static native boolean _add_mapping(int var0, int var1, String var2, String var3, int var4, boolean var5);

   private static native boolean _remove_mapping(int var0, String var1);

   private static native void _fetch_mappings();

   private static native int _num_mappings();

   private static native PortMappingEntry _get_mapping(int var0);

   private static native String _get_gateway_info();

   private static native String _get_external_address();
}
