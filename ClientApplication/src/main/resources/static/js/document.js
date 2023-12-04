let data = {}

function goback()
{
    location.href = "dashboard";
}

function load_providers()
{
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const users = JSON.parse(JSON.parse(xhr.responseText));

        let prov_html = '<option selected disabled>Choose provider</option>';
        let providers = data['userId'].split('/');

        for (let i = 0; i < users.length; i++) {
            let user = users[i].substring(0, users[i].indexOf('@'));
            let disabled_str = (providers.includes(user) ? 'disabled' : '');
            prov_html += '<option ' + disabled_str + ' value="' + user + '">' + user + '</option>'
        }

        $('#share-select').html(prov_html);
    };
    xhr.open("GET", "/usernames");
    xhr.send();
}

function load_doc_stats()
{
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const stats_lines = JSON.parse(JSON.parse(xhr.responseText)).split('\n');

        let stats = '';
        for (let i = 1; i < stats_lines.length; i++) {
            stats += stats_lines[i];
            if (i !== stats_lines.length - 1) stats += '\n';
        }

        $('#stats-data').html(stats);
    };
    xhr.open("GET", "/retrieve-document-stats?user_id=" + encodeURIComponent(user_id)
             + '&doc_id=' + encodeURIComponent(data['title']));
    xhr.send();
}

function refresh_doc()
{
    // Update doc name, version, and preview
    $('#doc-name-label-text').html(data['title'] + ' (version ' + data['previousVersions'].length + ')');
    $('#preview-data').html(atob(data['fileString'].substring(1)));
    $('title').html('Documents - ' + data['title']);

    // Update providers
    load_providers();

    // Update document options
    load_documents_for_comparison();

    // Update version picker
    let vers_html = '';
    for (let i = 0; i < data['previousVersions'].length - 1; i++) {
        let ver_str = (i + 1).toString();
        vers_html = '<option value="' + ver_str + '">' + ver_str + '</option>' + vers_html;
    }
    $('#version-select').html('<option selected disabled>Choose version</option>' + vers_html);

    // Update doc statistics
    load_doc_stats();
}

function download_helper(content, filename)
{
    const a = document.createElement('a');
    const file = new Blob([content], {type: 'text/plain'});
    a.href = URL.createObjectURL(file);
    a.download = filename;
    a.click();
    URL.revokeObjectURL(a.href);
}

function download_version()
{
    let version_val = $('#version-select').val();
    if (version_val === null) {
        alert("You must pick a previous version number to download.");
        return;
    }
    console.log("Downloading version: " + version_val);

    let idx = Number(version_val);
    let old_data = data['previousVersions'][idx];

    let filename_parts = old_data['title'].split('.');
    let filename = filename_parts[0] + '_v' + idx;
    for (let i = 1; i < filename_parts.length; i++) {
        filename += ('.' + filename_parts[i]);
    }

    download_helper(atob(old_data['fileString'].substring(1)), filename);
}

function download_latest()
{
    download_helper(atob(data['fileString'].substring(1)), data['title']);
}

function upload()
{
    const filepicker = document.getElementById('filepicker');


    if (filepicker.files.length == 0) {
        alert("You must select a file to upload.");
        return;
    }

    // Get doc contents from filepicker
    const file = filepicker.files[0];
    const fileExtension = file.name.split('.').pop().toLowerCase();

    if (fileExtension !== 'txt') {
        alert("Your selection must be a txt file.")
        return;
    }

    if (file.size > 1000000) {
        alert("This file is too large. Please select a file smaller than 1000 KB.");
        return;
    }

    const reader = new FileReader();
    let contents = '';
    reader.onload = (e) => {
        contents = e.target.result;
        console.log("Contents: " + contents);

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== 4) {
                return;
            }
            if (xhr.status !== 200) {
                alert("Server error: " + xhr.statusText);
                return;
            }
            console.log("Upload response: " + xhr.responseText);

            location.reload();
        };
        xhr.open("POST",
            "/upload-document?user_id=" + encodeURIComponent(user_id)
                + "&doc_name=" + encodeURIComponent(data['title'])
                + "&contents=" + encodeURIComponent(contents));
        xhr.send();
    };
    reader.readAsText(file);
}

function share()
{
    let share_val = $('#share-select').val();
    if (share_val === null) {
        alert("You must pick a patient or provider to share with.");
        return;
    }
    console.log("Sharing with: " + share_val);

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        console.log("Share response: " + xhr.responseText);

        location.reload();
    };
    xhr.open("PATCH",
        "/share-document?user_id=" + encodeURIComponent(user_id)
            + '&doc_id=' + encodeURIComponent(data['title'])
            + '&new_user_id=' + encodeURIComponent(share_val));
    xhr.send();
}

function delete_doc()
{
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        console.log("Delete response: " + xhr.responseText);

        goback();
    };
    xhr.open("DELETE", "/delete-document?user_id=" + encodeURIComponent(user_id)
             + '&doc_name=' + encodeURIComponent(data['title']));
    xhr.send();
}

function load_document()
{
    // load the document data from the backend API, and use it to populate the page.
    console.log("Loading document " + doc_id + "...");

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const docs = JSON.parse(JSON.parse(xhr.responseText));

        // Get all the docs, and query them for the doc of interest.
        // is this efficient? no. is it fine for our purposes? yes.
        let found_doc = false;
        for (let i = 0; i < docs.length; i++) {
            if (doc_id !== docs[i]['docId']) {
                continue;
            } else {
                data = docs[i];
                found_doc = true;
                break;
            }
        }

        if (!found_doc) {
            goback();
        }
        console.log(data);
        refresh_doc();
    };
    xhr.open("GET", "/retrieve-documents?user_id=" + encodeURIComponent(user_id));
    xhr.send();
}

function compare_documents()
{
    let compare_val = $('#compare-select').val();
    if (compare_val === null) {
        alert("You must pick another document to compare against.");
        return;
    }
    console.log("Comparing with: " + compare_val);

    location.href = ("compare?from_doc_id=" + encodeURIComponent(doc_id)
                     + "&to_doc_name=" + encodeURIComponent(compare_val));
}

function load_documents_for_comparison()
{
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert("Server error: " + xhr.statusText);
            return;
        }
        const titles = JSON.parse(JSON.parse(xhr.responseText));

        let doc_html = '<option selected disabled>Choose document</option>';
        let title = data['title'];

        for (let i = 0; i < titles.length; i++) {
            let newTitle = titles[i];
            let disabled_str = (title === newTitle ? 'disabled' : '');
            doc_html += '<option ' + disabled_str + ' value="' + newTitle + '">' + newTitle
                + '</option>';
        }

        $('#compare-select').html(doc_html);
    };
    xhr.open("GET", "/docnames?user_id=" + encodeURIComponent(user_id));
    xhr.send();
}

$(document).ready(() => {
    load_document();
});
