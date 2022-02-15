package zombie.ai.astar;

public interface IPathfinder {
   void Failed(Mover var1);

   void Succeeded(Path var1, Mover var2);

   String getName();
}
