package zombie.core.logger;

import java.util.function.Consumer;
import org.lwjglx.opengl.OpenGLException;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderThread;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugLogStream;
import zombie.debug.LogSeverity;
import zombie.network.GameServer;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.ui.UITransition;
import zombie.util.Type;

public final class ExceptionLogger {
   private static int exceptionCount;
   private static boolean bIgnore;
   private static boolean bExceptionPopup = true;
   private static long popupFrameMS = 0L;
   private static UITransition transition = new UITransition();
   private static boolean bHide;

   public static synchronized void logException(Throwable var0) {
      logException(var0, (String)null);
   }

   public static synchronized void logException(Throwable var0, String var1) {
      logException(var0, var1, DebugLog.General, LogSeverity.Error);
   }

   public static synchronized void logException(Throwable var0, String var1, DebugLogStream var2, LogSeverity var3) {
      OpenGLException var4 = (OpenGLException)Type.tryCastTo(var0, OpenGLException.class);
      if (var4 != null) {
         RenderThread.logGLException(var4, false);
      }

      var2.printException(var0, var1, DebugLogStream.generateCallerPrefix(), var3);

      try {
         if (bIgnore) {
            return;
         }

         bIgnore = true;
         ++exceptionCount;
         if (GameServer.bServer) {
            return;
         }

         if (bExceptionPopup) {
            showPopup();
         }
      } catch (Throwable var9) {
         var2.printException(var9, "Exception thrown while trying to logException.", LogSeverity.Error);
      } finally {
         bIgnore = false;
      }

   }

   public static void showPopup() {
      float var0 = popupFrameMS > 0L ? transition.getElapsed() : 0.0F;
      popupFrameMS = 3000L;
      transition.setIgnoreUpdateTime(true);
      transition.init(500.0F, false);
      transition.setElapsed(var0);
      bHide = false;
   }

   public static void render() {
      if (!UIManager.useUIFBO || Core.getInstance().UIRenderThisFrame) {
         boolean var0 = false;
         if (var0) {
            popupFrameMS = 3000L;
         }

         if (popupFrameMS > 0L) {
            popupFrameMS = (long)((double)popupFrameMS - UIManager.getMillisSinceLastRender());
            transition.update();
            int var1 = TextManager.instance.getFontHeight(UIFont.DebugConsole);
            byte var2 = 100;
            int var3 = var1 * 2 + 4;
            int var4 = Core.getInstance().getScreenWidth() - var2;
            int var5 = Core.getInstance().getScreenHeight() - (int)((float)var3 * transition.fraction());
            if (var0) {
               var5 = Core.getInstance().getScreenHeight() - var3;
            }

            SpriteRenderer.instance.renderi((Texture)null, var4, var5, var2, var3, 0.8F, 0.0F, 0.0F, 1.0F, (Consumer)null);
            SpriteRenderer.instance.renderi((Texture)null, var4 + 1, var5 + 1, var2 - 2, var1 - 1, 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)(var4 + var2 / 2), (double)var5, "ERROR", 1.0D, 0.0D, 0.0D, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)(var4 + var2 / 2), (double)(var5 + var1), var0 ? "999" : Integer.toString(exceptionCount), 0.0D, 0.0D, 0.0D, 1.0D);
            if (popupFrameMS <= 0L && !bHide) {
               popupFrameMS = 500L;
               transition.init(500.0F, true);
               bHide = true;
            }

         }
      }
   }
}
