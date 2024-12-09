package com.example.greetingcard

import android.app.Notification
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.pages.HomePage
import com.example.greetingcard.pages.NotificationPage
import com.example.greetingcard.pages.SettingsPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier,mangaViewModel: MangaViewModel) {

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home,0),
        NavItem("Notification", Icons.Default.Notifications,5),
        NavItem("Settings", Icons.Default.Settings,0),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed {index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                                BadgedBox(badge =  {
                                    if(navItem.badgeCount>0)
                                        Badge(){
                                            Text(text = navItem.badgeCount.toString() )
                                        }
                                    }) {
                                    Icon(imageVector = navItem.icon, contentDescription = "Icon")
                                }
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding),selectedIndex, mangaViewModel = mangaViewModel)

    }
    
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentScreen(modifier: Modifier = Modifier,selectedIndex:Int,mangaViewModel: MangaViewModel) {
    when(selectedIndex){

        0 -> HomePage(mangaViewModel = mangaViewModel)
        1 -> NotificationPage()
        2 -> SettingsPage()
    }
}