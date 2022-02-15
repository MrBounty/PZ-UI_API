package com.sixlegs.png;

public abstract class PngConstants {
   public static final long SIGNATURE = -8552249625308161526L;
   public static final int IHDR = 1229472850;
   public static final int PLTE = 1347179589;
   public static final int IDAT = 1229209940;
   public static final int IEND = 1229278788;
   public static final int bKGD = 1649100612;
   public static final int cHRM = 1665684045;
   public static final int gAMA = 1732332865;
   public static final int hIST = 1749635924;
   public static final int iCCP = 1766015824;
   public static final int iTXt = 1767135348;
   public static final int pHYs = 1883789683;
   public static final int sBIT = 1933723988;
   public static final int sPLT = 1934642260;
   public static final int sRGB = 1934772034;
   public static final int tEXt = 1950701684;
   public static final int tIME = 1950960965;
   public static final int tRNS = 1951551059;
   public static final int zTXt = 2052348020;
   public static final int oFFs = 1866876531;
   public static final int pCAL = 1883455820;
   public static final int sCAL = 1933787468;
   public static final int gIFg = 1732855399;
   public static final int gIFx = 1732855416;
   public static final int sTER = 1934902610;
   public static final String BIT_DEPTH = "bit_depth";
   public static final String COLOR_TYPE = "color_type";
   public static final String COMPRESSION = "compression";
   public static final String FILTER = "filter";
   public static final String GAMMA = "gamma";
   public static final String WIDTH = "width";
   public static final String HEIGHT = "height";
   public static final String INTERLACE = "interlace";
   public static final String PALETTE = "palette";
   public static final String PALETTE_ALPHA = "palette_alpha";
   public static final String TRANSPARENCY = "transparency";
   public static final String BACKGROUND = "background_rgb";
   public static final String PIXELS_PER_UNIT_X = "pixels_per_unit_x";
   public static final String PIXELS_PER_UNIT_Y = "pixels_per_unit_y";
   public static final String RENDERING_INTENT = "rendering_intent";
   public static final String SIGNIFICANT_BITS = "significant_bits";
   public static final String TEXT_CHUNKS = "text_chunks";
   public static final String TIME = "time";
   public static final String UNIT = "unit";
   public static final String CHROMATICITY = "chromaticity";
   public static final String ICC_PROFILE = "icc_profile";
   public static final String ICC_PROFILE_NAME = "icc_profile_name";
   public static final String HISTOGRAM = "histogram";
   public static final String SUGGESTED_PALETTES = "suggested_palettes";
   public static final String GIF_DISPOSAL_METHOD = "gif_disposal_method";
   public static final String GIF_USER_INPUT_FLAG = "gif_user_input_flag";
   public static final String GIF_DELAY_TIME = "gif_delay_time";
   public static final String SCALE_UNIT = "scale_unit";
   public static final String PIXEL_WIDTH = "pixel_width";
   public static final String PIXEL_HEIGHT = "pixel_height";
   public static final String POSITION_UNIT = "position_unit";
   public static final String STEREO_MODE = "stereo_mode";
   public static final int COLOR_TYPE_GRAY = 0;
   public static final int COLOR_TYPE_GRAY_ALPHA = 4;
   public static final int COLOR_TYPE_PALETTE = 3;
   public static final int COLOR_TYPE_RGB = 2;
   public static final int COLOR_TYPE_RGB_ALPHA = 6;
   public static final int INTERLACE_NONE = 0;
   public static final int INTERLACE_ADAM7 = 1;
   public static final int FILTER_BASE = 0;
   public static final int COMPRESSION_BASE = 0;
   public static final int UNIT_UNKNOWN = 0;
   public static final int UNIT_METER = 1;
   public static final int SRGB_PERCEPTUAL = 0;
   public static final int SRGB_RELATIVE_COLORIMETRIC = 1;
   public static final int SRGB_SATURATION_PRESERVING = 2;
   public static final int SRGB_ABSOLUTE_COLORIMETRIC = 3;
   public static final String POSITION_X = "position_x";
   public static final String POSITION_Y = "position_y";
   public static final int POSITION_UNIT_PIXEL = 0;
   public static final int POSITION_UNIT_MICROMETER = 1;
   public static final int SCALE_UNIT_METER = 1;
   public static final int SCALE_UNIT_RADIAN = 2;
   public static final int STEREO_MODE_CROSS = 0;
   public static final int STEREO_MODE_DIVERGING = 1;

   public static boolean isAncillary(int var0) {
      return (var0 & 536870912) != 0;
   }

   public static boolean isPrivate(int var0) {
      return (var0 & 2097152) != 0;
   }

   public static boolean isReserved(int var0) {
      return (var0 & 8192) != 0;
   }

   public static boolean isSafeToCopy(int var0) {
      return (var0 & 32) != 0;
   }

   public static String getChunkName(int var0) {
      return (char)(var0 >>> 24 & 255) + (char)(var0 >>> 16 & 255) + (char)(var0 >>> 8 & 255) + (char)(var0 & 255);
   }

   public static int getChunkType(String var0) {
      return (var0.charAt(0) & 255) << 24 | (var0.charAt(1) & 255) << 16 | (var0.charAt(2) & 255) << 8 | var0.charAt(3) & 255;
   }
}
