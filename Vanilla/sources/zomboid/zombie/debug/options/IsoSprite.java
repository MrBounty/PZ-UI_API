package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public final class IsoSprite extends OptionGroup {
   public final BooleanDebugOption RenderSprites;
   public final BooleanDebugOption RenderModels;
   public final BooleanDebugOption MovingObjectEdges;
   public final BooleanDebugOption DropShadowEdges;
   public final BooleanDebugOption NearestMagFilterAtMinZoom;
   public final BooleanDebugOption TextureWrapClampToEdge;
   public final BooleanDebugOption TextureWrapRepeat;
   public final BooleanDebugOption ForceLinearMagFilter;
   public final BooleanDebugOption ForceNearestMagFilter;
   public final BooleanDebugOption ForceNearestMipMapping;
   public final BooleanDebugOption CharacterMipmapColors;
   public final BooleanDebugOption WorldMipmapColors;

   public IsoSprite() {
      super("IsoSprite");
      this.RenderSprites = newDebugOnlyOption(this.Group, "Render.Sprites", true);
      this.RenderModels = newDebugOnlyOption(this.Group, "Render.Models", true);
      this.MovingObjectEdges = newDebugOnlyOption(this.Group, "Render.MovingObjectEdges", false);
      this.DropShadowEdges = newDebugOnlyOption(this.Group, "Render.DropShadowEdges", false);
      this.NearestMagFilterAtMinZoom = newDebugOnlyOption(this.Group, "Render.NearestMagFilterAtMinZoom", true);
      this.TextureWrapClampToEdge = newDebugOnlyOption(this.Group, "Render.TextureWrap.ClampToEdge", false);
      this.TextureWrapRepeat = newDebugOnlyOption(this.Group, "Render.TextureWrap.Repeat", false);
      this.ForceLinearMagFilter = newDebugOnlyOption(this.Group, "Render.ForceLinearMagFilter", false);
      this.ForceNearestMagFilter = newDebugOnlyOption(this.Group, "Render.ForceNearestMagFilter", false);
      this.ForceNearestMipMapping = newDebugOnlyOption(this.Group, "Render.ForceNearestMipMapping", false);
      this.CharacterMipmapColors = newDebugOnlyOption(this.Group, "Render.CharacterMipmapColors", false);
      this.WorldMipmapColors = newDebugOnlyOption(this.Group, "Render.WorldMipmapColors", false);
   }
}
