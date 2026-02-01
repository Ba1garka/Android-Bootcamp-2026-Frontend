package ru.sicampus.bootcamp2026

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.BlackIcon
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.Grey
import ru.sicampus.bootcamp2026.ui.theme.Yellow

@Composable
fun HomeScreen() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding( top = 40.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // аватарка и приветствие и кнопка уведомлений
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть: аватарка и приветствие
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Аватарка
                    Box(
                        modifier = Modifier.size(86.dp).clip(CircleShape).background(BluePrimary)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Аватар пользователя",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column() {
                        Text( text = "привет,", style = MaterialTheme.typography.displaySmall )
                        Text( text = "Ивана", style = MaterialTheme.typography.displayMedium )
                    }
                }

                // уведомления
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(Grey)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Уведомления",
                        tint = BlackIcon,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = "готовы к вызовам сегодняшнего дня?",
                style = MaterialTheme.typography.displayLarge,
                color = Color(0xFF666666)
            )

            Surface(
                modifier = Modifier.width(140.dp).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = Yellow,
                tonalElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "сегодня",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.Black
                    )
                }
            }

            val events = listOf(
                Event("11:00", "совещание", BluePrimary),
                Event("11:00", "совещание", BluePrimary),
                Event("10:00", "совещание", BluePrimary),
                Event("13:00", "совещание", BluePrimary),
                Event("15:00", "совещание", BluePrimary),
                Event("17:00", "совещание", BluePrimary),
                Event("15:00", "совещание", BluePrimary),
                Event("17:00", "совещание", BluePrimary)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(events) { event ->
                    EventCard(event = event)
                }
            }
        }

        // навигация
        Box(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 25.dp),
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
                    NavIcon(drawableId = R.drawable.home, isSelected = true)
                    NavIcon(drawableId = R.drawable.list)
                    NavIcon(drawableId = R.drawable.add)
                    NavIcon(drawableId = R.drawable.calendar)
                    NavIcon(drawableId = R.drawable.person)
                }
            }
        }
    }
}

@Composable
fun NavIcon(drawableId: Int, isSelected: Boolean = false) {
    IconButton(
        onClick = { },
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

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = event.cardColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {

            IconButton(
                onClick = { },
                modifier = Modifier.size(32.dp).align(Alignment.TopEnd)
            ){
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Стрелка",
                    tint = Color(0xFF000409),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Инфа карточки
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text( text = event.title, style = MaterialTheme.typography.bodyMedium )

                Text( text = event.time, style = MaterialTheme.typography.bodyLarge )
            }
        }
    }
}

data class Event(
    val time: String,
    val title: String,
    val cardColor: Color
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    MaterialTheme(
        typography = CustomTypography
    ) {
        HomeScreen()
    }
}

