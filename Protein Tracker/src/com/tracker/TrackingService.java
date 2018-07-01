package com.tracker;

import java.util.ArrayList;
import java.util.List;

public class TrackingService {

	private int total;
	private int goal;
	private List<HistoryItem> history = new ArrayList<>();
	private int historyId = 0;
	private Notifier notifier;
	
	public TrackingService(Notifier notifier)
	{
		this.notifier = notifier;
	}
	
	public void addProtein(int amount) {
		total += amount;
		String historyMessage = null;
		history.add(new HistoryItem(historyId++, amount, "add", total));
		if(total > goal)
		{
			boolean sendResult = notifier.send("Goal Met!");
			historyMessage = "sent:goal met";
			if(!sendResult)
				historyMessage = "Send_Error: Goal Met!";
			history.add(new HistoryItem(historyId++, 0, "sent:goal met", total));
			
		}	
	}

	public void removeProtein(int amount) {
		total -= amount;
		if (total < 0) {
			total = 0;
		}
		history.add(new HistoryItem(historyId++, amount, "substract", total));
	}

	public int getTotal() {
		return total;
	}

	public void setGoal(int value) throws InvalidGoalException {
		if (value<0) {
			throw new InvalidGoalException("Goal was less than zero.");
		}
		goal = value;
	}

	public boolean isGoalMet() {
		return total >= goal;
	}

	public List<HistoryItem> getHistory() {
		return history;
	}
}
