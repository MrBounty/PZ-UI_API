package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public final class Terrain extends OptionGroup {
   public final Terrain.RenderTiles RenderTiles;

   public Terrain() {
      super("Terrain");
      this.RenderTiles = new Terrain.RenderTiles(this.Group);
   }

   public final class RenderTiles extends OptionGroup {
      public final BooleanDebugOption Enable;
      public final BooleanDebugOption NewRender;
      public final BooleanDebugOption Shadows;
      public final BooleanDebugOption BloodDecals;
      public final BooleanDebugOption Water;
      public final BooleanDebugOption WaterShore;
      public final BooleanDebugOption WaterBody;
      public final BooleanDebugOption Lua;
      public final BooleanDebugOption VegetationCorpses;
      public final BooleanDebugOption MinusFloorCharacters;
      public final BooleanDebugOption RenderGridSquares;
      public final BooleanDebugOption RenderSprites;
      public final BooleanDebugOption OverlaySprites;
      public final BooleanDebugOption AttachedAnimSprites;
      public final BooleanDebugOption AttachedChildren;
      public final BooleanDebugOption AttachedWallBloodSplats;
      public final BooleanDebugOption UseShaders;
      public final BooleanDebugOption HighContrastBg;
      public final Terrain.RenderTiles.IsoGridSquare IsoGridSquare;

      public RenderTiles(IDebugOptionGroup var2) {
         super(var2, "RenderTiles");
         this.Enable = newDebugOnlyOption(this.Group, "Enable", true);
         this.NewRender = newDebugOnlyOption(this.Group, "NewRender", true);
         this.Shadows = newDebugOnlyOption(this.Group, "Shadows", true);
         this.BloodDecals = newDebugOnlyOption(this.Group, "BloodDecals", true);
         this.Water = newDebugOnlyOption(this.Group, "Water", true);
         this.WaterShore = newDebugOnlyOption(this.Group, "WaterShore", true);
         this.WaterBody = newDebugOnlyOption(this.Group, "WaterBody", true);
         this.Lua = newDebugOnlyOption(this.Group, "Lua", true);
         this.VegetationCorpses = newDebugOnlyOption(this.Group, "VegetationCorpses", true);
         this.MinusFloorCharacters = newDebugOnlyOption(this.Group, "MinusFloorCharacters", true);
         this.RenderGridSquares = newDebugOnlyOption(this.Group, "RenderGridSquares", true);
         this.RenderSprites = newDebugOnlyOption(this.Group, "RenderSprites", true);
         this.OverlaySprites = newDebugOnlyOption(this.Group, "OverlaySprites", true);
         this.AttachedAnimSprites = newDebugOnlyOption(this.Group, "AttachedAnimSprites", true);
         this.AttachedChildren = newDebugOnlyOption(this.Group, "AttachedChildren", true);
         this.AttachedWallBloodSplats = newDebugOnlyOption(this.Group, "AttachedWallBloodSplats", true);
         this.UseShaders = newOption(this.Group, "UseShaders", true);
         this.HighContrastBg = newDebugOnlyOption(this.Group, "HighContrastBg", false);
         this.IsoGridSquare = new Terrain.RenderTiles.IsoGridSquare(this.Group);
      }

      public final class IsoGridSquare extends OptionGroup {
         public final BooleanDebugOption RenderMinusFloor;
         public final BooleanDebugOption DoorsAndWalls;
         public final BooleanDebugOption DoorsAndWalls_SimpleLighting;
         public final BooleanDebugOption Objects;
         public final BooleanDebugOption MeshCutdown;
         public final BooleanDebugOption IsoPadding;
         public final BooleanDebugOption IsoPaddingDeDiamond;
         public final BooleanDebugOption IsoPaddingAttached;
         public final BooleanDebugOption ShoreFade;
         public final Terrain.RenderTiles.IsoGridSquare.Walls Walls;
         public final Terrain.RenderTiles.IsoGridSquare.Floor Floor;

         public IsoGridSquare(IDebugOptionGroup var2) {
            super(var2, "IsoGridSquare");
            this.RenderMinusFloor = newDebugOnlyOption(this.Group, "RenderMinusFloor", true);
            this.DoorsAndWalls = newDebugOnlyOption(this.Group, "DoorsAndWalls", true);
            this.DoorsAndWalls_SimpleLighting = newDebugOnlyOption(this.Group, "DoorsAndWallsSL", true);
            this.Objects = newDebugOnlyOption(this.Group, "Objects", true);
            this.MeshCutdown = newDebugOnlyOption(this.Group, "MeshCutDown", true);
            this.IsoPadding = newDebugOnlyOption(this.Group, "IsoPadding", true);
            this.IsoPaddingDeDiamond = newDebugOnlyOption(this.Group, "IsoPaddingDeDiamond", true);
            this.IsoPaddingAttached = newDebugOnlyOption(this.Group, "IsoPaddingAttached", true);
            this.ShoreFade = newDebugOnlyOption(this.Group, "ShoreFade", true);
            this.Walls = new Terrain.RenderTiles.IsoGridSquare.Walls(this.Group);
            this.Floor = new Terrain.RenderTiles.IsoGridSquare.Floor(this.Group);
         }

         public final class Walls extends OptionGroup {
            public final BooleanDebugOption NW;
            public final BooleanDebugOption W;
            public final BooleanDebugOption N;
            public final BooleanDebugOption Render;
            public final BooleanDebugOption Lighting;
            public final BooleanDebugOption LightingDebug;
            public final BooleanDebugOption LightingOldDebug;
            public final BooleanDebugOption AttachedSprites;

            public Walls(IDebugOptionGroup var2) {
               super(var2, "Walls");
               this.NW = newDebugOnlyOption(this.Group, "NW", true);
               this.W = newDebugOnlyOption(this.Group, "W", true);
               this.N = newDebugOnlyOption(this.Group, "N", true);
               this.Render = newDebugOnlyOption(this.Group, "Render", true);
               this.Lighting = newDebugOnlyOption(this.Group, "Lighting", true);
               this.LightingDebug = newDebugOnlyOption(this.Group, "LightingDebug", false);
               this.LightingOldDebug = newDebugOnlyOption(this.Group, "LightingOldDebug", false);
               this.AttachedSprites = newDebugOnlyOption(this.Group, "AttachedSprites", true);
            }
         }

         public final class Floor extends OptionGroup {
            public final BooleanDebugOption Lighting;
            public final BooleanDebugOption LightingOld;
            public final BooleanDebugOption LightingDebug;

            public Floor(IDebugOptionGroup var2) {
               super(var2, "Floor");
               this.Lighting = newDebugOnlyOption(this.Group, "Lighting", true);
               this.LightingOld = newDebugOnlyOption(this.Group, "LightingOld", false);
               this.LightingDebug = newDebugOnlyOption(this.Group, "LightingDebug", false);
            }
         }
      }
   }
}
