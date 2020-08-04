// initializes the table
$(document).ready( function () {
    $('#my-table').DataTable();
} );

/* TO DO (DEUS):   Use real data once David pushes the code for the EditComment class*/
function getUser() {
  fetch('/user').then(response => response.json()).then((user) => {
    const userNameSection = document.getElementById('user-name');
    userNameSection.innerText = user.userName;
    const userPersonalInformationSection = document.getElementById('personal-information-box');
    userPersonalInformationSection.innerText = "email: "+ "tom@gmail.com";
    const avgToxicityScore = document.getElementById('toxicity-score-box');
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
 
  table.row.add( [edit.toxicityObject,
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?&oldid=" + edit.revisionId + "\"> "+ edit.revisionId + "</a>", 
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/User:" + edit.userName + "\"> "+ edit.userName + "</a>", 
                  "<a target=\"_blank\" href=\"/edit-comment.html\" onClick=\" + viewEditComment(" + edit.revisionId + ") \"> "+ edit.comment + "</a>",
                  "<a target=\"_blank\" href=\"https://en.wikipedia.org/w/index.php?title=" + edit.parentArticle + "\"> "+ edit.parentArticle + "</a>", 
                  edit.date]).draw();
}


