// Get email from URL
const params = new URLSearchParams(window.location.search);
const email = params.get("email");

document.getElementById("email").innerText = email;

// Fetch inbox details
async function loadInbox() {
    const res = await fetch(`/api/inbox/${email}`);
    const data = await res.json();

    console.log(data);
}

loadInbox();