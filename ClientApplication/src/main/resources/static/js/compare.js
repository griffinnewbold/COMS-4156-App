let from_doc = {}
let to_doc = {}

function goback()
{
    location.href = "document?user_id=" + encodeURIComponent(user_id) + "&doc_id=" + encodeURIComponent(doc_id);
}

function refresh_docs()
{
    console.log("Found both documents, generating page contents...");

    // Update doc name, version, and preview
    // $('#preview-data').html(atob(data['fileString'].substring(1)));
    // $('title').html('Documents - ' + data['title']);

}

function load_documents()
{
    // load the document data from the backend API, and use it to populate the page.
    console.log("Comparing document " + doc_id + " against " + to_doc_name);

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState != 4) {
            return;
        }
        if (xhr.status != 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const docs = JSON.parse(JSON.parse(xhr.responseText));

        // Get all the docs, and query them for the docs of interest.
        // is this efficient? no. is it fine for our purposes? yes.
        let found_doc_1 = false;
        let found_doc_2 = false;
        for (let i = 0; i < docs.length; i++) {
            if ((doc_id != docs[i]['docId']) && (to_doc_name != docs[i]['title'])) {
                continue;
            } else if (doc_id == docs[i]['docId']) {
                from_doc = docs[i];
                found_doc_1 = true;
                if (found_doc_2) break;
            } else if (to_doc_name == docs[i]['title']) {
                to_doc = docs[i];
                found_doc_2 = true;
                if (found_doc_1) break;
            }
        }

        if (!found_doc_1 && !found_doc_2) {
            goback();
        }
        refresh_docs();
    };
    xhr.open("GET", "/retrieve-documents?user_id=" + encodeURIComponent(user_id));
    xhr.send();
}

$(document).ready(() => {
    load_documents();
});
