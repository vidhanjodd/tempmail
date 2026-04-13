const params = new URLSearchParams(window.location.search);
        const email = params.get("email");

        if (!email) window.location.href = "/";

        document.getElementById("emailDisplay").textContent = email;

        // Copy button
        document.getElementById("copyBtn").addEventListener("click", () => {
            navigator.clipboard.writeText(email);
            const btn = document.getElementById("copyBtn");
            btn.classList.add("copied");
            btn.innerHTML = `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg> Copied!`;
            setTimeout(() => {
                btn.classList.remove("copied");
                btn.innerHTML = `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg> Copy`;
            }, 2000);
        });

        // Timer
        function startTimer() {
            const expiryKey = "inboxExpiry_" + email;
            let expiry = localStorage.getItem(expiryKey);
            if (!expiry) {
                expiry = Date.now() + 10 * 60 * 1000;
                localStorage.setItem(expiryKey, expiry);
            }

            const badge = document.getElementById("timerBadge");
            const timerText = document.getElementById("timerText");

            function update() {
                const remaining = Math.max(0, expiry - Date.now());
                const mins = Math.floor(remaining / 60000);
                const secs = Math.floor((remaining % 60000) / 1000);
                timerText.textContent = `${String(mins).padStart(2,'0')}:${String(secs).padStart(2,'0')}`;
                if (remaining < 120000) badge.classList.add("expiring");
                else badge.classList.remove("expiring");
                if (remaining === 0) timerText.textContent = "Expired";
            }
            update();
            setInterval(update, 1000);
        }
        startTimer();

        // Format time
        function formatTime(iso) {
            const d = new Date(iso);
            return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }

        // Load emails
        async function loadEmails(showSpinner = false) {
            const token = localStorage.getItem("inboxToken");
            if (!token) { window.location.href = "/"; return; }

            const refreshBtn = document.getElementById("refreshBtn");
            if (showSpinner) refreshBtn.classList.add("spinning");

            try {
                const res = await fetch(`/api/emails/${encodeURIComponent(email)}?token=${token}`);

                if (res.status === 410) {
                    document.getElementById("emailsContainer").innerHTML = `
                        <div class="expired-notice">
                            <strong>Inbox Expired</strong>
                            <p>This inbox has expired. Generate a new one to continue.</p>
                        </div>`;
                    return;
                }

                if (res.status === 403) { window.location.href = "/"; return; }

                const emails = await res.json();
                document.getElementById("countBadge").textContent = emails.length;

                const container = document.getElementById("emailsContainer");

                if (emails.length === 0) {
                    container.innerHTML = `
                        <div class="empty-state">
                            <div class="empty-icon">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                                    <polyline points="22,6 12,13 2,6"/>
                                </svg>
                            </div>
                            <strong>No emails yet</strong>
                            <p>Waiting for incoming messages...</p>
                        </div>`;
                    return;
                }

                // Only re-render if count changed
                if (container.dataset.count === String(emails.length)) return;
                container.dataset.count = emails.length;

                container.innerHTML = "";
                emails.forEach((e, i) => {
                    const initials = e.sender ? e.sender[0].toUpperCase() : "?";
                    const div = document.createElement("div");
                    div.className = "email-card";
                    div.style.animationDelay = `${i * 0.05}s`;

                    const subject = document.createElement("div");
                    subject.className = "email-card-top";

                    const subjectText = document.createElement("div");
                    subjectText.className = "email-subject";
                    subjectText.textContent = e.subject || "(no subject)";

                    const timeEl = document.createElement("div");
                    timeEl.className = "email-time";
                    timeEl.textContent = formatTime(e.receivedAt);

                    subject.append(subjectText, timeEl);

                    const sender = document.createElement("div");
                    sender.className = "email-sender";
                    sender.innerHTML = `<span class="sender-dot">${initials}</span>`;
                    const senderText = document.createElement("span");
                    senderText.textContent = e.sender || "unknown";
                    sender.appendChild(senderText);

                    const preview = document.createElement("div");
                    preview.className = "email-preview";
                    preview.textContent = (e.body || "").replace(/<[^>]*>/g, "").slice(0, 120);

                    const body = document.createElement("div");
                    body.className = "email-body";
                    body.innerHTML = DOMPurify.sanitize(e.body || "");

                    div.append(subject, sender, preview, body);

                    div.addEventListener("click", () => {
                        div.classList.toggle("expanded");
                    });

                    container.appendChild(div);
                });

            } catch (err) {
                console.error(err);
            } finally {
                refreshBtn.classList.remove("spinning");
            }
        }

        document.getElementById("refreshBtn").addEventListener("click", () => loadEmails(true));

        loadEmails();
        setInterval(loadEmails, 10000);
