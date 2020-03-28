Blockchain School Project
Teacher@Sandro Impastato
Developer@Matteo Librizzi

Il progetto si divide in 3 file, Block.java, Chain.java, Miner.java:
	Block.java:
		Contiene la classe Block, contenente negli attributi, il tempo in cui è stato creato il blocco, l'hash del blocco precedente, il nonce, la validità del blocco, l'hash del blocco stesso, il merkleRoot che può, una volta sviluppato un formato, e allegato ad esso un ledger, contenere i dati. Contiene il metodo equals() overridato, per controllare i campi.

	Chain.java:
		Contiene la classe Chain, che possiede fra gli attributi il primo blocco a far parte della catena, ed una LinkedList con tutti i blocchi. La catena è iniziata con dei dati ed un blocco del blocco precedente casuale, questo potrebbe esporre, se la funzione di hash non fosse sicura, a cercare di emulare un blocco inizale, cambiando l'inizio della catena, ed aggiungendo potenzialmente dei dati. La funzione isValid() restituisce un vettore con dei booleani ad indicare se quella posizione nella catena sia valida o meno, tuttavia, per come è sviluppata la classe Chain risulta impossibile inserire un blocco non valido. E' possibile, ed è stato testata, la creazione dei blocchi senza l'utilizzo dei miners. è possibile, dare lo start ai miners subito dopo l'inserimento della string richiesta, tuttavia ritengo sia importante per capire il funzionamento della tecnologia BlockChain, lasciare la possibilità che i dati non vengano inseriti in ordine.

		Contiene un secondo Main commentato, qualora si voglia analizzare il funzionamento dei Miners in presenza di due catene differenti.

	Miner.java
		La classe Miner, non implementata nella versione 1.0, implementa i Thread, necessita come paramentri di una Chain su cui lavorare e dei dati(byte[]), quando avviato, esegue l'hash dei dati, prende l'ultimo blocco della catena così da poter ricavarne l'hash, e crea un blocco con questi 2 parametri. Per via del funzionamento della BlockChain è possibile che i Miners non trovino il nonce in ordine sequenziale a quello in cui sono stati iniziati, per questo motivo, se non dovesse avere successo l'inserimento del blocco, il Miner ricomincia a cercare il nonce, in questo modo si implementa anche il caso in cui il Miner "perda" alla gare nella ricerca del nonce.

Il main è contenuto nella classe Chain, richiede 3 Stringhe e le inserisce nella BlockChain, successivamente restituisce i tempi di inserimento dei blocchi e gli hash dei vari blocchi inseriti (non restituisce il primo che è generato casualmente). Un ulteriore miglioramento potrebbe essere effettuato creando una classe User che, in comunicazione con i Miners, da loro dei dati, su cui lavorare per creare un nuovo blocco, implementando anche la crittografia. Tuttavia per mancanza di tempo e della capacità di pensare ad una soluzione elegante per gestire la comunicazione fra Miners e Users ho preferito evitare.

Grazie a questo progetto ho imparato a conoscere meglio le funzioni di hash, i Thread, e soprattutto i tipi di dati, infatti problemi con questi ultimi mi hanno tenuto bloccato per svariati giorni senza avanzare, rendendo lo sviluppo molto frustrante in alcuni momenti, tuttavia il superamento di questi problemi mi ha portato a sviluppare un lavoro di cui sono fiero, e che credo sommi bene le mie conoscenze delle tecnologie BlockChain. La versione 1.0, che implementava solo le funzioni basilari richieste dall'esercitazione, può essere inviata su richiesta.