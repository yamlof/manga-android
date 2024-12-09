package com.example.greetingcard

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.greetingcard.database.AppDatabase
import com.example.greetingcard.database.MangaRepository
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.database.MangaViewModelFactory
import com.example.greetingcard.ui.theme.GreetingCardTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var mangaViewModel: MangaViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreetingCardTheme {

                val mangaDao = AppDatabase.getDatabase(application).mangaDao()
                val mangaRepository = MangaRepository(mangaDao)
                val mangaViewModelFactory = MangaViewModelFactory(mangaRepository)
                mangaViewModel = ViewModelProvider(this, mangaViewModelFactory).get(MangaViewModel::class.java)



                MainScreen(mangaViewModel = mangaViewModel)

            }
        }
    }
}

