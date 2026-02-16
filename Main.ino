#include <WiFi.h>
#include <PubSubClient.h>
#include <DHTesp.h>

// Credenciais WiFi e MQTT
const char* ssid = "Kast-GUEST";
const char* password = "123";
const char* mqtt_server = "broker.hivemq.com"; // Broker público
const int mqtt_port = 1883;

WiFiClient espClient;
PubSubClient client(espClient);
DHTesp dhtSensor;

void setup() {
  Serial.begin(115200);
  setup_wifi(); // Correctly initialize WiFi
  dhtSensor.setup(13, DHTesp::DHT22);
  client.setServer(mqtt_server, mqtt_port);
  pinMode(2, OUTPUT); // LED Pin
}

void setup_wifi() {
  delay(10);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("WiFi Conectado");
}

void reconnect() {
  while (!client.connected()) {
    if (client.connect("ESP32_Cibersistemas_Weg_Aluno")) {
      Serial.println("Conectado ao MQTT");
    } else {
      delay(5000);
    }
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop(); // Essential for MQTT stability

  TempAndHumidity data = dhtSensor.getTempAndHumidity();

  if (isnan(data.temperature) || isnan(data.humidity)) {
    Serial.println("Failed to read from DHT sensor!");
  } else {
    // Convert float to string for publishing
    String payLoad = String(data.temperature, 2); 
    
    client.publish("senai/data/temperature", payLoad.c_str());
    
    // Use the float variable directly in printf
    Serial.print("Send: ");
    Serial.println(payLoad); 
  }

  delay(5000); // Wait 5 seconds before the next reading
}
