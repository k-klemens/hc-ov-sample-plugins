package at.kk.msc.hcov.plugin.movie;

import at.kk.msc.hcov.sdk.verificationtask.IContextProviderPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Component;

@Component
public class MovieContextProvider implements IContextProviderPlugin {
  @Override
  public ProvidedContext provideContextFor(UUID uuid, OntModel ontModel, Map<String, Object> map) {
    String subClassName = ontModel.listClasses().toList().get(0).getLocalName();
    return new ProvidedContext(uuid, subClassName + "-Context");
  }

  @Override
  public boolean supports(String s) {
    return "MOVIE_CONTEXT_PROVIDER".equalsIgnoreCase(s);
  }
}
