package zombie.iso;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL20;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.MapCollisionData;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.ZomboidBitFlag;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.states.ZombieIdleState;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.Moodles.MoodleType;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.RenderSettings;
import zombie.core.opengl.Shader;
import zombie.core.opengl.ShaderProgram;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.erosion.ErosionData;
import zombie.erosion.categories.ErosionCategory;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.areas.SafeHouse;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.regions.IWorldRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoBrokenGlass;
import zombie.iso.objects.IsoCarBatteryCharger;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoFire;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoGenerator;
import zombie.iso.objects.IsoLightSwitch;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoRainSplash;
import zombie.iso.objects.IsoRaindrop;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTrap;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWaveSignal;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.sprite.shapers.FloorShaper;
import zombie.iso.sprite.shapers.FloorShaperAttachedSprites;
import zombie.iso.sprite.shapers.FloorShaperDeDiamond;
import zombie.iso.sprite.shapers.FloorShaperDiamond;
import zombie.iso.sprite.shapers.WallShaperN;
import zombie.iso.sprite.shapers.WallShaperW;
import zombie.iso.sprite.shapers.WallShaperWhole;
import zombie.iso.weather.fx.WeatherFxMask;
import zombie.meta.Meta;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerLOS;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.io.BitHeader;
import zombie.util.io.BitHeaderRead;
import zombie.util.io.BitHeaderWrite;
import zombie.util.list.PZArrayList;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PolygonalMap2;

public final class IsoGridSquare {
   private boolean hasTree;
   private ArrayList LightInfluenceB;
   private ArrayList LightInfluenceG;
   private ArrayList LightInfluenceR;
   public final IsoGridSquare[] nav = new IsoGridSquare[8];
   public int collideMatrix = -1;
   public int pathMatrix = -1;
   public int visionMatrix = -1;
   public IsoRoom room = null;
   public IsoGridSquare w;
   public IsoGridSquare nw;
   public IsoGridSquare sw;
   public IsoGridSquare s;
   public IsoGridSquare n;
   public IsoGridSquare ne;
   public IsoGridSquare se;
   public IsoGridSquare e;
   public boolean haveSheetRope = false;
   private IWorldRegion isoWorldRegion;
   private boolean hasSetIsoWorldRegion = false;
   public int ObjectsSyncCount = 0;
   public IsoBuilding roofHideBuilding;
   public boolean bFlattenGrassEtc;
   private static final long VisiFlagTimerPeriod_ms = 750L;
   private final boolean[] playerCutawayFlags = new boolean[4];
   private final long[] playerCutawayFlagLockUntilTimes = new long[4];
   private final boolean[] targetPlayerCutawayFlags = new boolean[4];
   private final boolean[] playerIsDissolvedFlags = new boolean[4];
   private final long[] playerIsDissolvedFlagLockUntilTimes = new long[4];
   private final boolean[] targetPlayerIsDissolvedFlags = new boolean[4];
   private IsoWaterGeometry water = null;
   private IsoPuddlesGeometry puddles = null;
   private float puddlesCacheSize = -1.0F;
   private float puddlesCacheLevel = -1.0F;
   public final IsoGridSquare.ILighting[] lighting = new IsoGridSquare.ILighting[4];
   public int x;
   public int y;
   public int z;
   private int CachedScreenValue = -1;
   public float CachedScreenX;
   public float CachedScreenY;
   private static long torchTimer = 0L;
   public boolean SolidFloorCached = false;
   public boolean SolidFloor = false;
   private boolean CacheIsFree = false;
   private boolean CachedIsFree = false;
   public IsoChunk chunk;
   public int roomID = -1;
   public Integer ID = -999;
   public IsoMetaGrid.Zone zone;
   private final ArrayList DeferedCharacters = new ArrayList();
   private int DeferredCharacterTick = -1;
   private final ArrayList StaticMovingObjects = new ArrayList(0);
   private final ArrayList MovingObjects = new ArrayList(0);
   protected final PZArrayList Objects = new PZArrayList(IsoObject.class, 2);
   protected final PZArrayList localTemporaryObjects = new PZArrayList(IsoObject.class, 2);
   private final ArrayList WorldObjects = new ArrayList();
   final ZomboidBitFlag hasTypes;
   private final PropertyContainer Properties;
   private final ArrayList SpecialObjects;
   public boolean haveRoof;
   private boolean burntOut;
   private boolean bHasFlies;
   private IsoGridOcclusionData OcclusionDataCache;
   public static final ConcurrentLinkedQueue isoGridSquareCache = new ConcurrentLinkedQueue();
   public static ArrayDeque loadGridSquareCache;
   private boolean overlayDone;
   private KahluaTable table;
   private int trapPositionX;
   private int trapPositionY;
   private int trapPositionZ;
   private boolean haveElectricity;
   public static int gridSquareCacheEmptyTimer = 0;
   private static float darkStep = 0.06F;
   public static int RecalcLightTime = 0;
   private static int lightcache = 0;
   public static final ArrayList choices = new ArrayList();
   public static boolean USE_WALL_SHADER = true;
   private static final int cutawayY = 0;
   private static final int cutawayNWWidth = 66;
   private static final int cutawayNWHeight = 226;
   private static final int cutawaySEXCut = 1084;
   private static final int cutawaySEXUncut = 1212;
   private static final int cutawaySEWidth = 6;
   private static final int cutawaySEHeight = 196;
   private static final int cutawayNXFullyCut = 700;
   private static final int cutawayNXCutW = 444;
   private static final int cutawayNXUncut = 828;
   private static final int cutawayNXCutE = 956;
   private static final int cutawayWXFullyCut = 512;
   private static final int cutawayWXCutS = 768;
   private static final int cutawayWXUncut = 896;
   private static final int cutawayWXCutN = 256;
   private static final int cutawayFenceXOffset = 1;
   private static final int cutawayLogWallXOffset = 1;
   private static final int cutawaySpiffoWindowXOffset = -24;
   private static final int cutawayRoof4XOffset = -60;
   private static final int cutawayRoof17XOffset = -46;
   private static final int cutawayRoof28XOffset = -60;
   private static final int cutawayRoof41XOffset = -46;
   private static final ColorInfo lightInfoTemp = new ColorInfo();
   private static final float doorWindowCutawayLightMin = 0.3F;
   private static boolean bWallCutawayW;
   private static boolean bWallCutawayN;
   public boolean isSolidFloorCache;
   public boolean isExteriorCache;
   public boolean isVegitationCache;
   public int hourLastSeen;
   static IsoGridSquare lastLoaded = null;
   public static int IDMax = -1;
   static int col = -1;
   static int path = -1;
   static int pathdoor = -1;
   static int vision = -1;
   public long hashCodeObjects;
   static final Color tr = new Color(1, 1, 1, 1);
   static final Color tl = new Color(1, 1, 1, 1);
   static final Color br = new Color(1, 1, 1, 1);
   static final Color bl = new Color(1, 1, 1, 1);
   static final Color interp1 = new Color(1, 1, 1, 1);
   static final Color interp2 = new Color(1, 1, 1, 1);
   static final Color finalCol = new Color(1, 1, 1, 1);
   public static final IsoGridSquare.CellGetSquare cellGetSquare = new IsoGridSquare.CellGetSquare();
   public boolean propertiesDirty;
   public static boolean UseSlowCollision = false;
   private static boolean bDoSlowPathfinding = false;
   private static final Comparator comp = (var0, var1) -> {
      return var0.compareToY(var1);
   };
   public static boolean isOnScreenLast = false;
   private float splashX;
   private float splashY;
   private float splashFrame;
   private int splashFrameNum;
   private final ColorInfo[] lightInfo;
   static String[] rainsplashCache = new String[50];
   private static final ColorInfo defColorInfo = new ColorInfo();
   private static final ColorInfo blackColorInfo = new ColorInfo();
   static int colu = 0;
   static int coll = 0;
   static int colr = 0;
   static int colu2 = 0;
   static int coll2 = 0;
   static int colr2 = 0;
   public static boolean CircleStencil = false;
   public static float rmod = 0.0F;
   public static float gmod = 0.0F;
   public static float bmod = 0.0F;
   static final Vector2 tempo = new Vector2();
   static final Vector2 tempo2 = new Vector2();
   private IsoRaindrop RainDrop;
   private IsoRainSplash RainSplash;
   private ErosionData.Square erosion;
   public static final int WALL_TYPE_N = 1;
   public static final int WALL_TYPE_S = 2;
   public static final int WALL_TYPE_W = 4;
   public static final int WALL_TYPE_E = 8;

   public static boolean getMatrixBit(int var0, int var1, int var2, int var3) {
      return getMatrixBit(var0, (byte)var1, (byte)var2, (byte)var3);
   }

   public static boolean getMatrixBit(int var0, byte var1, byte var2, byte var3) {
      return (var0 >> var1 + var2 * 3 + var3 * 9 & 1) != 0;
   }

   public static int setMatrixBit(int var0, int var1, int var2, int var3, boolean var4) {
      return setMatrixBit(var0, (byte)var1, (byte)var2, (byte)var3, var4);
   }

   public static int setMatrixBit(int var0, byte var1, byte var2, byte var3, boolean var4) {
      return var4 ? var0 | 1 << var1 + var2 * 3 + var3 * 9 : var0 & ~(1 << var1 + var2 * 3 + var3 * 9);
   }

   public void setPlayerCutawayFlag(int var1, boolean var2, long var3) {
      this.targetPlayerCutawayFlags[var1] = var2;
      if (var3 > this.playerCutawayFlagLockUntilTimes[var1] && this.playerCutawayFlags[var1] != this.targetPlayerCutawayFlags[var1]) {
         this.playerCutawayFlags[var1] = this.targetPlayerCutawayFlags[var1];
         this.playerCutawayFlagLockUntilTimes[var1] = var3 + 750L;
      }

   }

   public boolean getPlayerCutawayFlag(int var1, long var2) {
      return var2 > this.playerCutawayFlagLockUntilTimes[var1] ? this.targetPlayerCutawayFlags[var1] : this.playerCutawayFlags[var1];
   }

   public void setIsDissolved(int var1, boolean var2, long var3) {
      this.targetPlayerIsDissolvedFlags[var1] = var2;
      if (var3 > this.playerIsDissolvedFlagLockUntilTimes[var1] && this.playerIsDissolvedFlags[var1] != this.targetPlayerIsDissolvedFlags[var1]) {
         this.playerIsDissolvedFlags[var1] = this.targetPlayerIsDissolvedFlags[var1];
         this.playerIsDissolvedFlagLockUntilTimes[var1] = var3 + 750L;
      }

   }

   public boolean getIsDissolved(int var1, long var2) {
      return var2 > this.playerIsDissolvedFlagLockUntilTimes[var1] ? this.targetPlayerIsDissolvedFlags[var1] : this.playerIsDissolvedFlags[var1];
   }

   public IsoWaterGeometry getWater() {
      if (this.water != null && this.water.m_adjacentChunkLoadedCounter != this.chunk.m_adjacentChunkLoadedCounter) {
         this.water.m_adjacentChunkLoadedCounter = this.chunk.m_adjacentChunkLoadedCounter;
         if (this.water.hasWater || this.water.bShore) {
            this.clearWater();
         }
      }

      if (this.water == null) {
         try {
            this.water = (IsoWaterGeometry)IsoWaterGeometry.pool.alloc();
            this.water.m_adjacentChunkLoadedCounter = this.chunk.m_adjacentChunkLoadedCounter;
            if (this.water.init(this) == null) {
               IsoWaterGeometry.pool.release((Object)this.water);
               this.water = null;
            }
         } catch (Exception var2) {
            this.clearWater();
         }
      }

      return this.water;
   }

   public void clearWater() {
      if (this.water != null) {
         IsoWaterGeometry.pool.release((Object)this.water);
         this.water = null;
      }

   }

   public IsoPuddlesGeometry getPuddles() {
      if (this.puddles == null) {
         try {
            synchronized(IsoPuddlesGeometry.pool) {
               this.puddles = (IsoPuddlesGeometry)IsoPuddlesGeometry.pool.alloc();
            }

            this.puddles.square = this;
            this.puddles.bRecalc = true;
         } catch (Exception var4) {
            this.clearPuddles();
         }
      }

      return this.puddles;
   }

   public void clearPuddles() {
      if (this.puddles != null) {
         this.puddles.square = null;
         synchronized(IsoPuddlesGeometry.pool) {
            IsoPuddlesGeometry.pool.release((Object)this.puddles);
         }

         this.puddles = null;
      }

   }

   public float getPuddlesInGround() {
      if (this.isInARoom()) {
         return -1.0F;
      } else {
         if ((double)Math.abs(IsoPuddles.getInstance().getPuddlesSize() + (float)Core.getInstance().getPerfPuddles() + (float)IsoCamera.frameState.OffscreenWidth - this.puddlesCacheSize) > 0.01D) {
            this.puddlesCacheSize = IsoPuddles.getInstance().getPuddlesSize() + (float)Core.getInstance().getPerfPuddles() + (float)IsoCamera.frameState.OffscreenWidth;
            this.puddlesCacheLevel = IsoPuddlesCompute.computePuddle(this);
         }

         return this.puddlesCacheLevel;
      }
   }

   public IsoGridOcclusionData getOcclusionData() {
      return this.OcclusionDataCache;
   }

   public IsoGridOcclusionData getOrCreateOcclusionData() {
      assert !GameServer.bServer;

      if (this.OcclusionDataCache == null) {
         this.OcclusionDataCache = new IsoGridOcclusionData(this);
      }

      return this.OcclusionDataCache;
   }

   public void softClear() {
      this.zone = null;
      this.room = null;
      this.w = null;
      this.nw = null;
      this.sw = null;
      this.s = null;
      this.n = null;
      this.ne = null;
      this.se = null;
      this.e = null;
      this.isoWorldRegion = null;
      this.hasSetIsoWorldRegion = false;

      for(int var1 = 0; var1 < 8; ++var1) {
         this.nav[var1] = null;
      }

   }

   public float getGridSneakModifier(boolean var1) {
      if (!var1) {
         if (this.Properties.Is("CloseSneakBonus")) {
            return (float)Integer.parseInt(this.Properties.Val("CloseSneakBonus")) / 100.0F;
         }

         if (this.Properties.Is(IsoFlagType.collideN) || this.Properties.Is(IsoFlagType.collideW) || this.Properties.Is(IsoFlagType.WindowN) || this.Properties.Is(IsoFlagType.WindowW) || this.Properties.Is(IsoFlagType.doorN) || this.Properties.Is(IsoFlagType.doorW)) {
            return 8.0F;
         }
      } else if (this.Properties.Is(IsoFlagType.solidtrans)) {
         return 4.0F;
      }

      return 1.0F;
   }

   public boolean isSomethingTo(IsoGridSquare var1) {
      return this.isWallTo(var1) || this.isWindowTo(var1) || this.isDoorTo(var1);
   }

