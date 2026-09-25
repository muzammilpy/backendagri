// AgriShield v4.0 - Full-featured Service Worker (FIX 10)
const CACHE_NAME = 'agrishield-v4';
const API_CACHE = 'agrishield-api-v4';

// Resources to cache on install for offline use
const PRECACHE_URLS = ['/'];

self.addEventListener('install', evt => {
  evt.waitUntil(
    caches.open(CACHE_NAME).then(cache =>
      cache.addAll(PRECACHE_URLS).catch(() => {})
    )
  );
  self.skipWaiting();
});

self.addEventListener('activate', evt => {
  // Clean up old cache versions
  evt.waitUntil(
    caches.keys().then(keys =>
      Promise.all(
        keys
          .filter(k => k !== CACHE_NAME && k !== API_CACHE)
          .map(k => caches.delete(k))
      )
    )
  );
  clients.claim();
});

self.addEventListener('fetch', evt => {
  const url = new URL(evt.request.url);

  // Never cache POST requests (diagnosis)
  if (evt.request.method !== 'GET') return;

  // For diagnosis API - network only (needs AI)
  if (url.pathname === '/api/diagnose') return;

  // For static assets and API GET - cache with network fallback
  evt.respondWith(
    caches.match(evt.request).then(cached => {
      const networkFetch = fetch(evt.request)
        .then(response => {
          if (response && response.status === 200) {
            const clone = response.clone();
            const targetCache = url.pathname.startsWith('/api/')
              ? API_CACHE
              : CACHE_NAME;
            caches.open(targetCache).then(c => c.put(evt.request, clone));
          }
          return response;
        })
        .catch(() => cached); // offline fallback to cache
      return cached || networkFetch;
    })
  );
});

// Background sync for reports submitted while offline
self.addEventListener('sync', evt => {
  if (evt.tag === 'sync-pending-reports') {
    evt.waitUntil(syncPendingReports());
  }
});

async function syncPendingReports() {
  try {
    const cache = await caches.open('agrishield-pending');
    const keys = await cache.keys();
    for (const key of keys) {
      const req = await cache.match(key);
      if (!req) continue;
      try {
        await fetch('/api/diagnose', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: await req.text()
        });
        await cache.delete(key);
      } catch (e) {
        // Will retry on next sync
      }
    }
  } catch (e) {}
}

// Push notification handler for outbreak alerts
self.addEventListener('push', evt => {
  if (!evt.data) return;
  const data = evt.data.json();
  evt.waitUntil(
    self.registration.showNotification(data.title || 'AgriShield Alert', {
      body: data.body || 'New crop disease alert in your area.',
      icon: 'https://cdn-icons-png.flaticon.com/512/628/628324.png',
      badge: 'https://cdn-icons-png.flaticon.com/512/628/628324.png',
      tag: 'agrishield-alert',
      data: { url: data.url || '/' }
    })
  );
});

self.addEventListener('notificationclick', evt => {
  evt.notification.close();
  evt.waitUntil(
    clients.openWindow(evt.notification.data.url || '/')
  );
});
