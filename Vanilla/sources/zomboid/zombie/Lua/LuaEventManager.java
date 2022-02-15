package zombie.Lua;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;
import se.krka.kahlua.vm.Prototype;
import zombie.debug.DebugLog;

public final class LuaEventManager implements JavaFunction {
   public static final ArrayList OnTickCallbacks = new ArrayList();
   static Object[][] a1 = new Object[1][1];
   static Object[][] a2 = new Object[1][2];
   static Object[][] a3 = new Object[1][3];
   static Object[][] a4 = new Object[1][4];
   static Object[][] a5 = new Object[1][5];
   static Object[][] a6 = new Object[1][6];
   static int a1index = 0;
   static int a2index = 0;
   static int a3index = 0;
   static int a4index = 0;
   static int a5index = 0;
   static int a6index = 0;
   private static final ArrayList EventList = new ArrayList();
   private static final HashMap EventMap = new HashMap();

   private static Event checkEvent(String var0) {
      Event var1 = (Event)EventMap.get(var0);
      if (var1 == null) {
         DebugLog.log("LuaEventManager: adding unknown event \"" + var0 + "\"");
         var1 = AddEvent(var0);
      }

      return var1.callbacks.isEmpty() ? null : var1;
   }

   public static void triggerEvent(String var0) {
      synchronized(EventMap) {
         Event var2 = checkEvent(var0);
         if (var2 != null) {
            var2.trigger(LuaManager.env, LuaManager.caller, (Object[])null);
         }
      }
   }

   public static void triggerEvent(String var0, Object var1) {
      synchronized(EventMap) {
         Event var3 = checkEvent(var0);
         if (var3 != null) {
            if (a1index == a1.length) {
               a1 = (Object[][])Arrays.copyOf(a1, a1.length * 2);

               for(int var4 = a1index; var4 < a1.length; ++var4) {
                  a1[var4] = new Object[1];
               }
            }

            Object[] var11 = a1[a1index];
            var11[0] = var1;
            ++a1index;

            try {
               var3.trigger(LuaManager.env, LuaManager.caller, var11);
            } finally {
               --a1index;
               var11[0] = null;
            }

         }
      }
   }

   public static void triggerEventGarbage(String var0, Object var1) {
      triggerEvent(var0, var1);
   }

   public static void triggerEventUnique(String var0, Object var1) {
      triggerEvent(var0, var1);
   }

   public static void triggerEvent(String var0, Object var1, Object var2) {
      synchronized(EventMap) {
         Event var4 = checkEvent(var0);
         if (var4 != null) {
            if (a2index == a2.length) {
               a2 = (Object[][])Arrays.copyOf(a2, a2.length * 2);

               for(int var5 = a2index; var5 < a2.length; ++var5) {
                  a2[var5] = new Object[2];
               }
            }

            Object[] var12 = a2[a2index];
            var12[0] = var1;
            var12[1] = var2;
            ++a2index;

            try {
               var4.trigger(LuaManager.env, LuaManager.caller, var12);
            } finally {
               --a2index;
               var12[0] = null;
               var12[1] = null;
            }

         }
      }
   }

   public static void triggerEventGarbage(String var0, Object var1, Object var2) {
      triggerEvent(var0, var1, var2);
   }

   public static void triggerEvent(String var0, Object var1, Object var2, Object var3) {
      synchronized(EventMap) {
         Event var5 = checkEvent(var0);
         if (var5 != null) {
            if (a3index == a3.length) {
               a3 = (Object[][])Arrays.copyOf(a3, a3.length * 2);

               for(int var6 = a3index; var6 < a3.length; ++var6) {
                  a3[var6] = new Object[3];
               }
            }

            Object[] var13 = a3[a3index];
            var13[0] = var1;
            var13[1] = var2;
            var13[2] = var3;
            ++a3index;

            try {
               var5.trigger(LuaManager.env, LuaManager.caller, var13);
            } finally {
               --a3index;
               var13[0] = null;
               var13[1] = null;
               var13[2] = null;
            }

         }
      }
   }

   public static void triggerEventGarbage(String var0, Object var1, Object var2, Object var3) {
      triggerEvent(var0, var1, var2, var3);
   }

   public static void triggerEvent(String var0, Object var1, Object var2, Object var3, Object var4) {
      synchronized(EventMap) {
         Event var6 = checkEvent(var0);
         if (var6 != null) {
            if (a4index == a4.length) {
               a4 = (Object[][])Arrays.copyOf(a4, a4.length * 2);

               for(int var7 = a4index; var7 < a4.length; ++var7) {
                  a4[var7] = new Object[4];
               }
            }

            Object[] var14 = a4[a4index];
            var14[0] = var1;
            var14[1] = var2;
            var14[2] = var3;
            var14[3] = var4;
            ++a4index;

            try {
               var6.trigger(LuaManager.env, LuaManager.caller, var14);
            } finally {
               --a4index;
               var14[0] = null;
               var14[1] = null;
               var14[2] = null;
               var14[3] = null;
            }

         }
      }
   }

