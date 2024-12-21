package com.learn.androidplayground.compose.breathexercise

import android.content.Context
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learn.androidplayground.R
import com.learn.androidplayground.compose.ui.theme.InterFontFamily

@Composable
fun MainScreen(viewModel: MainViewmodel) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResumeState()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        viewModel.onPauseState()
    }

    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE)
            as Vibrator

    Scaffold(
        containerColor = Color.White,
    ) { paddingValues ->
        MainScreenContent(
            paddingValues = paddingValues,
            currentStateText = viewState.stateToShow,
            timeLimit = viewState.currentStateTimeLimit,
            remainingTime = viewState.currentStateTimeRemaining,
            buttonText = viewState.buttonText,
            onButtonClick = {
                viewModel.onActionButtonClick(vibrator)
            }
        )
    }
}

@Composable
private fun MainScreenContent(
    paddingValues: PaddingValues,
    currentStateText: String,
    timeLimit: Int,
    remainingTime: Int,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(paddingValues)
    ) {
        val (stateText, progressBar, remainingTimeText, actionButton) = createRefs()
        Text(
            text = currentStateText,
            modifier = Modifier
                .constrainAs(stateText) {
                    top.linkTo(parent.top, margin = 40.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            fontSize = 24.sp,
            color = colorResource(R.color.dark_orange),
            fontWeight = FontWeight.SemiBold,
        )
        CircularProgressIndicator(
            modifier = Modifier
                .size(200.dp)
                .constrainAs(progressBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            color = colorResource(R.color.dark_orange),
            progress = (remainingTime / timeLimit.toFloat()),
            trackColor = colorResource(R.color.light_orange),
            strokeWidth = 18.dp,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "0$remainingTime",
            fontSize = 30.sp,
            color = colorResource(R.color.dark_orange),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(remainingTimeText) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(color = colorResource(R.color.dark_orange))
                .clickable {
                    onButtonClick()
                }
                .constrainAs(actionButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 24.dp)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = buttonText,
                fontSize = 20.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = TextUnit(-0.03F, TextUnitType.Sp),
                color = Color.White,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}