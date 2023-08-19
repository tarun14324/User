package com.example.userapp

import android.app.Application
import com.example.userapp.viewmodel.AuthenticationViewModel
import com.example.userapp.viewmodel.MainViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    private val realmVersion = 1L

    override fun onCreate() {
        super.onCreate()

        // Start Koin and load modules
        startKoin {
            modules(appModule)
        }

        // Initialize Realm
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .name("user-data")
            .schemaVersion(realmVersion)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    private val appModule = module {
        viewModel { AuthenticationViewModel() }
        viewModel { MainViewModel() }
    }
}
