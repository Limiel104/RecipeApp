package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                    AccountContent()
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
            SearchBarItem()

            HomeTopCategories()

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Recipes",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
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
fun SearchBarItem(
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
                .padding(top = 16.dp)
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
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
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ImageItem()

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bookmark,
                    contentDescription = "icon button"
                )
            }
        }
    }
}

@Preview
@Composable
fun ImageItem() {
    Box(
        modifier = Modifier
//            .size(width.dp, height.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable {},
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data("")
                .crossfade(true)
                .placeholder(R.drawable.ic_image)
                .build(),
            contentDescription = "IMAGE",
            fallback = painterResource(R.drawable.ic_image),
            error = painterResource(R.drawable.ic_image),
            contentScale = ContentScale.Fit
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

            Column() {
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
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddRecipeContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Add recipe") },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add recipe button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Title",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Add photo button"
                    )

                    Text(
                        text = "Add photo",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }

            Text(
                text = "Description",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingredients",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Reorder",
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "Tap to edit, swipe to delete",
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )

            Column(
                modifier = Modifier
                    .padding(bottom = 20.dp)
            ) {
                for (i in 1..4) {
                    RecipeIngredientItem()

                    if (i != 4) {
                        HorizontalDivider()
                    }
                }
            }

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Servings",
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(
                    onClick = {}
                ) {
                    Text(text = "Text Button")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Prep time",
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(
                    onClick = {}
                ) {
                    Text(text = "Set time")
                }
            }
        }
    }
}

@Preview
@Composable
fun RecipeIngredientItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable {},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
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

        Text(
            text = "Ingredient",
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RecipeDetailsContent() {

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Ingredients", "Description")

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "Bookmark recipe button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Recipe name that is very long",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
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

            Column() {
                SecondaryTabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = state == index,
                            onClick = { state = index },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }

            if (state == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            imageVector = Icons.Default.HorizontalRule,
                            contentDescription = "Less button",
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
                        )
                    }

                    Text(
                        text = "1234 Servings",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(end = 16.dp)
                    )

                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "More button",
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    for (i in 1..7) {
                        RecipeIngredientItem()

                        if (i != 7) {
                            HorizontalDivider()
                        }
                    }
                }
            }
            else {
                Text(
                    text = "This is a description of a recipe.\nNew line\nAnother line",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SavedRecipesContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBarItem()

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recipes",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                        )

                        Text(
                            text = "Newest",
                            fontSize = 16.sp
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
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
fun AccountContent(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = "User Name")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit button"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add recipe"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recipes",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )

                Text(
                    text = "Newest",
                    fontSize = 16.sp
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(18) {
                    RecipeItem()
                }
            }
        }
    }
}