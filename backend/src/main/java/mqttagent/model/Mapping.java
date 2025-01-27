package mqttagent.model;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "source", "target", "snoopedTemplates" })
public class Mapping implements Serializable {

  @NotNull
  public long id;

  @NotNull
  public String subscriptionTopic;

  @NotNull
  public String templateTopic;

  @NotNull
  public long indexDeviceIdentifierInTemplateTopic;

  @NotNull
  public API targetAPI;

  @NotNull
  public String source;

  @NotNull
  public String target;

  @NotNull
  public boolean active;

  @NotNull
  public boolean tested;

  @NotNull
  public QOS qos;

  @NotNull
  public MappingSubstitution[] substitutions;

  @NotNull
  public boolean mapDeviceIdentifier;

  @NotNull
  public boolean createNonExistingDevice;

  @NotNull
  public boolean updateExistingDevice;

  @NotNull
  public String externalIdType;

  @NotNull
  public SnoopStatus snoopTemplates;

  @NotNull
  public ArrayList<String> snoopedTemplates;

  @NotNull
  public long lastUpdate;

  @Override
  public boolean equals(Object m) {
    return (m instanceof Mapping) && id == ((Mapping) m).id;
  }

  public void copyFrom(Mapping mapping) {
    this.subscriptionTopic = mapping.subscriptionTopic;
    this.templateTopic = mapping.templateTopic;
    this.indexDeviceIdentifierInTemplateTopic = mapping.indexDeviceIdentifierInTemplateTopic;
    this.targetAPI = mapping.targetAPI;
    this.source = mapping.source;
    this.target = mapping.target;
    this.active = mapping.active;
    this.tested = mapping.tested;
    this.qos = mapping.qos;
    this.substitutions = mapping.substitutions;
    this.mapDeviceIdentifier = mapping.mapDeviceIdentifier;
    this.externalIdType = mapping.externalIdType;
    this.snoopTemplates = mapping.snoopTemplates;
    this.snoopedTemplates = mapping.snoopedTemplates;
  }
}

/**
 * export interface MQTTMapping {
 * id: number,
 * topic: string,
 * targetAPI: string,
 * source: string,
 * target: string,
 * lastUpdate: number
 * }
 */