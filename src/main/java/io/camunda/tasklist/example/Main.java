package io.camunda.tasklist.example;

import io.camunda.common.auth.Authentication;
import io.camunda.common.auth.JwtConfig;
import io.camunda.common.auth.JwtCredential;
import io.camunda.common.auth.Product;
import io.camunda.common.auth.SelfManagedAuthentication;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final String TOKEN_URL =
      "http://localhost:18080/auth/realms/camunda-platform/protocol/openid-connect/token";
  private static final String CLIENT_ID = "SET HERE";
  private static final String CLIENT_SECRET = "SET HERE";
  private static final String TASKLIST_URL = "http://localhost:8082";

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    JwtConfig jwtConfig = new JwtConfig();
    jwtConfig.addProduct(Product.TASKLIST, new JwtCredential(CLIENT_ID, CLIENT_SECRET, null, null));
    Authentication auth =
        SelfManagedAuthentication.builder()
            .keycloakTokenUrl(TOKEN_URL)
            .jwtConfig(jwtConfig)
            .build();

    CamundaTaskListClient client =
        CamundaTaskListClient.builder()
            .taskListUrl(TASKLIST_URL)
            .shouldReturnVariables()
            .shouldLoadTruncatedVariables()
            .authentication(auth)
            .build();

    TaskSearch ts = new TaskSearch().setProcessInstanceKey("2251799814612600");
    TaskList tasksFromInstance = client.getTasks(ts);
    for (Task task : tasksFromInstance) {
      // complete task with variables
      logger.info(task.getName());
    }
  }
}
