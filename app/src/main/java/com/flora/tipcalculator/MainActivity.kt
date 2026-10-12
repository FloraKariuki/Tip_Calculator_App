package com.flora.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flora.tipcalculator.ui.theme.TipCalculatorTheme
import kotlin.math.roundToInt

object AppRoutes {
    const val HOME = "home"
    const val LIST = "list"
    const val ABOUT = "about"
}

data class AppFeatureItem(val id: Int, val title: String, val description: String, val tag: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()

    val items = listOf(
        Triple("Home", AppRoutes.HOME, Icons.Default.Home),
        Triple("List", AppRoutes.LIST, Icons.Default.List),
        Triple("About", AppRoutes.ABOUT, Icons.Default.Info)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E1B4B)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.third, contentDescription = item.first) },
                        label = { Text(item.first) },
                        selected = currentRoute == item.second,
                        onClick = {
                            if (currentRoute != item.second) {
                                navController.navigate(item.second) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFEC4899),
                            selectedTextColor = Color(0xFFEC4899),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.HOME) { HomeScreen() }
            composable(AppRoutes.LIST) { ListScreen() }
            composable(AppRoutes.ABOUT) { AboutScreen() }
        }
    }
}

// --- SCREEN 1: HOME (Tip Calculator) ---
@Composable
fun HomeScreen() {
    var amountInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(15f) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercentageValue = tipPercent.roundToInt()
    val tip = calculateTip(amount, tipPercentageValue.toDouble())
    val total = amount + tip

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Restaurant Tip Calculator",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B4B),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    singleLine = true,
                    label = { Text("Bill Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tip Percentage", fontSize = 14.sp, color = Color.DarkGray)
                    Text(text = "$tipPercentageValue%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1))
                }

                Slider(
                    value = tipPercent,
                    onValueChange = { tipPercent = it },
                    valueRange = 0f..50f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = Color(0xFF6366F1), activeTrackColor = Color(0xFF6366F1)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3F4F6))
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Tip Amount: $%.2f".format(tip), fontSize = 16.sp, color = Color(0xFF4B5563))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Total: $%.2f".format(total), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    }
                }
            }
        }
    }
}

// --- SCREEN 2: LIST  ---
@Composable
fun ListScreen() {
    val features = listOf(
        AppFeatureItem(1, "Lightning-Fast Calculations", "Instantly compute tips right at the table without awkward mental math.", "Speed"),
        AppFeatureItem(2, "Custom 0% - 50% Slider", "Tailor your tip precisely based on exceptional service quality.", "Flexibility"),
        AppFeatureItem(3, "Rotation State Memory", "Never lose entered values when handing the phone around to friends or rotating screens.", "Reliability"),
        AppFeatureItem(4, "Vibrant Restaurant UI", "Designed with a warm, inviting gradient aesthetic that brightens up dining out.", "Design")
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFEEF2FF), Color(0xFFF8FAFC), Color(0xFFFDF2F8))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "App Highlights & Perks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B4B),
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(features, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1B4B),
                                    fontSize = 16.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFEEF2FF)
                                ) {
                                    Text(
                                        text = item.tag,
                                        color = Color(0xFF6366F1),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.description,
                                color = Color(0xFF4B5563),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 3: ABOUT ---
@Composable
fun AboutScreen() {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFCE7F3),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "🍽️ Dining Companion",
                        color = Color(0xFFDB2777),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "About This App",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B4B),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "This application was custom-built to make tipping much easier, smoother, and more accurate for restaurant diners and servers alike. No more complex mental calculations at the end of a great meal—just open, slide, and settle up with confidence!",
                    fontSize = 15.sp,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Framework", color = Color.Gray, fontSize = 13.sp)
                    Text(text = "Jetpack Compose", fontWeight = FontWeight.Medium, color = Color(0xFF1E1B4B), fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Developer", color = Color.Gray, fontSize = 13.sp)
                    Text(text = "Kariuki Flora", fontWeight = FontWeight.Bold, color = Color(0xFF6366F1), fontSize = 13.sp)
                }
            }
        }
    }
}

internal fun calculateTip(amount: Double, tipPercent: Double): Double {
    return if (amount <= 0.0 || tipPercent <= 0.0) {
        0.0
    } else {
        (tipPercent / 100.0) * amount
    }
}