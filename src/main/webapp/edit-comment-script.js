async function getDetailedEdit(id) {
    let response = await fetch("/retrieve?id=" + id);
    let detailedEdit = await response.json();
    let computedAttribute = JSON.parse(detailedEdit.toxicityObject);
    
    const article = document.getElementById('article');
    const username = document.getElementById('user');
    const edit = document.getElementById('edit-comment');
    const date = document.getElementById('date');
    const score = document.getElementById('score');
    const scoreLabel = document.getElementById('label');
    const scoreIsExperimental = document.getElementById('experimental');
    const revid = document.getElementById('rev-id');

    const link = "https://en.wikipedia.org/w/index.php?title=" + detailedEdit.parentArticle;
    article.setAttribute("href", link);
    article.innerText = detailedEdit.parentArticle;
    username.setAttribute("href", "https://en.wikipedia.org/wiki/User:" + detailedEdit.userName);
    username.innerText = detailedEdit.userName;
    edit.innerText = detailedEdit.comment;
    date.innerText = detailedEdit.date;
    score.innerText = computedAttribute.score + "%";
    scoreLabel.innerText = computedAttribute.label + ': ' + computedAttribute.reason;
    scoreIsExperimental.innerText = experimentalMessage(computedAttribute.experimental);
    const revidLink = "https://en.wikipedia.org/w/index.php?diff=" + id;
    revid.setAttribute("href", revidLink);
    revid.innerText = " rev." + id;
}

function experimentalMessage(boolAns) {
    if (boolAns === true) {
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