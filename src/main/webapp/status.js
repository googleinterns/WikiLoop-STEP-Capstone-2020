function getActions() {

  fetch('/action') .then(response => response.json()).then((actions) => { 
    const actionList = document.getElementById('container');
    actions.forEach((a) => {
        actionList.appendChild(createActionListElem(a));
    })
  });
}

function createActionListElem(a) {
    var liElem = document.createElement('li');
    var pElem = document.createElement('p');

    var user = document.createElement('span');
    var id = document.createElement('a');
    var actionLine = document.createElement('span');
    var actionLabel = document.createElement('span');
    var time = document.createElement('span');

    user.innerHTML = a.user + " reviewed ";

    id.innerHTML = "edit:" + a.id;
    id.setAttribute('href', '/slider.html?id=' + a.id);

   var actionAndSays = document.createElement('span');
   actionAndSays.innerHTML = " and says ";

    if (a.action == "g") {
      actionLabel.innerHTML = "Looks good";
      actionLabel.setAttribute('class', 'badge-green');
    } else if (a.action == "ns") {
      actionLabel.innerHTML = "Not sure";
      actionLabel.setAttribute('class', 'badge-grey');
    } else if (a.action == "r") {
      actionLabel.innerHTML = "Should report";
      actionLabel.setAttribute('class', 'badge-red');
    }

    actionLine.appendChild(actionAndSays);
    actionLine.appendChild(actionLabel);

    var d = new Date();
    var currentTime = d.getTime();
    var timDiffInMilli = currentTime - a.time;

    var timDiffInSeconds = timDiffInMilli / 1000;
    timDiffInSeconds = Math.round(timDiffInSeconds);

    // what time to display
    if (timDiffInSeconds <= 1) {
      time.innerHTML = " just now.";
    } else if (timDiffInSeconds >= 1 && timDiffInSeconds < 60) {
      if (timDiffInSeconds == 1) {
        time.innerHTML = " " + timDiffInSeconds + " second ago.";
      } else {
        time.innerHTML = " " + timDiffInSeconds + " seconds ago.";
      }
    } else if (timDiffInSeconds >= 60 && timDiffInSeconds < 3600) {
      var mins = Math.round((timDiffInSeconds / 60));
      if (mins == 1) {
        time.innerHTML = " " + mins + " minute ago.";
      } else {
        time.innerHTML = " " + mins + " minutes ago.";
      }
    } else if (timDiffInSeconds >= 3600 && timDiffInSeconds < 86400) {
      var hrs = Math.round((timDiffInSeconds / 3600));
      if (hrs == 1) {
        time.innerHTML = " " + hrs + " hour ago.";
      } else {
        time.innerHTML = " " + hrs + " hours ago.";
      }
    } else if (timDiffInSeconds >= 86400) {
      var days = Math.round((timDiffInSeconds / 86400));
      if (days == 1) {
        time.innerHTML = " " + days + " day ago.";
      } else {
        time.innerHTML = " " + days + " days ago.";
      }
    }
    
    pElem.appendChild(user);
    pElem.appendChild(id);
    pElem.appendChild(actionLine);
    pElem.appendChild(time);

    liElem.appendChild(pElem);

    return liElem;
}