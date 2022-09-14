package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurParfait extends Transmetteur<Boolean, Boolean>{

	public void recevoir(Information <Boolean> information) throws InformationNonConformeException{

		informationRecue = information;
	}

	/**
	 * émet l'information construite par le transmetteur
	 */
	public void emettre() throws InformationNonConformeException{

		for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationRecue);
		}
		informationEmise = informationRecue;   	
	}

}
