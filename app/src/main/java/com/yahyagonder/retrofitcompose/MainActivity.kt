package com.yahyagonder.retrofitcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yahyagonder.retrofitcompose.model.CryptoModel
import com.yahyagonder.retrofitcompose.service.CryptoAPI
import com.yahyagonder.retrofitcompose.ui.theme.RetrofitComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var cryptoModels = remember { mutableStateListOf<CryptoModel>() }

    val baseUrl = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()

    call.enqueue(object: Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {

            if (response.isSuccessful) {
                response.body()?.let {
                    //code
                    cryptoModels.addAll(it)
                }
            }

        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    Scaffold(topBar = { AppBar() } ) {
        CryptoList(cryptos = cryptoModels)
    }

}

@Composable
fun CryptoList(cryptos: List<CryptoModel>) {

    LazyColumn(contentPadding = PaddingValues(5.dp)) {

        items(cryptos) {

            CryptoRow(crypto = it)

        }

    }

}

@Composable
fun CryptoRow(crypto: CryptoModel) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.surface)) {

        Text(text = crypto.currency,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold)
        Text(text = crypto.price,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(2.dp),)

    }

}

@ExperimentalMaterial3Api
@Composable
fun AppBar() {

    TopAppBar(title = { Text(text = "Test App", fontSize = 25.sp) })

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}