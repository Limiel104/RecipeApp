@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun  QuantityPicker(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    selectedWholeQuantity: String,
    selectedDecimalQuantity: String,
    selectedTypeQuantity: String,
    onSelectedWholeQuantity: (String) -> Unit,
    onSelectedDecimalQuantity: (String) -> Unit,
    onSelectedTypeQuantity: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val wholeList = listOf(
        "0",
        "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15",
        "16", "17", "18", "19", "20",
        "25", "30", "35", "40", "45",
        "50", "55", "60", "65", "70",
        "75", "60", "65", "90", "95",
        "100", "125", "150", "175", "200",
        "250", "300", "350", "400", "450",
        "500", "550", "600", "650", "700",
        "750", "800", "850", "900", "950",
    )

    val decimalList = listOf(
        "0", ".1", ".2", ".25", ".3",
        ".4", ".5", ".6", ".7", ".75",
        ".8", ".9"
    )

    val typeList = listOf(
        "-",
        "tsp", "tbsp", "g", "kg", "ml", "l",
        "can", "cup", "jar", "bottle", "bag",
        "bar", "block", "bowl", "box", "carton",
        "clove", "cube", "drop", "glass", "handful",
        "loaf", "piece", "pinch", "slice", "tin"
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                ListItemPicker(
                    value = selectedWholeQuantity,
                    onValueChange = { onSelectedWholeQuantity(it) },
                    dividersColor = MaterialTheme.colorScheme.primary,
                    list = wholeList,
                    modifier = modifier.weight(1F)
                )

                ListItemPicker(
                    value = selectedDecimalQuantity,
                    onValueChange = { onSelectedDecimalQuantity(it) },
                    dividersColor = MaterialTheme.colorScheme.primary,
                    list = decimalList,
                    modifier = modifier.weight(1F)
                )

                ListItemPicker(
                    value = selectedTypeQuantity,
                    onValueChange = { onSelectedTypeQuantity(it) },
                    dividersColor = MaterialTheme.colorScheme.primary,
                    list = typeList,
                    modifier = modifier.weight(1F)
                )
            }

            Button(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
                    .testTag("Save quantity button"),
                onClick = { onSave() }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun QuantityPickerPreview() {
    RecipeAppTheme {
        Surface {
            QuantityPicker(
                modalSheetState = rememberModalBottomSheetState(),
                selectedWholeQuantity = "2",
                selectedDecimalQuantity = ".0",
                selectedTypeQuantity = "cup",
                onSelectedWholeQuantity = {},
                onSelectedDecimalQuantity = {},
                onSelectedTypeQuantity = {},
                onDismiss = {},
                onSave = {}
            )
        }
    }
}