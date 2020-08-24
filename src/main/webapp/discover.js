document.onkeydown = checkKey;

/**
 * Handles the functionality of going to slide function
 */
function checkKey(e) {
  e = e || window.event;
  if (e.keyCode === 67) {
      if (window.location.href.indexOf('/') != -1) {
        window.location.href = '/slider.html'
    }
  }
}


/**
 * False
 */
function testData(){
  
}
/**
 * Load some comments from the Media API
 */
function loadData() {
  var ids = document.getElementById("revids").value;
  console.log(ids);
  ids = ids.replace(" ","|")
  // Build request url
  var url = "https://en.wikipedia.org/w/api.php"; 
  var params = {
	action: "query",
	format: "json",
	prop: "revisions",
	revids: ids
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
        document.location.href = response.url;
    });
  });
}

/** 
 * Get edit comments from server
 */
async function getComments(ids) {
  let response = await fetch('/comments?ids='+ids); 
  let listEditComments = await response.json();
  console.log(listEditComments);
  listEditComments.forEach(editComment => {
    let toxicityPercentage = JSON.parse(editComment.toxicityObject).score + "%";
    createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                        editComment.comment, 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                        editComment.date,
                        "<a target=\"_blank\" href=\"/edit-comment.html?id=" + editComment.revisionId + "\" class=\"material-icons md-36\">input</a>"
                        ]);
  });
}

/** 
 * Get edit comments from server
 */
async function getOneComments(ids) {
  let response = await fetch('/comments?ids='+ids); 
  let listEditComments = await response.json();
  console.log(listEditComments);
  let editComment = listEditComments[0];
    let toxicityPercentage = editComment.toxicityObject + "%";
    createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                        editComment.comment, 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                        editComment.date,
                        "<a target=\"_blank\" href=\"/edit-comment.html?id=" + editComment.revisionId + "\" class=\"material-icons md-36\">input</a>"
                        ]);
  
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
  var url_string = window.location.href 
  var url = new URL(url_string);
  var ids = url.searchParams.get("ids");
  console.log(ids);
  getComments(ids);
}

/**
 * Initializes the table
 */ 
$(document).ready( function () {
    $('#my-table').DataTable({
      "order": [[ 0, "desc" ]],
      "search": "Filter:"
    });
} );