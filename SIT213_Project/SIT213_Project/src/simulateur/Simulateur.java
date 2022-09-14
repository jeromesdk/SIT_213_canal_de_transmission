package simulateur;//54634452

import destinations.Destination;
import destinations.DestinationFinale;
import information.Information;
import sources.*;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurParfait;
import visualisations.*;
import information.*;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.Random;
import java.util.stream.IntStream;

/** La classe Simulateur permet de construire et simuler une chaîne de
 * transmission composée d'une Source, d'un nombre variable de
 * Transmetteur(s) et d'une Destination.
 * @author cousin
 * @author prou
 *
 */
public class Simulateur {
//hfgg
	private boolean SignalNRZ = false;

	private boolean SignalNRZT = false;

	private boolean SignalRZ = false;

	private int nbEch=30;

	private float min=0.0f;

	private float max=1.0f;


	/** indique si le Simulateur utilise des sondes d'affichage */
	private boolean affichage = false;

	/** indique si le Simulateur utilise un message généré de manière aléatoire (message imposé sinon) */
	private boolean messageAleatoire = true;

	/** indique si le Simulateur utilise un germe pour initialiser les générateurs aléatoires */
	private boolean aleatoireAvecGerme = false;

	/** la valeur de la semence utilisée pour les générateurs aléatoires */
	private Integer seed = 0; // pas de semence par défaut

	/** la longueur du message aléatoire à transmettre si un message n'est pas imposé */
	private int nbBitsMess = 100;

	/** la chaîne de caractères correspondant à m dans l'argument -mess m */
	private String messageString = "100";


	/** le  composant Source de la chaine de transmission */
	private Source <Boolean>  source = null;

	/** le  composant Transmetteur parfait logique de la chaine de transmission */
	private Transmetteur <Boolean, Boolean>  transmetteurLogique = null;

	/** le  composant Destination de la chaine de transmission */
	private Destination <Boolean>  destination = null;



	/**
	 * Un simple getter qui renvoie la taille du mot  reçu à la destiation
	 * @return int 
	 * 			la longueur du mot reçu
	 */
	public int getTailleMotDestination(){
		return destination.getLongueurInformationRecue();
	}

	/**
	 * Un simple getter qui renvoie un booléen disant si le message est aléatoire ou non
	 * @return -boolean
	 * 			vrai si le message est aléatoire. Faux sinon
	 */
	public boolean getMessageAleatoire() {
		return messageAleatoire;
	}

	/**
	 * Un simple getter qui renvoie un booleéen disant si le message a une germe ou non
	 * @return -boolean
	 * 			vrai si le message contient une germe. Faux sinon
	 */
	public boolean getAleatoireAvecGerme() {
		return aleatoireAvecGerme;
	}

	/**
	 * Un simple getter qui renvoie un booleéen disant si les sondes sont actives
	 * @return -boolean
	 * 			vrai si les sondes sont actives. Faux sinon
	 */
	public boolean getAffichage() {
		return affichage;
	}
	/** Le constructeur de Simulateur construit une chaîne de
	 * transmission composée d'une Source <Boolean>, d'une Destination
	 * <Boolean> et de Transmetteur(s) [voir la méthode
	 * analyseArguments]...  <br> Les différents composants de la
	 * chaîne de transmission (Source, Transmetteur(s), Destination,
	 * Sonde(s) de visualisation) sont créés et connectés.
	 * @param args le tableau des différents arguments.
	 *
	 * @throws ArgumentsException si un des arguments est incorrect
	 *
	 */  
	public  Simulateur(String [] args) throws ArgumentsException {
		// analyser et récupérer les arguments   	
		analyseArguments(args);

		if(messageAleatoire == false) {
			source = new SourceFixe(messageString);    		
		}

		else if(aleatoireAvecGerme == true) {
			source = new SourceAleatoire(nbBitsMess, seed);
		}
		else {
			source = new SourceAleatoire(nbBitsMess,0);

		}
		//initialisation des equipements
		transmetteurLogique = new TransmetteurParfait();
		destination = new DestinationFinale();
		// ajout des sondes source et transmetteur si affichage est vrai
		if (affichage) {
			source.connecter(new SondeLogique("Source", 200));
			transmetteurLogique.connecter(new SondeLogique("Transmetteur", 200));

		}		
		//connexion des equipements entre eux
		source.connecter(transmetteurLogique);
		transmetteurLogique.connecter(destination);

	}



