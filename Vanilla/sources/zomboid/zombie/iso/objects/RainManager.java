package zombie.iso.objects;

import fmod.fmod.Audio;
import java.util.ArrayList;
import java.util.Stack;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.weather.ClimateManager;
import zombie.network.GameServer;

public class RainManager {
   public static boolean IsRaining = false;
   public static int NumActiveRainSplashes = 0;
   public static int NumActiveRaindrops = 0;
   public static int MaxRainSplashObjects = 500;
   public static int MaxRaindropObjects = 500;
   public static float RainSplashAnimDelay = 0.2F;
   public static int AddNewSplashesDelay = 30;
   public static int AddNewSplashesTimer;
   public static float RaindropGravity;
   public static float GravModMin;
   public static float GravModMax;
   public static float RaindropStartDistance;
   public static IsoGridSquare[] PlayerLocation;
   public static IsoGridSquare[] PlayerOldLocation;
   public static boolean PlayerMoved;
   public static int RainRadius;
   public static Audio RainAmbient;
   public static Audio ThunderAmbient;
   public static ColorInfo RainSplashTintMod;
   public static ColorInfo RaindropTintMod;
   public static ColorInfo DarkRaindropTintMod;
   public static ArrayList RainSplashStack;
   public static ArrayList RaindropStack;
   public static Stack RainSplashReuseStack;
   public static Stack RaindropReuseStack;
   private static float RainChangeTimer;
   private static float RainChangeRate;
   private static float RainChangeRateMin;
   private static float RainChangeRateMax;
   public static float RainIntensity;
   public static float RainDesiredIntensity;
   private static int randRain;
   public static int randRainMin;
   public static int randRainMax;
   private static boolean stopRain;
   static Audio OutsideAmbient;
   static Audio OutsideNightAmbient;
   static ColorInfo AdjustedRainSplashTintMod;

   public static void reset() {
      RainSplashStack.clear();
      RaindropStack.clear();
      RaindropReuseStack.clear();
      RainSplashReuseStack.clear();
      NumActiveRainSplashes = 0;
      NumActiveRaindrops = 0;

      for(int var0 = 0; var0 < 4; ++var0) {
         PlayerLocation[var0] = null;
         PlayerOldLocation[var0] = null;
      }

      RainAmbient = null;
      ThunderAmbient = null;
      IsRaining = false;
      stopRain = false;
   }

   public static void AddRaindrop(IsoRaindrop var0) {
      if (NumActiveRaindrops < MaxRaindropObjects) {
         RaindropStack.add(var0);
         ++NumActiveRaindrops;
      } else {
         IsoRaindrop var1 = null;
         int var2 = -1;

         for(int var3 = 0; var3 < RaindropStack.size(); ++var3) {
            if (((IsoRaindrop)RaindropStack.get(var3)).Life > var2) {
               var2 = ((IsoRaindrop)RaindropStack.get(var3)).Life;
               var1 = (IsoRaindrop)RaindropStack.get(var3);
            }
         }

         if (var1 != null) {
            RemoveRaindrop(var1);
            RaindropStack.add(var0);
            ++NumActiveRaindrops;
         }
      }

   }

   public static void AddRainSplash(IsoRainSplash var0) {
      if (NumActiveRainSplashes < MaxRainSplashObjects) {
         RainSplashStack.add(var0);
         ++NumActiveRainSplashes;
      } else {
         IsoRainSplash var1 = null;
         int var2 = -1;

         for(int var3 = 0; var3 < RainSplashStack.size(); ++var3) {
            if (((IsoRainSplash)RainSplashStack.get(var3)).Age > var2) {
               var2 = ((IsoRainSplash)RainSplashStack.get(var3)).Age;
               var1 = (IsoRainSplash)RainSplashStack.get(var3);
            }
         }

         RemoveRainSplash(var1);
         RainSplashStack.add(var0);
         ++NumActiveRainSplashes;
      }

   }

