package sdp.group2.strategy;

import java.util.LinkedList;

public class PID {
	
	/* Keeps history of previously calculated errors, which are expected - actual */
	private LinkedList<Double> errorHistory = new LinkedList<Double>();
	private int historyLength = 10;
	private double p = 1/100;
	private double i = 1/50000;
	private double d = 3/10;
	private double expected;
	
	public PID(double expected, double p, double i, double d, int historyLength) {
		this(expected, p, i, d);
		this.historyLength = historyLength;
	}
	
	public PID(double expected, double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
		this.expected = expected;
	}
	
	public PID(double expected) {
		this.expected = expected;
	}
	
	public double getAdjustment(double actual) {
		double error = expected - actual;
		addToHistory(error);
		
		double proportional = p * error; 
		double integral = i * trapzHistory();
		double derivative = d * diffHistory();
		
		return proportional + integral + derivative;
	}
	
	private void addToHistory(double error) {
		errorHistory.add(error);
		if (errorHistory.size() > historyLength) {
			errorHistory.remove(errorHistory.size() - 1);
		}
	}
	
	/* Integrate the values in the current history */
	private double trapzHistory() {
		double sum = 0.0;
		for (int i = 1; i < errorHistory.size(); i++) {
			sum += 0.5 * (errorHistory.get(i - 1) + errorHistory.get(i));
		}
		return sum / errorHistory.size();
	}
	
	/* Compute the derivative over the current history interval */
	private double diffHistory() {
		LinkedList<Double> diff = new LinkedList<Double>();
		for (int i = 1; i < errorHistory.size(); i++) {
			diff.add(errorHistory.get(i) + errorHistory.get(i - 1));
		}
		
		double sum = 0.0;
		for (int i = 0; i < diff.size(); i++) {
			sum += diff.get(i);
		}
		
		return sum / diff.size();
	}
	
}
