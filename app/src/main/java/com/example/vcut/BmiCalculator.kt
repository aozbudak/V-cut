package com.example.vcut

enum class BmiKategori(val etiket: String) {
    ZAYIF("Zayıf"),
    NORMAL("Normal"),
    FAZLA_KILOLU("Fazla Kilolu"),
    OBEZ("Obez")
}

sealed class BmiSonuc {
    data class Basarili(val bmi: Double, val kategori: BmiKategori) : BmiSonuc()
    data class Hata(val mesaj: String) : BmiSonuc()
}

/** Güvenlik filtresi: boş, ≤0 ve mantıksız boy (<100 cm) değerlerini reddeder. */
fun String.bmiGirdiDogrula(alanAdi: String, minDeger: Double = 0.0): Double? {
    val temiz = trim().replace(',', '.')
    if (temiz.isEmpty()) return null
    return temiz.toDoubleOrNull()?.takeIf { it > minDeger }
}

/** Extension: boy (cm) üzerinden BMI hesaplar. */
fun Double.bmiHesapla(kilo: Double): Double {
    val boyMetre = this / 100.0
    return kilo / (boyMetre * boyMetre)
}

/** Infix: `kilo hesapla boy` şeklinde kullanım. */
infix fun Double.hesapla(boyCm: Double): Double = boyCm.bmiHesapla(this)

fun Double.bmiKategori(): BmiKategori = when {
    this < 18.5 -> BmiKategori.ZAYIF
    this < 25.0 -> BmiKategori.NORMAL
    this < 30.0 -> BmiKategori.FAZLA_KILOLU
    else -> BmiKategori.OBEZ
}

fun bmiHesaplaVeSiniflandir(boyMetni: String, kiloMetni: String): BmiSonuc {
    val boy = boyMetni.bmiGirdiDogrula("Boy")
        ?: return BmiSonuc.Hata("Boy alanı boş olamaz veya geçersiz.")
    val kilo = kiloMetni.bmiGirdiDogrula("Kilo")
        ?: return BmiSonuc.Hata("Kilo alanı boş olamaz veya geçersiz.")

    if (boy <= 0 || kilo <= 0) {
        return BmiSonuc.Hata("Boy ve kilo 0'dan büyük olmalıdır.")
    }
    if (boy < 100) {
        return BmiSonuc.Hata("Boy 100 cm'den küçük olamaz.")
    }

    val bmi = kilo hesapla boy
    return BmiSonuc.Basarili(bmi = bmi, kategori = bmi.bmiKategori())
}
