package com.learn.androidplayground.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learn.androidplayground.compose.ui.theme.AndroidPlaygroundTheme
import com.learn.androidplayground.compose.ui.theme.BlackText
import com.learn.androidplayground.compose.ui.theme.BorderColor
import com.learn.androidplayground.compose.ui.theme.InterFontFamily
import com.learn.androidplayground.compose.ui.theme.MerchantAppThemeColor

class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val (selected, setSelected) = remember {
        mutableIntStateOf(0)
    }
    val configuration = LocalConfiguration.current
    CustomTab(
        items = listOf("Overview", "Summary"),
        selectedItemIndex = selected,
        onClick = setSelected,
        tabWidth = (configuration.screenWidthDp.dp - 32.dp) / 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
private fun MyTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(
                width = indicatorWidth,
            )
            .offset(
                x = indicatorOffset,
            )
            .border(
                width = 2.dp,
                color = indicatorColor,
                shape = RoundedCornerShape(48.dp)
            )
            .clip(RoundedCornerShape(48.dp))
            .background(
                color = White,
            ),
    )
}

@Composable
private fun MyTabItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    tabWidth: Dp,
    text: String,
    modifier: Modifier
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            MerchantAppThemeColor
        } else {
            BlackText
        },
        animationSpec = tween(easing = LinearEasing), label = "",
    )

    Text(
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxHeight()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .width(tabWidth),
        text = text,
        fontFamily = InterFontFamily,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = tabTextColor,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun CustomTab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp,
    onClick: (index: Int) -> Unit,
) {
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing, durationMillis = 60), label = "",
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(BorderColor)
            .height(48.dp),
    ) {
        MyTabIndicator(
            indicatorWidth = tabWidth,
            indicatorOffset = indicatorOffset,
            indicatorColor = MerchantAppThemeColor,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clip(CircleShape),
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                MyTabItem(
                    isSelected = isSelected,
                    onClick = {
                        onClick(index)
                    },
                    tabWidth = tabWidth,
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}