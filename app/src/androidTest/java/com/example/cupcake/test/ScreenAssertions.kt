package com.example.cupcake.test

/**
 * 도우미 메서드를 사용하여 여러 번 사용되는 어셜션을 함수화하여 관리할 수 있다.
 */

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
     assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}