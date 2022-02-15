package zombie.input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Cursor;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;

public final class Mouse {
   protected static int x;
   protected static int y;
   public static boolean bLeftDown;
   public static boolean bLeftWasDown;
   public static boolean bRightDown;
   public static boolean bRightWasDown;
   public static boolean bMiddleDown;
   public static boolean bMiddleWasDown;
   public static boolean[] m_buttonDownStates;
   public static long lastActivity;
   public static int wheelDelta;
   private static final MouseStateCache s_mouseStateCache = new MouseStateCache();
   public static boolean[] UICaptured = new boolean[10];
   static Cursor blankCursor;
   static Cursor defaultCursor;
   private static boolean isCursorVisible = true;
   private static Texture mouseCursorTexture = null;

   public static int getWheelState() {
      return wheelDelta;
   }

   public static synchronized int getXA() {
      return x;
   }

   public static synchronized int getYA() {
      return y;
   }

   public static synchronized int getX() {
      return (int)((float)x * Core.getInstance().getZoom(0));
   }

   public static synchronized int getY() {
      return (int)((float)y * Core.getInstance().getZoom(0));
   }

   public static boolean isButtonDown(int var0) {
      return m_buttonDownStates != null ? m_buttonDownStates[var0] : false;
   }

   public static void UIBlockButtonDown(int var0) {
      UICaptured[var0] = true;
   }

   public static boolean isButtonDownUICheck(int var0) {
      if (m_buttonDownStates != null) {
         boolean var1 = m_buttonDownStates[var0];
         if (!var1) {
            UICaptured[var0] = false;
         } else if (UICaptured[var0]) {
            return false;
         }

         return var1;
      } else {
         return false;
      }
   }

   public static boolean isLeftDown() {
      return bLeftDown;
   }

   public static boolean isLeftPressed() {
      return !bLeftWasDown && bLeftDown;
   }

   public static boolean isLeftReleased() {
      return bLeftWasDown && !bLeftDown;
   }

   public static boolean isLeftUp() {
      return !bLeftDown;
   }

   public static boolean isMiddleDown() {
      return bMiddleDown;
   }

   public static boolean isMiddlePressed() {
      return !bMiddleWasDown && bMiddleDown;
   }

   public static boolean isMiddleReleased() {
      return bMiddleWasDown && !bMiddleDown;
   }

   public static boolean isMiddleUp() {
      return !bMiddleDown;
   }

   public static boolean isRightDown() {
      return bRightDown;
   }

   public static boolean isRightPressed() {
      return !bRightWasDown && bRightDown;
   }

   public static boolean isRightReleased() {
      return bRightWasDown && !bRightDown;
   }

   public static boolean isRightUp() {
      return !bRightDown;
   }

   public static synchronized void update() {
      MouseState var0 = s_mouseStateCache.getState();
      if (!var0.isCreated()) {
         s_mouseStateCache.swap();

         try {
            org.lwjglx.input.Mouse.create();
         } catch (LWJGLException var4) {
            var4.printStackTrace();
         }

      } else {
         bLeftWasDown = bLeftDown;
         bRightWasDown = bRightDown;
         bMiddleWasDown = bMiddleDown;
         int var1 = x;
         int var2 = y;
         x = var0.getX();
         y = Core.getInstance().getScreenHeight() - var0.getY() - 1;
         bLeftDown = var0.isButtonDown(0);
         bRightDown = var0.isButtonDown(1);
         bMiddleDown = var0.isButtonDown(2);
         wheelDelta = var0.getDWheel();
         var0.resetDWheel();
         if (m_buttonDownStates == null) {
            m_buttonDownStates = new boolean[var0.getButtonCount()];
         }

         for(int var3 = 0; var3 < m_buttonDownStates.length; ++var3) {
            m_buttonDownStates[var3] = var0.isButtonDown(var3);
         }

         if (var1 != x || var2 != y || wheelDelta != 0 || bLeftWasDown != bLeftDown || bRightWasDown != bRightDown || bMiddleWasDown != bMiddleDown) {
            lastActivity = System.currentTimeMillis();
         }

         s_mouseStateCache.swap();
      }
   }

   public static void poll() {
      s_mouseStateCache.poll();
   }

   public static synchronized void setXY(int var0, int var1) {
      s_mouseStateCache.getState().setCursorPosition(var0, Core.getInstance().getOffscreenHeight(0) - 1 - var1);
   }

   public static Cursor loadCursor(String var0) throws LWJGLException {
      File var1 = ZomboidFileSystem.instance.getMediaFile("ui/" + var0);
      BufferedImage var2 = null;

      try {
         var2 = ImageIO.read(var1);
         int var3 = var2.getWidth();
         int var4 = var2.getHeight();
         int[] var5 = new int[var3 * var4];

         for(int var6 = 0; var6 < var5.length; ++var6) {
            int var7 = var6 % var3;
            int var8 = var4 - 1 - var6 / var3;
            var5[var6] = var2.getRGB(var7, var8);
         }

         IntBuffer var11 = BufferUtils.createIntBuffer(var3 * var4);
         var11.put(var5);
         var11.rewind();
         byte var12 = 1;
         byte var13 = 1;
         Cursor var9 = new Cursor(var3, var4, var12, var13, 1, var11, (IntBuffer)null);
         return var9;
      } catch (Exception var10) {
         return null;
      }
   }

   public static void initCustomCursor() {
      if (blankCursor == null) {
         try {
            blankCursor = loadCursor("cursor_blank.png");
            defaultCursor = loadCursor("cursor_white.png");
         } catch (LWJGLException var2) {
            var2.printStackTrace();
         }
      }

      if (defaultCursor != null) {
         try {
            org.lwjglx.input.Mouse.setNativeCursor(defaultCursor);
         } catch (LWJGLException var1) {
            var1.printStackTrace();
         }

      }
   }

   public static void setCursorVisible(boolean var0) {
      isCursorVisible = var0;
   }

   public static boolean isCursorVisible() {
      return isCursorVisible;
   }

   public static void renderCursorTexture() {
      if (isCursorVisible()) {
         if (mouseCursorTexture == null) {
            mouseCursorTexture = Texture.getSharedTexture("media/ui/cursor_white.png");
         }

         if (mouseCursorTexture != null && mouseCursorTexture.isReady()) {
            int var0 = getXA();
            int var1 = getYA();
            byte var2 = 1;
            byte var3 = 1;
            SpriteRenderer.instance.render(mouseCursorTexture, (float)(var0 - var2), (float)(var1 - var3), (float)mouseCursorTexture.getWidth(), (float)mouseCursorTexture.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
         }
      }
   }
}
