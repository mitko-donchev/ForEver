package com.epicmillennium.furever.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.epicmillennium.furever.R
import com.epicmillennium.furever.presentation.model.ProfilePictureState
import com.epicmillennium.furever.presentation.theme.primaryLight
import com.epicmillennium.furever.presentation.ui.components.ChatFooter

@Composable
fun NewAdoptionView(
    pictureStates: List<ProfilePictureState>,
    onSendMessage: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box {
        var currentIndex by remember { mutableIntStateOf(0) }
        var isTextVisible by remember { mutableStateOf(false) }
        var isFirstTime by remember { mutableStateOf(true) }

        //Picture
        when (val pictureState = pictureStates[currentIndex]) {
            is ProfilePictureState.Loading -> CircularProgressIndicator()
            is ProfilePictureState.Remote -> {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = pictureState.uri,
                    contentScale = ContentScale.Crop,
                    onState = {
                        if (it is AsyncImagePainter.State.Success && isFirstTime) {
                            isTextVisible = true
                            isFirstTime = false
                        }
                    },
                    contentDescription = null
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            enter = scaleIn(tween(300, easing = LinearEasing), initialScale = 5f),
            exit = fadeOut(),
            visible = isTextVisible
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.you_ve_got).uppercase(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = primaryLight,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,

                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                val density = LocalDensity.current
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = stringResource(id = R.string.new_ongoing_adoption).uppercase(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = primaryLight,
                        fontStyle = FontStyle.Italic,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Black,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        shadow = Shadow(
                            color = Color.Blue.copy(alpha = .5f),
                            offset = with(density) {
                                Offset(4.dp.toPx(), 10.dp.toPx())
                            },
                            blurRadius = 3f
                        )
                    ),
                )

            }
        }
        Box(Modifier.fillMaxSize()) {
            //Upper picture index indicator
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                repeat(pictureStates.size) { index ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(3.dp)
                            .padding(horizontal = 4.dp)
                            .alpha(if (index == currentIndex) 1f else .5f)
                            .background(if (index == currentIndex) Color.White else Color.LightGray)
                    )
                }
            }
            //Clickable
            Row(Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (currentIndex > 0) currentIndex--
                        isTextVisible = false
                    })
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (currentIndex < pictureStates.size - 1) currentIndex++
                        isTextVisible = false
                    }
                )
            }

            IconButton(
                onClick = onDismissRequest, modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White,
                    contentDescription = null
                )
            }

            ChatFooter(
                modifier = Modifier.align(Alignment.BottomCenter),
                onSendClicked = onSendMessage,
                shape = RoundedCornerShape(6.dp)
            )
        }
    }
}