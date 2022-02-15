package zombie.characters;

public interface ILuaVariableSource {
   String GetVariable(String var1);

   void SetVariable(String var1, String var2);

   void ClearVariable(String var1);
}
