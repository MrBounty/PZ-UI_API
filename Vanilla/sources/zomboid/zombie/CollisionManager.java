package zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import zombie.characters.IsoSurvivor;
import zombie.characters.ZombieFootstepManager;
import zombie.characters.ZombieThumpManager;
import zombie.characters.ZombieVocalsManager;
import zombie.core.collision.Polygon;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoPushableObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;

public final class CollisionManager {
   static Vector2 temp = new Vector2();
   static Vector2 axis = new Vector2();
   static Polygon polygonA = new Polygon();
   static Polygon polygonB = new Polygon();
   float minA = 0.0F;
   float minB = 0.0F;
   float maxA = 0.0F;
   float maxB = 0.0F;
   CollisionManager.PolygonCollisionResult result = new CollisionManager.PolygonCollisionResult();
   public ArrayList ContactMap = new ArrayList();
   Long[] longArray = new Long[1000];
   Stack contacts = new Stack();
   public static final CollisionManager instance = new CollisionManager();

   private void ProjectPolygonA(Vector2 var1, Polygon var2) {
      float var3 = var1.dot((Vector2)var2.points.get(0));
      this.minA = var3;
      this.maxA = var3;

      for(int var4 = 0; var4 < var2.points.size(); ++var4) {
         var3 = ((Vector2)var2.points.get(var4)).dot(var1);
         if (var3 < this.minA) {
            this.minA = var3;
         } else if (var3 > this.maxA) {
            this.maxA = var3;
         }
      }

   }

   private void ProjectPolygonB(Vector2 var1, Polygon var2) {
      float var3 = var1.dot((Vector2)var2.points.get(0));
      this.minB = var3;
      this.maxB = var3;

      for(int var4 = 0; var4 < var2.points.size(); ++var4) {
         var3 = ((Vector2)var2.points.get(var4)).dot(var1);
         if (var3 < this.minB) {
            this.minB = var3;
         } else if (var3 > this.maxB) {
            this.maxB = var3;
         }
      }

   }

   public CollisionManager.PolygonCollisionResult PolygonCollision(Vector2 var1) {
      this.result.Intersect = true;
      this.result.WillIntersect = true;
      this.result.MinimumTranslationVector.x = 0.0F;
      this.result.MinimumTranslationVector.y = 0.0F;
      int var2 = polygonA.edges.size();
      int var3 = polygonB.edges.size();
      float var4 = Float.POSITIVE_INFINITY;
      Vector2 var5 = new Vector2();

      for(int var7 = 0; var7 < var2 + var3; ++var7) {
         Vector2 var6;
         if (var7 < var2) {
            var6 = (Vector2)polygonA.edges.get(var7);
         } else {
            var6 = (Vector2)polygonB.edges.get(var7 - var2);
         }

         axis.x = -var6.y;
         axis.y = var6.x;
         axis.normalize();
         this.minA = 0.0F;
         this.minB = 0.0F;
         this.maxA = 0.0F;
         this.maxB = 0.0F;
         this.ProjectPolygonA(axis, polygonA);
         this.ProjectPolygonB(axis, polygonB);
         if (this.IntervalDistance(this.minA, this.maxA, this.minB, this.maxB) > 0.0F) {
            this.result.Intersect = false;
         }

         float var8 = axis.dot(var1);
         if (var8 < 0.0F) {
            this.minA += var8;
         } else {
            this.maxA += var8;
         }

         float var9 = this.IntervalDistance(this.minA, this.maxA, this.minB, this.maxB);
         if (var9 > 0.0F) {
            this.result.WillIntersect = false;
         }

         if (!this.result.Intersect && !this.result.WillIntersect) {
            break;
         }

         var9 = Math.abs(var9);
         if (var9 < var4) {
            var4 = var9;
            var5.x = axis.x;
            var5.y = axis.y;
            temp.x = polygonA.Center().x - polygonB.Center().x;
            temp.y = polygonA.Center().y - polygonB.Center().y;
            if (temp.dot(var5) < 0.0F) {
               var5.x = -var5.x;
               var5.y = -var5.y;
            }
         }
      }

      if (this.result.WillIntersect) {
         this.result.MinimumTranslationVector.x = var5.x * var4;
         this.result.MinimumTranslationVector.y = var5.y * var4;
      }

      return this.result;
   }

   public float IntervalDistance(float var1, float var2, float var3, float var4) {
      return var1 < var3 ? var3 - var2 : var1 - var4;
   }

