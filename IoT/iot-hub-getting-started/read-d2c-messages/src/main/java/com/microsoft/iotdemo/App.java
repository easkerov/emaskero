package com.microsoft.iotdemo;

import java.io.IOException;
import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.servicebus.*;

import java.nio.charset.Charset;
import java.time.*;
import java.util.function.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String connStr = "Endpoint=sb://iothub-ns-demo-iot-h-178190-a55c155476.servicebus.windows.net/;EntityPath=demo-iot-hub-ea;SharedAccessKeyName=iothubowner;SharedAccessKey=dCXiFhga9GE+g6ZVb8c/g3agr/5rZ5Z8YOv9f7dNXWk=";

    private static EventHubClient receiveMessages(final String partitionId)
    {
        EventHubClient client = null;
        try {
            client = EventHubClient.createFromConnectionStringSync(connStr);
        }
        catch(Exception e) {
            System.out.println("Failed to create client: " + e.getMessage());
            System.exit(1);
        }
        try {
            client.createReceiver(
                    EventHubClient.DEFAULT_CONSUMER_GROUP_NAME,
                    partitionId,
                    Instant.now()).thenAccept(new Consumer<PartitionReceiver>()
            {
                public void accept(PartitionReceiver receiver)
                {
                    System.out.println("** Created receiver on partition " + partitionId);
                    try {
                        while (true) {
                            Iterable<EventData> receivedEvents = receiver.receive(100).get();
                            int batchSize = 0;
                            if (receivedEvents != null)
                            {
                                for(EventData receivedEvent: receivedEvents)
                                {
                                    System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s",
                                            receivedEvent.getSystemProperties().getOffset(),
                                            receivedEvent.getSystemProperties().getSequenceNumber(),
                                            receivedEvent.getSystemProperties().getEnqueuedTime()));
                                    System.out.println(String.format("| Device ID: %s", receivedEvent.getSystemProperties().get("iothub-connection-device-id")));
                                    System.out.println(String.format("| Message Payload: %s", new String(receivedEvent.getBytes(),
                                            Charset.defaultCharset())));
                                    batchSize++;
                                }
                            }
                            System.out.println(String.format("Partition: %s, ReceivedBatch Size: %s", partitionId,batchSize));
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Failed to receive messages: " + e.getMessage());
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Failed to create receiver: " + e.getMessage());
        }
        return client;
    }



    public static void main( String[] args ) throws IOException
    {
        EventHubClient client0 = receiveMessages("0");
        EventHubClient client1 = receiveMessages("1");
        EventHubClient client2 = receiveMessages("2");
        EventHubClient client3 = receiveMessages("3");
        System.out.println("Press ENTER to exit.");
        System.in.read();
        try
        {
            client0.closeSync();
            client1.closeSync();
            client2.closeSync();
            client3.closeSync();
            System.exit(0);
        }
        catch (ServiceBusException sbe)
        {
            System.exit(1);
        }
    }
}
