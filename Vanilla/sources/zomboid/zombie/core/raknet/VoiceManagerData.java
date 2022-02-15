package zombie.core.raknet;

import java.util.ArrayList;

public class VoiceManagerData {
   public static ArrayList data = new ArrayList();
   public long userplaychannel = 0L;
   public long userplaysound = 0L;
   public boolean userplaymute = false;
   public long voicetimeout = 0L;
   short index;

   public VoiceManagerData(short var1) {
      this.index = var1;
   }

   public static VoiceManagerData get(short var0) {
      if (data.size() <= var0) {
         for(short var1 = (short)data.size(); var1 <= var0; ++var1) {
            VoiceManagerData var2 = new VoiceManagerData(var1);
            data.add(var2);
         }
      }

      VoiceManagerData var3 = (VoiceManagerData)data.get(var0);
      if (var3 == null) {
         var3 = new VoiceManagerData(var0);
         data.set(var0, var3);
      }

      return var3;
   }
}
