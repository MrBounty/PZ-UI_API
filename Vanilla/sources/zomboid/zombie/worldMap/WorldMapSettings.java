package zombie.worldMap;

import java.util.ArrayList;
import zombie.ZomboidFileSystem;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.config.DoubleConfigOption;

public final class WorldMapSettings {
   public static int VERSION1 = 1;
   public static int VERSION;
   private static WorldMapSettings instance;
   final ArrayList m_options = new ArrayList();
   final WorldMapSettings.WorldMap mWorldMap = new WorldMapSettings.WorldMap();
   final WorldMapSettings.MiniMap mMiniMap = new WorldMapSettings.MiniMap();
   private int m_readVersion = 0;

   public static WorldMapSettings getInstance() {
      if (instance == null) {
         instance = new WorldMapSettings();
         instance.load();
      }

      return instance;
   }

   private BooleanConfigOption newOption(String var1, boolean var2) {
      BooleanConfigOption var3 = new BooleanConfigOption(var1, var2);
      this.m_options.add(var3);
      return var3;
   }

   private DoubleConfigOption newOption(String var1, double var2, double var4, double var6) {
      DoubleConfigOption var8 = new DoubleConfigOption(var1, var2, var4, var6);
      this.m_options.add(var8);
      return var8;
   }

   public ConfigOption getOptionByName(String var1) {
      for(int var2 = 0; var2 < this.m_options.size(); ++var2) {
         ConfigOption var3 = (ConfigOption)this.m_options.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getOptionCount() {
      return this.m_options.size();
   }

   public ConfigOption getOptionByIndex(int var1) {
      return (ConfigOption)this.m_options.get(var1);
   }

   public void setBoolean(String var1, boolean var2) {
      ConfigOption var3 = this.getOptionByName(var1);
      if (var3 instanceof BooleanConfigOption) {
         ((BooleanConfigOption)var3).setValue(var2);
      }

   }

   public boolean getBoolean(String var1) {
      ConfigOption var2 = this.getOptionByName(var1);
      return var2 instanceof BooleanConfigOption ? ((BooleanConfigOption)var2).getValue() : false;
   }

   public void setDouble(String var1, double var2) {
      ConfigOption var4 = this.getOptionByName(var1);
      if (var4 instanceof DoubleConfigOption) {
         ((DoubleConfigOption)var4).setValue(var2);
      }

   }

   public double getDouble(String var1, double var2) {
      ConfigOption var4 = this.getOptionByName(var1);
      return var4 instanceof DoubleConfigOption ? ((DoubleConfigOption)var4).getValue() : var2;
   }

   public int getFileVersion() {
      return this.m_readVersion;
   }

   public void save() {
      String var1 = ZomboidFileSystem.instance.getFileNameInCurrentSave("InGameMap.ini");
      ConfigFile var2 = new ConfigFile();
      var2.write(var1, VERSION, this.m_options);
      this.m_readVersion = VERSION;
   }

   public void load() {
      this.m_readVersion = 0;
      String var1 = ZomboidFileSystem.instance.getFileNameInCurrentSave("InGameMap.ini");
      ConfigFile var2 = new ConfigFile();
      if (var2.read(var1)) {
         this.m_readVersion = var2.getVersion();
         if (this.m_readVersion >= VERSION1 && this.m_readVersion <= VERSION) {
            for(int var3 = 0; var3 < var2.getOptions().size(); ++var3) {
               ConfigOption var4 = (ConfigOption)var2.getOptions().get(var3);

               try {
                  ConfigOption var5 = this.getOptionByName(var4.getName());
                  if (var5 != null) {
                     var5.parse(var4.getValueAsString());
                  }
               } catch (Exception var6) {
               }
            }

         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.m_options.clear();
         instance = null;
      }
   }

   static {
      VERSION = VERSION1;
   }

   public final class WorldMap {
      public DoubleConfigOption CenterX = WorldMapSettings.this.newOption("WorldMap.CenterX", -1.7976931348623157E308D, Double.MAX_VALUE, 0.0D);
      public DoubleConfigOption CenterY = WorldMapSettings.this.newOption("WorldMap.CenterY", -1.7976931348623157E308D, Double.MAX_VALUE, 0.0D);
      public DoubleConfigOption Zoom = WorldMapSettings.this.newOption("WorldMap.Zoom", 0.0D, 24.0D, 0.0D);
      public BooleanConfigOption Isometric = WorldMapSettings.this.newOption("WorldMap.Isometric", true);
      public BooleanConfigOption ShowSymbolsUI = WorldMapSettings.this.newOption("WorldMap.ShowSymbolsUI", true);
   }

   public class MiniMap {
      public DoubleConfigOption Zoom = WorldMapSettings.this.newOption("MiniMap.Zoom", 0.0D, 24.0D, 19.0D);
      public BooleanConfigOption Isometric = WorldMapSettings.this.newOption("MiniMap.Isometric", true);
      public BooleanConfigOption ShowSymbols = WorldMapSettings.this.newOption("MiniMap.ShowSymbols", false);
      public BooleanConfigOption StartVisible = WorldMapSettings.this.newOption("MiniMap.StartVisible", true);
   }
}
