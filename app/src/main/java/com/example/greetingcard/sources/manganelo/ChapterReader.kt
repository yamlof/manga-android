package com.example.greetingcard.sources.manganelo

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.greetingcard.models.ImageManga
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import coil3.Bitmap
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.greetingcard.requests.RetrofitClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

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
    val zoomState = rememberZoomState()
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var isLoading = remember { mutableStateOf(true) }
    var hasError = remember { mutableStateOf(false) }

    val userAgents = remember {
        listOf(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:121.0) Gecko/20100101 Firefox/121.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
    }

    val httpClient = remember {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 60000
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3, retryOnTimeout = true)
                exponentialDelay()
            }

            engine {
                https {
                    trustManager = object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                }
            }

            defaultRequest {
                header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
                header("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
                header("Accept-Language", "en-GB,en;q=0.5")
                header("Connection", "keep-alive")
                header("Referer", "https://www.mangabats.com/")
                header("Sec-Fetch-Dest", "image")
                header("Sec-Fetch-Mode", "no-cors")
                header("Sec-Fetch-Site", "cross-site")
                header("Priority", "u=5, i")
                header("Pragma", "no-cache")
                header("Cache-Control", "no-cache")
            }
        }
    }

    LaunchedEffect(imageManga.imgLink) {
        isLoading.value = true
        hasError.value = false
        bitmap.value = null

        try {
            delay(100)

            val response: HttpResponse = httpClient.get(imageManga.imgLink)

            if (response.status.isSuccess()) {
                val imageBytes = response.body<ByteArray>()
                val loadedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                if (loadedBitmap != null) {
                    bitmap.value = loadedBitmap
                    isLoading.value = false
                } else {
                    hasError.value = true
                    isLoading.value = false
                    Log.e("ImageViewer", "Failed to decode bitmap from: ${imageManga.imgLink}")
                }
            } else {
                hasError.value = true
                isLoading.value = false
                Log.e("ImageViewer", "HTTP error ${response.status.value} for: ${imageManga.imgLink}")
            }
        } catch (e: Exception) {
            hasError.value = true
            isLoading.value = false
            Log.e("ImageViewer", "Error loading image: ${imageManga.imgLink}", e)
        }
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

        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            hasError.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Failed to load image",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                isLoading.value = true
                                hasError.value = false
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            bitmap != null -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        bitmap = bitmap.value?.asImageBitmap() ?: ImageBitmap(1, 1) ,
                        contentDescription = imageManga.imgTitle,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
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
    onPageChange: (Int) -> Unit = {},
    onImageClick: () -> Unit = {},
    onImageDoubleTap: () -> Unit = {}

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
                    onImageClick = onImageClick,
                    onImageDoubleTap = onImageDoubleTap
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
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChapterReader(
    modifier: Modifier = Modifier,
    chapterTitle: String = "title",
    chapterUrl : String,
    navController: NavController
){

    val fetchedItem  = remember { mutableStateOf<List<ImageManga>>(emptyList()) }
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
            fetchedImages.value = fetchedItems
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
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EnhancedImageViewer(
                        imgManga = fetchedImages.value,
                        settings = settings.value,
                        onPageChange = { page -> currentPage.intValue = page },
                        onImageClick = { isUIVisible.value = !isUIVisible.value },
                        onImageDoubleTap = {
                            // Handle double tap to zoom functionality
                            // This would typically trigger zoom in the DisplayImage component
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isUIVisible.value) {
                        TopAppBar(
                            title = {
                                Text(text = "Back")
                            },
                            navigationIcon = {
                                IconButton(onClick = {navController.popBackStack()}) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        BottomAppBar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .zIndex(1f)
                        ) {
                            Text("Page ${currentPage.intValue}", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}