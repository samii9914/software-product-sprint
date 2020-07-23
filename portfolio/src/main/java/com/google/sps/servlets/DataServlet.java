/**
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author: samii9914
 */

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Handles comments data.
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
 /** 
  * Represents comment description.
  */
  private static class UserComment {
    private final String emailId;
    private final String comment;

    UserComment(String emailId, String comment) {
      this.emailId = emailId;
      this.comment = comment;
    }

    public String getEmail() {
      return this.emailId;
    }

    public String getComment() {
      return this.comment;
    }
  }

  private static final String INVALID_COMMENT_RESPONSE_STRING = "Please enter a valid comment";
  private static final String ENTITY_NAME = "Comment";
  private static final String ENTITY_NAME_TIMESTAMP = "timestamp";
  private static final String ENTITY_NAME_EMAILID = "EmailId";

 /**
  * Handles server side POST requests.
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if(!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String comment = request.getParameter(ENTITY_NAME);
    long timestamp = System.currentTimeMillis();
    if(comment.length() == 0) {
      response.setContentType("text/html;");
      response.getWriter().println(INVALID_COMMENT_RESPONSE_STRING);
      return;
    }

    String emailId = userService.getCurrentUser().getEmail();
    Entity entity = new Entity(ENTITY_NAME);
    entity.setProperty(ENTITY_NAME, comment);
    entity.setProperty(ENTITY_NAME_TIMESTAMP, timestamp);
    entity.setProperty(ENTITY_NAME_EMAILID, emailId);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(entity);
    response.sendRedirect("/index.html");
  }

 /**
  * Handles server side GET requests.
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query(ENTITY_NAME).addSort(ENTITY_NAME_TIMESTAMP, SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<UserComment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String comment = (String) entity.getProperty(ENTITY_NAME);
      long timestamp = (long) entity.getProperty(ENTITY_NAME_TIMESTAMP);
      String email = (String) entity.getProperty(ENTITY_NAME_EMAILID);
      UserComment userComment = new UserComment(email, comment);
      comments.add(userComment);
    }

    String json = convertToJsonUsingGson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Converts a List instance into a JSON string using the Gson library.
   * @param comments
   */
  private String convertToJsonUsingGson(List<UserComment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}