package signaux;


import information.Information;

public abstract class Signal<R,E> {
	
	protected Information<E> signalEntree;
	
	protected Information<R> signalsorieInformation;
	
	public Signal() {
		signalEntree = new Information<E>();
	}
	
	public Signal(Information<E> informationRecue) {
		signalEntree = informationRecue;
	}
	
	public abstract Information<R> generer();
}
