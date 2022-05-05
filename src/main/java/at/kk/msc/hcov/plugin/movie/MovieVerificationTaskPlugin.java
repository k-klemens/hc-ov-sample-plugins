package at.kk.msc.hcov.plugin.movie;

import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.IVerificationTaskPlugin;
import at.kk.msc.hcov.sdk.verificationtask.model.ProvidedContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

@Component
public class MovieVerificationTaskPlugin implements IVerificationTaskPlugin {

  private Map<String, Object> configuration;

  @Override
  public Function<OntModel, List<OntModel>> getElementExtractor() throws PluginConfigurationNotSetException {
    return ontModel -> {
      // Sample data extractor finding all subclasses of persons and their declared properties
      List<OntModel> returnModels = new ArrayList<>();

      Resource resource = ontModel.getOntClass("http://xmlns.com/foaf/0.1/Person");
      List<OntClass> personSubclasses = ontModel.listClasses().filterKeep(
          ontClass -> ontClass.hasSuperClass(resource)
      ).toList();

      personSubclasses.forEach(personSubclass -> {
            OntModel elementsToBeVerified = ModelFactory.createOntologyModel();
            OntClass classCopy = elementsToBeVerified.createClass(personSubclass.getURI());
            personSubclass.listDeclaredProperties().filterKeep(OntProperty::isObjectProperty).forEach(
                objectProperty -> {
                  ObjectProperty objectPropertyCopy = elementsToBeVerified.createObjectProperty(objectProperty.getURI());
                  objectPropertyCopy.addDomain(classCopy);
                  objectPropertyCopy.addRange(objectProperty.getRange());
                }
            );
            returnModels.add(elementsToBeVerified);
          }
      );
      return returnModels;
    };
  }

  @Override
  public BiFunction<OntModel, ProvidedContext, Map<String, Object>> getTemplateVariableValueResolver()
      throws PluginConfigurationNotSetException {
    return (ontModel, providedContext) -> {
      Map<String, Object> templateValues = new HashMap<>();
      String subClassName = ontModel.listClasses().toList().get(0).getLocalName();
      templateValues.put("subclass", subClassName);
      if (configuration != null && (boolean) configuration.getOrDefault("CONTEXT_ENABLED", false)) {
        templateValues.put("context", providedContext.getContextString());
      }
      return templateValues;
    };
  }

  @Override
  public String getTemplate() throws PluginConfigurationNotSetException {
    if (!configuration.containsKey("CONTEXT_ENABLED") || !(boolean) configuration.get("CONTEXT_ENABLED")) {
      return """
          <html>
                      <body> 
                        <h1>Verify the following fact:</h1>         
                        <p>Is <span th:text='${subclass}'> a type of Person?</p>
                        <input type='radio' id='yes' name='answer' value='Yes'>
                        <label for='yes'>Yes</label><br>
                        <input type='radio' id='no' name='answer' value='No'>
                        <label for='no'>No</label><br>
                      </body>
                    </html> 
          """.replace("\n", "").replace("\r", "");
    } else {
      return """
          <html>
            <body>    
              <h1>Verify the following fact:</h1>          
              <p>Is <span th:text='${subclass}'/> from <span th:text='${context}'/> a type of Person?</p> 
              <input type='radio' id='yes' name='answer' value='Yes'>
              <label for='yes'>Yes</label><br>
              <input type='radio' id='no' name='answer' value='No'>
              <label for='no'>No</label><br>
            </body>
          </html>          
          """.replace("\n", "").replace("\r", "");
    }
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {
    this.configuration = map;
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return this.configuration;
  }

  @Override
  public boolean supports(String s) {
    return "MOVIE_TASK_CREATOR".equalsIgnoreCase(s);
  }
}
