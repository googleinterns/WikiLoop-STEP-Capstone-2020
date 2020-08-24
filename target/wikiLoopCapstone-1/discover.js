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
 * Load some comments from the Media API
 */
function loadData() {
  // Build request url
  var url = "https://en.wikipedia.org/w/api.php"; 
  var params = {
	action: "query",
	format: "json",
	prop: "revisions",
	revids: "968857509|970167002|967664593|290490251|290547577|290692859|283242467|283416010|969495573"
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
 * Get edit comments from server
 */
async function getComments() {
  // first load some comments from the Media API
  loadData();

  let response = await fetch('/comments');
  let listEditComments = await response.json();
  listEditComments.forEach(editComment => {
    let toxicityPercentage = editComment.toxicityObject + "%";
    createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                        editComment.comment, 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                        editComment.date,
                        "<a target=\"_blank\" href=\"/edit-comment.html?" + editComment.revisionId + "\" class=\"material-icons md-36\">open_in_new</a>"
                        ]);
  });
}

/**
 * Send user to see edit comment breakdown and take action
 */
 function viewEditComment(id) {
   console.log(id)
 }

 /**
 * Send user to see their own profile
 */
 function viewUserProfile(id) {
   console.log(id)
 }

/**
 * Create new table element for
 */
function createTableElement(text) {
  var table = $('#my-table').DataTable();
  table.row.add(text).draw();
}

/**
 * Loads comments on the page if user is logged in
 */
window.onload = function() {
  getComments();
}

/**
 * Initializes the table
 */ 
$(document).ready( function () {
    $('#my-table').DataTable({
      "order": [[ 0, "desc" ]],
    });
} );
