package zombie.core.bucket;

public final class BucketManager {
   static final Bucket SharedBucket = new Bucket();

   public static Bucket Active() {
      return SharedBucket;
   }

   public static Bucket Shared() {
      return SharedBucket;
   }
}
