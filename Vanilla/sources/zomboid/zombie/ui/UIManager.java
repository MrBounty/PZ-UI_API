package zombie.ui;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.krka.kahlua.vm.KahluaThread;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.Styles.TransparentStyle;
import zombie.core.Styles.UIFBOStyle;
import zombie.core.opengl.RenderThread;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureFBO;
import zombie.debug.DebugOptions;
import zombie.gameStates.GameLoadingState;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.areas.SafeHouse;
import zombie.network.CoopMaster;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.list.PZArrayUtil;

public final class UIManager {
   public static int lastMouseX = 0;
   public static int lastMouseY = 0;
   public static IsoObjectPicker.ClickObject Picked = null;
   public static Clock clock;
   public static final ArrayList UI = new ArrayList();
   public static ObjectTooltip toolTip = null;
   public static Texture mouseArrow;
   public static Texture mouseExamine;
   public static Texture mouseAttack;
   public static Texture mouseGrab;
   public static SpeedControls speedControls;
   public static UIDebugConsole DebugConsole;
   public static UIServerToolbox ServerToolbox;
   public static final MoodlesUI[] MoodleUI = new MoodlesUI[4];
   public static boolean bFadeBeforeUI = false;
   public static final ActionProgressBar[] ProgressBar = new ActionProgressBar[4];
   public static float FadeAlpha = 1.0F;
   public static int FadeInTimeMax = 180;
   public static int FadeInTime = 180;
   public static boolean FadingOut = false;
   public static Texture lastMouseTexture;
   public static IsoObject LastPicked = null;
   public static final ArrayList DoneTutorials = new ArrayList();
   public static float lastOffX = 0.0F;
   public static float lastOffY = 0.0F;
   public static ModalDialog Modal = null;
   public static boolean KeyDownZoomIn = false;
   public static boolean KeyDownZoomOut = false;
   public static boolean doTick;
   public static boolean VisibleAllUI = true;
   public static TextureFBO UIFBO;
   public static boolean useUIFBO = false;
   public static Texture black = null;
   public static boolean bSuspend = false;
   public static float lastAlpha = 10000.0F;
   public static final Vector2 PickedTileLocal = new Vector2();
   public static final Vector2 PickedTile = new Vector2();
   public static IsoObject RightDownObject = null;
   public static long uiUpdateTimeMS = 0L;
   public static long uiUpdateIntervalMS = 0L;
   public static long uiRenderTimeMS = 0L;
   public static long uiRenderIntervalMS = 0L;
   private static final ArrayList tutorialStack = new ArrayList();
   public static final ArrayList toTop = new ArrayList();
   public static KahluaThread defaultthread = null;
   public static KahluaThread previousThread = null;
   static final ArrayList toRemove = new ArrayList();
   static final ArrayList toAdd = new ArrayList();
   static int wheel = 0;
   static int lastwheel = 0;
   static final ArrayList debugUI = new ArrayList();
   static boolean bShowLuaDebuggerOnError = true;
   static final UIManager.Sync sync = new UIManager.Sync();
   private static boolean showPausedMessage = true;
   private static UIElement playerInventoryUI;
   private static UIElement playerLootUI;
   private static UIElement playerInventoryTooltip;
   private static UIElement playerLootTooltip;
   private static final UIManager.FadeInfo[] playerFadeInfo = new UIManager.FadeInfo[4];

   public static void AddUI(UIElement var0) {
      toRemove.remove(var0);
      toRemove.add(var0);
      toAdd.remove(var0);
      toAdd.add(var0);
   }

   public static void RemoveElement(UIElement var0) {
      toAdd.remove(var0);
      toRemove.remove(var0);
      toRemove.add(var0);
   }

   public static void closeContainers() {
   }

   public static void CloseContainers() {
   }

