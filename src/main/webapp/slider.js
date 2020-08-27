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

let hiddenId = document.getElementById("hidden-id");
let hiddenUser = document.getElementById("userIP");

function setCookie(name, value, days) {
  var expires = "";
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
}

var seenRevisions;
console.log(getCookie("seenRevisions") + " hee");
if (getCookie("seenRevisions") === null) {
  console.log("Hello");
  seenRevisions = [];
  setCookie("seenRevisions", JSON.stringify(seenRevisions))
} else {
  seenRevisions = JSON.parse(getCookie("seenRevisions"));
}

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
      console.log(listEditComments.length);
      if (listEditComments.length > index) {
        // Add datastore as seen
        if (index !=  0) {
          seenRevisions.push(listEditComments[index - 1].revisionId);
          setCookie("seenRevisions", JSON.stringify(seenRevisions));
        }
        index++;
        setContent(listEditComments[index]);
      }   
    }
  } else if (e.keyCode == 18) {
    if (window.location.href.indexOf('slider.html') != -1) {
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
async function getComments(id) {
  console.log('/comments?id=' + id +"&type=revid");
  let response = await fetch('/comments?id=' + id +"&type=revid"); 
  let newEditComments = await response.json();  
  let alreadySeen = new Set(seenRevisions);
  console.log(newEditComments);
  // Add comments that haven't been seen
  newEditComments.forEach(editComment => {
    if (!alreadySeen.has(editComment.revisionId)) {
      listEditComments.push(editComment);
    }
  });
  console.log(listEditComments);
  let editComment = listEditComments[0];
  setContent(editComment);
}

/**
 * Loads comments on the page if user is logged in
 */
window.onload = function() {
  var script = document.createElement("script");
  script.type = "text/javascript";
  script.src = "https://api.ipify.org?format=jsonp&callback=DisplayIP";
  document.getElementsByTagName("head")[0].appendChild(script);
  if (window.location.href.indexOf('id') != -1) {
    console.log("Hello");
    var url_string = window.location.href 
    var url = new URL(url_string);
    var id = url.searchParams.get("id");
    getComments(id);
  } else {
    console.log("sdfsdf");
    getComments();
  }
}

/**
* Loads IP adress of user
*/
function DisplayIP(response) {
  document.getElementById("userIP").setAttribute('value', response.ip);
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
   hiddenId.setAttribute("value", editComment.revisionId);
   if (toxicityObject.experimental) {
     incivilityReason.innerHTML = `TOXICITIY: ${toxicityObject.toxicityReason} <br></br> <h3> Experimental Label </h3> ${toxicityObject.label}: ${toxicityObject.reason} <br></br> <i>This label is experimental, meaning it hasn't been thoroughly tested. Learn more about experimental labels <a style="color: blue;" target=\"_blank\" href="https://support.perspectiveapi.com/s/about-the-api-attributes-and-languages">here</a>. <br></br></i>`;
   } else {
     incivilityReason.innerHTML = `${toxicityObject.label}: ${toxicityObject.reason} <br></br><br></br>`;
   }
   
   notice.innerHTML = `
   <i> The incivility percentage and label comes from Jigsaw and Google's Counter Abuse Technology team's Perspective API, a machine learning model to detect abuse and harassment. You can learn more about the API <a target="_blank" style="color: blue;" href="https://support.perspectiveapi.com/s/about-the-api/">here</a>.
   Since this API utilizes a machine learning model to detect incivility, the percentages and labels are not guaranteed to be accurate and might contain false positives.</i>`;
 }

