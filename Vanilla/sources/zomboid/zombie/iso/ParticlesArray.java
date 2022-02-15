package zombie.iso;

import java.util.ArrayList;
import zombie.debug.DebugLog;

public final class ParticlesArray extends ArrayList {
   private boolean needToUpdate;
   private int ParticleSystemsCount = 0;
   private int ParticleSystemsLast = 0;

   public ParticlesArray() {
      this.ParticleSystemsCount = 0;
      this.ParticleSystemsLast = 0;
      this.needToUpdate = true;
   }

   public synchronized int addParticle(Object var1) {
      if (var1 == null) {
         return -1;
      } else if (this.size() == this.ParticleSystemsCount) {
         this.add(var1);
         ++this.ParticleSystemsCount;
         this.needToUpdate = true;
         return this.size() - 1;
      } else {
         int var2;
         for(var2 = this.ParticleSystemsLast; var2 < this.size(); ++var2) {
            if (this.get(var2) == null) {
               this.ParticleSystemsLast = var2;
               this.set(var2, var1);
               ++this.ParticleSystemsCount;
               this.needToUpdate = true;
               return var2;
            }
         }

         for(var2 = 0; var2 < this.ParticleSystemsLast; ++var2) {
            if (this.get(var2) == null) {
               this.ParticleSystemsLast = var2;
               this.set(var2, var1);
               ++this.ParticleSystemsCount;
               this.needToUpdate = true;
               return var2;
            }
         }

         DebugLog.log("ERROR: ParticlesArray.addParticle has unknown error");
         return -1;
      }
   }

   public synchronized boolean deleteParticle(int var1) {
      if (var1 >= 0 && var1 < this.size() && this.get(var1) != null) {
         this.set(var1, (Object)null);
         --this.ParticleSystemsCount;
         this.needToUpdate = true;
         return true;
      } else {
         return false;
      }
   }

   public synchronized void defragmentParticle() {
      this.needToUpdate = false;
      if (this.ParticleSystemsCount != this.size() && this.size() != 0) {
         int var1 = -1;

         int var2;
         for(var2 = 0; var2 < this.size(); ++var2) {
            if (this.get(var2) == null) {
               var1 = var2;
               break;
            }
         }

         for(var2 = this.size() - 1; var2 >= 0; --var2) {
            if (this.get(var2) != null) {
               this.set(var1, this.get(var2));
               this.set(var2, (Object)null);

               for(int var3 = var1; var3 < this.size(); ++var3) {
                  if (this.get(var3) == null) {
                     var1 = var3;
                     break;
                  }
               }

               if (var1 + 1 >= var2) {
                  this.ParticleSystemsLast = var1;
                  break;
               }
            }
         }

      }
   }

   public synchronized int getCount() {
      return this.ParticleSystemsCount;
   }

   public synchronized boolean getNeedToUpdate() {
      return this.needToUpdate;
   }
}
