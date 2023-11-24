function handleCheckboxes(checkboxId) {
    let checkboxes = document.getElementsByName('checkbox-group');
    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].id !== checkboxId) {
            checkboxes[i].checked = false;
        }
    }
}

function login()
{
    // Login logic goes here
}

function register()
{
    // Register logic goes here
}
