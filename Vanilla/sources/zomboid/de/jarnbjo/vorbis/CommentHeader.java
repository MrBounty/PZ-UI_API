package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CommentHeader {
   public static final String TITLE = "TITLE";
   public static final String ARTIST = "ARTIST";
   public static final String ALBUM = "ALBUM";
   public static final String TRACKNUMBER = "TRACKNUMBER";
   public static final String VERSION = "VERSION";
   public static final String PERFORMER = "PERFORMER";
   public static final String COPYRIGHT = "COPYRIGHT";
   public static final String LICENSE = "LICENSE";
   public static final String ORGANIZATION = "ORGANIZATION";
   public static final String DESCRIPTION = "DESCRIPTION";
   public static final String GENRE = "GENRE";
   public static final String DATE = "DATE";
   public static final String LOCATION = "LOCATION";
   public static final String CONTACT = "CONTACT";
   public static final String ISRC = "ISRC";
   private String vendor;
   private HashMap comments = new HashMap();
   private boolean framingBit;
   private static final long HEADER = 126896460427126L;

   public CommentHeader(BitInputStream var1) throws VorbisFormatException, IOException {
      if (var1.getLong(48) != 126896460427126L) {
         throw new VorbisFormatException("The identification header has an illegal leading.");
      } else {
         this.vendor = this.getString(var1);
         int var2 = var1.getInt(32);

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = this.getString(var1);
            int var5 = var4.indexOf(61);
            String var6 = var4.substring(0, var5);
            String var7 = var4.substring(var5 + 1);
            this.addComment(var6, var7);
         }

         this.framingBit = var1.getInt(8) != 0;
      }
   }

   private void addComment(String var1, String var2) {
      ArrayList var3 = (ArrayList)this.comments.get(var1);
      if (var3 == null) {
         var3 = new ArrayList();
         this.comments.put(var1, var3);
      }

      var3.add(var2);
   }

   public String getVendor() {
      return this.vendor;
   }

   public String getComment(String var1) {
      ArrayList var2 = (ArrayList)this.comments.get(var1);
      return var2 == null ? (String)null : (String)var2.get(0);
   }

   public String[] getComments(String var1) {
      ArrayList var2 = (ArrayList)this.comments.get(var1);
      return var2 == null ? new String[0] : (String[])var2.toArray(new String[var2.size()]);
   }

   public String getTitle() {
      return this.getComment("TITLE");
   }

   public String[] getTitles() {
      return this.getComments("TITLE");
   }

   public String getVersion() {
      return this.getComment("VERSION");
   }

   public String[] getVersions() {
      return this.getComments("VERSION");
   }

   public String getAlbum() {
      return this.getComment("ALBUM");
   }

   public String[] getAlbums() {
      return this.getComments("ALBUM");
   }

   public String getTrackNumber() {
      return this.getComment("TRACKNUMBER");
   }

   public String[] getTrackNumbers() {
      return this.getComments("TRACKNUMBER");
   }

   public String getArtist() {
      return this.getComment("ARTIST");
   }

   public String[] getArtists() {
      return this.getComments("ARTIST");
   }

   public String getPerformer() {
      return this.getComment("PERFORMER");
   }

   public String[] getPerformers() {
      return this.getComments("PERFORMER");
   }

   public String getCopyright() {
      return this.getComment("COPYRIGHT");
   }

   public String[] getCopyrights() {
      return this.getComments("COPYRIGHT");
   }

   public String getLicense() {
      return this.getComment("LICENSE");
   }

   public String[] getLicenses() {
      return this.getComments("LICENSE");
   }

   public String getOrganization() {
      return this.getComment("ORGANIZATION");
   }

   public String[] getOrganizations() {
      return this.getComments("ORGANIZATION");
   }

   public String getDescription() {
      return this.getComment("DESCRIPTION");
   }

   public String[] getDescriptions() {
      return this.getComments("DESCRIPTION");
   }

   public String getGenre() {
      return this.getComment("GENRE");
   }

   public String[] getGenres() {
      return this.getComments("GENRE");
   }

   public String getDate() {
      return this.getComment("DATE");
   }

   public String[] getDates() {
      return this.getComments("DATE");
   }

   public String getLocation() {
      return this.getComment("LOCATION");
   }

   public String[] getLocations() {
      return this.getComments("LOCATION");
   }

   public String getContact() {
      return this.getComment("CONTACT");
   }

   public String[] getContacts() {
      return this.getComments("CONTACT");
   }

   public String getIsrc() {
      return this.getComment("ISRC");
   }

   public String[] getIsrcs() {
      return this.getComments("ISRC");
   }

   private String getString(BitInputStream var1) throws IOException, VorbisFormatException {
      int var2 = var1.getInt(32);
      byte[] var3 = new byte[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = (byte)var1.getInt(8);
      }

      return new String(var3, "UTF-8");
   }
}
