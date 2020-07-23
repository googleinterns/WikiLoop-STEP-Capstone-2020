// initializes the table
$(document).ready( function () {
    $('#my-table').DataTable();
} );

function getUser() {
  fetch('/user').then(response => response.json()).then((user) => {
    const userNameSection = document.getElementById('user-name');
    userNameSection.innerText = user.userName;
    const userPersonalInformationSection = document.getElementById('personal-information-box');
    userPersonalInformationSection.innerText = "Personal Information (Just an ID for now): "+ user.id;
    const avgToxicityScore = document.getElementById('toxicity-score-box');
    avgToxicityScore.innerText= "Average Toxicity Score: \t" + user.avgToxicityScore;

    // Build the list of edits
    user.list_edit_comments.forEach((edit) => {
      createEditElement(edit);
    });
  });
}

// Creates a row containing an edit in the table.
function createEditElement(edit) {
  var table = $('#my-table').DataTable();
 
  table.row.add( ["50%", "Tom","www.fakeaticle.com",edit,"July 23, 2020"]).draw();
}


