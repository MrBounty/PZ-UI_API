package zombie;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoRoom;

public final class ZombieSpawnRecorder {
   public static final ZombieSpawnRecorder instance = new ZombieSpawnRecorder();
   public ZLogger m_logger;
   private final StringBuilder m_stringBuilder = new StringBuilder();

   public void init() {
      if (this.m_logger != null) {
         this.m_logger.write("================================================================================");
      } else {
         LoggerManager.init();
         LoggerManager.createLogger("ZombieSpawn", Core.bDebug);
         this.m_logger = LoggerManager.getLogger("ZombieSpawn");
      }
   }

   public void quit() {
      if (this.m_logger != null) {
         if (this.m_stringBuilder.length() > 0) {
            this.m_logger.write(this.m_stringBuilder.toString());
            this.m_stringBuilder.setLength(0);
         }

      }
   }

   public void record(IsoZombie var1, String var2) {
      if (var1 != null && var1.getCurrentSquare() != null) {
         if (this.m_logger != null) {
            IsoGridSquare var3 = var1.getCurrentSquare();
            this.m_stringBuilder.append("reason = ");
            this.m_stringBuilder.append(var2);
            this.m_stringBuilder.append(" x,y,z = ");
            this.m_stringBuilder.append(var3.x);
            this.m_stringBuilder.append(',');
            this.m_stringBuilder.append(var3.y);
            this.m_stringBuilder.append(',');
            this.m_stringBuilder.append(var3.z);
            IsoRoom var4 = var3.getRoom();
            if (var4 != null && var4.def != null) {
               this.m_stringBuilder.append(" room = ");
               this.m_stringBuilder.append(var4.def.name);
            }

            this.m_stringBuilder.append(System.lineSeparator());
            if (this.m_stringBuilder.length() >= 1024) {
               this.m_logger.write(this.m_stringBuilder.toString());
               this.m_stringBuilder.setLength(0);
            }

         }
      }
   }

   public void record(ArrayList var1, String var2) {
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.size(); ++var3) {
            this.record((IsoZombie)var1.get(var3), var2);
         }

      }
   }
}
