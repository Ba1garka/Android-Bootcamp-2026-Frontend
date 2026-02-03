package ru.sicampus.bootcamp2026.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.BlackIcon
import ru.sicampus.bootcamp2026.ui.theme.Grey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.screen.CalendarScreen
import ru.sicampus.bootcamp2026.ui.screen.home.HomeScreen
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("calendar") {
                CalendarScreen()
            }
//            composable("list") {
//                ListScreen()
//            }
//            composable("add") {
//                AddScreen()
//            }
//            composable("profile") {
//                ProfileScreen()
//            }
        }

        BottomNavBar(navController = navController,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter))
    }
}

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier.padding(bottom = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.width(320.dp).height(56.dp),
            shape = RoundedCornerShape(30.dp),
            color = Grey.copy(alpha = 0.90f),
            tonalElevation = 12.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                NavIcon(
                    drawableId = R.drawable.home,
                    isSelected = currentRoute == "home",
                    onClick = { navController.navigate("home") }
                )
                NavIcon(
                    drawableId = R.drawable.calendar,
                    isSelected = currentRoute == "calendar",
                    onClick = { navController.navigate("calendar") }
                )

                NavIcon(
                    drawableId = R.drawable.list,
                    isSelected = currentRoute == "list",
                    onClick = { navController.navigate("list") }
                )
                NavIcon(
                    drawableId = R.drawable.add,
                    isSelected = currentRoute == "add",
                    onClick = { navController.navigate("add") }
                )
                NavIcon(
                    drawableId = R.drawable.person,
                    isSelected = currentRoute == "profile",
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    }
}



@Composable
fun NavIcon(
    drawableId: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = "Навигация",
            tint = if (isSelected) Black else BlackIcon,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    MaterialTheme(
        typography = CustomTypography
    ) {
        Navigation()
    }
}