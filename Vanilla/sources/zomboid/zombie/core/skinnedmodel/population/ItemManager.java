package zombie.core.skinnedmodel.population;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import zombie.ZomboidFileSystem;
import zombie.core.Rand;

@XmlRootElement
public class ItemManager {
   public ArrayList m_Items = new ArrayList();
   @XmlTransient
   public static ItemManager instance;

   public static void init() {
      File var0 = ZomboidFileSystem.instance.getMediaFile("items" + File.separator + "items.xml");
      instance = Parse(var0.getPath());
   }

   public CarriedItem GetRandomItem() {
      int var1 = Rand.Next(this.m_Items.size() + 1);
      return var1 < this.m_Items.size() ? (CarriedItem)this.m_Items.get(var1) : null;
   }

   public static ItemManager Parse(String var0) {
      try {
         return parse(var0);
      } catch (JAXBException var2) {
         var2.printStackTrace();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return null;
   }

   public static ItemManager parse(String var0) throws JAXBException, IOException {
      FileInputStream var1 = new FileInputStream(var0);

      ItemManager var5;
      try {
         JAXBContext var2 = JAXBContext.newInstance(new Class[]{ItemManager.class});
         Unmarshaller var3 = var2.createUnmarshaller();
         ItemManager var4 = (ItemManager)var3.unmarshal(var1);
         var5 = var4;
      } catch (Throwable var7) {
         try {
            var1.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      var1.close();
      return var5;
   }

   public static void Write(ItemManager var0, String var1) {
      try {
         write(var0, var1);
      } catch (JAXBException var3) {
         var3.printStackTrace();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public static void write(ItemManager var0, String var1) throws IOException, JAXBException {
      FileOutputStream var2 = new FileOutputStream(var1);

      try {
         JAXBContext var3 = JAXBContext.newInstance(new Class[]{ItemManager.class});
         Marshaller var4 = var3.createMarshaller();
         var4.setProperty("jaxb.formatted.output", Boolean.TRUE);
         var4.marshal(var0, var2);
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
   }
}
