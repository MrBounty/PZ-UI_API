package zombie.core.znet;

public class ZNetStatistics {
   public long lastUserMessageBytesPushed;
   public long lastUserMessageBytesSent;
   public long lastUserMessageBytesResent;
   public long lastUserMessageBytesReceivedProcessed;
   public long lastUserMessageBytesReceivedIgnored;
   public long lastActualBytesSent;
   public long lastActualBytesReceived;
   public long totalUserMessageBytesPushed;
   public long totalUserMessageBytesSent;
   public long totalUserMessageBytesResent;
   public long totalUserMessageBytesReceivedProcessed;
   public long totalUserMessageBytesReceivedIgnored;
   public long totalActualBytesSent;
   public long totalActualBytesReceived;
   public long connectionStartTime;
   public boolean isLimitedByCongestionControl;
   public long BPSLimitByCongestionControl;
   public boolean isLimitedByOutgoingBandwidthLimit;
   public long BPSLimitByOutgoingBandwidthLimit;
   public long messageInSendBufferImmediate;
   public long messageInSendBufferHigh;
   public long messageInSendBufferMedium;
   public long messageInSendBufferLow;
   public double bytesInSendBufferImmediate;
   public double bytesInSendBufferHigh;
   public double bytesInSendBufferMedium;
   public double bytesInSendBufferLow;
   public long messagesInResendBuffer;
   public long bytesInResendBuffer;
   public double packetlossLastSecond;
   public double packetlossTotal;
}
