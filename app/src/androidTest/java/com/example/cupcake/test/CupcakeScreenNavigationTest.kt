package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//newly created test route must be "src > androidTest > java > <package route same as app> > test"

class CupcakeScreenNavigationTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /*탐색 상태를 확인하는 용도*/
    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        /**
         * UI 테스트에서 직접적으로 사용할 Composable
         * '@Before' 주석은 @Test가 달린 메서드보다 자동으로 먼저 실행되도록 해준다.
         */
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CupcakeApp(navController = navController)
        }
    }


    enum class CupcakeScreen(@StringRes val title: Int) {
        /**
         * 탐색을 위해 사용되는 경로들을 포함하는 enum class
         */
        Start(title = com.example.cupcake.R.string.app_name),
        Flavor(title = com.example.cupcake.R.string.choose_flavor),
        Pickup(title = com.example.cupcake.R.string.choose_pickup_date),
        Summary(title = com.example.cupcake.R.string.order_summary)
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        /**
         * 홈화면이 제대로 설정되었는지 확인하는 테스트 메서드
         * 현재 백 스택 항목의 대상 경로와 예상 경로를 비교하는 어셜션을 포함
         */
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }
}



