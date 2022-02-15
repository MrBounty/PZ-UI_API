package zombie.iso;

import zombie.GameTime;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.iso.areas.IsoRoom;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.ui.MoodlesUI;
import zombie.ui.UIManager;

public class IsoCamera {
   public static final PlayerCamera[] cameras = new PlayerCamera[4];
   public static IsoGameCharacter CamCharacter;
   public static Vector2 FakePos;
   public static Vector2 FakePosVec;
   public static int TargetTileX;
   public static int TargetTileY;
   public static int PLAYER_OFFSET_X;
   public static int PLAYER_OFFSET_Y;
   public static final IsoCamera.FrameState frameState;

   public static void init() {
      PLAYER_OFFSET_Y = -56 / (2 / Core.TileScale);
   }

   public static void update() {
      int var0 = IsoPlayer.getPlayerIndex();
      cameras[var0].update();
   }

   public static void updateAll() {
      for(int var0 = 0; var0 < 4; ++var0) {
         IsoPlayer var1 = IsoPlayer.players[var0];
         if (var1 != null) {
            CamCharacter = var1;
            cameras[var0].update();
         }
      }

   }

   public static void SetCharacterToFollow(IsoGameCharacter var0) {
      if (!GameClient.bClient && !GameServer.bServer) {
         CamCharacter = var0;
         if (CamCharacter instanceof IsoPlayer && ((IsoPlayer)CamCharacter).isLocalPlayer() && UIManager.getMoodleUI((double)((IsoPlayer)CamCharacter).getPlayerNum()) != null) {
            int var1 = ((IsoPlayer)CamCharacter).getPlayerNum();
            UIManager.getUI().remove(UIManager.getMoodleUI((double)var1));
            UIManager.setMoodleUI((double)var1, new MoodlesUI());
            UIManager.getMoodleUI((double)var1).setCharacter(CamCharacter);
            UIManager.getUI().add(UIManager.getMoodleUI((double)var1));
         }
      }

   }

   public static float getRightClickOffX() {
      return (float)((int)cameras[IsoPlayer.getPlayerIndex()].RightClickX);
   }

   public static float getRightClickOffY() {
      return (float)((int)cameras[IsoPlayer.getPlayerIndex()].RightClickY);
   }

   public static float getOffX() {
      return cameras[IsoPlayer.getPlayerIndex()].getOffX();
   }

   public static float getTOffX() {
      return cameras[IsoPlayer.getPlayerIndex()].getTOffX();
   }

   public static void setOffX(float var0) {
      cameras[IsoPlayer.getPlayerIndex()].OffX = var0;
   }

   public static float getOffY() {
      return cameras[IsoPlayer.getPlayerIndex()].getOffY();
   }

   public static float getTOffY() {
      return cameras[IsoPlayer.getPlayerIndex()].getTOffY();
   }

   public static void setOffY(float var0) {
      cameras[IsoPlayer.getPlayerIndex()].OffY = var0;
   }

   public static float getLastOffX() {
      return cameras[IsoPlayer.getPlayerIndex()].getLastOffX();
   }

   public static void setLastOffX(float var0) {
      cameras[IsoPlayer.getPlayerIndex()].lastOffX = var0;
   }

   public static float getLastOffY() {
      return cameras[IsoPlayer.getPlayerIndex()].getLastOffY();
   }

   public static void setLastOffY(float var0) {
      cameras[IsoPlayer.getPlayerIndex()].lastOffY = var0;
   }

   public static IsoGameCharacter getCamCharacter() {
      return CamCharacter;
   }

   public static void setCamCharacter(IsoGameCharacter var0) {
      CamCharacter = var0;
   }

   public static Vector2 getFakePos() {
      return FakePos;
   }

   public static void setFakePos(Vector2 var0) {
      FakePos = var0;
   }

   public static Vector2 getFakePosVec() {
      return FakePosVec;
   }

   public static void setFakePosVec(Vector2 var0) {
      FakePosVec = var0;
   }

   public static int getTargetTileX() {
      return TargetTileX;
   }

   public static void setTargetTileX(int var0) {
      TargetTileX = var0;
   }

   public static int getTargetTileY() {
      return TargetTileY;
   }

   public static void setTargetTileY(int var0) {
      TargetTileY = var0;
   }

   public static int getScreenLeft(int var0) {
      return var0 != 1 && var0 != 3 ? 0 : Core.getInstance().getScreenWidth() / 2;
   }

   public static int getScreenWidth(int var0) {
      return IsoPlayer.numPlayers > 1 ? Core.getInstance().getScreenWidth() / 2 : Core.getInstance().getScreenWidth();
   }

   public static int getScreenTop(int var0) {
      return var0 != 2 && var0 != 3 ? 0 : Core.getInstance().getScreenHeight() / 2;
   }

   public static int getScreenHeight(int var0) {
      return IsoPlayer.numPlayers > 2 ? Core.getInstance().getScreenHeight() / 2 : Core.getInstance().getScreenHeight();
   }

   public static int getOffscreenLeft(int var0) {
      return var0 != 1 && var0 != 3 ? 0 : Core.getInstance().getScreenWidth() / 2;
   }

   public static int getOffscreenWidth(int var0) {
      return Core.getInstance().getOffscreenWidth(var0);
   }

   public static int getOffscreenTop(int var0) {
      return var0 >= 2 ? Core.getInstance().getScreenHeight() / 2 : 0;
   }

   public static int getOffscreenHeight(int var0) {
      return Core.getInstance().getOffscreenHeight(var0);
   }

   static {
      for(int var0 = 0; var0 < cameras.length; ++var0) {
         cameras[var0] = new PlayerCamera(var0);
      }

      CamCharacter = null;
      FakePos = new Vector2();
      FakePosVec = new Vector2();
      TargetTileX = 0;
      TargetTileY = 0;
      PLAYER_OFFSET_X = 0;
      PLAYER_OFFSET_Y = -56 / (2 / Core.TileScale);
      frameState = new IsoCamera.FrameState();
   }

   public static class FrameState {
      public int frameCount;
      public boolean Paused;
      public int playerIndex;
      public float CamCharacterX;
      public float CamCharacterY;
      public float CamCharacterZ;
      public IsoGameCharacter CamCharacter;
      public IsoGridSquare CamCharacterSquare;
      public IsoRoom CamCharacterRoom;
      public float OffX;
      public float OffY;
      public int OffscreenWidth;
      public int OffscreenHeight;

      public void set(int var1) {
         this.Paused = GameTime.isGamePaused();
         this.playerIndex = var1;
         this.CamCharacter = IsoPlayer.players[var1];
         this.CamCharacterX = this.CamCharacter.getX();
         this.CamCharacterY = this.CamCharacter.getY();
         this.CamCharacterZ = this.CamCharacter.getZ();
         this.CamCharacterSquare = this.CamCharacter.getCurrentSquare();
         this.CamCharacterRoom = this.CamCharacterSquare == null ? null : this.CamCharacterSquare.getRoom();
         this.OffX = IsoCamera.getOffX();
         this.OffY = IsoCamera.getOffY();
         this.OffscreenWidth = IsoCamera.getOffscreenWidth(var1);
         this.OffscreenHeight = IsoCamera.getOffscreenHeight(var1);
      }
   }
}
