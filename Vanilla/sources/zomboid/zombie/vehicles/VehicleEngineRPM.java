package zombie.vehicles;

import java.util.Iterator;
import zombie.core.math.PZMath;
import zombie.scripting.ScriptParser;
import zombie.scripting.objects.BaseScriptObject;

public class VehicleEngineRPM extends BaseScriptObject {
   public static final int MAX_GEARS = 8;
   private static final int VERSION1 = 1;
   private static final int VERSION = 1;
   private String m_name;
   public final EngineRPMData[] m_rpmData = new EngineRPMData[8];

   public String getName() {
      return this.m_name;
   }

   public void Load(String var1, String var2) throws RuntimeException {
      this.m_name = var1;
      int var3 = -1;
      ScriptParser.Block var4 = ScriptParser.parse(var2);
      var4 = (ScriptParser.Block)var4.children.get(0);
      Iterator var5 = var4.values.iterator();

      String var8;
      do {
         String var7;
         do {
            if (!var5.hasNext()) {
               if (var3 == -1) {
                  throw new RuntimeException(String.format("unknown vehicleEngineRPM VERSION \"%s\"", var4.type));
               }

               int var9 = 0;

               for(Iterator var10 = var4.children.iterator(); var10.hasNext(); ++var9) {
                  ScriptParser.Block var11 = (ScriptParser.Block)var10.next();
                  if (!"data".equals(var11.type)) {
                     throw new RuntimeException(String.format("unknown block vehicleEngineRPM.%s", var11.type));
                  }

                  if (var9 == 8) {
                     throw new RuntimeException(String.format("too many vehicleEngineRPM.data blocks, max is %d", 8));
                  }

                  this.m_rpmData[var9] = new EngineRPMData();
                  this.LoadData(var11, this.m_rpmData[var9]);
               }

               return;
            }

            ScriptParser.Value var6 = (ScriptParser.Value)var5.next();
            var7 = var6.getKey().trim();
            var8 = var6.getValue().trim();
         } while(!"VERSION".equals(var7));

         var3 = PZMath.tryParseInt(var8, -1);
      } while(var3 >= 0 && var3 <= 1);

      throw new RuntimeException(String.format("unknown vehicleEngineRPM VERSION \"%s\"", var8));
   }

   private void LoadData(ScriptParser.Block var1, EngineRPMData var2) {
      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("afterGearChange".equals(var5)) {
            var2.afterGearChange = PZMath.tryParseFloat(var6, 0.0F);
         } else {
            if (!"gearChange".equals(var5)) {
               throw new RuntimeException(String.format("unknown value vehicleEngineRPM.data.%s", var4.string));
            }

            var2.gearChange = PZMath.tryParseFloat(var6, 0.0F);
         }
      }

      var3 = var1.children.iterator();

      ScriptParser.Block var7;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var7 = (ScriptParser.Block)var3.next();
      } while("xxx".equals(var7.type));

      throw new RuntimeException(String.format("unknown block vehicleEngineRPM.data.%s", var7.type));
   }

   public void reset() {
      for(int var1 = 0; var1 < this.m_rpmData.length; ++var1) {
         if (this.m_rpmData[var1] != null) {
            this.m_rpmData[var1].reset();
         }
      }

   }
}
