package at.kk.msc.hcov.plugin.movie;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrowdsourcingConnectorStubPlugin implements ICrowdsourcingConnectorPlugin {

  private static final Logger LOGGER = LoggerFactory.getLogger("sampleCrowdsourcingConnectorPlugin");
  private Map<String, Object> configuration;

  @Override
  public Map<UUID, String> publishTasks(List<VerificationTask> list) throws PluginConfigurationNotSetException {
    Map<UUID, String> returnMap = new HashMap<>();
    for (VerificationTask task : list) {
      if (task.getTaskHtml().contains("<span>Writer</span>")) {
        returnMap.put(task.getOntologyElementId(), "WriterExternalId");
      } else if (task.getTaskHtml().contains("<span>Person</span>")) {
        returnMap.put(task.getOntologyElementId(), "PersonExternalId");
      } else if (task.getTaskHtml().contains("<span>MovieDirector</span>")) {
        returnMap.put(task.getOntologyElementId(), "MovieDirectorExternalId");
      } else if (task.getTaskHtml().contains("<span>Actor</span>")) {
        returnMap.put(task.getOntologyElementId(), "ActorExternalId");
      } else if (task.getTaskHtml().contains("<span>QualityControl</span>")) {
        returnMap.put(task.getOntologyElementId(), "QualityControlExternalId");
      }
      LOGGER.info("Published task for element id {}", task.getOntologyElementId());
    }
    return returnMap;
  }

  @Override
  public Map<String, HitStatus> getStatusForHits(List<String> list) throws PluginConfigurationNotSetException {
    return Map.of(
        "WriterExternalId", new HitStatus("WriterExternalId", 5, 5),
        "PersonExternalId", new HitStatus("PersonExternalId", 5, 5),
        "MovieDirectorExternalId", new HitStatus("MovieDirectorExternalId", 5, 5),
        "ActorExternalId", new HitStatus("ActorExternalId", 5, 5)
    );
  }

  @Override
  public Map<String, List<RawResult>> getResultsForHits(List<String> list) throws PluginConfigurationNotSetException {
    return Map.of(
        "WriterExternalId", List.of(
            new RawResult("WriterResultId1", "WriterExternalId", "W1", "A"),
            new RawResult("WriterResultId2", "WriterExternalId", "W2", "A"),
            new RawResult("WriterResultId3", "WriterExternalId", "W3", "B"),
            new RawResult("WriterResultId4", "WriterExternalId", "W4", "A"),
            new RawResult("WriterResultId5", "WriterExternalId", "W5", "A")
        ),
        "PersonExternalId", List.of(
            new RawResult("PersonResultId1", "PersonExternalId", "W1", "A"),
            new RawResult("PersonResultId2", "PersonExternalId", "W2", "B"),
            new RawResult("PersonResultId3", "PersonExternalId", "W3", "B"),
            new RawResult("PersonResultId4", "PersonExternalId", "W4", "B"),
            new RawResult("PersonResultId5", "PersonExternalId", "W5", "C")
        ),
        "MovieDirectorExternalId", List.of(
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W1", "C"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W2", "B"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W3", "B"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W4", "C"),
            new RawResult("MovieDirectorResultId", "MovieDirectorExternalId", "W5", "C")
        ),
        "ActorExternalId", List.of(
            new RawResult("ActorResultId", "ActorExternalId", "W1", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W2", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W3", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W4", "A"),
            new RawResult("ActorResultId", "ActorExternalId", "W5", "A")
        )
    );
  }

  @Override
  public void validateConfigurationSetOrThrow() throws PluginConfigurationNotSetException {
    ICrowdsourcingConnectorPlugin.super.validateConfigurationSetOrThrow();
    if (!configuration.containsKey("REQUIRED_CONFIGURATION")) {
      throw new PluginConfigurationNotSetException("Configuration with key REQUIRED_CONFIGURATION not set!");
    }
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {
    configuration = map;
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return configuration;
  }

  @Override
  public boolean supports(String s) {
    return "CS_STUB".equalsIgnoreCase(s);
  }
}
