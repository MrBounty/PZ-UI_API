package org.lwjglx.util.glu.tessellation;

class GLUface {
   public GLUface next;
   public GLUface prev;
   public GLUhalfEdge anEdge;
   public Object data;
   public GLUface trail;
   public boolean marked;
   public boolean inside;
}
