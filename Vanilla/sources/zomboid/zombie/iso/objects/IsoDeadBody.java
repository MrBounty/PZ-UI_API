package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import zombie.FliesSound;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.SharedDescriptors;
import zombie.SoundManager;
import zombie.Lua.LuaEventManager;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.SurvivorDesc;
import zombie.characters.Talker;
import zombie.characters.AttachedItems.AttachedItem;
import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.AttachedItems.AttachedLocationGroup;
import zombie.characters.AttachedItems.AttachedLocations;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.WornItems.WornItems;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.opengl.Shader;
import zombie.core.physics.Transform;
import zombie.core.skinnedmodel.DeadBodyAtlas;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LineDrawer;
import zombie.debug.LogSeverity;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class IsoDeadBody extends IsoMovingObject implements Talker, IHumanVisual {
   public static final int MAX_ROT_STAGES = 3;
   private boolean bFemale;
   private boolean wasZombie;
   private boolean bFakeDead;
   private boolean bCrawling;
   private Color SpeakColor;
   private float SpeakTime;
   private int m_persistentOutfitID;
   private SurvivorDesc desc;
   private final HumanVisual humanVisual;
   private WornItems wornItems;
   private AttachedItems attachedItems;
   private float deathTime;
   private long realWorldDeathTime;
   private float reanimateTime;
   private IsoPlayer player;
   private boolean fallOnFront;
   private boolean wasSkeleton;
   private InventoryItem primaryHandItem;
   private InventoryItem secondaryHandItem;
   private float m_angle;
   private int m_zombieRotStageAtDeath;
   private short onlineID;
   private static final ThreadLocal tempZombie = new ThreadLocal() {
      public IsoZombie initialValue() {
         return new IsoZombie((IsoCell)null);
      }
   };
   private static ColorInfo inf = new ColorInfo();
   public Texture atlasTex;
   private static Texture DropShadow = null;
   private static final float HIT_TEST_WIDTH = 0.3F;
   private static final float HIT_TEST_HEIGHT = 0.9F;
   private static final Quaternionf _rotation = new Quaternionf();
   private static final Transform _transform = new Transform();
   private static final Vector3f _UNIT_Z = new Vector3f(0.0F, 0.0F, 1.0F);
   private static final Vector3f _tempVec3f_1 = new Vector3f();
   private static final Vector3f _tempVec3f_2 = new Vector3f();
   private float burnTimer;
   public boolean Speaking;
   public String sayLine;
   private static ArrayList AllBodies = new ArrayList(256);
   private static final ConcurrentHashMap ClientBodies = new ConcurrentHashMap();

   public static boolean isDead(short var0) {
      return ClientBodies.containsKey(var0);
   }

   public String getObjectName() {
      return "DeadBody";
   }

   public IsoDeadBody(IsoGameCharacter var1) {
      this(var1, false);
   }

   public IsoDeadBody(IsoGameCharacter var1, boolean var2) {
      super(var1.getCell(), false);
      this.bFemale = false;
      this.wasZombie = false;
      this.bFakeDead = false;
      this.bCrawling = false;
      this.SpeakTime = 0.0F;
      this.humanVisual = new HumanVisual(this);
      this.deathTime = -1.0F;
      this.reanimateTime = -1.0F;
      this.fallOnFront = false;
      this.wasSkeleton = false;
      this.primaryHandItem = null;
      this.secondaryHandItem = null;
      this.m_zombieRotStageAtDeath = 1;
      this.onlineID = -1;
      this.burnTimer = 0.0F;
      this.Speaking = false;
      this.sayLine = "";
      IsoZombie var3 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      this.setFallOnFront(var1.isFallOnFront());
      if (!GameClient.bClient && !GameServer.bServer && var3 != null && var3.bCrawling) {
         if (!var3.isReanimate()) {
            this.setFallOnFront(true);
         }

         this.bCrawling = true;
      }

      IsoGridSquare var4 = var1.getCurrentSquare();
      if (var4 != null) {
         if (var1.getZ() < 0.0F) {
            DebugLog.General.error("invalid z-coordinate %d,%d,%d", var1.x, var1.y, var1.z);
            var1.setZ(0.0F);
         }

         this.square = var4;
         this.current = var4;
         if (var1 instanceof IsoPlayer) {
            ((IsoPlayer)var1).removeSaveFile();
         }

         var4.getStaticMovingObjects().add(this);
         if (var1 instanceof IsoSurvivor) {
            IsoWorld var10000 = IsoWorld.instance;
            var10000.TotalSurvivorNights += ((IsoSurvivor)var1).nightsSurvived;
            ++IsoWorld.instance.TotalSurvivorsDead;
            if (IsoWorld.instance.SurvivorSurvivalRecord < ((IsoSurvivor)var1).nightsSurvived) {
               IsoWorld.instance.SurvivorSurvivalRecord = ((IsoSurvivor)var1).nightsSurvived;
            }
         }

         this.bFemale = var1.isFemale();
         this.wasZombie = var3 != null;
         if (this.wasZombie) {
            this.bFakeDead = var3.isFakeDead();
            this.wasSkeleton = var3.isSkeleton();
         }

         this.dir = var1.dir;
         this.m_angle = var1.getAnimAngleRadians();
         this.Collidable = false;
         this.x = var1.getX();
         this.y = var1.getY();
         this.z = var1.getZ();
         this.nx = this.x;
         this.ny = this.y;
         this.offsetX = var1.offsetX;
         this.offsetY = var1.offsetY;
         this.solid = false;
         this.shootable = false;
         this.onlineID = var1.getOnlineID();
         this.OutlineOnMouseover = true;
         this.setContainer(var1.getInventory());
         this.setWornItems(var1.getWornItems());
         this.setAttachedItems(var1.getAttachedItems());
         if (var1 instanceof IHumanVisual) {
            this.getHumanVisual().copyFrom(((IHumanVisual)var1).getHumanVisual());
         }

         if (Core.bDebug) {
            DebugLog.log(DebugType.Death, "Corpse create: " + this.getDescription());
         }

         var1.setInventory(new ItemContainer());
         var1.clearWornItems();
         var1.clearAttachedItems();
         this.m_zombieRotStageAtDeath = this.getHumanVisual().zombieRotStage;
         if (!this.container.bExplored) {
            this.container.setExplored(var1 instanceof IsoPlayer || var1 instanceof IsoZombie && ((IsoZombie)var1).isReanimatedPlayer());
         }

         boolean var5 = var1.isOnFire();
         int var6;
         if (var1 instanceof IsoZombie) {
            this.m_persistentOutfitID = var1.getPersistentOutfitID();
            if (!var2) {
               if (GameServer.bServer) {
                  GameServer.sendKillZombie((IsoZombie)var1);
               } else {
                  for(var6 = 0; var6 < IsoPlayer.numPlayers; ++var6) {
                     IsoPlayer var7 = IsoPlayer.players[var6];
                     if (var7 != null && var7.ReanimatedCorpse == var1) {
                        var7.ReanimatedCorpse = null;
                        var7.ReanimatedCorpseID = -1;
                     }
                  }

                  if (!GameClient.bClient && var1.emitter != null) {
                     var1.emitter.tick();
                  }
               }
            }
         } else {
            if (var1 instanceof IsoSurvivor) {
               this.getCell().getSurvivorList().remove(var1);
            }

            this.desc = new SurvivorDesc(var1.getDescriptor());
            if (var1 instanceof IsoPlayer) {
               if (GameServer.bServer) {
                  this.player = (IsoPlayer)var1;
               } else if (!GameClient.bClient && ((IsoPlayer)var1).isLocalPlayer()) {
                  this.player = (IsoPlayer)var1;
               }
            }
         }

         var1.removeFromWorld();
         var1.removeFromSquare();
         this.sayLine = var1.getSayLine();
         this.SpeakColor = var1.getSpeakColour();
         this.SpeakTime = var1.getSpeakTime();
         this.Speaking = var1.isSpeaking();
         if (var5) {
            if (!GameClient.bClient && SandboxOptions.instance.FireSpread.getValue()) {
               IsoFireManager.StartFire(this.getCell(), this.getSquare(), true, 100, 500);
            }

            this.container.setExplored(true);
         }

         if (!var2 && !GameServer.bServer) {
            LuaEventManager.triggerEvent("OnContainerUpdate", this);
         }

         if (var1 instanceof IsoPlayer) {
            ((IsoPlayer)var1).bDeathFinished = true;
         }

         this.deathTime = (float)GameTime.getInstance().getWorldAgeHours();
         this.realWorldDeathTime = System.currentTimeMillis();
         this.setEatingZombies(var1.getEatingZombies());
         int var12;
         if (!this.wasZombie) {
            ArrayList var11 = new ArrayList();

            for(var12 = -2; var12 < 2; ++var12) {
               for(int var8 = -2; var8 < 2; ++var8) {
                  IsoGridSquare var9 = var4.getCell().getGridSquare(var4.x + var12, var4.y + var8, var4.z);
                  if (var9 != null) {
                     for(int var10 = 0; var10 < var9.getMovingObjects().size(); ++var10) {
                        if (var9.getMovingObjects().get(var10) instanceof IsoZombie) {
                           var11.add((IsoMovingObject)var9.getMovingObjects().get(var10));
                        }
                     }
                  }
               }
            }

            for(var12 = 0; var12 < var11.size(); ++var12) {
               ((IsoZombie)var11.get(var12)).pathToLocationF(this.getX() + Rand.Next(-0.3F, 0.3F), this.getY() + Rand.Next(-0.3F, 0.3F), this.getZ());
               ((IsoZombie)var11.get(var12)).bodyToEat = this;
            }
         }

         if (!GameClient.bClient) {
            var6 = 0;

            for(var12 = 0; var12 < AllBodies.size(); ++var12) {
               IsoDeadBody var13 = (IsoDeadBody)AllBodies.get(var12);
               if (var13.deathTime >= this.deathTime) {
                  break;
               }

               ++var6;
            }

            AllBodies.add(var6, this);
         } else if (this.wasZombie) {
            ClientBodies.put(this.onlineID, this);
         }

         if (!GameServer.bServer) {
            FliesSound.instance.corpseAdded((int)this.getX(), (int)this.getY(), (int)this.getZ());
         }

      }
   }

   public IsoDeadBody(IsoCell var1) {
      super(var1, false);
      this.bFemale = false;
      this.wasZombie = false;
      this.bFakeDead = false;
      this.bCrawling = false;
      this.SpeakTime = 0.0F;
      this.humanVisual = new HumanVisual(this);
      this.deathTime = -1.0F;
      this.reanimateTime = -1.0F;
      this.fallOnFront = false;
      this.wasSkeleton = false;
      this.primaryHandItem = null;
      this.secondaryHandItem = null;
      this.m_zombieRotStageAtDeath = 1;
      this.onlineID = -1;
      this.burnTimer = 0.0F;
      this.Speaking = false;
      this.sayLine = "";
      this.SpeakColor = Color.white;
      this.solid = false;
      this.shootable = false;
      BodyLocationGroup var2 = BodyLocations.getGroup("Human");
      this.wornItems = new WornItems(var2);
      AttachedLocationGroup var3 = AttachedLocations.getGroup("Human");
      this.attachedItems = new AttachedItems(var3);
   }

   public HumanVisual getHumanVisual() {
      return this.humanVisual;
   }

   public void getItemVisuals(ItemVisuals var1) {
      this.wornItems.getItemVisuals(var1);
   }

   public boolean isFemale() {
      return this.bFemale;
   }

   public boolean isZombie() {
      return this.wasZombie;
   }

   public boolean isCrawling() {
      return this.bCrawling;
   }

   public void setCrawling(boolean var1) {
      this.bCrawling = var1;
   }

   public boolean isFakeDead() {
      return this.bFakeDead;
   }

   public void setFakeDead(boolean var1) {
      this.bFakeDead = var1;
   }

   public short getOnlineID() {
      return this.onlineID;
   }

   public boolean isSkeleton() {
      return this.wasSkeleton;
   }

   public void setWornItems(WornItems var1) {
      this.wornItems = new WornItems(var1);
   }

   public WornItems getWornItems() {
      return this.wornItems;
   }

   public void setAttachedItems(AttachedItems var1) {
      this.attachedItems = new AttachedItems(var1);

      for(int var2 = 0; var2 < this.attachedItems.size(); ++var2) {
         AttachedItem var3 = this.attachedItems.get(var2);
         InventoryItem var4 = var3.getItem();
         if (!this.container.contains(var4) && !GameClient.bClient && !GameServer.bServer) {
            var4.setContainer(this.container);
            this.container.getItems().add(var4);
         }
      }

   }

   public AttachedItems getAttachedItems() {
      return this.attachedItems;
   }

   public InventoryItem getItem() {
      InventoryItem var1 = InventoryItemFactory.CreateItem("Base.CorpseMale");
      var1.storeInByteData(this);
      return var1;
   }

   private IsoSprite loadSprite(ByteBuffer var1) {
      String var2 = GameWindow.ReadString(var1);
      float var3 = var1.getFloat();
      float var4 = var1.getFloat();
      float var5 = var1.getFloat();
      float var6 = var1.getFloat();
      return null;
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.bFemale = var1.get() == 1;
      this.wasZombie = var1.get() == 1;
      boolean var4 = var1.get() == 1;
      if (var2 >= 171) {
         this.m_persistentOutfitID = var1.getInt();
      }

      if (var4 && var2 < 171) {
         short var5 = var1.getShort();
      }

      if (var1.get() == 1) {
         this.desc = new SurvivorDesc(true);
         this.desc.load(var1, var2, (IsoGameCharacter)null);
      }

      this.humanVisual.load(var1, var2);
      if (var1.get() == 1) {
         int var13 = var1.getInt();

         try {
            this.setContainer(new ItemContainer());
            this.container.ID = var13;
            ArrayList var6 = this.container.load(var1, var2);
            byte var7 = var1.get();

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = GameWindow.ReadString(var1);
               short var10 = var1.getShort();
               if (var10 >= 0 && var10 < var6.size() && this.wornItems.getBodyLocationGroup().getLocation(var9) != null) {
                  this.wornItems.setItem(var9, (InventoryItem)var6.get(var10));
               }
            }

            byte var14 = var1.get();

            for(int var15 = 0; var15 < var14; ++var15) {
               String var16 = GameWindow.ReadString(var1);
               short var11 = var1.getShort();
               if (var11 >= 0 && var11 < var6.size() && this.attachedItems.getGroup().getLocation(var16) != null) {
                  this.attachedItems.setItem(var16, (InventoryItem)var6.get(var11));
               }
            }
         } catch (Exception var12) {
            if (this.container != null) {
               DebugLog.log("Failed to stream in container ID: " + this.container.ID);
            }
         }
      }

      this.deathTime = var1.getFloat();
      this.reanimateTime = var1.getFloat();
      this.fallOnFront = var1.get() == 1;
      if (var4 && (GameClient.bClient || GameServer.bServer && ServerGUI.isCreated())) {
         this.checkClothing((InventoryItem)null);
      }

      this.wasSkeleton = var1.get() == 1;
      if (var2 >= 159) {
         this.m_angle = var1.getFloat();
      } else {
         this.m_angle = this.dir.toAngle();
      }

      if (var2 >= 166) {
         this.m_zombieRotStageAtDeath = var1.get() & 255;
      }

      if (var2 >= 168) {
         this.bCrawling = var1.get() == 1;
         this.bFakeDead = var1.get() == 1;
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.bFemale ? 1 : 0));
      var1.put((byte)(this.wasZombie ? 1 : 0));
      if (!GameServer.bServer && !GameClient.bClient) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
      }

      var1.putInt(this.m_persistentOutfitID);
      if (this.desc != null) {
         var1.put((byte)1);
         this.desc.save(var1);
      } else {
         var1.put((byte)0);
      }

      this.humanVisual.save(var1);
      if (this.container != null) {
         var1.put((byte)1);
         var1.putInt(this.container.ID);
         ArrayList var3 = this.container.save(var1);
         if (this.wornItems.size() > 127) {
            throw new RuntimeException("too many worn items");
         }

         var1.put((byte)this.wornItems.size());
         this.wornItems.forEach((var2x) -> {
            GameWindow.WriteString(var1, var2x.getLocation());
            var1.putShort((short)var3.indexOf(var2x.getItem()));
         });
         if (this.attachedItems.size() > 127) {
            throw new RuntimeException("too many attached items");
         }

         var1.put((byte)this.attachedItems.size());
         this.attachedItems.forEach((var2x) -> {
            GameWindow.WriteString(var1, var2x.getLocation());
            var1.putShort((short)var3.indexOf(var2x.getItem()));
         });
      } else {
         var1.put((byte)0);
      }

      var1.putFloat(this.deathTime);
      var1.putFloat(this.reanimateTime);
      var1.put((byte)(this.fallOnFront ? 1 : 0));
      var1.put((byte)(this.isSkeleton() ? 1 : 0));
      var1.putFloat(this.m_angle);
      var1.put((byte)this.m_zombieRotStageAtDeath);
      var1.put((byte)(this.bCrawling ? 1 : 0));
      var1.put((byte)(this.bFakeDead ? 1 : 0));
   }

   public void softReset() {
      this.square.RemoveTileObject(this);
   }

   public void renderlast() {
      if (this.Speaking) {
         float var1 = this.sx;
         float var2 = this.sy;
         var1 -= IsoCamera.getOffX();
         var2 -= IsoCamera.getOffY();
         var1 += 8.0F;
         var2 += 32.0F;
         if (this.sayLine != null) {
            TextManager.instance.DrawStringCentre(UIFont.Medium, (double)var1, (double)var2, this.sayLine, (double)this.SpeakColor.r, (double)this.SpeakColor.g, (double)this.SpeakColor.b, (double)this.SpeakColor.a);
         }
      }

   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      boolean var8 = this.isHighlighted();
      float var9;
      float var10;
      if (ModelManager.instance.bDebugEnableModels && ModelManager.instance.isCreated()) {
         if (this.atlasTex == null) {
            this.atlasTex = DeadBodyAtlas.instance.getBodyTexture(this);
            DeadBodyAtlas.instance.render();
         }

         if (this.atlasTex != null) {
            if (IsoSprite.globalOffsetX == -1.0F) {
               IsoSprite.globalOffsetX = -IsoCamera.frameState.OffX;
               IsoSprite.globalOffsetY = -IsoCamera.frameState.OffY;
            }

            var9 = IsoUtils.XToScreen(var1, var2, var3, 0);
            var10 = IsoUtils.YToScreen(var1, var2, var3, 0);
            this.sx = var9;
            this.sy = var10;
            var9 = this.sx + IsoSprite.globalOffsetX;
            var10 = this.sy + IsoSprite.globalOffsetY;
            if (Core.TileScale == 1) {
            }

            if (var8) {
               inf.r = this.getHighlightColor().r;
               inf.g = this.getHighlightColor().g;
               inf.b = this.getHighlightColor().b;
               inf.a = this.getHighlightColor().a;
            } else {
               inf.r = var4.r;
               inf.g = var4.g;
               inf.b = var4.b;
               inf.a = var4.a;
            }

            var4 = inf;
            if (!var8 && PerformanceSettings.LightingFrameSkip < 3 && this.getCurrentSquare() != null) {
               this.getCurrentSquare().interpolateLight(var4, var1 - (float)this.getCurrentSquare().getX(), var2 - (float)this.getCurrentSquare().getY());
            }

            if (GameServer.bServer && ServerGUI.isCreated()) {
               inf.set(1.0F, 1.0F, 1.0F, 1.0F);
            }

            this.atlasTex.render((float)((int)var9 - this.atlasTex.getWidth() / 2), (float)((int)var10 - this.atlasTex.getHeight() / 2), (float)this.atlasTex.getWidth(), (float)this.atlasTex.getHeight(), var4.r, var4.g, var4.b, var4.a, (Consumer)null);
            if (Core.bDebug && DebugOptions.instance.DeadBodyAtlasRender.getValue()) {
               LineDrawer.DrawIsoLine(var1 - 0.5F, var2, var3, var1 + 0.5F, var2, var3, 1.0F, 1.0F, 1.0F, 0.25F, 1);
               LineDrawer.DrawIsoLine(var1, var2 - 0.5F, var3, var1, var2 + 0.5F, var3, 1.0F, 1.0F, 1.0F, 0.25F, 1);
            }

            this.sx = var9;
            this.sy = var10;
            if (IsoObjectPicker.Instance.wasDirty) {
               this.renderObjectPicker(this.getX(), this.getY(), this.getZ(), var4);
            }
         }
      }

      float var11;
      if (Core.bDebug && DebugOptions.instance.DeadBodyAtlasRender.getValue()) {
         _rotation.setAngleAxis((double)this.m_angle + 1.5707963267948966D, 0.0D, 0.0D, 1.0D);
         _transform.setRotation(_rotation);
         _transform.origin.set(this.x, this.y, this.z);
         Vector3f var28 = _tempVec3f_1;
         _transform.basis.getColumn(1, var28);
         Vector3f var30 = _tempVec3f_2;
         var28.cross(_UNIT_Z, var30);
         var11 = 0.3F;
         float var12 = 0.9F;
         var28.x *= var12;
         var28.y *= var12;
         var30.x *= var11;
         var30.y *= var11;
         float var13 = var1 + var28.x;
         float var14 = var2 + var28.y;
         float var15 = var1 - var28.x;
         float var16 = var2 - var28.y;
         float var17 = var13 - var30.x;
         float var18 = var13 + var30.x;
         float var19 = var15 - var30.x;
         float var20 = var15 + var30.x;
         float var21 = var16 - var30.y;
         float var22 = var16 + var30.y;
         float var23 = var14 - var30.y;
         float var24 = var14 + var30.y;
         float var25 = 1.0F;
         float var26 = 1.0F;
         float var27 = 1.0F;
         if (this.isMouseOver((float)Mouse.getX(), (float)Mouse.getY())) {
            var27 = 0.0F;
            var25 = 0.0F;
         }

         LineDrawer.addLine(var17, var23, 0.0F, var18, var24, 0.0F, var25, var26, var27, (String)null, true);
         LineDrawer.addLine(var17, var23, 0.0F, var19, var21, 0.0F, var25, var26, var27, (String)null, true);
         LineDrawer.addLine(var18, var24, 0.0F, var20, var22, 0.0F, var25, var26, var27, (String)null, true);
         LineDrawer.addLine(var19, var21, 0.0F, var20, var22, 0.0F, var25, var26, var27, (String)null, true);
      }

      if (this.isFakeDead() && DebugOptions.instance.ZombieRenderFakeDead.getValue()) {
         var9 = IsoUtils.XToScreen(var1, var2, var3, 0) + IsoSprite.globalOffsetX;
         var10 = IsoUtils.YToScreen(var1, var2, var3, 0) + IsoSprite.globalOffsetY - (float)(16 * Core.TileScale);
         var11 = this.getFakeDeadWakeupHours() - (float)GameTime.getInstance().getWorldAgeHours();
         var11 = Math.max(var11, 0.0F);
         TextManager.instance.DrawStringCentre(UIFont.Medium, (double)var9, (double)var10, String.format("FakeDead %.2f", var11), 1.0D, 1.0D, 1.0D, 1.0D);
      }

      if (Core.bDebug && (DebugOptions.instance.MultiplayerShowZombieStatus.getValue() || DebugOptions.instance.MultiplayerShowPlayerStatus.getValue() || DebugOptions.instance.MultiplayerShowZombieOwner.getValue()) && this.onlineID != -1) {
         Color var29 = Colors.Yellow;
         var10 = IsoUtils.XToScreenExact(var1 + 0.4F, var2 + 0.4F, var3, 0);
         var11 = IsoUtils.YToScreenExact(var1 + 0.4F, var2 - 1.4F, var3, 0);
         TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var10, (double)var11, String.valueOf(this.onlineID), (double)var29.r, (double)var29.g, (double)var29.b, (double)var29.a);
         TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var10, (double)(var11 + 10.0F), String.format("x=%09.3f", var1), (double)var29.r, (double)var29.g, (double)var29.b, (double)var29.a);
         TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var10, (double)(var11 + 20.0F), String.format("y=%09.3f", var2), (double)var29.r, (double)var29.g, (double)var29.b, (double)var29.a);
         TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var10, (double)(var11 + 30.0F), String.format("z=%d", (byte)((int)var3)), (double)var29.r, (double)var29.g, (double)var29.b, (double)var29.a);
      }

   }

   public void renderShadow() {
      _rotation.setAngleAxis((double)this.m_angle + 1.5707963267948966D, 0.0D, 0.0D, 1.0D);
      _transform.setRotation(_rotation);
      _transform.origin.set(this.x, this.y, this.z);
      Vector3f var1 = _tempVec3f_1;
      _transform.basis.getColumn(1, var1);
      float var2 = 0.45F;
      float var3 = 1.4F;
      float var4 = 1.125F;
      int var5 = IsoCamera.frameState.playerIndex;
      ColorInfo var6 = this.square.lighting[var5].lightInfo();
      renderShadow(this.x, this.y, this.z, var1, var2, var3, var4, var6, this.getAlpha(var5));
   }

   public static void renderShadow(float var0, float var1, float var2, Vector3f var3, float var4, float var5, float var6, ColorInfo var7, float var8) {
      float var9 = var8 * ((var7.r + var7.g + var7.b) / 3.0F);
      var9 *= 0.66F;
      var3.normalize();
      Vector3f var10 = _tempVec3f_2;
      var3.cross(_UNIT_Z, var10);
      var4 = Math.max(0.65F, var4);
      var5 = Math.max(var5, 0.65F);
      var6 = Math.max(var6, 0.65F);
      var10.x *= var4;
      var10.y *= var4;
      float var11 = var0 + var3.x * var5;
      float var12 = var1 + var3.y * var5;
      float var13 = var0 - var3.x * var6;
      float var14 = var1 - var3.y * var6;
      float var15 = var11 - var10.x;
      float var16 = var11 + var10.x;
      float var17 = var13 - var10.x;
      float var18 = var13 + var10.x;
      float var19 = var14 - var10.y;
      float var20 = var14 + var10.y;
      float var21 = var12 - var10.y;
      float var22 = var12 + var10.y;
      float var23 = IsoUtils.XToScreenExact(var15, var21, var2, 0);
      float var24 = IsoUtils.YToScreenExact(var15, var21, var2, 0);
      float var25 = IsoUtils.XToScreenExact(var16, var22, var2, 0);
      float var26 = IsoUtils.YToScreenExact(var16, var22, var2, 0);
      float var27 = IsoUtils.XToScreenExact(var18, var20, var2, 0);
      float var28 = IsoUtils.YToScreenExact(var18, var20, var2, 0);
      float var29 = IsoUtils.XToScreenExact(var17, var19, var2, 0);
      float var30 = IsoUtils.YToScreenExact(var17, var19, var2, 0);
      if (DropShadow == null) {
         DropShadow = Texture.getSharedTexture("media/textures/NewShadow.png");
      }

      SpriteRenderer.instance.renderPoly(DropShadow, var23, var24, var25, var26, var27, var28, var29, var30, 0.0F, 0.0F, 0.0F, var9);
      if (DebugOptions.instance.IsoSprite.DropShadowEdges.getValue()) {
         LineDrawer.addLine(var15, var21, var2, var16, var22, var2, 1, 1, 1, (String)null);
         LineDrawer.addLine(var16, var22, var2, var18, var20, var2, 1, 1, 1, (String)null);
         LineDrawer.addLine(var18, var20, var2, var17, var19, var2, 1, 1, 1, (String)null);
         LineDrawer.addLine(var17, var19, var2, var15, var21, var2, 1, 1, 1, (String)null);
      }

   }

   public void renderObjectPicker(float var1, float var2, float var3, ColorInfo var4) {
      if (this.atlasTex != null) {
         IsoObjectPicker.Instance.Add((int)(this.sx - (float)(this.atlasTex.getWidth() / 2)), (int)(this.sy - (float)(this.atlasTex.getHeight() / 2)), this.atlasTex.getWidthOrig(), this.atlasTex.getHeightOrig(), this.square, this, false, 1.0F, 1.0F);
      }
   }

   public boolean isMouseOver(float var1, float var2) {
      _rotation.setAngleAxis((double)this.m_angle + 1.5707963267948966D, 0.0D, 0.0D, 1.0D);
      _transform.setRotation(_rotation);
      _transform.origin.set(this.x, this.y, this.z);
      _transform.inverse();
      Vector3f var3 = _tempVec3f_1.set(IsoUtils.XToIso(var1, var2, this.z), IsoUtils.YToIso(var1, var2, this.z), this.z);
      _transform.transform(var3);
      return var3.x >= -0.3F && var3.y >= -0.9F && var3.x < 0.3F && var3.y < 0.9F;
   }

   public void Burn() {
      if (!GameClient.bClient) {
         if (this.getSquare() != null && this.getSquare().getProperties().Is(IsoFlagType.burning)) {
            this.burnTimer += GameTime.instance.getMultipliedSecondsSinceLastUpdate();
            if (this.burnTimer >= 10.0F) {
               boolean var1 = true;

               for(int var2 = 0; var2 < this.getSquare().getObjects().size(); ++var2) {
                  IsoObject var3 = (IsoObject)this.getSquare().getObjects().get(var2);
                  if (var3.getName() != null && "burnedCorpse".equals(var3.getName())) {
                     var1 = false;
                     break;
                  }
               }

               if (var1) {
                  IsoObject var4 = new IsoObject(this.getSquare(), "floors_burnt_01_" + Rand.Next(1, 3), "burnedCorpse");
                  this.getSquare().getObjects().add(var4);
                  var4.transmitCompleteItemToClients();
               }

               if (GameServer.bServer) {
                  GameServer.sendRemoveCorpseFromMap(this);
               }

               this.getSquare().removeCorpse(this, true);
            }

         }
      }
   }

   public void setContainer(ItemContainer var1) {
      super.setContainer(var1);
      var1.type = this.bFemale ? "inventoryfemale" : "inventorymale";
      var1.Capacity = 8;
      var1.SourceGrid = this.square;
   }

   public void checkClothing(InventoryItem var1) {
      int var2;
      InventoryItem var3;
      for(var2 = 0; var2 < this.wornItems.size(); ++var2) {
         var3 = this.wornItems.getItemByIndex(var2);
         if (this.container == null || this.container.getItems().indexOf(var3) == -1) {
            this.wornItems.remove(var3);
            this.atlasTex = null;
            --var2;
         }
      }

      if (var1 == this.getPrimaryHandItem()) {
         this.setPrimaryHandItem((InventoryItem)null);
         this.atlasTex = null;
      }

      if (var1 == this.getSecondaryHandItem()) {
         this.setSecondaryHandItem((InventoryItem)null);
         this.atlasTex = null;
      }

      for(var2 = 0; var2 < this.attachedItems.size(); ++var2) {
         var3 = this.attachedItems.getItemByIndex(var2);
         if (this.container == null || this.container.getItems().indexOf(var3) == -1) {
            this.attachedItems.remove(var3);
            this.atlasTex = null;
            --var2;
         }
      }

   }

   public boolean IsSpeaking() {
      return this.Speaking;
   }

   public void Say(String var1) {
      this.SpeakTime = (float)(var1.length() * 4);
      if (this.SpeakTime < 60.0F) {
         this.SpeakTime = 60.0F;
      }

      this.sayLine = var1;
      this.Speaking = true;
   }

   public String getSayLine() {
      return this.sayLine;
   }

   public String getTalkerType() {
      return "Talker";
   }

   public void addToWorld() {
      super.addToWorld();
      if (!GameServer.bServer) {
         FliesSound.instance.corpseAdded((int)this.getX(), (int)this.getY(), (int)this.getZ());
      }

      if (!GameClient.bClient) {
         if (this.reanimateTime > 0.0F) {
            this.getCell().addToStaticUpdaterObjectList(this);
            if (Core.bDebug) {
               DebugLog.log("reanimate: addToWorld reanimateTime=" + this.reanimateTime + this);
            }
         }

         float var1 = (float)GameTime.getInstance().getWorldAgeHours();
         if (this.deathTime < 0.0F) {
            this.deathTime = var1;
         }

         if (this.deathTime > var1) {
            this.deathTime = var1;
         }

         int var2 = 0;

         for(int var3 = 0; var3 < AllBodies.size() && !(((IsoDeadBody)AllBodies.get(var3)).deathTime >= this.deathTime); ++var3) {
            ++var2;
         }

         AllBodies.add(var2, this);
      }
   }

   public void removeFromWorld() {
      if (!GameServer.bServer) {
         FliesSound.instance.corpseRemoved((int)this.getX(), (int)this.getY(), (int)this.getZ());
      }

      if (!GameClient.bClient) {
         AllBodies.remove(this);
      }

      super.removeFromWorld();
   }

   public static void updateBodies() {
      if (GameClient.bClient) {
         float var10 = (float)GameTime.getInstance().getWorldAgeHours() - 0.1F;
         ClientBodies.values().removeIf((var1x) -> {
            return var10 > var1x.deathTime;
         });
      } else {
         if (Core.bDebug) {
         }

         boolean var0 = false;
         float var1 = (float)SandboxOptions.instance.HoursForCorpseRemoval.getValue();
         if (!(var1 <= 0.0F)) {
            float var2 = var1 / 3.0F;
            float var3 = (float)GameTime.getInstance().getWorldAgeHours();

            for(int var4 = 0; var4 < AllBodies.size(); ++var4) {
               IsoDeadBody var5 = (IsoDeadBody)AllBodies.get(var4);
               if (var5.deathTime > var3) {
                  var5.deathTime = var3;
                  var5.getHumanVisual().zombieRotStage = var5.m_zombieRotStageAtDeath;
               }

               if (!var5.updateFakeDead() && (ServerOptions.instance.RemovePlayerCorpsesOnCorpseRemoval.getValue() || var5.wasZombie)) {
                  int var6 = var5.getHumanVisual().zombieRotStage;
                  var5.updateRotting(var3, var2, var0);
                  int var7 = var5.getHumanVisual().zombieRotStage;
                  float var8 = var3 - var5.deathTime;
                  if (!(var8 < var1 + (var5.isSkeleton() ? var2 : 0.0F))) {
                     if (var0) {
                        int var9 = (int)(var8 / var2);
                        DebugLog.General.debugln("%s REMOVE %d -> %d age=%.2f stages=%d", var5, var6, var7, var8, var9);
                     }

                     if (GameServer.bServer) {
                        GameServer.sendRemoveCorpseFromMap(var5);
                     }

                     var5.removeFromWorld();
                     var5.removeFromSquare();
                     --var4;
                  }
               }
            }

         }
      }
   }

   private void updateRotting(float var1, float var2, boolean var3) {
      if (!this.isSkeleton()) {
         float var4 = var1 - this.deathTime;
         int var5 = (int)(var4 / var2);
         int var6 = this.m_zombieRotStageAtDeath + var5;
         if (var5 < 3) {
            var6 = PZMath.clamp(var6, 1, 3);
         }

         if (var6 <= 3 && var6 != this.getHumanVisual().zombieRotStage) {
            if (var3) {
               DebugLog.General.debugln("%s zombieRotStage %d -> %d age=%.2f stages=%d", this, this.getHumanVisual().zombieRotStage, var6, var4, var5);
            }

            this.getHumanVisual().zombieRotStage = var6;
            this.atlasTex = null;
            if (GameServer.bServer) {
               GameServer.sendRemoveCorpseFromMap(this);
               GameServer.sendCorpse(this);
            }

         } else {
            if (var5 == 3 && Rand.NextBool(7)) {
               if (var3) {
                  DebugLog.General.debugln("%s zombieRotStage %d -> x age=%.2f stages=%d", this, this.getHumanVisual().zombieRotStage, var4, var5);
               }

               this.getHumanVisual().setBeardModel("");
               this.getHumanVisual().setHairModel("");
               this.getHumanVisual().setSkinTextureIndex(Rand.Next(1, 3));
               this.wasSkeleton = true;
               this.getWornItems().clear();
               this.getAttachedItems().clear();
               this.getContainer().clear();
               this.atlasTex = null;
               if (GameServer.bServer) {
                  GameServer.sendRemoveCorpseFromMap(this);
                  GameServer.sendCorpse(this);
               }
            }

         }
      }
   }

   private boolean updateFakeDead() {
      if (!this.isFakeDead()) {
         return false;
      } else if (this.isSkeleton()) {
         return false;
      } else if ((double)this.getFakeDeadWakeupHours() > GameTime.getInstance().getWorldAgeHours()) {
         return false;
      } else if (!this.isPlayerNearby()) {
         return false;
      } else {
         this.reanimateNow();
         return true;
      }
   }

   private float getFakeDeadWakeupHours() {
      return this.deathTime + 0.5F;
   }

   private boolean isPlayerNearby() {
      if (!GameServer.bServer) {
         IsoGridSquare var1 = this.getSquare();

         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            IsoPlayer var3 = IsoPlayer.players[var2];
            boolean var4 = var1 != null && var1.isCanSee(var2);
            if (this.isPlayerNearby(var3, var4)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isPlayerNearby(IsoPlayer var1, boolean var2) {
      if (!var2) {
         return false;
      } else if (var1 != null && !var1.isDead()) {
         if (!var1.isGhostMode() && !var1.isInvisible()) {
            if (var1.getVehicle() != null) {
               return false;
            } else {
               float var3 = var1.DistToSquared(this);
               return !(var3 < 4.0F) && !(var3 > 16.0F);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void setReanimateTime(float var1) {
      this.reanimateTime = var1;
      if (!GameClient.bClient) {
         ArrayList var2 = IsoWorld.instance.CurrentCell.getStaticUpdaterObjectList();
         if (this.reanimateTime > 0.0F && !var2.contains(this)) {
            var2.add(this);
         } else if (this.reanimateTime <= 0.0F && var2.contains(this)) {
            var2.remove(this);
         }

      }
   }

   private float getReanimateDelay() {
      float var1 = 0.0F;
      float var2 = 0.0F;
      switch(SandboxOptions.instance.Lore.Reanimate.getValue()) {
      case 1:
      default:
         break;
      case 2:
         var2 = 0.5F;
         break;
      case 3:
         var2 = 0.016666668F;
         break;
      case 4:
         var2 = 12.0F;
         break;
      case 5:
         var1 = 48.0F;
         var2 = 72.0F;
         break;
      case 6:
         var1 = 168.0F;
         var2 = 336.0F;
      }

      if (Core.bTutorial) {
         var2 = 0.25F;
      }

      return var1 == var2 ? var1 : Rand.Next(var1, var2);
   }

   public void reanimateLater() {
      this.setReanimateTime((float)GameTime.getInstance().getWorldAgeHours() + this.getReanimateDelay());
   }

   public void reanimateNow() {
      this.setReanimateTime((float)GameTime.getInstance().getWorldAgeHours());
      this.realWorldDeathTime = 0L;
   }

   public void update() {
      if (this.current == null) {
         this.current = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)this.y, (double)this.z);
      }

      if (!GameClient.bClient) {
         if (this.reanimateTime > 0.0F) {
            if (System.currentTimeMillis() - this.realWorldDeathTime < 10000L) {
               return;
            }

            float var1 = (float)GameTime.getInstance().getWorldAgeHours();
            if (this.reanimateTime <= var1) {
               this.reanimate();
            }
         }

      }
   }

   public void reanimate() {
      short var1 = -1;
      if (GameServer.bServer) {
         var1 = ServerMap.instance.getUniqueZombieId();
         if (var1 == -1) {
            return;
         }
      }

      SurvivorDesc var2 = new SurvivorDesc();
      var2.setFemale(this.isFemale());
      IsoZombie var3 = new IsoZombie(IsoWorld.instance.CurrentCell, var2, -1);
      var3.setPersistentOutfitID(this.m_persistentOutfitID);
      if (this.container == null) {
         this.container = new ItemContainer();
      }

      var3.setInventory(this.container);
      this.container = null;
      var3.getHumanVisual().copyFrom(this.getHumanVisual());
      var3.getWornItems().copyFrom(this.wornItems);
      this.wornItems.clear();
      var3.getAttachedItems().copyFrom(this.attachedItems);
      this.attachedItems.clear();
      var3.setX(this.getX());
      var3.setY(this.getY());
      var3.setZ(this.getZ());
      var3.setCurrent(this.getCurrentSquare());
      var3.setMovingSquareNow();
      var3.setDir(this.dir);
      var3.getAnimationPlayer().setTargetAngle(this.m_angle);
      var3.getAnimationPlayer().setAngleToTarget();
      var3.setForwardDirection(Vector2.fromLengthDirection(1.0F, this.m_angle));
      var3.setAlphaAndTarget(1.0F);
      Arrays.fill(var3.IsVisibleToPlayer, true);
      var3.setOnFloor(true);
      var3.setCrawler(this.bCrawling);
      var3.setCanWalk(!this.bCrawling);
      var3.walkVariant = "ZombieWalk";
      var3.DoZombieStats();
      var3.setFallOnFront(this.isFallOnFront());
      if (SandboxOptions.instance.Lore.Toughness.getValue() == 1) {
         var3.setHealth(3.5F + Rand.Next(0.0F, 0.3F));
      }

      if (SandboxOptions.instance.Lore.Toughness.getValue() == 2) {
         var3.setHealth(1.8F + Rand.Next(0.0F, 0.3F));
      }

      if (SandboxOptions.instance.Lore.Toughness.getValue() == 3) {
         var3.setHealth(0.5F + Rand.Next(0.0F, 0.3F));
      }

      if (GameServer.bServer) {
         var3.OnlineID = var1;
         ServerMap.instance.ZombieMap.put(var3.OnlineID, var3);
      }

      if (this.isFakeDead()) {
         var3.setWasFakeDead(true);
      } else {
         var3.setReanimatedPlayer(true);
         var3.getDescriptor().setID(0);
         SharedDescriptors.createPlayerZombieDescriptor(var3);
      }

      var3.setReanimate(this.bCrawling);
      if (!IsoWorld.instance.CurrentCell.getZombieList().contains(var3)) {
         IsoWorld.instance.CurrentCell.getZombieList().add(var3);
      }

      if (!IsoWorld.instance.CurrentCell.getObjectList().contains(var3) && !IsoWorld.instance.CurrentCell.getAddList().contains(var3)) {
         IsoWorld.instance.CurrentCell.getAddList().add(var3);
      }

      if (GameServer.bServer) {
         if (this.player != null) {
            this.player.ReanimatedCorpse = var3;
            this.player.ReanimatedCorpseID = var3.OnlineID;
         }

         GameServer.sendRemoveCorpseFromMap(this);
      }

      this.removeFromWorld();
      this.removeFromSquare();
      LuaEventManager.triggerEvent("OnContainerUpdate");
      var3.setReanimateTimer(0.0F);
      var3.onWornItemsChanged();
      if (this.player != null) {
         if (GameServer.bServer) {
            GameServer.sendReanimatedZombieID(this.player, var3);
         } else if (!GameClient.bClient && this.player.isLocalPlayer()) {
            this.player.ReanimatedCorpse = var3;
         }

         this.player.setLeaveBodyTimedown(3601.0F);
      }

      var3.actionContext.update();
      float var4 = GameTime.getInstance().FPSMultiplier;
      GameTime.getInstance().FPSMultiplier = 100.0F;

      try {
         var3.advancedAnimator.update();
      } finally {
         GameTime.getInstance().FPSMultiplier = var4;
      }

      if (this.isFakeDead() && SoundManager.instance.isListenerInRange(this.x, this.y, 20.0F) && !GameServer.bServer) {
         var3.parameterZombieState.setState(ParameterZombieState.State.Reanimate);
      }

      if (Core.bDebug) {
         DebugLog.log(DebugType.Death, String.format("Corpse reanimate: Corpse(%d) Zombie(%d): items=%d", this.getOnlineID(), var3.getOnlineID(), var3.getInventory().getItems().size()));
      }

   }

   public static void Reset() {
      AllBodies.clear();
   }

   public void Collision(Vector2 var1, IsoObject var2) {
      if (var2 instanceof BaseVehicle) {
         BaseVehicle var3 = (BaseVehicle)var2;
         float var4 = 15.0F;
         Vector3f var5 = (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc();
         Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc();
         var3.getLinearVelocity(var5);
         var5.y = 0.0F;
         var6.set(var3.x - this.x, 0.0F, var3.z - this.z);
         var6.normalize();
         var5.mul((Vector3fc)var6);
         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var6);
         float var7 = var5.length();
         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var5);
         var7 = Math.min(var7, var4);
         if (var7 < 0.05F) {
            return;
         }

         if (Math.abs(var3.getCurrentSpeedKmHour()) > 20.0F) {
            var3.doChrHitImpulse(this);
         }
      }

   }

   public boolean isFallOnFront() {
      return this.fallOnFront;
   }

   public void setFallOnFront(boolean var1) {
      this.fallOnFront = var1;
   }

   public InventoryItem getPrimaryHandItem() {
      return this.primaryHandItem;
   }

   public void setPrimaryHandItem(InventoryItem var1) {
      this.primaryHandItem = var1;
      this.updateContainerWithHandItems();
   }

   private void updateContainerWithHandItems() {
      if (this.getContainer() != null) {
         if (this.getPrimaryHandItem() != null) {
            this.getContainer().AddItem(this.getPrimaryHandItem());
         }

         if (this.getSecondaryHandItem() != null) {
            this.getContainer().AddItem(this.getSecondaryHandItem());
         }
      }

   }

   public InventoryItem getSecondaryHandItem() {
      return this.secondaryHandItem;
   }

   public void setSecondaryHandItem(InventoryItem var1) {
      this.secondaryHandItem = var1;
      this.updateContainerWithHandItems();
   }

   public float getAngle() {
      return this.m_angle;
   }

   public String getOutfitName() {
      return this.getHumanVisual().getOutfit() != null ? this.getHumanVisual().getOutfit().m_Name : null;
   }

   private String getDescription() {
      return String.format("Corpse: id=%d bFakeDead=%b bCrawling=%b isFallOnFront=%b (x=%f,y=%f,z=%f;a=%f) outfit=%d", this.onlineID, this.bFakeDead, this.bCrawling, this.fallOnFront, this.x, this.y, this.z, this.m_angle, this.m_persistentOutfitID);
   }

   public String readInventory(ByteBuffer var1) {
      String var2 = GameWindow.ReadString(var1);
      if (this.getContainer() != null && this.getWornItems() != null && this.getAttachedItems() != null) {
         this.getContainer().clear();
         this.getWornItems().clear();
         this.getAttachedItems().clear();
         boolean var3 = var1.get() == 1;
         if (var3) {
            try {
               ArrayList var4 = this.getContainer().load(var1, IsoWorld.getWorldVersion());
               this.getContainer().Capacity = 8;
               byte var5 = var1.get();

               for(int var6 = 0; var6 < var5; ++var6) {
                  String var7 = GameWindow.ReadStringUTF(var1);
                  short var8 = var1.getShort();
                  if (var8 >= 0 && var8 < var4.size() && this.getWornItems().getBodyLocationGroup().getLocation(var7) != null) {
                     this.getWornItems().setItem(var7, (InventoryItem)var4.get(var8));
                  }
               }

               byte var11 = var1.get();

               for(int var12 = 0; var12 < var11; ++var12) {
                  String var13 = GameWindow.ReadStringUTF(var1);
                  short var9 = var1.getShort();
                  if (var9 >= 0 && var9 < var4.size() && this.getAttachedItems().getGroup().getLocation(var13) != null) {
                     this.getAttachedItems().setItem(var13, (InventoryItem)var4.get(var9));
                  }
               }
            } catch (IOException var10) {
               DebugLog.Multiplayer.printException(var10, "ReadDeadBodyInventory error for dead body " + this.getOnlineID(), LogSeverity.Error);
            }
         }

         return var2;
      } else {
         return var2;
      }
   }
}
