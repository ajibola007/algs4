/**
 * Created by petergoldsborough on 08/18/15.
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/*
public class BruteCollinearPoints {
   public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
   public           int numberOfSegments()        // the number of line segments
   public LineSegment[] segments()                // the line segments
}
 */

public class BruteCollinearPoints
{
	public BruteCollinearPoints(Point[] source)
	{
		if (source == null) throw new java.lang.NullPointerException();

		Point[] points = new Point[source.length];

		for (int i = 0; i < points.length; ++i)
		{
			points[i] = source[i];
		}

		check(points);

		find(points);
	}

	public int numberOfSegments()
	{
		return segments.length;
	}

	public LineSegment[] segments()
	{
		LineSegment[] copy = new LineSegment[segments.length];

		for (int i = 0; i < segments.length; ++i)
		{
			copy[i] = segments[i];
		}

		return copy;
	}

	public static void main(String[] args)
	{
		In input = new In(args[0]);

		int N = input.readInt();

		Point[] points = new Point[N];

		for (int i = 0; i < N; ++i)
		{
			int x = input.readInt();
			int y = input.readInt();

			points[i] = new Point(x, y);
		}

		StdDraw.show(0);

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);

		for (Point p : points) p.draw();

		StdDraw.show();

		BruteCollinearPoints brute = new BruteCollinearPoints(points);

		LineSegment[] s = brute.segments();

		s[0] = new LineSegment(new Point(0, 0), new Point(10, 10));

		LineSegment[] t = brute.segments();

		boolean equals = s[0] == t[0];

		System.out.println(equals);

		for (LineSegment segment : brute.segments())
		{
			StdOut.println(segment);
			segment.draw();
		}
	}

	private static void sort(Comparable[] sequence)
	{
		int h = 1;
		while (h < sequence.length/3) h = 3 * h + 1;

		while (h >= 1)
		{
			for (int i = h; i < sequence.length; i += h)
			{
				Comparable item = sequence[i];

				int j = i - h;

				for ( ; j >= 0 && item.compareTo(sequence[j]) < 0; j -= h)
				{
					sequence[j + h] = sequence[j];
				}

				sequence[j + h] = item;
			}

			h /= 3;
		}
	}

	private void find(Point[] points)
	{
		Stack<LineSegment> stack = new Stack<>();

		for (int i = 0; i < points.length; ++i)
		{
			Point source = points[i];

			for (int j = i + 1; j < points.length; ++j)
			{
				for (int k = j + 1; k < points.length; ++k)
				{
					for (int l = k + 1; l < points.length; ++l)
					{
						double slopeJ = source.slopeTo(points[j]);
						double slopeK = source.slopeTo(points[k]);
						double slopeL = source.slopeTo(points[l]);

						if (slopeJ == slopeK && slopeK == slopeL)
						{
							Point[] sorted = getSorted(points, i, j, k, l);

							stack.push(new LineSegment(sorted[0], sorted[3]));
						}
					}
				}
			}
		}

		segments = new LineSegment[stack.size()];

		for (int i = 0; i < segments.length; ++i)
		{
			segments[i] = stack.pop();
		}
	}

	private void check(Point[] points)
	{
		sort(points);

		for (int i = 0, end = points.length - 1; i < end; ++i)
		{
			if (points[i] == null)
			{
				throw new NullPointerException();
			}

			if (points[i].compareTo(points[i + 1]) == 0)
			{
				throw new IllegalArgumentException();
			}
		}
	}

	private Point[] getSorted(Point[] points, int a, int b, int c, int d)
	{
		Point[] sorted = new Point[4];

		sorted[0] = points[a];
		sorted[1] = points[b];
		sorted[2] = points[c];
		sorted[3] = points[d];

		sort(sorted);

		return sorted;
	}

	private LineSegment[] segments;
}

class Stack<T>
{
	public static int MINIMUM_CAPACITY = 8;

	public Stack()
	{
		data = (T[]) new Object[MINIMUM_CAPACITY];
	}

	public void push(T item)
	{
		data[size] = item;

		if (++size == data.length) resize();
	}

	public T top()
	{
		if (size == 0) throw new NoSuchElementException();

		return data[size - 1];
	}

	public T pop()
	{
		if (size == 0) throw new NoSuchElementException();

		T value = data[--size];

		if (size == data.length/4) resize();

		return value;
	}

	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	private void resize()
	{
		int capacity = size * 2;

		if (capacity < MINIMUM_CAPACITY) return;

		T[] old = data;

		data = (T[]) new Object[capacity];

		for (int i = 0; i < size; ++i)
		{
			data[i] = old[i];
		}

		old = null;
	}


	private T[] data;

	private int size = 0;
}