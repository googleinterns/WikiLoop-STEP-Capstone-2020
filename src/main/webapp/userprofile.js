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
    });
} );

/**
 * Load some comments 
 */
function loadData() {

  // Build request url
  var url = "https://en.wikipedia.org/w/api.php"; 
  var params = {
    action: "query",
    format: "json",
    list: "allrevisions",
    arvprop: "ids|user|comment|timestamp"
};
  url = url + "?origin=*";
  Object.keys(params).forEach(function(key){url += "&" + key + "=" + params[key];});
  fetch(url,{headers:{"User-agent":"WikiLoop DoubleCheck Team"}}).then(response => response.json()).then((json) => {
      console.log(json);
    fetch('/load-data', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify(json)
    }).then(response => {
        console.log("POST REQUEST WENT THROUGH");
    });
  });
}

/**
 * Get user profile from server
 */
function getUser() {
  loadData();
  fetch('/user').then(response => response.json()).then((user) => {
    console.log(user);
    const userPersonalInformationSection = document.getElementById('personal-information');
    userPersonalInformationSection.innerHTML = "email: "+ "tom@gmail.com";
    const avgToxicityScore = document.getElementById('incivility');
    avgToxicityScore.innerHTML= "Average Incivility Score: \t" + user.avgToxicityScore.substring(0,Math.min(5,user.avgToxicityScore.length)) + "%";
    // Build the list of edits
    user.listEditComments.forEach((edit) => {
      createEditElement(edit, user.userName, user.avgToxicityScore);
    });
  });
}

/**
 * Create a row containing an edit in the table.
 */ 
function createEditElement(edit, userName, avgToxicityScore) {
  var table = $('#my-table').DataTable();
 
  table.row.add( ["<span style=\"color:red;\">" + edit.toxicityObject + "</span>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + edit.revisionId + "\"> "+ edit.revisionId + "</a>", 
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + edit.userName + "\"> "+ edit.userName + "</a>", 
                  "<a target=\"_blank\" href=\"/edit-comment.html\" onClick=\" + viewEditComment(" + edit.revisionId + ") \"> "+ edit.comment + "</a>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + edit.parentArticle + "\"> "+ edit.parentArticle + "</a>", 
                  edit.date]).draw();
}


