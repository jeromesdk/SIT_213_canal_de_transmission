package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import simulateur.*;
import transmetteurs.*;

class SimulateurTest {

	@Test
	void testCalculTauxErreurBinaire() throws Exception{

		//transmetteurLogique
		Simulateur simulateur =  new Simulateur(new String[] {"-mess","1010010001"});
		simulateur.execute();
		assertEquals( 0.0 , simulateur.calculTauxErreurBinaire() , "Le taux d'erreur binaire n'est pas de 0");

		//Nous ne pouvons pas tester un cas ou le taux d'erreur binaire est différet de 0 car nous n'avons qu'un transmetteur parfait à disposition
	}

	@Test
	void testAnalyseArguments()  throws  ArgumentsException {
		Simulateur simulateur1 =  new Simulateur(new String[] {"-mess","1010010001", "-s"});
		assertEquals(simulateur1.getMessageAleatoire()  , false , "Le message est détecté comme aléatoire alors qu'il ne le devrait pas");
		assertEquals(simulateur1.getAffichage()  , true , "L'afficheur n'est pas actif alors qu'il devrait l'être");
		assertEquals(simulateur1.getAleatoireAvecGerme()  , false , "La seed est active alors qu'elle ne devrait pas l'être");
		
		Simulateur simulateur2 =  new Simulateur(new String[] {"-seed","35","-s"});
		assertEquals(simulateur2.getMessageAleatoire()  , true , "Le message est détecté comme non aléatoire alors qu'il devrait l'être");
		assertEquals(simulateur2.getAffichage()  , true , "L'afficheur est actif alors qu'il ne devrait pas l'être");
		assertEquals(simulateur2.getAleatoireAvecGerme()  , true , "La seed n'est pas active alor qu'elle devrait l'être");
		
		Simulateur simulateur3 =  new Simulateur(new String[0]);
		assertEquals(simulateur3.getMessageAleatoire()  , true , "Le message est détecté comme non aléatoire alors qu'il devrait l'être");
		assertEquals(simulateur3.getAffichage()  , false , "L'afficheur est actif alors qu'il ne devrait pas l'être");
		assertEquals(simulateur3.getAleatoireAvecGerme()  , false , "La seed est active alors qu'elle ne devrait pas l'être");
		
		Simulateur simulateur4 =  new Simulateur(new String[] {"-mess","47"});
		assertEquals(simulateur4.getMessageAleatoire()  , true , "Le message est détecté comme non aléatoire alors qu'il devrait l'être");
		assertEquals(simulateur4.getAffichage()  , false , "L'afficheur est actif alors qu'il ne devrait pas l'être");
		assertEquals(simulateur4.getAleatoireAvecGerme()  , false , "La seed est active alors qu'elle ne devrait pas l'être");
	}
	
	@Test
	void testExecute() throws Exception{
		Simulateur simulateur1 =  new Simulateur(new String[] {"-mess","1010010001"});
		simulateur1.execute();
		assertEquals(simulateur1.getTailleMotDestination()  , 10 , "Le mot recue ne peu pas être bon car il ne fait pas la bonne taille");
	}

}

