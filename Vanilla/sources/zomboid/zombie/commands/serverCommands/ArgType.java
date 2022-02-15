package zombie.commands.serverCommands;

public class ArgType {
   public static final String PlayerName = "(.+)";
   public static final String AnyText = "(.+)";
   public static final String Script = "([a-zA-Z0-9.-]*[a-zA-Z][a-zA-Z0-9_.-]*)";
   public static final String Coordinates = "(\\d+),(\\d+),(\\d+)";
   public static final String IP = "((?:\\d{1,3}\\.){3}\\d{1,3})";
   public static final String TrueFalse = "(-true|-false)";
   public static final String ItemName = "([a-zA-Z0-9.-]*[a-zA-Z][a-zA-Z0-9_.-]*)";
}
