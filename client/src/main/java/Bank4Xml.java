import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;


public class Bank4Xml{
    private final String EXCHANGE_NAME = "cphbusiness.bankXML";
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue_xml";
    private String replyQueueName;

    public Bank4Xml(){
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

    public String xmlBuilder(LoanRequest req){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateAsString="";
        try {
            Date date = sdf.parse("1970-01-01");
            date.setMonth((int)(date.getMonth()+req.getLoanDuration()));
            dateAsString = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String xml = "<LoanRequest> <ssn>"+req.getSsn()+"</ssn> <creditScore>"+req.getCreditScore()+"</creditScore> <loanAmount>"+req.getCreditScore()+"</loanAmount> <loanDuration>"+dateAsString+" 01:00:00.0 CET</loanDuration></LoanRequest>";
        return xml;
       }

    public String call(LoanRequest req) throws Exception {


        replyQueueName = channel.queueDeclare().getQueue();
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish(EXCHANGE_NAME, requestQueueName, props, xmlBuilder(req).getBytes("UTF-8"));

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
