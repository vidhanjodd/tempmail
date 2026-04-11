document.getElementById("generateBtn").addEventListener("click", async () => {
    const res = await fetch("/api/inbox/create", { method: "POST" });
    const data = await res.json();

    // Redirect to inbox page
    window.location.href = `/inbox.html?email=${data.emailAddress}`;
});