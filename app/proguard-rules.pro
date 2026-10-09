# ProGuard / R8 rules for Splitz
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep Room database, DAOs, and entities
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# Keep domain models and database entities
-keep class com.paisede.app.domain.model.** { *; }
-keep class com.paisede.app.data.db.entities.** { *; }

# Keep coroutines reflection internals if needed
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
