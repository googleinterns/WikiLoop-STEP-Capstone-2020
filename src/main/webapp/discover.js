
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
if (getCookie("seenRevisions") === null) {
  seenRevisions = [];
  setCookie("seenRevisions", JSON.stringify(seenRevisions))
} else {
  seenRevisions = JSON.parse(getCookie("seenRevisions"));
}
document.onkeydown = checkKey;

/**
 * Handles the functionality of going to slide function
 */
function checkKey(e) {
  e = e || window.event;
  if (e.keyCode === 18) {
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

var startTime, endTime;

function start() {
  startTime = new Date();
};

function end() {
  endTime = new Date();
  var timeDiff = endTime - startTime; //in ms
  // strip the ms
  timeDiff /= 1000;

  // get seconds 
  var seconds = Math.round(timeDiff);
  console.log(seconds + " seconds");
}

/**
 * Load some comments from the Media API
 */
function loadData() {
  var ids = document.getElementById("revids").value;
  console.log(ids);
  ids = ids.replace(" ","|");
  console.log("ids"); /*
  // Build request url
  var url = "https://en.wikipedia.org/w/api.php"; 
  var params = {
	action: "query",
	format: "json"
  prop: "revisions",
  revids: ids
}; */
/*

if (ids.length == 0){
  params["list"] = "recentchanges";
  params["rcnamespace"] = "1|2|3|4|5|11|9|7|12|13|15|101|109";
  params["rcprop"] = "title|timestamp|ids|parsedcomment|comment|flags|user|userid|tags"; 
  params["rclimit"] = "10"; 
} else {
  params["prop"] = "revisions";
  params["revids"] = ids;
}
  url = url + "?origin=*";
  Object.keys(params).forEach(function(key){url += "&" + key + "=" + params[key];});
  fetch(url,{headers:{"User-agent":"WikiLoop DoubleCheck Team"}}).then(response => response.json()).then((json) => {
      console.log(json);
    fetch('/load-data', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "GET",
        //body: JSON.stringify(json)
    }).then(response => {
        console.log("POST REQUEST WENT THROUGH");
        document.location.href = response.url;
    });
  }); */
} 

/** 
 * Get edit comments from server
 */
async function getComments(ids, type, num) {
  if (type == null) {
    type = "";
  }
  if (num == null) {
    num = "";
  }
  console.log(ids);
  if (ids === null) {
    ids = "";
  }
  if (ids === 'revid') {
    type = ids;
    ids = document.getElementById("revids").value;
    ids = ids.replace(" ","-");
    window.location.href = `/?id=${ids}&type=${type}&num=`;
    return;
  } else if (ids === 'user') {
    type = ids;
    ids = document.getElementById("userNames").value;
    num = document.getElementById("numComments").value;
    ids = ids.replace("%20","-");
    window.location.href = `/?id=${ids}&type=${type}&num=${num}`;
    return;
  }

  
 console.log(`/comments?id=${ids}&type=${type}&num=${num}`);
  let response = await fetch(`/comments?id=${ids}&type=${type}&num=${num}`); 
  let listEditComments = await response.json();
  console.log(response);
  console.log("Response returned")
  end();
  // Create a set to look for a given id
  let alreadySeen = new Set(seenRevisions);
  listEditComments.forEach(editComment => {
    let toxicityPercentage;
    if (!alreadySeen.has(editComment.revisionId)) {
      if (editComment.toxicityObject !== null && editComment.toxicityObject !== "" && editComment.toxicityObject !== "null" && editComment.toxicityObject !== "0") {
        toxicityPercentage = JSON.parse(editComment.toxicityObject).toxicityScore + "%";
      } else {
        toxicityPercentage = '0' + "%";
      }
      
      createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                          "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                          "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                          editComment.comment, 
                          "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                          editComment.date,
                          "<a target=\"_blank\" href=\"/slider.html?id=" + editComment.revisionId + "\" class=\"material-icons md-36\">input</a>  <a onClick=seenComment('"+ editComment.revisionId + "') class=\"material-icons\"> remove_circle</a>"
                          ], editComment.revisionId);
   }
  });

  //Remove loader comments finished
  document.getElementById('discover-loader').remove();
  //View table 
  document.getElementById("table-container").style.display = "block";
  console.log("Table Loaded")
  end();
}

/**
 * Given a revision id, stores the revision id inside a cookie
 * to show that the user has already seen comment
 */
 
function seenComment(id) {
  seenRevisions.push(id);
  setCookie("seenRevisions", JSON.stringify(seenRevisions));
   var $datatable = $('#my-table').DataTable();
    $datatable.row($`revid${id}`).remove().draw(false);
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
function createTableElement(text, id) {
  let rowId = `revid${id}`;
  var table = $('#my-table').DataTable();
  var rowNode = table.row.add(text).draw();
  rowNode.nodes().to$().attr('id', rowId);
}


/**
 * Loads comments on the page if user is logged in
 */
window.onload = function() {
  start();
  let url_string = window.location.href 
  let url = new URL(url_string);
  let ids = url.searchParams.get("id");
  let type = url.searchParams.get("type");
  let num = url.searchParams.get("num");

  getComments(ids, type, num);
}

/**
 * Initializes the table
 */ 
$(document).ready( function () {
    $('#my-table').DataTable({
      "order": [[ 0, "desc" ]],
      "search": "Filter:",
      "deferRender": true
    });
} );

    // Change the selector if needed
var $table = $('#my-table'),
    $bodyCells = $table.find('tbody tr:first').children(),
    colWidth;

// Get the tbody columns width array
colWidth = $bodyCells.map(function() {
    return $(this).width();
}).get();

// Set the width of thead columns
$table.find('thead tr').children().each(function(i, v) {
    $(v).width(colWidth[i]);
});  