package br.com.senai;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ColectorApp {
    public static void main(String[] args) {
        // 1. Change to ssl:// and use the URL from your image
        String broker = "ssl://e0722a79b31148c9b57155e48211f476.s1.eu.hivemq.cloud:8883";
        String clientId = "JavaClient_Debeterco_" + System.currentTimeMillis(); 
        String topic = "senai/debeterco/temperatura";

        // 2. These must be the credentials you created in the HiveMQ Cloud Console
        String username = "Kasteller"; 
        String password = "Brasil2025.";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            
            // HiveMQ Cloud Requirements
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            
            // This enables TLS/SSL for the connection
            options.setAutomaticReconnect(true);

            System.out.println("Connecting to Secure HiveMQ Cloud...");
            client.connect(options);
            System.out.println("Connected with success!");

            // Assinando o tópico
            client.subscribe(topic, (t, msg) -> {
                try {
                    String payload = new String(msg.getPayload());
                    double temp = Double.parseDouble(payload);
                    
                    System.out.println("\n----------------------------");
                    System.out.println("RECEIVED AUTOMATION DATA:");
                    System.out.println("Temperature: " + temp + "°C");

                    // Lógica do Desafio Extra
                    if (temp > 30.0) {
                        System.err.println("STATUS: [ALERT] Critical Temperature!");
                    } else {
                        System.out.println("STATUS: [NORMAL] Stable Operation.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing temperature: " + e.getMessage());
                }
            });

        } catch (MqttException e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}