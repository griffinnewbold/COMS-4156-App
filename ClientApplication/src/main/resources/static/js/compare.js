let from_doc = {}
let to_doc = {}

function goback()
{
    location.href = "document?user_id=" + encodeURIComponent(user_id) + "&doc_id=" + encodeURIComponent(doc_id);
}

function fetch_diffs()
{
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState != 4) {
            return;
        }
        if (xhr.status != 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const data = JSON.parse(JSON.parse(xhr.responseText));
        $('#comparison-info-text').html(data);
    };
    xhr.open("GET", "/doc-diffs?user_id=" + encodeURIComponent(user_id)
        + "&first_doc_name=" + encodeURIComponent(from_doc['title'])
        + "&second_doc_name=" + encodeURIComponent(to_doc['title']));
    xhr.send();
}

function refresh_docs()
{
    console.log("Found both documents, generating page contents...");

    // Update doc names and versions
    $('#l-preview-title').html(from_doc['title'] + ' (v' + from_doc['previousVersions'].length + ')');
    $('#r-preview-title').html(to_doc['title'] + ' (v' + to_doc['previousVersions'].length + ')');

    // Update previews
    $('#l-preview-data').html(atob(from_doc['fileString'].substring(1)));
    $('#r-preview-data').html(atob(to_doc['fileString'].substring(1)));

    // Update comparison stats
    fetch_diffs();
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