   public static void AddSplashes() {
      if (AddNewSplashesTimer > 0) {
         --AddNewSplashesTimer;
      } else {
         AddNewSplashesTimer = (int)((float)AddNewSplashesDelay * ((float)PerformanceSettings.getLockFPS() / 30.0F));
         IsoGridSquare var0 = null;
         int var1;
         IsoRainSplash var2;
         IsoRaindrop var9;
         if (!stopRain) {
            if (PlayerMoved) {
               for(var1 = RainSplashStack.size() - 1; var1 >= 0; --var1) {
                  var2 = (IsoRainSplash)RainSplashStack.get(var1);
                  if (!inBounds(var2.square)) {
                     RemoveRainSplash(var2);
                  }
               }

               for(var1 = RaindropStack.size() - 1; var1 >= 0; --var1) {
                  var9 = (IsoRaindrop)RaindropStack.get(var1);
                  if (!inBounds(var9.square)) {
                     RemoveRaindrop(var9);
                  }
               }
            }

            var1 = 0;

            int var10;
            for(var10 = 0; var10 < IsoPlayer.numPlayers; ++var10) {
               if (IsoPlayer.players[var10] != null) {
                  ++var1;
               }
            }

            var10 = RainRadius * 2 * RainRadius * 2;
            int var3 = var10 / (randRain + 1);
            var3 = Math.min(MaxRainSplashObjects, var3);

            while(NumActiveRainSplashes > var3 * var1) {
               RemoveRainSplash((IsoRainSplash)RainSplashStack.get(0));
            }

            while(NumActiveRaindrops > var3 * var1) {
               RemoveRaindrop((IsoRaindrop)RaindropStack.get(0));
            }

            IsoCell var4 = IsoWorld.instance.CurrentCell;

            for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
               if (IsoPlayer.players[var5] != null && PlayerLocation[var5] != null) {
                  for(int var6 = 0; var6 < var3; ++var6) {
                     int var7 = Rand.Next(-RainRadius, RainRadius);
                     int var8 = Rand.Next(-RainRadius, RainRadius);
                     var0 = var4.getGridSquare(PlayerLocation[var5].getX() + var7, PlayerLocation[var5].getY() + var8, 0);
                     if (var0 != null && var0.isSeen(var5) && !var0.getProperties().Is(IsoFlagType.vegitation) && var0.getProperties().Is(IsoFlagType.exterior)) {
                        StartRainSplash(var4, var0, true);
                     }
                  }
               }
            }
         }

         PlayerMoved = false;
         if (!stopRain) {
            --randRain;
            if (randRain < randRainMin) {
               randRain = randRainMin;
            }
         } else {
            randRain = (int)((float)randRain - 1.0F * GameTime.instance.getMultiplier());
            if (randRain < randRainMin) {
               removeAll();
               randRain = randRainMin;
            } else {
               for(var1 = RainSplashStack.size() - 1; var1 >= 0; --var1) {
                  if (Rand.Next(randRain) == 0) {
                     var2 = (IsoRainSplash)RainSplashStack.get(var1);
                     RemoveRainSplash(var2);
                  }
               }

               for(var1 = RaindropStack.size() - 1; var1 >= 0; --var1) {
                  if (Rand.Next(randRain) == 0) {
                     var9 = (IsoRaindrop)RaindropStack.get(var1);
                     RemoveRaindrop(var9);
                  }
               }
            }
         }
      }

   }

   public static void RemoveRaindrop(IsoRaindrop var0) {
      if (var0.square != null) {
         var0.square.getProperties().UnSet(IsoFlagType.HasRaindrop);
         var0.square.setRainDrop((IsoRaindrop)null);
         var0.square = null;
      }

      RaindropStack.remove(var0);
      --NumActiveRaindrops;
      RaindropReuseStack.push(var0);
   }

   public static void RemoveRainSplash(IsoRainSplash var0) {
      if (var0.square != null) {
         var0.square.getProperties().UnSet(IsoFlagType.HasRainSplashes);
         var0.square.setRainSplash((IsoRainSplash)null);
         var0.square = null;
      }

      RainSplashStack.remove(var0);
      --NumActiveRainSplashes;
      RainSplashReuseStack.push(var0);
   }

   public static void SetPlayerLocation(int var0, IsoGridSquare var1) {
      PlayerOldLocation[var0] = PlayerLocation[var0];
      PlayerLocation[var0] = var1;
      if (PlayerOldLocation[var0] != PlayerLocation[var0]) {
         PlayerMoved = true;
      }

   }

   public static Boolean isRaining() {
      return ClimateManager.getInstance().isRaining();
   }

   public static void stopRaining() {
      stopRain = true;
      randRain = randRainMax;
      RainDesiredIntensity = 0.0F;
      if (GameServer.bServer) {
         GameServer.stopRain();
      }

      LuaEventManager.triggerEvent("OnRainStop");
   }

   public static void startRaining() {
   }

   public static void StartRaindrop(IsoCell var0, IsoGridSquare var1, boolean var2) {
      if (!var1.getProperties().Is(IsoFlagType.HasRaindrop)) {
         IsoRaindrop var3 = null;
         if (!RaindropReuseStack.isEmpty()) {
            if (var2) {
               if (var1.getRainDrop() != null) {
                  return;
               }

               var3 = (IsoRaindrop)RaindropReuseStack.pop();
               var3.Reset(var1, var2);
               var1.setRainDrop(var3);
            }
         } else if (var2) {
            if (var1.getRainDrop() != null) {
               return;
            }

            var3 = new IsoRaindrop(var0, var1, var2);
            var1.setRainDrop(var3);
         }
      }

   }

   public static void StartRainSplash(IsoCell var0, IsoGridSquare var1, boolean var2) {
   }

   public static void Update() {
      IsRaining = ClimateManager.getInstance().isRaining();
      RainIntensity = IsRaining ? ClimateManager.getInstance().getPrecipitationIntensity() : 0.0F;
      if (IsoPlayer.getInstance() != null) {
         if (IsoPlayer.getInstance().getCurrentSquare() != null) {
            if (!GameServer.bServer) {
               AddSplashes();
            }

         }
      }
   }

   public static void UpdateServer() {
   }

   public static void setRandRainMax(int var0) {
      randRainMax = var0;
      randRain = randRainMax;
   }

   public static void setRandRainMin(int var0) {
      randRainMin = var0;
   }

   public static boolean inBounds(IsoGridSquare var0) {
      if (var0 == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoPlayer var2 = IsoPlayer.players[var1];
            if (var2 != null && PlayerLocation[var1] != null) {
               if (var0.getX() < PlayerLocation[var1].getX() - RainRadius || var0.getX() >= PlayerLocation[var1].getX() + RainRadius) {
                  return true;
               }

               if (var0.getY() < PlayerLocation[var1].getY() - RainRadius || var0.getY() >= PlayerLocation[var1].getY() + RainRadius) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static void RemoveAllOn(IsoGridSquare var0) {
      if (var0.getRainDrop() != null) {
         RemoveRaindrop(var0.getRainDrop());
      }

      if (var0.getRainSplash() != null) {
         RemoveRainSplash(var0.getRainSplash());
      }

   }

   public static float getRainIntensity() {
      return ClimateManager.getInstance().getPrecipitationIntensity();
   }

   private static void removeAll() {
      int var0;
      for(var0 = RainSplashStack.size() - 1; var0 >= 0; --var0) {
         IsoRainSplash var1 = (IsoRainSplash)RainSplashStack.get(var0);
         RemoveRainSplash(var1);
      }

      for(var0 = RaindropStack.size() - 1; var0 >= 0; --var0) {
         IsoRaindrop var2 = (IsoRaindrop)RaindropStack.get(var0);
         RemoveRaindrop(var2);
      }

      RaindropStack.clear();
      RainSplashStack.clear();
      NumActiveRainSplashes = 0;
      NumActiveRaindrops = 0;
   }

   private static boolean interruptSleep(IsoPlayer var0) {
      if (var0.isAsleep() && var0.isOutside() && var0.getBed() != null && !var0.getBed().getName().equals("Tent")) {
         IsoObject var1 = var0.getBed();
         if (var1.getCell().getGridSquare((double)var1.getX(), (double)var1.getY(), (double)(var1.getZ() + 1.0F)) == null || var1.getCell().getGridSquare((double)var1.getX(), (double)var1.getY(), (double)(var1.getZ() + 1.0F)).getFloor() == null) {
            return true;
         }
      }

      return false;
   }

   static {
      AddNewSplashesTimer = AddNewSplashesDelay;
      RaindropGravity = 0.065F;
      GravModMin = 0.28F;
      GravModMax = 0.5F;
      RaindropStartDistance = 850.0F;
      PlayerLocation = new IsoGridSquare[4];
      PlayerOldLocation = new IsoGridSquare[4];
      PlayerMoved = true;
      RainRadius = 18;
      ThunderAmbient = null;
      RainSplashTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.3F);
      RaindropTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.3F);
      DarkRaindropTintMod = new ColorInfo(0.8F, 0.9F, 1.0F, 0.3F);
      RainSplashStack = new ArrayList(1600);
      RaindropStack = new ArrayList(1600);
      RainSplashReuseStack = new Stack();
      RaindropReuseStack = new Stack();
      RainChangeTimer = 1.0F;
      RainChangeRate = 0.01F;
      RainChangeRateMin = 0.006F;
      RainChangeRateMax = 0.01F;
      RainIntensity = 1.0F;
      RainDesiredIntensity = 1.0F;
      randRain = 0;
      randRainMin = 0;
      randRainMax = 0;
      stopRain = false;
      OutsideAmbient = null;
      OutsideNightAmbient = null;
      AdjustedRainSplashTintMod = new ColorInfo();
   }
}
