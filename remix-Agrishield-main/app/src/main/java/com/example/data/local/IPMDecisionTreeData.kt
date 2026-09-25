package com.example.data.local

import com.example.model.*

object IPMDecisionTreeData {

    val allRecommendations: List<IPMProtocolRecommendation> = listOf(
        IPMProtocolRecommendation(
            diseaseId = "rec_late_blight",
            diseaseName = "Late Blight",
            scientificName = "Phytophthora infestans",
            targetCrops = listOf("Tomato", "Potato"),
            pathogenType = "Oomycete / Water Mold",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Water-soaked dark brown irregular foliar lesions, purplish margins, white fungal bloom on undersides during cool, wet weather (>80% RH, 15–22°C).",
            culturalActions = listOf(
                IPMActionItem("Certified Disease-Free Seed", "Plant only certified certified seed tubers or healthy nursery seedlings."),
                IPMActionItem("Canopy Spacing & Ridge Drainage", "Adopt wide row spacing (60 x 45 cm) and raised beds to prevent water retention.", timingOrFrequency = "At planting"),
                IPMActionItem("Drip vs Overhead Irrigation", "Switch strictly to drip irrigation; eliminate overhead sprinkler irrigation to avoid leaf wetness."),
                IPMActionItem("Rogueing Infected Debris", "Immediately uproot and burn/bury severely blighted plants outside the field perimeter.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Plastic Mulching", "Apply silver-black polyethylene mulch (25 micron) to inhibit soil-borne sporangia splash."),
                IPMActionItem("Sanitation of Pruning Tools", "Dip shears in 10% sodium hypochlorite solution between handling rows.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Trichoderma harzianum Soil & Foliar", "Spray Trichoderma harzianum @ 5g/liter water or drench root zone @ 10g/L.", dosageOrMethod = "5 g / L water", timingOrFrequency = "Every 10–14 days prophylactically"),
                IPMActionItem("Pseudomonas fluorescens Bio-protectant", "Apply Pseudomonas fluorescens 1% WP as prophylactic spray.", dosageOrMethod = "5 g / L water"),
                IPMActionItem("Cold-pressed Neem Seed Extract", "Spray 5% Neem Seed Kernel Extract (NSKE) or Azadirachtin 10,000 ppm.", dosageOrMethod = "3 ml / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Protective Copper Oxychloride 50 WP", "Preventive contact spray during overcast humid weather conditions.", dosageOrMethod = "2.5 g / L water", timingOrFrequency = "Preventive before symptoms spread", safetyWarning = "PHI: 3 days"),
                IPMActionItem("Mancozeb 75 WP (Contact)", "Foliar barrier spray prior to rain forecast.", dosageOrMethod = "2.0 g / L water", safetyWarning = "PHI: 5 days"),
                IPMActionItem("Cymoxanil + Mancozeb (Curative Rescue)", "Use only if lesion expansion exceeds 5% field threshold.", dosageOrMethod = "2.0 g / L water", safetyWarning = "Strict curative only. Max 2 sprays/season. PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf(
                "Monitor relative humidity thresholds (>80% RH triggers spore release)",
                "Avoid working in field when foliage is wet with morning dew",
                "Maintain 3-year non-solanaceous crop rotation (e.g. follow with maize/legumes)"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_early_blight",
            diseaseName = "Early Blight",
            scientificName = "Alternaria solani",
            targetCrops = listOf("Tomato", "Potato", "Eggplant"),
            pathogenType = "Fungal",
            severityLevel = ThreatSeverity.HIGH,
            keySymptomsSummary = "Dark brown circular spots with distinctive concentric rings ('target-board pattern') surrounded by chlorotic yellow halos on older lower leaves.",
            culturalActions = listOf(
                IPMActionItem("Lower Leaf Pruning (De-suckering)", "Prune lower senescent leaves (bottom 20 cm) to stop soil contact and improve air circulation."),
                IPMActionItem("Balanced Nitrogen & Potassium Nutrition", "Avoid nitrogen stress; maintain optimum potassium (K) levels to strengthen cell walls.", timingOrFrequency = "Split application at flowering"),
                IPMActionItem("Crop Rotation", "Rotate fields with cereals, pulses, or brassicas for minimum 2 seasons.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Straw Mulching", "Apply paddy straw or dried grass mulch (5-7 cm thickness) to reduce raindrop splash."),
                IPMActionItem("Staking & Trellising", "Stake indeterminate tomato plants on bamboo poles or trellis wires.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Bacillus subtilis Bio-Fungicide", "Spray Bacillus subtilis (QST 713 strain) at first notice of lower leaf flecks.", dosageOrMethod = "3 g / L water", timingOrFrequency = "Weekly"),
                IPMActionItem("Trichoderma viride Foliar Spray", "Spray Trichoderma viride 1% WP formulation mixed with 1% jaggery solution.", dosageOrMethod = "5 g / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Chlorothalonil 75 WP or Mancozeb", "Protective multi-site contact fungicide.", dosageOrMethod = "2.0 g / L water", safetyWarning = "PHI: 5 days"),
                IPMActionItem("Azoxystrobin 23% SC + Difenoconazole", "Translaminar systemic treatment for rapid blight arrest.", dosageOrMethod = "1.0 ml / L water", safetyWarning = "PHI: 5 days. Alternate with contact fungicide to prevent resistance.")
            ),
            preHarvestIntervalDays = 5,
            preventionChecklist = listOf(
                "Scout lower canopy leaves weekly for early target spots",
                "Ensure clean removal of crop residues post-harvest"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_whitefly_tylcv",
            diseaseName = "Whitefly & Tomato Yellow Leaf Curl Virus (TYLCV)",
            scientificName = "Bemisia tabaci / Begomovirus",
            targetCrops = listOf("Tomato", "Chilli", "Cotton", "Okra", "Papaya"),
            pathogenType = "Viral Vector / Insect Pest",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Leaves curling upward (cupping), interveinal chlorosis, severely stunted bushy plant habit, tiny white fluttering insects under leaf surfaces.",
            culturalActions = listOf(
                IPMActionItem("TYLCV-Resistant Hybrid Cultivars", "Sow certified TYLCV-resistant tomato hybrids (e.g. Abhinav, US-440, ToLCV resistant lines)."),
                IPMActionItem("Barrier Crop Border Planting", "Plant 2-3 border rows of tall maize, pearl millet, or sorghum 20 days prior to transplanting to act as live physical vector barriers.", timingOrFrequency = "Pre-season"),
                IPMActionItem("Immediate Rogueing of Infected Plants", "Uproot and bag viral-infected plants during the first 30 days post-transplant.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Yellow Sticky Traps (IPM Standard)", "Install yellow sticky traps at canopy level at density of 15–20 traps/acre.", dosageOrMethod = "15-20 traps / acre", timingOrFrequency = "Install right at transplanting"),
                IPMActionItem("Insect-Proof Nursery Netting (40-50 Mesh)", "Raise seedlings under 40-50 mesh nylon insect nets to ensure virus-free transplants.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Beauveria bassiana / Verticillium lecanii", "Entomopathogenic fungi spray targeting whitefly nymphs on leaf undersides during evening hours.", dosageOrMethod = "5 ml or 5 g / L water", timingOrFrequency = "Repeat every 7 days"),
                IPMActionItem("Chrysoperla carnea Predator Release", "Release green lacewing larvae (Chrysoperla carnea) in field.", dosageOrMethod = "10,000 larvae / acre"),
                IPMActionItem("Neem Oil 10,000 ppm (Azadirachtin)", "Repels adults and inhibits egg oviposition.", dosageOrMethod = "3 ml / L water + 0.5 ml sticker")
            ),
            chemicalActions = listOf(
                IPMActionItem("Pyriproxyfen 10% EC (Insect Growth Regulator)", "Targeted IGR that sterilizes female whiteflies and prevents nymph molting.", dosageOrMethod = "1.5 ml / L water", safetyWarning = "Selective action; preserves beneficials. PHI: 5 days"),
                IPMActionItem("Diafenthiuron 50% WP (Rescue Spray)", "Use only if whitefly threshold exceeds 8 adults per leaf.", dosageOrMethod = "1.2 g / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 5,
            preventionChecklist = listOf(
                "Keep farm borders free from weed hosts (Parthenium, Abutilon)",
                "Monitor sticky trap counts twice a week",
                "Avoid excessive chemical pyrethroids that trigger secondary whitefly surges"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_chilli_thrips_mites",
            diseaseName = "Chilli Murda Complex (Thrips & Yellow Mites)",
            scientificName = "Scirtothrips dorsalis / Polyphagotarsonemus latus",
            targetCrops = listOf("Chilli", "Capsicum", "Tomato", "Brinjal"),
            pathogenType = "Insect Pest & Acari",
            severityLevel = ThreatSeverity.HIGH,
            keySymptomsSummary = "Upward curling with boat-shaped leaf margins indicates thrips; downward curling ('inverted cup') with leaf thickening and bronzing underneath indicates mites.",
            culturalActions = listOf(
                IPMActionItem("Balanced Nitrogen Application", "Avoid heavy single-dose urea applications which produce succulent foliage favored by thrips.", timingOrFrequency = "Apply nitrogen in 4 equal splits"),
                IPMActionItem("Intercropping with Marigold / Coriander", "Intercrop 1 line of African marigold or coriander for every 10 rows of chilli as trap/repellent crops.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Blue & Yellow Sticky Traps", "Install blue sticky traps (specific for thrips attraction) and yellow traps (for aphids/whiteflies).", dosageOrMethod = "12 blue + 8 yellow traps / acre"),
                IPMActionItem("Sprinkler Mist Washing", "Short 15-minute overhead misting during hot dry afternoons to dislodge thrips and raise microclimate RH.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Lecanicillium (Verticillium) lecanii", "Bio-pesticide spray targeting thrips and mite colonies on foliage underside.", dosageOrMethod = "5 g / L water", timingOrFrequency = "Spray at dusk"),
                IPMActionItem("Pongamia / Karanj Oil + Neem Oil", "Botanical anti-feedant spray.", dosageOrMethod = "3 ml Karanj oil + 2 ml Neem oil / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Spinosad 45% SC (Bio-rational Fermentation)", "Highly effective bacterial fermentation derived insecticide for thrips.", dosageOrMethod = "0.3 ml / L water", safetyWarning = "PHI: 3 days. Safe for natural predators."),
                IPMActionItem("Fenazaquin 10% EC or Spiromesifen 22.9% SC", "Targeted acaricide if mite symptoms (downward curl) predominate.", dosageOrMethod = "1.0 ml / L water", safetyWarning = "PHI: 5 days")
            ),
            preHarvestIntervalDays = 3,
            preventionChecklist = listOf(
                "Inspect under surfaces of young top terminal leaves with 10x hand lens",
                "Ensure soil moisture is maintained during hot dry spells"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_bacterial_wilt",
            diseaseName = "Bacterial Wilt",
            scientificName = "Ralstonia solanacearum",
            targetCrops = listOf("Tomato", "Brinjal / Eggplant", "Potato", "Chilli"),
            pathogenType = "Bacterial",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Rapid daytime wilting of plant while foliage stays completely green. Vascular browning in stem base; white milky bacterial threads stream from cut stem in glass of water.",
            culturalActions = listOf(
                IPMActionItem("Wilt-Resistant Rootstock Grafting", "Graft scions onto resistant wild brinjal (Solanum torvum) or resistant tomato rootstocks."),
                IPMActionItem("Soil Solarization with Clear Polyethylene", "Solarize nursery beds under transparent polythene sheet (25 micron) for 4–6 weeks during peak summer months.", timingOrFrequency = "Pre-sowing summer"),
                IPMActionItem("Crop Rotation with Non-Host Cereals", "Rotate with paddy, maize, sorghum, or marigold for 2-3 seasons. Avoid all Solanaceae."),
                IPMActionItem("Liming Acidic Soils", "Apply agricultural lime (CaCO3) to raise soil pH to 6.5–7.0 (Ralstonia thrives in acidic wet soil).")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Isolation Trenching", "Dig 30 cm drainage isolation trenches around infected plot patches to prevent irrigation runoff spreading bacteria."),
                IPMActionItem("Cautious Cultivation", "Avoid deep inter-cultivation that damages root systems and provides bacterial entry wounds.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Pseudomonas fluorescens & Bacillus Root Dip", "Dip seedling roots for 30 minutes before transplanting in Pseudomonas solution.", dosageOrMethod = "10 g / L water", timingOrFrequency = "At transplanting"),
                IPMActionItem("Trichoderma Enriched FYM Drench", "Enrich 100 kg farmyard manure with 2 kg Trichoderma and apply to planting holes.", dosageOrMethod = "250 g enriched FYM / planting pit")
            ),
            chemicalActions = listOf(
                IPMActionItem("Streptomycin Sulphate + Tetracycline (Plantomycin)", "Soil collar drench around healthy surrounding plants in spot infection radius.", dosageOrMethod = "0.5 g / L water + Copper Oxychloride 2 g/L", safetyWarning = "Root zone drench only. Do not spray edible produce. PHI: 14 days"),
                IPMActionItem("Bleaching Powder Soil Treatment", "Apply bleaching powder (calcium hypochlorite) in irrigation channels.", dosageOrMethod = "2 kg / acre before planting")
            ),
            preHarvestIntervalDays = 14,
            preventionChecklist = listOf(
                "Perform clear water glass stem streaming test for field confirmation",
                "Ensure nursery beds are completely free of root-knot nematode infestations"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_powdery_mildew",
            diseaseName = "Powdery Mildew",
            scientificName = "Erysiphe / Podosphaera / Leveillula",
            targetCrops = listOf("Cucumber / Cucurbits", "Chilli", "Tomato", "Grapes", "Peas"),
            pathogenType = "Fungal",
            severityLevel = ThreatSeverity.MODERATE,
            keySymptomsSummary = "White, talcum powder-like fungal patches on upper and lower leaf surfaces, petioles, and young shoots; causes leaf yellowing, necrosis, and premature defoliation.",
            culturalActions = listOf(
                IPMActionItem("Canopy Pruning & Sunlight Penetration", "Prune overlapping branches and train vines on vertical trellises to maximize air movement."),
                IPMActionItem("Avoid Water Stress & Shading", "Maintain consistent soil irrigation; water-stressed shaded plants are highly susceptible to powdery mildew.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Removal of Severely Coated Foliage", "Clip off initial isolated infected leaves into plastic bags to avoid dispersing airborne conidia.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Ampelomyces quisqualis Bio-fungicide", "Hyperparasitic bio-fungicide that parasitizes powdery mildew mycelium.", dosageOrMethod = "5 g / L water"),
                IPMActionItem("Potassium Bicarbonate / Baking Soda Spray", "Spray potassium bicarbonate (or sodium bicarbonate) with liquid soap surfactant.", dosageOrMethod = "3 g / L water + 1 ml mild soap"),
                IPMActionItem("Milk-Water Dilution Spray (Organic)", "Foliar spray of 10-20% raw milk in water in bright sunlight generates free radicals that destroy spores.", dosageOrMethod = "100 ml milk / 900 ml water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Wettable Sulphur 80% WDG (Contact/Vapor)", "Standard multi-site preventive fungicide for powdery mildew.", dosageOrMethod = "2.0–2.5 g / L water", safetyWarning = "Do not spray when ambient temperature exceeds 32°C to prevent sulfur leaf scorch. PHI: 3 days"),
                IPMActionItem("Hexaconazole 5% EC or Penconazole", "Systemic ergosterol biosynthesis inhibitor for established infection.", dosageOrMethod = "1.0 ml / L water", safetyWarning = "PHI: 5 days")
            ),
            preHarvestIntervalDays = 3,
            preventionChecklist = listOf(
                "Scout shady corners of polyhouses and field perimeters",
                "Ensure sulfur sprays are applied during early morning cool hours"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_downy_mildew",
            diseaseName = "Downy Mildew",
            scientificName = "Pseudoperonospora cubensis / Peronospora",
            targetCrops = listOf("Cucumber", "Melons", "Grapes", "Onion", "Mustard"),
            pathogenType = "Oomycete",
            severityLevel = ThreatSeverity.HIGH,
            keySymptomsSummary = "Angular yellow chlorotic lesions strictly delimited by leaf veins on upper surface, with violet/grayish-brown downy sporulation on the lower leaf surface in morning dew.",
            culturalActions = listOf(
                IPMActionItem("Trellising Vines Off Ground", "Keep all vines elevated on bamboo wire trellises to avoid proximity to damp soil."),
                IPMActionItem("Morning Drip Irrigation", "Irrigate only early in the morning so foliage dries rapidly under sunlight.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Sanitary Leaf Stripping", "Strip infected lower senescent leaves and dispose in deep compost pit.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Trichoderma viride + Compost Tea", "Apply aerated compost tea with Trichoderma to enhance leaf phyllosphere immunity.", dosageOrMethod = "50 ml / L water"),
                IPMActionItem("Chitosan Foliar Spray", "Spray natural chitosan elicitor to trigger plant systemic acquired resistance (SAR).", dosageOrMethod = "2 ml / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Metalaxyl 8% + Mancozeb 64% WP (Ridomil)", "Systemic + contact protective and curative combination.", dosageOrMethod = "2.0 g / L water", safetyWarning = "Max 2 sprays per season. PHI: 7 days"),
                IPMActionItem("Fosetyl-Aluminium 80% WP", "Systemic phosphonate fungicide with true basipetal and acropetal translocation.", dosageOrMethod = "2.0 g / L water", safetyWarning = "PHI: 5 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf(
                "Track weather alerts for high humidity >85% + morning fog",
                "Select downy mildew tolerant cucumber/gourd hybrids"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_fall_armyworm",
            diseaseName = "Fall Armyworm (FAW)",
            scientificName = "Spodoptera frugiperda",
            targetCrops = listOf("Maize / Corn", "Sorghum", "Sugarcane", "Millet"),
            pathogenType = "Lepidopteran Insect Pest",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Pinholes in early whorl leaves, expanding to severe ragged window-pane feeding, heavy sawdust-like yellow-brown frass in leaf whorls, larvae with 4 dark spots in square on 8th segment and inverted Y-mark on head.",
            culturalActions = listOf(
                IPMActionItem("Synchronized Community Planting", "Plant within 10-day community planting window to prevent staggered food supply for pest."),
                IPMActionItem("Intercropping with Desmodium / Pulses (Push-Pull)", "Intercrop maize with silverleaf desmodium ('push' repellent) and plant Napier grass on borders ('pull' trap).")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Pheromone Trap Monitoring", "Install FAW specific sex pheromone delta traps.", dosageOrMethod = "5 traps / acre for monitoring, 15 for mass trapping"),
                IPMActionItem("Soil / Wood Ash Whorl Application", "Drop fine dry sand mixed with wood ash (9:1) or neem cake powder into infested plant whorls to mechanically suffocate and abrade larvae.", timingOrFrequency = "At knee-high stage (V4-V8)")
            ),
            biologicalActions = listOf(
                IPMActionItem("Bacillus thuringiensis kurstaki (Bt)", "Spray Bt formulation directly directed into leaf whorls in late afternoon.", dosageOrMethod = "2.0 g / L water"),
                IPMActionItem("Nomuraea rileyi / Metarhizium anisopliae", "Entomopathogenic fungus for larval suppression.", dosageOrMethod = "5 g / L water"),
                IPMActionItem("Trichogramma Egg Parasitoids", "Release egg parasitoid Trichogramma pretiosum / chilonis cards.", dosageOrMethod = "50,000 parasitized eggs / acre")
            ),
            chemicalActions = listOf(
                IPMActionItem("Chlorantraniliprole 18.5% SC (Targeted Diamide)", "Spray directed strictly inside leaf whorls using knapsack with hollow cone nozzle.", dosageOrMethod = "0.4 ml / L water", safetyWarning = "PHI: 14 days. Highly specific to lepidopterans, safe for beneficials."),
                IPMActionItem("Emamectin Benzoate 5% SG (Bio-rational)", "Fast-acting ingestion inhibitor derived from soil actinomycete Streptomyces.", dosageOrMethod = "0.4 g / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf(
                "Check 20 consecutive plants in 5 field locations (100 plants total) weekly",
                "Action threshold: 5% infested plants in seedling stage, 10% in mid-whorl stage"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_anthracnose_fruit_rot",
            diseaseName = "Anthracnose & Fruit Rot (Dieback)",
            scientificName = "Colletotrichum capsici / gloeosporioides",
            targetCrops = listOf("Chilli", "Tomato", "Pomegranate", "Mango", "Beans"),
            pathogenType = "Fungal",
            severityLevel = ThreatSeverity.HIGH,
            keySymptomsSummary = "Circular sunken water-soaked dark lesions on ripe fruit with concentric rings of salmon-pink/black gelatinous acervuli; twig die-back from tip downward.",
            culturalActions = listOf(
                IPMActionItem("Seed Treatment with Bio-Agent", "Treat seeds with Trichoderma viride @ 10g/kg seed or Thiram @ 2g/kg.", timingOrFrequency = "Prior to nursery sowing"),
                IPMActionItem("Harvest Promptly at Maturity", "Harvest ripe fruit promptly; avoid letting overripe pods hang in humid weather.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Pruning & Burning Die-Back Twigs", "Prune diseased twigs 2 cm below necrotic tissue into bucket; burn immediately.")
            ),
            biologicalActions = listOf(
                IPMActionItem("Pseudomonas fluorescens Foliar Spray", "Spray Pseudomonas @ 5g/L water at flowering and fruit set.", dosageOrMethod = "5 g / L water"),
                IPMActionItem("Garlic Bulb + Chilli Extract (Botanical)", "Spray 5% aqueous crude garlic extract with sulfur properties.", dosageOrMethod = "50 ml / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Copper Hydroxide 77% WP (Contact)", "Foliar spray before monsoon showers.", dosageOrMethod = "2.0 g / L water", safetyWarning = "PHI: 3 days"),
                IPMActionItem("Tebuconazole 25.9% EC or Difenoconazole", "Translaminar systemic triazole fungicide for active fruit infection.", dosageOrMethod = "1.0 ml / L water", safetyWarning = "PHI: 5 days")
            ),
            preHarvestIntervalDays = 5,
            preventionChecklist = listOf(
                "Avoid sprinkler overhead irrigation during fruit ripening stage",
                "Collect and bury fruit drop daily"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_brown_plant_hopper",
            diseaseName = "Brown Plant Hopper (BPH)",
            scientificName = "Nilaparvata lugens",
            targetCrops = listOf("Rice / Paddy"),
            pathogenType = "Sucking Insect Pest",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Circular patches of dried, burnt-looking tillers ('hopper burn') in paddy field; base of tillers teeming with small brown nymphs and adults; honeydew leading to sooty mold.",
            culturalActions = listOf(
                IPMActionItem("Alleyway Formation (Skipping Rows)", "Create 30 cm alleyways after every 2 meters of planting to allow sunlight and aeration at tiller base.", timingOrFrequency = "At transplanting"),
                IPMActionItem("Alternate Wetting and Drying (AWD)", "Drain standing water from paddy field for 3-4 days to expose hopper base and break nymph lifecycle.", timingOrFrequency = "Mid-tillering to flowering"),
                IPMActionItem("Moderate Nitrogen Fertilizer", "Avoid excessive nitrogen application; split urea into 3-4 top dressings.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Light Traps for Adult Attraction", "Set up light traps (200W incandescent or mercury vapor bulb) near field perimeter.", dosageOrMethod = "1 trap / 2 acres", timingOrFrequency = "Run from 7 PM to 10 PM")
            ),
            biologicalActions = listOf(
                IPMActionItem("Conservation of Mirid Bugs & Spiders", "Preserve natural Cyrtorhinus lividipennis mirid bugs and Lycosa wolf spiders by avoiding broad-spectrum synthetic pyrethroids.", timingOrFrequency = "All season"),
                IPMActionItem("Metarhizium anisopliae Bio-spray", "Spray Metarhizium directed at the base of tillers during cloudy/evening hours.", dosageOrMethod = "5 g / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Pymetrozine 50% WDG (Selective Chordotonal Disruptor)", "Specialized hopper feeding inhibitor; instantly stops sap ingestion while sparing spiders.", dosageOrMethod = "0.6 g / L water", safetyWarning = "Spray directed strictly at base of tillers. PHI: 14 days"),
                IPMActionItem("Trifiumezafurim 10% SC", "Modern low-dose neonicotinoid alternative for heavy hopper pressure.", dosageOrMethod = "0.5 ml / L water", safetyWarning = "PHI: 15 days")
            ),
            preHarvestIntervalDays = 14,
            preventionChecklist = listOf(
                "Scout base of 20 hills randomly; action threshold is 5-10 hoppers per hill",
                "Strictly avoid synthetic pyrethroids (e.g. cypermethrin) which cause severe BPH resurgence"
            )
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_pink_bollworm",
            diseaseName = "Pink Bollworm",
            scientificName = "Pectinophora gossypiella",
            targetCrops = listOf("Cotton"),
            pathogenType = "Lepidopteran Insect Pest",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Rosetted flowers ('fancy flowers'), entry holes in bolls, interior lint staining, damaged seeds, and larval feeding inside maturing cotton bolls.",
            culturalActions = listOf(
                IPMActionItem("Deep Summer Ploughing", "Expose diapausing pink bollworm larvae in soil to solar heat and predatory birds.", timingOrFrequency = "Post-harvest / pre-monsoon"),
                IPMActionItem("Early Maturing Bt Hybrids", "Sow recommended stacked-gene Bt cotton hybrids to escape peak late-season bollworm pressure.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Gossyplure Pheromone Traps", "Install sex pheromone traps for mating disruption and mass trapping.", dosageOrMethod = "5-8 traps / acre", timingOrFrequency = "At square formation")
            ),
            biologicalActions = listOf(
                IPMActionItem("Trichogramma brasiliensis Release", "Release egg parasitoid cards at 50,000/acre.", dosageOrMethod = "50,000 eggs / acre"),
                IPMActionItem("Habrobracon hebetor Larval Parasitoid", "Release larval ectoparasitoid wasps.")
            ),
            chemicalActions = listOf(
                IPMActionItem("Quinalphos 25% EC or Thiodicarb 75% WP", "Targeted spray when economic threshold (10% damaged bolls/flowers) is exceeded.", dosageOrMethod = "2.0 ml / L water", safetyWarning = "PHI: 14 days")
            ),
            preHarvestIntervalDays = 14,
            preventionChecklist = listOf("Monitor rosetted flowers weekly", "Destroy crop stubbles immediately after final picking")
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_american_bollworm",
            diseaseName = "American Bollworm",
            scientificName = "Helicoverpa armigera",
            targetCrops = listOf("Cotton", "Pigeonpea", "Chickpea", "Tomato"),
            pathogenType = "Polyphagous Lepidopteran Pest",
            severityLevel = ThreatSeverity.HIGH,
            keySymptomsSummary = "Large circular bored holes in squares and bolls, larvae feeding with half body inside, coarse dark green frass near entry point.",
            culturalActions = listOf(
                IPMActionItem("Trap Cropping with Marigold", "Plant 1 row of marigold for every 10 rows of cotton to attract ovipositing moths.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Bird Perches", "Erect T-shaped bird perches (10/acre) to encourage insectivorous birds.")
            ),
            biologicalActions = listOf(
                IPMActionItem("HaNPV (Nuclear Polyhedrosis Virus)", "Spray HaNPV during evening hours.", dosageOrMethod = "250 LE / acre")
            ),
            chemicalActions = listOf(
                IPMActionItem("Emamectin Benzoate 5% SG", "Fast-acting larval ingestion inhibitor.", dosageOrMethod = "0.4 g / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf("Check squares weekly for larval entry holes")
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_cotton_aphids",
            diseaseName = "Cotton Aphids",
            scientificName = "Aphis gossypii",
            targetCrops = listOf("Cotton", "Okra", "Brinjal"),
            pathogenType = "Sucking Insect Pest",
            severityLevel = ThreatSeverity.MODERATE,
            keySymptomsSummary = "Heavy leaf curling and cupping, sticky honeydew secretion on leaves and bolls, black sooty mold growth reducing photosynthesis.",
            culturalActions = listOf(
                IPMActionItem("Balanced Nitrogen", "Avoid excessive urea application which succulentizes foliage.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Yellow Sticky Traps", "Install yellow sticky cards.", dosageOrMethod = "15 traps / acre")
            ),
            biologicalActions = listOf(
                IPMActionItem("Ladybird Beetle Conservation", "Protect natural predators (*Coccinella septempunctata*). Spray Verticillium lecanii.", dosageOrMethod = "5 g / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Imidacloprid 17.8% SL", "Systemic sucking pest control.", dosageOrMethod = "0.3 ml / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf("Inspect undersides of young terminal leaves")
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_whitefly_cotton",
            diseaseName = "Cotton Whitefly & CLCuV Vector",
            scientificName = "Bemisia tabaci",
            targetCrops = listOf("Cotton", "Chilli", "Tomato"),
            pathogenType = "Sucking Pest / Viral Vector",
            severityLevel = ThreatSeverity.CRITICAL,
            keySymptomsSummary = "Interveinal chlorosis, heavy honeydew, transmission of Cotton Leaf Curl Virus (CLCuV) causing upward leaf enations and stunting.",
            culturalActions = listOf(
                IPMActionItem("Weed Host Removal", "Clear weed hosts (*Abutilon*, *Parthenium*) around field borders.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Greased Yellow Sticky Cards", "High density yellow traps coated with castor oil.", dosageOrMethod = "20 traps / acre")
            ),
            biologicalActions = listOf(
                IPMActionItem("Beauveria bassiana Spray", "Entomopathogenic fungal spray at dusk.", dosageOrMethod = "5 g / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Pyriproxyfen 10% EC or Diafenthiuron 50% WP", "Targeted IGR or rescue spray.", dosageOrMethod = "1.5 ml / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf("Monitor trap counts twice weekly")
        ),
        IPMProtocolRecommendation(
            diseaseId = "rec_thrips_cotton",
            diseaseName = "Cotton Thrips",
            scientificName = "Thrips tabaci",
            targetCrops = listOf("Cotton", "Onion", "Chilli"),
            pathogenType = "Thysanoptera Sucking Pest",
            severityLevel = ThreatSeverity.MODERATE,
            keySymptomsSummary = "Silvery transparent patches on leaf undersides, leaves curling upward, bronze discoloration and stunted terminal tips.",
            culturalActions = listOf(
                IPMActionItem("Overhead Sprinkler Mist", "Brief misting during dry hot afternoons to suppress nymphs.")
            ),
            mechanicalActions = listOf(
                IPMActionItem("Blue Sticky Traps", "Install blue sticky traps.", dosageOrMethod = "12 traps / acre")
            ),
            biologicalActions = listOf(
                IPMActionItem("Neem Oil 10,000 ppm", "Botanical anti-feedant spray.", dosageOrMethod = "3 ml / L water")
            ),
            chemicalActions = listOf(
                IPMActionItem("Fipronil 5% SC or Spinosad 45% SC", "Targeted thrips insecticide.", dosageOrMethod = "1.5 ml / L water", safetyWarning = "PHI: 7 days")
            ),
            preHarvestIntervalDays = 7,
            preventionChecklist = listOf("Check top terminals for silvery rasping marks")
        )
    )

    // Decision Tree Nodes
    val decisionTreeNodes: Map<String, DecisionTreeNode> = mapOf(
        "root" to DecisionTreeNode(
            nodeId = "root",
            question = "Which crop group are you scouting?",
            explanation = "Select the affected crop category to navigate specific regional pathology and IPM protocols.",
            iconName = "Yard",
            options = listOf(
                DecisionTreeOption("opt_cotton", "☁️ Cotton (Vidarbha, Khandesh, Marathwada)", "Pink Bollworm, American Bollworm, Whitefly, Aphids, Thrips", nextNodeId = "node_cotton_symptoms"),
                DecisionTreeOption("opt_solanaceous", "🍅 Solanaceous Crops (Tomato, Potato, Eggplant)", "Blights, Wilts, Viruses, Whiteflies", nextNodeId = "node_solanaceous_part"),
                DecisionTreeOption("opt_chilli", "🌶️ Chilli & Capsicum", "Leaf curl, Thrips, Mites, Anthracnose fruit rot", nextNodeId = "node_chilli_part"),
                DecisionTreeOption("opt_cucurbits", "🥒 Cucurbits & Gourds (Cucumber, Melons, Squash)", "Powdery mildew, Downy mildew, Fruit flies", nextNodeId = "node_cucurbit_symptoms"),
                DecisionTreeOption("opt_cereals", "🌾 Cereals & Grasses (Rice, Maize, Sorghum)", "Armyworm, Brown plant hopper, Blast, Blight", nextNodeId = "node_cereal_crop")
            )
        ),

        // Cotton Branch
        "node_cotton_symptoms" to DecisionTreeNode(
            nodeId = "node_cotton_symptoms",
            question = "What is the primary pest or symptom observed in the cotton field?",
            explanation = "Maharashtra cotton zones face critical pressure from bollworms, sucking pests, and virus vectors.",
            iconName = "Agriculture",
            options = listOf(
                DecisionTreeOption("opt_cot_bollworm", "🐛 Rosetted flowers ('fancy flowers') or bored bolls with lint staining", "Pink Bollworm (Pectinophora gossypiella)", finalRecommendationId = "rec_pink_bollworm"),
                DecisionTreeOption("opt_cot_american", "🕳️ Large bored holes in squares & bolls with dark green frass", "American Bollworm (Helicoverpa armigera)", finalRecommendationId = "rec_american_bollworm"),
                DecisionTreeOption("opt_cot_whitefly", "☁️ Interveinal chlorosis, sticky honeydew & upward leaf enations", "Whitefly & CLCuV Vector (Bemisia tabaci)", finalRecommendationId = "rec_whitefly_cotton"),
                DecisionTreeOption("opt_cot_aphids_thrips", "🍃 Leaf curling, sticky honeydew or silvery leaf undersides", "Cotton Aphids & Thrips Complex", finalRecommendationId = "rec_cotton_aphids")
            )
        ),

        // Solanaceous Branch
        "node_solanaceous_part" to DecisionTreeNode(
            nodeId = "node_solanaceous_part",
            question = "Where are the primary symptoms visible on the tomato/potato plant?",
            explanation = "Locating the symptom zone helps differentiate between foliar blights, vascular wilts, and insect-vectored viruses.",
            iconName = "FilterVintage",
            options = listOf(
                DecisionTreeOption("opt_foliar_spots", "🍃 Foliar Spots & Lesions", "Dark spots, target rings, or water-soaked patches on leaves", nextNodeId = "node_solanaceous_leaf_spots"),
                DecisionTreeOption("opt_leaf_curl_stunt", "🔄 Leaf Curling, Stunting & Yellowing", "Leaves cupping upwards, bushy stunted habit, tiny insects", nextNodeId = "node_whitefly_check"),
                DecisionTreeOption("opt_wilt_collapse", "🥀 Rapid Wilting & Stem Collapse", "Plant droops suddenly during daytime while leaves stay green", nextNodeId = "node_wilt_test")
            )
        ),
        "node_solanaceous_leaf_spots" to DecisionTreeNode(
            nodeId = "node_solanaceous_leaf_spots",
            question = "How do the leaf spots look and in what weather did they appear?",
            explanation = "Distinguishing concentric rings vs water-soaked spreading lesions is critical for choosing the correct fungicide / bio-control.",
            iconName = "BugReport",
            options = listOf(
                DecisionTreeOption(
                    "opt_late_blight",
                    "🌧️ Water-soaked dark brown blotches with white underside bloom",
                    "Spreads very rapidly during cool, humid, or rainy weather (>80% RH)",
                    finalRecommendationId = "rec_late_blight"
                ),
                DecisionTreeOption(
                    "opt_early_blight",
                    "🎯 Dark brown spots with concentric 'target-board' rings & yellow halos",
                    "Appears initially on older bottom leaves; dry or alternating dew weather",
                    finalRecommendationId = "rec_early_blight"
                ),
                DecisionTreeOption(
                    "opt_powdery_sol",
                    "❄️ White powdery talc-like coating on upper leaf surface",
                    "Leaves turn yellow and dry up; common in shaded or dry-warm weather",
                    finalRecommendationId = "rec_powdery_mildew"
                )
            )
        ),
        "node_whitefly_check" to DecisionTreeNode(
            nodeId = "node_whitefly_check",
            question = "Are there tiny white fluttering insects under the leaves when disturbed?",
            explanation = "Whiteflies transmit begomoviruses like TYLCV. Management requires vector-suppressing IPM rather than standard fungicides.",
            iconName = "PestControl",
            options = listOf(
                DecisionTreeOption(
                    "opt_whitefly_yes",
                    "Yes, tiny white flies flutter up, leaves curling upwards",
                    "Severe cupping, yellow leaf margins, and stunted growth",
                    finalRecommendationId = "rec_whitefly_tylcv"
                ),
                DecisionTreeOption(
                    "opt_thrips_sol",
                    "No whiteflies, but leaf edges curl and show silvery or bronzed streaks",
                    "Thrips / mite rasping and feeding damage",
                    finalRecommendationId = "rec_chilli_thrips_mites"
                )
            )
        ),
        "node_wilt_test" to DecisionTreeNode(
            nodeId = "node_wilt_test",
            question = "If you cut the lower stem and place it in clear water, do white milky bacterial threads stream out?",
            explanation = "The clear water glass test separates Ralstonia bacterial wilt from fungal Fusarium vascular wilt.",
            iconName = "Science",
            options = listOf(
                DecisionTreeOption(
                    "opt_bacterial_wilt_yes",
                    "🧪 Yes! Milky white bacterial streaming observed within 1–2 minutes",
                    "Rapid daytime wilting with green foliage and dark vascular ring",
                    finalRecommendationId = "rec_bacterial_wilt"
                ),
                DecisionTreeOption(
                    "opt_early_blight_stem",
                    "No streaming; stem has dark sunken collar lesions near soil line",
                    "Stem canker / Alternaria collar rot or early blight complex",
                    finalRecommendationId = "rec_early_blight"
                )
            )
        ),

        // Chilli Branch
        "node_chilli_part" to DecisionTreeNode(
            nodeId = "node_chilli_part",
            question = "What is the dominant symptom on the chilli crop?",
            explanation = "Chilli yields are commonly threatened by the leaf curl virus/thrip complex, anthracnose fruit rot, and powdery mildew.",
            iconName = "Psychology",
            options = listOf(
                DecisionTreeOption("opt_chilli_leaf_curl", "🔄 Leaf Curling & Crinkling ('Murda')", "Upward or downward boat-shaped curl, stunted bushy tips", nextNodeId = "node_chilli_curl_direction"),
                DecisionTreeOption("opt_chilli_fruit_lesions", "🔴 Circular Sunken Lesions on Ripe Chilli Fruits", "Salmon-pink to black concentric spots, tip die-back", nextNodeId = "node_chilli_fruit_confirm"),
                DecisionTreeOption("opt_chilli_powdery", "❄️ White Powdery Patches on Leaf Undersides", "Foliage curling upwards with yellowish patches on upper side", nextNodeId = "node_chilli_powdery_confirm"),
                DecisionTreeOption("opt_chilli_wilt", "🥀 Whole Plant Sudden Wilting", "Foliage droops rapidly in warm soil", nextNodeId = "node_wilt_test")
            )
        ),
        "node_chilli_curl_direction" to DecisionTreeNode(
            nodeId = "node_chilli_curl_direction",
            question = "Which direction are the leaves curling?",
            explanation = "Curling direction differentiates between Thrips (upward boat shape) and Yellow Mites (downward inverted cup shape).",
            iconName = "Tune",
            options = listOf(
                DecisionTreeOption(
                    "opt_curl_up_down_mix",
                    "⛵ Upward curl (boat-shape) or downward curl with bronzing underneath",
                    "Chilli thrips (Scirtothrips dorsalis) & yellow mite complex",
                    finalRecommendationId = "rec_chilli_thrips_mites"
                ),
                DecisionTreeOption(
                    "opt_whitefly_chilli",
                    "Curling accompanied by yellow mottling & tiny white flies underneath",
                    "Whitefly transmitted Chilli Leaf Curl Virus",
                    finalRecommendationId = "rec_whitefly_tylcv"
                )
            )
        ),
        "node_chilli_fruit_confirm" to DecisionTreeNode(
            nodeId = "node_chilli_fruit_confirm",
            question = "Are the fruit spots sunken with black concentric dots and twig tip die-back?",
            explanation = "This is the classic manifestation of Anthracnose / Fruit Rot (Colletotrichum capsici).",
            iconName = "Verified",
            options = listOf(
                DecisionTreeOption(
                    "opt_anthracnose_yes",
                    "Yes, sunken circular fruit lesions with concentric rings & dieback",
                    "Anthracnose fruit rot (Colletotrichum capsici)",
                    finalRecommendationId = "rec_anthracnose_fruit_rot"
                )
            )
        ),
        "node_chilli_powdery_confirm" to DecisionTreeNode(
            nodeId = "node_chilli_powdery_confirm",
            question = "Is there white floury fungal growth on the leaves causing premature drop?",
            explanation = "Powdery mildew in chillies (Leveillula taurica) typically starts on the underside of foliage.",
            iconName = "Flare",
            options = listOf(
                DecisionTreeOption(
                    "opt_chilli_powdery_yes",
                    "Yes, white powdery growth with yellow chlorotic upper blotches",
                    "Powdery Mildew (Leveillula taurica)",
                    finalRecommendationId = "rec_powdery_mildew"
                )
            )
        ),

        // Cucurbit Branch
        "node_cucurbit_symptoms" to DecisionTreeNode(
            nodeId = "node_cucurbit_symptoms",
            question = "What type of foliar discoloration is visible on the cucumber/gourd leaves?",
            explanation = "Angular vein-restricted spots indicate downy mildew; talcum-like powdery spots indicate powdery mildew.",
            iconName = "Eco",
            options = listOf(
                DecisionTreeOption(
                    "opt_downy_cucurbit",
                    "📐 Angular yellow patches restricted by leaf veins + purplish-gray down underneath",
                    "Rapid foliar necrosis in damp, humid morning dew conditions",
                    finalRecommendationId = "rec_downy_mildew"
                ),
                DecisionTreeOption(
                    "opt_powdery_cucurbit",
                    "⚪ White powdery talc-like patches covering upper leaf surfaces and stems",
                    "Leaves turn brown and crispy; thrives in dry warm air with shade",
                    finalRecommendationId = "rec_powdery_mildew"
                ),
                DecisionTreeOption(
                    "opt_anthracnose_cucurbit",
                    "🔴 Circular water-soaked lesions on fruits and stems with salmon-colored spore ooze",
                    "Fruit rot and anthracnose on cucurbit vines",
                    finalRecommendationId = "rec_anthracnose_fruit_rot"
                )
            )
        ),

        // Cereals Branch
        "node_cereal_crop" to DecisionTreeNode(
            nodeId = "node_cereal_crop",
            question = "Which cereal crop are you managing?",
            explanation = "Paddy/rice and maize have distinct key epidemic threats requiring targeted IPM protocols.",
            iconName = "Agriculture",
            options = listOf(
                DecisionTreeOption("opt_rice_paddy", "🌾 Rice / Paddy", "Hopper burn, leaf blast, bacterial blight, stem borers", nextNodeId = "node_rice_symptoms"),
                DecisionTreeOption("opt_maize_corn", "🌽 Maize / Corn / Sorghum", "Whorl defoliation, sawdust frass, leaf blight", nextNodeId = "node_maize_symptoms")
            )
        ),
        "node_rice_symptoms" to DecisionTreeNode(
            nodeId = "node_rice_symptoms",
            question = "What is the primary damage observed in the paddy field?",
            explanation = "Circular drying patches at tiller base indicate BPH; elongated spindle-shaped lesions indicate blast.",
            iconName = "Grass",
            options = listOf(
                DecisionTreeOption(
                    "opt_bph_hopper_burn",
                    "🔥 Circular patches of dried, burnt tillers ('hopper burn') with tiny brown insects at water line",
                    "Brown Plant Hopper (BPH - Nilaparvata lugens)",
                    finalRecommendationId = "rec_brown_plant_hopper"
                ),
                DecisionTreeOption(
                    "opt_rice_blight",
                    "🍂 Water-soaked leaf edges turning wavy yellow-white with bacterial ooze",
                    "Bacterial Leaf Blight (Xanthomonas oryzae)",
                    finalRecommendationId = "rec_late_blight"
                )
            )
        ),
        "node_maize_symptoms" to DecisionTreeNode(
            nodeId = "node_maize_symptoms",
            question = "Are there ragged holes in the whorl leaves with heavy sawdust-like frass inside?",
            explanation = "This is the signature feeding damage of the invasive Fall Armyworm (Spodoptera frugiperda).",
            iconName = "CrisisAlert",
            options = listOf(
                DecisionTreeOption(
                    "opt_faw_maize",
                    "🐛 Yes, ragged window-pane whorl feeding with sawtooth frass and striped caterpillar",
                    "Fall Armyworm (Spodoptera frugiperda)",
                    finalRecommendationId = "rec_fall_armyworm"
                )
            )
        )
    )

    fun getRecommendationById(id: String): IPMProtocolRecommendation? {
        return allRecommendations.find { it.diseaseId == id }
    }

    fun searchRecommendations(query: String): List<IPMProtocolRecommendation> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allRecommendations
        return allRecommendations.filter { rec ->
            rec.diseaseName.lowercase().contains(q) ||
            rec.scientificName.lowercase().contains(q) ||
            rec.targetCrops.any { it.lowercase().contains(q) } ||
            rec.pathogenType.lowercase().contains(q) ||
            rec.keySymptomsSummary.lowercase().contains(q)
        }
    }
}
