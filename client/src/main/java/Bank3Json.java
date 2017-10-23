import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;


public class Bank3Json {
    private final String EXCHANGE_NAME = "cphbusiness.bankJSON";
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue_json";
    private String replyQueueName;

    public Bank3Json(){
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri("amqp://student:cph@datdb.cphbusiness.dk:5672");
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String call(String message) throws Exception {
        replyQueueName = channel.queueDeclare().getQueue();
            final String corrId = UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            channel.basicPublish(EXCHANGE_NAME, requestQueueName, props, message.getBytes("UTF-8"));

            final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

            channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (properties.getCorrelationId().equals(corrId)) {
                        response.offer(new String(body, "UTF-8"));
                    }
                }
            });
            return response.take();
        }

    public void close() throws IOException {
        connection.close();
    }
}
