package project3;

public class Incident {

	private int reportTime;
	private IncidentType incidentType;
	private int duration;
	private int dispatchTime;
	private int dispatchedUnit;

	/**
	 * Constructor for incidents that have been reported, but does not have a unit dispatched to it.
	 *
	 * @param reportTime The time the incident was initially reported.
	 * @param incidentType The type of incident: wellness check, traffic collision, etc.
	 * @param duration The time it will take for a unit to deal with this incident.
	 */
	public Incident(int reportTime, IncidentType incidentType, int duration) {
		this.reportTime = reportTime;
		this.incidentType = incidentType;
		this.duration = duration;
		this.dispatchTime = -1;
		this.dispatchedUnit = -1;
	}

	/**
	 * Update this incident with who has been dispatched to it.
	 *
	 * @param currentTime The current time in the simulation.
	 * @param unit The unit that was dispatched to the incident.
	 */
	public void dispatch(int currentTime, int unit) {
		this.dispatchTime = currentTime;
		this.dispatchedUnit = unit;
	}

	/**
	 * Get whether a unit has been dispatched to this incident.
	 *
	 * @return True if a unit has been dispatched to this incident.
	 */
	public boolean dispatched() {
		return this.dispatchTime != -1 && this.dispatchedUnit != -1;
	}

	/**
	 * Get the time this incident was reported.
	 *
	 * @return The time this incident was reported.
	 */
	public int getReportTime() {
		return this.reportTime;
	}

	/**
	 * Get how long dealing with this incident will take.
	 *
	 * @return The duration this incident will take.
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * Get the type of this incident.
	 *
	 * @return The type of this incident.
	 */
	public IncidentType getIncidentType() {
		return this.incidentType;
	}

	/**
	 * Get the time a unit was dispatched to this incident.
	 *
	 * Will return -1 if no unit has yet been dispatched.
	 *
	 * @return The time a unit was dispatched to this incident.
	 */
	public int getDispatchTime() {
		return this.dispatchTime;
	}

	/**
	 * Get the unit that was dispatched to this incident.
	 *
	 * Will return -1 if no unit has yet been dispatched.
	 *
	 * @return The unit that was dispatched to this incident.
	 */
	public int getDispatchedUnit() {
		return this.dispatchedUnit;
	}

	/**
	 * Calculate the resolution time, if a unit has been dispatched.
	 *
	 * @return The time the incident will be resolved (if a unit has been dispatched); -1 otherwise.
	 */
	public int getResolutionTime() {
		if (this.dispatched()) {
			return this.dispatchTime + this.duration;
		} else {
			return -1;
		}
	}

	/**
	 * Get the time when this incident should be processed, depending on whether it's a report or a dispatch.
	 *
	 * @return The time the incident will need to be processed.
	 */
	public int getTimePriority() {
		if (this.dispatched()) {
			return this.getResolutionTime();
		} else {
			return this.reportTime;
		}
	}

	/**
	 * Get the triage priority of this incident, based on its type and report time.
	 *
	 * @return The triage priority of the incident.
	 */
	public int getTriagePriority() {
		// assumes the time will never be > 200,000
		int multiplier = 0;
		if (this.incidentType == IncidentType.WELLNESS_CHECK) {
			multiplier = 3;
		} else if (this.incidentType == IncidentType.TRAFFIC_COLLISION) {
			multiplier = 2;
		} else if (this.incidentType == IncidentType.ROBBERY) {
			multiplier = 1;
		} else if (this.incidentType == IncidentType.MURDER) {
			multiplier = 0;
		}
		return multiplier * 200000 + this.reportTime;
	}

	/**
	 * Represent the incident type in a string.
	 *
	 * @return A string description of the incident type.
	 */
	public String getIncidentTypeString() {
		if (this.incidentType == IncidentType.WELLNESS_CHECK) {
			return "wellness check";
		} else if (this.incidentType == IncidentType.TRAFFIC_COLLISION) {
			return "traffic collision";
		} else if (this.incidentType == IncidentType.ROBBERY) {
			return "robbery";
		} else if (this.incidentType == IncidentType.MURDER) {
			return "murder";
		}
		return "";
	}

	/**
	 * Represent the incident in a string, for debugging purposes.
	 *
	 * @return The string representation.
	 */
	public String toString() {
		return ("Incident("
			+ this.reportTime + ", "
			+ this.getIncidentTypeString() + ", "
			+ this.duration + ", "
			+ this.dispatchTime + ", "
			+ this.dispatchedUnit
			+ ")");
	}

}
