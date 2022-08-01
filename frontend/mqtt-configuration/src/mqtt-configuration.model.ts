export interface MQTTAuthentication {
  mqttHost: string;
  mqttPort: string;
  user: string;
  password: string;
  clientId: string;
  useTLS: boolean;
}

export interface MQTTMapping {
  id: number,
  topic: string,
  source: string,
  target: string
}