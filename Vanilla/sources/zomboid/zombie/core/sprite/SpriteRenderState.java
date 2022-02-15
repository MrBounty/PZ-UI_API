package zombie.core.sprite;

import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Styles.TransparentStyle;
import zombie.core.opengl.GLState;
import zombie.core.opengl.RenderSettings;
import zombie.core.textures.TextureFBO;
import zombie.input.Mouse;
import zombie.iso.PlayerCamera;

public final class SpriteRenderState extends GenericSpriteRenderState {
   public TextureFBO fbo = null;
   public long time;
   public final SpriteRenderStateUI stateUI;
   public int playerIndex;
   public final PlayerCamera[] playerCamera = new PlayerCamera[4];
   public final float[] playerAmbient = new float[4];
   public float maxZoomLevel = 0.0F;
   public float minZoomLevel = 0.0F;
   public final float[] zoomLevel = new float[4];

   public SpriteRenderState(int var1) {
      super(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         this.playerCamera[var2] = new PlayerCamera(var2);
      }

      this.stateUI = new SpriteRenderStateUI(var1);
   }

   public void onRendered() {
      super.onRendered();
      this.stateUI.onRendered();
   }

   public void onReady() {
      super.onReady();
      this.stateUI.onReady();
   }

   public void CheckSpriteSlots() {
      if (this.stateUI.bActive) {
         this.stateUI.CheckSpriteSlots();
      } else {
         super.CheckSpriteSlots();
      }

   }

   public void clear() {
      this.stateUI.clear();
      super.clear();
   }

   public GenericSpriteRenderState getActiveState() {
      return (GenericSpriteRenderState)(this.stateUI.bActive ? this.stateUI : this);
   }

   public void prePopulating() {
      this.clear();
      this.fbo = Core.getInstance().getOffscreenBuffer();

      for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         IsoPlayer var2 = IsoPlayer.players[var1];
         if (var2 != null) {
            this.playerCamera[var1].initFromIsoCamera(var1);
            this.playerAmbient[var1] = RenderSettings.getInstance().getAmbientForPlayer(var1);
            this.zoomLevel[var1] = Core.getInstance().getZoom(var1);
            this.maxZoomLevel = Core.getInstance().getMaxZoom();
            this.minZoomLevel = Core.getInstance().getMinZoom();
         }
      }

      this.defaultStyle = TransparentStyle.instance;
      this.bCursorVisible = Mouse.isCursorVisible();
      GLState.startFrame();
   }
}
