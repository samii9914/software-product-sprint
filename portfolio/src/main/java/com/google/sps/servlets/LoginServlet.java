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
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Checks login status.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

 /** 
  * Represents user details.
  */
  private static class UserDetails {
    private final boolean isLoggedIn;
    private final String Url;

    UserDetails(boolean isloggedin, String Url) {
      this.isLoggedIn = isloggedin;
      this.Url = Url;
    }

    public String getUrl() {
        return this.Url;
    }

    public boolean getIsLoggedIn() {
      return this.isLoggedIn;
    }
  }

 /**
  * Handles server side GET requests.
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String Url = "";
    UserService userService = UserServiceFactory.getUserService();
    boolean isLoggedIn = userService.isUserLoggedIn();

    if (isLoggedIn) {
      String urlToRedirectToAfterUserLogsOut = "/";
      Url = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

    } else {
      String urlToRedirectToAfterUserLogsIn = "/";
      Url = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
    }

    UserDetails userstatus = new UserDetails(isLoggedIn, Url);
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(userstatus));
  }
}