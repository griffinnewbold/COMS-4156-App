let data = {}

function goback()
{
    location.href = "dashboard?user_id=" + user_id;
}

function refresh_doc()
{
    // Update doc name, version, and preview
    $('#doc-name-label-text').html(data['title'] + ' (version ' + data['previousVersions'].length + ')');
    $('#preview-data').html(atob(data['fileString'].substring(1)));

    // TODO: update doc statistics
    $('#stats-data').html('TODO');

    // Update providers (id: share-select)
    let prov_html = '<option selected disabled>Choose provider</option>';
    let providers = data['userId'].split('/');
    for (let i = 0; i < providers.length; i++) {
        let disabled_str = (providers[i] == user_id ? 'disabled' : '');
        prov_html += '<option ' + disabled_str + ' value="' + providers[i] + '">' + providers[i] + '</option>'
    }
    $('#share-select').html(prov_html);

    // Update version picker
    let vers_html = '';
    for (let i = 0; i < data['previousVersions'].length - 1; i++) {
        let ver_str = (i + 1).toString();
        vers_html = '<option value="' + ver_str + '">' + ver_str + '</option>' + vers_html;
    }
    $('#version-select').html('<option selected disabled>Choose version</option>' + vers_html);
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
    // load the document data from the backend API, and use it to populate the page.
    console.log("Loading document " + doc_id + "...");

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

        // Get all the docs, and query them for the doc of interest.
        // is this efficient? no. is it fine for our purposes? yes.
        for (let i = 0; i < docs.length; i++) {
            if (doc_id != docs[i]['docId']) {
                continue;
            } else {
                data = docs[i];
            }
        }

        console.log(data);
        refresh_doc();
    };
    xhr.open("GET", "/retrieve-documents?user_id=" + user_id);
    xhr.send();
}

$(document).ready(() => {
    load_document();
});
