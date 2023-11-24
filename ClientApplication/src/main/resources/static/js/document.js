let data = {}

function goback()
{
    location.href = "dashboard";
}

function refresh_doc()
{
    // TODO: once the HTML of the page is finished, add code here to populate the page with data.
    // things to update:
    // - doc name and version (id: doc-name-label-text)
    // - doc preview (id: preview-data)
    // - doc statistics (id: stats-data)
    // - versions picker dropdown
}

function load_document()
{
    // TODO: first, load the document data from the backend API.
    // Until this is implemented, use test data.

    let test_data = {

    };

    data = test_data;
    refresh_doc();
}

$(document).ready(() => {
    load_document();
});
