package it.getinsight.core.queue;

public interface QueueProducer {

    void publish(QueueMessageDTO message, String queueName);
}
