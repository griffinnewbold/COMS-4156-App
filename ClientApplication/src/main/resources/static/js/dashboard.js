let data = [];

function render_docs(filter_string)
{
    let doc_html = '';
    let rendered = 0;

    for (let i = 0; i < data.length; i++) {
        let name = data[i]["name"];
        let id = data[i]["id"];

        if (filter_string.length > 0) {
            if (!(name.toLowerCase().includes(filter_string.toLowerCase()))) {
                continue;
            }
        }

        let new_row = (rendered % 4 == 0);
        if (new_row) {
            if (rendered != 0) {
                doc_html += '</div>';
            }
            doc_html += '<div class="row">';
        }

        doc_html += '<div class="col-md-3">';
        doc_html += '<a href=document?user_id=' + user_id + '&doc_id=' + id + '>'
        doc_html += '<div class="doc-link"><br>'
        doc_html += '<div class="doc-image"></div><br>'
        doc_html += '<p>' + name + '</p>'
        doc_html += '</div></a></div>';

        rendered++;
    }

    $('#docs-container').html(doc_html);
}

function search()
{
    let filter_string = $('#searchbox').val();
    console.log("Searching for " + filter_string);
    render_docs(filter_string);
}

function fetch_docs()
{
    console.log("Loading documents...");
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState != 4) {
            return;
        }
        if (xhr.status != 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        let clean_data = [];
        const docs = JSON.parse(JSON.parse(xhr.responseText));

        for (let i = 0; i < docs.length; i++) {
            clean_data.push({"name": docs[i]['title'],
                             "id": docs[i]['docId'],
                             "contents": atob(docs[i]['fileString'].substring(1))});
        }
        console.log(clean_data);
        data = clean_data;
        render_docs("");
    };
    xhr.open("GET", "/retrieve-documents?user_id=" + user_id);
    xhr.send();
}

function logout()
{
    location.href = "/";
}

function upload()
{
    // Steps:
    // 1. make sure the user selected a file
    // 2. make sure the user gave a name
    // 3. try and update!

    const filepicker = document.getElementById('filepicker');
    if (filepicker.files.length == 0) {
        alert("You must select a document to upload.");
        return;
    }

    let name = $('#name').val();
    if (name.length == 0) {
        alert("You must provide a name for your document to upload.");
        return;
    }

    // TODO: once the endpoint to upload data is setup, call that from here.

    location.reload();
}

$(document).ready(() => {
    fetch_docs();
});
