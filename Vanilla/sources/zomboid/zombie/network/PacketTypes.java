package zombie.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;

public class PacketTypes {
   public static final short SteamGeneric_ProfileName = 0;
   public static final short ContainerDeadBody = 0;
   public static final short ContainerWorldObject = 1;
   public static final short ContainerObject = 2;
   public static final short ContainerVehicle = 3;
   public static final Map packetTypes = new TreeMap();

   public static void doPingPacket(ByteBufferWriter var0) {
      var0.putInt(28);
   }

   static {
      PacketTypes.PacketType[] var0 = PacketTypes.PacketType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         PacketTypes.PacketType var3 = var0[var2];
         PacketTypes.PacketType var4 = (PacketTypes.PacketType)packetTypes.put(var3.getId(), var3);
         if (var4 != null) {
            DebugLog.Multiplayer.error(String.format("PacketType: duplicate \"%s\" \"%s\" id=%d", var4.name(), var3.name(), var3.getId()));
         }
      }

   }

   public static enum PacketType {
      ServerPulse(1, 1, 0, (PacketTypes.CallbackServerProcess)null, GameClient::receiveServerPulse, (PacketTypes.CallbackClientProcess)null),
      Login(2, 1, 3, GameServer::receiveLogin, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      HumanVisual(3, 1, 2, GameServer::receiveHumanVisual, GameClient::receiveHumanVisual, (PacketTypes.CallbackClientProcess)null),
      KeepAlive(4, 1, 0, GameServer::receiveKeepAlive, GameClient::receiveKeepAlive, GameClient::skipPacket),
      Vehicles(5, 1, 2, GameServer::receiveVehicles, GameClient::receiveVehicles, GameClient::receiveVehiclesLoading),
      PlayerConnect(6, 1, 3, GameServer::receivePlayerConnect, GameClient::receivePlayerConnect, (PacketTypes.CallbackClientProcess)null),
      VehiclesUnreliable(7, 2, 0, GameServer::receiveVehicles, GameClient::receiveVehicles, GameClient::receiveVehiclesLoading),
      MetaGrid(9, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveMetaGrid, (PacketTypes.CallbackClientProcess)null),
      Helicopter(11, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveHelicopter, (PacketTypes.CallbackClientProcess)null),
      SyncIsoObject(12, 1, 2, GameServer::receiveSyncIsoObject, GameClient::receiveSyncIsoObject, (PacketTypes.CallbackClientProcess)null),
      PlayerTimeout(13, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerTimeout, GameClient::receivePlayerTimeout),
      SteamGeneric(14, 1, 2, GameServer::receiveSteamGeneric, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ServerMap(15, 1, 3, (PacketTypes.CallbackServerProcess)null, GameClient::receiveServerMap, GameClient::receiveServerMapLoading),
      PassengerMap(16, 1, 2, GameServer::receivePassengerMap, GameClient::receivePassengerMap, (PacketTypes.CallbackClientProcess)null),
      AddItemToMap(17, 1, 2, GameServer::receiveAddItemToMap, GameClient::receiveAddItemToMap, (PacketTypes.CallbackClientProcess)null),
      SentChunk(18, 1, 2, (PacketTypes.CallbackServerProcess)null, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      SyncClock(19, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveSyncClock, (PacketTypes.CallbackClientProcess)null),
      AddInventoryItemToContainer(20, 1, 2, GameServer::receiveAddInventoryItemToContainer, GameClient::receiveAddInventoryItemToContainer, (PacketTypes.CallbackClientProcess)null),
      ConnectionDetails(21, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveConnectionDetails, GameClient::receiveConnectionDetails),
      RemoveInventoryItemFromContainer(22, 1, 2, GameServer::receiveRemoveInventoryItemFromContainer, GameClient::receiveRemoveInventoryItemFromContainer, (PacketTypes.CallbackClientProcess)null),
      RemoveItemFromSquare(23, 1, 2, GameServer::receiveRemoveItemFromSquare, GameClient::receiveRemoveItemFromSquare, (PacketTypes.CallbackClientProcess)null),
      RequestLargeAreaZip(24, 1, 2, GameServer::receiveRequestLargeAreaZip, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      Equip(25, 1, 2, GameServer::receiveEquip, GameClient::receiveEquip, (PacketTypes.CallbackClientProcess)null),
      HitCharacter(26, 0, 3, GameServer::receiveHitCharacter, GameClient::receiveHitCharacter, (PacketTypes.CallbackClientProcess)null),
      AddCoopPlayer(27, 1, 2, GameServer::receiveAddCoopPlayer, GameClient::receiveAddCoopPlayer, (PacketTypes.CallbackClientProcess)null),
      WeaponHit(28, 1, 2, GameServer::receiveWeaponHit, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      KillZombie(30, 1, 2, GameServer::receiveKillZombie, GameClient::receiveKillZombie, (PacketTypes.CallbackClientProcess)null),
      SandboxOptions(31, 1, 2, GameServer::receiveSandboxOptions, GameClient::receiveSandboxOptions, (PacketTypes.CallbackClientProcess)null),
      SmashWindow(32, 1, 2, GameServer::receiveSmashWindow, GameClient::receiveSmashWindow, (PacketTypes.CallbackClientProcess)null),
      PlayerDeath(33, 0, 3, GameServer::receivePlayerDeath, GameClient::receivePlayerDeath, (PacketTypes.CallbackClientProcess)null),
      RequestZipList(34, 0, 2, GameServer::receiveRequestZipList, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ItemStats(35, 1, 2, GameServer::receiveItemStats, GameClient::receiveItemStats, (PacketTypes.CallbackClientProcess)null),
      NotRequiredInZip(36, 0, 0, GameServer::receiveNotRequiredInZip, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RequestData(37, 1, 3, GameServer::receiveRequestData, GameClient::receiveRequestData, GameClient::receiveRequestData),
      GlobalObjects(38, 1, 2, GameServer::receiveGlobalObjects, GameClient::receiveGlobalObjects, (PacketTypes.CallbackClientProcess)null),
      ZombieDeath(39, 1, 3, GameServer::receiveZombieDeath, GameClient::receiveZombieDeath, (PacketTypes.CallbackClientProcess)null),
      AccessDenied(40, 1, 2, (PacketTypes.CallbackServerProcess)null, (PacketTypes.CallbackClientProcess)null, GameClient::receiveAccessDenied),
      PlayerDamage(41, 1, 2, GameServer::receivePlayerDamage, GameClient::receivePlayerDamage, (PacketTypes.CallbackClientProcess)null),
      Bandage(42, 1, 2, GameServer::receiveBandage, GameClient::receiveBandage, (PacketTypes.CallbackClientProcess)null),
      EatFood(43, 1, 2, GameServer::receiveEatFood, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RequestItemsForContainer(44, 1, 2, GameServer::receiveRequestItemsForContainer, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      Drink(45, 1, 2, GameServer::receiveDrink, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      SyncAlarmClock(46, 1, 2, GameServer::receiveSyncAlarmClock, GameClient::receiveSyncAlarmClock, (PacketTypes.CallbackClientProcess)null),
      PacketCounts(47, 1, 2, GameServer::receivePacketCounts, GameClient::receivePacketCounts, (PacketTypes.CallbackClientProcess)null),
      SendModData(48, 1, 2, GameServer::receiveSendModData, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RemoveContestedItemsFromInventory(49, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveRemoveContestedItemsFromInventory, (PacketTypes.CallbackClientProcess)null),
      ScoreboardUpdate(50, 1, 2, GameServer::receiveScoreboardUpdate, GameClient::receiveScoreboardUpdate, (PacketTypes.CallbackClientProcess)null),
      ReceiveModData(51, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveReceiveModData, (PacketTypes.CallbackClientProcess)null),
      ServerQuit(52, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveServerQuit, (PacketTypes.CallbackClientProcess)null),
      PlaySound(53, 1, 2, GameServer::receivePlaySound, GameClient::receivePlaySound, (PacketTypes.CallbackClientProcess)null),
      WorldSound(54, 1, 2, GameServer::receiveWorldSound, GameClient::receiveWorldSound, (PacketTypes.CallbackClientProcess)null),
      AddAmbient(55, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddAmbient, (PacketTypes.CallbackClientProcess)null),
      SyncClothing(56, 1, 2, GameServer::receiveSyncClothing, GameClient::receiveSyncClothing, (PacketTypes.CallbackClientProcess)null),
      ClientCommand(57, 1, 2, GameServer::receiveClientCommand, GameClient::receiveClientCommand, (PacketTypes.CallbackClientProcess)null),
      ObjectModData(58, 1, 2, GameServer::receiveObjectModData, GameClient::receiveObjectModData, (PacketTypes.CallbackClientProcess)null),
      ObjectChange(59, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveObjectChange, (PacketTypes.CallbackClientProcess)null),
      BloodSplatter(60, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveBloodSplatter, (PacketTypes.CallbackClientProcess)null),
      ZombieSound(61, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveZombieSound, (PacketTypes.CallbackClientProcess)null),
      ZombieDescriptors(62, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveZombieDescriptors, (PacketTypes.CallbackClientProcess)null),
      SlowFactor(63, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveSlowFactor, (PacketTypes.CallbackClientProcess)null),
      Weather(64, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveWeather, (PacketTypes.CallbackClientProcess)null),
      /** @deprecated */
      @Deprecated
      RequestPlayerData(67, 1, 2, GameServer::receiveRequestPlayerData, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RemoveCorpseFromMap(68, 1, 2, GameServer::receiveRemoveCorpseFromMap, GameClient::receiveRemoveCorpseFromMap, (PacketTypes.CallbackClientProcess)null),
      AddCorpseToMap(69, 1, 2, GameServer::receiveAddCorpseToMap, GameClient::receiveAddCorpseToMap, (PacketTypes.CallbackClientProcess)null),
      StartFire(75, 1, 2, GameServer::receiveStartFire, GameClient::receiveStartFire, (PacketTypes.CallbackClientProcess)null),
      UpdateItemSprite(76, 1, 2, GameServer::receiveUpdateItemSprite, GameClient::receiveUpdateItemSprite, (PacketTypes.CallbackClientProcess)null),
      StartRain(77, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveStartRain, (PacketTypes.CallbackClientProcess)null),
      StopRain(78, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveStopRain, (PacketTypes.CallbackClientProcess)null),
      WorldMessage(79, 1, 2, GameServer::receiveWorldMessage, GameClient::receiveWorldMessage, (PacketTypes.CallbackClientProcess)null),
      getModData(80, 1, 2, GameServer::receiveGetModData, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ReceiveCommand(81, 2, 3, GameServer::receiveReceiveCommand, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ReloadOptions(82, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveReloadOptions, (PacketTypes.CallbackClientProcess)null),
      Kicked(83, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveKicked, GameClient::receiveKickedLoading),
      ExtraInfo(84, 1, 2, GameServer::receiveExtraInfo, GameClient::receiveExtraInfo, (PacketTypes.CallbackClientProcess)null),
      AddItemInInventory(85, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddItemInInventory, (PacketTypes.CallbackClientProcess)null),
      ChangeSafety(86, 1, 2, GameServer::receiveChangeSafety, GameClient::receiveChangeSafety, (PacketTypes.CallbackClientProcess)null),
      Ping(87, 0, 0, GameServer::receivePing, GameClient::receivePing, GameClient::receivePing),
      WriteLog(88, 1, 2, GameServer::receiveWriteLog, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      AddXP(89, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddXP, (PacketTypes.CallbackClientProcess)null),
      UpdateOverlaySprite(90, 1, 2, GameServer::receiveUpdateOverlaySprite, GameClient::receiveUpdateOverlaySprite, (PacketTypes.CallbackClientProcess)null),
      Checksum(91, 1, 3, GameServer::receiveChecksum, GameClient::receiveChecksum, GameClient::receiveChecksumLoading),
      ConstructedZone(92, 1, 2, GameServer::receiveConstructedZone, GameClient::receiveConstructedZone, (PacketTypes.CallbackClientProcess)null),
      RegisterZone(94, 1, 2, GameServer::receiveRegisterZone, GameClient::receiveRegisterZone, (PacketTypes.CallbackClientProcess)null),
      WoundInfection(97, 1, 2, GameServer::receiveWoundInfection, GameClient::receiveWoundInfection, (PacketTypes.CallbackClientProcess)null),
      Stitch(98, 1, 2, GameServer::receiveStitch, GameClient::receiveStitch, (PacketTypes.CallbackClientProcess)null),
      Disinfect(99, 1, 2, GameServer::receiveDisinfect, GameClient::receiveDisinfect, (PacketTypes.CallbackClientProcess)null),
      AdditionalPain(100, 1, 2, GameServer::receiveAdditionalPain, GameClient::receiveAdditionalPain, (PacketTypes.CallbackClientProcess)null),
      RemoveGlass(101, 1, 2, GameServer::receiveRemoveGlass, GameClient::receiveRemoveGlass, (PacketTypes.CallbackClientProcess)null),
      Splint(102, 1, 2, GameServer::receiveSplint, GameClient::receiveSplint, (PacketTypes.CallbackClientProcess)null),
      RemoveBullet(103, 1, 2, GameServer::receiveRemoveBullet, GameClient::receiveRemoveBullet, (PacketTypes.CallbackClientProcess)null),
      CleanBurn(104, 1, 2, GameServer::receiveCleanBurn, GameClient::receiveCleanBurn, (PacketTypes.CallbackClientProcess)null),
      SyncThumpable(105, 1, 2, GameServer::receiveSyncThumpable, GameClient::receiveSyncThumpable, (PacketTypes.CallbackClientProcess)null),
      SyncDoorKey(106, 1, 2, GameServer::receiveSyncDoorKey, GameClient::receiveSyncDoorKey, (PacketTypes.CallbackClientProcess)null),
      AddXpCommand(107, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddXpCommand, (PacketTypes.CallbackClientProcess)null),
      Teleport(108, 1, 2, GameServer::receiveTeleport, GameClient::receiveTeleport, (PacketTypes.CallbackClientProcess)null),
      RemoveBlood(109, 1, 2, GameServer::receiveRemoveBlood, GameClient::receiveRemoveBlood, (PacketTypes.CallbackClientProcess)null),
      AddExplosiveTrap(110, 1, 2, GameServer::receiveAddExplosiveTrap, GameClient::receiveAddExplosiveTrap, (PacketTypes.CallbackClientProcess)null),
      BodyDamageUpdate(112, 1, 2, GameServer::receiveBodyDamageUpdate, GameClient::receiveBodyDamageUpdate, (PacketTypes.CallbackClientProcess)null),
      SyncSafehouse(114, 1, 2, GameServer::receiveSyncSafehouse, GameClient::receiveSyncSafehouse, (PacketTypes.CallbackClientProcess)null),
      SledgehammerDestroy(115, 1, 2, GameServer::receiveSledgehammerDestroy, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      StopFire(116, 1, 2, GameServer::receiveStopFire, GameClient::receiveStopFire, (PacketTypes.CallbackClientProcess)null),
      Cataplasm(117, 1, 2, GameServer::receiveCataplasm, GameClient::receiveCataplasm, (PacketTypes.CallbackClientProcess)null),
      AddAlarm(118, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddAlarm, (PacketTypes.CallbackClientProcess)null),
      PlaySoundEveryPlayer(119, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlaySoundEveryPlayer, (PacketTypes.CallbackClientProcess)null),
      SyncFurnace(120, 1, 2, GameServer::receiveSyncFurnace, GameClient::receiveSyncFurnace, (PacketTypes.CallbackClientProcess)null),
      SendCustomColor(121, 1, 2, GameServer::receiveSendCustomColor, GameClient::receiveSendCustomColor, (PacketTypes.CallbackClientProcess)null),
      SyncCompost(122, 1, 2, GameServer::receiveSyncCompost, GameClient::receiveSyncCompost, (PacketTypes.CallbackClientProcess)null),
      ChangePlayerStats(123, 1, 2, GameServer::receiveChangePlayerStats, GameClient::receiveChangePlayerStats, (PacketTypes.CallbackClientProcess)null),
      AddXpFromPlayerStatsUI(124, 1, 2, GameServer::receiveAddXpFromPlayerStatsUI, GameClient::receiveAddXpFromPlayerStatsUI, (PacketTypes.CallbackClientProcess)null),
      SyncXP(126, 1, 2, GameServer::receiveSyncXP, GameClient::receiveSyncXP, (PacketTypes.CallbackClientProcess)null),
      PacketTypeShort(127, 1, 2, GameServer::receivePacketTypeShort, GameClient::receivePacketTypeShort, (PacketTypes.CallbackClientProcess)null),
      Userlog(128, 1, 2, GameServer::receiveUserlog, GameClient::receiveUserlog, (PacketTypes.CallbackClientProcess)null),
      AddUserlog(129, 1, 2, GameServer::receiveAddUserlog, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RemoveUserlog(130, 1, 2, GameServer::receiveRemoveUserlog, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      AddWarningPoint(131, 1, 2, GameServer::receiveAddWarningPoint, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      MessageForAdmin(132, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveMessageForAdmin, (PacketTypes.CallbackClientProcess)null),
      WakeUpPlayer(133, 1, 2, GameServer::receiveWakeUpPlayer, GameClient::receiveWakeUpPlayer, (PacketTypes.CallbackClientProcess)null),
      /** @deprecated */
      @Deprecated
      SendTransactionID(134, 1, 2, (PacketTypes.CallbackServerProcess)null, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      GetDBSchema(135, 1, 2, GameServer::receiveGetDBSchema, GameClient::receiveGetDBSchema, (PacketTypes.CallbackClientProcess)null),
      GetTableResult(136, 1, 2, GameServer::receiveGetTableResult, GameClient::receiveGetTableResult, (PacketTypes.CallbackClientProcess)null),
      ExecuteQuery(137, 1, 2, GameServer::receiveExecuteQuery, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ChangeTextColor(138, 1, 2, GameServer::receiveChangeTextColor, GameClient::receiveChangeTextColor, (PacketTypes.CallbackClientProcess)null),
      SyncNonPvpZone(139, 1, 2, GameServer::receiveSyncNonPvpZone, GameClient::receiveSyncNonPvpZone, (PacketTypes.CallbackClientProcess)null),
      SyncFaction(140, 1, 2, GameServer::receiveSyncFaction, GameClient::receiveSyncFaction, (PacketTypes.CallbackClientProcess)null),
      SendFactionInvite(141, 1, 2, GameServer::receiveSendFactionInvite, GameClient::receiveSendFactionInvite, (PacketTypes.CallbackClientProcess)null),
      AcceptedFactionInvite(142, 1, 2, GameServer::receiveAcceptedFactionInvite, GameClient::receiveAcceptedFactionInvite, (PacketTypes.CallbackClientProcess)null),
      AddTicket(143, 1, 2, GameServer::receiveAddTicket, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ViewTickets(144, 1, 2, GameServer::receiveViewTickets, GameClient::receiveViewTickets, (PacketTypes.CallbackClientProcess)null),
      RemoveTicket(145, 1, 2, GameServer::receiveRemoveTicket, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RequestTrading(146, 1, 2, GameServer::receiveRequestTrading, GameClient::receiveRequestTrading, (PacketTypes.CallbackClientProcess)null),
      TradingUIAddItem(147, 1, 2, GameServer::receiveTradingUIAddItem, GameClient::receiveTradingUIAddItem, (PacketTypes.CallbackClientProcess)null),
      TradingUIRemoveItem(148, 1, 2, GameServer::receiveTradingUIRemoveItem, GameClient::receiveTradingUIRemoveItem, (PacketTypes.CallbackClientProcess)null),
      TradingUIUpdateState(149, 1, 2, GameServer::receiveTradingUIUpdateState, GameClient::receiveTradingUIUpdateState, (PacketTypes.CallbackClientProcess)null),
      SendItemListNet(150, 1, 2, GameServer::receiveSendItemListNet, GameClient::receiveSendItemListNet, (PacketTypes.CallbackClientProcess)null),
      ChunkObjectState(151, 1, 2, GameServer::receiveChunkObjectState, GameClient::receiveChunkObjectState, (PacketTypes.CallbackClientProcess)null),
      ReadAnnotedMap(152, 1, 2, GameServer::receiveReadAnnotedMap, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      RequestInventory(153, 1, 2, GameServer::receiveRequestInventory, GameClient::receiveRequestInventory, (PacketTypes.CallbackClientProcess)null),
      SendInventory(154, 1, 2, GameServer::receiveSendInventory, GameClient::receiveSendInventory, (PacketTypes.CallbackClientProcess)null),
      InvMngReqItem(155, 1, 2, GameServer::receiveInvMngReqItem, GameClient::receiveInvMngReqItem, (PacketTypes.CallbackClientProcess)null),
      InvMngGetItem(156, 1, 2, GameServer::receiveInvMngGetItem, GameClient::receiveInvMngGetItem, (PacketTypes.CallbackClientProcess)null),
      InvMngRemoveItem(157, 1, 2, GameServer::receiveInvMngRemoveItem, GameClient::receiveInvMngRemoveItem, (PacketTypes.CallbackClientProcess)null),
      StartPause(158, 1, 3, (PacketTypes.CallbackServerProcess)null, GameClient::receiveStartPause, (PacketTypes.CallbackClientProcess)null),
      StopPause(159, 1, 3, (PacketTypes.CallbackServerProcess)null, GameClient::receiveStopPause, (PacketTypes.CallbackClientProcess)null),
      TimeSync(160, 1, 2, GameServer::receiveTimeSync, GameClient::receiveTimeSync, (PacketTypes.CallbackClientProcess)null),
      SyncIsoObjectReq(161, 1, 2, GameServer::receiveSyncIsoObjectReq, GameClient::receiveSyncIsoObjectReq, (PacketTypes.CallbackClientProcess)null),
      PlayerSave(162, 1, 2, GameServer::receivePlayerSave, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      SyncWorldObjectsReq(163, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveSyncWorldObjectsReq, (PacketTypes.CallbackClientProcess)null),
      SyncObjects(164, 1, 2, GameServer::receiveSyncObjects, GameClient::receiveSyncObjects, (PacketTypes.CallbackClientProcess)null),
      SendPlayerProfile(166, 1, 3, GameServer::receiveSendPlayerProfile, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      LoadPlayerProfile(167, 1, 3, GameServer::receiveLoadPlayerProfile, GameClient::receiveLoadPlayerProfile, (PacketTypes.CallbackClientProcess)null),
      SpawnRegion(171, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveSpawnRegion, GameClient::receiveSpawnRegion),
      PlayerDamageFromCarCrash(172, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerDamageFromCarCrash, (PacketTypes.CallbackClientProcess)null),
      PlayerAttachedItem(173, 1, 2, GameServer::receivePlayerAttachedItem, GameClient::receivePlayerAttachedItem, (PacketTypes.CallbackClientProcess)null),
      ZombieHelmetFalling(174, 1, 2, GameServer::receiveZombieHelmetFalling, GameClient::receiveZombieHelmetFalling, (PacketTypes.CallbackClientProcess)null),
      AddBrokenGlass(175, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddBrokenGlass, (PacketTypes.CallbackClientProcess)null),
      SyncPerks(177, 1, 2, GameServer::receiveSyncPerks, GameClient::receiveSyncPerks, (PacketTypes.CallbackClientProcess)null),
      SyncWeight(178, 1, 2, GameServer::receiveSyncWeight, GameClient::receiveSyncWeight, (PacketTypes.CallbackClientProcess)null),
      SyncInjuries(179, 1, 2, GameServer::receiveSyncInjuries, GameClient::receiveSyncInjuries, (PacketTypes.CallbackClientProcess)null),
      SyncEquippedRadioFreq(181, 1, 2, GameServer::receiveSyncEquippedRadioFreq, GameClient::receiveSyncEquippedRadioFreq, (PacketTypes.CallbackClientProcess)null),
      InitPlayerChat(182, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveInitPlayerChat, (PacketTypes.CallbackClientProcess)null),
      PlayerJoinChat(183, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerJoinChat, (PacketTypes.CallbackClientProcess)null),
      PlayerLeaveChat(184, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerLeaveChat, (PacketTypes.CallbackClientProcess)null),
      ChatMessageFromPlayer(185, 1, 2, GameServer::receiveChatMessageFromPlayer, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      ChatMessageToPlayer(186, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveChatMessageToPlayer, (PacketTypes.CallbackClientProcess)null),
      PlayerStartPMChat(187, 1, 2, GameServer::receivePlayerStartPMChat, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      AddChatTab(189, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveAddChatTab, (PacketTypes.CallbackClientProcess)null),
      RemoveChatTab(190, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveRemoveChatTab, (PacketTypes.CallbackClientProcess)null),
      PlayerConnectedToChat(191, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerConnectedToChat, (PacketTypes.CallbackClientProcess)null),
      PlayerNotFound(192, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receivePlayerNotFound, (PacketTypes.CallbackClientProcess)null),
      SendSafehouseInvite(193, 1, 2, GameServer::receiveSendSafehouseInvite, GameClient::receiveSendSafehouseInvite, (PacketTypes.CallbackClientProcess)null),
      AcceptedSafehouseInvite(194, 1, 2, GameServer::receiveAcceptedSafehouseInvite, GameClient::receiveAcceptedSafehouseInvite, (PacketTypes.CallbackClientProcess)null),
      ClimateManagerPacket(200, 1, 2, GameServer::receiveClimateManagerPacket, GameClient::receiveClimateManagerPacket, (PacketTypes.CallbackClientProcess)null),
      IsoRegionServerPacket(201, 1, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveIsoRegionServerPacket, (PacketTypes.CallbackClientProcess)null),
      IsoRegionClientRequestFullUpdate(202, 1, 2, GameServer::receiveIsoRegionClientRequestFullUpdate, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null),
      EventPacket(210, 0, 3, GameServer::receiveEventPacket, GameClient::receiveEventPacket, (PacketTypes.CallbackClientProcess)null),
      Statistic(211, 1, 0, GameServer::receiveStatistic, GameClient::receiveStatistic, (PacketTypes.CallbackClientProcess)null),
      StatisticRequest(212, 1, 2, GameServer::receiveStatisticRequest, GameClient::receiveStatisticRequest, (PacketTypes.CallbackClientProcess)null),
      PlayerUpdateReliable(213, 0, 2, GameServer::receivePlayerUpdate, GameClient::receivePlayerUpdate, (PacketTypes.CallbackClientProcess)null),
      ActionPacket(214, 1, 3, GameServer::receiveActionPacket, GameClient::receiveActionPacket, (PacketTypes.CallbackClientProcess)null),
      ZombieControl(215, 0, 2, (PacketTypes.CallbackServerProcess)null, GameClient::receiveZombieControl, (PacketTypes.CallbackClientProcess)null),
      PlayWorldSound(216, 1, 2, GameServer::receivePlayWorldSound, GameClient::receivePlayWorldSound, (PacketTypes.CallbackClientProcess)null),
      StopSound(217, 1, 2, GameServer::receiveStopSound, GameClient::receiveStopSound, (PacketTypes.CallbackClientProcess)null),
      PlayerUpdate(218, 2, 0, GameServer::receivePlayerUpdate, GameClient::receivePlayerUpdate, (PacketTypes.CallbackClientProcess)null),
      ZombieSimulation(219, 2, 0, GameServer::receiveZombieSimulation, GameClient::receiveZombieSimulation, (PacketTypes.CallbackClientProcess)null),
      PingFromClient(220, 1, 0, GameServer::receivePingFromClient, GameClient::receivePingFromClient, (PacketTypes.CallbackClientProcess)null),
      ZombieSimulationReliable(221, 0, 2, GameServer::receiveZombieSimulation, GameClient::receiveZombieSimulation, (PacketTypes.CallbackClientProcess)null),
      EatBody(222, 1, 2, GameServer::receiveEatBody, GameClient::receiveEatBody, (PacketTypes.CallbackClientProcess)null),
      Thump(223, 1, 2, GameServer::receiveThump, GameClient::receiveThump, (PacketTypes.CallbackClientProcess)null),
      GlobalModData(32000, 0, 2, GameServer::receiveGlobalModData, GameClient::receiveGlobalModData, (PacketTypes.CallbackClientProcess)null),
      GlobalModDataRequest(32001, 0, 2, GameServer::receiveGlobalModDataRequest, (PacketTypes.CallbackClientProcess)null, (PacketTypes.CallbackClientProcess)null);

      private final short id;
      public int PacketPriority;
      public int PacketReliability;
      public byte OrderingChannel;
      PacketTypes.CallbackServerProcess serverProcess;
      PacketTypes.CallbackClientProcess mainLoopHandlePacketInternal;
      PacketTypes.CallbackClientProcess gameLoadingDealWithNetData;
      public int incomePackets;
      public int outcomePackets;
      public int incomeBytes;
      public int outcomeBytes;

      private PacketType(int var3, int var4, int var5, PacketTypes.CallbackServerProcess var6, PacketTypes.CallbackClientProcess var7, PacketTypes.CallbackClientProcess var8) {
         this(var3, var4, var5, 0, var6, var7, var8);
      }

      private PacketType(int var3, int var4, int var5, int var6, PacketTypes.CallbackServerProcess var7, PacketTypes.CallbackClientProcess var8, PacketTypes.CallbackClientProcess var9) {
         this.id = (short)var3;
         this.PacketPriority = var4;
         this.PacketReliability = var5;
         this.OrderingChannel = (byte)var6;
         this.serverProcess = var7;
         this.mainLoopHandlePacketInternal = var8;
         this.gameLoadingDealWithNetData = var9;
         this.resetStatistics();
      }

      public void resetStatistics() {
         this.incomePackets = 0;
         this.outcomePackets = 0;
         this.incomeBytes = 0;
         this.outcomeBytes = 0;
      }

      public void send(UdpConnection var1) {
         var1.endPacket(this.PacketPriority, this.PacketReliability, this.OrderingChannel);
      }

      public void doPacket(ByteBufferWriter var1) {
         var1.putByte((byte)-122);
         var1.putShort(this.getId());
      }

      public short getId() {
         return this.id;
      }

      public void onServerPacket(ByteBuffer var1, UdpConnection var2, short var3) throws Exception {
         this.serverProcess.call(var1, var2, var3);
      }

      public void onMainLoopHandlePacketInternal(ByteBuffer var1, short var2) throws IOException {
         this.mainLoopHandlePacketInternal.call(var1, var2);
      }

      public boolean onGameLoadingDealWithNetData(ByteBuffer var1, short var2) {
         if (this.gameLoadingDealWithNetData == null) {
            if (Core.bDebug) {
               DebugLog.log(DebugType.Network, "Delay processing packet of type " + this.name() + " while loading game");
            }

            return false;
         } else {
            try {
               this.gameLoadingDealWithNetData.call(var1, var2);
               return true;
            } catch (Exception var4) {
               return false;
            }
         }
      }

      // $FF: synthetic method
      private static PacketTypes.PacketType[] $values() {
         return new PacketTypes.PacketType[]{ServerPulse, Login, HumanVisual, KeepAlive, Vehicles, PlayerConnect, VehiclesUnreliable, MetaGrid, Helicopter, SyncIsoObject, PlayerTimeout, SteamGeneric, ServerMap, PassengerMap, AddItemToMap, SentChunk, SyncClock, AddInventoryItemToContainer, ConnectionDetails, RemoveInventoryItemFromContainer, RemoveItemFromSquare, RequestLargeAreaZip, Equip, HitCharacter, AddCoopPlayer, WeaponHit, KillZombie, SandboxOptions, SmashWindow, PlayerDeath, RequestZipList, ItemStats, NotRequiredInZip, RequestData, GlobalObjects, ZombieDeath, AccessDenied, PlayerDamage, Bandage, EatFood, RequestItemsForContainer, Drink, SyncAlarmClock, PacketCounts, SendModData, RemoveContestedItemsFromInventory, ScoreboardUpdate, ReceiveModData, ServerQuit, PlaySound, WorldSound, AddAmbient, SyncClothing, ClientCommand, ObjectModData, ObjectChange, BloodSplatter, ZombieSound, ZombieDescriptors, SlowFactor, Weather, RequestPlayerData, RemoveCorpseFromMap, AddCorpseToMap, StartFire, UpdateItemSprite, StartRain, StopRain, WorldMessage, getModData, ReceiveCommand, ReloadOptions, Kicked, ExtraInfo, AddItemInInventory, ChangeSafety, Ping, WriteLog, AddXP, UpdateOverlaySprite, Checksum, ConstructedZone, RegisterZone, WoundInfection, Stitch, Disinfect, AdditionalPain, RemoveGlass, Splint, RemoveBullet, CleanBurn, SyncThumpable, SyncDoorKey, AddXpCommand, Teleport, RemoveBlood, AddExplosiveTrap, BodyDamageUpdate, SyncSafehouse, SledgehammerDestroy, StopFire, Cataplasm, AddAlarm, PlaySoundEveryPlayer, SyncFurnace, SendCustomColor, SyncCompost, ChangePlayerStats, AddXpFromPlayerStatsUI, SyncXP, PacketTypeShort, Userlog, AddUserlog, RemoveUserlog, AddWarningPoint, MessageForAdmin, WakeUpPlayer, SendTransactionID, GetDBSchema, GetTableResult, ExecuteQuery, ChangeTextColor, SyncNonPvpZone, SyncFaction, SendFactionInvite, AcceptedFactionInvite, AddTicket, ViewTickets, RemoveTicket, RequestTrading, TradingUIAddItem, TradingUIRemoveItem, TradingUIUpdateState, SendItemListNet, ChunkObjectState, ReadAnnotedMap, RequestInventory, SendInventory, InvMngReqItem, InvMngGetItem, InvMngRemoveItem, StartPause, StopPause, TimeSync, SyncIsoObjectReq, PlayerSave, SyncWorldObjectsReq, SyncObjects, SendPlayerProfile, LoadPlayerProfile, SpawnRegion, PlayerDamageFromCarCrash, PlayerAttachedItem, ZombieHelmetFalling, AddBrokenGlass, SyncPerks, SyncWeight, SyncInjuries, SyncEquippedRadioFreq, InitPlayerChat, PlayerJoinChat, PlayerLeaveChat, ChatMessageFromPlayer, ChatMessageToPlayer, PlayerStartPMChat, AddChatTab, RemoveChatTab, PlayerConnectedToChat, PlayerNotFound, SendSafehouseInvite, AcceptedSafehouseInvite, ClimateManagerPacket, IsoRegionServerPacket, IsoRegionClientRequestFullUpdate, EventPacket, Statistic, StatisticRequest, PlayerUpdateReliable, ActionPacket, ZombieControl, PlayWorldSound, StopSound, PlayerUpdate, ZombieSimulation, PingFromClient, ZombieSimulationReliable, EatBody, Thump, GlobalModData, GlobalModDataRequest};
      }
   }

   public interface CallbackClientProcess {
      void call(ByteBuffer var1, short var2) throws IOException;
   }

   public interface CallbackServerProcess {
      void call(ByteBuffer var1, UdpConnection var2, short var3) throws Exception;
   }
}
