package zombie.radio;

public enum ChannelCategory {
   Undefined,
   Radio,
   Television,
   Military,
   Amateur,
   Bandit,
   Emergency,
   Other;

   // $FF: synthetic method
   private static ChannelCategory[] $values() {
      return new ChannelCategory[]{Undefined, Radio, Television, Military, Amateur, Bandit, Emergency, Other};
   }
}
