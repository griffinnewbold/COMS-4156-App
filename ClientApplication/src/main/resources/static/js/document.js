let data = {}

function goback()
{
    location.href = "dashboard?user_id=" + user_id;
}

function refresh_doc()
{
    // TODO: once the HTML of the page is finished, add code here to populate the page with data.
    // things to update:
    // - doc name and version (id: doc-name-label-text)
    // - doc preview (id: preview-data)
    // - doc statistics (id: stats-data)
    // - providers / patients dropdown (id: share-select)
    // - versions picker dropdown (id: version-select)
}

function download_version()
{
    let version_val = $('#version-select').val();
    if (version_val === null) {
        alert("You must pick a previous version number to download.");
        return;
    }
    console.log("Downloading version: " + version_val);

    // TODO
}

function download_latest()
{
    // TODO
}

function upload()
{
    const filepicker = document.getElementById('filepicker');
    if (filepicker.files.length == 0) {
        alert("You must select a file to upload.");
        return;
    }

    // TODO
}

function share()
{
    let share_val = $('#share-select').val();
    if (share_val === null) {
        alert("You must pick a patient or provider to share with.");
        return;
    }
    console.log("Sharing with: " + share_val);

    // TODO
}

function delete_doc()
{
    // TODO: delete the document

    goback();
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
