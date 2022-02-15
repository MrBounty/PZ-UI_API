package zombie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public final class FileGuidTable {
   public final ArrayList files = new ArrayList();
   @XmlTransient
   private final Map guidToPath;
   @XmlTransient
   private final Map pathToGuid;

   public FileGuidTable() {
      this.guidToPath = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.pathToGuid = new TreeMap(String.CASE_INSENSITIVE_ORDER);
   }

   public void setModID(String var1) {
      FileGuidPair var3;
      for(Iterator var2 = this.files.iterator(); var2.hasNext(); var3.guid = var1 + "-" + var3.guid) {
         var3 = (FileGuidPair)var2.next();
      }

   }

   public void mergeFrom(FileGuidTable var1) {
      this.files.addAll(var1.files);
   }

   public void loaded() {
      Iterator var1 = this.files.iterator();

      while(var1.hasNext()) {
         FileGuidPair var2 = (FileGuidPair)var1.next();
         this.guidToPath.put(var2.guid, var2.path);
         this.pathToGuid.put(var2.path, var2.guid);
      }

   }

   public void clear() {
      this.files.clear();
      this.guidToPath.clear();
      this.pathToGuid.clear();
   }

   public String getFilePathFromGuid(String var1) {
      return (String)this.guidToPath.get(var1);
   }

   public String getGuidFromFilePath(String var1) {
      return (String)this.pathToGuid.get(var1);
   }
}
