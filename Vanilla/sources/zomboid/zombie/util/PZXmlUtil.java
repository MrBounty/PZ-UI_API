package zombie.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;

public final class PZXmlUtil {
   private static boolean s_debugLogging = false;
   private static final ThreadLocal documentBuilders = ThreadLocal.withInitial(() -> {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         return var0.newDocumentBuilder();
      } catch (ParserConfigurationException var1) {
         ExceptionLogger.logException(var1);
         throw new RuntimeException(var1);
      }
   });

   public static Element parseXml(String var0) throws PZXmlParserException {
      String var1 = ZomboidFileSystem.instance.resolveFileOrGUID(var0);

      Element var2;
      try {
         var2 = parseXmlInternal(var1);
      } catch (IOException | SAXException var6) {
         throw new PZXmlParserException("Exception thrown while parsing XML file \"" + var1 + "\"", var6);
      }

      var2 = includeAnotherFile(var2, var1);
      String var3 = var2.getAttribute("x_extends");
      if (var3 != null && var3.trim().length() != 0) {
         if (!ZomboidFileSystem.instance.isValidFilePathGuid(var3)) {
            var3 = ZomboidFileSystem.instance.resolveRelativePath(var1, var3);
         }

         Element var4 = parseXml(var3);
         Element var5 = resolve(var2, var4);
         return var5;
      } else {
         return var2;
      }
   }

   private static Element includeAnotherFile(Element var0, String var1) throws PZXmlParserException {
      String var2 = var0.getAttribute("x_include");
      if (var2 != null && var2.trim().length() != 0) {
         if (!ZomboidFileSystem.instance.isValidFilePathGuid(var2)) {
            var2 = ZomboidFileSystem.instance.resolveRelativePath(var1, var2);
         }

         Element var3 = parseXml(var2);
         if (!var3.getTagName().equals(var0.getTagName())) {
            return var0;
         } else {
            Document var4 = createNewDocument();
            Node var5 = var4.importNode(var0, true);
            Node var6 = var5.getFirstChild();

            for(Node var7 = var3.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
               if (var7 instanceof Element) {
                  Element var8 = (Element)var4.importNode(var7, true);
                  var5.insertBefore(var8, var6);
               }
            }

            var5.normalize();
            return (Element)var5;
         }
      } else {
         return var0;
      }
   }

   private static Element resolve(Element var0, Element var1) {
      Document var2 = createNewDocument();
      Element var3 = resolve(var0, var1, var2);
      var2.appendChild(var3);
      if (s_debugLogging) {
         PrintStream var10000 = System.out;
         String var10001 = elementToPrettyStringSafe(var1);
         var10000.println("PZXmlUtil.resolve> \r\n<Parent>\r\n" + var10001 + "\r\n</Parent>\r\n<Child>\r\n" + elementToPrettyStringSafe(var0) + "\r\n</Child>\r\n<Resolved>\r\n" + elementToPrettyStringSafe(var3) + "\r\n</Resolved>");
      }

      return var3;
   }

   private static Element resolve(Element var0, Element var1, Document var2) {
      Element var3;
      if (isTextOnly(var0)) {
         var3 = (Element)var2.importNode(var0, true);
         return var3;
      } else {
         var3 = var2.createElement(var0.getTagName());
         ArrayList var4 = new ArrayList();
         NamedNodeMap var5 = var1.getAttributes();

         Node var7;
         Attr var8;
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            var7 = var5.item(var6);
            if (!(var7 instanceof Attr)) {
               if (s_debugLogging) {
                  System.out.println("PZXmlUtil.resolve> Skipping parent.attrib: " + var7);
               }
            } else {
               var8 = (Attr)var2.importNode(var7, true);
               var4.add(var8);
            }
         }

         NamedNodeMap var19 = var0.getAttributes();

         int var12;
         for(int var21 = 0; var21 < var19.getLength(); ++var21) {
            Node var24 = var19.item(var21);
            if (!(var24 instanceof Attr)) {
               if (s_debugLogging) {
                  System.out.println("PZXmlUtil.resolve> Skipping attrib: " + var24);
               }
            } else {
               Attr var9 = (Attr)var2.importNode(var24, true);
               String var10 = var9.getName();
               boolean var11 = true;

               for(var12 = 0; var12 < var4.size(); ++var12) {
                  Attr var13 = (Attr)var4.get(var12);
                  String var14 = var13.getName();
                  if (var14.equals(var10)) {
                     var4.set(var12, var9);
                     var11 = false;
                     break;
                  }
               }

               if (var11) {
                  var4.add(var9);
               }
            }
         }

         Iterator var23 = var4.iterator();

         while(var23.hasNext()) {
            var8 = (Attr)var23.next();
            var3.setAttributeNode(var8);
         }

         var4 = new ArrayList();
         HashMap var18 = new HashMap();

         for(Node var20 = var1.getFirstChild(); var20 != null; var20 = var20.getNextSibling()) {
            if (!(var20 instanceof Element)) {
               if (s_debugLogging) {
                  System.out.println("PZXmlUtil.resolve> Skipping parent.node: " + var20);
               }
            } else {
               Element var25 = (Element)var2.importNode(var20, true);
               String var26 = var25.getTagName();
               var18.put(var26, 1 + (Integer)var18.getOrDefault(var26, 0));
               var4.add(var25);
            }
         }

         HashMap var22 = new HashMap();

         Element var27;
         for(var7 = var0.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
            if (!(var7 instanceof Element)) {
               if (s_debugLogging) {
                  System.out.println("PZXmlUtil.resolve> Skipping node: " + var7);
               }
            } else {
               var27 = (Element)var2.importNode(var7, true);
               String var28 = var27.getTagName();
               int var29 = (Integer)var22.getOrDefault(var28, 0);
               int var30 = 1 + var29;
               var22.put(var28, var30);
               var12 = (Integer)var18.getOrDefault(var28, 0);
               if (var12 < var30) {
                  var4.add(var27);
               } else {
                  int var31 = 0;

                  for(int var32 = 0; var31 < var4.size(); ++var31) {
                     Element var15 = (Element)var4.get(var31);
                     String var16 = var15.getTagName();
                     if (var16.equals(var28)) {
                        if (var32 == var29) {
                           Element var17 = resolve(var27, var15, var2);
                           var4.set(var31, var17);
                           break;
                        }

                        ++var32;
                     }
                  }
               }
            }
         }

         var23 = var4.iterator();

         while(var23.hasNext()) {
            var27 = (Element)var23.next();
            var3.appendChild(var27);
         }

         return var3;
      }
   }

   private static boolean isTextOnly(Element var0) {
      boolean var1 = false;

      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         boolean var3 = false;
         if (var2 instanceof Text) {
            String var4 = var2.getTextContent();
            boolean var5 = StringUtils.isNullOrWhitespace(var4);
            var3 = !var5;
         }

         if (!var3) {
            var1 = false;
            break;
         }

         var1 = true;
      }

      return var1;
   }

   private static String elementToPrettyStringSafe(Element var0) {
      try {
         return elementToPrettyString(var0);
      } catch (TransformerException var2) {
         return null;
      }
   }

   private static String elementToPrettyString(Element var0) throws TransformerException {
      Transformer var1 = TransformerFactory.newInstance().newTransformer();
      var1.setOutputProperty("indent", "yes");
      var1.setOutputProperty("omit-xml-declaration", "yes");
      var1.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      StreamResult var2 = new StreamResult(new StringWriter());
      DOMSource var3 = new DOMSource(var0);
      var1.transform(var3, var2);
      String var4 = var2.getWriter().toString();
      return var4;
   }

   public static Document createNewDocument() {
      DocumentBuilder var0 = (DocumentBuilder)documentBuilders.get();
      Document var1 = var0.newDocument();
      return var1;
   }

   private static Element parseXmlInternal(String var0) throws SAXException, IOException {
      try {
         FileInputStream var1 = new FileInputStream(var0);

         Element var6;
         try {
            BufferedInputStream var2 = new BufferedInputStream(var1);

            try {
               DocumentBuilder var3 = (DocumentBuilder)documentBuilders.get();
               Document var4 = var3.parse(var2);
               var2.close();
               Element var5 = var4.getDocumentElement();
               var5.normalize();
               var6 = var5;
            } catch (Throwable var9) {
               try {
                  var2.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var2.close();
         } catch (Throwable var10) {
            try {
               var1.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var1.close();
         return var6;
      } catch (SAXException var11) {
         System.err.println("Exception parsing filename: " + var0);
         throw var11;
      }
   }

   public static void forEachElement(Element var0, Consumer var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof Element) {
            var1.accept((Element)var2);
         }
      }

   }

   public static Object parse(Class var0, String var1) throws PZXmlParserException {
      Element var2 = parseXml(var1);

      try {
         Unmarshaller var3 = PZXmlUtil.UnmarshallerAllocator.get(var0);
         Object var4 = var3.unmarshal(var2);
         return var4;
      } catch (JAXBException var5) {
         throw new PZXmlParserException("Exception thrown loading source: \"" + var1 + "\". Loading for type \"" + var0 + "\"", var5);
      }
   }

   public static void write(Object var0, File var1) throws TransformerException, IOException, JAXBException {
      Document var2 = createNewDocument();
      Marshaller var3 = PZXmlUtil.MarshallerAllocator.get(var0);
      var3.marshal(var0, var2);
      write(var2, var1);
   }

   public static void write(Document var0, File var1) throws TransformerException, IOException {
      Element var2 = var0.getDocumentElement();
      String var3 = elementToPrettyString(var2);
      FileOutputStream var4 = new FileOutputStream(var1, false);
      PrintWriter var5 = new PrintWriter(var4);
      var5.write(var3);
      var5.flush();
      var4.flush();
      var4.close();
   }

   public static boolean tryWrite(Object var0, File var1) {
      try {
         write(var0, var1);
         return true;
      } catch (IOException | JAXBException | TransformerException var3) {
         ExceptionLogger.logException(var3, "Exception thrown writing data: \"" + var0 + "\". Out file: \"" + var1 + "\"");
         return false;
      }
   }

   public static boolean tryWrite(Document var0, File var1) {
      try {
         write(var0, var1);
         return true;
      } catch (IOException | TransformerException var3) {
         ExceptionLogger.logException(var3, "Exception thrown writing document: \"" + var0 + "\". Out file: \"" + var1 + "\"");
         return false;
      }
   }

   private static final class UnmarshallerAllocator {
      private static final ThreadLocal instance = ThreadLocal.withInitial(PZXmlUtil.UnmarshallerAllocator::new);
      private final Map m_map = new HashMap();

      public static Unmarshaller get(Class var0) throws JAXBException {
         return ((PZXmlUtil.UnmarshallerAllocator)instance.get()).getOrCreate(var0);
      }

      private Unmarshaller getOrCreate(Class var1) throws JAXBException {
         Unmarshaller var2 = (Unmarshaller)this.m_map.get(var1);
         if (var2 == null) {
            JAXBContext var3 = JAXBContext.newInstance(new Class[]{var1});
            var2 = var3.createUnmarshaller();
            var2.setListener(new Listener() {
               public void beforeUnmarshal(Object var1, Object var2) {
                  super.beforeUnmarshal(var1, var2);
               }
            });
            this.m_map.put(var1, var2);
         }

         return var2;
      }
   }

   private static final class MarshallerAllocator {
      private static final ThreadLocal instance = ThreadLocal.withInitial(PZXmlUtil.MarshallerAllocator::new);
      private final Map m_map = new HashMap();

      public static Marshaller get(Object var0) throws JAXBException {
         return get(var0.getClass());
      }

      public static Marshaller get(Class var0) throws JAXBException {
         return ((PZXmlUtil.MarshallerAllocator)instance.get()).getOrCreate(var0);
      }

      private Marshaller getOrCreate(Class var1) throws JAXBException {
         Marshaller var2 = (Marshaller)this.m_map.get(var1);
         if (var2 == null) {
            JAXBContext var3 = JAXBContext.newInstance(new Class[]{var1});
            var2 = var3.createMarshaller();
            var2.setListener(new javax.xml.bind.Marshaller.Listener() {
               public void beforeMarshal(Object var1) {
                  super.beforeMarshal(var1);
               }
            });
            this.m_map.put(var1, var2);
         }

         return var2;
      }
   }
}
