## UI 테스트

### androidTest 종속 항목

```gradle
androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("androidx.navigation:navigation-testing:2.6.0")
androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
```

### UI 테스트 파일 경로

- `src` 밑에 `androidTest/java` 디렉토리를 생성한다.
- `java` 밑에 코드가 포함된 `main`의 패키지 구조와 동일한 패키지를 생성한다.

> 변수, 함수와 클래스 등의 이름은 실제 코드에 사용된 것들로 설명하겠다.

### 테스트용 Navigation Host 설정

UI를 테스트하려면 Compose 테스트 규칙이 필요하다. 이는 Navigation을 테스트할 때도 마찬가지지만 Navigation의 경우 추가적인 설정이 필요하다.

테스트 클래스에선 `NavHostController` 대신 `TestNavHostController`를 사용하고 이 탐색 컨트롤러로 테스트 규칙을 구성한다.

1. `createAndroidComposeRule`을 사용하여 `ComponentActivity`를 유형 매개변수로 전달하고 테스트 규칙을 만든다.
   ```kotlin
   @get:Rule
   val composeTestRule = createAndroidComposeRule<ComponentActivity>()
   ```
2. `TestNavHostController`를 `lateinit` 변수로 인스턴스화한다.
   ```kotlin
   private lateinit var navController: TestNavHostController
   ```
3. `setUpCupcakeNavHost()`라는 UI 테스트에 사용할 컴포저블을 만든다. 이 메서드에서 Compose 테스트 규칙의 `setContent()`를 호출하고, `setContent()`의 람다 내에서 테스트할 컴포저블을 호출한다.
   ```kotlin
   fun setupCupcakeNavHost() {
       composeTestRule.setContent {
           CupcakeApp()
       }
   }
   ```
4. 테스트가 여러 개 작성되고, 모든 테스트마다 동일한 함수가 호출되는 경우, 그 함수가 포함된 테스트에 `@Before` 주석을 붙여 어떤 테스트가 실행되는 그 전에 무조건 `@Before` 테스트가 실행되도록 할 수 있다.   
   여기에선 `setupCupcakeNavHost` 내부에 `TestNavHostController` 객체를 선언하였다. 따라서 다른 테스트들이 실행되기 전 navController가 생성될 것이다.
   ```kotlin
   @Before
   fun setupCupcakeNavHost() {
       composeTestRule.setContent {
           navController = TestNavHostController(LocalContext.current).apply {
               navigatorProvider.addNavigator(ComposeNavigator())
           }
           CupcakeApp(navController = navController)
       }
   } 
   ```

## 테스트 작성

```kotlin
@Test
fun cupcakeNavHost_clickNextOnFlavorScreen_navigatesToPickupScreen() {
    navigateToFlavorScreen()
    composeTestRule.onNodeWithStringId(R.string.next)
        .performClick()
    navController.assertCurrentRouteName(CupcakeScreen.Pickup.name)
}

@Test
fun cupcakeNavHost_clickBackOnFlavorScreen_navigatesToStartOrderScreen() {
    navigateToFlavorScreen()
    performNavigateUp()
    navController.assertCurrentRouteName(CupcakeScreen.Start.name)
}

@Test
fun cupcakeNavHost_clickCancelOnFlavorScreen_navigatesToStartOrderScreen() {
    navigateToFlavorScreen()
    composeTestRule.onNodeWithStringId(R.string.cancel)
        .performClick()
    navController.assertCurrentRouteName(CupcakeScreen.Start.name)
}

@Test
fun cupcakeNavHost_clickNextOnPickupScreen_navigatesToSummaryScreen() {
    navigateToPickupScreen()
    composeTestRule.onNodeWithText(getFormattedDate())
        .performClick()
    composeTestRule.onNodeWithStringId(R.string.next)
        .performClick()
    navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
}

@Test
fun cupcakeNavHost_clickBackOnPickupScreen_navigatesToFlavorScreen() {
    navigateToPickupScreen()
    performNavigateUp()
    navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
}

@Test
fun cupcakeNavHost_clickCancelOnPickupScreen_navigatesToStartOrderScreen() {
    navigateToPickupScreen()
    composeTestRule.onNodeWithStringId(R.string.cancel)
        .performClick()
    navController.assertCurrentRouteName(CupcakeScreen.Start.name)
}

@Test
fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
    navigateToSummaryScreen()
    composeTestRule.onNodeWithStringId(R.string.cancel)
        .performClick()
    navController.assertCurrentRouteName(CupcakeScreen.Start.name)
}
```

>[!NOTE]
> `assertCurrentRouteName`은 NavController 클래스에 확장함수를 추가한 것이다. 이러한 함수를 `도우미 함수`라고 하며, 여러 테스트에서 반복적으로 불리는 코드들을 이런 식으로 관리할 수 있다.
>
> 기타 메서드들도 모두 도우미 메서드들이다.




