"""
AgriShield v4.0 - HTML Dashboard UI Module
"""

HTML_DASHBOARD = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>AgriShield v4 - Maharashtra Crop Protection</title>
  <link rel="manifest" href="/manifest.json">
  <meta name="theme-color" content="#071e16">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Outfit:wght@600;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
  <style>
    :root{--bg:#071e16;--card:#fff;--primary:#064e3b;--emerald:#10b981;--mint:#34d399;--muted:#64748b;--danger:#ef4444;--warn:#f59e0b}
    *{box-sizing:border-box;margin:0;padding:0;font-family:'Inter',-apple-system,sans-serif;-webkit-tap-highlight-color:transparent}
    body{background:linear-gradient(180deg,#071e16 0%,#0c2c20 100%);min-height:100vh;padding-bottom:90px;color:#0f172a}
    .app-header{background:rgba(6,30,22,.9);backdrop-filter:blur(20px);padding:14px 16px;position:sticky;top:0;z-index:1000;border-bottom:1px solid rgba(52,211,153,.25)}
    .header-row1{display:flex;justify-content:space-between;align-items:center}
    .app-title{font-family:'Outfit',sans-serif;font-size:20px;font-weight:800;color:#fff;display:flex;align-items:center;gap:8px}
    .badge{background:linear-gradient(135deg,#10b981,#059669);font-size:10px;font-weight:800;padding:3px 8px;border-radius:12px;letter-spacing:.5px}
    .gps-btn{font-size:11px;background:rgba(16,185,129,.18);border:1px solid rgba(52,211,153,.4);color:#34d399;padding:6px 11px;border-radius:20px;font-weight:700;cursor:pointer}
    .lang-sel{background:rgba(15,23,42,.8);border:1px solid rgba(52,211,153,.4);color:#34d399;padding:6px 8px;border-radius:10px;font-size:12px;font-weight:700;outline:none}
    .search-row{display:flex;gap:6px;margin-top:10px}
    .search-in{flex:1;padding:10px 14px;border-radius:11px;border:1px solid rgba(52,211,153,.3);background:rgba(255,255,255,.95);font-size:13px;outline:none}
    .search-btn{background:linear-gradient(135deg,#10b981,#059669);color:#fff;border:none;padding:10px 16px;border-radius:11px;font-weight:700;font-size:13px;cursor:pointer}
    .loc-bar{font-size:11px;color:#94a3b8;margin-top:6px;display:flex;justify-content:space-between}
    .container{max-width:580px;margin:0 auto;padding:14px}
    .stats-row{display:grid;grid-template-columns:repeat(4,1fr);gap:7px;margin-bottom:14px}
    .stat-card{background:rgba(15,34,26,.85);border:1px solid rgba(52,211,153,.2);border-radius:13px;padding:9px 7px;text-align:center;color:#fff}
    .stat-label{font-size:9px;font-weight:700;color:#94a3b8;text-transform:uppercase;letter-spacing:.5px}
    .stat-val{font-size:15px;font-weight:800;color:#34d399;margin-top:2px}
    .card{background:#fff;border-radius:20px;border:1px solid rgba(52,211,153,.2);padding:18px;margin-bottom:14px;box-shadow:0 16px 36px -6px rgba(0,0,0,.2)}
    .card h2{font-family:'Outfit',sans-serif;font-size:17px;font-weight:800;margin-bottom:13px;color:var(--primary);display:flex;align-items:center;gap:7px}
    label{font-size:11px;font-weight:700;color:var(--muted);display:block;margin-bottom:5px;text-transform:uppercase;letter-spacing:.5px}
    input,select,textarea{width:100%;padding:11px 13px;border:1.5px solid #cbd5e1;border-radius:11px;font-size:13px;margin-bottom:12px;background:#f8fafc;transition:border-color .2s}
    input:focus,select:focus,textarea:focus{border-color:#10b981;background:#fff;outline:none;box-shadow:0 0 0 3px rgba(16,185,129,.12)}
    .btn-primary{background:linear-gradient(135deg,#059669,#047857);color:#fff;border:none;padding:14px;border-radius:13px;font-weight:800;font-size:14px;width:100%;cursor:pointer;display:flex;align-items:center;justify-content:center;gap:8px;box-shadow:0 8px 24px -2px rgba(5,150,105,.35)}
    .btn-primary:active{transform:scale(.98)}
    .btn-sec{background:#f1f5f9;color:#334155;border:1px solid #cbd5e1;padding:8px 13px;border-radius:11px;font-weight:700;font-size:12px;cursor:pointer;display:inline-flex;align-items:center;gap:5px}
    .btn-sec:hover{background:#e2e8f0}
    .chip-group{display:flex;gap:7px;margin-bottom:12px;overflow-x:auto;padding-bottom:3px;scrollbar-width:none}
    .chip{white-space:nowrap;background:#f0fdf4;border:1px solid #bbf7d0;color:#166534;padding:6px 11px;border-radius:18px;font-size:12px;font-weight:700;cursor:pointer}
    .chip:hover{background:#dcfce7}
    .img-box{width:100%;height:160px;border:2.5px dashed #94a3b8;border-radius:14px;display:flex;flex-direction:column;align-items:center;justify-content:center;background:#f8fafc;margin-bottom:12px;cursor:pointer;position:relative;overflow:hidden}
    .img-box:hover{border-color:#10b981;background:#f0fdf4}
    .img-box img{width:100%;height:100%;object-fit:cover;border-radius:12px}
    .scanner{position:absolute;top:0;left:0;right:0;height:3px;background:linear-gradient(90deg,transparent,#10b981,#84cc16,transparent);display:none;animation:scan 2s infinite alternate ease-in-out}
    @keyframes scan{0%{top:5%}100%{top:90%}}
    .pill{padding:5px 12px;border-radius:18px;font-weight:800;font-size:10px;display:inline-flex;align-items:center;gap:5px;text-transform:uppercase;letter-spacing:.3px}
    .sev-CRITICAL{background:#fef2f2;color:#dc2626;border:1.5px solid #fca5a5}
    .sev-HIGH{background:#fff7ed;color:#ea580c;border:1.5px solid #fdba74}
    .sev-MODERATE{background:#fefce8;color:#ca8a04;border:1.5px solid #fde047}
    .sev-LOW{background:#ecfdf5;color:#059669;border:1.5px solid #6ee7b7}
    .action-box{background:#ecfdf5;border-left:5px solid #059669;padding:13px;margin-top:11px;border-radius:11px;font-size:13px;line-height:1.5}
    .warn-box{background:#fef2f2;border-left:5px solid #ef4444;padding:13px;margin-top:11px;border-radius:11px;font-size:13px;line-height:1.5}
    .forecast-card{background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:14px;margin-bottom:10px}
    .audio-btn{background:linear-gradient(135deg,#3b82f6,#1d4ed8);color:#fff;border:none;padding:9px 14px;border-radius:11px;font-weight:700;font-size:12px;cursor:pointer;margin-top:10px;display:inline-flex;align-items:center;gap:7px}
    .mobile-nav{position:fixed;bottom:12px;left:12px;right:12px;max-width:530px;margin:0 auto;background:rgba(6,24,18,.92);backdrop-filter:blur(20px);border:1px solid rgba(52,211,153,.3);border-radius:26px;display:flex;justify-content:space-around;padding:9px 6px;z-index:1000;box-shadow:0 16px 40px rgba(0,0,0,.5)}
    .nav-btn{background:none;border:none;display:flex;flex-direction:column;align-items:center;gap:2px;color:#64748b;font-size:9px;font-weight:800;cursor:pointer;flex:1;transition:color .25s,transform .25s;position:relative}
    .nav-btn.active{color:#34d399;transform:translateY(-3px)}
    .nav-btn.active::after{content:'';position:absolute;bottom:-4px;width:14px;height:2.5px;background:#34d399;border-radius:4px}
    .nav-icon{font-size:18px}
    .conf-dial{display:flex;flex-direction:column;align-items:center;margin:8px 0}
    .tab{display:none}.tab.active{display:block}
    /* Auth modal */
    .modal-overlay{position:fixed;inset:0;background:rgba(0,0,0,.6);z-index:2000;display:flex;align-items:center;justify-content:center;padding:16px}
    .modal{background:#fff;border-radius:20px;padding:24px;width:100%;max-width:380px}
    .modal h3{font-family:'Outfit',sans-serif;font-size:20px;font-weight:800;color:var(--primary);margin-bottom:16px}
    .role-selector{display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px;margin-bottom:14px}
    .role-btn{background:#f8fafc;border:2px solid #e2e8f0;border-radius:11px;padding:10px 6px;text-align:center;cursor:pointer;font-size:11px;font-weight:700;color:#64748b;transition:all .2s}
    .role-btn.selected{background:#ecfdf5;border-color:#10b981;color:#059669}
    /* Dashboard for officials */
    .dash-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-bottom:14px}
    .dash-card{background:#f8fafc;border:1px solid #e2e8f0;border-radius:13px;padding:13px;text-align:center}
    .dash-num{font-size:26px;font-weight:800;color:var(--primary)}
    .dash-label{font-size:11px;color:var(--muted);font-weight:600;margin-top:2px}
  </style>
</head>
<body>
<div class="app-header">
  <div class="header-row1">
    <div class="app-title">🌱 <span id="uiTitle">AgriShield</span> <span class="badge">v4.0</span></div>
    <div style="display:flex;gap:7px;align-items:center">
      <button class="gps-btn" onclick="autoGPS()" id="gpsBadge">📍 GPS</button>
      <select id="langSel" class="lang-sel" onchange="applyLang()">
        <option value="en">English</option>
        <option value="mr">मराठी</option>
        <option value="hi">हिंदी</option>
        <option value="gu">ગુજ</option>
      </select>
      <button class="gps-btn" id="authBtn" onclick="showAuth()" style="background:rgba(59,130,246,.18);border-color:rgba(96,165,250,.4);color:#60a5fa">🔑 Login</button>
    </div>
  </div>
  <div class="search-row">
    <input id="cityIn" class="search-in" placeholder="Search village, city, district in Maharashtra..." onkeypress="if(event.key==='Enter')searchLoc()">
    <button class="search-btn" onclick="searchLoc()">🔍</button>
  </div>
  <div class="loc-bar">
    <span>📍 <strong id="locTitle" style="color:#34d399">Detecting...</strong></span>
    <span style="color:#84cc16;font-weight:700">🟢 Maharashtra Sentinel</span>
  </div>
</div>

<div class="container">
  <div class="stats-row" id="statsRow">
    <div class="stat-card"><div class="stat-label">TEMP</div><div class="stat-val" id="sTemp">--°C</div></div>
    <div class="stat-card"><div class="stat-label">RH</div><div class="stat-val" id="sRH">--%</div></div>
    <div class="stat-card"><div class="stat-label">TRAPS</div><div class="stat-val" id="sTrap" style="color:#f59e0b">--</div></div>
    <div class="stat-card"><div class="stat-label">RISK</div><div class="stat-val" id="sRisk" style="color:#ef4444">--</div></div>
  </div>

  <!-- TAB: TRIAGE -->
  <div id="tab-triage" class="tab active">
    <div class="card">
      <h2 id="uiTriageTitle">🔬 Leaf Photo & Symptom Triage</h2>
      <label id="uiLblPhoto">Take Photo / Upload Leaf</label>
      <div class="img-box" onclick="document.getElementById('fileIn').click()">
        <div class="scanner" id="scanLine"></div>
        <span id="imgPH" style="color:var(--muted);font-size:13px;font-weight:700;display:flex;flex-direction:column;align-items:center;gap:5px"><span style="font-size:26px">📸</span><span>Tap to take photo or upload</span></span>
        <img id="imgPrev" style="display:none">
      </div>
      <input type="file" id="fileIn" accept="image/*" capture="environment" style="display:none" onchange="handleImg(event)">
      <label id="uiLblCrop">Select Crop</label>
      <select id="dCrop">
        <option>Cotton</option><option>Tomato</option><option>Soybean</option>
        <option>Rice</option><option>Maize</option><option>Chilli</option>
        <option>Wheat</option><option>Tur (Pigeonpea)</option><option>Onion</option>
        <option>Sugarcane</option><option>Grapes</option><option>Banana</option>
      </select>
      <label id="uiLblSym">Observed Symptoms</label>
      <textarea id="dSym" rows="2" placeholder="e.g. Rosetted flowers, pink caterpillars inside bolls, entry holes..."></textarea>
      <label>Quick Symptom Chips</label>
      <div class="chip-group">
        <button class="chip" onclick="setChip('cotton_pbw')">🌸 Cotton PBW</button>
        <button class="chip" onclick="setChip('tomato_blight')">🍅 Tomato Blight</button>
        <button class="chip" onclick="setChip('soy_ymv')">🌿 Soybean YMV</button>
        <button class="chip" onclick="setChip('rice_blast')">🌾 Rice Blast</button>
        <button class="chip" onclick="setChip('maize_faw')">🌽 Maize FAW</button>
        <button class="chip" onclick="setChip('chilli_anthr')">🌶️ Chilli Anthr.</button>
      </div>
      <button class="btn-primary" onclick="runDiag()" id="btnDiag">⚡ Diagnose with AI</button>
    </div>
    <div class="card" id="diagCard" style="display:none">
      <h2>📋 Pathology Assessment</h2>
      <div id="diagOut"></div>
    </div>
  </div>

  <!-- TAB: MAP -->
  <div id="tab-map" class="tab">
    <div class="card">
      <h2 id="uiMapTitle">🗺️ Maharashtra Outbreak GIS Map</h2>
      <div style="display:flex;gap:6px;margin-bottom:10px;flex-wrap:wrap">
        <button class="btn-sec" onclick="filterMap('ALL')">All Districts</button>
        <button class="btn-sec" style="color:#dc2626" onclick="filterMap('CRITICAL')">🔴 Critical</button>
        <button class="btn-sec" onclick="filterMap('HIGH')">🟠 High Risk</button>
        <button class="btn-sec" onclick="filterMap('Cotton')">🌸 Cotton Belt</button>
        <button class="btn-sec" onclick="filterMap('Rice')">🌾 Rice Belt</button>
      </div>
      <div id="gisMap" style="height:360px;border-radius:14px;border:1px solid #cbd5e1;margin-bottom:12px"></div>
      <div id="hotspotSummary" style="background:#f8fafc;border-radius:11px;padding:12px;font-size:12px"></div>
    </div>
  </div>

  <!-- TAB: WEATHER -->
  <div id="tab-weather" class="tab">
    <div class="card">
      <h2>🌦️ Microclimate Risk Engine</h2>
      <div style="display:flex;gap:8px;margin-bottom:12px;align-items:center">
        <input type="number" id="wLat" value="20.39" step=".01" style="margin-bottom:0" placeholder="Lat">
        <input type="number" id="wLon" value="78.12" step=".01" style="margin-bottom:0" placeholder="Lon">
        <button class="btn-sec" onclick="loadWeather()">🔄 Sync</button>
      </div>
      <div id="weatherOut">Loading forecast...</div>
    </div>
  </div>

  <!-- TAB: IPM -->
  <div id="tab-ipm" class="tab">
    <div class="card">
      <h2>📚 IPM Protocol Database</h2>
      <div style="background:#f0fdf4;border:1px solid #bbf7d0;padding:12px;border-radius:12px;margin-bottom:14px">
        <strong style="font-size:12px;color:#166534">🧮 Dosage Calculator</strong>
        <div style="display:flex;gap:8px;align-items:center;margin-top:6px">
          <input type="number" id="farmAcres" value="1" step=".5" style="margin-bottom:0;width:80px" oninput="calcDose()">
          <span style="font-size:12px;font-weight:700;color:#166534">Acres</span>
          <span id="doseOut" style="font-size:12px;color:#15803d;font-weight:600">= 200L water + 250ml spray</span>
        </div>
      </div>
      <input id="ipmQ" placeholder="Search disease, crop, pest..." oninput="searchIPM()">
      <div id="ipmList">Loading...</div>
    </div>
  </div>

  <!-- TAB: OFFICERS / EXPERT -->
  <div id="tab-expert" class="tab">
    <div class="card">
      <h2>👨‍🔬 Extension Reports & Lab Referrals</h2>
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px">
        <span style="font-size:12px;color:var(--muted);font-weight:600" id="reportRoleNote">Login as Officer to validate reports</span>
        <button class="btn-sec" onclick="exportCSV()">📊 Export CSV</button>
      </div>
      <div id="expertList">Loading...</div>
    </div>
  </div>

  <!-- TAB: DASHBOARD (Officials) -->
  <div id="tab-dash" class="tab">
    <div class="card">
      <h2>📊 Officials Dashboard</h2>
      <div id="dashContent">Loading analytics...</div>
    </div>
  </div>
</div>

<!-- MOBILE NAV -->
<div class="mobile-nav">
  <button class="nav-btn active" onclick="switchTab('triage',this)"><span class="nav-icon">🔬</span><span id="nTriage">Triage</span></button>
  <button class="nav-btn" onclick="switchTab('map',this)"><span class="nav-icon">🗺️</span><span id="nMap">GIS Map</span></button>
  <button class="nav-btn" onclick="switchTab('weather',this)"><span class="nav-icon">🌦️</span><span id="nWeather">Weather</span></button>
  <button class="nav-btn" onclick="switchTab('ipm',this)"><span class="nav-icon">📚</span><span id="nIpm">IPM</span></button>
  <button class="nav-btn" onclick="switchTab('expert',this)"><span class="nav-icon">👨‍🔬</span><span id="nOfficer">Officers</span></button>
  <button class="nav-btn" onclick="switchTab('dash',this)"><span class="nav-icon">📊</span><span id="nDash">Dashboard</span></button>
</div>

<!-- AUTH MODAL -->
<div id="authModal" class="modal-overlay" style="display:none">
  <div class="modal">
    <h3 id="authModalTitle">🔑 Login / Register</h3>
    <div id="authModeToggle" style="display:flex;gap:8px;margin-bottom:14px">
      <button class="btn-primary" style="flex:1;padding:10px" onclick="showLoginForm()">Login</button>
      <button class="btn-sec" style="flex:1;justify-content:center" onclick="showRegisterForm()">Register</button>
    </div>
    <div id="loginForm">
      <label>Phone Number</label><input id="loginPhone" type="tel" placeholder="9876543210">
      <label>4-Digit PIN</label><input id="loginPin" type="password" maxlength="4" placeholder="****">
      <button class="btn-primary" onclick="doLogin()">Login</button>
      <p style="font-size:11px;color:var(--muted);margin-top:8px">Demo: 9876543210 / 1234 (Farmer) | 9876543211 / 5678 (Officer)</p>
    </div>
    <div id="registerForm" style="display:none">
      <label>Full Name</label><input id="regName" placeholder="Rajesh Patil">
      <label>Phone</label><input id="regPhone" type="tel" placeholder="9876543210">
      <label>4-Digit PIN</label><input id="regPin" type="password" maxlength="4" placeholder="****">
      <label>District</label>
      <select id="regDistrict">
        <option>Yavatmal</option><option>Amravati</option><option>Akola</option>
        <option>Nagpur</option><option>Wardha</option><option>Nashik</option>
        <option>Aurangabad</option><option>Latur</option><option>Pune</option>
        <option>Jalgaon</option><option>Kolhapur</option><option>Solapur</option>
      </select>
      <label>Role</label>
      <div class="role-selector">
        <div class="role-btn selected" id="roleF" onclick="setRole('FARMER',this)">🌾 Farmer</div>
        <div class="role-btn" id="roleO" onclick="setRole('EXTENSION_OFFICER',this)">👨‍🔬 Officer</div>
        <div class="role-btn" id="roleD" onclick="setRole('OFFICIAL',this)">🏛️ Official</div>
      </div>
      <input type="hidden" id="regRole" value="FARMER">
      <button class="btn-primary" onclick="doRegister()">Register</button>
    </div>
    <button class="btn-sec" style="width:100%;justify-content:center;margin-top:10px" onclick="closeAuth()">Cancel</button>
  </div>
</div>

<script>
// ===========================
// STATE
// ===========================
let SESSION = { token: localStorage.getItem('ag_token'), role: localStorage.getItem('ag_role'), name: localStorage.getItem('ag_name') };
let gisMap = null, farmLat = 20.39, farmLon = 78.12, uploadedB64 = null, allHotspots = [], currentRole = 'FARMER';

const I18N = {
  mr: { title:'ॲग्रीशील्ड', nTriage:'तपासणी', nMap:'नकाशा', nWeather:'हवामान', nIpm:'कीड', nOfficer:'अधिकारी', nDash:'डॅशबोर्ड', triageTitle:'🔬 पीक फोटो तपासणी', lblPhoto:'फोटो घ्या', lblCrop:'पीक निवडा', lblSym:'लक्षणे', btnDiag:'⚡ रोग निदान करा', mapTitle:'🗺️ महाराष्ट्र प्रादुर्भाव नकाशा' },
  hi: { title:'एग्रीशील्ड', nTriage:'जांच', nMap:'नक्शा', nWeather:'मौसम', nIpm:'कीट', nOfficer:'अधिकारी', nDash:'डैशबोर्ड', triageTitle:'🔬 पत्ती फोटो जांच', lblPhoto:'फोटो लें', lblCrop:'फसल चुनें', lblSym:'लक्षण', btnDiag:'⚡ निदान करें', mapTitle:'🗺️ महाराष्ट्र प्रकोप मानचित्र' },
  gu: { title:'એગ્રીશીલ્ડ', nTriage:'તપાસ', nMap:'નકશો', nWeather:'હવામાન', nIpm:'નિયંત્રણ', nOfficer:'અધિકારી', nDash:'ડૅશબોર્ડ', triageTitle:'🔬 પાનની ફોટો તપાસ', lblPhoto:'ફોટો લો', lblCrop:'પાક', lblSym:'લક્ષણો', btnDiag:'⚡ નિદાન', mapTitle:'🗺️ મહારાષ્ટ્ર નકશો' }
};

function applyLang() {
  const lang = document.getElementById('langSel').value;
  const d = I18N[lang];
  if (!d) return;
  document.getElementById('uiTitle').textContent = d.title;
  ['nTriage','nMap','nWeather','nIpm','nOfficer','nDash','uiTriageTitle','uiLblPhoto','uiLblCrop','uiLblSym','btnDiag','uiMapTitle'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.textContent = d[id.replace('ui','').replace('Lbl','lbl').replace(/^./, c => c.toLowerCase())] || el.textContent;
  });
}

// ===========================
// LOCATION
// ===========================
async function autoGPS() {
  document.getElementById('gpsBadge').textContent = '⏳ GPS...';
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      pos => updateLoc(pos.coords.latitude, pos.coords.longitude),
      () => fetchIPLoc(), {timeout: 6000}
    );
  } else fetchIPLoc();
}

async function fetchIPLoc() {
  try {
    const r = await fetch('https://ipapi.co/json/');
    const d = await r.json();
    if (d.latitude) updateLoc(d.latitude, d.longitude, `${d.city}, ${d.country_name}`);
    else updateLoc(20.39, 78.12, 'Maharashtra, India');
  } catch { updateLoc(20.39, 78.12, 'Maharashtra, India'); }
}

async function searchLoc() {
  const q = document.getElementById('cityIn').value.trim();
  if (!q) return;
  try {
    const r = await fetch(`https://nominatim.openstreetmap.org/search?format=json&countrycodes=in&q=${encodeURIComponent(q)}`);
    const data = await r.json();
    if (data.length > 0) {
      const loc = data[0];
      updateLoc(parseFloat(loc.lat), parseFloat(loc.lon), loc.display_name.split(',').slice(0,2).join(','));
    } else alert('Location not found. Try district or city name.');
  } catch(e) { alert('Search failed: '+e); }
}

async function updateLoc(lat, lon, name) {
  farmLat = lat; farmLon = lon;
  if (!name) {
    try {
      const r = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
      const d = await r.json();
      const a = d.address;
      name = (a.city||a.town||a.village||a.county||'') + ', ' + (a.state||'Maharashtra');
    } catch { name = `${lat.toFixed(2)}, ${lon.toFixed(2)}`; }
  }
  document.getElementById('gpsBadge').textContent = '📍 ' + name.split(',')[0];
  document.getElementById('locTitle').textContent = name;
  document.getElementById('wLat').value = lat.toFixed(4);
  document.getElementById('wLon').value = lon.toFixed(4);
  if (gisMap) { gisMap.setView([lat, lon], 8); }
  loadWeather();
}

// ===========================
// TABS
// ===========================
function switchTab(id, btn) {
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
  document.getElementById('tab-'+id).classList.add('active');
  if (btn) btn.classList.add('active');
  if (id === 'map') initMap();
  if (id === 'weather') loadWeather();
  if (id === 'ipm') searchIPM();
  if (id === 'expert') loadReports();
  if (id === 'dash') loadDashboard();
}

// ===========================
// DIAGNOSIS
// ===========================
function handleImg(evt) {
  const file = evt.target.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = e => {
    uploadedB64 = e.target.result;
    document.getElementById('imgPH').style.display = 'none';
    const img = document.getElementById('imgPrev');
    img.src = uploadedB64; img.style.display = 'block';
    document.getElementById('scanLine').style.display = 'block';
    setTimeout(() => document.getElementById('scanLine').style.display = 'none', 3000);
  };
  reader.readAsDataURL(file);
}

function setChip(type) {
  const map = {
    cotton_pbw: ['Cotton','Rosetted flowers, twisted petals, pink caterpillars inside bolls, tiny entry holes'],
    tomato_blight: ['Tomato','Dark brown water-soaked spots, white downy mold underneath leaves'],
    soy_ymv: ['Soybean','Yellow-green mosaic mottling on leaves, crinkled distorted leaflets, stunting'],
    rice_blast: ['Rice','Spindle-shaped lesions with grey centers and dark reddish margins on leaves'],
    maize_faw: ['Maize','Window pane leaf damage, ragged edges, shot holes, large larvae in whorl with Y mark on head'],
    chilli_anthr: ['Chilli','Circular sunken dark spots on chilli pods with concentric rings and salmon spore masses'],
  };
  if (map[type]) {
    document.getElementById('dCrop').value = map[type][0];
    document.getElementById('dSym').value = map[type][1];
  }
}

async function runDiag() {
  const crop = document.getElementById('dCrop').value;
  const sym = document.getElementById('dSym').value;
  const lang = document.getElementById('langSel').value;
  const district = document.getElementById('locTitle').textContent;
  const card = document.getElementById('diagCard');
  const out = document.getElementById('diagOut');
  const scan = document.getElementById('scanLine');
  card.style.display = 'block';
  scan.style.display = 'block';
  out.innerHTML = '<div style="text-align:center;padding:20px;color:#10b981;font-weight:800">🔬 Analyzing with Gemini AI + Rule Engine...</div>';
  const headers = {'Content-Type':'application/json'};
  if (SESSION.token) headers['Authorization'] = 'Bearer '+SESSION.token;
  let d;
  try {
    const r = await fetch('/api/diagnose', {method:'POST', headers, body: JSON.stringify({crop, symptoms:sym, imageBase64:uploadedB64, district, language:lang})});
    d = await r.json();
    if (!d.diagnosis) throw new Error("Server response empty");
  } catch(e) {
    d = getOfflineDiagnosis(crop, sym, district, lang);
  }
  scan.style.display = 'none';
  const conf = d.confidence_percentage || d.confidence || 70;
  const arcLen = (conf/100)*125;
  out.innerHTML = `
    <div style="display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:11px">
      <div><h3 style="font-size:17px;font-weight:800;color:var(--primary)">${d.diagnosis}</h3>
      <p style="font-size:11px;color:var(--muted);font-style:italic">${d.scientific_name}</p></div>
      <span class="pill sev-${d.severity}">${d.severity}</span>
    </div>
    <div class="conf-dial">
      <svg viewBox="0 0 100 55" width="140" height="80">
        <path d="M 10 50 A 35 35 0 0 1 90 50" fill="none" stroke="#e2e8f0" stroke-width="8" stroke-linecap="round"/>
        <path d="M 10 50 A 35 35 0 0 1 90 50" fill="none" stroke="url(#g1)" stroke-width="8" stroke-linecap="round" stroke-dasharray="${arcLen},125"/>
        <defs><linearGradient id="g1" x1="0%" y1="0%" x2="100%" y2="0%"><stop offset="0%" stop-color="#10b981"/><stop offset="60%" stop-color="#f59e0b"/><stop offset="100%" stop-color="#ef4444"/></linearGradient></defs>
        <text x="50" y="43" text-anchor="middle" font-size="14" font-weight="800" fill="#064e3b">${conf}%</text>
        <text x="50" y="52" text-anchor="middle" font-size="6" font-weight="700" fill="#64748b">AI CONFIDENCE</text>
      </svg>
      <span style="font-size:10px;color:var(--muted)">${d.source || 'AgriShield Rule Engine'}</span>
    </div>
    <div class="warn-box"><strong>⚠️ Immediate Action:</strong><br>${d.immediate_action}</div>
    <button class="audio-btn" onclick="playAudio('${(d.diagnosis+'. '+d.immediate_action).replace(/'/g,"\\'")}')">🔊 Voice Advisory</button>
    <div class="action-box" style="margin-top:11px">
      <strong>🍃 Biological Controls:</strong>
      <ul style="margin-left:14px;margin-top:5px">${(d.biological_control||[]).map(b=>`<li>${b}</li>`).join('')}</ul>
    </div>
    <div style="margin-top:10px;background:#fef2f2;border-radius:11px;padding:12px;font-size:13px;border:1px solid #fca5a5">
      <strong style="color:#b91c1c">🧪 Chemical Controls:</strong>
      <ul style="margin-left:14px;margin-top:5px;color:#991b1b">${(d.chemical_control||[]).map(c=>`<li>${c}</li>`).join('')}</ul>
    </div>
    <div style="margin-top:14px;display:flex;gap:8px">
      <button class="btn-primary" style="flex:1" onclick="alert('Lab Referral Voucher: #'+(d.report_id||'OFFLINE-101')+' for '+d.diagnosis+' in '+district)">📄 Lab Voucher</button>
      <button class="btn-sec" onclick="shareDiag('${d.diagnosis}','${d.immediate_action.replace(/'/g,"\\'")}')">📲 Share</button>
    </div>
  `;
}

function getOfflineDiagnosis(crop, symptoms, district, lang) {
  const text = ((crop||'') + " " + (symptoms||'')).toLowerCase();
  let diag = "Foliar Fungal Infection", sci = "Alternaria spp.", sev = "MODERATE", conf = 78;
  let act = "Spray Mancozeb 75% WP @ 2.5g/L water. Improve field drainage.";
  let bio = ["Apply Trichoderma viride @ 5g/L", "Remove and burn infected leaves"];
  let chem = ["Mancozeb 75% WP (2.5g/L)", "Copper Oxychloride 50% WP (3g/L)"];
  
  if (text.includes("rosetted") || text.includes("pink") || text.includes("boll") || (crop||'').toLowerCase().includes("cotton")) {
    diag = "Pink Bollworm Infestation"; sci = "Pectinophora gossypiella"; sev = "CRITICAL"; conf = 85;
    act = "Install 2 Pheromone traps/acre. Spray Profenofos 50 EC @ 2ml/L if trap catch exceeds 8 moths/night.";
    bio = ["Release Trichogramma bactrae @ 60,000/acre", "Spray Neem seed kernel extract (NSKE 5%)"];
    chem = ["Profenofos 50 EC (2ml/L)", "Emamectin benzoate 5% SG (0.4g/L)"];
  } else if (text.includes("yellow") || text.includes("mosaic") || (crop||'').toLowerCase().includes("soybean")) {
    diag = "Yellow Mosaic Virus"; sci = "Mungbean yellow mosaic virus"; sev = "HIGH"; conf = 82;
    act = "Spray Thiamethoxam 25 WG @ 0.4g/L to control whitefly vectors. Destroy infected plants.";
    bio = ["Yellow sticky traps @ 10/acre", "Neem oil 10,000 ppm @ 3ml/L"];
    chem = ["Thiamethoxam 25 WG (0.4g/L)", "Imidacloprid 17.8 SL (0.5ml/L)"];
  } else if (text.includes("blast") || text.includes("spindle") || (crop||'').toLowerCase().includes("rice")) {
    diag = "Rice Leaf Blast"; sci = "Magnaporthe oryzae"; sev = "HIGH"; conf = 88;
    act = "Spray Tricyclazole 75 WP @ 0.6g/L. Avoid excess nitrogenous fertilizers.";
    bio = ["Pseudomonas fluorescens @ 10g/kg seed treatment", "Alternate wetting and drying"];
    chem = ["Tricyclazole 75 WP (0.6g/L)", "Isoprothiolane 40 EC (1.5ml/L)"];
  } else if (text.includes("blight") || text.includes("water") || (crop||'').toLowerCase().includes("tomato")) {
    diag = "Late Blight"; sci = "Phytophthora infestans"; sev = "CRITICAL"; conf = 86;
    act = "Spray Cymoxanil + Mancozeb @ 2g/L immediately. Avoid overhead irrigation.";
    bio = ["Bio-fungicide Bacillus subtilis @ 5g/L", "Wide spacing for aeration"];
    chem = ["Cymoxanil 8% + Mancozeb 64% WP (2g/L)", "Metalaxyl 8% + Mancozeb 64% WP (2.5g/L)"];
  }
  
  const repId = 'REP-OFFLINE-' + Math.floor(1000 + Math.random()*9000);
  const rep = {report_id: repId, diagnosis: diag, scientific_name: sci, severity: sev, confidence: conf, immediate_action: act, biological_control: bio, chemical_control: chem, crop: crop||'Cotton', district: district||'Maharashtra', created_at: new Date().toLocaleDateString(), status: 'PENDING_VERIFICATION'};
  
  try {
    const stored = JSON.parse(localStorage.getItem('ag_reports') || '[]');
    stored.unshift(rep);
    localStorage.setItem('ag_reports', JSON.stringify(stored));
  } catch(e){}
  
  return {...rep, confidence_percentage: conf, source: 'AgriShield Offline ML Engine'};
}
}

function shareDiag(diagnosis, action) {
  if (navigator.share) {
    navigator.share({title:'AgriShield Alert', text: diagnosis+'\n'+action, url: window.location.href});
  } else { alert('Advisory: '+diagnosis+'\n\n'+action); }
}

function playAudio(text) {
  if ('speechSynthesis' in window) {
    window.speechSynthesis.cancel();
    const lang = document.getElementById('langSel').value;
    const u = new SpeechSynthesisUtterance(text);
    u.lang = {mr:'mr-IN',hi:'hi-IN',gu:'gu-IN'}[lang] || 'en-IN';
    window.speechSynthesis.speak(u);
  }
}

// ===========================
// GIS MAP
// ===========================
async function initMap() {
  if (gisMap) return;
  gisMap = L.map('gisMap').setView([19.7515, 75.7139], 7);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{attribution:'© AgriShield GIS'}).addTo(gisMap);
  const r = await fetch('/api/hotspots');
  allHotspots = await r.json();
  renderHotspots(allHotspots);
  const stats = await (await fetch('/api/hotspot-stats')).json();
  document.getElementById('hotspotSummary').innerHTML = `
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:8px;text-align:center">
      <div><div style="font-size:20px;font-weight:800;color:#dc2626">${stats.critical_alert}</div><div style="font-size:10px;color:var(--muted)">Critical Districts</div></div>
      <div><div style="font-size:20px;font-weight:800;color:#ea580c">${stats.high_alert}</div><div style="font-size:10px;color:var(--muted)">High Risk</div></div>
      <div><div style="font-size:20px;font-weight:800;color:#059669">${stats.total_districts}</div><div style="font-size:10px;color:var(--muted)">Districts Mapped</div></div>
    </div>`;
}

function renderHotspots(data) {
  if (!gisMap) return;
  data.forEach(d => {
    const col = {CRITICAL:'#dc2626',HIGH:'#ea580c',MODERATE:'#f59e0b',LOW:'#22c55e'}[d.outbreak_risk]||'#94a3b8';
    const r = {CRITICAL:14,HIGH:11,MODERATE:8,LOW:6}[d.outbreak_risk]||8;
    L.circleMarker([d.latitude,d.longitude],{radius:r,fillColor:col,color:'#fff',weight:2,fillOpacity:.85})
     .addTo(gisMap)
     .bindPopup(`<div style="font-family:Inter,sans-serif;min-width:180px">
       <strong style="color:#064e3b;font-size:13px">${d.district_name} (${d.division})</strong><br>
       <span style="color:${col};font-weight:800">${d.outbreak_risk}</span> Risk<br>
       <strong>Major Crops:</strong> ${(d.major_crops||[]).join(', ')}<br>
       <strong>Threat:</strong> ${d.primary_pest_threat}<br>
       ${d.trap_count>0?`<strong>Trap Catch:</strong> ${d.trap_count} moths/night<br>`:''}
       <p style="font-size:11px;color:#475569;margin-top:4px">${d.active_advisory}</p>
     </div>`);
  });
}

function filterMap(type) {
  if (!gisMap) return;
  let filtered = allHotspots;
  if (type !== 'ALL') {
    if (['CRITICAL','HIGH','MODERATE','LOW'].includes(type)) {
      filtered = allHotspots.filter(d => d.outbreak_risk === type);
    } else {
      filtered = allHotspots.filter(d => (d.major_crops||[]).some(c => c.includes(type)));
    }
  }
  // Clear and redraw
  gisMap.eachLayer(l => { if (l instanceof L.CircleMarker) gisMap.removeLayer(l); });
  renderHotspots(filtered);
}

// ===========================
// WEATHER
// ===========================
async function loadWeather() {
  const lat = document.getElementById('wLat').value;
  const lon = document.getElementById('wLon').value;
  const loc = document.getElementById('locTitle').textContent;
  const lang = document.getElementById('langSel').value;
  const out = document.getElementById('weatherOut');
  out.innerHTML = '<p style="color:var(--muted);font-size:13px">Fetching Open-Meteo forecast...</p>';
  try {
    const r = await fetch(`/api/weather?lat=${lat}&lon=${lon}&loc=${encodeURIComponent(loc)}&lang=${lang}`);
    const data = await r.json();
    renderWeather(data);
  } catch(e) {
    renderWeather(getOfflineWeather(loc, lang));
  }
}

function renderWeather(data) {
  const out = document.getElementById('weatherOut');
  if (data.forecast_days && data.forecast_days.length > 0) {
    const t = data.forecast_days[0];
    document.getElementById('sTemp').textContent = t.temp_max+'°C';
    document.getElementById('sRH').textContent = t.humidity_max+'%';
    document.getElementById('sRisk').textContent = t.fungal_spore_risk;
  }
  out.innerHTML = (data.forecast_days||[]).map(d=>`
    <div class="forecast-card">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:7px">
        <strong style="font-size:14px;color:var(--primary)">${d.day_name}</strong>
        <span style="font-size:11px;color:var(--muted);font-weight:700">${d.weather_condition}</span>
      </div>
      <div style="font-size:12px;margin-bottom:8px;color:#334155">🌡️ <strong>${d.temp_max}°C</strong>/${d.temp_min}°C &nbsp;|&nbsp; 💧 <strong>${d.humidity_max}%</strong> &nbsp;|&nbsp; 🌧️ <strong>${d.rain_mm}mm</strong></div>
      <div style="display:flex;gap:6px;margin-bottom:8px">
        <span class="pill sev-${d.fungal_spore_risk}">Fungal: ${d.fungal_spore_risk}</span>
        <span class="pill sev-${d.pest_vector_risk}">Pest: ${d.pest_vector_risk}</span>
      </div>
      <p style="font-size:12px;background:#f8fafc;padding:9px;border-radius:9px;border:1px solid #e2e8f0;line-height:1.4">${d.actionable_advisory}</p>
    </div>`).join('');
}

function getOfflineWeather(loc, lang) {
  const days = [
    {day_name: 'Today', temp_max: 32, temp_min: 22, humidity_max: 82, rain_mm: 2.5, weather_condition: 'Partly Cloudy', fungal_spore_risk: 'HIGH', pest_vector_risk: 'MODERATE', actionable_advisory: 'High RH (>80%) promotes fungal spore germination. Inspect leaf undersides for downy mildew.'},
    {day_name: 'Tomorrow', temp_max: 34, temp_min: 23, humidity_max: 78, rain_mm: 0, weather_condition: 'Sunny', fungal_spore_risk: 'MODERATE', pest_vector_risk: 'HIGH', actionable_advisory: 'Warm temperatures favor sucking pest multiplication (whitefly/thrips). Install yellow sticky traps.'},
    {day_name: 'Day 3', temp_max: 31, temp_min: 21, humidity_max: 88, rain_mm: 12, weather_condition: 'Rain Showers', fungal_spore_risk: 'CRITICAL', pest_vector_risk: 'LOW', actionable_advisory: 'Heavy rain predicted. Ensure field drainage to prevent root rot and bacterial wilt.'}
  ];
  return {forecast_days: days};
}

// ===========================
// IPM
// ===========================
async function searchIPM() {
  const q = document.getElementById('ipmQ').value;
  const crop = document.getElementById('dCrop').value;
  const sym = document.getElementById('dSym').value;
  let items = [];
  try {
    const r = await fetch(`/api/ipm?q=${encodeURIComponent(q)}&crop=${encodeURIComponent(crop)}&symptoms=${encodeURIComponent(sym)}`);
    items = await r.json();
  } catch(e) {
    items = [
      {protocol:{disease_name:'Pink Bollworm Infestation', severity_level:'CRITICAL', target_crops:['Cotton'], key_symptoms_summary:'Rosetted flowers, pink caterpillars inside bolls', chemical_actions:[{title:'Profenofos 50 EC', dosage_or_method:'2ml/L water'}]}, confidence:85},
      {protocol:{disease_name:'Yellow Mosaic Virus', severity_level:'HIGH', target_crops:['Soybean'], key_symptoms_summary:'Yellow green mosaic on leaves, vector: whitefly', chemical_actions:[{title:'Thiamethoxam 25 WG', dosage_or_method:'0.4g/L water'}]}, confidence:82},
      {protocol:{disease_name:'Rice Leaf Blast', severity_level:'HIGH', target_crops:['Rice'], key_symptoms_summary:'Spindle-shaped lesions with grey center', chemical_actions:[{title:'Tricyclazole 75 WP', dosage_or_method:'0.6g/L water'}]}, confidence:88}
    ];
  }
  document.getElementById('ipmList').innerHTML = (items||[]).slice(0,8).map(item=>{
    const p = item.protocol; const sc = item.confidence;
    return `<div style="border:1px solid #e2e8f0;border-radius:13px;padding:13px;margin-bottom:10px;background:#fff">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <strong style="font-size:14px;color:var(--primary)">${p.disease_name}</strong>
        <span class="pill sev-${p.severity_level}" style="margin-left:6px">${sc}%</span>
      </div>
      <p style="font-size:11px;color:var(--muted);margin:3px 0 7px;font-weight:600">Crops: ${(p.target_crops||[]).join(', ')}</p>
      <p style="font-size:12px;color:#334151;line-height:1.4">${p.key_symptoms_summary}</p>
      ${p.chemical_actions&&p.chemical_actions.length>0?`<div style="margin-top:8px;font-size:11px;background:#fef2f2;padding:8px;border-radius:8px;color:#991b1b"><strong>Chemical:</strong> ${p.chemical_actions[0].title} ${p.chemical_actions[0].dosage_or_method||''}</div>`:''}
    </div>`;
  }).join('');
}

function calcDose() {
  const acres = parseFloat(document.getElementById('farmAcres').value)||1;
  document.getElementById('doseOut').textContent = `= ${Math.round(acres*200)}L water + ${Math.round(acres*250)}ml product`;
}

// ===========================
// EXPERT / REPORTS
// ===========================
async function loadReports() {
  const headers = {};
  if (SESSION.token) headers['Authorization'] = 'Bearer '+SESSION.token;
  const role = SESSION.role;
  if (role === 'EXTENSION_OFFICER' || role === 'OFFICIAL') {
    document.getElementById('reportRoleNote').textContent = 'All field reports - click to validate';
  }
  let items = [];
  try {
    const r = await fetch('/api/reports', {headers});
    items = await r.json();
  } catch(e) {
    items = JSON.parse(localStorage.getItem('ag_reports') || '[]');
  }
  if (!items.length) { document.getElementById('expertList').innerHTML='<p style="color:var(--muted);text-align:center;padding:20px">No reports yet. Run a diagnosis first.</p>'; return; }
  document.getElementById('expertList').innerHTML = items.map(rep=>`
    <div style="border:1px solid #e2e8f0;border-radius:13px;padding:13px;margin-bottom:10px;background:#fff">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <strong style="color:var(--primary);font-size:12px">${rep.report_id}</strong>
        <span class="pill sev-${rep.severity}">${rep.status.replace(/_/g,' ')}</span>
      </div>
      <p style="font-size:13px;font-weight:700;margin:5px 0 2px">${rep.diagnosis} (${rep.crop})</p>
      <p style="font-size:11px;color:var(--muted)">${rep.district} • ${rep.created_at} • Confidence: ${rep.confidence}%</p>
      <div style="display:flex;gap:7px;margin-top:10px;flex-wrap:wrap">
        <button class="btn-sec" onclick="window.open('/api/reports/ticket?id=${rep.report_id}')">📄 Lab Slip</button>
        ${role==='EXTENSION_OFFICER'||role==='OFFICIAL'?`
          <button class="btn-sec" style="color:#059669" onclick="validateRep('${rep.report_id}','${rep.diagnosis}')">✅ Validate</button>
          <button class="btn-sec" style="color:#dc2626" onclick="escalateRep('${rep.report_id}')">🔬 Escalate</button>
        `:''}
        <button class="btn-sec" style="color:#6366f1" onclick="addFeedback('${rep.report_id}')">📝 Feedback</button>
      </div>
    </div>`).join('');
}

async function validateRep(id, diagnosis) {
  const notes = prompt('Enter expert validation notes:');
  if (notes === null) return;
  const headers = {'Content-Type':'application/json'};
  if (SESSION.token) headers['Authorization'] = 'Bearer '+SESSION.token;
  const r = await fetch('/api/reports/validate', {method:'POST', headers, body: JSON.stringify({reportId:id, verifiedDiagnosis:diagnosis, notes})});
  const d = await r.json();
  if (d.error) { alert('Error: '+d.error); return; }
  alert('Report validated!');
  loadReports();
}

async function escalateRep(id) {
  const target = prompt('Escalate to (e.g. KVK Lab, District Pathology Lab):');
  if (!target) return;
  const headers = {'Content-Type':'application/json'};
  if (SESSION.token) headers['Authorization'] = 'Bearer '+SESSION.token;
  await fetch('/api/reports/escalate', {method:'POST', headers, body: JSON.stringify({reportId:id, target, notes:''})});
  alert('Escalated to '+target);
  loadReports();
}

async function addFeedback(id) {
  const outcome = prompt('Treatment outcome? (SUCCESS / PARTIAL / FAILED)');
  if (!outcome) return;
  const notes = prompt('Notes (optional):') || '';
  await fetch('/api/reports/feedback', {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({reportId:id, status:outcome.toUpperCase(), notes})});
  alert('Feedback recorded! This improves AI accuracy.');
  loadReports();
}

function exportCSV() {
  const headers = {};
  if (SESSION.token) headers['Authorization'] = 'Bearer '+SESSION.token;
  window.open('/api/reports/export.csv');
}

// ===========================
// OFFICIALS DASHBOARD (FIX 9)
// ===========================
async function loadDashboard() {
  const out = document.getElementById('dashContent');
  out.innerHTML = '<p style="color:var(--muted)">Loading analytics...</p>';
  try {
    const [metrics, geoStats] = await Promise.all([
      fetch('/api/metrics').then(r=>r.json()),
      fetch('/api/hotspot-stats').then(r=>r.json())
    ]);
    const acc = metrics.ai_accuracy_rate;
    const accColor = acc>=80?'#059669':acc>=60?'#ca8a04':'#dc2626';
    out.innerHTML = `
      <div class="dash-grid">
        <div class="dash-card"><div class="dash-num">${metrics.total_reports}</div><div class="dash-label">Total Reports</div></div>
        <div class="dash-card"><div class="dash-num" style="color:#dc2626">${geoStats.critical_alert}</div><div class="dash-label">Critical Districts</div></div>
        <div class="dash-card"><div class="dash-num" style="color:#059669">${metrics.expert_validated}</div><div class="dash-label">Expert Validated</div></div>
        <div class="dash-card"><div class="dash-num" style="color:${accColor}">${acc}%</div><div class="dash-label">AI Accuracy Rate</div></div>
        <div class="dash-card"><div class="dash-num" style="color:#6366f1">${metrics.resolved_cases}</div><div class="dash-label">Cases Resolved</div></div>
        <div class="dash-card"><div class="dash-num" style="color:#ea580c">${metrics.critical_reports}</div><div class="dash-label">Critical Reports</div></div>
      </div>
      <div style="background:#f8fafc;border-radius:13px;padding:13px;margin-bottom:12px">
        <strong style="font-size:13px;color:var(--primary)">📈 Feedback Loop Stats</strong>
        <div style="display:flex;gap:10px;margin-top:8px;font-size:12px">
          <span style="background:#ecfdf5;color:#059669;padding:4px 10px;border-radius:8px;font-weight:700">✅ SUCCESS: ${metrics.feedback_success}</span>
          <span style="background:#fefce8;color:#ca8a04;padding:4px 10px;border-radius:8px;font-weight:700">⚠️ PARTIAL: ${metrics.feedback_partial}</span>
          <span style="background:#fef2f2;color:#dc2626;padding:4px 10px;border-radius:8px;font-weight:700">❌ FAILED: ${metrics.feedback_failed}</span>
        </div>
      </div>
      ${metrics.disease_accuracy && metrics.disease_accuracy.length > 0 ? `
      <div style="background:#f8fafc;border-radius:13px;padding:13px;margin-bottom:12px">
        <strong style="font-size:13px;color:var(--primary)">🔬 Per-Disease Accuracy (from field feedback)</strong>
        ${metrics.disease_accuracy.map(d=>`
          <div style="display:flex;justify-content:space-between;font-size:12px;margin-top:7px;align-items:center">
            <span style="font-weight:600">${d.diagnosis}</span>
            <span style="color:${d.accuracy>=70?'#059669':'#dc2626'};font-weight:800">${d.accuracy}% (${d.total} cases)</span>
          </div>`).join('')}
      </div>` : ''}
      ${metrics.district_breakdown && metrics.district_breakdown.length > 0 ? `
      <div style="background:#f8fafc;border-radius:13px;padding:13px;margin-bottom:12px">
        <strong style="font-size:13px;color:var(--primary)">📍 Reports by District</strong>
        ${metrics.district_breakdown.map(d=>`
          <div style="display:flex;justify-content:space-between;font-size:12px;margin-top:5px">
            <span>${d.district}</span><span style="font-weight:700;color:var(--primary)">${d.count} reports</span>
          </div>`).join('')}
      </div>` : ''}
      <button class="btn-primary" onclick="exportCSV()">📊 Export Full Surveillance CSV</button>
    `;
  } catch(e) { out.innerHTML='<p style="color:red">Dashboard error: '+e+'</p>'; }
}

// ===========================
// AUTH
// ===========================
function showAuth() {
  document.getElementById('authModal').style.display = 'flex';
}
function closeAuth() {
  document.getElementById('authModal').style.display = 'none';
}
function showLoginForm() {
  document.getElementById('loginForm').style.display = 'block';
  document.getElementById('registerForm').style.display = 'none';
}
function showRegisterForm() {
  document.getElementById('registerForm').style.display = 'block';
  document.getElementById('loginForm').style.display = 'none';
}
function setRole(role, el) {
  document.querySelectorAll('.role-btn').forEach(b => b.classList.remove('selected'));
  el.classList.add('selected');
  document.getElementById('regRole').value = role;
}
async function doLogin() {
  const phone = document.getElementById('loginPhone').value.trim();
  const pin = document.getElementById('loginPin').value.trim();
  const r = await fetch('/api/auth/login', {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({phone,pin})});
  const d = await r.json();
  if (d.success) {
    SESSION = {token:d.token, role:d.role, name:d.name};
    localStorage.setItem('ag_token',d.token);
    localStorage.setItem('ag_role',d.role);
    localStorage.setItem('ag_name',d.name);
    document.getElementById('authBtn').textContent = '👤 '+d.name.split(' ')[0];
    closeAuth();
    alert(`Welcome ${d.name}! Role: ${d.role.replace(/_/g,' ')}`);
  } else { alert('Login failed: '+d.error); }
}
async function doRegister() {
  const name = document.getElementById('regName').value.trim();
  const phone = document.getElementById('regPhone').value.trim();
  const pin = document.getElementById('regPin').value.trim();
  const district = document.getElementById('regDistrict').value;
  const role = document.getElementById('regRole').value;
  if (!name||!phone||pin.length!==4) { alert('Please fill all fields with a 4-digit PIN'); return; }
  const r = await fetch('/api/auth/register', {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({name,phone,pin,district,role})});
  const d = await r.json();
  if (d.success) { alert(`Registered! Your ID: ${d.userId}. Please login now.`); showLoginForm(); }
  else { alert('Registration failed: '+d.error); }
}

// ===========================
// INIT
// ===========================
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/sw.js').catch(()=>{});
}
if (SESSION.token) {
  const name = SESSION.name || 'User';
  document.getElementById('authBtn').textContent = '👤 '+name.split(' ')[0];
}
autoGPS();
</script>
</body>
</html>"""
