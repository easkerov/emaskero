package com.microsoft.iotdemo;

import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String connectionString = "HostName=demo-iot-hub-ea.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=dCXiFhga9GE+g6ZVb8c/g3agr/5rZ5Z8YOv9f7dNXWk=";
    private static final String deviceId = "myFirstJavaDevice";


    public static void main( String[] args ) throws IOException, URISyntaxException, Exception
    {
        RegistryManager registryManager = RegistryManager.createFromConnectionString(connectionString);

        Device device = Device.createFromId(deviceId, null, null);
        try {
            device = registryManager.addDevice(device);
        } catch (IotHubException iote) {
            try {
                device = registryManager.getDevice(deviceId);
            } catch (IotHubException iotf) {
                iotf.printStackTrace();
            }
        }
        System.out.println("Device ID: " + device.getDeviceId());
        System.out.println("Device key: " + device.getPrimaryKey());
    }
}
