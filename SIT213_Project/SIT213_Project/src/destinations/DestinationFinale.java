package destinations;

import information.Information;
import information.InformationNonConformeException;

public class DestinationFinale extends Destination <Boolean>{

	
	public void recevoir(Information <Boolean> information) throws InformationNonConformeException{
		
		informationRecue = information;
	}
	

}