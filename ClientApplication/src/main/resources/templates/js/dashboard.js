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
        doc_html += '<a href=document.html?id=' + id + '>'
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

    // TODO: in practice, we will make a backend call and use that to render the docs.
    // however, since the backend isn't ready yet, for now we use some mock data.

    let test_data = [
        {"name": "Hello",
         "id": "id-A"},
        {"name": "World",
         "id": "id-B"},
        {"name": "Hello world",
         "id": "id-C"},
        {"name": "Test Document",
         "id": "id-D"},
        {"name": "Hello world 2",
         "id": "id-E"},
        {"name": "hello world 3",
         "id": "id-F"}
    ]

    data = test_data;
    render_docs("");
}

$(document).ready(() => {
    fetch_docs();
});
