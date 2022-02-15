package zombie.iso.areas.isoregion.jobs;

import zombie.core.raknet.UdpConnection;

public class JobServerSendFullData extends RegionJob {
   protected UdpConnection targetConn;

   protected JobServerSendFullData() {
      super(RegionJobType.ServerSendFullData);
   }

   protected void reset() {
      this.targetConn = null;
   }

   public UdpConnection getTargetConn() {
      return this.targetConn;
   }
}
