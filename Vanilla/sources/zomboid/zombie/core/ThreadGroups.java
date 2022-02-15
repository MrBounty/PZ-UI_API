package zombie.core;

public final class ThreadGroups {
   public static final ThreadGroup Root = new ThreadGroup("PZ");
   public static final ThreadGroup Main;
   public static final ThreadGroup Workers;
   public static final ThreadGroup Network;

   static {
      Main = new ThreadGroup(Root, "Main");
      Workers = new ThreadGroup(Root, "Workers");
      Network = new ThreadGroup(Root, "Network");
   }
}
