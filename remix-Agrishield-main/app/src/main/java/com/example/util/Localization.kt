package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.example.model.AppLanguage
import com.example.model.IPMInterventionTier
import com.example.model.ReportStatus
import com.example.model.ThreatSeverity

val LocalAppLanguage = compositionLocalOf { AppLanguage.ENGLISH }

object AppStrings {

    fun appName(lang: AppLanguage = AppLanguage.ENGLISH): String = "AgriShield"

    fun appTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Intelligent Crop Health & Outbreak Defense"
        AppLanguage.HINDI -> "बुद्धिमान फसल स्वास्थ्य और प्रकोप सुरक्षा"
        AppLanguage.MARATHI -> "स्मार्ट पीक आरोग्य आणि कीड प्रादुर्भाव सुरक्षा"
        AppLanguage.SPANISH -> "Salud de Cultivos Inteligente y Defensa Fitosanitaria"
        AppLanguage.SWAHILI -> "Afya ya Mazao na Kinga Dhidi ya Milipuko"
        AppLanguage.BENGALI -> "স্মার্ট ফসল স্বাস্থ্য ও রোগবালাই প্রতিরোধ"
        AppLanguage.FRENCH -> "Santé Végétale Intelligente et Défense Anti-Épidémie"
    }

    fun greeting(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Good Morning,"
        AppLanguage.HINDI -> "शुभ प्रभात,"
        AppLanguage.MARATHI -> "शुभ सकाळ,"
        AppLanguage.SPANISH -> "Buenos Días,"
        AppLanguage.SWAHILI -> "Habari za Asubuhi,"
        AppLanguage.BENGALI -> "সুপ্রভাত,"
        AppLanguage.FRENCH -> "Bonjour,"
    }

    fun defaultFarmerName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Farmer"
        AppLanguage.HINDI -> "किसान"
        AppLanguage.MARATHI -> "शेतकरी"
        AppLanguage.SPANISH -> "Agricultor"
        AppLanguage.SWAHILI -> "Mkulima"
        AppLanguage.BENGALI -> "কৃষক"
        AppLanguage.FRENCH -> "Agriculteur"
    }

    fun editName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Edit Name"
        AppLanguage.HINDI -> "नाम बदलें"
        AppLanguage.MARATHI -> "नाव बदला"
        AppLanguage.SPANISH -> "Editar Nombre"
        AppLanguage.SWAHILI -> "Hariri Jina"
        AppLanguage.BENGALI -> "নাম পরিবর্তন করুন"
        AppLanguage.FRENCH -> "Modifier le Nom"
    }

    fun farmerProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Farmer Profile"
        AppLanguage.HINDI -> "किसान प्रोफ़ाइल"
        AppLanguage.MARATHI -> "शेतकरी प्रोफाइल"
        AppLanguage.SPANISH -> "Perfil del Agricultor"
        AppLanguage.SWAHILI -> "Wasifu wa Mkulima"
        AppLanguage.BENGALI -> "কৃষক প্রোফাইল"
        AppLanguage.FRENCH -> "Profil Agriculteur"
    }

    fun fullNameFarmId(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Full Name / Farm ID"
        AppLanguage.HINDI -> "पूरा नाम / फार्म आईडी"
        AppLanguage.MARATHI -> "पूर्ण नाव / शेत आयडी"
        AppLanguage.SPANISH -> "Nombre Completo / ID Granja"
        AppLanguage.SWAHILI -> "Jina Kamili / Kitambulisho cha Shamba"
        AppLanguage.BENGALI -> "সম্পূর্ণ নাম / খামার আইডি"
        AppLanguage.FRENCH -> "Nom Complet / ID Ferme"
    }

    fun roleAndRegion(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Role & Agrarian Region"
        AppLanguage.HINDI -> "भूमिका और कृषि क्षेत्र"
        AppLanguage.MARATHI -> "भूमिका आणि कृषी क्षेत्र"
        AppLanguage.SPANISH -> "Rol y Región Agraria"
        AppLanguage.SWAHILI -> "Wajibu na Eneo la Kilimo"
        AppLanguage.BENGALI -> "ভূমিকা ও কৃষি অঞ্চল"
        AppLanguage.FRENCH -> "Rôle et Région Agricole"
    }

    fun phoneContact(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Phone / Contact"
        AppLanguage.HINDI -> "फोन / संपर्क"
        AppLanguage.MARATHI -> "फोन / संपर्क"
        AppLanguage.SPANISH -> "Teléfono / Contacto"
        AppLanguage.SWAHILI -> "Simu / Mawasiliano"
        AppLanguage.BENGALI -> "ফোন / যোগাযোগ"
        AppLanguage.FRENCH -> "Téléphone / Contact"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Save"
        AppLanguage.HINDI -> "सहेजें"
        AppLanguage.MARATHI -> "जतन करा"
        AppLanguage.BENGALI -> "সংরক্ষণ করুন"
        AppLanguage.SPANISH -> "Guardar"
        AppLanguage.FRENCH -> "Enregistrer"
        AppLanguage.SWAHILI -> "Hifadhi"
    }

    fun saveChanges(lang: AppLanguage): String = save(lang)

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.MARATHI -> "रद्द करा"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.SWAHILI -> "Ghairi"
        AppLanguage.BENGALI -> "বাতিল"
        AppLanguage.FRENCH -> "Annuler"
    }

    fun done(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Done"
        AppLanguage.HINDI -> "पूर्ण"
        AppLanguage.MARATHI -> "पूर्ण"
        AppLanguage.SPANISH -> "Listo"
        AppLanguage.SWAHILI -> "Imekamilika"
        AppLanguage.BENGALI -> "সম্পন্ন"
        AppLanguage.FRENCH -> "Terminé"
    }

    fun invalidPlantDiagnosisFailed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "⚠️ Diagnosis Failed: No plant or crop leaf detected. Please take a clear photo of an infected leaf or field specimen."
        AppLanguage.HINDI -> "⚠️ निदान विफल: कोई पौधा या फसल का पत्ता नहीं मिला। कृपया संक्रमित पत्ते या खेत के नमूने की स्पष्ट तस्वीर लें।"
        AppLanguage.MARATHI -> "⚠️ तपासणी अयशस्वी: कोणतीही वनस्पती किंवा पिकाचे पान आढळले नाही. कृपया बाधित पानाचा किंवा शेतातील नमुन्याचा स्पष्ट फोटो घ्या."
        AppLanguage.SPANISH -> "⚠️ Diagnóstico Fallido: No se detectó ninguna planta o cultivo. Tome una foto clara de una hoja infectada o muestra de campo."
        AppLanguage.SWAHILI -> "⚠️ Utambuzi Umeshindwa: Hakuna mmea au jani la zao lililogunduliwa. Tafadhali piga picha iliyo wazi ya jani lililoathirika au sampuli ya shambani."
        AppLanguage.BENGALI -> "⚠️ শনাক্তকরণ ব্যর্থ হয়েছে: কোনো উদ্ভিদ বা ফসলের পাতা শনাক্ত করা যায়নি। অনুগ্রহ করে আক্রান্ত পাতা বা মাঠের নমুনার একটি পরিষ্কার ছবি তুলুন।"
        AppLanguage.FRENCH -> "⚠️ Échec du Diagnostic : Aucune plante ou feuille de culture détectée. Veuillez prendre une photo claire d'une feuille infectée ou d'un spécimen de champ."
    }

    fun invalidPlantTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnosis Failed: Invalid Specimen"
        AppLanguage.HINDI -> "निदान विफल: अमान्य नमूना"
        AppLanguage.MARATHI -> "तपासणी अयशस्वी: अमान्य नमुना"
        AppLanguage.SPANISH -> "Diagnóstico Fallido: Muestra No Válida"
        AppLanguage.SWAHILI -> "Utambuzi Umeshindwa: Sampuli Isiyo Sahihi"
        AppLanguage.BENGALI -> "শনাক্তকরণ ব্যর্থ: অকার্যকর নমুনা"
        AppLanguage.FRENCH -> "Échec du Diagnostic : Spécimen Non Valide"
    }

    fun networkErrorTapToRetry(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Network Error, tap to retry"
        AppLanguage.HINDI -> "नेटवर्क त्रुटि, पुनः प्रयास करने के लिए टैप करें"
        AppLanguage.MARATHI -> "नेटवर्क त्रुटी, पुन्हा प्रयत्न करण्यासाठी टॅप करा"
        AppLanguage.SPANISH -> "Error de red, toca para reintentar"
        AppLanguage.SWAHILI -> "Hitilafu ya mtandao, gusa ili ujaribu tena"
        AppLanguage.BENGALI -> "নেটওয়ার্ক ত্রুটি, পুনরায় চেষ্টা করতে ট্যাপ করুন"
        AppLanguage.FRENCH -> "Erreur réseau, appuyez pour réessayer"
    }

    fun retakePhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Retake Photo"
        AppLanguage.HINDI -> "फोटो पुनः लें"
        AppLanguage.MARATHI -> "फोटो पुन्हा घ्या"
        AppLanguage.SPANISH -> "Tomar Foto de Nuevo"
        AppLanguage.SWAHILI -> "Piga Picha Tena"
        AppLanguage.BENGALI -> "পুনরায় ছবি তুলুন"
        AppLanguage.FRENCH -> "Reprendre la Photo"
    }

    fun defaultRejectionReason(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No crop or leaf detected. Please upload a clear photo of an infected crop leaf or agricultural specimen."
        AppLanguage.HINDI -> "कोई फसल या पत्ता नहीं मिला। कृपया संक्रमित फसल के पत्ते या कृषि नमूने की स्पष्ट तस्वीर अपलोड करें।"
        AppLanguage.MARATHI -> "कोणतेही पीक किंवा पान आढळले नाही. कृपया बाधित पिकाच्या पानाचा किंवा कृषी नमुन्याचा स्पष्ट फोटो अपलोड करा."
        AppLanguage.SPANISH -> "No se detectó ningún cultivo ni hoja. Suba una foto clara de una hoja infectada o muestra agrícola."
        AppLanguage.SWAHILI -> "Hakuna zao wala jani lililogunduliwa. Tafadhali pakia picha iliyo wazi ya jani la zao au sampuli ya kilimo."
        AppLanguage.BENGALI -> "কোনো ফসল বা পাতা শনাক্ত করা যায়নি। অনুগ্রহ করে আক্রান্ত ফসল বা কৃষিজ নমুনার একটি পরিষ্কার ছবি আপলোড করুন।"
        AppLanguage.FRENCH -> "Aucune culture ni feuille détectée. Veuillez téléverser une photo claire d'une feuille infectée ou d'un spécimen agricole."
    }

    fun selectAdvisoryLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Select Advisory Language"
        AppLanguage.HINDI -> "सलाहकार भाषा चुनें"
        AppLanguage.MARATHI -> "सल्लागार भाषा निवडा"
        AppLanguage.SPANISH -> "Seleccionar Idioma de Asesoría"
        AppLanguage.SWAHILI -> "Chagua Lugha ya Ushauri"
        AppLanguage.BENGALI -> "পরামর্শের ভাষা নির্বাচন করুন"
        AppLanguage.FRENCH -> "Sélectionner la Langue du Conseil"
    }

    // Navigation Destinations
    fun navDiagnosis(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnosis"
        AppLanguage.HINDI -> "निदान"
        AppLanguage.MARATHI -> "निदान"
        AppLanguage.SPANISH -> "Diagnóstico"
        AppLanguage.SWAHILI -> "Utambuzi"
        AppLanguage.BENGALI -> "রোগ নির্ণয়"
        AppLanguage.FRENCH -> "Diagnostic"
    }

    fun navIpmAdvisor(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM Advisor"
        AppLanguage.HINDI -> "आईपीएम सलाहकार"
        AppLanguage.MARATHI -> "आयपीएम सल्लागार"
        AppLanguage.SPANISH -> "Asesor MIP"
        AppLanguage.SWAHILI -> "Mshauri wa IPM"
        AppLanguage.BENGALI -> "আইপিএম পরামর্শ"
        AppLanguage.FRENCH -> "Conseil IPM"
    }

    fun navHotspots(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Hotspots"
        AppLanguage.HINDI -> "हॉटस्पॉट"
        AppLanguage.MARATHI -> "हॉटस्पॉट"
        AppLanguage.SPANISH -> "Zonas Críticas"
        AppLanguage.SWAHILI -> "Maeneo Hatari"
        AppLanguage.BENGALI -> "হটস্পট"
        AppLanguage.FRENCH -> "Zones Critiques"
    }

    fun navForecasting(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Forecast"
        AppLanguage.HINDI -> "पूर्वानुमान"
        AppLanguage.MARATHI -> "हवामान अंदाज"
        AppLanguage.SPANISH -> "Pronóstico"
        AppLanguage.SWAHILI -> "Utabiri"
        AppLanguage.BENGALI -> "পূর্বাভাস"
        AppLanguage.FRENCH -> "Prévisions"
    }

    fun navExtension(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Extension Lab"
        AppLanguage.HINDI -> "विस्तार लैब"
        AppLanguage.MARATHI -> "विस्तार प्रयोगशाळा"
        AppLanguage.SPANISH -> "Laboratorio"
        AppLanguage.SWAHILI -> "Maabara"
        AppLanguage.BENGALI -> "ল্যাব রিপোর্ট"
        AppLanguage.FRENCH -> "Laboratoire"
    }

    fun navDashboard(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Dashboard"
        AppLanguage.HINDI -> "डैशबोर्ड"
        AppLanguage.MARATHI -> "डॅशबोर्ड"
        AppLanguage.SPANISH -> "Panel"
        AppLanguage.SWAHILI -> "Dashibodi"
        AppLanguage.BENGALI -> "ড্যাশবোর্ড"
        AppLanguage.FRENCH -> "Tableau de Bord"
    }

    // Threat Severity Badges
    fun severity(severity: ThreatSeverity, lang: AppLanguage): String = when (severity) {
        ThreatSeverity.CRITICAL -> when (lang) {
            AppLanguage.ENGLISH -> "Severe Alert"
            AppLanguage.HINDI -> "गंभीर चेतावनी"
            AppLanguage.MARATHI -> "गंभीर इशारा"
            AppLanguage.SPANISH -> "Alerta Grave"
            AppLanguage.SWAHILI -> "Tahadhari Kali"
            AppLanguage.BENGALI -> "তীব্র সতর্কতা"
            AppLanguage.FRENCH -> "Alerte Sévère"
        }
        ThreatSeverity.HIGH -> when (lang) {
            AppLanguage.ENGLISH -> "High Outbreak"
            AppLanguage.HINDI -> "उच्च प्रकोप"
            AppLanguage.MARATHI -> "उच्च प्रादुर्भाव"
            AppLanguage.SPANISH -> "Brote Alto"
            AppLanguage.SWAHILI -> "Mlipuko Mkubwa"
            AppLanguage.BENGALI -> "উচ্চ প্রকোপ"
            AppLanguage.FRENCH -> "Forte Épidémie"
        }
        ThreatSeverity.MODERATE -> when (lang) {
            AppLanguage.ENGLISH -> "Moderate Risk"
            AppLanguage.HINDI -> "मध्यम जोखिम"
            AppLanguage.MARATHI -> "मध्यम धोका"
            AppLanguage.SPANISH -> "Riesgo Moderado"
            AppLanguage.SWAHILI -> "Hatari ya Wastani"
            AppLanguage.BENGALI -> "মাঝারি ঝুঁকি"
            AppLanguage.FRENCH -> "Risque Modéré"
        }
        ThreatSeverity.LOW -> when (lang) {
            AppLanguage.ENGLISH -> "Low Risk"
            AppLanguage.HINDI -> "कम जोखिम"
            AppLanguage.MARATHI -> "कमी धोका"
            AppLanguage.SPANISH -> "Riesgo Bajo"
            AppLanguage.SWAHILI -> "Hatari Ndogo"
            AppLanguage.BENGALI -> "স্বল্প ঝুঁকি"
            AppLanguage.FRENCH -> "Risque Faible"
        }
    }

    // Forecast & Weather
    fun forecastHeader(lang: AppLanguage): String = weatherForecastHeader(lang)
    fun forecastSubtitle(lang: AppLanguage): String = dynamicForecastEngineSubtitle(lang)

    fun weatherForecastHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "WEATHER-DRIVEN EPIDEMIC FORECAST"
        AppLanguage.HINDI -> "मौसम-संचालित महामारी पूर्वानुमान"
        AppLanguage.MARATHI -> "हवामान-आधारित रोग प्रादुर्भाव अंदाज"
        AppLanguage.SPANISH -> "PRONÓSTICO DE EPIDEMIAS POR CLIMA"
        AppLanguage.SWAHILI -> "UTABIRI WA MARADHI KULINGANA NA HEWA"
        AppLanguage.BENGALI -> "আবহাওয়া-ভিত্তিক মহামারী পূর্বাভাস"
        AppLanguage.FRENCH -> "PRÉVISIONS D'ÉPIDÉMIE MÉTÉO"
    }

    fun dynamicForecastEngineSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Dynamic 6-Day Disease & Pest Microclimate Engine"
        AppLanguage.HINDI -> "डायनामिक 6-दिवसीय रोग व कीट माइक्रोक्लाइमेट इंजन"
        AppLanguage.MARATHI -> "डायनॅमिक ६-दिवसीय रोग व कीटक सूक्ष्महवामान इंजिन"
        AppLanguage.SPANISH -> "Motor Microclimático de 6 Días de Plagas y Enfermedades"
        AppLanguage.SWAHILI -> "Injini ya Hali ya Hewa ya Siku 6 ya Magonjwa"
        AppLanguage.BENGALI -> "ডায়নামিক ৬-দিনের রোগ ও পোকা মাইক্রোক্লাইমেট ইঞ্জিন"
        AppLanguage.FRENCH -> "Moteur Microclimatique 6 Jours Maladies et Ravageurs"
    }

    fun liveTelemetryBanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "LIVE OPEN-METEO TELEMETRY"
        AppLanguage.HINDI -> "लाइव ओपन-मेटियो टेलीमेट्री"
        AppLanguage.MARATHI -> "लाइव्ह ओपन-मेटिओ टेलिमेट्री"
        AppLanguage.SPANISH -> "TELEMETRÍA EN VIVO OPEN-METEO"
        AppLanguage.SWAHILI -> "TAARIFA ZA MOJA KWA MOJA ZA OPEN-METEO"
        AppLanguage.BENGALI -> "লাইভ ওপেন-মেটিও টেলিমেট্রি"
        AppLanguage.FRENCH -> "TÉLÉMÉTRIE EN DIRECT OPEN-METEO"
    }

    fun synced(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Synced:"
        AppLanguage.HINDI -> "सिंक समय:"
        AppLanguage.MARATHI -> "सिंक वेळ:"
        AppLanguage.SPANISH -> "Sincronizado:"
        AppLanguage.SWAHILI -> "Ilisawazishwa:"
        AppLanguage.BENGALI -> "সিঙ্ক সময়:"
        AppLanguage.FRENCH -> "Synchronisé :"
    }

    fun refresh(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Refresh"
        AppLanguage.HINDI -> "ताज़ा करें"
        AppLanguage.MARATHI -> "रिफ्रेश करा"
        AppLanguage.SPANISH -> "Actualizar"
        AppLanguage.SWAHILI -> "Onyesha Upya"
        AppLanguage.BENGALI -> "রিফ্রেশ"
        AppLanguage.FRENCH -> "Actualiser"
    }

    fun syncing(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Syncing..."
        AppLanguage.HINDI -> "सिंक हो रहा है..."
        AppLanguage.MARATHI -> "सिंक होत आहे..."
        AppLanguage.SPANISH -> "Sincronizando..."
        AppLanguage.SWAHILI -> "Inasawazisha..."
        AppLanguage.BENGALI -> "সিঙ্ক হচ্ছে..."
        AppLanguage.FRENCH -> "Synchronisation..."
    }

    fun fungalSporeRisk(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Fungal Spore Risk"
        AppLanguage.HINDI -> "फंगल बीजाणु जोखिम"
        AppLanguage.MARATHI -> "बुरशीजन्य बीजाणू धोका"
        AppLanguage.SPANISH -> "Riesgo de Esporas Fúngicas"
        AppLanguage.SWAHILI -> "Hatari ya Viini vya Fangasi"
        AppLanguage.BENGALI -> "ছত্রাক স্পোর ঝুঁকি"
        AppLanguage.FRENCH -> "Risque Spores Fongiques"
    }

    fun pestVectorRisk(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Pest Vector Risk"
        AppLanguage.HINDI -> "कीट वाहक जोखिम"
        AppLanguage.MARATHI -> "कीटक प्रसारक धोका"
        AppLanguage.SPANISH -> "Riesgo de Vector de Plagas"
        AppLanguage.SWAHILI -> "Hatari ya Wadudu Wasambazaji"
        AppLanguage.BENGALI -> "কীটপতঙ্গ ভেক্টর ঝুঁকি"
        AppLanguage.FRENCH -> "Risque Vecteur Ravageurs"
    }

    fun daysHighSporeRisk(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Days High Spore Risk"
        AppLanguage.HINDI -> "$count दिन उच्च बीजाणु जोखिम"
        AppLanguage.MARATHI -> "$count दिवस उच्च बीजाणू धोका"
        AppLanguage.SPANISH -> "$count Días de Alto Riesgo"
        AppLanguage.SWAHILI -> "Siku $count za Hatari Kubwa"
        AppLanguage.BENGALI -> "$count দিন উচ্চ স্পোর ঝুঁকি"
        AppLanguage.FRENCH -> "$count Jours Risque Élevé"
    }

    fun optimalMicroclimate(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Optimal Microclimate"
        AppLanguage.HINDI -> "अनुकूल मौसम"
        AppLanguage.MARATHI -> "अनुकूल हवामान"
        AppLanguage.SPANISH -> "Microclima Óptimo"
        AppLanguage.SWAHILI -> "Hali ya Hewa Nzuri"
        AppLanguage.BENGALI -> "অনুকূল আবহাওয়া"
        AppLanguage.FRENCH -> "Microclimat Optimal"
    }

    fun pestTrapTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "PEST-TRAP LOGS & THRESHOLD ALARMS"
        AppLanguage.HINDI -> "कीट जाल लॉग और थ्रेशोल्ड अलर्ट"
        AppLanguage.MARATHI -> "कीटक सापळा नोंदी व मर्यादा इशारे"
        AppLanguage.SPANISH -> "REGISTRO DE TRAMPAS Y ALERTAS ETL"
        AppLanguage.SWAHILI -> "KUMBUKUMBU ZA MITEGO NA ETL"
        AppLanguage.BENGALI -> "কীট ফাঁদ লগ ও থ্রেশহোল্ড সতর্কতা"
        AppLanguage.FRENCH -> "PIÈGES À RAVAGEURS ET ALERTES SEUIL"
    }

    fun logTrapCatch(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Log Trap Catch"
        AppLanguage.HINDI -> "जाल कैच दर्ज करें"
        AppLanguage.MARATHI -> "सापळा नोंद जोडा"
        AppLanguage.SPANISH -> "Registrar Trampa"
        AppLanguage.SWAHILI -> "Rekodi Mtego"
        AppLanguage.BENGALI -> "ফাঁদ রেকর্ড করুন"
        AppLanguage.FRENCH -> "Enregistrer Piège"
    }

    fun iotSensorsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IOT CANOPY & SOIL SENSORS"
        AppLanguage.HINDI -> "आईओटी कैनोपी और मिट्टी सेंसर"
        AppLanguage.MARATHI -> "आयओटी कॅनॉपी व माती सेन्सर्स"
        AppLanguage.SPANISH -> "SENSORES IOT DE DOSEL Y SUELO"
        AppLanguage.SWAHILI -> "SENSA ZA IOT ZA MAJANI NA UDONGO"
        AppLanguage.BENGALI -> "আইওটি ক্যানোপি ও মাটি সেন্সর"
        AppLanguage.FRENCH -> "CAPTEURS IOT CANOPÉE ET SOL"
    }

    fun soilMoisture(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Soil Moisture"
        AppLanguage.HINDI -> "मिट्टी की नमी"
        AppLanguage.MARATHI -> "मातीतील ओलावा"
        AppLanguage.SPANISH -> "Humedad del Suelo"
        AppLanguage.SWAHILI -> "Unyevu wa Udongo"
        AppLanguage.BENGALI -> "মাটির আর্দ্রতা"
        AppLanguage.FRENCH -> "Humidité du Sol"
    }

    fun canopyTemp(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Canopy Temp"
        AppLanguage.HINDI -> "कैनोपी तापमान"
        AppLanguage.MARATHI -> "कॅनॉपी तापमान"
        AppLanguage.SPANISH -> "Temp. del Dosel"
        AppLanguage.SWAHILI -> "Joto la Majani"
        AppLanguage.BENGALI -> "ক্যানোপি তাপমাত্রা"
        AppLanguage.FRENCH -> "Temp. Canopée"
    }

    fun humidity(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Humidity"
        AppLanguage.HINDI -> "आर्द्रता"
        AppLanguage.MARATHI -> "हवेतील आर्द्रता"
        AppLanguage.SPANISH -> "Humedad"
        AppLanguage.SWAHILI -> "Unyevu wa Hewa"
        AppLanguage.BENGALI -> "আর্দ্রতা"
        AppLanguage.FRENCH -> "Humidité"
    }

    fun leafWetness(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Leaf Wetness"
        AppLanguage.HINDI -> "पत्तियों का गीलापन"
        AppLanguage.MARATHI -> "पानांवरील ओलावा"
        AppLanguage.SPANISH -> "Humedad Foliar"
        AppLanguage.SWAHILI -> "Unyevu wa Majani"
        AppLanguage.BENGALI -> "পাতার ভেজাভাব"
        AppLanguage.FRENCH -> "Humectation Foliaire"
    }

    fun exceedsEtl(count: Int, limit: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "EXCEEDS ETL ($count/$limit)"
        AppLanguage.HINDI -> "ईटीएल पार ($count/$limit)"
        AppLanguage.MARATHI -> "ईटीएल मर्यादा पार ($count/$limit)"
        AppLanguage.SPANISH -> "SUPERA ETL ($count/$limit)"
        AppLanguage.SWAHILI -> "IMEZIDI ETL ($count/$limit)"
        AppLanguage.BENGALI -> "ইটিএল অতিক্রম ($count/$limit)"
        AppLanguage.FRENCH -> "DÉPASSE ETL ($count/$limit)"
    }

    fun safeEtl(count: Int, limit: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "SAFE ($count/$limit)"
        AppLanguage.HINDI -> "सुरक्षित ($count/$limit)"
        AppLanguage.MARATHI -> "सुरक्षित ($count/$limit)"
        AppLanguage.SPANISH -> "SEGURO ($count/$limit)"
        AppLanguage.SWAHILI -> "SALAMA ($count/$limit)"
        AppLanguage.BENGALI -> "নিরাপদ ($count/$limit)"
        AppLanguage.FRENCH -> "SÉCURISÉ ($count/$limit)"
    }

    // Diagnosis & IPM
    fun cropHealthVisionEngine(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "AI Crop Health Vision Engine"
        AppLanguage.HINDI -> "एआई फसल स्वास्थ्य विज़न इंजन"
        AppLanguage.MARATHI -> "एआय पीक आरोग्य व्हिजन इंजिन"
        AppLanguage.SPANISH -> "Motor de Visión de Salud de Cultivos IA"
        AppLanguage.SWAHILI -> "Injini ya Maono ya Afya ya Mazao ya AI"
        AppLanguage.BENGALI -> "এআই শস্য স্বাস্থ্য ভিশন ইঞ্জিন"
        AppLanguage.FRENCH -> "Moteur de Vision de Santé des Cultures IA"
    }

    fun multimodalGemini(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Multimodal Gemini"
        AppLanguage.HINDI -> "मल्टीमॉडल जेमिनी"
        AppLanguage.MARATHI -> "मल्टीमॉडल जेमिनी"
        AppLanguage.SPANISH -> "Gemini Multimodal"
        AppLanguage.SWAHILI -> "Gemini Multimodal"
        AppLanguage.BENGALI -> "মাল্টিমোডাল জেমিনি"
        AppLanguage.FRENCH -> "Gemini Multimodal"
    }

    fun cropLeafDiagnosisTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Crop Leaf Pest & Disease AI Diagnosis"
        AppLanguage.HINDI -> "फसल पत्ती कीट और रोग एआई निदान"
        AppLanguage.MARATHI -> "पीक पान कीड व रोग एआय निदान"
        AppLanguage.SPANISH -> "Diagnóstico IA de Plagas y Enfermedades de Hojas"
        AppLanguage.SWAHILI -> "Utambuzi wa AI wa Wadudu na Magonjwa ya Majani"
        AppLanguage.BENGALI -> "ফসলের পাতার কীটপতঙ্গ ও রোগ এআই নির্ণয়"
        AppLanguage.FRENCH -> "Diagnostic IA des Ravageurs et Maladies Foliaires"
    }

    fun cropLeafDiagnosisDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Upload a leaf photo or choose a field specimen. Gemini AI inspects lesions, color degradation, and pest damage to deliver precision IPM advisories."
        AppLanguage.HINDI -> "पत्ती की तस्वीर अपलोड करें या खेत का नमूना चुनें। जेमिनी एआई सटीक आईपीएम सलाह देने के लिए घावों, रंग में बदलाव और कीटों के नुकसान की जांच करता है।"
        AppLanguage.MARATHI -> "पानाचा फोटो अपलोड करा किंवा शेतातील नमुना निवडा. जेमिनी एआय अचूक आयपीएम सल्ला देण्यासाठी पानांवरील डाग, रंगातील बदल आणि किडीच्या नुकसानीची तपासणी करते."
        AppLanguage.SPANISH -> "Sube una foto de la hoja o selecciona un espécimen de campo. Gemini AI inspecciona lesiones, pérdida de color y daños para ofrecer recomendaciones de MIP de precisión."
        AppLanguage.SWAHILI -> "Pakia picha ya jani au chagua sampuli ya shambani. Gemini AI inachunguza vidonda, mabadiliko ya rangi na uharibifu ili kutoa ushauri sahihi wa IPM."
        AppLanguage.BENGALI -> "পাতার ছবি আপলোড করুন বা মাঠের নমুনা বেছে নিন। জেমিনি এআই ক্ষত, রঙের পরিবর্তন এবং কীটপতঙ্গের ক্ষতি পরীক্ষা করে নির্ভুল আইপিএম পরামর্শ প্রদান করে।"
        AppLanguage.FRENCH -> "Téléchargez une photo de feuille ou choisissez un spécimen de terrain. Gemini AI inspecte les lésions, la décoloration et les dégâts pour fournir des conseils IPM précis."
    }

    fun aiScannerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "AI Plant Pathology Scanner"
        AppLanguage.HINDI -> "एआई पादप रोग स्कैनर"
        AppLanguage.MARATHI -> "एआय वनस्पती रोग स्कॅनर"
        AppLanguage.SPANISH -> "Escáner IA de Patología Vegetal"
        AppLanguage.SWAHILI -> "Kichunguzi cha Magonjwa ya Mimea cha AI"
        AppLanguage.BENGALI -> "এআই উদ্ভিদ রোগ স্ক্যানার"
        AppLanguage.FRENCH -> "Scanner IA de Pathologie Végétale"
    }

    fun takeLeafPhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Take Leaf Photo"
        AppLanguage.HINDI -> "पत्ती की फोटो लें"
        AppLanguage.MARATHI -> "पानाचा फोटो काढा"
        AppLanguage.SPANISH -> "Tomar Foto"
        AppLanguage.SWAHILI -> "Piga Picha"
        AppLanguage.BENGALI -> "পাতার ছবি তুলুন"
        AppLanguage.FRENCH -> "Prendre une Photo"
    }

    fun chooseFromGallery(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Choose from Gallery"
        AppLanguage.HINDI -> "गैलरी से चुनें"
        AppLanguage.MARATHI -> "गॅलरीतून निवडा"
        AppLanguage.SPANISH -> "Elegir de Galería"
        AppLanguage.SWAHILI -> "Chagua kwenye Matunzio"
        AppLanguage.BENGALI -> "গ্যালারি থেকে নির্বাচন করুন"
        AppLanguage.FRENCH -> "Choisir dans la Galerie"
    }

    fun diagnoseDisease(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnose Plant Disease"
        AppLanguage.HINDI -> "रोग का निदान करें"
        AppLanguage.MARATHI -> "रोगाचे निदान करा"
        AppLanguage.SPANISH -> "Diagnosticar Enfermedad"
        AppLanguage.SWAHILI -> "Chunguza Ugonjwa"
        AppLanguage.BENGALI -> "রোগ নির্ণয় করুন"
        AppLanguage.FRENCH -> "Diagnostiquer la Maladie"
    }

    fun analyzingSymptoms(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Analyzing Leaf Symptoms..."
        AppLanguage.HINDI -> "पत्ती के लक्षणों का विश्लेषण हो रहा है..."
        AppLanguage.MARATHI -> "पानांच्या लक्षणांचे विश्लेषण सुरू आहे..."
        AppLanguage.SPANISH -> "Analizando Síntomas de la Hoja..."
        AppLanguage.SWAHILI -> "Inachambua Dalili za Majani..."
        AppLanguage.BENGALI -> "পাতার লক্ষণ বিশ্লেষণ করা হচ্ছে..."
        AppLanguage.FRENCH -> "Analyse des Symptômes Foliaires..."
    }

    fun recommendedIpmPrescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Recommended IPM Prescription"
        AppLanguage.HINDI -> "अनुशंसित आईपीएम नुस्खा"
        AppLanguage.MARATHI -> "शिफारस केलेला आयपीएम सल्ला"
        AppLanguage.SPANISH -> "Prescripción Recomendada de MIP"
        AppLanguage.SWAHILI -> "Dawa ya IPM Iliyopendekezwa"
        AppLanguage.BENGALI -> "প্রস্তাবিত আইপিএম পরামর্শ"
        AppLanguage.FRENCH -> "Prescription IPM Recommandée"
    }

    fun biologicalControl(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Biological Control"
        AppLanguage.HINDI -> "जैविक नियंत्रण"
        AppLanguage.MARATHI -> "जैविक नियंत्रण"
        AppLanguage.SPANISH -> "Control Biológico"
        AppLanguage.SWAHILI -> "Udhibiti wa Kibiolojia"
        AppLanguage.BENGALI -> "জৈবিক নিয়ন্ত্রণ"
        AppLanguage.FRENCH -> "Lutte Biologique"
    }

    fun culturalPractices(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Cultural Practices"
        AppLanguage.HINDI -> "कृषि पद्धतियां"
        AppLanguage.MARATHI -> "मशागतीय पद्धती"
        AppLanguage.SPANISH -> "Prácticas Culturales"
        AppLanguage.SWAHILI -> "Mbinu za Kilimo"
        AppLanguage.BENGALI -> "কৃষি অনুশীলন"
        AppLanguage.FRENCH -> "Pratiques Culturales"
    }

    fun chemicalAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Targeted Chemical Action"
        AppLanguage.HINDI -> "लक्षित रासायनिक उपचार"
        AppLanguage.MARATHI -> "लक्षित रासायनिक उपाय"
        AppLanguage.SPANISH -> "Acción Química Dirigida"
        AppLanguage.SWAHILI -> "Hatua ya Kemikali"
        AppLanguage.BENGALI -> "নির্দিষ্ট রাসায়নিক ব্যবস্থা"
        AppLanguage.FRENCH -> "Action Chimique Ciblée"
    }

    fun safeDosage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Safe Dosage:"
        AppLanguage.HINDI -> "सुरक्षित खुराक:"
        AppLanguage.MARATHI -> "सुरक्षित प्रमाण:"
        AppLanguage.SPANISH -> "Dosis Segura:"
        AppLanguage.SWAHILI -> "Kipimo Salama:"
        AppLanguage.BENGALI -> "নিরাপদ মাত্রা:"
        AppLanguage.FRENCH -> "Dosage Sécurisé :"
    }

    fun preHarvestInterval(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Pre-Harvest Interval (PHI):"
        AppLanguage.HINDI -> "कटाई पूर्व अंतराल (PHI):"
        AppLanguage.MARATHI -> "काढणीपूर्व कालावधी (PHI):"
        AppLanguage.SPANISH -> "Intervalo de Seguridad (PHI):"
        AppLanguage.SWAHILI -> "Muda Kabla ya Kuvuna (PHI):"
        AppLanguage.BENGALI -> "ফসল তোলার পূর্ব সময় (PHI):"
        AppLanguage.FRENCH -> "Délai Avant Récolte (DAR) :"
    }

    fun safetyPpe(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Safety & PPE Instructions:"
        AppLanguage.HINDI -> "सुरक्षा और पीपीई निर्देश:"
        AppLanguage.MARATHI -> "सुरक्षा व पीपीई सूचना:"
        AppLanguage.SPANISH -> "Instrucciones de Seguridad y EPP:"
        AppLanguage.SWAHILI -> "Maagizo ya Usalama na Kinga:"
        AppLanguage.BENGALI -> "নিরাপত্তা ও পিপিই নির্দেশাবলী:"
        AppLanguage.FRENCH -> "Consignes de Sécurité et EPI :"
    }

    fun listenAudioAdvisory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Listen Audio Advisory"
        AppLanguage.HINDI -> "ऑडियो सलाह सुनें"
        AppLanguage.MARATHI -> "ऑडिओ सल्ला ऐका"
        AppLanguage.SPANISH -> "Escuchar Asesoría en Audio"
        AppLanguage.SWAHILI -> "Sikiliza Ushauri wa Sauti"
        AppLanguage.BENGALI -> "অডিও পরামর্শ শুনুন"
        AppLanguage.FRENCH -> "Écouter le Conseil Audio"
    }

    fun stopAudio(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Stop Audio"
        AppLanguage.HINDI -> "ऑडियो बंद करें"
        AppLanguage.MARATHI -> "ऑडिओ थांबवा"
        AppLanguage.SPANISH -> "Detener Audio"
        AppLanguage.SWAHILI -> "Simamisha Sauti"
        AppLanguage.BENGALI -> "অডিও বন্ধ করুন"
        AppLanguage.FRENCH -> "Arrêter l'Audio"
    }

    // Additional Localization Helpers
    fun ipmAdvisorTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM Advisor"
        AppLanguage.HINDI -> "आईपीएम सलाहकार"
        AppLanguage.MARATHI -> "आयपीएम सल्लागार"
        AppLanguage.SPANISH -> "Asesor MIP"
        AppLanguage.SWAHILI -> "Mshauri wa IPM"
        AppLanguage.BENGALI -> "আইপিএম পরামর্শদাতা"
        AppLanguage.FRENCH -> "Conseil IPM"
    }

    fun ipmAdvisorSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Integrated Pest Management Decision Trees"
        AppLanguage.HINDI -> "एकीकृत कीट प्रबंधन निर्णय वृक्ष"
        AppLanguage.MARATHI -> "एकीकृत कीड व्यवस्थापन निर्णय वृक्ष"
        AppLanguage.SPANISH -> "Árboles de Decisión de Manejo Integrado de Plagas"
        AppLanguage.SWAHILI -> "Miti ya Maamuzi ya Usimamizi Shirikishi wa Wadudu"
        AppLanguage.BENGALI -> "সমন্বিত বালাই ব্যবস্থাপনা সিদ্ধান্ত বৃক্ষ"
        AppLanguage.FRENCH -> "Arbres de Décision de Gestion Intégrée des Ravageurs"
    }

    fun ipmSearchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Search IPM protocols, pests, symptoms, crops..."
        AppLanguage.HINDI -> "आईपीएम प्रोटोकॉल, कीट, लक्षण, फसलें खोजें..."
        AppLanguage.MARATHI -> "आयपीएम प्रोटोकॉल, कीड, लक्षणे, पिके शोधा..."
        AppLanguage.SPANISH -> "Buscar protocolos MIP, plagas, síntomas, cultivos..."
        AppLanguage.SWAHILI -> "Tafuta itifaki za IPM, wadudu, dalili, mazao..."
        AppLanguage.BENGALI -> "আইপিএম প্রটোকল, পোকা, লক্ষণ, ফসল অনুসন্ধান করুন..."
        AppLanguage.FRENCH -> "Rechercher protocoles IPM, ravageurs, symptômes..."
    }

    fun decisionTreeTab(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Decision Tree"
        AppLanguage.HINDI -> "निर्णय वृक्ष"
        AppLanguage.MARATHI -> "निर्णय वृक्ष"
        AppLanguage.SPANISH -> "Árbol de Decisión"
        AppLanguage.SWAHILI -> "Mti wa Maamuzi"
        AppLanguage.BENGALI -> "সিদ্ধান্ত বৃক্ষ"
        AppLanguage.FRENCH -> "Arbre de Décision"
    }

    fun protocolLibraryTab(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Protocol Library"
        AppLanguage.HINDI -> "प्रोटोकॉल लाइब्रेरी"
        AppLanguage.MARATHI -> "प्रोटोकॉल लायब्ररी"
        AppLanguage.SPANISH -> "Biblioteca de Protocolos"
        AppLanguage.SWAHILI -> "Maktaba ya Itifaki"
        AppLanguage.BENGALI -> "প্রটোকল লাইব্রেরি"
        AppLanguage.FRENCH -> "Bibliothèque de Protocoles"
    }

    fun directSearchMatches(count: Int, lang: AppLanguage): String = directIpmSearchMatches(count, lang)

    fun cropsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Crops"
        AppLanguage.HINDI -> "फसलें"
        AppLanguage.MARATHI -> "पिके"
        AppLanguage.SPANISH -> "Cultivos"
        AppLanguage.SWAHILI -> "Mazao"
        AppLanguage.BENGALI -> "ফসল"
        AppLanguage.FRENCH -> "Cultures"
    }

    fun decisionPathHeader(steps: Int, lang: AppLanguage): String = decisionPathTitle(steps, lang)
    fun backBtn(lang: AppLanguage): String = backStep(lang)
    fun resetBtn(lang: AppLanguage): String = resetTree(lang)
    fun rootStep(lang: AppLanguage): String = rootNode(lang)
    fun diagnosticQuestionHeader(lang: AppLanguage): String = diagnosticQuestionTitle(lang)
    fun protocolReadyBadge(lang: AppLanguage): String = protocolReady(lang)
    fun backToTreeBtn(lang: AppLanguage): String = backToTree(lang)
    fun newDecisionTreeBtn(lang: AppLanguage): String = newDecisionTree(lang)

    fun typeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Type"
        AppLanguage.HINDI -> "प्रकार"
        AppLanguage.MARATHI -> "प्रकार"
        AppLanguage.SPANISH -> "Tipo"
        AppLanguage.SWAHILI -> "Aina"
        AppLanguage.BENGALI -> "প্রকার"
        AppLanguage.FRENCH -> "Type"
    }

    fun phiDays(days: Int, lang: AppLanguage): String = phiDaysBadge(days, lang)

    fun targetCrops(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Target Crops"
        AppLanguage.HINDI -> "लक्षित फसलें"
        AppLanguage.MARATHI -> "लक्षित पिके"
        AppLanguage.SPANISH -> "Cultivos Objetivo"
        AppLanguage.SWAHILI -> "Mazao Lengwa"
        AppLanguage.BENGALI -> "লক্ষ্য ফসল"
        AppLanguage.FRENCH -> "Cultures Cibles"
    }

    fun foliarFieldSymptoms(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "FOLIAR & FIELD SYMPTOMS"
        AppLanguage.HINDI -> "पत्ती और खेत के लक्षण"
        AppLanguage.MARATHI -> "पाने व शेतातील लक्षणे"
        AppLanguage.SPANISH -> "SÍNTOMAS FOLIARES Y EN CAMPO"
        AppLanguage.SWAHILI -> "DALILI ZA MAJANI NA SHAMBA"
        AppLanguage.BENGALI -> "পাতা ও মাঠের লক্ষণ"
        AppLanguage.FRENCH -> "SYMPTÔMES FOLIAIRES ET SUR LE TERRAIN"
    }

    fun agronomistChecklist(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Agronomist Prevention Checklist"
        AppLanguage.HINDI -> "कृषि विज्ञानी रोकथाम चेकलिस्ट"
        AppLanguage.MARATHI -> "कृषितज्ज्ञ प्रतिबंधक चेकलिस्ट"
        AppLanguage.SPANISH -> "Lista de Prevención del Agrónomo"
        AppLanguage.SWAHILI -> "Orodha ya Kinga ya Mtaalamu wa Kilimo"
        AppLanguage.BENGALI -> "কৃষিবিদ প্রতিরোধ চেকলিস্ট"
        AppLanguage.FRENCH -> "Liste de Contrôle Préventive de l'Agronome"
    }

    fun agronomistPreventionChecklist(lang: AppLanguage): String = agronomistChecklist(lang)

    fun ipmProtocolVerified(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM PROTOCOL VERIFIED"
        AppLanguage.HINDI -> "आईपीएम प्रोटोकॉल सत्यापित"
        AppLanguage.MARATHI -> "आयपीएम प्रोटोकॉल प्रमाणित"
        AppLanguage.SPANISH -> "PROTOCOLO MIP VERIFICADO"
        AppLanguage.SWAHILI -> "ITIFAKI YA IPM IMETHIBITISHWA"
        AppLanguage.BENGALI -> "আইপিএম প্রটোকল যাচাইকৃত"
        AppLanguage.FRENCH -> "PROTOCOLE IPM VÉRIFIÉ"
    }

    fun fourTierIpmHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "4-TIER INTEGRATED PEST MANAGEMENT (IPM) ADVISORY"
        AppLanguage.HINDI -> "4-स्तरीय एकीकृत कीट प्रबंधन (IPM) सलाह"
        AppLanguage.MARATHI -> "४-स्तरीय एकात्मिक कीड व्यवस्थापन (IPM) सल्ला"
        AppLanguage.SPANISH -> "ASESORÍA MIP EN 4 NIVELES"
        AppLanguage.SWAHILI -> "USHAURI WA NGAZI 4 WA IPM"
        AppLanguage.BENGALI -> "৪-স্তরের সমন্বিত বালাই ব্যবস্থাপনা (IPM) পরামর্শ"
        AppLanguage.FRENCH -> "CONSEIL EN 4 NIVEAUX DE GESTION INTÉGRÉE (IPM)"
    }

    fun microclimateRiskAlerts(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "MICROCLIMATE RISK ALERTS"
        AppLanguage.HINDI -> "माइक्रोक्लाइमेट जोखिम अलर्ट"
        AppLanguage.MARATHI -> "सूक्ष्महवामान धोका इशारे"
        AppLanguage.SPANISH -> "ALERTAS DE RIESGO MICROCLIMÁTICO"
        AppLanguage.SWAHILI -> "TAHADHARI ZA HALI YA HEWA"
        AppLanguage.BENGALI -> "মাইক্রোক্লাইমেট ঝুঁকি সতর্কতা"
        AppLanguage.FRENCH -> "ALERTES DE RISQUE MICROCLIMATIQUE"
    }

    fun localizeTierTitle(tier: IPMInterventionTier, lang: AppLanguage): String = when (tier) {
        IPMInterventionTier.CULTURAL -> culturalSanitationHeader(lang)
        IPMInterventionTier.MECHANICAL -> mechanicalPhysicalHeader(lang)
        IPMInterventionTier.BIOLOGICAL -> biologicalMicrobialHeader(lang)
        IPMInterventionTier.CHEMICAL_RATIONAL -> targetedChemicalHeader(lang)
    }

    fun localizeTierDescription(tier: IPMInterventionTier, lang: AppLanguage): String = when (tier) {
        IPMInterventionTier.CULTURAL -> when (lang) {
            AppLanguage.ENGLISH -> "Sanitation, crop rotation, resistant varieties & spacing"
            AppLanguage.HINDI -> "स्वच्छता, फसल चक्र, प्रतिरोधी किस्में और दूरी"
            AppLanguage.MARATHI -> "स्वच्छता, पीक फेरपालट, रोगप्रतिकारक वाण आणि अंतर"
            AppLanguage.SPANISH -> "Saneamiento, rotación, variedades resistentes y espaciamiento"
            AppLanguage.SWAHILI -> "Usafi, mzunguko wa mazao, aina sugu na nafasi"
            AppLanguage.BENGALI -> "পরিচ্ছন্নতা, ফসল পর্যায়ক্রম, প্রতিরোধী জাত ও দূরত্ব"
            AppLanguage.FRENCH -> "Assainissement, rotation des cultures, variétés résistantes et espacement"
        }
        IPMInterventionTier.MECHANICAL -> when (lang) {
            AppLanguage.ENGLISH -> "Sticky traps, pheromone lures, mulching & row covers"
            AppLanguage.HINDI -> "स्टिकी ट्रैप, फेरोमोन ल्यूर, मल्चिंग और रो कवर"
            AppLanguage.MARATHI -> "चिकट सापळे, फेरोमोन ल्यूर, मल्चिंग आणि रो कव्हर्स"
            AppLanguage.SPANISH -> "Trampas adhesivas, feromonas, acolchado y mallas"
            AppLanguage.SWAHILI -> "Mitego ya kunata, pheromone, matandazo na vizuizi"
            AppLanguage.BENGALI -> "আঠালো ফাঁদ, ফেরোমোন ফাঁদ, মালচিং এবং নেট"
            AppLanguage.FRENCH -> "Pièges englués, phéromones, paillage et voiles de forçage"
        }
        IPMInterventionTier.BIOLOGICAL -> when (lang) {
            AppLanguage.ENGLISH -> "Bio-fungicides, beneficial parasitoids, neem extracts & botanical oils"
            AppLanguage.HINDI -> "जैव-कवकनाशी, लाभकारी परजीवी, नीम अर्क और वानस्पतिक तेल"
            AppLanguage.MARATHI -> "जैव-बुरशीनाशके, उपयुक्त परजीवी, कडुलिंब अर्क आणि वनस्पतीजन्य तेले"
            AppLanguage.SPANISH -> "Biofungicidas, parasitoides benéficos, extractos de neem y aceites"
            AppLanguage.SWAHILI -> "Kuvu-hai za kinga, vidudu rafiki, dondoo za mwarobaini na mafuta"
            AppLanguage.BENGALI -> "বায়ো-ফাংগিসাইড, উপকারী পরজীবী, নিমের নির্যাস ও উদ্ভিজ্জ তেল"
            AppLanguage.FRENCH -> "Biofongicides, parasitoïdes utiles, extraits de neem et huiles végétales"
        }
        IPMInterventionTier.CHEMICAL_RATIONAL -> when (lang) {
            AppLanguage.ENGLISH -> "Targeted low-toxicity synthetics with strict Pre-Harvest Interval (PHI) compliance"
            AppLanguage.HINDI -> "सख्त कटाई पूर्व अंतराल (PHI) के साथ लक्षित कम विषाक्त रसायन"
            AppLanguage.MARATHI -> "कडक कापणीपूर्व अंतर (PHI) सह लक्षित कमी विषारी रसायने"
            AppLanguage.SPANISH -> "Sintéticos dirigidos de baja toxicidad con estricto cumplimiento de PHI"
            AppLanguage.SWAHILI -> "Kemikali zenye sumu ya chini zilizo na uzingatiaji mkali wa PHI"
            AppLanguage.BENGALI -> "কঠোর পিএইচআই মান্যতা সহ লক্ষ্যভিত্তিক কম বিষাক্ত রাসায়নিক"
            AppLanguage.FRENCH -> "Synthétiques ciblés à faible toxicité avec respect strict du DAR"
        }
    }

    fun noSpecificActions(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No specific actions required in this tier."
        AppLanguage.HINDI -> "इस स्तर में किसी विशेष कार्रवाई की आवश्यकता नहीं है।"
        AppLanguage.MARATHI -> "या टप्प्यात कोणत्याही विशिष्ट कृतीची आवश्यकता नाही."
        AppLanguage.SPANISH -> "No se requieren acciones específicas en este nivel."
        AppLanguage.SWAHILI -> "Hakuna hatua maalum zinazohitajika katika ngazi hii."
        AppLanguage.BENGALI -> "এই স্তরে কোনো নির্দিষ্ট পদক্ষেপের প্রয়োজন নেই।"
        AppLanguage.FRENCH -> "Aucune action spécifique requise à ce niveau."
    }

    fun ipmProtocolsAvailable(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM PROTOCOLS AVAILABLE ($count)"
        AppLanguage.HINDI -> "उपलब्ध आईपीएम प्रोटोकॉल ($count)"
        AppLanguage.MARATHI -> "उपलब्ध आयपीएम प्रोटोकॉल ($count)"
        AppLanguage.SPANISH -> "PROTOCOLOS MIP DISPONIBLES ($count)"
        AppLanguage.SWAHILI -> "MIONGOZO YA IPM INAYOPATIKANA ($count)"
        AppLanguage.BENGALI -> "উপলব্ধ আইপিএম প্রোটোকল ($count)"
        AppLanguage.FRENCH -> "PROTOCOLES IPM DISPONIBLES ($count)"
    }

    fun noMatchingIpmProtocols(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No matching IPM protocols found"
        AppLanguage.HINDI -> "कोई मेल खाने वाला आईपीएम प्रोटोकॉल नहीं मिला"
        AppLanguage.MARATHI -> "कोणताही जुळणारा आयपीएम प्रोटोकॉल सापडला नाही"
        AppLanguage.SPANISH -> "No se encontraron protocolos MIP coincidentes"
        AppLanguage.SWAHILI -> "Hakuna miongozo ya IPM iliyolingana iliyopatikana"
        AppLanguage.BENGALI -> "কোনো মিল থাকা আইপিএম প্রোটোকল পাওয়া যায়নি"
        AppLanguage.FRENCH -> "Aucun protocole IPM correspondant trouvé"
    }

    fun tryBroaderIpmSearch(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Try searching for a different crop, pest name, or clearing filters."
        AppLanguage.HINDI -> "किसी भिन्न फसल, कीट का नाम खोजें या फ़िल्टर साफ़ करें।"
        AppLanguage.MARATHI -> "वेगळे पीक, कीड नाव शोधून पहा किंवा फिल्टर्स साफ करा."
        AppLanguage.SPANISH -> "Intente buscar otro cultivo, plaga o limpiar filtros."
        AppLanguage.SWAHILI -> "Jaribu kutafuta zao tofauti, wadudu, au futa vichujio."
        AppLanguage.BENGALI -> "অন্য কোনো ফসল, পোকার নাম অনুসন্ধান করুন বা ফিল্টার সাফ করুন।"
        AppLanguage.FRENCH -> "Essayez de rechercher une autre culture, un ravageur ou d'effacer les filtres."
    }

    fun resetSearchFilterBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Reset Search & Filters"
        AppLanguage.HINDI -> "खोज और फिल्टर रीसेट करें"
        AppLanguage.MARATHI -> "शोध व फिल्टर्स रीसेट करा"
        AppLanguage.SPANISH -> "Restablecer Búsqueda y Filtros"
        AppLanguage.SWAHILI -> "Weka Upya Utafutaji na Vichujio"
        AppLanguage.BENGALI -> "অনুসন্ধান ও ফিল্টার রিসেট করুন"
        AppLanguage.FRENCH -> "Réinitialiser la Recherche et Filtres"
    }

    fun view4TierPlan(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View 4-Tier Plan"
        AppLanguage.HINDI -> "4-स्तरीय योजना देखें"
        AppLanguage.MARATHI -> "४-स्तरीय योजना पहा"
        AppLanguage.SPANISH -> "Ver Plan de 4 Niveles"
        AppLanguage.SWAHILI -> "Tazama Mpango wa Hatua 4"
        AppLanguage.BENGALI -> "৪-স্তরের পরিকল্পনা দেখুন"
        AppLanguage.FRENCH -> "Voir le Plan à 4 Niveaux"
    }

    // Diagnosis Screen Localization Methods
    fun ipmDecisionTreeAdvisorTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM Decision Tree Advisor"
        AppLanguage.HINDI -> "आईपीएम निर्णय वृक्ष सलाहकार"
        AppLanguage.MARATHI -> "आयपीएम निर्णय वृक्ष सल्लागार"
        AppLanguage.SPANISH -> "Asesor de Árbol de Decisión MIP"
        AppLanguage.SWAHILI -> "Mshauri wa Mti wa Maamuzi wa IPM"
        AppLanguage.BENGALI -> "আইপিএম সিদ্ধান্ত বৃক্ষ পরামর্শদাতা"
        AppLanguage.FRENCH -> "Arbre de Décision et Conseil IPM"
    }

    fun ipmDecisionTreeAdvisorSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Step-by-step diagnostic tree with 4-tier bio & cultural protocols"
        AppLanguage.HINDI -> "4-स्तरीय जैविक और कृषि पद्धतियों के साथ चरण-दर-चरण निदान"
        AppLanguage.MARATHI -> "4-स्तरीय जैविक आणि कृषी पद्धतींसह टप्प्याटप्प्याने निदान"
        AppLanguage.SPANISH -> "Árbol de diagnóstico paso a paso con protocolos biológicos y culturales"
        AppLanguage.SWAHILI -> "Mti wa utambuzi wa hatua kwa hatua wenye itifaki za kibaolojia na kilimo"
        AppLanguage.BENGALI -> "৪-স্তরের জৈব ও কৃষি প্রোটোকল সহ ধাপে ধাপে নির্ণয়"
        AppLanguage.FRENCH -> "Arbre de diagnostic étape par étape avec protocoles bio et culturaux"
    }

    fun openButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Open"
        AppLanguage.HINDI -> "खोलें"
        AppLanguage.MARATHI -> "उघडा"
        AppLanguage.SPANISH -> "Abrir"
        AppLanguage.SWAHILI -> "Fungua"
        AppLanguage.BENGALI -> "খুলুন"
        AppLanguage.FRENCH -> "Ouvrir"
    }

    fun cropLeafSpecimenImageHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "1. CROP LEAF SPECIMEN IMAGE"
        AppLanguage.HINDI -> "1. फसल पत्ती नमूना छवि"
        AppLanguage.MARATHI -> "1. पीक पान नमुना फोटो"
        AppLanguage.SPANISH -> "1. IMAGEN DE LA MUESTRA DE HOJA"
        AppLanguage.SWAHILI -> "1. PICHA YA SAMPULI YA JANI"
        AppLanguage.BENGALI -> "১. ফসলের পাতার নমুনার ছবি"
        AppLanguage.FRENCH -> "1. IMAGE DU SPÉCIMEN DE FEUILLE"
    }

    fun customPhotoBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Custom Photo"
        AppLanguage.HINDI -> "कस्टम फोटो"
        AppLanguage.MARATHI -> "स्वतःचा फोटो"
        AppLanguage.SPANISH -> "Foto Personalizada"
        AppLanguage.SWAHILI -> "Picha Maalum"
        AppLanguage.BENGALI -> "কাস্টম ছবি"
        AppLanguage.FRENCH -> "Photo Personnalisée"
    }

    fun targetCuticle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Multimodal AI Target: Leaf Cuticle"
        AppLanguage.HINDI -> "मल्टीमॉडल एआई लक्ष्य: पत्ती की सतह"
        AppLanguage.MARATHI -> "मल्टीमॉडल एआय लक्ष्य: पानाचा थर"
        AppLanguage.SPANISH -> "Objetivo IA: Cutícula Foliar"
        AppLanguage.SWAHILI -> "Lengo la AI: Tabaka la Jani"
        AppLanguage.BENGALI -> "মাল্টিমোডাল এআই লক্ষ্য: পাতার উপরিভাগ"
        AppLanguage.FRENCH -> "Cible IA : Cuticule Foliaire"
    }

    fun clear(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Clear"
        AppLanguage.HINDI -> "हटाएं"
        AppLanguage.MARATHI -> "हटवा"
        AppLanguage.SPANISH -> "Borrar"
        AppLanguage.SWAHILI -> "Futa"
        AppLanguage.BENGALI -> "মুছুন"
        AppLanguage.FRENCH -> "Effacer"
    }

    fun retake(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Retake"
        AppLanguage.HINDI -> "फिर से लें"
        AppLanguage.MARATHI -> "पुन्हा घ्या"
        AppLanguage.SPANISH -> "Reintentar"
        AppLanguage.SWAHILI -> "Piga Tena"
        AppLanguage.BENGALI -> "পুনরায় নিন"
        AppLanguage.FRENCH -> "Reprendre"
    }

    fun scan(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Scan"
        AppLanguage.HINDI -> "स्कैन करें"
        AppLanguage.MARATHI -> "स्कॅन करा"
        AppLanguage.BENGALI -> "স্ক্যান করুন"
        AppLanguage.SPANISH -> "Escanear"
        AppLanguage.FRENCH -> "Scanner"
        AppLanguage.SWAHILI -> "Changanua"
    }

    fun scanWithCamera(lang: AppLanguage): String = scan(lang)

    fun fromGallery(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "From Gallery"
        AppLanguage.HINDI -> "गैलरी से चुनें"
        AppLanguage.MARATHI -> "गॅलरीमधून निवडा"
        AppLanguage.SPANISH -> "Desde la Galería"
        AppLanguage.SWAHILI -> "Kutoka Matunzio"
        AppLanguage.BENGALI -> "গ্যালারি থেকে নির্বাচন করুন"
        AppLanguage.FRENCH -> "Depuis la Galerie"
    }

    fun loadingProgress(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Loading..."
        AppLanguage.HINDI -> "लोड हो रहा है..."
        AppLanguage.MARATHI -> "लोड होत आहे..."
        AppLanguage.SPANISH -> "Cargando..."
        AppLanguage.SWAHILI -> "Inapakia..."
        AppLanguage.BENGALI -> "লোড হচ্ছে..."
        AppLanguage.FRENCH -> "Chargement..."
    }

    fun selectBenchmarkHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Or Select a Disease Specimen Benchmark:"
        AppLanguage.HINDI -> "या रोग नमूना मानक चुनें:"
        AppLanguage.MARATHI -> "किंवा रोग नमुना मानक निवडा:"
        AppLanguage.SPANISH -> "O selecciona una muestra de referencia:"
        AppLanguage.SWAHILI -> "Au chagua kigezo cha sampuli ya ugonjwa:"
        AppLanguage.BENGALI -> "অথবা একটি রোগ নমুনা বেঞ্চমার্ক নির্বাচন করুন:"
        AppLanguage.FRENCH -> "Ou sélectionnez un échantillon de référence :"
    }

    fun cropName(crop: String, lang: AppLanguage): String = when {
        crop.equals("All", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "All"
            AppLanguage.HINDI -> "सभी"
            AppLanguage.MARATHI -> "सर्व"
            AppLanguage.SPANISH -> "Todos"
            AppLanguage.SWAHILI -> "Yote"
            AppLanguage.BENGALI -> "সব"
            AppLanguage.FRENCH -> "Tous"
        }
        crop.equals("Auto-Detect", ignoreCase = true) || crop.equals("auto_detect", ignoreCase = true) -> autoDetect(lang)
        crop.startsWith("Tomato", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Tomato"
            AppLanguage.HINDI -> "टमाटर"
            AppLanguage.MARATHI -> "टोमॅटो"
            AppLanguage.SPANISH -> "Tomate"
            AppLanguage.SWAHILI -> "Nyanya"
            AppLanguage.BENGALI -> "টমেটো"
            AppLanguage.FRENCH -> "Tomate"
        }
        crop.startsWith("Rice", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Rice (Paddy)"
            AppLanguage.HINDI -> "चावल (धान)"
            AppLanguage.MARATHI -> "भात (धान)"
            AppLanguage.SPANISH -> "Arroz"
            AppLanguage.SWAHILI -> "Mpunga"
            AppLanguage.BENGALI -> "ধান"
            AppLanguage.FRENCH -> "Riz"
        }
        crop.startsWith("Maize", ignoreCase = true) || crop.startsWith("Corn", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Maize (Corn)"
            AppLanguage.HINDI -> "मक्का"
            AppLanguage.MARATHI -> "मका"
            AppLanguage.SPANISH -> "Maíz"
            AppLanguage.SWAHILI -> "Mahindi"
            AppLanguage.BENGALI -> "ভুট্টা"
            AppLanguage.FRENCH -> "Maïs"
        }
        crop.startsWith("Cotton", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Cotton"
            AppLanguage.HINDI -> "कपास"
            AppLanguage.MARATHI -> "कापूस"
            AppLanguage.SPANISH -> "Algodón"
            AppLanguage.SWAHILI -> "Pamba"
            AppLanguage.BENGALI -> "তুলা"
            AppLanguage.FRENCH -> "Coton"
        }
        crop.startsWith("Wheat", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Wheat"
            AppLanguage.HINDI -> "गेहूं"
            AppLanguage.MARATHI -> "गहू"
            AppLanguage.SPANISH -> "Trigo"
            AppLanguage.SWAHILI -> "Ngano"
            AppLanguage.BENGALI -> "গম"
            AppLanguage.FRENCH -> "Blé"
        }
        else -> crop
    }

    fun fieldConditionsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "2. FIELD CONDITIONS & SYMPTOMS"
        AppLanguage.HINDI -> "2. खेत की स्थिति और लक्षण"
        AppLanguage.MARATHI -> "2. शेतातील परिस्थिती आणि लक्षणे"
        AppLanguage.SPANISH -> "2. CONDICIONES DE CAMPO Y SÍNTOMAS"
        AppLanguage.SWAHILI -> "2. HALI YA SHAMBA NA DALILI"
        AppLanguage.BENGALI -> "২. মাঠের অবস্থা ও লক্ষণ"
        AppLanguage.FRENCH -> "2. CONDITIONS DU TERRAIN ET SYMPTÔMES"
    }

    fun cropTypeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Crop Type"
        AppLanguage.HINDI -> "फसल का प्रकार"
        AppLanguage.MARATHI -> "पिकाचा प्रकार"
        AppLanguage.SPANISH -> "Tipo de Cultivo"
        AppLanguage.SWAHILI -> "Aina ya Zao"
        AppLanguage.BENGALI -> "ফসলের ধরন"
        AppLanguage.FRENCH -> "Type de Culture"
    }

    fun cropTypePlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. Tomato, Rice, Maize"
        AppLanguage.HINDI -> "जैसे टमाटर, धान, मक्का"
        AppLanguage.MARATHI -> "उदा. टोमॅटो, भात, मका"
        AppLanguage.SPANISH -> "ej. Tomate, Arroz, Maíz"
        AppLanguage.SWAHILI -> "mf. Nyanya, Mpunga, Mahindi"
        AppLanguage.BENGALI -> "যেমন টমেটো, ধান, ভুট্টা"
        AppLanguage.FRENCH -> "ex. Tomate, Riz, Maïs"
    }

    fun varietyLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Variety / Hybrid"
        AppLanguage.HINDI -> "किस्म / हाइब्रिड"
        AppLanguage.MARATHI -> "वाण / संकरित"
        AppLanguage.SPANISH -> "Variedad / Híbrido"
        AppLanguage.SWAHILI -> "Aina / Chotara"
        AppLanguage.BENGALI -> "জাত / হাইব্রিড"
        AppLanguage.FRENCH -> "Variété / Hybride"
    }

    fun varietyPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. Abhinav F1"
        AppLanguage.HINDI -> "जैसे अभिनव F1"
        AppLanguage.MARATHI -> "उदा. अभिनव F1"
        AppLanguage.SPANISH -> "ej. Abhinav F1"
        AppLanguage.SWAHILI -> "mf. Abhinav F1"
        AppLanguage.BENGALI -> "যেমন অভিনব F1"
        AppLanguage.FRENCH -> "ex. Abhinav F1"
    }

    fun growthStageLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Growth Stage"
        AppLanguage.HINDI -> "विकास चरण"
        AppLanguage.MARATHI -> "वाढीची अवस्था"
        AppLanguage.SPANISH -> "Etapa de Crecimiento"
        AppLanguage.SWAHILI -> "Hatua ya Ukuaji"
        AppLanguage.BENGALI -> "বৃদ্ধির ধাপ"
        AppLanguage.FRENCH -> "Stade de Croissance"
    }

    fun growthStagePlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. Vegetative, Flowering"
        AppLanguage.HINDI -> "जैसे वानस्पतिक, फूल आना"
        AppLanguage.MARATHI -> "उदा. शाकीय, फुलधारणा"
        AppLanguage.SPANISH -> "ej. Vegetativo, Floración"
        AppLanguage.SWAHILI -> "mf. Ukuaji wa Majani, Maua"
        AppLanguage.BENGALI -> "যেমন অঙ্গজ, ফুল ফোটা"
        AppLanguage.FRENCH -> "ex. Végétatif, Floraison"
    }

    fun soilConditionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Soil Condition"
        AppLanguage.HINDI -> "मिट्टी की स्थिति"
        AppLanguage.MARATHI -> "मातीची स्थिती"
        AppLanguage.SPANISH -> "Condición del Suelo"
        AppLanguage.SWAHILI -> "Hali ya Udongo"
        AppLanguage.BENGALI -> "মাটির অবস্থা"
        AppLanguage.FRENCH -> "État du Sol"
    }

    fun soilConditionPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. Loamy, High Moisture"
        AppLanguage.HINDI -> "जैसे दोमट, अधिक नमी"
        AppLanguage.MARATHI -> "उदा. गाळाची, जास्त ओलावा"
        AppLanguage.SPANISH -> "ej. Franco, Alta Humedad"
        AppLanguage.SWAHILI -> "mf. Tifu, Unyevu Mwingi"
        AppLanguage.BENGALI -> "যেমন দোআঁশ, উচ্চ আর্দ্রতা"
        AppLanguage.FRENCH -> "ex. Limoneux, Forte Humidité"
    }

    fun observedSymptomsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Observed Symptoms / Leaf Lesions"
        AppLanguage.HINDI -> "देखे गए लक्षण / पत्ती के घाव"
        AppLanguage.MARATHI -> "दिसून आलेली लक्षणे / पानांवरील डाग"
        AppLanguage.SPANISH -> "Síntomas Observados / Lesiones en Hojas"
        AppLanguage.SWAHILI -> "Dalili Zilizoonwa / Vidonda vya Majani"
        AppLanguage.BENGALI -> "পর্যবেক্ষিত লক্ষণ / পাতার ক্ষত"
        AppLanguage.FRENCH -> "Symptômes Observés / Lésions Foliaires"
    }

    fun observedSymptomsPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Describe visible spots, wilting, curling, discoloration, or pest presence..."
        AppLanguage.HINDI -> "दिखने वाले धब्बे, मुरझाना, मुड़ना, रंग में बदलाव या कीटों की उपस्थिति बताएं..."
        AppLanguage.MARATHI -> "दिसणारे डाग, सुकणे, पाने वाकडी होणे, रंगातील बदल किंवा किडीचे प्रमाण लिहा..."
        AppLanguage.SPANISH -> "Describe manchas visibles, marchitez, enrollamiento, decoloración o presencia de plagas..."
        AppLanguage.SWAHILI -> "Eleza madoa yanayoonekana, kunyauka, kujikunja, kubadilika rangi, au uwepo wa wadudu..."
        AppLanguage.BENGALI -> "দৃশ্যমান দাগ, শুকিয়ে যাওয়া, কোঁকড়ানো, বিবর্ণতা বা কীটপতঙ্গের উপস্থিতি বর্ণনা করুন..."
        AppLanguage.FRENCH -> "Décrivez les taches visibles, le flétrissement, l'enroulement, la décoloration ou la présence de ravageurs..."
    }

    fun microclimateTelemetryHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "3. MICROCLIMATE & PEST-TRAP TELEMETRY"
        AppLanguage.HINDI -> "3. सूक्ष्म जलवायु और कीट-जाल टेलीमेट्री"
        AppLanguage.MARATHI -> "3. सूक्ष्म हवामान आणि कीटक-सापळा टेलीमेट्री"
        AppLanguage.SPANISH -> "3. TELEMETRÍA DE MICROCLIMA Y TRAMPAS DE PLAGAS"
        AppLanguage.SWAHILI -> "3. TELEMETRI YA HALI YA HEWA NA MITEGO YA WADUDU"
        AppLanguage.BENGALI -> "৩. মাইক্রোক্লাইমেট এবং পোকা-ফাঁদ টেলিমেট্রি"
        AppLanguage.FRENCH -> "3. TÉLÉMÉTRIE MICROCLIMAT ET PIÈGES À RAVAGEURS"
    }

    fun syncLiveWeather(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sync Live Weather"
        AppLanguage.HINDI -> "लाइव मौसम सिंक करें"
        AppLanguage.MARATHI -> "थेट हवामान सिंक करा"
        AppLanguage.SPANISH -> "Sincronizar Clima en Vivo"
        AppLanguage.SWAHILI -> "Sawazisha Hali ya Hewa"
        AppLanguage.BENGALI -> "লাইভ আবহাওয়া সিঙ্ক করুন"
        AppLanguage.FRENCH -> "Synchroniser Météo en Direct"
    }

    fun recentWeatherLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Recent Weather & Microclimate"
        AppLanguage.HINDI -> "हाल का मौसम और सूक्ष्म जलवायु"
        AppLanguage.MARATHI -> "हवामान आणि सूक्ष्म हवामान"
        AppLanguage.SPANISH -> "Clima Reciente y Microclima"
        AppLanguage.SWAHILI -> "Hali ya Hewa ya Hivi Karibuni"
        AppLanguage.BENGALI -> "সাম্প্রতিক আবহাওয়া ও মাইক্রোক্লাইমেট"
        AppLanguage.FRENCH -> "Météo Récente et Microclimat"
    }

    fun recentWeatherPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. Continuous rain, 85% humidity, morning mist"
        AppLanguage.HINDI -> "जैसे लगातार बारिश, 85% आर्द्रता, सुबह का कोहरा"
        AppLanguage.MARATHI -> "उदा. सतत पाऊस, ८५% आर्द्रता, सकाळचे धुके"
        AppLanguage.SPANISH -> "ej. Lluvia continua, 85% humedad, niebla matutina"
        AppLanguage.SWAHILI -> "mf. Mvua mfululizo, unyevu 85%, ukungu wa asubuhi"
        AppLanguage.BENGALI -> "যেমন অবিরাম বৃষ্টি, ৮৫% আর্দ্রতা, সকালের কুয়াশা"
        AppLanguage.FRENCH -> "ex. Pluie continue, humidité 85%, brume matinale"
    }

    fun trapCountLabel(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Pheromone/Sticky Trap Count: $count / day"
        AppLanguage.HINDI -> "फेरोमोन/स्टिक ट्रैप गणना: $count / दिन"
        AppLanguage.MARATHI -> "फेरोमोन/चिकट सापळा गणना: $count / दिवस"
        AppLanguage.SPANISH -> "Conteo Trampa Feromonas/Pegajosa: $count / día"
        AppLanguage.SWAHILI -> "Idadi ya Mitego ya Feromoni/Kunata: $count / siku"
        AppLanguage.BENGALI -> "ফেরোমোন/আঠালো ফাঁদ গণনা: $count / দিন"
        AppLanguage.FRENCH -> "Comptage Piège à Phéromone/Gluant : $count / jour"
    }

    fun thresholdExceeded(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Threshold Exceeded"
        AppLanguage.HINDI -> "सीमा पार (चेतावनी)"
        AppLanguage.MARATHI -> "मर्यादा ओलांडली (धोका)"
        AppLanguage.SPANISH -> "Umbral Superado"
        AppLanguage.SWAHILI -> "Kiwango Kimezidi"
        AppLanguage.BENGALI -> "সীমা অতিক্রম করেছে"
        AppLanguage.FRENCH -> "Seuil Dépassé"
    }

    fun canopyMoistureLabel(moisture: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Canopy Moisture: $moisture%"
        AppLanguage.HINDI -> "कैनोपी नमी: $moisture%"
        AppLanguage.MARATHI -> "कॅनॉपी ओलावा: $moisture%"
        AppLanguage.SPANISH -> "Humedad de Dosel: $moisture%"
        AppLanguage.SWAHILI -> "Unyevu wa Majani: $moisture%"
        AppLanguage.BENGALI -> "ক্যানোপি আর্দ্রতা: $moisture%"
        AppLanguage.FRENCH -> "Humidité de la Canopée : $moisture%"
    }

    fun tempLabel(temp: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Temp: $temp°C"
        AppLanguage.HINDI -> "तापमान: $temp°C"
        AppLanguage.MARATHI -> "तापमान: $temp°C"
        AppLanguage.SPANISH -> "Temp: $temp°C"
        AppLanguage.SWAHILI -> "Joto: $temp°C"
        AppLanguage.BENGALI -> "তাপমাত্রা: $temp°C"
        AppLanguage.FRENCH -> "Temp : $temp°C"
    }

    fun diagnosingLeafLesionsWithAi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnosing Leaf Lesions with Gemini AI..."
        AppLanguage.HINDI -> "जेमिनी एआई से पत्ती के लक्षणों का निदान हो रहा है..."
        AppLanguage.MARATHI -> "जेमिनी एआय द्वारे पानांवरील लक्षणांचे निदान सुरू आहे..."
        AppLanguage.SPANISH -> "Diagnosticando Lesiones con Gemini AI..."
        AppLanguage.SWAHILI -> "Inatambua Vidonda vya Majani kwa Gemini AI..."
        AppLanguage.BENGALI -> "জেমিনি এআই দিয়ে পাতার ক্ষত নির্ণয় করা হচ্ছে..."
        AppLanguage.FRENCH -> "Diagnostic des Lésions avec Gemini AI..."
    }

    fun runAiCropHealthDiagnosis(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Run AI Crop Health Diagnosis"
        AppLanguage.HINDI -> "एआई फसल स्वास्थ्य निदान चलाएं"
        AppLanguage.MARATHI -> "एआय पीक आरोग्य निदान सुरू करा"
        AppLanguage.SPANISH -> "Ejecutar Diagnóstico de Cultivo con IA"
        AppLanguage.SWAHILI -> "Endesha Utambuzi wa Afya ya Mazao kwa AI"
        AppLanguage.BENGALI -> "এআই শস্য স্বাস্থ্য রোগনির্ণয় শুরু করুন"
        AppLanguage.FRENCH -> "Lancer le Diagnostic des Cultures par IA"
    }

    fun diagnoseUploadedLeafWithAi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnose Uploaded Leaf with AI"
        AppLanguage.HINDI -> "अपलोड की गई पत्ती का एआई निदान करें"
        AppLanguage.MARATHI -> "अपलोड केलेल्या पानाचे एआय निदान करा"
        AppLanguage.SPANISH -> "Diagnosticar Hoja Subida con IA"
        AppLanguage.SWAHILI -> "Tambua Jani Lililopakiwa kwa AI"
        AppLanguage.BENGALI -> "আপলোড করা পাতার এআই রোগনির্ণয় করুন"
        AppLanguage.FRENCH -> "Diagnostiquer la Feuille Téléchargée par IA"
    }

    fun diagnosisComplete(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "DIAGNOSIS COMPLETE"
        AppLanguage.HINDI -> "निदान पूर्ण"
        AppLanguage.MARATHI -> "रोगनिदान पूर्ण"
        AppLanguage.SPANISH -> "DIAGNÓSTICO COMPLETADO"
        AppLanguage.SWAHILI -> "UTAMBUZI UMEKAMILIKA"
        AppLanguage.BENGALI -> "রোগনির্ণয় সম্পন্ন"
        AppLanguage.FRENCH -> "DIAGNOSTIC TERMINÉ"
    }

    fun confidenceAndPhi(confidence: Int, phiDays: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Confidence: $confidence% • PHI: $phiDays Days"
        AppLanguage.HINDI -> "विश्वसनीयता: $confidence% • PHI: $phiDays दिन"
        AppLanguage.MARATHI -> "विश्वासार्हता: $confidence% • PHI: $phiDays दिवस"
        AppLanguage.SPANISH -> "Confianza: $confidence% • PHI: $phiDays Días"
        AppLanguage.SWAHILI -> "Uhakika: $confidence% • PHI: Siku $phiDays"
        AppLanguage.BENGALI -> "নির্ভুলতা: $confidence% • PHI: $phiDays দিন"
        AppLanguage.FRENCH -> "Confiance : $confidence% • DAR : $phiDays Jours"
    }

    fun viewCompletePrescriptionButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View Complete IPM Prescription & Dosage"
        AppLanguage.HINDI -> "पूरी आईपीएम सलाह और खुराक देखें"
        AppLanguage.MARATHI -> "संपूर्ण आयपीएम सल्ला आणि प्रमाण पहा"
        AppLanguage.SPANISH -> "Ver Prescripción y Dosis MIP Completa"
        AppLanguage.SWAHILI -> "Tazama Maagizo Kamili ya IPM na Kipimo"
        AppLanguage.BENGALI -> "সম্পূর্ণ আইপিএম পরামর্শ ও মাত্রা দেখুন"
        AppLanguage.FRENCH -> "Voir Prescription et Dosage IPM Complets"
    }

    fun recentFarmDiagnosesHeader(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "RECENT FARM DIAGNOSES & TRIAGE ($count)"
        AppLanguage.HINDI -> "हालिया फार्म निदान और वर्गीकरण ($count)"
        AppLanguage.MARATHI -> "नुकतेच केलेले शेत निदान आणि वर्गीकरण ($count)"
        AppLanguage.SPANISH -> "DIAGNÓSTICOS Y TRIAJE RECIENTES ($count)"
        AppLanguage.SWAHILI -> "UTAMBUZI NA UPANGAJI WA HIVI KARIBUNI ($count)"
        AppLanguage.BENGALI -> "সাম্প্রতিক খামার রোগনির্ণয় ও ট্রায়াজ ($count)"
        AppLanguage.FRENCH -> "DIAGNOSTICS ET TRIAGE RÉCENTS ($count)"
    }

    fun viewHotspotMap(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View Hotspot Map"
        AppLanguage.HINDI -> "हॉटस्पॉट मानचित्र देखें"
        AppLanguage.MARATHI -> "हॉटस्पॉट नकाशा पहा"
        AppLanguage.SPANISH -> "Ver Mapa de Focos"
        AppLanguage.SWAHILI -> "Tazama Ramani ya Maeneo Hatari"
        AppLanguage.BENGALI -> "হটস্পট মানচিত্র দেখুন"
        AppLanguage.FRENCH -> "Voir Carte des Foyers"
    }

    fun searchPreviousDiagnosesPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Search previous diagnoses, crops, varieties, symptoms..."
        AppLanguage.HINDI -> "पिछले निदान, फसलें, किस्में, लक्षण खोजें..."
        AppLanguage.MARATHI -> "मागील निदान, पिके, वाण, लक्षणे शोधा..."
        AppLanguage.SPANISH -> "Buscar diagnósticos anteriores, cultivos, variedades, síntomas..."
        AppLanguage.SWAHILI -> "Tafuta utambuzi wa awali, mazao, aina, dalili..."
        AppLanguage.BENGALI -> "পূর্ববর্তী রোগনির্ণয়, ফসল, জাত, লক্ষণ অনুসন্ধান করুন..."
        AppLanguage.FRENCH -> "Rechercher diagnostics précédents, cultures, variétés, symptômes..."
    }

    fun foundRecordsCount(found: Int, total: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Found $found of $total records"
        AppLanguage.HINDI -> "$total में से $found रिकॉर्ड मिले"
        AppLanguage.MARATHI -> "$total पैकी $found नोंदी आढळल्या"
        AppLanguage.SPANISH -> "$found de $total registros encontrados"
        AppLanguage.SWAHILI -> "Rekodi $found kati ya $total zimepatikana"
        AppLanguage.BENGALI -> "$total এর মধ্যে $found টি রেকর্ড পাওয়া গেছে"
        AppLanguage.FRENCH -> "$found sur $total enregistrements trouvés"
    }

    fun resetFilters(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Reset Filters"
        AppLanguage.HINDI -> "फ़िल्टर रीसेट करें"
        AppLanguage.MARATHI -> "फिल्टर्स पूर्ववत करा"
        AppLanguage.SPANISH -> "Restablecer Filtros"
        AppLanguage.SWAHILI -> "Weka Upya Vichujio"
        AppLanguage.BENGALI -> "ফিল্টার রিসেট করুন"
        AppLanguage.FRENCH -> "Réinitialiser Filtres"
    }

    fun noCropReportsRecorded(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No crop reports recorded yet."
        AppLanguage.HINDI -> "अभी तक कोई फसल रिपोर्ट दर्ज नहीं है।"
        AppLanguage.MARATHI -> "अद्याप कोणतीही पीक नोंद नाही."
        AppLanguage.SPANISH -> "Aún no hay informes de cultivos registrados."
        AppLanguage.SWAHILI -> "Hakuna ripoti za mazao zilizorekodiwa bado."
        AppLanguage.BENGALI -> "এখনও কোনও শস্যের রিপোর্ট নথিভুক্ত হয়নি।"
        AppLanguage.FRENCH -> "Aucun rapport de culture enregistré."
    }

    fun noMatchingDiagnoses(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No matching diagnoses found"
        AppLanguage.HINDI -> "कोई मेल खाता निदान नहीं मिला"
        AppLanguage.MARATHI -> "कोणतेही जुळणारे निदान सापडले नाही"
        AppLanguage.SPANISH -> "No se encontraron diagnósticos coincidentes"
        AppLanguage.SWAHILI -> "Hakuna utambuzi unaolingana uliopatikana"
        AppLanguage.BENGALI -> "কোনও মিল পাওয়া রোগনির্ণয় মেলেনি"
        AppLanguage.FRENCH -> "Aucun diagnostic correspondant trouvé"
    }

    fun tryAdjustingKeywords(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Try adjusting your keywords or selecting another crop filter."
        AppLanguage.HINDI -> "कीवर्ड बदलकर देखें या कोई अन्य फसल फ़िल्टर चुनें।"
        AppLanguage.MARATHI -> "कृपया शब्द बदलून पहा किंवा दुसरा पीक फिल्टर निवडा."
        AppLanguage.SPANISH -> "Intenta ajustar tus palabras clave o selecciona otro filtro de cultivo."
        AppLanguage.SWAHILI -> "Jaribu kurekebisha maneno yako au kuchagua kichujio kingine cha zao."
        AppLanguage.BENGALI -> "অনুগ্রহ করে কীওয়ার্ড পরিবর্তন করুন বা অন্য ফসলের ফিল্টার নির্বাচন করুন।"
        AppLanguage.FRENCH -> "Essayez d'ajuster vos mots-clés ou de sélectionner un autre filtre de culture."
    }

    fun clearFilters(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Clear Filters"
        AppLanguage.HINDI -> "फ़िल्टर हटाएं"
        AppLanguage.MARATHI -> "फिल्टर काढा"
        AppLanguage.SPANISH -> "Limpiar Filtros"
        AppLanguage.SWAHILI -> "Futa Vichujio"
        AppLanguage.BENGALI -> "ফিল্টার মুছুন"
        AppLanguage.FRENCH -> "Effacer les Filtres"
    }

    fun reportStatus(status: ReportStatus, lang: AppLanguage): String = when (status) {
        ReportStatus.AI_DIAGNOSED -> when (lang) {
            AppLanguage.ENGLISH -> "AI Diagnosed"
            AppLanguage.HINDI -> "एआई निदान"
            AppLanguage.MARATHI -> "एआय निदान"
            AppLanguage.SPANISH -> "Diagnosticado por IA"
            AppLanguage.SWAHILI -> "Imetambuliwa na AI"
            AppLanguage.BENGALI -> "এআই নির্ণয়"
            AppLanguage.FRENCH -> "Diagnostiqué par IA"
        }
        ReportStatus.SUBMITTED_TO_EXPERT -> when (lang) {
            AppLanguage.ENGLISH -> "Submitted to Extension"
            AppLanguage.HINDI -> "विस्तार सेवा को प्रेषित"
            AppLanguage.MARATHI -> "विस्तार सेवेकडे पाठवले"
            AppLanguage.SPANISH -> "Enviado a Extensión"
            AppLanguage.SWAHILI -> "Imetumwa kwa Wataalamu"
            AppLanguage.BENGALI -> "এক্সটেনশনে জমা দেওয়া হয়েছে"
            AppLanguage.FRENCH -> "Soumis à la Vulgarisation"
        }
        ReportStatus.IN_LAB_REVIEW -> when (lang) {
            AppLanguage.ENGLISH -> "In Laboratory Triage"
            AppLanguage.HINDI -> "प्रयोगशाला जांच में"
            AppLanguage.MARATHI -> "प्रयोगशाळा तपासणीत"
            AppLanguage.SPANISH -> "En Triaje de Laboratorio"
            AppLanguage.SWAHILI -> "Kwenye Maabara"
            AppLanguage.BENGALI -> "ল্যাবরেটরি পর্যালোচনায়"
            AppLanguage.FRENCH -> "En Triage de Laboratoire"
        }
        ReportStatus.EXPERT_VALIDATED -> when (lang) {
            AppLanguage.ENGLISH -> "Expert Confirmed"
            AppLanguage.HINDI -> "विशेषज्ञ द्वारा सत्यापित"
            AppLanguage.MARATHI -> "तज्ञांनी पुष्टी केली"
            AppLanguage.SPANISH -> "Confirmado por Experto"
            AppLanguage.SWAHILI -> "Imethibitishwa na Mtaalamu"
            AppLanguage.BENGALI -> "বিশেষজ্ঞ দ্বারা নিশ্চিত"
            AppLanguage.FRENCH -> "Validé par l'Expert"
        }
        ReportStatus.RESOLVED -> when (lang) {
            AppLanguage.ENGLISH -> "Treatment Completed"
            AppLanguage.HINDI -> "उपचार पूर्ण"
            AppLanguage.MARATHI -> "उपचार पूर्ण"
            AppLanguage.SPANISH -> "Tratamiento Completado"
            AppLanguage.SWAHILI -> "Matibabu Yamekamilika"
            AppLanguage.BENGALI -> "চিকিৎসা সম্পন্ন"
            AppLanguage.FRENCH -> "Traitement Terminé"
        }
    }

    fun diseaseName(name: String, lang: AppLanguage): String = when {
        name.contains("Late Blight", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Late Blight"
            AppLanguage.HINDI -> "अगेती/पिछेती झुलसा"
            AppLanguage.MARATHI -> "करपा रोग"
            AppLanguage.SPANISH -> "Tizón Tardío"
            AppLanguage.SWAHILI -> "Ukungu wa Kuchelewa"
            AppLanguage.BENGALI -> "লেইট ব্লাইট"
            AppLanguage.FRENCH -> "Mildiou de la Tomate"
        }
        name.contains("Armyworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Fall Armyworm"
            AppLanguage.HINDI -> "फॉल आर्मीवर्म / लश्करी कीट"
            AppLanguage.MARATHI -> "लष्करी अळी"
            AppLanguage.SPANISH -> "Gusano Cogollero"
            AppLanguage.SWAHILI -> "Funza wa Jeshi"
            AppLanguage.BENGALI -> "ফল আর্মিওয়ার্ম"
            AppLanguage.FRENCH -> "Légionnaire d'Automne"
        }
        name.contains("Blast", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Rice Blast"
            AppLanguage.HINDI -> "धान का झुलसा रोग"
            AppLanguage.MARATHI -> "भातावरील करपा"
            AppLanguage.SPANISH -> "Pyriculariosis del Arroz"
            AppLanguage.SWAHILI -> "Kinyaushi cha Mpunga"
            AppLanguage.BENGALI -> "রাইস ব্লাস্ট"
            AppLanguage.FRENCH -> "Pyriculariose du Riz"
        }
        name.contains("Bollworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Pink Bollworm"
            AppLanguage.HINDI -> "गुलाबी सुंडी"
            AppLanguage.MARATHI -> "गुलाबी बोंडअळी"
            AppLanguage.SPANISH -> "Gusano Rosado"
            AppLanguage.SWAHILI -> "Funza Waridi"
            AppLanguage.BENGALI -> "গোলাপী বোলওয়ার্ম"
            AppLanguage.FRENCH -> "Ver Rose"
        }
        name.contains("Rust", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> "Yellow Rust"
            AppLanguage.HINDI -> "पीला रतुआ"
            AppLanguage.MARATHI -> "पिवळा तांबेरा"
            AppLanguage.SPANISH -> "Roya Amarilla"
            AppLanguage.SWAHILI -> "Kutu ya Njano"
            AppLanguage.BENGALI -> "হলুদ মরিচা"
            AppLanguage.FRENCH -> "Rouille Jaune"
        }
        else -> name
    }

    /**
     * Localized Critical Disease Advisory Alert string across 7 supported languages.
     */
    fun criticalDiseaseAdvisoryAlert(humidity: Int = 90, lang: AppLanguage): String {
        val bnDigits = mapOf('0' to '০', '1' to '১', '2' to '২', '3' to '৩', '4' to '৪', '5' to '৫', '6' to '৬', '7' to '৭', '8' to '৮', '9' to '৯')
        val bnHumidity = if (humidity == 90) "৯০" else humidity.toString().map { bnDigits[it] ?: it }.joinToString("")
        return when (lang) {
            AppLanguage.ENGLISH -> "⚠️ Critical Late Blight & Downy Mildew spore germination risk (RH $humidity%). Delay nitrogen top-dress; apply protective bio-fungicide (Trichoderma viride @ 5g/L) or Copper Oxychloride prior to rain."
            AppLanguage.HINDI -> "⚠️ लेट ब्लाइट (झुलसा) और डाउनी मिल्ड्यू बीजाणु अंकुरण का गंभीर जोखिम (RH $humidity%)। नाइट्रोजन की टॉप-ड्रेसिंग टालें; बारिश से पहले सुरक्षात्मक जैव-कवकनाशी (ट्राइकोडर्मा विरिडी @ 5g/L) या कॉपर ऑक्सीक्लोराइड का छिड़काव करें।"
            AppLanguage.MARATHI -> "⚠️ लेट ब्लाइट (करपा) आणि डाऊनी मिल्ड्यू बीजाणू उगवणीचा गंभीर धोका (RH $humidity%). नत्राचा (युरिया) वरखत देणे टाळा; पावसापूर्वी संरक्षणात्मक जैविक बुरशीनाशक (ट्रायकोडर्मा व्हिरिडी @ 5g/L) किंवा कॉपर ऑक्सिक्लोराईडची फवारणी करा."
            AppLanguage.BENGALI -> "⚠️ লেট ব্লাইট এবং ডাউনি মিলডিউ স্পোর অঙ্কুরোদগমের মারাত্মক ঝুঁকি (RH $bnHumidity%)। নাইট্রোজেন টপ-ড্রেসিং স্থগিত রাখুন; বৃষ্টির আগে সুরক্ষামূলক জৈব ছত্রাকনাশক (ট্রাইকোডার্মা ভিরিডি @ ৫ গ্রাম/লিটার) বা কপার অক্সিক্লোরাইড স্প্রে করুন।"
            AppLanguage.SPANISH -> "⚠️ Riesgo crítico de germinación de esporas de tizón tardío y mildiu velloso (HR $humidity%). Retrase la fertilización nitrogenada; aplique biofungicida protector (Trichoderma viride @ 5g/L) o oxicloruro de cobre antes de la lluvia."
            AppLanguage.FRENCH -> "⚠️ Risque critique de germination des spores de mildiou (HR $humidity%). Retarder l'apport d'azote en couverture; appliquer un biofongicide protecteur (Trichoderma viride @ 5g/L) ou de l'oxychlorure de cuivre avant la pluie."
            AppLanguage.SWAHILI -> "⚠️ Hatari kubwa ya kuota kwa spora za ukungu wa Late Blight na Downy Mildew (Unyevu $humidity%). Ahirisha uwekaji wa mbolea ya nitrojeni; nyunyizia dawa ya kibayolojia (Trichoderma viride @ 5g/L) au Copper Oxychloride kabla ya mvua."
        }
    }

    /**
     * Localizes actionable agronomic advisories dynamically based on language preference.
     */
    fun localizeAdvisory(advisory: String, humidity: Int = 90, lang: AppLanguage): String {
        return when {
            advisory.contains("Late Blight", ignoreCase = true) ||
            advisory.contains("spore germination", ignoreCase = true) ||
            advisory.contains("Trichoderma", ignoreCase = true) ||
            advisory.contains("Copper Oxychloride", ignoreCase = true) -> {
                criticalDiseaseAdvisoryAlert(humidity, lang)
            }
            advisory.contains("High precipitation", ignoreCase = true) || advisory.contains("drainage furrows", ignoreCase = true) -> when (lang) {
                AppLanguage.ENGLISH -> "🌧️ High precipitation & prolonged leaf wetness forecast. Ensure drainage furrows and runoff trenches are cleared to prevent root hypoxia and water-soaked fungal lesions."
                AppLanguage.HINDI -> "🌧️ अत्यधिक वर्षा और पत्तियों के लगातार गीले रहने का पूर्वानुमान। जड़ों को नुकसान और फंगल संक्रमण रोकने के लिए जल निकासी नालियों को साफ रखें।"
                AppLanguage.MARATHI -> "🌧️ मुसळधार पाऊस आणि पानांवरील सततच्या ओलाव्याचा अंदाज. मुळांचे नुकसान व बुरशीजन्य रोग रोखण्यासाठी पाण्याचा निचरा करणारी चर मोकळी ठेवा."
                AppLanguage.BENGALI -> "🌧️ ভারী বৃষ্টিপাত ও দীর্ঘস্থায়ী পাতার আর্দ্রতার পূর্বাভাস। শিকড়ের ক্ষতি ও ছত্রাক আক্রমণ প্রতিরোধে নিষ্কাশন নালা পরিষ্কার রাখুন।"
                AppLanguage.SPANISH -> "🌧️ Pronóstico de alta precipitación y humedad prolongada en hojas. Limpie los surcos de drenaje para prevenir hipoxia radicular y lesiones fúngicas."
                AppLanguage.FRENCH -> "🌧️ Fortes précipitations et humectation foliaire prolongée prévues. Dégager les rigoles de drainage pour éviter l'hypoxie racinaire et le mildiou."
                AppLanguage.SWAHILI -> "🌧️ Utabiri wa mvua kubwa na unyevu wa majani. Hakikisha mifereji ya maji ni safi kuzuia kuoza kwa mizizi na magonjwa ya ukungu."
            }
            advisory.contains("Post-rain warming", ignoreCase = true) || advisory.contains("Whitefly/Armyworm", ignoreCase = true) -> when (lang) {
                AppLanguage.ENGLISH -> "⚡ Post-rain warming period accelerates both fungal leaf spots and Whitefly/Armyworm oviposition. Inspect sticky traps and spray botanical neem 10,000 ppm during early morning."
                AppLanguage.HINDI -> "⚡ बारिश के बाद तापमान वृद्धि से फंगल धब्बे और सफेद मक्खी/आर्मीवर्म का प्रकोप बढ़ता है। स्टिकी ट्रैप्स की जांच करें और सुबह नीम तेल 10,000 ppm का छिड़काव करें।"
                AppLanguage.MARATHI -> "⚡ पावसानंतर वाढणाऱ्या तापमानामुळे बुरशीजन्य डाग आणि पांढरी माशी/लष्करी अळीचा प्रादुर्भाव वाढतो. चिकट सापळे तपासा आणि सकाळी कडुनिंब तेल फवारा."
                AppLanguage.BENGALI -> "⚡ বৃষ্টির পর উষ্ণ আবহাওয়া ছত্রাক ও সাদা মাছি/আর্মিওয়ার্ম বৃদ্ধি করে। স্টিকি ট্র্যাপ পরীক্ষা করুন এবং সকালে নিম তেল স্প্রে করুন।"
                AppLanguage.SPANISH -> "⚡ El calentamiento post-lluvia acelera manchas fúngicas y la oviposición de mosca blanca. Revise trampas y aplique aceite de neem por la mañana."
                AppLanguage.FRENCH -> "⚡ Le réchauffement post-pluie accélère les taches foliaires et la ponte des aleurodes. Inspecter les pièges et pulvériser de l'huile de neem le matin."
                AppLanguage.SWAHILI -> "⚡ Kipindi cha joto baada ya mvua huongeza ukungu na wadudu waharibifu. Kagua mitego ya kunata na nyunyizia mafuta ya mwarobaini asubuhi."
            }
            advisory.contains("Heatwave threshold", ignoreCase = true) || advisory.contains("Beauveria", ignoreCase = true) -> when (lang) {
                AppLanguage.ENGLISH -> advisory
                AppLanguage.HINDI -> "🔥 अत्यधिक तापमान चूसक कीटों के जीवन चक्र को तेज करता है। पीले स्टिकी कार्ड लगाएं और ब्यूवेरिया बेसियाना का छिड़काव करें।"
                AppLanguage.MARATHI -> "🔥 अति उष्णतेमुळे रसशोषक किडींचे प्रमाण वाढते. पिवळे चिकट सापळे लावा आणि बिव्हेरिया बॅसियानाची फवारणी करा."
                AppLanguage.BENGALI -> "🔥 উচ্চ তাপমাত্রা শোষক পোকার জীবনচক্র দ্রুত করে। হলুদ স্টিকি কার্ড লাগান এবং বিউভেরিয়া বাসিয়ানা প্রয়োগ করুন।"
                AppLanguage.SPANISH -> "🔥 Ola de calor acelera insectos chupadores. Coloque trampas adhesivas amarillas y aplique Beauveria bassiana."
                AppLanguage.FRENCH -> "🔥 La canicule accélère le cycle des ravageurs piqueurs. Poser des pièges englués jaunes et appliquer Beauveria bassiana."
                AppLanguage.SWAHILI -> "🔥 Mawimbi ya joto huongeza wadudu wanyonyao. Weka mitego ya njano na nyunyizia dawa ya kibayolojia ya Beauveria bassiana."
            }
            advisory.contains("Moderate breeze", ignoreCase = true) || advisory.contains("pheromone", ignoreCase = true) -> when (lang) {
                AppLanguage.ENGLISH -> advisory
                AppLanguage.HINDI -> "🪲 मध्यम हवा कीटों के फैलाव में सहायक है। फेरोमोन ल्यूर की जांच करें और निचली पुरानी पत्तियों की छंटाई करें।"
                AppLanguage.MARATHI -> "🪲 मध्यम वाऱ्यामुळे किडींचा प्रसार वाढतो. फेरोमोन सापळे तपासा आणि खालची जुनी पाने काढून टाका."
                AppLanguage.BENGALI -> "🪲 মাঝারি বাতাস পোকার বিস্তার বাড়ায়। ফেরোমোন ফাঁদ পরীক্ষা করুন এবং পুরানো পাতা ছাঁটাই করুন।"
                AppLanguage.SPANISH -> "🪲 La brisa moderada favorece la dispersión de plagas. Revise trampas de feromonas y pode hojas bajas."
                AppLanguage.FRENCH -> "🪲 Une brise modérée favorise la dispersion des ravageurs. Vérifier les pièges à phéromones et tailler le feuillage bas."
                AppLanguage.SWAHILI -> "🪲 Upepo wa wastani husaidia kusambaa kwa wadudu. Kagua mitego ya pheromone na punguza majani ya chini."
            }
            advisory.contains("Optimal foliar", ignoreCase = true) || advisory.contains("Conduct preventive", ignoreCase = true) -> when (lang) {
                AppLanguage.ENGLISH -> advisory
                AppLanguage.HINDI -> "🌿 पर्णीय छिड़काव के लिए अनुकूल समय। सुबह 6:30 से 8:30 के बीच निवारक पोषक तत्व और जैव-नियंत्रण का छिड़काव करें।"
                AppLanguage.MARATHI -> "🌿 फवारणीसाठी अनुकूल वेळ. सकाळी 6:30 ते 8:30 दरम्यान प्रतिबंधक जैविक औषधांची फवारणी करा."
                AppLanguage.BENGALI -> "🌿 স্প্রে করার সর্বোত্তম সময়। সকাল ৬:৩০ থেকে ৮:৩০ এর মধ্যে সুরক্ষামূলক স্প্রে সম্পন্ন করুন।"
                AppLanguage.SPANISH -> "🌿 Ventana óptima de aplicación foliar. Realice la pulverización preventiva entre 6:30 AM y 8:30 AM."
                AppLanguage.FRENCH -> "🌿 Fenêtre idéale pour pulvérisation foliaire. Effectuer le traitement préventif entre 6h30 et 8h30."
                AppLanguage.SWAHILI -> "🌿 Muda muafaka wa kunyunyizia dawa. Fanya unyunyiziaji wa kinga kati ya saa 12:30 na 2:30 asubuhi."
            }
            else -> advisory
        }
    }

    fun hideDetails(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Hide details ▲"
        AppLanguage.HINDI -> "विवरण छिपाएं ▲"
        AppLanguage.MARATHI -> "तपशील लपवा ▲"
        AppLanguage.SPANISH -> "Ocultar detalles ▲"
        AppLanguage.SWAHILI -> "Ficha maelezo ▲"
        AppLanguage.BENGALI -> "বিবরণ লুকান ▲"
        AppLanguage.FRENCH -> "Masquer les détails ▲"
    }

    fun viewIpmAdvice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View IPM advice ▼"
        AppLanguage.HINDI -> "आईपीएम सलाह देखें ▼"
        AppLanguage.MARATHI -> "आयपीएम सल्ला पहा ▼"
        AppLanguage.SPANISH -> "Ver consejos de MIP ▼"
        AppLanguage.SWAHILI -> "Ona ushauri wa IPM ▼"
        AppLanguage.BENGALI -> "আইপিএম পরামর্শ দেখুন ▼"
        AppLanguage.FRENCH -> "Voir les conseils IPM ▼"
    }

    fun vulnerableCropsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Vulnerable Crops:"
        AppLanguage.HINDI -> "संवेदनशील फसलें:"
        AppLanguage.MARATHI -> "संवेदनशील पिके:"
        AppLanguage.SPANISH -> "Cultivos Vulnerables:"
        AppLanguage.SWAHILI -> "Mazao Yaliyo Hatari:"
        AppLanguage.BENGALI -> "সংবেদনশীল ফসল:"
        AppLanguage.FRENCH -> "Cultures Vulnérables :"
    }

    fun pathogenMechanismLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Pathogen Propagation Mechanism:"
        AppLanguage.HINDI -> "रोगज़नक़ प्रसार तंत्र:"
        AppLanguage.MARATHI -> "रोगकारक प्रसार यंत्रणा:"
        AppLanguage.SPANISH -> "Mecanismo de Propagación de Patógenos:"
        AppLanguage.SWAHILI -> "Utaratibu wa Kuenea kwa Ugonjwa:"
        AppLanguage.BENGALI -> "রোগজীবাণু বিস্তার প্রক্রিয়া:"
        AppLanguage.FRENCH -> "Mécanisme de Propagation du Pathogène :"
    }

    fun ipmPrecautionaryActionLabel(urgency: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "IPM Precautionary Action ($urgency):"
        AppLanguage.HINDI -> "आईपीएम एहतियाती कार्रवाई ($urgency):"
        AppLanguage.MARATHI -> "आयपीएम प्रतिबंधात्मक उपाय ($urgency):"
        AppLanguage.SPANISH -> "Acción Preventiva de MIP ($urgency):"
        AppLanguage.SWAHILI -> "Hatua ya Tahadhari ya IPM ($urgency):"
        AppLanguage.BENGALI -> "আইপিএম সতর্কতামূলক ব্যবস্থা ($urgency):"
        AppLanguage.FRENCH -> "Action Préventive IPM ($urgency) :"
    }

    fun applyWeatherRiskToAiForm(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Apply Weather Risk to AI Diagnosis Form"
        AppLanguage.HINDI -> "मौसम जोखिम को एआई निदान फॉर्म में लागू करें"
        AppLanguage.MARATHI -> "हवामान धोका एआय निदान फॉर्ममध्ये लागू करा"
        AppLanguage.SPANISH -> "Aplicar Riesgo Climático al Formulario de Diagnóstico IA"
        AppLanguage.SWAHILI -> "Tumia Hatari ya Hali ya Hewa kwenye Fomu ya AI"
        AppLanguage.BENGALI -> "আবহাওয়ার ঝুঁকি এআই ডায়াগনসিস ফর্মে প্রয়োগ করুন"
        AppLanguage.FRENCH -> "Appliquer le Risque Météo au Formulaire de Diagnostic IA"
    }

    // ==========================================
    // HOTSPOTS SCREEN LOCALIZATION
    // ==========================================
    fun regionalOutbreakAlert(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "REGIONAL OUTBREAK ALERT"
        AppLanguage.HINDI -> "क्षेत्रीय प्रकोप चेतावनी"
        AppLanguage.MARATHI -> "प्रादेशिक प्रादुर्भाव इशारा"
        AppLanguage.SPANISH -> "ALERTA DE BROTE REGIONAL"
        AppLanguage.SWAHILI -> "TAHADHORI YA MLIPUKO WA KIKANDA"
        AppLanguage.BENGALI -> "আঞ্চলিক প্রাদুর্ভাব সতর্কতা"
        AppLanguage.FRENCH -> "ALERTE ÉPIDÉMIQUE RÉGIONALE"
    }

    fun surveillanceFilters(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "SURVEILLANCE FILTERS"
        AppLanguage.HINDI -> "निगरानी फ़िल्टर"
        AppLanguage.MARATHI -> "पाळत ठेवण्याचे फिल्टर्स"
        AppLanguage.SPANISH -> "FILTROS DE VIGILANCIA"
        AppLanguage.SWAHILI -> "VICHUJIO VYA UCHUNGUZI"
        AppLanguage.BENGALI -> "নজরদারি ফিল্টার"
        AppLanguage.FRENCH -> "FILTRES DE SURVEILLANCE"
    }

    fun filterAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All"
        AppLanguage.HINDI -> "सभी"
        AppLanguage.MARATHI -> "सर्व"
        AppLanguage.SPANISH -> "Todos"
        AppLanguage.SWAHILI -> "Yote"
        AppLanguage.BENGALI -> "সমস্ত"
        AppLanguage.FRENCH -> "Tous"
    }

    fun filterTomatoPotato(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Tomato & Potato"
        AppLanguage.HINDI -> "टमाटर और आलू"
        AppLanguage.MARATHI -> "टोमॅटो आणि बटाटा"
        AppLanguage.SPANISH -> "Tomate y Papa"
        AppLanguage.SWAHILI -> "Nyanya na Viazi"
        AppLanguage.BENGALI -> "টমেটো ও আলু"
        AppLanguage.FRENCH -> "Tomate et Pomme de terre"
    }

    fun filterRice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Rice"
        AppLanguage.HINDI -> "चावल"
        AppLanguage.MARATHI -> "भात / तांदूळ"
        AppLanguage.SPANISH -> "Arroz"
        AppLanguage.SWAHILI -> "Mpunga"
        AppLanguage.BENGALI -> "ধান / চাল"
        AppLanguage.FRENCH -> "Riz"
    }

    fun filterMaize(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Maize"
        AppLanguage.HINDI -> "मक्का"
        AppLanguage.MARATHI -> "मका"
        AppLanguage.SPANISH -> "Maíz"
        AppLanguage.SWAHILI -> "Mahindi"
        AppLanguage.BENGALI -> "ভুট্টা"
        AppLanguage.FRENCH -> "Maïs"
    }

    fun filterCotton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Cotton"
        AppLanguage.HINDI -> "कपास"
        AppLanguage.MARATHI -> "कापूस"
        AppLanguage.SPANISH -> "Algodón"
        AppLanguage.SWAHILI -> "Pamba"
        AppLanguage.BENGALI -> "তুলা"
        AppLanguage.FRENCH -> "Coton"
    }

    fun filterChilli(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Chilli"
        AppLanguage.HINDI -> "मिर्च"
        AppLanguage.MARATHI -> "मिरची"
        AppLanguage.SPANISH -> "Chile / Ají"
        AppLanguage.SWAHILI -> "Pilipili"
        AppLanguage.BENGALI -> "মরিচ"
        AppLanguage.FRENCH -> "Piment"
    }

    fun filterAllSeverities(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All Severities"
        AppLanguage.HINDI -> "सभी गंभीरता स्तर"
        AppLanguage.MARATHI -> "सर्व तीव्रता"
        AppLanguage.SPANISH -> "Todas las Severidades"
        AppLanguage.SWAHILI -> "Viwango Vyote"
        AppLanguage.BENGALI -> "সমস্ত তীব্রতা"
        AppLanguage.FRENCH -> "Toutes les Gravités"
    }

    fun geospatialOutbreakSurveillance(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "GEOSPATIAL OUTBREAK SURVEILLANCE"
        AppLanguage.HINDI -> "भू-स्थानिक प्रकोप निगरानी"
        AppLanguage.MARATHI -> "भू-स्थानिक प्रादुर्भाव पाळत"
        AppLanguage.SPANISH -> "VIGILANCIA GEOESPACIAL DE BROTES"
        AppLanguage.SWAHILI -> "UCHUNGUZI WA MAENEO YA MILIPUKO"
        AppLanguage.BENGALI -> "ভৌগোলিক প্রাদুর্ভাব নজরদারি"
        AppLanguage.FRENCH -> "SURVEILLANCE GÉOSPATIALE DES ÉPIDÉMIES"
    }

    fun aggregatedFromReports(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Aggregated from $count Diagnosis Records"
        AppLanguage.HINDI -> "$count निदान रिकॉर्ड से एकत्रित"
        AppLanguage.MARATHI -> "$count रोगनिदान नोंदींवरून एकत्रित"
        AppLanguage.SPANISH -> "Agregado de $count registros de diagnóstico"
        AppLanguage.SWAHILI -> "Imekusanywa kutoka rekodi $count za utambuzi"
        AppLanguage.BENGALI -> "$count টি ডায়াগনসিস রেকর্ড থেকে সংকলিত"
        AppLanguage.FRENCH -> "Agrégé à partir de $count dossiers de diagnostic"
    }

    fun gpsRadarSurveillance(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "GPS RADAR SURVEILLANCE"
        AppLanguage.HINDI -> "जीपीएस रडार निगरानी"
        AppLanguage.MARATHI -> "जीपीएस रडार पाळत"
        AppLanguage.SPANISH -> "VIGILANCIA POR RADAR GPS"
        AppLanguage.SWAHILI -> "UCHUNGUZI WA RADA YA GPS"
        AppLanguage.BENGALI -> "জিপিএস রাডার নজরদারি"
        AppLanguage.FRENCH -> "SURVEILLANCE RADAR GPS"
    }

    fun tapNodesToInspect(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Tap nodes to inspect"
        AppLanguage.HINDI -> "जांच के लिए नोड्स पर टैप करें"
        AppLanguage.MARATHI -> "तपासण्यासाठी नोड्सवर टॅप करा"
        AppLanguage.SPANISH -> "Toque los nodos para inspeccionar"
        AppLanguage.SWAHILI -> "Gusa alama kukagua"
        AppLanguage.BENGALI -> "পরীক্ষা করতে নোড স্পর্শ করুন"
        AppLanguage.FRENCH -> "Appuyez sur les nœuds pour inspecter"
    }

    fun activeClustersLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Active Clusters"
        AppLanguage.HINDI -> "सक्रिय क्लस्टर"
        AppLanguage.MARATHI -> "सक्रिय क्लस्टर्स"
        AppLanguage.SPANISH -> "Grupos Activos"
        AppLanguage.SWAHILI -> "Vikundi Hai"
        AppLanguage.BENGALI -> "সক্রিয় ক্লাস্টার"
        AppLanguage.FRENCH -> "Foyers Actifs"
    }

    fun severeOutbreaksLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Severe Outbreaks"
        AppLanguage.HINDI -> "गंभीर प्रकोप"
        AppLanguage.MARATHI -> "तीव्र प्रादुर्भाव"
        AppLanguage.SPANISH -> "Brotes Severos"
        AppLanguage.SWAHILI -> "Milipuko Mikali"
        AppLanguage.BENGALI -> "তীব্র প্রাদুর্ভাব"
        AppLanguage.FRENCH -> "Épidémies Sévères"
    }

    fun diagnosisCasesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnosis Cases"
        AppLanguage.HINDI -> "निदान मामले"
        AppLanguage.MARATHI -> "रोगनिदान प्रकरणे"
        AppLanguage.SPANISH -> "Casos Diagnosticados"
        AppLanguage.SWAHILI -> "Kesi za Utambuzi"
        AppLanguage.BENGALI -> "ডায়াগনসিস কেস"
        AppLanguage.FRENCH -> "Cas Diagnostiqués"
    }

    fun maxRadiusLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Max Radius"
        AppLanguage.HINDI -> "अधिकतम त्रिज्या"
        AppLanguage.MARATHI -> "कमाल त्रिज्या"
        AppLanguage.SPANISH -> "Radio Máximo"
        AppLanguage.SWAHILI -> "Kipenyo cha Juu"
        AppLanguage.BENGALI -> "সর্বোচ্চ ব্যাসার্ধ"
        AppLanguage.FRENCH -> "Rayon Maximum"
    }

    fun regionalEpidemicSectorMatrix(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "REGIONAL EPIDEMIC SECTOR MATRIX"
        AppLanguage.HINDI -> "क्षेत्रीय महामारी सेक्टर मैट्रिक्स"
        AppLanguage.MARATHI -> "प्रादेशिक रोग प्रादुर्भाव सेक्टर मॅट्रिक्स"
        AppLanguage.SPANISH -> "MATRIZ DE SECTORES EPIDÉMICOS REGIONALES"
        AppLanguage.SWAHILI -> "TATHMINI YA KIKANDA YA SEKTA ZA MARADHI"
        AppLanguage.BENGALI -> "আঞ্চলিক মহামারী সেক্টর ম্যাট্রিক্স"
        AppLanguage.FRENCH -> "MATRICE DES SECTEURS ÉPIDÉMIQUES RÉGIONAUX"
    }

    fun mapRadarMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Map Radar"
        AppLanguage.HINDI -> "मानचित्र रडार"
        AppLanguage.MARATHI -> "नकाशा रडार"
        AppLanguage.SPANISH -> "Radar de Mapa"
        AppLanguage.SWAHILI -> "Rada ya Ramani"
        AppLanguage.BENGALI -> "ম্যাপ রাডার"
        AppLanguage.FRENCH -> "Radar Carte"
    }

    fun sectorGridMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sector Grid"
        AppLanguage.HINDI -> "सेक्टर ग्रिड"
        AppLanguage.MARATHI -> "सेक्टर ग्रिड"
        AppLanguage.SPANISH -> "Cuadrícula de Sectores"
        AppLanguage.SWAHILI -> "Gridi ya Sekta"
        AppLanguage.BENGALI -> "সেক্টর গ্রিড"
        AppLanguage.FRENCH -> "Grille Sectorielle"
    }

    fun activeSectorsCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Active Sectors"
        AppLanguage.HINDI -> "$count सक्रिय सेक्टर"
        AppLanguage.MARATHI -> "$count सक्रिय सेक्टर्स"
        AppLanguage.SPANISH -> "$count Sectores Activos"
        AppLanguage.SWAHILI -> "Sekta Hai $count"
        AppLanguage.BENGALI -> "$count টি সক্রিয় সেক্টর"
        AppLanguage.FRENCH -> "$count Secteurs Actifs"
    }

    fun rangeRadiusLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Radius: 0 - 5.0 km"
        AppLanguage.HINDI -> "दायरा: 0 - 5.0 किमी"
        AppLanguage.MARATHI -> "त्रिज्या: ० - ५.० किमी"
        AppLanguage.SPANISH -> "Radio: 0 - 5.0 km"
        AppLanguage.SWAHILI -> "Eneo: km 0 - 5.0"
        AppLanguage.BENGALI -> "ব্যাসার্ধ: ০ - ৫.০ কিমি"
        AppLanguage.FRENCH -> "Rayon : 0 - 5,0 km"
    }

    fun regionalClustersCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Regional Clusters"
        AppLanguage.HINDI -> "$count क्षेत्रीय क्लस्टर"
        AppLanguage.MARATHI -> "$count प्रादेशिक क्लस्टर्स"
        AppLanguage.SPANISH -> "$count Grupos Regionales"
        AppLanguage.SWAHILI -> "Vikundi $count vya Kikanda"
        AppLanguage.BENGALI -> "$count টি আঞ্চলিক ক্লাস্টার"
        AppLanguage.FRENCH -> "$count Foyers Régionaux"
    }

    fun confirmedFarmCasesIn48h(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count confirmed farm cases in 48 hrs"
        AppLanguage.HINDI -> "48 घंटों में $count पुष्ट खेत मामले"
        AppLanguage.MARATHI -> "४८ तासांत $count पुष्टी झालेली शेत प्रकरणे"
        AppLanguage.SPANISH -> "$count casos de granja confirmados en 48 h"
        AppLanguage.SWAHILI -> "Kesi $count za mashambani zimethibitishwa kwa saa 48"
        AppLanguage.BENGALI -> "৪৮ ঘণ্টায় $count টি নিশ্চিত খামারের কেস"
        AppLanguage.FRENCH -> "$count cas confirmés en 48h"
    }

    fun confirmedFarmDiagnoses(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Confirmed Farm Diagnoses"
        AppLanguage.HINDI -> "$count पुष्ट खेत निदान"
        AppLanguage.MARATHI -> "$count पुष्टी झालेले शेत रोगनिदान"
        AppLanguage.SPANISH -> "$count Diagnósticos de Granja Confirmados"
        AppLanguage.SWAHILI -> "Utambuzi $count wa Mashambani Uliothibitishwa"
        AppLanguage.BENGALI -> "$count টি নিশ্চিত খামার ডায়াগনসিস"
        AppLanguage.FRENCH -> "$count Diagnostics de Ferme Confirmés"
    }

    fun targetCropLabel(crop: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Target Crop: $crop"
        AppLanguage.HINDI -> "लक्षित फसल: $crop"
        AppLanguage.MARATHI -> "लक्षित पीक: $crop"
        AppLanguage.SPANISH -> "Cultivo Objetivo: $crop"
        AppLanguage.SWAHILI -> "Zao Lengwa: $crop"
        AppLanguage.BENGALI -> "লক্ষ্য ফসল: $crop"
        AppLanguage.FRENCH -> "Culture Ciblée : $crop"
    }

    fun casesCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count cases"
        AppLanguage.HINDI -> "$count मामले"
        AppLanguage.MARATHI -> "$count प्रकरणे"
        AppLanguage.SPANISH -> "$count casos"
        AppLanguage.SWAHILI -> "kesi $count"
        AppLanguage.BENGALI -> "$count টি কেস"
        AppLanguage.FRENCH -> "$count cas"
    }

    fun savedDiagnosesInSector(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "SAVED DIAGNOSES IN THIS SECTOR ($count)"
        AppLanguage.HINDI -> "इस सेक्टर में सहेजे गए निदान ($count)"
        AppLanguage.MARATHI -> "या सेक्टरमधील सेव्ह केलेले रोगनिदान ($count)"
        AppLanguage.SPANISH -> "DIAGNÓSTICOS GUARDADOS EN ESTE SECTOR ($count)"
        AppLanguage.SWAHILI -> "UTAMBUZI ULIOHIFADHIWA KWENYE SEKTA HII ($count)"
        AppLanguage.BENGALI -> "এই সেক্টরে সংরক্ষিত ডায়াগনসিস ($count)"
        AppLanguage.FRENCH -> "DIAGNOSTICS ENREGISTRÉS DANS CE SECTEUR ($count)"
    }

    fun officialEpidemicHotspots(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "OFFICIAL EPIDEMIC HOTSPOTS ($count)"
        AppLanguage.HINDI -> "आधिकारिक महामारी हॉटस्पॉट ($count)"
        AppLanguage.MARATHI -> "अधिकृत रोग प्रादुर्भाव हॉटस्पॉट्स ($count)"
        AppLanguage.SPANISH -> "ZONAS CRÍTICAS EPIDÉMICAS OFICIALES ($count)"
        AppLanguage.SWAHILI -> "MAENEO HATARI RASMI YA MILIPUKO ($count)"
        AppLanguage.BENGALI -> "অফিসিয়াল মহামারী হটস্পট ($count)"
        AppLanguage.FRENCH -> "POINTS CHAUDS ÉPIDÉMIQUES OFFICIELS ($count)"
    }

    fun activeOutbreakAlertAdvisory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Active outbreak alert: immediate containment, fungicide rotation, and quarantine barrier advised."
        AppLanguage.HINDI -> "सक्रिय प्रकोप चेतावनी: तत्काल रोकथाम, फफूंदनाशी चक्रण और संगरोध बाधा की सलाह दी जाती है।"
        AppLanguage.MARATHI -> "सक्रिय प्रादुर्भाव इशारा: त्वरित नियंत्रण, बुरशीनाशक फेरबदल आणि प्रतिबंधक अडथळा सल्ला दिला जातो."
        AppLanguage.SPANISH -> "Alerta de brote activo: se aconseja contención inmediata, rotación de fungicidas y barrera de cuarentena."
        AppLanguage.SWAHILI -> "Tahadhari ya mlipuko: udhibiti wa haraka, mzunguko wa dawa na kizuizi cha karantini vinashauriwa."
        AppLanguage.BENGALI -> "সক্রিয় প্রাদুর্ভাব সতর্কতা: অবিলম্বে নিয়ন্ত্রণ, ছত্রাকনাশক পরিবর্তন এবং কোয়ারেন্টাইন ব্যবস্থা নেওয়ার পরামর্শ।"
        AppLanguage.FRENCH -> "Alerte d'épidémie active : confinement immédiat, rotation des fongicides et barrière sanitaire conseillés."
    }

    fun localizeZoneName(zone: String, lang: AppLanguage): String = when {
        zone.contains("Alpha", ignoreCase = true) || zone.contains("Khed", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> zone
            AppLanguage.HINDI -> "जोन अल्फा - खेड रिज"
            AppLanguage.MARATHI -> "झोन अल्फा - खेड रिज"
            AppLanguage.BENGALI -> "জোন আলফা - খেদ রিজ"
            AppLanguage.SPANISH -> "Zona Alfa - Cordillera Khed"
            AppLanguage.FRENCH -> "Zone Alpha - Crête de Khed"
            AppLanguage.SWAHILI -> "Ukanda wa Alpha - Safu ya Khed"
        }
        zone.contains("Beta", ignoreCase = true) || zone.contains("Valley", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> zone
            AppLanguage.HINDI -> "जोन बीटा - ईस्ट वैली"
            AppLanguage.MARATHI -> "झोन बीटा - पूर्व व्हॅली"
            AppLanguage.BENGALI -> "জোন বিটা - পূর্ব উপত্যকা"
            AppLanguage.SPANISH -> "Zona Beta - Valle del Este"
            AppLanguage.FRENCH -> "Zone Bêta - Vallée Est"
            AppLanguage.SWAHILI -> "Ukanda wa Beta - Bonde la Mashariki"
        }
        zone.contains("Gamma", ignoreCase = true) || zone.contains("Delta", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> zone
            AppLanguage.HINDI -> "जोन गामा - डेल्टा लोलैंड्स"
            AppLanguage.MARATHI -> "झोन गॅमा - डेल्टा सखल भाग"
            AppLanguage.BENGALI -> "জোন গামা - বদ্বীপ নিচুভূমি"
            AppLanguage.SPANISH -> "Zona Gamma - Tierras Bajas del Delta"
            AppLanguage.FRENCH -> "Zone Gamma - Basses-terres du Delta"
            AppLanguage.SWAHILI -> "Ukanda wa Gamma - Nyanda za Chini za Delta"
        }
        zone.contains("Delta", ignoreCase = true) || zone.contains("Uplands", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> zone
            AppLanguage.HINDI -> "जोन डेल्टा - दक्षिणी उच्चभूमि"
            AppLanguage.MARATHI -> "झोन डेल्टा - दक्षिण पठार"
            AppLanguage.BENGALI -> "জোন ডেল্টা - দক্ষিণ উচ্চভূমি"
            AppLanguage.SPANISH -> "Zona Delta - Tierras Altas del Sur"
            AppLanguage.FRENCH -> "Zone Delta - Hauts Plateaux du Sud"
            AppLanguage.SWAHILI -> "Ukanda wa Delta - Nyanda za Juu za Kusini"
        }
        else -> zone
    }

    fun localizeHotspotTitle(title: String, lang: AppLanguage): String = when {
        title.contains("Late Blight", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "लेट ब्लाइट (झुलसा) महामारी क्लस्टर"
            AppLanguage.MARATHI -> "लेट ब्लाइट (करपा) प्रादुर्भाव क्लस्टर"
            AppLanguage.BENGALI -> "লেট ব্লাইট মহামারী ক্লাস্টার"
            AppLanguage.SPANISH -> "Foco Epidémico de Tizón Tardío"
            AppLanguage.FRENCH -> "Foyer Épidémique de Mildiou"
            AppLanguage.SWAHILI -> "Kikundi cha Mlipuko wa Ukungu wa Majani"
        }
        title.contains("Armyworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "फॉल आर्मीवर्म (सैनिक कीट) प्रकोप"
            AppLanguage.MARATHI -> "लष्करी अळी (फॉल आर्मीवर्म) प्रादुर्भाव"
            AppLanguage.BENGALI -> "ফল আর্মিওয়ার্ম প্রাদুর্ভাব"
            AppLanguage.SPANISH -> "Brote de Cogollero del Maíz"
            AppLanguage.FRENCH -> "Épidémie de Légionnaire d'Automne"
            AppLanguage.SWAHILI -> "Mlipuko wa Viwavi Jeshi"
        }
        title.contains("Bacterial Leaf Streak", ignoreCase = true) || title.contains("Blast", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "जीवाणु पत्ती धारी और ब्लास्ट प्रकोप"
            AppLanguage.MARATHI -> "जिवाणू करपा व ब्लास्ट प्रादुर्भाव"
            AppLanguage.BENGALI -> "ব্যাকটেরিয়াল পাতার দাগ ও ব্লাস্ট"
            AppLanguage.SPANISH -> "Estría Bacteriana y Piricularia del Arroz"
            AppLanguage.FRENCH -> "Strie Bactérienne et Pyriculariose"
            AppLanguage.SWAHILI -> "Milia ya Bakteria na Ukungu wa Mpunga"
        }
        title.contains("Bollworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "गुलाबी सुंडी (पिंक बॉलवर्म) निगरानी"
            AppLanguage.MARATHI -> "गुलाबी बोंडअळी पाळत व नियंत्रण"
            AppLanguage.BENGALI -> "গোলাপী বোলওয়ার্ম নজরদারি"
            AppLanguage.SPANISH -> "Vigilancia del Gusano Rosado"
            AppLanguage.FRENCH -> "Surveillance du Ver Rose du Cotonnier"
            AppLanguage.SWAHILI -> "Uchunguzi wa Funza wa Pamba"
        }
        else -> title
    }

    fun localizeHotspotAdvisory(advisory: String, lang: AppLanguage): String = when {
        advisory.contains("Delay nitrogen", ignoreCase = true) || advisory.contains("Trichoderma", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> advisory
            AppLanguage.HINDI -> "नाइट्रोजन टॉप-ड्रेसिंग टालें; सुरक्षात्मक ट्राइकोडर्मा विरिडी या कॉपर ऑक्सीक्लोराइड का छिड़काव करें।"
            AppLanguage.MARATHI -> "नायट्रोजन खत देणे पुढे ढकला; संरक्षक ट्रायकोडर्मा किंवा कॉपर ऑक्सिक्लोराईड फवारा."
            AppLanguage.BENGALI -> "নাইট্রোজেন প্রয়োগ স্থগিত রাখুন; সুরক্ষামূলক ট্রাইকোডার্মা বা কপার অক্সিক্লোরাইড স্প্রে করুন।"
            AppLanguage.SPANISH -> "Retrase la fertilización nitrogenada; aplique Trichoderma o oxicloruro de cobre preventivo."
            AppLanguage.FRENCH -> "Différer l'apport d'azote ; appliquer Trichoderma ou de l'oxychlorure de cuivre préventif."
            AppLanguage.SWAHILI -> "Ahirisha mbolea ya naitrojeni; weka dawa ya kinga ya Trichoderma au kopa kabla ya mvua."
        }
        advisory.contains("Pheromone", ignoreCase = true) || advisory.contains("Azadirachtin", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> advisory
            AppLanguage.HINDI -> "फेरोमोन ट्रैप लगाएं (5/एकड़) और मक्का के पौधे के घेरे में एज़ाडिराक्टिन 10,000 ppm डालें।"
            AppLanguage.MARATHI -> "फेरोमोन सापळे लावा (५/एकर) आणि मक्याच्या पोंग्यात अझाडिराक्टिन १०,००० ppm टाका."
            AppLanguage.BENGALI -> "ফেরোমোন ফাঁদ লাগান (৫টি/একর) এবং ভুট্টার পাতায় অ্যাজাডিরাকটিন ১০,০০০ পিপিএম দিন।"
            AppLanguage.SPANISH -> "Instale trampas de feromonas (5/acre) y aplique Azadiractina 10,000 ppm en el cogollo."
            AppLanguage.FRENCH -> "Installer des pièges à phéromones (5/acre) et appliquer de l'Azadirachtine 10 000 ppm dans le cornet."
            AppLanguage.SWAHILI -> "Weka mitego ya pheromone (5 kwa ekari) na weka Azadirachtin ppm 10,000 kwenye majani ya mahindi."
        }
        advisory.contains("Drain standing water", ignoreCase = true) || advisory.contains("Pseudomonas", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> advisory
            AppLanguage.HINDI -> "खेत से जमा पानी निकालें; स्यूडोमोनास फ्लोरोसेंस @ 5g/L का छिड़काव करें।"
            AppLanguage.MARATHI -> "साचलेले पाणी शेतातून काढून टाका; स्यूडोमोनस फ्लोरोसन्स @ ५ ग्रॅम/लिटर फवारा."
            AppLanguage.BENGALI -> "জমে থাকা পানি নিষ্কাশন করুন; সিউডোমোনাস ফ্লুরোসেন্স @ ৫ গ্রাম/লিটার স্প্রে করুন।"
            AppLanguage.SPANISH -> "Drene el agua estancada; pulverice Pseudomonas fluorescens a 5 g/L."
            AppLanguage.FRENCH -> "Drainer l'eau stagnante ; pulvériser Pseudomonas fluorescens à 5 g/L."
            AppLanguage.SWAHILI -> "Ondoa maji yaliyotuama shambani; nyunyizia Pseudomonas fluorescens gramu 5/L."
        }
        advisory.contains("sticky traps", ignoreCase = true) || advisory.contains("Beauveria", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> advisory
            AppLanguage.HINDI -> "पीले स्टिकी ट्रैप लगाएं और शाम के समय ब्यूवेरिया बेसियाना का छिड़काव करें।"
            AppLanguage.MARATHI -> "पिवळे चिकट सापळे लावा आणि संध्याकाळी बिव्हेरिया बॅसियानाची फवारणी करा."
            AppLanguage.BENGALI -> "হলুদ স্টিকি ফাঁদ লাগান এবং সন্ধ্যায় বিউভেরিয়া বাসিয়ানা স্প্রে করুন।"
            AppLanguage.SPANISH -> "Instale trampas adhesivas amarillas y pulverice Beauveria bassiana al atardecer."
            AppLanguage.FRENCH -> "Poser des pièges englués jaunes et pulvériser Beauveria bassiana en soirée."
            AppLanguage.SWAHILI -> "Weka mitego ya njano ya kunata na nyunyizia Beauveria bassiana nyakati za jioni."
        }
        else -> advisory
    }

    // ==========================================
    // IPM DECISION ADVISOR SCREEN LOCALIZATION
    // ==========================================
    fun diagnosticQuestionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "DIAGNOSTIC QUESTION"
        AppLanguage.HINDI -> "निदान प्रश्न"
        AppLanguage.MARATHI -> "रोगनिदान प्रश्न"
        AppLanguage.SPANISH -> "PREGUNTA DE DIAGNÓSTICO"
        AppLanguage.SWAHILI -> "SWALI LA UTAMBUZI"
        AppLanguage.BENGALI -> "ডায়াগনস্টিক প্রশ্ন"
        AppLanguage.FRENCH -> "QUESTION DE DIAGNOSTIC"
    }

    fun decisionPathTitle(steps: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "DECISION PATH ($steps STEPS)"
        AppLanguage.HINDI -> "निर्णय पथ ($steps चरण)"
        AppLanguage.MARATHI -> "निर्णय मार्ग ($steps टप्पे)"
        AppLanguage.SPANISH -> "RUTA DE DECISIÓN ($steps PASOS)"
        AppLanguage.SWAHILI -> "NJIA YA UAMUZI (HATUA $steps)"
        AppLanguage.BENGALI -> "সিদ্ধান্তের পথ ($steps টি ধাপ)"
        AppLanguage.FRENCH -> "CHEMIN DE DÉCISION ($steps ÉTAPES)"
    }

    fun backStep(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Back"
        AppLanguage.HINDI -> "पीछे"
        AppLanguage.MARATHI -> "मागे"
        AppLanguage.SPANISH -> "Atrás"
        AppLanguage.SWAHILI -> "Nyuma"
        AppLanguage.BENGALI -> "পেছনে"
        AppLanguage.FRENCH -> "Retour"
    }

    fun resetTree(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Reset"
        AppLanguage.HINDI -> "रीसेट"
        AppLanguage.MARATHI -> "रीसेट"
        AppLanguage.SPANISH -> "Reiniciar"
        AppLanguage.SWAHILI -> "Weka Upya"
        AppLanguage.BENGALI -> "রিসেট"
        AppLanguage.FRENCH -> "Réinitialiser"
    }

    fun rootNode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "🌱 Root"
        AppLanguage.HINDI -> "🌱 मूल"
        AppLanguage.MARATHI -> "🌱 मूळ"
        AppLanguage.SPANISH -> "🌱 Inicio"
        AppLanguage.SWAHILI -> "🌱 Mwanzo"
        AppLanguage.BENGALI -> "🌱 শুরু"
        AppLanguage.FRENCH -> "🌱 Racine"
    }

    fun selectMatchingObservation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Select matching observation:"
        AppLanguage.HINDI -> "मिलता-जुलता लक्षण चुनें:"
        AppLanguage.MARATHI -> "जुळणारे लक्षण निवडा:"
        AppLanguage.SPANISH -> "Seleccione la observación correspondiente:"
        AppLanguage.SWAHILI -> "Chagua dalili inayolingana:"
        AppLanguage.BENGALI -> "মিলে যাওয়া পর্যবেক্ষণ নির্বাচন করুন:"
        AppLanguage.FRENCH -> "Sélectionnez l'observation correspondante :"
    }

    fun protocolReady(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "PROTOCOL READY"
        AppLanguage.HINDI -> "प्रोटोकॉल तैयार है"
        AppLanguage.MARATHI -> "प्रोटोकॉल तयार आहे"
        AppLanguage.SPANISH -> "PROTOCOLO LISTO"
        AppLanguage.SWAHILI -> "MWONGOZO TAYARI"
        AppLanguage.BENGALI -> "প্রোটোকল প্রস্তুত"
        AppLanguage.FRENCH -> "PROTOCOLE PRÊT"
    }

    fun directIpmSearchMatches(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "DIRECT IPM SEARCH MATCHES ($count)"
        AppLanguage.HINDI -> "प्रत्यक्ष आईपीएम खोज परिणाम ($count)"
        AppLanguage.MARATHI -> "थेट आयपीएम शोध परिणाम ($count)"
        AppLanguage.SPANISH -> "COINCIDENCIAS DIRECTAS DE MIP ($count)"
        AppLanguage.SWAHILI -> "MATOKEO YA MOJA KWA MOJA YA IPM ($count)"
        AppLanguage.BENGALI -> "সরাসরি আইপিএম অনুসন্ধান ফলাফল ($count)"
        AppLanguage.FRENCH -> "CORRESPONDANCES DIRECTES IPM ($count)"
    }

    fun noIpmMatches(query: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No IPM protocols directly match '$query'. Explore options below or try broader terms."
        AppLanguage.HINDI -> "'$query' से कोई सीधा आईपीएम प्रोटोकॉल नहीं मिला। नीचे दिए गए विकल्प देखें या अन्य शब्द आज़माएं।"
        AppLanguage.MARATHI -> "'$query' शी कोणताही थेट आयपीएम प्रोटोकॉल जुळत नाही. खालील पर्याय पहा किंवा इतर शब्द शोधा."
        AppLanguage.SPANISH -> "Ningún protocolo de MIP coincide directamente con '$query'. Explore las opciones abajo."
        AppLanguage.SWAHILI -> "Hakuna mwongozo wa IPM unaolingana na '$query'. Chunguza chaguo hapa chini."
        AppLanguage.BENGALI -> "'$query' এর সাথে সরাসরি কোনো আইপিএম প্রোটোকল মেলেনি। নিচের বিকল্পগুলি দেখুন।"
        AppLanguage.FRENCH -> "Aucun protocole IPM ne correspond directement à '$query'. Explorez les options ci-dessous."
    }

    fun backToTree(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Back to Tree"
        AppLanguage.HINDI -> "वृक्ष पर वापस जाएं"
        AppLanguage.MARATHI -> "वृक्षावर परत जा"
        AppLanguage.SPANISH -> "Volver al Árbol"
        AppLanguage.SWAHILI -> "Rudi kwenye Mti"
        AppLanguage.BENGALI -> "বৃক্ষে ফিরে যান"
        AppLanguage.FRENCH -> "Retour à l'Arbre"
    }

    fun newDecisionTree(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "New Decision Tree"
        AppLanguage.HINDI -> "नया निर्णय वृक्ष"
        AppLanguage.MARATHI -> "नवीन निर्णय वृक्ष"
        AppLanguage.SPANISH -> "Nuevo Árbol de Decisión"
        AppLanguage.SWAHILI -> "Mti Mpya wa Uamuzi"
        AppLanguage.BENGALI -> "নতুন সিদ্ধান্তের বৃক্ষ"
        AppLanguage.FRENCH -> "Nouvel Arbre de Décision"
    }

    fun applyToDiagnosisBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Apply Protocol to Diagnosis Form"
        AppLanguage.HINDI -> "प्रोटोकॉल को निदान फॉर्म में लागू करें"
        AppLanguage.MARATHI -> "प्रोटोकॉल रोगनिदान फॉर्ममध्ये लागू करा"
        AppLanguage.SPANISH -> "Aplicar Protocolo al Formulario"
        AppLanguage.SWAHILI -> "Tumia Mwongozo kwenye Fomu ya Utambuzi"
        AppLanguage.BENGALI -> "প্রোটোকল ডায়াগনসিস ফর্মে প্রয়োগ করুন"
        AppLanguage.FRENCH -> "Appliquer le Protocole au Diagnostic"
    }

    fun protocolDiagnosisHeroSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Diagnostic Result & Standard 4-Tier IPM Protocol"
        AppLanguage.HINDI -> "निदान परिणाम और मानक 4-स्तरीय आईपीएम प्रोटोकॉल"
        AppLanguage.MARATHI -> "रोगनिदान निकाल आणि मानक ४-स्तरीय आयपीएम प्रोटोकॉल"
        AppLanguage.SPANISH -> "Resultado Diagnóstico y Protocolo MIP de 4 Niveles"
        AppLanguage.SWAHILI -> "Matokeo ya Utambuzi na Mwongozo wa Hatua 4 za IPM"
        AppLanguage.BENGALI -> "ডায়াগনস্টিক ফলাফল এবং মানক ৪-স্তরীয় আইপিএম প্রোটোকল"
        AppLanguage.FRENCH -> "Résultat Diagnostique et Protocole IPM à 4 Niveaux"
    }

    fun culturalSanitationHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "1. CULTURAL & AGRONOMIC CONTROLS"
        AppLanguage.HINDI -> "1. कृषि एवं सांस्कृतिक नियंत्रण"
        AppLanguage.MARATHI -> "१. मशागत व कृषी पद्धती नियंत्रण"
        AppLanguage.SPANISH -> "1. CONTROLES CULTURALES Y AGRONÓMICOS"
        AppLanguage.SWAHILI -> "1. MBINU ZA KIKILIMO NA MAZINGIRA"
        AppLanguage.BENGALI -> "১. কৃষি ও পরিবেশগত নিয়ন্ত্রণ"
        AppLanguage.FRENCH -> "1. CONTRÔLES CULTURAUX ET AGRONOMIQUES"
    }

    fun mechanicalPhysicalHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "2. MECHANICAL & PHYSICAL BARRIERS"
        AppLanguage.HINDI -> "2. यांत्रिक और भौतिक बाधाएं"
        AppLanguage.MARATHI -> "२. यांत्रिक आणि भौतिक अडथळे"
        AppLanguage.SPANISH -> "2. BARRERAS FÍSICAS Y MECÁNICAS"
        AppLanguage.SWAHILI -> "2. VIZUIZI VYA KIMITAMBO NA KIMWILI"
        AppLanguage.BENGALI -> "২. যান্ত্রিক ও শারীরিক বাধা"
        AppLanguage.FRENCH -> "2. BARRIÈRES MÉCANIQUES ET PHYSIQUES"
    }

    fun biologicalMicrobialHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "3. BIOLOGICAL & BOTANICAL BIO-CONTROLS"
        AppLanguage.HINDI -> "3. जैविक और वानस्पतिक जैव-नियंत्रण"
        AppLanguage.MARATHI -> "३. जैविक आणि वनस्पतीजन्य नियंत्रण"
        AppLanguage.SPANISH -> "3. BIOCONTROLADORES BIOLÓGICOS Y BOTÁNICOS"
        AppLanguage.SWAHILI -> "3. DAWA ZA KIBAYOLOJIA NA ZA ASILI"
        AppLanguage.BENGALI -> "৩. জৈবিক ও ভেষজ বায়ো-কন্ট্রোল"
        AppLanguage.FRENCH -> "3. BIOCONTRÔLES BIOLOGIQUES ET BOTANIQUES"
    }

    fun targetedChemicalHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "4. TARGETED CHEMICAL INTERVENTIONS (LAST RESORT)"
        AppLanguage.HINDI -> "4. लक्षित रासायनिक हस्तक्षेप (अंतिम उपाय)"
        AppLanguage.MARATHI -> "४. लक्षित रासायनिक उपाययोजना (शेवटचा पर्याय)"
        AppLanguage.SPANISH -> "4. INTERVENCIONES QUÍMICAS DIRIGIDAS (ÚLTIMO RECURSO)"
        AppLanguage.SWAHILI -> "4. DAWA ZA KIKEMIKALI ZILIZOLENGWA (CHAGUO LA MWISHO)"
        AppLanguage.BENGALI -> "৪. লক্ষ্যভিত্তিক রাসায়নিক প্রয়োগ (সর্বশেষ উপায়)"
        AppLanguage.FRENCH -> "4. INTERVENTIONS CHIMIQUES CIBLÉES (DERNIER RECOURS)"
    }

    fun preventionChecklistHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "5. FIELD PREVENTION & EPIDEMIOLOGY CHECKLIST"
        AppLanguage.HINDI -> "5. खेत रोकथाम और महामारी विज्ञान चेकलिस्ट"
        AppLanguage.MARATHI -> "५. शेतातील प्रतिबंध व रोग नियंत्रण चेकलिस्ट"
        AppLanguage.SPANISH -> "5. LISTA DE PREVENCIÓN Y EPIDEMIOLOGÍA EN CAMPO"
        AppLanguage.SWAHILI -> "5. ORODHA YA KUZUIA MAGONJWA SHAMBANI"
        AppLanguage.BENGALI -> "৫. খামার প্রতিরোধ ও মহামারী চেকলিস্ট"
        AppLanguage.FRENCH -> "5. LISTE DE CONTRÔLE PRÉVENTION ET ÉPIDÉMIOLOGIE"
    }

    fun preHarvestSafetyTimerHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "PRE-HARVEST INTERVAL (PHI) & SAFETY"
        AppLanguage.HINDI -> "कटाई पूर्व अंतराल (PHI) और सुरक्षा"
        AppLanguage.MARATHI -> "कापणीपूर्व अंतर (PHI) आणि सुरक्षितता"
        AppLanguage.SPANISH -> "INTERVALO PRE-COSECHA (PHI) Y SEGURIDAD"
        AppLanguage.SWAHILI -> "MUDA WA KABLA YA KUVUNA (PHI) NA USALAMA"
        AppLanguage.BENGALI -> "ফসল কাটার পূর্ববর্তী ব্যবধান (PHI) ও নিরাপত্তা"
        AppLanguage.FRENCH -> "DÉLAI AVANT RÉCOLTE (DAR) ET SÉCURITÉ"
    }

    fun phiDaysBadge(days: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$days Days PHI"
        AppLanguage.HINDI -> "$days दिन PHI"
        AppLanguage.MARATHI -> "$days दिवस PHI"
        AppLanguage.SPANISH -> "$days Días PHI"
        AppLanguage.SWAHILI -> "Siku $days za PHI"
        AppLanguage.BENGALI -> "$days দিন PHI"
        AppLanguage.FRENCH -> "$days Jours DAR"
    }

    fun localizeDecisionQuestion(nodeId: String, defaultQ: String, lang: AppLanguage): String = when {
        nodeId == "root" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "आप किस फसल समूह का निरीक्षण कर रहे हैं?"
            AppLanguage.MARATHI -> "तुम्ही कोणत्या पीक गटाची पाहणी करत आहात?"
            AppLanguage.BENGALI -> "আপনি কোন ফসলের দল পর্যবেক্ষণ করছেন?"
            AppLanguage.SPANISH -> "¿Qué grupo de cultivos está inspeccionando?"
            AppLanguage.FRENCH -> "Quel groupe de cultures inspectez-vous ?"
            AppLanguage.SWAHILI -> "Unakagua kundi gani la mazao?"
        }
        nodeId == "node_solanaceous_part" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "टमाटर/आलू के पौधे पर मुख्य लक्षण कहाँ दिखाई दे रहे हैं?"
            AppLanguage.MARATHI -> "टोमॅटो/बटाट्याच्या रोपावर मुख्य लक्षणे कुठे दिसत आहेत?"
            AppLanguage.BENGALI -> "টমেটো/আলু গাছে প্রাথমিক লক্ষণগুলি কোথায় দেখা যাচ্ছে?"
            AppLanguage.SPANISH -> "¿Dónde son visibles los síntomas primarios en la planta de tomate/papa?"
            AppLanguage.FRENCH -> "Où sont visibles les symptômes principaux sur la tomate/pomme de terre ?"
            AppLanguage.SWAHILI -> "Dalili kuu zinaonekana wapi kwenye mmea wa nyanya au viazi?"
        }
        nodeId == "node_solanaceous_leaf_spots" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "पत्तियों के धब्बे कैसे दिखते हैं और किस मौसम में दिखाई दिए?"
            AppLanguage.MARATHI -> "पानावरील डाग कसे दिसतात आणि कोणत्या हवामानात आढळले?"
            AppLanguage.BENGALI -> "পাতার দাগগুলি কেমন দেখতে এবং কোন আবহাওয়ায় দেখা দিয়েছে?"
            AppLanguage.SPANISH -> "¿Cómo se ven las manchas y en qué clima aparecieron?"
            AppLanguage.FRENCH -> "À quoi ressemblent les taches foliaires et sous quel climat sont-elles apparues ?"
            AppLanguage.SWAHILI -> "Madoa ya majani yanaonekanaje na yalionekana katika hali gani ya hewa?"
        }
        nodeId == "node_whitefly_check" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "क्या पत्तियों को हिलाने पर नीचे छोटे सफेद कीड़े उड़ते दिखाई देते हैं?"
            AppLanguage.MARATHI -> "पाने हलवल्यावर खाली लहान पांढऱ्या माश्या उडताना दिसतात का?"
            AppLanguage.BENGALI -> "পাতা নাড়ালে কি নিচে ছোট সাদা মাছি উড়তে দেখা যায়?"
            AppLanguage.SPANISH -> "¿Hay pequeños insectos blancos revoloteando bajo las hojas?"
            AppLanguage.FRENCH -> "Y a-t-il de petits insectes blancs qui volent sous les feuilles ?"
            AppLanguage.SWAHILI -> "Je, kuna wadudu wadogo weupe wanaoruka chini ya majani yakitikiswa?"
        }
        nodeId == "node_wilt_test" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "यदि आप तने को काटकर साफ पानी में रखते हैं, तो क्या दूधिया सफेद जीवाणु धागे निकलते हैं?"
            AppLanguage.MARATHI -> "खालचा खोड कापून स्वच्छ पाण्यात ठेवल्यास पांढरे दुधाळ जिवाणू धागे बाहेर पडतात का?"
            AppLanguage.BENGALI -> "গাছের কাণ্ড কেটে পরিষ্কার জলে রাখলে কি দুধের মতো সাদা সুতো বের হয়?"
            AppLanguage.SPANISH -> "Si corta el tallo y lo coloca en agua clara, ¿salen hebras bacterianas blancas?"
            AppLanguage.FRENCH -> "Si vous coupez la tige dans l'eau claire, des filaments bactériens blancs s'écoulent-ils ?"
            AppLanguage.SWAHILI -> "Ukikata shina la chini na kuliweka kwenye maji, je nyuzi nyeupe za bakteria zinatoka?"
        }
        nodeId == "node_chilli_part" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "मिर्च की फसल में मुख्य लक्षण क्या है?"
            AppLanguage.MARATHI -> "मिरचीच्या पिकामध्ये प्रमुख लक्षण कोणते आहे?"
            AppLanguage.BENGALI -> "মরিচ ফসলে প্রধান লক্ষণ কোনটি?"
            AppLanguage.SPANISH -> "¿Cuál es el síntoma dominante en el cultivo de chile?"
            AppLanguage.FRENCH -> "Quel est le symptôme dominant sur le piment ?"
            AppLanguage.SWAHILI -> "Ni dalili gani kuu kwenye zao la pilipili?"
        }
        nodeId == "node_cereal_crop" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "आप किस अनाज की फसल का प्रबंधन कर रहे हैं?"
            AppLanguage.MARATHI -> "तुम्ही कोणत्या तृणधान्य पिकाचे व्यवस्थापन करत आहात?"
            AppLanguage.BENGALI -> "আপনি কোন খাদ্যশস্য ফসল পরিচালনা করছেন?"
            AppLanguage.SPANISH -> "¿Qué cultivo de cereales está gestionando?"
            AppLanguage.FRENCH -> "Quelle culture céréalière gérez-vous ?"
            AppLanguage.SWAHILI -> "Unasimamia zao gani la nafaka?"
        }
        nodeId == "node_rice_symptoms" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "धान के खेत में मुख्य क्षति क्या देखी गई है?"
            AppLanguage.MARATHI -> "भाताच्या शेतात मुख्य नुकसान काय दिसून येत आहे?"
            AppLanguage.BENGALI -> "ধানের জমিতে প্রধান ক্ষতি কি দেখা গেছে?"
            AppLanguage.SPANISH -> "¿Cuál es el daño principal observado en el arrozal?"
            AppLanguage.FRENCH -> "Quel est le principal dommage observé dans la rizière ?"
            AppLanguage.SWAHILI -> "Ni uharibifu gani mkuu unaoonekana kwenye shamba la mpunga?"
        }
        nodeId == "node_maize_symptoms" -> when (lang) {
            AppLanguage.ENGLISH -> defaultQ
            AppLanguage.HINDI -> "क्या भुट्टे/मक्के के पत्तों में छेद और अंदर बुरादे जैसा मल दिखाई देता है?"
            AppLanguage.MARATHI -> "मक्याच्या पोंग्यातील पानांना छिद्रे आणि आत लाकडाच्या भुशासारखी विष्ठा दिसते का?"
            AppLanguage.BENGALI -> "ভুট্টার পাতার ভেতর কি কাটা দাগ ও কাঠের গুঁড়োর মতো বর্জ্য দেখা যায়?"
            AppLanguage.SPANISH -> "¿Hay agujeros en las hojas del cogollo con excrementos similares al aserrín?"
            AppLanguage.FRENCH -> "Y a-t-il des trous irréguliers dans le cornet avec de la sciure ?"
            AppLanguage.SWAHILI -> "Je, kuna matundu kwenye majani ya katikati na uchafu kama unga wa mbao ndani?"
        }
        else -> defaultQ
    }

    fun localizeOptionLabel(optionId: String, defaultLabel: String, lang: AppLanguage): String = when {
        optionId == "opt_solanaceous" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🍅 सोलानेसी फसलें (टमाटर, आलू, बैंगन)"
            AppLanguage.MARATHI -> "🍅 सोलानेसी पिके (टोमॅटो, बटाटा, वांगी)"
            AppLanguage.BENGALI -> "🍅 সোলানেসিয়াস ফসল (টমেটো, আলু, বেগুন)"
            AppLanguage.SPANISH -> "🍅 Solanáceas (Tomate, Papa, Berenjena)"
            AppLanguage.FRENCH -> "🍅 Solanacées (Tomate, Pomme de terre, Aubergine)"
            AppLanguage.SWAHILI -> "🍅 Mazao ya Solanaceae (Nyanya, Viazi, Biringanya)"
        }
        optionId == "opt_chilli" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🌶️ मिर्च और शिमला मिर्च"
            AppLanguage.MARATHI -> "🌶️ मिरची व सिमला मिरची"
            AppLanguage.BENGALI -> "🌶️ মরিচ ও ক্যাপসিকাম"
            AppLanguage.SPANISH -> "🌶️ Chile y Pimiento"
            AppLanguage.FRENCH -> "🌶️ Piment et Poivron"
            AppLanguage.SWAHILI -> "🌶️ Pilipili na Pilipili Hoho"
        }
        optionId == "opt_cucurbits" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🥒 कद्दूवर्गीय फसलें (खीरा, खरबूजा, लौकी)"
            AppLanguage.MARATHI -> "🥒 वेलवर्गीय पिके (काकडी, खरबूज, भोपळा)"
            AppLanguage.BENGALI -> "🥒 শসা ও লাউ জাতীয় ফসল"
            AppLanguage.SPANISH -> "🥒 Cucurbitáceas (Pepino, Melón, Calabacín)"
            AppLanguage.FRENCH -> "🥒 Cucurbitacées (Concombre, Melon, Courge)"
            AppLanguage.SWAHILI -> "🥒 Matango na Maboga"
        }
        optionId == "opt_cereals" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🌾 अनाज और घास (धान, मक्का, ज्वार)"
            AppLanguage.MARATHI -> "🌾 तृणधान्ये (भात, मका, ज्वारी)"
            AppLanguage.BENGALI -> "🌾 খাদ্যশস্য ও ঘাস (ধান, ভুট্টা, জোয়ার)"
            AppLanguage.SPANISH -> "🌾 Cereales y Gramíneas (Arroz, Maíz, Sorgo)"
            AppLanguage.FRENCH -> "🌾 Céréales et Graminées (Riz, Maïs, Sorgho)"
            AppLanguage.SWAHILI -> "🌾 Nafaka (Mpunga, Mahindi, Mtama)"
        }
        optionId == "opt_foliar_spots" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🍃 पत्तियों पर धब्बे और घाव"
            AppLanguage.MARATHI -> "🍃 पानावरील डाग आणि चट्टे"
            AppLanguage.BENGALI -> "🍃 পাতার দাগ ও ক্ষত"
            AppLanguage.SPANISH -> "🍃 Manchas y Lesiones Foliares"
            AppLanguage.FRENCH -> "🍃 Taches et Lésions Foliaires"
            AppLanguage.SWAHILI -> "🍃 Madoa na Vidonda kwenye Majani"
        }
        optionId == "opt_leaf_curl_stunt" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🔄 पत्ती मुड़ना, बौनापन और पीलापन"
            AppLanguage.MARATHI -> "🔄 पाने आकसणे, खुंटणे आणि पिवळे पडणे"
            AppLanguage.BENGALI -> "🔄 পাতা কোঁকড়ানো ও গাছ খর্বাকৃতি হওয়া"
            AppLanguage.SPANISH -> "🔄 Rizado de Hojas y Enanismo"
            AppLanguage.FRENCH -> "🔄 Enroulement Foliaire et Nanisme"
            AppLanguage.SWAHILI -> "🔄 Kujikunja kwa Majani na Kudumaa"
        }
        optionId == "opt_wilt_collapse" -> when (lang) {
            AppLanguage.ENGLISH -> defaultLabel
            AppLanguage.HINDI -> "🥀 तेजी से मुरझाना और तना ढहना"
            AppLanguage.MARATHI -> "🥀 अचानक सुकणे व खोड कोलमडणे"
            AppLanguage.BENGALI -> "🥀 দ্রুত শুকিয়ে যাওয়া ও কাণ্ড ঢলে পড়া"
            AppLanguage.SPANISH -> "🥀 Marchitamiento Rápido"
            AppLanguage.FRENCH -> "🥀 Flétrissement Rapide"
            AppLanguage.SWAHILI -> "🥀 Kunyauka kwa Haraka kwa Mmea"
        }
        else -> defaultLabel
    }

    // ==========================================
    // EXTENSION WORKER & LAB PORTAL LOCALIZATION
    // ==========================================
    fun extensionWorkerAndLabPortal(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "EXTENSION WORKER & LAB PORTAL"
        AppLanguage.HINDI -> "विस्तार कार्यकर्ता और प्रयोगशाला पोर्टल"
        AppLanguage.MARATHI -> "विस्तार कार्यकर्ते आणि प्रयोगशाळा पोर्टल"
        AppLanguage.SPANISH -> "PORTAL DE EXTENSIÓN Y LABORATORIO"
        AppLanguage.SWAHILI -> "TOVUTI YA MAAFISA UGANI NA MAABARA"
        AppLanguage.BENGALI -> "এক্সটেনশন কর্মী ও ল্যাব পোর্টাল"
        AppLanguage.FRENCH -> "PORTAIL VULGARISATION ET LABORATOIRE"
    }

    fun validatedCountBadge(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Validated"
        AppLanguage.HINDI -> "$count सत्यापित"
        AppLanguage.MARATHI -> "$count प्रमाणित"
        AppLanguage.SPANISH -> "$count Validados"
        AppLanguage.SWAHILI -> "$count Zimethibitishwa"
        AppLanguage.BENGALI -> "$count টি যাচাইকৃত"
        AppLanguage.FRENCH -> "$count Validés"
    }

    fun expertTriageAndLabValidation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Expert Triage & Laboratory Validation"
        AppLanguage.HINDI -> "विशेषज्ञ ट्राइएज और प्रयोगशाला सत्यापन"
        AppLanguage.MARATHI -> "तज्ज्ञ तपासणी आणि प्रयोगशाळा प्रमाणीकरण"
        AppLanguage.SPANISH -> "Triaje Experto y Validación de Laboratorio"
        AppLanguage.SWAHILI -> "Uchunguzi wa Wataalamu na Uthibitisho wa Maabara"
        AppLanguage.BENGALI -> "বিশেষজ্ঞ ট্রায়াজ এবং ল্যাবরেটরি যাচাইকরণ"
        AppLanguage.FRENCH -> "Triage d'Expert et Validation en Laboratoire"
    }

    fun extensionPortalHeaderDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Review farmer field submissions, verify microscopy/visual symptoms, attach laboratory diagnosis, and issue official agronomic guidance."
        AppLanguage.HINDI -> "किसान के नमूनों की समीक्षा करें, माइक्रोस्कोपी/दृश्य लक्षणों की पुष्टि करें, लैब निदान संलग्न करें और आधिकारिक कृषि मार्गदर्शन जारी करें।"
        AppLanguage.MARATHI -> "शेतकऱ्यांच्या नोंदी तपासा, सूक्ष्मदर्शक/दृश्य लक्षणांची पुष्टी करा, प्रयोगशाळा अहवाल जोडा आणि अधिकृत कृषी सल्ला जारी करा."
        AppLanguage.SPANISH -> "Revise los envíos de campo, verifique síntomas visuales y microscópicos, adjunte diagnósticos y emita orientación agronómica oficial."
        AppLanguage.SWAHILI -> "Kagua ripoti za wakulima, thibitisha dalili za maabara na toa mwongozo rasmi wa kitaalamu wa kilimo."
        AppLanguage.BENGALI -> "কৃষকের মাঠের নমুনা পর্যালোচনা করুন, অণুবীক্ষণিক লক্ষণ যাচাই করুন এবং অফিসিয়াল পরামর্শ প্রদান করুন।"
        AppLanguage.FRENCH -> "Examinez les soumissions des agriculteurs, vérifiez les symptômes au microscope, joignez le diagnostic et publiez des conseils agronomiques officiels."
    }

    fun allCasesTab(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All Cases ($count)"
        AppLanguage.HINDI -> "सभी मामले ($count)"
        AppLanguage.MARATHI -> "सर्व प्रकरणे ($count)"
        AppLanguage.SPANISH -> "Todos los Casos ($count)"
        AppLanguage.SWAHILI -> "Kesi Zote ($count)"
        AppLanguage.BENGALI -> "সমস্ত কেস ($count)"
        AppLanguage.FRENCH -> "Tous les Cas ($count)"
    }

    fun pendingReviewTab(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Pending Review ($count)"
        AppLanguage.HINDI -> "समीक्षा लंबित ($count)"
        AppLanguage.MARATHI -> "तपासणी बाकी ($count)"
        AppLanguage.SPANISH -> "Pendiente de Revisión ($count)"
        AppLanguage.SWAHILI -> "Inasubiri Ukaguzi ($count)"
        AppLanguage.BENGALI -> "পর্যালোচনা বাকি ($count)"
        AppLanguage.FRENCH -> "En Attente d'Examen ($count)"
    }

    fun confirmedTab(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Confirmed ($count)"
        AppLanguage.HINDI -> "पुष्ट ($count)"
        AppLanguage.MARATHI -> "पुष्टी झालेले ($count)"
        AppLanguage.SPANISH -> "Confirmados ($count)"
        AppLanguage.SWAHILI -> "Zimethibitishwa ($count)"
        AppLanguage.BENGALI -> "নিশ্চিত ($count)"
        AppLanguage.FRENCH -> "Confirmés ($count)"
    }

    fun expertAgronomistNoteLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Expert Agronomist Note:"
        AppLanguage.HINDI -> "विशेषज्ञ कृषि विज्ञानी टिप्पणी:"
        AppLanguage.MARATHI -> "तज्ज्ञ कृषी शास्त्रज्ञ टीप:"
        AppLanguage.SPANISH -> "Nota del Agrónomo Experto:"
        AppLanguage.SWAHILI -> "Ujumbe wa Mtaalamu wa Kilimo:"
        AppLanguage.BENGALI -> "বিশেষজ্ঞ কৃষিবিদ মন্তব্য:"
        AppLanguage.FRENCH -> "Note de l'Agronome Expert :"
    }

    fun editValidationBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Edit Validation"
        AppLanguage.HINDI -> "सत्यापन संपादित करें"
        AppLanguage.MARATHI -> "प्रमाणीकरण संपादित करा"
        AppLanguage.SPANISH -> "Editar Validación"
        AppLanguage.SWAHILI -> "Hariri Uthibitisho"
        AppLanguage.BENGALI -> "যাচাইকরণ সম্পাদনা করুন"
        AppLanguage.FRENCH -> "Modifier la Validation"
    }

    fun triageAndValidateBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Triage & Validate"
        AppLanguage.HINDI -> "जांचें और सत्यापित करें"
        AppLanguage.MARATHI -> "तपासणी व प्रमाणीकरण करा"
        AppLanguage.SPANISH -> "Triaje y Validar"
        AppLanguage.SWAHILI -> "Chambua na Thibitisha"
        AppLanguage.BENGALI -> "পরীক্ষা ও যাচাই করুন"
        AppLanguage.FRENCH -> "Trier et Valider"
    }

    fun validateCropDiagnosisDialogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Validate Crop Diagnosis"
        AppLanguage.HINDI -> "फसल निदान को सत्यापित करें"
        AppLanguage.MARATHI -> "पीक रोगनिदान प्रमाणित करा"
        AppLanguage.SPANISH -> "Validar Diagnóstico de Cultivo"
        AppLanguage.SWAHILI -> "Thibitisha Utambuzi wa Zao"
        AppLanguage.BENGALI -> "ফসলের ডায়াগনসিস যাচাই করুন"
        AppLanguage.FRENCH -> "Valider le Diagnostic de Culture"
    }

    fun extensionOfficerNameLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Extension Officer / Specialist Name"
        AppLanguage.HINDI -> "विस्तार अधिकारी / विशेषज्ञ का नाम"
        AppLanguage.MARATHI -> "कृषी अधिकारी / तज्ज्ञाचे नाव"
        AppLanguage.SPANISH -> "Nombre del Oficial de Extensión / Especialista"
        AppLanguage.SWAHILI -> "Jina la Afisa Ugani / Mtaalamu"
        AppLanguage.BENGALI -> "এক্সটেনশন অফিসার / বিশেষজ্ঞের নাম"
        AppLanguage.FRENCH -> "Nom du Conseiller Agricole / Spécialiste"
    }

    fun officialAgronomicNotesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Official Agronomic Notes & Lab Findings"
        AppLanguage.HINDI -> "आधिकारिक कृषि नोट्स और लैब निष्कर्ष"
        AppLanguage.MARATHI -> "अधिकृत कृषी नोंदी व प्रयोगशाळा निष्कर्ष"
        AppLanguage.SPANISH -> "Notas Agronómicas Oficiales y Hallazgos de Laboratorio"
        AppLanguage.SWAHILI -> "Maelezo Rasmi ya Kilimo na Matokeo ya Maabara"
        AppLanguage.BENGALI -> "অফিসিয়াল কৃষি নোট এবং ল্যাব ফলাফল"
        AppLanguage.FRENCH -> "Notes Agronomiques Officielles et Résultats de Laboratoire"
    }

    fun validationStatusLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Validation Status:"
        AppLanguage.HINDI -> "सत्यापन स्थिति:"
        AppLanguage.MARATHI -> "प्रमाणीकरण स्थिती:"
        AppLanguage.SPANISH -> "Estado de Validación:"
        AppLanguage.SWAHILI -> "Hali ya Uthibitisho:"
        AppLanguage.BENGALI -> "যাচাইকরণের স্থিতি:"
        AppLanguage.FRENCH -> "Statut de Validation :"
    }

    fun confirmValidBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Confirm Valid"
        AppLanguage.HINDI -> "पुष्टि करें"
        AppLanguage.MARATHI -> "पुष्टी करा"
        AppLanguage.SPANISH -> "Confirmar Válido"
        AppLanguage.SWAHILI -> "Thibitisha Sahihi"
        AppLanguage.BENGALI -> "বৈধ নিশ্চিত করুন"
        AppLanguage.FRENCH -> "Confirmer Valide"
    }

    fun labTestingBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Lab Testing"
        AppLanguage.HINDI -> "लैब परीक्षण"
        AppLanguage.MARATHI -> "प्रयोगशाळा चाचणी"
        AppLanguage.SPANISH -> "Pruebas de Laboratorio"
        AppLanguage.SWAHILI -> "Upimaji wa Maabara"
        AppLanguage.BENGALI -> "ল্যাব পরীক্ষা"
        AppLanguage.FRENCH -> "Analyse Labo"
    }

    fun saveAndIssueValidationBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Save & Issue Validation"
        AppLanguage.HINDI -> "सहेजें और सत्यापन जारी करें"
        AppLanguage.MARATHI -> "जतन करा आणि प्रमाणीकरण जारी करा"
        AppLanguage.SPANISH -> "Guardar y Emitir Validación"
        AppLanguage.SWAHILI -> "Hifadhi na Toa Uthibitisho"
        AppLanguage.BENGALI -> "সংরক্ষণ ও বৈধতা প্রকাশ করুন"
        AppLanguage.FRENCH -> "Enregistrer et Valider"
    }

    fun cancelBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.MARATHI -> "रद्द करा"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.SWAHILI -> "Ghairi"
        AppLanguage.BENGALI -> "বাতিল"
        AppLanguage.FRENCH -> "Annuler"
    }

    fun localizeReportStatus(status: ReportStatus, lang: AppLanguage): String = when (status) {
        ReportStatus.EXPERT_VALIDATED -> when (lang) {
            AppLanguage.ENGLISH -> "Expert Confirmed"
            AppLanguage.HINDI -> "विशेषज्ञ द्वारा पुष्ट"
            AppLanguage.MARATHI -> "तज्ज्ञांनी पुष्टी केलेले"
            AppLanguage.SPANISH -> "Confirmado por Experto"
            AppLanguage.SWAHILI -> "Imethibitishwa na Mtaalamu"
            AppLanguage.BENGALI -> "বিশেষজ্ঞ দ্বারা নিশ্চিত"
            AppLanguage.FRENCH -> "Confirmé par Expert"
        }
        ReportStatus.SUBMITTED_TO_EXPERT -> when (lang) {
            AppLanguage.ENGLISH -> "Submitted to Extension"
            AppLanguage.HINDI -> "विस्तार अधिकारी को भेजा गया"
            AppLanguage.MARATHI -> "विस्तार अधिकाऱ्याकडे पाठवले"
            AppLanguage.SPANISH -> "Enviado a Extensión"
            AppLanguage.SWAHILI -> "Imetumwa kwa Ugani"
            AppLanguage.BENGALI -> "এক্সটেনশনে জমা দেওয়া হয়েছে"
            AppLanguage.FRENCH -> "Soumis à la Vulgarisation"
        }
        ReportStatus.IN_LAB_REVIEW -> when (lang) {
            AppLanguage.ENGLISH -> "In Lab Review"
            AppLanguage.HINDI -> "प्रयोगशाला समीक्षा जारी"
            AppLanguage.MARATHI -> "प्रयोगशाळा तपासणी सुरू"
            AppLanguage.SPANISH -> "En Revisión de Laboratorio"
            AppLanguage.SWAHILI -> "Inakaguliwa Maabara"
            AppLanguage.BENGALI -> "ল্যাব পর্যালোচনাধীন"
            AppLanguage.FRENCH -> "En Examen au Labo"
        }
        ReportStatus.AI_DIAGNOSED -> when (lang) {
            AppLanguage.ENGLISH -> "AI Diagnosed"
            AppLanguage.HINDI -> "एआई द्वारा निदानित"
            AppLanguage.MARATHI -> "एआय द्वारे निदान"
            AppLanguage.SPANISH -> "Diagnosticado por IA"
            AppLanguage.SWAHILI -> "Imetambuliwa na AI"
            AppLanguage.BENGALI -> "এআই দ্বারা নির্ণীত"
            AppLanguage.FRENCH -> "Diagnostiqué par IA"
        }
        ReportStatus.RESOLVED -> when (lang) {
            AppLanguage.ENGLISH -> "Treatment Completed"
            AppLanguage.HINDI -> "उपचार पूर्ण"
            AppLanguage.MARATHI -> "उपचार पूर्ण"
            AppLanguage.SPANISH -> "Tratamiento Completado"
            AppLanguage.SWAHILI -> "Matibabu Yamekamilika"
            AppLanguage.BENGALI -> "চিকিৎসা সম্পন্ন"
            AppLanguage.FRENCH -> "Traitement Terminé"
        }
    }

    // ==========================================
    // OFFICIAL DASHBOARD SCREEN LOCALIZATION
    // ==========================================
    fun stateAndDistrictCommandCenter(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "STATE & DISTRICT COMMAND CENTER"
        AppLanguage.HINDI -> "राज्य एवं जिला कमान केंद्र"
        AppLanguage.MARATHI -> "राज्य आणि जिल्हा नियंत्रण केंद्र"
        AppLanguage.SPANISH -> "CENTRO DE MANDO ESTATAL Y DISTRITAL"
        AppLanguage.SWAHILI -> "KITUO CHA AMRI CHA WILAYA NA JIMBO"
        AppLanguage.BENGALI -> "রাজ্য ও জেলা কমান্ড সেন্টার"
        AppLanguage.FRENCH -> "CENTRE DE COMMANDEMENT D'ÉTAT ET DE DISTRICT"
    }

    fun broadcastAdvisoryBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Broadcast Advisory"
        AppLanguage.HINDI -> "सलाह प्रसारित करें"
        AppLanguage.MARATHI -> "सल्ला प्रसारित करा"
        AppLanguage.SPANISH -> "Transmitir Aviso"
        AppLanguage.SWAHILI -> "Tangaza Ushauri"
        AppLanguage.BENGALI -> "পরামর্শ সম্প্রচার করুন"
        AppLanguage.FRENCH -> "Diffuser l'Avis"
    }

    fun agriculturalHealthSurveillanceSystem(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Agricultural Health & Pest Surveillance System"
        AppLanguage.HINDI -> "कृषि स्वास्थ्य एवं कीट निगरानी प्रणाली"
        AppLanguage.MARATHI -> "कृषी आरोग्य आणि कीड पाळत यंत्रणा"
        AppLanguage.SPANISH -> "Sistema de Vigilancia de Sanidad Agrícola y Plagas"
        AppLanguage.SWAHILI -> "Mfumo wa Uchunguzi wa Afya ya Kilimo na Wadudu"
        AppLanguage.BENGALI -> "কৃষি স্বাস্থ্য ও পোকা নজরদারি ব্যবস্থা"
        AppLanguage.FRENCH -> "Système de Surveillance Sanitaire Agricole et Ravageurs"
    }

    fun commandCenterSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Regional epidemiological tracking, targeted input compliance, and district-wide IPM containment operations."
        AppLanguage.HINDI -> "क्षेत्रीय महामारी ट्रैकिंग, लक्षित इनपुट अनुपालन और जिला स्तरीय आईपीएम रोकथाम अभियान।"
        AppLanguage.MARATHI -> "प्रादेशिक रोग प्रादुर्भाव ट्रॅकिंग, अचूक औषध वापर आणि जिल्हास्तरीय आयपीएम नियंत्रण मोहीम."
        AppLanguage.SPANISH -> "Seguimiento epidemiológico regional, cumplimiento de insumos y operaciones de contención de MIP."
        AppLanguage.SWAHILI -> "Ufuatiliaji wa magonjwa kikanda, matumizi sahihi ya dawa na shughuli za IPM wilayani."
        AppLanguage.BENGALI -> "আঞ্চলিক মহামারী ট্র্যাকিং এবং জেলাব্যাপী আইপিএম নিয়ন্ত্রণ কার্যক্রম।"
        AppLanguage.FRENCH -> "Suivi épidémiologique régional, conformité des intrants et opérations de confinement IPM à l'échelle du district."
    }

    fun activeHotspotsKpi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Active Hotspots"
        AppLanguage.HINDI -> "सक्रिय हॉटस्पॉट"
        AppLanguage.MARATHI -> "सक्रिय हॉटस्पॉट्स"
        AppLanguage.SPANISH -> "Zonas Activas"
        AppLanguage.SWAHILI -> "Maeneo Hatari Hai"
        AppLanguage.BENGALI -> "সক্রিয় হটস্পট"
        AppLanguage.FRENCH -> "Foyers Actifs"
    }

    fun criticalClustersKpi(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Critical Clusters"
        AppLanguage.HINDI -> "$count गंभीर क्लस्टर"
        AppLanguage.MARATHI -> "$count गंभीर क्लस्टर्स"
        AppLanguage.SPANISH -> "$count Focos Críticos"
        AppLanguage.SWAHILI -> "Vikundi Hatari $count"
        AppLanguage.BENGALI -> "$count টি সংকটজনক ক্লাস্টার"
        AppLanguage.FRENCH -> "$count Foyers Critiques"
    }

    fun totalDiagnosesKpi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Total Diagnoses"
        AppLanguage.HINDI -> "कुल निदान"
        AppLanguage.MARATHI -> "एकूण रोगनिदान"
        AppLanguage.SPANISH -> "Total Diagnósticos"
        AppLanguage.SWAHILI -> "Jumla ya Utambuzi"
        AppLanguage.BENGALI -> "মোট ডায়াগনসিস"
        AppLanguage.FRENCH -> "Total Diagnostics"
    }

    fun ipmTargetedKpi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "100% IPM Targeted"
        AppLanguage.HINDI -> "100% आईपीएम लक्षित"
        AppLanguage.MARATHI -> "१००% आयपीएम लक्षित"
        AppLanguage.SPANISH -> "100% Enfocado en MIP"
        AppLanguage.SWAHILI -> "100% Ililenga IPM"
        AppLanguage.BENGALI -> "১০০% আইপিএম লক্ষ্যভিত্তিক"
        AppLanguage.FRENCH -> "100% Ciblé IPM"
    }

    fun residueReductionKpi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Residue Reduction"
        AppLanguage.HINDI -> "अवशेष में कमी"
        AppLanguage.MARATHI -> "रासायनिक अवशेष घट"
        AppLanguage.SPANISH -> "Reducción de Residuos"
        AppLanguage.SWAHILI -> "Kupunguza Masalio"
        AppLanguage.BENGALI -> "রাসায়নিক অবশিষ্টাংশ হ্রাস"
        AppLanguage.FRENCH -> "Réduction des Résidus"
    }

    fun overSprayAvoidedViaPhi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Over-spray avoided via PHI"
        AppLanguage.HINDI -> "PHI द्वारा अत्यधिक छिड़काव से बचाव"
        AppLanguage.MARATHI -> "PHI मुळे अतिरिक्त फवारणी टाळली"
        AppLanguage.SPANISH -> "Exceso evitado vía PHI"
        AppLanguage.SWAHILI -> "Kupulizia kupita kiasi kumeepukwa na PHI"
        AppLanguage.BENGALI -> "PHI দ্বারা অতিরিক্ত স্প্রে রোধ"
        AppLanguage.FRENCH -> "Surdosage évité grâce au DAR"
    }

    fun detectionLatencyKpi(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Detection Latency"
        AppLanguage.HINDI -> "पहचान में लगने वाला समय"
        AppLanguage.MARATHI -> "रोगनिदान वेग"
        AppLanguage.SPANISH -> "Latencia de Detección"
        AppLanguage.SWAHILI -> "Kasi ya Utambuzi"
        AppLanguage.BENGALI -> "শনাক্তকরণে সময়"
        AppLanguage.FRENCH -> "Délai de Détection"
    }

    fun vsTraditionalLab(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Vs 7+ days traditional lab"
        AppLanguage.HINDI -> "पारंपरिक लैब के 7+ दिनों की तुलना में"
        AppLanguage.MARATHI -> "पारंपरिक लॅबच्या ७+ दिवसांच्या तुलनेत"
        AppLanguage.SPANISH -> "Frente a más de 7 días de lab tradicional"
        AppLanguage.SWAHILI -> "Dhidi ya siku 7+ za maabara ya jadi"
        AppLanguage.BENGALI -> "ঐতিহ্যবাহী ল্যাবের ৭+ দিনের তুলনায়"
        AppLanguage.FRENCH -> "Vs 7+ jours en laboratoire traditionnel"
    }

    fun safeInputUsageHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "SAFE INPUT USAGE & PHI / MRL ADHERENCE"
        AppLanguage.HINDI -> "सुरक्षित इनपुट उपयोग और PHI / MRL अनुपालन"
        AppLanguage.MARATHI -> "सुरक्षित औषध वापर आणि PHI / MRL पालन"
        AppLanguage.SPANISH -> "USO SEGURO DE INSUMOS Y ADHERENCIA A PHI / LMR"
        AppLanguage.SWAHILI -> "MATUMIZI SALAMA YA DAWA NA KUZINGATIA PHI / MRL"
        AppLanguage.BENGALI -> "নিরাপদ কীটনাশক ব্যবহার এবং PHI / MRL নিয়মাবলী"
        AppLanguage.FRENCH -> "UTILISATION SÛRE DES INTRANTS ET RESPECT DAR / LMR"
    }

    fun safeInputUsageDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All farmer prescriptions generated by the system mandate biological first-line treatment, cultural row aeration, and exact chemical dosages with compulsory Pre-Harvest Interval (PHI) timers."
        AppLanguage.HINDI -> "प्रणाली द्वारा उत्पन्न सभी नुस्खों में जैविक प्रथम-पंक्ति उपचार, कृषि वायुसंचार और अनिवार्य PHI टाइमर के साथ सटीक रासायनिक खुराक शामिल है।"
        AppLanguage.MARATHI -> "प्रणालीद्वारे तयार केलेल्या सर्व सल्ल्यांमध्ये जैविक प्रथम प्राधान्य उपचार आणि अचूक रासायनिक डोससह अनिवार्य PHI टायमर समाविष्ट आहेत."
        AppLanguage.SPANISH -> "Todas las prescripciones exigen tratamiento biológico de primera línea y dosis químicas exactas con temporizadores de Intervalo Pre-Cosecha (PHI)."
        AppLanguage.SWAHILI -> "Maagizo yote yanasisitiza tiba ya kwanza ya kibayolojia na vipimo sahihi vya kemikali na muda wa kabla ya kuvuna (PHI)."
        AppLanguage.BENGALI -> "সিস্টেম দ্বারা প্রদত্ত সমস্ত প্রেসক্রিপশনে জৈবিক প্রাথমিক চিকিৎসা এবং বাধ্যতামূলক পিএইচআই টাইমার সহ রাসায়নিক প্রয়োগ নির্দেশ করা হয়েছে।"
        AppLanguage.FRENCH -> "Toutes les prescriptions imposent un traitement biologique de première ligne et des dosages chimiques précis avec délais avant récolte (DAR)."
    }

    fun bioCulturalStat(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Bio/Cultural: 68%"
        AppLanguage.HINDI -> "जैविक/सांस्कृतिक: 68%"
        AppLanguage.MARATHI -> "जैविक/कृषी पद्धती: ६८%"
        AppLanguage.SPANISH -> "Bio/Cultural: 68%"
        AppLanguage.SWAHILI -> "Biolojia/Kilimo: 68%"
        AppLanguage.BENGALI -> "জৈব/কৃষি পদ্ধতি: ৬৮%"
        AppLanguage.FRENCH -> "Bio/Cultural : 68%"
    }

    fun targetedChemicalStat(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Targeted Chemical: 32%"
        AppLanguage.HINDI -> "लक्षित रासायनिक: 32%"
        AppLanguage.MARATHI -> "लक्षित रासायनिक: ३२%"
        AppLanguage.SPANISH -> "Químico Dirigido: 32%"
        AppLanguage.SWAHILI -> "Kemikali Lengwa: 32%"
        AppLanguage.BENGALI -> "লক্ষ্যভিত্তিক রাসায়নিক: ৩২%"
        AppLanguage.FRENCH -> "Chimique Ciblé : 32%"
    }

    fun blanketSprayStat(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Blanket Spray: 0%"
        AppLanguage.HINDI -> "अंधाधुंध छिड़काव: 0%"
        AppLanguage.MARATHI -> "सरसकट फवारणी: ०%"
        AppLanguage.SPANISH -> "Pulverización Indiscriminada: 0%"
        AppLanguage.SWAHILI -> "Mpulizo wa Holela: 0%"
        AppLanguage.BENGALI -> "যথেচ্ছ স্প্রে: ০%"
        AppLanguage.FRENCH -> "Pulvérisation Massive : 0%"
    }

    fun activeDistrictBroadcastAdvisories(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "ACTIVE DISTRICT BROADCAST ADVISORIES ($count)"
        AppLanguage.HINDI -> "सक्रिय जिला प्रसारण सलाह ($count)"
        AppLanguage.MARATHI -> "सक्रिय जिल्हा प्रसारण सल्ले ($count)"
        AppLanguage.SPANISH -> "AVISOS DISTRITALES TRANSMITIDOS ACTIVOS ($count)"
        AppLanguage.SWAHILI -> "TAARIFA ZA TAHADHARI ZA WILAYA ($count)"
        AppLanguage.BENGALI -> "সক্রিয় জেলা সম্প্রচার পরামর্শ ($count)"
        AppLanguage.FRENCH -> "AVIS DE DIFFUSION DU DISTRICT ACTIFS ($count)"
    }

    fun localizeAlertTitle(title: String, lang: AppLanguage): String = when {
        title.contains("MRL", ignoreCase = true) || title.contains("Residue", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "सुरक्षित कीटनाशक उपयोग और अधिकतम अवशेष सीमा (MRL) सूचना"
            AppLanguage.MARATHI -> "सुरक्षित कीटकनाशक वापर आणि कमाल अवशेष मर्यादा (MRL) सूचना"
            AppLanguage.BENGALI -> "নিরাপদ কীটনাশক ব্যবহার এবং সর্বোচ্চ অবশিষ্টাংশ সীমা (MRL) বিজ্ঞপ্তি"
            AppLanguage.SPANISH -> "Aviso de Uso Seguro de Plaguicidas y Límite Máximo de Residuos (LMR)"
            AppLanguage.FRENCH -> "Avis d'Utilisation Sûre des Pesticides et Limite Maximale de Résidus (LMR)"
            AppLanguage.SWAHILI -> "Taarifa ya Matumizi Salama ya Dawa za Wadudu na Kiwango cha Juu cha Masalio (MRL)"
        }
        title.contains("Pheromone Trap", ignoreCase = true) || title.contains("Armyworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "फेरोमोन ट्रैप थ्रेशोल्ड चेतावनी: आर्मीवर्म"
            AppLanguage.MARATHI -> "फेरोमोन सापळा मर्यादा इशारा: लष्करी अळी"
            AppLanguage.BENGALI -> "ফেরোমোন ফাঁদ থ্রেশহোল্ড সতর্কতা: আর্মিওয়ার্ম"
            AppLanguage.SPANISH -> "Advertencia de Umbral de Trampa de Feromonas: Gusano Cogollero"
            AppLanguage.FRENCH -> "Alerte Seuil de Piège à Phéromones : Légionnaire d'Automne"
            AppLanguage.SWAHILI -> "Tahadhari ya Kizingiti cha Mitego ya Pheromone: Funza Jeshi"
        }
        title.contains("Late Blight Surge", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "जिला कृषि आपातकालीन सलाह: लेट ब्लाइट (झुलसा) प्रकोप"
            AppLanguage.MARATHI -> "जिल्हा कृषी तातडीचा सल्ला: लेट ब्लाइट (करपा) वाढ"
            AppLanguage.BENGALI -> "জেলা কৃষি জরুরি পরামর্শ: লেট ব্লাইট সংক্রমণ"
            AppLanguage.SPANISH -> "Aviso Agrícola Urgente de Distrito: Aumento de Tizón Tardío"
            AppLanguage.FRENCH -> "Avis Agricole Urgent du District : Recrudescence du Mildiou"
            AppLanguage.SWAHILI -> "Ushauri wa Dharura wa Kilimo wa Wilaya: Ongezeko la Ukungu wa Majani"
        }
        title.contains("Downy Mildew & Blast Alert", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> title
            AppLanguage.HINDI -> "त्वरित जिला सलाह: डाउनी मिल्ड्यू एवं ब्लास्ट चेतावनी"
            AppLanguage.MARATHI -> "तातडीचा जिल्हा सल्ला: डाऊनी मिल्ड्यू आणि ब्लास्ट अलर्ट"
            AppLanguage.BENGALI -> "জরুরি জেলা পরামর্শ: ডাউনি মিলডিউ এবং ব্লাস্ট সতর্কতা"
            AppLanguage.SPANISH -> "Aviso Urgente de Distrito: Alerta de Mildiu y Piricularia"
            AppLanguage.FRENCH -> "Avis Urgent du District : Alerte Mildiou et Pyriculariose"
            AppLanguage.SWAHILI -> "Ushauri wa Dharura wa Wilaya: Tahadhari ya Ukungu na Magonjwa ya Mpunga"
        }
        else -> title
    }

    fun localizeAlertMessage(message: String, lang: AppLanguage): String = when {
        message.contains("Export consignment rejection", ignoreCase = true) || message.contains("MRL", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> message
            AppLanguage.HINDI -> "निर्यात खेपों में रसायन अवशेषों के जोखिम के कारण, सभी किसान अनिवार्य Pre-Harvest Interval (PHI) दिनों का कड़ाई से पालन करें।"
            AppLanguage.MARATHI -> "निर्यात मालामध्ये रासायनिक अवशेष आढळण्याचा धोका टाळण्यासाठी सर्व शेतकऱ्यांनी अनिवार्य Pre-Harvest Interval (PHI) चे काटेकोर पालन करावे."
            AppLanguage.BENGALI -> "রপ্তানি চালানে অবশিষ্টাংশের ঝুঁকি এড়াতে সমস্ত কৃষক বাধ্যতামূলক প্রি-হার্ভেস্ট ব্যবধান (PHI) কঠোরভাবে মেনে চলুন।"
            AppLanguage.SPANISH -> "Para evitar rechazos por exceso de residuos, todos los agricultores deben cumplir estrictamente el Intervalo Pre-Cosecha (PHI)."
            AppLanguage.FRENCH -> "Pour éviter les rejets d'exportation liés aux résidus chimiques, tous les agriculteurs doivent respecter strictement le Délai Avant Récolte (DAR)."
            AppLanguage.SWAHILI -> "Ili kuzuia kukataliwa kwa mazao ya kuuza nje, wakulima wote lazima wazingatie kikamilifu Muda wa Kabla ya Kuvuna (PHI)."
        }
        message.contains("trap catches exceeded", ignoreCase = true) || message.contains("Armyworm", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> message
            AppLanguage.HINDI -> "ट्रैप में 8 पतंगे/ट्रैप/रात से अधिक पाए गए हैं। मक्का के किसान तुरंत जैविक परजीवी और नीम तेल 10,000 ppm का छिड़काव करें।"
            AppLanguage.MARATHI -> "सापळ्यात प्रति रात्र ८ पेक्षा जास्त पतंग आढळले आहेत. मका उत्पादकांनी त्वरित जैविक परजीवी व कडुनिंब तेल १०,००० ppm फवारावे."
            AppLanguage.BENGALI -> "ফাঁদে প্রতি রাতে ৮টির বেশি পোকা ধরা পড়েছে। ভুট্টা চাষীরা অবিলম্বে জৈব পরজীবী এবং নিম তেল প্রয়োগ করুন।"
            AppLanguage.SPANISH -> "Las capturas superaron el umbral (8 polillas/trampa/noche). Aplique parasitoides de huevos y aceite de neem inmediatamente."
            AppLanguage.FRENCH -> "Les captures ont dépassé le seuil (8 papillons/piège/nuit). Appliquer immédiatement des parasitoïdes et de l'huile de neem."
            AppLanguage.SWAHILI -> "Mitego imevuka kizingiti (nondo 8 kwa usiku). Weka mara moja viuadudu vya asili na mafuta ya mwarobaini."
        }
        message.contains("Continuous humidity", ignoreCase = true) || message.contains("blight spore", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> message
            AppLanguage.HINDI -> "लगातार 85% से अधिक आर्द्रता से झुलसा रोग के बीजाणुओं का फैलाव तेज हो गया है। रात के समय सिंचाई न करें और जल निकास सुनिश्चित करें।"
            AppLanguage.MARATHI -> "सतत ८५% पेक्षा जास्त आर्द्रतेमुळे करपा रोगाचा प्रसार वेगाने होत आहे. रात्री पाणी देणे टाळा आणि शेतातून पाण्याचा निचरा करा."
            AppLanguage.BENGALI -> "টানা ৮৫% আর্দ্রতার কারণে ব্লাইটের স্পোর বৃদ্ধি পাচ্ছে। রাতে সেচ দেওয়া এড়িয়ে চলুন এবং পানি নিষ্কাশন নিশ্চিত করুন।"
            AppLanguage.SPANISH -> "La humedad continua superior al 85% ha acelerado la liberación de esporas. Evite el riego nocturno."
            AppLanguage.FRENCH -> "Une humidité continue supérieure à 85% accélère la libération des spores. Éviter l'irrigation nocturne."
            AppLanguage.SWAHILI -> "Unyevu wa zaidi ya 85% umechochea kuenea kwa viini vya ukungu. Epuka kumwagilia usiku na weka mifereji."
        }
        message.contains("continuous canopy wetness", ignoreCase = true) || message.contains("overcast sky", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> message
            AppLanguage.HINDI -> "अगले 48 घंटों में बादलों और पत्तियों पर नमी की संभावना है। किसान तुरंत निवारक जैव-कवकनाशी का छिड़काव करें।"
            AppLanguage.MARATHI -> "पुढील ४८ तासांत ढगाळ हवामान आणि पानांवर ओलावा राहण्याची शक्यता आहे. शेतकऱ्यांनी प्रतिबंधक जैविक बुरशीनाशके फवारावीत."
            AppLanguage.BENGALI -> "আগামী ৪৮ ঘণ্টায় মেঘলা আকাশ ও পাতায় আর্দ্রতা থাকার পূর্বাভাস। কৃষকদের সুরক্ষামূলক জৈব ছত্রাকনাশক স্প্রে করার নির্দেশ দেওয়া হচ্ছে।"
            AppLanguage.SPANISH -> "Humedad foliar prolongada y cielo nublado previstos para las próximas 48 h. Aplique biofungicidas preventivos."
            AppLanguage.FRENCH -> "Humectation foliaire prolongée et ciel couvert prévus pour les prochaines 48 h. Appliquer des biofongicides préventifs."
            AppLanguage.SWAHILI -> "Unyevu wa majani na mawingu unatarajiwa kwa saa 48 zijazo. Wakulima wanashauriwa kutumia dawa za kibayolojia za kuzuia ukungu."
        }
        else -> message
    }

    fun localizeActionRequired(action: String, lang: AppLanguage): String = when {
        action.contains("Stop organophosphates", ignoreCase = true) || action.contains("certified bio-inputs", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> action
            AppLanguage.HINDI -> "कटाई से 14 दिन पहले ऑर्गनोफॉस्फेट्स का उपयोग रोकें; केवल प्रमाणित जैव-इनपुट का उपयोग करें।"
            AppLanguage.MARATHI -> "कापणीपूर्वी १४ दिवस ऑर्गनोफॉस्फेटचा वापर थांबवा; केवळ प्रमाणित जैविक औषधांचा वापर करा."
            AppLanguage.BENGALI -> "ফসল কাটার ১৪ দিন আগে অর্গানোফসফেটের ব্যবহার বন্ধ করুন; কেবল প্রত্যয়িত জৈব ইনপুট ব্যবহার করুন।"
            AppLanguage.SPANISH -> "Detenga organofosforados 14 días antes de cosechar; use bioinsumos certificados."
            AppLanguage.FRENCH -> "Arrêter les organophosphorés 14 jours avant récolte ; utiliser uniquement des bio-intrants certifiés."
            AppLanguage.SWAHILI -> "Acha kemikali za organophosphate siku 14 kabla ya kuvuna; tumia pembejeo za kibayolojia zilizoidhinishwa."
        }
        action.contains("mass trapping", ignoreCase = true) || action.contains("Corcyra", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> action
            AppLanguage.HINDI -> "व्यापक फेरोमोन ट्रैप लगाएं और ट्राइकोग्रामा अंड परजीवी छोड़ें।"
            AppLanguage.MARATHI -> "सामूहिक फेरोमोन सापळे लावा आणि ट्रायकोड्रॉमा परजीवी सोडा."
            AppLanguage.BENGALI -> "ব্যাপক ফেরোমোন ফাঁদ স্থাপন করুন এবং ট্রাইকোগ্রামা পরজীবী ছাড়ুন।"
            AppLanguage.SPANISH -> "Instale trampas masivas de feromonas y libere parasitoides Trichogramma."
            AppLanguage.FRENCH -> "Installer des pièges de masse et lâcher des parasitoïdes Trichogramma."
            AppLanguage.SWAHILI -> "Weka mitego mingi ya pheromone na toa vimelea rafiki vya Trichogramma."
        }
        action.contains("Bordeaux mixture", ignoreCase = true) || action.contains("Copper Oxychloride", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> action
            AppLanguage.HINDI -> "सुरक्षात्मक 1% बोर्डो मिश्रण या कॉपर ऑक्सीक्लोराइड 50 WP @ 2.5g/L का छिड़काव करें।"
            AppLanguage.MARATHI -> "प्रतिबंधक १% बोर्डो मिश्रण किंवा कॉपर ऑक्सिक्लोराईड ५० WP @ २.५ ग्रॅम/लिटर फवारा."
            AppLanguage.BENGALI -> "সুরক্ষামূলক ১% বোর্দো মিশ্রণ বা কপার অক্সিক্লোরাইড ৫০ WP @ ২.৫ গ্রাম/লিটার স্প্রে করুন।"
            AppLanguage.SPANISH -> "Aplique caldo bordelés al 1% o oxicloruro de cobre 50 WP a 2.5 g/L."
            AppLanguage.FRENCH -> "Appliquer de la bouillie bordelaise 1% ou de l'oxychlorure de cuivre 50 WP à 2,5 g/L."
            AppLanguage.SWAHILI -> "Nyunyizia mchanganyiko wa Bordeaux 1% au Copper Oxychloride 50 WP gramu 2.5/L."
        }
        action.contains("Trichoderma", ignoreCase = true) || action.contains("Copper Hydroxide", ignoreCase = true) -> when (lang) {
            AppLanguage.ENGLISH -> action
            AppLanguage.HINDI -> "पत्तियों की निचली सतह की जांच करें; सुरक्षात्मक ट्राइकोडर्मा या कॉपर हाइड्रॉक्साइड का छिड़काव करें।"
            AppLanguage.MARATHI -> "पानांची खालची बाजू तपासा; संरक्षक ट्रायकोडर्मा किंवा कॉपर हायड्रॉक्साइड फवारा."
            AppLanguage.BENGALI -> "পাতার নিচে পরীক্ষা করুন; সুরক্ষামূলক ট্রাইকোডার্মা বা কপার হাইড্রক্সাইড স্প্রে করুন।"
            AppLanguage.SPANISH -> "Inspeccione el envés de las hojas; aplique Trichoderma o hidróxido de cobre."
            AppLanguage.FRENCH -> "Inspecter la face inférieure des feuilles ; appliquer Trichoderma ou hydroxyde de cuivre."
            AppLanguage.SWAHILI -> "Kagua chini ya majani; nyunyizia kinga ya Trichoderma au Copper Hydroxide."
        }
        else -> action
    }

    // Additional Localization Helpers for Hotspots & Visualizer
    fun radiusRange(lang: AppLanguage): String = rangeRadiusLabel(lang)
    fun confirmedFarmCases(count: Int, lang: AppLanguage): String = confirmedFarmCasesIn48h(count, lang)
    fun geospatialSurveillanceHeader(lang: AppLanguage): String = geospatialOutbreakSurveillance(lang)
    fun aggregatedFromRecords(count: Int, lang: AppLanguage): String = aggregatedFromReports(count, lang)
    fun regionalSectorMatrix(lang: AppLanguage): String = regionalEpidemicSectorMatrix(lang)

    fun confirmedDiagnosesTargetCrop(count: Int, crop: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$count Confirmed Farm Diagnoses • Target Crop: $crop"
        AppLanguage.HINDI -> "$count पुष्ट खेत निदान • लक्षित फसल: $crop"
        AppLanguage.MARATHI -> "$count पुष्टी झालेले शेत निदान • लक्षित पीक: $crop"
        AppLanguage.SPANISH -> "$count Diagnósticos Confirmados • Cultivo: $crop"
        AppLanguage.SWAHILI -> "Utambuzi $count Uliothibitishwa • Zao: $crop"
        AppLanguage.BENGALI -> "$count টি নিশ্চিত খামার ডায়াগনসিস • লক্ষ্য ফসল: $crop"
        AppLanguage.FRENCH -> "$count Diagnostics Confirmés • Culture Cible : $crop"
    }

    fun cropClusterRadius(crop: String, radius: Float, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Crop: $crop • Cluster Radius: $radius km"
        AppLanguage.HINDI -> "फसल: $crop • क्लस्टर त्रिज्या: $radius किमी"
        AppLanguage.MARATHI -> "पीक: $crop • क्लस्टर त्रिज्या: $radius किमी"
        AppLanguage.SPANISH -> "Cultivo: $crop • Radio: $radius km"
        AppLanguage.SWAHILI -> "Zao: $crop • Eneo: km $radius"
        AppLanguage.BENGALI -> "ফসল: $crop • ক্লাস্টার ব্যাসার্ধ: $radius কিমি"
        AppLanguage.FRENCH -> "Culture : $crop • Rayon : $radius km"
    }

    fun allFilter(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All"
        AppLanguage.HINDI -> "सभी"
        AppLanguage.MARATHI -> "सर्व"
        AppLanguage.SPANISH -> "Todos"
        AppLanguage.SWAHILI -> "Yote"
        AppLanguage.BENGALI -> "সব"
        AppLanguage.FRENCH -> "Tous"
    }

    fun allSeverities(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All Severities"
        AppLanguage.HINDI -> "सभी गंभीरता"
        AppLanguage.MARATHI -> "सर्व तीव्रता"
        AppLanguage.SPANISH -> "Todas las Severidades"
        AppLanguage.SWAHILI -> "Viwango Vyote"
        AppLanguage.BENGALI -> "সমস্ত তীব্রতা"
        AppLanguage.FRENCH -> "Toutes Sévérités"
    }

    fun severityLabel(severity: ThreatSeverity, lang: AppLanguage): String = when (severity) {
        ThreatSeverity.CRITICAL -> when (lang) {
            AppLanguage.ENGLISH -> "Severe Alert"
            AppLanguage.HINDI -> "गंभीर चेतावनी"
            AppLanguage.MARATHI -> "गंभीर इशारा"
            AppLanguage.SPANISH -> "Alerta Grave"
            AppLanguage.SWAHILI -> "Tahadhari Kubwa"
            AppLanguage.BENGALI -> "জরুরি সতর্কতা"
            AppLanguage.FRENCH -> "Alerte Sévère"
        }
        ThreatSeverity.HIGH -> when (lang) {
            AppLanguage.ENGLISH -> "High Outbreak"
            AppLanguage.HINDI -> "उच्च प्रकोप"
            AppLanguage.MARATHI -> "उच्च प्रादुर्भाव"
            AppLanguage.SPANISH -> "Brote Alto"
            AppLanguage.SWAHILI -> "Mlipuko Mkubwa"
            AppLanguage.BENGALI -> "উচ্চ প্রাদুর্ভাব"
            AppLanguage.FRENCH -> "Épidémie Élevée"
        }
        ThreatSeverity.MODERATE -> when (lang) {
            AppLanguage.ENGLISH -> "Moderate Risk"
            AppLanguage.HINDI -> "मध्यम जोखिम"
            AppLanguage.MARATHI -> "मध्यम धोका"
            AppLanguage.SPANISH -> "Riesgo Moderado"
            AppLanguage.SWAHILI -> "Hatari ya Wastani"
            AppLanguage.BENGALI -> "মাঝারি ঝুঁকি"
            AppLanguage.FRENCH -> "Risque Modéré"
        }
        ThreatSeverity.LOW -> when (lang) {
            AppLanguage.ENGLISH -> "Low Risk"
            AppLanguage.HINDI -> "कम जोखिम"
            AppLanguage.MARATHI -> "कमी धोका"
            AppLanguage.SPANISH -> "Riesgo Bajo"
            AppLanguage.SWAHILI -> "Hatari Ndogo"
            AppLanguage.BENGALI -> "কম ঝুঁকি"
            AppLanguage.FRENCH -> "Faible Risque"
        }
    }

    fun allCropsFilter(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "ALL CROPS"
        AppLanguage.HINDI -> "सभी फसलें"
        AppLanguage.MARATHI -> "सर्व पिके"
        AppLanguage.SPANISH -> "TODOS LOS CULTIVOS"
        AppLanguage.SWAHILI -> "MAZAO YOTE"
        AppLanguage.BENGALI -> "সমস্ত ফসল"
        AppLanguage.FRENCH -> "TOUTES CULTURES"
    }

    fun keySymptomsAndTriage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Key Symptoms & Triage:"
        AppLanguage.HINDI -> "मुख्य लक्षण और ट्राइएज:"
        AppLanguage.MARATHI -> "प्रमुख लक्षणे आणि तपासणी:"
        AppLanguage.SPANISH -> "Síntomas Clave y Triaje:"
        AppLanguage.SWAHILI -> "Dalili Kuu na Uchunguzi:"
        AppLanguage.BENGALI -> "প্রধান লক্ষণ ও ট্রায়াজ:"
        AppLanguage.FRENCH -> "Symptômes Clés et Triage :"
    }

    fun fourTierProtocolSummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "4-Tier Protocol Summary:"
        AppLanguage.HINDI -> "4-स्तरीय प्रोटोकॉल सारांश:"
        AppLanguage.MARATHI -> "४-स्तरीय प्रोटोकॉल सारांश:"
        AppLanguage.SPANISH -> "Resumen del Protocolo de 4 Niveles:"
        AppLanguage.SWAHILI -> "Muhtasari wa Hatua 4 za Mwongozo:"
        AppLanguage.BENGALI -> "৪-স্তরের প্রোটোকল সারসংক্ষেপ:"
        AppLanguage.FRENCH -> "Résumé du Protocole à 4 Niveaux :"
    }

    fun exploreDecisionTreeBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Explore Decision Tree"
        AppLanguage.HINDI -> "निर्णय वृक्ष देखें"
        AppLanguage.MARATHI -> "निर्णय वृक्ष एक्सप्लोर करा"
        AppLanguage.SPANISH -> "Explorar Árbol de Decisión"
        AppLanguage.SWAHILI -> "Chunguza Mti wa Uamuzi"
        AppLanguage.BENGALI -> "সিদ্ধান্ত বৃক্ষ অন্বেষণ করুন"
        AppLanguage.FRENCH -> "Explorer l'Arbre de Décision"
    }

    fun viewFullIpmProtocolBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View Full IPM Protocol"
        AppLanguage.HINDI -> "पूर्ण आईपीएम प्रोटोकॉल देखें"
        AppLanguage.MARATHI -> "संपूर्ण आयपीएम प्रोटोकॉल पहा"
        AppLanguage.SPANISH -> "Ver Protocolo Completo"
        AppLanguage.SWAHILI -> "Tazama Mwongozo Kamili wa IPM"
        AppLanguage.BENGALI -> "সম্পূর্ণ আইপিএম প্রোটোকল দেখুন"
        AppLanguage.FRENCH -> "Voir le Protocole Complet"
    }

    fun broadcastDistrictAdvisoryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Broadcast District Advisory"
        AppLanguage.HINDI -> "जिला स्तरीय सलाह प्रसारित करें"
        AppLanguage.MARATHI -> "जिल्हास्तरीय सल्ला प्रसारित करा"
        AppLanguage.SPANISH -> "Emitir Aviso Distrital"
        AppLanguage.SWAHILI -> "Tangaza Ushauri wa Wilaya"
        AppLanguage.BENGALI -> "জেলা পরামর্শ সম্প্রচার করুন"
        AppLanguage.FRENCH -> "Diffuser l'Avis de District"
    }

    fun advisoryTitleLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Advisory Title"
        AppLanguage.HINDI -> "सलाह का शीर्षक"
        AppLanguage.MARATHI -> "सल्ल्याचे शीर्षक"
        AppLanguage.SPANISH -> "Título del Aviso"
        AppLanguage.SWAHILI -> "Kichwa cha Ushauri"
        AppLanguage.BENGALI -> "পরামর্শের শিরোনাম"
        AppLanguage.FRENCH -> "Titre de l'Avis"
    }

    fun broadcastAgroZoneLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Broadcast Agro-Climatic Zone"
        AppLanguage.HINDI -> "प्रसारण कृषि-जलवायु क्षेत्र"
        AppLanguage.MARATHI -> "प्रसारण कृषी-हवामान विभाग"
        AppLanguage.SPANISH -> "Zona Agroclimática"
        AppLanguage.SWAHILI -> "Eneo la Kilimo na Hali ya Hewa"
        AppLanguage.BENGALI -> "কৃষি-জলবায়ু অঞ্চল"
        AppLanguage.FRENCH -> "Zone Agro-Climatique"
    }

    fun targetCropsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Target Crops"
        AppLanguage.HINDI -> "लक्षित फसलें"
        AppLanguage.MARATHI -> "लक्षित पिके"
        AppLanguage.SPANISH -> "Cultivos Objetivo"
        AppLanguage.SWAHILI -> "Mazao Lengwa"
        AppLanguage.BENGALI -> "লক্ষ্য ফসল"
        AppLanguage.FRENCH -> "Cultures Cibles"
    }

    fun advisoryMessageLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Advisory Message"
        AppLanguage.HINDI -> "सलाह संदेश"
        AppLanguage.MARATHI -> "सल्ला संदेश"
        AppLanguage.SPANISH -> "Mensaje del Aviso"
        AppLanguage.SWAHILI -> "Ujumbe wa Ushauri"
        AppLanguage.BENGALI -> "পরামর্শ বার্তা"
        AppLanguage.FRENCH -> "Message de l'Avis"
    }

    fun requiredActionForFarmersLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Required Action for Farmers"
        AppLanguage.HINDI -> "किसानों के लिए आवश्यक कार्रवाई"
        AppLanguage.MARATHI -> "शेतकऱ्यांसाठी आवश्यक कृती"
        AppLanguage.SPANISH -> "Acción Requerida para Agricultores"
        AppLanguage.SWAHILI -> "Hatua Inayotakiwa kwa Wakulima"
        AppLanguage.BENGALI -> "কৃষকদের জন্য প্রয়োজনীয় পদক্ষেপ"
        AppLanguage.FRENCH -> "Action Requise pour les Agriculteurs"
    }

    fun broadcastNowBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Broadcast Now"
        AppLanguage.HINDI -> "अभी प्रसारित करें"
        AppLanguage.MARATHI -> "आत्ताच प्रसारित करा"
        AppLanguage.SPANISH -> "Emitir Ahora"
        AppLanguage.SWAHILI -> "Tangaza Sasa"
        AppLanguage.BENGALI -> "এখনই সম্প্রচার করুন"
        AppLanguage.FRENCH -> "Diffuser Maintenant"
    }

    fun issuedByLabel(name: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Issued by: $name"
        AppLanguage.HINDI -> "जारीकर्ता: $name"
        AppLanguage.MARATHI -> "जारीकर्ता: $name"
        AppLanguage.SPANISH -> "Emitido por: $name"
        AppLanguage.SWAHILI -> "Imetolewa na: $name"
        AppLanguage.BENGALI -> "প্রকাশক: $name"
        AppLanguage.FRENCH -> "Émis par : $name"
    }

    fun actionLabel(action: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Action: $action"
        AppLanguage.HINDI -> "कार्रवाई: $action"
        AppLanguage.MARATHI -> "कृती: $action"
        AppLanguage.SPANISH -> "Acción: $action"
        AppLanguage.SWAHILI -> "Hatua: $action"
        AppLanguage.BENGALI -> "পদক্ষেপ: $action"
        AppLanguage.FRENCH -> "Action : $action"
    }

    fun localizeCropName(crop: String, lang: AppLanguage): String = cropName(crop, lang)

    fun confidenceAndStage(confidence: Int, stage: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Confidence: $confidence% • Stage: $stage"
        AppLanguage.HINDI -> "विश्वसनीयता: $confidence% • चरण: $stage"
        AppLanguage.MARATHI -> "विश्वासार्हता: $confidence% • अवस्था: $stage"
        AppLanguage.SPANISH -> "Confianza: $confidence% • Etapa: $stage"
        AppLanguage.SWAHILI -> "Uhakika: $confidence% • Hatua: $stage"
        AppLanguage.BENGALI -> "নির্ভুলতা: $confidence% • ধাপ: $stage"
        AppLanguage.FRENCH -> "Confiance : $confidence% • Stade : $stage"
    }

    fun specimenSummary(crop: String, disease: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Specimen: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.HINDI -> "नमूना: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.MARATHI -> "नमुना: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.SPANISH -> "Muestra: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.SWAHILI -> "Sampuli: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.BENGALI -> "নমুনা: ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
        AppLanguage.FRENCH -> "Spécimen : ${localizeCropName(crop, lang)} (${diseaseName(disease, lang)})"
    }

    fun reportedByFarmer(farmer: String, region: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Reported by $farmer • $region"
        AppLanguage.HINDI -> "$farmer द्वारा सूचित • $region"
        AppLanguage.MARATHI -> "$farmer यांनी नोंदवले • $region"
        AppLanguage.SPANISH -> "Reportado por $farmer • $region"
        AppLanguage.SWAHILI -> "Imeripotiwa na $farmer • $region"
        AppLanguage.BENGALI -> "$farmer দ্বারা রিপোর্ট করা • $region"
        AppLanguage.FRENCH -> "Signalé par $farmer • $region"
    }

    fun zoneAndCrops(zone: String, crops: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Zone: $zone • Crops: $crops"
        AppLanguage.HINDI -> "जोन: $zone • फसलें: $crops"
        AppLanguage.MARATHI -> "झोन: $zone • पिके: $crops"
        AppLanguage.SPANISH -> "Zona: $zone • Cultivos: $crops"
        AppLanguage.SWAHILI -> "Ukanda: $zone • Mazao: $crops"
        AppLanguage.BENGALI -> "জোন: $zone • ফসল: $crops"
        AppLanguage.FRENCH -> "Zone : $zone • Cultures : $crops"
    }

    fun autoDetect(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Auto-Detect"
        AppLanguage.HINDI -> "स्वतः पहचानें"
        AppLanguage.MARATHI -> "स्वयं-ओळखा"
        AppLanguage.SPANISH -> "Detección automática"
        AppLanguage.SWAHILI -> "Gundua Kiotomatiki"
        AppLanguage.BENGALI -> "স্বয়ংক্রিয়ভাবে সনাক্ত করুন"
        AppLanguage.FRENCH -> "Détection automatique"
    }
}

