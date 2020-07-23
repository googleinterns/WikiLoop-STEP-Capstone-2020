
function getUser() {
  fetch('/user').then(response => response.json()).then((user) => {
    const userNameSection = document.getElementById('user-name');
    userNameSection.innerText = user.userName;
    const userPersonalInformationSection = document.getElementById('personal-information-box');
    userPersonalInformationSection.innerText = "Personal Information (Just an ID for now): "+ user.id;
    const avgToxicityScore = document.getElementById('toxicity-score-box');
    avgToxicityScore.innerText= "Average Toxicity Score: \t" + user.avgToxicityScore;


    const editsListElement = document.getElementById('edits-list');
    
    // Build the list of edits
    user.list_edit_comments.forEach((edit) => {
      editsListElement.appendChild(createEditElement(edit));
    });
  });
}

// Creates an <li> element containing an edit.
function createEditElement(edit) {
  const editElement = document.createElement('li');
  editElement.className = 'edit';
  editElement.innerText = edit;

  return editElement;
}
