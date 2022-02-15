package zombie.commands.serverCommands;

import zombie.Lua.LuaManager;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.Rand;
import zombie.core.logger.LoggerManager;
import zombie.core.math.PZMath;
import zombie.core.raknet.UdpConnection;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.util.StringUtils;

@CommandName(
   name = "createhorde2"
)
@CommandArgs(
   varArgs = true
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_CreateHorde2"
)
@RequiredRight(
   requiredRights = 44
)
public class CreateHorde2Command extends CommandBase {
   public CreateHorde2Command(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      int var1 = -1;
      int var2 = -1;
      int var3 = -1;
      int var4 = -1;
      int var5 = -1;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      float var10 = 1.0F;
      String var11 = null;

      for(int var12 = 0; var12 < this.getCommandArgsCount() - 1; var12 += 2) {
         String var13 = this.getCommandArg(var12);
         String var14 = this.getCommandArg(var12 + 1);
         byte var16 = -1;
         switch(var13.hashCode()) {
         case -1248644872:
            if (var13.equals("-isFallOnFront")) {
               var16 = 7;
            }
            break;
         case -782900888:
            if (var13.equals("-knockedDown")) {
               var16 = 9;
            }
            break;
         case 1515:
            if (var13.equals("-x")) {
               var16 = 2;
            }
            break;
         case 1516:
            if (var13.equals("-y")) {
               var16 = 3;
            }
            break;
         case 1517:
            if (var13.equals("-z")) {
               var16 = 4;
            }
            break;
         case 61697225:
            if (var13.equals("-health")) {
               var16 = 10;
            }
            break;
         case 277437552:
            if (var13.equals("-outfit")) {
               var16 = 5;
            }
            break;
         case 344381183:
            if (var13.equals("-radius")) {
               var16 = 1;
            }
            break;
         case 673556624:
            if (var13.equals("-isFakeDead")) {
               var16 = 8;
            }
            break;
         case 1383163138:
            if (var13.equals("-count")) {
               var16 = 0;
            }
            break;
         case 2142561863:
            if (var13.equals("-crawler")) {
               var16 = 6;
            }
         }

         switch(var16) {
         case 0:
            var1 = PZMath.tryParseInt(var14, -1);
            break;
         case 1:
            var2 = PZMath.tryParseInt(var14, -1);
            break;
         case 2:
            var3 = PZMath.tryParseInt(var14, -1);
            break;
         case 3:
            var4 = PZMath.tryParseInt(var14, -1);
            break;
         case 4:
            var5 = PZMath.tryParseInt(var14, -1);
            break;
         case 5:
            var11 = StringUtils.discardNullOrWhitespace(var14);
            break;
         case 6:
            var6 = !"false".equals(var14);
            break;
         case 7:
            var7 = !"false".equals(var14);
            break;
         case 8:
            var8 = !"false".equals(var14);
            break;
         case 9:
            var9 = !"false".equals(var14);
            break;
         case 10:
            var10 = Float.valueOf(var14);
            break;
         default:
            return this.getHelp();
         }
      }

      var1 = PZMath.clamp(var1, 1, 500);
      IsoGridSquare var17 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
      if (var17 == null) {
         return "invalid location";
      } else if (var11 != null && OutfitManager.instance.FindMaleOutfit(var11) == null && OutfitManager.instance.FindFemaleOutfit(var11) == null) {
         return "invalid outfit";
      } else {
         Integer var18 = null;
         if (var11 != null) {
            if (OutfitManager.instance.FindFemaleOutfit(var11) == null) {
               var18 = Integer.MIN_VALUE;
            } else if (OutfitManager.instance.FindMaleOutfit(var11) == null) {
               var18 = Integer.MAX_VALUE;
            }
         }

         for(int var19 = 0; var19 < var1; ++var19) {
            int var15 = var2 <= 0 ? var3 : Rand.Next(var3 - var2, var3 + var2 + 1);
            int var20 = var2 <= 0 ? var4 : Rand.Next(var4 - var2, var4 + var2 + 1);
            LuaManager.GlobalObject.addZombiesInOutfit(var15, var20, var5, 1, var11, var18, var6, var7, var8, var9, var10);
         }

         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " created a horde of " + var1 + " zombies near " + var3 + "," + var4, "IMPORTANT");
         return "Horde spawned.";
      }
   }
}
