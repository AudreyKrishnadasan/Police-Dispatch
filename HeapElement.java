package project3;

public class HeapElement {

	Incident value = null;

	public HeapElement(Incident incident) {
		this.value = incident;
	}

	public String toString() {
		return this.value.toString();
	}

}
