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


/**  
 * Initialize the table
 */
$(document).ready( function () {
    $('#my-table').DataTable({
      "order": [[ 0, "desc" ]],
      "search": "Filter:",
      "deferRender": true
    });
} );

/**
 * Get user profile from server
 */
function getUser(user) {
  user = user.replace(" ","%20");
  timeFrame = 0;
  //const timeFrameSection = document.getElementById("time-frame");
  //var timeFrame = timeFrameSection.value;
  fetch('/user?timeFrame='+timeFrame + "&User="+user).then(response => response.json()).then((user) => {
    //timeFrameSection.value = timeFrame;
    const userPersonalInformationSection = document.getElementById('personal-information');
    userPersonalInformationSection.innerHTML = "User name: "+ user.userName;
    const avgToxicityScore = document.getElementById('incivility');
    avgToxicityScore.innerHTML= "Average Incivility Score: \t" + user.avgToxicityScore.substring(0,Math.min(5,user.avgToxicityScore.length)) + "%";

    // Get the time frame
    

    // Build the list of edits
    console.log(user.listEditComments);
    user.listEditComments.forEach((edit) => {
      createEditElement(edit, user.userName, user.avgToxicityScore);
    });
    //Remove loader comments finished
  document.getElementById('discover-loader').remove();
  //View table 
  document.getElementById("table-container").style.display = "block";
  console.log("Table Loaded")
  });
}

/**
 * Create a row containing an edit in the table.
 */ 
function createEditElement(edit, userName, avgToxicityScore) {
  var table = $('#my-table').DataTable();
 
  var rowNode = table.row.add( ["<span style=\"color:red;\">" + edit.toxicityScore + "%" + "</span>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + edit.revisionId + "\"> "+ edit.revisionId + "</a>", 
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + edit.userName + "\"> "+ edit.userName + "</a>", 
                  edit.comment,
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + edit.parentArticle + "\"> "+ edit.parentArticle + "</a>", 
                  edit.date,
                  "<a target=\"_blank\" href=\"/edit-comment.html?id=" + edit.revisionId + "\" class=\"material-icons md-36\">input</a>"]).draw();
  rowNode.nodes().to$().attr('id', `revid${edit.revisionId}`);
  console.log(edit.toxicityScore);
}


/**
 * Get user parameter from the url
 */
window.onload = function() {
  if (window.location.href.indexOf('User') != -1) {
    console.log("Hello");
    var url_string = window.location.href 
    var url = new URL(url_string);
    var user = url.searchParams.get("User");
    getUser(user);
  } 
  else {
    console.log("sdfsdf");
    getUser();
  }
}