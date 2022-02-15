package zombie.gameStates;

import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatElement;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.debug.LineDrawer;
import zombie.globalObjects.CGlobalObjectSystem;
import zombie.globalObjects.CGlobalObjects;
import zombie.globalObjects.GlobalObject;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.TextDrawObject;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.vehicles.EditVehicleState;

public final class DebugGlobalObjectState extends GameState {
   public static DebugGlobalObjectState instance;
   private EditVehicleState.LuaEnvironment m_luaEnv;
   private boolean bExit = false;
   private final ArrayList m_gameUI = new ArrayList();
   private final ArrayList m_selfUI = new ArrayList();
   private boolean m_bSuspendUI;
   private KahluaTable m_table = null;
   private int m_playerIndex = 0;
   private int m_z = 0;
   private int gridX = -1;
   private int gridY = -1;
   private UIFont FONT;

   public DebugGlobalObjectState() {
      this.FONT = UIFont.DebugConsole;
      instance = this;
   }

   public void enter() {
      instance = this;
      if (this.m_luaEnv == null) {
         this.m_luaEnv = new EditVehicleState.LuaEnvironment(LuaManager.platform, LuaManager.converterManager, LuaManager.env);
      }

      this.saveGameUI();
      if (this.m_selfUI.size() == 0) {
         IsoPlayer var1 = IsoPlayer.players[this.m_playerIndex];
         this.m_z = var1 == null ? 0 : (int)var1.z;
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_luaEnv.env.rawget("DebugGlobalObjectState_InitUI"), (Object)this);
      } else {
         UIManager.UI.addAll(this.m_selfUI);
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_table.rawget("showUI"), (Object)this.m_table);
      }

      this.bExit = false;
   }

   public void yield() {
      this.restoreGameUI();
   }

   public void reenter() {
      this.saveGameUI();
   }

   public void exit() {
      this.restoreGameUI();

      for(int var1 = 0; var1 < IsoCamera.cameras.length; ++var1) {
         IsoCamera.cameras[var1].DeferedX = IsoCamera.cameras[var1].DeferedY = 0.0F;
      }

   }

   public void render() {
      IsoPlayer.setInstance(IsoPlayer.players[this.m_playerIndex]);
      IsoCamera.CamCharacter = IsoPlayer.players[this.m_playerIndex];
      boolean var1 = true;

      int var2;
      for(var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (var2 != this.m_playerIndex && IsoPlayer.players[var2] != null) {
            Core.getInstance().StartFrame(var2, var1);
            Core.getInstance().EndFrame(var2);
            var1 = false;
         }
      }

      Core.getInstance().StartFrame(this.m_playerIndex, var1);
      this.renderScene();
      Core.getInstance().EndFrame(this.m_playerIndex);
      Core.getInstance().RenderOffScreenBuffer();

      for(var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         TextDrawObject.NoRender(var2);
         ChatElement.NoRender(var2);
      }

      if (Core.getInstance().StartFrameUI()) {
         this.renderUI();
      }

      Core.getInstance().EndFrameUI();
   }

   public GameStateMachine.StateAction update() {
      if (!this.bExit && !GameKeyboard.isKeyPressed(60)) {
         IsoChunkMap var1 = IsoWorld.instance.CurrentCell.ChunkMap[this.m_playerIndex];
         var1.ProcessChunkPos(IsoPlayer.players[this.m_playerIndex]);
         var1.update();
         return this.updateScene();
      } else {
         return GameStateMachine.StateAction.Continue;
      }
   }

   public void renderScene() {
      IsoCamera.frameState.set(this.m_playerIndex);
      SpriteRenderer.instance.doCoreIntParam(0, IsoCamera.CamCharacter.x);
      SpriteRenderer.instance.doCoreIntParam(1, IsoCamera.CamCharacter.y);
      SpriteRenderer.instance.doCoreIntParam(2, IsoCamera.CamCharacter.z);
      IsoSprite.globalOffsetX = -1.0F;
      IsoWorld.instance.CurrentCell.render();
      IsoChunkMap var1 = IsoWorld.instance.CurrentCell.ChunkMap[this.m_playerIndex];
      int var2 = var1.getWorldXMin();
      int var3 = var1.getWorldYMin();
      int var4 = var2 + IsoChunkMap.ChunkGridWidth;
      int var5 = var3 + IsoChunkMap.ChunkGridWidth;
      int var6 = CGlobalObjects.getSystemCount();

      for(int var7 = 0; var7 < var6; ++var7) {
         CGlobalObjectSystem var8 = CGlobalObjects.getSystemByIndex(var7);

         for(int var9 = var3; var9 < var5; ++var9) {
            for(int var10 = var2; var10 < var4; ++var10) {
               ArrayList var11 = var8.getObjectsInChunk(var10, var9);

               for(int var12 = 0; var12 < var11.size(); ++var12) {
                  GlobalObject var13 = (GlobalObject)var11.get(var12);
                  float var14 = 1.0F;
                  float var15 = 1.0F;
                  float var16 = 1.0F;
                  if (var13.getZ() != this.m_z) {
                     var16 = 0.5F;
                     var15 = 0.5F;
                     var14 = 0.5F;
                  }

                  this.DrawIsoRect((float)var13.getX(), (float)var13.getY(), (float)var13.getZ(), 1.0F, 1.0F, var14, var15, var16, 1.0F, 1);
               }

               var8.finishedWithList(var11);
            }
         }
      }

      LineDrawer.render();
      LineDrawer.clear();
   }

   private void renderUI() {
      UIManager.render();
   }

   public void setTable(KahluaTable var1) {
      this.m_table = var1;
   }

   public GameStateMachine.StateAction updateScene() {
      IsoPlayer.setInstance(IsoPlayer.players[this.m_playerIndex]);
      IsoCamera.CamCharacter = IsoPlayer.players[this.m_playerIndex];
      UIManager.setPicked(IsoObjectPicker.Instance.ContextPick(Mouse.getXA(), Mouse.getYA()));
      IsoObject var1 = UIManager.getPicked() == null ? null : UIManager.getPicked().tile;
      UIManager.setLastPicked(var1);
      IsoCamera.update();
      this.updateCursor();
      return GameStateMachine.StateAction.Remain;
   }

   private void updateCursor() {
      int var1 = this.m_playerIndex;
      float var2 = (float)Mouse.getXA();
      float var3 = (float)Mouse.getYA();
      var2 -= (float)IsoCamera.getScreenLeft(var1);
      var3 -= (float)IsoCamera.getScreenTop(var1);
      var2 *= Core.getInstance().getZoom(var1);
      var3 *= Core.getInstance().getZoom(var1);
      int var4 = this.m_z;
      this.gridX = (int)IsoUtils.XToIso(var2, var3, (float)var4);
      this.gridY = (int)IsoUtils.YToIso(var2, var3, (float)var4);
   }

   private void saveGameUI() {
      this.m_gameUI.clear();
      this.m_gameUI.addAll(UIManager.UI);
      UIManager.UI.clear();
      this.m_bSuspendUI = UIManager.bSuspend;
      UIManager.bSuspend = false;
      UIManager.setShowPausedMessage(false);
      UIManager.defaultthread = this.m_luaEnv.thread;
   }

   private void restoreGameUI() {
      this.m_selfUI.clear();
      this.m_selfUI.addAll(UIManager.UI);
      UIManager.UI.clear();
      UIManager.UI.addAll(this.m_gameUI);
      UIManager.bSuspend = this.m_bSuspendUI;
      UIManager.setShowPausedMessage(true);
      UIManager.defaultthread = LuaManager.thread;
   }

   private void DrawIsoLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, int var11) {
      float var12 = IsoUtils.XToScreenExact(var1, var2, var3, 0);
      float var13 = IsoUtils.YToScreenExact(var1, var2, var3, 0);
      float var14 = IsoUtils.XToScreenExact(var4, var5, var6, 0);
      float var15 = IsoUtils.YToScreenExact(var4, var5, var6, 0);
      LineDrawer.drawLine(var12, var13, var14, var15, var7, var8, var9, var10, var11);
   }

   private void DrawIsoRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      this.DrawIsoLine(var1, var2, var3, var1 + var4, var2, var3, var6, var7, var8, var9, var10);
      this.DrawIsoLine(var1 + var4, var2, var3, var1 + var4, var2 + var5, var3, var6, var7, var8, var9, var10);
      this.DrawIsoLine(var1 + var4, var2 + var5, var3, var1, var2 + var5, var3, var6, var7, var8, var9, var10);
      this.DrawIsoLine(var1, var2 + var5, var3, var1, var2, var3, var6, var7, var8, var9, var10);
   }

   public Object fromLua0(String var1) {
      byte var3 = -1;
      switch(var1.hashCode()) {
      case -103642821:
         if (var1.equals("getPlayerIndex")) {
            var3 = 3;
         }
         break;
      case 3127582:
         if (var1.equals("exit")) {
            var3 = 0;
         }
         break;
      case 3169220:
         if (var1.equals("getZ")) {
            var3 = 4;
         }
         break;
      case 1393900617:
         if (var1.equals("getCameraDragX")) {
            var3 = 1;
         }
         break;
      case 1393900618:
         if (var1.equals("getCameraDragY")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         this.bExit = true;
         return null;
      case 1:
         return BoxedStaticValues.toDouble((double)(-IsoCamera.cameras[this.m_playerIndex].DeferedX));
      case 2:
         return BoxedStaticValues.toDouble((double)(-IsoCamera.cameras[this.m_playerIndex].DeferedY));
      case 3:
         return BoxedStaticValues.toDouble((double)this.m_playerIndex);
      case 4:
         return BoxedStaticValues.toDouble((double)this.m_z);
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\"", var1));
      }
   }

   public Object fromLua1(String var1, Object var2) {
      byte var4 = -1;
      switch(var1.hashCode()) {
      case -1875379025:
         if (var1.equals("setPlayerIndex")) {
            var4 = 0;
         }
         break;
      case 3526712:
         if (var1.equals("setZ")) {
            var4 = 1;
         }
      }

      switch(var4) {
      case 0:
         this.m_playerIndex = PZMath.clamp(((Double)var2).intValue(), 0, 3);
         return null;
      case 1:
         this.m_z = PZMath.clamp(((Double)var2).intValue(), 0, 7);
         return null;
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\"", var1, var2));
      }
   }

   public Object fromLua2(String var1, Object var2, Object var3) {
      byte var5 = -1;
      switch(var1.hashCode()) {
      case -1879300743:
         if (var1.equals("dragCamera")) {
            var5 = 0;
         }
      default:
         switch(var5) {
         case 0:
            float var6 = ((Double)var2).floatValue();
            float var7 = ((Double)var3).floatValue();
            IsoCamera.cameras[this.m_playerIndex].DeferedX = -var6;
            IsoCamera.cameras[this.m_playerIndex].DeferedY = -var7;
            return null;
         default:
            throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \\\"%s\\\"", var1, var2, var3));
         }
      }
   }
}
