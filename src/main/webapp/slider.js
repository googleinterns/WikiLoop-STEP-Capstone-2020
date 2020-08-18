let index = 0;
let listEditComments = [];
let articleName = document.getElementById("discover-card-title-id");
let dateHeader = document.getElementById("discover-subtitle-date");
let userHeader = document.getElementById("discover-subtitle-user");
let scoreHeader = document.getElementById("discover-subtitle-score");
let textHeader = document.getElementById("discover-edit-comment-text");
let revisionHeader = document.getElementById("discover-subtitle-revision");
let incivilityReason = document.getElementById("discover-edit-comment-reason");
let notice = document.getElementById("slider-notice");

document.onkeydown = checkKey;

/**
 * Handles the functionality of going to next comment and taking actions
 * based on key press down
 */
function checkKey(e) {
  console.log(e.keyCode)
  e = e || window.event;
  // right arrow, go to next edit comment
  if (e.keyCode === 39) {
      if (window.location.href.indexOf('?id=') == -1) {
      index++;
      if (listEditComments.length > index) {
        setContent(listEditComments[index]);
      }   
    }
  } else if (e.keyCode == 67) {
    if (window.location.href.indexOf('slider.html') != -1) {
      console.log('he')
      window.location.href = '/'
    } else {
       window.location.href = '/slider.html'
    }
  }
}

/** 
 * Get edit comments from server
 */
async function getSpecificComment(id) {
  let response = await fetch('/comments?id=' + id); 
  let newEditComments = await response.json();
  listEditComments = newEditComments;
  let editComment = newEditComments[0];
  setContent(editComment);
}

/** 
 * Get edit comments from server
 */
async function getComments() {
  let response = await fetch('/comments'); 
  let newEditComments = await response.json();
  listEditComments = newEditComments;
  let editComment = newEditComments[0];
  setContent(editComment);
}

/**
 * This function sets the content of the slider page
 */
 function setContent(editComment) {
   let toxicityObject = JSON.parse(editComment.toxicityObject);
   articleName.innerHTML = "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>";
   dateHeader.innerText = editComment.date;
   userHeader.innerHTML = "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>";
   scoreHeader.innerHTML = "<span style=\"color:red;\">" + toxicityObject.toxicityScore + "% </span>";
   textHeader.innerText = editComment.comment;
   revisionHeader.innerHTML = "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>";
   if (toxicityObject.experimental) {
     incivilityReason.innerHTML = `${toxicityObject.label}: ${toxicityObject.reason} <br></br> <i>This label is experimental, meaning it hasn't been thoroughly tested. Learn more about experimental labels <a style="color: blue;" href="https://support.perspectiveapi.com/s/about-the-api-attributes-and-languages">here</a>. <br></br></i>`;
   } else {
     incivilityReason.innerHTML = `${toxicityObject.label}: ${toxicityObject.reason} <br></br><br></br>`;
   }
   
   notice.innerHTML = `
   <i> The incivility percentage and label comes from Jigsaw and Google's Counter Abuse Technology team's Perspective API, a machine learning model to detect abuse and harassment. You can learn more about the API <a style="color: blue;" href="https://support.perspectiveapi.com/s/about-the-api/">here</a>.
   Since this API utilizes a machine learning model to detect incivility, the percentages and labels are not guaranteed to be accurate and might contain false positives.</i>`;
 }


/**
 * Loads comments on the page if user is logged in
 */
window.onload = function() {
  if (window.location.href.indexOf('id') != -1) {
    var url_string = window.location.href 
    var url = new URL(url_string);
    var id = url.searchParams.get("id");
    getSpecificComment(id);
  } else {
    getComments();
}
  }


