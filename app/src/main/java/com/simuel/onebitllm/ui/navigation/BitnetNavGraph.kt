package com.simuel.onebitllm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simuel.onebitllm.ui.chatlist.ChatRoomListScreen
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.splash.SplashRoute

/**
 * App navigation graph. Starts at [Splash] and navigates to [ChatList].
 */
object BitnetDestination {
    const val Splash = "splash"
    const val ChatList = "chat_list"
}

@Composable
fun BitnetNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BitnetDestination.Splash
    ) {
        addSplash(navController)
        addChatList()
    }
}

private fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(BitnetDestination.Splash) {
        SplashRoute(onNavigateToChatList = {
            navController.navigate(BitnetDestination.ChatList) {
                popUpTo(BitnetDestination.Splash) { inclusive = true }
            }
        })
    }
}

private fun NavGraphBuilder.addChatList() {
    composable(BitnetDestination.ChatList) {
        ChatRoomListScreen(
            chats = temporaryChats,
        )
    }
}

private val temporaryChats = listOf(
    ChatRoomItemUiState(1, "Title 1", "Hey, how are you?"),
    ChatRoomItemUiState(2, "Title 2", "I'm on my way"),
)