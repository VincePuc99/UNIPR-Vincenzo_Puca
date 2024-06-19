# MobileDev2022-2023

## PlaceReminder by Vincenzo Puca 297113
Xcode 14.3 - MacOS Ventura 13.4 - iPhone 13 Pro iOS 16.4 (Virtuale) - Objective C

### Descrizione
L'applicazione è interamente gestita dal ViewController.m con file di interfaccia ViewController.h, la classe di appoggio Place.m (Place.h) viene usata come modello per la creazione dei singoli oggetti place,
i quali verranno poi aggiunti alla lista di oggetti "places" dichiarata in ViewController.h. All'avvio verranno mostrati i pulsanti di creazione del nuovo luogo e della visualizzazione della mappa, alla pressione del pulsante di creazione verrà visualizzato un dialog box per l'inserimento dei dati del place, alla conferma del luogo, tramite il pulsante della dialog, verrà effettuato un reverse geocoding con precedenza sulle coordinate (lat,long), se queste dovessero essere vuote verrà utilizzato il campo dell'indirizzo con un metodo preposto di reverse geocoding tramite indirizzo (https://developer.apple.com/documentation/corelocation/clgeocoder).

Se l'aggiunta si è conclusa senza errori (segnalati tramite Alert) verrà quindi aggiornata la visualizzazione della lista nella TableView preposta, verrà quindi effettuato il salvataggio della lista stessa in maniera persistente. La TableView sarà ordinata per data di aggiunta o di modifica. All'interno delle celle nella TableView, oltre alla visualizzazione di tutte le informazioni precedentemente descritte, sarà possibile eliminare o modificare tramite pulsanti il place selezionato. Verrà quindi aggiornata sia la lista che la tabella in modo da rispecchiare le modifiche effettuate, con la visualizzazione in cima dell'ultimo place aggiunto o modificato.

Dopo aver popolato la tabella con i place desiderati, alla pressione del tasto di visualizzazione della mappa, verranno mostrati tutti i place salvati in precedenza.
Alla selezione del singolo pin verranno visualizzate alcune informazion di base, ma con la conseguente richiesta di callout tramite il pulsante "i" sul pin, saranno visualizzate tutte le informazioni precedentemente aggiunte tramite box.

Se l'utente ha garantito i permessi di posizione e di notifica, verrà segnalata la vicinanza nel raggio di 100 metri ai place aggiunti.

Per la persistenza dei dati, si utilizza il modello di salvataggio della lista di oggetti "places" su file apposito .dat, la lista infatti viene caricata e salvata all'apertura e alla modifica dei place.
Il file di salvataggio (in byte) viene codificato e decodificato di conseguenza tramite i metodi salvati nella classe Place.h in modo da avere una gestione non umanamente leggibile dei dati (https://developer.apple.com/documentation/foundation/nscoder).
