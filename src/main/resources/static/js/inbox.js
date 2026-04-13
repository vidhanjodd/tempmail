const params = new URLSearchParams(window.location.search);
const email = params.get("email");

document.getElementById("email").innerText = email;

async function loadEmails() {
    try {
        const res = await fetch(`/api/emails/${email}`);
        const emails = await res.json();

        const container = document.getElementById("emails");
        container.innerHTML = "";

        if (emails.length === 0) {
            container.innerHTML = "<p>No emails yet</p>";
            return;
        }

        emails.forEach(e => {
            const div = document.createElement("div");
            div.classList.add("email-card");

            div.innerHTML = `
                <h4>${e.subject}</h4>
                <p><b>From:</b> ${e.sender}</p>
                <p>${e.body}</p>
                <small>${e.receivedAt}</small>
                <hr/>
            `;

            container.appendChild(div);
        });

    } catch (err) {
        console.error(err);
    }
}

loadEmails();