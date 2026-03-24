package it.getinsight.core.queue;


public interface QueueMessageHandler<T> {

    
    Class<T> payloadType();

    
    String exchangeId();

    
    String channel();

    
    void onMessage(QueueMessageDTO<T> message, String routingKey);
}
