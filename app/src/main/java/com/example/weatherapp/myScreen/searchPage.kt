package com.example.weatherapp.myScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.api_Retrofit.NetworkResponse
import com.example.weatherapp.api_Retrofit.WeatherModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun MakeSearch(viewModel: WeatherViewModel){
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherModel.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                singleLine = true,
                label = {
                    Text("Search location...")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Blue,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Blue
                )
            )
            IconButton(
                onClick = {
                    viewModel.getData(city)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    Modifier.size(30.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ){
            when(val result = weatherResult.value){
                is NetworkResponse.Failure -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {

                    CircularProgressIndicator()
                }
                is NetworkResponse.Success -> {
                    //Text(text = result.data.toString())
                    WeatherDetails(result.data)
                }
                null -> {}
            }
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        CityAndCountry(data)
        CenterColumn(data)
        Spacer(modifier = Modifier.height(32.dp))
        DetailCard(data)
    }
}

@Composable
fun DetailCard(data: WeatherModel){
    Card(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MyText("Wind Speed",data.current.wind_mph + "mph")
                MyText("Humidity",data.current.humidity)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MyText("Precipitation",data.current.precip_in + "mm")
                MyText("UV",data.current.uv)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MyText("Date",data.location.localtime.split(" ")[0])
                MyText("Time",data.location.localtime.split(" ")[1])
            }
        }
    }
}


@Composable
fun MyText(condition: String, key :String){
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = key,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = condition,
            style = MaterialTheme.typography.titleSmall,
            color = Color.DarkGray
        )
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CenterColumn(data: WeatherModel){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.current.temp_c,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        GlideImage(
            modifier = Modifier.size((120.dp)),
            model = "https:${data.current.condition.icon}",
            contentDescription = "Condition"
        )
        Text(
            text = data.current.condition.text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CityAndCountry(data: WeatherModel){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location Icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer( modifier = Modifier.width(10.dp))
        Text(
            text = data.location.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer( modifier = Modifier.width(5.dp))
        Text(
            text = data.location.country,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {

    }
}