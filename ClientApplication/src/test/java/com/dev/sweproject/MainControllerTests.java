package com.dev.sweproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
class MainControllerTests {

  private static MainController controller;

  @BeforeAll
  static void setup() {
    controller = new MainController();
  }

  @Test
  void indexPageTest() {
    assertEquals("index", controller.index());
  }

  @Test
  void documentPageTest() {
    assertEquals("document", controller.document());
  }

  @Test
  void dashboardPageTest() {
    assertEquals("dashboard", controller.dashboard());
  }

  @Test
  void comparePageTest() {
    assertEquals("compare", controller.compare());
  }

  @Test
  void getRegisterPageTest() {
    assertEquals("register_form", controller.showForm(null));
  }

  @Test
  void generateKeyPageTest() {
    assertEquals("service_registration", controller.generateKey());
  }

  @Test
  void getLoginPageTest() {
    assertEquals("login_form", controller.gotoLogin(null));
  }

  @Test
  void postRegisterPageTest1() {
    User testUser = new User();
    testUser.setName("Test");
    testUser.setPassword("Test");
    testUser.setProfession("Test");
    testUser.setBirthday("Test");
    testUser.setGender("Test");
    testUser.setEmail("Test");
    assertEquals("register_form", controller.submitForm(null, testUser));
  }