   public static void triggerEventGarbage(String var0, Object var1, Object var2, Object var3, Object var4) {
      triggerEvent(var0, var1, var2, var3, var4);
   }

   public static void triggerEvent(String var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      synchronized(EventMap) {
         Event var7 = checkEvent(var0);
         if (var7 != null) {
            if (a5index == a5.length) {
               a5 = (Object[][])Arrays.copyOf(a5, a5.length * 2);

               for(int var8 = a5index; var8 < a5.length; ++var8) {
                  a5[var8] = new Object[5];
               }
            }

            Object[] var15 = a5[a5index];
            var15[0] = var1;
            var15[1] = var2;
            var15[2] = var3;
            var15[3] = var4;
            var15[4] = var5;
            ++a5index;

            try {
               var7.trigger(LuaManager.env, LuaManager.caller, var15);
            } finally {
               --a5index;
               var15[0] = null;
               var15[1] = null;
               var15[2] = null;
               var15[3] = null;
               var15[4] = null;
            }

         }
      }
   }

   public static void triggerEvent(String var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      synchronized(EventMap) {
         Event var8 = checkEvent(var0);
         if (var8 != null) {
            if (a6index == a6.length) {
               a6 = (Object[][])Arrays.copyOf(a6, a6.length * 2);

               for(int var9 = a6index; var9 < a6.length; ++var9) {
                  a6[var9] = new Object[6];
               }
            }

            Object[] var16 = a6[a6index];
            var16[0] = var1;
            var16[1] = var2;
            var16[2] = var3;
            var16[3] = var4;
            var16[4] = var5;
            var16[5] = var6;
            ++a6index;

            try {
               var8.trigger(LuaManager.env, LuaManager.caller, var16);
            } finally {
               --a6index;
               var16[0] = null;
               var16[1] = null;
               var16[2] = null;
               var16[3] = null;
               var16[4] = null;
               var16[5] = null;
            }

         }
      }
   }

