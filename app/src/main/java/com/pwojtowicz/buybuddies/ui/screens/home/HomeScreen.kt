package com.pwojtowicz.buybuddies.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.GroceryListMenuSheet
import com.pwojtowicz.buybuddies.ui.screens.home.container.MainListContainer
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel
import com.pwojtowicz.buybuddies.viewmodel.HomeViewModel
import com.pwojtowicz.buybuddies.viewmodel.SharedListsViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    groceryListViewModel: GroceryViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel(),
    sharedListsViewModel: SharedListsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredGroceryLists by viewModel.filteredGroceryLists.collectAsState()
    val groceryListLabels by viewModel.groceryListLabels.collectAsState()

    val localFocusManager = LocalFocusManager.current

    val toolbarHeight = 100.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
    var toolbarOffsetHeightPx by remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(uiState.newListId) {
        uiState.newListId?.let {
            navigateToScreen(
                navController = navController,
                route = "${NavItems.GroceryList.route}/$it"
            )
            viewModel.resetNewListId()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshGroceryLists()
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // TODO ERROR SNACK BAR
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .nestedScroll(nestedScrollConnection)
    ) {
        HomeTopContainer(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) }
                .fillMaxWidth(),
            onProfileClick = {
                navigateToScreen(
                    navController = navController,
                    route = NavItems.Profile.route
                )
            }
        )
        MainListContainer(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = toolbarOffsetHeightPx.roundToInt() + toolbarHeightPx.roundToInt()
                    )
                },
            scrollProgress = (toolbarOffsetHeightPx / -toolbarHeightPx).coerceIn(0f, 1f),
            groceryLists = filteredGroceryLists,
            groceryListLabels = groceryListLabels,
            onClickGroceryList = { groceryListId ->
                viewModel.setShowCardVisibility(false)
                viewModel.viewModelScope.launch {
                    navigateToScreen(
                        navController = navController,
                        route = "${NavItems.GroceryList.route}/$groceryListId"
                    )
                }
            },
            onLongPressGroceryList = { groceryListId ->
                viewModel.setShowMenuSheetVisibility(true)
                viewModel.setLongPressedGroceryList(groceryListId)
            },
            onSearchInput = viewModel::setSearchText,
            onStatusFilterChange = viewModel::setSelectedStatus,
            onLabelFilterChange = viewModel::setSelectedLabel
        )

        GroceryListAddFAB(
            onClick = { viewModel.setShowCardVisibility(true) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .size(60.dp)
        )

        AddGroceryList(
            isVisible = uiState.showCard,
            onDismiss = { viewModel.setShowCardVisibility(false) },
            onCreateList = { listName ->
                viewModel.viewModelScope.launch {
                    viewModel.createGroceryList(listName)
                }
                viewModel.setShowCardVisibility(false)
            }
        )

        GroceryListMenuSheet(
            isVisible = uiState.showMenuSheet,
            onDismiss = { viewModel.setShowMenuSheetVisibility(false) },
            onEdit = {},
            onMarkAsDone = {},
            onDelete = {
                viewModel.deleteGroceryListById(uiState.longPressedListId)
            },
            onShare = {}
        )
    }
}


@Preview
@Composable
fun PreviewHomeScreen() {
    val paddingValues = PaddingValues(30.dp)
    val navController = rememberNavController()

    HomeScreen(
        navController = navController,
        paddingValues = paddingValues
    )
}
