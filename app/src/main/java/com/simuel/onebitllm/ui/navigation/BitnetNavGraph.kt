package com.simuel.onebitllm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simuel.onebitllm.ui.chat.ChatRoute
import com.simuel.onebitllm.ui.chatlist.ChatRoomListRoute
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.splash.SplashRoute

/**
 * App navigation graph. Starts at [SPLASH] and navigates to [CHAT_LIST].
 */
object BitnetDestination {
    const val SPLASH = "splash"
    const val CHAT_LIST = "chat_list"
    const val CHAT = "chat/{id}"
}

@Composable
fun BitnetNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BitnetDestination.SPLASH
    ) {
        addSplash(navController)
        addChatList(navController)
        addChat(navController)
    }
}

private fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(BitnetDestination.SPLASH) {
        SplashRoute(onNavigateToChatList = {
            navController.navigate(BitnetDestination.CHAT_LIST) {
                popUpTo(BitnetDestination.SPLASH) { inclusive = true }
            }
        })
    }
}

private fun NavGraphBuilder.addChatList(navController: NavHostController) {
    composable(BitnetDestination.CHAT_LIST) {
        ChatRoomListRoute(
            onNavigateChat = { id ->
                navController.navigate("chat/$id")
            }
        )
    }
}
private fun NavGraphBuilder.addChat(navController: NavHostController) {
    composable(BitnetDestination.CHAT) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toLong() ?: return@composable
        ChatRoute(
            chatId = id,
            onBack = { navController.popBackStack() }
        )
    }
}
