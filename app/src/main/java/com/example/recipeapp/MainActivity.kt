package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeContent()
                }
            }
        }
    }
}
@Preview
@Composable
fun HomeContent(

) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            HomeSearchBar()

            HomeTopCategories()

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Recipes",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2)
                    ) {
                        items(10) {
                            RecipeItem()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeSearchBar(
) {
    SearchBar(
        query = "searching",
        onQueryChange = {},
        onSearch = {},
        active = false,
        onActiveChange = {},
        trailingIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filter"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .padding(horizontal = 16.dp)
    ) {

    }
}

@Preview
@Composable
fun HomeTopCategories(
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "Top Categories",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TopCategoryItem()
                TopCategoryItem()
                TopCategoryItem()
                TopCategoryItem()
                TopCategoryItem()
            }
        }
    }
}

@Preview
@Composable
fun TopCategoryItem(
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null,
                )

                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null
                )
            }

            Row {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null
                )

                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null
                )
            }

            Text(text = "Category")
        }
    }
}

@Preview
@Composable
fun RecipeItem() {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(R.drawable.ic_image)
                .crossfade(true)
                .placeholder(R.drawable.ic_image)
                .build(),
            contentDescription = "IMAGE",
            fallback = painterResource(R.drawable.ic_image),
            error = painterResource(R.drawable.ic_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .clickable {}
        )
    }
}

@Preview
@Composable
fun ShoppingListContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Text(
                text = "Shopping List",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(top = 8.dp)
                    .padding(start = 16.dp)
            )

            Text(
                text = "123 items",
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(start = 16.dp)
            )

            LazyColumn() {
                items(5) {
                    ShoppingListCategoryItem()
                }
            }
        }
    }
}

@Preview
@Composable
fun ShoppingListCategoryItem() {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "Category Name",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(start = 16.dp)
            )

            Column {
                for (i in 1..3) {
                    ShoppingListIngredientItem()

                    if (i != 3) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ShoppingListIngredientItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(R.drawable.ic_image)
                   .crossfade(true)
                   .placeholder(R.drawable.ic_image)
                  .build(),
                contentDescription = "IMAGE",
                fallback = painterResource(R.drawable.ic_image),
                error = painterResource(R.drawable.ic_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .clickable {}
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row() {
                Text(
                    text = "Ingredient",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Quantity",
                    fontWeight = FontWeight.Light,
                )
            }

            Text(
                text = "Note",
                fontWeight = FontWeight.Light,
            )
        }
        
        Checkbox(
            checked = false,
            onCheckedChange = {}
        )
    }
}