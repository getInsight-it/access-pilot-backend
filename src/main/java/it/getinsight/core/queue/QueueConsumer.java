package it.getinsight.core.queue;

public interface QueueConsumer {

    void onMessage(QueueMessageDTO message);
}
