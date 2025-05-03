package kmp.project.gameoflife.ui.onboard

import gameoflife.composeapp.generated.resources.Res
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
import gameoflife.composeapp.generated.resources.second_page_description
import gameoflife.composeapp.generated.resources.second_page_title
import gameoflife.composeapp.generated.resources.third_page_description
import gameoflife.composeapp.generated.resources.third_page_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class OnboardingModel(
    val image: DrawableResource,
    val title: StringResource,
    val description: StringResource,
) {
    data object FirstPage : OnboardingModel(
        image = Res.drawable.page1,
        title = Res.string.first_page_title,
        description = Res.string.first_page_description
    )

    data object SecondPage : OnboardingModel(
        image = Res.drawable.page2,
        title = Res.string.second_page_title,
        description = Res.string.second_page_description
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
}

