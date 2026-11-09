# SEBA – szkic aplikacji Android

Projekt demonstracyjny w Java + XML dla Android Studio.

## Funkcje
- Ekran logowania.
- Panel administratora z dwoma przyciskami: ALARM i OSTRZEŻENIE.
- Dla obu typów: popup wpisania treści, a następnie osobne potwierdzenie wysłania.
- Panel użytkownika z podglądem ostatniego komunikatu.
- Historia komunikatów.
- Wylogowanie.

## Dane demonstracyjne
- Administrator: `admin` / `admin`
- Uczeń: `uczen` / `uczen`

## Ograniczenia wersji demo
To nie jest jeszcze wersja produkcyjna:
- logowanie jest lokalną symulacją, bez Firebase;
- komunikaty są przechowywane w pamięci aplikacji i znikają po jej zamknięciu;
- komunikaty nie są wysyłane na inne telefony;
- FCM, Firestore i zabezpieczenia ról zostaną dodane w kolejnym etapie.

## Uruchomienie
1. Rozpakuj ZIP.
2. Otwórz folder `SEBA_Android_Szkic` w Android Studio.
3. Poczekaj na Gradle Sync.
4. Uruchom aplikację na emulatorze lub telefonie.

Wymagane: Android Studio z JDK 17 i Android SDK 35.
