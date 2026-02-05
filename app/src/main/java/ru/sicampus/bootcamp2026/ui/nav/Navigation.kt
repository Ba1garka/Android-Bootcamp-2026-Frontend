package ru.sicampus.bootcamp2026.ui.nav


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
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthScreen
import ru.sicampus.bootcamp2026.ui.screen.register.RegistrationScreen
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // TODO в отдельный Use Case
//    val currentToken = runBlocking {AuthLocalDataSource.getToken()}
//    if (currentToken == null ) RegisterRoute else HomeRoute
    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = RegisterRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<HomeRoute> {
                HomeScreen()
            }

            composable<CalendarRoute> {
                CalendarScreen()
            }
            composable<RegisterRoute> {
                RegistrationScreen(
                    navController = navController,
                    onRegisterSuccess = {
                        navController.navigate(HomeRoute) {
                            popUpTo(AuthRoute) { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate(AuthRoute){
                            popUpTo(AuthRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<AuthRoute> {
                AuthScreen(
                    navController = navController,
                    onLoginSuccess = {
                        navController.navigate(HomeRoute) {
                            popUpTo(AuthRoute) { inclusive = true }
                        }
                    }
                )
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

        val showBottomBar = when (currentRoute) {
            "ru.sicampus.bootcamp2026.ui.nav.HomeRoute",
            "ru.sicampus.bootcamp2026.ui.nav.CalendarRoute",
            "ru.sicampus.bootcamp2026.ui.nav.ProfileRoute" -> true
            else -> false
        }

        if (showBottomBar) {
            BottomNavBar(navController = navController,
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier.padding(bottom = 25.dp),
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
                    isSelected = currentRoute == HomeRoute.route,
                    onClick = { navController.navigate(HomeRoute) }
                )
                NavIcon(
                    drawableId = R.drawable.calendar,
                    isSelected = currentRoute == CalendarRoute.route,
                    onClick = { navController.navigate(CalendarRoute) }
                )

                NavIcon(
                    drawableId = R.drawable.list,
                    isSelected = currentRoute == ListRoute.route,
                    onClick = { navController.navigate(ListRoute) }
                )
                NavIcon(
                    drawableId = R.drawable.add,
                    isSelected = currentRoute == AddRoute.route,
                    onClick = { navController.navigate(AddRoute) }
                )
                NavIcon(
                    drawableId = R.drawable.person,
                    isSelected = currentRoute == ProfileRoute.route,
                    onClick = { navController.navigate(ProfileRoute) }
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme(
        typography = CustomTypography
    ) {
        Navigation()
    }
}