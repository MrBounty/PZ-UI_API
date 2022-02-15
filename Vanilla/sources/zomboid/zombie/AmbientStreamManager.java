package zombie;

import fmod.javafmod;
import fmod.fmod.FMODSoundEmitter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.Lua.LuaEventManager;
import zombie.audio.parameters.ParameterCameraZoom;
import zombie.audio.parameters.ParameterClosestWallDistance;
import zombie.audio.parameters.ParameterFogIntensity;
import zombie.audio.parameters.ParameterHardOfHearing;
import zombie.audio.parameters.ParameterInside;
import zombie.audio.parameters.ParameterMoodlePanic;
import zombie.audio.parameters.ParameterRainIntensity;
import zombie.audio.parameters.ParameterRoomSize;
import zombie.audio.parameters.ParameterSeason;
import zombie.audio.parameters.ParameterSnowIntensity;
import zombie.audio.parameters.ParameterStorm;
import zombie.audio.parameters.ParameterTemperature;
import zombie.audio.parameters.ParameterTimeOfDay;
import zombie.audio.parameters.ParameterWeatherEvent;
import zombie.audio.parameters.ParameterWindIntensity;
import zombie.audio.parameters.ParameterZone;
import zombie.audio.parameters.ParameterZoneWaterSide;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.input.Mouse;
import zombie.iso.Alarm;
import zombie.iso.IsoCamera;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.Vector2;
import zombie.iso.objects.RainManager;
import zombie.iso.weather.ClimateManager;
import zombie.network.GameClient;

public final class AmbientStreamManager extends BaseAmbientStreamManager {
   public static int OneInAmbienceChance = 2500;
   public static int MaxAmbientCount = 20;
   public static float MaxRange = 1000.0F;
   private final ArrayList alarmList = new ArrayList();
   public static BaseAmbientStreamManager instance;
   public final ArrayList ambient = new ArrayList();
   public final ArrayList worldEmitters = new ArrayList();
   public final ArrayDeque freeEmitters = new ArrayDeque();
   public final ArrayList allAmbient = new ArrayList();
   public final ArrayList nightAmbient = new ArrayList();
   public final ArrayList dayAmbient = new ArrayList();
   public final ArrayList rainAmbient = new ArrayList();
   public final ArrayList indoorAmbient = new ArrayList();
   public final ArrayList outdoorAmbient = new ArrayList();
   public final ArrayList windAmbient = new ArrayList();
   public boolean initialized = false;
   private final ParameterFogIntensity parameterFogIntensity = new ParameterFogIntensity();
   private final ParameterRainIntensity parameterRainIntensity = new ParameterRainIntensity();
   private final ParameterSeason parameterSeason = new ParameterSeason();
   private final ParameterSnowIntensity parameterSnowIntensity = new ParameterSnowIntensity();
   private final ParameterStorm parameterStorm = new ParameterStorm();
   private final ParameterTimeOfDay parameterTimeOfDay = new ParameterTimeOfDay();
   private final ParameterTemperature parameterTemperature = new ParameterTemperature();
   private final ParameterWeatherEvent parameterWeatherEvent = new ParameterWeatherEvent();
   private final ParameterWindIntensity parameterWindIntensity = new ParameterWindIntensity();
   private final ParameterZone parameterZoneDeepForest = new ParameterZone("ZoneDeepForest", "DeepForest");
   private final ParameterZone parameterZoneFarm = new ParameterZone("ZoneFarm", "Farm");
   private final ParameterZone parameterZoneForest = new ParameterZone("ZoneForest", "Forest");
   private final ParameterZone parameterZoneNav = new ParameterZone("ZoneNav", "Nav");
   private final ParameterZone parameterZoneTown = new ParameterZone("ZoneTown", "TownZone");
   private final ParameterZone parameterZoneTrailerPark = new ParameterZone("ZoneTrailerPark", "TrailerPark");
   private final ParameterZone parameterZoneVegetation = new ParameterZone("ZoneVegetation", "Vegitation");
   private final ParameterZoneWaterSide parameterZoneWaterSide = new ParameterZoneWaterSide();
   private final ParameterCameraZoom parameterCameraZoom = new ParameterCameraZoom();
   private final ParameterClosestWallDistance parameterClosestWallDistance = new ParameterClosestWallDistance();
   private final ParameterHardOfHearing parameterHardOfHearing = new ParameterHardOfHearing();
   private final ParameterInside parameterInside = new ParameterInside();
   private final ParameterMoodlePanic parameterMoodlePanic = new ParameterMoodlePanic();
   private final ParameterRoomSize parameterRoomSize = new ParameterRoomSize();
   private final Vector2 tempo = new Vector2();

