# Lietuvių kalbos mokytojas

Android programėlė rusakalbiams, besimokantiems lietuvių kalbos.

## Mokymosi turinys
- 2 000 lietuviškų žodžių ir kasdienių sakinių su vertimais į rusų kalbą
- Teminiai žodynai ir praktinės sakinių kortelės
- Kortelės, pasirinkimo testai, rašymo ir klausymo pratimai
- Kartojimas, mėgstamiausi žodžiai, dienos tikslai ir statistika
- Šviesi ir tamsi temos
- Pažanga saugoma įrenginyje; turinys veikia neprisijungus
- Teksto skaitymui naudojamas Android lietuvių kalbos TTS balsas, jei jis įdiegtas

## Nemokamas APK kūrimas per GitHub Actions

Šiame projekte yra paruoštas debesijos kūrimo procesas: `.github/workflows/android-apk.yml`.

1. Atsisiųskite šį ZIP ir išskleiskite jį kompiuteryje.
2. Prisijunkite prie [GitHub](https://github.com/) ir sukurkite naują **tuščią** repository. README, `.gitignore` ir licencijos generuoti nereikia.
3. Įkelkite visus šio aplanko failus į repository (įskaitant paslėptą `.github` aplanką). Paprasčiausia: repository puslapyje pasirinkite **Add file → Upload files**, nutempkite failus ir aplankus, tada **Commit changes**. Jei naršyklė neleidžia įkelti `.github`, naudokite GitHub Desktop arba `git`.
4. GitHub repository puslapyje atverkite **Actions**. Jei prašoma, patvirtinkite Actions įjungimą.
5. Pasirinkite **Build Android APK** → **Run workflow** → **Run workflow**. Pirmas įkėlimas į `main` arba `master` šaką taip pat paleidžia kūrimą automatiškai.
6. Atverkite užbaigtą veiksmą (žalia varnelė), nuslinkite į **Artifacts** ir atsisiųskite `lietuviu-kalbos-mokytojas-debug-apk`.
7. Išskleiskite atsisiųstą artifact ZIP — viduje bus `app-debug.apk`. Perkelkite APK į Android telefoną ir jį įdiekite.

Kūrimas naudoja GitHub-hosted Ubuntu runner, Java 17 ir Gradle 8.11.1. Debug APK yra skirtas bandymui ir asmeniniam įdiegimui; viešam platinimui Google Play reikalingas pasirašytas release AAB/APK.

## Vietinis kūrimas

Atidarykite projektą Android Studio ir paleiskite `:app:assembleDebug`.

Pastaba: sakinių kortelės sudarytos iš dažnų pirmojo asmens veiksmažodžių formų ir laiko / dažnumo aplinkybių. Vertimai pateikiami mokymosi tikslais.
