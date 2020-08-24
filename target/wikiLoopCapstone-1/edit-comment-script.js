async function getDetailedEdit(id) {
    let response = await fetch("/retrieve?id=" + id);
    let detailedEdit = await response.json();
    let computedAttribute = JSON.parse(detailedEdit.toxicityObject);
    console.log(computedAttribute);
    
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
    score.innerText = "Score: " + computedAttribute.score + "%";
    scoreLabel.innerText = "Label: " + computedAttribute.label;
    scoreReason.innerText = "Reason: " + computedAttribute.reason;
    scoreIsExperimental.innerText = experimentalMessage(computedAttribute.scoreIsExperimental);
}

function experimentalMessage(boolAns) {
    if (boolAns == 'true') {
        return "This is an experimental score from the Perspective API."
    } else {
        return "This is not an experimental score."
    }
}

window.onload = function() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var id = url.searchParams.get("id");
    getDetailedEdit(id);
}