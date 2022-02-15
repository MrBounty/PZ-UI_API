package zombie.characters;

import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import zombie.DummySoundManager;
import zombie.SoundManager;
import zombie.core.Rand;
import zombie.network.ServerMap;

public class ZombieIDMapTest extends Assert {
   HashSet IDs = new HashSet();

   @Test
   public void test10Allocations() {
      Rand.init();
      this.IDs.clear();
      byte var1 = 10;

      for(short var2 = 0; var2 < var1; ++var2) {
         short var3 = ServerMap.instance.getUniqueZombieId();
         System.out.println("id:" + var3);
      }

   }

   @Test
   public void test32653Allocations() {
      Rand.init();
      this.IDs.clear();
      char var1 = '蝝';
      long var2 = System.nanoTime();

      for(int var4 = 0; var4 < var1; ++var4) {
         short var5 = ServerMap.instance.getUniqueZombieId();
         assertFalse(this.IDs.contains(var5));
         this.IDs.add(var5);
      }

      long var7 = System.nanoTime();
      float var6 = (float)(var7 - var2) / 1000000.0F;
      System.out.println("time:" + var6);
      System.out.println("time per task:" + var6 / (float)var1);
   }

   @Test
   public void test32653Adds() {
      SoundManager.instance = new DummySoundManager();
      Rand.init();
      SurvivorFactory.addMaleForename("Bob");
      SurvivorFactory.addFemaleForename("Kate");
      SurvivorFactory.addSurname("Testova");
      this.IDs.clear();
      short var1 = 32653;
      long var2 = System.nanoTime();

      for(short var4 = 0; var4 < var1; ++var4) {
         short var5 = ServerMap.instance.getUniqueZombieId();
         assertNull(ServerMap.instance.ZombieMap.get(var5));
         assertFalse(this.IDs.contains(var5));
         ServerMap.instance.ZombieMap.put(var5, new IsoZombie(var5));
         assertEquals((long)var5, (long)ServerMap.instance.ZombieMap.get(var5).OnlineID);
         this.IDs.add(var5);
      }

      long var7 = System.nanoTime();
      float var6 = (float)(var7 - var2) / 1000000.0F;
      System.out.println("time:" + var6);
      System.out.println("time per task:" + var6 / (float)var1);
   }

   @Test
   public void test32653Process() {
      Rand.init();
      ServerMap.instance = new ServerMap();
      SoundManager.instance = new DummySoundManager();
      SurvivorFactory.addMaleForename("Bob");
      SurvivorFactory.addFemaleForename("Kate");
      SurvivorFactory.addSurname("Testova");
      this.IDs.clear();
      short var1 = 32653;
      long var2 = System.nanoTime();

      for(short var4 = 0; var4 < var1; ++var4) {
         assertNull(ServerMap.instance.ZombieMap.get(var4));
         ServerMap.instance.ZombieMap.put(var4, new IsoZombie(var4));
         assertEquals((long)var4, (long)ServerMap.instance.ZombieMap.get(var4).OnlineID);
      }

      long var19 = System.nanoTime();

      for(short var6 = 0; var6 < var1; ++var6) {
         assertEquals((long)var6, (long)ServerMap.instance.ZombieMap.get(var6).OnlineID);
         ServerMap.instance.ZombieMap.remove(var6);
         assertNull(ServerMap.instance.ZombieMap.get(var6));
      }

      long var20 = System.nanoTime();

      for(short var8 = 0; var8 < var1; ++var8) {
         assertNull(ServerMap.instance.ZombieMap.get(var8));
         ServerMap.instance.ZombieMap.put(var8, new IsoZombie(var8));
         assertEquals((long)var8, (long)ServerMap.instance.ZombieMap.get(var8).OnlineID);
      }

      long var21 = System.nanoTime();

      for(short var10 = 0; var10 < var1; ++var10) {
         assertEquals((long)var10, (long)ServerMap.instance.ZombieMap.get(var10).OnlineID);
         ServerMap.instance.ZombieMap.remove(var10);
         assertNull(ServerMap.instance.ZombieMap.get(var10));
      }

      long var22 = System.nanoTime();

      for(short var12 = 0; var12 < var1; ++var12) {
         assertNull(ServerMap.instance.ZombieMap.get(var12));
         ServerMap.instance.ZombieMap.put(var12, new IsoZombie(var12));
         assertEquals((long)var12, (long)ServerMap.instance.ZombieMap.get(var12).OnlineID);
      }

      long var23 = System.nanoTime();
      float var14 = (float)(var19 - var2) / 1000000.0F;
      float var15 = (float)(var20 - var19) / 1000000.0F;
      float var16 = (float)(var21 - var20) / 1000000.0F;
      float var17 = (float)(var22 - var21) / 1000000.0F;
      float var18 = (float)(var23 - var22) / 1000000.0F;
      System.out.println("time1:" + var14);
      System.out.println("time2:" + var15);
      System.out.println("time3:" + var16);
      System.out.println("time4:" + var17);
      System.out.println("time5:" + var18);
   }
}
