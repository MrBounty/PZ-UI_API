package zombie.worldMap;

import java.util.ArrayList;
import org.w3c.dom.Element;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.util.Lambda;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.SharedStrings;

public final class WorldMapXML {
   private final SharedStrings m_sharedStrings = new SharedStrings();
   private final WorldMapPoint m_point = new WorldMapPoint();
   private final WorldMapProperties m_properties = new WorldMapProperties();
   private final ArrayList m_sharedProperties = new ArrayList();

   public boolean read(String var1, WorldMapData var2) throws PZXmlParserException {
      Element var3 = PZXmlUtil.parseXml(var1);
      if (var3.getNodeName().equals("world")) {
         this.parseWorld(var3, var2);
         return true;
      } else {
         return false;
      }
   }

   private void parseWorld(Element var1, WorldMapData var2) {
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var1, var2, (var1x, var2x) -> {
         if (!var1x.getNodeName().equals("cell")) {
            DebugLog.General.warn("Warning: Unrecognised element '" + var1x.getNodeName());
         } else {
            WorldMapCell var3 = this.parseCell(var1x);
            var2x.m_cells.add(var3);
         }
      });
   }

   private WorldMapCell parseCell(Element var1) {
      WorldMapCell var2 = new WorldMapCell();
      var2.m_x = PZMath.tryParseInt(var1.getAttribute("x"), 0);
      var2.m_y = PZMath.tryParseInt(var1.getAttribute("y"), 0);
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var1, var2, (var2x, var3) -> {
         try {
            String var4 = var2x.getNodeName();
            if ("feature".equalsIgnoreCase(var4)) {
               WorldMapFeature var5 = this.parseFeature(var2, var2x);
               var3.m_features.add(var5);
            }
         } catch (Exception var6) {
            DebugLog.General.error("Error while parsing xml element: " + var2x.getNodeName());
            DebugLog.General.error(var6);
         }

      });
      return var2;
   }

   private WorldMapFeature parseFeature(WorldMapCell var1, Element var2) {
      WorldMapFeature var3 = new WorldMapFeature(var1);
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var2, var3, (var1x, var2x) -> {
         try {
            String var3 = var1x.getNodeName();
            if ("geometry".equalsIgnoreCase(var3)) {
               WorldMapGeometry var4 = this.parseGeometry(var1x);
               var2x.m_geometries.add(var4);
            }

            if ("properties".equalsIgnoreCase(var3)) {
               this.parseFeatureProperties(var1x, var2x);
            }
         } catch (Exception var5) {
            DebugLog.General.error("Error while parsing xml element: " + var1x.getNodeName());
            DebugLog.General.error(var5);
         }

      });
      return var3;
   }

   private void parseFeatureProperties(Element var1, WorldMapFeature var2) {
      this.m_properties.clear();
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var1, var2, (var1x, var2x) -> {
         try {
            String var3 = var1x.getNodeName();
            if ("property".equalsIgnoreCase(var3)) {
               String var4 = this.m_sharedStrings.get(var1x.getAttribute("name"));
               String var5 = this.m_sharedStrings.get(var1x.getAttribute("value"));
               this.m_properties.put(var4, var5);
            }
         } catch (Exception var6) {
            DebugLog.General.error("Error while parsing xml element: " + var1x.getNodeName());
            DebugLog.General.error(var6);
         }

      });
      var2.m_properties = this.getOrCreateProperties(this.m_properties);
   }

   private WorldMapProperties getOrCreateProperties(WorldMapProperties var1) {
      for(int var2 = 0; var2 < this.m_sharedProperties.size(); ++var2) {
         if (((WorldMapProperties)this.m_sharedProperties.get(var2)).equals(var1)) {
            return (WorldMapProperties)this.m_sharedProperties.get(var2);
         }
      }

      WorldMapProperties var3 = new WorldMapProperties();
      var3.putAll(var1);
      this.m_sharedProperties.add(var3);
      return var3;
   }

   private WorldMapGeometry parseGeometry(Element var1) {
      WorldMapGeometry var2 = new WorldMapGeometry();
      var2.m_type = WorldMapGeometry.Type.valueOf(var1.getAttribute("type"));
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var1, var2, (var1x, var2x) -> {
         try {
            String var3 = var1x.getNodeName();
            if ("coordinates".equalsIgnoreCase(var3)) {
               WorldMapPoints var4 = new WorldMapPoints();
               this.parseGeometryCoordinates(var1x, var4);
               var2x.m_points.add(var4);
            }
         } catch (Exception var5) {
            DebugLog.General.error("Error while parsing xml element: " + var1x.getNodeName());
            DebugLog.General.error(var5);
         }

      });
      var2.calculateBounds();
      return var2;
   }

   private void parseGeometryCoordinates(Element var1, WorldMapPoints var2) {
      Lambda.forEachFrom(PZXmlUtil::forEachElement, (Object)var1, var2, (var1x, var2x) -> {
         try {
            String var3 = var1x.getNodeName();
            if ("point".equalsIgnoreCase(var3)) {
               WorldMapPoint var4 = this.parsePoint(var1x, this.m_point);
               var2x.add(var4.x);
               var2x.add(var4.y);
            }
         } catch (Exception var5) {
            DebugLog.General.error("Error while parsing xml element: " + var1x.getNodeName());
            DebugLog.General.error(var5);
         }

      });
   }

   private WorldMapPoint parsePoint(Element var1, WorldMapPoint var2) {
      var2.x = PZMath.tryParseInt(var1.getAttribute("x"), 0);
      var2.y = PZMath.tryParseInt(var1.getAttribute("y"), 0);
      return var2;
   }
}
