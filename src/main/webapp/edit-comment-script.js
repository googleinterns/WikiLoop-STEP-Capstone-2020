function getDetailedEdit() {
    fetch('/retrieve').then(response => response.json()).then((detailedEdit) => {
        const article = document.getElementById('article');
        const username = document.getElementById('username');
        const edit = document.getElementById('edit-comment');
        const date = document.getElementById('date');
        const score = document.getElementById('toxic-score');

        article.setAttribute("href", detailedEdit.parentArticle);
        article.innerText = "Article Link";
        username.innerText = detailedEdit.userName;
        edit.innerText = detailedEdit.comment;
        date.innerText = detailedEdit.date;
        score.innerText = detailedEdit.toxicityObject;
    });
}

