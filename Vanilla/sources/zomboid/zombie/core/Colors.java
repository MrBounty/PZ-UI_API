package zombie.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class Colors {
   private static final ArrayList colors = new ArrayList();
   private static final HashMap colorMap = new HashMap();
   private static final ArrayList colorNames = new ArrayList();
   private static final HashSet colorSet = new HashSet();
   public static final Color IndianRed = addColor("IndianRed", new Color(0.804F, 0.361F, 0.361F));
   public static final Color LightCoral = addColor("LightCoral", new Color(0.941F, 0.502F, 0.502F));
   public static final Color Salmon = addColor("Salmon", new Color(0.98F, 0.502F, 0.447F));
   public static final Color DarkSalmon = addColor("DarkSalmon", new Color(0.914F, 0.588F, 0.478F));
   public static final Color LightSalmon = addColor("LightSalmon", new Color(1.0F, 0.627F, 0.478F));
   public static final Color Crimson = addColor("Crimson", new Color(0.863F, 0.078F, 0.235F));
   public static final Color Red = addColor("Red", new Color(1.0F, 0.0F, 0.0F));
   public static final Color FireBrick = addColor("FireBrick", new Color(0.698F, 0.133F, 0.133F));
   public static final Color DarkRed = addColor("DarkRed", new Color(0.545F, 0.0F, 0.0F));
   public static final Color Pink = addColor("Pink", new Color(1.0F, 0.753F, 0.796F));
   public static final Color LightPink = addColor("LightPink", new Color(1.0F, 0.714F, 0.757F));
   public static final Color HotPink = addColor("HotPink", new Color(1.0F, 0.412F, 0.706F));
   public static final Color DeepPink = addColor("DeepPink", new Color(1.0F, 0.078F, 0.576F));
   public static final Color MediumVioletRed = addColor("MediumVioletRed", new Color(0.78F, 0.082F, 0.522F));
   public static final Color PaleVioletRed = addColor("PaleVioletRed", new Color(0.859F, 0.439F, 0.576F));
   public static final Color Coral = addColor("Coral", new Color(1.0F, 0.498F, 0.314F));
   public static final Color Tomato = addColor("Tomato", new Color(1.0F, 0.388F, 0.278F));
   public static final Color OrangeRed = addColor("OrangeRed", new Color(1.0F, 0.271F, 0.0F));
   public static final Color DarkOrange = addColor("DarkOrange", new Color(1.0F, 0.549F, 0.0F));
   public static final Color Orange = addColor("Orange", new Color(1.0F, 0.647F, 0.0F));
   public static final Color Gold = addColor("Gold", new Color(1.0F, 0.843F, 0.0F));
   public static final Color Yellow = addColor("Yellow", new Color(1.0F, 1.0F, 0.0F));
   public static final Color LightYellow = addColor("LightYellow", new Color(1.0F, 1.0F, 0.878F));
   public static final Color LemonChiffon = addColor("LemonChiffon", new Color(1.0F, 0.98F, 0.804F));
   public static final Color LightGoldenrodYellow = addColor("LightGoldenrodYellow", new Color(0.98F, 0.98F, 0.824F));
   public static final Color PapayaWhip = addColor("PapayaWhip", new Color(1.0F, 0.937F, 0.835F));
   public static final Color Moccasin = addColor("Moccasin", new Color(1.0F, 0.894F, 0.71F));
   public static final Color PeachPu = addColor("PeachPu", new Color(1.0F, 0.855F, 0.725F));
   public static final Color PaleGoldenrod = addColor("PaleGoldenrod", new Color(0.933F, 0.91F, 0.667F));
   public static final Color Khaki = addColor("Khaki", new Color(0.941F, 0.902F, 0.549F));
   public static final Color DarkKhaki = addColor("DarkKhaki", new Color(0.741F, 0.718F, 0.42F));
   public static final Color Lavender = addColor("Lavender", new Color(0.902F, 0.902F, 0.98F));
   public static final Color Thistle = addColor("Thistle", new Color(0.847F, 0.749F, 0.847F));
   public static final Color Plum = addColor("Plum", new Color(0.867F, 0.627F, 0.867F));
   public static final Color Violet = addColor("Violet", new Color(0.933F, 0.51F, 0.933F));
   public static final Color Orchid = addColor("Orchid", new Color(0.855F, 0.439F, 0.839F));
   public static final Color Fuchsia = addColor("Fuchsia", new Color(1.0F, 0.0F, 1.0F));
   public static final Color Magenta = addColor("Magenta", new Color(1.0F, 0.0F, 1.0F));
   public static final Color MediumOrchid = addColor("MediumOrchid", new Color(0.729F, 0.333F, 0.827F));
   public static final Color MediumPurple = addColor("MediumPurple", new Color(0.576F, 0.439F, 0.859F));
   public static final Color BlueViolet = addColor("BlueViolet", new Color(0.541F, 0.169F, 0.886F));
   public static final Color DarkViolet = addColor("DarkViolet", new Color(0.58F, 0.0F, 0.827F));
   public static final Color DarkOrchid = addColor("DarkOrchid", new Color(0.6F, 0.196F, 0.8F));
   public static final Color DarkMagenta = addColor("DarkMagenta", new Color(0.545F, 0.0F, 0.545F));
   public static final Color Purple = addColor("Purple", new Color(0.502F, 0.0F, 0.502F));
   public static final Color Indigo = addColor("Indigo", new Color(0.294F, 0.0F, 0.51F));
   public static final Color SlateBlue = addColor("SlateBlue", new Color(0.416F, 0.353F, 0.804F));
   public static final Color DarkSlateBlue = addColor("DarkSlateBlue", new Color(0.282F, 0.239F, 0.545F));
   public static final Color GreenYellow = addColor("GreenYellow", new Color(0.678F, 1.0F, 0.184F));
   public static final Color Chartreuse = addColor("Chartreuse", new Color(0.498F, 1.0F, 0.0F));
   public static final Color LawnGreen = addColor("LawnGreen", new Color(0.486F, 0.988F, 0.0F));
   public static final Color Lime = addColor("Lime", new Color(0.0F, 1.0F, 0.0F));
   public static final Color LimeGreen = addColor("LimeGreen", new Color(0.196F, 0.804F, 0.196F));
   public static final Color PaleGreen = addColor("PaleGreen", new Color(0.596F, 0.984F, 0.596F));
   public static final Color LightGreen = addColor("LightGreen", new Color(0.565F, 0.933F, 0.565F));
   public static final Color MediumSpringGreen = addColor("MediumSpringGreen", new Color(0.0F, 0.98F, 0.604F));
   public static final Color SpringGreen = addColor("SpringGreen", new Color(0.0F, 1.0F, 0.498F));
   public static final Color MediumSeaGreen = addColor("MediumSeaGreen", new Color(0.235F, 0.702F, 0.443F));
   public static final Color SeaGreen = addColor("SeaGreen", new Color(0.18F, 0.545F, 0.341F));
   public static final Color ForestGreen = addColor("ForestGreen", new Color(0.133F, 0.545F, 0.133F));
   public static final Color Green = addColor("Green", new Color(0.0F, 0.502F, 0.0F));
   public static final Color DarkGreen = addColor("DarkGreen", new Color(0.0F, 0.392F, 0.0F));
   public static final Color YellowGreen = addColor("YellowGreen", new Color(0.604F, 0.804F, 0.196F));
   public static final Color OliveDrab = addColor("OliveDrab", new Color(0.42F, 0.557F, 0.137F));
   public static final Color Olive = addColor("Olive", new Color(0.502F, 0.502F, 0.0F));
   public static final Color DarkOliveGreen = addColor("DarkOliveGreen", new Color(0.333F, 0.42F, 0.184F));
   public static final Color MediumAquamarine = addColor("MediumAquamarine", new Color(0.4F, 0.804F, 0.667F));
   public static final Color DarkSeaGreen = addColor("DarkSeaGreen", new Color(0.561F, 0.737F, 0.561F));
   public static final Color LightSeaGreen = addColor("LightSeaGreen", new Color(0.125F, 0.698F, 0.667F));
   public static final Color DarkCyan = addColor("DarkCyan", new Color(0.0F, 0.545F, 0.545F));
   public static final Color Teal = addColor("Teal", new Color(0.0F, 0.502F, 0.502F));
   public static final Color Aqua = addColor("Aqua", new Color(0.0F, 1.0F, 1.0F));
   public static final Color Cyan = addColor("Cyan", new Color(0.0F, 1.0F, 1.0F));
   public static final Color LightCyan = addColor("LightCyan", new Color(0.878F, 1.0F, 1.0F));
   public static final Color PaleTurquoise = addColor("PaleTurquoise", new Color(0.686F, 0.933F, 0.933F));
   public static final Color Aquamarine = addColor("Aquamarine", new Color(0.498F, 1.0F, 0.831F));
   public static final Color Turquoise = addColor("Turquoise", new Color(0.251F, 0.878F, 0.816F));
   public static final Color MediumTurquoise = addColor("MediumTurquoise", new Color(0.282F, 0.82F, 0.8F));
   public static final Color DarkTurquoise = addColor("DarkTurquoise", new Color(0.0F, 0.808F, 0.82F));
   public static final Color CadetBlue = addColor("CadetBlue", new Color(0.373F, 0.62F, 0.627F));
   public static final Color SteelBlue = addColor("SteelBlue", new Color(0.275F, 0.51F, 0.706F));
   public static final Color LightSteelBlue = addColor("LightSteelBlue", new Color(0.69F, 0.769F, 0.871F));
   public static final Color PowderBlue = addColor("PowderBlue", new Color(0.69F, 0.878F, 0.902F));
   public static final Color LightBlue = addColor("LightBlue", new Color(0.678F, 0.847F, 0.902F));
   public static final Color SkyBlue = addColor("SkyBlue", new Color(0.529F, 0.808F, 0.922F));
   public static final Color LightSkyBlue = addColor("LightSkyBlue", new Color(0.529F, 0.808F, 0.98F));
   public static final Color DeepSkyBlue = addColor("DeepSkyBlue", new Color(0.0F, 0.749F, 1.0F));
   public static final Color DodgerBlue = addColor("DodgerBlue", new Color(0.118F, 0.565F, 1.0F));
   public static final Color CornFlowerBlue = addColor("CornFlowerBlue", new Color(0.392F, 0.584F, 0.929F));
   public static final Color MediumSlateBlue = addColor("MediumSlateBlue", new Color(0.482F, 0.408F, 0.933F));
   public static final Color RoyalBlue = addColor("RoyalBlue", new Color(0.255F, 0.412F, 0.882F));
   public static final Color Blue = addColor("Blue", new Color(0.0F, 0.0F, 1.0F));
   public static final Color MediumBlue = addColor("MediumBlue", new Color(0.0F, 0.0F, 0.804F));
   public static final Color DarkBlue = addColor("DarkBlue", new Color(0.0F, 0.0F, 0.545F));
   public static final Color Navy = addColor("Navy", new Color(0.0F, 0.0F, 0.502F));
   public static final Color MidnightBlue = addColor("MidnightBlue", new Color(0.098F, 0.098F, 0.439F));
   public static final Color CornSilk = addColor("CornSilk", new Color(1.0F, 0.973F, 0.863F));
   public static final Color BlanchedAlmond = addColor("BlanchedAlmond", new Color(1.0F, 0.922F, 0.804F));
   public static final Color Bisque = addColor("Bisque", new Color(1.0F, 0.894F, 0.769F));
   public static final Color NavajoWhite = addColor("NavajoWhite", new Color(1.0F, 0.871F, 0.678F));
   public static final Color Wheat = addColor("Wheat", new Color(0.961F, 0.871F, 0.702F));
   public static final Color BurlyWood = addColor("BurlyWood", new Color(0.871F, 0.722F, 0.529F));
   public static final Color Tan = addColor("Tan", new Color(0.824F, 0.706F, 0.549F));
   public static final Color RosyBrown = addColor("RosyBrown", new Color(0.737F, 0.561F, 0.561F));
   public static final Color SandyBrown = addColor("SandyBrown", new Color(0.957F, 0.643F, 0.376F));
   public static final Color Goldenrod = addColor("Goldenrod", new Color(0.855F, 0.647F, 0.125F));
   public static final Color DarkGoldenrod = addColor("DarkGoldenrod", new Color(0.722F, 0.525F, 0.043F));
   public static final Color Peru = addColor("Peru", new Color(0.804F, 0.522F, 0.247F));
   public static final Color Chocolate = addColor("Chocolate", new Color(0.824F, 0.412F, 0.118F));
   public static final Color SaddleBrown = addColor("SaddleBrown", new Color(0.545F, 0.271F, 0.075F));
   public static final Color Sienna = addColor("Sienna", new Color(0.627F, 0.322F, 0.176F));
   public static final Color Brown = addColor("Brown", new Color(0.647F, 0.165F, 0.165F));
   public static final Color Maroon = addColor("Maroon", new Color(0.502F, 0.0F, 0.0F));

   private static Color addColor(String var0, Color var1) {
      colors.add(var1);
      colorMap.put(var0.toLowerCase(), var1);
      colorNames.add(var0);
      colorSet.add(var0.toLowerCase());
      return var1;
   }

   public static Color GetRandomColor() {
      return (Color)colors.get(Rand.Next(0, colors.size() - 1));
   }

   public static Color GetColorFromIndex(int var0) {
      return (Color)colors.get(var0);
   }

   public static int GetColorsCount() {
      return colors.size();
   }

   public static Color GetColorByName(String var0) {
      return (Color)colorMap.get(var0.toLowerCase());
   }

   public static ArrayList GetColorNames() {
      return colorNames;
   }

   public static boolean ColorExists(String var0) {
      return colorSet.contains(var0.toLowerCase());
   }
}
