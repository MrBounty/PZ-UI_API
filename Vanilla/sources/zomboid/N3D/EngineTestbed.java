package N3D;

import java.io.IOException;
import org.lwjglx.LWJGLException;
import org.lwjglx.opengl.Display;
import zombie.GameWindow;
import zombie.core.Rand;
import zombie.input.KeyboardState;
import zombie.input.MouseState;

public class EngineTestbed {
   static MouseState mouse = new MouseState();
   static KeyboardState keyboard = new KeyboardState();

   public static void main(String[] var0) throws InterruptedException {
      Rand.init();

      try {
         GameWindow.InitDisplay();
      } catch (IOException var2) {
         var2.printStackTrace();
      } catch (LWJGLException var3) {
         var3.printStackTrace();
      }

      while(!Display.isCloseRequested()) {
         mouse.poll();
         keyboard.poll();
         Display.update(true);
      }

   }
}
