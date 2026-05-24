package kmp.project.gameoflife.ui.onboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.onboarding_color_and
import gameoflife.composeapp.generated.resources.onboarding_color_custom
import gameoflife.composeapp.generated.resources.onboarding_color_explanation_prefix
import gameoflife.composeapp.generated.resources.onboarding_color_fixed
import gameoflife.composeapp.generated.resources.onboarding_color_moving
import kmp.project.gameoflife.GifImage
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingGraphUI(onboardingModel: OnboardingModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        GifImage(
            ressources = onboardingModel.image,
            modifier = Modifier
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(onboardingModel.title),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(onboardingModel.description),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (onboardingModel.showColorExplanation) {
            Spacer(modifier = Modifier.height(16.dp))
            val explanation = buildAnnotatedString {
                append(stringResource(Res.string.onboarding_color_explanation_prefix))

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(stringResource(Res.string.onboarding_color_custom))
                }
                append(", ")

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(stringResource(Res.string.onboarding_color_moving))
                }

                append(stringResource(Res.string.onboarding_color_and))

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)) {
                    append(stringResource(Res.string.onboarding_color_fixed))
                }
            }
            Text(
                text = explanation,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

    }



}