	/** La méthode analyseArguments extrait d'un tableau de chaînes de
	 * caractères les différentes options de la simulation.  <br>Elle met
	 * à jour les attributs correspondants du Simulateur.
	 *
	 * @param args le tableau des différents arguments.
	 * <br>
	 * <br>Les arguments autorisés sont : 
	 * <br> 
	 * <dl>
	 * <dt> -mess m  </dt><dd> m (String) constitué de 7 ou plus digits à 0 | 1, le message à transmettre</dd>
	 * <dt> -mess m  </dt><dd> m (int) constitué de 1 à 6 digits, le nombre de bits du message "aléatoire" à transmettre</dd> 
	 * <dt> -s </dt><dd> pour demander l'utilisation des sondes d'affichage</dd>
	 * <dt> -seed v </dt><dd> v (int) d'initialisation pour les générateurs aléatoires</dd> 
	 * </dl>
	 *
	 * @throws ArgumentsException si un des arguments est incorrect.
	 *
	 */   
	public void analyseArguments(String[] args)  throws  ArgumentsException {

		for (int i=0;i<args.length;i++){ // traiter les arguments 1 par 1

			if (args[i].matches("-s")){
				affichage = true;
			}

			else if (args[i].matches("-seed")) {
				aleatoireAvecGerme = true;
				i++; 
				// traiter la valeur associee
				try { 
					seed = Integer.valueOf(args[i]);
				}
				catch (Exception e) {
					throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
				}           		
			}

			else if (args[i].matches("-mess")){
				i++; 
				// traiter la valeur associee

				if (args[i].matches("[0,1]{7,}")) { // au moins 7 digits entre 1 et 0
					messageAleatoire = false;
					nbBitsMess = args[i].length();
					messageString=args[i];
				} 
				else if (args[i].matches("[0-9]{1,6}")) { // de 1 à 6 chiffres
					messageAleatoire = true;
					nbBitsMess = Integer.valueOf(args[i]);
					if (nbBitsMess < 1) 
						throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
				}
				else 
					throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
			}

			else if(args[i].matches("-mess")){
				i++; 

				if (args[i].matches("NRZ")) { 
					SignalNRZ = true;
				} 
				else if (args[i].matches("NRZT")) { 
					SignalNRZT = true;
				}
				else if (args[i].matches("RZ")) { 
					SignalRZ = true;
				}		
			}


			else if(args[i].matches("nbEch")){
				i++; 		
				nbEch=Integer.valueOf(args[i]);
			}	



			else if(args[i].matches("ampl")){
				i++; 
				try {
					min=Integer.valueOf(args[i]);
					i++; 
					min=Integer.valueOf(args[i]);
				}
				catch (Exception e) {
					System.out.println("erreur dans les parmètres "); 
					System.exit(-1);
				} 

			}




			else throw new ArgumentsException("Option invalide :"+ args[i]);	
		}


	}



	/** La méthode execute effectue un envoi de message par la source
	 * de la chaîne de transmission du Simulateur.
	 *
	 * @throws Exception si un problème survient lors de l'exécution
	 *
	 */ 
	public void execute() throws Exception {      
		source.emettre();
		transmetteurLogique.emettre();
	}



	/** La méthode qui calcule le taux d'erreur binaire en comparant
	 * les bits du message émis avec ceux du message reçu.
	 *
	 * @return  La valeur du Taux dErreur Binaire.
	 */   	   
	public float  calculTauxErreurBinaire() {

		Information <Boolean> chaineEmise = source.getInformationEmise();
		Information <Boolean> chaineRecue = destination.getInformationRecue();
		int nbVariablesDifferentes = 0;
		for(int indice = 0; indice < source.getInformationEmise().nbElements(); indice++) {
			if ((chaineEmise.iemeElement(indice) != chaineRecue.iemeElement(indice))) {
				nbVariablesDifferentes += 1;
			}
		}
		return  nbVariablesDifferentes/source.getInformationEmise().nbElements();
	}



	/** La fonction main instancie un Simulateur à l'aide des
	 *  arguments paramètres et affiche le résultat de l'exécution
	 *  d'une transmission.
	 *  @param args les différents arguments qui serviront à l'instanciation du Simulateur.
	 */
	public static void main(String [] args) { 

		Simulateur simulateur = null;

		try {
			simulateur = new Simulateur(args);
		}
		catch (Exception e) {
			System.out.println(e); 
			System.exit(-1);
		} 

		try {
			simulateur.execute();
			String s = "java  Simulateur  ";
			for (int i = 0; i < args.length; i++) { //copier tous les paramètres de simulation
				s += args[i] + "  ";
			}
			System.out.println(s + "  =>   TEB : " + simulateur.calculTauxErreurBinaire());
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-2);

		}   

	}
}

