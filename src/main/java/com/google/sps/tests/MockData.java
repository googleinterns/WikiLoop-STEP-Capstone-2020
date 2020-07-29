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

package com.google.sps.tests;
import com.google.sps.data.MockComment;
import java.util.List;
import java.util.Arrays;

/** Class mocking the comment object that will be fed to Discover Page backend */
public final class MockData {
  
  private final List<MockComment> listMockComments;
  private final String expectedResponse;
 

  public MockData() {
    this.listMockComments = Arrays.asList(new MockComment(861223655,"K6ka", 
                                        "Your explanation on the talk page is completely ludicrous.",  
                                        "September, 25 2018‎, 23:40",
                                        "Incivility"));
    this.expectedResponse = "[{\"userName\":\"K6ka\",\"comment\":\"Your explanation on the talk page is completely ludicrous.\",\"date\":\"September, 25 2018‎, 23:40\",\"parentArticle\":\"Incivility\",\"status\":\"NEW\",\"revisionId\":861223655,\"toxicityObject\":\"\"}]";
  }
  public List<MockComment> getMockComments() {
    return listMockComments;
  }

public String getExpectedResponse() {
    return expectedResponse;
  }
  

}
