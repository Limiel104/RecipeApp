package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepTimePicker(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    selectedPrepTimeHours: String,
    selectedPrepTimeMinutes: String,
    onSelectedPrepTimeHours: (String) -> Unit,
    onSelectedPrepTimeMinutes: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val hourList = listOf(
        "0 hours", "1 hour", "2 hours",
        "3 hours","4 hours","5 hours",
        "6 hours","7 hours","8 hours",
        "9 hours","10 hours","11 hours",
        "12 hours", "13 hours", "14 hours",
        "15 hours", "16 hours", "17 hours",
        "18 hours","19 hours","20 hours",
        "21 hours","22 hours","23 hours"
    )

    val minuteList = listOf(
        "0 min", "5 min", "10 min",
        "15 min", "20 min", "25 min",
        "30 min", "35 min", "40 min",
        "45 min", "50 min", "55 min",
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("Prep time picker"),
        ) {
            Text(
                text = stringResource(id = R.string.prep_time),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(id = R.string.prep_time_sheet_subtext),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Light,
                modifier = modifier.padding(bottom = 20.dp)
            )

            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                ListItemPicker(
                    value = selectedPrepTimeHours,
                    onValueChange = { onSelectedPrepTimeHours(it) },
                    dividersColor = MaterialTheme.colorScheme.primary,
                    list = hourList,
                    modifier = modifier
                        .weight(1F)
                        .testTag("Hours list item picker")
                )

                ListItemPicker(
                    value = selectedPrepTimeMinutes,
                    onValueChange = { onSelectedPrepTimeMinutes(it) },
                    dividersColor = MaterialTheme.colorScheme.primary,
                    list = minuteList,
                    modifier = modifier
                        .weight(1F)
                        .testTag("Minutes list item picker")

                )
            }

            Button(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
                    .testTag("Save prep time button"),
                onClick = { onSave() }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PrepTimePickerPreview() {
    RecipeAppTheme {
        val modalSheetState = rememberModalBottomSheetState()

        PrepTimePicker(
            modalSheetState = modalSheetState,
            selectedPrepTimeHours = "4 hours",
            selectedPrepTimeMinutes = "0 min",
            onSelectedPrepTimeHours = {},
            onSelectedPrepTimeMinutes = {},
            onDismiss = {},
            onSave = {}
        )
    }
}