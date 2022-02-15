package de.jarnbjo.vorbis;

import de.jarnbjo.ogg.BasicStream;
import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.FileStream;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.ogg.OggFormatException;
import de.jarnbjo.ogg.PhysicalOggStream;
import de.jarnbjo.ogg.UncachedUrlStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Collection;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.spi.AudioFileReader;

public class VorbisAudioFileReader extends AudioFileReader {
   public AudioFileFormat getAudioFileFormat(File var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioFileFormat((PhysicalOggStream)(new FileStream(new RandomAccessFile(var1, "r"))));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   public AudioFileFormat getAudioFileFormat(InputStream var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioFileFormat((PhysicalOggStream)(new BasicStream(var1)));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   public AudioFileFormat getAudioFileFormat(URL var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioFileFormat((PhysicalOggStream)(new UncachedUrlStream(var1)));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   private AudioFileFormat getAudioFileFormat(PhysicalOggStream var1) throws IOException, UnsupportedAudioFileException {
      try {
         Collection var2 = var1.getLogicalStreams();
         if (var2.size() != 1) {
            throw new UnsupportedAudioFileException("Only Ogg files with one logical Vorbis stream are supported.");
         } else {
            LogicalOggStream var3 = (LogicalOggStream)var2.iterator().next();
            if (var3.getFormat() != "audio/x-vorbis") {
               throw new UnsupportedAudioFileException("Only Ogg files with one logical Vorbis stream are supported.");
            } else {
               VorbisStream var4 = new VorbisStream(var3);
               AudioFormat var5 = new AudioFormat((float)var4.getIdentificationHeader().getSampleRate(), 16, var4.getIdentificationHeader().getChannels(), true, true);
               return new AudioFileFormat(VorbisAudioFileReader.VorbisFormatType.getInstance(), var5, -1);
            }
         }
      } catch (OggFormatException var6) {
         throw new UnsupportedAudioFileException(var6.getMessage());
      } catch (VorbisFormatException var7) {
         throw new UnsupportedAudioFileException(var7.getMessage());
      }
   }

   public AudioInputStream getAudioInputStream(File var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioInputStream((PhysicalOggStream)(new FileStream(new RandomAccessFile(var1, "r"))));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   public AudioInputStream getAudioInputStream(InputStream var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioInputStream((PhysicalOggStream)(new BasicStream(var1)));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   public AudioInputStream getAudioInputStream(URL var1) throws IOException, UnsupportedAudioFileException {
      try {
         return this.getAudioInputStream((PhysicalOggStream)(new UncachedUrlStream(var1)));
      } catch (OggFormatException var3) {
         throw new UnsupportedAudioFileException(var3.getMessage());
      }
   }

   private AudioInputStream getAudioInputStream(PhysicalOggStream var1) throws IOException, UnsupportedAudioFileException {
      try {
         Collection var2 = var1.getLogicalStreams();
         if (var2.size() != 1) {
            throw new UnsupportedAudioFileException("Only Ogg files with one logical Vorbis stream are supported.");
         } else {
            LogicalOggStream var3 = (LogicalOggStream)var2.iterator().next();
            if (var3.getFormat() != "audio/x-vorbis") {
               throw new UnsupportedAudioFileException("Only Ogg files with one logical Vorbis stream are supported.");
            } else {
               VorbisStream var4 = new VorbisStream(var3);
               AudioFormat var5 = new AudioFormat((float)var4.getIdentificationHeader().getSampleRate(), 16, var4.getIdentificationHeader().getChannels(), true, true);
               return new AudioInputStream(new VorbisAudioFileReader.VorbisInputStream(var4), var5, -1L);
            }
         }
      } catch (OggFormatException var6) {
         throw new UnsupportedAudioFileException(var6.getMessage());
      } catch (VorbisFormatException var7) {
         throw new UnsupportedAudioFileException(var7.getMessage());
      }
   }

   public static class VorbisFormatType extends Type {
      private static final VorbisAudioFileReader.VorbisFormatType instance = new VorbisAudioFileReader.VorbisFormatType();

      private VorbisFormatType() {
         super("VORBIS", "ogg");
      }

      public static Type getInstance() {
         return instance;
      }
   }

   public static class VorbisInputStream extends InputStream {
      private VorbisStream source;
      private byte[] buffer = new byte[8192];

      public VorbisInputStream(VorbisStream var1) {
         this.source = var1;
      }

      public int read() throws IOException {
         return 0;
      }

      public int read(byte[] var1) throws IOException {
         return this.read(var1, 0, var1.length);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         try {
            return this.source.readPcm(var1, var2, var3);
         } catch (EndOfOggStreamException var5) {
            return -1;
         }
      }
   }
}
