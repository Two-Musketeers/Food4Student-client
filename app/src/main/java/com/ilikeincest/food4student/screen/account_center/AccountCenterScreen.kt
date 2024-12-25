package com.ilikeincest.food4student.screen.account_center

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.account_center.component.DisplayNameCard
import com.ilikeincest.food4student.screen.account_center.component.ExitAppCard
import com.ilikeincest.food4student.screen.account_center.component.RemoveAccountCard
import com.ilikeincest.food4student.screen.account_center.component.card
import com.ilikeincest.food4student.model.Account
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountCenterScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    viewModel: AccountCenterViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState(initial = Account())
    val provider = user.provider.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    val context = LocalContext.current

    // Log the user token to the LogCat
    LaunchedEffect(Unit) {
        viewModel.logUserToken()
    }

    Scaffold(contentWindowInsets = contentWindowInsets) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = { Text(stringResource(R.string.account_center)) })

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            DisplayNameCard(user.displayName) { viewModel.onUpdateDisplayNameClick(it) }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            Card(modifier = Modifier.card()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = String.format(
                            stringResource(R.string.profile_email),
                            user.email
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = String.format(stringResource(R.string.profile_uid), user.id),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = String.format(stringResource(R.string.profile_provider), provider),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            ExitAppCard(
                navController,
            ) { navController -> viewModel.onSignOutClick(context) }
            // TODO: reauth before delete (yes its needed)
            RemoveAccountCard(
                navController,
            ) { navController -> viewModel.onDeleteAccountClick(context) }
        }
    }
}

@Preview
@Composable
private fun AccountCenterPrev() {
    ScreenPreview {
        AccountCenterScreen(
            NavController(LocalContext.current),
            viewModel = hiltViewModel()
        )
    }
}