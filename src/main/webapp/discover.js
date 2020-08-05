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
 * Get edit comments from server
 */
async function getComments() {
  let response = await fetch('/comments');
  let listEditComments = await response.json();
  listEditComments.forEach(editComment => {
    let toxicityPercentage = editComment.toxicityObject + "%";
    createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                        "<a target=\"_blank\" href=\"/edit-comment.html\" onClick=\" + viewEditComment(" + editComment.revisionId + ") \"> "+ editComment.comment + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                        editComment.date,
                        "<a target=\"_blank\" href=\"/edit-comment?" + editComment.revisionId + "\" class=\"material-icons md-36\">open_in_new</a>"
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