   public IsoObject getTransparentWallTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this && this.isWallTo(var1)) {
         if (var1.x > this.x && var1.Properties.Is(IsoFlagType.SpearOnlyAttackThrough) && !var1.Properties.Is(IsoFlagType.WindowW)) {
            return var1.getWall();
         } else if (this.x > var1.x && this.Properties.Is(IsoFlagType.SpearOnlyAttackThrough) && !this.Properties.Is(IsoFlagType.WindowW)) {
            return this.getWall();
         } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.SpearOnlyAttackThrough) && !var1.Properties.Is(IsoFlagType.WindowN)) {
            return var1.getWall();
         } else if (this.y > var1.y && this.Properties.Is(IsoFlagType.SpearOnlyAttackThrough) && !this.Properties.Is(IsoFlagType.WindowN)) {
            return this.getWall();
         } else {
            if (var1.x != this.x && var1.y != this.y) {
               IsoObject var2 = this.getTransparentWallTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z));
               IsoObject var3 = this.getTransparentWallTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z));
               if (var2 != null) {
                  return var2;
               }

               if (var3 != null) {
                  return var3;
               }

               var2 = var1.getTransparentWallTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z));
               var3 = var1.getTransparentWallTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z));
               if (var2 != null) {
                  return var2;
               }

               if (var3 != null) {
                  return var3;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public boolean isWallTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         if (var1.x > this.x && var1.Properties.Is(IsoFlagType.collideW) && !var1.Properties.Is(IsoFlagType.WindowW)) {
            return true;
         } else if (this.x > var1.x && this.Properties.Is(IsoFlagType.collideW) && !this.Properties.Is(IsoFlagType.WindowW)) {
            return true;
         } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.collideN) && !var1.Properties.Is(IsoFlagType.WindowN)) {
            return true;
         } else if (this.y > var1.y && this.Properties.Is(IsoFlagType.collideN) && !this.Properties.Is(IsoFlagType.WindowN)) {
            return true;
         } else {
            if (var1.x != this.x && var1.y != this.y) {
               if (this.isWallTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || this.isWallTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }

               if (var1.isWallTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || var1.isWallTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isWindowTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         if (var1.x > this.x && var1.Properties.Is(IsoFlagType.windowW)) {
            return true;
         } else if (this.x > var1.x && this.Properties.Is(IsoFlagType.windowW)) {
            return true;
         } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.windowN)) {
            return true;
         } else if (this.y > var1.y && this.Properties.Is(IsoFlagType.windowN)) {
            return true;
         } else {
            if (var1.x != this.x && var1.y != this.y) {
               if (this.isWindowTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || this.isWindowTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }

               if (var1.isWindowTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || var1.isWindowTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean haveDoor() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         if (this.Objects.get(var1) instanceof IsoDoor) {
            return true;
         }
      }

      return false;
   }

   public boolean isDoorTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         if (var1.x > this.x && var1.Properties.Is(IsoFlagType.doorW)) {
            return true;
         } else if (this.x > var1.x && this.Properties.Is(IsoFlagType.doorW)) {
            return true;
         } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.doorN)) {
            return true;
         } else if (this.y > var1.y && this.Properties.Is(IsoFlagType.doorN)) {
            return true;
         } else {
            if (var1.x != this.x && var1.y != this.y) {
               if (this.isDoorTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || this.isDoorTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }

               if (var1.isDoorTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || var1.isDoorTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isBlockedTo(IsoGridSquare var1) {
      return this.isWallTo(var1) || this.isWindowBlockedTo(var1) || this.isDoorBlockedTo(var1);
   }

   public boolean isWindowBlockedTo(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else if (var1.x > this.x && var1.hasBlockedWindow(false)) {
         return true;
      } else if (this.x > var1.x && this.hasBlockedWindow(false)) {
         return true;
      } else if (var1.y > this.y && var1.hasBlockedWindow(true)) {
         return true;
      } else if (this.y > var1.y && this.hasBlockedWindow(true)) {
         return true;
      } else {
         if (var1.x != this.x && var1.y != this.y) {
            if (this.isWindowBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || this.isWindowBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
               return true;
            }

            if (var1.isWindowBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || var1.isWindowBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasBlockedWindow(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3 instanceof IsoWindow) {
            IsoWindow var4 = (IsoWindow)var3;
            if (var4.getNorth() == var1) {
               return !var4.isDestroyed() && !var4.open || var4.isBarricaded();
            }
         }
      }

      return false;
   }

   public boolean isDoorBlockedTo(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else if (var1.x > this.x && var1.hasBlockedDoor(false)) {
         return true;
      } else if (this.x > var1.x && this.hasBlockedDoor(false)) {
         return true;
      } else if (var1.y > this.y && var1.hasBlockedDoor(true)) {
         return true;
      } else if (this.y > var1.y && this.hasBlockedDoor(true)) {
         return true;
      } else {
         if (var1.x != this.x && var1.y != this.y) {
            if (this.isDoorBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || this.isDoorBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
               return true;
            }

            if (var1.isDoorBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(var1.x, this.y, this.z)) || var1.isDoorBlockedTo(IsoWorld.instance.CurrentCell.getGridSquare(this.x, var1.y, this.z))) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasBlockedDoor(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3 instanceof IsoDoor) {
            IsoDoor var4 = (IsoDoor)var3;
            if (var4.getNorth() == var1) {
               return !var4.open || var4.isBarricaded();
            }
         }

         if (var3 instanceof IsoThumpable) {
            IsoThumpable var5 = (IsoThumpable)var3;
            if (var5.isDoor() && var5.getNorth() == var1) {
               return !var5.open || var5.isBarricaded();
            }
         }
      }

      return false;
   }

   public IsoCurtain getCurtain(IsoObjectType var1) {
      for(int var2 = 0; var2 < this.getSpecialObjects().size(); ++var2) {
         IsoCurtain var3 = (IsoCurtain)Type.tryCastTo((IsoObject)this.getSpecialObjects().get(var2), IsoCurtain.class);
         if (var3 != null && var3.getType() == var1) {
            return var3;
         }
      }

      return null;
   }

   public IsoObject getHoppable(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         PropertyContainer var4 = var3.getProperties();
         if (var4 != null && var4.Is(var1 ? IsoFlagType.HoppableN : IsoFlagType.HoppableW)) {
            return var3;
         }

         if (var4 != null && var4.Is(var1 ? IsoFlagType.WindowN : IsoFlagType.WindowW)) {
            return var3;
         }
      }

      return null;
   }

   public IsoObject getHoppableTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoObject var2;
         if (var1.x < this.x && var1.y == this.y) {
            var2 = this.getHoppable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x == this.x && var1.y < this.y) {
            var2 = this.getHoppable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x && var1.y == this.y) {
            var2 = var1.getHoppable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x == this.x && var1.y > this.y) {
            var2 = var1.getHoppable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getHoppableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getHoppableTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getHoppableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getHoppableTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public boolean isHoppableTo(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else if (var1.x != this.x && var1.y != this.y) {
         return false;
      } else if (var1.x > this.x && var1.Properties.Is(IsoFlagType.HoppableW)) {
         return true;
      } else if (this.x > var1.x && this.Properties.Is(IsoFlagType.HoppableW)) {
         return true;
      } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.HoppableN)) {
         return true;
      } else {
         return this.y > var1.y && this.Properties.Is(IsoFlagType.HoppableN);
      }
   }

   public void discard() {
      this.hourLastSeen = -32768;
      this.chunk = null;
      this.zone = null;
      this.LightInfluenceB = null;
      this.LightInfluenceG = null;
      this.LightInfluenceR = null;
      this.room = null;
      this.w = null;
      this.nw = null;
      this.sw = null;
      this.s = null;
      this.n = null;
      this.ne = null;
      this.se = null;
      this.e = null;
      this.isoWorldRegion = null;
      this.hasSetIsoWorldRegion = false;
      this.nav[0] = null;
      this.nav[1] = null;
      this.nav[2] = null;
      this.nav[3] = null;
      this.nav[4] = null;
      this.nav[5] = null;
      this.nav[6] = null;
      this.nav[7] = null;

      for(int var1 = 0; var1 < 4; ++var1) {
         if (this.lighting[var1] != null) {
            this.lighting[var1].reset();
         }
      }

      this.SolidFloorCached = false;
      this.SolidFloor = false;
      this.CacheIsFree = false;
      this.CachedIsFree = false;
      this.chunk = null;
      this.roomID = -1;
      this.DeferedCharacters.clear();
      this.DeferredCharacterTick = -1;
      this.StaticMovingObjects.clear();
      this.MovingObjects.clear();
      this.Objects.clear();
      this.WorldObjects.clear();
      this.hasTypes.clear();
      this.table = null;
      this.Properties.Clear();
      this.SpecialObjects.clear();
      this.RainDrop = null;
      this.RainSplash = null;
      this.overlayDone = false;
      this.haveRoof = false;
      this.burntOut = false;
      this.trapPositionX = this.trapPositionY = this.trapPositionZ = -1;
      this.haveElectricity = false;
      this.haveSheetRope = false;
      if (this.erosion != null) {
         this.erosion.reset();
      }

      if (this.OcclusionDataCache != null) {
         this.OcclusionDataCache.Reset();
      }

      this.roofHideBuilding = null;
      this.bHasFlies = false;
      isoGridSquareCache.add(this);
   }

   private static boolean validateUser(String var0, String var1) throws MalformedURLException, IOException {
      URL var2 = new URL("http://www.projectzomboid.com/scripts/auth.php?username=" + var0 + "&password=" + var1);
      URLConnection var3 = var2.openConnection();
      BufferedReader var4 = new BufferedReader(new InputStreamReader(var3.getInputStream()));

      String var5;
      do {
         if ((var5 = var4.readLine()) == null) {
            return false;
         }
      } while(!var5.contains("success"));

      return true;
   }

   public float DistTo(int var1, int var2) {
      return IsoUtils.DistanceManhatten((float)var1 + 0.5F, (float)var2 + 0.5F, (float)this.x, (float)this.y);
   }

   public float DistTo(IsoGridSquare var1) {
      return IsoUtils.DistanceManhatten((float)this.x + 0.5F, (float)this.y + 0.5F, (float)var1.x + 0.5F, (float)var1.y + 0.5F);
   }

   public float DistToProper(IsoGridSquare var1) {
      return IsoUtils.DistanceTo((float)this.x + 0.5F, (float)this.y + 0.5F, (float)var1.x + 0.5F, (float)var1.y + 0.5F);
   }

   public float DistTo(IsoMovingObject var1) {
      return IsoUtils.DistanceManhatten((float)this.x + 0.5F, (float)this.y + 0.5F, var1.getX(), var1.getY());
   }

   public float DistToProper(IsoMovingObject var1) {
      return IsoUtils.DistanceTo((float)this.x + 0.5F, (float)this.y + 0.5F, var1.getX(), var1.getY());
   }

   public boolean isSafeToSpawn() {
      choices.clear();
      this.isSafeToSpawn(this, 0);
      if (choices.size() > 7) {
         choices.clear();
         return true;
      } else {
         choices.clear();
         return false;
      }
   }

   public void isSafeToSpawn(IsoGridSquare var1, int var2) {
      if (var2 <= 5) {
         choices.add(var1);
         if (var1.n != null && !choices.contains(var1.n)) {
            this.isSafeToSpawn(var1.n, var2 + 1);
         }

         if (var1.s != null && !choices.contains(var1.s)) {
            this.isSafeToSpawn(var1.s, var2 + 1);
         }

         if (var1.e != null && !choices.contains(var1.e)) {
            this.isSafeToSpawn(var1.e, var2 + 1);
         }

         if (var1.w != null && !choices.contains(var1.w)) {
            this.isSafeToSpawn(var1.w, var2 + 1);
         }

      }
   }

   public static boolean auth(String var0, char[] var1) {
      if (var0.length() > 64) {
         return false;
      } else {
         String var2 = var1.toString();
         if (var2.length() > 64) {
            return false;
         } else {
            try {
               return validateUser(var0, var2);
            } catch (MalformedURLException var4) {
               Logger.getLogger(IsoGridSquare.class.getName()).log(Level.SEVERE, (String)null, var4);
            } catch (IOException var5) {
               Logger.getLogger(IsoGridSquare.class.getName()).log(Level.SEVERE, (String)null, var5);
            }

            return false;
         }
      }
   }

   private void renderAttachedSpritesWithNoWallLighting(IsoObject var1, ColorInfo var2) {
      if (var1.AttachedAnimSprite != null && !var1.AttachedAnimSprite.isEmpty()) {
         boolean var3 = false;

         for(int var4 = 0; var4 < var1.AttachedAnimSprite.size(); ++var4) {
            IsoSpriteInstance var5 = (IsoSpriteInstance)var1.AttachedAnimSprite.get(var4);
            if (var5.parentSprite != null && var5.parentSprite.Properties.Is(IsoFlagType.NoWallLighting)) {
               var3 = true;
               break;
            }
         }

         if (var3) {
            defColorInfo.r = var2.r;
            defColorInfo.g = var2.g;
            defColorInfo.b = var2.b;
            float var7 = defColorInfo.a;
            if (CircleStencil) {
            }

            for(int var8 = 0; var8 < var1.AttachedAnimSprite.size(); ++var8) {
               IsoSpriteInstance var6 = (IsoSpriteInstance)var1.AttachedAnimSprite.get(var8);
               if (var6.parentSprite != null && var6.parentSprite.Properties.Is(IsoFlagType.NoWallLighting)) {
                  defColorInfo.a = var6.alpha;
                  var6.render(var1, (float)this.x, (float)this.y, (float)this.z, var1.dir, var1.offsetX, var1.offsetY + var1.getRenderYOffset() * (float)Core.TileScale, defColorInfo);
                  var6.update();
               }
            }

            defColorInfo.r = 1.0F;
            defColorInfo.g = 1.0F;
            defColorInfo.b = 1.0F;
            defColorInfo.a = var7;
         }
      }
   }

   public void DoCutawayShader(IsoObject var1, IsoDirections var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, WallShaperWhole var10) {
      Texture var11 = Texture.getSharedTexture("media/wallcutaways.png");
      if (var11 != null && var11.getID() != -1) {
         boolean var12 = var1.sprite.getProperties().Is(IsoFlagType.NoWallLighting);
         int var13 = IsoCamera.frameState.playerIndex;
         ColorInfo var14 = this.lightInfo[var13];

         try {
            float var15 = 0.0F;
            float var16 = var1.getCurrentFrameTex().getOffsetY();
            int var17 = 0;
            int var18 = 226 - var1.getCurrentFrameTex().getHeight();
            if (var2 != IsoDirections.NW) {
               var17 = 66 - var1.getCurrentFrameTex().getWidth();
            }

            if (var1.sprite.getProperties().Is(IsoFlagType.WallSE)) {
               var17 = 6 - var1.getCurrentFrameTex().getWidth();
               var18 = 196 - var1.getCurrentFrameTex().getHeight();
            }

            if (var1.sprite.name.contains("fencing_01_11")) {
               var15 = 1.0F;
            } else if (var1.sprite.name.contains("carpentry_02_80")) {
               var15 = 1.0F;
            } else if (var1.sprite.name.contains("spiffos_01_71")) {
               var15 = -24.0F;
            } else if (var1.sprite.name.contains("walls_exterior_roofs")) {
               String var19 = var1.sprite.name.replaceAll("(.*)_", "");
               int var20 = Integer.parseInt(var19);
               if (var20 == 4) {
                  var15 = -60.0F;
               } else if (var20 == 17) {
                  var15 = -46.0F;
               } else if (var20 == 28 && !var1.sprite.name.contains("03")) {
                  var15 = -60.0F;
               } else if (var20 == 41) {
                  var15 = -46.0F;
               }
            }

            IsoGridSquare.CircleStencilShader var27 = IsoGridSquare.CircleStencilShader.instance;
            short var21;
            short var22;
            int var23;
            short var28;
            if (var2 == IsoDirections.N || var2 == IsoDirections.NW) {
               var28 = 700;
               var21 = 1084;
               if (var4) {
                  var21 = 1212;
                  if (!var5) {
                     var28 = 444;
                  }
               } else if (!var5) {
                  var28 = 828;
               } else {
                  var28 = 956;
               }

               var22 = 0;
               if (var6) {
                  var22 = 904;
                  if (var1.sprite.name.contains("garage") || var1.sprite.name.contains("industry_trucks")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 % 8 == 5) {
                        var22 = 1356;
                     } else if (var23 % 8 == 4) {
                        var22 = 1582;
                     } else if (var23 % 8 == 3) {
                        var22 = 1130;
                     }
                  }

                  if (var1.sprite.name.contains("community_church")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 == 19) {
                        var22 = 1356;
                     } else if (var23 == 18) {
                        var22 = 1130;
                     }
                  }
               } else if (var8) {
                  var22 = 226;
                  if (var1.sprite.name.contains("trailer")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 != 14 && var23 != 38) {
                        if (var23 == 15 || var23 == 39) {
                           var22 = 452;
                        }
                     } else {
                        var22 = 678;
                     }
                  }

                  if (var1.sprite.name.contains("sunstarmotel")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 != 22 && var23 != 18) {
                        if (var23 == 23 || var23 == 19) {
                           var22 = 452;
                        }
                     } else {
                        var22 = 678;
                     }
                  }
               }

               colu = this.getVertLight(0, var13);
               coll = this.getVertLight(1, var13);
               colu2 = this.getVertLight(4, var13);
               coll2 = this.getVertLight(5, var13);
               if (Core.bDebug && DebugOptions.instance.DebugDraw_SkipWorldShading.getValue()) {
                  coll2 = -1;
                  colu2 = -1;
                  coll = -1;
                  colu = -1;
                  var14 = defColorInfo;
               }

               if (var1.sprite.getProperties().Is(IsoFlagType.WallSE)) {
                  SpriteRenderer.instance.setCutawayTexture(var11, var21 + (int)var15, var22 + (int)var16, 6 - var17, 196 - var18);
               } else {
                  SpriteRenderer.instance.setCutawayTexture(var11, var28 + (int)var15, var22 + (int)var16, 66 - var17, 226 - var18);
               }

               if (var2 == IsoDirections.N) {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.All);
               } else {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.RightOnly);
               }

               var10.col[0] = colu2;
               var10.col[1] = coll2;
               var10.col[2] = coll;
               var10.col[3] = colu;
               var1.renderWallTileOnly((float)this.x, (float)this.y, (float)this.z, var12 ? var14 : defColorInfo, var27, var10);
            }

            if (var2 == IsoDirections.W || var2 == IsoDirections.NW) {
               var28 = 512;
               var21 = 1084;
               if (var3) {
                  if (!var4) {
                     var28 = 768;
                     var21 = 1212;
                  }
               } else if (!var4) {
                  var28 = 896;
                  var21 = 1212;
               } else {
                  var28 = 256;
               }

               var22 = 0;
               if (var7) {
                  var22 = 904;
                  if (var1.sprite.name.contains("garage") || var1.sprite.name.contains("industry_trucks")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 % 8 == 0) {
                        var22 = 1356;
                     } else if (var23 % 8 == 1) {
                        var22 = 1582;
                     } else if (var23 % 8 == 2) {
                        var22 = 1130;
                     }
                  }

                  if (var1.sprite.name.contains("community_church")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 == 16) {
                        var22 = 1356;
                     } else if (var23 == 17) {
                        var22 = 1130;
                     }
                  }
               } else if (var9) {
                  var22 = 226;
                  if (var1.sprite.name.contains("trailer")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 != 13 && var23 != 37) {
                        if (var23 == 12 || var23 == 36) {
                           var22 = 452;
                        }
                     } else {
                        var22 = 678;
                     }
                  }

                  if (var1.sprite.name.contains("sunstarmotel")) {
                     var23 = var1.sprite.tileSheetIndex;
                     if (var23 == 17) {
                        var22 = 678;
                     } else if (var23 == 16) {
                        var22 = 452;
                     }
                  }
               }

               colu = this.getVertLight(0, var13);
               coll = this.getVertLight(3, var13);
               colu2 = this.getVertLight(4, var13);
               coll2 = this.getVertLight(7, var13);
               if (Core.bDebug && DebugOptions.instance.DebugDraw_SkipWorldShading.getValue()) {
                  coll2 = -1;
                  colu2 = -1;
                  coll = -1;
                  colu = -1;
                  var14 = defColorInfo;
               }

               if (var1.sprite.getProperties().Is(IsoFlagType.WallSE)) {
                  SpriteRenderer.instance.setCutawayTexture(var11, var21 + (int)var15, var22 + (int)var16, 6 - var17, 196 - var18);
               } else {
                  SpriteRenderer.instance.setCutawayTexture(var11, var28 + (int)var15, var22 + (int)var16, 66 - var17, 226 - var18);
               }

               if (var2 == IsoDirections.W) {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.All);
               } else {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.LeftOnly);
               }

               var10.col[0] = coll2;
               var10.col[1] = colu2;
               var10.col[2] = colu;
               var10.col[3] = coll;
               var1.renderWallTileOnly((float)this.x, (float)this.y, (float)this.z, var12 ? var14 : defColorInfo, var27, var10);
            }
         } finally {
            SpriteRenderer.instance.setExtraWallShaderParams((SpriteRenderer.WallShaderTexRender)null);
            SpriteRenderer.instance.clearCutawayTexture();
            SpriteRenderer.instance.clearUseVertColorsArray();
         }

         var1.renderAttachedAndOverlaySprites((float)this.x, (float)this.y, (float)this.z, var12 ? var14 : defColorInfo, false, !var12, (Shader)null, var10);
      }
   }

   public void DoCutawayShaderSprite(IsoSprite var1, IsoDirections var2, boolean var3, boolean var4, boolean var5) {
      IsoGridSquare.CircleStencilShader var6 = IsoGridSquare.CircleStencilShader.instance;
      WallShaperWhole var7 = WallShaperWhole.instance;
      int var8 = IsoCamera.frameState.playerIndex;
      Texture var9 = Texture.getSharedTexture("media/wallcutaways.png");
      if (var9 != null && var9.getID() != -1) {
         try {
            Texture var10 = ((IsoDirectionFrame)var1.CurrentAnim.Frames.get((int)var1.def.Frame)).getTexture(var2);
            float var11 = 0.0F;
            float var12 = ((IsoDirectionFrame)var1.CurrentAnim.Frames.get((int)var1.def.Frame)).getTexture(var2).getOffsetY();
            int var13 = 0;
            int var14 = 226 - var10.getHeight();
            if (var2 != IsoDirections.NW) {
               var13 = 66 - ((IsoDirectionFrame)var1.CurrentAnim.Frames.get((int)var1.def.Frame)).getTexture(var2).getWidth();
            }

            if (var1.getProperties().Is(IsoFlagType.WallSE)) {
               var13 = 6 - ((IsoDirectionFrame)var1.CurrentAnim.Frames.get((int)var1.def.Frame)).getTexture(var2).getWidth();
               var14 = 196 - ((IsoDirectionFrame)var1.CurrentAnim.Frames.get((int)var1.def.Frame)).getTexture(var2).getHeight();
            }

            if (var1.name.contains("fencing_01_11")) {
               var11 = 1.0F;
            } else if (var1.name.contains("carpentry_02_80")) {
               var11 = 1.0F;
            } else if (var1.name.contains("spiffos_01_71")) {
               var11 = -24.0F;
            } else if (var1.name.contains("walls_exterior_roofs")) {
               String var15 = var1.name.replaceAll("(.*)_", "");
               int var16 = Integer.parseInt(var15);
               if (var16 == 4) {
                  var11 = -60.0F;
               } else if (var16 == 17) {
                  var11 = -46.0F;
               } else if (var16 == 28 && !var1.name.contains("03")) {
                  var11 = -60.0F;
               } else if (var16 == 41) {
                  var11 = -46.0F;
               }
            }

            short var20;
            short var21;
            if (var2 == IsoDirections.N || var2 == IsoDirections.NW) {
               var20 = 700;
               var21 = 1084;
               if (var4) {
                  var21 = 1212;
                  if (!var5) {
                     var20 = 444;
                  }
               } else if (!var5) {
                  var20 = 828;
               } else {
                  var20 = 956;
               }

               colu = this.getVertLight(0, var8);
               coll = this.getVertLight(1, var8);
               colu2 = this.getVertLight(4, var8);
               coll2 = this.getVertLight(5, var8);
               if (var1.getProperties().Is(IsoFlagType.WallSE)) {
                  SpriteRenderer.instance.setCutawayTexture(var9, var21 + (int)var11, 0 + (int)var12, 6 - var13, 196 - var14);
               } else {
                  SpriteRenderer.instance.setCutawayTexture(var9, var20 + (int)var11, 0 + (int)var12, 66 - var13, 226 - var14);
               }

               if (var2 == IsoDirections.N) {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.All);
               } else {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.RightOnly);
               }

               var7.col[0] = colu2;
               var7.col[1] = coll2;
               var7.col[2] = coll;
               var7.col[3] = colu;
               IndieGL.bindShader(var6, var1, var2, var7, (var1x, var2x, var3x) -> {
                  var1x.render((IsoObject)null, (float)this.x, (float)this.y, (float)this.z, var2x, WeatherFxMask.offsetX, WeatherFxMask.offsetY, defColorInfo, false, var3x);
               });
            }

            if (var2 == IsoDirections.W || var2 == IsoDirections.NW) {
               var20 = 512;
               var21 = 1084;
               if (var3) {
                  if (!var4) {
                     var20 = 768;
                     var21 = 1212;
                  }
               } else if (!var4) {
                  var20 = 896;
                  var21 = 1212;
               } else {
                  var20 = 256;
               }

               colu = this.getVertLight(0, var8);
               coll = this.getVertLight(3, var8);
               colu2 = this.getVertLight(4, var8);
               coll2 = this.getVertLight(7, var8);
               if (var1.getProperties().Is(IsoFlagType.WallSE)) {
                  SpriteRenderer.instance.setCutawayTexture(var9, var21 + (int)var11, 0 + (int)var12, 6 - var13, 196 - var14);
               } else {
                  SpriteRenderer.instance.setCutawayTexture(var9, var20 + (int)var11, 0 + (int)var12, 66 - var13, 226 - var14);
               }

               if (var2 == IsoDirections.W) {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.All);
               } else {
                  SpriteRenderer.instance.setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender.LeftOnly);
               }

               var7.col[0] = coll2;
               var7.col[1] = colu2;
               var7.col[2] = colu;
               var7.col[3] = coll;
               IndieGL.bindShader(var6, var1, var2, var7, (var1x, var2x, var3x) -> {
                  var1x.render((IsoObject)null, (float)this.x, (float)this.y, (float)this.z, var2x, WeatherFxMask.offsetX, WeatherFxMask.offsetY, defColorInfo, false, var3x);
               });
            }
         } finally {
            SpriteRenderer.instance.setExtraWallShaderParams((SpriteRenderer.WallShaderTexRender)null);
            SpriteRenderer.instance.clearCutawayTexture();
            SpriteRenderer.instance.clearUseVertColorsArray();
         }

      }
   }

   public int DoWallLightingNW(IsoObject var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, Shader var10) {
      if (!DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.NW.getValue()) {
         return var2;
      } else {
         boolean var11 = var3 || var4 || var5;
         IsoDirections var12 = IsoDirections.NW;
         int var13 = IsoCamera.frameState.playerIndex;
         colu = this.getVertLight(0, var13);
         coll = this.getVertLight(3, var13);
         colr = this.getVertLight(1, var13);
         colu2 = this.getVertLight(4, var13);
         coll2 = this.getVertLight(7, var13);
         colr2 = this.getVertLight(5, var13);
         if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingDebug.getValue()) {
            colu = -65536;
            coll = -16711936;
            colr = -16711681;
            colu2 = -16776961;
            coll2 = -65281;
            colr2 = -256;
         }

         boolean var14 = CircleStencil;
         if (this.z != (int)IsoCamera.CamCharacter.z) {
            var14 = false;
         }

         boolean var15 = var1.sprite.getType() == IsoObjectType.doorFrN || var1.sprite.getType() == IsoObjectType.doorN;
         boolean var16 = var1.sprite.getType() == IsoObjectType.doorFrW || var1.sprite.getType() == IsoObjectType.doorW;
         boolean var17 = false;
         boolean var18 = false;
         boolean var19 = (var15 || var17 || var16 || var17) && var11 || var1.sprite.getProperties().Is(IsoFlagType.NoWallLighting);
         var14 = this.calculateWallAlphaAndCircleStencilCorner(var1, var3, var4, var5, var6, var7, var8, var9, var14, var13, var15, var16, var17, var18);
         if (USE_WALL_SHADER && var14 && var11) {
            this.DoCutawayShader(var1, var12, var3, var4, var5, var6, var7, var8, var9, WallShaperWhole.instance);
            bWallCutawayN = true;
            bWallCutawayW = true;
            return var2;
         } else {
            WallShaperWhole.instance.col[0] = colu2;
            WallShaperWhole.instance.col[1] = colr2;
            WallShaperWhole.instance.col[2] = colr;
            WallShaperWhole.instance.col[3] = colu;
            WallShaperN var20 = WallShaperN.instance;
            var20.col[0] = colu2;
            var20.col[1] = colr2;
            var20.col[2] = colr;
            var20.col[3] = colu;
            var2 = this.performDrawWall(var1, var2, var13, var19, var20, var10);
            WallShaperWhole.instance.col[0] = coll2;
            WallShaperWhole.instance.col[1] = colu2;
            WallShaperWhole.instance.col[2] = colu;
            WallShaperWhole.instance.col[3] = coll;
            WallShaperW var21 = WallShaperW.instance;
            var21.col[0] = coll2;
            var21.col[1] = colu2;
            var21.col[2] = colu;
            var21.col[3] = coll;
            var2 = this.performDrawWall(var1, var2, var13, var19, var21, var10);
            return var2;
         }
      }
   }

   public int DoWallLightingN(IsoObject var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, Shader var7) {
      if (!DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.N.getValue()) {
         return var2;
      } else {
         boolean var8 = !var5;
         boolean var9 = !var6;
         IsoObjectType var10 = IsoObjectType.doorFrN;
         IsoObjectType var11 = IsoObjectType.doorN;
         boolean var12 = var3 || var4;
         IsoFlagType var13 = IsoFlagType.transparentN;
         IsoFlagType var14 = IsoFlagType.WindowN;
         IsoFlagType var15 = IsoFlagType.HoppableN;
         IsoDirections var16 = IsoDirections.N;
         boolean var17 = CircleStencil;
         int var18 = IsoCamera.frameState.playerIndex;
         colu = this.getVertLight(0, var18);
         coll = this.getVertLight(1, var18);
         colu2 = this.getVertLight(4, var18);
         coll2 = this.getVertLight(5, var18);
         if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingDebug.getValue()) {
            colu = -65536;
            coll = -16711936;
            colu2 = -16776961;
            coll2 = -65281;
         }

         WallShaperWhole var19 = WallShaperWhole.instance;
         var19.col[0] = colu2;
         var19.col[1] = coll2;
         var19.col[2] = coll;
         var19.col[3] = colu;
         return this.performDrawWallSegmentSingle(var1, var2, false, var3, false, false, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var19, var7);
      }
   }

   public int DoWallLightingW(IsoObject var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, Shader var7) {
      if (!DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.W.getValue()) {
         return var2;
      } else {
         boolean var8 = !var5;
         boolean var9 = !var6;
         IsoObjectType var10 = IsoObjectType.doorFrW;
         IsoObjectType var11 = IsoObjectType.doorW;
         boolean var12 = var3 || var4;
         IsoFlagType var13 = IsoFlagType.transparentW;
         IsoFlagType var14 = IsoFlagType.WindowW;
         IsoFlagType var15 = IsoFlagType.HoppableW;
         IsoDirections var16 = IsoDirections.W;
         boolean var17 = CircleStencil;
         int var18 = IsoCamera.frameState.playerIndex;
         colu = this.getVertLight(0, var18);
         coll = this.getVertLight(3, var18);
         colu2 = this.getVertLight(4, var18);
         coll2 = this.getVertLight(7, var18);
         if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.LightingDebug.getValue()) {
            colu = -65536;
            coll = -16711936;
            colu2 = -16776961;
            coll2 = -65281;
         }

         WallShaperWhole var19 = WallShaperWhole.instance;
         var19.col[0] = coll2;
         var19.col[1] = colu2;
         var19.col[2] = colu;
         var19.col[3] = coll;
         return this.performDrawWallSegmentSingle(var1, var2, var3, var4, var5, var6, false, false, false, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var19, var7);
      }
   }

   private int performDrawWallSegmentSingle(IsoObject var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, IsoObjectType var12, IsoObjectType var13, boolean var14, IsoFlagType var15, IsoFlagType var16, IsoFlagType var17, IsoDirections var18, boolean var19, WallShaperWhole var20, Shader var21) {
      int var22 = IsoCamera.frameState.playerIndex;
      if (this.z != (int)IsoCamera.CamCharacter.z) {
         var19 = false;
      }

      boolean var23 = var1.sprite.getType() == var12 || var1.sprite.getType() == var13;
      boolean var24 = var1 instanceof IsoWindow;
      boolean var25 = (var23 || var24) && var14 || var1.sprite.getProperties().Is(IsoFlagType.NoWallLighting);
      var19 = this.calculateWallAlphaAndCircleStencilEdge(var1, var10, var11, var14, var15, var16, var17, var19, var22, var23, var24);
      if (USE_WALL_SHADER && var19 && var14) {
         this.DoCutawayShader(var1, var18, var3, var4, var7, var8, var5, var9, var6, var20);
         bWallCutawayN |= var18 == IsoDirections.N;
         bWallCutawayW |= var18 == IsoDirections.W;
         return var2;
      } else {
         return this.performDrawWall(var1, var2, var22, var25, var20, var21);
      }
   }

   private int performDrawWallOnly(IsoObject var1, int var2, int var3, boolean var4, Consumer var5, Shader var6) {
      if (var2 == 0 && !var4) {
         var2 = this.getCell().getStencilValue(this.x, this.y, this.z);
      }

      IndieGL.enableAlphaTest();
      IndieGL.glAlphaFunc(516, 0.0F);
      IndieGL.glStencilFunc(519, var2, 127);
      if (!var4) {
         IndieGL.glStencilOp(7680, 7680, 7681);
      }

      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.Render.getValue()) {
         var1.renderWallTile((float)this.x, (float)this.y, (float)this.z, var4 ? lightInfoTemp : defColorInfo, true, !var4, var6, var5);
      }

      var1.setAlpha(var3, 1.0F);
      if (var4) {
         IndieGL.glStencilFunc(519, 1, 255);
         IndieGL.glStencilOp(7680, 7680, 7680);
         return var2;
      } else {
         this.getCell().setStencilValue(this.x, this.y, this.z, var2);
         return var2 + 1;
      }
   }

   private int performDrawWall(IsoObject var1, int var2, int var3, boolean var4, Consumer var5, Shader var6) {
      lightInfoTemp.set(this.lightInfo[var3]);
      if (Core.bDebug && DebugOptions.instance.DebugDraw_SkipWorldShading.getValue()) {
         var1.render((float)this.x, (float)this.y, (float)this.z, defColorInfo, true, !var4, (Shader)null);
         return var2;
      } else {
         int var7 = this.performDrawWallOnly(var1, var2, var3, var4, var5, var6);
         if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Walls.AttachedSprites.getValue()) {
            this.renderAttachedSpritesWithNoWallLighting(var1, lightInfoTemp);
         }

         return var7;
      }
   }

   private void calculateWallAlphaCommon(IsoObject var1, boolean var2, boolean var3, boolean var4, int var5, boolean var6, boolean var7) {
      if (var6 || var7) {
         if (var2) {
            var1.setAlpha(var5, 0.4F);
            var1.setTargetAlpha(var5, 0.4F);
            lightInfoTemp.r = Math.max(0.3F, lightInfoTemp.r);
            lightInfoTemp.g = Math.max(0.3F, lightInfoTemp.g);
            lightInfoTemp.b = Math.max(0.3F, lightInfoTemp.b);
            if (var6 && !var3) {
               var1.setAlpha(var5, 0.0F);
               var1.setTargetAlpha(var5, 0.0F);
            }

            if (var7 && !var4) {
               var1.setAlpha(var5, 0.0F);
               var1.setTargetAlpha(var5, 0.0F);
            }
         }

      }
   }

   private boolean calculateWallAlphaAndCircleStencilEdge(IsoObject var1, boolean var2, boolean var3, boolean var4, IsoFlagType var5, IsoFlagType var6, IsoFlagType var7, boolean var8, int var9, boolean var10, boolean var11) {
      if (var10 || var11) {
         if (!var1.sprite.getProperties().Is("GarageDoor")) {
            var8 = false;
         }

         this.calculateWallAlphaCommon(var1, var4, !var2, !var3, var9, var10, var11);
      }

      if (var8 && var1.sprite.getType() == IsoObjectType.wall && var1.sprite.getProperties().Is(var5) && !var1.getSprite().getProperties().Is(IsoFlagType.exterior) && !var1.sprite.getProperties().Is(var6)) {
         var8 = false;
      }

      return var8;
   }

   private boolean calculateWallAlphaAndCircleStencilCorner(IsoObject var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, int var10, boolean var11, boolean var12, boolean var13, boolean var14) {
      this.calculateWallAlphaCommon(var1, var3 || var4, var5, var7, var10, var11, var13);
      this.calculateWallAlphaCommon(var1, var3 || var2, var6, var8, var10, var12, var14);
      var9 = var9 && !var11 && !var13;
      if (var9 && var1.sprite.getType() == IsoObjectType.wall && (var1.sprite.getProperties().Is(IsoFlagType.transparentN) || var1.sprite.getProperties().Is(IsoFlagType.transparentW)) && !var1.getSprite().getProperties().Is(IsoFlagType.exterior) && !var1.sprite.getProperties().Is(IsoFlagType.WindowN) && !var1.sprite.getProperties().Is(IsoFlagType.WindowW)) {
         var9 = false;
      }

      return var9;
   }

   public KahluaTable getLuaMovingObjectList() {
      KahluaTable var1 = LuaManager.platform.newTable();
      LuaManager.env.rawset("Objects", var1);

      for(int var2 = 0; var2 < this.MovingObjects.size(); ++var2) {
         var1.rawset(var2 + 1, this.MovingObjects.get(var2));
      }

      return var1;
   }

   public boolean Is(IsoFlagType var1) {
      return this.Properties.Is(var1);
   }

   public boolean Is(String var1) {
      return this.Properties.Is(var1);
   }

   public boolean Has(IsoObjectType var1) {
      return this.hasTypes.isSet(var1);
   }

   public void DeleteTileObject(IsoObject var1) {
      this.Objects.remove(var1);
      this.RecalcAllWithNeighbours(true);
   }

   public KahluaTable getLuaTileObjectList() {
      KahluaTable var1 = LuaManager.platform.newTable();
      LuaManager.env.rawset("Objects", var1);

      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         var1.rawset(var2 + 1, this.Objects.get(var2));
      }

      return var1;
   }

   boolean HasDoor(boolean var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         if (this.SpecialObjects.get(var2) instanceof IsoDoor && ((IsoDoor)this.SpecialObjects.get(var2)).north == var1) {
            return true;
         }

         if (this.SpecialObjects.get(var2) instanceof IsoThumpable && ((IsoThumpable)this.SpecialObjects.get(var2)).isDoor && ((IsoThumpable)this.SpecialObjects.get(var2)).north == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean HasStairs() {
      return this.HasStairsNorth() || this.HasStairsWest();
   }

   public boolean HasStairsNorth() {
      return this.Has(IsoObjectType.stairsTN) || this.Has(IsoObjectType.stairsMN) || this.Has(IsoObjectType.stairsBN);
   }

   public boolean HasStairsWest() {
      return this.Has(IsoObjectType.stairsTW) || this.Has(IsoObjectType.stairsMW) || this.Has(IsoObjectType.stairsBW);
   }

   public boolean HasStairsBelow() {
      if (this.z == 0) {
         return false;
      } else {
         IsoGridSquare var1 = this.getCell().getGridSquare(this.x, this.y, this.z - 1);
         return var1 != null && var1.HasStairs();
      }
   }

   public boolean HasElevatedFloor() {
      return this.Has(IsoObjectType.stairsTN) || this.Has(IsoObjectType.stairsMN) || this.Has(IsoObjectType.stairsTW) || this.Has(IsoObjectType.stairsMW);
   }

   public boolean isSameStaircase(int var1, int var2, int var3) {
      if (var3 != this.getZ()) {
         return false;
      } else {
         int var4 = this.getX();
         int var5 = this.getY();
         int var6 = var4;
         int var7 = var5;
         if (this.Has(IsoObjectType.stairsTN)) {
            var7 = var5 + 2;
         } else if (this.Has(IsoObjectType.stairsMN)) {
            --var5;
            ++var7;
         } else if (this.Has(IsoObjectType.stairsBN)) {
            var5 -= 2;
         } else if (this.Has(IsoObjectType.stairsTW)) {
            var6 = var4 + 2;
         } else if (this.Has(IsoObjectType.stairsMW)) {
            --var4;
            ++var6;
         } else {
            if (!this.Has(IsoObjectType.stairsBW)) {
               return false;
            }

            var4 -= 2;
         }

         if (var1 >= var4 && var2 >= var5 && var1 <= var6 && var2 <= var7) {
            IsoGridSquare var8 = this.getCell().getGridSquare(var1, var2, var3);
            return var8 != null && var8.HasStairs();
         } else {
            return false;
         }
      }
   }

   public boolean HasSlopedRoof() {
      return this.HasSlopedRoofWest() || this.HasSlopedRoofNorth();
   }

   public boolean HasSlopedRoofWest() {
      return this.Has(IsoObjectType.WestRoofB) || this.Has(IsoObjectType.WestRoofM) || this.Has(IsoObjectType.WestRoofT);
   }

   public boolean HasSlopedRoofNorth() {
      return this.Has(IsoObjectType.WestRoofB) || this.Has(IsoObjectType.WestRoofM) || this.Has(IsoObjectType.WestRoofT);
   }

   public boolean HasTree() {
      return this.hasTree;
   }

   public IsoTree getTree() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoTree var2 = (IsoTree)Type.tryCastTo((IsoObject)this.Objects.get(var1), IsoTree.class);
         if (var2 != null) {
            return var2;
         }
      }

      return null;
   }

   private void fudgeShadowsToAlpha(IsoObject var1, Color var2) {
      float var3 = 1.0F - var1.getAlpha();
      if (var2.r < var3) {
         var2.r = var3;
      }

      if (var2.g < var3) {
         var2.g = var3;
      }

      if (var2.b < var3) {
         var2.b = var3;
      }

   }

   public boolean shouldSave() {
      return !this.Objects.isEmpty();
   }

   public void save(ByteBuffer var1, ObjectOutputStream var2) throws IOException {
      this.save(var1, var2, false);
   }

   public void save(ByteBuffer var1, ObjectOutputStream var2, boolean var3) throws IOException {
      this.getErosionData().save(var1);
      BitHeaderWrite var4 = BitHeader.allocWrite(BitHeader.HeaderSize.Byte, var1);
      int var5 = this.Objects.size();
      int var7;
      if (this.Objects.size() > 0) {
         var4.addFlags(1);
         if (var5 == 2) {
            var4.addFlags(2);
         } else if (var5 == 3) {
            var4.addFlags(4);
         } else if (var5 >= 4) {
            var4.addFlags(8);
         }

         if (var3) {
            GameWindow.WriteString(var1, "Number of objects (" + var5 + ")");
         }

         if (var5 >= 4) {
            var1.putShort((short)this.Objects.size());
         }

         for(int var6 = 0; var6 < this.Objects.size(); ++var6) {
            var7 = var1.position();
            if (var3) {
               var1.putInt(0);
            }

            byte var8 = 0;
            if (this.SpecialObjects.contains(this.Objects.get(var6))) {
               var8 = (byte)(var8 | 2);
            }

            if (this.WorldObjects.contains(this.Objects.get(var6))) {
               var8 = (byte)(var8 | 4);
            }

            var1.put(var8);
            if (var3) {
               GameWindow.WriteStringUTF(var1, ((IsoObject)this.Objects.get(var6)).getClass().getName());
            }

            ((IsoObject)this.Objects.get(var6)).save(var1, var3);
            if (var3) {
               int var9 = var1.position();
               var1.position(var7);
               var1.putInt(var9 - var7);
               var1.position(var9);
            }
         }

         if (var3) {
            var1.put((byte)67);
            var1.put((byte)82);
            var1.put((byte)80);
            var1.put((byte)83);
         }
      }

      if (this.isOverlayDone()) {
         var4.addFlags(16);
      }

      if (this.haveRoof) {
         var4.addFlags(32);
      }

      BitHeaderWrite var10 = BitHeader.allocWrite(BitHeader.HeaderSize.Byte, var1);
      var7 = 0;

      int var12;
      for(var12 = 0; var12 < this.StaticMovingObjects.size(); ++var12) {
         if (this.StaticMovingObjects.get(var12) instanceof IsoDeadBody) {
            ++var7;
         }
      }

      if (var7 > 0) {
         var10.addFlags(1);
         if (var3) {
            GameWindow.WriteString(var1, "Number of bodies");
         }

         var1.putShort((short)var7);

         for(var12 = 0; var12 < this.StaticMovingObjects.size(); ++var12) {
            IsoMovingObject var11 = (IsoMovingObject)this.StaticMovingObjects.get(var12);
            if (var11 instanceof IsoDeadBody) {
               if (var3) {
                  GameWindow.WriteStringUTF(var1, var11.getClass().getName());
               }

               var11.save(var1, var3);
            }
         }
      }

      if (this.table != null && !this.table.isEmpty()) {
         var10.addFlags(2);
         this.table.save(var1);
      }

      if (this.burntOut) {
         var10.addFlags(4);
      }

      if (this.getTrapPositionX() > 0) {
         var10.addFlags(8);
         var1.putInt(this.getTrapPositionX());
         var1.putInt(this.getTrapPositionY());
         var1.putInt(this.getTrapPositionZ());
      }

      if (this.haveSheetRope) {
         var10.addFlags(16);
      }

      if (!var10.equals(0)) {
         var4.addFlags(64);
         var10.write();
      } else {
         var1.position(var10.getStartPosition());
      }

      var4.write();
      var4.release();
      var10.release();
   }

   static void loadmatrix(boolean[][][] var0, DataInputStream var1) throws IOException {
   }

   static void savematrix(boolean[][][] var0, DataOutputStream var1) throws IOException {
      for(int var2 = 0; var2 < 3; ++var2) {
         for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
               var1.writeBoolean(var0[var2][var3][var4]);
            }
         }
      }

   }

   public boolean isCommonGrass() {
      if (this.Objects.isEmpty()) {
         return false;
      } else {
         IsoObject var1 = (IsoObject)this.Objects.get(0);
         return var1.sprite.getProperties().Is(IsoFlagType.solidfloor) && ("TileFloorExt_3".equals(var1.tile) || "TileFloorExt_4".equals(var1.tile));
      }
   }

   public static boolean toBoolean(byte[] var0) {
      return var0 != null && var0.length != 0 ? var0[0] != 0 : false;
   }

   public void removeCorpse(IsoDeadBody var1, boolean var2) {
      if (GameClient.bClient && !var2) {
         try {
            GameClient.instance.checkAddedRemovedItems(var1);
         } catch (Exception var4) {
            GameClient.connection.cancelPacket();
            ExceptionLogger.logException(var4);
         }

         GameClient.sendRemoveCorpseFromMap(var1);
      }

      var1.removeFromWorld();
      var1.removeFromSquare();
      if (!GameServer.bServer) {
         LuaEventManager.triggerEvent("OnContainerUpdate", this);
      }

   }

   public IsoDeadBody getDeadBody() {
      for(int var1 = 0; var1 < this.StaticMovingObjects.size(); ++var1) {
         if (this.StaticMovingObjects.get(var1) instanceof IsoDeadBody) {
            return (IsoDeadBody)this.StaticMovingObjects.get(var1);
         }
      }

      return null;
   }

   public List getDeadBodys() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.StaticMovingObjects.size(); ++var2) {
         if (this.StaticMovingObjects.get(var2) instanceof IsoDeadBody) {
            var1.add((IsoDeadBody)this.StaticMovingObjects.get(var2));
         }
      }

      return var1;
   }

   public void addCorpse(IsoDeadBody var1, boolean var2) {
      if (GameClient.bClient && !var2) {
         ByteBufferWriter var3 = GameClient.connection.startPacket();
         PacketTypes.PacketType.AddCorpseToMap.doPacket(var3);
         var3.putInt(this.x);
         var3.putInt(this.y);
         var3.putInt(this.z);
         var1.writeToRemoteBuffer(var3);
         PacketTypes.PacketType.AddCorpseToMap.send(GameClient.connection);
      }

      if (!this.StaticMovingObjects.contains(var1)) {
         this.StaticMovingObjects.add(var1);
      }

      var1.addToWorld();
      this.burntOut = false;
      this.Properties.UnSet(IsoFlagType.burntOut);
   }

   public IsoBrokenGlass getBrokenGlass() {
      for(int var1 = 0; var1 < this.SpecialObjects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.SpecialObjects.get(var1);
         if (var2 instanceof IsoBrokenGlass) {
            return (IsoBrokenGlass)var2;
         }
      }

      return null;
   }

   public IsoBrokenGlass addBrokenGlass() {
      if (!this.isFree(false)) {
         return this.getBrokenGlass();
      } else {
         IsoBrokenGlass var1 = this.getBrokenGlass();
         if (var1 == null) {
            var1 = new IsoBrokenGlass(this.getCell());
            var1.setSquare(this);
            this.AddSpecialObject(var1);
            if (GameServer.bServer) {
               GameServer.transmitBrokenGlass(this);
            }
         }

         return var1;
      }
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.load(var1, var2, false);
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      this.getErosionData().load(var1, var2);
      BitHeaderRead var4 = BitHeader.allocRead(BitHeader.HeaderSize.Byte, var1);
      if (!var4.equals(0)) {
         int var7;
         if (var4.hasFlags(1)) {
            if (var3) {
               String var5 = GameWindow.ReadStringUTF(var1);
               DebugLog.log(var5);
            }

            short var19 = 1;
            if (var4.hasFlags(2)) {
               var19 = 2;
            } else if (var4.hasFlags(4)) {
               var19 = 3;
            } else if (var4.hasFlags(8)) {
               var19 = var1.getShort();
            }

            int var6 = 0;

            while(true) {
               byte var9;
               if (var6 >= var19) {
                  if (!var3) {
                     break;
                  }

                  byte var20 = var1.get();
                  byte var24 = var1.get();
                  byte var25 = var1.get();
                  var9 = var1.get();
                  if (var20 != 67 || var24 != 82 || var25 != 80 || var9 != 83) {
                     DebugLog.log("***** Expected CRPS here");
                  }
                  break;
               }

               var7 = var1.position();
               int var8 = 0;
               if (var3) {
                  var8 = var1.getInt();
               }

               var9 = var1.get();
               boolean var10 = (var9 & 2) != 0;
               boolean var11 = (var9 & 4) != 0;
               IsoObject var12 = null;
               String var13;
               if (var3) {
                  var13 = GameWindow.ReadStringUTF(var1);
                  DebugLog.log(var13);
               }

               var12 = IsoObject.factoryFromFileInput(this.getCell(), var1);
               int var28;
               if (var12 == null) {
                  if (var3) {
                     var28 = var1.position();
                     if (var28 - var7 != var8) {
                        DebugLog.log("***** Object loaded size " + (var28 - var7) + " != saved size " + var8 + ", reading obj size: " + var19 + ", Object == null");
                        if (var12.getSprite() != null && var12.getSprite().getName() != null) {
                           DebugLog.log("Obj sprite = " + var12.getSprite().getName());
                        }
                     }
                  }
               } else {
                  label252: {
                     var12.square = this;

                     try {
                        var12.load(var1, var2, var3);
                     } catch (Exception var16) {
                        this.debugPrintGridSquare();
                        if (lastLoaded != null) {
                           lastLoaded.debugPrintGridSquare();
                        }

                        throw new RuntimeException(var16);
                     }

                     if (var3) {
                        var28 = var1.position();
                        if (var28 - var7 != var8) {
                           DebugLog.log("***** Object loaded size " + (var28 - var7) + " != saved size " + var8 + ", reading obj size: " + var19);
                           if (var12.getSprite() != null && var12.getSprite().getName() != null) {
                              DebugLog.log("Obj sprite = " + var12.getSprite().getName());
                           }
                        }
                     }

                     if (var12 instanceof IsoWorldInventoryObject) {
                        if (((IsoWorldInventoryObject)var12).getItem() == null) {
                           break label252;
                        }

                        var13 = ((IsoWorldInventoryObject)var12).getItem().getFullType();
                        Item var14 = ScriptManager.instance.FindItem(var13);
                        if (var14 != null && var14.getObsolete()) {
                           break label252;
                        }

                        String[] var15 = var13.split("_");
                        if (((IsoWorldInventoryObject)var12).dropTime > -1.0D && SandboxOptions.instance.HoursForWorldItemRemoval.getValue() > 0.0D && (SandboxOptions.instance.WorldItemRemovalList.getValue().contains(var15[0]) && !SandboxOptions.instance.ItemRemovalListBlacklistToggle.getValue() || !SandboxOptions.instance.WorldItemRemovalList.getValue().contains(var15[0]) && SandboxOptions.instance.ItemRemovalListBlacklistToggle.getValue()) && !((IsoWorldInventoryObject)var12).isIgnoreRemoveSandbox() && GameTime.instance.getWorldAgeHours() > ((IsoWorldInventoryObject)var12).dropTime + SandboxOptions.instance.HoursForWorldItemRemoval.getValue()) {
                           break label252;
                        }
                     }

                     if (!(var12 instanceof IsoWindow) || var12.getSprite() == null || !"walls_special_01_8".equals(var12.getSprite().getName()) && !"walls_special_01_9".equals(var12.getSprite().getName())) {
                        this.Objects.add(var12);
                        if (var10) {
                           this.SpecialObjects.add(var12);
                        }

                        if (var11) {
                           if (Core.bDebug && !(var12 instanceof IsoWorldInventoryObject)) {
                              DebugLog.log("Bitflags = " + var9 + ", obj name = " + var12.getObjectName() + ", sprite = " + (var12.getSprite() != null ? var12.getSprite().getName() : "unknown"));
                           }

                           this.WorldObjects.add((IsoWorldInventoryObject)var12);
                           var12.square.chunk.recalcHashCodeObjects();
                        }
                     }
                  }
               }

               ++var6;
            }
         }

         this.setOverlayDone(var4.hasFlags(16));
         this.haveRoof = var4.hasFlags(32);
         if (var4.hasFlags(64)) {
            BitHeaderRead var21 = BitHeader.allocRead(BitHeader.HeaderSize.Byte, var1);
            if (var21.hasFlags(1)) {
               if (var3) {
                  String var22 = GameWindow.ReadStringUTF(var1);
                  DebugLog.log(var22);
               }

               short var23 = var1.getShort();

               for(var7 = 0; var7 < var23; ++var7) {
                  IsoMovingObject var26 = null;
                  if (var3) {
                     String var27 = GameWindow.ReadStringUTF(var1);
                     DebugLog.log(var27);
                  }

                  try {
                     var26 = (IsoMovingObject)IsoObject.factoryFromFileInput(this.getCell(), var1);
                  } catch (Exception var18) {
                     this.debugPrintGridSquare();
                     if (lastLoaded != null) {
                        lastLoaded.debugPrintGridSquare();
                     }

                     throw new RuntimeException(var18);
                  }

                  if (var26 != null) {
                     var26.square = this;
                     var26.current = this;

                     try {
                        var26.load(var1, var2, var3);
                     } catch (Exception var17) {
                        this.debugPrintGridSquare();
                        if (lastLoaded != null) {
                           lastLoaded.debugPrintGridSquare();
                        }

                        throw new RuntimeException(var17);
                     }

                     this.StaticMovingObjects.add(var26);
                     this.recalcHashCodeObjects();
                  }
               }
            }

            if (var21.hasFlags(2)) {
               if (this.table == null) {
                  this.table = LuaManager.platform.newTable();
               }

               this.table.load(var1, var2);
            }

            this.burntOut = var21.hasFlags(4);
            if (var21.hasFlags(8)) {
               this.setTrapPositionX(var1.getInt());
               this.setTrapPositionY(var1.getInt());
               this.setTrapPositionZ(var1.getInt());
            }

            this.haveSheetRope = var21.hasFlags(16);
            var21.release();
         }
      }

      var4.release();
      lastLoaded = this;
   }

   private void debugPrintGridSquare() {
      System.out.println("x=" + this.x + " y=" + this.y + " z=" + this.z);
      System.out.println("objects");

      int var1;
      for(var1 = 0; var1 < this.Objects.size(); ++var1) {
         ((IsoObject)this.Objects.get(var1)).debugPrintout();
      }

      System.out.println("staticmovingobjects");

      for(var1 = 0; var1 < this.StaticMovingObjects.size(); ++var1) {
         ((IsoObject)this.Objects.get(var1)).debugPrintout();
      }

   }

   public float scoreAsWaypoint(int var1, int var2) {
      float var3 = 2.0F;
      var3 -= IsoUtils.DistanceManhatten((float)var1, (float)var2, (float)this.getX(), (float)this.getY()) * 5.0F;
      return var3;
   }

   public void InvalidateSpecialObjectPaths() {
   }

   public boolean isSolid() {
      return this.Properties.Is(IsoFlagType.solid);
   }

   public boolean isSolidTrans() {
      return this.Properties.Is(IsoFlagType.solidtrans);
   }

   public boolean isFree(boolean var1) {
      if (var1 && this.MovingObjects.size() > 0) {
         return false;
      } else if (this.CachedIsFree) {
         return this.CacheIsFree;
      } else {
         this.CachedIsFree = true;
         this.CacheIsFree = true;
         if (this.Properties.Is(IsoFlagType.solid) || this.Properties.Is(IsoFlagType.solidtrans) || this.Has(IsoObjectType.tree)) {
            this.CacheIsFree = false;
         }

         if (!this.Properties.Is(IsoFlagType.solidfloor)) {
            this.CacheIsFree = false;
         }

         if (!this.Has(IsoObjectType.stairsBN) && !this.Has(IsoObjectType.stairsMN) && !this.Has(IsoObjectType.stairsTN)) {
            if (this.Has(IsoObjectType.stairsBW) || this.Has(IsoObjectType.stairsMW) || this.Has(IsoObjectType.stairsTW)) {
               this.CacheIsFree = true;
            }
         } else {
            this.CacheIsFree = true;
         }

         return this.CacheIsFree;
      }
   }

   public boolean isFreeOrMidair(boolean var1) {
      if (var1 && this.MovingObjects.size() > 0) {
         return false;
      } else {
         boolean var2 = true;
         if (this.Properties.Is(IsoFlagType.solid) || this.Properties.Is(IsoFlagType.solidtrans) || this.Has(IsoObjectType.tree)) {
            var2 = false;
         }

         if (!this.Has(IsoObjectType.stairsBN) && !this.Has(IsoObjectType.stairsMN) && !this.Has(IsoObjectType.stairsTN)) {
            if (this.Has(IsoObjectType.stairsBW) || this.Has(IsoObjectType.stairsMW) || this.Has(IsoObjectType.stairsTW)) {
               var2 = true;
            }
         } else {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isFreeOrMidair(boolean var1, boolean var2) {
      if (var1 && this.MovingObjects.size() > 0) {
         if (!var2) {
            return false;
         }

         for(int var3 = 0; var3 < this.MovingObjects.size(); ++var3) {
            IsoMovingObject var4 = (IsoMovingObject)this.MovingObjects.get(var3);
            if (!(var4 instanceof IsoDeadBody)) {
               return false;
            }
         }
      }

      boolean var5 = true;
      if (this.Properties.Is(IsoFlagType.solid) || this.Properties.Is(IsoFlagType.solidtrans) || this.Has(IsoObjectType.tree)) {
         var5 = false;
      }

      if (!this.Has(IsoObjectType.stairsBN) && !this.Has(IsoObjectType.stairsMN) && !this.Has(IsoObjectType.stairsTN)) {
         if (this.Has(IsoObjectType.stairsBW) || this.Has(IsoObjectType.stairsMW) || this.Has(IsoObjectType.stairsTW)) {
            var5 = true;
         }
      } else {
         var5 = true;
      }

      return var5;
   }

   public boolean connectedWithFloor() {
      if (this.getZ() == 0) {
         return true;
      } else {
         IsoGridSquare var1 = null;
         var1 = this.getCell().getGridSquare(this.getX() - 1, this.getY(), this.getZ());
         if (var1 != null && var1.Properties.Is(IsoFlagType.solidfloor)) {
            return true;
         } else {
            var1 = this.getCell().getGridSquare(this.getX() + 1, this.getY(), this.getZ());
            if (var1 != null && var1.Properties.Is(IsoFlagType.solidfloor)) {
               return true;
            } else {
               var1 = this.getCell().getGridSquare(this.getX(), this.getY() - 1, this.getZ());
               if (var1 != null && var1.Properties.Is(IsoFlagType.solidfloor)) {
                  return true;
               } else {
                  var1 = this.getCell().getGridSquare(this.getX(), this.getY() + 1, this.getZ());
                  return var1 != null && var1.Properties.Is(IsoFlagType.solidfloor);
               }
            }
         }
      }
   }

   public boolean hasFloor(boolean var1) {
      if (this.Properties.Is(IsoFlagType.solidfloor)) {
         return true;
      } else {
         IsoGridSquare var2 = null;
         if (var1) {
            var2 = this.getCell().getGridSquare(this.getX(), this.getY() - 1, this.getZ());
         } else {
            var2 = this.getCell().getGridSquare(this.getX() - 1, this.getY(), this.getZ());
         }

         return var2 != null && var2.Properties.Is(IsoFlagType.solidfloor);
      }
   }

   public boolean isNotBlocked(boolean var1) {
      if (!this.CachedIsFree) {
         this.CacheIsFree = true;
         this.CachedIsFree = true;
         if (this.Properties.Is(IsoFlagType.solid) || this.Properties.Is(IsoFlagType.solidtrans)) {
            this.CacheIsFree = false;
         }

         if (!this.Properties.Is(IsoFlagType.solidfloor)) {
            this.CacheIsFree = false;
         }
      } else if (!this.CacheIsFree) {
         return false;
      }

      return !var1 || this.MovingObjects.size() <= 0;
   }

   public IsoObject getDoor(boolean var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoThumpable) {
            IsoThumpable var4 = (IsoThumpable)var3;
            if (var4.isDoor() && var1 == var4.north) {
               return var4;
            }
         }

         if (var3 instanceof IsoDoor) {
            IsoDoor var5 = (IsoDoor)var3;
            if (var1 == var5.north) {
               return var5;
            }
         }
      }

      return null;
   }

   public IsoDoor getIsoDoor() {
      for(int var1 = 0; var1 < this.SpecialObjects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.SpecialObjects.get(var1);
         if (var2 instanceof IsoDoor) {
            return (IsoDoor)var2;
         }
      }

      return null;
   }

   public IsoObject getDoorTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoObject var2 = null;
         if (var1.x < this.x) {
            var2 = this.getDoor(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getDoor(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getDoor(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getDoor(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getDoorTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getDoorTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getDoorTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getDoorTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IsoWindow getWindow(boolean var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoWindow) {
            IsoWindow var4 = (IsoWindow)var3;
            if (var1 == var4.north) {
               return var4;
            }
         }
      }

      return null;
   }

   public IsoWindow getWindow() {
      for(int var1 = 0; var1 < this.SpecialObjects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.SpecialObjects.get(var1);
         if (var2 instanceof IsoWindow) {
            return (IsoWindow)var2;
         }
      }

      return null;
   }

   public IsoWindow getWindowTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoWindow var2 = null;
         if (var1.x < this.x) {
            var2 = this.getWindow(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getWindow(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getWindow(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getWindow(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getWindowTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getWindowTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public boolean isAdjacentToWindow() {
      if (this.getWindow() != null) {
         return true;
      } else if (this.hasWindowFrame()) {
         return true;
      } else if (this.getThumpableWindow(false) == null && this.getThumpableWindow(true) == null) {
         IsoGridSquare var1 = this.nav[IsoDirections.S.index()];
         if (var1 != null && (var1.getWindow(true) != null || var1.getWindowFrame(true) != null || var1.getThumpableWindow(true) != null)) {
            return true;
         } else {
            IsoGridSquare var2 = this.nav[IsoDirections.E.index()];
            return var2 != null && (var2.getWindow(false) != null || var2.getWindowFrame(false) != null || var2.getThumpableWindow(false) != null);
         }
      } else {
         return true;
      }
   }

   public IsoThumpable getThumpableWindow(boolean var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoThumpable) {
            IsoThumpable var4 = (IsoThumpable)var3;
            if (var4.isWindow() && var1 == var4.north) {
               return var4;
            }
         }
      }

      return null;
   }

   public IsoThumpable getWindowThumpableTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoThumpable var2 = null;
         if (var1.x < this.x) {
            var2 = this.getThumpableWindow(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getThumpableWindow(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getThumpableWindow(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getThumpableWindow(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getWindowThumpableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getWindowThumpableTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowThumpableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowThumpableTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IsoThumpable getHoppableThumpable(boolean var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoThumpable) {
            IsoThumpable var4 = (IsoThumpable)var3;
            if (var4.isHoppable() && var1 == var4.north) {
               return var4;
            }
         }
      }

      return null;
   }

   public IsoThumpable getHoppableThumpableTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoThumpable var2 = null;
         if (var1.x < this.x) {
            var2 = this.getHoppableThumpable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getHoppableThumpable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getHoppableThumpable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getHoppableThumpable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getHoppableThumpableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getHoppableThumpableTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getHoppableThumpableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getHoppableThumpableTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IsoObject getWallHoppable(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         if (((IsoObject)this.Objects.get(var2)).isHoppable() && var1 == ((IsoObject)this.Objects.get(var2)).isNorthHoppable()) {
            return (IsoObject)this.Objects.get(var2);
         }
      }

      return null;
   }

   public IsoObject getWallHoppableTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoObject var2 = null;
         if (var1.x < this.x) {
            var2 = this.getWallHoppable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getWallHoppable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getWallHoppable(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getWallHoppable(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getWallHoppableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getWallHoppableTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWallHoppableTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWallHoppableTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IsoObject getBedTo(IsoGridSquare var1) {
      ArrayList var2 = null;
      if (var1.y >= this.y && var1.x >= this.x) {
         var2 = var1.SpecialObjects;
      } else {
         var2 = this.SpecialObjects;
      }

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoObject var4 = (IsoObject)var2.get(var3);
         if (var4.getProperties().Is(IsoFlagType.bed)) {
            return var4;
         }
      }

      return null;
   }

   public IsoObject getWindowFrame(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (!(var3 instanceof IsoWorldInventoryObject) && IsoWindowFrame.isWindowFrame(var3, var1)) {
            return var3;
         }
      }

      return null;
   }

   public IsoObject getWindowFrameTo(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoObject var2 = null;
         if (var1.x < this.x) {
            var2 = this.getWindowFrame(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y < this.y) {
            var2 = this.getWindowFrame(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x > this.x) {
            var2 = var1.getWindowFrame(false);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.y > this.y) {
            var2 = var1.getWindowFrame(true);
            if (var2 != null) {
               return var2;
            }
         }

         if (var1.x != this.x && var1.y != this.y) {
            IsoGridSquare var3 = this.getCell().getGridSquare(this.x, var1.y, this.z);
            IsoGridSquare var4 = this.getCell().getGridSquare(var1.x, this.y, this.z);
            var2 = this.getWindowFrameTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = this.getWindowFrameTo(var4);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowFrameTo(var3);
            if (var2 != null) {
               return var2;
            }

            var2 = var1.getWindowFrameTo(var4);
            if (var2 != null) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public boolean hasWindowFrame() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (!(var2 instanceof IsoWorldInventoryObject) && IsoWindowFrame.isWindowFrame(var2)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasWindowOrWindowFrame() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (!(var2 instanceof IsoWorldInventoryObject) && (this.isWindowOrWindowFrame(var2, true) || this.isWindowOrWindowFrame(var2, false))) {
            return true;
         }
      }

      return false;
   }

   private IsoObject getSpecialWall(boolean var1) {
      for(int var2 = this.SpecialObjects.size() - 1; var2 >= 0; --var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoThumpable) {
            IsoThumpable var4 = (IsoThumpable)var3;
            if (var4.isStairs() || !var4.isThumpable() && !var4.isWindow() && !var4.isDoor() || var4.isDoor() && var4.open || var4.isBlockAllTheSquare()) {
               continue;
            }

            if (var1 == var4.north && !var4.isCorner()) {
               return var4;
            }
         }

         if (var3 instanceof IsoWindow) {
            IsoWindow var6 = (IsoWindow)var3;
            if (var1 == var6.north) {
               return var6;
            }
         }

         if (var3 instanceof IsoDoor) {
            IsoDoor var7 = (IsoDoor)var3;
            if (var1 == var7.north && !var7.open) {
               return var7;
            }
         }
      }

      if (var1 && !this.Is(IsoFlagType.WindowN) || !var1 && !this.Is(IsoFlagType.WindowW)) {
         return null;
      } else {
         IsoObject var5 = this.getWindowFrame(var1);
         if (var5 != null) {
            return var5;
         } else {
            return null;
         }
      }
   }

   public IsoObject getSheetRope() {
      for(int var1 = 0; var1 < this.getObjects().size(); ++var1) {
         IsoObject var2 = (IsoObject)this.getObjects().get(var1);
         if (var2.sheetRope) {
            return var2;
         }
      }

      return null;
   }

   public boolean damageSpriteSheetRopeFromBottom(IsoPlayer var1, boolean var2) {
      IsoGridSquare var4 = this;
      IsoFlagType var3;
      if (var2) {
         if (this.Is(IsoFlagType.climbSheetN)) {
            var3 = IsoFlagType.climbSheetN;
         } else {
            if (!this.Is(IsoFlagType.climbSheetS)) {
               return false;
            }

            var3 = IsoFlagType.climbSheetS;
         }
      } else if (this.Is(IsoFlagType.climbSheetW)) {
         var3 = IsoFlagType.climbSheetW;
      } else {
         if (!this.Is(IsoFlagType.climbSheetE)) {
            return false;
         }

         var3 = IsoFlagType.climbSheetE;
      }

      while(var4 != null) {
         for(int var5 = 0; var5 < var4.getObjects().size(); ++var5) {
            IsoObject var6 = (IsoObject)var4.getObjects().get(var5);
            if (var6.getProperties() != null && var6.getProperties().Is(var3)) {
               int var7 = Integer.parseInt(var6.getSprite().getName().split("_")[2]);
               if (var7 > 14) {
                  return false;
               }

               String var10000 = var6.getSprite().getName().split("_")[0];
               String var8 = var10000 + "_" + var6.getSprite().getName().split("_")[1];
               var7 += 40;
               var6.setSprite(IsoSpriteManager.instance.getSprite(var8 + "_" + var7));
               var6.transmitUpdatedSprite();
               break;
            }
         }

         if (var4.getZ() == 7) {
            break;
         }

         var4 = var4.getCell().getGridSquare(var4.getX(), var4.getY(), var4.getZ() + 1);
      }

      return true;
   }

   public boolean removeSheetRopeFromBottom(IsoPlayer var1, boolean var2) {
      IsoGridSquare var6 = this;
      IsoFlagType var3;
      IsoFlagType var4;
      String var5;
      int var7;
      IsoObject var8;
      if (var2) {
         if (this.Is(IsoFlagType.climbSheetN)) {
            var3 = IsoFlagType.climbSheetTopN;
            var4 = IsoFlagType.climbSheetN;
         } else {
            if (!this.Is(IsoFlagType.climbSheetS)) {
               return false;
            }

            var3 = IsoFlagType.climbSheetTopS;
            var4 = IsoFlagType.climbSheetS;
            var5 = "crafted_01_4";

            for(var7 = 0; var7 < var6.getObjects().size(); ++var7) {
               var8 = (IsoObject)var6.getObjects().get(var7);
               if (var8.sprite != null && var8.sprite.getName() != null && var8.sprite.getName().equals(var5)) {
                  var6.transmitRemoveItemFromSquare(var8);
                  break;
               }
            }
         }
      } else if (this.Is(IsoFlagType.climbSheetW)) {
         var3 = IsoFlagType.climbSheetTopW;
         var4 = IsoFlagType.climbSheetW;
      } else {
         if (!this.Is(IsoFlagType.climbSheetE)) {
            return false;
         }

         var3 = IsoFlagType.climbSheetTopE;
         var4 = IsoFlagType.climbSheetE;
         var5 = "crafted_01_3";

         for(var7 = 0; var7 < var6.getObjects().size(); ++var7) {
            var8 = (IsoObject)var6.getObjects().get(var7);
            if (var8.sprite != null && var8.sprite.getName() != null && var8.sprite.getName().equals(var5)) {
               var6.transmitRemoveItemFromSquare(var8);
               break;
            }
         }
      }

      boolean var12 = false;

      IsoGridSquare var13;
      for(var13 = null; var6 != null; var12 = false) {
         for(int var9 = 0; var9 < var6.getObjects().size(); ++var9) {
            IsoObject var10 = (IsoObject)var6.getObjects().get(var9);
            if (var10.getProperties() != null && (var10.getProperties().Is(var3) || var10.getProperties().Is(var4))) {
               var13 = var6;
               var12 = true;
               var6.transmitRemoveItemFromSquare(var10);
               if (GameServer.bServer) {
                  if (var1 != null) {
                     var1.sendObjectChange("addItemOfType", new Object[]{"type", var10.getName()});
                  }
               } else if (var1 != null) {
                  var1.getInventory().AddItem(var10.getName());
               }
               break;
            }
         }

         if (var6.getZ() == 7) {
            break;
         }

         var6 = var6.getCell().getGridSquare(var6.getX(), var6.getY(), var6.getZ() + 1);
      }

      if (!var12) {
         var6 = var13.getCell().getGridSquare(var13.getX(), var13.getY(), var13.getZ());
         IsoGridSquare var14 = var2 ? var6.nav[IsoDirections.S.index()] : var6.nav[IsoDirections.E.index()];
         if (var14 == null) {
            return true;
         }

         for(int var15 = 0; var15 < var14.getObjects().size(); ++var15) {
            IsoObject var11 = (IsoObject)var14.getObjects().get(var15);
            if (var11.getProperties() != null && (var11.getProperties().Is(var3) || var11.getProperties().Is(var4))) {
               var14.transmitRemoveItemFromSquare(var11);
               break;
            }
         }
      }

      return true;
   }

   private IsoObject getSpecialSolid() {
      int var1;
      IsoObject var2;
      for(var1 = 0; var1 < this.SpecialObjects.size(); ++var1) {
         var2 = (IsoObject)this.SpecialObjects.get(var1);
         if (var2 instanceof IsoThumpable) {
            IsoThumpable var3 = (IsoThumpable)var2;
            if (!var3.isStairs() && var3.isThumpable() && var3.isBlockAllTheSquare()) {
               if (var3.getProperties().Is(IsoFlagType.solidtrans) && this.isAdjacentToWindow()) {
                  return null;
               }

               return var3;
            }
         }
      }

      for(var1 = 0; var1 < this.Objects.size(); ++var1) {
         var2 = (IsoObject)this.Objects.get(var1);
         if (var2.isMovedThumpable()) {
            if (this.isAdjacentToWindow()) {
               return null;
            }

            return var2;
         }
      }

      return null;
   }

   public IsoObject testCollideSpecialObjects(IsoGridSquare var1) {
      if (var1 != null && var1 != this) {
         IsoObject var2;
         if (var1.x < this.x && var1.y == this.y) {
            if (var1.z == this.z && this.Has(IsoObjectType.stairsTW)) {
               return null;
            } else {
               var2 = this.getSpecialWall(false);
               if (var2 != null) {
                  return var2;
               } else if (this.isBlockedTo(var1)) {
                  return null;
               } else {
                  var2 = var1.getSpecialSolid();
                  return var2 != null ? var2 : null;
               }
            }
         } else if (var1.x == this.x && var1.y < this.y) {
            if (var1.z == this.z && this.Has(IsoObjectType.stairsTN)) {
               return null;
            } else {
               var2 = this.getSpecialWall(true);
               if (var2 != null) {
                  return var2;
               } else if (this.isBlockedTo(var1)) {
                  return null;
               } else {
                  var2 = var1.getSpecialSolid();
                  return var2 != null ? var2 : null;
               }
            }
         } else if (var1.x > this.x && var1.y == this.y) {
            var2 = var1.getSpecialWall(false);
            if (var2 != null) {
               return var2;
            } else if (this.isBlockedTo(var1)) {
               return null;
            } else {
               var2 = var1.getSpecialSolid();
               return var2 != null ? var2 : null;
            }
         } else if (var1.x == this.x && var1.y > this.y) {
            var2 = var1.getSpecialWall(true);
            if (var2 != null) {
               return var2;
            } else if (this.isBlockedTo(var1)) {
               return null;
            } else {
               var2 = var1.getSpecialSolid();
               return var2 != null ? var2 : null;
            }
         } else {
            IsoGridSquare var3;
            IsoGridSquare var4;
            if (var1.x < this.x && var1.y < this.y) {
               var2 = this.getSpecialWall(true);
               if (var2 != null) {
                  return var2;
               } else {
                  var2 = this.getSpecialWall(false);
                  if (var2 != null) {
                     return var2;
                  } else {
                     var3 = this.getCell().getGridSquare(this.x, this.y - 1, this.z);
                     if (var3 != null && !this.isBlockedTo(var3)) {
                        var2 = var3.getSpecialSolid();
                        if (var2 != null) {
                           return var2;
                        }

                        var2 = var3.getSpecialWall(false);
                        if (var2 != null) {
                           return var2;
                        }
                     }

                     var4 = this.getCell().getGridSquare(this.x - 1, this.y, this.z);
                     if (var4 != null && !this.isBlockedTo(var4)) {
                        var2 = var4.getSpecialSolid();
                        if (var2 != null) {
                           return var2;
                        }

                        var2 = var4.getSpecialWall(true);
                        if (var2 != null) {
                           return var2;
                        }
                     }

                     if (var3 != null && !this.isBlockedTo(var3) && var4 != null && !this.isBlockedTo(var4)) {
                        if (!var3.isBlockedTo(var1) && !var4.isBlockedTo(var1)) {
                           var2 = var1.getSpecialSolid();
                           return var2 != null ? var2 : null;
                        } else {
                           return null;
                        }
                     } else {
                        return null;
                     }
                  }
               }
            } else if (var1.x > this.x && var1.y < this.y) {
               var2 = this.getSpecialWall(true);
               if (var2 != null) {
                  return var2;
               } else {
                  var3 = this.getCell().getGridSquare(this.x, this.y - 1, this.z);
                  if (var3 != null && !this.isBlockedTo(var3)) {
                     var2 = var3.getSpecialSolid();
                     if (var2 != null) {
                        return var2;
                     }
                  }

                  var4 = this.getCell().getGridSquare(this.x + 1, this.y, this.z);
                  if (var4 != null) {
                     var2 = var4.getSpecialWall(false);
                     if (var2 != null) {
                        return var2;
                     }

                     if (!this.isBlockedTo(var4)) {
                        var2 = var4.getSpecialSolid();
                        if (var2 != null) {
                           return var2;
                        }

                        var2 = var4.getSpecialWall(true);
                        if (var2 != null) {
                           return var2;
                        }
                     }
                  }

                  if (var3 != null && !this.isBlockedTo(var3) && var4 != null && !this.isBlockedTo(var4)) {
                     var2 = var1.getSpecialWall(false);
                     if (var2 != null) {
                        return var2;
                     } else if (!var3.isBlockedTo(var1) && !var4.isBlockedTo(var1)) {
                        var2 = var1.getSpecialSolid();
                        return var2 != null ? var2 : null;
                     } else {
                        return null;
                     }
                  } else {
                     return null;
                  }
               }
            } else if (var1.x > this.x && var1.y > this.y) {
               var3 = this.getCell().getGridSquare(this.x, this.y + 1, this.z);
               if (var3 != null) {
                  var2 = var3.getSpecialWall(true);
                  if (var2 != null) {
                     return var2;
                  }

                  if (!this.isBlockedTo(var3)) {
                     var2 = var3.getSpecialSolid();
                     if (var2 != null) {
                        return var2;
                     }
                  }
               }

               var4 = this.getCell().getGridSquare(this.x + 1, this.y, this.z);
               if (var4 != null) {
                  var2 = var4.getSpecialWall(false);
                  if (var2 != null) {
                     return var2;
                  }

                  if (!this.isBlockedTo(var4)) {
                     var2 = var4.getSpecialSolid();
                     if (var2 != null) {
                        return var2;
                     }
                  }
               }

               if (var3 != null && !this.isBlockedTo(var3) && var4 != null && !this.isBlockedTo(var4)) {
                  var2 = var1.getSpecialWall(false);
                  if (var2 != null) {
                     return var2;
                  } else {
                     var2 = var1.getSpecialWall(true);
                     if (var2 != null) {
                        return var2;
                     } else if (!var3.isBlockedTo(var1) && !var4.isBlockedTo(var1)) {
                        var2 = var1.getSpecialSolid();
                        return var2 != null ? var2 : null;
                     } else {
                        return null;
                     }
                  }
               } else {
                  return null;
               }
            } else if (var1.x < this.x && var1.y > this.y) {
               var2 = this.getSpecialWall(false);
               if (var2 != null) {
                  return var2;
               } else {
                  var3 = this.getCell().getGridSquare(this.x, this.y + 1, this.z);
                  if (var3 != null) {
                     var2 = var3.getSpecialWall(true);
                     if (var2 != null) {
                        return var2;
                     }

                     if (!this.isBlockedTo(var3)) {
                        var2 = var3.getSpecialSolid();
                        if (var2 != null) {
                           return var2;
                        }
                     }
                  }

                  var4 = this.getCell().getGridSquare(this.x - 1, this.y, this.z);
                  if (var4 != null && !this.isBlockedTo(var4)) {
                     var2 = var4.getSpecialSolid();
                     if (var2 != null) {
                        return var2;
                     }
                  }

                  if (var3 != null && !this.isBlockedTo(var3) && var4 != null && !this.isBlockedTo(var4)) {
                     var2 = var1.getSpecialWall(true);
                     if (var2 != null) {
                        return var2;
                     } else if (!var3.isBlockedTo(var1) && !var4.isBlockedTo(var1)) {
                        var2 = var1.getSpecialSolid();
                        return var2 != null ? var2 : null;
                     } else {
                        return null;
                     }
                  } else {
                     return null;
                  }
               }
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public IsoObject getDoorFrameTo(IsoGridSquare var1) {
      ArrayList var2 = null;
      if (var1.y >= this.y && var1.x >= this.x) {
         var2 = var1.SpecialObjects;
      } else {
         var2 = this.SpecialObjects;
      }

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         boolean var5;
         if (var2.get(var3) instanceof IsoDoor) {
            IsoDoor var4 = (IsoDoor)var2.get(var3);
            var5 = var4.north;
            if (var5 && var1.y != this.y) {
               return var4;
            }

            if (!var5 && var1.x != this.x) {
               return var4;
            }
         } else if (var2.get(var3) instanceof IsoThumpable && ((IsoThumpable)var2.get(var3)).isDoor) {
            IsoThumpable var6 = (IsoThumpable)var2.get(var3);
            var5 = var6.north;
            if (var5 && var1.y != this.y) {
               return var6;
            }

            if (!var5 && var1.x != this.x) {
               return var6;
            }
         }
      }

      return null;
   }

   public static void getSquaresForThread(ArrayDeque var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         IsoGridSquare var3 = (IsoGridSquare)isoGridSquareCache.poll();
         if (var3 == null) {
            var0.add(new IsoGridSquare((IsoCell)null, (SliceY)null, 0, 0, 0));
         } else {
            var0.add(var3);
         }
      }

   }

   public static IsoGridSquare getNew(IsoCell var0, SliceY var1, int var2, int var3, int var4) {
      IsoGridSquare var5 = (IsoGridSquare)isoGridSquareCache.poll();
      if (var5 == null) {
         return new IsoGridSquare(var0, var1, var2, var3, var4);
      } else {
         var5.x = var2;
         var5.y = var3;
         var5.z = var4;
         var5.CachedScreenValue = -1;
         col = 0;
         path = 0;
         pathdoor = 0;
         vision = 0;
         var5.collideMatrix = 134217727;
         var5.pathMatrix = 134217727;
         var5.visionMatrix = 0;
         return var5;
      }
   }

   public static IsoGridSquare getNew(ArrayDeque var0, IsoCell var1, SliceY var2, int var3, int var4, int var5) {
      IsoGridSquare var6 = null;
      if (var0.isEmpty()) {
         return new IsoGridSquare(var1, var2, var3, var4, var5);
      } else {
         var6 = (IsoGridSquare)var0.pop();
         var6.x = var3;
         var6.y = var4;
         var6.z = var5;
         var6.CachedScreenValue = -1;
         col = 0;
         path = 0;
         pathdoor = 0;
         vision = 0;
         var6.collideMatrix = 134217727;
         var6.pathMatrix = 134217727;
         var6.visionMatrix = 0;
         return var6;
      }
   }

   /** @deprecated */
   @Deprecated
   public long getHashCodeObjects() {
      this.recalcHashCodeObjects();
      return this.hashCodeObjects;
   }

   /** @deprecated */
   @Deprecated
   public int getHashCodeObjectsInt() {
      this.recalcHashCodeObjects();
      return (int)this.hashCodeObjects;
   }

   /** @deprecated */
   @Deprecated
   public void recalcHashCodeObjects() {
      long var1 = 0L;
      this.hashCodeObjects = var1;
   }

   /** @deprecated */
   @Deprecated
   public int hashCodeNoOverride() {
      byte var1 = 0;
      this.recalcHashCodeObjects();
      int var5 = var1 * 2 + this.Objects.size();
      var5 = (int)((long)var5 + this.getHashCodeObjects());

      int var2;
      for(var2 = 0; var2 < this.Objects.size(); ++var2) {
         var5 = var5 * 2 + ((IsoObject)this.Objects.get(var2)).hashCode();
      }

      var2 = 0;

      int var3;
      for(var3 = 0; var3 < this.StaticMovingObjects.size(); ++var3) {
         if (this.StaticMovingObjects.get(var3) instanceof IsoDeadBody) {
            ++var2;
         }
      }

      var5 = var5 * 2 + var2;

      for(var3 = 0; var3 < this.StaticMovingObjects.size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)this.StaticMovingObjects.get(var3);
         if (var4 instanceof IsoDeadBody) {
            var5 = var5 * 2 + var4.hashCode();
         }
      }

      if (this.table != null && !this.table.isEmpty()) {
         var5 = var5 * 2 + this.table.hashCode();
      }

      byte var6 = 0;
      if (this.isOverlayDone()) {
         var6 = (byte)(var6 | 1);
      }

      if (this.haveRoof) {
         var6 = (byte)(var6 | 2);
      }

      if (this.burntOut) {
         var6 = (byte)(var6 | 4);
      }

      var5 = var5 * 2 + var6;
      var5 = var5 * 2 + this.getErosionData().hashCode();
      if (this.getTrapPositionX() > 0) {
         var5 = var5 * 2 + this.getTrapPositionX();
         var5 = var5 * 2 + this.getTrapPositionY();
         var5 = var5 * 2 + this.getTrapPositionZ();
      }

      var5 = var5 * 2 + (this.haveElectricity() ? 1 : 0);
      var5 = var5 * 2 + (this.haveSheetRope ? 1 : 0);
      return var5;
   }

   public IsoGridSquare(IsoCell var1, SliceY var2, int var3, int var4, int var5) {
      this.hasTypes = new ZomboidBitFlag(IsoObjectType.MAX.index());
      this.Properties = new PropertyContainer();
      this.SpecialObjects = new ArrayList(0);
      this.haveRoof = false;
      this.burntOut = false;
      this.bHasFlies = false;
      this.OcclusionDataCache = null;
      this.overlayDone = false;
      this.table = null;
      this.trapPositionX = -1;
      this.trapPositionY = -1;
      this.trapPositionZ = -1;
      this.haveElectricity = false;
      this.hourLastSeen = Integer.MIN_VALUE;
      this.propertiesDirty = true;
      this.splashFrame = -1.0F;
      this.lightInfo = new ColorInfo[4];
      this.RainDrop = null;
      this.RainSplash = null;
      this.ID = ++IDMax;
      this.x = var3;
      this.y = var4;
      this.z = var5;
      this.CachedScreenValue = -1;
      col = 0;
      path = 0;
      pathdoor = 0;
      vision = 0;
      this.collideMatrix = 134217727;
      this.pathMatrix = 134217727;
      this.visionMatrix = 0;

      for(int var6 = 0; var6 < 4; ++var6) {
         if (GameServer.bServer) {
            if (var6 == 0) {
               this.lighting[var6] = new ServerLOS.ServerLighting();
            }
         } else if (LightingJNI.init) {
            this.lighting[var6] = new LightingJNI.JNILighting(var6, this);
         } else {
            this.lighting[var6] = new IsoGridSquare.Lighting();
         }
      }

   }

   public IsoGridSquare getTileInDirection(IsoDirections var1) {
      if (var1 == IsoDirections.N) {
         return this.getCell().getGridSquare(this.x, this.y - 1, this.z);
      } else if (var1 == IsoDirections.NE) {
         return this.getCell().getGridSquare(this.x + 1, this.y - 1, this.z);
      } else if (var1 == IsoDirections.NW) {
         return this.getCell().getGridSquare(this.x - 1, this.y - 1, this.z);
      } else if (var1 == IsoDirections.E) {
         return this.getCell().getGridSquare(this.x + 1, this.y, this.z);
      } else if (var1 == IsoDirections.W) {
         return this.getCell().getGridSquare(this.x - 1, this.y, this.z);
      } else if (var1 == IsoDirections.SE) {
         return this.getCell().getGridSquare(this.x + 1, this.y + 1, this.z);
      } else if (var1 == IsoDirections.SW) {
         return this.getCell().getGridSquare(this.x - 1, this.y + 1, this.z);
      } else {
         return var1 == IsoDirections.S ? this.getCell().getGridSquare(this.x, this.y + 1, this.z) : null;
      }
   }

   IsoObject getWall() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2 != null && var2.sprite != null && (var2.sprite.cutW || var2.sprite.cutN)) {
            return var2;
         }
      }

      return null;
   }

   public IsoObject getThumpableWall(boolean var1) {
      IsoObject var2 = this.getWall(var1);
      return var2 != null && var2 instanceof IsoThumpable ? var2 : null;
   }

   public IsoObject getHoppableWall(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3 != null && var3.sprite != null) {
            PropertyContainer var4 = var3.getProperties();
            boolean var5 = var4.Is(IsoFlagType.TallHoppableW) && !var4.Is(IsoFlagType.WallWTrans);
            boolean var6 = var4.Is(IsoFlagType.TallHoppableN) && !var4.Is(IsoFlagType.WallNTrans);
            if (var5 && !var1 || var6 && var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public IsoObject getThumpableWallOrHoppable(boolean var1) {
      IsoObject var2 = this.getThumpableWall(var1);
      IsoObject var3 = this.getHoppableWall(var1);
      if (var2 != null && var3 != null && var2 == var3) {
         return var2;
      } else if (var2 == null && var3 != null) {
         return var3;
      } else {
         return var2 != null && var3 == null ? var2 : null;
      }
   }

   public Boolean getWallFull() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2 != null && var2.sprite != null && (var2.sprite.cutN || var2.sprite.cutW || var2.sprite.getProperties().Is(IsoFlagType.WallN) || var2.sprite.getProperties().Is(IsoFlagType.WallW))) {
            return true;
         }
      }

      IsoGridSquare var3 = this.getCell().getGridSquare(this.x, this.y + 1, this.z);
      if (var3 != null && this.isWallTo(var3)) {
         return true;
      } else {
         return false;
      }
   }

   public IsoObject getWall(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3 != null && var3.sprite != null && (var3.sprite.cutN && var1 || var3.sprite.cutW && !var1)) {
            return var3;
         }
      }

      return null;
   }

   public IsoObject getWallSE() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2 != null && var2.sprite != null && var2.sprite.getProperties().Is(IsoFlagType.WallSE)) {
            return var2;
         }
      }

      return null;
   }

   public IsoObject getFloor() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2.sprite != null && var2.sprite.getProperties().Is(IsoFlagType.solidfloor)) {
            return var2;
         }
      }

      return null;
   }

   public IsoObject getPlayerBuiltFloor() {
      return this.getBuilding() == null && this.roofHideBuilding == null ? this.getFloor() : null;
   }

   public void interpolateLight(ColorInfo var1, float var2, float var3) {
      IsoCell var4 = this.getCell();
      if (var2 < 0.0F) {
         var2 = 0.0F;
      }

      if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      if (var3 < 0.0F) {
         var3 = 0.0F;
      }

      if (var3 > 1.0F) {
         var3 = 1.0F;
      }

      int var5 = IsoCamera.frameState.playerIndex;
      int var6 = this.getVertLight(0, var5);
      int var7 = this.getVertLight(1, var5);
      int var8 = this.getVertLight(2, var5);
      int var9 = this.getVertLight(3, var5);
      tl.fromColor(var6);
      bl.fromColor(var9);
      tr.fromColor(var7);
      br.fromColor(var8);
      tl.interp(tr, var2, interp1);
      bl.interp(br, var2, interp2);
      interp1.interp(interp2, var3, finalCol);
      var1.r = finalCol.r;
      var1.g = finalCol.g;
      var1.b = finalCol.b;
      var1.a = finalCol.a;
   }

   public void EnsureSurroundNotNull() {
      assert !GameServer.bServer;

      for(int var1 = -1; var1 <= 1; ++var1) {
         for(int var2 = -1; var2 <= 1; ++var2) {
            if ((var1 != 0 || var2 != 0) && IsoWorld.instance.isValidSquare(this.x + var1, this.y + var2, this.z) && this.getCell().getChunkForGridSquare(this.x + var1, this.y + var2, this.z) != null) {
               IsoGridSquare var3 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z);
               if (var3 == null) {
                  var3 = getNew(this.getCell(), (SliceY)null, this.x + var1, this.y + var2, this.z);
                  IsoGridSquare var4 = this.getCell().ConnectNewSquare(var3, false);
               }
            }
         }
      }

   }

   public IsoObject addFloor(String var1) {
      IsoRegions.setPreviousFlags(this);
      IsoObject var2 = new IsoObject(this.getCell(), this, var1);
      boolean var3 = false;

      int var4;
      for(var4 = 0; var4 < this.getObjects().size(); ++var4) {
         IsoObject var5 = (IsoObject)this.getObjects().get(var4);
         IsoSprite var6 = var5.sprite;
         if (var6 != null && (var6.getProperties().Is(IsoFlagType.solidfloor) || var6.getProperties().Is(IsoFlagType.noStart) || var6.getProperties().Is(IsoFlagType.vegitation) && var5.getType() != IsoObjectType.tree || var6.getName() != null && var6.getName().startsWith("blends_grassoverlays"))) {
            if (var6.getName() != null && var6.getName().startsWith("floors_rugs")) {
               var3 = true;
            } else {
               this.transmitRemoveItemFromSquare(var5);
               --var4;
            }
         }
      }

      var2.sprite.getProperties().Set(IsoFlagType.solidfloor);
      if (var3) {
         this.getObjects().add(0, var2);
      } else {
         this.getObjects().add(var2);
      }

      this.EnsureSurroundNotNull();
      this.RecalcProperties();
      this.getCell().checkHaveRoof(this.x, this.y);

      for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
         LosUtil.cachecleared[var4] = true;
      }

      setRecalcLightTime(-1);
      GameTime.getInstance().lightSourceUpdate = 100.0F;
      var2.transmitCompleteItemToServer();
      this.RecalcAllWithNeighbours(true);

      for(var4 = this.z - 1; var4 > 0; --var4) {
         IsoGridSquare var7 = this.getCell().getGridSquare(this.x, this.y, var4);
         if (var7 == null) {
            var7 = getNew(this.getCell(), (SliceY)null, this.x, this.y, var4);
            this.getCell().ConnectNewSquare(var7, false);
         }

         var7.EnsureSurroundNotNull();
         var7.RecalcAllWithNeighbours(true);
      }

      this.setCachedIsFree(false);
      PolygonalMap2.instance.squareChanged(this);
      IsoGridOcclusionData.SquareChanged();
      IsoRegions.squareChanged(this);
      this.clearWater();
      return var2;
   }

   public IsoThumpable AddStairs(boolean var1, int var2, String var3, String var4, KahluaTable var5) {
      IsoRegions.setPreviousFlags(this);
      this.EnsureSurroundNotNull();
      boolean var6 = !this.TreatAsSolidFloor() && !this.HasStairsBelow();
      this.CachedIsFree = false;
      IsoThumpable var7 = new IsoThumpable(this.getCell(), this, var3, var1, var5);
      if (var1) {
         if (var2 == 0) {
            var7.setType(IsoObjectType.stairsBN);
         }

         if (var2 == 1) {
            var7.setType(IsoObjectType.stairsMN);
         }

         if (var2 == 2) {
            var7.setType(IsoObjectType.stairsTN);
            var7.sprite.getProperties().Set(var1 ? IsoFlagType.cutN : IsoFlagType.cutW);
         }
      }

      if (!var1) {
         if (var2 == 0) {
            var7.setType(IsoObjectType.stairsBW);
         }

         if (var2 == 1) {
            var7.setType(IsoObjectType.stairsMW);
         }

         if (var2 == 2) {
            var7.setType(IsoObjectType.stairsTW);
            var7.sprite.getProperties().Set(var1 ? IsoFlagType.cutN : IsoFlagType.cutW);
         }
      }

      this.AddSpecialObject(var7);
      int var8;
      if (var6 && var2 == 2) {
         var8 = this.z - 1;
         IsoGridSquare var9 = this.getCell().getGridSquare(this.x, this.y, var8);
         if (var9 == null) {
            var9 = new IsoGridSquare(this.getCell(), (SliceY)null, this.x, this.y, var8);
            this.getCell().ConnectNewSquare(var9, true);
         }

         while(var8 >= 0) {
            IsoThumpable var10 = new IsoThumpable(this.getCell(), var9, var4, var1, var5);
            var9.AddSpecialObject(var10);
            if (var9.TreatAsSolidFloor()) {
               break;
            }

            --var8;
            if (this.getCell().getGridSquare(var9.x, var9.y, var8) == null) {
               var9 = new IsoGridSquare(this.getCell(), (SliceY)null, var9.x, var9.y, var8);
               this.getCell().ConnectNewSquare(var9, true);
            } else {
               var9 = this.getCell().getGridSquare(var9.x, var9.y, var8);
            }
         }
      }

      if (var2 == 2) {
         IsoGridSquare var12 = null;
         if (var1) {
            if (IsoWorld.instance.isValidSquare(this.x, this.y - 1, this.z + 1)) {
               var12 = this.getCell().getGridSquare(this.x, this.y - 1, this.z + 1);
               if (var12 == null) {
                  var12 = new IsoGridSquare(this.getCell(), (SliceY)null, this.x, this.y - 1, this.z + 1);
                  this.getCell().ConnectNewSquare(var12, false);
               }

               if (!var12.Properties.Is(IsoFlagType.solidfloor)) {
                  var12.addFloor("carpentry_02_57");
               }
            }
         } else if (IsoWorld.instance.isValidSquare(this.x - 1, this.y, this.z + 1)) {
            var12 = this.getCell().getGridSquare(this.x - 1, this.y, this.z + 1);
            if (var12 == null) {
               var12 = new IsoGridSquare(this.getCell(), (SliceY)null, this.x - 1, this.y, this.z + 1);
               this.getCell().ConnectNewSquare(var12, false);
            }

            if (!var12.Properties.Is(IsoFlagType.solidfloor)) {
               var12.addFloor("carpentry_02_57");
            }
         }

         var12.getModData().rawset("ConnectedToStairs" + var1, true);
         var12 = this.getCell().getGridSquare(this.x, this.y, this.z + 1);
         if (var12 == null) {
            var12 = new IsoGridSquare(this.getCell(), (SliceY)null, this.x, this.y, this.z + 1);
            this.getCell().ConnectNewSquare(var12, false);
         }
      }

      for(var8 = this.getX() - 1; var8 <= this.getX() + 1; ++var8) {
         for(int var14 = this.getY() - 1; var14 <= this.getY() + 1; ++var14) {
            for(int var13 = this.getZ() - 1; var13 <= this.getZ() + 1; ++var13) {
               if (IsoWorld.instance.isValidSquare(var8, var14, var13)) {
                  IsoGridSquare var11 = this.getCell().getGridSquare(var8, var14, var13);
                  if (var11 == null) {
                     var11 = new IsoGridSquare(this.getCell(), (SliceY)null, var8, var14, var13);
                     this.getCell().ConnectNewSquare(var11, false);
                  }

                  var11.ReCalculateCollide(this);
                  var11.ReCalculateVisionBlocked(this);
                  var11.ReCalculatePathFind(this);
                  this.ReCalculateCollide(var11);
                  this.ReCalculatePathFind(var11);
                  this.ReCalculateVisionBlocked(var11);
                  var11.CachedIsFree = false;
               }
            }
         }
      }

      return var7;
   }

   void ReCalculateAll(IsoGridSquare var1) {
      this.ReCalculateAll(var1, cellGetSquare);
   }

   void ReCalculateAll(IsoGridSquare var1, IsoGridSquare.GetSquare var2) {
      if (var1 != null && var1 != this) {
         this.SolidFloorCached = false;
         var1.SolidFloorCached = false;
         this.RecalcPropertiesIfNeeded();
         var1.RecalcPropertiesIfNeeded();
         this.ReCalculateCollide(var1, var2);
         var1.ReCalculateCollide(this, var2);
         this.ReCalculatePathFind(var1, var2);
         var1.ReCalculatePathFind(this, var2);
         this.ReCalculateVisionBlocked(var1, var2);
         var1.ReCalculateVisionBlocked(this, var2);
         this.setBlockedGridPointers(var2);
         var1.setBlockedGridPointers(var2);
      }
   }

   void ReCalculateAll(boolean var1, IsoGridSquare var2, IsoGridSquare.GetSquare var3) {
      if (var2 != null && var2 != this) {
         this.SolidFloorCached = false;
         var2.SolidFloorCached = false;
         this.RecalcPropertiesIfNeeded();
         if (var1) {
            var2.RecalcPropertiesIfNeeded();
         }

         this.ReCalculateCollide(var2, var3);
         if (var1) {
            var2.ReCalculateCollide(this, var3);
         }

         this.ReCalculatePathFind(var2, var3);
         if (var1) {
            var2.ReCalculatePathFind(this, var3);
         }

         this.ReCalculateVisionBlocked(var2, var3);
         if (var1) {
            var2.ReCalculateVisionBlocked(this, var3);
         }

         this.setBlockedGridPointers(var3);
         if (var1) {
            var2.setBlockedGridPointers(var3);
         }

      }
   }

   void ReCalculateMineOnly(IsoGridSquare var1) {
      this.SolidFloorCached = false;
      this.RecalcProperties();
      this.ReCalculateCollide(var1);
      this.ReCalculatePathFind(var1);
      this.ReCalculateVisionBlocked(var1);
      this.setBlockedGridPointers(cellGetSquare);
   }

   public void RecalcAllWithNeighbours(boolean var1) {
      this.RecalcAllWithNeighbours(var1, cellGetSquare);
   }

   public void RecalcAllWithNeighbours(boolean var1, IsoGridSquare.GetSquare var2) {
      this.SolidFloorCached = false;
      this.RecalcPropertiesIfNeeded();

      for(int var3 = this.getX() - 1; var3 <= this.getX() + 1; ++var3) {
         for(int var4 = this.getY() - 1; var4 <= this.getY() + 1; ++var4) {
            for(int var5 = this.getZ() - 1; var5 <= this.getZ() + 1; ++var5) {
               if (IsoWorld.instance.isValidSquare(var3, var4, var5)) {
                  int var6 = var3 - this.getX();
                  int var7 = var4 - this.getY();
                  int var8 = var5 - this.getZ();
                  if (var6 != 0 || var7 != 0 || var8 != 0) {
                     IsoGridSquare var9 = var2.getGridSquare(var3, var4, var5);
                     if (var9 != null) {
                        var9.DirtySlice();
                        this.ReCalculateAll(var1, var9, var2);
                     }
                  }
               }
            }
         }
      }

      IsoWorld.instance.CurrentCell.DoGridNav(this, var2);
      IsoGridSquare var10 = this.nav[IsoDirections.N.index()];
      IsoGridSquare var11 = this.nav[IsoDirections.S.index()];
      IsoGridSquare var12 = this.nav[IsoDirections.W.index()];
      IsoGridSquare var13 = this.nav[IsoDirections.E.index()];
      if (var10 != null && var12 != null) {
         var10.ReCalculateAll(var12, var2);
      }

      if (var10 != null && var13 != null) {
         var10.ReCalculateAll(var13, var2);
      }

      if (var11 != null && var12 != null) {
         var11.ReCalculateAll(var12, var2);
      }

      if (var11 != null && var13 != null) {
         var11.ReCalculateAll(var13, var2);
      }

   }

   public void RecalcAllWithNeighboursMineOnly() {
      this.SolidFloorCached = false;
      this.RecalcProperties();

      for(int var1 = this.getX() - 1; var1 <= this.getX() + 1; ++var1) {
         for(int var2 = this.getY() - 1; var2 <= this.getY() + 1; ++var2) {
            for(int var3 = this.getZ() - 1; var3 <= this.getZ() + 1; ++var3) {
               if (var3 >= 0) {
                  int var4 = var1 - this.getX();
                  int var5 = var2 - this.getY();
                  int var6 = var3 - this.getZ();
                  if (var4 != 0 || var5 != 0 || var6 != 0) {
                     IsoGridSquare var7 = this.getCell().getGridSquare(var1, var2, var3);
                     if (var7 != null) {
                        var7.DirtySlice();
                        this.ReCalculateMineOnly(var7);
                     }
                  }
               }
            }
         }
      }

   }

   boolean IsWindow(int var1, int var2, int var3) {
      IsoGridSquare var4 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z + var3);
      return this.getWindowTo(var4) != null || this.getWindowThumpableTo(var4) != null;
   }

   void RemoveAllWith(IsoFlagType var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3.sprite != null && var3.sprite.getProperties().Is(var1)) {
            this.Objects.remove(var3);
            this.SpecialObjects.remove(var3);
            --var2;
         }
      }

      this.RecalcAllWithNeighbours(true);
   }

   public boolean hasSupport() {
      IsoGridSquare var1 = this.getCell().getGridSquare(this.x, this.y + 1, this.z);
      IsoGridSquare var2 = this.getCell().getGridSquare(this.x + 1, this.y, this.z);

      for(int var3 = 0; var3 < this.Objects.size(); ++var3) {
         IsoObject var4 = (IsoObject)this.Objects.get(var3);
         if (var4.sprite != null && (var4.sprite.getProperties().Is(IsoFlagType.solid) || (var4.sprite.getProperties().Is(IsoFlagType.cutW) || var4.sprite.getProperties().Is(IsoFlagType.cutN)) && !var4.sprite.Properties.Is(IsoFlagType.halfheight))) {
            return true;
         }
      }

      if (var1 != null && var1.Properties.Is(IsoFlagType.cutN) && !var1.Properties.Is(IsoFlagType.halfheight)) {
         return true;
      } else if (var2 != null && var2.Properties.Is(IsoFlagType.cutW) && !var1.Properties.Is(IsoFlagType.halfheight)) {
         return true;
      } else {
         return false;
      }
   }

   public Integer getID() {
      return this.ID;
   }

   public void setID(int var1) {
      this.ID = var1;
   }

   private int savematrix(boolean[][][] var1, byte[] var2, int var3) {
      for(int var4 = 0; var4 <= 2; ++var4) {
         for(int var5 = 0; var5 <= 2; ++var5) {
            for(int var6 = 0; var6 <= 2; ++var6) {
               var2[var3] = (byte)(var1[var4][var5][var6] ? 1 : 0);
               ++var3;
            }
         }
      }

      return var3;
   }

   private int loadmatrix(boolean[][][] var1, byte[] var2, int var3) {
      for(int var4 = 0; var4 <= 2; ++var4) {
         for(int var5 = 0; var5 <= 2; ++var5) {
            for(int var6 = 0; var6 <= 2; ++var6) {
               var1[var4][var5][var6] = var2[var3] != 0;
               ++var3;
            }
         }
      }

      return var3;
   }

   private void savematrix(boolean[][][] var1, ByteBuffer var2) {
      for(int var3 = 0; var3 <= 2; ++var3) {
         for(int var4 = 0; var4 <= 2; ++var4) {
            for(int var5 = 0; var5 <= 2; ++var5) {
               var2.put((byte)(var1[var3][var4][var5] ? 1 : 0));
            }
         }
      }

   }

   private void loadmatrix(boolean[][][] var1, ByteBuffer var2) {
      for(int var3 = 0; var3 <= 2; ++var3) {
         for(int var4 = 0; var4 <= 2; ++var4) {
            for(int var5 = 0; var5 <= 2; ++var5) {
               var1[var3][var4][var5] = var2.get() != 0;
            }
         }
      }

   }

   public void DirtySlice() {
   }

   public void setHourSeenToCurrent() {
      this.hourLastSeen = (int)GameTime.instance.getWorldAgeHours();
   }

   public void splatBlood(int var1, float var2) {
      var2 *= 2.0F;
      var2 *= 3.0F;
      if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      IsoGridSquare var3 = this;
      IsoGridSquare var4 = this;

      for(int var5 = 0; var5 < var1; ++var5) {
         if (var3 != null) {
            var3 = this.getCell().getGridSquare(this.getX(), this.getY() - var5, this.getZ());
         }

         if (var4 != null) {
            var4 = this.getCell().getGridSquare(this.getX() - var5, this.getY(), this.getZ());
         }

         float var6 = 0.0F;
         boolean var7;
         boolean var8;
         byte var9;
         byte var10;
         int var11;
         boolean var12;
         byte var13;
         byte var14;
         float var15;
         IsoGridSquare var16;
         IsoGridSquare var17;
         int var18;
         if (var4 != null && var4.testCollideAdjacent((IsoMovingObject)null, -1, 0, 0)) {
            var7 = false;
            var8 = false;
            var9 = 0;
            var10 = 0;
            if (var4.getS() != null && var4.getS().testCollideAdjacent((IsoMovingObject)null, -1, 0, 0)) {
               var7 = true;
            }

            if (var4.getN() != null && var4.getN().testCollideAdjacent((IsoMovingObject)null, -1, 0, 0)) {
               var8 = true;
            }

            if (var7) {
               var9 = -1;
            }

            if (var8) {
               var10 = 1;
            }

            var11 = var10 - var9;
            var12 = false;
            var13 = 0;
            var14 = 0;
            if (var11 > 0 && Rand.Next(2) == 0) {
               var12 = true;
               if (var11 > 1) {
                  if (Rand.Next(2) == 0) {
                     var13 = -1;
                     var14 = 0;
                  } else {
                     var13 = 0;
                     var14 = 1;
                  }
               } else {
                  var13 = var9;
                  var14 = var10;
               }
            }

            var15 = (float)Rand.Next(100) / 300.0F;
            var16 = this.getCell().getGridSquare(var4.getX(), var4.getY() + var13, var4.getZ());
            var17 = this.getCell().getGridSquare(var4.getX(), var4.getY() + var14, var4.getZ());
            if (var16 == null || var17 == null || !var16.Is(IsoFlagType.cutW) || !var17.Is(IsoFlagType.cutW) || var16.getProperties().Is(IsoFlagType.WallSE) || var17.getProperties().Is(IsoFlagType.WallSE) || var16.Is(IsoFlagType.HoppableW) || var17.Is(IsoFlagType.HoppableW)) {
               var12 = false;
            }

            if (var12) {
               var18 = 24 + Rand.Next(2) * 2;
               if (Rand.Next(2) == 0) {
                  var18 += 8;
               }

               var16.DoSplat("overlay_blood_wall_01_" + (var18 + 1), false, IsoFlagType.cutW, var6, var15, var2);
               var17.DoSplat("overlay_blood_wall_01_" + (var18 + 0), false, IsoFlagType.cutW, var6, var15, var2);
            } else {
               var18 = 0;
               switch(Rand.Next(3)) {
               case 0:
                  var18 = 0 + Rand.Next(4);
                  break;
               case 1:
                  var18 = 8 + Rand.Next(4);
                  break;
               case 2:
                  var18 = 16 + Rand.Next(4);
               }

               if (var18 == 17 || var18 == 19) {
                  var15 = 0.0F;
               }

               if (var4.Is(IsoFlagType.HoppableW)) {
                  var4.DoSplat("overlay_blood_fence_01_" + var18, false, IsoFlagType.HoppableW, var6, 0.0F, var2);
               } else {
                  var4.DoSplat("overlay_blood_wall_01_" + var18, false, IsoFlagType.cutW, var6, var15, var2);
               }
            }

            var4 = null;
         }

         if (var3 != null && var3.testCollideAdjacent((IsoMovingObject)null, 0, -1, 0)) {
            var7 = false;
            var8 = false;
            var9 = 0;
            var10 = 0;
            if (var3.getW() != null && var3.getW().testCollideAdjacent((IsoMovingObject)null, 0, -1, 0)) {
               var7 = true;
            }

            if (var3.getE() != null && var3.getE().testCollideAdjacent((IsoMovingObject)null, 0, -1, 0)) {
               var8 = true;
            }

            if (var7) {
               var9 = -1;
            }

            if (var8) {
               var10 = 1;
            }

            var11 = var10 - var9;
            var12 = false;
            var13 = 0;
            var14 = 0;
            if (var11 > 0 && Rand.Next(2) == 0) {
               var12 = true;
               if (var11 > 1) {
                  if (Rand.Next(2) == 0) {
                     var13 = -1;
                     var14 = 0;
                  } else {
                     var13 = 0;
                     var14 = 1;
                  }
               } else {
                  var13 = var9;
                  var14 = var10;
               }
            }

            var15 = (float)Rand.Next(100) / 300.0F;
            var16 = this.getCell().getGridSquare(var3.getX() + var13, var3.getY(), var3.getZ());
            var17 = this.getCell().getGridSquare(var3.getX() + var14, var3.getY(), var3.getZ());
            if (var16 == null || var17 == null || !var16.Is(IsoFlagType.cutN) || !var17.Is(IsoFlagType.cutN) || var16.getProperties().Is(IsoFlagType.WallSE) || var17.getProperties().Is(IsoFlagType.WallSE) || var16.Is(IsoFlagType.HoppableN) || var17.Is(IsoFlagType.HoppableN)) {
               var12 = false;
            }

            if (var12) {
               var18 = 28 + Rand.Next(2) * 2;
               if (Rand.Next(2) == 0) {
                  var18 += 8;
               }

               var16.DoSplat("overlay_blood_wall_01_" + (var18 + 0), false, IsoFlagType.cutN, var6, var15, var2);
               var17.DoSplat("overlay_blood_wall_01_" + (var18 + 1), false, IsoFlagType.cutN, var6, var15, var2);
            } else {
               var18 = 0;
               switch(Rand.Next(3)) {
               case 0:
                  var18 = 4 + Rand.Next(4);
                  break;
               case 1:
                  var18 = 12 + Rand.Next(4);
                  break;
               case 2:
                  var18 = 20 + Rand.Next(4);
               }

               if (var18 == 20 || var18 == 22) {
                  var15 = 0.0F;
               }

               if (var3.Is(IsoFlagType.HoppableN)) {
                  var3.DoSplat("overlay_blood_fence_01_" + var18, false, IsoFlagType.HoppableN, var6, var15, var2);
               } else {
                  var3.DoSplat("overlay_blood_wall_01_" + var18, false, IsoFlagType.cutN, var6, var15, var2);
               }
            }

            var3 = null;
         }
      }

   }

   public boolean haveBlood() {
      if (Core.OptionBloodDecals == 0) {
         return false;
      } else {
         int var1;
         for(var1 = 0; var1 < this.getObjects().size(); ++var1) {
            IsoObject var2 = (IsoObject)this.getObjects().get(var1);
            if (var2.wallBloodSplats != null && !var2.wallBloodSplats.isEmpty()) {
               return true;
            }
         }

         for(var1 = 0; var1 < this.getChunk().FloorBloodSplats.size(); ++var1) {
            IsoFloorBloodSplat var5 = (IsoFloorBloodSplat)this.getChunk().FloorBloodSplats.get(var1);
            float var3 = var5.x + (float)(this.getChunk().wx * 10);
            float var4 = var5.y + (float)(this.getChunk().wy * 10);
            if ((int)var3 - 1 <= this.x && (int)var3 + 1 >= this.x && (int)var4 - 1 <= this.y && (int)var4 + 1 >= this.y) {
               return true;
            }
         }

         return false;
      }
   }

   public void removeBlood(boolean var1, boolean var2) {
      int var3;
      for(var3 = 0; var3 < this.getObjects().size(); ++var3) {
         IsoObject var4 = (IsoObject)this.getObjects().get(var3);
         if (var4.wallBloodSplats != null) {
            var4.wallBloodSplats.clear();
         }
      }

      if (!var2) {
         for(var3 = 0; var3 < this.getChunk().FloorBloodSplats.size(); ++var3) {
            IsoFloorBloodSplat var7 = (IsoFloorBloodSplat)this.getChunk().FloorBloodSplats.get(var3);
            int var5 = (int)((float)(this.getChunk().wx * 10) + var7.x);
            int var6 = (int)((float)(this.getChunk().wy * 10) + var7.y);
            if (var5 >= this.getX() - 1 && var5 <= this.getX() + 1 && var6 >= this.getY() - 1 && var6 <= this.getY() + 1) {
               this.getChunk().FloorBloodSplats.remove(var3);
               --var3;
            }
         }
      }

      if (GameClient.bClient && !var1) {
         ByteBufferWriter var8 = GameClient.connection.startPacket();
         PacketTypes.PacketType.RemoveBlood.doPacket(var8);
         var8.putInt(this.x);
         var8.putInt(this.y);
         var8.putInt(this.z);
         var8.putBoolean(var2);
         PacketTypes.PacketType.RemoveBlood.send(GameClient.connection);
      }

   }

   public void DoSplat(String var1, boolean var2, IsoFlagType var3, float var4, float var5, float var6) {
      for(int var7 = 0; var7 < this.getObjects().size(); ++var7) {
         IsoObject var8 = (IsoObject)this.getObjects().get(var7);
         if (var8.sprite != null && var8.sprite.getProperties().Is(var3)) {
            IsoSprite var9 = IsoSprite.getSprite(IsoSpriteManager.instance, (String)var1, 0);
            if (var9 == null) {
               return;
            }

            if (var8.wallBloodSplats == null) {
               var8.wallBloodSplats = new ArrayList();
            }

            IsoWallBloodSplat var10 = new IsoWallBloodSplat((float)GameTime.getInstance().getWorldAgeHours(), var9);
            var8.wallBloodSplats.add(var10);
         }
      }

   }

   public void ClearTileObjects() {
      this.Objects.clear();
      this.RecalcProperties();
   }

   public void ClearTileObjectsExceptFloor() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2.sprite == null || !var2.sprite.getProperties().Is(IsoFlagType.solidfloor)) {
            this.Objects.remove(var2);
            --var1;
         }
      }

      this.RecalcProperties();
   }

   public int RemoveTileObject(IsoObject var1) {
      IsoRegions.setPreviousFlags(this);
      int var2 = this.Objects.indexOf(var1);
      if (!this.Objects.contains(var1)) {
         var2 = this.SpecialObjects.indexOf(var1);
      }

      if (var1 != null && this.Objects.contains(var1)) {
         if (var1.isTableSurface()) {
            for(int var3 = this.Objects.indexOf(var1) + 1; var3 < this.Objects.size(); ++var3) {
               IsoObject var4 = (IsoObject)this.Objects.get(var3);
               if (var4.isTableTopObject() || var4.isTableSurface()) {
                  var4.setRenderYOffset(var4.getRenderYOffset() - var1.getSurfaceOffset());
                  var4.sx = 0.0F;
                  var4.sy = 0.0F;
               }
            }
         }

         IsoObject var5 = this.getPlayerBuiltFloor();
         if (var1 == var5) {
            IsoGridOcclusionData.SquareChanged();
         }

         LuaEventManager.triggerEvent("OnObjectAboutToBeRemoved", var1);
         if (!this.Objects.contains(var1)) {
            throw new IllegalArgumentException("OnObjectAboutToBeRemoved not allowed to remove the object");
         }

         var2 = this.Objects.indexOf(var1);
         var1.removeFromWorld();
         var1.removeFromSquare();

         assert !this.Objects.contains(var1);

         assert !this.SpecialObjects.contains(var1);

         if (!(var1 instanceof IsoWorldInventoryObject)) {
            this.RecalcAllWithNeighbours(true);
            this.getCell().checkHaveRoof(this.getX(), this.getY());

            for(int var6 = 0; var6 < IsoPlayer.numPlayers; ++var6) {
               LosUtil.cachecleared[var6] = true;
            }

            setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
         }
      }

      MapCollisionData.instance.squareChanged(this);
      LuaEventManager.triggerEvent("OnTileRemoved", var1);
      PolygonalMap2.instance.squareChanged(this);
      IsoRegions.squareChanged(this, true);
      return var2;
   }

   public int RemoveTileObjectErosionNoRecalc(IsoObject var1) {
      int var2 = this.Objects.indexOf(var1);
      IsoGridSquare var3 = var1.square;
      var1.removeFromWorld();
      var1.removeFromSquare();
      var3.RecalcPropertiesIfNeeded();

      assert !this.Objects.contains(var1);

      assert !this.SpecialObjects.contains(var1);

      return var2;
   }

   public void AddSpecialObject(IsoObject var1) {
      this.AddSpecialObject(var1, -1);
   }

   public void AddSpecialObject(IsoObject var1, int var2) {
      if (var1 != null) {
         IsoRegions.setPreviousFlags(this);
         var2 = this.placeWallAndDoorCheck(var1, var2);
         if (var2 != -1 && var2 >= 0 && var2 <= this.Objects.size()) {
            this.Objects.add(var2, var1);
         } else {
            this.Objects.add(var1);
         }

         this.SpecialObjects.add(var1);
         this.burntOut = false;
         var1.addToWorld();
         if (!GameServer.bServer && !GameClient.bClient) {
            this.restackSheetRope();
         }

         this.RecalcAllWithNeighbours(true);
         if (!(var1 instanceof IsoWorldInventoryObject)) {
            for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
               LosUtil.cachecleared[var3] = true;
            }

            setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
            if (var1 == this.getPlayerBuiltFloor()) {
               IsoGridOcclusionData.SquareChanged();
            }
         }

         MapCollisionData.instance.squareChanged(this);
         PolygonalMap2.instance.squareChanged(this);
         IsoRegions.squareChanged(this);
      }
   }

   public void AddTileObject(IsoObject var1) {
      this.AddTileObject(var1, -1);
   }

   public void AddTileObject(IsoObject var1, int var2) {
      if (var1 != null) {
         IsoRegions.setPreviousFlags(this);
         var2 = this.placeWallAndDoorCheck(var1, var2);
         if (var2 != -1 && var2 >= 0 && var2 <= this.Objects.size()) {
            this.Objects.add(var2, var1);
         } else {
            this.Objects.add(var1);
         }

         this.burntOut = false;
         var1.addToWorld();
         this.RecalcAllWithNeighbours(true);
         if (!(var1 instanceof IsoWorldInventoryObject)) {
            for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
               LosUtil.cachecleared[var3] = true;
            }

            setRecalcLightTime(-1);
            GameTime.instance.lightSourceUpdate = 100.0F;
            if (var1 == this.getPlayerBuiltFloor()) {
               IsoGridOcclusionData.SquareChanged();
            }
         }

         MapCollisionData.instance.squareChanged(this);
         PolygonalMap2.instance.squareChanged(this);
         IsoRegions.squareChanged(this);
      }
   }

   public int placeWallAndDoorCheck(IsoObject var1, int var2) {
      int var4 = -1;
      if (var1.sprite != null) {
         IsoObjectType var3 = var1.sprite.getType();
         boolean var5 = var3 == IsoObjectType.doorN || var3 == IsoObjectType.doorW;
         boolean var6 = !var5 && (var1.sprite.cutW || var1.sprite.cutN || var3 == IsoObjectType.doorFrN || var3 == IsoObjectType.doorFrW || var1.sprite.treatAsWallOrder);
         if (var6 || var5) {
            int var8 = 0;

            while(true) {
               if (var8 >= this.Objects.size()) {
                  if (var5 && var4 > var2) {
                     var2 = var4 + 1;
                     return var2;
                  }

                  if (var6 && var4 >= 0 && (var4 < var2 || var2 < 0)) {
                     return var4;
                  }
                  break;
               }

               IsoObject var7 = (IsoObject)this.Objects.get(var8);
               var3 = IsoObjectType.MAX;
               if (var7.sprite != null) {
                  var3 = var7.sprite.getType();
                  if (var6 && (var3 == IsoObjectType.doorN || var3 == IsoObjectType.doorW)) {
                     var4 = var8;
                  }

                  if (var5 && (var3 == IsoObjectType.doorFrN || var3 == IsoObjectType.doorFrW || var7.sprite.cutW || var7.sprite.cutN || var7.sprite.treatAsWallOrder)) {
                     var4 = var8;
                  }
               }

               ++var8;
            }
         }
      }

      return var2;
   }

   public void transmitAddObjectToSquare(IsoObject var1, int var2) {
      if (var1 != null && !this.Objects.contains(var1)) {
         this.AddTileObject(var1, var2);
         if (GameClient.bClient) {
            var1.transmitCompleteItemToServer();
         }

         if (GameServer.bServer) {
            var1.transmitCompleteItemToClients();
         }

      }
   }

   public int transmitRemoveItemFromSquare(IsoObject var1) {
      if (var1 != null && this.Objects.contains(var1)) {
         if (GameClient.bClient) {
            try {
               GameClient.instance.checkAddedRemovedItems(var1);
            } catch (Exception var3) {
               GameClient.connection.cancelPacket();
               ExceptionLogger.logException(var3);
            }

            ByteBufferWriter var2 = GameClient.connection.startPacket();
            PacketTypes.PacketType.RemoveItemFromSquare.doPacket(var2);
            var2.putInt(this.getX());
            var2.putInt(this.getY());
            var2.putInt(this.getZ());
            var2.putInt(this.Objects.indexOf(var1));
            PacketTypes.PacketType.RemoveItemFromSquare.send(GameClient.connection);
         }

         return GameServer.bServer ? GameServer.RemoveItemFromMap(var1) : this.RemoveTileObject(var1);
      } else {
         return -1;
      }
   }

   public void transmitRemoveItemFromSquareOnServer(IsoObject var1) {
      if (var1 != null && this.Objects.contains(var1)) {
         if (GameServer.bServer) {
            GameServer.RemoveItemFromMap(var1);
         }

      }
   }

   public void transmitModdata() {
      if (GameClient.bClient) {
         ByteBufferWriter var1 = GameClient.connection.startPacket();
         PacketTypes.PacketType.SendModData.doPacket(var1);
         var1.putInt(this.getX());
         var1.putInt(this.getY());
         var1.putInt(this.getZ());

         try {
            this.getModData().save(var1.bb);
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         PacketTypes.PacketType.SendModData.send(GameClient.connection);
      } else if (GameServer.bServer) {
         GameServer.loadModData(this);
      }

   }

   public void AddWorldInventoryItem(String var1, float var2, float var3, float var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         this.AddWorldInventoryItem(var1, var2, var3, var4);
      }

   }

   public InventoryItem AddWorldInventoryItem(String var1, float var2, float var3, float var4) {
      InventoryItem var5 = InventoryItemFactory.CreateItem(var1);
      if (var5 == null) {
         return null;
      } else {
         IsoWorldInventoryObject var6 = new IsoWorldInventoryObject(var5, this, var2, var3, var4);
         var5.setWorldItem(var6);
         var6.setKeyId(var5.getKeyId());
         var6.setName(var5.getName());
         this.Objects.add(var6);
         this.WorldObjects.add(var6);
         var6.square.chunk.recalcHashCodeObjects();
         if (GameClient.bClient) {
            var6.transmitCompleteItemToServer();
         }

         if (GameServer.bServer) {
            var6.transmitCompleteItemToClients();
         }

         return var5;
      }
   }

   public InventoryItem AddWorldInventoryItem(InventoryItem var1, float var2, float var3, float var4) {
      return this.AddWorldInventoryItem(var1, var2, var3, var4, true);
   }

   public InventoryItem AddWorldInventoryItem(InventoryItem var1, float var2, float var3, float var4, boolean var5) {
      if (!var1.getFullType().contains(".Corpse")) {
         if (var1.getFullType().contains(".Generator")) {
            new IsoGenerator(var1, IsoWorld.instance.CurrentCell, this);
            return var1;
         } else {
            IsoWorldInventoryObject var14 = new IsoWorldInventoryObject(var1, this, var2, var3, var4);
            var14.setName(var1.getName());
            var14.setKeyId(var1.getKeyId());
            this.Objects.add(var14);
            this.WorldObjects.add(var14);
            var14.square.chunk.recalcHashCodeObjects();
            var1.setWorldItem(var14);
            var14.addToWorld();
            if (var5) {
               if (GameClient.bClient) {
                  var14.transmitCompleteItemToServer();
               }

               if (GameServer.bServer) {
                  var14.transmitCompleteItemToClients();
               }
            }

            return var1;
         }
      } else if (var1.byteData == null) {
         IsoZombie var13 = new IsoZombie(IsoWorld.instance.CurrentCell);
         var13.setDir(IsoDirections.fromIndex(Rand.Next(8)));
         var13.getForwardDirection().set(var13.dir.ToVector());
         var13.setFakeDead(false);
         var13.setHealth(0.0F);
         var13.upKillCount = false;
         var13.setX((float)this.x + var2);
         var13.setY((float)this.y + var3);
         var13.setZ((float)this.z);
         var13.square = this;
         var13.current = this;
         var13.dressInRandomOutfit();
         var13.DoZombieInventory();
         IsoDeadBody var15 = new IsoDeadBody(var13, true);
         this.addCorpse(var15, false);
         if (GameServer.bServer) {
            GameServer.sendCorpse(var15);
         }

         return var1;
      } else {
         IsoDeadBody var6 = new IsoDeadBody(IsoWorld.instance.CurrentCell);

         try {
            byte var7 = var1.byteData.get();
            byte var16 = var1.byteData.get();
            byte var9 = var1.byteData.get();
            byte var10 = var1.byteData.get();
            int var11 = 56;
            if (var7 == 87 && var16 == 86 && var9 == 69 && var10 == 82) {
               var11 = var1.byteData.getInt();
            } else {
               var1.byteData.rewind();
            }

            var6.load(var1.byteData, var11);
         } catch (IOException var12) {
            var12.printStackTrace();
            IsoZombie var8 = new IsoZombie((IsoCell)null);
            var8.dir = var6.dir;
            var8.current = this;
            var8.x = var6.x;
            var8.y = var6.y;
            var8.z = var6.z;
            var6 = new IsoDeadBody(var8);
         }

         var6.setX((float)this.x + var2);
         var6.setY((float)this.y + var3);
         var6.setZ((float)this.z);
         var6.square = this;
         this.addCorpse(var6, false);
         if (GameServer.bServer) {
            GameServer.sendCorpse(var6);
         }

         return var1;
      }
   }

   public void restackSheetRope() {
      if (this.Is(IsoFlagType.climbSheetW) || this.Is(IsoFlagType.climbSheetN) || this.Is(IsoFlagType.climbSheetE) || this.Is(IsoFlagType.climbSheetS)) {
         for(int var1 = 0; var1 < this.getObjects().size() - 1; ++var1) {
            IsoObject var2 = (IsoObject)this.getObjects().get(var1);
            if (var2.getProperties() != null && (var2.getProperties().Is(IsoFlagType.climbSheetW) || var2.getProperties().Is(IsoFlagType.climbSheetN) || var2.getProperties().Is(IsoFlagType.climbSheetE) || var2.getProperties().Is(IsoFlagType.climbSheetS))) {
               if (GameServer.bServer) {
                  this.transmitRemoveItemFromSquare(var2);
                  this.Objects.add(var2);
                  var2.transmitCompleteItemToClients();
               } else if (!GameClient.bClient) {
                  this.Objects.remove(var2);
                  this.Objects.add(var2);
               }
               break;
            }
         }

      }
   }

   public void Burn() {
      if (!GameServer.bServer && !GameClient.bClient || !ServerOptions.instance.NoFire.getValue()) {
         if (this.getCell() != null) {
            this.BurnWalls(true);
            LuaEventManager.triggerEvent("OnGridBurnt", this);
         }
      }
   }

   public void Burn(boolean var1) {
      if (!GameServer.bServer && !GameClient.bClient || !ServerOptions.instance.NoFire.getValue()) {
         if (this.getCell() != null) {
            this.BurnWalls(var1);
         }
      }
   }

   public void BurnWalls(boolean var1) {
      if (!GameClient.bClient) {
         if (GameServer.bServer && SafeHouse.isSafeHouse(this, (String)null, false) != null) {
            if (ServerOptions.instance.NoFire.getValue()) {
               return;
            }

            if (!ServerOptions.instance.SafehouseAllowFire.getValue()) {
               return;
            }
         }

         for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
            IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
            if (var3 instanceof IsoThumpable && ((IsoThumpable)var3).haveSheetRope()) {
               ((IsoThumpable)var3).removeSheetRope((IsoPlayer)null);
            }

            if (var3 instanceof IsoWindow) {
               if (((IsoWindow)var3).haveSheetRope()) {
                  ((IsoWindow)var3).removeSheetRope((IsoPlayer)null);
               }

               ((IsoWindow)var3).removeSheet((IsoGameCharacter)null);
            }

            if (IsoWindowFrame.isWindowFrame(var3) && IsoWindowFrame.haveSheetRope(var3)) {
               IsoWindowFrame.removeSheetRope(var3, (IsoPlayer)null);
            }

            if (var3 instanceof BarricadeAble) {
               IsoBarricade var4 = ((BarricadeAble)var3).getBarricadeOnSameSquare();
               IsoBarricade var5 = ((BarricadeAble)var3).getBarricadeOnOppositeSquare();
               if (var4 != null) {
                  if (GameServer.bServer) {
                     GameServer.RemoveItemFromMap(var4);
                  } else {
                     this.RemoveTileObject(var4);
                  }
               }

               if (var5 != null) {
                  if (GameServer.bServer) {
                     GameServer.RemoveItemFromMap(var5);
                  } else {
                     var5.getSquare().RemoveTileObject(var5);
                  }
               }
            }
         }

         this.SpecialObjects.clear();
         boolean var10 = false;
         if (!this.getProperties().Is(IsoFlagType.burntOut)) {
            int var11 = 0;

            for(int var12 = 0; var12 < this.Objects.size(); ++var12) {
               IsoObject var13 = (IsoObject)this.Objects.get(var12);
               boolean var6 = false;
               if (var13.getSprite() != null && var13.getSprite().getName() != null && !var13.getSprite().getProperties().Is(IsoFlagType.water) && !var13.getSprite().getName().contains("_burnt_")) {
                  IsoObject var14;
                  if (var13 instanceof IsoThumpable && var13.getSprite().burntTile != null) {
                     var14 = IsoObject.getNew();
                     var14.setSprite(IsoSpriteManager.instance.getSprite(var13.getSprite().burntTile));
                     var14.setSquare(this);
                     if (GameServer.bServer) {
                        var13.sendObjectChange("replaceWith", "object", var14);
                     }

                     var13.removeFromWorld();
                     this.Objects.set(var12, var14);
                  } else if (var13.getSprite().burntTile != null) {
                     var13.sprite = IsoSpriteManager.instance.getSprite(var13.getSprite().burntTile);
                     var13.RemoveAttachedAnims();
                     if (var13.Children != null) {
                        var13.Children.clear();
                     }

                     var13.transmitUpdatedSpriteToClients();
                     var13.setOverlaySprite((String)null);
                  } else {
                     IsoSpriteManager var10001;
                     if (var13.getType() == IsoObjectType.tree) {
                        var10001 = IsoSpriteManager.instance;
                        int var16 = Rand.Next(15, 19);
                        var13.sprite = var10001.getSprite("fencing_burnt_01_" + (var16 + 1));
                        var13.RemoveAttachedAnims();
                        if (var13.Children != null) {
                           var13.Children.clear();
                        }

                        var13.transmitUpdatedSpriteToClients();
                        var13.setOverlaySprite((String)null);
                     } else if (!(var13 instanceof IsoTrap)) {
                        if (!(var13 instanceof IsoBarricade) && !(var13 instanceof IsoMannequin)) {
                           if (var13 instanceof IsoGenerator) {
                              IsoGenerator var15 = (IsoGenerator)var13;
                              if (var15.getFuel() > 0.0F) {
                                 var11 += 20;
                              }

                              if (var15.isActivated()) {
                                 var15.activated = false;
                                 var15.setSurroundingElectricity();
                                 if (GameServer.bServer) {
                                    var15.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
                                 }
                              }

                              if (GameServer.bServer) {
                                 GameServer.RemoveItemFromMap(var13);
                              } else {
                                 this.RemoveTileObject(var13);
                              }

                              --var12;
                           } else {
                              if (var13.getType() == IsoObjectType.wall && !var13.getProperties().Is(IsoFlagType.DoorWallW) && !var13.getProperties().Is(IsoFlagType.DoorWallN) && !var13.getProperties().Is("WindowN") && !var13.getProperties().Is(IsoFlagType.WindowW) && !var13.getSprite().getName().startsWith("walls_exterior_roofs_") && !var13.getSprite().getName().startsWith("fencing_") && !var13.getSprite().getName().startsWith("fixtures_railings_")) {
                                 if (var13.getSprite().getProperties().Is(IsoFlagType.collideW) && !var13.getSprite().getProperties().Is(IsoFlagType.collideN)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "0" : "4"));
                                 } else if (var13.getSprite().getProperties().Is(IsoFlagType.collideN) && !var13.getSprite().getProperties().Is(IsoFlagType.collideW)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "1" : "5"));
                                 } else if (var13.getSprite().getProperties().Is(IsoFlagType.collideW) && var13.getSprite().getProperties().Is(IsoFlagType.collideN)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "2" : "6"));
                                 } else if (var13.getProperties().Is(IsoFlagType.WallSE)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "3" : "7"));
                                 }
                              } else {
                                 if (var13 instanceof IsoDoor || var13 instanceof IsoWindow || var13 instanceof IsoCurtain) {
                                    if (GameServer.bServer) {
                                       GameServer.RemoveItemFromMap(var13);
                                    } else {
                                       this.RemoveTileObject(var13);
                                       var10 = true;
                                    }

                                    --var12;
                                    continue;
                                 }

                                 if (var13.getProperties().Is(IsoFlagType.WindowW)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "8" : "12"));
                                 } else if (var13.getProperties().Is("WindowN")) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "9" : "13"));
                                 } else if (var13.getProperties().Is(IsoFlagType.DoorWallW)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "10" : "14"));
                                 } else if (var13.getProperties().Is(IsoFlagType.DoorWallN)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("walls_burnt_01_" + (Rand.Next(2) == 0 ? "11" : "15"));
                                 } else if (var13.getSprite().getProperties().Is(IsoFlagType.solidfloor) && !var13.getSprite().getProperties().Is(IsoFlagType.exterior)) {
                                    var13.sprite = IsoSpriteManager.instance.getSprite("floors_burnt_01_0");
                                 } else {
                                    if (var13 instanceof IsoWaveSignal) {
                                       if (GameServer.bServer) {
                                          GameServer.RemoveItemFromMap(var13);
                                       } else {
                                          this.RemoveTileObject(var13);
                                          var10 = true;
                                       }

                                       --var12;
                                       continue;
                                    }

                                    if (var13.getContainer() != null && var13.getContainer().getItems() != null) {
                                       InventoryItem var7 = null;

                                       int var8;
                                       for(var8 = 0; var8 < var13.getContainer().getItems().size(); ++var8) {
                                          var7 = (InventoryItem)var13.getContainer().getItems().get(var8);
                                          if (var7 instanceof Food && ((Food)var7).isAlcoholic() || var7.getType().equals("PetrolCan") || var7.getType().equals("Bleach")) {
                                             var11 += 20;
                                             if (var11 > 100) {
                                                var11 = 100;
                                                break;
                                             }
                                          }
                                       }

                                       var13.sprite = IsoSpriteManager.instance.getSprite("floors_burnt_01_" + Rand.Next(1, 2));

                                       for(var8 = 0; var8 < var13.getContainerCount(); ++var8) {
                                          ItemContainer var9 = var13.getContainerByIndex(var8);
                                          var9.removeItemsFromProcessItems();
                                          var9.removeAllItems();
                                       }

                                       var13.removeAllContainers();
                                       if (var13.getOverlaySprite() != null) {
                                          var13.setOverlaySprite((String)null);
                                       }

                                       var6 = true;
                                    } else if (!var13.getSprite().getProperties().Is(IsoFlagType.solidtrans) && !var13.getSprite().getProperties().Is(IsoFlagType.bed) && !var13.getSprite().getProperties().Is(IsoFlagType.waterPiped)) {
                                       String var10002;
                                       if (var13.getSprite().getName().startsWith("walls_exterior_roofs_")) {
                                          var10001 = IsoSpriteManager.instance;
                                          var10002 = var13.getSprite().getName();
                                          var13.sprite = var10001.getSprite("walls_burnt_roofs_01_" + var10002.substring(var13.getSprite().getName().lastIndexOf("_") + 1, var13.getSprite().getName().length()));
                                       } else if (!var13.getSprite().getName().startsWith("roofs_accents")) {
                                          if (var13.getSprite().getName().startsWith("roofs_")) {
                                             var10001 = IsoSpriteManager.instance;
                                             var10002 = var13.getSprite().getName();
                                             var13.sprite = var10001.getSprite("roofs_burnt_01_" + var10002.substring(var13.getSprite().getName().lastIndexOf("_") + 1, var13.getSprite().getName().length()));
                                          } else if ((var13.getSprite().getName().startsWith("fencing_") || var13.getSprite().getName().startsWith("fixtures_railings_")) && (var13.getSprite().getProperties().Is(IsoFlagType.HoppableN) || var13.getSprite().getProperties().Is(IsoFlagType.HoppableW))) {
                                             if (var13.getSprite().getProperties().Is(IsoFlagType.transparentW) && !var13.getSprite().getProperties().Is(IsoFlagType.transparentN)) {
                                                var13.sprite = IsoSpriteManager.instance.getSprite("fencing_burnt_01_0");
                                             } else if (var13.getSprite().getProperties().Is(IsoFlagType.transparentN) && !var13.getSprite().getProperties().Is(IsoFlagType.transparentW)) {
                                                var13.sprite = IsoSpriteManager.instance.getSprite("fencing_burnt_01_1");
                                             } else {
                                                var13.sprite = IsoSpriteManager.instance.getSprite("fencing_burnt_01_2");
                                             }
                                          }
                                       }
                                    } else {
                                       var13.sprite = IsoSpriteManager.instance.getSprite("floors_burnt_01_" + Rand.Next(1, 2));
                                       if (var13.getOverlaySprite() != null) {
                                          var13.setOverlaySprite((String)null);
                                       }
                                    }
                                 }
                              }

                              if (!var6 && !(var13 instanceof IsoThumpable)) {
                                 var13.RemoveAttachedAnims();
                                 var13.transmitUpdatedSpriteToClients();
                                 var13.setOverlaySprite((String)null);
                              } else {
                                 var14 = IsoObject.getNew();
                                 var14.setSprite(var13.getSprite());
                                 var14.setSquare(this);
                                 if (GameServer.bServer) {
                                    var13.sendObjectChange("replaceWith", "object", var14);
                                 }

                                 this.Objects.set(var12, var14);
                              }

                              if (var13.emitter != null) {
                                 var13.emitter.stopAll();
                                 var13.emitter = null;
                              }
                           }
                        } else {
                           if (GameServer.bServer) {
                              GameServer.RemoveItemFromMap(var13);
                           } else {
                              this.Objects.remove(var13);
                           }

                           --var12;
                        }
                     }
                  }
               }
            }

            if (var11 > 0 && var1) {
               if (GameServer.bServer) {
                  GameServer.PlayWorldSoundServer("BurnedObjectExploded", false, this, 0.0F, 50.0F, 1.0F, false);
               } else {
                  SoundManager.instance.PlayWorldSound("BurnedObjectExploded", this, 0.0F, 50.0F, 1.0F, false);
               }

               IsoFireManager.explode(this.getCell(), this, var11);
            }
         }

         if (!var10) {
            this.RecalcProperties();
         }

         this.getProperties().Set(IsoFlagType.burntOut);
         this.burntOut = true;
         MapCollisionData.instance.squareChanged(this);
         PolygonalMap2.instance.squareChanged(this);
      }
   }

   public void BurnWallsTCOnly() {
      for(int var1 = 0; var1 < this.Objects.size(); ++var1) {
         IsoObject var2 = (IsoObject)this.Objects.get(var1);
         if (var2.sprite == null) {
         }
      }

   }

   public void BurnTick() {
      if (!GameClient.bClient) {
         for(int var1 = 0; var1 < this.StaticMovingObjects.size(); ++var1) {
            IsoMovingObject var2 = (IsoMovingObject)this.StaticMovingObjects.get(var1);
            if (var2 instanceof IsoDeadBody) {
               ((IsoDeadBody)var2).Burn();
               if (!this.StaticMovingObjects.contains(var2)) {
                  --var1;
               }
            }
         }

      }
   }

   public boolean CalculateCollide(IsoGridSquare var1, boolean var2, boolean var3, boolean var4) {
      return this.CalculateCollide(var1, var2, var3, var4, false);
   }

   public boolean CalculateCollide(IsoGridSquare var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      return this.CalculateCollide(var1, var2, var3, var4, var5, cellGetSquare);
   }

   public boolean CalculateCollide(IsoGridSquare var1, boolean var2, boolean var3, boolean var4, boolean var5, IsoGridSquare.GetSquare var6) {
      if (var1 == null && var3) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         if (var2 && var1.Properties.Is(IsoFlagType.trans)) {
         }

         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         if (var1.x < this.x) {
            var7 = true;
         }

         if (var1.y < this.y) {
            var9 = true;
         }

         if (var1.x > this.x) {
            var8 = true;
         }

         if (var1.y > this.y) {
            var10 = true;
         }

         if (!var5 && var1.Properties.Is(IsoFlagType.solid)) {
            if (this.Has(IsoObjectType.stairsTW) && !var3 && var1.x < this.x && var1.y == this.y && var1.z == this.z) {
               return false;
            } else {
               return !this.Has(IsoObjectType.stairsTN) || var3 || var1.x != this.x || var1.y >= this.y || var1.z != this.z;
            }
         } else {
            if (!var4 && var1.Properties.Is(IsoFlagType.solidtrans)) {
               if (this.Has(IsoObjectType.stairsTW) && !var3 && var1.x < this.x && var1.y == this.y && var1.z == this.z) {
                  return false;
               }

               if (this.Has(IsoObjectType.stairsTN) && !var3 && var1.x == this.x && var1.y < this.y && var1.z == this.z) {
                  return false;
               }

               boolean var11 = false;
               if (var1.Properties.Is(IsoFlagType.windowW) || var1.Properties.Is(IsoFlagType.windowN)) {
                  var11 = true;
               }

               if (!var11 && (var1.Properties.Is(IsoFlagType.WindowW) || var1.Properties.Is(IsoFlagType.WindowN))) {
                  var11 = true;
               }

               IsoGridSquare var12;
               if (!var11) {
                  var12 = var6.getGridSquare(var1.x, var1.y + 1, this.z);
                  if (var12 != null && (var12.Is(IsoFlagType.windowN) || var12.Is(IsoFlagType.WindowN))) {
                     var11 = true;
                  }
               }

               if (!var11) {
                  var12 = var6.getGridSquare(var1.x + 1, var1.y, this.z);
                  if (var12 != null && (var12.Is(IsoFlagType.windowW) || var12.Is(IsoFlagType.WindowW))) {
                     var11 = true;
                  }
               }

               if (!var11) {
                  return true;
               }
            }

            if (var1.x != this.x && var1.y != this.y && this.z != var1.z && var3) {
               return true;
            } else {
               if (var3 && var1.z < this.z) {
                  label695: {
                     if (this.SolidFloorCached) {
                        if (this.SolidFloor) {
                           break label695;
                        }
                     } else if (this.TreatAsSolidFloor()) {
                        break label695;
                     }

                     if (!var1.Has(IsoObjectType.stairsTN) && !var1.Has(IsoObjectType.stairsTW)) {
                        return false;
                     }

                     return true;
                  }
               }

               if (var3 && var1.z == this.z) {
                  if (var1.x > this.x && var1.y == this.y && var1.Properties.Is(IsoFlagType.windowW)) {
                     return false;
                  }

                  if (var1.y > this.y && var1.x == this.x && var1.Properties.Is(IsoFlagType.windowN)) {
                     return false;
                  }

                  if (var1.x < this.x && var1.y == this.y && this.Properties.Is(IsoFlagType.windowW)) {
                     return false;
                  }

                  if (var1.y < this.y && var1.x == this.x && this.Properties.Is(IsoFlagType.windowN)) {
                     return false;
                  }
               }

               if (var1.x > this.x && var1.z < this.z && var1.Has(IsoObjectType.stairsTW)) {
                  return false;
               } else if (var1.y > this.y && var1.z < this.z && var1.Has(IsoObjectType.stairsTN)) {
                  return false;
               } else {
                  IsoGridSquare var20 = var6.getGridSquare(var1.x, var1.y, var1.z - 1);
                  if (var1.x != this.x && var1.z == this.z && var1.Has(IsoObjectType.stairsTN) && (var20 == null || !var20.Has(IsoObjectType.stairsTN) || var3)) {
                     return true;
                  } else if (var1.y > this.y && var1.x == this.x && var1.z == this.z && var1.Has(IsoObjectType.stairsTN) && (var20 == null || !var20.Has(IsoObjectType.stairsTN) || var3)) {
                     return true;
                  } else if (var1.x > this.x && var1.y == this.y && var1.z == this.z && var1.Has(IsoObjectType.stairsTW) && (var20 == null || !var20.Has(IsoObjectType.stairsTW) || var3)) {
                     return true;
                  } else if (var1.y == this.y || var1.z != this.z || !var1.Has(IsoObjectType.stairsTW) || var20 != null && var20.Has(IsoObjectType.stairsTW) && !var3) {
                     if (var1.x != this.x && var1.z == this.z && var1.Has(IsoObjectType.stairsMN)) {
                        return true;
                     } else if (var1.y != this.y && var1.z == this.z && var1.Has(IsoObjectType.stairsMW)) {
                        return true;
                     } else if (var1.x != this.x && var1.z == this.z && var1.Has(IsoObjectType.stairsBN)) {
                        return true;
                     } else if (var1.y != this.y && var1.z == this.z && var1.Has(IsoObjectType.stairsBW)) {
                        return true;
                     } else if (var1.x != this.x && var1.z == this.z && this.Has(IsoObjectType.stairsTN)) {
                        return true;
                     } else if (var1.y != this.y && var1.z == this.z && this.Has(IsoObjectType.stairsTW)) {
                        return true;
                     } else if (var1.x != this.x && var1.z == this.z && this.Has(IsoObjectType.stairsMN)) {
                        return true;
                     } else if (var1.y != this.y && var1.z == this.z && this.Has(IsoObjectType.stairsMW)) {
                        return true;
                     } else if (var1.x != this.x && var1.z == this.z && this.Has(IsoObjectType.stairsBN)) {
                        return true;
                     } else if (var1.y != this.y && var1.z == this.z && this.Has(IsoObjectType.stairsBW)) {
                        return true;
                     } else if (var1.y < this.y && var1.x == this.x && var1.z > this.z && this.Has(IsoObjectType.stairsTN)) {
                        return false;
                     } else if (var1.x < this.x && var1.y == this.y && var1.z > this.z && this.Has(IsoObjectType.stairsTW)) {
                        return false;
                     } else if (var1.y > this.y && var1.x == this.x && var1.z < this.z && var1.Has(IsoObjectType.stairsTN)) {
                        return false;
                     } else if (var1.x > this.x && var1.y == this.y && var1.z < this.z && var1.Has(IsoObjectType.stairsTW)) {
                        return false;
                     } else {
                        if (var1.z == this.z) {
                           label529: {
                              if (var1.SolidFloorCached) {
                                 if (var1.SolidFloor) {
                                    break label529;
                                 }
                              } else if (var1.TreatAsSolidFloor()) {
                                 break label529;
                              }

                              if (var3) {
                                 return true;
                              }
                           }
                        }

                        if (var1.z == this.z) {
                           label522: {
                              if (var1.SolidFloorCached) {
                                 if (var1.SolidFloor) {
                                    break label522;
                                 }
                              } else if (var1.TreatAsSolidFloor()) {
                                 break label522;
                              }

                              if (var1.z > 0) {
                                 var20 = var6.getGridSquare(var1.x, var1.y, var1.z - 1);
                                 if (var20 == null) {
                                    return true;
                                 }
                              }
                           }
                        }

                        if (this.z != var1.z) {
                           if (var1.z < this.z && var1.x == this.x && var1.y == this.y) {
                              if (this.SolidFloorCached) {
                                 if (!this.SolidFloor) {
                                    return false;
                                 }
                              } else if (!this.TreatAsSolidFloor()) {
                                 return false;
                              }
                           }

                           return true;
                        } else {
                           boolean var19 = var9 && this.Properties.Is(IsoFlagType.collideN);
                           boolean var13 = var7 && this.Properties.Is(IsoFlagType.collideW);
                           boolean var14 = var10 && var1.Properties.Is(IsoFlagType.collideN);
                           boolean var15 = var8 && var1.Properties.Is(IsoFlagType.collideW);
                           if (var19 && var3 && this.Properties.Is(IsoFlagType.canPathN)) {
                              var19 = false;
                           }

                           if (var13 && var3 && this.Properties.Is(IsoFlagType.canPathW)) {
                              var13 = false;
                           }

                           if (var14 && var3 && var1.Properties.Is(IsoFlagType.canPathN)) {
                              var14 = false;
                           }

                           if (var15 && var3 && var1.Properties.Is(IsoFlagType.canPathW)) {
                              var15 = false;
                           }

                           if (var13 && this.Has(IsoObjectType.stairsTW) && !var3) {
                              var13 = false;
                           }

                           if (var19 && this.Has(IsoObjectType.stairsTN) && !var3) {
                              var19 = false;
                           }

                           if (!var19 && !var13 && !var14 && !var15) {
                              boolean var16 = var1.x != this.x && var1.y != this.y;
                              if (var16) {
                                 IsoGridSquare var17 = var6.getGridSquare(this.x, var1.y, this.z);
                                 IsoGridSquare var18 = var6.getGridSquare(var1.x, this.y, this.z);
                                 if (var17 != null && var17 != this && var17 != var1) {
                                    var17.RecalcPropertiesIfNeeded();
                                 }

                                 if (var18 != null && var18 != this && var18 != var1) {
                                    var18.RecalcPropertiesIfNeeded();
                                 }

                                 if (var1 == this || var17 == var18 || var17 == this || var18 == this || var17 == var1 || var18 == var1) {
                                    return true;
                                 }

                                 if (var1.x == this.x + 1 && var1.y == this.y + 1 && var17 != null && var18 != null && var17.Is(IsoFlagType.windowN) && var18.Is(IsoFlagType.windowW)) {
                                    return true;
                                 }

                                 if (var1.x == this.x - 1 && var1.y == this.y - 1 && var17 != null && var18 != null && var17.Is(IsoFlagType.windowW) && var18.Is(IsoFlagType.windowN)) {
                                    return true;
                                 }

                                 if (this.CalculateCollide(var17, var2, var3, var4, false, var6)) {
                                    return true;
                                 }

                                 if (this.CalculateCollide(var18, var2, var3, var4, false, var6)) {
                                    return true;
                                 }

                                 if (var1.CalculateCollide(var17, var2, var3, var4, false, var6)) {
                                    return true;
                                 }

                                 if (var1.CalculateCollide(var18, var2, var3, var4, false, var6)) {
                                    return true;
                                 }
                              }

                              return false;
                           } else {
                              return true;
                           }
                        }
                     }
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public boolean CalculateVisionBlocked(IsoGridSquare var1) {
      return this.CalculateVisionBlocked(var1, cellGetSquare);
   }

   public boolean CalculateVisionBlocked(IsoGridSquare var1, IsoGridSquare.GetSquare var2) {
      if (var1 == null) {
         return false;
      } else if (Math.abs(var1.getX() - this.getX()) <= 1 && Math.abs(var1.getY() - this.getY()) <= 1) {
         boolean var3 = false;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         if (var1.x < this.x) {
            var3 = true;
         }

         if (var1.y < this.y) {
            var5 = true;
         }

         if (var1.x > this.x) {
            var4 = true;
         }

         if (var1.y > this.y) {
            var6 = true;
         }

         if (!var1.Properties.Is(IsoFlagType.trans) && !this.Properties.Is(IsoFlagType.trans)) {
            if (this.z != var1.z) {
               IsoGridSquare var7;
               if (var1.z > this.z) {
                  label255: {
                     label234: {
                        if (var1.SolidFloorCached) {
                           if (!var1.SolidFloor) {
                              break label234;
                           }
                        } else if (!var1.TreatAsSolidFloor()) {
                           break label234;
                        }

                        if (!var1.getProperties().Is(IsoFlagType.transparentFloor)) {
                           return true;
                        }
                     }

                     if (this.Properties.Is(IsoFlagType.noStart)) {
                        return true;
                     }

                     var7 = var2.getGridSquare(this.x, this.y, var1.z);
                     if (var7 == null) {
                        return false;
                     }

                     if (var7.SolidFloorCached) {
                        if (!var7.SolidFloor) {
                           break label255;
                        }
                     } else if (!var7.TreatAsSolidFloor()) {
                        break label255;
                     }

                     if (!var7.getProperties().Is(IsoFlagType.transparentFloor)) {
                        return true;
                     }
                  }
               } else {
                  label256: {
                     label220: {
                        if (this.SolidFloorCached) {
                           if (!this.SolidFloor) {
                              break label220;
                           }
                        } else if (!this.TreatAsSolidFloor()) {
                           break label220;
                        }

                        if (!this.getProperties().Is(IsoFlagType.transparentFloor)) {
                           return true;
                        }
                     }

                     if (this.Properties.Is(IsoFlagType.noStart)) {
                        return true;
                     }

                     var7 = var2.getGridSquare(var1.x, var1.y, this.z);
                     if (var7 == null) {
                        return false;
                     }

                     if (var7.SolidFloorCached) {
                        if (!var7.SolidFloor) {
                           break label256;
                        }
                     } else if (!var7.TreatAsSolidFloor()) {
                        break label256;
                     }

                     if (!var7.getProperties().Is(IsoFlagType.transparentFloor)) {
                        return true;
                     }
                  }
               }
            }

            if (var1.x > this.x && var1.Properties.Is(IsoFlagType.transparentW)) {
               return false;
            } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.transparentN)) {
               return false;
            } else if (var1.x < this.x && this.Properties.Is(IsoFlagType.transparentW)) {
               return false;
            } else if (var1.y < this.y && this.Properties.Is(IsoFlagType.transparentN)) {
               return false;
            } else if (var1.x > this.x && var1.Properties.Is(IsoFlagType.doorW)) {
               return false;
            } else if (var1.y > this.y && var1.Properties.Is(IsoFlagType.doorN)) {
               return false;
            } else if (var1.x < this.x && this.Properties.Is(IsoFlagType.doorW)) {
               return false;
            } else if (var1.y < this.y && this.Properties.Is(IsoFlagType.doorN)) {
               return false;
            } else {
               boolean var14 = var5 && this.Properties.Is(IsoFlagType.collideN);
               boolean var8 = var3 && this.Properties.Is(IsoFlagType.collideW);
               boolean var9 = var6 && var1.Properties.Is(IsoFlagType.collideN);
               boolean var10 = var4 && var1.Properties.Is(IsoFlagType.collideW);
               if (!var14 && !var8 && !var9 && !var10) {
                  boolean var11 = var1.x != this.x && var1.y != this.y;
                  if (!var1.Properties.Is(IsoFlagType.solid) && !var1.Properties.Is(IsoFlagType.blocksight)) {
                     if (var11) {
                        IsoGridSquare var12 = var2.getGridSquare(this.x, var1.y, this.z);
                        IsoGridSquare var13 = var2.getGridSquare(var1.x, this.y, this.z);
                        if (var12 != null && var12 != this && var12 != var1) {
                           var12.RecalcPropertiesIfNeeded();
                        }

                        if (var13 != null && var13 != this && var13 != var1) {
                           var13.RecalcPropertiesIfNeeded();
                        }

                        if (this.CalculateVisionBlocked(var12)) {
                           return true;
                        }

                        if (this.CalculateVisionBlocked(var13)) {
                           return true;
                        }

                        if (var1.CalculateVisionBlocked(var12)) {
                           return true;
                        }

                        if (var1.CalculateVisionBlocked(var13)) {
                           return true;
                        }
                     }

                     return false;
                  } else {
                     return true;
                  }
               } else {
                  return true;
               }
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public IsoGameCharacter FindFriend(IsoGameCharacter var1, int var2, Stack var3) {
      Stack var4 = new Stack();

      for(int var5 = 0; var5 < var1.getLocalList().size(); ++var5) {
         IsoMovingObject var6 = (IsoMovingObject)var1.getLocalList().get(var5);
         if (var6 != var1 && var6 != var1.getFollowingTarget() && var6 instanceof IsoGameCharacter && !(var6 instanceof IsoZombie) && !var3.contains(var6)) {
            var4.add((IsoGameCharacter)var6);
         }
      }

      float var10 = 1000000.0F;
      IsoGameCharacter var11 = null;
      Iterator var7 = var4.iterator();

      while(var7.hasNext()) {
         IsoGameCharacter var8 = (IsoGameCharacter)var7.next();
         float var9 = 0.0F;
         var9 += Math.abs((float)this.getX() - var8.getX());
         var9 += Math.abs((float)this.getY() - var8.getY());
         var9 += Math.abs((float)this.getZ() - var8.getZ());
         if (var9 < var10) {
            var11 = var8;
            var10 = var9;
         }

         if (var8 == IsoPlayer.getInstance()) {
            var11 = var8;
            var9 = 0.0F;
         }
      }

      if (var10 > (float)var2) {
         return null;
      } else {
         return var11;
      }
   }

   public IsoGameCharacter FindEnemy(IsoGameCharacter var1, int var2, ArrayList var3, IsoGameCharacter var4, int var5) {
      float var6 = 1000000.0F;
      IsoGameCharacter var7 = null;

      for(int var8 = 0; var8 < var3.size(); ++var8) {
         IsoGameCharacter var9 = (IsoGameCharacter)var3.get(var8);
         float var10 = 0.0F;
         var10 += Math.abs((float)this.getX() - var9.getX());
         var10 += Math.abs((float)this.getY() - var9.getY());
         var10 += Math.abs((float)this.getZ() - var9.getZ());
         if (var10 < (float)var2 && var10 < var6 && var9.DistTo(var4) < (float)var5) {
            var7 = var9;
            var6 = var10;
         }
      }

      if (var6 > (float)var2) {
         return null;
      } else {
         return var7;
      }
   }

   public IsoGameCharacter FindEnemy(IsoGameCharacter var1, int var2, ArrayList var3) {
      float var4 = 1000000.0F;
      IsoGameCharacter var5 = null;

      for(int var6 = 0; var6 < var3.size(); ++var6) {
         IsoGameCharacter var7 = (IsoGameCharacter)var3.get(var6);
         float var8 = 0.0F;
         var8 += Math.abs((float)this.getX() - var7.getX());
         var8 += Math.abs((float)this.getY() - var7.getY());
         var8 += Math.abs((float)this.getZ() - var7.getZ());
         if (var8 < var4) {
            var5 = var7;
            var4 = var8;
         }
      }

      if (var4 > (float)var2) {
         return null;
      } else {
         return var5;
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public void RecalcProperties() {
      this.CachedIsFree = false;
      String var1 = null;
      if (this.Properties.Is("waterAmount")) {
         var1 = this.Properties.Val("waterAmount");
      }

      String var2 = null;
      if (this.Properties.Is("fuelAmount")) {
         var2 = this.Properties.Val("fuelAmount");
      }

      if (this.zone == null) {
         this.zone = IsoWorld.instance.MetaGrid.getZoneAt(this.x, this.y, this.z);
      }

      this.Properties.Clear();
      this.hasTypes.clear();
      this.hasTree = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      int var11 = this.Objects.size();
      IsoObject[] var12 = (IsoObject[])this.Objects.getElements();

      for(int var13 = 0; var13 < var11; ++var13) {
         IsoObject var14 = var12[var13];
         if (var14 != null) {
            PropertyContainer var15 = var14.getProperties();
            if (var15 != null && !var15.Is(IsoFlagType.blueprint)) {
               if (var14.sprite.forceRender) {
                  var10 = true;
               }

               if (var14.getType() == IsoObjectType.tree) {
                  this.hasTree = true;
               }

               this.hasTypes.set(var14.getType(), true);
               this.Properties.AddProperties(var15);
               if (var15.Is(IsoFlagType.water)) {
                  var4 = false;
               } else {
                  if (!var4 && var15.Is(IsoFlagType.solidfloor)) {
                     var4 = true;
                  }

                  if (!var3 && var15.Is(IsoFlagType.solidtrans)) {
                     var3 = true;
                  }

                  if (!var5 && var15.Is(IsoFlagType.solidfloor) && !var15.Is(IsoFlagType.transparentFloor)) {
                     var5 = true;
                  }
               }

               if (!var6 && var15.Is(IsoFlagType.collideN) && !var15.Is(IsoFlagType.HoppableN)) {
                  var6 = true;
               }

               if (!var7 && var15.Is(IsoFlagType.collideW) && !var15.Is(IsoFlagType.HoppableW)) {
                  var7 = true;
               }

               if (!var8 && var15.Is(IsoFlagType.cutN) && !var15.Is(IsoFlagType.transparentN)) {
                  var8 = true;
               }

               if (!var9 && var15.Is(IsoFlagType.cutW) && !var15.Is(IsoFlagType.transparentW)) {
                  var9 = true;
               }
            }
         }
      }

      if (this.roomID == -1 && !this.haveRoof) {
         this.getProperties().Set(IsoFlagType.exterior);

         try {
            this.getPuddles().bRecalc = true;
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      } else {
         this.getProperties().UnSet(IsoFlagType.exterior);

         try {
            this.getPuddles().bRecalc = true;
         } catch (Exception var17) {
            var17.printStackTrace();
         }
      }

      if (var1 != null) {
         this.getProperties().Set("waterAmount", var1, false);
      }

      if (var2 != null) {
         this.getProperties().Set("fuelAmount", var2, false);
      }

      if (this.RainDrop != null) {
         this.Properties.Set(IsoFlagType.HasRaindrop);
      }

      if (var10) {
         this.Properties.Set(IsoFlagType.forceRender);
      }

      if (this.RainSplash != null) {
         this.Properties.Set(IsoFlagType.HasRainSplashes);
      }

      if (this.burntOut) {
         this.Properties.Set(IsoFlagType.burntOut);
      }

      if (!var3 && var4 && this.Properties.Is(IsoFlagType.water)) {
         this.Properties.UnSet(IsoFlagType.solidtrans);
      }

      if (var5 && this.Properties.Is(IsoFlagType.transparentFloor)) {
         this.Properties.UnSet(IsoFlagType.transparentFloor);
      }

      if (var6 && this.Properties.Is(IsoFlagType.HoppableN)) {
         this.Properties.UnSet(IsoFlagType.canPathN);
         this.Properties.UnSet(IsoFlagType.HoppableN);
      }

      if (var7 && this.Properties.Is(IsoFlagType.HoppableW)) {
         this.Properties.UnSet(IsoFlagType.canPathW);
         this.Properties.UnSet(IsoFlagType.HoppableW);
      }

      if (var8 && this.Properties.Is(IsoFlagType.transparentN)) {
         this.Properties.UnSet(IsoFlagType.transparentN);
      }

      if (var9 && this.Properties.Is(IsoFlagType.transparentW)) {
         this.Properties.UnSet(IsoFlagType.transparentW);
      }

      this.propertiesDirty = this.chunk == null || this.chunk.bLoaded;
      if (this.chunk != null) {
         this.chunk.lightCheck[0] = this.chunk.lightCheck[1] = this.chunk.lightCheck[2] = this.chunk.lightCheck[3] = true;
      }

      if (this.chunk != null) {
         this.chunk.physicsCheck = true;
         this.chunk.collision.clear();
      }

      this.isExteriorCache = this.Is(IsoFlagType.exterior);
      this.isSolidFloorCache = this.Is(IsoFlagType.solidfloor);
      this.isVegitationCache = this.Is(IsoFlagType.vegitation);
   }

   public void RecalcPropertiesIfNeeded() {
      if (this.propertiesDirty) {
         this.RecalcProperties();
      }

   }

   public void ReCalculateCollide(IsoGridSquare var1) {
      this.ReCalculateCollide(var1, cellGetSquare);
   }

   public void ReCalculateCollide(IsoGridSquare var1, IsoGridSquare.GetSquare var2) {
      if (1 + (var1.x - this.x) < 0 || 1 + (var1.y - this.y) < 0 || 1 + (var1.z - this.z) < 0) {
         DebugLog.log("ERROR");
      }

      boolean var3 = this.CalculateCollide(var1, false, false, false, false, var2);
      this.collideMatrix = setMatrixBit(this.collideMatrix, 1 + (var1.x - this.x), 1 + (var1.y - this.y), 1 + (var1.z - this.z), var3);
   }

   public void ReCalculatePathFind(IsoGridSquare var1) {
      this.ReCalculatePathFind(var1, cellGetSquare);
   }

   public void ReCalculatePathFind(IsoGridSquare var1, IsoGridSquare.GetSquare var2) {
      boolean var3 = this.CalculateCollide(var1, false, true, false, false, var2);
      this.pathMatrix = setMatrixBit(this.pathMatrix, 1 + (var1.x - this.x), 1 + (var1.y - this.y), 1 + (var1.z - this.z), var3);
   }

   public void ReCalculateVisionBlocked(IsoGridSquare var1) {
      this.ReCalculateVisionBlocked(var1, cellGetSquare);
   }

   public void ReCalculateVisionBlocked(IsoGridSquare var1, IsoGridSquare.GetSquare var2) {
      boolean var3 = this.CalculateVisionBlocked(var1, var2);
      this.visionMatrix = setMatrixBit(this.visionMatrix, 1 + (var1.x - this.x), 1 + (var1.y - this.y), 1 + (var1.z - this.z), var3);
   }

   private static boolean testCollideSpecialObjects(IsoMovingObject var0, IsoGridSquare var1, IsoGridSquare var2) {
      for(int var3 = 0; var3 < var2.SpecialObjects.size(); ++var3) {
         IsoObject var4 = (IsoObject)var2.SpecialObjects.get(var3);
         if (var4.TestCollide(var0, var1, var2)) {
            if (var4 instanceof IsoDoor) {
               var0.setCollidedWithDoor(true);
            } else if (var4 instanceof IsoThumpable && ((IsoThumpable)var4).isDoor) {
               var0.setCollidedWithDoor(true);
            }

            var0.setCollidedObject(var4);
            return true;
         }
      }

      return false;
   }

   public boolean testCollideAdjacent(IsoMovingObject var1, int var2, int var3, int var4) {
      if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isNoClip()) {
         return false;
      } else if (this.collideMatrix == -1) {
         return true;
      } else if (var2 >= -1 && var2 <= 1 && var3 >= -1 && var3 <= 1 && var4 >= -1 && var4 <= 1) {
         if (this.x + var2 >= 0 && this.y + var3 >= 0 && IsoWorld.instance.MetaGrid.isValidChunk((this.x + var2) / 10, (this.y + var3) / 10)) {
            IsoGridSquare var5 = this.getCell().getGridSquare(this.x + var2, this.y + var3, this.z + var4);
            SafeHouse var6 = null;
            if ((GameServer.bServer || GameClient.bClient) && var1 instanceof IsoPlayer && !ServerOptions.instance.SafehouseAllowTrepass.getValue()) {
               IsoGridSquare var7 = this.getCell().getGridSquare(this.x + var2, this.y + var3, 0);
               var6 = SafeHouse.isSafeHouse(var7, ((IsoPlayer)var1).getUsername(), true);
            }

            if (var6 != null) {
               return true;
            } else {
               if (var5 != null && var1 != null) {
                  IsoObject var8 = this.testCollideSpecialObjects(var5);
                  if (var8 != null) {
                     var1.collideWith(var8);
                     if (var8 instanceof IsoDoor) {
                        var1.setCollidedWithDoor(true);
                     } else if (var8 instanceof IsoThumpable && ((IsoThumpable)var8).isDoor) {
                        var1.setCollidedWithDoor(true);
                     }

                     var1.setCollidedObject(var8);
                     return true;
                  }
               }

               if (UseSlowCollision) {
                  return this.CalculateCollide(var5, false, false, false);
               } else {
                  if (var1 instanceof IsoPlayer && getMatrixBit(this.collideMatrix, var2 + 1, var3 + 1, var4 + 1)) {
                     this.RecalcAllWithNeighbours(true);
                  }

                  return getMatrixBit(this.collideMatrix, var2 + 1, var3 + 1, var4 + 1);
               }
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public boolean testCollideAdjacentAdvanced(int var1, int var2, int var3, boolean var4) {
      if (this.collideMatrix == -1) {
         return true;
      } else if (var1 >= -1 && var1 <= 1 && var2 >= -1 && var2 <= 1 && var3 >= -1 && var3 <= 1) {
         IsoGridSquare var5 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z + var3);
         if (var5 != null) {
            int var6;
            IsoObject var7;
            if (!var5.SpecialObjects.isEmpty()) {
               for(var6 = 0; var6 < var5.SpecialObjects.size(); ++var6) {
                  var7 = (IsoObject)var5.SpecialObjects.get(var6);
                  if (var7.TestCollide((IsoMovingObject)null, this, var5)) {
                     return true;
                  }
               }
            }

            if (!this.SpecialObjects.isEmpty()) {
               for(var6 = 0; var6 < this.SpecialObjects.size(); ++var6) {
                  var7 = (IsoObject)this.SpecialObjects.get(var6);
                  if (var7.TestCollide((IsoMovingObject)null, this, var5)) {
                     return true;
                  }
               }
            }
         }

         return UseSlowCollision ? this.CalculateCollide(var5, false, false, false) : getMatrixBit(this.collideMatrix, var1 + 1, var2 + 1, var3 + 1);
      } else {
         return true;
      }
   }

   public static void setCollisionMode() {
      UseSlowCollision = !UseSlowCollision;
   }

   public boolean testPathFindAdjacent(IsoMovingObject var1, int var2, int var3, int var4) {
      return this.testPathFindAdjacent(var1, var2, var3, var4, cellGetSquare);
   }

   public boolean testPathFindAdjacent(IsoMovingObject var1, int var2, int var3, int var4, IsoGridSquare.GetSquare var5) {
      if (var2 >= -1 && var2 <= 1 && var3 >= -1 && var3 <= 1 && var4 >= -1 && var4 <= 1) {
         IsoGridSquare var6;
         if (this.Has(IsoObjectType.stairsTN) || this.Has(IsoObjectType.stairsTW)) {
            var6 = var5.getGridSquare(var2 + this.x, var3 + this.y, var4 + this.z);
            if (var6 == null) {
               return true;
            }

            if (this.Has(IsoObjectType.stairsTN) && var6.y < this.y && var6.z == this.z) {
               return true;
            }

            if (this.Has(IsoObjectType.stairsTW) && var6.x < this.x && var6.z == this.z) {
               return true;
            }
         }

         if (bDoSlowPathfinding) {
            var6 = var5.getGridSquare(var2 + this.x, var3 + this.y, var4 + this.z);
            return this.CalculateCollide(var6, false, true, false, false, var5);
         } else {
            return getMatrixBit(this.pathMatrix, var2 + 1, var3 + 1, var4 + 1);
         }
      } else {
         return true;
      }
   }

   public LosUtil.TestResults testVisionAdjacent(int var1, int var2, int var3, boolean var4, boolean var5) {
      if (var1 >= -1 && var1 <= 1 && var2 >= -1 && var2 <= 1 && var3 >= -1 && var3 <= 1) {
         IsoGridSquare var6;
         if (var3 == 1 && (var1 != 0 || var2 != 0) && this.HasElevatedFloor()) {
            var6 = this.getCell().getGridSquare(this.x, this.y, this.z + var3);
            if (var6 != null) {
               return var6.testVisionAdjacent(var1, var2, 0, var4, var5);
            }
         }

         if (var3 == -1 && (var1 != 0 || var2 != 0)) {
            var6 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z + var3);
            if (var6 != null && var6.HasElevatedFloor()) {
               return this.testVisionAdjacent(var1, var2, 0, var4, var5);
            }
         }

         LosUtil.TestResults var12 = LosUtil.TestResults.Clear;
         IsoGridSquare var7;
         if (var1 != 0 && var2 != 0 && var4) {
            var12 = this.DoDiagnalCheck(var1, var2, var3, var5);
            if (var12 == LosUtil.TestResults.Clear || var12 == LosUtil.TestResults.ClearThroughWindow || var12 == LosUtil.TestResults.ClearThroughOpenDoor || var12 == LosUtil.TestResults.ClearThroughClosedDoor) {
               var7 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z + var3);
               if (var7 != null) {
                  var12 = var7.DoDiagnalCheck(-var1, -var2, -var3, var5);
               }
            }

            return var12;
         } else {
            var7 = this.getCell().getGridSquare(this.x + var1, this.y + var2, this.z + var3);
            LosUtil.TestResults var8 = LosUtil.TestResults.Clear;
            if (var7 != null && var7.z == this.z) {
               int var9;
               IsoObject var10;
               IsoObject.VisionResult var11;
               if (!this.SpecialObjects.isEmpty()) {
                  for(var9 = 0; var9 < this.SpecialObjects.size(); ++var9) {
                     var10 = (IsoObject)this.SpecialObjects.get(var9);
                     if (var10 == null) {
                        return LosUtil.TestResults.Clear;
                     }

                     var11 = var10.TestVision(this, var7);
                     if (var11 != IsoObject.VisionResult.NoEffect) {
                        if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoDoor) {
                           var8 = ((IsoDoor)var10).IsOpen() ? LosUtil.TestResults.ClearThroughOpenDoor : LosUtil.TestResults.ClearThroughClosedDoor;
                        } else if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isDoor) {
                           var8 = LosUtil.TestResults.ClearThroughOpenDoor;
                        } else if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoWindow) {
                           var8 = LosUtil.TestResults.ClearThroughWindow;
                        } else {
                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoDoor && !var5) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isDoor && !var5) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isWindow()) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoCurtain) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoWindow) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoBarricade) {
                              return LosUtil.TestResults.Blocked;
                           }
                        }
                     }
                  }
               }

               if (!var7.SpecialObjects.isEmpty()) {
                  for(var9 = 0; var9 < var7.SpecialObjects.size(); ++var9) {
                     var10 = (IsoObject)var7.SpecialObjects.get(var9);
                     if (var10 == null) {
                        return LosUtil.TestResults.Clear;
                     }

                     var11 = var10.TestVision(this, var7);
                     if (var11 != IsoObject.VisionResult.NoEffect) {
                        if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoDoor) {
                           var8 = ((IsoDoor)var10).IsOpen() ? LosUtil.TestResults.ClearThroughOpenDoor : LosUtil.TestResults.ClearThroughClosedDoor;
                        } else if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isDoor) {
                           var8 = LosUtil.TestResults.ClearThroughOpenDoor;
                        } else if (var11 == IsoObject.VisionResult.Unblocked && var10 instanceof IsoWindow) {
                           var8 = LosUtil.TestResults.ClearThroughWindow;
                        } else {
                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoDoor && !var5) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isDoor && !var5) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoThumpable && ((IsoThumpable)var10).isWindow()) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoCurtain) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoWindow) {
                              return LosUtil.TestResults.Blocked;
                           }

                           if (var11 == IsoObject.VisionResult.Blocked && var10 instanceof IsoBarricade) {
                              return LosUtil.TestResults.Blocked;
                           }
                        }
                     }
                  }
               }
            }

            return !getMatrixBit(this.visionMatrix, var1 + 1, var2 + 1, var3 + 1) ? var8 : LosUtil.TestResults.Blocked;
         }
      } else {
         return LosUtil.TestResults.Blocked;
      }
   }

   public boolean TreatAsSolidFloor() {
      if (this.SolidFloorCached) {
         return this.SolidFloor;
      } else {
         if (!this.Properties.Is(IsoFlagType.solidfloor) && !this.HasStairs()) {
            this.SolidFloor = false;
         } else {
            this.SolidFloor = true;
         }

         this.SolidFloorCached = true;
         return this.SolidFloor;
      }
   }

   public void AddSpecialTileObject(IsoObject var1) {
      this.AddSpecialObject(var1);
   }

   public void renderCharacters(int var1, boolean var2, boolean var3) {
      if (this.z < var1) {
         if (!isOnScreenLast) {
         }

         if (var3) {
            IndieGL.glBlendFunc(770, 771);
         }

         if (this.MovingObjects.size() > 1) {
            Collections.sort(this.MovingObjects, comp);
         }

         int var4 = IsoCamera.frameState.playerIndex;
         ColorInfo var5 = this.lightInfo[var4];
         int var6 = this.StaticMovingObjects.size();

         int var7;
         IsoMovingObject var8;
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = (IsoMovingObject)this.StaticMovingObjects.get(var7);
            if ((var8.sprite != null || var8 instanceof IsoDeadBody) && (!var2 || var8 instanceof IsoDeadBody && !this.HasStairs()) && (var2 || !(var8 instanceof IsoDeadBody) || this.HasStairs())) {
               var8.render(var8.getX(), var8.getY(), var8.getZ(), var5, true, false, (Shader)null);
            }
         }

         var6 = this.MovingObjects.size();

         for(var7 = 0; var7 < var6; ++var7) {
            var8 = (IsoMovingObject)this.MovingObjects.get(var7);
            if (var8 != null && var8.sprite != null) {
               boolean var9 = var8.bOnFloor;
               if (var9 && var8 instanceof IsoZombie) {
                  IsoZombie var10 = (IsoZombie)var8;
                  var9 = var10.isProne();
                  if (!BaseVehicle.RENDER_TO_TEXTURE) {
                     var9 = false;
                  }
               }

               if ((!var2 || var9) && (var2 || !var9)) {
                  var8.render(var8.getX(), var8.getY(), var8.getZ(), var5, true, false, (Shader)null);
               }
            }
         }

      }
   }

   public void renderDeferredCharacters(int var1) {
      if (!this.DeferedCharacters.isEmpty()) {
         if (this.DeferredCharacterTick != this.getCell().DeferredCharacterTick) {
            this.DeferedCharacters.clear();
         } else if (this.z >= var1) {
            this.DeferedCharacters.clear();
         } else if (PerformanceSettings.LightingFrameSkip != 3) {
            short var2 = this.getCell().getStencilValue2z(this.x, this.y, this.z - 1);
            this.getCell().setStencilValue2z(this.x, this.y, this.z - 1, var2);
            IndieGL.enableAlphaTest();
            IndieGL.glAlphaFunc(516, 0.0F);
            IndieGL.glStencilFunc(519, var2, 127);
            IndieGL.glStencilOp(7680, 7680, 7681);
            float var3 = IsoUtils.XToScreen((float)this.x, (float)this.y, (float)this.z, 0);
            float var4 = IsoUtils.YToScreen((float)this.x, (float)this.y, (float)this.z, 0);
            var3 -= IsoCamera.frameState.OffX;
            var4 -= IsoCamera.frameState.OffY;
            IndieGL.glColorMask(false, false, false, false);
            Texture.getWhite().renderwallnw(var3, var4, (float)(64 * Core.TileScale), (float)(32 * Core.TileScale), -1, -1, -1, -1, -1, -1);
            IndieGL.glColorMask(true, true, true, true);
            IndieGL.enableAlphaTest();
            IndieGL.glAlphaFunc(516, 0.0F);
            IndieGL.glStencilFunc(514, var2, 127);
            IndieGL.glStencilOp(7680, 7680, 7680);
            ColorInfo var5 = this.lightInfo[IsoCamera.frameState.playerIndex];
            Collections.sort(this.DeferedCharacters, comp);

            for(int var6 = 0; var6 < this.DeferedCharacters.size(); ++var6) {
               IsoGameCharacter var7 = (IsoGameCharacter)this.DeferedCharacters.get(var6);
               if (var7.sprite != null) {
                  var7.setbDoDefer(false);
                  var7.render(var7.getX(), var7.getY(), var7.getZ(), var5, true, false, (Shader)null);
                  var7.renderObjectPicker(var7.getX(), var7.getY(), var7.getZ(), var5);
                  var7.setbDoDefer(true);
               }
            }

            this.DeferedCharacters.clear();
            IndieGL.glAlphaFunc(516, 0.0F);
            IndieGL.glStencilFunc(519, 1, 255);
            IndieGL.glStencilOp(7680, 7680, 7680);
         }
      }
   }

   public void switchLight(boolean var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3 instanceof IsoLightSwitch) {
            ((IsoLightSwitch)var3).setActive(var1);
         }
      }

   }

   public boolean IsOnScreen() {
      return this.IsOnScreen(false);
   }

   public boolean IsOnScreen(boolean var1) {
      if (this.CachedScreenValue != Core.TileScale) {
         this.CachedScreenX = IsoUtils.XToScreen((float)this.x, (float)this.y, (float)this.z, 0);
         this.CachedScreenY = IsoUtils.YToScreen((float)this.x, (float)this.y, (float)this.z, 0);
         this.CachedScreenValue = Core.TileScale;
      }

      float var2 = this.CachedScreenX;
      float var3 = this.CachedScreenY;
      var2 -= IsoCamera.frameState.OffX;
      var3 -= IsoCamera.frameState.OffY;
      int var4 = var1 ? 32 * Core.TileScale : 0;
      if (this.hasTree) {
         int var5 = 384 * Core.TileScale / 2 - 96 * Core.TileScale;
         int var6 = 256 * Core.TileScale - 32 * Core.TileScale;
         if (var2 + (float)var5 <= (float)(0 - var4)) {
            return false;
         } else if (var3 + (float)(32 * Core.TileScale) <= (float)(0 - var4)) {
            return false;
         } else if (var2 - (float)var5 >= (float)(IsoCamera.frameState.OffscreenWidth + var4)) {
            return false;
         } else {
            return !(var3 - (float)var6 >= (float)(IsoCamera.frameState.OffscreenHeight + var4));
         }
      } else if (var2 + (float)(32 * Core.TileScale) <= (float)(0 - var4)) {
         return false;
      } else if (var3 + (float)(32 * Core.TileScale) <= (float)(0 - var4)) {
         return false;
      } else if (var2 - (float)(32 * Core.TileScale) >= (float)(IsoCamera.frameState.OffscreenWidth + var4)) {
         return false;
      } else {
         return !(var3 - (float)(96 * Core.TileScale) >= (float)(IsoCamera.frameState.OffscreenHeight + var4));
      }
   }

   void cacheLightInfo() {
      int var1 = IsoCamera.frameState.playerIndex;
      this.lightInfo[var1] = this.lighting[var1].lightInfo();
   }

   public void setLightInfoServerGUIOnly(ColorInfo var1) {
      this.lightInfo[0] = var1;
   }

   int renderFloor(Shader var1) {
      int var2;
      try {
         IsoGridSquare.s_performance.renderFloor.start();
         var2 = this.renderFloorInternal(var1);
      } finally {
         IsoGridSquare.s_performance.renderFloor.end();
      }

      return var2;
   }

   private int renderFloorInternal(Shader var1) {
      int var2 = IsoCamera.frameState.playerIndex;
      ColorInfo var3 = this.lightInfo[var2];
      IsoGridSquare var4 = IsoCamera.frameState.CamCharacterSquare;
      boolean var5 = this.lighting[var2].bCouldSee();
      float var6 = this.lighting[var2].darkMulti();
      boolean var7 = GameClient.bClient && IsoPlayer.players[var2] != null && IsoPlayer.players[var2].isSeeNonPvpZone();
      boolean var8 = Core.bDebug && GameClient.bClient && SafeHouse.isSafeHouse(this, (String)null, true) != null;
      boolean var9 = true;
      float var10 = 1.0F;
      float var11 = 1.0F;
      if (var4 != null) {
         int var12 = this.getRoomID();
         if (var12 != -1) {
            int var13 = IsoWorld.instance.CurrentCell.GetEffectivePlayerRoomId();
            if (var13 == -1 && IsoWorld.instance.CurrentCell.CanBuildingSquareOccludePlayer(this, var2)) {
               var9 = false;
               var10 = 1.0F;
               var11 = 1.0F;
            } else if (!var5 && var12 != var13 && var6 < 0.5F) {
               var9 = false;
               var10 = 0.0F;
               var11 = var6 * 2.0F;
            }
         }
      }

      IsoWaterGeometry var30 = this.z == 0 ? this.getWater() : null;
      boolean var31 = var30 != null && var30.bShore;
      float var14 = var30 == null ? 0.0F : var30.depth[0];
      float var15 = var30 == null ? 0.0F : var30.depth[3];
      float var16 = var30 == null ? 0.0F : var30.depth[2];
      float var17 = var30 == null ? 0.0F : var30.depth[1];
      int var18 = 0;
      int var19 = this.Objects.size();
      IsoObject[] var20 = (IsoObject[])this.Objects.getElements();

      int var21;
      for(var21 = 0; var21 < var19; ++var21) {
         IsoObject var22 = var20[var21];
         if (var7 && (var22.highlightFlags & 1) == 0) {
            var22.setHighlighted(true);
            if (NonPvpZone.getNonPvpZone(this.x, this.y) != null) {
               var22.setHighlightColor(0.6F, 0.6F, 1.0F, 0.5F);
            } else {
               var22.setHighlightColor(1.0F, 0.6F, 0.6F, 0.5F);
            }
         }

         if (var8) {
            var22.setHighlighted(true);
            var22.setHighlightColor(1.0F, 0.0F, 0.0F, 1.0F);
         }

         boolean var23 = true;
         if (var22.sprite != null && !var22.sprite.solidfloor && var22.sprite.renderLayer != 1) {
            var23 = false;
            var18 |= 4;
         }

         if (var22 instanceof IsoFire || var22 instanceof IsoCarBatteryCharger) {
            var23 = false;
            var18 |= 4;
         }

         if (!var23) {
            boolean var24 = var22.sprite != null && (var22.sprite.isBush || var22.sprite.canBeRemoved || var22.sprite.attachedFloor);
            if (this.bFlattenGrassEtc && var24) {
               var18 |= 2;
            }
         } else {
            IndieGL.glAlphaFunc(516, 0.0F);
            var22.setTargetAlpha(var2, var11);
            if (var9) {
               var22.setAlpha(var2, var10);
            }

            if (DebugOptions.instance.Terrain.RenderTiles.RenderGridSquares.getValue() && var22.sprite != null) {
               IndieGL.StartShader(var1, var2);
               FloorShaperAttachedSprites var25 = FloorShaperAttachedSprites.instance;
               Object var34;
               if (!var22.getProperties().Is(IsoFlagType.diamondFloor) && !var22.getProperties().Is(IsoFlagType.water)) {
                  var34 = FloorShaperDeDiamond.instance;
               } else {
                  var34 = FloorShaperDiamond.instance;
               }

               int var26 = this.getVertLight(0, var2);
               int var27 = this.getVertLight(1, var2);
               int var28 = this.getVertLight(2, var2);
               int var29 = this.getVertLight(3, var2);
               if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Floor.LightingDebug.getValue()) {
                  var26 = -65536;
                  var27 = -65536;
                  var28 = -16776961;
                  var29 = -16776961;
               }

               var25.setShore(var31);
               var25.setWaterDepth(var14, var15, var16, var17);
               var25.setVertColors(var26, var27, var28, var29);
               ((FloorShaper)var34).setShore(var31);
               ((FloorShaper)var34).setWaterDepth(var14, var15, var16, var17);
               ((FloorShaper)var34).setVertColors(var26, var27, var28, var29);
               var22.renderFloorTile((float)this.x, (float)this.y, (float)this.z, PerformanceSettings.LightingFrameSkip < 3 ? defColorInfo : var3, true, false, var1, (Consumer)var34, var25);
               IndieGL.StartShader((Shader)null);
            }

            var18 |= 1;
            if ((var22.highlightFlags & 1) == 0) {
               var18 |= 8;
            }

            if ((var22.highlightFlags & 2) != 0) {
               var22.highlightFlags &= -4;
            }
         }
      }

      if ((this.getCell().rainIntensity > 0 || RainManager.isRaining() && RainManager.RainIntensity > 0.0F) && this.isExteriorCache && !this.isVegitationCache && this.isSolidFloorCache && this.isCouldSee(var2)) {
         if (!IsoCamera.frameState.Paused) {
            var21 = this.getCell().rainIntensity == 0 ? (int)Math.min(Math.floor((double)(RainManager.RainIntensity / 0.2F)) + 1.0D, 5.0D) : this.getCell().rainIntensity;
            if (this.splashFrame < 0.0F && Rand.Next(Rand.AdjustForFramerate((int)(5.0F / (float)var21) * 100)) == 0) {
               this.splashFrame = 0.0F;
            }
         }

         if (this.splashFrame >= 0.0F) {
            var21 = (int)(this.splashFrame * 4.0F);
            if (rainsplashCache[var21] == null) {
               rainsplashCache[var21] = "RainSplash_00_" + var21;
            }

            Texture var32 = Texture.getSharedTexture(rainsplashCache[var21]);
            if (var32 != null) {
               float var33 = IsoUtils.XToScreen((float)this.x + this.splashX, (float)this.y + this.splashY, (float)this.z, 0) - IsoCamera.frameState.OffX;
               float var35 = IsoUtils.YToScreen((float)this.x + this.splashX, (float)this.y + this.splashY, (float)this.z, 0) - IsoCamera.frameState.OffY;
               var33 -= (float)(var32.getWidth() / 2 * Core.TileScale);
               var35 -= (float)(var32.getHeight() / 2 * Core.TileScale);
               float var36 = 0.6F * (this.getCell().rainIntensity > 0 ? 1.0F : RainManager.RainIntensity);
               float var37 = Core.getInstance().RenderShader != null ? 0.6F : 1.0F;
               SpriteRenderer.instance.render(var32, var33, var35, (float)(var32.getWidth() * Core.TileScale), (float)(var32.getHeight() * Core.TileScale), 0.8F * var3.r, 0.9F * var3.g, 1.0F * var3.b, var36 * var37, (Consumer)null);
            }

            if (!IsoCamera.frameState.Paused && this.splashFrameNum != IsoCamera.frameState.frameCount) {
               this.splashFrame += 0.08F * (30.0F / (float)PerformanceSettings.getLockFPS());
               if (this.splashFrame >= 1.0F) {
                  this.splashX = Rand.Next(0.1F, 0.9F);
                  this.splashY = Rand.Next(0.1F, 0.9F);
                  this.splashFrame = -1.0F;
               }

               this.splashFrameNum = IsoCamera.frameState.frameCount;
            }
         }
      } else {
         this.splashFrame = -1.0F;
      }

      return var18;
   }

   private boolean isSpriteOnSouthOrEastWall(IsoObject var1) {
      if (var1 instanceof IsoBarricade) {
         return var1.getDir() == IsoDirections.S || var1.getDir() == IsoDirections.E;
      } else if (var1 instanceof IsoCurtain) {
         IsoCurtain var3 = (IsoCurtain)var1;
         return var3.getType() == IsoObjectType.curtainS || var3.getType() == IsoObjectType.curtainE;
      } else {
         PropertyContainer var2 = var1.getProperties();
         return var2 != null && (var2.Is(IsoFlagType.attachedE) || var2.Is(IsoFlagType.attachedS));
      }
   }

   public void RenderOpenDoorOnly() {
      int var1 = this.Objects.size();
      IsoObject[] var2 = (IsoObject[])this.Objects.getElements();

      try {
         byte var3 = 0;
         int var4 = var1 - 1;

         for(int var5 = var3; var5 <= var4; ++var5) {
            IsoObject var6 = var2[var5];
            if (var6.sprite != null && (var6.sprite.getProperties().Is(IsoFlagType.attachedN) || var6.sprite.getProperties().Is(IsoFlagType.attachedW))) {
               var6.renderFxMask((float)this.x, (float)this.y, (float)this.z, false);
            }
         }
      } catch (Exception var7) {
         ExceptionLogger.logException(var7);
      }

   }

   public boolean RenderMinusFloorFxMask(int var1, boolean var2, boolean var3) {
      boolean var4 = false;
      int var5 = this.Objects.size();
      IsoObject[] var6 = (IsoObject[])this.Objects.getElements();
      long var7 = System.currentTimeMillis();

      try {
         int var9 = var2 ? var5 - 1 : 0;
         int var10 = var2 ? 0 : var5 - 1;
         int var11 = var9;

         while(true) {
            if (var2) {
               if (var11 < var10) {
                  break;
               }
            } else if (var11 > var10) {
               break;
            }

            IsoObject var12 = var6[var11];
            if (var12.sprite != null) {
               boolean var13 = true;
               IsoObjectType var14 = var12.sprite.getType();
               if (var12.sprite.solidfloor || var12.sprite.renderLayer == 1) {
                  var13 = false;
               }

               if (this.z >= var1 && !var12.sprite.alwaysDraw) {
                  var13 = false;
               }

               boolean var15 = var12.sprite.isBush || var12.sprite.canBeRemoved || var12.sprite.attachedFloor;
               if ((!var3 || var15 && this.bFlattenGrassEtc) && (var3 || !var15 || !this.bFlattenGrassEtc)) {
                  if ((var14 == IsoObjectType.WestRoofB || var14 == IsoObjectType.WestRoofM || var14 == IsoObjectType.WestRoofT) && this.z == var1 - 1 && this.z == (int)IsoCamera.CamCharacter.getZ()) {
                     var13 = false;
                  }

                  if (this.isSpriteOnSouthOrEastWall(var12)) {
                     if (!var2) {
                        var13 = false;
                     }

                     var4 = true;
                  } else if (var2) {
                     var13 = false;
                  }

                  if (var13) {
                     if (!var12.sprite.cutW && !var12.sprite.cutN) {
                        var12.renderFxMask((float)this.x, (float)this.y, (float)this.z, false);
                     } else {
                        int var16 = IsoCamera.frameState.playerIndex;
                        boolean var17 = var12.sprite.cutN;
                        boolean var18 = var12.sprite.cutW;
                        IsoGridSquare var19 = this.nav[IsoDirections.S.index()];
                        IsoGridSquare var20 = this.nav[IsoDirections.E.index()];
                        boolean var21 = var19 != null && var19.getPlayerCutawayFlag(var16, var7);
                        boolean var22 = this.getPlayerCutawayFlag(var16, var7);
                        boolean var23 = var20 != null && var20.getPlayerCutawayFlag(var16, var7);
                        IsoDirections var24;
                        if (var17 && var18) {
                           var24 = IsoDirections.NW;
                        } else if (var17) {
                           var24 = IsoDirections.N;
                        } else if (var18) {
                           var24 = IsoDirections.W;
                        } else {
                           var24 = IsoDirections.W;
                        }

                        this.DoCutawayShaderSprite(var12.sprite, var24, var21, var22, var23);
                     }
                  }
               }
            }

            var11 += var2 ? -1 : 1;
         }
      } catch (Exception var25) {
         ExceptionLogger.logException(var25);
      }

      return var4;
   }

   private boolean isWindowOrWindowFrame(IsoObject var1, boolean var2) {
      if (var1 != null && var1.sprite != null) {
         if (var2 && var1.sprite.getProperties().Is(IsoFlagType.windowN)) {
            return true;
         } else if (!var2 && var1.sprite.getProperties().Is(IsoFlagType.windowW)) {
            return true;
         } else {
            IsoThumpable var3 = (IsoThumpable)Type.tryCastTo(var1, IsoThumpable.class);
            if (var3 != null && var3.isWindow()) {
               return var2 == var3.getNorth();
            } else {
               return IsoWindowFrame.isWindowFrame(var1, var2);
            }
         }
      } else {
         return false;
      }
   }

   boolean renderMinusFloor(int var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, Shader var7) {
      boolean var8 = false;
      if (!this.localTemporaryObjects.isEmpty()) {
         var8 = this.renderMinusFloor(this.localTemporaryObjects, var1, var2, var3, var4, var5, var6, var7);
      }

      return this.renderMinusFloor(this.Objects, var1, var2, var3, var4, var5, var6, var7) || var8;
   }

   boolean renderMinusFloor(PZArrayList var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, Shader var8) {
      if (!DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.RenderMinusFloor.getValue()) {
         return false;
      } else {
         IndieGL.glBlendFunc(770, 771);
         int var9 = 0;
         isOnScreenLast = this.IsOnScreen();
         int var10 = IsoCamera.frameState.playerIndex;
         IsoGridSquare var11 = IsoCamera.frameState.CamCharacterSquare;
         ColorInfo var12 = this.lightInfo[var10];
         boolean var13 = this.lighting[var10].bCouldSee();
         float var14 = this.lighting[var10].darkMulti();
         boolean var15 = IsoWorld.instance.CurrentCell.CanBuildingSquareOccludePlayer(this, var10);
         var12.a = 1.0F;
         defColorInfo.r = 1.0F;
         defColorInfo.g = 1.0F;
         defColorInfo.b = 1.0F;
         defColorInfo.a = 1.0F;
         if (Core.bDebug && DebugOptions.instance.DebugDraw_SkipWorldShading.getValue()) {
            var12 = defColorInfo;
         }

         float var16 = this.CachedScreenX - IsoCamera.frameState.OffX;
         float var17 = this.CachedScreenY - IsoCamera.frameState.OffY;
         boolean var18 = true;
         IsoCell var19 = this.getCell();
         if (var16 + (float)(32 * Core.TileScale) <= (float)var19.StencilX1 || var16 - (float)(32 * Core.TileScale) >= (float)var19.StencilX2 || var17 + (float)(32 * Core.TileScale) <= (float)var19.StencilY1 || var17 - (float)(96 * Core.TileScale) >= (float)var19.StencilY2) {
            var18 = false;
         }

         boolean var20 = false;
         int var21 = var1.size();
         IsoObject[] var22 = (IsoObject[])var1.getElements();
         int var23 = var3 ? var21 - 1 : 0;
         int var24 = var3 ? 0 : var21 - 1;
         boolean var25 = false;
         boolean var26 = false;
         boolean var27 = false;
         boolean var28 = false;
         int var29;
         if (!var3) {
            for(var29 = var23; var29 <= var24; ++var29) {
               IsoObject var30 = var22[var29];
               IsoGridSquare var31;
               if (this.isWindowOrWindowFrame(var30, true) && (var6 || var7)) {
                  var31 = this.nav[IsoDirections.N.index()];
                  var27 = var13 || var31 != null && var31.isCouldSee(var10);
               }

               if (this.isWindowOrWindowFrame(var30, false) && (var6 || var5)) {
                  var31 = this.nav[IsoDirections.W.index()];
                  var28 = var13 || var31 != null && var31.isCouldSee(var10);
               }

               if (var30.sprite != null && (var30.sprite.getType() == IsoObjectType.doorFrN || var30.sprite.getType() == IsoObjectType.doorN) && (var6 || var7)) {
                  var31 = this.nav[IsoDirections.N.index()];
                  var25 = var13 || var31 != null && var31.isCouldSee(var10);
               }

               if (var30.sprite != null && (var30.sprite.getType() == IsoObjectType.doorFrW || var30.sprite.getType() == IsoObjectType.doorW) && (var6 || var5)) {
                  var31 = this.nav[IsoDirections.W.index()];
                  var26 = var13 || var31 != null && var31.isCouldSee(var10);
               }
            }
         }

         var29 = IsoWorld.instance.CurrentCell.GetEffectivePlayerRoomId();
         bWallCutawayN = false;
         bWallCutawayW = false;
         int var47 = var23;

         while(true) {
            if (var3) {
               if (var47 < var24) {
                  break;
               }
            } else if (var47 > var24) {
               break;
            }

            IsoObject var48 = var22[var47];
            boolean var32 = true;
            IsoObjectType var33 = IsoObjectType.MAX;
            if (var48.sprite != null) {
               var33 = var48.sprite.getType();
            }

            CircleStencil = false;
            if (var48.sprite != null && (var48.sprite.solidfloor || var48.sprite.renderLayer == 1)) {
               var32 = false;
            }

            if (var48 instanceof IsoFire) {
               var32 = !var4;
            }

            if (this.z >= var2 && (var48.sprite == null || !var48.sprite.alwaysDraw)) {
               var32 = false;
            }

            boolean var34 = var48.sprite != null && (var48.sprite.isBush || var48.sprite.canBeRemoved || var48.sprite.attachedFloor);
            if ((!var4 || var34 && this.bFlattenGrassEtc) && (var4 || !var34 || !this.bFlattenGrassEtc)) {
               if (var48.sprite != null && (var33 == IsoObjectType.WestRoofB || var33 == IsoObjectType.WestRoofM || var33 == IsoObjectType.WestRoofT) && this.z == var2 - 1 && this.z == (int)IsoCamera.CamCharacter.getZ()) {
                  var32 = false;
               }

               boolean var35 = var33 == IsoObjectType.doorFrW || var33 == IsoObjectType.doorW || var48.sprite != null && var48.sprite.cutW;
               boolean var36 = var33 == IsoObjectType.doorFrN || var33 == IsoObjectType.doorN || var48.sprite != null && var48.sprite.cutN;
               boolean var37 = var48 instanceof IsoDoor && ((IsoDoor)var48).open || var48 instanceof IsoThumpable && ((IsoThumpable)var48).open;
               boolean var38 = var48.container != null;
               boolean var39 = var48.sprite != null && var48.sprite.getProperties().Is(IsoFlagType.waterPiped);
               if (var48.sprite != null && var33 == IsoObjectType.MAX && !(var48 instanceof IsoDoor) && !(var48 instanceof IsoWindow) && !var38 && !var39) {
                  if (!var35 && var48.sprite.getProperties().Is(IsoFlagType.attachedW) && (var15 || var5 || var6)) {
                     var32 = !bWallCutawayW;
                  } else if (!var36 && var48.sprite.getProperties().Is(IsoFlagType.attachedN) && (var15 || var6 || var7)) {
                     var32 = !bWallCutawayN;
                  }
               }

               if (var48.sprite != null && !var48.sprite.solidfloor && IsoPlayer.getInstance().isClimbing()) {
                  var32 = true;
               }

               if (this.isSpriteOnSouthOrEastWall(var48)) {
                  if (!var3) {
                     var32 = false;
                  }

                  var20 = true;
               } else if (var3) {
                  var32 = false;
               }

               if (var32) {
                  IndieGL.glAlphaFunc(516, 0.0F);
                  var48.bAlphaForced = false;
                  if (var37) {
                     var48.setTargetAlpha(var10, 0.6F);
                     var48.setAlpha(var10, 0.6F);
                  }

                  if (var48.sprite == null || !var35 && !var36) {
                     if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Objects.getValue()) {
                        if (this.getRoomID() != -1 && this.getRoomID() != var29 && IsoPlayer.players[var10].isSeatedInVehicle() && IsoPlayer.players[var10].getVehicle().getCurrentSpeedKmHour() >= 50.0F) {
                           break;
                        }

                        if (this.getRoomID() != -1 || !var7 && !var5 || var33 != IsoObjectType.WestRoofB && var33 != IsoObjectType.WestRoofM && var33 != IsoObjectType.WestRoofT) {
                           if (var11 != null && !var13 && this.getRoomID() != var29 && var14 < 0.5F) {
                              var48.setTargetAlpha(var10, var14 * 2.0F);
                           } else {
                              if (!var37) {
                                 var48.setTargetAlpha(var10, 1.0F);
                              }

                              if (IsoPlayer.getInstance() != null && var48.getProperties() != null && (var48.getProperties().Is(IsoFlagType.solid) || var48.getProperties().Is(IsoFlagType.solidtrans) || var48.getProperties().Is(IsoFlagType.attachedCeiling)) || var33.index() > 2 && var33.index() < 9 && IsoCamera.frameState.CamCharacterZ <= var48.getZ()) {
                                 byte var40 = 2;
                                 float var41 = 0.75F;
                                 if (var33.index() > 2 && var33.index() < 9) {
                                    var40 = 4;
                                    var41 = 0.5F;
                                 }

                                 int var42 = this.getX() - (int)IsoPlayer.getInstance().getX();
                                 int var43 = this.getY() - (int)IsoPlayer.getInstance().getY();
                                 if (var42 > 0 && var42 < var40 && var43 >= 0 && var43 < var40 || var43 > 0 && var43 < var40 && var42 >= 0 && var42 < var40) {
                                    var48.setTargetAlpha(var10, var41);
                                 }

                                 IsoZombie var44 = IsoCell.getInstance().getNearestVisibleZombie(var10);
                                 if (var44 != null && var44.getCurrentSquare() != null && var44.getCurrentSquare().isCanSee(var10)) {
                                    int var45 = this.getX() - (int)var44.x;
                                    int var46 = this.getY() - (int)var44.y;
                                    if (var45 > 0 && var45 < var40 && var46 >= 0 && var46 < var40 || var46 > 0 && var46 < var40 && var45 >= 0 && var45 < var40) {
                                       var48.setTargetAlpha(var10, var41);
                                    }
                                 }
                              }
                           }
                        } else {
                           var48.setTargetAlpha(var10, 0.0F);
                        }

                        if (var48 instanceof IsoWindow) {
                           IsoWindow var49 = (IsoWindow)var48;
                           if (var48.getTargetAlpha(var10) < 1.0E-4F) {
                              IsoGridSquare var50 = var49.getOppositeSquare();
                              if (var50 != null && var50 != this && var50.lighting[var10].bSeen()) {
                                 var48.setTargetAlpha(var10, var50.lighting[var10].darkMulti() * 2.0F);
                              }
                           }

                           if (var48.getTargetAlpha(var10) > 0.4F) {
                              if ((var6 || var7) && var48.sprite.getProperties().Is(IsoFlagType.windowN)) {
                                 var48.setTargetAlpha(var10, 0.4F);
                                 bWallCutawayN = true;
                              } else if ((var6 || var5) && var48.sprite.getProperties().Is(IsoFlagType.windowW)) {
                                 var48.setTargetAlpha(var10, 0.4F);
                                 bWallCutawayW = true;
                              }
                           }
                        }

                        if (var48 instanceof IsoTree) {
                           if (var18 && this.x >= (int)IsoCamera.frameState.CamCharacterX && this.y >= (int)IsoCamera.frameState.CamCharacterY && var11 != null && var11.Is(IsoFlagType.exterior)) {
                              ((IsoTree)var48).bRenderFlag = true;
                              var48.setTargetAlpha(var10, Math.min(0.99F, var48.getTargetAlpha(var10)));
                           } else {
                              ((IsoTree)var48).bRenderFlag = false;
                           }
                        }

                        var48.render((float)this.x, (float)this.y, (float)this.z, var12, true, false, (Shader)null);
                     }
                  } else if (PerformanceSettings.LightingFrameSkip < 3) {
                     if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.DoorsAndWalls.getValue()) {
                        CircleStencil = true;
                        if (var11 != null && this.getRoomID() != -1 && var29 == -1 && var15) {
                           var48.setTargetAlpha(var10, 0.5F);
                           var48.setAlpha(var10, 0.5F);
                        } else if (!var37) {
                           var48.setTargetAlpha(var10, 1.0F);
                           var48.setAlpha(var10, 1.0F);
                        }

                        var48.bAlphaForced = true;
                        if (var48.sprite.cutW && var48.sprite.cutN) {
                           var9 = this.DoWallLightingNW(var48, var9, var5, var6, var7, var25, var26, var27, var28, var8);
                        } else if (var48.sprite.getType() != IsoObjectType.doorFrW && var33 != IsoObjectType.doorW && !var48.sprite.cutW) {
                           if (var33 == IsoObjectType.doorFrN || var33 == IsoObjectType.doorN || var48.sprite.cutN) {
                              var9 = this.DoWallLightingN(var48, var9, var6, var7, var25, var27, var8);
                           }
                        } else {
                           var9 = this.DoWallLightingW(var48, var9, var5, var6, var26, var28, var8);
                        }

                        if (var48 instanceof IsoWindow && var48.getTargetAlpha(var10) < 1.0F) {
                           bWallCutawayN |= var48.sprite.cutN;
                           bWallCutawayW |= var48.sprite.cutW;
                        }
                     }
                  } else if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.DoorsAndWalls_SimpleLighting.getValue()) {
                     if (this.z != (int)IsoCamera.frameState.CamCharacterZ || var33 == IsoObjectType.doorFrW || var33 == IsoObjectType.doorFrN || var48 instanceof IsoWindow) {
                        var18 = false;
                     }

                     if (var48.getTargetAlpha(var10) < 1.0F) {
                        if (!var18) {
                           var48.setTargetAlpha(var10, 1.0F);
                        }

                        var48.setAlphaToTarget(var10);
                        IsoObject.LowLightingQualityHack = false;
                        var48.render((float)this.x, (float)this.y, (float)this.z, var12, true, false, (Shader)null);
                        if (!IsoObject.LowLightingQualityHack) {
                           var48.setTargetAlpha(var10, 1.0F);
                        }
                     } else {
                        var48.render((float)this.x, (float)this.y, (float)this.z, var12, true, false, (Shader)null);
                     }
                  }

                  if ((var48.highlightFlags & 2) != 0) {
                     var48.highlightFlags &= -4;
                  }
               }
            }

            var47 += var3 ? -1 : 1;
         }

         return var20;
      }
   }

   void RereouteWallMaskTo(IsoObject var1) {
      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (var3.sprite.getProperties().Is(IsoFlagType.collideW) || var3.sprite.getProperties().Is(IsoFlagType.collideN)) {
            var3.rerouteMask = var1;
         }
      }

   }

   void setBlockedGridPointers(IsoGridSquare.GetSquare var1) {
      this.w = var1.getGridSquare(this.x - 1, this.y, this.z);
      this.e = var1.getGridSquare(this.x + 1, this.y, this.z);
      this.s = var1.getGridSquare(this.x, this.y + 1, this.z);
      this.n = var1.getGridSquare(this.x, this.y - 1, this.z);
      this.ne = var1.getGridSquare(this.x + 1, this.y - 1, this.z);
      this.nw = var1.getGridSquare(this.x - 1, this.y - 1, this.z);
      this.se = var1.getGridSquare(this.x + 1, this.y + 1, this.z);
      this.sw = var1.getGridSquare(this.x - 1, this.y + 1, this.z);
      if (this.s != null && this.testPathFindAdjacent((IsoMovingObject)null, this.s.x - this.x, this.s.y - this.y, this.s.z - this.z, var1)) {
         this.s = null;
      }

      if (this.w != null && this.testPathFindAdjacent((IsoMovingObject)null, this.w.x - this.x, this.w.y - this.y, this.w.z - this.z, var1)) {
         this.w = null;
      }

      if (this.n != null && this.testPathFindAdjacent((IsoMovingObject)null, this.n.x - this.x, this.n.y - this.y, this.n.z - this.z, var1)) {
         this.n = null;
      }

      if (this.e != null && this.testPathFindAdjacent((IsoMovingObject)null, this.e.x - this.x, this.e.y - this.y, this.e.z - this.z, var1)) {
         this.e = null;
      }

      if (this.sw != null && this.testPathFindAdjacent((IsoMovingObject)null, this.sw.x - this.x, this.sw.y - this.y, this.sw.z - this.z, var1)) {
         this.sw = null;
      }

      if (this.se != null && this.testPathFindAdjacent((IsoMovingObject)null, this.se.x - this.x, this.se.y - this.y, this.se.z - this.z, var1)) {
         this.se = null;
      }

      if (this.nw != null && this.testPathFindAdjacent((IsoMovingObject)null, this.nw.x - this.x, this.nw.y - this.y, this.nw.z - this.z, var1)) {
         this.nw = null;
      }

      if (this.ne != null && this.testPathFindAdjacent((IsoMovingObject)null, this.ne.x - this.x, this.ne.y - this.y, this.ne.z - this.z, var1)) {
         this.ne = null;
      }

   }

   public IsoObject getContainerItem(String var1) {
      int var2 = this.getObjects().size();
      IsoObject[] var3 = (IsoObject[])this.getObjects().getElements();

      for(int var4 = 0; var4 < var2; ++var4) {
         IsoObject var5 = var3[var4];
         if (var5.getContainer() != null && var1.equals(var5.getContainer().getType())) {
            return var5;
         }
      }

      return null;
   }

   public void StartFire() {
      IsoFireManager.StartFire(this.getCell(), this, true, 100000);
   }

   public void explode() {
      IsoFireManager.explode(this.getCell(), this, 100000);
   }

   public int getHourLastSeen() {
      return this.hourLastSeen;
   }

   public float getHoursSinceLastSeen() {
      return (float)GameTime.instance.getWorldAgeHours() - (float)this.hourLastSeen;
   }

   public void CalcVisibility(int var1) {
      IsoPlayer var2 = IsoPlayer.players[var1];
      IsoGridSquare.ILighting var3 = this.lighting[var1];
      var3.bCanSee(false);
      var3.bCouldSee(false);
      if (GameServer.bServer || var2 != null && (!var2.isDead() || var2.ReanimatedCorpse != null)) {
         if (var2 != null) {
            IsoGameCharacter.LightInfo var4 = var2.getLightInfo2();
            IsoGridSquare var5 = var4.square;
            if (var5 != null) {
               IsoChunk var6 = this.getChunk();
               if (var6 != null) {
                  tempo.x = (float)this.x + 0.5F;
                  tempo.y = (float)this.y + 0.5F;
                  tempo2.x = var4.x;
                  tempo2.y = var4.y;
                  Vector2 var10000 = tempo2;
                  var10000.x -= tempo.x;
                  var10000 = tempo2;
                  var10000.y -= tempo.y;
                  Vector2 var7 = tempo;
                  float var8 = tempo2.getLength();
                  tempo2.normalize();
                  if (var2 instanceof IsoSurvivor) {
                     var2.setForwardDirection(var7);
                     var4.angleX = var7.x;
                     var4.angleY = var7.y;
                  }

                  var7.x = var4.angleX;
                  var7.y = var4.angleY;
                  var7.normalize();
                  float var9 = tempo2.dot(var7);
                  if (var5 == this) {
                     var9 = -1.0F;
                  }

                  float var11;
                  if (!GameServer.bServer) {
                     float var10 = var2.getStats().fatigue - 0.6F;
                     if (var10 < 0.0F) {
                        var10 = 0.0F;
                     }

                     var10 *= 2.5F;
                     if (var2.Traits.HardOfHearing.isSet() && var10 < 0.7F) {
                        var10 = 0.7F;
                     }

                     var11 = 2.0F;
                     if (var2.Traits.KeenHearing.isSet()) {
                        var11 += 3.0F;
                     }

                     if (var8 < var11 * (1.0F - var10) && !var2.Traits.Deaf.isSet()) {
                        var9 = -1.0F;
                     }
                  }

                  LosUtil.TestResults var16 = LosUtil.lineClearCached(this.getCell(), this.x, this.y, this.z, (int)var4.x, (int)var4.y, (int)var4.z, false, var1);
                  var11 = -0.2F;
                  var11 -= var2.getStats().fatigue - 0.6F;
                  if (var11 > -0.2F) {
                     var11 = -0.2F;
                  }

                  if (var2.getStats().fatigue >= 1.0F) {
                     var11 -= 0.2F;
                  }

                  if (var2.getMoodles().getMoodleLevel(MoodleType.Panic) == 4) {
                     var11 -= 0.2F;
                  }

                  if (var11 < -0.9F) {
                     var11 = -0.9F;
                  }

                  if (var2.Traits.EagleEyed.isSet()) {
                     var11 += 0.2F;
                  }

                  if (var2 instanceof IsoPlayer && var2.getVehicle() != null) {
                     var11 = 1.0F;
                  }

                  if (!(var9 > var11) && var16 != LosUtil.TestResults.Blocked) {
                     var3.bCouldSee(true);
                     if (this.room != null && this.room.def != null && !this.room.def.bExplored) {
                        byte var17 = 10;
                        if (var4.square != null && var4.square.getBuilding() == this.room.building) {
                           var17 = 50;
                        }

                        if ((!GameServer.bServer || !(var2 instanceof IsoPlayer) || !((IsoPlayer)var2).isGhostMode()) && IsoUtils.DistanceManhatten(var4.x, var4.y, (float)this.x, (float)this.y) < (float)var17 && this.z == (int)var4.z) {
                           if (GameServer.bServer) {
                              DebugLog.log(DebugType.Zombie, "bExplored room=" + this.room.def.ID);
                           }

                           this.room.def.bExplored = true;
                           this.room.onSee();
                           this.room.seen = 0;
                        }
                     }

                     if (!GameClient.bClient) {
                        Meta.instance.dealWithSquareSeen(this);
                     }

                     var3.bCanSee(true);
                     var3.bSeen(true);
                     var3.targetDarkMulti(1.0F);
                  } else {
                     if (var16 == LosUtil.TestResults.Blocked) {
                        var3.bCouldSee(false);
                     } else {
                        var3.bCouldSee(true);
                     }

                     if (!GameServer.bServer) {
                        if (var3.bSeen()) {
                           float var12 = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex());
                           if (!var3.bCouldSee()) {
                              var12 *= 0.5F;
                           } else {
                              var12 *= 0.94F;
                           }

                           if (this.room == null && var5.getRoom() == null) {
                              var3.targetDarkMulti(var12);
                           } else if (this.room != null && var5.getRoom() != null && this.room.building == var5.getRoom().building) {
                              if (this.room != var5.getRoom() && !var3.bCouldSee()) {
                                 var3.targetDarkMulti(0.0F);
                              } else {
                                 var3.targetDarkMulti(var12);
                              }
                           } else if (this.room == null) {
                              var3.targetDarkMulti(var12 / 2.0F);
                           } else if (var3.lampostTotalR() + var3.lampostTotalG() + var3.lampostTotalB() == 0.0F) {
                              var3.targetDarkMulti(0.0F);
                           }

                           if (this.room != null) {
                              var3.targetDarkMulti(var3.targetDarkMulti() * 0.7F);
                           }
                        } else {
                           var3.targetDarkMulti(0.0F);
                           var3.darkMulti(0.0F);
                        }
                     }
                  }

                  if (var9 > var11) {
                     var3.targetDarkMulti(var3.targetDarkMulti() * 0.85F);
                  }

                  if (!GameServer.bServer) {
                     for(int var18 = 0; var18 < var4.torches.size(); ++var18) {
                        IsoGameCharacter.TorchInfo var13 = (IsoGameCharacter.TorchInfo)var4.torches.get(var18);
                        tempo2.x = var13.x;
                        tempo2.y = var13.y;
                        var10000 = tempo2;
                        var10000.x -= (float)this.x + 0.5F;
                        var10000 = tempo2;
                        var10000.y -= (float)this.y + 0.5F;
                        var8 = tempo2.getLength();
                        tempo2.normalize();
                        var7.x = var13.angleX;
                        var7.y = var13.angleY;
                        var7.normalize();
                        var9 = tempo2.dot(var7);
                        if ((int)var13.x == this.getX() && (int)var13.y == this.getY() && (int)var13.z == this.getZ()) {
                           var9 = -1.0F;
                        }

                        boolean var14 = false;
                        if (IsoUtils.DistanceManhatten((float)this.getX(), (float)this.getY(), var13.x, var13.y) < var13.dist && (var13.bCone && var9 < -var13.dot || var9 == -1.0F || !var13.bCone && var9 < 0.8F)) {
                           var14 = true;
                        }

                        if ((var13.bCone && var8 < var13.dist || !var13.bCone && var8 < var13.dist) && var3.bCanSee() && var14 && this.z == (int)var2.getZ()) {
                           float var15 = var8 / var13.dist;
                           if (var15 > 1.0F) {
                              var15 = 1.0F;
                           }

                           if (var15 < 0.0F) {
                              var15 = 0.0F;
                           }

                           var3.targetDarkMulti(var3.targetDarkMulti() + var13.strength * (1.0F - var15) * 3.0F);
                           if (var3.targetDarkMulti() > 2.5F) {
                              var3.targetDarkMulti(2.5F);
                           }

                           torchTimer = var4.time;
                        }
                     }

                  }
               }
            }
         }
      } else {
         var3.bSeen(true);
         var3.bCanSee(true);
         var3.bCouldSee(true);
      }
   }

   private LosUtil.TestResults DoDiagnalCheck(int var1, int var2, int var3, boolean var4) {
      LosUtil.TestResults var5 = this.testVisionAdjacent(var1, 0, var3, false, var4);
      if (var5 == LosUtil.TestResults.Blocked) {
         return LosUtil.TestResults.Blocked;
      } else {
         LosUtil.TestResults var6 = this.testVisionAdjacent(0, var2, var3, false, var4);
         if (var6 == LosUtil.TestResults.Blocked) {
            return LosUtil.TestResults.Blocked;
         } else {
            return var5 != LosUtil.TestResults.ClearThroughWindow && var6 != LosUtil.TestResults.ClearThroughWindow ? this.testVisionAdjacent(var1, var2, var3, false, var4) : LosUtil.TestResults.ClearThroughWindow;
         }
      }
   }

   boolean HasNoCharacters() {
      int var1;
      for(var1 = 0; var1 < this.MovingObjects.size(); ++var1) {
         if (this.MovingObjects.get(var1) instanceof IsoGameCharacter) {
            return false;
         }
      }

      for(var1 = 0; var1 < this.SpecialObjects.size(); ++var1) {
         if (this.SpecialObjects.get(var1) instanceof IsoBarricade) {
            return false;
         }
      }

      return true;
   }

   public IsoZombie getZombie() {
      for(int var1 = 0; var1 < this.MovingObjects.size(); ++var1) {
         if (this.MovingObjects.get(var1) instanceof IsoZombie) {
            return (IsoZombie)this.MovingObjects.get(var1);
         }
      }

      return null;
   }

   public IsoPlayer getPlayer() {
      for(int var1 = 0; var1 < this.MovingObjects.size(); ++var1) {
         if (this.MovingObjects.get(var1) instanceof IsoPlayer) {
            return (IsoPlayer)this.MovingObjects.get(var1);
         }
      }

      return null;
   }

   public static float getDarkStep() {
      return darkStep;
   }

   public static void setDarkStep(float var0) {
      darkStep = var0;
   }

   public static int getRecalcLightTime() {
      return RecalcLightTime;
   }

   public static void setRecalcLightTime(int var0) {
      RecalcLightTime = var0;
   }

   public static int getLightcache() {
      return lightcache;
   }

   public static void setLightcache(int var0) {
      lightcache = var0;
   }

   public boolean isCouldSee(int var1) {
      return this.lighting[var1].bCouldSee();
   }

   public void setCouldSee(int var1, boolean var2) {
      this.lighting[var1].bCouldSee(var2);
   }

   public boolean isCanSee(int var1) {
      return this.lighting[var1].bCanSee();
   }

   public void setCanSee(int var1, boolean var2) {
      this.lighting[var1].bCanSee(var2);
   }

   public IsoCell getCell() {
      return IsoWorld.instance.CurrentCell;
   }

   public IsoGridSquare getE() {
      return this.e;
   }

   public void setE(IsoGridSquare var1) {
      this.e = var1;
   }

   public ArrayList getLightInfluenceB() {
      return this.LightInfluenceB;
   }

   public void setLightInfluenceB(ArrayList var1) {
      this.LightInfluenceB = var1;
   }

   public ArrayList getLightInfluenceG() {
      return this.LightInfluenceG;
   }

   public void setLightInfluenceG(ArrayList var1) {
      this.LightInfluenceG = var1;
   }

   public ArrayList getLightInfluenceR() {
      return this.LightInfluenceR;
   }

   public void setLightInfluenceR(ArrayList var1) {
      this.LightInfluenceR = var1;
   }

   public ArrayList getStaticMovingObjects() {
      return this.StaticMovingObjects;
   }

   public ArrayList getMovingObjects() {
      return this.MovingObjects;
   }

   public IsoGridSquare getN() {
      return this.n;
   }

   public void setN(IsoGridSquare var1) {
      this.n = var1;
   }

   public PZArrayList getObjects() {
      return this.Objects;
   }

   public PropertyContainer getProperties() {
      return this.Properties;
   }

   public IsoRoom getRoom() {
      return this.roomID == -1 ? null : this.room;
   }

   public void setRoom(IsoRoom var1) {
      this.room = var1;
   }

   public IsoBuilding getBuilding() {
      IsoRoom var1 = this.getRoom();
      return var1 != null ? var1.getBuilding() : null;
   }

   public IsoGridSquare getS() {
      return this.s;
   }

   public void setS(IsoGridSquare var1) {
      this.s = var1;
   }

   public ArrayList getSpecialObjects() {
      return this.SpecialObjects;
   }

   public IsoGridSquare getW() {
      return this.w;
   }

   public void setW(IsoGridSquare var1) {
      this.w = var1;
   }

   public float getLampostTotalR() {
      return this.lighting[0].lampostTotalR();
   }

   public void setLampostTotalR(float var1) {
      this.lighting[0].lampostTotalR(var1);
   }

   public float getLampostTotalG() {
      return this.lighting[0].lampostTotalG();
   }

   public void setLampostTotalG(float var1) {
      this.lighting[0].lampostTotalG(var1);
   }

   public float getLampostTotalB() {
      return this.lighting[0].lampostTotalB();
   }

   public void setLampostTotalB(float var1) {
      this.lighting[0].lampostTotalB(var1);
   }

   public boolean isSeen(int var1) {
      return this.lighting[var1].bSeen();
   }

   public void setIsSeen(int var1, boolean var2) {
      this.lighting[var1].bSeen(var2);
   }

   public float getDarkMulti(int var1) {
      return this.lighting[var1].darkMulti();
   }

   public void setDarkMulti(int var1, float var2) {
      this.lighting[var1].darkMulti(var2);
   }

   public float getTargetDarkMulti(int var1) {
      return this.lighting[var1].targetDarkMulti();
   }

   public void setTargetDarkMulti(int var1, float var2) {
      this.lighting[var1].targetDarkMulti(var2);
   }

   public void setX(int var1) {
      this.x = var1;
      this.CachedScreenValue = -1;
   }

   public void setY(int var1) {
      this.y = var1;
      this.CachedScreenValue = -1;
   }

   public void setZ(int var1) {
      this.z = var1;
      this.CachedScreenValue = -1;
   }

   public ArrayList getDeferedCharacters() {
      return this.DeferedCharacters;
   }

   public void addDeferredCharacter(IsoGameCharacter var1) {
      if (this.DeferredCharacterTick != this.getCell().DeferredCharacterTick) {
         if (!this.DeferedCharacters.isEmpty()) {
            this.DeferedCharacters.clear();
         }

         this.DeferredCharacterTick = this.getCell().DeferredCharacterTick;
      }

      this.DeferedCharacters.add(var1);
   }

   public boolean isCacheIsFree() {
      return this.CacheIsFree;
   }

   public void setCacheIsFree(boolean var1) {
      this.CacheIsFree = var1;
   }

   public boolean isCachedIsFree() {
      return this.CachedIsFree;
   }

   public void setCachedIsFree(boolean var1) {
      this.CachedIsFree = var1;
   }

   public static boolean isbDoSlowPathfinding() {
      return bDoSlowPathfinding;
   }

   public static void setbDoSlowPathfinding(boolean var0) {
      bDoSlowPathfinding = var0;
   }

   public boolean isSolidFloorCached() {
      return this.SolidFloorCached;
   }

   public void setSolidFloorCached(boolean var1) {
      this.SolidFloorCached = var1;
   }

   public boolean isSolidFloor() {
      return this.SolidFloor;
   }

   public void setSolidFloor(boolean var1) {
      this.SolidFloor = var1;
   }

   public static ColorInfo getDefColorInfo() {
      return defColorInfo;
   }

   public boolean isOutside() {
      return this.Properties.Is(IsoFlagType.exterior);
   }

   public boolean HasPushable() {
      int var1 = this.MovingObjects.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (this.MovingObjects.get(var2) instanceof IsoPushableObject) {
            return true;
         }
      }

      return false;
   }

   public void setRoomID(int var1) {
      this.roomID = var1;
      if (var1 != -1) {
         this.getProperties().UnSet(IsoFlagType.exterior);
         this.room = this.chunk.getRoom(var1);
      }

   }

   public int getRoomID() {
      return this.roomID;
   }

   public boolean getCanSee(int var1) {
      return this.lighting[var1].bCanSee();
   }

   public boolean getSeen(int var1) {
      return this.lighting[var1].bSeen();
   }

   public IsoChunk getChunk() {
      return this.chunk;
   }

   public IsoObject getDoorOrWindow(boolean var1) {
      for(int var2 = this.SpecialObjects.size() - 1; var2 >= 0; --var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         if (var3 instanceof IsoDoor && ((IsoDoor)var3).north == var1) {
            return var3;
         }

         if (var3 instanceof IsoThumpable && ((IsoThumpable)var3).north == var1 && (((IsoThumpable)var3).isDoor() || ((IsoThumpable)var3).isWindow())) {
            return var3;
         }

         if (var3 instanceof IsoWindow && ((IsoWindow)var3).north == var1) {
            return var3;
         }
      }

      return null;
   }

   public IsoObject getDoorOrWindowOrWindowFrame(IsoDirections var1, boolean var2) {
      for(int var3 = this.Objects.size() - 1; var3 >= 0; --var3) {
         IsoObject var4 = (IsoObject)this.Objects.get(var3);
         IsoDoor var5 = (IsoDoor)Type.tryCastTo(var4, IsoDoor.class);
         IsoThumpable var6 = (IsoThumpable)Type.tryCastTo(var4, IsoThumpable.class);
         IsoWindow var7 = (IsoWindow)Type.tryCastTo(var4, IsoWindow.class);
         if (var5 != null && var5.getSpriteEdge(var2) == var1) {
            return var4;
         }

         if (var6 != null && var6.getSpriteEdge(var2) == var1) {
            return var4;
         }

         if (var7 != null) {
            if (var7.north && var1 == IsoDirections.N) {
               return var4;
            }

            if (!var7.north && var1 == IsoDirections.W) {
               return var4;
            }
         }

         if (IsoWindowFrame.isWindowFrame(var4)) {
            if (IsoWindowFrame.isWindowFrame(var4, true) && var1 == IsoDirections.N) {
               return var4;
            }

            if (IsoWindowFrame.isWindowFrame(var4, false) && var1 == IsoDirections.W) {
               return var4;
            }
         }
      }

      return null;
   }

   public IsoObject getOpenDoor(IsoDirections var1) {
      for(int var2 = 0; var2 < this.SpecialObjects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.SpecialObjects.get(var2);
         IsoDoor var4 = (IsoDoor)Type.tryCastTo(var3, IsoDoor.class);
         IsoThumpable var5 = (IsoThumpable)Type.tryCastTo(var3, IsoThumpable.class);
         if (var4 != null && var4.open && var4.getSpriteEdge(false) == var1) {
            return var4;
         }

         if (var5 != null && var5.open && var5.getSpriteEdge(false) == var1) {
            return var5;
         }
      }

      return null;
   }

   public void removeWorldObject(IsoWorldInventoryObject var1) {
      if (var1 != null) {
         var1.removeFromWorld();
         var1.removeFromSquare();
      }
   }

   public void removeAllWorldObjects() {
      for(int var1 = 0; var1 < this.getWorldObjects().size(); ++var1) {
         IsoObject var2 = (IsoObject)this.getWorldObjects().get(var1);
         var2.removeFromWorld();
         var2.removeFromSquare();
         --var1;
      }

   }

   public ArrayList getWorldObjects() {
      return this.WorldObjects;
   }

   public PZArrayList getLocalTemporaryObjects() {
      return this.localTemporaryObjects;
   }

   public KahluaTable getModData() {
      if (this.table == null) {
         this.table = LuaManager.platform.newTable();
      }

      return this.table;
   }

   public boolean hasModData() {
      return this.table != null && !this.table.isEmpty();
   }

   public ZomboidBitFlag getHasTypes() {
      return this.hasTypes;
   }

   public void setVertLight(int var1, int var2, int var3) {
      this.lighting[var3].lightverts(var1, var2);
   }

   public int getVertLight(int var1, int var2) {
      return this.lighting[var2].lightverts(var1);
   }

   public void setRainDrop(IsoRaindrop var1) {
      this.RainDrop = var1;
   }

   public IsoRaindrop getRainDrop() {
      return this.RainDrop;
   }

   public void setRainSplash(IsoRainSplash var1) {
      this.RainSplash = var1;
   }

   public IsoRainSplash getRainSplash() {
      return this.RainSplash;
   }

   public IsoMetaGrid.Zone getZone() {
      return this.zone;
   }

   public String getZoneType() {
      return this.zone != null ? this.zone.getType() : null;
   }

   public boolean isOverlayDone() {
      return this.overlayDone;
   }

   public void setOverlayDone(boolean var1) {
      this.overlayDone = var1;
   }

   public ErosionData.Square getErosionData() {
      if (this.erosion == null) {
         this.erosion = new ErosionData.Square();
      }

      return this.erosion;
   }

   public void disableErosion() {
      ErosionData.Square var1 = this.getErosionData();
      if (var1 != null && !var1.doNothing) {
         var1.doNothing = true;
      }

   }

   public void removeErosionObject(String var1) {
      if (this.erosion != null) {
         if ("WallVines".equals(var1)) {
            for(int var2 = 0; var2 < this.erosion.regions.size(); ++var2) {
               ErosionCategory.Data var3 = (ErosionCategory.Data)this.erosion.regions.get(var2);
               if (var3.regionID == 2 && var3.categoryID == 0) {
                  this.erosion.regions.remove(var2);
                  break;
               }
            }
         }

      }
   }

   public void syncIsoTrap(HandWeapon var1) {
      ByteBufferWriter var2 = GameClient.connection.startPacket();
      PacketTypes.PacketType.AddExplosiveTrap.doPacket(var2);
      var2.putInt(this.getX());
      var2.putInt(this.getY());
      var2.putInt(this.getZ());

      try {
         var1.saveWithSize(var2.bb, false);
      } catch (Exception var4) {
         ExceptionLogger.logException(var4);
      }

      PacketTypes.PacketType.AddExplosiveTrap.send(GameClient.connection);
   }

   public void drawCircleExplosion(int var1, IsoTrap var2, IsoTrap.ExplosionMode var3) {
      if (var1 > 15) {
         var1 = 15;
      }

      for(int var4 = this.getX() - var1; var4 <= this.getX() + var1; ++var4) {
         for(int var5 = this.getY() - var1; var5 <= this.getY() + var1; ++var5) {
            if (!(IsoUtils.DistanceTo((float)var4 + 0.5F, (float)var5 + 0.5F, (float)this.getX() + 0.5F, (float)this.getY() + 0.5F) > (float)var1)) {
               LosUtil.TestResults var6 = LosUtil.lineClear(this.getCell(), (int)var2.getX(), (int)var2.getY(), (int)var2.getZ(), var4, var5, this.z, false);
               if (var6 != LosUtil.TestResults.Blocked && var6 != LosUtil.TestResults.ClearThroughClosedDoor) {
                  IsoGridSquare var7 = this.getCell().getGridSquare(var4, var5, this.getZ());
                  if (var7 != null) {
                     if (var3 == IsoTrap.ExplosionMode.Smoke) {
                        if (!GameClient.bClient && Rand.Next(2) == 0) {
                           IsoFireManager.StartSmoke(this.getCell(), var7, true, 40, 0);
                        }

                        var7.smoke();
                     }

                     if (var3 == IsoTrap.ExplosionMode.Explosion) {
                        if (!GameClient.bClient && var2.getExplosionPower() > 0 && Rand.Next(80 - var2.getExplosionPower()) <= 0) {
                           var7.Burn();
                        }

                        var7.explosion(var2);
                        if (!GameClient.bClient && var2.getExplosionPower() > 0 && Rand.Next(100 - var2.getExplosionPower()) == 0) {
                           IsoFireManager.StartFire(this.getCell(), var7, true, 20);
                        }
                     }

                     if (var3 == IsoTrap.ExplosionMode.Fire && !GameClient.bClient && Rand.Next(100 - var2.getFirePower()) == 0) {
                        IsoFireManager.StartFire(this.getCell(), var7, true, 40);
                     }

                     if (var3 == IsoTrap.ExplosionMode.Sensor) {
                        var7.setTrapPositionX(this.getX());
                        var7.setTrapPositionY(this.getY());
                        var7.setTrapPositionZ(this.getZ());
                     }
                  }
               }
            }
         }
      }

   }

   public void explosion(IsoTrap var1) {
      if (!GameServer.bServer || !var1.isInstantExplosion()) {
         for(int var2 = 0; var2 < this.getMovingObjects().size(); ++var2) {
            IsoMovingObject var3 = (IsoMovingObject)this.getMovingObjects().get(var2);
            if (var3 instanceof IsoGameCharacter) {
               if (GameServer.bServer || !(var3 instanceof IsoZombie) || !((IsoZombie)var3).isRemoteZombie()) {
                  int var4 = Math.min(var1.getExplosionPower(), 80);
                  var3.Hit((HandWeapon)InventoryItemFactory.CreateItem("Base.Axe"), IsoWorld.instance.CurrentCell.getFakeZombieForHit(), Rand.Next((float)var4 / 30.0F, (float)var4 / 30.0F * 2.0F) + var1.getExtraDamage(), false, 1.0F);
                  if (var1.getExplosionPower() > 0) {
                     boolean var5 = !(var3 instanceof IsoZombie);

                     while(var5) {
                        var5 = false;
                        BodyPart var6 = ((IsoGameCharacter)var3).getBodyDamage().getBodyPart(BodyPartType.FromIndex(Rand.Next(15)));
                        var6.setBurned();
                        if (Rand.Next((100 - var4) / 2) == 0) {
                           var5 = true;
                        }
                     }
                  }
               }

               if (GameClient.bClient && var3 instanceof IsoZombie && ((IsoZombie)var3).isRemoteZombie()) {
                  var3.Hit((HandWeapon)InventoryItemFactory.CreateItem("Base.Axe"), IsoWorld.instance.CurrentCell.getFakeZombieForHit(), 0.0F, true, 0.0F);
               }
            }
         }

      }
   }

   public void smoke() {
      for(int var1 = 0; var1 < this.getMovingObjects().size(); ++var1) {
         IsoMovingObject var2 = (IsoMovingObject)this.getMovingObjects().get(var1);
         if (var2 instanceof IsoZombie) {
            ((IsoZombie)var2).setTarget((IsoMovingObject)null);
            ((IsoZombie)var2).changeState(ZombieIdleState.instance());
         }
      }

   }

   public void explodeTrap() {
      IsoGridSquare var1 = this.getCell().getGridSquare(this.getTrapPositionX(), this.getTrapPositionY(), this.getTrapPositionZ());
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.getObjects().size(); ++var2) {
            IsoObject var3 = (IsoObject)var1.getObjects().get(var2);
            if (var3 instanceof IsoTrap) {
               IsoTrap var4 = (IsoTrap)var3;
               var4.triggerExplosion(false);
               IsoGridSquare var5 = null;
               int var6 = var4.getSensorRange();

               for(int var7 = var1.getX() - var6; var7 <= var1.getX() + var6; ++var7) {
                  for(int var8 = var1.getY() - var6; var8 <= var1.getY() + var6; ++var8) {
                     if (IsoUtils.DistanceTo((float)var7 + 0.5F, (float)var8 + 0.5F, (float)var1.getX() + 0.5F, (float)var1.getY() + 0.5F) <= (float)var6) {
                        var5 = this.getCell().getGridSquare(var7, var8, this.getZ());
                        if (var5 != null) {
                           var5.setTrapPositionX(-1);
                           var5.setTrapPositionY(-1);
                           var5.setTrapPositionZ(-1);
                        }
                     }
                  }
               }

               return;
            }
         }
      }

   }

   public int getTrapPositionX() {
      return this.trapPositionX;
   }

   public void setTrapPositionX(int var1) {
      this.trapPositionX = var1;
   }

   public int getTrapPositionY() {
      return this.trapPositionY;
   }

   public void setTrapPositionY(int var1) {
      this.trapPositionY = var1;
   }

   public int getTrapPositionZ() {
      return this.trapPositionZ;
   }

   public void setTrapPositionZ(int var1) {
      this.trapPositionZ = var1;
   }

   public boolean haveElectricity() {
      if ((this.chunk == null || !this.chunk.bLoaded) && this.haveElectricity) {
         return true;
      } else if (!SandboxOptions.getInstance().AllowExteriorGenerator.getValue() && this.Is(IsoFlagType.exterior)) {
         return false;
      } else {
         return this.chunk != null && this.chunk.isGeneratorPoweringSquare(this.x, this.y, this.z);
      }
   }

   public void setHaveElectricity(boolean var1) {
      if (!var1) {
         this.haveElectricity = false;
      }

      if (this.getObjects() != null) {
         for(int var2 = 0; var2 < this.getObjects().size(); ++var2) {
            if (this.getObjects().get(var2) instanceof IsoLightSwitch) {
               ((IsoLightSwitch)this.getObjects().get(var2)).update();
            }
         }
      }

   }

   public IsoGenerator getGenerator() {
      if (this.getSpecialObjects() != null) {
         for(int var1 = 0; var1 < this.getSpecialObjects().size(); ++var1) {
            if (this.getSpecialObjects().get(var1) instanceof IsoGenerator) {
               return (IsoGenerator)this.getSpecialObjects().get(var1);
            }
         }
      }

      return null;
   }

   public void stopFire() {
      IsoFireManager.RemoveAllOn(this);
      this.getProperties().Set(IsoFlagType.burntOut);
      this.getProperties().UnSet(IsoFlagType.burning);
      this.burntOut = true;
   }

   public void transmitStopFire() {
      if (GameClient.bClient) {
         GameClient.sendStopFire(this);
      }

   }

   public long playSound(String var1) {
      BaseSoundEmitter var2 = IsoWorld.instance.getFreeEmitter((float)this.x + 0.5F, (float)this.y + 0.5F, (float)this.z);
      return var2.playSound(var1);
   }

   /** @deprecated */
   @Deprecated
   public long playSound(String var1, boolean var2) {
      BaseSoundEmitter var3 = IsoWorld.instance.getFreeEmitter((float)this.x + 0.5F, (float)this.y + 0.5F, (float)this.z);
      return var3.playSound(var1, var2);
   }

   public void FixStackableObjects() {
      IsoObject var1 = null;

      for(int var2 = 0; var2 < this.Objects.size(); ++var2) {
         IsoObject var3 = (IsoObject)this.Objects.get(var2);
         if (!(var3 instanceof IsoWorldInventoryObject) && var3.sprite != null) {
            PropertyContainer var4 = var3.sprite.getProperties();
            if (var4.getStackReplaceTileOffset() != 0) {
               var3.sprite = IsoSprite.getSprite(IsoSpriteManager.instance, var3.sprite.ID + var4.getStackReplaceTileOffset());
               if (var3.sprite == null) {
                  continue;
               }

               var4 = var3.sprite.getProperties();
            }

            if (var4.isTable() || var4.isTableTop()) {
               float var5 = var4.isSurfaceOffset() ? (float)var4.getSurface() : 0.0F;
               if (var1 != null) {
                  var3.setRenderYOffset(var1.getRenderYOffset() + var1.getSurfaceOffset() - var5);
               } else {
                  var3.setRenderYOffset(0.0F - var5);
               }
            }

            if (var4.isTable()) {
               var1 = var3;
            }

            if (var3 instanceof IsoLightSwitch && var4.isTableTop() && var1 != null && !var4.Is("IgnoreSurfaceSnap")) {
               int var14 = PZMath.tryParseInt(var4.Val("Noffset"), 0);
               int var6 = PZMath.tryParseInt(var4.Val("Soffset"), 0);
               int var7 = PZMath.tryParseInt(var4.Val("Woffset"), 0);
               int var8 = PZMath.tryParseInt(var4.Val("Eoffset"), 0);
               String var9 = var4.Val("Facing");
               PropertyContainer var10 = var1.getProperties();
               String var11 = var10.Val("Facing");
               if (!StringUtils.isNullOrWhitespace(var11) && !var11.equals(var9)) {
                  int var12 = 0;
                  if ("N".equals(var11)) {
                     if (var14 != 0) {
                        var12 = var14;
                     } else if (var6 != 0) {
                        var12 = var6;
                     }
                  } else if ("S".equals(var11)) {
                     if (var6 != 0) {
                        var12 = var6;
                     } else if (var14 != 0) {
                        var12 = var14;
                     }
                  } else if ("W".equals(var11)) {
                     if (var7 != 0) {
                        var12 = var7;
                     } else if (var8 != 0) {
                        var12 = var8;
                     }
                  } else if ("E".equals(var11)) {
                     if (var8 != 0) {
                        var12 = var8;
                     } else if (var7 != 0) {
                        var12 = var7;
                     }
                  }

                  if (var12 != 0) {
                     IsoSprite var13 = IsoSpriteManager.instance.getSprite(var3.sprite.ID + var12);
                     if (var13 != null) {
                        var3.setSprite(var13);
                     }
                  }
               }
            }
         }
      }

   }

   public BaseVehicle getVehicleContainer() {
      int var1 = (int)(((float)this.x - 4.0F) / 10.0F);
      int var2 = (int)(((float)this.y - 4.0F) / 10.0F);
      int var3 = (int)Math.ceil((double)(((float)this.x + 4.0F) / 10.0F));
      int var4 = (int)Math.ceil((double)(((float)this.y + 4.0F) / 10.0F));

      for(int var5 = var2; var5 < var4; ++var5) {
         for(int var6 = var1; var6 < var3; ++var6) {
            IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var6, var5) : IsoWorld.instance.CurrentCell.getChunk(var6, var5);
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.vehicles.size(); ++var8) {
                  BaseVehicle var9 = (BaseVehicle)var7.vehicles.get(var8);
                  if (var9.isIntersectingSquare(this.x, this.y, this.z)) {
                     return var9;
                  }
               }
            }
         }
      }

      return null;
   }

   public boolean isVehicleIntersecting() {
      int var1 = (int)(((float)this.x - 4.0F) / 10.0F);
      int var2 = (int)(((float)this.y - 4.0F) / 10.0F);
      int var3 = (int)Math.ceil((double)(((float)this.x + 4.0F) / 10.0F));
      int var4 = (int)Math.ceil((double)(((float)this.y + 4.0F) / 10.0F));

      for(int var5 = var2; var5 < var4; ++var5) {
         for(int var6 = var1; var6 < var3; ++var6) {
            IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var6, var5) : IsoWorld.instance.CurrentCell.getChunk(var6, var5);
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.vehicles.size(); ++var8) {
                  BaseVehicle var9 = (BaseVehicle)var7.vehicles.get(var8);
                  if (var9.isIntersectingSquare(this.x, this.y, this.z)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public IsoCompost getCompost() {
      if (this.getSpecialObjects() != null) {
         for(int var1 = 0; var1 < this.getSpecialObjects().size(); ++var1) {
            if (this.getSpecialObjects().get(var1) instanceof IsoCompost) {
               return (IsoCompost)this.getSpecialObjects().get(var1);
            }
         }
      }

      return null;
   }

   public void setIsoWorldRegion(IsoWorldRegion var1) {
      this.hasSetIsoWorldRegion = var1 != null;
      this.isoWorldRegion = var1;
   }

   public IWorldRegion getIsoWorldRegion() {
      if (GameServer.bServer) {
         return IsoRegions.getIsoWorldRegion(this.x, this.y, this.z);
      } else {
         if (!this.hasSetIsoWorldRegion) {
            this.isoWorldRegion = IsoRegions.getIsoWorldRegion(this.x, this.y, this.z);
            this.hasSetIsoWorldRegion = true;
         }

         return this.isoWorldRegion;
      }
   }

   public void ResetIsoWorldRegion() {
      this.isoWorldRegion = null;
      this.hasSetIsoWorldRegion = false;
   }

   public boolean isInARoom() {
      return this.getRoom() != null || this.getIsoWorldRegion() != null && this.getIsoWorldRegion().isPlayerRoom();
   }

   public int getWallType() {
      int var1 = 0;
      if (this.getProperties().Is(IsoFlagType.WallN)) {
         var1 |= 1;
      }

      if (this.getProperties().Is(IsoFlagType.WallW)) {
         var1 |= 4;
      }

      if (this.getProperties().Is(IsoFlagType.WallNW)) {
         var1 |= 5;
      }

      IsoGridSquare var2 = this.nav[IsoDirections.E.index()];
      if (var2 != null && (var2.getProperties().Is(IsoFlagType.WallW) || var2.getProperties().Is(IsoFlagType.WallNW))) {
         var1 |= 8;
      }

      IsoGridSquare var3 = this.nav[IsoDirections.S.index()];
      if (var3 != null && (var3.getProperties().Is(IsoFlagType.WallN) || var3.getProperties().Is(IsoFlagType.WallNW))) {
         var1 |= 2;
      }

      return var1;
   }

   public int getPuddlesDir() {
      byte var1 = IsoGridSquare.PuddlesDirection.PUDDLES_DIR_ALL;
      if (this.isInARoom()) {
         return IsoGridSquare.PuddlesDirection.PUDDLES_DIR_NONE;
      } else {
         for(int var2 = 0; var2 < this.getObjects().size(); ++var2) {
            IsoObject var3 = (IsoObject)this.getObjects().get(var2);
            if (var3.AttachedAnimSprite != null) {
               for(int var4 = 0; var4 < var3.AttachedAnimSprite.size(); ++var4) {
                  IsoSprite var5 = ((IsoSpriteInstance)var3.AttachedAnimSprite.get(var4)).parentSprite;
                  if (var5.name != null) {
                     if (var5.name.equals("street_trafficlines_01_2") || var5.name.equals("street_trafficlines_01_6") || var5.name.equals("street_trafficlines_01_22") || var5.name.equals("street_trafficlines_01_32")) {
                        var1 = IsoGridSquare.PuddlesDirection.PUDDLES_DIR_NW;
                     }

                     if (var5.name.equals("street_trafficlines_01_4") || var5.name.equals("street_trafficlines_01_0") || var5.name.equals("street_trafficlines_01_16")) {
                        var1 = IsoGridSquare.PuddlesDirection.PUDDLES_DIR_NE;
                     }
                  }
               }
            }
         }

         return var1;
      }
   }

   public boolean haveFire() {
      int var1 = this.Objects.size();
      IsoObject[] var2 = (IsoObject[])this.Objects.getElements();

      for(int var3 = 0; var3 < var1; ++var3) {
         IsoObject var4 = var2[var3];
         if (var4 instanceof IsoFire) {
            return true;
         }
      }

      return false;
   }

   public IsoBuilding getRoofHideBuilding() {
      return this.roofHideBuilding;
   }

   public IsoGridSquare getAdjacentSquare(IsoDirections var1) {
      return this.nav[var1.index()];
   }

   public IsoGridSquare getAdjacentPathSquare(IsoDirections var1) {
      switch(var1) {
      case NW:
         return this.nw;
      case N:
         return this.n;
      case NE:
         return this.ne;
      case W:
         return this.w;
      case E:
         return this.e;
      case SW:
         return this.sw;
      case S:
         return this.s;
      case SE:
         return this.se;
      default:
         return null;
      }
   }

   public float getApparentZ(float var1, float var2) {
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      var2 = PZMath.clamp(var2, 0.0F, 1.0F);
      if (this.Has(IsoObjectType.stairsTN)) {
         return (float)this.getZ() + PZMath.lerp(0.6666F, 1.0F, 1.0F - var2);
      } else if (this.Has(IsoObjectType.stairsTW)) {
         return (float)this.getZ() + PZMath.lerp(0.6666F, 1.0F, 1.0F - var1);
      } else if (this.Has(IsoObjectType.stairsMN)) {
         return (float)this.getZ() + PZMath.lerp(0.3333F, 0.6666F, 1.0F - var2);
      } else if (this.Has(IsoObjectType.stairsMW)) {
         return (float)this.getZ() + PZMath.lerp(0.3333F, 0.6666F, 1.0F - var1);
      } else if (this.Has(IsoObjectType.stairsBN)) {
         return (float)this.getZ() + PZMath.lerp(0.01F, 0.3333F, 1.0F - var2);
      } else {
         return this.Has(IsoObjectType.stairsBW) ? (float)this.getZ() + PZMath.lerp(0.01F, 0.3333F, 1.0F - var1) : (float)this.getZ();
      }
   }

   public float getTotalWeightOfItemsOnFloor() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.WorldObjects.size(); ++var2) {
         InventoryItem var3 = ((IsoWorldInventoryObject)this.WorldObjects.get(var2)).getItem();
         if (var3 != null) {
            var1 += var3.getUnequippedWeight();
         }
      }

      return var1;
   }

   public boolean getCollideMatrix(int var1, int var2, int var3) {
      return getMatrixBit(this.collideMatrix, var1 + 1, var2 + 1, var3 + 1);
   }

   public boolean getPathMatrix(int var1, int var2, int var3) {
      return getMatrixBit(this.pathMatrix, var1 + 1, var2 + 1, var3 + 1);
   }

   public boolean getVisionMatrix(int var1, int var2, int var3) {
      return getMatrixBit(this.visionMatrix, var1 + 1, var2 + 1, var3 + 1);
   }

   public void checkRoomSeen(int var1) {
      IsoRoom var2 = this.getRoom();
      if (var2 != null && var2.def != null && !var2.def.bExplored) {
         IsoPlayer var3 = IsoPlayer.players[var1];
         if (var3 != null) {
            if (this.z == (int)var3.z) {
               byte var4 = 10;
               if (var3.getBuilding() == var2.building) {
                  var4 = 50;
               }

               if (IsoUtils.DistanceToSquared(var3.x, var3.y, (float)this.x + 0.5F, (float)this.y + 0.5F) < (float)(var4 * var4)) {
                  var2.def.bExplored = true;
                  var2.onSee();
                  var2.seen = 0;
               }

            }
         }
      }
   }

   public boolean hasFlies() {
      return this.bHasFlies;
   }

   public void setHasFlies(boolean var1) {
      this.bHasFlies = var1;
   }

   public float getLightLevel(int var1) {
      return (this.lighting[var1].lightInfo().r + this.lighting[var1].lightInfo().g + this.lighting[var1].lightInfo().b) / 3.0F;
   }

   public interface ILighting {
      int lightverts(int var1);

      float lampostTotalR();

      float lampostTotalG();

      float lampostTotalB();

      boolean bSeen();

      boolean bCanSee();

      boolean bCouldSee();

      float darkMulti();

      float targetDarkMulti();

      ColorInfo lightInfo();

      void lightverts(int var1, int var2);

      void lampostTotalR(float var1);

      void lampostTotalG(float var1);

      void lampostTotalB(float var1);

      void bSeen(boolean var1);

      void bCanSee(boolean var1);

      void bCouldSee(boolean var1);

      void darkMulti(float var1);

      void targetDarkMulti(float var1);

      int resultLightCount();

      IsoGridSquare.ResultLight getResultLight(int var1);

      void reset();
   }

   public static final class CircleStencilShader extends Shader {
      public static final IsoGridSquare.CircleStencilShader instance = new IsoGridSquare.CircleStencilShader();
      public int a_wallShadeColor = -1;

      public CircleStencilShader() {
         super("CircleStencil");
      }

      protected void onCompileSuccess(ShaderProgram var1) {
         this.Start();
         this.a_wallShadeColor = GL20.glGetAttribLocation(this.getID(), "a_wallShadeColor");
         var1.setSamplerUnit("texture", 0);
         var1.setSamplerUnit("CutawayStencil", 1);
         this.End();
      }
   }

   public static final class Lighting implements IsoGridSquare.ILighting {
      private final int[] lightverts = new int[8];
      private float lampostTotalR = 0.0F;
      private float lampostTotalG = 0.0F;
      private float lampostTotalB = 0.0F;
      private boolean bSeen;
      private boolean bCanSee;
      private boolean bCouldSee;
      private float darkMulti;
      private float targetDarkMulti;
      private final ColorInfo lightInfo = new ColorInfo();

      public int lightverts(int var1) {
         return this.lightverts[var1];
      }

      public float lampostTotalR() {
         return this.lampostTotalR;
      }

      public float lampostTotalG() {
         return this.lampostTotalG;
      }

      public float lampostTotalB() {
         return this.lampostTotalB;
      }

      public boolean bSeen() {
         return this.bSeen;
      }

      public boolean bCanSee() {
         return this.bCanSee;
      }

      public boolean bCouldSee() {
         return this.bCouldSee;
      }

      public float darkMulti() {
         return this.darkMulti;
      }

      public float targetDarkMulti() {
         return this.targetDarkMulti;
      }

      public ColorInfo lightInfo() {
         return this.lightInfo;
      }

      public void lightverts(int var1, int var2) {
         this.lightverts[var1] = var2;
      }

      public void lampostTotalR(float var1) {
         this.lampostTotalR = var1;
      }

      public void lampostTotalG(float var1) {
         this.lampostTotalG = var1;
      }

      public void lampostTotalB(float var1) {
         this.lampostTotalB = var1;
      }

      public void bSeen(boolean var1) {
         this.bSeen = var1;
      }

      public void bCanSee(boolean var1) {
         this.bCanSee = var1;
      }

      public void bCouldSee(boolean var1) {
         this.bCouldSee = var1;
      }

      public void darkMulti(float var1) {
         this.darkMulti = var1;
      }

      public void targetDarkMulti(float var1) {
         this.targetDarkMulti = var1;
      }

      public int resultLightCount() {
         return 0;
      }

      public IsoGridSquare.ResultLight getResultLight(int var1) {
         return null;
      }

      public void reset() {
         this.lampostTotalR = 0.0F;
         this.lampostTotalG = 0.0F;
         this.lampostTotalB = 0.0F;
         this.bSeen = false;
         this.bCouldSee = false;
         this.bCanSee = false;
         this.targetDarkMulti = 0.0F;
         this.darkMulti = 0.0F;
         this.lightInfo.r = 0.0F;
         this.lightInfo.g = 0.0F;
         this.lightInfo.b = 0.0F;
         this.lightInfo.a = 1.0F;
      }
   }

   public static class CellGetSquare implements IsoGridSquare.GetSquare {
      public IsoGridSquare getGridSquare(int var1, int var2, int var3) {
         return IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      }
   }

   public interface GetSquare {
      IsoGridSquare getGridSquare(int var1, int var2, int var3);
   }

   private static final class s_performance {
      static final PerformanceProfileProbe renderFloor = new PerformanceProfileProbe("IsoGridSquare.renderFloor", false);
   }

   public static class PuddlesDirection {
      public static byte PUDDLES_DIR_NONE = 1;
      public static byte PUDDLES_DIR_NE = 2;
      public static byte PUDDLES_DIR_NW = 4;
      public static byte PUDDLES_DIR_ALL = 8;
   }

   public static final class NoCircleStencilShader {
      public static final IsoGridSquare.NoCircleStencilShader instance = new IsoGridSquare.NoCircleStencilShader();
      private ShaderProgram shaderProgram;
      public int ShaderID = -1;
      public int a_wallShadeColor = -1;

      private void initShader() {
         this.shaderProgram = ShaderProgram.createShaderProgram("NoCircleStencil", false, true);
         if (this.shaderProgram.isCompiled()) {
            this.ShaderID = this.shaderProgram.getShaderID();
            this.a_wallShadeColor = GL20.glGetAttribLocation(this.ShaderID, "a_wallShadeColor");
         }

      }
   }

   private interface RenderWallCallback {
      void invoke(Texture var1, float var2, float var3);
   }

   public static final class ResultLight {
      public int id;
      public int x;
      public int y;
      public int z;
      public int radius;
      public float r;
      public float g;
      public float b;
      public static final int RLF_NONE = 0;
      public static final int RLF_ROOMLIGHT = 1;
      public static final int RLF_TORCH = 2;
      public int flags;

      public IsoGridSquare.ResultLight copyFrom(IsoGridSquare.ResultLight var1) {
         this.id = var1.id;
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.radius = var1.radius;
         this.r = var1.r;
         this.g = var1.g;
         this.b = var1.b;
         this.flags = var1.flags;
         return this;
      }
   }
}
