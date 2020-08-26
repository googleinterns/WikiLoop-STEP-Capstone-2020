
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
  // Create a set to look for a given id
  let alreadySeen = new Set(seenRevisions);
  listEditComments.forEach(editComment => {
    if (!alreadySeen.has(editComment.revisionId)) {
      let toxicityPercentage = JSON.parse(editComment.toxicityObject).toxicityScore + "%";
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
      "search": "Filter:"
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