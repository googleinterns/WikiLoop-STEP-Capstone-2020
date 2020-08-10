function getDetailedEdit() {
    fetch('/retrieve').then(response => response.json()).then((detailedEdit) => {
        const article = document.getElementById('article');
        const username = document.getElementById('username');
        const edit = document.getElementById('edit-comment');
        const date = document.getElementById('date');
        const score = document.getElementById('score');
        const scoreLabel = document.getElementById('label');
        const scoreReason = document.getElementById('reason');
        const scoreIsExperimental = document.getElementById('experimental');

        article.setAttribute("href", detailedEdit.parentArticle);
        article.innerText = "Article Link";
        username.innerText = detailedEdit.userName;
        edit.innerText = detailedEdit.comment;
        date.innerText = detailedEdit.date;
        scoreLabel.innerText = detailedEdit.toxicityObject.label;
        score.innerText = detailedEdit.toxicityObject.score;
        scoreReason.innerText = detailedEdit.toxicityObject.reason;
        scoreIsExperimental.innerText = experimentalMessage(detailedEdit.toxicityObject.scoreIsExperimental);
    });
}

function experimentalMessage(boolAns) {
    if (boolAns == 'true') {
        return "This is an experimental score from the Perspective API."
    } else {
        return "This is not an experimental score."
    }
}
