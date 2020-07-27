function getDetailedEdit() {
    fetch('/retrieve').then(response => response.json()).then((detailedEdit) => {
        const article = document.getElementById('article');
        const username = document.getElementById('username');
        const edit = document.getElementById('edit-comment');
        const date = document.getElementById('date');
        const score = document.getElementById('toxic-score');

        article.innerText = detailedEdit.parentArticle;
        username.innerText = detailedEdit.userName;
        edit.innerText = detailedEdit.editCommentText;
        date.innerText = detailedEdit.date;
        score.innerText = detailedEdit.toxicity;
    });
}

/* psuedo code
function changeStatus() {
    if remove is pressed {
        status = removed;
    } else if suggest ban is pressed {
        status = ban;
    } else if warn is pressed {
        status = warn;
    }
}
*/