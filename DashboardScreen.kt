package com.yourpackage.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boostcamp.ui.theme.BluePrimary

@Composable
fun ImprovedDashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
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
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFA37FE5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "И",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = Color.Black
                    )
                }

                Column() {
                    Text(
                        text = "привет,",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.Black
                    )

                    Text(
                        text = "Ивана",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                }
            }

            // Кнопка уведомлений
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F7))
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Уведомления",
                    tint = Color(0xFF333333),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = "готовы к вызовам сегодняшнего дня?",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp
            ),
            color = Color(0xFF666666)
        )

        // Кнопка "сегодня"
        Surface(
            onClick = { },
            modifier = Modifier.width(140.dp).height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFDCB84C),
            tonalElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "сегодня",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
        }

        // Заголовок
        Text(
            text = "Предстоящие события",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.Black
        )

        val events = listOf(
            Event("11:00", "совещание", BluePrimary),
            Event("11:00", "совещание", BluePrimary),
            Event("10:00", "совещание", BluePrimary),
            Event("13:00", "совещание", BluePrimary),
            Event("15:00", "совещание", BluePrimary),
            Event("17:00", "совещание", BluePrimary)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(events) { event ->
                NewEventCard(event = event)
            }
        }
    }
}

@Composable
fun NewEventCard(event: Event) {
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

            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Стрелка",
                tint = Color(0xFF000409),
                modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
            )

            // Контент карточки
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Текст "совещание"
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF333333)
                )

                // Время
                Text(
                    text = event.time,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color(0xFF333333)
                )
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
fun DashboardScreenPreview() {
    MaterialTheme {
        ImprovedDashboardScreen()
    }
}