   public static void DrawTexture(Texture var0, double var1, double var3) {
      double var5 = var1 + (double)var0.offsetX;
      double var7 = var3 + (double)var0.offsetY;
      SpriteRenderer.instance.renderi(var0, (int)var5, (int)var7, var0.getWidth(), var0.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
   }

   public static void DrawTexture(Texture var0, double var1, double var3, double var5, double var7, double var9) {
      double var11 = var1 + (double)var0.offsetX;
      double var13 = var3 + (double)var0.offsetY;
      SpriteRenderer.instance.renderi(var0, (int)var11, (int)var13, (int)var5, (int)var7, 1.0F, 1.0F, 1.0F, (float)var9, (Consumer)null);
   }

   public static void FadeIn(double var0) {
      setFadeInTimeMax((double)((int)(var0 * 30.0D * (double)((float)PerformanceSettings.getLockFPS() / 30.0F))));
      setFadeInTime(getFadeInTimeMax());
      setFadingOut(false);
   }

   public static void FadeOut(double var0) {
      setFadeInTimeMax((double)((int)(var0 * 30.0D * (double)((float)PerformanceSettings.getLockFPS() / 30.0F))));
      setFadeInTime(getFadeInTimeMax());
      setFadingOut(true);
   }

   public static void CreateFBO(int var0, int var1) {
      if (Core.SafeMode) {
         useUIFBO = false;
      } else {
         if (useUIFBO && (UIFBO == null || UIFBO.getTexture().getWidth() != var0 || UIFBO.getTexture().getHeight() != var1)) {
            if (UIFBO != null) {
               RenderThread.invokeOnRenderContext(() -> {
                  UIFBO.destroy();
               });
            }

            try {
               UIFBO = createTexture((float)var0, (float)var1, false);
            } catch (Exception var3) {
               useUIFBO = false;
               var3.printStackTrace();
            }
         }

      }
   }

   public static TextureFBO createTexture(float var0, float var1, boolean var2) throws Exception {
      Texture var3;
      if (var2) {
         var3 = new Texture((int)var0, (int)var1, 16);
         TextureFBO var4 = new TextureFBO(var3);
         var4.destroy();
         return null;
      } else {
         var3 = new Texture((int)var0, (int)var1, 16);
         return new TextureFBO(var3);
      }
   }

   public static void init() {
      showPausedMessage = true;
      getUI().clear();
      debugUI.clear();
      clock = null;

      int var0;
      for(var0 = 0; var0 < 4; ++var0) {
         MoodleUI[var0] = null;
      }

      setSpeedControls(new SpeedControls());
      SpeedControls.instance = getSpeedControls();
      setbFadeBeforeUI(false);
      VisibleAllUI = true;

      for(var0 = 0; var0 < 4; ++var0) {
         playerFadeInfo[var0].setFadeBeforeUI(false);
         playerFadeInfo[var0].setFadeTime(0);
         playerFadeInfo[var0].setFadingOut(false);
      }

      setPicked((IsoObjectPicker.ClickObject)null);
      setLastPicked((IsoObject)null);
      RightDownObject = null;
      if (IsoPlayer.getInstance() != null) {
         if (!Core.GameMode.equals("LastStand") && !GameClient.bClient) {
            getUI().add(getSpeedControls());
         }

         if (!GameServer.bServer) {
            setToolTip(new ObjectTooltip());
            if (Core.getInstance().getOptionClockSize() == 2) {
               setClock(new Clock(Core.getInstance().getOffscreenWidth(0) - 166, 10));
            } else {
               setClock(new Clock(Core.getInstance().getOffscreenWidth(0) - 91, 10));
            }

            if (!Core.GameMode.equals("LastStand")) {
               getUI().add(getClock());
            }

            getUI().add(getToolTip());
            setDebugConsole(new UIDebugConsole(20, Core.getInstance().getScreenHeight() - 265));
            setServerToolbox(new UIServerToolbox(100, 200));
            if (Core.bDebug && DebugOptions.instance.UIDebugConsoleStartVisible.getValue()) {
               DebugConsole.setVisible(true);
            } else {
               DebugConsole.setVisible(false);
            }

            if (CoopMaster.instance.isRunning()) {
               ServerToolbox.setVisible(true);
            } else {
               ServerToolbox.setVisible(false);
            }

            for(var0 = 0; var0 < 4; ++var0) {
               MoodlesUI var1 = new MoodlesUI();
               setMoodleUI((double)var0, var1);
               var1.setVisible(true);
               getUI().add(var1);
            }

            getUI().add(getDebugConsole());
            getUI().add(getServerToolbox());
            setLastMouseTexture(getMouseArrow());
            resize();

            for(var0 = 0; var0 < 4; ++var0) {
               ActionProgressBar var2 = new ActionProgressBar(300, 300);
               var2.setRenderThisPlayerOnly(var0);
               setProgressBar((double)var0, var2);
               getUI().add(var2);
               var2.setValue(1.0F);
               var2.setVisible(false);
            }

            playerInventoryUI = null;
            playerLootUI = null;
            LuaEventManager.triggerEvent("OnCreateUI");
         }
      }
   }

   public static void render() {
      if (!useUIFBO || Core.getInstance().UIRenderThisFrame) {
         if (!bSuspend) {
            long var0 = System.currentTimeMillis();
            uiRenderIntervalMS = Math.min(var0 - uiRenderTimeMS, 1000L);
            uiRenderTimeMS = var0;
            UIElement.StencilLevel = 0;
            if (useUIFBO) {
               SpriteRenderer.instance.setDefaultStyle(UIFBOStyle.instance);
            }

            UITransition.UpdateAll();
            if (getBlack() == null) {
               setBlack(Texture.getSharedTexture("black.png"));
            }

            if (LuaManager.thread == defaultthread) {
               LuaEventManager.triggerEvent("OnPreUIDraw");
            }

            int var2 = Mouse.getXA();
            int var3 = Mouse.getYA();
            if (isbFadeBeforeUI()) {
               setFadeAlpha((double)(getFadeInTime().floatValue() / getFadeInTimeMax().floatValue()));
               if (getFadeAlpha() > 1.0D) {
                  setFadeAlpha(1.0D);
               }

               if (getFadeAlpha() < 0.0D) {
                  setFadeAlpha(0.0D);
               }

               if (isFadingOut()) {
                  setFadeAlpha(1.0D - getFadeAlpha());
               }

               if (IsoCamera.CamCharacter != null && getFadeAlpha() > 0.0D) {
                  DrawTexture(getBlack(), 0.0D, 0.0D, (double)Core.getInstance().getScreenWidth(), (double)Core.getInstance().getScreenHeight(), getFadeAlpha());
               }
            }

            setLastAlpha(getFadeAlpha().floatValue());

            int var4;
            for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
               if (IsoPlayer.players[var4] != null && playerFadeInfo[var4].isFadeBeforeUI()) {
                  playerFadeInfo[var4].render();
               }
            }

            for(var4 = 0; var4 < getUI().size(); ++var4) {
               if ((((UIElement)UI.get(var4)).isIgnoreLossControl() || !TutorialManager.instance.StealControl) && !((UIElement)UI.get(var4)).isFollowGameWorld()) {
                  try {
                     if (((UIElement)getUI().get(var4)).isDefaultDraw()) {
                        ((UIElement)getUI().get(var4)).render();
                     }
                  } catch (Exception var8) {
                     Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var8);
                  }
               }
            }

            if (getToolTip() != null) {
               getToolTip().render();
            }

            if (isShowPausedMessage() && GameTime.isGamePaused() && (getModal() == null || !Modal.isVisible()) && VisibleAllUI) {
               String var9 = Translator.getText("IGUI_GamePaused");
               int var5 = TextManager.instance.MeasureStringX(UIFont.Small, var9) + 32;
               int var6 = TextManager.instance.font.getLineHeight();
               int var7 = (int)Math.ceil((double)var6 * 1.5D);
               SpriteRenderer.instance.renderi((Texture)null, Core.getInstance().getScreenWidth() / 2 - var5 / 2, Core.getInstance().getScreenHeight() / 2 - var7 / 2, var5, var7, 0.0F, 0.0F, 0.0F, 0.75F, (Consumer)null);
               TextManager.instance.DrawStringCentre((double)(Core.getInstance().getScreenWidth() / 2), (double)(Core.getInstance().getScreenHeight() / 2 - var6 / 2), var9, 1.0D, 1.0D, 1.0D, 1.0D);
            }

            if (!isbFadeBeforeUI()) {
               setFadeAlpha(getFadeInTime() / getFadeInTimeMax());
               if (getFadeAlpha() > 1.0D) {
                  setFadeAlpha(1.0D);
               }

               if (getFadeAlpha() < 0.0D) {
                  setFadeAlpha(0.0D);
               }

               if (isFadingOut()) {
                  setFadeAlpha(1.0D - getFadeAlpha());
               }

               if (IsoCamera.CamCharacter != null && getFadeAlpha() > 0.0D) {
                  DrawTexture(getBlack(), 0.0D, 0.0D, (double)Core.getInstance().getScreenWidth(), (double)Core.getInstance().getScreenHeight(), getFadeAlpha());
               }
            }

            for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
               if (IsoPlayer.players[var4] != null && !playerFadeInfo[var4].isFadeBeforeUI()) {
                  playerFadeInfo[var4].render();
               }
            }

            if (LuaManager.thread == defaultthread) {
               LuaEventManager.triggerEvent("OnPostUIDraw");
            }

            if (useUIFBO) {
               SpriteRenderer.instance.setDefaultStyle(TransparentStyle.instance);
            }

         }
      }
   }

   public static void resize() {
      if (useUIFBO && UIFBO != null) {
         CreateFBO(Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight());
      }

      if (getClock() != null) {
         setLastOffX((float)Core.getInstance().getScreenWidth());
         setLastOffY((float)Core.getInstance().getScreenHeight());

         for(int var0 = 0; var0 < 4; ++var0) {
            int var1 = Core.getInstance().getScreenWidth();
            int var2 = Core.getInstance().getScreenHeight();
            byte var3;
            if (!Clock.instance.isVisible()) {
               var3 = 24;
            } else {
               var3 = 64;
            }

            if (var0 == 0 && IsoPlayer.numPlayers > 1 || var0 == 2) {
               var1 /= 2;
            }

            MoodleUI[var0].setX((double)(var1 - 50));
            if ((var0 == 0 || var0 == 1) && IsoPlayer.numPlayers > 1) {
               MoodleUI[var0].setY((double)var3);
            }

            if (var0 == 2 || var0 == 3) {
               MoodleUI[var0].setY((double)(var2 / 2 + var3));
            }

            MoodleUI[var0].setVisible(VisibleAllUI && IsoPlayer.players[var0] != null);
         }

         clock.resize();
         if (IsoPlayer.numPlayers == 1) {
            if (Core.getInstance().getOptionClockSize() == 2) {
               clock.setX((double)(Core.getInstance().getScreenWidth() - 166));
            } else {
               clock.setX((double)(Core.getInstance().getScreenWidth() - 91));
            }
         } else {
            if (Core.getInstance().getOptionClockSize() == 2) {
               clock.setX((double)((float)Core.getInstance().getScreenWidth() / 2.0F - 83.0F));
            } else {
               clock.setX((double)((float)Core.getInstance().getScreenWidth() / 2.0F - 45.5F));
            }

            clock.setY((double)(Core.getInstance().getScreenHeight() - 70));
         }

         if (IsoPlayer.numPlayers == 1) {
            speedControls.setX((double)(Core.getInstance().getScreenWidth() - 110));
         } else {
            speedControls.setX((double)(Core.getInstance().getScreenWidth() / 2 - 50));
         }

         if (IsoPlayer.numPlayers == 1 && !clock.isVisible()) {
            speedControls.setY(clock.getY());
         } else {
            speedControls.setY(clock.getY() + clock.getHeight() + 6.0D);
         }

         speedControls.setVisible(VisibleAllUI && !IsoPlayer.allPlayersDead());
      }
   }

   public static Vector2 getTileFromMouse(double var0, double var2, double var4) {
      PickedTile.x = IsoUtils.XToIso((float)(var0 - 0.0D), (float)(var2 - 0.0D), (float)var4);
      PickedTile.y = IsoUtils.YToIso((float)(var0 - 0.0D), (float)(var2 - 0.0D), (float)var4);
      PickedTileLocal.x = getPickedTile().x - (float)((int)getPickedTile().x);
      PickedTileLocal.y = getPickedTile().y - (float)((int)getPickedTile().y);
      PickedTile.x = (float)((int)getPickedTile().x);
      PickedTile.y = (float)((int)getPickedTile().y);
      return getPickedTile();
   }

   public static void update() {
      if (!bSuspend) {
         if (!toRemove.isEmpty()) {
            UI.removeAll(toRemove);
         }

         toRemove.clear();
         if (!toAdd.isEmpty()) {
            UI.addAll(toAdd);
         }

         toAdd.clear();
         setFadeInTime(getFadeInTime() - 1.0D);

         for(int var0 = 0; var0 < IsoPlayer.numPlayers; ++var0) {
            playerFadeInfo[var0].update();
         }

         long var13 = System.currentTimeMillis();
         if (var13 - uiUpdateTimeMS >= 100L) {
            doTick = true;
            uiUpdateIntervalMS = Math.min(var13 - uiUpdateTimeMS, 1000L);
            uiUpdateTimeMS = var13;
         } else {
            doTick = false;
         }

         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;
         int var5 = Mouse.getXA();
         int var6 = Mouse.getYA();
         int var7 = Mouse.getX();
         int var8 = Mouse.getY();
         tutorialStack.clear();

         int var9;
         UIElement var10;
         UIElement var11;
         for(var9 = UI.size() - 1; var9 >= 0; --var9) {
            var10 = (UIElement)UI.get(var9);
            if (var10.getParent() != null) {
               UI.remove(var9);
               throw new IllegalStateException();
            }

            if (var10.isFollowGameWorld()) {
               tutorialStack.add(var10);
            }

            if (var10 instanceof ObjectTooltip) {
               var11 = (UIElement)UI.remove(var9);
               UI.add(var11);
            }
         }

         for(var9 = 0; var9 < UI.size(); ++var9) {
            var10 = (UIElement)UI.get(var9);
            if (var10.alwaysOnTop || toTop.contains(var10)) {
               var11 = (UIElement)UI.remove(var9);
               --var9;
               toAdd.add(var11);
            }
         }

         if (!toAdd.isEmpty()) {
            UI.addAll(toAdd);
            toAdd.clear();
         }

         toTop.clear();

         for(var9 = 0; var9 < UI.size(); ++var9) {
            var10 = (UIElement)UI.get(var9);
            if (var10.alwaysBack) {
               var11 = (UIElement)UI.remove(var9);
               UI.add(0, var11);
            }
         }

         for(var9 = 0; var9 < tutorialStack.size(); ++var9) {
            UI.remove(tutorialStack.get(var9));
            UI.add(0, (UIElement)tutorialStack.get(var9));
         }

         if (Mouse.isLeftPressed()) {
            Core.UnfocusActiveTextEntryBox();

            for(var9 = UI.size() - 1; var9 >= 0; --var9) {
               var10 = (UIElement)UI.get(var9);
               if ((getModal() == null || getModal() == var10 || !getModal().isVisible()) && (var10.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && var10.isVisible()) {
                  if ((!((double)var5 >= var10.getX()) || !((double)var6 >= var10.getY()) || !((double)var5 < var10.getX() + var10.getWidth()) || !((double)var6 < var10.getY() + var10.getHeight())) && !var10.isCapture()) {
                     var10.onMouseDownOutside((double)(var5 - var10.getX().intValue()), (double)(var6 - var10.getY().intValue()));
                  } else if (var10.onMouseDown((double)(var5 - var10.getX().intValue()), (double)(var6 - var10.getY().intValue()))) {
                     var2 = true;
                     break;
                  }
               }
            }

            if (checkPicked() && !var2) {
               LuaEventManager.triggerEvent("OnObjectLeftMouseButtonDown", Picked.tile, BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
            }

            if (!var2) {
               LuaEventManager.triggerEvent("OnMouseDown", BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               CloseContainers();
               if (IsoWorld.instance.CurrentCell != null && !IsoWorld.instance.CurrentCell.DoBuilding(0, false) && getPicked() != null && !GameTime.isGamePaused() && IsoPlayer.getInstance() != null && !IsoPlayer.getInstance().isAiming() && !IsoPlayer.getInstance().isAsleep()) {
                  getPicked().tile.onMouseLeftClick(getPicked().lx, getPicked().ly);
               }
            } else {
               Mouse.UIBlockButtonDown(0);
            }
         }

         int var14;
         boolean var18;
         if (Mouse.isLeftReleased()) {
            var18 = false;

            for(var14 = UI.size() - 1; var14 >= 0; --var14) {
               var11 = (UIElement)UI.get(var14);
               if ((var11.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && var11.isVisible() && (getModal() == null || getModal() == var11 || !getModal().isVisible())) {
                  if ((!((double)var5 >= var11.getX()) || !((double)var6 >= var11.getY()) || !((double)var5 < var11.getX() + var11.getWidth()) || !((double)var6 < var11.getY() + var11.getHeight())) && !var11.isCapture()) {
                     var11.onMouseUpOutside((double)(var5 - var11.getX().intValue()), (double)(var6 - var11.getY().intValue()));
                  } else if (var11.onMouseUp((double)(var5 - var11.getX().intValue()), (double)(var6 - var11.getY().intValue()))) {
                     var18 = true;
                     break;
                  }
               }
            }

            if (!var18) {
               LuaEventManager.triggerEvent("OnMouseUp", BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               if (checkPicked() && !var2) {
                  LuaEventManager.triggerEvent("OnObjectLeftMouseButtonUp", Picked.tile, BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               }
            }
         }

         if (Mouse.isRightPressed()) {
            for(var9 = UI.size() - 1; var9 >= 0; --var9) {
               var10 = (UIElement)UI.get(var9);
               if (var10.isVisible() && (getModal() == null || getModal() == var10 || !getModal().isVisible())) {
                  if ((!((double)var5 >= var10.getX()) || !((double)var6 >= var10.getY()) || !((double)var5 < var10.getX() + var10.getWidth()) || !((double)var6 < var10.getY() + var10.getHeight())) && !var10.isCapture()) {
                     var10.onRightMouseDownOutside((double)(var5 - var10.getX().intValue()), (double)(var6 - var10.getY().intValue()));
                  } else if (var10.onRightMouseDown((double)(var5 - var10.getX().intValue()), (double)(var6 - var10.getY().intValue()))) {
                     var3 = true;
                     break;
                  }
               }
            }

            if (!var3) {
               LuaEventManager.triggerEvent("OnRightMouseDown", BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               if (checkPicked() && !var3) {
                  LuaEventManager.triggerEvent("OnObjectRightMouseButtonDown", Picked.tile, BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               }
            } else {
               Mouse.UIBlockButtonDown(1);
            }

            if (IsoWorld.instance.CurrentCell != null && getPicked() != null && getSpeedControls() != null && !IsoPlayer.getInstance().isAiming() && !IsoPlayer.getInstance().isAsleep() && !GameTime.isGamePaused()) {
               getSpeedControls().SetCurrentGameSpeed(1);
               getPicked().tile.onMouseRightClick(getPicked().lx, getPicked().ly);
               setRightDownObject(getPicked().tile);
            }
         }

         if (Mouse.isRightReleased()) {
            var18 = false;
            boolean var15 = false;

            for(var9 = UI.size() - 1; var9 >= 0; --var9) {
               var11 = (UIElement)UI.get(var9);
               if ((var11.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && var11.isVisible() && (getModal() == null || getModal() == var11 || !getModal().isVisible())) {
                  if ((!((double)var5 >= var11.getX()) || !((double)var6 >= var11.getY()) || !((double)var5 < var11.getX() + var11.getWidth()) || !((double)var6 < var11.getY() + var11.getHeight())) && !var11.isCapture()) {
                     var11.onRightMouseUpOutside((double)(var5 - var11.getX().intValue()), (double)(var6 - var11.getY().intValue()));
                  } else if (var11.onRightMouseUp((double)(var5 - var11.getX().intValue()), (double)(var6 - var11.getY().intValue()))) {
                     var15 = true;
                     break;
                  }
               }
            }

            if (!var15) {
               LuaEventManager.triggerEvent("OnRightMouseUp", BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
               if (checkPicked()) {
                  boolean var17 = true;
                  if (GameClient.bClient && Picked.tile.getSquare() != null) {
                     SafeHouse var12 = SafeHouse.isSafeHouse(Picked.tile.getSquare(), IsoPlayer.getInstance().getUsername(), true);
                     if (var12 != null) {
                        var17 = false;
                     }
                  }

                  if (var17) {
                     LuaEventManager.triggerEvent("OnObjectRightMouseButtonUp", Picked.tile, BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6));
                  }
               }
            }

            if (IsoPlayer.getInstance() != null) {
               IsoPlayer.getInstance().setDragObject((IsoMovingObject)null);
            }

            if (IsoWorld.instance.CurrentCell != null && getRightDownObject() != null && IsoPlayer.getInstance() != null && !IsoPlayer.getInstance().IsAiming() && !IsoPlayer.getInstance().isAsleep()) {
               getRightDownObject().onMouseRightReleased();
               setRightDownObject((IsoObject)null);
            }
         }

         lastwheel = 0;
         wheel = Mouse.getWheelState();
         var18 = false;
         if (wheel != lastwheel) {
            var14 = wheel - lastwheel < 0 ? 1 : -1;

            for(int var19 = UI.size() - 1; var19 >= 0; --var19) {
               UIElement var16 = (UIElement)UI.get(var19);
               if ((var16.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && var16.isVisible() && (var16.isPointOver((double)var5, (double)var6) || var16.isCapture()) && var16.onMouseWheel((double)var14)) {
                  var18 = true;
                  break;
               }
            }

            if (!var18) {
               Core.getInstance().doZoomScroll(0, var14);
            }
         }

         if (getLastMouseX() != (double)var5 || getLastMouseY() != (double)var6) {
            for(var14 = UI.size() - 1; var14 >= 0; --var14) {
               var11 = (UIElement)UI.get(var14);
               if ((var11.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && var11.isVisible()) {
                  if ((!((double)var5 >= var11.getX()) || !((double)var6 >= var11.getY()) || !((double)var5 < var11.getX() + var11.getWidth()) || !((double)var6 < var11.getY() + var11.getHeight())) && !var11.isCapture()) {
                     var11.onMouseMoveOutside((double)var5 - getLastMouseX(), (double)var6 - getLastMouseY());
                  } else if (!var4 && var11.onMouseMove((double)var5 - getLastMouseX(), (double)var6 - getLastMouseY())) {
                     var4 = true;
                  }
               }
            }
         }

         if (!var4 && IsoPlayer.players[0] != null) {
            setPicked(IsoObjectPicker.Instance.ContextPick(var5, var6));
            if (IsoCamera.CamCharacter != null) {
               setPickedTile(getTileFromMouse((double)var7, (double)var8, (double)((int)IsoPlayer.players[0].getZ())));
            }

            LuaEventManager.triggerEvent("OnMouseMove", BoxedStaticValues.toDouble((double)var5), BoxedStaticValues.toDouble((double)var6), BoxedStaticValues.toDouble((double)var7), BoxedStaticValues.toDouble((double)var8));
         } else {
            Mouse.UIBlockButtonDown(2);
         }

         setLastMouseX((double)var5);
         setLastMouseY((double)var6);

         for(var14 = 0; var14 < UI.size(); ++var14) {
            ((UIElement)UI.get(var14)).update();
         }

         updateTooltip((double)var5, (double)var6);
         handleZoomKeys();
         IsoCamera.cameras[0].lastOffX = (float)((int)IsoCamera.cameras[0].OffX);
         IsoCamera.cameras[0].lastOffY = (float)((int)IsoCamera.cameras[0].OffY);
      }
   }

   private static boolean checkPicked() {
      return Picked != null && Picked.tile != null && Picked.tile.getObjectIndex() != -1;
   }

   private static void handleZoomKeys() {
      boolean var0 = true;
      if (Core.CurrentTextEntryBox != null && Core.CurrentTextEntryBox.IsEditable && Core.CurrentTextEntryBox.DoingTextEntry) {
         var0 = false;
      }

      if (GameTime.isGamePaused()) {
         var0 = false;
      }

      if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Zoom in"))) {
         if (var0 && !KeyDownZoomIn) {
            Core.getInstance().doZoomScroll(0, -1);
         }

         KeyDownZoomIn = true;
      } else {
         KeyDownZoomIn = false;
      }

      if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Zoom out"))) {
         if (var0 && !KeyDownZoomOut) {
            Core.getInstance().doZoomScroll(0, 1);
         }

         KeyDownZoomOut = true;
      } else {
         KeyDownZoomOut = false;
      }

   }

   public static Double getLastMouseX() {
      return BoxedStaticValues.toDouble((double)lastMouseX);
   }

   public static void setLastMouseX(double var0) {
      lastMouseX = (int)var0;
   }

   public static Double getLastMouseY() {
      return BoxedStaticValues.toDouble((double)lastMouseY);
   }

   public static void setLastMouseY(double var0) {
      lastMouseY = (int)var0;
   }

   public static IsoObjectPicker.ClickObject getPicked() {
      return Picked;
   }

   public static void setPicked(IsoObjectPicker.ClickObject var0) {
      Picked = var0;
   }

   public static Clock getClock() {
      return clock;
   }

   public static void setClock(Clock var0) {
      clock = var0;
   }

   public static ArrayList getUI() {
      return UI;
   }

   public static void setUI(ArrayList var0) {
      PZArrayUtil.copy(UI, var0);
   }

   public static ObjectTooltip getToolTip() {
      return toolTip;
   }

   public static void setToolTip(ObjectTooltip var0) {
      toolTip = var0;
   }

   public static Texture getMouseArrow() {
      return mouseArrow;
   }

   public static void setMouseArrow(Texture var0) {
      mouseArrow = var0;
   }

   public static Texture getMouseExamine() {
      return mouseExamine;
   }

   public static void setMouseExamine(Texture var0) {
      mouseExamine = var0;
   }

   public static Texture getMouseAttack() {
      return mouseAttack;
   }

   public static void setMouseAttack(Texture var0) {
      mouseAttack = var0;
   }

   public static Texture getMouseGrab() {
      return mouseGrab;
   }

   public static void setMouseGrab(Texture var0) {
      mouseGrab = var0;
   }

   public static SpeedControls getSpeedControls() {
      return speedControls;
   }

   public static void setSpeedControls(SpeedControls var0) {
      speedControls = var0;
   }

   public static UIDebugConsole getDebugConsole() {
      return DebugConsole;
   }

   public static void setDebugConsole(UIDebugConsole var0) {
      DebugConsole = var0;
   }

   public static UIServerToolbox getServerToolbox() {
      return ServerToolbox;
   }

   public static void setServerToolbox(UIServerToolbox var0) {
      ServerToolbox = var0;
   }

   public static MoodlesUI getMoodleUI(double var0) {
      return MoodleUI[(int)var0];
   }

   public static void setMoodleUI(double var0, MoodlesUI var2) {
      MoodleUI[(int)var0] = var2;
   }

   public static boolean isbFadeBeforeUI() {
      return bFadeBeforeUI;
   }

   public static void setbFadeBeforeUI(boolean var0) {
      bFadeBeforeUI = var0;
   }

   public static ActionProgressBar getProgressBar(double var0) {
      return ProgressBar[(int)var0];
   }

   public static void setProgressBar(double var0, ActionProgressBar var2) {
      ProgressBar[(int)var0] = var2;
   }

   public static Double getFadeAlpha() {
      return BoxedStaticValues.toDouble((double)FadeAlpha);
   }

   public static void setFadeAlpha(double var0) {
      FadeAlpha = (float)var0;
   }

   public static Double getFadeInTimeMax() {
      return BoxedStaticValues.toDouble((double)FadeInTimeMax);
   }

   public static void setFadeInTimeMax(double var0) {
      FadeInTimeMax = (int)var0;
   }

   public static Double getFadeInTime() {
      return BoxedStaticValues.toDouble((double)FadeInTime);
   }

   public static void setFadeInTime(double var0) {
      FadeInTime = Math.max((int)var0, 0);
   }

   public static Boolean isFadingOut() {
      return FadingOut ? Boolean.TRUE : Boolean.FALSE;
   }

   public static void setFadingOut(boolean var0) {
      FadingOut = var0;
   }

   public static Texture getLastMouseTexture() {
      return lastMouseTexture;
   }

   public static void setLastMouseTexture(Texture var0) {
      lastMouseTexture = var0;
   }

   public static IsoObject getLastPicked() {
      return LastPicked;
   }

   public static void setLastPicked(IsoObject var0) {
      LastPicked = var0;
   }

   public static ArrayList getDoneTutorials() {
      return DoneTutorials;
   }

   public static void setDoneTutorials(ArrayList var0) {
      PZArrayUtil.copy(DoneTutorials, var0);
   }

   public static float getLastOffX() {
      return lastOffX;
   }

   public static void setLastOffX(float var0) {
      lastOffX = var0;
   }

   public static float getLastOffY() {
      return lastOffY;
   }

   public static void setLastOffY(float var0) {
      lastOffY = var0;
   }

   public static ModalDialog getModal() {
      return Modal;
   }

   public static void setModal(ModalDialog var0) {
      Modal = var0;
   }

   public static Texture getBlack() {
      return black;
   }

   public static void setBlack(Texture var0) {
      black = var0;
   }

   public static float getLastAlpha() {
      return lastAlpha;
   }

   public static void setLastAlpha(float var0) {
      lastAlpha = var0;
   }

   public static Vector2 getPickedTileLocal() {
      return PickedTileLocal;
   }

   public static void setPickedTileLocal(Vector2 var0) {
      PickedTileLocal.set(var0);
   }

   public static Vector2 getPickedTile() {
      return PickedTile;
   }

   public static void setPickedTile(Vector2 var0) {
      PickedTile.set(var0);
   }

   public static IsoObject getRightDownObject() {
      return RightDownObject;
   }

   public static void setRightDownObject(IsoObject var0) {
      RightDownObject = var0;
   }

   static void pushToTop(UIElement var0) {
      toTop.add(var0);
   }

   public static boolean isShowPausedMessage() {
      return showPausedMessage;
   }

   public static void setShowPausedMessage(boolean var0) {
      showPausedMessage = var0;
   }

   public static void setShowLuaDebuggerOnError(boolean var0) {
      bShowLuaDebuggerOnError = var0;
   }

   public static boolean isShowLuaDebuggerOnError() {
      return bShowLuaDebuggerOnError;
   }

   public static void debugBreakpoint(String var0, long var1) {
      if (bShowLuaDebuggerOnError) {
         if (Core.CurrentTextEntryBox != null) {
            Core.CurrentTextEntryBox.DoingTextEntry = false;
            Core.CurrentTextEntryBox = null;
         }

         if (!GameServer.bServer) {
            if (!(GameWindow.states.current instanceof GameLoadingState)) {
               previousThread = defaultthread;
               defaultthread = LuaManager.debugthread;
               int var3 = Core.getInstance().frameStage;
               if (var3 != 0) {
                  if (var3 <= 1) {
                     Core.getInstance().EndFrame(0);
                  }

                  if (var3 <= 2) {
                     Core.getInstance().StartFrameUI();
                  }

                  if (var3 <= 3) {
                     Core.getInstance().EndFrameUI();
                  }
               }

               LuaManager.thread.bStep = false;
               LuaManager.thread.bStepInto = false;
               ArrayList var4 = new ArrayList();
               boolean var5 = bSuspend;
               var4.addAll(UI);
               UI.clear();
               bSuspend = false;
               setShowPausedMessage(false);
               boolean var6 = false;
               boolean[] var7 = new boolean[11];

               int var8;
               for(var8 = 0; var8 < 11; ++var8) {
                  var7[var8] = true;
               }

               if (debugUI.size() == 0) {
                  LuaManager.debugcaller.pcall(LuaManager.debugthread, LuaManager.env.rawget("DoLuaDebugger"), var0, var1);
               } else {
                  UI.addAll(debugUI);
                  LuaManager.debugcaller.pcall(LuaManager.debugthread, LuaManager.env.rawget("DoLuaDebuggerOnBreak"), var0, var1);
               }

               Mouse.setCursorVisible(true);
               sync.begin();

               while(!var6) {
                  if (RenderThread.isCloseRequested()) {
                     System.exit(0);
                  }

                  if (!GameWindow.bLuaDebuggerKeyDown && GameKeyboard.isKeyDown(Core.getInstance().getKey("Toggle Lua Debugger"))) {
                     GameWindow.bLuaDebuggerKeyDown = true;
                     executeGame(var4, var5, var3);
                     return;
                  }

                  sync.startFrame();

                  for(var8 = 0; var8 < 11; ++var8) {
                     boolean var9 = GameKeyboard.isKeyDown(59 + var8);
                     if (var9) {
                        if (!var7[var8]) {
                           if (var8 + 1 == 5) {
                              LuaManager.thread.bStep = true;
                              LuaManager.thread.bStepInto = true;
                              executeGame(var4, var5, var3);
                              return;
                           }

                           if (var8 + 1 == 6) {
                              LuaManager.thread.bStep = true;
                              LuaManager.thread.bStepInto = false;
                              LuaManager.thread.lastCallFrame = LuaManager.thread.getCurrentCoroutine().getCallframeTop();
                              executeGame(var4, var5, var3);
                              return;
                           }
                        }

                        var7[var8] = true;
                     } else {
                        var7[var8] = false;
                     }
                  }

                  Mouse.update();
                  GameKeyboard.update();
                  Core.getInstance().DoFrameReady();
                  update();
                  Core.getInstance().StartFrame(0, true);
                  Core.getInstance().EndFrame(0);
                  Core.getInstance().RenderOffScreenBuffer();
                  if (Core.getInstance().StartFrameUI()) {
                     render();
                  }

                  Core.getInstance().EndFrameUI();
                  resize();
                  if (!GameKeyboard.isKeyDown(Core.getInstance().getKey("Toggle Lua Debugger"))) {
                     GameWindow.bLuaDebuggerKeyDown = false;
                  }

                  sync.endFrame();
                  Core.getInstance().setScreenSize(RenderThread.getDisplayWidth(), RenderThread.getDisplayHeight());
               }

            }
         }
      }
   }

   private static void executeGame(ArrayList var0, boolean var1, int var2) {
      debugUI.clear();
      debugUI.addAll(UI);
      UI.clear();
      UI.addAll(var0);
      bSuspend = var1;
      setShowPausedMessage(true);
      if (!LuaManager.thread.bStep && var2 != 0) {
         if (var2 == 1) {
            Core.getInstance().StartFrame(0, true);
         }

         if (var2 == 2) {
            Core.getInstance().StartFrame(0, true);
            Core.getInstance().EndFrame(0);
         }

         if (var2 == 3) {
            Core.getInstance().StartFrame(0, true);
            Core.getInstance().EndFrame(0);
            Core.getInstance().StartFrameUI();
         }
      }

      defaultthread = previousThread;
   }

   public static KahluaThread getDefaultThread() {
      if (defaultthread == null) {
         defaultthread = LuaManager.thread;
      }

      return defaultthread;
   }

   public static Double getDoubleClickInterval() {
      return BoxedStaticValues.toDouble(500.0D);
   }

   public static Double getDoubleClickDist() {
      return BoxedStaticValues.toDouble(5.0D);
   }

   public static Boolean isDoubleClick(double var0, double var2, double var4, double var6, double var8) {
      if (Math.abs(var4 - var0) > getDoubleClickDist()) {
         return false;
      } else if (Math.abs(var6 - var2) > getDoubleClickDist()) {
         return false;
      } else {
         return (double)System.currentTimeMillis() - var8 > getDoubleClickInterval() ? Boolean.FALSE : Boolean.TRUE;
      }
   }

   protected static void updateTooltip(double var0, double var2) {
      UIElement var4 = null;

      for(int var5 = getUI().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)getUI().get(var5);
         if (var6 != toolTip && var6.isVisible() && var0 >= var6.getX() && var2 >= var6.getY() && var0 < var6.getX() + var6.getWidth() && var2 < var6.getY() + var6.getHeight() && (var6.maxDrawHeight == -1 || var2 < var6.getY() + (double)var6.maxDrawHeight)) {
            var4 = var6;
            break;
         }
      }

      IsoObject var7 = null;
      if (var4 == null && getPicked() != null) {
         var7 = getPicked().tile;
         if (var7 != getLastPicked() && toolTip != null) {
            toolTip.targetAlpha = 0.0F;
            if (var7.haveSpecialTooltip()) {
               if (getToolTip().Object != var7) {
                  getToolTip().show(var7, (double)((int)var0 + 8), (double)((int)var2 + 16));
                  if (toolTip.isVisible()) {
                     toolTip.showDelay = 0;
                  }
               } else {
                  toolTip.targetAlpha = 1.0F;
               }
            }
         }
      }

      setLastPicked(var7);
      if (toolTip != null && (var7 == null || toolTip.alpha <= 0.0F && toolTip.targetAlpha <= 0.0F)) {
         toolTip.hide();
      }

   }

   public static void setPlayerInventory(int var0, UIElement var1, UIElement var2) {
      if (var0 == 0) {
         playerInventoryUI = var1;
         playerLootUI = var2;
      }
   }

   public static void setPlayerInventoryTooltip(int var0, UIElement var1, UIElement var2) {
      if (var0 == 0) {
         playerInventoryTooltip = var1;
         playerLootTooltip = var2;
      }
   }

   public static boolean isMouseOverInventory() {
      if (playerInventoryTooltip != null && playerInventoryTooltip.isMouseOver()) {
         return true;
      } else if (playerLootTooltip != null && playerLootTooltip.isMouseOver()) {
         return true;
      } else if (playerInventoryUI != null && playerLootUI != null) {
         if (playerInventoryUI.getMaxDrawHeight() == -1.0D && playerInventoryUI.isMouseOver()) {
            return true;
         } else {
            return playerLootUI.getMaxDrawHeight() == -1.0D && playerLootUI.isMouseOver();
         }
      } else {
         return false;
      }
   }

   public static void updateBeforeFadeOut() {
      if (!toRemove.isEmpty()) {
         UI.removeAll(toRemove);
         toRemove.clear();
      }

      if (!toAdd.isEmpty()) {
         UI.addAll(toAdd);
         toAdd.clear();
      }

   }

   public static void setVisibleAllUI(boolean var0) {
      VisibleAllUI = var0;
   }

   public static void setFadeBeforeUI(int var0, boolean var1) {
      playerFadeInfo[var0].setFadeBeforeUI(var1);
   }

   public static float getFadeAlpha(double var0) {
      return playerFadeInfo[(int)var0].getFadeAlpha();
   }

   public static void setFadeTime(double var0, double var2) {
      playerFadeInfo[(int)var0].setFadeTime((int)var2);
   }

   public static void FadeIn(double var0, double var2) {
      playerFadeInfo[(int)var0].FadeIn((int)var2);
   }

   public static void FadeOut(double var0, double var2) {
      playerFadeInfo[(int)var0].FadeOut((int)var2);
   }

   public static boolean isFBOActive() {
      return useUIFBO;
   }

   public static double getMillisSinceLastUpdate() {
      return (double)uiUpdateIntervalMS;
   }

   public static double getSecondsSinceLastUpdate() {
      return (double)uiUpdateIntervalMS / 1000.0D;
   }

   public static double getMillisSinceLastRender() {
      return (double)uiRenderIntervalMS;
   }

   public static double getSecondsSinceLastRender() {
      return (double)uiRenderIntervalMS / 1000.0D;
   }

   public static boolean onKeyPress(int var0) {
      for(int var1 = UI.size() - 1; var1 >= 0; --var1) {
         UIElement var2 = (UIElement)UI.get(var1);
         if (var2.isVisible() && var2.isWantKeyEvents()) {
            var2.onKeyPress(var0);
            if (var2.isKeyConsumed(var0)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean onKeyRepeat(int var0) {
      for(int var1 = UI.size() - 1; var1 >= 0; --var1) {
         UIElement var2 = (UIElement)UI.get(var1);
         if (var2.isVisible() && var2.isWantKeyEvents()) {
            var2.onKeyRepeat(var0);
            if (var2.isKeyConsumed(var0)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean onKeyRelease(int var0) {
      for(int var1 = UI.size() - 1; var1 >= 0; --var1) {
         UIElement var2 = (UIElement)UI.get(var1);
         if (var2.isVisible() && var2.isWantKeyEvents()) {
            var2.onKeyRelease(var0);
            if (var2.isKeyConsumed(var0)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isForceCursorVisible() {
      for(int var0 = UI.size() - 1; var0 >= 0; --var0) {
         UIElement var1 = (UIElement)UI.get(var0);
         if (var1.isVisible() && (var1.isForceCursorVisible() || var1.isMouseOver())) {
            return true;
         }
      }

      return false;
   }

   static {
      for(int var0 = 0; var0 < 4; ++var0) {
         playerFadeInfo[var0] = new UIManager.FadeInfo(var0);
      }

   }

   private static class FadeInfo {
      public int playerIndex;
      public boolean bFadeBeforeUI = false;
      public float FadeAlpha = 0.0F;
      public int FadeTime = 2;
      public int FadeTimeMax = 2;
      public boolean FadingOut = false;

      public FadeInfo(int var1) {
         this.playerIndex = var1;
      }

      public boolean isFadeBeforeUI() {
         return this.bFadeBeforeUI;
      }

      public void setFadeBeforeUI(boolean var1) {
         this.bFadeBeforeUI = var1;
      }

      public float getFadeAlpha() {
         return this.FadeAlpha;
      }

      public void setFadeAlpha(float var1) {
         this.FadeAlpha = var1;
      }

      public int getFadeTime() {
         return this.FadeTime;
      }

      public void setFadeTime(int var1) {
         this.FadeTime = var1;
      }

      public int getFadeTimeMax() {
         return this.FadeTimeMax;
      }

      public void setFadeTimeMax(int var1) {
         this.FadeTimeMax = var1;
      }

      public boolean isFadingOut() {
         return this.FadingOut;
      }

      public void setFadingOut(boolean var1) {
         this.FadingOut = var1;
      }

      public void FadeIn(int var1) {
         this.setFadeTimeMax((int)((float)(var1 * 30) * ((float)PerformanceSettings.getLockFPS() / 30.0F)));
         this.setFadeTime(this.getFadeTimeMax());
         this.setFadingOut(false);
      }

      public void FadeOut(int var1) {
         this.setFadeTimeMax((int)((float)(var1 * 30) * ((float)PerformanceSettings.getLockFPS() / 30.0F)));
         this.setFadeTime(this.getFadeTimeMax());
         this.setFadingOut(true);
      }

      public void update() {
         this.setFadeTime(this.getFadeTime() - 1);
      }

      public void render() {
         this.setFadeAlpha((float)this.getFadeTime() / (float)this.getFadeTimeMax());
         if (this.getFadeAlpha() > 1.0F) {
            this.setFadeAlpha(1.0F);
         }

         if (this.getFadeAlpha() < 0.0F) {
            this.setFadeAlpha(0.0F);
         }

         if (this.isFadingOut()) {
            this.setFadeAlpha(1.0F - this.getFadeAlpha());
         }

         if (!(this.getFadeAlpha() <= 0.0F)) {
            int var1 = IsoCamera.getScreenLeft(this.playerIndex);
            int var2 = IsoCamera.getScreenTop(this.playerIndex);
            int var3 = IsoCamera.getScreenWidth(this.playerIndex);
            int var4 = IsoCamera.getScreenHeight(this.playerIndex);
            UIManager.DrawTexture(UIManager.getBlack(), (double)var1, (double)var2, (double)var3, (double)var4, (double)this.getFadeAlpha());
         }
      }
   }

   static class Sync {
      private int fps = 30;
      private long period;
      private long excess;
      private long beforeTime;
      private long overSleepTime;

      Sync() {
         this.period = 1000000000L / (long)this.fps;
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void begin() {
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void startFrame() {
         this.excess = 0L;
      }

      void endFrame() {
         long var1 = System.nanoTime();
         long var3 = var1 - this.beforeTime;
         long var5 = this.period - var3 - this.overSleepTime;
         if (var5 > 0L) {
            try {
               Thread.sleep(var5 / 1000000L);
            } catch (InterruptedException var8) {
            }

            this.overSleepTime = System.nanoTime() - var1 - var5;
         } else {
            this.excess -= var5;
            this.overSleepTime = 0L;
         }

         this.beforeTime = System.nanoTime();
      }
   }
}
