if ("serviceWorker" in navigator) {
    window.addEventListener("load", () => {
        navigator.serviceWorker.register("/service-worker.js").catch((error) => {
            console.error("Service worker registration failed:", error);
        });
    });
}

let deferredInstallPrompt = null;

function isStandaloneMode() {
    return window.matchMedia("(display-mode: standalone)").matches || window.navigator.standalone === true;
}

function showInstallUi() {
    const installButton = document.getElementById("installAppBtn");
    const installNote = document.getElementById("installNote");

    if (!installButton || isStandaloneMode()) {
        return;
    }

    installButton.hidden = false;
    if (installNote) {
        installNote.hidden = false;
    }
}

window.addEventListener("beforeinstallprompt", (event) => {
    event.preventDefault();
    deferredInstallPrompt = event;
    showInstallUi();
});

window.addEventListener("appinstalled", () => {
    deferredInstallPrompt = null;
    const installButton = document.getElementById("installAppBtn");
    const installNote = document.getElementById("installNote");
    if (installButton) {
        installButton.hidden = true;
    }
    if (installNote) {
        installNote.hidden = true;
    }
});

window.addEventListener("load", () => {
    const installButton = document.getElementById("installAppBtn");
    if (!installButton || isStandaloneMode()) {
        return;
    }

    const isIos = /iphone|ipad|ipod/i.test(window.navigator.userAgent);
    const isSafari = /^((?!chrome|android).)*safari/i.test(window.navigator.userAgent);

    if (isIos && isSafari) {
        showInstallUi();
    }

    installButton.addEventListener("click", async () => {
        if (deferredInstallPrompt) {
            deferredInstallPrompt.prompt();
            await deferredInstallPrompt.userChoice;
            deferredInstallPrompt = null;
            installButton.hidden = true;
            const installNote = document.getElementById("installNote");
            if (installNote) {
                installNote.hidden = true;
            }
            return;
        }

        if (isIos && isSafari) {
            window.alert("To install TMPBOX on iPhone or iPad, tap Share and then Add to Home Screen.");
        }
    });
});
