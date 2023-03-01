package com.ruben.apiremota.navigation

import com.ruben.apiremota.ui.theme.DetailScreenConst
import com.ruben.apiremota.ui.theme.GuessScreenConst
import com.ruben.apiremota.ui.theme.MainScreenConst
import com.ruben.apiremota.ui.theme.SearchScreenConst

sealed class AppScreens(val route: String) {
    object Main : AppScreens(MainScreenConst)
    object Search : AppScreens(SearchScreenConst)
    object Detail : AppScreens(DetailScreenConst)
    object Guess : AppScreens(GuessScreenConst)


}
