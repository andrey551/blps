/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tad.externaltask;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Never
 */
public class ExternalTask {

    public static void main(String[] args) {
      ExternalTaskClient client = ExternalTaskClient.create()
      .baseUrl("http://localhost:8080/engine-rest")
      .asyncResponseTimeout(1000)
      .customizeHttpClient(httpClientBuilder -> {
        httpClientBuilder.setDefaultRequestConfig(RequestConfig.custom()
            .setResponseTimeout(Timeout.ofSeconds(15))
            .build());
      })
      .build();
      
//      Suscribe register task
      client.subscribe("registerTask") .handler((externalTask, externalTaskService) -> {
          String username = externalTask.getVariable("username_reg");
          String password = externalTask.getVariable("password_reg");
          String email = externalTask.getVariable("email_reg");
          System.out.println(username + password + email);
          RestTemplate restTemplate = new RestTemplate();
          String url = "http://localhost:8081/auth/signup";
         Map<String, Object> map = new HashMap<>();
                map.put("username",username);
                map.put("password",password);
                map.put("email",email);
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>(map), 
                              String.class);
          Map<String, Object> returnVariables = new HashMap<>();
          if(response.getStatusCode().is2xxSuccessful()) {
              returnVariables.put("isRegistered", true);
          } else {
              returnVariables.put("isRegistered", false);
          }
          
          externalTaskService.complete(externalTask, returnVariables);
      }).open();
      
//      Suscribe delete user task
        client.subscribe("deleteUserTask").handler(
            (externalTask, externalTaskService) -> {
                String username = externalTask.getVariable("admin_username");
                String password = externalTask.getVariable("admin_password");
                Long userId = externalTask.getVariable("removal_id");

                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/user/" + userId;
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.DELETE, 
                              null, 
                              String.class);
                          Map<String, Object> returnVariables = new HashMap<>();

                if(response.getStatusCode().is2xxSuccessful()) {
                    returnVariables.put("isDeleted", true);
                } else {
                    returnVariables.put("isDeleted", false);
                }

                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
//      Subscribe authorize task
        client.subscribe("authTask").handler(
            (externalTask, externalTaskService) -> {
                String username = externalTask.getVariable("username_log");
                String password = externalTask.getVariable("password_log");
                System.out.println(username + " " + password);
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/auth/login";
                Map<String, Object> map = new HashMap<>();
                map.put("username",username);
                map.put("password",password);
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>(map), 
                              String.class);
                 Map<String, Object> returnVariables = new HashMap<>();
                          
                if(response.getStatusCode().is2xxSuccessful()) {
                    String[] arr = response.getBody().split("\"");
                    String token = arr[3];
                    returnVariables.put("token", token);
                } 
                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
//      Subscribe execute file task
        client.subscribe("receiveFileTask").handler(
            (externalTask, externalTaskService) -> {
                String fileName = externalTask.getVariable("fileName");
                System.out.println(fileName); 
               Map<String, Object> returnVariables = new HashMap<>();
                
                if(fileName.endsWith(".exe")) {
                    returnVariables.put("isExecutable", true);
                } else {
                    returnVariables.put("isExecutable", false);
                }

                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
        //      Subscribe add file to database task
        client.subscribe("addFileToDatabaseTask").handler(
            (externalTask, externalTaskService) -> {
                String token = externalTask.getVariable("token");
                String fileName = externalTask.getVariable("filename");
                Long size = externalTask.getVariable("size");

                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/file/add";
                System.out.println(fileName + size);
                Map<String, Object> map = new HashMap<>();
                map.put("filename",fileName);
                map.put("size",size);
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              createRequest(token, map), 
                              String.class);
                          Map<String, Object> returnVariables = new HashMap<>();
                          
                if(response.getStatusCode().is2xxSuccessful()) {
                    returnVariables.put("isloggedIn", true);
                } else {
                    returnVariables.put("isloggedIn", false);
                }

                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
        client.subscribe("getListTask").handler(
            (externalTask, externalTaskService) -> {
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String username = "postgres";
                String password = "05052001";
                List<Long> filesToDel = new ArrayList<>();
                
                try ( Connection  conn = DriverManager.getConnection(url, username, password);
                     Statement statement = conn.createStatement()) {
                    
                    String query = "select * from todel where datecreated <= CURRENT_DATE - 1 ;";
                    ResultSet result = statement.executeQuery(query);
                    
                    while(result.next()) {
                        filesToDel.add(result.getLong("fileid"));
                    }
                    
                } catch( Exception e) {
                        
                }
                
                Map<String, Object> returnVariables = new HashMap<>();
                returnVariables.put("listToDel", filesToDel);
                System.out.println(filesToDel.toString());
                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
        client.subscribe("sendListTask").handler(
            (externalTask, externalTaskService) -> {
                List<Long> filesToDel = externalTask.getVariable("listToDel");
                
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/message/send";
                Map<String, Object> map = new HashMap<>();
                map.put("message",filesToDel.toString());
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>(map), 
                              String.class);
                Map<String, Object> returnVariables = new HashMap<>();
                          
                if(response.getStatusCode().is2xxSuccessful()) {
                    returnVariables.put("isSent", true);
                } else {
                    returnVariables.put("isSent", false);
                }
                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
        client.subscribe("deleteFileTask").handler(
            (externalTask, externalTaskService) -> {
                List<Long> filesToDel = externalTask.getVariable("listToDel");
                
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8082/inner/delFiles";
                Map<String, Object> map = new HashMap<>();
                map.put("message",filesToDel.toString());
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>("{" + filesToDel.toString() + "}"), 
                              String.class);
                Gson gson = new Gson();
                Map<String, Object> returnVariables = new HashMap<>();
                returnVariables.put("mails", gson.fromJson(response.getBody(), Map.class));

                externalTaskService.complete(externalTask, returnVariables);
         }).open();
        
        client.subscribe("sendMailTask").handler(
            (externalTask, externalTaskService) -> {
                Map<String, List<String>> mailList = externalTask.getVariable("mails");
                
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8082/inner/sendEmail";
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>(mailList), 
                              String.class);  
                externalTaskService.complete(externalTask);
         }).open();
    }
 
   static HttpEntity createRequest(String token, Map<String, Object> entities){
        try {
            System.out.println(token);
            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `content-type` header
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(token);
            // set `accept` header
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(entities, headers);
            return entity;
        } catch (Exception e) {
            System.out.println("");
        }
        return null;
    }
}


