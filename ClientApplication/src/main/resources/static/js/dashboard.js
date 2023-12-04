let data = [];

function render_docs(filter_string)
{
    let doc_html = '';
    let rendered = 0;

    for (let i = 0; i < data.length; i++) {
        let name = data[i]["name"];
        let id = data[i]["id"];
        let contents = data[i]["contents"]

        if (filter_string.length > 0) {
            if (!(name.toLowerCase().includes(filter_string)) &&
                !(contents.toLowerCase().includes(filter_string))) {
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
        doc_html += '<a href=document?doc_id=' + id + '>'
        doc_html += '<div class="doc-link"><br>'
        doc_html += '<div class="doc-image">'
        doc_html += '<p class="preview-data">' + contents + '</p>'
        doc_html += '</div><br>'
        doc_html += '<p>' + name + '</p>'
        doc_html += '</div></a></div>';

        rendered++;
    }

    $('#docs-container').html(doc_html);
}

function search()
{
    let filter_string = $('#searchbox').val().toLowerCase();
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
    xhr.open("GET", "/retrieve-documents?user_id=" + encodeURIComponent(user_id));
    xhr.send();
}

function logout()
{
    localStorage.removeItem("user-id");
    location.href = "/";
}

function upload()
{
    // Steps:
    // 1. make sure the user selected a file
    // 2. make sure the user gave a name
    // 3. try and upload!

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

    // Get doc contents from filepicker
    const file = filepicker.files[0];

    //checking if file size exceeds 1000 kd
    if (file.size > 1000000) { // size in bytes
        alert("File size exceeds the maximum limit of 1000 KB.");
        return;
    }

    const reader = new FileReader();
    let contents = '';
    reader.onload = (e) => {
        contents = e.target.result;

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState != 4) {
                return;
            }
            if (xhr.status != 200) {
                alert("Server error: " + xhr.statusText);
                return;
            }
            console.log("Upload response: " + xhr.responseText);

            location.href = 'dashboard';
        };
        xhr.open("POST", "/upload-document?user_id=" + encodeURIComponent(user_id)
            + "&doc_name=" + encodeURIComponent(name)
            + "&contents=" + encodeURIComponent(contents));
        xhr.send();
    };
    reader.readAsText(file);
}

$(document).ready(() => {
    fetch_docs();
});
