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
    var action = document.createElement('span');
    var time = document.createElement('span');

    user.innerHTML = a.user + " reviewed ";

    console.log(a.revisionId);
    id.innerHTML = a.revisionId;
    id.setAttribute('href', '/slider.html?id=' + a.revisionId);

    if (a.action == "g") {
      action.innerHTML = " and says Looks good ";
    } else if (a.action == "ns") {
      action.innerHTML = " and says Not sure ";
    } else if (a.action == "r") {
      action.innerHTML = " and says Should report ";
    }

    time.innerHTML = a.time + " ago."
    
    pElem.appendChild(user);
    pElem.appendChild(id);
    pElem.appendChild(action);
    pElem.appendChild(time);

    liElem.appendChild(pElem);

    return liElem;
}