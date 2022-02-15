package zombie.Lua;

import fmod.fmod.EmitterType;
import fmod.fmod.FMODAudio;
import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundBank;
import fmod.fmod.FMODSoundEmitter;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.luaj.kahluafork.compiler.FuncState;
import org.lwjglx.input.Controller;
import org.lwjglx.input.Controllers;
import org.lwjglx.input.Keyboard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.Coroutine;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;
import zombie.AmbientStreamManager;
import zombie.BaseAmbientStreamManager;
import zombie.BaseSoundManager;
import zombie.DummySoundManager;
import zombie.GameSounds;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MapGroups;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZombieSpawnRecorder;
import zombie.ZomboidFileSystem;
import zombie.ai.GameCharacterAIBrain;
import zombie.ai.MapKnowledge;
import zombie.ai.sadisticAIDirector.SleepingEvent;
import zombie.ai.states.AttackState;
import zombie.ai.states.BurntToDeath;
import zombie.ai.states.ClimbDownSheetRopeState;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbOverWallState;
import zombie.ai.states.ClimbSheetRopeState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.CloseWindowState;
import zombie.ai.states.CrawlingZombieTurnState;
import zombie.ai.states.FakeDeadAttackState;
import zombie.ai.states.FakeDeadZombieState;
import zombie.ai.states.FishingState;
import zombie.ai.states.FitnessState;
import zombie.ai.states.IdleState;
import zombie.ai.states.LungeState;
import zombie.ai.states.OpenWindowState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.PlayerActionsState;
import zombie.ai.states.PlayerAimState;
import zombie.ai.states.PlayerEmoteState;
import zombie.ai.states.PlayerExtState;
import zombie.ai.states.PlayerFallDownState;
import zombie.ai.states.PlayerFallingState;
import zombie.ai.states.PlayerGetUpState;
import zombie.ai.states.PlayerHitReactionPVPState;
import zombie.ai.states.PlayerHitReactionState;
import zombie.ai.states.PlayerKnockedDown;
import zombie.ai.states.PlayerOnGroundState;
import zombie.ai.states.PlayerSitOnGroundState;
import zombie.ai.states.PlayerStrafeState;
import zombie.ai.states.SmashWindowState;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.ai.states.ThumpState;
import zombie.ai.states.WalkTowardState;
import zombie.ai.states.ZombieFallDownState;
import zombie.ai.states.ZombieGetDownState;
import zombie.ai.states.ZombieGetUpState;
import zombie.ai.states.ZombieIdleState;
import zombie.ai.states.ZombieOnGroundState;
import zombie.ai.states.ZombieReanimateState;
import zombie.ai.states.ZombieSittingState;
import zombie.asset.Asset;
import zombie.asset.AssetPath;
import zombie.audio.BaseSoundBank;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundBank;
import zombie.audio.DummySoundEmitter;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.CharacterActionAnims;
import zombie.characters.CharacterSoundEmitter;
import zombie.characters.DummyCharacterSoundEmitter;
import zombie.characters.Faction;
import zombie.characters.HairOutfitDefinitions;
import zombie.characters.HaloTextHelper;
import zombie.characters.IsoDummyCameraCharacter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.Stats;
import zombie.characters.SurvivorDesc;
import zombie.characters.SurvivorFactory;
import zombie.characters.ZombiesZoneDefinition;
import zombie.characters.AttachedItems.AttachedItem;
import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.AttachedItems.AttachedLocation;
import zombie.characters.AttachedItems.AttachedLocationGroup;
import zombie.characters.AttachedItems.AttachedLocations;
import zombie.characters.AttachedItems.AttachedWeaponDefinitions;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.BodyDamage.Fitness;
import zombie.characters.BodyDamage.Metabolics;
import zombie.characters.BodyDamage.Nutrition;
import zombie.characters.BodyDamage.Thermoregulator;
import zombie.characters.CharacterTimedActions.LuaTimedAction;
import zombie.characters.CharacterTimedActions.LuaTimedActionNew;
import zombie.characters.Moodles.Moodle;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.WornItems.BodyLocation;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.WornItems.WornItem;
import zombie.characters.WornItems.WornItems;
import zombie.characters.action.ActionGroup;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.ObservationFactory;
import zombie.characters.traits.TraitCollection;
import zombie.characters.traits.TraitFactory;
import zombie.chat.ChatBase;
import zombie.chat.ChatManager;
import zombie.chat.ChatMessage;
import zombie.chat.ServerChatMessage;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigOption;
import zombie.config.DoubleConfigOption;
import zombie.config.EnumConfigOption;
import zombie.config.IntegerConfigOption;
import zombie.config.StringConfigOption;
import zombie.core.BoxedStaticValues;
import zombie.core.Clipboard;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.GameVersion;
import zombie.core.ImmutableColor;
import zombie.core.IndieFileLoader;
import zombie.core.Language;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.ThreadGroups;
import zombie.core.Translator;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.input.Input;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.ZLogger;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.physics.Bullet;
import zombie.core.physics.WorldSimulation;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.VoiceManager;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AnimNodeAssetManager;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelAssetManager;
import zombie.core.skinnedmodel.model.WorldItemModelDrawer;
import zombie.core.skinnedmodel.population.BeardStyle;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingDecalGroup;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.DefaultClothing;
import zombie.core.skinnedmodel.population.HairStyle;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.stash.Stash;
import zombie.core.stash.StashBuilding;
import zombie.core.stash.StashSystem;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.znet.GameServerDetails;
import zombie.core.znet.ISteamWorkshopCallback;
import zombie.core.znet.ServerBrowser;
import zombie.core.znet.SteamFriend;
import zombie.core.znet.SteamFriends;
import zombie.core.znet.SteamUGCDetails;
import zombie.core.znet.SteamUser;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.core.znet.SteamWorkshopItem;
import zombie.debug.BooleanDebugOption;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LineDrawer;
import zombie.erosion.ErosionConfig;
import zombie.erosion.ErosionData;
import zombie.erosion.ErosionMain;
import zombie.erosion.season.ErosionSeason;
import zombie.gameStates.AnimationViewerState;
import zombie.gameStates.AttachmentEditorState;
import zombie.gameStates.ChooseGameInfo;
import zombie.gameStates.ConnectToServerState;
import zombie.gameStates.DebugChunkState;
import zombie.gameStates.DebugGlobalObjectState;
import zombie.gameStates.GameLoadingState;
import zombie.gameStates.GameState;
import zombie.gameStates.IngameState;
import zombie.gameStates.MainScreenState;
import zombie.globalObjects.CGlobalObject;
import zombie.globalObjects.CGlobalObjectSystem;
import zombie.globalObjects.CGlobalObjects;
import zombie.globalObjects.SGlobalObject;
import zombie.globalObjects.SGlobalObjectSystem;
import zombie.globalObjects.SGlobalObjects;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.inventory.FixingManager;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.ItemType;
import zombie.inventory.RecipeManager;
import zombie.inventory.types.AlarmClock;
import zombie.inventory.types.AlarmClockClothing;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.ComboItem;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Key;
import zombie.inventory.types.KeyRing;
import zombie.inventory.types.Literature;
import zombie.inventory.types.MapItem;
import zombie.inventory.types.Moveable;
import zombie.inventory.types.Radio;
import zombie.inventory.types.WeaponPart;
import zombie.inventory.types.WeaponType;
import zombie.iso.BentFences;
import zombie.iso.BrokenFences;
import zombie.iso.BuildingDef;
import zombie.iso.CellLoader;
import zombie.iso.ContainerOverlays;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoDirectionSet;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoHeatSource;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoLot;
import zombie.iso.IsoLuaMover;
import zombie.iso.IsoMarkers;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaChunk;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoPuddles;
import zombie.iso.IsoPushableObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWaterGeometry;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LosUtil;
import zombie.iso.MetaObject;
import zombie.iso.MultiStageBuilding;
import zombie.iso.RoomDef;
import zombie.iso.SearchMode;
import zombie.iso.SliceY;
import zombie.iso.TileOverlays;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.iso.WorldMarkers;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.areas.SafeHouse;
import zombie.iso.areas.isoregion.IsoRegionLogType;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.IsoRegionsLogger;
import zombie.iso.areas.isoregion.IsoRegionsRenderer;
import zombie.iso.areas.isoregion.data.DataCell;
import zombie.iso.areas.isoregion.data.DataChunk;
import zombie.iso.areas.isoregion.regions.IsoChunkRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;
import zombie.iso.objects.BSFurnace;
import zombie.iso.objects.IsoBarbecue;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoBrokenGlass;
import zombie.iso.objects.IsoCarBatteryCharger;
import zombie.iso.objects.IsoClothingDryer;
import zombie.iso.objects.IsoClothingWasher;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoFire;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoFireplace;
import zombie.iso.objects.IsoGenerator;
import zombie.iso.objects.IsoJukebox;
import zombie.iso.objects.IsoLightSwitch;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoMolotovCocktail;
import zombie.iso.objects.IsoRadio;
import zombie.iso.objects.IsoStove;
import zombie.iso.objects.IsoTelevision;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTrap;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWaveSignal;
import zombie.iso.objects.IsoWheelieBin;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.ObjectRenderEffects;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteGrid;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.weather.ClimateColorInfo;
import zombie.iso.weather.ClimateForecaster;
import zombie.iso.weather.ClimateHistory;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.ClimateMoon;
import zombie.iso.weather.ClimateValues;
import zombie.iso.weather.Temperature;
import zombie.iso.weather.ThunderStorm;
import zombie.iso.weather.WeatherPeriod;
import zombie.iso.weather.WorldFlares;
import zombie.iso.weather.fog.ImprovedFog;
import zombie.iso.weather.fx.IsoWeatherFX;
import zombie.modding.ActiveMods;
import zombie.modding.ActiveModsFile;
import zombie.modding.ModUtilsJava;
import zombie.network.CoopMaster;
import zombie.network.DBResult;
import zombie.network.DBTicket;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.MPStatistic;
import zombie.network.MPStatistics;
import zombie.network.NetChecksum;
import zombie.network.NetworkAIParams;
import zombie.network.PacketTypes;
import zombie.network.Server;
import zombie.network.ServerOptions;
import zombie.network.ServerSettings;
import zombie.network.ServerSettingsManager;
import zombie.network.ServerWorldDatabase;
import zombie.network.Userlog;
import zombie.network.chat.ChatType;
import zombie.popman.ZombiePopulationManager;
import zombie.popman.ZombiePopulationRenderer;
import zombie.profanity.ProfanityFilter;
import zombie.radio.ChannelCategory;
import zombie.radio.RadioAPI;
import zombie.radio.RadioData;
import zombie.radio.ZomboidRadio;
import zombie.radio.StorySounds.DataPoint;
import zombie.radio.StorySounds.EventSound;
import zombie.radio.StorySounds.SLSoundManager;
import zombie.radio.StorySounds.StorySound;
import zombie.radio.StorySounds.StorySoundEvent;
import zombie.radio.devices.DeviceData;
import zombie.radio.devices.DevicePresets;
import zombie.radio.devices.PresetEntry;
import zombie.radio.media.MediaData;
import zombie.radio.media.RecordedMedia;
import zombie.radio.scripting.DynamicRadioChannel;
import zombie.radio.scripting.RadioBroadCast;
import zombie.radio.scripting.RadioChannel;
import zombie.radio.scripting.RadioLine;
import zombie.radio.scripting.RadioScript;
import zombie.radio.scripting.RadioScriptManager;
import zombie.randomizedWorld.RandomizedWorldBase;
import zombie.randomizedWorld.randomizedBuilding.RBBar;
import zombie.randomizedWorld.randomizedBuilding.RBBasic;
import zombie.randomizedWorld.randomizedBuilding.RBBurnt;
import zombie.randomizedWorld.randomizedBuilding.RBBurntCorpse;
import zombie.randomizedWorld.randomizedBuilding.RBBurntFireman;
import zombie.randomizedWorld.randomizedBuilding.RBCafe;
import zombie.randomizedWorld.randomizedBuilding.RBClinic;
import zombie.randomizedWorld.randomizedBuilding.RBHairSalon;
import zombie.randomizedWorld.randomizedBuilding.RBKateAndBaldspot;
import zombie.randomizedWorld.randomizedBuilding.RBLooted;
import zombie.randomizedWorld.randomizedBuilding.RBOffice;
import zombie.randomizedWorld.randomizedBuilding.RBOther;
import zombie.randomizedWorld.randomizedBuilding.RBPileOCrepe;
import zombie.randomizedWorld.randomizedBuilding.RBPizzaWhirled;
import zombie.randomizedWorld.randomizedBuilding.RBSafehouse;
import zombie.randomizedWorld.randomizedBuilding.RBSchool;
import zombie.randomizedWorld.randomizedBuilding.RBShopLooted;
import zombie.randomizedWorld.randomizedBuilding.RBSpiffo;
import zombie.randomizedWorld.randomizedBuilding.RBStripclub;
import zombie.randomizedWorld.randomizedBuilding.RandomizedBuildingBase;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBandPractice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBathroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBedroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBleach;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSCorpsePsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSDeadDrunk;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSFootballNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunmanInBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunslinger;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHenDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHockeyPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHouseParty;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPokerNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPoliceAtHouse;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscape;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscapeWithPolice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSkeletonPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSpecificProfession;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStagDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStudentNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSuicidePact;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSTinFoilHat;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombieLockedBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombiesEating;
import zombie.randomizedWorld.randomizedDeadSurvivor.RandomizedDeadSurvivorBase;
import zombie.randomizedWorld.randomizedVehicleStory.RVSAmbulanceCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBanditRoad;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBurntCar;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrashCorpse;
import zombie.randomizedWorld.randomizedVehicleStory.RVSChangingTire;
import zombie.randomizedWorld.randomizedVehicleStory.RVSConstructionSite;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCrashHorde;
import zombie.randomizedWorld.randomizedVehicleStory.RVSFlippedCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockade;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockadeShooting;
import zombie.randomizedWorld.randomizedVehicleStory.RVSTrailerCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSUtilityVehicle;
import zombie.randomizedWorld.randomizedVehicleStory.RandomizedVehicleStoryBase;
import zombie.randomizedWorld.randomizedZoneStory.RZSBBQParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBaseball;
import zombie.randomizedWorld.randomizedZoneStory.RZSBeachParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBuryingCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSFishingTrip;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCampEaten;
import zombie.randomizedWorld.randomizedZoneStory.RZSHunterCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFest;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFestStage;
import zombie.randomizedWorld.randomizedZoneStory.RZSSexyTime;
import zombie.randomizedWorld.randomizedZoneStory.RZSTrapperCamp;
import zombie.randomizedWorld.randomizedZoneStory.RandomizedZoneStoryBase;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDBHelper;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.EvolvedRecipe;
import zombie.scripting.objects.Fixing;
import zombie.scripting.objects.GameSoundScript;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.ItemRecipe;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.scripting.objects.MovableRecipe;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.VehicleScript;
import zombie.spnetwork.SinglePlayerClient;
import zombie.ui.ActionProgressBar;
import zombie.ui.Clock;
import zombie.ui.ModalDialog;
import zombie.ui.MoodlesUI;
import zombie.ui.NewHealthPanel;
import zombie.ui.ObjectTooltip;
import zombie.ui.RadarPanel;
import zombie.ui.RadialMenu;
import zombie.ui.RadialProgressBar;
import zombie.ui.SpeedControls;
import zombie.ui.TextDrawObject;
import zombie.ui.TextManager;
import zombie.ui.UI3DModel;
import zombie.ui.UIDebugConsole;
import zombie.ui.UIElement;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.ui.UIServerToolbox;
import zombie.ui.UITextBox2;
import zombie.ui.UITransition;
import zombie.ui.VehicleGauge;
import zombie.util.AddCoopPlayer;
import zombie.util.PZCalendar;
import zombie.util.PublicServerUtil;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayList;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.EditVehicleState;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PathFindState2;
import zombie.vehicles.UI3DScene;
import zombie.vehicles.VehicleDoor;
import zombie.vehicles.VehicleLight;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleType;
import zombie.vehicles.VehicleWindow;
import zombie.vehicles.VehiclesDB2;
import zombie.world.moddata.ModData;
import zombie.worldMap.UIWorldMap;

public final class LuaManager {
   public static KahluaConverterManager converterManager = new KahluaConverterManager();
   public static J2SEPlatform platform = new J2SEPlatform();
   public static KahluaTable env;
   public static KahluaThread thread;
   public static KahluaThread debugthread;
   public static LuaCaller caller;
   public static LuaCaller debugcaller;
   public static LuaManager.Exposer exposer;
   public static ArrayList loaded;
   private static final HashSet loading;
   public static HashMap loadedReturn;
   public static boolean checksumDone;
   public static ArrayList loadList;
   static ArrayList paths;
   private static final HashMap luaFunctionMap;
   private static final HashSet s_wiping;

   public static void outputTable(KahluaTable var0, int var1) {
   }

   private static void wipeRecurse(KahluaTable var0) {
      if (!var0.isEmpty()) {
         if (!s_wiping.contains(var0)) {
            s_wiping.add(var0);
            KahluaTableIterator var1 = var0.iterator();

            while(var1.advance()) {
               KahluaTable var2 = (KahluaTable)Type.tryCastTo(var1.getValue(), KahluaTable.class);
               if (var2 != null) {
                  wipeRecurse(var2);
               }
            }

            s_wiping.remove(var0);
            var0.wipe();
         }
      }
   }

   public static void init() {
      loaded.clear();
      loading.clear();
      loadedReturn.clear();
      paths.clear();
      luaFunctionMap.clear();
      platform = new J2SEPlatform();
      if (env != null) {
         s_wiping.clear();
         wipeRecurse(env);
      }

      env = platform.newEnvironment();
      converterManager = new KahluaConverterManager();
      if (thread != null) {
         thread.bReset = true;
      }

      thread = new KahluaThread(platform, env);
      debugthread = new KahluaThread(platform, env);
      UIManager.defaultthread = thread;
      caller = new LuaCaller(converterManager);
      debugcaller = new LuaCaller(converterManager);
      if (exposer != null) {
         exposer.destroy();
      }

      exposer = new LuaManager.Exposer(converterManager, platform, env);
      loaded = new ArrayList();
      checksumDone = false;
      GameClient.checksum = "";
      GameClient.checksumValid = false;
      KahluaNumberConverter.install(converterManager);
      LuaEventManager.register(platform, env);
      LuaHookManager.register(platform, env);
      if (CoopMaster.instance != null) {
         CoopMaster.instance.register(platform, env);
      }

      if (VoiceManager.instance != null) {
         VoiceManager.instance.LuaRegister(platform, env);
      }

      KahluaTable var0 = env;
      exposer.exposeAll();
      exposer.TypeMap.put("function", LuaClosure.class);
      exposer.TypeMap.put("table", KahluaTable.class);
      outputTable(env, 0);
   }

   public static void LoadDir(String var0) throws URISyntaxException {
   }

   public static void LoadDirBase(String var0) throws Exception {
      LoadDirBase(var0, false);
   }

   public static void LoadDirBase(String var0, boolean var1) throws Exception {
      String var2 = "media/lua/" + var0 + "/";
      File var3 = ZomboidFileSystem.instance.getMediaFile("lua" + File.separator + var0);
      if (!paths.contains(var2)) {
         paths.add(var2);
      }

      try {
         searchFolders(ZomboidFileSystem.instance.baseURI, var3);
      } catch (IOException var10) {
         ExceptionLogger.logException(var10);
      }

      ArrayList var11 = loadList;
      loadList = new ArrayList();
      ArrayList var12 = ZomboidFileSystem.instance.getModIDs();

      for(int var4 = 0; var4 < var12.size(); ++var4) {
         String var5 = ZomboidFileSystem.instance.getModDir((String)var12.get(var4));
         if (var5 != null) {
            URI var6 = (new File(var5)).getCanonicalFile().toURI();
            File var7 = new File(var5 + File.separator + "media" + File.separator + "lua" + File.separator + var0);

            try {
               searchFolders(var6, var7);
            } catch (IOException var9) {
               ExceptionLogger.logException(var9);
            }
         }
      }

      Collections.sort(var11);
      Collections.sort(loadList);
      var11.addAll(loadList);
      loadList.clear();
      loadList = var11;
      HashSet var13 = new HashSet();
      Iterator var14 = loadList.iterator();

      while(true) {
         String var15;
         String var16;
         do {
            do {
               do {
                  do {
                     if (!var14.hasNext()) {
                        loadList.clear();
                        return;
                     }

                     var15 = (String)var14.next();
                  } while(var13.contains(var15));

                  var13.add(var15);
                  var16 = ZomboidFileSystem.instance.getAbsolutePath(var15);
                  if (var16 == null) {
                     throw new IllegalStateException("couldn't find \"" + var15 + "\"");
                  }

                  if (!var1) {
                     RunLua(var16);
                  }
               } while(checksumDone);
            } while(var15.contains("SandboxVars.lua"));
         } while(!GameServer.bServer && !GameClient.bClient);

         NetChecksum.checksummer.addFile(var15, var16);
      }
   }

   public static void initChecksum() throws Exception {
      if (!checksumDone) {
         if (GameClient.bClient || GameServer.bServer) {
            NetChecksum.checksummer.reset(false);
         }

      }
   }

   public static void finishChecksum() {
      if (GameServer.bServer) {
         GameServer.checksum = NetChecksum.checksummer.checksumToString();
         DebugLog.General.println("luaChecksum: " + GameServer.checksum);
      } else {
         if (!GameClient.bClient) {
            return;
         }

         GameClient.checksum = NetChecksum.checksummer.checksumToString();
      }

      NetChecksum.GroupOfFiles.finishChecksum();
      checksumDone = true;
   }

   public static void LoadDirBase() throws Exception {
      initChecksum();
      LoadDirBase("shared");
      LoadDirBase("client");
   }

