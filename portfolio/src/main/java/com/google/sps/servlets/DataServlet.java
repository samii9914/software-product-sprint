// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> values;

  @Override
  public void init() {
    values = new ArrayList<>();
    values.add("Amazing!");
    values.add("Good work.");
    values.add("Keep it up!");
  }
  
  @Override
  public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
      String comment = request.getParameter("text-input");
      if(comment==null)
      {
          response.setContentType("text/html;");
          response.getWriter().println("Please enter a valid comment");
          return;
      }
      values.add(comment);
      response.sendRedirect("/index.html");
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJsonUsingGson(values);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Converts a List instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(List<String> values) {
    Gson gson = new Gson();
    String json = gson.toJson(values);
    return json;
  }
}
