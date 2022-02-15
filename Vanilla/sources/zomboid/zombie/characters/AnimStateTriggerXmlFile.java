package zombie.characters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class AnimStateTriggerXmlFile {
   public boolean forceAnim;
   public String animSet;
   public String stateName;
   public String nodeName;
   public boolean setScalarValues;
   public String scalarValue;
   public String scalarValue2;
}
