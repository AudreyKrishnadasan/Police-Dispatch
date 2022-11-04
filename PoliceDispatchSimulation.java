package project3;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;

public class PoliceDispatchSimulation {

	private boolean[] availableUnits;
	private MinHeap triageQueue;
	private MinHeap eventQueue;

	/**
	 * Create a new simulation.
	 *
	 * @param numUnits The number of units available in this simulation.
	 */
	public PoliceDispatchSimulation(int numUnits) {
		if (numUnits < 1) {
			throw new IllegalArgumentException("There must be at least one unit to dispatch.");
		}
		this.availableUnits = new boolean[numUnits];
		this.triageQueue = new MinHeap();
		this.eventQueue = new MinHeap();
		for (int i = 0; i < numUnits; i++) {
			this.markUnitAvailable(i);
		}
	}

	/**
	 * Add an incident to the incident queue.
	 *
	 * @param incident The incident to add.
	 */
	public void addToIncidentQueue(Incident incident) {
		this.eventQueue.add(
			new HeapElement(incident), 
			incident.getTimePriority()
		);
	}

	/**
	 * Get the next available police unit.
	 *
	 * @return The ID (int) of the next available police unit.
	 */
	private int nextAvailableUnit() {
		for (int i = 0; i < this.availableUnits.length; i++) {
			if (this.availableUnits[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Mark a unit as unavailable (ie. after it has been dispatched)
	 *
	 * @param unit The int ID of the unit.
	 */
	private void markUnitUnavailable(int unit) {
		this.availableUnits[unit] = false;
	}

	/**
	 * Mark a unit as available (ie. after it has resolved an incident)
	 *
	 * @param unit The int ID of the unit.
	 */
	private void markUnitAvailable(int unit) {
		this.availableUnits[unit] = true;
	}

	/**
	 * Print out that an incident has been reported.
	 *
	 * @param reportIncident The Incident describing the report.
	 */
	private void logReport(int time, Incident reportIncident) {
		System.out.println(
			"Time " + time + ": " + reportIncident.getIncidentTypeString() + " reported"
			+ " (duration: " + reportIncident.getDuration() + ")"
		);
	}

	/**
	 * Print out that a unit has been dispatched to an incident.
	 *
	 * @param time The current time.
	 * @param dispatchedIncident The Incident describing the dispatch.
	 */
	private void logDispatch(int time, Incident dispatchIncident) {
		System.out.println(
			"Time " + time + ": unit " + dispatchIncident.getDispatchedUnit() + " dispatched to the " 
			+ dispatchIncident.getIncidentTypeString() + " reported at time " + dispatchIncident.getReportTime() + ";"
			+ " will be resolved at time " + (dispatchIncident.getDispatchTime() + dispatchIncident.getDuration())
		);
	}

	/**
	 * Print out that an incident has been resolved.
	 *
	 * @param time The current time.
	 * @param dispatchedIncident The Incident describing the dispatch.
	 */
	private void logResolution(int time, Incident dispatchIncident) {
		System.out.println(
			"Time " + time + ": unit " + dispatchIncident.getDispatchedUnit() + " resolved the " 
			+ dispatchIncident.getIncidentTypeString() + " reported at time " + dispatchIncident.getReportTime()
		);
	}

	private boolean availUnits() {
		boolean availUnits = false;
		for(int j = 0; j < this.availableUnits.length; j++) {
			if(this.availableUnits[j] == true) {
				availUnits = true;
			}
		}
		return availUnits;
	}
	
	/**
	 * Run the simulation
	 */
	public void run() {
		// FIXME
		int time = 0;
		while(eventQueue.size() != 0) {
			//REPORT
			if(eventQueue.peek() == null) {
				break;
			}	
			ArrayList<HeapElement> resolutions = new ArrayList<HeapElement>();
			ArrayList<HeapElement> reports = new ArrayList<HeapElement>();
			HeapElement i = eventQueue.peek();
			
			 // sort into resolutions and reports at that time stamp
			while(i.value.getReportTime() == time || i.value.getResolutionTime() == time) {
				if(i.value.getResolutionTime() > 0) {
					resolutions.add(eventQueue.poll());
				}
				else {
					reports.add(eventQueue.poll());
				}
				i = eventQueue.peek();
				if(i == null) {
					break;
				}
			}	
			 
			//do the reports
			for(int j = 0; j < reports.size(); j++) {
				HeapElement d = reports.get(j);
				triageQueue.add(d, d.value.getTriagePriority());
				logReport(time,d.value);	
			}
				
			//RESOLUTIONS
			for(int j = 0; j < resolutions.size(); j++){
				Incident done = resolutions.get(j).value;
				markUnitAvailable(done.getDispatchedUnit());
				logResolution(time,done);
			}
			
			//DISPATCH !!		
			while(availUnits() && triageQueue.size() > 0) {
				HeapElement t = triageQueue.poll();
				int unitNum = this.nextAvailableUnit();
				t.value.dispatch(time, unitNum);
				markUnitUnavailable(unitNum);				
				logDispatch(time,t.value);				
				eventQueue.add(t, t.value.getResolutionTime());	
			}	
			
			time++;
		}
		
	}

	public static void main(String[] args) {
		// A test case for the simulation. The correct output is after the code.

		 PoliceDispatchSimulation sim = new PoliceDispatchSimulation(4);
	        sim.addToIncidentQueue(new Incident(339, IncidentType.ROBBERY, 97));
	        sim.addToIncidentQueue(new Incident(331, IncidentType.TRAFFIC_COLLISION, 97));
	        sim.addToIncidentQueue(new Incident(314, IncidentType.ROBBERY, 33));
	        sim.addToIncidentQueue(new Incident(301, IncidentType.TRAFFIC_COLLISION, 46));
	        sim.addToIncidentQueue(new Incident(294, IncidentType.ROBBERY, 80));
	        sim.addToIncidentQueue(new Incident(280, IncidentType.WELLNESS_CHECK, 21));
	        sim.addToIncidentQueue(new Incident(280, IncidentType.MURDER, 118));
	        sim.addToIncidentQueue(new Incident(274, IncidentType.ROBBERY, 96));
	        sim.addToIncidentQueue(new Incident(273, IncidentType.TRAFFIC_COLLISION, 53));
	        sim.addToIncidentQueue(new Incident(272, IncidentType.WELLNESS_CHECK, 58));
	        sim.addToIncidentQueue(new Incident(272, IncidentType.ROBBERY, 119));
	        sim.addToIncidentQueue(new Incident(269, IncidentType.WELLNESS_CHECK, 16));
	        sim.addToIncidentQueue(new Incident(261, IncidentType.MURDER, 80));
	        sim.addToIncidentQueue(new Incident(253, IncidentType.ROBBERY, 76));
	        sim.addToIncidentQueue(new Incident(242, IncidentType.TRAFFIC_COLLISION, 45));
	        sim.addToIncidentQueue(new Incident(236, IncidentType.MURDER, 45));
	        sim.addToIncidentQueue(new Incident(230, IncidentType.WELLNESS_CHECK, 30));
	        sim.addToIncidentQueue(new Incident(228, IncidentType.ROBBERY, 24));
	        sim.addToIncidentQueue(new Incident(212, IncidentType.MURDER, 53));
	        sim.addToIncidentQueue(new Incident(205, IncidentType.TRAFFIC_COLLISION, 80));
	        sim.addToIncidentQueue(new Incident(204, IncidentType.TRAFFIC_COLLISION, 71));
	        sim.addToIncidentQueue(new Incident(193, IncidentType.TRAFFIC_COLLISION, 81));
	        sim.addToIncidentQueue(new Incident(191, IncidentType.TRAFFIC_COLLISION, 118));
	        sim.addToIncidentQueue(new Incident(181, IncidentType.MURDER, 105));
	        sim.addToIncidentQueue(new Incident(179, IncidentType.WELLNESS_CHECK, 16));
	        sim.addToIncidentQueue(new Incident(171, IncidentType.WELLNESS_CHECK, 103));
	        sim.addToIncidentQueue(new Incident(158, IncidentType.TRAFFIC_COLLISION, 42));
	        sim.addToIncidentQueue(new Incident(125, IncidentType.TRAFFIC_COLLISION, 92));
	        sim.addToIncidentQueue(new Incident(121, IncidentType.WELLNESS_CHECK, 118));
	        sim.addToIncidentQueue(new Incident(117, IncidentType.TRAFFIC_COLLISION, 109));
	        sim.addToIncidentQueue(new Incident(96, IncidentType.ROBBERY, 64));
	        sim.addToIncidentQueue(new Incident(70, IncidentType.MURDER, 38));
	        sim.addToIncidentQueue(new Incident(53, IncidentType.MURDER, 24));
	        sim.addToIncidentQueue(new Incident(52, IncidentType.TRAFFIC_COLLISION, 61));
	        sim.addToIncidentQueue(new Incident(42, IncidentType.ROBBERY, 99));
	        sim.addToIncidentQueue(new Incident(41, IncidentType.TRAFFIC_COLLISION, 89));
	        sim.addToIncidentQueue(new Incident(28, IncidentType.TRAFFIC_COLLISION, 41));
	        sim.addToIncidentQueue(new Incident(25, IncidentType.TRAFFIC_COLLISION, 62));
	        sim.addToIncidentQueue(new Incident(16, IncidentType.MURDER, 58));
	        sim.addToIncidentQueue(new Incident(10, IncidentType.ROBBERY, 108));
	        sim.run();
		 
	        
	}

}