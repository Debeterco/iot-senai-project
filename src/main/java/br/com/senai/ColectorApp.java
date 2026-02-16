package br.com.senai;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ColectorApp {
    public static void main(String[] args) {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "JavaClient_Aluno_" + System.currentTimeMillis(); 
        String topic = "senai/data/temperature";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            System.out.println("Conecting to Broker...");
            client.connect(options);
            System.out.println("Connected with sucess!");

            // Assinando o tópico
            client.subscribe(topic, (t, msg) -> {
                String payload = new String(msg.getPayload());
                double temp = Double.parseDouble(payload);
                
                System.out.println("\n----------------------------");
                System.out.println("RECEIVED AUTOMATION DATA:");
                System.out.println("Temperature: " + temp + "°C");

                // Lógica do Desafio Extra
                if (temp > 30.0) {
                    System.err.println("STATUS: [ALERT] Critic Temperature!");
                } else {
                    System.out.println("STATUS: [NORMAL] Stable Operation.");
                }
            });

        } catch (MqttException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}