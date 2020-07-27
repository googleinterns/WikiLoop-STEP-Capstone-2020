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
    userPersonalInformationSection.innerText = "Personal Information (Just an ID for now): "+ user.id;
    const avgToxicityScore = document.getElementById('toxicity-score-box');
    avgToxicityScore.innerText= "Average Toxicity Score: \t" + user.avgToxicityScore;

    // Build the list of edits
    // first clear the rows
    //var table = $('#my-table').DataTable();
    //table.clear().draw();
    user.listEditComments.forEach((edit) => {
      createEditElement(edit, user.userName, user.avgToxicityScore);
    });
  });
}

// Creates a row containing an edit in the table.
function createEditElement(edit, userName, avgToxicityScore) {
  var table = $('#my-table').DataTable();
 
  table.row.add( [avgToxicityScore, userName,"www.fakeaticle.com",edit,"July 23, 2020"]).draw();
}


