package zombie.Lua;

import gnu.trove.list.array.TShortArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Prototype;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public final class MapObjects {
   private static final HashMap onNew = new HashMap();
   private static final HashMap onLoad = new HashMap();
   private static final ArrayList tempObjects = new ArrayList();
   private static final Object[] params = new Object[1];

   private static MapObjects.Callback getOnNew(String var0) {
      MapObjects.Callback var1 = (MapObjects.Callback)onNew.get(var0);
      if (var1 == null) {
         var1 = new MapObjects.Callback(var0);
         onNew.put(var0, var1);
      }

      return var1;
   }

   public static void OnNewWithSprite(String var0, LuaClosure var1, int var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (var1 == null) {
            throw new NullPointerException("function is null");
         } else {
            MapObjects.Callback var3 = getOnNew(var0);

            for(int var4 = 0; var4 < var3.functions.size(); ++var4) {
               if (var3.priority.get(var4) < var2) {
                  var3.functions.add(var4, var1);
                  var3.priority.insert(var4, (short)var2);
                  return;
               }

               if (var3.priority.get(var4) == var2) {
                  var3.functions.set(var4, var1);
                  var3.priority.set(var4, (short)var2);
                  return;
               }
            }

            var3.functions.add(var1);
            var3.priority.add((short)var2);
         }
      } else {
         throw new IllegalArgumentException("invalid sprite name");
      }
   }

   public static void OnNewWithSprite(KahluaTable var0, LuaClosure var1, int var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (var1 == null) {
            throw new NullPointerException("function is null");
         } else {
            KahluaTableIterator var3 = var0.iterator();

            while(var3.advance()) {
               Object var4 = var3.getValue();
               if (!(var4 instanceof String)) {
                  throw new IllegalArgumentException("expected string but got \"" + var4 + "\"");
               }

               OnNewWithSprite((String)var4, var1, var2);
            }

         }
      } else {
         throw new IllegalArgumentException("invalid sprite-name table");
      }
   }

   public static void newGridSquare(IsoGridSquare var0) {
      if (var0 != null && !var0.getObjects().isEmpty()) {
         tempObjects.clear();

         int var1;
         for(var1 = 0; var1 < var0.getObjects().size(); ++var1) {
            tempObjects.add((IsoObject)var0.getObjects().get(var1));
         }

         for(var1 = 0; var1 < tempObjects.size(); ++var1) {
            IsoObject var2 = (IsoObject)tempObjects.get(var1);
            if (var0.getObjects().contains(var2) && !(var2 instanceof IsoWorldInventoryObject) && var2 != null && var2.sprite != null) {
               String var3 = var2.sprite.name == null ? var2.spriteName : var2.sprite.name;
               if (var3 != null && !var3.isEmpty()) {
                  MapObjects.Callback var4 = (MapObjects.Callback)onNew.get(var3);
                  if (var4 != null) {
                     params[0] = var2;

                     for(int var5 = 0; var5 < var4.functions.size(); ++var5) {
                        try {
                           LuaManager.caller.protectedCallVoid(LuaManager.thread, var4.functions.get(var5), params);
                        } catch (Throwable var7) {
                           ExceptionLogger.logException(var7);
                        }

                        var3 = var2.sprite != null && var2.sprite.name != null ? var2.sprite.name : var2.spriteName;
                        if (!var0.getObjects().contains(var2) || var2.sprite == null || !var4.spriteName.equals(var3)) {
                           break;
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private static MapObjects.Callback getOnLoad(String var0) {
      MapObjects.Callback var1 = (MapObjects.Callback)onLoad.get(var0);
      if (var1 == null) {
         var1 = new MapObjects.Callback(var0);
         onLoad.put(var0, var1);
      }

      return var1;
   }

   public static void OnLoadWithSprite(String var0, LuaClosure var1, int var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (var1 == null) {
            throw new NullPointerException("function is null");
         } else {
            MapObjects.Callback var3 = getOnLoad(var0);

            for(int var4 = 0; var4 < var3.functions.size(); ++var4) {
               if (var3.priority.get(var4) < var2) {
                  var3.functions.add(var4, var1);
                  var3.priority.insert(var4, (short)var2);
                  return;
               }

               if (var3.priority.get(var4) == var2) {
                  var3.functions.set(var4, var1);
                  var3.priority.set(var4, (short)var2);
                  return;
               }
            }

            var3.functions.add(var1);
            var3.priority.add((short)var2);
         }
      } else {
         throw new IllegalArgumentException("invalid sprite name");
      }
   }

   public static void OnLoadWithSprite(KahluaTable var0, LuaClosure var1, int var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (var1 == null) {
            throw new NullPointerException("function is null");
         } else {
            KahluaTableIterator var3 = var0.iterator();

            while(var3.advance()) {
               Object var4 = var3.getValue();
               if (!(var4 instanceof String)) {
                  throw new IllegalArgumentException("expected string but got \"" + var4 + "\"");
               }

               OnLoadWithSprite((String)var4, var1, var2);
            }

         }
      } else {
         throw new IllegalArgumentException("invalid sprite-name table");
      }
   }

   public static void loadGridSquare(IsoGridSquare var0) {
      if (var0 != null && !var0.getObjects().isEmpty()) {
         tempObjects.clear();

         int var1;
         for(var1 = 0; var1 < var0.getObjects().size(); ++var1) {
            tempObjects.add((IsoObject)var0.getObjects().get(var1));
         }

         for(var1 = 0; var1 < tempObjects.size(); ++var1) {
            IsoObject var2 = (IsoObject)tempObjects.get(var1);
            if (var0.getObjects().contains(var2) && !(var2 instanceof IsoWorldInventoryObject) && var2 != null && var2.sprite != null) {
               String var3 = var2.sprite.name == null ? var2.spriteName : var2.sprite.name;
               if (var3 != null && !var3.isEmpty()) {
                  MapObjects.Callback var4 = (MapObjects.Callback)onLoad.get(var3);
                  if (var4 != null) {
                     params[0] = var2;

                     for(int var5 = 0; var5 < var4.functions.size(); ++var5) {
                        try {
                           LuaManager.caller.protectedCallVoid(LuaManager.thread, var4.functions.get(var5), params);
                        } catch (Throwable var7) {
                           ExceptionLogger.logException(var7);
                        }

                        var3 = var2.sprite != null && var2.sprite.name != null ? var2.sprite.name : var2.spriteName;
                        if (!var0.getObjects().contains(var2) || var2.sprite == null || !var4.spriteName.equals(var3)) {
                           break;
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public static void debugNewSquare(int var0, int var1, int var2) {
      if (Core.bDebug) {
         IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var0, var1, var2);
         if (var3 != null) {
            newGridSquare(var3);
         }
      }
   }

   public static void debugLoadSquare(int var0, int var1, int var2) {
      if (Core.bDebug) {
         IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var0, var1, var2);
         if (var3 != null) {
            loadGridSquare(var3);
         }
      }
   }

   public static void debugLoadChunk(int var0, int var1) {
      if (Core.bDebug) {
         IsoChunk var2 = GameServer.bServer ? ServerMap.instance.getChunk(var0, var1) : IsoWorld.instance.CurrentCell.getChunk(var0, var1);
         if (var2 != null) {
            for(int var3 = 0; var3 <= var2.maxLevel; ++var3) {
               for(int var4 = 0; var4 < 10; ++var4) {
                  for(int var5 = 0; var5 < 10; ++var5) {
                     IsoGridSquare var6 = var2.getGridSquare(var4, var5, var3);
                     if (var6 != null && !var6.getObjects().isEmpty()) {
                        loadGridSquare(var6);
                     }
                  }
               }
            }

         }
      }
   }

   public static void reroute(Prototype var0, LuaClosure var1) {
      Iterator var2 = onNew.values().iterator();

      while(var2.hasNext()) {
         MapObjects.Callback var3 = (MapObjects.Callback)var2.next();

         for(int var4 = 0; var4 < var3.functions.size(); ++var4) {
            LuaClosure var5 = (LuaClosure)var3.functions.get(var4);
            if (var5.prototype.filename.equals(var0.filename) && var5.prototype.name.equals(var0.name)) {
               var3.functions.set(var4, var1);
            }
         }
      }

   }

   public static void Reset() {
      onNew.clear();
      onLoad.clear();
   }

   private static final class Callback {
      final String spriteName;
      final ArrayList functions = new ArrayList();
      final TShortArrayList priority = new TShortArrayList();

      Callback(String var1) {
         this.spriteName = var1;
      }
   }
}
