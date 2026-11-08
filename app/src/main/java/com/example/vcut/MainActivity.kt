package com.example.vcut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vcut.ui.theme.ArkaPlan
import com.example.vcut.ui.theme.EtiketGri
import com.example.vcut.ui.theme.FazlaKiloluTuruncu
import com.example.vcut.ui.theme.HesaplaMavi
import com.example.vcut.ui.theme.KartBeyaz
import com.example.vcut.ui.theme.NormalMavi
import com.example.vcut.ui.theme.ObezKirmizi
import com.example.vcut.ui.theme.VücutTheme
import com.example.vcut.ui.theme.ZayifYesil
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VücutTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BmiEkrani(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BmiEkrani(modifier: Modifier = Modifier) {
    var boy by remember { mutableStateOf("") }
    var kilo by remember { mutableStateOf("") }
    var sonuc by remember { mutableStateOf<BmiSonuc?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArkaPlan)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Boy (cm):",
            color = EtiketGri,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = boy,
            onValueChange = { boy = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = KartBeyaz,
                unfocusedContainerColor = KartBeyaz,
                focusedBorderColor = HesaplaMavi,
                unfocusedBorderColor = Color(0xFFDDDDDD)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Kilo (kg):",
            color = EtiketGri,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = kilo,
            onValueChange = { kilo = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = KartBeyaz,
                unfocusedContainerColor = KartBeyaz,
                focusedBorderColor = HesaplaMavi,
                unfocusedBorderColor = Color(0xFFDDDDDD)
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { sonuc = bmiHesaplaVeSiniflandir(boy, kilo) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HesaplaMavi)
        ) {
            Text(
                text = "HESAPLA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        when (val mevcut = sonuc) {
            is BmiSonuc.Basarili -> {
                val kategoriRenk = when (mevcut.kategori) {
                    BmiKategori.ZAYIF -> ZayifYesil
                    BmiKategori.NORMAL -> NormalMavi
                    BmiKategori.FAZLA_KILOLU -> FazlaKiloluTuruncu
                    BmiKategori.OBEZ -> ObezKirmizi
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = KartBeyaz),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BMI: ${String.format(Locale("tr", "TR"), "%.1f", mevcut.bmi)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = EtiketGri
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "(Kategori: ${mevcut.kategori.etiket})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = kategoriRenk
                        )
                    }
                }
            }
            is BmiSonuc.Hata -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = KartBeyaz),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = mevcut.mesaj,
                        modifier = Modifier.padding(24.dp),
                        color = ObezKirmizi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            null -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BmiEkraniPreview() {
    VücutTheme(darkTheme = false, dynamicColor = false) {
        BmiEkrani()
    }
}
