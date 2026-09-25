# TFLite
-keep class org.tensorflow.** { *; }
-keep class org.tensorflow.lite.** { *; }
# Retrofit + OkHttp
-keepattributes Signature,Exceptions,*Annotation*
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
# Gson + data models
-keep class com.google.gson.** { *; }
-keep class com.agrishield.app.data.model.** { *; }
# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
# WorkManager
-keep class * extends androidx.work.CoroutineWorker { *; }
# Location
-keep class com.google.android.gms.location.** { *; }
