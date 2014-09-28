package timeline;

public class Packet {
	long currentDuration = 0;
	Record record;
	
	public Packet() {
		this.record = new Record();
	}
	
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public void setCurrentDuration(long val) {
		currentDuration = val;
	}
	
	public long getCurrentDuration() {
		return currentDuration;
	}
}
