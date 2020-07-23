 /**
  * This is the main javascript file for the portfolio page. 
  * Contains a showRandomQuotes method, showComments, createListItem, hideComments and
  * checkLoginInfo, enableForm and showLoginButton methods.
  *
  * Copyright 2019 Google LLC
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *  https://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  * @author: samii9914
  */


/**
 * Shows random quotes on the page.
 */
function showRandomQuotes() {
  const quotes = [
    "Bring your passion and whole self to work everyday!",
    "The secret of getting ahead is getting started.",
    "Do one thing every day that scares you.",
    "Great things are done by a series of small things brought together.",
    "Work hard in silence, let your success be the noise.",
    "The hard days are what make you stronger.",
    "In the middle of every difficulty lies opportunity",
  ];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quotesContainer = document.getElementById('greeting-container');
  quotesContainer.innerText = quote;
}

/**
 * Shows comments on the page.
 */
function showComments() {
  hideComments();
  fetch('/data').then(response => response.json()).then((comments) => {
    const commentsListElement = document.getElementById('comments-container');
    comments.forEach(comment => {
      commentsListElement.appendChild(
      createListElement(comment));
    });
  });
}

/**
 * Creates a list of comments and displays it to main page.
 * @param {string} user
 * @return {!Element}
 */
function createListElement(user) {
  const liElement = document.createElement('li');
  liElement.innerText = user.emailId + " : " + user.comment;
  return liElement;
}

/**
 * Hides comments from the main page.
 */
function hideComments() {
  const list = document.getElementById('comments-container');
  list.innerText = "";
}

/**
 * Checks whether the user is logged in or not.
 */
function checkLoginInfo() {
  fetch('/login').then(response => response.json()).then(userLogin => {
    const checkLoginInfo = document.getElementById('checklogininfo-container');
    const form = document.getElementById('form-container');
    const login = document.getElementById('login-container');
    const logout = document.getElementById('logout-container');

    if(userLogin.isLoggedIn) {
      enableForm(userLogin, form, login, logout);
      checkLoginInfo.hidden = true;
    } else {
      showLoginButton(userLogin, form, login, logout);
      checkLoginInfo.hidden = true;
    }
  });
}

/**
 * Enables form on the main page.
 * @param {!Object} userLogin
 * @param {!Element} form
 * @param {!Element} login
 * @param {!Element} logout
 */
function enableForm(userLogin, form, login, logout) {
  form.hidden = false;
  login.hidden = true;
  logout.hidden = false;
  logout.href = userLogin.Url;
}

/**
 * Shows login link on the main page.
 * @param {!Object} userLogin
 * @param {!Element} form
 * @param {!Element} login
 * @param {!Element} logout 
 */
function showLoginButton(userLogin, form, login, logout) {
  login.hidden = false;
  login.href = userLogin.Url;
  form.hidden = true;
  logout.hidden = true;
}