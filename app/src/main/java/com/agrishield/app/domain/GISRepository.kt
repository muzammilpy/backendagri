package com.agrishield.app.domain

import com.agrishield.app.data.model.DistrictHotspot
import com.agrishield.app.data.model.Severity

object GISRepository {

    val maharashtraDistricts: List<DistrictHotspot> = listOf(
        DistrictHotspot("Yavatmal","Amravati",20.3888,78.1204,listOf("Cotton","Soybean","Tur"),"Black Cotton Soil","Pink Bollworm",Severity.CRITICAL,14,0.42,true,"ETL exceeded. Immediate pheromone trap + Chlorantraniliprole required.","ETL ओलांडले. फेरोमोन सापळे + क्लोरंट्रॅनिलिप्रोल आवश्यक."),
        DistrictHotspot("Wardha","Nagpur",20.7453,78.6022,listOf("Cotton","Soybean"),"Black Cotton Soil","Pink Bollworm",Severity.CRITICAL,15,0.40,true,"CRITICAL outbreak. Immediate chemical intervention.","गंभीर प्रादुर्भाव. तत्काळ रासायनिक उपाय."),
        DistrictHotspot("Nashik","Nashik",20.0059,73.7898,listOf("Grapes","Onion","Tomato"),"Sandy Loam","Grape Downy Mildew",Severity.CRITICAL,0,0.39,true,"Downy mildew. Metalaxyl+Mancozeb before rain mandatory.","डाउनी मिल्ड्यू. पावसापूर्वी Metalaxyl+Mancozeb अनिवार्य."),
        DistrictHotspot("Jalgaon","Nashik",21.0077,75.5626,listOf("Cotton","Banana","Onion"),"Alluvial Loam","Whitefly",Severity.CRITICAL,22,0.39,true,"Severe Whitefly ETL breach. Spiromesifen spray immediately.","तीव्र पांढरी माशी ETL. Spiromesifen फवारणी तत्काळ."),
        DistrictHotspot("Amravati","Amravati",20.9320,77.7523,listOf("Cotton","Soybean","Orange"),"Medium Black","Helicoverpa Bollworm",Severity.HIGH,9,0.48,true,"High Helicoverpa. Scout 100 plants. NPV traps ready.","उच्च हेलिकोव्हर्पा. 100 झाडे पाहणी. NPV सापळे तयार."),
        DistrictHotspot("Akola","Amravati",20.7090,77.0082,listOf("Cotton","Soybean","Chickpea"),"Black Cotton","Whitefly CLCuV",Severity.HIGH,8,0.51,true,"Elevated whitefly. CLCuV risk. Yellow sticky traps now.","पांढरी माशी वाढ. CLCuV धोका. पिवळे सापळे लावा."),
        DistrictHotspot("Washim","Amravati",20.1100,77.1300,listOf("Cotton","Soybean"),"Black Cotton","Pink Bollworm",Severity.HIGH,10,0.44,true,"Pink bollworm trap catches rising. Apply before ETL breach.","PBW सापळे वाढत आहेत. ETL आधी उपाय करा."),
        DistrictHotspot("Nanded","Marathwada",19.1383,77.3210,listOf("Cotton","Soybean","Tur"),"Deep Black","Pink Bollworm",Severity.HIGH,11,0.43,true,"Bollworm approaching ETL. Set gossyplure traps now.","बोंड अळी ETL जवळ. Gossyplure सापळे लावा."),
        DistrictHotspot("Parbhani","Marathwada",19.2704,76.7747,listOf("Cotton","Soybean","Tur"),"Deep Black","Whitefly CLCuV",Severity.HIGH,7,0.46,true,"Whitefly ETL breach. Spiromesifen spray immediate.","पांढरी माशी ETL ओलांडले. Spiromesifen फवारणी."),
        DistrictHotspot("Aurangabad","Marathwada",19.8762,75.3433,listOf("Grapes","Cotton","Soybean"),"Medium Black","Grape Downy Mildew",Severity.HIGH,0,0.50,true,"Grape downy mildew risk. Metalaxyl spray needed.","द्राक्ष डाउनी मिल्ड्यू. Metalaxyl फवारणी आवश्यक."),
        DistrictHotspot("Jalna","Marathwada",19.8347,75.8816,listOf("Cotton","Soybean","Tur"),"Deep Black","Soybean Yellow Mosaic",Severity.HIGH,0,0.47,true,"YMV >5%. Remove infected plants. Control whitefly.","YMV >5%. रोगग्रस्त झाडे काढा. पांढरी माशी नियंत्रण."),
        DistrictHotspot("Latur","Marathwada",18.4088,76.5604,listOf("Soybean","Tur","Chickpea"),"Deep Black","Pigeonpea Wilt",Severity.HIGH,0,0.44,true,"Fusarium wilt spreading. Remove wilted plants. Soil solarize.","फ्युजेरियम मर पसरतोय. विलेपित झाडे काढा."),
        DistrictHotspot("Dhule","Nashik",20.9020,74.7748,listOf("Cotton","Maize","Soybean"),"Medium Loam","Fall Armyworm",Severity.HIGH,0,0.47,true,"FAW outbreak in maize. Chlorantraniliprole whorl treatment.","मक्यात FAW उद्रेक. Chlorantraniliprole खोड उपचार."),
        DistrictHotspot("Ahmednagar","Nashik",19.0952,74.7389,listOf("Sugarcane","Onion","Wheat"),"Medium Black","Onion Purple Blotch",Severity.HIGH,0,0.48,true,"Purple blotch spreading in onion. Iprodione spray needed.","कांद्यात जांभळा डाग. Iprodione फवारणी आवश्यक."),
        DistrictHotspot("Hingoli","Marathwada",19.7160,77.1490,listOf("Soybean","Cotton","Tur"),"Black Cotton","Soybean Yellow Mosaic",Severity.HIGH,0,0.46,true,"YMV spreading. Rogue plants; control whitefly vector.","YMV पसरतोय. रोगग्रस्त झाडे काढा."),
        DistrictHotspot("Chandrapur","Nagpur",19.9615,79.2961,listOf("Rice","Cotton"),"Laterite","Rice Blast",Severity.HIGH,0,0.46,true,"High blast risk. RH >80%. Apply Tricyclazole preventively.","उच्च करपा धोका. Tricyclazole प्रतिबंधात्मक फवारणी."),
        DistrictHotspot("Bhandara","Nagpur",21.1666,79.6500,listOf("Rice"),"Alluvial","Sheath Blight",Severity.HIGH,0,0.49,true,"Sheath blight spreading. Drain excess water.","आवरण करपा पसरत आहे. अतिरिक्त पाणी काढा."),
        DistrictHotspot("Nagpur","Nagpur",21.1458,79.0882,listOf("Orange","Cotton","Wheat"),"Red Sandy Loam","Thrips",Severity.MODERATE,6,0.53,false,"Orange thrips moderate. Monitor leaves. Blue sticky traps.","संत्र्यावर फुलकिडे मध्यम. पाने तपासा."),
        DistrictHotspot("Beed","Marathwada",18.9891,75.7601,listOf("Sugarcane","Cotton","Soybean"),"Medium Black","Sugarcane Smut",Severity.MODERATE,0,0.53,false,"Smut scouting recommended in ratoon sugarcane.","रटून उसात काणी सर्वेक्षण आवश्यक."),
        DistrictHotspot("Osmanabad","Marathwada",18.1860,76.0359,listOf("Soybean","Tur","Sunflower"),"Medium Black","Soybean Rust",Severity.MODERATE,0,0.50,false,"Monitor lower leaf undersides for rust. Prophylactic tebuconazole.","खालच्या पानांवर गंज तपासा. Tebuconazole तयार ठेवा."),
        DistrictHotspot("Gondia","Nagpur",21.4608,80.1917,listOf("Rice","Wheat"),"Medium Black","Brown Plant Hopper",Severity.MODERATE,0,0.52,false,"BPH building. Monitor 10 hills/point. Buprofezin ready.","BPH वाढतोय. 10 झुडपे/बिंदू तपासा."),
        DistrictHotspot("Buldhana","Amravati",20.5292,76.1842,listOf("Cotton","Soybean","Tur"),"Black Cotton","Whitefly",Severity.MODERATE,5,0.55,false,"Moderate whitefly. Preventive neem spray.","मध्यम पांढरी माशी. प्रतिबंधात्मक निंब फवारणी."),
        DistrictHotspot("Sangli","Pune",16.8524,74.5815,listOf("Grapes","Sugarcane","Turmeric"),"Sandy Loam","Grape Downy Mildew",Severity.HIGH,0,0.43,true,"Downy mildew elevated in vineyards during monsoon.","पावसात द्राक्ष मळ्यांत डाउनी मिल्ड्यू उंचावलेला."),
        DistrictHotspot("Pune","Pune",18.5204,73.8567,listOf("Grapes","Onion","Tomato"),"Laterite","Powdery Mildew",Severity.MODERATE,0,0.55,false,"Powdery mildew moderate on grapes. Sulphur spray advisory.","द्राक्षावर भुरी रोग मध्यम. गंधक फवारणी सल्ला."),
        DistrictHotspot("Satara","Pune",17.6805,74.0183,listOf("Sugarcane","Tomato","Pomegranate"),"Red Sandy Loam","Bacterial Blight",Severity.MODERATE,0,0.56,false,"Bacterial blight in pomegranate orchards. Copper spray.","डाळिंब बागेत जिवाणूजन्य करपा. कॉपर फवारणी."),
        DistrictHotspot("Solapur","Pune",17.6599,75.9064,listOf("Sorghum","Cotton","Pomegranate"),"Black Cotton","Stem Borer",Severity.MODERATE,0,0.54,false,"Sorghum stem borer scouting. Pheromone traps deployed.","ज्वारी खोड किडा. फेरोमोन सापळे लावलेले."),
        DistrictHotspot("Kolhapur","Pune",16.7050,74.2433,listOf("Sugarcane","Rice","Banana"),"Red Laterite","Pyrilla",Severity.LOW,0,0.60,false,"Low pyrilla. Epipyrops parasitoid controlling naturally.","कमी पायरिला. Epipyrops परजीवी नैसर्गिकरित्या नियंत्रण."),
        DistrictHotspot("Nandurbar","Nashik",21.3700,74.2400,listOf("Rice","Maize","Tur"),"Laterite","Stem Borer",Severity.MODERATE,0,0.52,false,"Stem borer monitoring. Trichogramma @ 50,000/acre.","खोड किडा देखरेख. Trichogramma @ 50,000/एकर."),
        DistrictHotspot("Raigad","Konkan",18.5158,73.1298,listOf("Rice","Mango","Cashew"),"Laterite","Rice Blast",Severity.MODERATE,0,0.50,false,"Rice blast risk. Tricyclazole 75 WP precaution.","भात करपा धोका. Tricyclazole 75 WP सावधानता."),
        DistrictHotspot("Ratnagiri","Konkan",16.9902,73.3120,listOf("Mango","Cashew","Rice"),"Laterite","Powdery Mildew",Severity.LOW,0,0.58,false,"Low disease pressure in mango orchards.","आंबा बागेत कमी रोग दबाव."),
        DistrictHotspot("Thane","Konkan",19.2183,72.9781,listOf("Rice","Vegetables"),"Laterite","Sheath Blight",Severity.MODERATE,0,0.51,false,"Sheath blight moderate in paddy. Monitor from tillering.","भातात आवरण करपा मध्यम. फुटव्यापासून देखरेख."),
        DistrictHotspot("Palghar","Konkan",19.6967,72.7697,listOf("Rice","Maize","Vegetables"),"Sandy Loam","Fall Armyworm",Severity.MODERATE,0,0.52,false,"FAW monitoring in maize blocks.","मक्यात FAW देखरेख."),
        DistrictHotspot("Gadchiroli","Nagpur",20.1809,79.9444,listOf("Rice","Teak"),"Red Laterite","Rice Blast",Severity.MODERATE,0,0.51,false,"Blast risk in tribal rice. Resistant varieties recommended.","आदिवासी भातशेतीत करपा धोका."),
        DistrictHotspot("Sindhudurg","Konkan",16.3500,73.8200,listOf("Coconut","Cashew","Rice"),"Coastal Laterite","Aphids",Severity.LOW,0,0.63,false,"Minimal pest activity. Favorable conditions.","कमीतकमी कीड क्रियाकलाप. अनुकूल परिस्थिती."),
        DistrictHotspot("Mumbai Suburban","Konkan",19.0760,72.8777,listOf("Vegetables","Rice"),"Coastal Sandy","Aphids",Severity.LOW,0,0.62,false,"Urban vegetable gardens. Low aphid. Neem preventive.","शहरी भाजीपाला बागा. कमी मावा. निंब प्रतिबंधात्मक.")
    )

    fun getCriticalDistricts() = maharashtraDistricts.filter { it.outbreakRisk == Severity.CRITICAL }
    fun getHighRiskDistricts() = maharashtraDistricts.filter { it.outbreakRisk == Severity.HIGH }
    fun getByDivision(div: String) = maharashtraDistricts.filter { it.division == div }
    fun getByCrop(crop: String) = maharashtraDistricts.filter {
        it.majorCrops.any { c -> c.contains(crop, ignoreCase = true) }
    }
}