   public void initUpdate() {
      int var1;
      if (this.longArray[0] == null) {
         for(var1 = 0; var1 < this.longArray.length; ++var1) {
            this.longArray[var1] = new Long(0L);
         }
      }

      for(var1 = 0; var1 < this.ContactMap.size(); ++var1) {
         ((CollisionManager.Contact)this.ContactMap.get(var1)).a = null;
         ((CollisionManager.Contact)this.ContactMap.get(var1)).b = null;
         this.contacts.push((CollisionManager.Contact)this.ContactMap.get(var1));
      }

      this.ContactMap.clear();
   }

   public void AddContact(IsoMovingObject var1, IsoMovingObject var2) {
      if (!(var1 instanceof IsoSurvivor) && !(var2 instanceof IsoSurvivor) || !(var1 instanceof IsoPushableObject) && !(var2 instanceof IsoPushableObject)) {
         if (var1.getID() < var2.getID()) {
            this.ContactMap.add(this.contact(var1, var2));
         }

      }
   }

   CollisionManager.Contact contact(IsoMovingObject var1, IsoMovingObject var2) {
      if (this.contacts.isEmpty()) {
         for(int var3 = 0; var3 < 50; ++var3) {
            this.contacts.push(new CollisionManager.Contact((IsoMovingObject)null, (IsoMovingObject)null));
         }
      }

      CollisionManager.Contact var4 = (CollisionManager.Contact)this.contacts.pop();
      var4.a = var1;
      var4.b = var2;
      return var4;
   }

   public void ResolveContacts() {
      CollisionManager.s_performance.profile_ResolveContacts.invokeAndMeasure(this, CollisionManager::resolveContactsInternal);
   }

   private void resolveContactsInternal() {
      Vector2 var1 = CollisionManager.l_ResolveContacts.vel;
      Vector2 var2 = CollisionManager.l_ResolveContacts.vel2;
      List var3 = CollisionManager.l_ResolveContacts.pushables;
      ArrayList var4 = IsoWorld.instance.CurrentCell.getPushableObjectList();
      int var5 = var4.size();

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         IsoPushableObject var7 = (IsoPushableObject)var4.get(var6);
         if (var7.getImpulsex() != 0.0F || var7.getImpulsey() != 0.0F) {
            if (var7.connectList != null) {
               var3.add(var7);
            } else {
               var7.setNx(var7.getNx() + var7.getImpulsex());
               var7.setNy(var7.getNy() + var7.getImpulsey());
               var7.setImpulsex(var7.getNx() - var7.getX());
               var7.setImpulsey(var7.getNy() - var7.getY());
               var7.setNx(var7.getX());
               var7.setNy(var7.getY());
            }
         }
      }

      var6 = var3.size();

      float var10;
      int var24;
      for(var24 = 0; var24 < var6; ++var24) {
         IsoPushableObject var8 = (IsoPushableObject)var3.get(var24);
         float var9 = 0.0F;
         var10 = 0.0F;

         int var11;
         for(var11 = 0; var11 < var8.connectList.size(); ++var11) {
            var9 += ((IsoPushableObject)var8.connectList.get(var11)).getImpulsex();
            var10 += ((IsoPushableObject)var8.connectList.get(var11)).getImpulsey();
         }

         var9 /= (float)var8.connectList.size();
         var10 /= (float)var8.connectList.size();

         for(var11 = 0; var11 < var8.connectList.size(); ++var11) {
            ((IsoPushableObject)var8.connectList.get(var11)).setImpulsex(var9);
            ((IsoPushableObject)var8.connectList.get(var11)).setImpulsey(var10);
            int var12 = var3.indexOf(var8.connectList.get(var11));
            var3.remove(var8.connectList.get(var11));
            if (var12 <= var24) {
               --var24;
            }
         }

         if (var24 < 0) {
            var24 = 0;
         }
      }

      var3.clear();
      var24 = this.ContactMap.size();