   public static void searchFolders(URI var0, File var1) throws IOException {
      if (var1.isDirectory()) {
         String[] var2 = var1.list();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var10003 = var1.getCanonicalFile().getAbsolutePath();
            searchFolders(var0, new File(var10003 + File.separator + var2[var3]));
         }
      } else if (var1.getAbsolutePath().toLowerCase().endsWith(".lua")) {
         loadList.add(ZomboidFileSystem.instance.getRelativeFile(var0, var1.getAbsolutePath()));
      }

   }

   public static String getLuaCacheDir() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var0 = var10000 + File.separator + "Lua";
      File var1 = new File(var0);
      if (!var1.exists()) {
         var1.mkdir();
      }

      return var0;
   }

   public static String getSandboxCacheDir() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var0 = var10000 + File.separator + "Sandbox Presets";
      File var1 = new File(var0);
      if (!var1.exists()) {
         var1.mkdir();
      }

      return var0;
   }

   public static void fillContainer(ItemContainer var0, IsoPlayer var1) {
      ItemPickerJava.fillContainer(var0, var1);
   }

   public static void updateOverlaySprite(IsoObject var0) {
      ItemPickerJava.updateOverlaySprite(var0);
   }

   public static LuaClosure getDotDelimitedClosure(String var0) {
      String[] var1 = var0.split("\\.");
      KahluaTable var2 = env;

      for(int var3 = 0; var3 < var1.length - 1; ++var3) {
         var2 = (KahluaTable)env.rawget(var1[var3]);
      }

      return (LuaClosure)var2.rawget(var1[var1.length - 1]);
   }

   public static void transferItem(IsoGameCharacter var0, InventoryItem var1, ItemContainer var2, ItemContainer var3) {
      LuaClosure var4 = (LuaClosure)env.rawget("javaTransferItems");
      caller.pcall(thread, var4, (Object[])(var0, var1, var2, var3));
   }

   public static void dropItem(InventoryItem var0) {
      LuaClosure var1 = getDotDelimitedClosure("ISInventoryPaneContextMenu.dropItem");
      caller.pcall(thread, var1, (Object)var0);
   }

   public static IsoGridSquare AdjacentFreeTileFinder(IsoGridSquare var0, IsoPlayer var1) {
      KahluaTable var2 = (KahluaTable)env.rawget("AdjacentFreeTileFinder");
      LuaClosure var3 = (LuaClosure)var2.rawget("Find");
      return (IsoGridSquare)caller.pcall(thread, var3, (Object[])(var0, var1))[1];
   }

   public static Object RunLua(String var0) {
      return RunLua(var0, false);
   }

   public static Object RunLua(String var0, boolean var1) {
      String var2 = var0.replace("\\", "/");
      if (loading.contains(var2)) {
         DebugLog.Lua.warn("recursive require(): %s", var2);
         return null;
      } else {
         loading.add(var2);

         Object var3;
         try {
            var3 = RunLuaInternal(var0, var1);
         } finally {
            loading.remove(var2);
         }

         return var3;
      }
   }

   private static Object RunLuaInternal(String var0, boolean var1) {
      var0 = var0.replace("\\", "/");
      if (loaded.contains(var0)) {
         return loadedReturn.get(var0);
      } else {
         FuncState.currentFile = var0.substring(var0.lastIndexOf(47) + 1);
         FuncState.currentfullFile = var0;
         String var2 = var0;
         var0 = ZomboidFileSystem.instance.getString(var0.replace("\\", "/"));
         if (DebugLog.isEnabled(DebugType.Lua)) {
            DebugLog.Lua.println("Loading: " + ZomboidFileSystem.instance.getRelativeFile(var0));
         }

         InputStreamReader var3;
         try {
            var3 = IndieFileLoader.getStreamReader(var0);
         } catch (FileNotFoundException var11) {
            ExceptionLogger.logException(var11);
            return null;
         }

         LuaCompiler.rewriteEvents = var1;

         LuaClosure var4;
         try {
            BufferedReader var5 = new BufferedReader(var3);

            try {
               var4 = LuaCompiler.loadis((Reader)var5, var0.substring(var0.lastIndexOf(47) + 1), env);
            } catch (Throwable var9) {
               try {
                  var5.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var5.close();
         } catch (Exception var10) {
            Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, "Error found in LUA file: " + var0, (Object)null);
            ExceptionLogger.logException(var10);
            thread.debugException(var10);
            return null;
         }

         luaFunctionMap.clear();
         AttachedWeaponDefinitions.instance.m_dirty = true;
         DefaultClothing.instance.m_dirty = true;
         HairOutfitDefinitions.instance.m_dirty = true;
         ZombiesZoneDefinition.bDirty = true;
         LuaReturn var12 = caller.protectedCall(thread, var4);
         if (!var12.isSuccess()) {
            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getErrorString(), (Object)null);
            if (var12.getJavaException() != null) {
               Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getJavaException().toString(), (Object)null);
            }

            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getLuaStackTrace(), (Object)null);
         }

         loaded.add(var2);
         Object var6 = var12.isSuccess() && var12.size() > 0 ? var12.getFirst() : null;
         if (var6 != null) {
            loadedReturn.put(var2, var6);
         } else {
            loadedReturn.remove(var2);
         }

         LuaCompiler.rewriteEvents = false;
         return var6;
      }
   }

   public static Object getFunctionObject(String var0) {
      Object var1 = luaFunctionMap.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         KahluaTable var2 = env;
         if (var0.contains(".")) {
            String[] var3 = var0.split("\\.");

            for(int var4 = 0; var4 < var3.length - 1; ++var4) {
               KahluaTable var5 = (KahluaTable)Type.tryCastTo(var2.rawget(var3[var4]), KahluaTable.class);
               if (var5 == null) {
                  DebugLog.General.error("no such function \"%s\"", var0);
                  return null;
               }

               var2 = var5;
            }

            var1 = var2.rawget(var3[var3.length - 1]);
         } else {
            var1 = var2.rawget(var0);
         }

         if (!(var1 instanceof JavaFunction) && !(var1 instanceof LuaClosure)) {
            DebugLog.General.error("no such function \"%s\"", var0);
            return null;
         } else {
            luaFunctionMap.put(var0, var1);
            return var1;
         }
      }
   }

   public static void Test() throws IOException {
   }

   public static Object get(Object var0) {
      return env.rawget(var0);
   }

   public static void call(String var0, Object var1) {
      caller.pcall(thread, env.rawget(var0), var1);
   }

   private static void exposeKeyboardKeys(KahluaTable var0) {
      Object var1 = var0.rawget("Keyboard");
      if (var1 instanceof KahluaTable) {
         KahluaTable var2 = (KahluaTable)var1;
         Field[] var3 = Keyboard.class.getFields();

         try {
            Field[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Field var7 = var4[var6];
               if (Modifier.isStatic(var7.getModifiers()) && Modifier.isPublic(var7.getModifiers()) && Modifier.isFinal(var7.getModifiers()) && var7.getType().equals(Integer.TYPE) && var7.getName().startsWith("KEY_") && !var7.getName().endsWith("WIN")) {
                  var2.rawset(var7.getName(), (double)var7.getInt((Object)null));
               }
            }
         } catch (Exception var8) {
         }

      }
   }

   private static void exposeLuaCalendar() {
      KahluaTable var0 = (KahluaTable)env.rawget("PZCalendar");
      if (var0 != null) {
         Field[] var1 = Calendar.class.getFields();

         try {
            Field[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Field var5 = var2[var4];
               if (Modifier.isStatic(var5.getModifiers()) && Modifier.isPublic(var5.getModifiers()) && Modifier.isFinal(var5.getModifiers()) && var5.getType().equals(Integer.TYPE)) {
                  var0.rawset(var5.getName(), BoxedStaticValues.toDouble((double)var5.getInt((Object)null)));
               }
            }
         } catch (Exception var6) {
         }

         env.rawset("Calendar", var0);
      }
   }

   public static String getHourMinuteJava() {
      Calendar var10000 = Calendar.getInstance();
      String var0 = var10000.get(12).makeConcatWithConstants<invokedynamic>(var10000.get(12));
      if (Calendar.getInstance().get(12) < 10) {
         var0 = "0" + var0;
      }

      int var1 = Calendar.getInstance().get(11);
      return var1 + ":" + var0;
   }

   public static KahluaTable copyTable(KahluaTable var0) {
      return copyTable((KahluaTable)null, var0);
   }

   public static KahluaTable copyTable(KahluaTable var0, KahluaTable var1) {
      if (var0 == null) {
         var0 = platform.newTable();
      } else {
         var0.wipe();
      }

      if (var1 != null && !var1.isEmpty()) {
         KahluaTableIterator var2 = var1.iterator();

         while(var2.advance()) {
            Object var3 = var2.getKey();
            Object var4 = var2.getValue();
            if (var4 instanceof KahluaTable) {
               var0.rawset(var3, copyTable((KahluaTable)null, (KahluaTable)var4));
            } else {
               var0.rawset(var3, var4);
            }
         }

         return var0;
      } else {
         return var0;
      }
   }

   static {
      caller = new LuaCaller(converterManager);
      debugcaller = new LuaCaller(converterManager);
      loaded = new ArrayList();
      loading = new HashSet();
      loadedReturn = new HashMap();
      checksumDone = false;
      loadList = new ArrayList();
      paths = new ArrayList();
      luaFunctionMap = new HashMap();
      s_wiping = new HashSet();
   }

   public static final class Exposer extends LuaJavaClassExposer {
      private final HashSet exposed = new HashSet();

      public Exposer(KahluaConverterManager var1, Platform var2, KahluaTable var3) {
         super(var1, var2, var3);
      }

      public void exposeAll() {
         this.setExposed(BufferedReader.class);
         this.setExposed(BufferedWriter.class);
         this.setExposed(DataInputStream.class);
         this.setExposed(DataOutputStream.class);
         this.setExposed(Double.class);
         this.setExposed(Long.class);
         this.setExposed(Float.class);
         this.setExposed(Integer.class);
         this.setExposed(Math.class);
         this.setExposed(Void.class);
         this.setExposed(SimpleDateFormat.class);
         this.setExposed(ArrayList.class);
         this.setExposed(EnumMap.class);
         this.setExposed(HashMap.class);
         this.setExposed(LinkedList.class);
         this.setExposed(Stack.class);
         this.setExposed(Vector.class);
         this.setExposed(Iterator.class);
         this.setExposed(EmitterType.class);
         this.setExposed(FMODAudio.class);
         this.setExposed(FMODSoundBank.class);
         this.setExposed(FMODSoundEmitter.class);
         this.setExposed(Vector2f.class);
         this.setExposed(Vector3f.class);
         this.setExposed(KahluaUtil.class);
         this.setExposed(DummySoundBank.class);
         this.setExposed(DummySoundEmitter.class);
         this.setExposed(BaseSoundEmitter.class);
         this.setExposed(GameSound.class);
         this.setExposed(GameSoundClip.class);
         this.setExposed(AttackState.class);
         this.setExposed(BurntToDeath.class);
         this.setExposed(ClimbDownSheetRopeState.class);
         this.setExposed(ClimbOverFenceState.class);
         this.setExposed(ClimbOverWallState.class);
         this.setExposed(ClimbSheetRopeState.class);
         this.setExposed(ClimbThroughWindowState.class);
         this.setExposed(CloseWindowState.class);
         this.setExposed(CrawlingZombieTurnState.class);
         this.setExposed(FakeDeadAttackState.class);
         this.setExposed(FakeDeadZombieState.class);
         this.setExposed(FishingState.class);
         this.setExposed(FitnessState.class);
         this.setExposed(IdleState.class);
         this.setExposed(LungeState.class);
         this.setExposed(OpenWindowState.class);
         this.setExposed(PathFindState.class);
         this.setExposed(PlayerActionsState.class);
         this.setExposed(PlayerAimState.class);
         this.setExposed(PlayerEmoteState.class);
         this.setExposed(PlayerExtState.class);
         this.setExposed(PlayerFallDownState.class);
         this.setExposed(PlayerFallingState.class);
         this.setExposed(PlayerGetUpState.class);
         this.setExposed(PlayerHitReactionPVPState.class);
         this.setExposed(PlayerHitReactionState.class);
         this.setExposed(PlayerKnockedDown.class);
         this.setExposed(PlayerOnGroundState.class);
         this.setExposed(PlayerSitOnGroundState.class);
         this.setExposed(PlayerStrafeState.class);
         this.setExposed(SmashWindowState.class);
         this.setExposed(StaggerBackState.class);
         this.setExposed(SwipeStatePlayer.class);
         this.setExposed(ThumpState.class);
         this.setExposed(WalkTowardState.class);
         this.setExposed(ZombieFallDownState.class);
         this.setExposed(ZombieGetDownState.class);
         this.setExposed(ZombieGetUpState.class);
         this.setExposed(ZombieIdleState.class);
         this.setExposed(ZombieOnGroundState.class);
         this.setExposed(ZombieReanimateState.class);
         this.setExposed(ZombieSittingState.class);
         this.setExposed(GameCharacterAIBrain.class);
         this.setExposed(MapKnowledge.class);
         this.setExposed(BodyPartType.class);
         this.setExposed(BodyPart.class);
         this.setExposed(BodyDamage.class);
         this.setExposed(Thermoregulator.class);
         this.setExposed(Thermoregulator.ThermalNode.class);
         this.setExposed(Metabolics.class);
         this.setExposed(Fitness.class);
         this.setExposed(GameKeyboard.class);
         this.setExposed(LuaTimedAction.class);
         this.setExposed(LuaTimedActionNew.class);
         this.setExposed(Moodle.class);
         this.setExposed(Moodles.class);
         this.setExposed(MoodleType.class);
         this.setExposed(ProfessionFactory.class);
         this.setExposed(ProfessionFactory.Profession.class);
         this.setExposed(PerkFactory.class);
         this.setExposed(PerkFactory.Perk.class);
         this.setExposed(PerkFactory.Perks.class);
         this.setExposed(ObservationFactory.class);
         this.setExposed(ObservationFactory.Observation.class);
         this.setExposed(TraitFactory.class);
         this.setExposed(TraitFactory.Trait.class);
         this.setExposed(IsoDummyCameraCharacter.class);
         this.setExposed(Stats.class);
         this.setExposed(SurvivorDesc.class);
         this.setExposed(SurvivorFactory.class);
         this.setExposed(SurvivorFactory.SurvivorType.class);
         this.setExposed(IsoGameCharacter.class);
         this.setExposed(IsoGameCharacter.PerkInfo.class);
         this.setExposed(IsoGameCharacter.XP.class);
         this.setExposed(IsoGameCharacter.CharacterTraits.class);
         this.setExposed(TraitCollection.TraitSlot.class);
         this.setExposed(TraitCollection.class);
         this.setExposed(IsoPlayer.class);
         this.setExposed(IsoSurvivor.class);
         this.setExposed(IsoZombie.class);
         this.setExposed(CharacterActionAnims.class);
         this.setExposed(HaloTextHelper.class);
         this.setExposed(HaloTextHelper.ColorRGB.class);
         this.setExposed(NetworkAIParams.class);
         this.setExposed(BloodBodyPartType.class);
         this.setExposed(Clipboard.class);
         this.setExposed(AngelCodeFont.class);
         this.setExposed(ZLogger.class);
         this.setExposed(PropertyContainer.class);
         this.setExposed(ClothingItem.class);
         this.setExposed(AnimatorDebugMonitor.class);
         this.setExposed(ColorInfo.class);
         this.setExposed(Texture.class);
         this.setExposed(SteamFriend.class);
         this.setExposed(SteamUGCDetails.class);
         this.setExposed(SteamWorkshopItem.class);
         this.setExposed(Color.class);
         this.setExposed(Colors.class);
         this.setExposed(Core.class);
         this.setExposed(GameVersion.class);
         this.setExposed(ImmutableColor.class);
         this.setExposed(Language.class);
         this.setExposed(PerformanceSettings.class);
         this.setExposed(SpriteRenderer.class);
         this.setExposed(Translator.class);
         this.setExposed(PZMath.class);
         this.setExposed(DebugLog.class);
         this.setExposed(DebugOptions.class);
         this.setExposed(BooleanDebugOption.class);
         this.setExposed(DebugType.class);
         this.setExposed(ErosionConfig.class);
         this.setExposed(ErosionConfig.Debug.class);
         this.setExposed(ErosionConfig.Season.class);
         this.setExposed(ErosionConfig.Seeds.class);
         this.setExposed(ErosionConfig.Time.class);
         this.setExposed(ErosionMain.class);
         this.setExposed(ErosionSeason.class);
         this.setExposed(AnimationViewerState.class);
         this.setExposed(AnimationViewerState.BooleanDebugOption.class);
         this.setExposed(AttachmentEditorState.class);
         this.setExposed(ChooseGameInfo.Mod.class);
         this.setExposed(DebugChunkState.class);
         this.setExposed(DebugChunkState.BooleanDebugOption.class);
         this.setExposed(DebugGlobalObjectState.class);
         this.setExposed(GameLoadingState.class);
         this.setExposed(MainScreenState.class);
         this.setExposed(CGlobalObject.class);
         this.setExposed(CGlobalObjects.class);
         this.setExposed(CGlobalObjectSystem.class);
         this.setExposed(SGlobalObject.class);
         this.setExposed(SGlobalObjects.class);
         this.setExposed(SGlobalObjectSystem.class);
         this.setExposed(Mouse.class);
         this.setExposed(AlarmClock.class);
         this.setExposed(AlarmClockClothing.class);
         this.setExposed(Clothing.class);
         this.setExposed(Clothing.ClothingPatch.class);
         this.setExposed(Clothing.ClothingPatchFabricType.class);
         this.setExposed(ComboItem.class);
         this.setExposed(Drainable.class);
         this.setExposed(DrainableComboItem.class);
         this.setExposed(Food.class);
         this.setExposed(HandWeapon.class);
         this.setExposed(InventoryContainer.class);
         this.setExposed(Key.class);
         this.setExposed(KeyRing.class);
         this.setExposed(Literature.class);
         this.setExposed(MapItem.class);
         this.setExposed(Moveable.class);
         this.setExposed(Radio.class);
         this.setExposed(WeaponPart.class);
         this.setExposed(ItemContainer.class);
         this.setExposed(ItemPickerJava.class);
         this.setExposed(InventoryItem.class);
         this.setExposed(InventoryItemFactory.class);
         this.setExposed(FixingManager.class);
         this.setExposed(RecipeManager.class);
         this.setExposed(IsoRegions.class);
         this.setExposed(IsoRegionsLogger.class);
         this.setExposed(IsoRegionsLogger.IsoRegionLog.class);
         this.setExposed(IsoRegionLogType.class);
         this.setExposed(DataCell.class);
         this.setExposed(DataChunk.class);
         this.setExposed(IsoChunkRegion.class);
         this.setExposed(IsoWorldRegion.class);
         this.setExposed(IsoRegionsRenderer.class);
         this.setExposed(IsoRegionsRenderer.BooleanDebugOption.class);
         this.setExposed(IsoBuilding.class);
         this.setExposed(IsoRoom.class);
         this.setExposed(SafeHouse.class);
         this.setExposed(BarricadeAble.class);
         this.setExposed(IsoBarbecue.class);
         this.setExposed(IsoBarricade.class);
         this.setExposed(IsoBrokenGlass.class);
         this.setExposed(IsoClothingDryer.class);
         this.setExposed(IsoClothingWasher.class);
         this.setExposed(IsoCurtain.class);
         this.setExposed(IsoCarBatteryCharger.class);
         this.setExposed(IsoDeadBody.class);
         this.setExposed(IsoDoor.class);
         this.setExposed(IsoFire.class);
         this.setExposed(IsoFireManager.class);
         this.setExposed(IsoFireplace.class);
         this.setExposed(IsoGenerator.class);
         this.setExposed(IsoJukebox.class);
         this.setExposed(IsoLightSwitch.class);
         this.setExposed(IsoMannequin.class);
         this.setExposed(IsoMolotovCocktail.class);
         this.setExposed(IsoWaveSignal.class);
         this.setExposed(IsoRadio.class);
         this.setExposed(IsoTelevision.class);
         this.setExposed(IsoStove.class);
         this.setExposed(IsoThumpable.class);
         this.setExposed(IsoTrap.class);
         this.setExposed(IsoTree.class);
         this.setExposed(IsoWheelieBin.class);
         this.setExposed(IsoWindow.class);
         this.setExposed(IsoWindowFrame.class);
         this.setExposed(IsoWorldInventoryObject.class);
         this.setExposed(IsoZombieGiblets.class);
         this.setExposed(RainManager.class);
         this.setExposed(ObjectRenderEffects.class);
         this.setExposed(HumanVisual.class);
         this.setExposed(ItemVisual.class);
         this.setExposed(ItemVisuals.class);
         this.setExposed(IsoSprite.class);
         this.setExposed(IsoSpriteInstance.class);
         this.setExposed(IsoSpriteManager.class);
         this.setExposed(IsoSpriteGrid.class);
         this.setExposed(IsoFlagType.class);
         this.setExposed(IsoObjectType.class);
         this.setExposed(ClimateManager.class);
         this.setExposed(ClimateManager.DayInfo.class);
         this.setExposed(ClimateManager.ClimateFloat.class);
         this.setExposed(ClimateManager.ClimateColor.class);
         this.setExposed(ClimateManager.ClimateBool.class);
         this.setExposed(WeatherPeriod.class);
         this.setExposed(WeatherPeriod.WeatherStage.class);
         this.setExposed(WeatherPeriod.StrLerpVal.class);
         this.setExposed(ClimateManager.AirFront.class);
         this.setExposed(ThunderStorm.class);
         this.setExposed(ThunderStorm.ThunderCloud.class);
         this.setExposed(IsoWeatherFX.class);
         this.setExposed(Temperature.class);
         this.setExposed(ClimateColorInfo.class);
         this.setExposed(ClimateValues.class);
         this.setExposed(ClimateForecaster.class);
         this.setExposed(ClimateForecaster.DayForecast.class);
         this.setExposed(ClimateForecaster.ForecastValue.class);
         this.setExposed(ClimateHistory.class);
         this.setExposed(WorldFlares.class);
         this.setExposed(WorldFlares.Flare.class);
         this.setExposed(ImprovedFog.class);
         this.setExposed(ClimateMoon.class);
         this.setExposed(IsoPuddles.class);
         this.setExposed(IsoPuddles.PuddlesFloat.class);
         this.setExposed(BentFences.class);
         this.setExposed(BrokenFences.class);
         this.setExposed(ContainerOverlays.class);
         this.setExposed(IsoChunk.class);
         this.setExposed(BuildingDef.class);
         this.setExposed(IsoCamera.class);
         this.setExposed(IsoCell.class);
         this.setExposed(IsoChunkMap.class);
         this.setExposed(IsoDirections.class);
         this.setExposed(IsoDirectionSet.class);
         this.setExposed(IsoGridSquare.class);
         this.setExposed(IsoHeatSource.class);
         this.setExposed(IsoLightSource.class);
         this.setExposed(IsoLot.class);
         this.setExposed(IsoLuaMover.class);
         this.setExposed(IsoMetaChunk.class);
         this.setExposed(IsoMetaCell.class);
         this.setExposed(IsoMetaGrid.class);
         this.setExposed(IsoMetaGrid.Trigger.class);
         this.setExposed(IsoMetaGrid.VehicleZone.class);
         this.setExposed(IsoMetaGrid.Zone.class);
         this.setExposed(IsoMovingObject.class);
         this.setExposed(IsoObject.class);
         this.setExposed(IsoObjectPicker.class);
         this.setExposed(IsoPushableObject.class);
         this.setExposed(IsoUtils.class);
         this.setExposed(IsoWorld.class);
         this.setExposed(LosUtil.class);
         this.setExposed(MetaObject.class);
         this.setExposed(RoomDef.class);
         this.setExposed(SliceY.class);
         this.setExposed(TileOverlays.class);
         this.setExposed(Vector2.class);
         this.setExposed(Vector3.class);
         this.setExposed(WorldMarkers.class);
         this.setExposed(WorldMarkers.DirectionArrow.class);
         this.setExposed(WorldMarkers.GridSquareMarker.class);
         this.setExposed(WorldMarkers.PlayerHomingPoint.class);
         this.setExposed(SearchMode.class);
         this.setExposed(SearchMode.PlayerSearchMode.class);
         this.setExposed(SearchMode.SearchModeFloat.class);
         this.setExposed(IsoMarkers.class);
         this.setExposed(IsoMarkers.IsoMarker.class);
         this.setExposed(LuaEventManager.class);
         this.setExposed(MapObjects.class);
         this.setExposed(ActiveMods.class);
         this.setExposed(Server.class);
         this.setExposed(ServerOptions.class);
         this.setExposed(ServerOptions.BooleanServerOption.class);
         this.setExposed(ServerOptions.DoubleServerOption.class);
         this.setExposed(ServerOptions.IntegerServerOption.class);
         this.setExposed(ServerOptions.StringServerOption.class);
         this.setExposed(ServerOptions.TextServerOption.class);
         this.setExposed(ServerSettings.class);
         this.setExposed(ServerSettingsManager.class);
         this.setExposed(ZombiePopulationRenderer.class);
         this.setExposed(ZombiePopulationRenderer.BooleanDebugOption.class);
         this.setExposed(RadioAPI.class);
         this.setExposed(DeviceData.class);
         this.setExposed(DevicePresets.class);
         this.setExposed(PresetEntry.class);
         this.setExposed(ZomboidRadio.class);
         this.setExposed(RadioData.class);
         this.setExposed(RadioScriptManager.class);
         this.setExposed(DynamicRadioChannel.class);
         this.setExposed(RadioChannel.class);
         this.setExposed(RadioBroadCast.class);
         this.setExposed(RadioLine.class);
         this.setExposed(RadioScript.class);
         this.setExposed(RadioScript.ExitOption.class);
         this.setExposed(ChannelCategory.class);
         this.setExposed(SLSoundManager.class);
         this.setExposed(StorySound.class);
         this.setExposed(StorySoundEvent.class);
         this.setExposed(EventSound.class);
         this.setExposed(DataPoint.class);
         this.setExposed(RecordedMedia.class);
         this.setExposed(MediaData.class);
         this.setExposed(EvolvedRecipe.class);
         this.setExposed(Fixing.class);
         this.setExposed(Fixing.Fixer.class);
         this.setExposed(Fixing.FixerSkill.class);
         this.setExposed(GameSoundScript.class);
         this.setExposed(Item.class);
         this.setExposed(Item.Type.class);
         this.setExposed(ItemRecipe.class);
         this.setExposed(ModelAttachment.class);
         this.setExposed(ModelScript.class);
         this.setExposed(MovableRecipe.class);
         this.setExposed(Recipe.class);
         this.setExposed(Recipe.RequiredSkill.class);
         this.setExposed(Recipe.Result.class);
         this.setExposed(Recipe.Source.class);
         this.setExposed(ScriptModule.class);
         this.setExposed(VehicleScript.class);
         this.setExposed(VehicleScript.Area.class);
         this.setExposed(VehicleScript.Model.class);
         this.setExposed(VehicleScript.Part.class);
         this.setExposed(VehicleScript.Passenger.class);
         this.setExposed(VehicleScript.PhysicsShape.class);
         this.setExposed(VehicleScript.Position.class);
         this.setExposed(VehicleScript.Wheel.class);
         this.setExposed(ScriptManager.class);
         this.setExposed(ActionProgressBar.class);
         this.setExposed(Clock.class);
         this.setExposed(UIDebugConsole.class);
         this.setExposed(ModalDialog.class);
         this.setExposed(MoodlesUI.class);
         this.setExposed(NewHealthPanel.class);
         this.setExposed(ObjectTooltip.class);
         this.setExposed(ObjectTooltip.Layout.class);
         this.setExposed(ObjectTooltip.LayoutItem.class);
         this.setExposed(RadarPanel.class);
         this.setExposed(RadialMenu.class);
         this.setExposed(RadialProgressBar.class);
         this.setExposed(SpeedControls.class);
         this.setExposed(TextManager.class);
         this.setExposed(UI3DModel.class);
         this.setExposed(UIElement.class);
         this.setExposed(UIFont.class);
         this.setExposed(UITransition.class);
         this.setExposed(UIManager.class);
         this.setExposed(UIServerToolbox.class);
         this.setExposed(UITextBox2.class);
         this.setExposed(VehicleGauge.class);
         this.setExposed(TextDrawObject.class);
         this.setExposed(PZArrayList.class);
         this.setExposed(PZCalendar.class);
         this.setExposed(BaseVehicle.class);
         this.setExposed(EditVehicleState.class);
         this.setExposed(PathFindBehavior2.BehaviorResult.class);
         this.setExposed(PathFindBehavior2.class);
         this.setExposed(PathFindState2.class);
         this.setExposed(UI3DScene.class);
         this.setExposed(VehicleDoor.class);
         this.setExposed(VehicleLight.class);
         this.setExposed(VehiclePart.class);
         this.setExposed(VehicleType.class);
         this.setExposed(VehicleWindow.class);
         this.setExposed(AttachedItem.class);
         this.setExposed(AttachedItems.class);
         this.setExposed(AttachedLocation.class);
         this.setExposed(AttachedLocationGroup.class);
         this.setExposed(AttachedLocations.class);
         this.setExposed(WornItems.class);
         this.setExposed(WornItem.class);
         this.setExposed(BodyLocation.class);
         this.setExposed(BodyLocationGroup.class);
         this.setExposed(BodyLocations.class);
         this.setExposed(DummySoundManager.class);
         this.setExposed(GameSounds.class);
         this.setExposed(GameTime.class);
         this.setExposed(GameWindow.class);
         this.setExposed(SandboxOptions.class);
         this.setExposed(SandboxOptions.BooleanSandboxOption.class);
         this.setExposed(SandboxOptions.DoubleSandboxOption.class);
         this.setExposed(SandboxOptions.StringSandboxOption.class);
         this.setExposed(SandboxOptions.EnumSandboxOption.class);
         this.setExposed(SandboxOptions.IntegerSandboxOption.class);
         this.setExposed(SoundManager.class);
         this.setExposed(SystemDisabler.class);
         this.setExposed(VirtualZombieManager.class);
         this.setExposed(WorldSoundManager.class);
         this.setExposed(WorldSoundManager.WorldSound.class);
         this.setExposed(DummyCharacterSoundEmitter.class);
         this.setExposed(CharacterSoundEmitter.class);
         this.setExposed(SoundManager.AmbientSoundEffect.class);
         this.setExposed(BaseAmbientStreamManager.class);
         this.setExposed(AmbientStreamManager.class);
         this.setExposed(Nutrition.class);
         this.setExposed(BSFurnace.class);
         this.setExposed(MultiStageBuilding.class);
         this.setExposed(MultiStageBuilding.Stage.class);
         this.setExposed(SleepingEvent.class);
         this.setExposed(IsoCompost.class);
         this.setExposed(Userlog.class);
         this.setExposed(Userlog.UserlogType.class);
         this.setExposed(ConfigOption.class);
         this.setExposed(BooleanConfigOption.class);
         this.setExposed(DoubleConfigOption.class);
         this.setExposed(EnumConfigOption.class);
         this.setExposed(IntegerConfigOption.class);
         this.setExposed(StringConfigOption.class);
         this.setExposed(Faction.class);
         this.setExposed(LuaManager.GlobalObject.LuaFileWriter.class);
         this.setExposed(Keyboard.class);
         this.setExposed(DBResult.class);
         this.setExposed(NonPvpZone.class);
         this.setExposed(DBTicket.class);
         this.setExposed(StashSystem.class);
         this.setExposed(StashBuilding.class);
         this.setExposed(Stash.class);
         this.setExposed(ItemType.class);
         this.setExposed(RandomizedWorldBase.class);
         this.setExposed(RandomizedBuildingBase.class);
         this.setExposed(RBBurntFireman.class);
         this.setExposed(RBBasic.class);
         this.setExposed(RBBurnt.class);
         this.setExposed(RBOther.class);
         this.setExposed(RBStripclub.class);
         this.setExposed(RBSchool.class);
         this.setExposed(RBSpiffo.class);
         this.setExposed(RBPizzaWhirled.class);
         this.setExposed(RBOffice.class);
         this.setExposed(RBHairSalon.class);
         this.setExposed(RBClinic.class);
         this.setExposed(RBPileOCrepe.class);
         this.setExposed(RBCafe.class);
         this.setExposed(RBBar.class);
         this.setExposed(RBLooted.class);
         this.setExposed(RBSafehouse.class);
         this.setExposed(RBBurntCorpse.class);
         this.setExposed(RBShopLooted.class);
         this.setExposed(RBKateAndBaldspot.class);
         this.setExposed(RandomizedDeadSurvivorBase.class);
         this.setExposed(RDSZombiesEating.class);
         this.setExposed(RDSBleach.class);
         this.setExposed(RDSDeadDrunk.class);
         this.setExposed(RDSGunmanInBathroom.class);
         this.setExposed(RDSGunslinger.class);
         this.setExposed(RDSZombieLockedBathroom.class);
         this.setExposed(RDSBandPractice.class);
         this.setExposed(RDSBathroomZed.class);
         this.setExposed(RDSBedroomZed.class);
         this.setExposed(RDSFootballNight.class);
         this.setExposed(RDSHenDo.class);
         this.setExposed(RDSStagDo.class);
         this.setExposed(RDSStudentNight.class);
         this.setExposed(RDSPokerNight.class);
         this.setExposed(RDSSuicidePact.class);
         this.setExposed(RDSPrisonEscape.class);
         this.setExposed(RDSPrisonEscapeWithPolice.class);
         this.setExposed(RDSSkeletonPsycho.class);
         this.setExposed(RDSCorpsePsycho.class);
         this.setExposed(RDSSpecificProfession.class);
         this.setExposed(RDSPoliceAtHouse.class);
         this.setExposed(RDSHouseParty.class);
         this.setExposed(RDSTinFoilHat.class);
         this.setExposed(RDSHockeyPsycho.class);
         this.setExposed(RandomizedVehicleStoryBase.class);
         this.setExposed(RVSCarCrash.class);
         this.setExposed(RVSBanditRoad.class);
         this.setExposed(RVSAmbulanceCrash.class);
         this.setExposed(RVSCrashHorde.class);
         this.setExposed(RVSCarCrashCorpse.class);
         this.setExposed(RVSPoliceBlockade.class);
         this.setExposed(RVSPoliceBlockadeShooting.class);
         this.setExposed(RVSBurntCar.class);
         this.setExposed(RVSConstructionSite.class);
         this.setExposed(RVSUtilityVehicle.class);
         this.setExposed(RVSChangingTire.class);
         this.setExposed(RVSFlippedCrash.class);
         this.setExposed(RVSTrailerCrash.class);
         this.setExposed(RandomizedZoneStoryBase.class);
         this.setExposed(RZSForestCamp.class);
         this.setExposed(RZSForestCampEaten.class);
         this.setExposed(RZSBuryingCamp.class);
         this.setExposed(RZSBeachParty.class);
         this.setExposed(RZSFishingTrip.class);
         this.setExposed(RZSBBQParty.class);
         this.setExposed(RZSHunterCamp.class);
         this.setExposed(RZSSexyTime.class);
         this.setExposed(RZSTrapperCamp.class);
         this.setExposed(RZSBaseball.class);
         this.setExposed(RZSMusicFestStage.class);
         this.setExposed(RZSMusicFest.class);
         this.setExposed(MapGroups.class);
         this.setExposed(BeardStyles.class);
         this.setExposed(BeardStyle.class);
         this.setExposed(HairStyles.class);
         this.setExposed(HairStyle.class);
         this.setExposed(BloodClothingType.class);
         this.setExposed(WeaponType.class);
         this.setExposed(IsoWaterGeometry.class);
         this.setExposed(ModData.class);
         this.setExposed(WorldMarkers.class);
         this.setExposed(ChatMessage.class);
         this.setExposed(ChatBase.class);
         this.setExposed(ServerChatMessage.class);
         if (Core.bDebug) {
            this.setExposed(Field.class);
            this.setExposed(Method.class);
            this.setExposed(Coroutine.class);
         }

         UIWorldMap.setExposed(this);
         if (Core.bDebug) {
            try {
               this.exposeMethod(Class.class, Class.class.getMethod("getName"), LuaManager.env);
               this.exposeMethod(Class.class, Class.class.getMethod("getSimpleName"), LuaManager.env);
            } catch (NoSuchMethodException var3) {
               var3.printStackTrace();
            }
         }

         Iterator var1 = this.exposed.iterator();

         while(var1.hasNext()) {
            Class var2 = (Class)var1.next();
            this.exposeLikeJavaRecursively(var2, LuaManager.env);
         }

         this.exposeGlobalFunctions(new LuaManager.GlobalObject());
         LuaManager.exposeKeyboardKeys(LuaManager.env);
         LuaManager.exposeLuaCalendar();
      }

      public void setExposed(Class var1) {
         this.exposed.add(var1);
      }

      public boolean shouldExpose(Class var1) {
         return var1 == null ? false : this.exposed.contains(var1);
      }
   }

   public static class GlobalObject {
      static FileOutputStream outStream;
      static FileInputStream inStream;
      static FileReader inFileReader = null;
      static BufferedReader inBufferedReader = null;
      static long timeLastRefresh = 0L;
      private static final LuaManager.GlobalObject.TimSortComparator timSortComparator = new LuaManager.GlobalObject.TimSortComparator();

      @LuaMethod(
         name = "loadVehicleModel",
         global = true
      )
      public static Model loadVehicleModel(String var0, String var1, String var2) {
         return loadZomboidModel(var0, var1, var2, "vehicle", true);
      }

      @LuaMethod(
         name = "loadStaticZomboidModel",
         global = true
      )
      public static Model loadStaticZomboidModel(String var0, String var1, String var2) {
         return loadZomboidModel(var0, var1, var2, (String)null, true);
      }

      @LuaMethod(
         name = "loadSkinnedZomboidModel",
         global = true
      )
      public static Model loadSkinnedZomboidModel(String var0, String var1, String var2) {
         return loadZomboidModel(var0, var1, var2, (String)null, false);
      }

      @LuaMethod(
         name = "loadZomboidModel",
         global = true
      )
      public static Model loadZomboidModel(String var0, String var1, String var2, String var3, boolean var4) {
         try {
            if (var1.startsWith("/")) {
               var1 = var1.substring(1);
            }

            if (var2.startsWith("/")) {
               var2 = var2.substring(1);
            }

            if (StringUtils.isNullOrWhitespace(var3)) {
               var3 = "basicEffect";
            }

            if ("vehicle".equals(var3) && !Core.getInstance().getPerfReflectionsOnLoad()) {
               var3 = var3 + "_noreflect";
            }

            Model var5 = ModelManager.instance.tryGetLoadedModel(var1, var2, var4, var3, false);
            if (var5 != null) {
               return var5;
            } else {
               ModelManager.instance.setModelMetaData(var0, var1, var2, var3, var4);
               Model.ModelAssetParams var6 = new Model.ModelAssetParams();
               var6.bStatic = var4;
               var6.meshName = var1;
               var6.shaderName = var3;
               var6.textureName = var2;
               var6.textureFlags = ModelManager.instance.getTextureFlags();
               var5 = (Model)ModelAssetManager.instance.load(new AssetPath(var0), var6);
               if (var5 != null) {
                  ModelManager.instance.putLoadedModel(var1, var2, var4, var3, var5);
               }

               return var5;
            }
         } catch (Exception var7) {
            DebugLog.General.error("LuaManager.loadZomboidModel> Exception thrown loading model: " + var0 + " mesh:" + var1 + " tex:" + var2 + " shader:" + var3 + " isStatic:" + var4);
            var7.printStackTrace();
            return null;
         }
      }

      @LuaMethod(
         name = "setModelMetaData",
         global = true
      )
      public static void setModelMetaData(String var0, String var1, String var2, String var3, boolean var4) {
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         if (var2.startsWith("/")) {
            var2 = var2.substring(1);
         }

         ModelManager.instance.setModelMetaData(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "reloadModelsMatching",
         global = true
      )
      public static void reloadModelsMatching(String var0) {
         ModelManager.instance.reloadModelsMatching(var0);
      }

      @LuaMethod(
         name = "getSLSoundManager",
         global = true
      )
      public static SLSoundManager getSLSoundManager() {
         return null;
      }

      @LuaMethod(
         name = "getRadioAPI",
         global = true
      )
      public static RadioAPI getRadioAPI() {
         return RadioAPI.hasInstance() ? RadioAPI.getInstance() : null;
      }

      @LuaMethod(
         name = "getRadioTranslators",
         global = true
      )
      public static ArrayList getRadioTranslators(Language var0) {
         return RadioData.getTranslatorNames(var0);
      }

      @LuaMethod(
         name = "getTranslatorCredits",
         global = true
      )
      public static ArrayList getTranslatorCredits(Language var0) {
         File var1 = new File(ZomboidFileSystem.instance.getString("media/lua/shared/Translate/" + var0.name() + "/credits.txt"));

         try {
            FileReader var2 = new FileReader(var1);

            ArrayList var6;
            try {
               BufferedReader var3 = new BufferedReader(var2);

               try {
                  ArrayList var4 = new ArrayList();

                  String var5;
                  while((var5 = var3.readLine()) != null) {
                     if (!StringUtils.isNullOrWhitespace(var5)) {
                        var4.add(var5.trim());
                     }
                  }

                  var6 = var4;
               } catch (Throwable var9) {
                  try {
                     var3.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               var3.close();
            } catch (Throwable var10) {
               try {
                  var2.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            var2.close();
            return var6;
         } catch (FileNotFoundException var11) {
            return null;
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
            return null;
         }
      }

      @LuaMethod(
         name = "getBehaviourDebugPlayer",
         global = true
      )
      public static IsoGameCharacter getBehaviourDebugPlayer() {
         return null;
      }

      @LuaMethod(
         name = "setBehaviorStep",
         global = true
      )
      public static void setBehaviorStep(boolean var0) {
      }

      @LuaMethod(
         name = "getPuddlesManager",
         global = true
      )
      public static IsoPuddles getPuddlesManager() {
         return IsoPuddles.getInstance();
      }

      @LuaMethod(
         name = "setPuddles",
         global = true
      )
      public static void setPuddles(float var0) {
         IsoPuddles.PuddlesFloat var1 = IsoPuddles.getInstance().getPuddlesFloat(3);
         var1.setEnableAdmin(true);
         var1.setAdminValue(var0);
         var1 = IsoPuddles.getInstance().getPuddlesFloat(1);
         var1.setEnableAdmin(true);
         var1.setAdminValue(PZMath.clamp_01(var0 * 1.2F));
      }

      @LuaMethod(
         name = "getZomboidRadio",
         global = true
      )
      public static ZomboidRadio getZomboidRadio() {
         return ZomboidRadio.hasInstance() ? ZomboidRadio.getInstance() : null;
      }

      @LuaMethod(
         name = "getRandomUUID",
         global = true
      )
      public static String getRandomUUID() {
         return ModUtilsJava.getRandomUUID();
      }

      @LuaMethod(
         name = "sendItemListNet",
         global = true
      )
      public static boolean sendItemListNet(IsoPlayer var0, ArrayList var1, IsoPlayer var2, String var3, String var4) {
         return ModUtilsJava.sendItemListNet(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "instanceof",
         global = true
      )
      public static boolean instof(Object var0, String var1) {
         if ("PZKey".equals(var1)) {
            boolean var2 = false;
         }

         if (var0 == null) {
            return false;
         } else if (LuaManager.exposer.TypeMap.containsKey(var1)) {
            Class var3 = (Class)LuaManager.exposer.TypeMap.get(var1);
            return var3.isInstance(var0);
         } else if (var1.equals("LuaClosure") && var0 instanceof LuaClosure) {
            return true;
         } else {
            return var1.equals("KahluaTableImpl") && var0 instanceof KahluaTableImpl;
         }
      }

      @LuaMethod(
         name = "serverConnect",
         global = true
      )
      public static void serverConnect(String var0, String var1, String var2, String var3, String var4, String var5) {
         Core.GameMode = "Multiplayer";
         Core.setDifficulty("Hardcore");
         if (GameClient.connection != null) {
            GameClient.connection.forceDisconnect();
         }

         GameClient.bClient = true;
         GameClient.bCoopInvite = false;
         ZomboidFileSystem.instance.cleanMultiplayerSaves();
         GameClient.instance.doConnect(var0, var1, var2, var3, var4, var5);
      }

      @LuaMethod(
         name = "serverConnectCoop",
         global = true
      )
      public static void serverConnectCoop(String var0) {
         Core.GameMode = "Multiplayer";
         Core.setDifficulty("Hardcore");
         if (GameClient.connection != null) {
            GameClient.connection.forceDisconnect();
         }

         GameClient.bClient = true;
         GameClient.bCoopInvite = true;
         GameClient.instance.doConnectCoop(var0);
      }

      @LuaMethod(
         name = "sendPing",
         global = true
      )
      public static void sendPing() {
         if (GameClient.bClient) {
            ByteBufferWriter var0 = GameClient.connection.startPingPacket();
            PacketTypes.doPingPacket(var0);
            var0.putLong(System.currentTimeMillis());
            GameClient.connection.endPingPacket();
         }

      }

      @LuaMethod(
         name = "forceDisconnect",
         global = true
      )
      public static void forceDisconnect() {
         if (GameClient.connection != null) {
            GameClient.connection.forceDisconnect();
         }

      }

      @LuaMethod(
         name = "backToSinglePlayer",
         global = true
      )
      public static void backToSinglePlayer() {
         if (GameClient.bClient) {
            GameClient.instance.doDisconnect("going back to single-player");
            GameClient.bClient = false;
            timeLastRefresh = 0L;
         }

      }

      @LuaMethod(
         name = "isIngameState",
         global = true
      )
      public static boolean isIngameState() {
         return GameWindow.states.current == IngameState.instance;
      }

      @LuaMethod(
         name = "requestPacketCounts",
         global = true
      )
      public static void requestPacketCounts() {
         if (GameClient.bClient) {
            GameClient.instance.requestPacketCounts();
         }

      }

      @LuaMethod(
         name = "getPacketCounts",
         global = true
      )
      public static KahluaTable getPacketCounts(int var0) {
         return GameClient.bClient ? GameClient.instance.getPacketCounts(var0) : null;
      }

      @LuaMethod(
         name = "getAllItems",
         global = true
      )
      public static ArrayList getAllItems() {
         return ScriptManager.instance.getAllItems();
      }

      @LuaMethod(
         name = "scoreboardUpdate",
         global = true
      )
      public static void scoreboardUpdate() {
         GameClient.instance.scoreboardUpdate();
      }

      @LuaMethod(
         name = "save",
         global = true
      )
      public static void save(boolean var0) {
         try {
            GameWindow.save(var0);
         } catch (Throwable var2) {
            ExceptionLogger.logException(var2);
         }

      }

      @LuaMethod(
         name = "saveGame",
         global = true
      )
      public static void saveGame() {
         save(true);
      }

      @LuaMethod(
         name = "getAllRecipes",
         global = true
      )
      public static ArrayList getAllRecipes() {
         return new ArrayList(ScriptManager.instance.getAllRecipes());
      }

      @LuaMethod(
         name = "requestUserlog",
         global = true
      )
      public static void requestUserlog(String var0) {
         if (GameClient.bClient) {
            GameClient.instance.requestUserlog(var0);
         }

      }

      @LuaMethod(
         name = "addUserlog",
         global = true
      )
      public static void addUserlog(String var0, String var1, String var2) {
         if (GameClient.bClient) {
            GameClient.instance.addUserlog(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "removeUserlog",
         global = true
      )
      public static void removeUserlog(String var0, String var1, String var2) {
         if (GameClient.bClient) {
            GameClient.instance.removeUserlog(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "tabToX",
         global = true
      )
      public static String tabToX(String var0, int var1) {
         while(var0.length() < var1) {
            var0 = var0 + " ";
         }

         return var0;
      }

      @LuaMethod(
         name = "istype",
         global = true
      )
      public static boolean isType(Object var0, String var1) {
         if (LuaManager.exposer.TypeMap.containsKey(var1)) {
            Class var2 = (Class)LuaManager.exposer.TypeMap.get(var1);
            return var2.equals(var0.getClass());
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "isoToScreenX",
         global = true
      )
      public static float isoToScreenX(int var0, float var1, float var2, float var3) {
         float var4 = IsoUtils.XToScreen(var1, var2, var3, 0) - IsoCamera.cameras[var0].getOffX();
         var4 /= Core.getInstance().getZoom(var0);
         return (float)IsoCamera.getScreenLeft(var0) + var4;
      }

      @LuaMethod(
         name = "isoToScreenY",
         global = true
      )
      public static float isoToScreenY(int var0, float var1, float var2, float var3) {
         float var4 = IsoUtils.YToScreen(var1, var2, var3, 0) - IsoCamera.cameras[var0].getOffY();
         var4 /= Core.getInstance().getZoom(var0);
         return (float)IsoCamera.getScreenTop(var0) + var4;
      }

      @LuaMethod(
         name = "screenToIsoX",
         global = true
      )
      public static float screenToIsoX(int var0, float var1, float var2, float var3) {
         float var4 = Core.getInstance().getZoom(var0);
         var1 -= (float)IsoCamera.getScreenLeft(var0);
         var2 -= (float)IsoCamera.getScreenTop(var0);
         return IsoCamera.cameras[var0].XToIso(var1 * var4, var2 * var4, var3);
      }

      @LuaMethod(
         name = "screenToIsoY",
         global = true
      )
      public static float screenToIsoY(int var0, float var1, float var2, float var3) {
         float var4 = Core.getInstance().getZoom(var0);
         var1 -= (float)IsoCamera.getScreenLeft(var0);
         var2 -= (float)IsoCamera.getScreenTop(var0);
         return IsoCamera.cameras[var0].YToIso(var1 * var4, var2 * var4, var3);
      }

      @LuaMethod(
         name = "getAmbientStreamManager",
         global = true
      )
      public static BaseAmbientStreamManager getAmbientStreamManager() {
         return AmbientStreamManager.instance;
      }

      @LuaMethod(
         name = "getSleepingEvent",
         global = true
      )
      public static SleepingEvent getSleepingEvent() {
         return SleepingEvent.instance;
      }

      @LuaMethod(
         name = "setPlayerMovementActive",
         global = true
      )
      public static void setPlayerMovementActive(int var0, boolean var1) {
         IsoPlayer.players[var0].bJoypadMovementActive = var1;
      }

      @LuaMethod(
         name = "setActivePlayer",
         global = true
      )
      public static void setActivePlayer(int var0) {
         if (!GameClient.bClient) {
            IsoPlayer.setInstance(IsoPlayer.players[var0]);
            IsoCamera.CamCharacter = IsoPlayer.getInstance();
         }
      }

      @LuaMethod(
         name = "getPlayer",
         global = true
      )
      public static IsoPlayer getPlayer() {
         return IsoPlayer.getInstance();
      }

      @LuaMethod(
         name = "getNumActivePlayers",
         global = true
      )
      public static int getNumActivePlayers() {
         return IsoPlayer.numPlayers;
      }

      @LuaMethod(
         name = "playServerSound",
         global = true
      )
      public static void playServerSound(String var0, IsoGridSquare var1) {
         GameServer.PlayWorldSoundServer(var0, false, var1, 0.2F, 5.0F, 1.1F, true);
      }

      @LuaMethod(
         name = "getMaxActivePlayers",
         global = true
      )
      public static int getMaxActivePlayers() {
         return 4;
      }

      @LuaMethod(
         name = "getPlayerScreenLeft",
         global = true
      )
      public static int getPlayerScreenLeft(int var0) {
         return IsoCamera.getScreenLeft(var0);
      }

      @LuaMethod(
         name = "getPlayerScreenTop",
         global = true
      )
      public static int getPlayerScreenTop(int var0) {
         return IsoCamera.getScreenTop(var0);
      }

      @LuaMethod(
         name = "getPlayerScreenWidth",
         global = true
      )
      public static int getPlayerScreenWidth(int var0) {
         return IsoCamera.getScreenWidth(var0);
      }

      @LuaMethod(
         name = "getPlayerScreenHeight",
         global = true
      )
      public static int getPlayerScreenHeight(int var0) {
         return IsoCamera.getScreenHeight(var0);
      }

      @LuaMethod(
         name = "getPlayerByOnlineID",
         global = true
      )
      public static IsoPlayer getPlayerByOnlineID(int var0) {
         if (GameServer.bServer) {
            return (IsoPlayer)GameServer.IDToPlayerMap.get((short)var0);
         } else {
            return GameClient.bClient ? (IsoPlayer)GameClient.IDToPlayerMap.get((short)var0) : null;
         }
      }

      @LuaMethod(
         name = "initUISystem",
         global = true
      )
      public static void initUISystem() {
         UIManager.init();
         LuaEventManager.triggerEvent("OnCreatePlayer", 0, IsoPlayer.players[0]);
      }

      @LuaMethod(
         name = "getPerformance",
         global = true
      )
      public static PerformanceSettings getPerformance() {
         return PerformanceSettings.instance;
      }

      @LuaMethod(
         name = "getDBSchema",
         global = true
      )
      public static void getDBSchema() {
         GameClient.instance.getDBSchema();
      }

      @LuaMethod(
         name = "getTableResult",
         global = true
      )
      public static void getTableResult(String var0, int var1) {
         GameClient.instance.getTableResult(var0, var1);
      }

      @LuaMethod(
         name = "getWorldSoundManager",
         global = true
      )
      public static WorldSoundManager getWorldSoundManager() {
         return WorldSoundManager.instance;
      }

      @LuaMethod(
         name = "AddWorldSound",
         global = true
      )
      public static void AddWorldSound(IsoPlayer var0, int var1, int var2) {
         WorldSoundManager.instance.addSound((Object)null, (int)var0.getX(), (int)var0.getY(), (int)var0.getZ(), var1, var2, false);
      }

      @LuaMethod(
         name = "AddNoiseToken",
         global = true
      )
      public static void AddNoiseToken(IsoGridSquare var0, int var1) {
      }

      @LuaMethod(
         name = "pauseSoundAndMusic",
         global = true
      )
      public static void pauseSoundAndMusic() {
         DebugLog.log("EXITDEBUG: pauseSoundAndMusic 1");
         SoundManager.instance.pauseSoundAndMusic();
         DebugLog.log("EXITDEBUG: pauseSoundAndMusic 2");
      }

      @LuaMethod(
         name = "resumeSoundAndMusic",
         global = true
      )
      public static void resumeSoundAndMusic() {
         SoundManager.instance.resumeSoundAndMusic();
      }

      @LuaMethod(
         name = "isDemo",
         global = true
      )
      public static boolean isDemo() {
         Core.getInstance();
         return false;
      }

      @LuaMethod(
         name = "getTimeInMillis",
         global = true
      )
      public static long getTimeInMillis() {
         return System.currentTimeMillis();
      }

      @LuaMethod(
         name = "getCurrentCoroutine",
         global = true
      )
      public static Coroutine getCurrentCoroutine() {
         return LuaManager.thread.getCurrentCoroutine();
      }

      @LuaMethod(
         name = "reloadLuaFile",
         global = true
      )
      public static void reloadLuaFile(String var0) {
         LuaManager.loaded.remove(var0);
         LuaManager.RunLua(var0, true);
      }

      @LuaMethod(
         name = "reloadServerLuaFile",
         global = true
      )
      public static void reloadServerLuaFile(String var0) {
         if (GameServer.bServer) {
            String var10000 = ZomboidFileSystem.instance.getCacheDir();
            var0 = var10000 + File.separator + "Server" + File.separator + var0;
            LuaManager.loaded.remove(var0);
            LuaManager.RunLua(var0, true);
         }
      }

      @LuaMethod(
         name = "getServerSpawnRegions",
         global = true
      )
      public static KahluaTable getServerSpawnRegions() {
         return !GameClient.bClient ? null : GameClient.instance.getServerSpawnRegions();
      }

      @LuaMethod(
         name = "getServerOptions",
         global = true
      )
      public static ServerOptions getServerOptions() {
         return ServerOptions.instance;
      }

      @LuaMethod(
         name = "getServerName",
         global = true
      )
      public static String getServerName() {
         return GameServer.ServerName;
      }

      @LuaMethod(
         name = "getSpecificPlayer",
         global = true
      )
      public static IsoPlayer getSpecificPlayer(int var0) {
         return IsoPlayer.players[var0];
      }

      @LuaMethod(
         name = "getCameraOffX",
         global = true
      )
      public static float getCameraOffX() {
         return IsoCamera.getOffX();
      }

      @LuaMethod(
         name = "getLatestSave",
         global = true
      )
      public static KahluaTable getLatestSave() {
         KahluaTable var0 = LuaManager.platform.newTable();
         BufferedReader var1 = null;

         try {
            String var10006 = ZomboidFileSystem.instance.getCacheDir();
            var1 = new BufferedReader(new FileReader(new File(var10006 + File.separator + "latestSave.ini")));
         } catch (FileNotFoundException var4) {
            return var0;
         }

         try {
            String var2 = null;

            for(int var3 = 1; (var2 = var1.readLine()) != null; ++var3) {
               var0.rawset(var3, var2);
            }

            var1.close();
            return var0;
         } catch (Exception var5) {
            return var0;
         }
      }

      @LuaMethod(
         name = "isCurrentExecutionPoint",
         global = true
      )
      public static boolean isCurrentExecutionPoint(String var0, int var1) {
         int var2 = LuaManager.thread.currentCoroutine.getCallframeTop() - 1;
         if (var2 < 0) {
            var2 = 0;
         }

         LuaCallFrame var3 = LuaManager.thread.currentCoroutine.getCallFrame(var2);
         if (var3.closure == null) {
            return false;
         } else {
            return var3.closure.prototype.lines[var3.pc] == var1 && var0.equals(var3.closure.prototype.filename);
         }
      }

      @LuaMethod(
         name = "toggleBreakOnChange",
         global = true
      )
      public static void toggleBreakOnChange(KahluaTable var0, Object var1) {
         if (Core.bDebug) {
            LuaManager.thread.toggleBreakOnChange(var0, var1);
         }

      }

      @LuaMethod(
         name = "isDebugEnabled",
         global = true
      )
      public static boolean isDebugEnabled() {
         return Core.bDebug;
      }

      @LuaMethod(
         name = "toggleBreakOnRead",
         global = true
      )
      public static void toggleBreakOnRead(KahluaTable var0, Object var1) {
         if (Core.bDebug) {
            LuaManager.thread.toggleBreakOnRead(var0, var1);
         }

      }

      @LuaMethod(
         name = "toggleBreakpoint",
         global = true
      )
      public static void toggleBreakpoint(String var0, int var1) {
         var0 = var0.replace("\\", "/");
         if (Core.bDebug) {
            LuaManager.thread.breakpointToggle(var0, var1);
         }

      }

      @LuaMethod(
         name = "sendVisual",
         global = true
      )
      public static void sendVisual(IsoPlayer var0) {
         if (GameClient.bClient) {
            GameClient.instance.sendVisual(var0);
         }

      }

      @LuaMethod(
         name = "sendClothing",
         global = true
      )
      public static void sendClothing(IsoPlayer var0) {
         if (GameClient.bClient) {
            GameClient.instance.sendClothing(var0, "", (InventoryItem)null);
         }

      }

      @LuaMethod(
         name = "hasDataReadBreakpoint",
         global = true
      )
      public static boolean hasDataReadBreakpoint(KahluaTable var0, Object var1) {
         return LuaManager.thread.hasReadDataBreakpoint(var0, var1);
      }

      @LuaMethod(
         name = "hasDataBreakpoint",
         global = true
      )
      public static boolean hasDataBreakpoint(KahluaTable var0, Object var1) {
         return LuaManager.thread.hasDataBreakpoint(var0, var1);
      }

      @LuaMethod(
         name = "hasBreakpoint",
         global = true
      )
      public static boolean hasBreakpoint(String var0, int var1) {
         return LuaManager.thread.hasBreakpoint(var0, var1);
      }

      @LuaMethod(
         name = "getLoadedLuaCount",
         global = true
      )
      public static int getLoadedLuaCount() {
         return LuaManager.loaded.size();
      }

      @LuaMethod(
         name = "getLoadedLua",
         global = true
      )
      public static String getLoadedLua(int var0) {
         return (String)LuaManager.loaded.get(var0);
      }

      @LuaMethod(
         name = "isServer",
         global = true
      )
      public static boolean isServer() {
         return GameServer.bServer;
      }

      @LuaMethod(
         name = "isServerSoftReset",
         global = true
      )
      public static boolean isServerSoftReset() {
         return GameServer.bServer && System.getProperty("softreset") != null;
      }

      @LuaMethod(
         name = "isClient",
         global = true
      )
      public static boolean isClient() {
         return GameClient.bClient;
      }

      @LuaMethod(
         name = "canModifyPlayerStats",
         global = true
      )
      public static boolean canModifyPlayerStats() {
         return !GameClient.bClient ? true : GameClient.canModifyPlayerStats();
      }

      @LuaMethod(
         name = "executeQuery",
         global = true
      )
      public static void executeQuery(String var0, KahluaTable var1) {
         GameClient.instance.executeQuery(var0, var1);
      }

      @LuaMethod(
         name = "canSeePlayerStats",
         global = true
      )
      public static boolean canSeePlayerStats() {
         return GameClient.canSeePlayerStats();
      }

      @LuaMethod(
         name = "getAccessLevel",
         global = true
      )
      public static String getAccessLevel() {
         return GameClient.accessLevel;
      }

      @LuaMethod(
         name = "getOnlinePlayers",
         global = true
      )
      public static ArrayList getOnlinePlayers() {
         if (GameServer.bServer) {
            return GameServer.getPlayers();
         } else {
            return GameClient.bClient ? GameClient.instance.getPlayers() : null;
         }
      }

      @LuaMethod(
         name = "getDebug",
         global = true
      )
      public static boolean getDebug() {
         return Core.bDebug || GameServer.bServer && GameServer.bDebug;
      }

      @LuaMethod(
         name = "getCameraOffY",
         global = true
      )
      public static float getCameraOffY() {
         return IsoCamera.getOffY();
      }

      @LuaMethod(
         name = "createRegionFile",
         global = true
      )
      public static KahluaTable createRegionFile() {
         KahluaTable var0 = LuaManager.platform.newTable();
         String var1 = IsoWorld.instance.getMap();
         if (var1.equals("DEFAULT")) {
            MapGroups var2 = new MapGroups();
            var2.createGroups();
            if (var2.getNumberOfGroups() != 1) {
               throw new RuntimeException("GameMap is DEFAULT but there are multiple worlds to choose from");
            }

            var2.setWorld(0);
            var1 = IsoWorld.instance.getMap();
         }

         if (!GameClient.bClient && !GameServer.bServer) {
            var1 = MapGroups.addMissingVanillaDirectories(var1);
         }

         String[] var10 = var1.split(";");
         int var3 = 1;
         String[] var4 = var10;
         int var5 = var10.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            var7 = var7.trim();
            if (!var7.isEmpty()) {
               File var8 = new File(ZomboidFileSystem.instance.getString("media/maps/" + var7 + "/spawnpoints.lua"));
               if (var8.exists()) {
                  KahluaTable var9 = LuaManager.platform.newTable();
                  var9.rawset("name", var7);
                  var9.rawset("file", "media/maps/" + var7 + "/spawnpoints.lua");
                  var0.rawset(var3, var9);
                  ++var3;
               }
            }
         }

         return var0;
      }

      @LuaMethod(
         name = "getMapDirectoryTable",
         global = true
      )
      public static KahluaTable getMapDirectoryTable() {
         KahluaTable var0 = LuaManager.platform.newTable();
         File var1 = ZomboidFileSystem.instance.getMediaFile("maps");
         String[] var2 = var1.list();
         if (var2 == null) {
            return var0;
         } else {
            int var3 = 1;

            String var5;
            for(int var4 = 0; var4 < var2.length; ++var4) {
               var5 = var2[var4];
               if (!var5.equals("challengemaps")) {
                  var0.rawset(var3, var5);
                  ++var3;
               }
            }

            Iterator var11 = ZomboidFileSystem.instance.getModIDs().iterator();

            while(var11.hasNext()) {
               var5 = (String)var11.next();
               ChooseGameInfo.Mod var6 = null;

               try {
                  var6 = ChooseGameInfo.getAvailableModDetails(var5);
               } catch (Exception var10) {
               }

               if (var6 != null) {
                  var1 = new File(var6.getDir() + "/media/maps/");
                  if (var1.exists()) {
                     var2 = var1.list();
                     if (var2 != null) {
                        for(int var7 = 0; var7 < var2.length; ++var7) {
                           String var8 = var2[var7];
                           ChooseGameInfo.Map var9 = ChooseGameInfo.getMapDetails(var8);
                           if (var9.getLotDirectories() != null && !var9.getLotDirectories().isEmpty() && !var8.equals("challengemaps")) {
                              var0.rawset(var3, var8);
                              ++var3;
                           }
                        }
                     }
                  }
               }
            }

            return var0;
         }
      }

      @LuaMethod(
         name = "deleteSave",
         global = true
      )
      public static void deleteSave(String var0) {
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator + var0);
         String[] var2 = var1.list();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               File var4 = new File(ZomboidFileSystem.instance.getSaveDir() + File.separator + var0 + File.separator + var2[var3]);
               if (var4.isDirectory()) {
                  deleteSave(var0 + File.separator + var4.getName());
               }

               var4.delete();
            }

            var1.delete();
         }
      }

      @LuaMethod(
         name = "sendPlayerExtraInfo",
         global = true
      )
      public static void sendPlayerExtraInfo(IsoPlayer var0) {
         GameClient.sendPlayerExtraInfo(var0);
      }

      @LuaMethod(
         name = "getServerAddressFromArgs",
         global = true
      )
      public static String getServerAddressFromArgs() {
         if (System.getProperty("args.server.connect") != null) {
            String var0 = System.getProperty("args.server.connect");
            System.clearProperty("args.server.connect");
            return var0;
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "getServerPasswordFromArgs",
         global = true
      )
      public static String getServerPasswordFromArgs() {
         if (System.getProperty("args.server.password") != null) {
            String var0 = System.getProperty("args.server.password");
            System.clearProperty("args.server.password");
            return var0;
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "getServerListFile",
         global = true
      )
      public static String getServerListFile() {
         return SteamUtils.isSteamModeEnabled() ? "ServerListSteam.txt" : "ServerList.txt";
      }

      @LuaMethod(
         name = "getServerList",
         global = true
      )
      public static KahluaTable getServerList() {
         ArrayList var0 = new ArrayList();
         KahluaTable var1 = LuaManager.platform.newTable();
         BufferedReader var2 = null;

         try {
            String var10002 = LuaManager.getLuaCacheDir();
            File var3 = new File(var10002 + File.separator + getServerListFile());
            if (!var3.exists()) {
               var3.createNewFile();
            }

            var2 = new BufferedReader(new FileReader(var3));
            String var4 = null;
            Server var5 = null;

            while((var4 = var2.readLine()) != null) {
               if (var4.startsWith("name=")) {
                  var5 = new Server();
                  var0.add(var5);
                  var5.setName(var4.replaceFirst("name=", ""));
               } else if (var4.startsWith("ip=")) {
                  var5.setIp(var4.replaceFirst("ip=", ""));
               } else if (var4.startsWith("localip=")) {
                  var5.setLocalIP(var4.replaceFirst("localip=", ""));
               } else if (var4.startsWith("description=")) {
                  var5.setDescription(var4.replaceFirst("description=", ""));
               } else if (var4.startsWith("port=")) {
                  var5.setPort(var4.replaceFirst("port=", ""));
               } else if (var4.startsWith("user=")) {
                  var5.setUserName(var4.replaceFirst("user=", ""));
               } else if (var4.startsWith("password=")) {
                  var5.setPwd(var4.replaceFirst("password=", ""));
               } else if (var4.startsWith("serverpassword=")) {
                  var5.setServerPassword(var4.replaceFirst("serverpassword=", ""));
               }
            }

            int var6 = 1;

            for(int var7 = 0; var7 < var0.size(); ++var7) {
               Server var8 = (Server)var0.get(var7);
               Double var9 = (double)var6;
               var1.rawset(var9, var8);
               ++var6;
            }
         } catch (Exception var18) {
            var18.printStackTrace();
         } finally {
            try {
               var2.close();
            } catch (Exception var17) {
            }

         }

         return var1;
      }

      @LuaMethod(
         name = "ping",
         global = true
      )
      public static void ping(String var0, String var1, String var2, String var3) {
         GameClient.askPing = true;
         serverConnect(var0, var1, var2, "", var3, "");
      }

      @LuaMethod(
         name = "stopPing",
         global = true
      )
      public static void stopPing() {
         GameClient.askPing = false;
      }

      @LuaMethod(
         name = "transformIntoKahluaTable",
         global = true
      )
      public static KahluaTable transformIntoKahluaTable(HashMap var0) {
         KahluaTable var1 = LuaManager.platform.newTable();
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            var1.rawset(var3.getKey(), var3.getValue());
         }

         return var1;
      }

      @LuaMethod(
         name = "getSaveDirectory",
         global = true
      )
      public static ArrayList getSaveDirectory(String var0) {
         File var1 = new File(var0 + File.separator);
         if (!var1.exists() && !Core.getInstance().isNoSave()) {
            var1.mkdir();
         }

         String[] var2 = var1.list();
         if (var2 == null) {
            return null;
         } else {
            ArrayList var3 = new ArrayList();

            for(int var4 = 0; var4 < var2.length; ++var4) {
               File var5 = new File(var0 + File.separator + var2[var4]);
               if (var5.isDirectory()) {
                  var3.add(var5);
               }
            }

            return var3;
         }
      }

      @LuaMethod(
         name = "getFullSaveDirectoryTable",
         global = true
      )
      public static KahluaTable getFullSaveDirectoryTable() {
         KahluaTable var0 = LuaManager.platform.newTable();
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator);
         if (!var1.exists()) {
            var1.mkdir();
         }

         String[] var2 = var1.list();
         if (var2 == null) {
            return var0;
         } else {
            ArrayList var3 = new ArrayList();

            int var4;
            for(var4 = 0; var4 < var2.length; ++var4) {
               var10002 = ZomboidFileSystem.instance.getSaveDir();
               File var5 = new File(var10002 + File.separator + var2[var4]);
               if (var5.isDirectory() && !"Multiplayer".equals(var2[var4])) {
                  String var10000 = ZomboidFileSystem.instance.getSaveDir();
                  ArrayList var6 = getSaveDirectory(var10000 + File.separator + var2[var4]);
                  var3.addAll(var6);
               }
            }

            Collections.sort(var3, new Comparator() {
               public int compare(File var1, File var2) {
                  return Long.valueOf(var2.lastModified()).compareTo(var1.lastModified());
               }
            });
            var4 = 1;

            for(int var9 = 0; var9 < var3.size(); ++var9) {
               File var10 = (File)var3.get(var9);
               String var7 = getSaveName(var10);
               Double var8 = (double)var4;
               var0.rawset(var8, var7);
               ++var4;
            }

            return var0;
         }
      }

      public static String getSaveName(File var0) {
         String[] var1 = var0.getAbsolutePath().split("\\" + File.separator);
         return var1[var1.length - 2] + File.separator + var0.getName();
      }

      @LuaMethod(
         name = "getSaveDirectoryTable",
         global = true
      )
      public static KahluaTable getSaveDirectoryTable() {
         KahluaTable var0 = LuaManager.platform.newTable();
         return var0;
      }

      public static List getMods() {
         ArrayList var0 = new ArrayList();
         ZomboidFileSystem.instance.getAllModFolders(var0);
         return var0;
      }

      @LuaMethod(
         name = "doChallenge",
         global = true
      )
      public static void doChallenge(KahluaTable var0) {
         Core.GameMode = var0.rawget("gameMode").toString();
         Core.ChallengeID = var0.rawget("id").toString();
         Core.bLastStand = Core.GameMode.equals("LastStand");
         Core.getInstance().setChallenge(true);
         getWorld().setMap(var0.getString("world"));
         Integer var1 = Rand.Next(100000000);
         IsoWorld.instance.setWorld(var1.toString());
         getWorld().bDoChunkMapUpdate = false;
      }

      @LuaMethod(
         name = "doTutorial",
         global = true
      )
      public static void doTutorial(KahluaTable var0) {
         Core.GameMode = "Tutorial";
         Core.bLastStand = false;
         Core.ChallengeID = null;
         Core.getInstance().setChallenge(false);
         Core.bTutorial = true;
         getWorld().setMap(var0.getString("world"));
         getWorld().bDoChunkMapUpdate = false;
      }

      @LuaMethod(
         name = "deleteAllGameModeSaves",
         global = true
      )
      public static void deleteAllGameModeSaves(String var0) {
         String var1 = Core.GameMode;
         Core.GameMode = var0;
         Path var2 = Paths.get(ZomboidFileSystem.instance.getGameModeCacheDir());
         if (!Files.exists(var2, new LinkOption[0])) {
            Core.GameMode = var1;
         } else {
            try {
               Files.walkFileTree(var2, new FileVisitor() {
                  public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) throws IOException {
                     return FileVisitResult.CONTINUE;
                  }

                  public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) throws IOException {
                     Files.delete(var1);
                     return FileVisitResult.CONTINUE;
                  }

                  public FileVisitResult visitFileFailed(Path var1, IOException var2) throws IOException {
                     var2.printStackTrace();
                     return FileVisitResult.CONTINUE;
                  }

                  public FileVisitResult postVisitDirectory(Path var1, IOException var2) throws IOException {
                     Files.delete(var1);
                     return FileVisitResult.CONTINUE;
                  }
               });
            } catch (IOException var4) {
               var4.printStackTrace();
            }

            Core.GameMode = var1;
         }
      }

      @LuaMethod(
         name = "sledgeDestroy",
         global = true
      )
      public static void sledgeDestroy(IsoObject var0) {
         if (GameClient.bClient) {
            GameClient.destroy(var0);
         }

      }

      @LuaMethod(
         name = "getTickets",
         global = true
      )
      public static void getTickets(String var0) {
         if (GameClient.bClient) {
            GameClient.getTickets(var0);
         }

      }

      @LuaMethod(
         name = "addTicket",
         global = true
      )
      public static void addTicket(String var0, String var1, int var2) {
         if (GameClient.bClient) {
            GameClient.addTicket(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "removeTicket",
         global = true
      )
      public static void removeTicket(int var0) {
         if (GameClient.bClient) {
            GameClient.removeTicket(var0);
         }

      }

      @LuaMethod(
         name = "sendFactionInvite",
         global = true
      )
      public static void sendFactionInvite(Faction var0, IsoPlayer var1, String var2) {
         if (GameClient.bClient) {
            GameClient.sendFactionInvite(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "acceptFactionInvite",
         global = true
      )
      public static void acceptFactionInvite(Faction var0, String var1) {
         if (GameClient.bClient) {
            GameClient.acceptFactionInvite(var0, var1);
         }

      }

      @LuaMethod(
         name = "sendSafehouseInvite",
         global = true
      )
      public static void sendSafehouseInvite(SafeHouse var0, IsoPlayer var1, String var2) {
         if (GameClient.bClient) {
            GameClient.sendSafehouseInvite(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "acceptSafehouseInvite",
         global = true
      )
      public static void acceptSafehouseInvite(SafeHouse var0, String var1) {
         if (GameClient.bClient) {
            GameClient.acceptSafehouseInvite(var0, var1);
         }

      }

      @LuaMethod(
         name = "createHordeFromTo",
         global = true
      )
      public static void createHordeFromTo(float var0, float var1, float var2, float var3, int var4) {
         ZombiePopulationManager.instance.createHordeFromTo((int)var0, (int)var1, (int)var2, (int)var3, var4);
      }

      @LuaMethod(
         name = "createHordeInAreaTo",
         global = true
      )
      public static void createHordeInAreaTo(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
         ZombiePopulationManager.instance.createHordeInAreaTo(var0, var1, var2, var3, var4, var5, var6);
      }

      @LuaMethod(
         name = "spawnHorde",
         global = true
      )
      public static void spawnHorde(float var0, float var1, float var2, float var3, float var4, int var5) {
         for(int var6 = 0; var6 < var5; ++var6) {
            VirtualZombieManager.instance.choices.clear();
            IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare((double)Rand.Next(var0, var2), (double)Rand.Next(var1, var3), (double)var4);
            if (var7 != null) {
               VirtualZombieManager.instance.choices.add(var7);
               IsoZombie var8 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.fromIndex(Rand.Next(IsoDirections.Max.index())).index(), false);
               var8.dressInRandomOutfit();
               ZombieSpawnRecorder.instance.record(var8, "LuaManager.spawnHorde");
            }
         }

      }

      @LuaMethod(
         name = "createZombie",
         global = true
      )
      public static IsoZombie createZombie(float var0, float var1, float var2, SurvivorDesc var3, int var4, IsoDirections var5) {
         VirtualZombieManager.instance.choices.clear();
         IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare((double)var0, (double)var1, (double)var2);
         VirtualZombieManager.instance.choices.add(var6);
         IsoZombie var7 = VirtualZombieManager.instance.createRealZombieAlways(var5.index(), false);
         ZombieSpawnRecorder.instance.record(var7, "LuaManager.createZombie");
         return var7;
      }

      @LuaMethod(
         name = "triggerEvent",
         global = true
      )
      public static void triggerEvent(String var0) {
         LuaEventManager.triggerEvent(var0);
      }

      @LuaMethod(
         name = "triggerEvent",
         global = true
      )
      public static void triggerEvent(String var0, Object var1) {
         LuaEventManager.triggerEventGarbage(var0, var1);
      }

      @LuaMethod(
         name = "triggerEvent",
         global = true
      )
      public static void triggerEvent(String var0, Object var1, Object var2) {
         LuaEventManager.triggerEventGarbage(var0, var1, var2);
      }

      @LuaMethod(
         name = "triggerEvent",
         global = true
      )
      public static void triggerEvent(String var0, Object var1, Object var2, Object var3) {
         LuaEventManager.triggerEventGarbage(var0, var1, var2, var3);
      }

      @LuaMethod(
         name = "triggerEvent",
         global = true
      )
      public static void triggerEvent(String var0, Object var1, Object var2, Object var3, Object var4) {
         LuaEventManager.triggerEventGarbage(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "debugLuaTable",
         global = true
      )
      public static void debugLuaTable(Object var0, int var1) {
         if (var1 <= 1) {
            if (var0 instanceof KahluaTable) {
               KahluaTable var2 = (KahluaTable)var0;
               KahluaTableIterator var3 = var2.iterator();
               String var4 = "";

               for(int var5 = 0; var5 < var1; ++var5) {
                  var4 = var4 + "\t";
               }

               do {
                  Object var7 = var3.getKey();
                  Object var6 = var3.getValue();
                  if (var7 != null) {
                     if (var6 != null) {
                        DebugLog.Lua.debugln(var4 + var7 + " : " + var6.toString());
                     }

                     if (var6 instanceof KahluaTable) {
                        debugLuaTable(var6, var1 + 1);
                     }
                  }
               } while(var3.advance());

               if (var2.getMetatable() != null) {
                  debugLuaTable(var2.getMetatable(), var1);
               }
            }

         }
      }

      @LuaMethod(
         name = "debugLuaTable",
         global = true
      )
      public static void debugLuaTable(Object var0) {
         debugLuaTable(var0, 0);
      }

      @LuaMethod(
         name = "sendItemsInContainer",
         global = true
      )
      public static void sendItemsInContainer(IsoObject var0, ItemContainer var1) {
         GameServer.sendItemsInContainer(var0, var1 == null ? var0.getContainer() : var1);
      }

      @LuaMethod(
         name = "getModDirectoryTable",
         global = true
      )
      public static KahluaTable getModDirectoryTable() {
         KahluaTable var0 = LuaManager.platform.newTable();
         List var1 = getMods();
         int var2 = 1;

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            String var4 = (String)var1.get(var3);
            Double var5 = (double)var2;
            var0.rawset(var5, var4);
            ++var2;
         }

         return var0;
      }

      @LuaMethod(
         name = "getModInfoByID",
         global = true
      )
      public static ChooseGameInfo.Mod getModInfoByID(String var0) {
         try {
            return ChooseGameInfo.getModDetails(var0);
         } catch (Exception var2) {
            var2.printStackTrace();
            return null;
         }
      }

      @LuaMethod(
         name = "getModInfo",
         global = true
      )
      public static ChooseGameInfo.Mod getModInfo(String var0) {
         try {
            return ChooseGameInfo.readModInfo(var0);
         } catch (Exception var2) {
            ExceptionLogger.logException(var2);
            return null;
         }
      }

      @LuaMethod(
         name = "getMapFoldersForMod",
         global = true
      )
      public static ArrayList getMapFoldersForMod(String var0) {
         try {
            ChooseGameInfo.Mod var1 = ChooseGameInfo.getModDetails(var0);
            if (var1 == null) {
               return null;
            } else {
               String var10000 = var1.getDir();
               String var2 = var10000 + File.separator + "media" + File.separator + "maps";
               File var3 = new File(var2);
               if (var3.exists() && var3.isDirectory()) {
                  ArrayList var4 = null;
                  DirectoryStream var5 = Files.newDirectoryStream(var3.toPath());

                  try {
                     Iterator var6 = var5.iterator();

                     while(var6.hasNext()) {
                        Path var7 = (Path)var6.next();
                        if (Files.isDirectory(var7, new LinkOption[0])) {
                           var3 = new File(var2 + File.separator + var7.getFileName().toString() + File.separator + "map.info");
                           if (var3.exists()) {
                              if (var4 == null) {
                                 var4 = new ArrayList();
                              }

                              var4.add(var7.getFileName().toString());
                           }
                        }
                     }
                  } catch (Throwable var9) {
                     if (var5 != null) {
                        try {
                           var5.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (var5 != null) {
                     var5.close();
                  }

                  return var4;
               } else {
                  return null;
               }
            }
         } catch (Exception var10) {
            var10.printStackTrace();
            return null;
         }
      }

      @LuaMethod(
         name = "spawnpointsExistsForMod",
         global = true
      )
      public static boolean spawnpointsExistsForMod(String var0, String var1) {
         try {
            ChooseGameInfo.Mod var2 = ChooseGameInfo.getModDetails(var0);
            if (var2 == null) {
               return false;
            } else {
               String var10000 = var2.getDir();
               String var3 = var10000 + File.separator + "media" + File.separator + "maps" + File.separator + var1 + File.separator + "spawnpoints.lua";
               return (new File(var3)).exists();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }
      }

      @LuaMethod(
         name = "getFileSeparator",
         global = true
      )
      public static String getFileSeparator() {
         return File.separator;
      }

      @LuaMethod(
         name = "getScriptManager",
         global = true
      )
      public static ScriptManager getScriptManager() {
         return ScriptManager.instance;
      }

      @LuaMethod(
         name = "checkSaveFolderExists",
         global = true
      )
      public static boolean checkSaveFolderExists(String var0) {
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator + var0);
         return var1.exists();
      }

      @LuaMethod(
         name = "getAbsoluteSaveFolderName",
         global = true
      )
      public static String getAbsoluteSaveFolderName(String var0) {
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator + var0);
         return var1.getAbsolutePath();
      }

      @LuaMethod(
         name = "checkSaveFileExists",
         global = true
      )
      public static boolean checkSaveFileExists(String var0) {
         String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
         File var1 = new File(var10002 + Core.GameSaveWorld + File.separator + var0);
         return var1.exists();
      }

      @LuaMethod(
         name = "checkSavePlayerExists",
         global = true
      )
      public static boolean checkSavePlayerExists() {
         if (!GameClient.bClient) {
            return PlayerDBHelper.isPlayerAlive(ZomboidFileSystem.instance.getGameModeCacheDir() + Core.GameSaveWorld, 1);
         } else if (ClientPlayerDB.getInstance() == null) {
            return false;
         } else {
            return ClientPlayerDB.getInstance().clientLoadNetworkPlayer() && ClientPlayerDB.getInstance().isAliveMainNetworkPlayer();
         }
      }

      @LuaMethod(
         name = "fileExists",
         global = true
      )
      public static boolean fileExists(String var0) {
         String var1 = var0.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(ZomboidFileSystem.instance.getString(var1));
         return var2.exists();
      }

      @LuaMethod(
         name = "serverFileExists",
         global = true
      )
      public static boolean serverFileExists(String var0) {
         String var1 = var0.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         String var10002 = ZomboidFileSystem.instance.getCacheDir();
         File var2 = new File(var10002 + File.separator + "Server" + File.separator + var1);
         return var2.exists();
      }

      @LuaMethod(
         name = "takeScreenshot",
         global = true
      )
      public static void takeScreenshot() {
         Core.getInstance().TakeFullScreenshot((String)null);
      }

      @LuaMethod(
         name = "takeScreenshot",
         global = true
      )
      public static void takeScreenshot(String var0) {
         Core.getInstance().TakeFullScreenshot(var0);
      }

      @LuaMethod(
         name = "instanceItem",
         global = true
      )
      public static InventoryItem instanceItem(Item var0) {
         return InventoryItemFactory.CreateItem(var0.moduleDotType);
      }

      @LuaMethod(
         name = "instanceItem",
         global = true
      )
      public static InventoryItem instanceItem(String var0) {
         return InventoryItemFactory.CreateItem(var0);
      }

      @LuaMethod(
         name = "createNewScriptItem",
         global = true
      )
      public static Item createNewScriptItem(String var0, String var1, String var2, String var3, String var4) {
         Item var5 = new Item();
         var5.module = ScriptManager.instance.getModule(var0);
         var5.module.ItemMap.put(var1, var5);
         var5.Icon = "Item_" + var4;
         var5.DisplayName = var2;
         var5.name = var1;
         var5.moduleDotType = var5.module.name + "." + var1;

         try {
            var5.type = Item.Type.valueOf(var3);
         } catch (Exception var7) {
         }

         return var5;
      }

      @LuaMethod(
         name = "cloneItemType",
         global = true
      )
      public static Item cloneItemType(String var0, String var1) {
         Item var2 = ScriptManager.instance.FindItem(var1);
         Item var3 = new Item();
         var3.module = var2.getModule();
         var3.module.ItemMap.put(var0, var3);
         return var3;
      }

      @LuaMethod(
         name = "moduleDotType",
         global = true
      )
      public static String moduleDotType(String var0, String var1) {
         return StringUtils.moduleDotType(var0, var1);
      }

      @LuaMethod(
         name = "require",
         global = true
      )
      public static Object require(String var0) {
         String var1 = var0;
         if (!var0.endsWith(".lua")) {
            var1 = var0 + ".lua";
         }

         for(int var2 = 0; var2 < LuaManager.paths.size(); ++var2) {
            String var3 = (String)LuaManager.paths.get(var2);
            String var4 = ZomboidFileSystem.instance.getAbsolutePath(var3 + var1);
            if (var4 != null) {
               return LuaManager.RunLua(ZomboidFileSystem.instance.getString(var4));
            }
         }

         DebugLog.Lua.warn("require(\"" + var0 + "\") failed");
         return null;
      }

      @LuaMethod(
         name = "getRenderer",
         global = true
      )
      public static SpriteRenderer getRenderer() {
         return SpriteRenderer.instance;
      }

      @LuaMethod(
         name = "getGameTime",
         global = true
      )
      public static GameTime getGameTime() {
         return GameTime.instance;
      }

      @LuaMethod(
         name = "getStatistics",
         global = true
      )
      public static KahluaTable getStatistics() {
         return MPStatistics.getLuaStatistics();
      }

      @LuaMethod(
         name = "getPing",
         global = true
      )
      public static KahluaTable getPing() {
         return MPStatistics.getLuaPing();
      }

      @LuaMethod(
         name = "getWorld",
         global = true
      )
      public static IsoWorld getWorld() {
         return IsoWorld.instance;
      }

      @LuaMethod(
         name = "getCell",
         global = true
      )
      public static IsoCell getCell() {
         return IsoWorld.instance.getCell();
      }

      @LuaMethod(
         name = "getSandboxOptions",
         global = true
      )
      public static SandboxOptions getSandboxOptions() {
         return SandboxOptions.instance;
      }

      @LuaMethod(
         name = "getFileOutput",
         global = true
      )
      public static DataOutputStream getFileOutput(String var0) {
         if (var0.contains("..")) {
            DebugLog.Lua.warn("relative paths not allowed");
            return null;
         } else {
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = var10000 + File.separator + var0;
            var1 = var1.replace("/", File.separator);
            var1 = var1.replace("\\", File.separator);
            String var2 = var1.substring(0, var1.lastIndexOf(File.separator));
            var2 = var2.replace("\\", "/");
            File var3 = new File(var2);
            if (!var3.exists()) {
               var3.mkdirs();
            }

            File var4 = new File(var1);

            try {
               outStream = new FileOutputStream(var4);
            } catch (FileNotFoundException var6) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var6);
            }

            DataOutputStream var5 = new DataOutputStream(outStream);
            return var5;
         }
      }

      @LuaMethod(
         name = "getLastStandPlayersDirectory",
         global = true
      )
      public static String getLastStandPlayersDirectory() {
         return "LastStand";
      }

      @LuaMethod(
         name = "getLastStandPlayerFileNames",
         global = true
      )
      public static List getLastStandPlayerFileNames() throws IOException {
         ArrayList var0 = new ArrayList();
         String var10000 = LuaManager.getLuaCacheDir();
         String var1 = var10000 + File.separator + getLastStandPlayersDirectory();
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(var1);
         if (!var2.exists()) {
            var2.mkdir();
         }

         File[] var3 = var2.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (!var6.isDirectory() && var6.getName().endsWith(".txt")) {
               String var10001 = getLastStandPlayersDirectory();
               var0.add(var10001 + File.separator + var6.getName());
            }
         }

         return var0;
      }

      /** @deprecated */
      @Deprecated
      @LuaMethod(
         name = "getAllSavedPlayers",
         global = true
      )
      public static List getAllSavedPlayers() throws IOException {
         ArrayList var0 = new ArrayList();
         String var10000 = LuaManager.getLuaCacheDir();
         String var1 = var10000 + File.separator + getLastStandPlayersDirectory();
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(var1);
         if (!var2.exists()) {
            var2.mkdir();
         }

         File[] var3 = var2.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            var0.add(new BufferedReader(new FileReader(var6)));
         }

         return var0;
      }

      @LuaMethod(
         name = "getSandboxPresets",
         global = true
      )
      public static List getSandboxPresets() throws IOException {
         ArrayList var0 = new ArrayList();
         String var1 = LuaManager.getSandboxCacheDir();
         File var2 = new File(var1);
         if (!var2.exists()) {
            var2.mkdir();
         }

         File[] var3 = var2.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (var6.getName().endsWith(".cfg")) {
               var0.add(var6.getName().replace(".cfg", ""));
            }
         }

         Collections.sort(var0);
         return var0;
      }

      @LuaMethod(
         name = "deleteSandboxPreset",
         global = true
      )
      public static void deleteSandboxPreset(String var0) {
         if (var0.contains("..")) {
            DebugLog.Lua.warn("relative paths not allowed");
         } else {
            String var10000 = LuaManager.getSandboxCacheDir();
            String var1 = var10000 + File.separator + var0 + ".cfg";
            File var2 = new File(var1);
            if (var2.exists()) {
               var2.delete();
            }

         }
      }

      @LuaMethod(
         name = "getFileReader",
         global = true
      )
      public static BufferedReader getFileReader(String var0, boolean var1) throws IOException {
         if (var0.contains("..")) {
            DebugLog.Lua.warn("relative paths not allowed");
            return null;
         } else {
            String var10000 = LuaManager.getLuaCacheDir();
            String var2 = var10000 + File.separator + var0;
            var2 = var2.replace("/", File.separator);
            var2 = var2.replace("\\", File.separator);
            File var3 = new File(var2);
            if (!var3.exists() && var1) {
               var3.createNewFile();
            }

            if (var3.exists()) {
               BufferedReader var4 = null;

               try {
                  FileInputStream var5 = new FileInputStream(var3);
                  InputStreamReader var6 = new InputStreamReader(var5, StandardCharsets.UTF_8);
                  var4 = new BufferedReader(var6);
               } catch (IOException var7) {
                  Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var7);
               }

               return var4;
            } else {
               return null;
            }
         }
      }

      @LuaMethod(
         name = "getModFileReader",
         global = true
      )
      public static BufferedReader getModFileReader(String var0, String var1, boolean var2) throws IOException {
         if (!var1.isEmpty() && !var1.contains("..") && !(new File(var1)).isAbsolute()) {
            String var10000 = ZomboidFileSystem.instance.getCacheDir();
            String var3 = var10000 + File.separator + "mods" + File.separator + var1;
            if (var0 != null) {
               ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var0);
               if (var4 == null) {
                  return null;
               }

               var10000 = var4.getDir();
               var3 = var10000 + File.separator + var1;
            }

            var3 = var3.replace("/", File.separator);
            var3 = var3.replace("\\", File.separator);
            File var9 = new File(var3);
            if (!var9.exists() && var2) {
               String var5 = var3.substring(0, var3.lastIndexOf(File.separator));
               File var6 = new File(var5);
               if (!var6.exists()) {
                  var6.mkdirs();
               }

               var9.createNewFile();
            }

            if (var9.exists()) {
               BufferedReader var10 = null;

               try {
                  FileInputStream var11 = new FileInputStream(var9);
                  InputStreamReader var7 = new InputStreamReader(var11, StandardCharsets.UTF_8);
                  var10 = new BufferedReader(var7);
               } catch (IOException var8) {
                  Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var8);
               }

               return var10;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      public static void refreshAnimSets(boolean var0) {
         try {
            Iterator var1;
            if (var0) {
               AnimationSet.Reset();
               var1 = AnimNodeAssetManager.instance.getAssetTable().values().iterator();

               while(var1.hasNext()) {
                  Asset var2 = (Asset)var1.next();
                  AnimNodeAssetManager.instance.reload(var2);
               }
            }

            AnimationSet.GetAnimationSet("player", true);
            AnimationSet.GetAnimationSet("player-vehicle", true);
            AnimationSet.GetAnimationSet("zombie", true);
            AnimationSet.GetAnimationSet("zombie-crawler", true);

            for(int var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
               IsoPlayer var5 = IsoPlayer.players[var4];
               if (var5 != null) {
                  var5.advancedAnimator.OnAnimDataChanged(var0);
               }
            }

            var1 = IsoWorld.instance.CurrentCell.getZombieList().iterator();

            while(var1.hasNext()) {
               IsoZombie var6 = (IsoZombie)var1.next();
               var6.advancedAnimator.OnAnimDataChanged(var0);
            }
         } catch (Exception var3) {
            ExceptionLogger.logException(var3);
         }

      }

      public static void reloadActionGroups() {
         try {
            ActionGroup.reloadAll();
         } catch (Exception var1) {
         }

      }

      @LuaMethod(
         name = "getModFileWriter",
         global = true
      )
      public static LuaManager.GlobalObject.LuaFileWriter getModFileWriter(String var0, String var1, boolean var2, boolean var3) {
         if (!var1.isEmpty() && !var1.contains("..") && !(new File(var1)).isAbsolute()) {
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var0);
            if (var4 == null) {
               return null;
            } else {
               String var10000 = var4.getDir();
               String var5 = var10000 + File.separator + var1;
               var5 = var5.replace("/", File.separator);
               var5 = var5.replace("\\", File.separator);
               String var6 = var5.substring(0, var5.lastIndexOf(File.separator));
               File var7 = new File(var6);
               if (!var7.exists()) {
                  var7.mkdirs();
               }

               File var8 = new File(var5);
               if (!var8.exists() && var2) {
                  try {
                     var8.createNewFile();
                  } catch (IOException var13) {
                     Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var13);
                  }
               }

               PrintWriter var9 = null;

               try {
                  FileOutputStream var10 = new FileOutputStream(var8, var3);
                  OutputStreamWriter var11 = new OutputStreamWriter(var10, StandardCharsets.UTF_8);
                  var9 = new PrintWriter(var11);
               } catch (IOException var12) {
                  Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var12);
               }

               return new LuaManager.GlobalObject.LuaFileWriter(var9);
            }
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "updateFire",
         global = true
      )
      public static void updateFire() {
         IsoFireManager.Update();
      }

      @LuaMethod(
         name = "deletePlayerSave",
         global = true
      )
      public static void deletePlayerSave(String var0) {
         String var10000 = LuaManager.getLuaCacheDir();
         String var1 = var10000 + File.separator + "Players" + File.separator + "player" + var0 + ".txt";
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(var1);
         var2.delete();
      }

      @LuaMethod(
         name = "getControllerCount",
         global = true
      )
      public static int getControllerCount() {
         return GameWindow.GameInput.getControllerCount();
      }

      @LuaMethod(
         name = "isControllerConnected",
         global = true
      )
      public static boolean isControllerConnected(int var0) {
         if (var0 >= 0 && var0 <= GameWindow.GameInput.getControllerCount()) {
            return GameWindow.GameInput.getController(var0) != null;
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "getControllerGUID",
         global = true
      )
      public static String getControllerGUID(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 != null ? var1.getGUID() : "???";
         } else {
            return "???";
         }
      }

      @LuaMethod(
         name = "getControllerName",
         global = true
      )
      public static String getControllerName(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 != null ? var1.getGamepadName() : "???";
         } else {
            return "???";
         }
      }

      @LuaMethod(
         name = "getControllerAxisCount",
         global = true
      )
      public static int getControllerAxisCount(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 == null ? 0 : var1.getAxisCount();
         } else {
            return 0;
         }
      }

      @LuaMethod(
         name = "getControllerAxisValue",
         global = true
      )
      public static float getControllerAxisValue(int var0, int var1) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var2 = GameWindow.GameInput.getController(var0);
            if (var2 == null) {
               return 0.0F;
            } else {
               return var1 >= 0 && var1 < var2.getAxisCount() ? var2.getAxisValue(var1) : 0.0F;
            }
         } else {
            return 0.0F;
         }
      }

      @LuaMethod(
         name = "getControllerDeadZone",
         global = true
      )
      public static float getControllerDeadZone(int var0, int var1) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            return var1 >= 0 && var1 < GameWindow.GameInput.getAxisCount(var0) ? JoypadManager.instance.getDeadZone(var0, var1) : 0.0F;
         } else {
            return 0.0F;
         }
      }

      @LuaMethod(
         name = "setControllerDeadZone",
         global = true
      )
      public static void setControllerDeadZone(int var0, int var1, float var2) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            if (var1 >= 0 && var1 < GameWindow.GameInput.getAxisCount(var0)) {
               JoypadManager.instance.setDeadZone(var0, var1, var2);
            }
         }
      }

      @LuaMethod(
         name = "saveControllerSettings",
         global = true
      )
      public static void saveControllerSettings(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            JoypadManager.instance.saveControllerSettings(var0);
         }
      }

      @LuaMethod(
         name = "getControllerButtonCount",
         global = true
      )
      public static int getControllerButtonCount(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 == null ? 0 : var1.getButtonCount();
         } else {
            return 0;
         }
      }

      @LuaMethod(
         name = "getControllerPovX",
         global = true
      )
      public static float getControllerPovX(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 == null ? 0.0F : var1.getPovX();
         } else {
            return 0.0F;
         }
      }

      @LuaMethod(
         name = "getControllerPovY",
         global = true
      )
      public static float getControllerPovY(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 == null ? 0.0F : var1.getPovY();
         } else {
            return 0.0F;
         }
      }

      @LuaMethod(
         name = "reloadControllerConfigFiles",
         global = true
      )
      public static void reloadControllerConfigFiles() {
         JoypadManager.instance.reloadControllerFiles();
      }

      @LuaMethod(
         name = "isJoypadPressed",
         global = true
      )
      public static boolean isJoypadPressed(int var0, int var1) {
         return GameWindow.GameInput.isButtonPressedD(var1, var0);
      }

      @LuaMethod(
         name = "isJoypadDown",
         global = true
      )
      public static boolean isJoypadDown(int var0) {
         return JoypadManager.instance.isDownPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadLTPressed",
         global = true
      )
      public static boolean isJoypadLTPressed(int var0) {
         return JoypadManager.instance.isLTPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadRTPressed",
         global = true
      )
      public static boolean isJoypadRTPressed(int var0) {
         return JoypadManager.instance.isRTPressed(var0);
      }

      @LuaMethod(
         name = "getJoypadAimingAxisX",
         global = true
      )
      public static float getJoypadAimingAxisX(int var0) {
         return JoypadManager.instance.getAimingAxisX(var0);
      }

      @LuaMethod(
         name = "getJoypadAimingAxisY",
         global = true
      )
      public static float getJoypadAimingAxisY(int var0) {
         return JoypadManager.instance.getAimingAxisY(var0);
      }

      @LuaMethod(
         name = "getJoypadMovementAxisX",
         global = true
      )
      public static float getJoypadMovementAxisX(int var0) {
         return JoypadManager.instance.getMovementAxisX(var0);
      }

      @LuaMethod(
         name = "getJoypadMovementAxisY",
         global = true
      )
      public static float getJoypadMovementAxisY(int var0) {
         return JoypadManager.instance.getMovementAxisY(var0);
      }

      @LuaMethod(
         name = "getJoypadAButton",
         global = true
      )
      public static int getJoypadAButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getAButton() : -1;
      }

      @LuaMethod(
         name = "getJoypadBButton",
         global = true
      )
      public static int getJoypadBButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getBButton() : -1;
      }

      @LuaMethod(
         name = "getJoypadXButton",
         global = true
      )
      public static int getJoypadXButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getXButton() : -1;
      }

      @LuaMethod(
         name = "getJoypadYButton",
         global = true
      )
      public static int getJoypadYButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getYButton() : -1;
      }

      @LuaMethod(
         name = "getJoypadLBumper",
         global = true
      )
      public static int getJoypadLBumper(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getLBumper() : -1;
      }

      @LuaMethod(
         name = "getJoypadRBumper",
         global = true
      )
      public static int getJoypadRBumper(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getRBumper() : -1;
      }

      @LuaMethod(
         name = "getJoypadBackButton",
         global = true
      )
      public static int getJoypadBackButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getBackButton() : -1;
      }

      @LuaMethod(
         name = "getJoypadStartButton",
         global = true
      )
      public static int getJoypadStartButton(int var0) {
         JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
         return var1 != null ? var1.getStartButton() : -1;
      }

      @LuaMethod(
         name = "wasMouseActiveMoreRecentlyThanJoypad",
         global = true
      )
      public static boolean wasMouseActiveMoreRecentlyThanJoypad() {
         if (IsoPlayer.players[0] == null) {
            return true;
         } else {
            int var0 = IsoPlayer.players[0].getJoypadBind();
            if (var0 == -1) {
               return true;
            } else {
               return JoypadManager.instance.getLastActivity(var0) < Mouse.lastActivity;
            }
         }
      }

      @LuaMethod(
         name = "reactivateJoypadAfterResetLua",
         global = true
      )
      public static boolean reactivateJoypadAfterResetLua() {
         if (GameWindow.ActivatedJoyPad != null) {
            LuaEventManager.triggerEvent("OnJoypadActivateUI", GameWindow.ActivatedJoyPad.getID());
            return true;
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "isJoypadConnected",
         global = true
      )
      public static boolean isJoypadConnected(int var0) {
         return JoypadManager.instance.isJoypadConnected(var0);
      }

      private static void addPlayerToWorld(int var0, IsoPlayer var1, boolean var2) {
         if (IsoPlayer.players[var0] != null) {
            IsoPlayer.players[var0].getEmitter().stopAll();
            IsoPlayer.players[var0].getEmitter().unregister();
            IsoPlayer.players[var0].updateUsername();
            IsoPlayer.players[var0].setSceneCulled(true);
            IsoPlayer.players[var0] = null;
         }

         var1.PlayerIndex = var0;
         if (GameClient.bClient && var0 != 0 && var1.serverPlayerIndex == 1) {
            var1.serverPlayerIndex = ClientPlayerDB.getInstance().getNextServerPlayerIndex();
         }

         if (var0 == 0) {
            var1.sqlID = 1;
         }

         if (var2) {
            var1.applyTraits(IsoWorld.instance.getLuaTraits());
            var1.createKeyRing();
            ProfessionFactory.Profession var3 = ProfessionFactory.getProfession(var1.getDescriptor().getProfession());
            Iterator var4;
            String var5;
            if (var3 != null && !var3.getFreeRecipes().isEmpty()) {
               var4 = var3.getFreeRecipes().iterator();

               while(var4.hasNext()) {
                  var5 = (String)var4.next();
                  var1.getKnownRecipes().add(var5);
               }
            }

            var4 = IsoWorld.instance.getLuaTraits().iterator();

            label52:
            while(true) {
               TraitFactory.Trait var6;
               do {
                  do {
                     if (!var4.hasNext()) {
                        var1.setDir(IsoDirections.SE);
                        LuaEventManager.triggerEvent("OnNewGame", var1, var1.getCurrentSquare());
                        break label52;
                     }

                     var5 = (String)var4.next();
                     var6 = TraitFactory.getTrait(var5);
                  } while(var6 == null);
               } while(var6.getFreeRecipes().isEmpty());

               Iterator var7 = var6.getFreeRecipes().iterator();

               while(var7.hasNext()) {
                  String var8 = (String)var7.next();
                  var1.getKnownRecipes().add(var8);
               }
            }
         }

         IsoPlayer.numPlayers = Math.max(IsoPlayer.numPlayers, var0 + 1);
         IsoWorld.instance.AddCoopPlayers.add(new AddCoopPlayer(var1));
         if (var0 == 0) {
            IsoPlayer.setInstance(var1);
         }

      }

      @LuaMethod(
         name = "toInt",
         global = true
      )
      public static int toInt(double var0) {
         return (int)var0;
      }

      @LuaMethod(
         name = "getClientUsername",
         global = true
      )
      public static String getClientUsername() {
         return GameClient.bClient ? GameClient.username : null;
      }

      @LuaMethod(
         name = "setPlayerJoypad",
         global = true
      )
      public static void setPlayerJoypad(int var0, int var1, IsoPlayer var2, String var3) {
         if (IsoPlayer.players[var0] == null || IsoPlayer.players[var0].isDead()) {
            boolean var4 = var2 == null;
            if (var2 == null) {
               IsoPlayer var5 = IsoPlayer.getInstance();
               IsoWorld var6 = IsoWorld.instance;
               int var7 = var6.getLuaPosX() + 300 * var6.getLuaSpawnCellX();
               int var8 = var6.getLuaPosY() + 300 * var6.getLuaSpawnCellY();
               int var9 = var6.getLuaPosZ();
               DebugLog.Lua.debugln("coop player spawning at " + var7 + "," + var8 + "," + var9);
               var2 = new IsoPlayer(var6.CurrentCell, var6.getLuaPlayerDesc(), var7, var8, var9);
               IsoPlayer.setInstance(var5);
               var6.CurrentCell.getAddList().remove(var2);
               var6.CurrentCell.getObjectList().remove(var2);
               var2.SaveFileName = IsoPlayer.getUniqueFileName();
            }

            if (GameClient.bClient) {
               if (var3 != null) {
                  assert var0 != 0;

                  var2.username = var3;
                  var2.getModData().rawset("username", var3);
               } else {
                  assert var0 == 0;

                  var2.username = GameClient.username;
               }
            }

            addPlayerToWorld(var0, var2, var4);
         }

         var2.JoypadBind = var1;
         JoypadManager.instance.assignJoypad(var1, var0);
      }

      @LuaMethod(
         name = "setPlayerMouse",
         global = true
      )
      public static void setPlayerMouse(IsoPlayer var0) {
         byte var1 = 0;
         boolean var2 = var0 == null;
         if (var0 == null) {
            IsoPlayer var3 = IsoPlayer.getInstance();
            IsoWorld var4 = IsoWorld.instance;
            int var5 = var4.getLuaPosX() + 300 * var4.getLuaSpawnCellX();
            int var6 = var4.getLuaPosY() + 300 * var4.getLuaSpawnCellY();
            int var7 = var4.getLuaPosZ();
            DebugLog.Lua.debugln("coop player spawning at " + var5 + "," + var6 + "," + var7);
            var0 = new IsoPlayer(var4.CurrentCell, var4.getLuaPlayerDesc(), var5, var6, var7);
            IsoPlayer.setInstance(var3);
            var4.CurrentCell.getAddList().remove(var0);
            var4.CurrentCell.getObjectList().remove(var0);
            var0.SaveFileName = null;
         }

         if (GameClient.bClient) {
            var0.username = GameClient.username;
         }

         addPlayerToWorld(var1, var0, var2);
      }

      @LuaMethod(
         name = "revertToKeyboardAndMouse",
         global = true
      )
      public static void revertToKeyboardAndMouse() {
         JoypadManager.instance.revertToKeyboardAndMouse();
      }

      @LuaMethod(
         name = "isJoypadUp",
         global = true
      )
      public static boolean isJoypadUp(int var0) {
         return JoypadManager.instance.isUpPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadLeft",
         global = true
      )
      public static boolean isJoypadLeft(int var0) {
         return JoypadManager.instance.isLeftPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadRight",
         global = true
      )
      public static boolean isJoypadRight(int var0) {
         return JoypadManager.instance.isRightPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadLBPressed",
         global = true
      )
      public static boolean isJoypadLBPressed(int var0) {
         return JoypadManager.instance.isLBPressed(var0);
      }

      @LuaMethod(
         name = "isJoypadRBPressed",
         global = true
      )
      public static boolean isJoypadRBPressed(int var0) {
         return JoypadManager.instance.isRBPressed(var0);
      }

      @LuaMethod(
         name = "getButtonCount",
         global = true
      )
      public static int getButtonCount(int var0) {
         if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            return var1 == null ? 0 : var1.getButtonCount();
         } else {
            return 0;
         }
      }

      @LuaMethod(
         name = "setDebugToggleControllerPluggedIn",
         global = true
      )
      public static void setDebugToggleControllerPluggedIn(int var0) {
         Controllers.setDebugToggleControllerPluggedIn(var0);
      }

      @LuaMethod(
         name = "getFileWriter",
         global = true
      )
      public static LuaManager.GlobalObject.LuaFileWriter getFileWriter(String var0, boolean var1, boolean var2) {
         if (var0.contains("..")) {
            DebugLog.Lua.warn("relative paths not allowed");
            return null;
         } else {
            String var10000 = LuaManager.getLuaCacheDir();
            String var3 = var10000 + File.separator + var0;
            var3 = var3.replace("/", File.separator);
            var3 = var3.replace("\\", File.separator);
            String var4 = var3.substring(0, var3.lastIndexOf(File.separator));
            var4 = var4.replace("\\", "/");
            File var5 = new File(var4);
            if (!var5.exists()) {
               var5.mkdirs();
            }

            File var6 = new File(var3);
            if (!var6.exists() && var1) {
               try {
                  var6.createNewFile();
               } catch (IOException var11) {
                  Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var11);
               }
            }

            PrintWriter var7 = null;

            try {
               FileOutputStream var8 = new FileOutputStream(var6, var2);
               OutputStreamWriter var9 = new OutputStreamWriter(var8, StandardCharsets.UTF_8);
               var7 = new PrintWriter(var9);
            } catch (IOException var10) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var10);
            }

            return new LuaManager.GlobalObject.LuaFileWriter(var7);
         }
      }

      @LuaMethod(
         name = "getSandboxFileWriter",
         global = true
      )
      public static LuaManager.GlobalObject.LuaFileWriter getSandboxFileWriter(String var0, boolean var1, boolean var2) {
         String var10000 = LuaManager.getSandboxCacheDir();
         String var3 = var10000 + File.separator + var0;
         var3 = var3.replace("/", File.separator);
         var3 = var3.replace("\\", File.separator);
         String var4 = var3.substring(0, var3.lastIndexOf(File.separator));
         var4 = var4.replace("\\", "/");
         File var5 = new File(var4);
         if (!var5.exists()) {
            var5.mkdirs();
         }

         File var6 = new File(var3);
         if (!var6.exists() && var1) {
            try {
               var6.createNewFile();
            } catch (IOException var11) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var11);
            }
         }

         PrintWriter var7 = null;

         try {
            FileOutputStream var8 = new FileOutputStream(var6, var2);
            OutputStreamWriter var9 = new OutputStreamWriter(var8, StandardCharsets.UTF_8);
            var7 = new PrintWriter(var9);
         } catch (IOException var10) {
            Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var10);
         }

         return new LuaManager.GlobalObject.LuaFileWriter(var7);
      }

      @LuaMethod(
         name = "createStory",
         global = true
      )
      public static void createStory(String var0) {
         Core.GameMode = var0;
         String var1 = ZomboidFileSystem.instance.getGameModeCacheDir();
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         int var2 = 1;
         File var3 = null;
         boolean var4 = false;

         while(!var4) {
            var3 = new File(var1 + File.separator + "Game" + var2);
            if (!var3.exists()) {
               var4 = true;
            } else {
               ++var2;
            }
         }

         Core.GameSaveWorld = "newstory";
      }

      @LuaMethod(
         name = "createWorld",
         global = true
      )
      public static void createWorld(String var0) {
         if (var0 == null || var0.isEmpty()) {
            var0 = "blah";
         }

         var0 = sanitizeWorldName(var0);
         String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
         String var1 = var10000 + File.separator + var0 + File.separator;
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         String var2 = var1.substring(0, var1.lastIndexOf(File.separator));
         var2 = var2.replace("\\", "/");
         File var3 = new File(var2);
         if (!var3.exists() && !Core.getInstance().isNoSave()) {
            var3.mkdirs();
         }

         Core.GameSaveWorld = var0;
      }

      @LuaMethod(
         name = "sanitizeWorldName",
         global = true
      )
      public static String sanitizeWorldName(String var0) {
         return var0.replace(" ", "_").replace("/", "").replace("\\", "").replace("?", "").replace("*", "").replace("<", "").replace(">", "").replace(":", "").replace("|", "").trim();
      }

      @LuaMethod(
         name = "forceChangeState",
         global = true
      )
      public static void forceChangeState(GameState var0) {
         GameWindow.states.forceNextState(var0);
      }

      @LuaMethod(
         name = "endFileOutput",
         global = true
      )
      public static void endFileOutput() {
         if (outStream != null) {
            try {
               outStream.close();
            } catch (IOException var1) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var1);
            }
         }

         outStream = null;
      }

      @LuaMethod(
         name = "getFileInput",
         global = true
      )
      public static DataInputStream getFileInput(String var0) throws IOException {
         String var10000 = LuaManager.getLuaCacheDir();
         String var1 = var10000 + File.separator + var0;
         var1 = var1.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(var1);
         if (var2.exists()) {
            try {
               inStream = new FileInputStream(var2);
            } catch (FileNotFoundException var4) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var4);
            }

            DataInputStream var3 = new DataInputStream(inStream);
            return var3;
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "getGameFilesInput",
         global = true
      )
      public static DataInputStream getGameFilesInput(String var0) {
         String var1 = var0.replace("/", File.separator);
         var1 = var1.replace("\\", File.separator);
         File var2 = new File(ZomboidFileSystem.instance.getString(var1));
         if (var2.exists()) {
            try {
               inStream = new FileInputStream(var2);
            } catch (FileNotFoundException var4) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var4);
            }

            DataInputStream var3 = new DataInputStream(inStream);
            return var3;
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "getGameFilesTextInput",
         global = true
      )
      public static BufferedReader getGameFilesTextInput(String var0) {
         if (!Core.getInstance().getDebug()) {
            return null;
         } else {
            String var1 = var0.replace("/", File.separator);
            var1 = var1.replace("\\", File.separator);
            File var2 = new File(var1);
            if (var2.exists()) {
               try {
                  inFileReader = new FileReader(var0);
                  inBufferedReader = new BufferedReader(inFileReader);
                  return inBufferedReader;
               } catch (FileNotFoundException var4) {
                  Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var4);
               }
            }

            return null;
         }
      }

      @LuaMethod(
         name = "endTextFileInput",
         global = true
      )
      public static void endTextFileInput() {
         if (inBufferedReader != null) {
            try {
               inBufferedReader.close();
               inFileReader.close();
            } catch (IOException var1) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var1);
            }
         }

         inBufferedReader = null;
         inFileReader = null;
      }

      @LuaMethod(
         name = "endFileInput",
         global = true
      )
      public static void endFileInput() {
         if (inStream != null) {
            try {
               inStream.close();
            } catch (IOException var1) {
               Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String)null, var1);
            }
         }

         inStream = null;
      }

      @LuaMethod(
         name = "getLineNumber",
         global = true
      )
      public static int getLineNumber(LuaCallFrame var0) {
         if (var0.closure == null) {
            return 0;
         } else {
            int var1 = var0.pc;
            if (var1 < 0) {
               var1 = 0;
            }

            if (var1 >= var0.closure.prototype.lines.length) {
               var1 = var0.closure.prototype.lines.length - 1;
            }

            return var0.closure.prototype.lines[var1];
         }
      }

      @LuaMethod(
         name = "ZombRand",
         global = true
      )
      public static double ZombRand(double var0) {
         if (var0 == 0.0D) {
            return 0.0D;
         } else {
            return var0 < 0.0D ? (double)(-Rand.Next(-((long)var0), Rand.randlua)) : (double)Rand.Next((long)var0, Rand.randlua);
         }
      }

      @LuaMethod(
         name = "ZombRandBetween",
         global = true
      )
      public static double ZombRandBetween(double var0, double var2) {
         return (double)Rand.Next((long)var0, (long)var2, Rand.randlua);
      }

      @LuaMethod(
         name = "ZombRand",
         global = true
      )
      public static double ZombRand(double var0, double var2) {
         return (double)Rand.Next((int)var0, (int)var2, Rand.randlua);
      }

      @LuaMethod(
         name = "ZombRandFloat",
         global = true
      )
      public static float ZombRandFloat(float var0, float var1) {
         return Rand.Next(var0, var1, Rand.randlua);
      }

      @LuaMethod(
         name = "getShortenedFilename",
         global = true
      )
      public static String getShortenedFilename(String var0) {
         return var0.substring(var0.indexOf("lua/") + 4);
      }

      @LuaMethod(
         name = "isKeyDown",
         global = true
      )
      public static boolean isKeyDown(int var0) {
         return GameKeyboard.isKeyDown(var0);
      }

      @LuaMethod(
         name = "wasKeyDown",
         global = true
      )
      public static boolean wasKeyDown(int var0) {
         return GameKeyboard.wasKeyDown(var0);
      }

      @LuaMethod(
         name = "isKeyPressed",
         global = true
      )
      public static boolean isKeyPressed(int var0) {
         return GameKeyboard.isKeyPressed(var0);
      }

      @LuaMethod(
         name = "getFMODSoundBank",
         global = true
      )
      public static BaseSoundBank getFMODSoundBank() {
         return BaseSoundBank.instance;
      }

      @LuaMethod(
         name = "isSoundPlaying",
         global = true
      )
      public static boolean isSoundPlaying(Object var0) {
         return var0 instanceof Double ? FMODManager.instance.isPlaying(((Double)var0).longValue()) : false;
      }

      @LuaMethod(
         name = "stopSound",
         global = true
      )
      public static void stopSound(long var0) {
         FMODManager.instance.stopSound(var0);
      }

      @LuaMethod(
         name = "isShiftKeyDown",
         global = true
      )
      public static boolean isShiftKeyDown() {
         return GameKeyboard.isKeyDown(42) || GameKeyboard.isKeyDown(54);
      }

      @LuaMethod(
         name = "isCtrlKeyDown",
         global = true
      )
      public static boolean isCtrlKeyDown() {
         return GameKeyboard.isKeyDown(29) || GameKeyboard.isKeyDown(157);
      }

      @LuaMethod(
         name = "isAltKeyDown",
         global = true
      )
      public static boolean isAltKeyDown() {
         return GameKeyboard.isKeyDown(56) || GameKeyboard.isKeyDown(184);
      }

      @LuaMethod(
         name = "getCore",
         global = true
      )
      public static Core getCore() {
         return Core.getInstance();
      }

      @LuaMethod(
         name = "getSquare",
         global = true
      )
      public static IsoGridSquare getSquare(double var0, double var2, double var4) {
         return IsoCell.getInstance().getGridSquare(var0, var2, var4);
      }

      @LuaMethod(
         name = "getDebugOptions",
         global = true
      )
      public static DebugOptions getDebugOptions() {
         return DebugOptions.instance;
      }

      @LuaMethod(
         name = "setShowPausedMessage",
         global = true
      )
      public static void setShowPausedMessage(boolean var0) {
         DebugLog.log("EXITDEBUG: setShowPausedMessage 1");
         UIManager.setShowPausedMessage(var0);
         DebugLog.log("EXITDEBUG: setShowPausedMessage 2");
      }

      @LuaMethod(
         name = "getFilenameOfCallframe",
         global = true
      )
      public static String getFilenameOfCallframe(LuaCallFrame var0) {
         return var0.closure == null ? null : var0.closure.prototype.filename;
      }

      @LuaMethod(
         name = "getFilenameOfClosure",
         global = true
      )
      public static String getFilenameOfClosure(LuaClosure var0) {
         return var0 == null ? null : var0.prototype.filename;
      }

      @LuaMethod(
         name = "getFirstLineOfClosure",
         global = true
      )
      public static int getFirstLineOfClosure(LuaClosure var0) {
         return var0 == null ? 0 : var0.prototype.lines[0];
      }

      @LuaMethod(
         name = "getLocalVarCount",
         global = true
      )
      public static int getLocalVarCount(Coroutine var0) {
         LuaCallFrame var1 = var0.currentCallFrame();
         return var1 == null ? 0 : var1.LocalVarNames.size();
      }

      @LuaMethod(
         name = "isSystemLinux",
         global = true
      )
      public static boolean isSystemLinux() {
         return !isSystemMacOS() && !isSystemWindows();
      }

      @LuaMethod(
         name = "isSystemMacOS",
         global = true
      )
      public static boolean isSystemMacOS() {
         return System.getProperty("os.name").contains("OS X");
      }

      @LuaMethod(
         name = "isSystemWindows",
         global = true
      )
      public static boolean isSystemWindows() {
         return System.getProperty("os.name").startsWith("Win");
      }

      @LuaMethod(
         name = "isModActive",
         global = true
      )
      public static boolean isModActive(ChooseGameInfo.Mod var0) {
         String var1 = var0.getDir();
         if (!StringUtils.isNullOrWhitespace(var0.getId())) {
            var1 = var0.getId();
         }

         return ZomboidFileSystem.instance.getModIDs().contains(var1);
      }

      @LuaMethod(
         name = "openUrl",
         global = true
      )
      public static void openURl(String var0) {
         Desktop var1 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
         if (var1 != null && var1.isSupported(Action.BROWSE)) {
            try {
               URI var2 = new URI(var0);
               var1.browse(var2);
            } catch (Exception var3) {
               ExceptionLogger.logException(var3);
            }
         }

      }

      @LuaMethod(
         name = "isDesktopOpenSupported",
         global = true
      )
      public static boolean isDesktopOpenSupported() {
         return !Desktop.isDesktopSupported() ? false : Desktop.getDesktop().isSupported(Action.OPEN);
      }

      @LuaMethod(
         name = "showFolderInDesktop",
         global = true
      )
      public static void showFolderInDesktop(String var0) {
         File var1 = new File(var0);
         if (var1.exists() && var1.isDirectory()) {
            Desktop var2 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (var2 != null && var2.isSupported(Action.OPEN)) {
               try {
                  var2.open(var1);
               } catch (Exception var4) {
                  ExceptionLogger.logException(var4);
               }
            }

         }
      }

      @LuaMethod(
         name = "getActivatedMods",
         global = true
      )
      public static ArrayList getActivatedMods() {
         return ZomboidFileSystem.instance.getModIDs();
      }

      @LuaMethod(
         name = "toggleModActive",
         global = true
      )
      public static void toggleModActive(ChooseGameInfo.Mod var0, boolean var1) {
         String var2 = var0.getDir();
         if (!StringUtils.isNullOrWhitespace(var0.getId())) {
            var2 = var0.getId();
         }

         ActiveMods.getById("default").setModActive(var2, var1);
      }

      @LuaMethod(
         name = "saveModsFile",
         global = true
      )
      public static void saveModsFile() {
         ZomboidFileSystem.instance.saveModsFile();
      }

      private static void deleteSavefileFilesMatching(File var0, String var1) {
         Filter var2 = (var1x) -> {
            return var1x.getFileName().toString().matches(var1);
         };

         try {
            DirectoryStream var3 = Files.newDirectoryStream(var0.toPath(), var2);

            try {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Path var5 = (Path)var4.next();
                  System.out.println("DELETE " + var5);
                  Files.deleteIfExists(var5);
               }
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var8) {
            ExceptionLogger.logException(var8);
         }

      }

      @LuaMethod(
         name = "manipulateSavefile",
         global = true
      )
      public static void manipulateSavefile(String var0, String var1) {
         if (!StringUtils.isNullOrWhitespace(var0)) {
            if (!var0.contains("..")) {
               String var10000 = ZomboidFileSystem.instance.getSaveDir();
               String var2 = var10000 + File.separator + var0;
               File var3 = new File(var2);
               if (var3.exists() && var3.isDirectory()) {
                  byte var5 = -1;
                  switch(var1.hashCode()) {
                  case -2086819803:
                     if (var1.equals("DeletePlayersDB")) {
                        var5 = 5;
                     }
                     break;
                  case -1291996175:
                     if (var1.equals("DeleteMapMetaBin")) {
                        var5 = 2;
                     }
                     break;
                  case -1139427258:
                     if (var1.equals("DeleteReanimatedBin")) {
                        var5 = 6;
                     }
                     break;
                  case -1035136361:
                     if (var1.equals("WriteModsDotTxt")) {
                        var5 = 11;
                     }
                     break;
                  case -970886236:
                     if (var1.equals("DeleteZPopXYBin")) {
                        var5 = 10;
                     }
                     break;
                  case -351123098:
                     if (var1.equals("DeleteZOutfitsBin")) {
                        var5 = 8;
                     }
                     break;
                  case 305594266:
                     if (var1.equals("DeleteChunkDataXYBin")) {
                        var5 = 0;
                     }
                     break;
                  case 552703408:
                     if (var1.equals("DeleteVehiclesDB")) {
                        var5 = 7;
                     }
                     break;
                  case 1411759445:
                     if (var1.equals("DeleteMapXYBin")) {
                        var5 = 1;
                     }
                     break;
                  case 1430873892:
                     if (var1.equals("DeleteMapTBin")) {
                        var5 = 3;
                     }
                     break;
                  case 1760896894:
                     if (var1.equals("DeleteZPopVirtualBin")) {
                        var5 = 9;
                     }
                     break;
                  case 1936486634:
                     if (var1.equals("DeleteMapZoneBin")) {
                        var5 = 4;
                     }
                  }

                  switch(var5) {
                  case 0:
                     deleteSavefileFilesMatching(var3, "chunkdata_[0-9]+_[0-9]+\\.bin");
                     break;
                  case 1:
                     deleteSavefileFilesMatching(var3, "map_[0-9]+_[0-9]+\\.bin");
                     break;
                  case 2:
                     deleteSavefileFilesMatching(var3, "map_meta\\.bin");
                     break;
                  case 3:
                     deleteSavefileFilesMatching(var3, "map_t\\.bin");
                     break;
                  case 4:
                     deleteSavefileFilesMatching(var3, "map_zone\\.bin");
                     break;
                  case 5:
                     deleteSavefileFilesMatching(var3, "players\\.db");
                     break;
                  case 6:
                     deleteSavefileFilesMatching(var3, "reanimated\\.bin");
                     break;
                  case 7:
                     deleteSavefileFilesMatching(var3, "vehicles\\.db");
                     break;
                  case 8:
                     deleteSavefileFilesMatching(var3, "z_outfits\\.bin");
                     break;
                  case 9:
                     deleteSavefileFilesMatching(var3, "zpop_virtual\\.bin");
                     break;
                  case 10:
                     deleteSavefileFilesMatching(var3, "zpop_[0-9]+_[0-9]+\\.bin");
                     break;
                  case 11:
                     ActiveMods var6 = ActiveMods.getById("currentGame");
                     ActiveModsFile var7 = new ActiveModsFile();
                     var7.write(var2 + File.separator + "mods.txt", var6);
                     break;
                  default:
                     throw new IllegalArgumentException("unknown action \"" + var1 + "\"");
                  }

               }
            }
         }
      }

      @LuaMethod(
         name = "getLocalVarName",
         global = true
      )
      public static String getLocalVarName(Coroutine var0, int var1) {
         LuaCallFrame var2 = var0.currentCallFrame();
         return (String)var2.LocalVarNames.get(var1);
      }

      @LuaMethod(
         name = "getLocalVarStack",
         global = true
      )
      public static int getLocalVarStack(Coroutine var0, int var1) {
         LuaCallFrame var2 = var0.currentCallFrame();
         return (Integer)var2.LocalVarToStackMap.get(var2.LocalVarNames.get(var1));
      }

      @LuaMethod(
         name = "getCallframeTop",
         global = true
      )
      public static int getCallframeTop(Coroutine var0) {
         return var0.getCallframeTop();
      }

      @LuaMethod(
         name = "getCoroutineTop",
         global = true
      )
      public static int getCoroutineTop(Coroutine var0) {
         return var0.getTop();
      }

      @LuaMethod(
         name = "getCoroutineObjStack",
         global = true
      )
      public static Object getCoroutineObjStack(Coroutine var0, int var1) {
         return var0.getObjectFromStack(var1);
      }

      @LuaMethod(
         name = "getCoroutineObjStackWithBase",
         global = true
      )
      public static Object getCoroutineObjStackWithBase(Coroutine var0, int var1) {
         return var0.getObjectFromStack(var1 - var0.currentCallFrame().localBase);
      }

      @LuaMethod(
         name = "localVarName",
         global = true
      )
      public static String localVarName(Coroutine var0, int var1) {
         int var2 = var0.getCallframeTop() - 1;
         if (var2 < 0) {
            boolean var3 = false;
         }

         return "";
      }

      @LuaMethod(
         name = "getCoroutineCallframeStack",
         global = true
      )
      public static LuaCallFrame getCoroutineCallframeStack(Coroutine var0, int var1) {
         return var0.getCallFrame(var1);
      }

      @LuaMethod(
         name = "createTile",
         global = true
      )
      public static void createTile(String var0, IsoGridSquare var1) {
         synchronized(IsoSpriteManager.instance.NamedMap) {
            IsoSprite var3 = (IsoSprite)IsoSpriteManager.instance.NamedMap.get(var0);
            if (var3 != null) {
               int var4 = 0;
               int var5 = 0;
               int var6 = 0;
               if (var1 != null) {
                  var4 = var1.getX();
                  var5 = var1.getY();
                  var6 = var1.getZ();
               }

               CellLoader.DoTileObjectCreation(var3, var3.getType(), var1, IsoWorld.instance.CurrentCell, var4, var5, var6, var0);
            }
         }
      }

      @LuaMethod(
         name = "getNumClassFunctions",
         global = true
      )
      public static int getNumClassFunctions(Object var0) {
         return var0.getClass().getDeclaredMethods().length;
      }

      @LuaMethod(
         name = "getClassFunction",
         global = true
      )
      public static Method getClassFunction(Object var0, int var1) {
         Method var2 = var0.getClass().getDeclaredMethods()[var1];
         return var2;
      }

      @LuaMethod(
         name = "getNumClassFields",
         global = true
      )
      public static int getNumClassFields(Object var0) {
         return var0.getClass().getDeclaredFields().length;
      }

      @LuaMethod(
         name = "getClassField",
         global = true
      )
      public static Field getClassField(Object var0, int var1) {
         Field var2 = var0.getClass().getDeclaredFields()[var1];
         var2.setAccessible(true);
         return var2;
      }

      @LuaMethod(
         name = "getDirectionTo",
         global = true
      )
      public static IsoDirections getDirectionTo(IsoGameCharacter var0, IsoObject var1) {
         Vector2 var2 = new Vector2(var1.getX(), var1.getY());
         var2.x -= var0.x;
         var2.y -= var0.y;
         return IsoDirections.fromAngle(var2);
      }

      @LuaMethod(
         name = "translatePointXInOverheadMapToWindow",
         global = true
      )
      public static float translatePointXInOverheadMapToWindow(float var0, UIElement var1, float var2, float var3) {
         IngameState.draww = (float)var1.getWidth().intValue();
         return IngameState.translatePointX(var0, var3, var2, 0.0F);
      }

      @LuaMethod(
         name = "translatePointYInOverheadMapToWindow",
         global = true
      )
      public static float translatePointYInOverheadMapToWindow(float var0, UIElement var1, float var2, float var3) {
         IngameState.drawh = (float)var1.getHeight().intValue();
         return IngameState.translatePointY(var0, var3, var2, 0.0F);
      }

      @LuaMethod(
         name = "translatePointXInOverheadMapToWorld",
         global = true
      )
      public static float translatePointXInOverheadMapToWorld(float var0, UIElement var1, float var2, float var3) {
         IngameState.draww = (float)var1.getWidth().intValue();
         return IngameState.invTranslatePointX(var0, var3, var2, 0.0F);
      }

      @LuaMethod(
         name = "translatePointYInOverheadMapToWorld",
         global = true
      )
      public static float translatePointYInOverheadMapToWorld(float var0, UIElement var1, float var2, float var3) {
         IngameState.drawh = (float)var1.getHeight().intValue();
         return IngameState.invTranslatePointY(var0, var3, var2, 0.0F);
      }

      @LuaMethod(
         name = "drawOverheadMap",
         global = true
      )
      public static void drawOverheadMap(UIElement var0, float var1, float var2, float var3) {
         IngameState.renderDebugOverhead2(getCell(), 0, var1, var0.getAbsoluteX().intValue(), var0.getAbsoluteY().intValue(), var2, var3, var0.getWidth().intValue(), var0.getHeight().intValue());
      }

      @LuaMethod(
         name = "assaultPlayer",
         global = true
      )
      public static void assaultPlayer() {
         assert false;

      }

      @LuaMethod(
         name = "isoRegionsRenderer",
         global = true
      )
      public static IsoRegionsRenderer isoRegionsRenderer() {
         return new IsoRegionsRenderer();
      }

      @LuaMethod(
         name = "zpopNewRenderer",
         global = true
      )
      public static ZombiePopulationRenderer zpopNewRenderer() {
         return new ZombiePopulationRenderer();
      }

      @LuaMethod(
         name = "zpopSpawnTimeToZero",
         global = true
      )
      public static void zpopSpawnTimeToZero(int var0, int var1) {
         ZombiePopulationManager.instance.dbgSpawnTimeToZero(var0, var1);
      }

      @LuaMethod(
         name = "zpopClearZombies",
         global = true
      )
      public static void zpopClearZombies(int var0, int var1) {
         ZombiePopulationManager.instance.dbgClearZombies(var0, var1);
      }

      @LuaMethod(
         name = "zpopSpawnNow",
         global = true
      )
      public static void zpopSpawnNow(int var0, int var1) {
         ZombiePopulationManager.instance.dbgSpawnNow(var0, var1);
      }

      @LuaMethod(
         name = "addVirtualZombie",
         global = true
      )
      public static void addVirtualZombie(int var0, int var1) {
      }

      @LuaMethod(
         name = "luaDebug",
         global = true
      )
      public static void luaDebug() {
         try {
            throw new Exception("LuaDebug");
         } catch (Exception var1) {
            var1.printStackTrace();
         }
      }

      @LuaMethod(
         name = "setAggroTarget",
         global = true
      )
      public static void setAggroTarget(int var0, int var1, int var2) {
         ZombiePopulationManager.instance.setAggroTarget(var0, var1, var2);
      }

      @LuaMethod(
         name = "debugFullyStreamedIn",
         global = true
      )
      public static void debugFullyStreamedIn(int var0, int var1) {
         IngameState.instance.debugFullyStreamedIn(var0, var1);
      }

      @LuaMethod(
         name = "getClassFieldVal",
         global = true
      )
      public static Object getClassFieldVal(Object var0, Field var1) {
         try {
            return var1.get(var0);
         } catch (Exception var3) {
            return "<private>";
         }
      }

      @LuaMethod(
         name = "getMethodParameter",
         global = true
      )
      public static String getMethodParameter(Method var0, int var1) {
         return var0.getParameterTypes()[var1].getSimpleName();
      }

      @LuaMethod(
         name = "getMethodParameterCount",
         global = true
      )
      public static int getMethodParameterCount(Method var0) {
         return var0.getParameterTypes().length;
      }

      @LuaMethod(
         name = "breakpoint",
         global = true
      )
      public static void breakpoint() {
         boolean var0 = false;
      }

      @LuaMethod(
         name = "getLuaDebuggerErrorCount",
         global = true
      )
      public static int getLuaDebuggerErrorCount() {
         KahluaThread var10000 = LuaManager.thread;
         return KahluaThread.m_error_count;
      }

      @LuaMethod(
         name = "getLuaDebuggerErrors",
         global = true
      )
      public static ArrayList getLuaDebuggerErrors() {
         KahluaThread var10002 = LuaManager.thread;
         ArrayList var0 = new ArrayList(KahluaThread.m_errors_list);
         return var0;
      }

      @LuaMethod(
         name = "getGameSpeed",
         global = true
      )
      public static int getGameSpeed() {
         return UIManager.getSpeedControls() != null ? UIManager.getSpeedControls().getCurrentGameSpeed() : 0;
      }

      @LuaMethod(
         name = "setGameSpeed",
         global = true
      )
      public static void setGameSpeed(int var0) {
         DebugLog.log("EXITDEBUG: setGameSpeed 1");
         if (UIManager.getSpeedControls() == null) {
            DebugLog.log("EXITDEBUG: setGameSpeed 2");
         } else {
            UIManager.getSpeedControls().SetCurrentGameSpeed(var0);
            DebugLog.log("EXITDEBUG: setGameSpeed 3");
         }
      }

      @LuaMethod(
         name = "isGamePaused",
         global = true
      )
      public static boolean isGamePaused() {
         return GameTime.isGamePaused();
      }

      @LuaMethod(
         name = "getMouseXScaled",
         global = true
      )
      public static int getMouseXScaled() {
         return Mouse.getX();
      }

      @LuaMethod(
         name = "getMouseYScaled",
         global = true
      )
      public static int getMouseYScaled() {
         return Mouse.getY();
      }

      @LuaMethod(
         name = "getMouseX",
         global = true
      )
      public static int getMouseX() {
         return Mouse.getXA();
      }

      @LuaMethod(
         name = "setMouseXY",
         global = true
      )
      public static void setMouseXY(int var0, int var1) {
         Mouse.setXY(var0, var1);
      }

      @LuaMethod(
         name = "isMouseButtonDown",
         global = true
      )
      public static boolean isMouseButtonDown(int var0) {
         return Mouse.isButtonDown(var0);
      }

      @LuaMethod(
         name = "getMouseY",
         global = true
      )
      public static int getMouseY() {
         return Mouse.getYA();
      }

      @LuaMethod(
         name = "getSoundManager",
         global = true
      )
      public static BaseSoundManager getSoundManager() {
         return SoundManager.instance;
      }

      @LuaMethod(
         name = "getLastPlayedDate",
         global = true
      )
      public static String getLastPlayedDate(String var0) {
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator + var0);
         if (!var1.exists()) {
            return Translator.getText("UI_LastPlayed") + "???";
         } else {
            Date var2 = new Date(var1.lastModified());
            SimpleDateFormat var3 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String var4 = var3.format(var2);
            String var10000 = Translator.getText("UI_LastPlayed");
            return var10000 + var4;
         }
      }

      @LuaMethod(
         name = "getTextureFromSaveDir",
         global = true
      )
      public static Texture getTextureFromSaveDir(String var0, String var1) {
         TextureID.UseFiltering = true;
         String var2 = ZomboidFileSystem.instance.getSaveDir() + File.separator + var1 + File.separator + var0;
         Texture var3 = Texture.getSharedTexture(var2);
         TextureID.UseFiltering = false;
         return var3;
      }

      @LuaMethod(
         name = "getSaveInfo",
         global = true
      )
      public static KahluaTable getSaveInfo(String var0) {
         String var10000;
         if (!var0.contains(File.separator)) {
            var10000 = IsoWorld.instance.getGameMode();
            var0 = var10000 + File.separator + var0;
         }

         KahluaTable var1 = LuaManager.platform.newTable();
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var2 = new File(var10002 + File.separator + var0);
         if (var2.exists()) {
            Date var3 = new Date(var2.lastModified());
            SimpleDateFormat var4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String var5 = var4.format(var3);
            var1.rawset("lastPlayed", var5);
            String[] var6 = var0.split("\\" + File.separator);
            var1.rawset("saveName", var2.getName());
            var1.rawset("gameMode", var6[var6.length - 2]);
         }

         var10002 = ZomboidFileSystem.instance.getSaveDir();
         var2 = new File(var10002 + File.separator + var0 + File.separator + "map_ver.bin");
         String var28;
         if (var2.exists()) {
            try {
               FileInputStream var22 = new FileInputStream(var2);

               try {
                  DataInputStream var24 = new DataInputStream(var22);

                  try {
                     int var26 = var24.readInt();
                     var1.rawset("worldVersion", (double)var26);
                     if (var26 >= 18) {
                        try {
                           var28 = GameWindow.ReadString(var24);
                           if (var28.equals("DEFAULT")) {
                              var28 = "Muldraugh, KY";
                           }

                           var1.rawset("mapName", var28);
                        } catch (Exception var17) {
                        }
                     }

                     if (var26 >= 74) {
                        try {
                           var28 = GameWindow.ReadString(var24);
                           var1.rawset("difficulty", var28);
                        } catch (Exception var16) {
                        }
                     }
                  } catch (Throwable var19) {
                     try {
                        var24.close();
                     } catch (Throwable var15) {
                        var19.addSuppressed(var15);
                     }

                     throw var19;
                  }

                  var24.close();
               } catch (Throwable var20) {
                  try {
                     var22.close();
                  } catch (Throwable var14) {
                     var20.addSuppressed(var14);
                  }

                  throw var20;
               }

               var22.close();
            } catch (Exception var21) {
               ExceptionLogger.logException(var21);
            }
         }

         var10000 = ZomboidFileSystem.instance.getSaveDir();
         String var23 = var10000 + File.separator + var0 + File.separator + "mods.txt";
         ActiveMods var25 = new ActiveMods(var0);
         ActiveModsFile var27 = new ActiveModsFile();
         if (var27.read(var23, var25)) {
            var1.rawset("activeMods", var25);
         }

         var10000 = ZomboidFileSystem.instance.getSaveDir();
         var28 = var10000 + File.separator + var0;
         var1.rawset("playerAlive", PlayerDBHelper.isPlayerAlive(var28, 1));
         KahluaTable var7 = LuaManager.platform.newTable();

         try {
            ArrayList var8 = PlayerDBHelper.getPlayers(var28);

            for(int var9 = 0; var9 < var8.size(); var9 += 3) {
               Double var10 = (Double)var8.get(var9);
               String var11 = (String)var8.get(var9 + 1);
               Boolean var12 = (Boolean)var8.get(var9 + 2);
               KahluaTable var13 = LuaManager.platform.newTable();
               var13.rawset("sqlID", var10);
               var13.rawset("name", var11);
               var13.rawset("isDead", var12);
               var7.rawset(var9 / 3 + 1, var13);
            }
         } catch (Exception var18) {
            ExceptionLogger.logException(var18);
         }

         var1.rawset("players", var7);
         return var1;
      }

      @LuaMethod(
         name = "setSavefilePlayer1",
         global = true
      )
      public static void setSavefilePlayer1(String var0, String var1, int var2) {
         String var3 = ZomboidFileSystem.instance.getSaveDirSub(var0 + File.separator + var1);

         try {
            PlayerDBHelper.setPlayer1(var3, var2);
         } catch (Exception var5) {
            ExceptionLogger.logException(var5);
         }

      }

      @LuaMethod(
         name = "getServerSavedWorldVersion",
         global = true
      )
      public static int getServerSavedWorldVersion(String var0) {
         String var10002 = ZomboidFileSystem.instance.getSaveDir();
         File var1 = new File(var10002 + File.separator + var0 + File.separator + "map_t.bin");
         if (var1.exists()) {
            try {
               FileInputStream var2 = new FileInputStream(var1);

               int var8;
               label64: {
                  byte var14;
                  try {
                     DataInputStream var3 = new DataInputStream(var2);

                     label60: {
                        try {
                           byte var4 = var3.readByte();
                           byte var5 = var3.readByte();
                           byte var6 = var3.readByte();
                           byte var7 = var3.readByte();
                           if (var4 != 71 || var5 != 77 || var6 != 84 || var7 != 77) {
                              var14 = 1;
                              break label60;
                           }

                           var8 = var3.readInt();
                        } catch (Throwable var11) {
                           try {
                              var3.close();
                           } catch (Throwable var10) {
                              var11.addSuppressed(var10);
                           }

                           throw var11;
                        }

                        var3.close();
                        break label64;
                     }

                     var3.close();
                  } catch (Throwable var12) {
                     try {
                        var2.close();
                     } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                     }

                     throw var12;
                  }

                  var2.close();
                  return var14;
               }

               var2.close();
               return var8;
            } catch (Exception var13) {
               var13.printStackTrace();
            }
         }

         return 0;
      }

      @LuaMethod(
         name = "getGameVersionInfo",
         global = true
      )
      public static KahluaTable getGameVersionInfo() {
         KahluaTable var0 = LuaManager.platform.newTable();
         var0.rawset("svnRevision", "");
         var0.rawset("buildDate", "");
         var0.rawset("buildTime", "");
         var0.rawset("version", Core.getInstance().getVersionNumber());
         return var0;
      }

      @LuaMethod(
         name = "getZombieInfo",
         global = true
      )
      public static KahluaTable getZombieInfo(IsoZombie var0) {
         KahluaTable var1 = LuaManager.platform.newTable();
         if (var0 == null) {
            return var1;
         } else {
            var1.rawset("OnlineID", var0.OnlineID);
            var1.rawset("RealX", var0.realx);
            var1.rawset("RealY", var0.realy);
            var1.rawset("X", var0.x);
            var1.rawset("Y", var0.y);
            var1.rawset("TargetX", var0.networkAI.targetX);
            var1.rawset("TargetY", var0.networkAI.targetY);
            var1.rawset("PathLength", var0.getPathFindBehavior2().getPathLength());
            var1.rawset("TargetLength", Math.sqrt((double)((var0.x - var0.getPathFindBehavior2().getTargetX()) * (var0.x - var0.getPathFindBehavior2().getTargetX()) + (var0.y - var0.getPathFindBehavior2().getTargetY()) * (var0.y - var0.getPathFindBehavior2().getTargetY()))));
            var1.rawset("clientActionState", var0.getActionStateName());
            var1.rawset("clientAnimationState", var0.getAnimationStateName());
            var1.rawset("finderProgress", var0.getFinder().progress.name());
            var1.rawset("usePathFind", Boolean.toString(var0.networkAI.usePathFind));
            var1.rawset("owner", var0.authOwner.username);
            var0.networkAI.DebugInterfaceActive = true;
            return var1;
         }
      }

      @LuaMethod(
         name = "getPlayerInfo",
         global = true
      )
      public static KahluaTable getPlayerInfo(IsoPlayer var0) {
         KahluaTable var1 = LuaManager.platform.newTable();
         if (var0 == null) {
            return var1;
         } else {
            long var2 = GameTime.getServerTime() / 1000000L;
            var1.rawset("OnlineID", var0.OnlineID);
            var1.rawset("RealX", var0.realx);
            var1.rawset("RealY", var0.realy);
            var1.rawset("X", var0.x);
            var1.rawset("Y", var0.y);
            var1.rawset("TargetX", var0.networkAI.targetX);
            var1.rawset("TargetY", var0.networkAI.targetY);
            var1.rawset("TargetT", var0.networkAI.targetZ);
            var1.rawset("ServerT", var2);
            var1.rawset("PathLength", var0.getPathFindBehavior2().getPathLength());
            var1.rawset("TargetLength", Math.sqrt((double)((var0.x - var0.getPathFindBehavior2().getTargetX()) * (var0.x - var0.getPathFindBehavior2().getTargetX()) + (var0.y - var0.getPathFindBehavior2().getTargetY()) * (var0.y - var0.getPathFindBehavior2().getTargetY()))));
            var1.rawset("clientActionState", var0.getActionStateName());
            var1.rawset("clientAnimationState", var0.getAnimationStateName());
            var1.rawset("finderProgress", var0.getFinder().progress.name());
            var1.rawset("usePathFind", Boolean.toString(var0.networkAI.usePathFind));
            return var1;
         }
      }

      @LuaMethod(
         name = "getMapInfo",
         global = true
      )
      public static KahluaTable getMapInfo(String var0) {
         if (var0.contains(";")) {
            var0 = var0.split(";")[0];
         }

         ChooseGameInfo.Map var1 = ChooseGameInfo.getMapDetails(var0);
         if (var1 == null) {
            return null;
         } else {
            KahluaTable var2 = LuaManager.platform.newTable();
            var2.rawset("description", var1.getDescription());
            var2.rawset("dir", var1.getDirectory());
            KahluaTable var3 = LuaManager.platform.newTable();
            byte var4 = 1;
            Iterator var5 = var1.getLotDirectories().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               var3.rawset((double)var4, var6);
            }

            var2.rawset("lots", var3);
            var2.rawset("thumb", var1.getThumbnail());
            var2.rawset("title", var1.getTitle());
            return var2;
         }
      }

      @LuaMethod(
         name = "getVehicleInfo",
         global = true
      )
      public static KahluaTable getVehicleInfo(BaseVehicle var0) {
         if (var0 == null) {
            return null;
         } else {
            KahluaTable var1 = LuaManager.platform.newTable();
            var1.rawset("name", var0.getScript().getName());
            var1.rawset("weight", var0.getMass());
            var1.rawset("speed", var0.getMaxSpeed());
            var1.rawset("frontEndDurability", Integer.toString(var0.frontEndDurability));
            var1.rawset("rearEndDurability", Integer.toString(var0.rearEndDurability));
            var1.rawset("currentFrontEndDurability", Integer.toString(var0.currentFrontEndDurability));
            var1.rawset("currentRearEndDurability", Integer.toString(var0.currentRearEndDurability));
            var1.rawset("engine_running", var0.isEngineRunning());
            var1.rawset("engine_started", var0.isEngineStarted());
            var1.rawset("engine_quality", var0.getEngineQuality());
            var1.rawset("engine_loudness", var0.getEngineLoudness());
            var1.rawset("engine_power", var0.getEnginePower());
            var1.rawset("battery_isset", var0.getBattery() != null);
            var1.rawset("battery_charge", var0.getBatteryCharge());
            var1.rawset("gas_amount", var0.getPartById("GasTank").getContainerContentAmount());
            var1.rawset("gas_capacity", var0.getPartById("GasTank").getContainerCapacity());
            VehiclePart var2 = var0.getPartById("DoorFrontLeft");
            var1.rawset("doorleft_exist", var2 != null);
            if (var2 != null) {
               var1.rawset("doorleft_open", var2.getDoor().isOpen());
               var1.rawset("doorleft_locked", var2.getDoor().isLocked());
               var1.rawset("doorleft_lockbroken", var2.getDoor().isLockBroken());
               VehicleWindow var3 = var2.findWindow();
               var1.rawset("windowleft_exist", var3 != null);
               if (var3 != null) {
                  var1.rawset("windowleft_open", var3.isOpen());
                  var1.rawset("windowleft_health", var3.getHealth());
               }
            }

            VehiclePart var5 = var0.getPartById("DoorFrontRight");
            var1.rawset("doorright_exist", var5 != null);
            if (var2 != null) {
               var1.rawset("doorright_open", var5.getDoor().isOpen());
               var1.rawset("doorright_locked", var5.getDoor().isLocked());
               var1.rawset("doorright_lockbroken", var5.getDoor().isLockBroken());
               VehicleWindow var4 = var5.findWindow();
               var1.rawset("windowright_exist", var4 != null);
               if (var4 != null) {
                  var1.rawset("windowright_open", var4.isOpen());
                  var1.rawset("windowright_health", var4.getHealth());
               }
            }

            var1.rawset("headlights_set", var0.hasHeadlights());
            var1.rawset("headlights_on", var0.getHeadlightsOn());
            if (var0.getPartById("Heater") != null) {
               var1.rawset("heater_isset", true);
               Object var6 = var0.getPartById("Heater").getModData().rawget("active");
               if (var6 == null) {
                  var1.rawset("heater_on", false);
               } else {
                  var1.rawset("heater_on", var6 == Boolean.TRUE);
               }
            } else {
               var1.rawset("heater_isset", false);
            }

            return var1;
         }
      }

      @LuaMethod(
         name = "getLotDirectories",
         global = true
      )
      public static ArrayList getLotDirectories() {
         return IsoWorld.instance.MetaGrid != null ? IsoWorld.instance.MetaGrid.getLotDirectories() : null;
      }

      @LuaMethod(
         name = "useTextureFiltering",
         global = true
      )
      public static void useTextureFiltering(boolean var0) {
         TextureID.UseFiltering = var0;
      }

      @LuaMethod(
         name = "getTexture",
         global = true
      )
      public static Texture getTexture(String var0) {
         return Texture.getSharedTexture(var0);
      }

      @LuaMethod(
         name = "getTextManager",
         global = true
      )
      public static TextManager getTextManager() {
         return TextManager.instance;
      }

      @LuaMethod(
         name = "setProgressBarValue",
         global = true
      )
      public static void setProgressBarValue(IsoPlayer var0, int var1) {
         if (var0.isLocalPlayer()) {
            UIManager.getProgressBar((double)var0.getPlayerNum()).setValue((float)var1);
         }

      }

      @LuaMethod(
         name = "getText",
         global = true
      )
      public static String getText(String var0) {
         return Translator.getText(var0);
      }

      @LuaMethod(
         name = "getText",
         global = true
      )
      public static String getText(String var0, Object var1) {
         return Translator.getText(var0, var1);
      }

      @LuaMethod(
         name = "getText",
         global = true
      )
      public static String getText(String var0, Object var1, Object var2) {
         return Translator.getText(var0, var1, var2);
      }

      @LuaMethod(
         name = "getText",
         global = true
      )
      public static String getText(String var0, Object var1, Object var2, Object var3) {
         return Translator.getText(var0, var1, var2, var3);
      }

      @LuaMethod(
         name = "getText",
         global = true
      )
      public static String getText(String var0, Object var1, Object var2, Object var3, Object var4) {
         return Translator.getText(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "getTextOrNull",
         global = true
      )
      public static String getTextOrNull(String var0) {
         return Translator.getTextOrNull(var0);
      }

      @LuaMethod(
         name = "getTextOrNull",
         global = true
      )
      public static String getTextOrNull(String var0, Object var1) {
         return Translator.getTextOrNull(var0, var1);
      }

      @LuaMethod(
         name = "getTextOrNull",
         global = true
      )
      public static String getTextOrNull(String var0, Object var1, Object var2) {
         return Translator.getTextOrNull(var0, var1, var2);
      }

      @LuaMethod(
         name = "getTextOrNull",
         global = true
      )
      public static String getTextOrNull(String var0, Object var1, Object var2, Object var3) {
         return Translator.getTextOrNull(var0, var1, var2, var3);
      }

      @LuaMethod(
         name = "getTextOrNull",
         global = true
      )
      public static String getTextOrNull(String var0, Object var1, Object var2, Object var3, Object var4) {
         return Translator.getTextOrNull(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "getItemText",
         global = true
      )
      public static String getItemText(String var0) {
         return Translator.getDisplayItemName(var0);
      }

      @LuaMethod(
         name = "getRadioText",
         global = true
      )
      public static String getRadioText(String var0) {
         return Translator.getRadioText(var0);
      }

      @LuaMethod(
         name = "getTextMediaEN",
         global = true
      )
      public static String getTextMediaEN(String var0) {
         return Translator.getTextMediaEN(var0);
      }

      @LuaMethod(
         name = "getItemNameFromFullType",
         global = true
      )
      public static String getItemNameFromFullType(String var0) {
         return Translator.getItemNameFromFullType(var0);
      }

      @LuaMethod(
         name = "getRecipeDisplayName",
         global = true
      )
      public static String getRecipeDisplayName(String var0) {
         return Translator.getRecipeName(var0);
      }

      @LuaMethod(
         name = "getMyDocumentFolder",
         global = true
      )
      public static String getMyDocumentFolder() {
         return Core.getMyDocumentFolder();
      }

      @LuaMethod(
         name = "getSpriteManager",
         global = true
      )
      public static IsoSpriteManager getSpriteManager(String var0) {
         return IsoSpriteManager.instance;
      }

      @LuaMethod(
         name = "getSprite",
         global = true
      )
      public static IsoSprite getSprite(String var0) {
         return IsoSpriteManager.instance.getSprite(var0);
      }

      @LuaMethod(
         name = "getServerModData",
         global = true
      )
      public static void getServerModData() {
         GameClient.getCustomModData();
      }

      @LuaMethod(
         name = "isXBOXController",
         global = true
      )
      public static boolean isXBOXController() {
         for(int var0 = 0; var0 < GameWindow.GameInput.getControllerCount(); ++var0) {
            Controller var1 = GameWindow.GameInput.getController(var0);
            if (var1 != null && var1.getGamepadName().contains("XBOX 360")) {
               return true;
            }
         }

         return false;
      }

      @LuaMethod(
         name = "sendClientCommand",
         global = true
      )
      public static void sendClientCommand(String var0, String var1, KahluaTable var2) {
         if (GameClient.bClient && GameClient.bIngame) {
            GameClient.instance.sendClientCommand((IsoPlayer)null, var0, var1, var2);
         } else {
            if (GameServer.bServer) {
               throw new IllegalStateException("can't call this function on the server");
            }

            SinglePlayerClient.sendClientCommand((IsoPlayer)null, var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "sendClientCommand",
         global = true
      )
      public static void sendClientCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
         if (var0 != null && var0.isLocalPlayer()) {
            if (GameClient.bClient && GameClient.bIngame) {
               GameClient.instance.sendClientCommand(var0, var1, var2, var3);
            } else {
               if (GameServer.bServer) {
                  throw new IllegalStateException("can't call this function on the server");
               }

               SinglePlayerClient.sendClientCommand(var0, var1, var2, var3);
            }

         }
      }

      @LuaMethod(
         name = "sendServerCommand",
         global = true
      )
      public static void sendServerCommand(String var0, String var1, KahluaTable var2) {
         if (GameServer.bServer) {
            GameServer.sendServerCommand(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "sendServerCommand",
         global = true
      )
      public static void sendServerCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
         if (GameServer.bServer) {
            GameServer.sendServerCommand(var0, var1, var2, var3);
         }

      }

      @LuaMethod(
         name = "getOnlineUsername",
         global = true
      )
      public static String getOnlineUsername() {
         return IsoPlayer.getInstance().getDisplayName();
      }

      @LuaMethod(
         name = "isValidUserName",
         global = true
      )
      public static boolean isValidUserName(String var0) {
         return ServerWorldDatabase.isValidUserName(var0);
      }

      @LuaMethod(
         name = "getHourMinute",
         global = true
      )
      public static String getHourMinute() {
         return LuaManager.getHourMinuteJava();
      }

      @LuaMethod(
         name = "SendCommandToServer",
         global = true
      )
      public static void SendCommandToServer(String var0) {
         GameClient.SendCommandToServer(var0);
      }

      @LuaMethod(
         name = "isAdmin",
         global = true
      )
      public static boolean isAdmin() {
         return GameClient.bClient && GameClient.accessLevel.equals("admin");
      }

      @LuaMethod(
         name = "canModifyPlayerScoreboard",
         global = true
      )
      public static boolean canModifyPlayerScoreboard() {
         return GameClient.bClient && !GameClient.accessLevel.equals("");
      }

      @LuaMethod(
         name = "isAccessLevel",
         global = true
      )
      public static boolean isAccessLevel(String var0) {
         if (GameClient.bClient) {
            return GameClient.accessLevel.equals("") ? false : GameClient.accessLevel.equals(var0);
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "sendBandage",
         global = true
      )
      public static void sendBandage(int var0, int var1, boolean var2, float var3, boolean var4, String var5) {
         GameClient.instance.sendBandage(var0, var1, var2, var3, var4, var5);
      }

      @LuaMethod(
         name = "sendCataplasm",
         global = true
      )
      public static void sendCataplasm(int var0, int var1, float var2, float var3, float var4) {
         GameClient.instance.sendCataplasm(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "sendStitch",
         global = true
      )
      public static void sendStitch(int var0, int var1, boolean var2, float var3) {
         GameClient.instance.sendStitch(var0, var1, var2, var3);
      }

      @LuaMethod(
         name = "sendWoundInfection",
         global = true
      )
      public static void sendWoundInfection(int var0, int var1, boolean var2) {
         GameClient.instance.sendWoundInfection(var0, var1, var2);
      }

      @LuaMethod(
         name = "sendDisinfect",
         global = true
      )
      public static void sendDisinfect(int var0, int var1, float var2) {
         GameClient.instance.sendDisinfect(var0, var1, var2);
      }

      @LuaMethod(
         name = "sendSplint",
         global = true
      )
      public static void sendSplint(int var0, int var1, boolean var2, float var3, String var4) {
         GameClient.instance.sendSplint(var0, var1, var2, var3, var4);
      }

      @LuaMethod(
         name = "sendAdditionalPain",
         global = true
      )
      public static void sendAdditionalPain(int var0, int var1, float var2) {
         GameClient.instance.sendAdditionalPain(var0, var1, var2);
      }

      @LuaMethod(
         name = "sendRemoveGlass",
         global = true
      )
      public static void sendRemoveGlass(int var0, int var1) {
         GameClient.instance.sendRemoveGlass(var0, var1);
      }

      @LuaMethod(
         name = "sendRemoveBullet",
         global = true
      )
      public static void sendRemoveBullet(int var0, int var1, int var2) {
         GameClient.instance.sendRemoveBullet(var0, var1, var2);
      }

      @LuaMethod(
         name = "sendCleanBurn",
         global = true
      )
      public static void sendCleanBurn(int var0, int var1) {
         GameClient.instance.sendCleanBurn(var0, var1);
      }

      @LuaMethod(
         name = "getGameClient",
         global = true
      )
      public static GameClient getGameClient() {
         return GameClient.instance;
      }

      @LuaMethod(
         name = "sendRequestInventory",
         global = true
      )
      public static void sendRequestInventory(IsoPlayer var0) {
         GameClient.sendRequestInventory(var0);
      }

      @LuaMethod(
         name = "InvMngGetItem",
         global = true
      )
      public static void InvMngGetItem(long var0, String var2, IsoPlayer var3) {
         GameClient.invMngRequestItem(var0, var2, var3);
      }

      @LuaMethod(
         name = "InvMngRemoveItem",
         global = true
      )
      public static void InvMngRemoveItem(long var0, IsoPlayer var2) {
         GameClient.invMngRequestRemoveItem(var0, var2);
      }

      @LuaMethod(
         name = "getConnectedPlayers",
         global = true
      )
      public static ArrayList getConnectedPlayers() {
         return GameClient.instance.getConnectedPlayers();
      }

      @LuaMethod(
         name = "getPlayerFromUsername",
         global = true
      )
      public static IsoPlayer getPlayerFromUsername(String var0) {
         return GameClient.instance.getPlayerFromUsername(var0);
      }

      @LuaMethod(
         name = "isCoopHost",
         global = true
      )
      public static boolean isCoopHost() {
         return GameClient.connection != null && GameClient.connection.isCoopHost;
      }

      @LuaMethod(
         name = "setAdmin",
         global = true
      )
      public static void setAdmin() {
         if (CoopMaster.instance.isRunning()) {
            String var0 = "admin";
            if (GameClient.connection.accessLevel.equals("admin")) {
               var0 = "";
            }

            GameClient.connection.accessLevel = var0;
            GameClient.accessLevel = var0;
            IsoPlayer.getInstance().accessLevel = var0;
            GameClient.SendCommandToServer("/setaccesslevel \"" + IsoPlayer.getInstance().username + "\" \"" + (var0.equals("") ? "none" : var0) + "\"");
            if (var0.equals("") && IsoPlayer.getInstance().isInvisible() || var0.equals("admin") && !IsoPlayer.getInstance().isInvisible()) {
               GameClient.SendCommandToServer("/invisible");
            }

         }
      }

      @LuaMethod(
         name = "addWarningPoint",
         global = true
      )
      public static void addWarningPoint(String var0, String var1, int var2) {
         if (GameClient.bClient) {
            GameClient.instance.addWarningPoint(var0, var1, var2);
         }

      }

      @LuaMethod(
         name = "toggleSafetyServer",
         global = true
      )
      public static void toggleSafetyServer(IsoPlayer var0) {
         GameClient.toggleSafety(var0);
      }

      @LuaMethod(
         name = "disconnect",
         global = true
      )
      public static void disconnect() {
         GameClient.connection.forceDisconnect();
      }

      @LuaMethod(
         name = "writeLog",
         global = true
      )
      public static void writeLog(String var0, String var1) {
         ByteBufferWriter var2 = GameClient.connection.startPacket();
         PacketTypes.PacketType.WriteLog.doPacket(var2);
         var2.putUTF(var0);
         var2.putUTF(var1);
         PacketTypes.PacketType.WriteLog.send(GameClient.connection);
      }

      @LuaMethod(
         name = "doKeyPress",
         global = true
      )
      public static void doKeyPress(boolean var0) {
         GameKeyboard.doLuaKeyPressed = var0;
      }

      @LuaMethod(
         name = "getEvolvedRecipes",
         global = true
      )
      public static Stack getEvolvedRecipes() {
         return ScriptManager.instance.getAllEvolvedRecipes();
      }

      @LuaMethod(
         name = "getZone",
         global = true
      )
      public static IsoMetaGrid.Zone getZone(int var0, int var1, int var2) {
         return IsoWorld.instance.MetaGrid.getZoneAt(var0, var1, var2);
      }

      @LuaMethod(
         name = "getZones",
         global = true
      )
      public static ArrayList getZones(int var0, int var1, int var2) {
         return IsoWorld.instance.MetaGrid.getZonesAt(var0, var1, var2);
      }

      @LuaMethod(
         name = "getVehicleZoneAt",
         global = true
      )
      public static IsoMetaGrid.VehicleZone getVehicleZoneAt(int var0, int var1, int var2) {
         return IsoWorld.instance.MetaGrid.getVehicleZoneAt(var0, var1, var2);
      }

      @LuaMethod(
         name = "replaceWith",
         global = true
      )
      public static String replaceWith(String var0, String var1, String var2) {
         return var0.replaceFirst(var1, var2);
      }

      @LuaMethod(
         name = "getTimestamp",
         global = true
      )
      public static long getTimestamp() {
         return System.currentTimeMillis() / 1000L;
      }

      @LuaMethod(
         name = "getTimestampMs",
         global = true
      )
      public static long getTimestampMs() {
         return System.currentTimeMillis();
      }

      @LuaMethod(
         name = "forceSnowCheck",
         global = true
      )
      public static void forceSnowCheck() {
         ErosionMain.getInstance().snowCheck();
      }

      @LuaMethod(
         name = "getGametimeTimestamp",
         global = true
      )
      public static long getGametimeTimestamp() {
         return GameTime.instance.getCalender().getTimeInMillis() / 1000L;
      }

      @LuaMethod(
         name = "canInviteFriends",
         global = true
      )
      public static boolean canInviteFriends() {
         if (GameClient.bClient && SteamUtils.isSteamModeEnabled()) {
            return CoopMaster.instance.isRunning() || !GameClient.bCoopInvite;
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "inviteFriend",
         global = true
      )
      public static void inviteFriend(String var0) {
         if (CoopMaster.instance != null && CoopMaster.instance.isRunning()) {
            CoopMaster.instance.sendMessage("invite-add", var0);
         }

         SteamFriends.InviteUserToGame(SteamUtils.convertStringToSteamID(var0), "+connect " + GameClient.ip + ":" + GameClient.port);
      }

      @LuaMethod(
         name = "getFriendsList",
         global = true
      )
      public static KahluaTable getFriendsList() {
         KahluaTable var0 = LuaManager.platform.newTable();
         if (!getSteamModeActive()) {
            return var0;
         } else {
            List var1 = SteamFriends.GetFriendList();
            int var2 = 1;

            for(int var3 = 0; var3 < var1.size(); ++var3) {
               SteamFriend var4 = (SteamFriend)var1.get(var3);
               Double var5 = (double)var2;
               var0.rawset(var5, var4);
               ++var2;
            }

            return var0;
         }
      }

      @LuaMethod(
         name = "getSteamModeActive",
         global = true
      )
      public static Boolean getSteamModeActive() {
         return SteamUtils.isSteamModeEnabled();
      }

      @LuaMethod(
         name = "isValidSteamID",
         global = true
      )
      public static boolean isValidSteamID(String var0) {
         return var0 != null && !var0.isEmpty() ? SteamUtils.isValidSteamID(var0) : false;
      }

      @LuaMethod(
         name = "getCurrentUserSteamID",
         global = true
      )
      public static String getCurrentUserSteamID() {
         return SteamUtils.isSteamModeEnabled() && !GameServer.bServer ? SteamUser.GetSteamIDString() : null;
      }

      @LuaMethod(
         name = "getCurrentUserProfileName",
         global = true
      )
      public static String getCurrentUserProfileName() {
         return SteamUtils.isSteamModeEnabled() && !GameServer.bServer ? SteamFriends.GetFriendPersonaName(SteamUser.GetSteamID()) : null;
      }

      @LuaMethod(
         name = "getSteamScoreboard",
         global = true
      )
      public static boolean getSteamScoreboard() {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            String var0 = ServerOptions.instance.SteamScoreboard.getValue();
            return "true".equals(var0) || GameClient.accessLevel.equals("admin") && "admin".equals(var0);
         } else {
            return false;
         }
      }

      @LuaMethod(
         name = "isSteamOverlayEnabled",
         global = true
      )
      public static boolean isSteamOverlayEnabled() {
         return SteamUtils.isOverlayEnabled();
      }

      @LuaMethod(
         name = "activateSteamOverlayToWorkshop",
         global = true
      )
      public static void activateSteamOverlayToWorkshop() {
         if (SteamUtils.isOverlayEnabled()) {
            SteamFriends.ActivateGameOverlayToWebPage("steam://url/SteamWorkshopPage/108600");
         }

      }

      @LuaMethod(
         name = "activateSteamOverlayToWorkshopUser",
         global = true
      )
      public static void activateSteamOverlayToWorkshopUser() {
         if (SteamUtils.isOverlayEnabled()) {
            SteamFriends.ActivateGameOverlayToWebPage("steam://url/SteamIDCommunityFilesPage/" + SteamUser.GetSteamIDString() + "/108600");
         }

      }

      @LuaMethod(
         name = "activateSteamOverlayToWorkshopItem",
         global = true
      )
      public static void activateSteamOverlayToWorkshopItem(String var0) {
         if (SteamUtils.isOverlayEnabled() && SteamUtils.isValidSteamID(var0)) {
            SteamFriends.ActivateGameOverlayToWebPage("steam://url/CommunityFilePage/" + var0);
         }

      }

      @LuaMethod(
         name = "activateSteamOverlayToWebPage",
         global = true
      )
      public static void activateSteamOverlayToWebPage(String var0) {
         if (SteamUtils.isOverlayEnabled()) {
            SteamFriends.ActivateGameOverlayToWebPage(var0);
         }

      }

      @LuaMethod(
         name = "getSteamProfileNameFromSteamID",
         global = true
      )
      public static String getSteamProfileNameFromSteamID(String var0) {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            long var1 = SteamUtils.convertStringToSteamID(var0);
            if (var1 != -1L) {
               return SteamFriends.GetFriendPersonaName(var1);
            }
         }

         return null;
      }

      @LuaMethod(
         name = "getSteamAvatarFromSteamID",
         global = true
      )
      public static Texture getSteamAvatarFromSteamID(String var0) {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            long var1 = SteamUtils.convertStringToSteamID(var0);
            if (var1 != -1L) {
               return Texture.getSteamAvatar(var1);
            }
         }

         return null;
      }

      @LuaMethod(
         name = "getSteamIDFromUsername",
         global = true
      )
      public static String getSteamIDFromUsername(String var0) {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            IsoPlayer var1 = GameClient.instance.getPlayerFromUsername(var0);
            if (var1 != null) {
               return SteamUtils.convertSteamIDToString(var1.getSteamID());
            }
         }

         return null;
      }

      @LuaMethod(
         name = "resetRegionFile",
         global = true
      )
      public static void resetRegionFile() {
         ServerOptions.getInstance().resetRegionFile();
      }

      @LuaMethod(
         name = "getSteamProfileNameFromUsername",
         global = true
      )
      public static String getSteamProfileNameFromUsername(String var0) {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            IsoPlayer var1 = GameClient.instance.getPlayerFromUsername(var0);
            if (var1 != null) {
               return SteamFriends.GetFriendPersonaName(var1.getSteamID());
            }
         }

         return null;
      }

      @LuaMethod(
         name = "getSteamAvatarFromUsername",
         global = true
      )
      public static Texture getSteamAvatarFromUsername(String var0) {
         if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
            IsoPlayer var1 = GameClient.instance.getPlayerFromUsername(var0);
            if (var1 != null) {
               return Texture.getSteamAvatar(var1.getSteamID());
            }
         }

         return null;
      }

      @LuaMethod(
         name = "getSteamWorkshopStagedItems",
         global = true
      )
      public static ArrayList getSteamWorkshopStagedItems() {
         return SteamUtils.isSteamModeEnabled() ? SteamWorkshop.instance.loadStagedItems() : null;
      }

      @LuaMethod(
         name = "getSteamWorkshopItemIDs",
         global = true
      )
      public static ArrayList getSteamWorkshopItemIDs() {
         if (SteamUtils.isSteamModeEnabled()) {
            ArrayList var0 = new ArrayList();
            String[] var1 = SteamWorkshop.instance.GetInstalledItemFolders();
            if (var1 == null) {
               return var0;
            } else {
               for(int var2 = 0; var2 < var1.length; ++var2) {
                  String var3 = SteamWorkshop.instance.getIDFromItemInstallFolder(var1[var2]);
                  if (var3 != null) {
                     var0.add(var3);
                  }
               }

               return var0;
            }
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "getSteamWorkshopItemMods",
         global = true
      )
      public static ArrayList getSteamWorkshopItemMods(String var0) {
         if (SteamUtils.isSteamModeEnabled()) {
            long var1 = SteamUtils.convertStringToSteamID(var0);
            if (var1 > 0L) {
               return ZomboidFileSystem.instance.getWorkshopItemMods(var1);
            }
         }

         return null;
      }

      @LuaMethod(
         name = "sendPlayerStatsChange",
         global = true
      )
      public static void sendPlayerStatsChange(IsoPlayer var0) {
         if (GameClient.bClient) {
            GameClient.instance.sendChangedPlayerStats(var0);
         }

      }

      @LuaMethod(
         name = "sendPersonalColor",
         global = true
      )
      public static void sendPersonalColor(IsoPlayer var0) {
         if (GameClient.bClient) {
            GameClient.instance.sendPersonalColor(var0);
         }

      }

      @LuaMethod(
         name = "requestTrading",
         global = true
      )
      public static void requestTrading(IsoPlayer var0, IsoPlayer var1) {
         GameClient.instance.requestTrading(var0, var1);
      }

      @LuaMethod(
         name = "acceptTrading",
         global = true
      )
      public static void acceptTrading(IsoPlayer var0, IsoPlayer var1, boolean var2) {
         GameClient.instance.acceptTrading(var0, var1, var2);
      }

      @LuaMethod(
         name = "tradingUISendAddItem",
         global = true
      )
      public static void tradingUISendAddItem(IsoPlayer var0, IsoPlayer var1, InventoryItem var2) {
         GameClient.instance.tradingUISendAddItem(var0, var1, var2);
      }

      @LuaMethod(
         name = "tradingUISendRemoveItem",
         global = true
      )
      public static void tradingUISendRemoveItem(IsoPlayer var0, IsoPlayer var1, int var2) {
         GameClient.instance.tradingUISendRemoveItem(var0, var1, var2);
      }

      @LuaMethod(
         name = "tradingUISendUpdateState",
         global = true
      )
      public static void tradingUISendUpdateState(IsoPlayer var0, IsoPlayer var1, int var2) {
         GameClient.instance.tradingUISendUpdateState(var0, var1, var2);
      }

      @LuaMethod(
         name = "querySteamWorkshopItemDetails",
         global = true
      )
      public static void querySteamWorkshopItemDetails(ArrayList var0, LuaClosure var1, Object var2) {
         if (var0 != null && var1 != null) {
            if (var0.isEmpty()) {
               if (var2 == null) {
                  LuaManager.caller.pcall(LuaManager.thread, var1, (Object[])("Completed", new ArrayList()));
               } else {
                  LuaManager.caller.pcall(LuaManager.thread, var1, (Object[])(var2, "Completed", new ArrayList()));
               }

            } else {
               new LuaManager.GlobalObject.ItemQuery(var0, var1, var2);
            }
         } else {
            throw new NullPointerException();
         }
      }

      @LuaMethod(
         name = "connectToServerStateCallback",
         global = true
      )
      public static void connectToServerStateCallback(String var0) {
         if (ConnectToServerState.instance != null) {
            ConnectToServerState.instance.FromLua(var0);
         }

      }

      @LuaMethod(
         name = "getPublicServersList",
         global = true
      )
      public static KahluaTable getPublicServersList() {
         KahluaTable var0 = LuaManager.platform.newTable();
         if (!SteamUtils.isSteamModeEnabled() && !PublicServerUtil.isEnabled()) {
            return var0;
         } else if (System.currentTimeMillis() - timeLastRefresh < 60000L) {
            return var0;
         } else {
            ArrayList var1 = new ArrayList();

            try {
               Server var5;
               if (getSteamModeActive()) {
                  ServerBrowser.RefreshInternetServers();
                  List var2 = ServerBrowser.GetServerList();
                  Iterator var3 = var2.iterator();

                  while(var3.hasNext()) {
                     GameServerDetails var4 = (GameServerDetails)var3.next();
                     var5 = new Server();
                     var5.setName(var4.name);
                     var5.setDescription(var4.gameDescription);
                     var5.setSteamId(Long.toString(var4.steamId));
                     var5.setPing(Integer.toString(var4.ping));
                     var5.setPlayers(Integer.toString(var4.numPlayers));
                     var5.setMaxPlayers(Integer.toString(var4.maxPlayers));
                     var5.setOpen(true);
                     var5.setIp(var4.address);
                     var5.setPort(Integer.toString(var4.port));
                     var5.setMods(var4.tags);
                     var5.setVersion(Core.getInstance().getVersionNumber());
                     var5.setLastUpdate(1);
                     var1.add(var5);
                  }

                  System.out.printf("%d servers\n", var2.size());
               } else {
                  URL var18 = new URL(PublicServerUtil.webSite + "servers.xml");
                  InputStreamReader var20 = new InputStreamReader(var18.openStream());
                  BufferedReader var22 = new BufferedReader(var20);
                  var5 = null;
                  StringBuffer var6 = new StringBuffer();

                  String var24;
                  while((var24 = var22.readLine()) != null) {
                     var6.append(var24).append('\n');
                  }

                  var22.close();
                  DocumentBuilderFactory var7 = DocumentBuilderFactory.newInstance();
                  DocumentBuilder var8 = var7.newDocumentBuilder();
                  Document var9 = var8.parse(new InputSource(new StringReader(var6.toString())));
                  var9.getDocumentElement().normalize();
                  NodeList var10 = var9.getElementsByTagName("server");

                  for(int var11 = 0; var11 < var10.getLength(); ++var11) {
                     Node var12 = var10.item(var11);
                     if (var12.getNodeType() == 1) {
                        Element var13 = (Element)var12;
                        Server var14 = new Server();
                        var14.setName(var13.getElementsByTagName("name").item(0).getTextContent());
                        if (var13.getElementsByTagName("desc").item(0) != null && !"".equals(var13.getElementsByTagName("desc").item(0).getTextContent())) {
                           var14.setDescription(var13.getElementsByTagName("desc").item(0).getTextContent());
                        }

                        var14.setIp(var13.getElementsByTagName("ip").item(0).getTextContent());
                        var14.setPort(var13.getElementsByTagName("port").item(0).getTextContent());
                        var14.setPlayers(var13.getElementsByTagName("players").item(0).getTextContent());
                        var14.setMaxPlayers(var13.getElementsByTagName("maxPlayers").item(0).getTextContent());
                        if (var13.getElementsByTagName("version") != null && var13.getElementsByTagName("version").item(0) != null) {
                           var14.setVersion(var13.getElementsByTagName("version").item(0).getTextContent());
                        }

                        var14.setOpen(var13.getElementsByTagName("open").item(0).getTextContent().equals("1"));
                        Integer var15 = Integer.parseInt(var13.getElementsByTagName("lastUpdate").item(0).getTextContent());
                        if (var13.getElementsByTagName("mods").item(0) != null && !"".equals(var13.getElementsByTagName("mods").item(0).getTextContent())) {
                           var14.setMods(var13.getElementsByTagName("mods").item(0).getTextContent());
                        }

                        var14.setLastUpdate((new Double(Math.floor((double)((getTimestamp() - (long)var15) / 60L)))).intValue());
                        NodeList var16 = var13.getElementsByTagName("password");
                        var14.setPasswordProtected(var16 != null && var16.getLength() != 0 && var16.item(0).getTextContent().equals("1"));
                        var1.add(var14);
                     }
                  }
               }

               int var19 = 1;

               for(int var21 = 0; var21 < var1.size(); ++var21) {
                  Server var23 = (Server)var1.get(var21);
                  Double var25 = (double)var19;
                  var0.rawset(var25, var23);
                  ++var19;
               }

               timeLastRefresh = Calendar.getInstance().getTimeInMillis();
               return var0;
            } catch (Exception var17) {
               var17.printStackTrace();
               return null;
            }
         }
      }

      @LuaMethod(
         name = "steamRequestInternetServersList",
         global = true
      )
      public static void steamRequestInternetServersList() {
         ServerBrowser.RefreshInternetServers();
      }

      @LuaMethod(
         name = "steamReleaseInternetServersRequest",
         global = true
      )
      public static void steamReleaseInternetServersRequest() {
         ServerBrowser.Release();
      }

      @LuaMethod(
         name = "steamGetInternetServersCount",
         global = true
      )
      public static int steamRequestInternetServersCount() {
         return ServerBrowser.GetServerCount();
      }

      @LuaMethod(
         name = "steamGetInternetServerDetails",
         global = true
      )
      public static Server steamGetInternetServerDetails(int var0) {
         if (!ServerBrowser.IsRefreshing()) {
            return null;
         } else {
            GameServerDetails var1 = ServerBrowser.GetServerDetails(var0);
            if (var1 == null) {
               return null;
            } else if (!var1.tags.contains("hidden") && !var1.tags.contains("hosted")) {
               if (!var1.tags.contains("hidden") && !var1.tags.contains("hosted")) {
                  Server var2 = new Server();
                  var2.setName(var1.name);
                  var2.setDescription("");
                  var2.setSteamId(Long.toString(var1.steamId));
                  var2.setPing(Integer.toString(var1.ping));
                  var2.setPlayers(Integer.toString(var1.numPlayers));
                  var2.setMaxPlayers(Integer.toString(var1.maxPlayers));
                  var2.setOpen(true);
                  var2.setPublic(true);
                  if (var1.tags.contains("hidden")) {
                     var2.setOpen(false);
                     var2.setPublic(false);
                  }

                  var2.setIp(var1.address);
                  var2.setPort(Integer.toString(var1.port));
                  var2.setMods("");
                  if (!var1.tags.replace("hidden", "").replace("hosted", "").replace(";", "").isEmpty()) {
                     var2.setMods(var1.tags.replace(";hosted", "").replace("hidden", ""));
                  }

                  var2.setHosted(var1.tags.contains("hosted"));
                  var2.setVersion("");
                  var2.setLastUpdate(1);
                  var2.setPasswordProtected(var1.passwordProtected);
                  return var2;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         }
      }

      @LuaMethod(
         name = "steamRequestServerRules",
         global = true
      )
      public static boolean steamRequestServerRules(String var0, int var1) {
         return ServerBrowser.RequestServerRules(var0, var1);
      }

      @LuaMethod(
         name = "steamRequestServerDetails",
         global = true
      )
      public static boolean steamRequestServerDetails(String var0, int var1) {
         return ServerBrowser.QueryServer(var0, var1);
      }

      @LuaMethod(
         name = "isPublicServerListAllowed",
         global = true
      )
      public static boolean isPublicServerListAllowed() {
         return SteamUtils.isSteamModeEnabled() ? true : PublicServerUtil.isEnabled();
      }

      @LuaMethod(
         name = "is64bit",
         global = true
      )
      public static boolean is64bit() {
         return "64".equals(System.getProperty("sun.arch.data.model"));
      }

      @LuaMethod(
         name = "testSound",
         global = true
      )
      public static void testSound() {
         float var0 = (float)Mouse.getX();
         float var1 = (float)Mouse.getY();
         int var2 = (int)IsoPlayer.getInstance().getZ();
         int var3 = (int)IsoUtils.XToIso(var0, var1, (float)var2);
         int var4 = (int)IsoUtils.YToIso(var0, var1, (float)var2);
         float var5 = 50.0F;
         float var6 = 1.0F;
         AmbientStreamManager.Ambient var7 = new AmbientStreamManager.Ambient("Meta/House Alarm", (float)var3, (float)var4, var5, var6);
         var7.trackMouse = true;
         ((AmbientStreamManager)AmbientStreamManager.instance).ambient.add(var7);
      }

      @LuaMethod(
         name = "copyTable",
         global = true
      )
      public static KahluaTable copyTable(KahluaTable var0) {
         return LuaManager.copyTable(var0);
      }

      @LuaMethod(
         name = "getUrlInputStream",
         global = true
      )
      public static DataInputStream getUrlInputStream(String var0) {
         if (var0 != null && (var0.startsWith("https://") || var0.startsWith("http://"))) {
            try {
               return new DataInputStream((new URL(var0)).openStream());
            } catch (IOException var2) {
               var2.printStackTrace();
               return null;
            }
         } else {
            return null;
         }
      }

      @LuaMethod(
         name = "renderIsoCircle",
         global = true
      )
      public static void renderIsoCircle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
         double var9 = 0.3490658503988659D;

         for(double var11 = 0.0D; var11 < 6.283185307179586D; var11 += var9) {
            float var13 = var0 + var3 * (float)Math.cos(var11);
            float var14 = var1 + var3 * (float)Math.sin(var11);
            float var15 = var0 + var3 * (float)Math.cos(var11 + var9);
            float var16 = var1 + var3 * (float)Math.sin(var11 + var9);
            float var17 = IsoUtils.XToScreenExact(var13, var14, var2, 0);
            float var18 = IsoUtils.YToScreenExact(var13, var14, var2, 0);
            float var19 = IsoUtils.XToScreenExact(var15, var16, var2, 0);
            float var20 = IsoUtils.YToScreenExact(var15, var16, var2, 0);
            LineDrawer.drawLine(var17, var18, var19, var20, var4, var5, var6, var7, var8);
         }

      }

      @LuaMethod(
         name = "configureLighting",
         global = true
      )
      public static void configureLighting(float var0) {
         if (LightingJNI.init) {
            LightingJNI.configure(var0);
         }

      }

      @LuaMethod(
         name = "testHelicopter",
         global = true
      )
      public static void testHelicopter() {
         if (GameClient.bClient) {
            GameClient.SendCommandToServer("/chopper start");
         } else {
            IsoWorld.instance.helicopter.pickRandomTarget();
         }

      }

      @LuaMethod(
         name = "endHelicopter",
         global = true
      )
      public static void endHelicopter() {
         if (GameClient.bClient) {
            GameClient.SendCommandToServer("/chopper stop");
         } else {
            IsoWorld.instance.helicopter.deactivate();
         }

      }

      @LuaMethod(
         name = "getServerSettingsManager",
         global = true
      )
      public static ServerSettingsManager getServerSettingsManager() {
         return ServerSettingsManager.instance;
      }

      @LuaMethod(
         name = "rainConfig",
         global = true
      )
      public static void rainConfig(String var0, int var1) {
         if ("alpha".equals(var0)) {
            IsoWorld.instance.CurrentCell.setRainAlpha(var1);
         }

         if ("intensity".equals(var0)) {
            IsoWorld.instance.CurrentCell.setRainIntensity(var1);
         }

         if ("speed".equals(var0)) {
            IsoWorld.instance.CurrentCell.setRainSpeed(var1);
         }

         if ("reloadTextures".equals(var0)) {
            IsoWorld.instance.CurrentCell.reloadRainTextures();
         }

      }

      @LuaMethod(
         name = "getVehicleById",
         global = true
      )
      public static BaseVehicle getVehicleById(int var0) {
         return GameServer.bServer ? VehicleManager.instance.getVehicleByID((short)var0) : VehicleManager.instance.getVehicleByID((short)var0);
      }

      @LuaMethod(
         name = "addBloodSplat",
         global = true
      )
      public void addBloodSplat(IsoGridSquare var1, int var2) {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1.getChunk().addBloodSplat((float)var1.x + Rand.Next(-0.5F, 0.5F), (float)var1.y + Rand.Next(-0.5F, 0.5F), (float)var1.z, Rand.Next(8));
         }

      }

      @LuaMethod(
         name = "addCarCrash",
         global = true
      )
      public static void addCarCrash() {
         IsoGridSquare var0 = IsoPlayer.getInstance().getCurrentSquare();
         if (var0 != null) {
            IsoChunk var1 = var0.getChunk();
            if (var1 != null) {
               IsoMetaGrid.Zone var2 = var0.getZone();
               if (var2 != null) {
                  if (var1.canAddRandomCarCrash(var2, true)) {
                     var0.chunk.addRandomCarCrash(var2, true);
                  }
               }
            }
         }
      }

      @LuaMethod(
         name = "createRandomDeadBody",
         global = true
      )
      public static IsoDeadBody createRandomDeadBody(IsoGridSquare var0, int var1) {
         if (var0 == null) {
            return null;
         } else {
            ItemPickerJava.ItemPickerRoom var2 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get("all");
            RandomizedBuildingBase.HumanCorpse var3 = new RandomizedBuildingBase.HumanCorpse(IsoWorld.instance.getCell(), (float)var0.x, (float)var0.y, (float)var0.z);
            var3.setDir(IsoDirections.getRandom());
            var3.setDescriptor(SurvivorFactory.CreateSurvivor());
            var3.setFemale(var3.getDescriptor().isFemale());
            var3.initWornItems("Human");
            var3.initAttachedItems("Human");
            Outfit var4 = var3.getRandomDefaultOutfit();
            var3.dressInNamedOutfit(var4.m_Name);
            var3.initSpritePartsEmpty();
            var3.Dressup(var3.getDescriptor());

            for(int var5 = 0; var5 < var1; ++var5) {
               var3.addBlood((BloodBodyPartType)null, false, true, false);
            }

            IsoDeadBody var6 = new IsoDeadBody(var3, true);
            ItemPickerJava.fillContainerType(var2, var6.getContainer(), var3.isFemale() ? "inventoryfemale" : "inventorymale", (IsoGameCharacter)null);
            return var6;
         }
      }

      @LuaMethod(
         name = "addZombieSitting",
         global = true
      )
      public void addZombieSitting(int var1, int var2, int var3) {
         IsoGridSquare var4 = IsoCell.getInstance().getGridSquare(var1, var2, var3);
         if (var4 != null) {
            VirtualZombieManager.instance.choices.clear();
            VirtualZombieManager.instance.choices.add(var4);
            IsoZombie var5 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
            var5.bDressInRandomOutfit = true;
            ZombiePopulationManager.instance.sitAgainstWall(var5, var4);
         }
      }

      @LuaMethod(
         name = "addZombiesEating",
         global = true
      )
      public void addZombiesEating(int var1, int var2, int var3, int var4, boolean var5) {
         IsoGridSquare var6 = IsoCell.getInstance().getGridSquare(var1, var2, var3);
         if (var6 != null) {
            VirtualZombieManager.instance.choices.clear();
            VirtualZombieManager.instance.choices.add(var6);
            IsoZombie var7 = VirtualZombieManager.instance.createRealZombieAlways(Rand.Next(8), false);
            var7.setX((float)var6.x);
            var7.setY((float)var6.y);
            var7.setFakeDead(false);
            var7.setHealth(0.0F);
            var7.upKillCount = false;
            if (!var5) {
               var7.dressInRandomOutfit();

               for(int var8 = 0; var8 < 10; ++var8) {
                  var7.addHole((BloodBodyPartType)null);
                  var7.addBlood((BloodBodyPartType)null, false, true, false);
               }

               var7.DoZombieInventory();
            }

            var7.setSkeleton(var5);
            if (var5) {
               var7.getHumanVisual().setSkinTextureIndex(2);
            }

            IsoDeadBody var9 = new IsoDeadBody(var7, true);
            VirtualZombieManager.instance.createEatingZombies(var9, var4);
         }
      }

      @LuaMethod(
         name = "addZombiesInOutfitArea",
         global = true
      )
      public ArrayList addZombiesInOutfitArea(int var1, int var2, int var3, int var4, int var5, int var6, String var7, Integer var8) {
         ArrayList var9 = new ArrayList();

         for(int var10 = 0; var10 < var6; ++var10) {
            var9.addAll(addZombiesInOutfit(Rand.Next(var1, var3), Rand.Next(var2, var4), var5, 1, var7, var8));
         }

         return var9;
      }

      @LuaMethod(
         name = "addZombiesInOutfit",
         global = true
      )
      public static ArrayList addZombiesInOutfit(int var0, int var1, int var2, int var3, String var4, Integer var5) {
         return addZombiesInOutfit(var0, var1, var2, var3, var4, var5, false, false, false, false, 1.0F);
      }

      @LuaMethod(
         name = "addZombiesInOutfit",
         global = true
      )
      public static ArrayList addZombiesInOutfit(int var0, int var1, int var2, int var3, String var4, Integer var5, boolean var6, boolean var7, boolean var8, boolean var9, float var10) {
         ArrayList var11 = new ArrayList();
         if (IsoWorld.getZombiesDisabled()) {
            return var11;
         } else {
            IsoGridSquare var12 = IsoCell.getInstance().getGridSquare(var0, var1, var2);
            if (var12 == null) {
               return var11;
            } else {
               for(int var13 = 0; var13 < var3; ++var13) {
                  if (var10 <= 0.0F) {
                     var12.getChunk().AddCorpses(var0 / 10, var1 / 10);
                  } else {
                     VirtualZombieManager.instance.choices.clear();
                     VirtualZombieManager.instance.choices.add(var12);
                     IsoZombie var14 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
                     if (var14 != null) {
                        if (var5 != null) {
                           var14.setFemaleEtc(Rand.Next(100) < var5);
                        }

                        if (var4 != null) {
                           var14.dressInPersistentOutfit(var4);
                           var14.bDressInRandomOutfit = false;
                        } else {
                           var14.bDressInRandomOutfit = true;
                        }

                        var14.bLunger = true;
                        var14.setKnockedDown(var9);
                        if (var6) {
                           var14.setCrawler(true);
                           var14.setCanWalk(false);
                           var14.setOnFloor(true);
                           var14.setKnockedDown(true);
                           var14.setCrawlerType(1);
                           var14.DoZombieStats();
                        }

                        var14.setFakeDead(var8);
                        var14.setFallOnFront(var7);
                        var14.setHealth(var10);
                        var11.add(var14);
                     }
                  }
               }

               ZombieSpawnRecorder.instance.record(var11, LuaManager.GlobalObject.class.getSimpleName());
               return var11;
            }
         }
      }

      @LuaMethod(
         name = "addZombiesInBuilding",
         global = true
      )
      public ArrayList addZombiesInBuilding(BuildingDef var1, int var2, String var3, RoomDef var4, Integer var5) {
         boolean var6 = var4 == null;
         ArrayList var7 = new ArrayList();
         if (IsoWorld.getZombiesDisabled()) {
            return var7;
         } else {
            if (var4 == null) {
               var4 = var1.getRandomRoom(6);
            }

            int var8 = 2;
            int var9 = var4.area / 2;
            if (var2 == 0) {
               if (SandboxOptions.instance.Zombies.getValue() == 1) {
                  var9 += 4;
               } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
                  var9 += 3;
               } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
                  var9 += 2;
               } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
                  var9 -= 4;
               }

               if (var9 > 8) {
                  var9 = 8;
               }

               if (var9 < var8) {
                  var9 = var8 + 1;
               }
            } else {
               var8 = var2;
               var9 = var2;
            }

            int var10 = Rand.Next(var8, var9);

            for(int var11 = 0; var11 < var10; ++var11) {
               IsoGridSquare var12 = RandomizedBuildingBase.getRandomSpawnSquare(var4);
               if (var12 == null) {
                  break;
               }

               VirtualZombieManager.instance.choices.clear();
               VirtualZombieManager.instance.choices.add(var12);
               IsoZombie var13 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
               if (var13 != null) {
                  if (var5 != null) {
                     var13.setFemaleEtc(Rand.Next(100) < var5);
                  }

                  if (var3 != null) {
                     var13.dressInPersistentOutfit(var3);
                     var13.bDressInRandomOutfit = false;
                  } else {
                     var13.bDressInRandomOutfit = true;
                  }

                  var7.add(var13);
                  if (var6) {
                     var4 = var1.getRandomRoom(6);
                  }
               }
            }

            ZombieSpawnRecorder.instance.record(var7, this.getClass().getSimpleName());
            return var7;
         }
      }

      @LuaMethod(
         name = "addVehicleDebug",
         global = true
      )
      public static BaseVehicle addVehicleDebug(String var0, IsoDirections var1, Integer var2, IsoGridSquare var3) {
         if (var1 == null) {
            var1 = IsoDirections.getRandom();
         }

         BaseVehicle var4 = new BaseVehicle(IsoWorld.instance.CurrentCell);
         if (!StringUtils.isNullOrEmpty(var0)) {
            var4.setScriptName(var0);
            var4.setScript();
            if (var2 != null) {
               var4.setSkinIndex(var2);
            }
         }

         var4.setDir(var1);

         float var5;
         for(var5 = var1.toAngle() + 3.1415927F + Rand.Next(-0.2F, 0.2F); (double)var5 > 6.283185307179586D; var5 = (float)((double)var5 - 6.283185307179586D)) {
         }

         var4.savedRot.setAngleAxis(var5, 0.0F, 1.0F, 0.0F);
         var4.jniTransform.setRotation(var4.savedRot);
         var4.setX((float)var3.x);
         var4.setY((float)var3.y);
         var4.setZ((float)var3.z);
         if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var4)) {
            var4.setSquare(var3);
            var3.chunk.vehicles.add(var4);
            var4.chunk = var3.chunk;
            var4.addToWorld();
            VehiclesDB2.instance.addVehicle(var4);
         }

         var4.setGeneralPartCondition(1.3F, 10.0F);
         var4.rust = 0.0F;
         return var4;
      }

      @LuaMethod(
         name = "addVehicle",
         global = true
      )
      public static BaseVehicle addVehicle(String var0) {
         if (!StringUtils.isNullOrWhitespace(var0) && ScriptManager.instance.getVehicle(var0) == null) {
            DebugLog.Lua.warn("No such vehicle script \"" + var0 + "\"");
            return null;
         } else {
            ArrayList var1 = ScriptManager.instance.getAllVehicleScripts();
            if (var1.isEmpty()) {
               DebugLog.Lua.warn("No vehicle scripts defined");
               return null;
            } else {
               WorldSimulation.instance.create();
               BaseVehicle var2 = new BaseVehicle(IsoWorld.instance.CurrentCell);
               if (StringUtils.isNullOrWhitespace(var0)) {
                  VehicleScript var3 = (VehicleScript)PZArrayUtil.pickRandom((List)var1);
                  var0 = var3.getFullName();
               }

               var2.setScriptName(var0);
               var2.setX(IsoPlayer.getInstance().getX());
               var2.setY(IsoPlayer.getInstance().getY());
               var2.setZ(0.0F);
               if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var2)) {
                  var2.setSquare(IsoPlayer.getInstance().getSquare());
                  var2.square.chunk.vehicles.add(var2);
                  var2.chunk = var2.square.chunk;
                  var2.addToWorld();
                  VehiclesDB2.instance.addVehicle(var2);
               } else {
                  DebugLog.Lua.error("ERROR: I can not spawn the vehicle. Invalid position. Try to change position.");
               }

               return null;
            }
         }
      }

      @LuaMethod(
         name = "attachTrailerToPlayerVehicle",
         global = true
      )
      public static void attachTrailerToPlayerVehicle(int var0) {
         IsoPlayer var1 = IsoPlayer.players[var0];
         IsoGridSquare var2 = var1.getCurrentSquare();
         BaseVehicle var3 = var1.getVehicle();
         if (var3 == null) {
            var3 = addVehicleDebug("Base.OffRoad", IsoDirections.N, 0, var2);
            var3.repair();
            var1.getInventory().AddItem(var3.createVehicleKey());
         }

         var2 = IsoWorld.instance.CurrentCell.getGridSquare(var2.x, var2.y + 5, var2.z);
         BaseVehicle var4 = addVehicleDebug("Base.Trailer", IsoDirections.N, 0, var2);
         var4.repair();
         var3.addPointConstraint(var4, "trailer", "trailer");
      }

      @LuaMethod(
         name = "getKeyName",
         global = true
      )
      public static String getKeyName(int var0) {
         return Input.getKeyName(var0);
      }

      @LuaMethod(
         name = "getKeyCode",
         global = true
      )
      public static int getKeyCode(String var0) {
         return Input.getKeyCode(var0);
      }

      @LuaMethod(
         name = "addAllVehicles",
         global = true
      )
      public static void addAllVehicles() {
         addAllVehicles((var0) -> {
            return !var0.getName().contains("Smashed") && !var0.getName().contains("Burnt");
         });
      }

      @LuaMethod(
         name = "addAllBurntVehicles",
         global = true
      )
      public static void addAllBurntVehicles() {
         addAllVehicles((var0) -> {
            return var0.getName().contains("Burnt");
         });
      }

      @LuaMethod(
         name = "addAllSmashedVehicles",
         global = true
      )
      public static void addAllSmashedVehicles() {
         addAllVehicles((var0) -> {
            return var0.getName().contains("Smashed");
         });
      }

      public static void addAllVehicles(Predicate var0) {
         ArrayList var1 = ScriptManager.instance.getAllVehicleScripts();
         Collections.sort(var1, Comparator.comparing(VehicleScript::getName));
         float var2 = (float)(IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles() + 5);
         float var3 = IsoPlayer.getInstance().getY();
         float var4 = 0.0F;

         for(int var5 = 0; var5 < var1.size(); ++var5) {
            VehicleScript var6 = (VehicleScript)var1.get(var5);
            if (var6.getModel() != null && var0.test(var6) && IsoWorld.instance.CurrentCell.getGridSquare((double)var2, (double)var3, (double)var4) != null) {
               WorldSimulation.instance.create();
               BaseVehicle var7 = new BaseVehicle(IsoWorld.instance.CurrentCell);
               var7.setScriptName(var6.getFullName());
               var7.setX(var2);
               var7.setY(var3);
               var7.setZ(var4);
               if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var7)) {
                  var7.setSquare(IsoPlayer.getInstance().getSquare());
                  var7.square.chunk.vehicles.add(var7);
                  var7.chunk = var7.square.chunk;
                  var7.addToWorld();
                  VehiclesDB2.instance.addVehicle(var7);
                  IsoChunk.addFromCheckedVehicles(var7);
               } else {
                  DebugLog.Lua.warn(var6.getName() + " not spawned, position invalid");
               }

               var2 += 4.0F;
               if (var2 > (float)(IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMaxTiles() - 5)) {
                  var2 = (float)(IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles() + 5);
                  var3 += 8.0F;
               }
            }
         }

      }

      @LuaMethod(
         name = "addPhysicsObject",
         global = true
      )
      public static BaseVehicle addPhysicsObject() {
         MPStatistic.getInstance().Bullet.Start();
         int var0 = Bullet.addPhysicsObject(getPlayer().getX(), getPlayer().getY());
         MPStatistic.getInstance().Bullet.End();
         IsoPushableObject var1 = new IsoPushableObject(IsoWorld.instance.getCell(), IsoPlayer.getInstance().getCurrentSquare(), IsoSpriteManager.instance.getSprite("trashcontainers_01_16"));
         WorldSimulation.instance.physicsObjectMap.put(var0, var1);
         return null;
      }

      @LuaMethod(
         name = "toggleVehicleRenderToTexture",
         global = true
      )
      public static void toggleVehicleRenderToTexture() {
         BaseVehicle.RENDER_TO_TEXTURE = !BaseVehicle.RENDER_TO_TEXTURE;
      }

      @LuaMethod(
         name = "reloadSoundFiles",
         global = true
      )
      public static void reloadSoundFiles() {
         try {
            Iterator var0 = ZomboidFileSystem.instance.ActiveFileMap.keySet().iterator();

            while(var0.hasNext()) {
               String var1 = (String)var0.next();
               if (var1.matches(".*/sounds_.+\\.txt")) {
                  GameSounds.ReloadFile(var1);
               }
            }
         } catch (Throwable var2) {
            ExceptionLogger.logException(var2);
         }

      }

      @LuaMethod(
         name = "getAnimationViewerState",
         global = true
      )
      public static AnimationViewerState getAnimationViewerState() {
         return AnimationViewerState.instance;
      }

      @LuaMethod(
         name = "getAttachmentEditorState",
         global = true
      )
      public static AttachmentEditorState getAttachmentEditorState() {
         return AttachmentEditorState.instance;
      }

      @LuaMethod(
         name = "getEditVehicleState",
         global = true
      )
      public static EditVehicleState getEditVehicleState() {
         return EditVehicleState.instance;
      }

      @LuaMethod(
         name = "showAnimationViewer",
         global = true
      )
      public static void showAnimationViewer() {
         IngameState.instance.showAnimationViewer = true;
      }

      @LuaMethod(
         name = "showAttachmentEditor",
         global = true
      )
      public static void showAttachmentEditor() {
         IngameState.instance.showAttachmentEditor = true;
      }

      @LuaMethod(
         name = "showChunkDebugger",
         global = true
      )
      public static void showChunkDebugger() {
         IngameState.instance.showChunkDebugger = true;
      }

      @LuaMethod(
         name = "showGlobalObjectDebugger",
         global = true
      )
      public static void showGlobalObjectDebugger() {
         IngameState.instance.showGlobalObjectDebugger = true;
      }

      @LuaMethod(
         name = "showVehicleEditor",
         global = true
      )
      public static void showVehicleEditor(String var0) {
         IngameState.instance.showVehicleEditor = StringUtils.isNullOrWhitespace(var0) ? "" : var0;
      }

      @LuaMethod(
         name = "showWorldMapEditor",
         global = true
      )
      public static void showWorldMapEditor(String var0) {
         IngameState.instance.showWorldMapEditor = StringUtils.isNullOrWhitespace(var0) ? "" : var0;
      }

      @LuaMethod(
         name = "reloadVehicles",
         global = true
      )
      public static void reloadVehicles() {
         try {
            Iterator var0 = ScriptManager.instance.scriptsWithVehicleTemplates.iterator();

            String var1;
            while(var0.hasNext()) {
               var1 = (String)var0.next();
               ScriptManager.instance.LoadFile(var1, true);
            }

            var0 = ScriptManager.instance.scriptsWithVehicles.iterator();

            while(var0.hasNext()) {
               var1 = (String)var0.next();
               ScriptManager.instance.LoadFile(var1, true);
            }

            BaseVehicle.LoadAllVehicleTextures();
            var0 = IsoWorld.instance.CurrentCell.vehicles.iterator();

            while(var0.hasNext()) {
               BaseVehicle var3 = (BaseVehicle)var0.next();
               var3.scriptReloaded();
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }

      @LuaMethod(
         name = "reloadEngineRPM",
         global = true
      )
      public static void reloadEngineRPM() {
         try {
            ScriptManager.instance.LoadFile(ZomboidFileSystem.instance.getString("media/scripts/vehicles/engine_rpm.txt"), true);
         } catch (Exception var1) {
            var1.printStackTrace();
         }

      }

      @LuaMethod(
         name = "proceedPM",
         global = true
      )
      public static String proceedPM(String var0) {
         var0 = var0.trim();
         String var1 = null;
         String var2 = null;
         Matcher var3 = Pattern.compile("(\"[^\"]*\\s+[^\"]*\"|[^\"]\\S*)\\s(.+)").matcher(var0);
         if (var3.matches()) {
            var1 = var3.group(1);
            var2 = var3.group(2);
            String var4 = var1.replaceAll("\"", "");
            Thread var6 = new Thread(ThreadGroups.Workers, () -> {
               ChatManager.getInstance().sendWhisperMessage(var4, var2);
            });
            var6.setUncaughtExceptionHandler(GameWindow::uncaughtException);
            var6.start();
            return var1;
         } else {
            ChatManager.getInstance().addMessage("Error", getText("IGUI_Commands_Whisper"));
            return "";
         }
      }

      @LuaMethod(
         name = "processSayMessage",
         global = true
      )
      public static void processSayMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.say, var0);
         }
      }

      @LuaMethod(
         name = "processGeneralMessage",
         global = true
      )
      public static void processGeneralMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.general, var0);
         }
      }

      @LuaMethod(
         name = "processShoutMessage",
         global = true
      )
      public static void processShoutMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.shout, var0);
         }
      }

      @LuaMethod(
         name = "proceedFactionMessage",
         global = true
      )
      public static void ProceedFactionMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.faction, var0);
         }
      }

      @LuaMethod(
         name = "processSafehouseMessage",
         global = true
      )
      public static void ProcessSafehouseMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.safehouse, var0);
         }
      }

      @LuaMethod(
         name = "processAdminChatMessage",
         global = true
      )
      public static void ProcessAdminChatMessage(String var0) {
         if (var0 != null && !var0.isEmpty()) {
            var0 = var0.trim();
            ChatManager.getInstance().sendMessageToChat(ChatType.admin, var0);
         }
      }

      @LuaMethod(
         name = "showWrongChatTabMessage",
         global = true
      )
      public static void showWrongChatTabMessage(int var0, int var1, String var2) {
         String var3 = ChatManager.getInstance().getTabName((short)var0);
         String var4 = ChatManager.getInstance().getTabName((short)var1);
         String var5 = Translator.getText("UI_chat_wrong_tab", var3, var4, var2);
         ChatManager.getInstance().showServerChatMessage(var5);
      }

      @LuaMethod(
         name = "focusOnTab",
         global = true
      )
      public static void focusOnTab(Short var0) {
         ChatManager.getInstance().focusOnTab(var0);
      }

      @LuaMethod(
         name = "updateChatSettings",
         global = true
      )
      public static void updateChatSettings(String var0, boolean var1, boolean var2) {
         ChatManager.getInstance().updateChatSettings(var0, var1, var2);
      }

      @LuaMethod(
         name = "checkPlayerCanUseChat",
         global = true
      )
      public static Boolean checkPlayerCanUseChat(String var0) {
         var0 = var0.trim();
         byte var3 = -1;
         switch(var0.hashCode()) {
         case -1769046940:
            if (var0.equals("/safehouse")) {
               var3 = 10;
            }
            break;
         case -784181491:
            if (var0.equals("/faction")) {
               var3 = 8;
            }
            break;
         case 1554:
            if (var0.equals("/a")) {
               var3 = 1;
            }
            break;
         case 1559:
            if (var0.equals("/f")) {
               var3 = 7;
            }
            break;
         case 1571:
            if (var0.equals("/r")) {
               var3 = 14;
            }
            break;
         case 1572:
            if (var0.equals("/s")) {
               var3 = 3;
            }
            break;
         case 1576:
            if (var0.equals("/w")) {
               var3 = 11;
            }
            break;
         case 1578:
            if (var0.equals("/y")) {
               var3 = 5;
            }
            break;
         case 48836:
            if (var0.equals("/sh")) {
               var3 = 9;
            }
            break;
         case 1496850:
            if (var0.equals("/all")) {
               var3 = 0;
            }
            break;
         case 1513820:
            if (var0.equals("/say")) {
               var3 = 4;
            }
            break;
         case 47110715:
            if (var0.equals("/yell")) {
               var3 = 6;
            }
            break;
         case 1438238848:
            if (var0.equals("/admin")) {
               var3 = 2;
            }
            break;
         case 1453840684:
            if (var0.equals("/radio")) {
               var3 = 13;
            }
            break;
         case 1624401011:
            if (var0.equals("/whisper")) {
               var3 = 12;
            }
         }

         ChatType var1;
         switch(var3) {
         case 0:
            var1 = ChatType.general;
            break;
         case 1:
         case 2:
            var1 = ChatType.admin;
            break;
         case 3:
         case 4:
            var1 = ChatType.say;
            break;
         case 5:
         case 6:
            var1 = ChatType.shout;
            break;
         case 7:
         case 8:
            var1 = ChatType.faction;
            break;
         case 9:
         case 10:
            var1 = ChatType.safehouse;
            break;
         case 11:
         case 12:
            var1 = ChatType.whisper;
            break;
         case 13:
         case 14:
            var1 = ChatType.radio;
            break;
         default:
            var1 = ChatType.notDefined;
            DebugLog.Lua.warn("Chat command not found");
         }

         return ChatManager.getInstance().isPlayerCanUseChat(var1);
      }

      @LuaMethod(
         name = "reloadVehicleTextures",
         global = true
      )
      public static void reloadVehicleTextures(String var0) {
         VehicleScript var1 = ScriptManager.instance.getVehicle(var0);
         if (var1 == null) {
            DebugLog.Lua.warn("no such vehicle script");
         } else {
            for(int var2 = 0; var2 < var1.getSkinCount(); ++var2) {
               VehicleScript.Skin var3 = var1.getSkin(var2);
               if (var3.texture != null) {
                  Texture.reload("media/textures/" + var3.texture + ".png");
               }

               if (var3.textureRust != null) {
                  Texture.reload("media/textures/" + var3.textureRust + ".png");
               }

               if (var3.textureMask != null) {
                  Texture.reload("media/textures/" + var3.textureMask + ".png");
               }

               if (var3.textureLights != null) {
                  Texture.reload("media/textures/" + var3.textureLights + ".png");
               }

               if (var3.textureDamage1Overlay != null) {
                  Texture.reload("media/textures/" + var3.textureDamage1Overlay + ".png");
               }

               if (var3.textureDamage1Shell != null) {
                  Texture.reload("media/textures/" + var3.textureDamage1Shell + ".png");
               }

               if (var3.textureDamage2Overlay != null) {
                  Texture.reload("media/textures/" + var3.textureDamage2Overlay + ".png");
               }

               if (var3.textureDamage2Shell != null) {
                  Texture.reload("media/textures/" + var3.textureDamage2Shell + ".png");
               }

               if (var3.textureShadow != null) {
                  Texture.reload("media/textures/" + var3.textureShadow + ".png");
               }
            }

         }
      }

      @LuaMethod(
         name = "useStaticErosionRand",
         global = true
      )
      public static void useStaticErosionRand(boolean var0) {
         ErosionData.staticRand = var0;
      }

      @LuaMethod(
         name = "getClimateManager",
         global = true
      )
      public static ClimateManager getClimateManager() {
         return ClimateManager.getInstance();
      }

      @LuaMethod(
         name = "getClimateMoon",
         global = true
      )
      public static ClimateMoon getClimateMoon() {
         return ClimateMoon.getInstance();
      }

      @LuaMethod(
         name = "getWorldMarkers",
         global = true
      )
      public static WorldMarkers getWorldMarkers() {
         return WorldMarkers.instance;
      }

      @LuaMethod(
         name = "getIsoMarkers",
         global = true
      )
      public static IsoMarkers getIsoMarkers() {
         return IsoMarkers.instance;
      }

      @LuaMethod(
         name = "getErosion",
         global = true
      )
      public static ErosionMain getErosion() {
         return ErosionMain.getInstance();
      }

      @LuaMethod(
         name = "getAllOutfits",
         global = true
      )
      public static ArrayList getAllOutfits(boolean var0) {
         ArrayList var1 = new ArrayList();
         ModelManager.instance.create();
         if (OutfitManager.instance == null) {
            return var1;
         } else {
            ArrayList var2 = var0 ? OutfitManager.instance.m_FemaleOutfits : OutfitManager.instance.m_MaleOutfits;
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Outfit var4 = (Outfit)var3.next();
               var1.add(var4.m_Name);
            }

            Collections.sort(var1);
            return var1;
         }
      }

      @LuaMethod(
         name = "getAllVehicles",
         global = true
      )
      public static ArrayList getAllVehicles() {
         return (ArrayList)ScriptManager.instance.getAllVehicleScripts().stream().filter((var0) -> {
            return !var0.getName().contains("Smashed") && !var0.getName().contains("Burnt");
         }).map(VehicleScript::getFullName).sorted().collect(Collectors.toCollection(ArrayList::new));
      }

      @LuaMethod(
         name = "getAllHairStyles",
         global = true
      )
      public static ArrayList getAllHairStyles(boolean var0) {
         ArrayList var1 = new ArrayList();
         if (HairStyles.instance == null) {
            return var1;
         } else {
            ArrayList var2 = new ArrayList(var0 ? HairStyles.instance.m_FemaleStyles : HairStyles.instance.m_MaleStyles);
            var2.sort((var0x, var1x) -> {
               if (var0x.name.isEmpty()) {
                  return -1;
               } else if (var1x.name.isEmpty()) {
                  return 1;
               } else {
                  String var2 = getText("IGUI_Hair_" + var0x.name);
                  String var3 = getText("IGUI_Hair_" + var1x.name);
                  return var2.compareTo(var3);
               }
            });
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               HairStyle var4 = (HairStyle)var3.next();
               var1.add(var4.name);
            }

            return var1;
         }
      }

      @LuaMethod(
         name = "getHairStylesInstance",
         global = true
      )
      public static HairStyles getHairStylesInstance() {
         return HairStyles.instance;
      }

      @LuaMethod(
         name = "getBeardStylesInstance",
         global = true
      )
      public static BeardStyles getBeardStylesInstance() {
         return BeardStyles.instance;
      }

      @LuaMethod(
         name = "getAllBeardStyles",
         global = true
      )
      public static ArrayList getAllBeardStyles() {
         ArrayList var0 = new ArrayList();
         if (BeardStyles.instance == null) {
            return var0;
         } else {
            ArrayList var1 = new ArrayList(BeardStyles.instance.m_Styles);
            var1.sort((var0x, var1x) -> {
               if (var0x.name.isEmpty()) {
                  return -1;
               } else if (var1x.name.isEmpty()) {
                  return 1;
               } else {
                  String var2 = getText("IGUI_Beard_" + var0x.name);
                  String var3 = getText("IGUI_Beard_" + var1x.name);
                  return var2.compareTo(var3);
               }
            });
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               BeardStyle var3 = (BeardStyle)var2.next();
               var0.add(var3.name);
            }

            return var0;
         }
      }

      @LuaMethod(
         name = "getAllItemsForBodyLocation",
         global = true
      )
      public static KahluaTable getAllItemsForBodyLocation(String var0) {
         KahluaTable var1 = LuaManager.platform.newTable();
         if (StringUtils.isNullOrWhitespace(var0)) {
            return var1;
         } else {
            int var2 = 1;
            ArrayList var3 = ScriptManager.instance.getAllItems();
            Iterator var4 = var3.iterator();

            while(true) {
               Item var5;
               do {
                  do {
                     if (!var4.hasNext()) {
                        return var1;
                     }

                     var5 = (Item)var4.next();
                  } while(StringUtils.isNullOrWhitespace(var5.getClothingItem()));
               } while(!var0.equals(var5.getBodyLocation()) && !var0.equals(var5.CanBeEquipped));

               var1.rawset(var2++, var5.getFullName());
            }
         }
      }

      @LuaMethod(
         name = "getAllDecalNamesForItem",
         global = true
      )
      public static ArrayList getAllDecalNamesForItem(InventoryItem var0) {
         ArrayList var1 = new ArrayList();
         if (var0 != null && ClothingDecals.instance != null) {
            ClothingItem var2 = var0.getClothingItem();
            if (var2 == null) {
               return var1;
            } else {
               String var3 = var2.getDecalGroup();
               if (StringUtils.isNullOrWhitespace(var3)) {
                  return var1;
               } else {
                  ClothingDecalGroup var4 = ClothingDecals.instance.FindGroup(var3);
                  if (var4 == null) {
                     return var1;
                  } else {
                     var4.getDecals(var1);
                     return var1;
                  }
               }
            }
         } else {
            return var1;
         }
      }

      @LuaMethod(
         name = "screenZoomIn",
         global = true
      )
      public void screenZoomIn() {
      }

      @LuaMethod(
         name = "screenZoomOut",
         global = true
      )
      public void screenZoomOut() {
      }

      @LuaMethod(
         name = "addSound",
         global = true
      )
      public void addSound(IsoObject var1, int var2, int var3, int var4, int var5, int var6) {
         WorldSoundManager.instance.addSound(var1, var2, var3, var4, var5, var6);
      }

      @LuaMethod(
         name = "sendAddXp",
         global = true
      )
      public void sendAddXp(IsoPlayer var1, PerkFactory.Perk var2, int var3, boolean var4, boolean var5) {
         if (GameClient.bClient && var1.isExistInTheWorld()) {
            GameClient.instance.sendAddXpFromPlayerStatsUI(var1, var2, var3, var4, var5);
         }

      }

      @LuaMethod(
         name = "SyncXp",
         global = true
      )
      public void SyncXp(IsoPlayer var1) {
         if (GameClient.bClient) {
            GameClient.instance.sendSyncXp(var1);
         }

      }

      @LuaMethod(
         name = "checkServerName",
         global = true
      )
      public String checkServerName(String var1) {
         String var2 = ProfanityFilter.getInstance().validateString(var1, true, true, true);
         return !StringUtils.isNullOrEmpty(var2) ? Translator.getText("UI_BadWordCheck", var2) : null;
      }

      @LuaMethod(
         name = "Render3DItem",
         global = true
      )
      public void Render3DItem(InventoryItem var1, IsoGridSquare var2, float var3, float var4, float var5, float var6) {
         WorldItemModelDrawer.renderMain(var1, var2, var3, var4, var5, 0.0F, var6);
      }

      @LuaMethod(
         name = "getContainerOverlays",
         global = true
      )
      public ContainerOverlays getContainerOverlays() {
         return ContainerOverlays.instance;
      }

      @LuaMethod(
         name = "getTileOverlays",
         global = true
      )
      public TileOverlays getTileOverlays() {
         return TileOverlays.instance;
      }

      @LuaMethod(
         name = "getAverageFPS",
         global = true
      )
      public Double getAverageFSP() {
         float var1 = GameWindow.averageFPS;
         if (!PerformanceSettings.isUncappedFPS()) {
            var1 = Math.min(var1, (float)PerformanceSettings.getLockFPS());
         }

         return BoxedStaticValues.toDouble(Math.floor((double)var1));
      }

      @LuaMethod(
         name = "getServerStatistic",
         global = true
      )
      public static KahluaTable getServerStatistic() {
         return MPStatistic.getInstance().getStatisticTableForLua();
      }

      @LuaMethod(
         name = "setServerStatisticEnable",
         global = true
      )
      public static void setServerStatisticEnable(boolean var0) {
         if (GameClient.bClient) {
            GameClient.setServerStatisticEnable(var0);
         }

      }

      @LuaMethod(
         name = "getServerStatisticEnable",
         global = true
      )
      public static boolean getServerStatisticEnable() {
         return GameClient.bClient ? GameClient.getServerStatisticEnable() : false;
      }

      @LuaMethod(
         name = "getSearchMode",
         global = true
      )
      public static SearchMode getSearchMode() {
         return SearchMode.getInstance();
      }

      @LuaMethod(
         name = "timSort",
         global = true
      )
      public static void timSort(KahluaTable var0, Object var1) {
         KahluaTableImpl var2 = (KahluaTableImpl)Type.tryCastTo(var0, KahluaTableImpl.class);
         if (var2 != null && var2.len() >= 2 && var1 != null) {
            timSortComparator.comp = var1;
            Object[] var3 = var2.delegate.values().toArray();
            Arrays.sort(var3, timSortComparator);

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var2.rawset(var4 + 1, var3[var4]);
               var3[var4] = null;
            }

         }
      }

      public static final class LuaFileWriter {
         private final PrintWriter writer;

         public LuaFileWriter(PrintWriter var1) {
            this.writer = var1;
         }

         public void write(String var1) throws IOException {
            this.writer.write(var1);
         }

         public void writeln(String var1) throws IOException {
            this.writer.write(var1);
            this.writer.write(System.lineSeparator());
         }

         public void close() throws IOException {
            this.writer.close();
         }
      }

      private static final class ItemQuery implements ISteamWorkshopCallback {
         private LuaClosure functionObj;
         private Object arg1;
         private long handle;

         public ItemQuery(ArrayList var1, LuaClosure var2, Object var3) {
            this.functionObj = var2;
            this.arg1 = var3;
            long[] var4 = new long[var1.size()];
            int var5 = 0;

            for(int var6 = 0; var6 < var1.size(); ++var6) {
               long var7 = SteamUtils.convertStringToSteamID((String)var1.get(var6));
               if (var7 != -1L) {
                  var4[var5++] = var7;
               }
            }

            this.handle = SteamWorkshop.instance.CreateQueryUGCDetailsRequest(var4, this);
            if (this.handle == 0L) {
               SteamWorkshop.instance.RemoveCallback(this);
               if (var3 == null) {
                  LuaManager.caller.pcall(LuaManager.thread, var2, (Object)"NotCompleted");
               } else {
                  LuaManager.caller.pcall(LuaManager.thread, var2, (Object[])(var3, "NotCompleted"));
               }
            }

         }

         public void onItemCreated(long var1, boolean var3) {
         }

         public void onItemNotCreated(int var1) {
         }

         public void onItemUpdated(boolean var1) {
         }

         public void onItemNotUpdated(int var1) {
         }

         public void onItemSubscribed(long var1) {
         }

         public void onItemNotSubscribed(long var1, int var3) {
         }

         public void onItemDownloaded(long var1) {
         }

         public void onItemNotDownloaded(long var1, int var3) {
         }

         public void onItemQueryCompleted(long var1, int var3) {
            if (var1 == this.handle) {
               SteamWorkshop.instance.RemoveCallback(this);
               ArrayList var4 = new ArrayList();

               for(int var5 = 0; var5 < var3; ++var5) {
                  SteamUGCDetails var6 = SteamWorkshop.instance.GetQueryUGCResult(var1, var5);
                  if (var6 != null) {
                     var4.add(var6);
                  }
               }

               SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
               if (this.arg1 == null) {
                  LuaManager.caller.pcall(LuaManager.thread, this.functionObj, (Object[])("Completed", var4));
               } else {
                  LuaManager.caller.pcall(LuaManager.thread, this.functionObj, (Object[])(this.arg1, "Completed", var4));
               }

            }
         }

         public void onItemQueryNotCompleted(long var1, int var3) {
            if (var1 == this.handle) {
               SteamWorkshop.instance.RemoveCallback(this);
               SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
               if (this.arg1 == null) {
                  LuaManager.caller.pcall(LuaManager.thread, this.functionObj, (Object)"NotCompleted");
               } else {
                  LuaManager.caller.pcall(LuaManager.thread, this.functionObj, (Object[])(this.arg1, "NotCompleted"));
               }

            }
         }
      }

      private static final class TimSortComparator implements Comparator {
         Object comp;

         public int compare(Object var1, Object var2) {
            if (Objects.equals(var1, var2)) {
               return 0;
            } else {
               Boolean var3 = LuaManager.thread.pcallBoolean(this.comp, var1, var2);
               return var3 == Boolean.TRUE ? -1 : 1;
            }
         }
      }
   }
}
