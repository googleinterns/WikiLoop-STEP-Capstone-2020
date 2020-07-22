

function getDetailedEdit() {
    fetch('/retrieve').then(response => response.json()).then((detailedEdit) => {
        const article = document.getElementById('article');
        const edit = document.getElementById('edit-comment');
        const date = document.getElementById('date');
        const toxicity = document.getElementById('toxicity');

        article.appendChild(detailedEdit.parentArticle);
        edit.appendChild(detailedEdit.editCommentText);
        date.appendChild(detailedEdit.date);
        toxicity.appendChild(detailedEdit.toxicity);
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