package zombie.iso.areas.isoregion.jobs;

public abstract class RegionJob {
   private final RegionJobType type;

   protected RegionJob(RegionJobType var1) {
      this.type = var1;
   }

   protected void reset() {
   }

   public RegionJobType getJobType() {
      return this.type;
   }
}
