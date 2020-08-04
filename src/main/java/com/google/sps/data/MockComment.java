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

package com.google.sps.data;

/** Class mocking the comment object that will be fed to Discover Page backend */
public final class MockComment {
  
  public final String userName;
  public final String text;
  public final String date;
  public final String parentArticle;
  public final String revisionId;

  public MockComment(String revisionId, String userName, String text, String date, String parentArticle) {
    this.userName = userName;
    this.text = text;
    this.date = date;
    this.parentArticle = parentArticle;
    this.revisionId = revisionId;

  }

  public String toString() {
    return revisionId + " " + userName + " " + text + " " + parentArticle + " " + date;
  }

}
