document.onkeydown = checkKey;

/**
 * Handles the functionality of going to slide function
 */
function checkKey(e) {
  e = e || window.event;
  if (e.keyCode === 84) {
      if (window.location.href.indexOf('/') != -1) {
        window.location.href = '/slider.html'
    }
  }
}

/** 
 * Get edit comments from server
 */
async function getComments(ids, type, date) {
  if (type == null) {
    type = "";
  }
  if (date == null) {
    date = "";
  }
  console.log(ids);
  if (ids === 'revid') {
    type = ids;
    ids = document.getElementById("revids").value;
    ids = ids.replace(" ","-");
    window.location.href = `/?id=${ids}&type=${type}&date=${date}`;
    return;
  } else if (ids === 'user') {
    type = ids;
    ids = document.getElementById("userNames").value;
    date = `${document.getElementById("startDate")}+${document.getElementById("endDate")}`;
    ids = ids.replace(" ","-");
    window.location.href = `/?id=${ids}&type=${type}&date=${date}`;
    return;
  } else if (ids === 'article') {
    type = ids;
    ids = document.getElementById("articles").value;
    ids = ids.replace(" ","-");
    window.location.href = `/?id=${ids}&type=${type}&date=${date}`;
    date = `${document.getElementById("startDateArticle")}+${document.getElementById("endDateArticle")}`;
    return;
  }

  console.log(ids);
  let response = await fetch(`/comments?id=${ids}&type=${type}&date=${date}`); 
  let listEditComments = await response.json();
  console.log(listEditComments);
  listEditComments.forEach(editComment => {
    let toxicityPercentage = JSON.parse(editComment.toxicityObject).toxicityScore + "%";
    createTableElement(["<span style=\"color:red;\">" + toxicityPercentage + "</span>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + editComment.revisionId + "\"> "+ editComment.revisionId + "</a>", 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + editComment.userName + "\"> "+ editComment.userName + "</a>", 
                        editComment.comment, 
                        "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + editComment.parentArticle + "\"> "+ editComment.parentArticle + "</a>", 
                        editComment.date,
                        "<a target=\"_blank\" href=\"/slider.html?id=" + editComment.revisionId + "\" class=\"material-icons md-36\">input</a>  <span class=\"material-icons\"> remove_circle</span>"
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
  let url_string = window.location.href 
  let url = new URL(url_string);
  let ids = url.searchParams.get("id");
  let type = url.searchParams.get("type");
  let date = url.searchParams.get("date");

  getComments(ids, type, date);
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