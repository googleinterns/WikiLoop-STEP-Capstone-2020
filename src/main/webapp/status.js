function getActions() {
  fetch('/actions') .then(response => response.json()).then((actions) => { 
    const actionList = document.getElementById('container');
    actions.forEach((a) => {
        actionList.appendChild(createActionListElem(a));
    })
  });
}

function createActionListElem(a) {
    const liElem = document.createElement('li');
    liElem.classname = 'w3-bar';

    const line = document.createElement('p');

    const user = document.createElement('a');
    user.setAttribute('id', 'user');
    user.innerText = a.user;

    const edit = document.createElement('a');
    edit.setAttribute('id', 'edit');
    edit.innerText = a.revisionId;

    const btn = document.createElement('a');
    btn.setAttribute('id', 'action');
    action.innerText = a.action;

    const time = document.createElement('a');
    time.setAttribute('id', 'time');
    const timeDif = System.currentTimeMillis() - a.time;
    time.innerText = timeDif;

    const statement = `${user}reviewed${edit}and says${action}about${time}ago.`;

    line.appendChild(statement);
    liElem.appendChild(line);

    return liElem;
}