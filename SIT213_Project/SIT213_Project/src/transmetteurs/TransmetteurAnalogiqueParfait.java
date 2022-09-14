package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurAnalogiqueParfait extends Transmetteur<Float, Float> {

	public void recevoir(Information <Float> information) throws InformationNonConformeException{

		informationRecue = information;
	}

	/**
	 * émet l'information construite par le transmetteur
	 */
	public void emettre() throws InformationNonConformeException{

		for (DestinationInterface<Float> recepteurConnectee : destinationsConnectees) {
			recepteurConnectee.recevoir(informationRecue);
		}
		informationEmise = informationRecue;   	
	}
}
