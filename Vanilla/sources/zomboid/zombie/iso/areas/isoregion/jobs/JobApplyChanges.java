package zombie.iso.areas.isoregion.jobs;

public class JobApplyChanges extends RegionJob {
   protected boolean saveToDisk;

   protected JobApplyChanges() {
      super(RegionJobType.ApplyChanges);
   }

   protected void reset() {
      this.saveToDisk = false;
   }

   public boolean isSaveToDisk() {
      return this.saveToDisk;
   }
}