  @Test
  void retrieveDocumentsApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).retrieveDocuments(anyString());

    ResponseEntity<?> responseEntity = spyController.retrieveDocumentsApi(mockUserId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void retrieveDocumentsStatsApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).retrieveDocumentStats(anyString(), anyString());

    ResponseEntity<?> responseEntity = spyController.retrieveDocumentStatsApi(mockUserId, "r");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void retrieveDocumentContentsApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).retrieveDocumentContents(anyString(), anyString());

    ResponseEntity<?> responseEntity = spyController.retrieveDocContentsApi(mockUserId, "r");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void retrieveDocumentExistenceApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).retrieveDocumentExistence(anyString(), anyString());

    ResponseEntity<?> responseEntity = spyController.retrieveDocExistenceApi(mockUserId, "r");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void retrieveUsernamesApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).retrieveUsernames();

    ResponseEntity<?> responseEntity = spyController.retrieveUsernamesApi();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void postDeleteRequestApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).deleteRequest(anyString(), anyString());

    ResponseEntity<?> responseEntity = spyController.postDeleteRequestApi(mockUserId, "r");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void patchShareRequestApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).patchShareRequest(anyString(), anyString(),
        anyString());

    ResponseEntity<?> responseEntity = spyController.patchShareRequestApi(mockUserId, "r",
        "new");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void postUploadRequestApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockResult = "result";

    MainController spyController = spy(mainController);
    doReturn(mockResult).when(spyController).postUploadRequest(anyString(), anyString(),
        anyString());

    ResponseEntity<?> responseEntity = spyController.postUploadRequestApi(mockUserId, "r",
        "new");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockResult);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void attemptLoginTest() {
    MainController mainController = new MainController();
    String mockEmail = "test.user@example.com";
    String mockPassword = "password";
    Model mockModel = mock(Model.class);
    CompletableFuture<Boolean> mockResult = CompletableFuture.completedFuture(true);

    FirebaseService mockFirebaseDataService = mock(FirebaseService.class);
    when(mockFirebaseDataService.confirmLogin(anyString(), anyString(), any()))
        .thenReturn(mockResult);

    GlobalInfo.assignDatabase(mockFirebaseDataService);

    String viewName = mainController.attemptLogin(mockEmail, mockPassword, mockModel);

    assertEquals("dashboard", viewName);
  }

  @Test
  void attemptLoginInvalidCredentialsTest() throws Exception {
    MainController mainController = new MainController();
    String mockEmail = "test.user@example.com";
    String mockPassword = "wrong_password";
    Model mockModel = mock(Model.class);
    CompletableFuture<Boolean> mockResult = CompletableFuture.completedFuture(false);

    FirebaseService mockFirebaseDataService = mock(FirebaseService.class);
    when(mockFirebaseDataService.confirmLogin(anyString(), anyString(), any()))
        .thenReturn(mockResult);

    GlobalInfo.assignDatabase(mockFirebaseDataService);
    String viewName = mainController.attemptLogin(mockEmail, mockPassword, mockModel);
    assertEquals("login_form", viewName);
  }

  @Test
  void attemptLoginExceptionTest() {
    MainController mainController = new MainController();
    String mockEmail = "test.user@example.com";
    String mockPassword = "password";
    Model mockModel = mock(Model.class);

    FirebaseService mockFirebaseDataService = mock(FirebaseService.class);
    when(mockFirebaseDataService.confirmLogin(anyString(), anyString(), any()))
        .thenThrow(new RuntimeException("Mock Exception"));

    GlobalInfo.assignDatabase(mockFirebaseDataService);
    String viewName = mainController.attemptLogin(mockEmail, mockPassword, mockModel);
    assertEquals("error", viewName);
  }

  @Test
  void submitFormInvalidUserTest() {
    MainController mainController = new MainController();
    Model mockModel = mock(Model.class);
    User invalidUser = new User();

    String viewName = mainController.submitForm(mockModel, invalidUser);

    assertEquals("register_form", viewName);
  }

  @Test
  void retrieveDocumentDiffsApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockDocOne = "Doc1";
    String mockDocTwo = "Doc2";
    String mockDifferences = "no diff";

    MainController spyController = spy(mainController);
    doReturn(mockDifferences).when(spyController).retrieveDocumentDifferences(anyString(),
        anyString(), anyString());
    ResponseEntity<?> responseEntity = spyController.retrieveDocumentDiffsApi(mockUserId,
        mockDocOne, mockDocTwo);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockDifferences);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void retrieveDocumentNamesApiTest() throws JsonProcessingException {
    MainController mainController = new MainController();
    String mockUserId = "mockUserId";
    String mockDocNames = "Doc1";

    MainController spyController = spy(mainController);
    doReturn(mockDocNames).when(spyController).retrieveDocNames(anyString());
    ResponseEntity<?> responseEntity = spyController.retrieveDocumentNamesApi(mockUserId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedJson = objectMapper.writeValueAsString(mockDocNames);
    assertEquals(expectedJson, responseEntity.getBody());
  }

  @Test
  void showFormTest() {
    MainController mainController = new MainController();
    Model mockModel = mock(Model.class);

    String viewName = mainController.showForm(mockModel);

    assertEquals("register_form", viewName);
  }

  @Test
  void convertDocumentTitleTest() {
    String expected = "test%20doc";
    assertEquals(expected, controller.convertDocumentTitle("test doc"));
  }

  @Test
  void sendHttpRequestSuccessTest() {
    MainController mainController = new MainController();
    String mockUrl = "http://example.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    GlobalInfo.assignTemplate(mockRestTemplate);

    ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Success response",
        HttpStatus.OK);
    when(mockRestTemplate.getForEntity(eq(mockUrl), eq(String.class))).thenReturn(
        mockResponseEntity);

    String result = mainController.sendHttpRequest(mockUrl);
    assertEquals("Success response", result);
  }

  @Test
  void sendHttpRequestFailureTest() {
    MainController mainController = new MainController();
    String mockUrl = "http://example.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    GlobalInfo.assignTemplate(mockRestTemplate);

    ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Error response",
        HttpStatus.INTERNAL_SERVER_ERROR);
    when(mockRestTemplate.getForEntity(eq(mockUrl), eq(String.class))).thenReturn(
        mockResponseEntity);
    String result = mainController.sendHttpRequest(mockUrl);
    assertEquals("failure: 500 INTERNAL_SERVER_ERROR - Error response", result);
  }

  @Test
  void sendHttpRequestHttpClientErrorTest() {
    MainController mainController = new MainController();
    String mockUrl = "http://example.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    GlobalInfo.assignTemplate(mockRestTemplate);

    when(mockRestTemplate.getForEntity(eq(mockUrl), eq(String.class))).thenThrow(
        new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found",
            "Error response".getBytes(), Charset.defaultCharset()));
    String result = mainController.sendHttpRequest(mockUrl);
    assertEquals("failure: 404 NOT_FOUND - Error response", result);
  }

  @Test
  void postServiceRegistrationRequestErrorTest() throws IOException {
    MainController mainController = new MainController();
    CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);

    when(mockHttpClient.execute(any(HttpPost.class))).thenThrow(new IOException("Test error"));
    String result = mainController.postServiceRegistrationRequest();
    assertEquals("error: An unexpected error has occurred.", result);
  }

  @Test
  void postUploadRequestErrorTest() {
    MainController mainController = new MainController();
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    GlobalInfo.assignTemplate(mockRestTemplate);

    when(mockRestTemplate.postForEntity(anyString(),
        any(org.springframework.http.HttpEntity.class), eq(String.class)))
        .thenThrow(new RestClientException("Test error"));

    String result = mainController.postUploadRequest("userId", "documentTitle",
        "Test contents");
    assertEquals("error: An unexpected error has occurred.", result);
  }

  @Test
  void deleteRequestErrorTest() throws IOException {
    MainController mainController = new MainController();
    CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
    when(mockHttpClient.execute(any(HttpDelete.class))).thenThrow(new IOException("Test error"));
    String result = mainController.deleteRequest("userId", "documentTitle");
    assertEquals("error: An unexpected error has occurred.", result);
  }

  @Test
  void retrieveUsernamesSuccessTest() {
    FirebaseService mockFirebaseDataService = mock(FirebaseService.class);
    GlobalInfo.assignDatabase(mockFirebaseDataService);

    CompletableFuture<List<String>> mockResult = CompletableFuture.completedFuture(
        Arrays.asList("user1", "user2"));
    MainController mainController = new MainController();
    when(mockFirebaseDataService.getSubcollectionNames(null)).thenReturn(mockResult);
    String result = mainController.retrieveUsernames();
    assertEquals("[\"user1\",\"user2\"]", result);
  }

  @Test
  void retrieveUsernamesErrorTest() {
    FirebaseService mockFirebaseDataService = mock(FirebaseService.class);
    GlobalInfo.assignDatabase(mockFirebaseDataService);

    CompletableFuture<List<String>> mockResult = new CompletableFuture<>();
    mockResult.completeExceptionally(new RuntimeException("Test error"));

    MainController mainController = new MainController();
    when(mockFirebaseDataService.getSubcollectionNames(null)).thenReturn(mockResult);
    String result = mainController.retrieveUsernames();
    assertEquals("[]", result);
  }

  @Test
  void patchShareRequestErrorTest() throws IOException {
    MainController mainController = new MainController();
    CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);

    when(mockHttpClient.execute(any(HttpPatch.class))).thenThrow(new IOException("Test error"));
    String result = mainController.patchShareRequest("userId", "documentTitle",
        "newUserId");
    assertEquals("error: An unexpected error has occurred.", result);
  }
}


