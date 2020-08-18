function getActions() {
  fetch('/actions') .then(response => response.json()).then((actions) => { 
    const actionList = document.getElementById('container');
    actions.forEach((status) => {
        console.log(status);
        console.log(actions);
        actionList.appendChild(createActionElem(status));

    })
  });
}

function createActionElem(status) {
  const commentElem = document.createElement('li');
  commentElem.className = 'status';

  const bodyElem = document.createElement('span');
  bodyElem.innerText = comment.body;

  commentElem.appendChild(bodyElem);
  return commentElem;
}