   public static BaseAmbientStreamManager getInstance() {
      return instance;
   }

   public void update() {
      if (this.initialized) {
         if (!GameTime.isGamePaused()) {
            if (IsoPlayer.getInstance() != null) {
               if (IsoPlayer.getInstance().getCurrentSquare() != null) {
                  this.parameterFogIntensity.update();
                  this.parameterRainIntensity.update();
                  this.parameterSeason.update();
                  this.parameterSnowIntensity.update();
                  this.parameterStorm.update();
                  this.parameterTemperature.update();
                  this.parameterTimeOfDay.update();
                  this.parameterWeatherEvent.update();
                  this.parameterWindIntensity.update();
                  this.parameterZoneDeepForest.update();
                  this.parameterZoneFarm.update();
                  this.parameterZoneForest.update();
                  this.parameterZoneNav.update();
                  this.parameterZoneVegetation.update();
                  this.parameterZoneTown.update();
                  this.parameterZoneTrailerPark.update();
                  this.parameterZoneWaterSide.update();
                  this.parameterCameraZoom.update();
                  this.parameterClosestWallDistance.update();
                  this.parameterHardOfHearing.update();
                  this.parameterInside.update();
                  this.parameterMoodlePanic.update();
                  this.parameterRoomSize.update();
                  float var1 = GameTime.instance.getTimeOfDay();

                  for(int var2 = 0; var2 < this.worldEmitters.size(); ++var2) {
                     AmbientStreamManager.WorldSoundEmitter var3 = (AmbientStreamManager.WorldSoundEmitter)this.worldEmitters.get(var2);
                     IsoGridSquare var4;
                     if (var3.daytime != null) {
                        var4 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3.x, (double)var3.y, (double)var3.z);
                        if (var4 == null) {
                           var3.fmodEmitter.stopAll();
                           SoundManager.instance.unregisterEmitter(var3.fmodEmitter);
                           this.worldEmitters.remove(var3);
                           this.freeEmitters.add(var3);
                           --var2;
                        } else {
                           if (var1 > var3.dawn && var1 < var3.dusk) {
                              if (var3.fmodEmitter.isEmpty()) {
                                 var3.channel = var3.fmodEmitter.playAmbientLoopedImpl(var3.daytime);
                              }
                           } else if (!var3.fmodEmitter.isEmpty()) {
                              var3.fmodEmitter.stopSound(var3.channel);
                              var3.channel = 0L;
                           }

                           if (!var3.fmodEmitter.isEmpty() && (IsoWorld.instance.emitterUpdate || var3.fmodEmitter.hasSoundsToStart())) {
                              var3.fmodEmitter.tick();
                           }
                        }
                     } else if (IsoPlayer.getInstance() != null && IsoPlayer.getInstance().Traits.Deaf.isSet()) {
                        var3.fmodEmitter.stopAll();
                        SoundManager.instance.unregisterEmitter(var3.fmodEmitter);
                        this.worldEmitters.remove(var3);
                        this.freeEmitters.add(var3);
                        --var2;
                     } else {
                        var4 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3.x, (double)var3.y, (double)var3.z);
                        if (var4 != null && !var3.fmodEmitter.isEmpty()) {
                           var3.fmodEmitter.x = var3.x;
                           var3.fmodEmitter.y = var3.y;
                           var3.fmodEmitter.z = var3.z;
                           if (IsoWorld.instance.emitterUpdate || var3.fmodEmitter.hasSoundsToStart()) {
                              var3.fmodEmitter.tick();
                           }
                        } else {
                           var3.fmodEmitter.stopAll();
                           SoundManager.instance.unregisterEmitter(var3.fmodEmitter);
                           this.worldEmitters.remove(var3);
                           this.freeEmitters.add(var3);
                           --var2;
                        }
                     }
                  }

                  float var7 = GameTime.instance.getNight();
                  boolean var6 = IsoPlayer.getInstance().getCurrentSquare().getRoom() != null;
                  boolean var8 = RainManager.isRaining();

                  int var5;
                  for(var5 = 0; var5 < this.allAmbient.size(); ++var5) {
                     ((AmbientStreamManager.AmbientLoop)this.allAmbient.get(var5)).targVol = 1.0F;
                  }

                  AmbientStreamManager.AmbientLoop var10000;
                  for(var5 = 0; var5 < this.nightAmbient.size(); ++var5) {
                     var10000 = (AmbientStreamManager.AmbientLoop)this.nightAmbient.get(var5);
                     var10000.targVol *= var7;
                  }

                  for(var5 = 0; var5 < this.dayAmbient.size(); ++var5) {
                     var10000 = (AmbientStreamManager.AmbientLoop)this.dayAmbient.get(var5);
                     var10000.targVol *= 1.0F - var7;
                  }

                  for(var5 = 0; var5 < this.indoorAmbient.size(); ++var5) {
                     var10000 = (AmbientStreamManager.AmbientLoop)this.indoorAmbient.get(var5);
                     var10000.targVol *= var6 ? 0.8F : 0.0F;
                  }

                  for(var5 = 0; var5 < this.outdoorAmbient.size(); ++var5) {
                     var10000 = (AmbientStreamManager.AmbientLoop)this.outdoorAmbient.get(var5);
                     var10000.targVol *= var6 ? 0.15F : 0.8F;
                  }

                  for(var5 = 0; var5 < this.rainAmbient.size(); ++var5) {
                     var10000 = (AmbientStreamManager.AmbientLoop)this.rainAmbient.get(var5);
                     var10000.targVol *= var8 ? 1.0F : 0.0F;
                     if (((AmbientStreamManager.AmbientLoop)this.rainAmbient.get(var5)).channel != 0L) {
                        javafmod.FMOD_Studio_EventInstance_SetParameterByName(((AmbientStreamManager.AmbientLoop)this.rainAmbient.get(var5)).channel, "RainIntensity", ClimateManager.getInstance().getPrecipitationIntensity());
                     }
                  }

                  for(var5 = 0; var5 < this.allAmbient.size(); ++var5) {
                     ((AmbientStreamManager.AmbientLoop)this.allAmbient.get(var5)).update();
                  }

                  for(var5 = 0; var5 < this.alarmList.size(); ++var5) {
                     ((Alarm)this.alarmList.get(var5)).update();
                     if (((Alarm)this.alarmList.get(var5)).finished) {
                        this.alarmList.remove(var5);
                        --var5;
                     }
                  }

                  this.doOneShotAmbients();
               }
            }
         }
      }
   }

   public void doOneShotAmbients() {
      for(int var1 = 0; var1 < this.ambient.size(); ++var1) {
         AmbientStreamManager.Ambient var2 = (AmbientStreamManager.Ambient)this.ambient.get(var1);
         if (var2.finished()) {
            DebugLog.log(DebugType.Sound, "ambient: removing ambient sound " + var2.name);
            this.ambient.remove(var1--);
         } else {
            var2.update();
         }
      }

   }

   public void addRandomAmbient() {
      if (!Core.GameMode.equals("LastStand") && !Core.GameMode.equals("Tutorial")) {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            IsoPlayer var3 = IsoPlayer.players[var2];
            if (var3 != null && var3.isAlive()) {
               var1.add(var3);
            }
         }

         if (!var1.isEmpty()) {
            IsoPlayer var9 = (IsoPlayer)var1.get(Rand.Next(var1.size()));
            String var10 = "";
            if (GameTime.instance.getHour() > 7 && GameTime.instance.getHour() < 21) {
               switch(Rand.Next(3)) {
               case 0:
                  if (Rand.Next(10) < 2) {
                     var10 = "MetaDogBark";
                  }
                  break;
               case 1:
                  if (Rand.Next(10) < 3) {
                     var10 = "MetaScream";
                  }
               }
            } else {
               switch(Rand.Next(5)) {
               case 0:
                  if (Rand.Next(10) < 2) {
                     var10 = "MetaDogBark";
                  }
                  break;
               case 1:
                  if (Rand.Next(13) < 3) {
                     var10 = "MetaScream";
                  }
                  break;
               case 2:
                  var10 = "MetaOwl";
                  break;
               case 3:
                  var10 = "MetaWolfHowl";
               }
            }

            if (!var10.isEmpty()) {
               float var4 = var9.x;
               float var5 = var9.y;
               double var6 = (double)Rand.Next(-3.1415927F, 3.1415927F);
               this.tempo.x = (float)Math.cos(var6);
               this.tempo.y = (float)Math.sin(var6);
               this.tempo.setLength(1000.0F);
               var4 += this.tempo.x;
               var5 += this.tempo.y;
               if (!GameClient.bClient) {
                  System.out.println("playing ambient: " + var10 + " at dist: " + Math.abs(var4 - var9.x) + "," + Math.abs(var5 - var9.y));
                  AmbientStreamManager.Ambient var8 = new AmbientStreamManager.Ambient(var10, var4, var5, 50.0F, Rand.Next(0.2F, 0.5F));
                  this.ambient.add(var8);
               }

            }
         }
      }
   }

   public void addBlend(String var1, float var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      AmbientStreamManager.AmbientLoop var7 = new AmbientStreamManager.AmbientLoop(0.0F, var1, var2);
      this.allAmbient.add(var7);
      if (var3) {
         this.indoorAmbient.add(var7);
      } else {
         this.outdoorAmbient.add(var7);
      }

      if (var4) {
         this.rainAmbient.add(var7);
      }

      if (var5) {
         this.nightAmbient.add(var7);
      }

      if (var6) {
         this.dayAmbient.add(var7);
      }

   }

   public void init() {
      if (!this.initialized) {
         this.initialized = true;
      }
   }

   public void doGunEvent() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         IsoPlayer var3 = IsoPlayer.players[var2];
         if (var3 != null && var3.isAlive()) {
            var1.add(var3);
         }
      }

      if (!var1.isEmpty()) {
         IsoPlayer var11 = (IsoPlayer)var1.get(Rand.Next(var1.size()));
         String var12 = null;
         switch(Rand.Next(6)) {
         case 0:
            var12 = "MetaAssaultRifle1";
            break;
         case 1:
            var12 = "MetaPistol1";
            break;
         case 2:
            var12 = "MetaShotgun1";
            break;
         case 3:
            var12 = "MetaPistol2";
            break;
         case 4:
            var12 = "MetaPistol3";
            break;
         case 5:
            var12 = "MetaShotgun1";
         }

         float var4 = var11.x;
         float var5 = var11.y;
         short var6 = 600;
         double var7 = (double)Rand.Next(-3.1415927F, 3.1415927F);
         this.tempo.x = (float)Math.cos(var7);
         this.tempo.y = (float)Math.sin(var7);
         this.tempo.setLength((float)(var6 - 100));
         var4 += this.tempo.x;
         var5 += this.tempo.y;
         WorldSoundManager.instance.addSound((Object)null, (int)var4, (int)var5, 0, var6, var6);
         float var9 = 1.0F;
         AmbientStreamManager.Ambient var10 = new AmbientStreamManager.Ambient(var12, var4, var5, 700.0F, var9);
         this.ambient.add(var10);
      }
   }

   public void doAlarm(RoomDef var1) {
      if (var1 != null && var1.building != null && var1.building.bAlarmed) {
         var1.building.bAlarmed = false;
         var1.building.setAllExplored(true);
         this.alarmList.add(new Alarm(var1.x + var1.getW() / 2, var1.y + var1.getH() / 2));
      }

   }

   public void stop() {
      Iterator var1 = this.allAmbient.iterator();

      while(var1.hasNext()) {
         AmbientStreamManager.AmbientLoop var2 = (AmbientStreamManager.AmbientLoop)var1.next();
         var2.stop();
      }

      this.allAmbient.clear();
      this.ambient.clear();
      this.dayAmbient.clear();
      this.indoorAmbient.clear();
      this.nightAmbient.clear();
      this.outdoorAmbient.clear();
      this.rainAmbient.clear();
      this.windAmbient.clear();
      this.alarmList.clear();
      this.initialized = false;
   }

   public void addAmbient(String var1, int var2, int var3, int var4, float var5) {
      if (GameClient.bClient) {
         AmbientStreamManager.Ambient var6 = new AmbientStreamManager.Ambient(var1, (float)var2, (float)var3, (float)var4, var5, true);
         this.ambient.add(var6);
      }
   }

   public void addAmbientEmitter(float var1, float var2, int var3, String var4) {
      AmbientStreamManager.WorldSoundEmitter var5 = this.freeEmitters.isEmpty() ? new AmbientStreamManager.WorldSoundEmitter() : (AmbientStreamManager.WorldSoundEmitter)this.freeEmitters.pop();
      var5.x = var1;
      var5.y = var2;
      var5.z = (float)var3;
      var5.daytime = null;
      if (var5.fmodEmitter == null) {
         var5.fmodEmitter = new FMODSoundEmitter();
      }

      var5.fmodEmitter.x = var1;
      var5.fmodEmitter.y = var2;
      var5.fmodEmitter.z = (float)var3;
      var5.channel = var5.fmodEmitter.playAmbientLoopedImpl(var4);
      var5.fmodEmitter.randomStart();
      SoundManager.instance.registerEmitter(var5.fmodEmitter);
      this.worldEmitters.add(var5);
   }

   public void addDaytimeAmbientEmitter(float var1, float var2, int var3, String var4) {
      AmbientStreamManager.WorldSoundEmitter var5 = this.freeEmitters.isEmpty() ? new AmbientStreamManager.WorldSoundEmitter() : (AmbientStreamManager.WorldSoundEmitter)this.freeEmitters.pop();
      var5.x = var1;
      var5.y = var2;
      var5.z = (float)var3;
      if (var5.fmodEmitter == null) {
         var5.fmodEmitter = new FMODSoundEmitter();
      }

      var5.fmodEmitter.x = var1;
      var5.fmodEmitter.y = var2;
      var5.fmodEmitter.z = (float)var3;
      var5.daytime = var4;
      var5.dawn = Rand.Next(7.0F, 8.0F);
      var5.dusk = Rand.Next(19.0F, 20.0F);
      SoundManager.instance.registerEmitter(var5.fmodEmitter);
      this.worldEmitters.add(var5);
   }

   public static final class WorldSoundEmitter {
      public FMODSoundEmitter fmodEmitter;
      public float x;
      public float y;
      public float z;
      public long channel = -1L;
      public String daytime;
      public float dawn;
      public float dusk;
   }

   public static final class AmbientLoop {
      public static float volChangeAmount = 0.01F;
      public float targVol;
      public float currVol;
      public String name;
      public float volumedelta = 1.0F;
      public long channel = -1L;
      public final FMODSoundEmitter emitter = new FMODSoundEmitter();

      public AmbientLoop(float var1, String var2, float var3) {
         this.volumedelta = var3;
         this.channel = this.emitter.playAmbientLoopedImpl(var2);
         this.targVol = var1;
         this.currVol = 0.0F;
         this.update();
      }

      public void update() {
         if (this.targVol > this.currVol) {
            this.currVol += volChangeAmount;
            if (this.currVol > this.targVol) {
               this.currVol = this.targVol;
            }
         }

         if (this.targVol < this.currVol) {
            this.currVol -= volChangeAmount;
            if (this.currVol < this.targVol) {
               this.currVol = this.targVol;
            }
         }

         this.emitter.setVolumeAll(this.currVol * this.volumedelta);
         this.emitter.tick();
      }

      public void stop() {
         this.emitter.stopAll();
      }
   }

   public static final class Ambient {
      public float x;
      public float y;
      public String name;
      float radius;
      float volume;
      int worldSoundRadius;
      int worldSoundVolume;
      public boolean trackMouse;
      final FMODSoundEmitter emitter;

      public Ambient(String var1, float var2, float var3, float var4, float var5) {
         this(var1, var2, var3, var4, var5, false);
      }

      public Ambient(String var1, float var2, float var3, float var4, float var5, boolean var6) {
         this.trackMouse = false;
         this.emitter = new FMODSoundEmitter();
         this.name = var1;
         this.x = var2;
         this.y = var3;
         this.radius = var4;
         this.volume = var5;
         this.emitter.x = var2;
         this.emitter.y = var3;
         this.emitter.z = 0.0F;
         this.emitter.playAmbientSound(var1);
         this.update();
         LuaEventManager.triggerEvent("OnAmbientSound", var1, var2, var3);
      }

      public boolean finished() {
         return this.emitter.isEmpty();
      }

      public void update() {
         this.emitter.tick();
         if (this.trackMouse && IsoPlayer.getInstance() != null) {
            float var1 = (float)Mouse.getXA();
            float var2 = (float)Mouse.getYA();
            var1 -= (float)IsoCamera.getScreenLeft(IsoPlayer.getPlayerIndex());
            var2 -= (float)IsoCamera.getScreenTop(IsoPlayer.getPlayerIndex());
            var1 *= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
            var2 *= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
            int var3 = (int)IsoPlayer.getInstance().getZ();
            this.emitter.x = (float)((int)IsoUtils.XToIso(var1, var2, (float)var3));
            this.emitter.y = (float)((int)IsoUtils.YToIso(var1, var2, (float)var3));
         }

         if (!GameClient.bClient && this.worldSoundRadius > 0 && this.worldSoundVolume > 0) {
            WorldSoundManager.instance.addSound((Object)null, (int)this.x, (int)this.y, 0, this.worldSoundRadius, this.worldSoundVolume);
         }

      }

      public void repeatWorldSounds(int var1, int var2) {
         this.worldSoundRadius = var1;
         this.worldSoundVolume = var2;
      }

      private IsoGameCharacter getClosestListener(float var1, float var2) {
         IsoPlayer var3 = null;
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null && var6.getCurrentSquare() != null) {
               float var7 = var6.getX();
               float var8 = var6.getY();
               float var9 = IsoUtils.DistanceToSquared(var7, var8, var1, var2);
               if (var6.Traits.HardOfHearing.isSet()) {
                  var9 *= 4.5F;
               }

               if (var6.Traits.Deaf.isSet()) {
                  var9 = Float.MAX_VALUE;
               }

               if (var9 < var4) {
                  var3 = var6;
                  var4 = var9;
               }
            }
         }

         return var3;
      }
   }
}
