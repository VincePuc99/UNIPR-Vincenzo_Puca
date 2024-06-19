# MobileDev2022-2023

## MyCurrency by Vincenzo Puca 297113
Android Studio Flamingo 2022.2.1 Patch 1 - Windows 11 22H2 - Galaxy S8 Android 13 (Fisico) - Java

### Descrizione
MainActivity si appoggia alla classe astratta SingleFragment per la creazione del singolo fragment home_page_layout_fragment,
sarà possibile infatti riutilizzare la classe per la creazione dei fragment da parte di altre Activity, MainActivity è gestore diretto del Database tramite la classe DataHelper.
La classe HomePageFragment viene usata per la gestione di tutto ciò che concerne gli elementi di UI del fragment. Per la gestione del database da parte della View, 
la HomePageFragment chiamerà il metodo apposito OnDataPass, sito nella MainActivity, per la richiesta e il passaggio di informazioni, questo per evitare che il fragment abbia diretto accesso alle informazioni
del database principale.

Il DataBase (basato su Room https://developer.android.com/training/data-storage/room) è direttamente gestito dalla classe DataHelper, la quale, verrà richiamata a sua volta dalla MainActivity all'avvio dell'applicazione. La base di dati viene utilizzata per salvare in maniera persistente le valute preferite dall'utente, il modello è così strutturato: Entità Currencies, la quale continue gli attributi per la Valuta di inzio e la Valuta di arrivo, Query DAO, interamente gestite dal file CurrenciesDAO, a loro volta richiamate nel DataHelper.

Ogni Classe è impostata per essere visualizzata nella propria cartella di appartenenza secondo il proprio funzionamento. Esistono difatti le cartelle Data e UI all'interno del progetto stesso.

### Interfaccia
Vista Home con TextInputEditBox per inserimento della valuta da convertire, alla pressione del tasto "Converti" verrà richiesta una chiamata API (https://apilayer.com/marketplace/exchangerates_data-api) in background (classe APIRequester), mediante l'uso del CompletableFuture, la risposta verrà salvata poi in una variabile Data JSONObject la quale,
dopo essere stata scompattata nelle mappe JSON, mostrerà la valuta convertita all'utente tramite un'altra TextInputEditBox.

Il pulsante "Cambio Settimanale" effettuerà 7 richieste API sempre alla classe APIRequester (gestito in background tramite un CompletableFuture https://developer.android.com/reference/java/util/concurrent/CompletableFuture) con il campo della richiesta che include i 7 giorni prima la data attuale
dell'utente, dopo la successiva risposta di ogni richiesta, con gestione di eventuali errori, mostrerà all'utente un dialog box con l'andamento della settimana precedente.

Il pulsante "Aggiungi ai preferiti" richiama il metodo OnDataPass per estrapolare i valori visualizzati negli Spinner, e mandarli alla MainActivity, la quale inserirà i valori nel database sempre tramite uso di DAO.

Il pulsante "Seleziona dai preferiti" richiede una Query di selezione al MainActivity, la quale risponderà al gestore della View con i valori preferiti direttamente dal database. Il controller mostrerà quindi un DialogBox di selezione, alla scelta delle valute preferite queste verranno automaticamente impostate negli Spinner dell'interfaccia.

Il pulsante "Rimuovi tutti i preferiti" funzionerà in modo analogo a "Seleziona dai preferiti" richiedendo una Query di rimozione totale dei preferiti dal DataBase,
attraverso OnDataPass gestito dalla MainActivity.
