// initializes the table
$(document).ready( function () {
    $('#my-table').DataTable();
} );

/* Load some comments */
function loadData() {
  var url = "https://en.wikipedia.org/w/api.php"; 

  var params = {
    action: "query",
    format: "json",
    list: "allrevisions",
    arvprop: "ids|user|comment|timestamp"
    //arvuser: "Place holder"
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

function getUser() {
  loadData();
  fetch('/user').then(response => response.json()).then((user) => {
    console.log(user);
    const userPersonalInformationSection = document.getElementsByClassName('personal-information');
    userPersonalInformationSection.innerText = "email: "+ "tom@gmail.com";
    const avgToxicityScore = document.getElementsByClassName('toxicity-incivility');
    avgToxicityScore.innerText= "Average Toxicity Score: \t" + user.avgToxicityScore;

    // Build the list of edits
    user.listEditComments.forEach((edit) => {
      createEditElement(edit, user.userName, user.avgToxicityScore);
    });
  });
}

// Creates a row containing an edit in the table.
function createEditElement(edit, userName, avgToxicityScore) {
  var table = $('#my-table').DataTable();
 
  table.row.add( ["<span style=\"color:red;\">" + edit.toxicityObject + "</span>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + edit.revisionId + "\"> "+ edit.revisionId + "</a>", 
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + edit.userName + "\"> "+ edit.userName + "</a>", 
                  "<a target=\"_blank\" href=\"/edit-comment.html\" onClick=\" + viewEditComment(" + edit.revisionId + ") \"> "+ edit.comment + "</a>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + edit.parentArticle + "\"> "+ edit.parentArticle + "</a>", 
                  edit.date]).draw();
}


