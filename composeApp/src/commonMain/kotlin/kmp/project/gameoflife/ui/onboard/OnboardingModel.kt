package kmp.project.gameoflife.ui.onboard

import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.eighth_page_description
import gameoflife.composeapp.generated.resources.eighth_page_title
import gameoflife.composeapp.generated.resources.fifth_page_description
import gameoflife.composeapp.generated.resources.fifth_page_title
import gameoflife.composeapp.generated.resources.first_page_description
import gameoflife.composeapp.generated.resources.first_page_title
import gameoflife.composeapp.generated.resources.fourth_page_description
import gameoflife.composeapp.generated.resources.fourth_page_title
import gameoflife.composeapp.generated.resources.page1
import gameoflife.composeapp.generated.resources.page2
import gameoflife.composeapp.generated.resources.page3
import gameoflife.composeapp.generated.resources.page4
import gameoflife.composeapp.generated.resources.page5
import gameoflife.composeapp.generated.resources.page6_en
import gameoflife.composeapp.generated.resources.page7
import gameoflife.composeapp.generated.resources.second_page_description
import gameoflife.composeapp.generated.resources.second_page_title
import gameoflife.composeapp.generated.resources.settings_24px
import gameoflife.composeapp.generated.resources.seventh_page_description
import gameoflife.composeapp.generated.resources.seventh_page_title
import gameoflife.composeapp.generated.resources.sixth_page_description
import gameoflife.composeapp.generated.resources.sixth_page_title
import gameoflife.composeapp.generated.resources.third_page_description
import gameoflife.composeapp.generated.resources.third_page_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class OnboardingModel(
    val image: DrawableResource,
    val title: StringResource,
    val description: StringResource,
    val showColorExplanation: Boolean = false
) {
    data object FirstPage : OnboardingModel(
        image = Res.drawable.page1,
        title = Res.string.first_page_title,
        description = Res.string.first_page_description
    )

    data object SecondPage : OnboardingModel(
        image = Res.drawable.page2,
        title = Res.string.second_page_title,
        description = Res.string.second_page_description,
        showColorExplanation = true
    )

    data object ThirdPage : OnboardingModel(
        image = Res.drawable.page3,
        title = Res.string.third_page_title,
        description = Res.string.third_page_description
    )

    data object FourthPage : OnboardingModel(
        image = Res.drawable.page4,
        title = Res.string.fourth_page_title,
        description = Res.string.fourth_page_description
    )

    data object FifthPage : OnboardingModel(
        image = Res.drawable.page5,
        title = Res.string.fifth_page_title,
        description = Res.string.fifth_page_description
    )

    data object SixthPage : OnboardingModel(
        image = Res.drawable.page6_en, // Placeholder, will be handled in Platform.android.kt
        title = Res.string.sixth_page_title,
        description = Res.string.sixth_page_description
    )

    data object SeventhPage : OnboardingModel(
        image = Res.drawable.page7,
        title = Res.string.seventh_page_title,
        description = Res.string.seventh_page_description
    )

    data object EighthPage : OnboardingModel(
        image = Res.drawable.settings_24px,
        title = Res.string.eighth_page_title,
        description = Res.string.eighth_page_description
    )
}

