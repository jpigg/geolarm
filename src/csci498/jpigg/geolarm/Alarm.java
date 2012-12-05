package csci498.jpigg.geolarm;

public class Alarm {
	private String alarmId = "";
	private String name = "";
	private String description = "";
	//location will probably need to change from a string
	private String location = "";
	private int isActive = 0;
	private int useLocation = 0;
	private int hour = 0;
	private int minute = 0;
	
	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public Alarm() {
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getUseLocation() {
		return useLocation;
	}

	public void setUseLocation(int useLocation) {
		this.useLocation = useLocation;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
	

}
