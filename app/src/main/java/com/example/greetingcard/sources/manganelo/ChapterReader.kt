package com.example.greetingcard.sources.manganelo


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.greetingcard.models.ImageManga
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import com.example.greetingcard.requests.RetrofitClient
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.style.TextAlign
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.net.URLDecoder
import java.util.concurrent.TimeUnit


class RequestHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("User-Agent", "Mozilla/5.0")
            .addHeader("Referer", "https://chapmanganelo.com/")
            .build()
        return chain.proceed(newRequest)
    }
}

enum class ReadingMode {
    HORIZONTAL_PAGER,
    VERTICAL_SCROLL,
    WEBTOON
}

data class ReaderSettings(
    val readingMode: ReadingMode = ReadingMode.HORIZONTAL_PAGER,
    val backgroundColor: Color = Color.Black,
    val showPageNumbers: Boolean = true,
    val autoHideUI: Boolean = true,
    val zoomEnabled: Boolean = true,
    val doubleTapToZoom: Boolean = true
)


@Composable
@OptIn(ExperimentalCoilApi::class, ExperimentalGlideComposeApi::class)
fun DisplayImage(
    modifier: Modifier = Modifier,
    imageManga: ImageManga,
    settings: ReaderSettings,
    onImageClick: () -> Unit = {},
    onImageDoubleTap: () -> Unit = {}
) {
    val context = LocalContext.current
    val zoomState = rememberZoomState()

    val userAgents = remember {
        listOf(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:121.0) Gecko/20100101 Firefox/121.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
    }

    val okHttpClient = remember {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val randomUserAgent = userAgents.random()
                val request = chain.request().newBuilder()
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.mangabats.com/")
                    .removeHeader("Connection") // Let OkHttp handle this
                    .build()

                // Add small delay to avoid rapid requests
                Thread.sleep(100)
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    // Create GlideUrl with custom headers
    val glideUrl = remember(imageManga.imgLink) {
        GlideUrl(
            imageManga.imgLink,
            LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
                .addHeader("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
                .addHeader("Accept-Language", "en-GB,en;q=0.5")
                .addHeader("Connection", "keep-alive")
                .addHeader("Referer", "https://chapmanganelo.com/")
                .addHeader("Sec-Fetch-Dest", "image")
                .addHeader("Sec-Fetch-Mode", "no-cors")
                .addHeader("Sec-Fetch-Site", "cross-site")
                .addHeader("Priority", "u=5, i")
                .addHeader("Pragma", "no-cache")
                .addHeader("Cache-Control", "no-cache")
                .build()
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(settings.backgroundColor)
            .then(
                if (settings.zoomEnabled) {
                    Modifier.zoomable(zoomState)
                } else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onImageClick() },
                    onDoubleTap = {
                        if (settings.doubleTapToZoom) {
                            onImageDoubleTap()
                        }
                    }
                )
            }
    ) {
        Log.d("ImageViewer", "Loading image: ${imageManga.imgLink}")

        GlideImage(
            model = glideUrl,
            contentDescription = imageManga.imgTitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentScale = ContentScale.FillBounds,
            /*loading = {
                // Optional loading placeholder
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            failure = {
                // Optional error placeholder
                Log.e("ImageViewer", "Image failed to load: ${imageManga.imgLink}")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to load image")
                }
            }*/
        ) { requestBuilder ->
            requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(30000) // 30 second timeout
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedReaderTopBar(
    visible: Boolean,
    currentPage: Int,
    totalPages: Int,
    chapterTitle: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            with(density) { -60.dp.roundToPx() }
        } + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically {
            with(density) { -60.dp.roundToPx() }
        } + fadeOut(animationSpec = tween(300))
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.8f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = chapterTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(
                        text = "Page $currentPage of $totalPages",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBottomBar(
    visible: Boolean,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            with(density) { 60.dp.roundToPx() }
        } + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically {
            with(density) { 60.dp.roundToPx() }
        } + fadeOut(animationSpec = tween(300))
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.8f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Page slider
                Slider(
                    value = currentPage.toFloat(),
                    onValueChange = { onPageChange(it.toInt()) },
                    valueRange = 1f..totalPages.toFloat(),
                    steps = totalPages - 2,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.Gray
                    )
                )

                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onPreviousPage,
                        enabled = currentPage > 1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous"
                        )
                        Text("Previous")
                    }

                    Text(
                        text = "$currentPage / $totalPages",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = onNextPage,
                        enabled = currentPage < totalPages,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    ) {
                        Text("Next")
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnhancedImageViewer(
    modifier: Modifier = Modifier,
    imgManga: List<ImageManga>,
    settings: ReaderSettings,
    onPageChange: (Int) -> Unit = {}

) {
    when (settings.readingMode) {
        ReadingMode.HORIZONTAL_PAGER -> {
            val pagerState = rememberPagerState(pageCount = { imgManga.size })

            LaunchedEffect(pagerState.currentPage) {
                onPageChange(pagerState.currentPage + 1)
            }

            HorizontalPager(
                state = pagerState,
                modifier = modifier.fillMaxSize()
            ) { page ->
                DisplayImage(
                    imageManga = imgManga[page],
                    settings = settings,
                    onImageClick = { /* Handle click */ },
                    onImageDoubleTap = { /* Handle double tap */ }
                )
            }
        }

        ReadingMode.VERTICAL_SCROLL -> {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize()
            ) {
                items(imgManga.size) { index ->
                    DisplayImage(
                        imageManga = imgManga[index],
                        settings = settings,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (index < imgManga.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        ReadingMode.WEBTOON -> {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(imgManga.size) { index ->
                    DisplayImage(
                        imageManga = imgManga[index],
                        settings = settings.copy(zoomEnabled = false),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ChapterReader(
    modifier: Modifier = Modifier,
    chapterTitle: String = "Chapter",
    chapterUrl : String,
    navController: NavController
){


    //val decodedChapterUrl = Uri.decode(chapterUrl)

    val fetchedItem  = remember { mutableStateOf<List<ImageManga>>(emptyList()) }

    //val isLoading = remember { mutableStateOf(true) }

    var isUIVisible = remember { mutableStateOf(false) }
    var currentPage = remember { mutableIntStateOf(1) }
    var settings = remember { mutableStateOf(ReaderSettings()) }
    var showSettings = remember { mutableStateOf(false) }

    val decodedChapterUrl = URLDecoder.decode(chapterUrl, "UTF-8")
    var fetchedImages = remember { mutableStateOf<List<ImageManga>>(emptyList()) }
    var isLoading = remember { mutableStateOf(true) }
    var errorMessage = remember { mutableStateOf<String?>(null) }





    LaunchedEffect(Unit) {
        try {
            val fetchedItems = RetrofitClient.apiService.getChapterInfo(decodedChapterUrl)
            fetchedItem.value = fetchedItems
            fetchedImages.value = fetchedItems // 🔥 this was missing
            isLoading.value = false
            Log.d("MangaNelo", "Fetched items: $fetchedItems")
        } catch (e: Exception) {
            Log.e("MangaNelo", "error fetching chapter info", e)
            errorMessage.value = "Failed to load chapter: ${e.message}"
            isLoading.value = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading chapter...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            errorMessage.value != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage.value ?: "Failed to load chapter",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.popBackStack() }
                            ) {
                                Text("Go Back")
                            }
                        }
                    }
                }
            }

            fetchedItem.value.isNotEmpty() -> {
                // Main content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isUIVisible.value = !isUIVisible.value }
                    ) {
                    EnhancedImageViewer(
                        imgManga = fetchedImages.value,
                        settings = settings.value,
                        onPageChange = { page -> currentPage.intValue = page }
                    )

                    // Top bar
                    AnimatedReaderTopBar(
                        visible = isUIVisible.value,
                        currentPage = currentPage.intValue,
                        totalPages = fetchedImages.value.size,
                        chapterTitle = chapterTitle,
                        onBackClick = { navController.popBackStack() },
                        onSettingsClick = { showSettings.value = true }
                    )

                    // Bottom bar
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        ReaderBottomBar(
                            visible = isUIVisible.value,
                            currentPage = currentPage.intValue,
                            totalPages = fetchedImages.value.size,
                            onPageChange = { /* Handle page change */ },
                            onPreviousPage = { /* Handle previous */ },
                            onNextPage = { /* Handle next */ }
                        )
                    }
                }
            }
        }
    }
}