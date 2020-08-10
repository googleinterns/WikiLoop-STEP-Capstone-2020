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

        const link = "https://en.wikipedia.org/w/index.php?title=" + detailedEdit.parentArticle;
        article.setAttribute("href", link);
        article.innerText = detailedEdit.parentArticle;
        username.innerText = detailedEdit.userName;
        edit.innerText = detailedEdit.comment;
        date.innerText = detailedEdit.date;
        score.innerText = detailedEdit.toxicityObject.score;
        scoreLabel.innerText = detailedEdit.toxicityObject.label;
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
