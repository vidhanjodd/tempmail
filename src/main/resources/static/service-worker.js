const CACHE_VERSION = "tmpbox-v1";
const APP_SHELL_CACHE = `${CACHE_VERSION}-app-shell`;
const STATIC_ASSETS = [
    "/",
    "/index.html",
    "/inbox.html",
    "/offline.html",
    "/manifest.webmanifest",
    "/css/common.css",
    "/css/index.css",
    "/css/inbox.css",
    "/js/common.js",
    "/js/index.js",
    "/js/inbox.js",
    "/icons/favicon.svg",
    "/icons/apple-touch-icon.png",
    "/icons/icon-192.png",
    "/icons/icon-512.png",
    "/icons/icon-192.svg",
    "/icons/icon-512.svg",
    "/og-image.png"
];

self.addEventListener("install", (event) => {
    event.waitUntil(
        caches.open(APP_SHELL_CACHE).then((cache) => cache.addAll(STATIC_ASSETS))
    );
    self.skipWaiting();
});

self.addEventListener("activate", (event) => {
    event.waitUntil(
        caches.keys().then((keys) =>
            Promise.all(
                keys
                    .filter((key) => key !== APP_SHELL_CACHE)
                    .map((key) => caches.delete(key))
            )
        )
    );
    self.clients.claim();
});

self.addEventListener("fetch", (event) => {
    const { request } = event;
    if (request.method !== "GET") return;

    const url = new URL(request.url);

    if (url.origin !== self.location.origin) {
        return;
    }

    if (url.pathname.startsWith("/api/")) {
        event.respondWith(fetch(request));
        return;
    }

    const isNavigationRequest = request.mode === "navigate";

    if (isNavigationRequest) {
        event.respondWith(
            fetch(request).catch(async () => {
                const cachedResponse = await caches.match(request);
                return cachedResponse || caches.match("/offline.html");
            })
        );
        return;
    }

    event.respondWith(
        caches.match(request).then((cachedResponse) => {
            if (cachedResponse) {
                return cachedResponse;
            }

            return fetch(request).then((networkResponse) => {
                if (!networkResponse || networkResponse.status !== 200) {
                    return networkResponse;
                }

                const responseClone = networkResponse.clone();
                caches.open(APP_SHELL_CACHE).then((cache) => {
                    cache.put(request, responseClone);
                });
                return networkResponse;
            });
        })
    );
});
