package emetteur;


import destinations.DestinationInterface;
import destinations.*;
import information.*;
import transmetteurs.*;
import signaux.*;

public class EmetteurAnalogique extends Transmetteur<Boolean, Float>{

	
	
	public EmetteurAnalogique(String typeEmetteur) {
		
		if(typeEmetteur.equalsIgnoreCase("RZ")) {
			Signal<Boolean,Float> signal = new SignalRZ<>(informationRecue);
			informationEmise = signal.generer();
		}
		else if(typeEmetteur.equalsIgnoreCase("NRZ")) {
			Signal<Boolean,Float> signal = new SignalNRZ<>(informationRecue);
			informationEmise = signal.generer();
		}
		else if(typeEmetteur.equalsIgnoreCase("NRZT")) {
			Signal<Boolean,Float> signal = new SignalNRZT<>(informationRecue);
			informationEmise = signal.generer();
		}
		else {
			throw new InformationNonConformeException("Vous devez spécifier RZ, NRZ ou NRZT en"
					+ " fonction du type de signal à émmettre");
		}
		
		
	}

	public  void recevoir(Information <Boolean> information) throws InformationNonConformeException{
		informationRecue = information;
		this.emettre();
	}

	/*
	 * émet l'information construite par le transmetteur
	 */
	public void emettre() throws InformationNonConformeException{



		for (DestinationInterface<Float> transmetteurConnectee : destinationsConnectees) {
			transmetteurConnectee.recevoir(informationEmise);
		}

	}
}
