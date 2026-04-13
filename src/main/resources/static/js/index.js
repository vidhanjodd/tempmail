document.getElementById("generateBtn").addEventListener("click", async function() {
            this.classList.add("loading");
            try {
                const res = await fetch("/api/inbox/create", { method: "POST" });
                if (!res.ok) throw new Error("Server error");
                const data = await res.json();
                localStorage.setItem("inboxToken", data.accessToken);
                window.location.href = `/inbox.html?email=${encodeURIComponent(data.emailAddress)}`;
            } catch (err) {
                alert("Failed to create inbox. Please try again.");
                this.classList.remove("loading");
            }
        });