      for(int var25 = 0; var25 < var24; ++var25) {
         CollisionManager.Contact var27 = (CollisionManager.Contact)this.ContactMap.get(var25);
         if (!(Math.abs(var27.a.getZ() - var27.b.getZ()) > 0.3F)) {
            var1.x = var27.a.getNx() - var27.a.getX();
            var1.y = var27.a.getNy() - var27.a.getY();
            var2.x = var27.b.getNx() - var27.b.getX();
            var2.y = var27.b.getNy() - var27.b.getY();
            if (var1.x != 0.0F || var1.y != 0.0F || var2.x != 0.0F || var2.y != 0.0F || var27.a.getImpulsex() != 0.0F || var27.a.getImpulsey() != 0.0F || var27.b.getImpulsex() != 0.0F || var27.b.getImpulsey() != 0.0F) {
               var10 = var27.a.getX() - var27.a.getWidth();
               float var29 = var27.a.getX() + var27.a.getWidth();
               float var30 = var27.a.getY() - var27.a.getWidth();
               float var13 = var27.a.getY() + var27.a.getWidth();
               float var14 = var27.b.getX() - var27.b.getWidth();
               float var15 = var27.b.getX() + var27.b.getWidth();
               float var16 = var27.b.getY() - var27.b.getWidth();
               float var17 = var27.b.getY() + var27.b.getWidth();
               polygonA.Set(var10, var30, var29, var13);
               polygonB.Set(var14, var16, var15, var17);
               CollisionManager.PolygonCollisionResult var18 = this.PolygonCollision(var1);
               if (var18.WillIntersect) {
                  var27.a.collideWith(var27.b);
                  var27.b.collideWith(var27.a);
                  float var19 = 1.0F - var27.a.getWeight(var18.MinimumTranslationVector.x, var18.MinimumTranslationVector.y) / (var27.a.getWeight(var18.MinimumTranslationVector.x, var18.MinimumTranslationVector.y) + var27.b.getWeight(var18.MinimumTranslationVector.x, var18.MinimumTranslationVector.y));
                  if (var27.a instanceof IsoPushableObject && var27.b instanceof IsoSurvivor) {
                     ((IsoSurvivor)var27.b).bCollidedWithPushable = true;
                     ((IsoSurvivor)var27.b).collidePushable = (IsoPushableObject)var27.a;
                  } else if (var27.b instanceof IsoPushableObject && var27.a instanceof IsoSurvivor) {
                     ((IsoSurvivor)var27.a).bCollidedWithPushable = true;
                     ((IsoSurvivor)var27.a).collidePushable = (IsoPushableObject)var27.b;
                  }

                  ArrayList var20;
                  int var21;
                  int var22;
                  IsoPushableObject var23;
                  if (var27.a instanceof IsoPushableObject) {
                     var20 = ((IsoPushableObject)var27.a).connectList;
                     if (var20 != null) {
                        var21 = var20.size();

                        for(var22 = 0; var22 < var21; ++var22) {
                           var23 = (IsoPushableObject)var20.get(var22);
                           var23.setImpulsex(var23.getImpulsex() + var18.MinimumTranslationVector.x * var19);
                           var23.setImpulsey(var23.getImpulsey() + var18.MinimumTranslationVector.y * var19);
                        }
                     }
                  } else {
                     var27.a.setImpulsex(var27.a.getImpulsex() + var18.MinimumTranslationVector.x * var19);
                     var27.a.setImpulsey(var27.a.getImpulsey() + var18.MinimumTranslationVector.y * var19);
                  }

                  if (var27.b instanceof IsoPushableObject) {
                     var20 = ((IsoPushableObject)var27.b).connectList;
                     if (var20 != null) {
                        var21 = var20.size();

                        for(var22 = 0; var22 < var21; ++var22) {
                           var23 = (IsoPushableObject)var20.get(var22);
                           var23.setImpulsex(var23.getImpulsex() - var18.MinimumTranslationVector.x * (1.0F - var19));
                           var23.setImpulsey(var23.getImpulsey() - var18.MinimumTranslationVector.y * (1.0F - var19));
                        }
                     }
                  } else {
                     var27.b.setImpulsex(var27.b.getImpulsex() - var18.MinimumTranslationVector.x * (1.0F - var19));
                     var27.b.setImpulsey(var27.b.getImpulsey() - var18.MinimumTranslationVector.y * (1.0F - var19));
                  }
               }
            }
         }
      }

      ArrayList var26 = IsoWorld.instance.CurrentCell.getObjectList();
      int var28 = var26.size();
      MovingObjectUpdateScheduler.instance.postupdate();
      IsoMovingObject.treeSoundMgr.update();
      ZombieFootstepManager.instance.update();
      ZombieThumpManager.instance.update();
      ZombieVocalsManager.instance.update();
   }

   public class PolygonCollisionResult {
      public boolean WillIntersect;
      public boolean Intersect;
      public Vector2 MinimumTranslationVector = new Vector2();
   }

   public class Contact {
      public IsoMovingObject a;
      public IsoMovingObject b;

      public Contact(IsoMovingObject var2, IsoMovingObject var3) {
         this.a = var2;
         this.b = var3;
      }
   }

   private static class s_performance {
      static final PerformanceProfileProbe profile_ResolveContacts = new PerformanceProfileProbe("CollisionManager.ResolveContacts");
      static final PerformanceProfileProbe profile_MovingObjectPostUpdate = new PerformanceProfileProbe("IsoMovingObject.postupdate");
   }

   private static class l_ResolveContacts {
      static final Vector2 vel = new Vector2();
      static final Vector2 vel2 = new Vector2();
      static final List pushables = new ArrayList();
      static IsoMovingObject[] objectListInvoking = new IsoMovingObject[1024];
   }
}
