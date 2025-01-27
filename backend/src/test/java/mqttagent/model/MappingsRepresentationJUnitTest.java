package mqttagent.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MappingsRepresentationJUnitTest {

  @Test
  void testRegexpNormalizeTopic() {

    String topic1 = "/rom/hamburg/madrid/#/";
    String nt1 = topic1.replaceAll(MappingsRepresentation.REGEXP_REMOVE_TRAILING_SLASHES, "#");
    assertEquals(nt1, "/rom/hamburg/madrid/#");

    String topic2 = "////rom/hamburg/madrid/#/////";
    String nt2 = topic2.replaceAll(MappingsRepresentation.REGEXP_REDUCE_LEADING_TRAILING_SLASHES, "/");
    assertEquals(nt2, "/rom/hamburg/madrid/#/");

    String topic3 = "////rom/hamburg/madrid//+//+//";
    int count = topic3.length() - topic3.replace("+", "").length();
    System.out.println(count);

  }

  @Test
  void testNormalizeTopic() {

    String topic1 = "/rom/hamburg/madrid/#/";
    assertEquals(MappingsRepresentation.normalizeTopic(topic1), "/rom/hamburg/madrid/#");

    String topic2 = "///rom/hamburg/madrid/+//";
    assertEquals(MappingsRepresentation.normalizeTopic(topic2), "/rom/hamburg/madrid/+/");

  }

  @Test
  void testIsTemplateTopicValid() {

    Mapping m1 = new Mapping();
    m1.setTemplateTopic("/device/+/east/");
    m1.setSubscriptionTopic("/device/#");
    assertEquals(new ArrayList<ValidationError>(), MappingsRepresentation.isTemplateTopicValid(m1));

    Mapping m2 = new Mapping();
    m2.setTemplateTopic("/device");
    m2.setSubscriptionTopic("/device/#");
    ValidationError[] l2 = {ValidationError.TemplateTopic_Must_Match_The_SubscriptionTopic};
    assertEquals(Arrays.asList(l2), MappingsRepresentation.isTemplateTopicValid(m2));

    Mapping m3 = new Mapping();
    m3.setTemplateTopic("/device/");
    m3.setSubscriptionTopic("/device/#");
    assertEquals(new ArrayList<ValidationError>(), MappingsRepresentation.isTemplateTopicValid(m3));
  }

}
