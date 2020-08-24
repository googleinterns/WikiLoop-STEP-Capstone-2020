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
async function getComments(ids) {
  console.log(ids);
  if (ids === 'revid') {
    ids = document.getElementById("revids").value;
    ids = ids.replace(" ","-");
    window.location.href = '/?id='+ids;
    return;
  } else if (ids === 'user') {
    ids = document.getElementById("userNames").value;
    ids = ids.replace(" ","-");
    ids += "&type=user"
    window.location.href = '/?id='+ids;
    return;
  } else if (ids === 'article') {
    ids = document.getElementById("userNames").value;
    ids = ids.replace(" ","-");
    ids += "&type=article"
    window.location.href = '/?id='+ids;
  }

  console.log(ids);
  let response = await fetch('/comments?id='+ids); 
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
                        "<a target=\"_blank\" href=\"/slider.html?id=" + editComment.revisionId + "\" class=\"material-icons md-36\">input</a>"
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
  var url_string = window.location.href 
  var url = new URL(url_string);
  var ids = url.searchParams.get("id");
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

$(function () {
        $('#datetimepicker6').datetimepicker();
        $('#datetimepicker7').datetimepicker({
            useCurrent: false //Important! See issue #1075
        });
        $("#datetimepicker6").on("dp.change", function (e) {
            $('#datetimepicker7').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker7").on("dp.change", function (e) {
            $('#datetimepicker6').data("DateTimePicker").maxDate(e.date);
        });
    });

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