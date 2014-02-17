package sdp.group2.strategy;

public class CircularBuffer<T> {

	private T[] buffer = null;
	private int lo = 0;
	private int hi = 0;
	private int capacity;
	
	@SuppressWarnings("unchecked")
	public CircularBuffer(int capacity)
	{
		this.capacity = capacity;
		buffer = (T[]) new Object[capacity];
	}
	
	public int size()
	{ return hi - lo; }
	
	public T dequeue() throws EmptyBufferException
	{
		if (size() > 0)
		{ 
			T value = buffer[lo];
			lo %= lo + 1;
			return value;
		}
		else
		{ throw new EmptyBufferException("Tried to obtain an item from the empty buffer."); }
	}
	
	public void enqueue(T item) throws FullBufferException
	{
		if (size() == capacity)
		{ throw new FullBufferException("Tried to add an item to the full buffer."); }
		else
		{
			buffer[hi] = item;
			hi %= hi + 1;
		}
	}
	
}