   public static Event AddEvent(String var0) {
      Event var1 = (Event)EventMap.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         var1 = new Event(var0, EventList.size());
         EventList.add(var1);
         EventMap.put(var0, var1);
         Object var2 = LuaManager.env.rawget("Events");
         if (var2 instanceof KahluaTable) {
            KahluaTable var3 = (KahluaTable)var2;
            var1.register(LuaManager.platform, var3);
         } else {
            DebugLog.log("ERROR: 'Events' table not found or not a table");
         }

         return var1;
      }
   }

   private static void AddEvents() {
      AddEvent("OnGameBoot");
      AddEvent("OnPreGameStart");
      AddEvent("OnTick");
      AddEvent("OnTickEvenPaused");
      AddEvent("OnRenderUpdate");
      AddEvent("OnFETick");
      AddEvent("OnGameStart");
      AddEvent("OnPreUIDraw");
      AddEvent("OnPostUIDraw");
      AddEvent("OnCharacterCollide");
      AddEvent("OnKeyStartPressed");
      AddEvent("OnKeyPressed");
      AddEvent("OnObjectCollide");
      AddEvent("OnNPCSurvivorUpdate");
      AddEvent("OnPlayerUpdate");
      AddEvent("OnZombieUpdate");
      AddEvent("OnTriggerNPCEvent");
      AddEvent("OnMultiTriggerNPCEvent");
      AddEvent("OnLoadMapZones");
      AddEvent("OnAddBuilding");
      AddEvent("OnCreateLivingCharacter");
      AddEvent("OnChallengeQuery");
      AddEvent("OnFillInventoryObjectContextMenu");
      AddEvent("OnPreFillInventoryObjectContextMenu");
      AddEvent("OnFillWorldObjectContextMenu");
      AddEvent("OnPreFillWorldObjectContextMenu");
      AddEvent("OnRefreshInventoryWindowContainers");
      AddEvent("OnGamepadConnect");
      AddEvent("OnGamepadDisconnect");
      AddEvent("OnJoypadActivate");
      AddEvent("OnJoypadActivateUI");
      AddEvent("OnJoypadBeforeDeactivate");
      AddEvent("OnJoypadDeactivate");
      AddEvent("OnJoypadBeforeReactivate");
      AddEvent("OnJoypadReactivate");
      AddEvent("OnJoypadRenderUI");
      AddEvent("OnMakeItem");
      AddEvent("OnWeaponHitCharacter");
      AddEvent("OnWeaponSwing");
      AddEvent("OnWeaponHitTree");
      AddEvent("OnWeaponHitXp");
      AddEvent("OnWeaponSwingHitPoint");
      AddEvent("OnPlayerAttackFinished");
      AddEvent("OnLoginState");
      AddEvent("OnLoginStateSuccess");
      AddEvent("OnCharacterCreateStats");
      AddEvent("OnLoadSoundBanks");
      AddEvent("OnObjectLeftMouseButtonDown");
      AddEvent("OnObjectLeftMouseButtonUp");
      AddEvent("OnObjectRightMouseButtonDown");
      AddEvent("OnObjectRightMouseButtonUp");
      AddEvent("OnDoTileBuilding");
      AddEvent("OnDoTileBuilding2");
      AddEvent("OnDoTileBuilding3");
      AddEvent("OnConnectFailed");
      AddEvent("OnConnected");
      AddEvent("OnDisconnect");
      AddEvent("OnConnectionStateChanged");
      AddEvent("OnScoreboardUpdate");
      AddEvent("OnMouseMove");
      AddEvent("OnMouseDown");
      AddEvent("OnMouseUp");
      AddEvent("OnRightMouseDown");
      AddEvent("OnRightMouseUp");
      AddEvent("OnNewSurvivorGroup");
      AddEvent("OnPlayerSetSafehouse");
      AddEvent("OnLoad");
      AddEvent("AddXP");
      AddEvent("LevelPerk");
      AddEvent("OnSave");
      AddEvent("OnMainMenuEnter");
      AddEvent("OnPreMapLoad");
      AddEvent("OnPostFloorSquareDraw");
      AddEvent("OnPostFloorLayerDraw");
      AddEvent("OnPostTilesSquareDraw");
      AddEvent("OnPostTileDraw");
      AddEvent("OnPostWallSquareDraw");
      AddEvent("OnPostCharactersSquareDraw");
      AddEvent("OnCreateUI");
      AddEvent("OnMapLoadCreateIsoObject");
      AddEvent("OnCreateSurvivor");
      AddEvent("OnCreatePlayer");
      AddEvent("OnPlayerDeath");
      AddEvent("OnZombieDead");
      AddEvent("OnCharacterDeath");
      AddEvent("OnCharacterMeet");
      AddEvent("OnSpawnRegionsLoaded");
      AddEvent("OnPostMapLoad");
      AddEvent("OnAIStateExecute");
      AddEvent("OnAIStateEnter");
      AddEvent("OnAIStateExit");
      AddEvent("OnAIStateChange");
      AddEvent("OnPlayerMove");
      AddEvent("OnInitWorld");
      AddEvent("OnNewGame");
      AddEvent("OnIsoThumpableLoad");
      AddEvent("OnIsoThumpableSave");
      AddEvent("ReuseGridsquare");
      AddEvent("LoadGridsquare");
      AddEvent("EveryOneMinute");
      AddEvent("EveryTenMinutes");
      AddEvent("EveryDays");
      AddEvent("EveryHours");
      AddEvent("OnDusk");
      AddEvent("OnDawn");
      AddEvent("OnEquipPrimary");
      AddEvent("OnEquipSecondary");
      AddEvent("OnClothingUpdated");
      AddEvent("OnWeatherPeriodStart");
      AddEvent("OnWeatherPeriodStage");
      AddEvent("OnWeatherPeriodComplete");
      AddEvent("OnWeatherPeriodStop");
      AddEvent("OnRainStart");
      AddEvent("OnRainStop");
      AddEvent("OnAmbientSound");
      AddEvent("OnWorldSound");
      AddEvent("OnResetLua");
      AddEvent("OnModsModified");
      AddEvent("OnSeeNewRoom");
      AddEvent("OnNewFire");
      AddEvent("OnFillContainer");
      AddEvent("OnChangeWeather");
      AddEvent("OnRenderTick");
      AddEvent("OnDestroyIsoThumpable");
      AddEvent("OnPostSave");
      AddEvent("OnResolutionChange");
      AddEvent("OnWaterAmountChange");
      AddEvent("OnClientCommand");
      AddEvent("OnServerCommand");
      AddEvent("OnContainerUpdate");
      AddEvent("OnObjectAdded");
      AddEvent("OnObjectAboutToBeRemoved");
      AddEvent("onLoadModDataFromServer");
      AddEvent("OnGameTimeLoaded");
      AddEvent("OnCGlobalObjectSystemInit");
      AddEvent("OnSGlobalObjectSystemInit");
      AddEvent("OnWorldMessage");
      AddEvent("OnKeyKeepPressed");
      AddEvent("SendCustomModData");
      AddEvent("ServerPinged");
      AddEvent("OnServerStarted");
      AddEvent("OnLoadedTileDefinitions");
      AddEvent("OnPostRender");
      AddEvent("DoSpecialTooltip");
      AddEvent("OnCoopJoinFailed");
      AddEvent("OnServerWorkshopItems");
      AddEvent("OnVehicleDamageTexture");
      AddEvent("OnCustomUIKey");
      AddEvent("OnCustomUIKeyPressed");
      AddEvent("OnCustomUIKeyReleased");
      AddEvent("OnDeviceText");
      AddEvent("OnRadioInteraction");
      AddEvent("OnLoadRadioScripts");
      AddEvent("OnAcceptInvite");
      AddEvent("OnCoopServerMessage");
      AddEvent("OnReceiveUserlog");
      AddEvent("OnAdminMessage");
      AddEvent("OnGetDBSchema");
      AddEvent("OnGetTableResult");
      AddEvent("ReceiveFactionInvite");
      AddEvent("AcceptedFactionInvite");
      AddEvent("ReceiveSafehouseInvite");
      AddEvent("AcceptedSafehouseInvite");
      AddEvent("ViewTickets");
      AddEvent("SyncFaction");
      AddEvent("OnReceiveItemListNet");
      AddEvent("OnMiniScoreboardUpdate");
      AddEvent("OnSafehousesChanged");
      AddEvent("RequestTrade");
      AddEvent("AcceptedTrade");
      AddEvent("TradingUIAddItem");
      AddEvent("TradingUIRemoveItem");
      AddEvent("TradingUIUpdateState");
      AddEvent("OnGridBurnt");
      AddEvent("OnPreDistributionMerge");
      AddEvent("OnDistributionMerge");
      AddEvent("OnPostDistributionMerge");
      AddEvent("MngInvReceiveItems");
      AddEvent("OnTileRemoved");
      AddEvent("OnServerStartSaving");
      AddEvent("OnServerFinishSaving");
      AddEvent("OnMechanicActionDone");
      AddEvent("OnClimateTick");
      AddEvent("OnThunderEvent");
      AddEvent("OnEnterVehicle");
      AddEvent("OnSteamGameJoin");
      AddEvent("OnTabAdded");
      AddEvent("OnSetDefaultTab");
      AddEvent("OnTabRemoved");
      AddEvent("OnAddMessage");
      AddEvent("SwitchChatStream");
      AddEvent("OnChatWindowInit");
      AddEvent("OnInitSeasons");
      AddEvent("OnClimateTickDebug");
      AddEvent("OnInitModdedWeatherStage");
      AddEvent("OnUpdateModdedWeatherStage");
      AddEvent("OnClimateManagerInit");
      AddEvent("OnPressReloadButton");
      AddEvent("OnPressRackButton");
      AddEvent("OnHitZombie");
      AddEvent("OnBeingHitByZombie");
      AddEvent("OnServerStatisticReceived");
      AddEvent("OnDynamicMovableRecipe");
      AddEvent("OnInitGlobalModData");
      AddEvent("OnReceiveGlobalModData");
      AddEvent("OnInitRecordedMedia");
      AddEvent("onUpdateIcon");
      AddEvent("preAddForageDefs");
      AddEvent("preAddZoneDefs");
      AddEvent("preAddCatDefs");
      AddEvent("preAddItemDefs");
      AddEvent("onAddForageDefs");
      AddEvent("onFillSearchIconContextMenu");
      AddEvent("onItemFall");
   }

   public static void clear() {
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();
      var1.rawset("Events", var2);
      AddEvents();
   }

   public static void reroute(Prototype var0, LuaClosure var1) {
      for(int var2 = 0; var2 < EventList.size(); ++var2) {
         Event var3 = (Event)EventList.get(var2);

         for(int var4 = 0; var4 < var3.callbacks.size(); ++var4) {
            LuaClosure var5 = (LuaClosure)var3.callbacks.get(var4);
            if (var5.prototype.filename.equals(var0.filename) && var5.prototype.name.equals(var0.name)) {
               var3.callbacks.set(var4, var1);
            }
         }
      }

   }

   public static void Reset() {
      for(int var0 = 0; var0 < EventList.size(); ++var0) {
         Event var1 = (Event)EventList.get(var0);
         var1.callbacks.clear();
      }

      EventList.clear();
      EventMap.clear();
   }

   public static void ResetCallbacks() {
      for(int var0 = 0; var0 < EventList.size(); ++var0) {
         Event var1 = (Event)EventList.get(var0);
         var1.callbacks.clear();
      }

   }

   public int call(LuaCallFrame var1, int var2) {
      return 0;
   }

   private int OnTick(LuaCallFrame var1, int var2) {
      return 0;
   }
}
