package zombie.scripting.objects;

import java.util.ArrayList;
import org.joml.Vector3f;
import zombie.util.StringUtils;

public final class ModelAttachment {
   private String id;
   private final Vector3f offset = new Vector3f();
   private final Vector3f rotate = new Vector3f();
   private String bone;
   private ArrayList canAttach;
   private float zoffset;
   private boolean updateConstraint = true;

   public ModelAttachment(String var1) {
      this.setId(var1);
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         throw new IllegalArgumentException("ModelAttachment id is null or empty");
      } else {
         this.id = var1;
      }
   }

   public Vector3f getOffset() {
      return this.offset;
   }

   public Vector3f getRotate() {
      return this.rotate;
   }

   public String getBone() {
      return this.bone;
   }

   public void setBone(String var1) {
      var1 = var1.trim();
      this.bone = var1.isEmpty() ? null : var1;
   }

   public ArrayList getCanAttach() {
      return this.canAttach;
   }

   public void setCanAttach(ArrayList var1) {
      this.canAttach = var1;
   }

   public float getZOffset() {
      return this.zoffset;
   }

   public void setZOffset(float var1) {
      this.zoffset = var1;
   }

   public boolean isUpdateConstraint() {
      return this.updateConstraint;
   }

   public void setUpdateConstraint(boolean var1) {
      this.updateConstraint = var1;
   }
}
