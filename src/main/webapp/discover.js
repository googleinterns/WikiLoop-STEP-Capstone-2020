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
  const commentsListElement = document.getElementById("feed-list");
  commentsListElement.innerHTML = '';
  console.log(listEditComments);
  listEditComments.forEach(editComment => {
    let toxicityObject = JSON.parse(editComment.toxicityObject);
    let toxicityPercentage = toxicityObject.attributeScores.TOXICITY.summaryScore.value * 100;
    commentsListElement.appendChild(createListElement("Toxicity: " + toxicityPercentage + "% User: " + editComment.userName 
                                 + " Comment: " + editComment.comment + " Parent Artcle: " + editComment.parentArticle + 
                                 " Revision ID: " + editComment.revisionId + " Date: " + editComment.date));
    });
}

/** 
 * Creates an <li> element containing text. 
 */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerHTML = text;
  return liElement;
}

/**
 * Loads comments on the page if user is logged in
 */
window.onload = function() {
  console.log("testing")
  getComments();
}
