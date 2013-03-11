package org.pmedv.blackboard.connections;

//
//  (C) 1997 Sergey Solyanik.
//
//  This program is not in public domain.
//
//  All rights to distribute are hereby granted under GNU Public License.
//
//  THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
//  ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//  PARTICULAR PURPOSE.
//
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StreamTokenizer;

class DebugException extends Error {
	public DebugException(String s) {
		super(s);
	}
}

class Debug {
	public static final boolean debug = false;
	public static final boolean report = false;
	public static final boolean report_inout = false;
	public static final boolean dump_in = false;
	public static final String dump_name = "pointx.txt";
	public static final boolean suppress_prints = true;
	public static final boolean suppress_errors = true;

	public static FeedBack f = null;

	public static final void println(String s) {
		if (!suppress_prints)
			System.out.println(s);
		else if (!suppress_errors)
			throw new Error(s);
		else if (f != null)
			f.report(s);
	}
}

class SyntaxError extends Exception {
	private String st;

	public SyntaxError(String s) {
		super(s);

		st = s;
	}

	public String toString() {
		return st;
	}
}

class NS {
	public static final double EPSILON = 1.0e-10;

	public static final double fuzzy(double alpha) {
		if ((alpha > -EPSILON) && (alpha < EPSILON))
			return 0.0;

		return alpha;
	}
}

class Point {
	public double x;
	public double y;

	public final int cmp(Point p2) {
		if ((NS.fuzzy(y - p2.y) > 0.0) || ((NS.fuzzy(y - p2.y) == 0.0) && (NS.fuzzy(x - p2.x) < 0.0)))
			return -1;

		if ((NS.fuzzy(y - p2.y) < 0.0) || ((NS.fuzzy(y - p2.y) == 0.0) && (NS.fuzzy(x - p2.x) > 0)))
			return 1;

		return 0;
	}

	public Point(double xs, double ys) {
		x = xs;
		y = ys;
	}

	public final String toString() {
		return "(" + x + ", " + y + ")";
	}
}

class Segment {
	//
	// Vertices within a segment are either top - down or left to right, as
	// defined in Point.cmp
	//
	public Point p1; // This is always an "upper" point according to Point.cmp
	public Point p2; // This is always a "lower" point according to Point.cmp

	public boolean z_len;

	private double a, b, c; // Equation of a line the segment is on

	public Segment(double x1, double y1, double x2, double y2) {
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);

		if (p1.cmp(p2) > 0) {
			Point swap = p1;
			p1 = p2;
			p2 = swap;

			x1 = p1.x;
			y1 = p1.y;
			x2 = p2.x;
			y2 = p2.y;
		}

		if (NS.fuzzy(x1 - x2) != 0.0) {
			b = 1.0;
			a = (y2 - y1) / (x1 - x2);
			c = -a * x1 - b * y1;

			//
			// This is here so that ax + by + c > 0 if (x, y) is to the left
			//
			if (NS.fuzzy(x1 - x2) > 0) {
				a = -a;
				b = -b;
				c = -c;
			}

			z_len = false;
		} else if (NS.fuzzy(y1 - y2) != 0.0) {
			b = 0;
			a = 1;
			c = -x1;
		} else
			z_len = true;

		if (Debug.report) {
			if (p1.cmp(p2) == 0)
				Debug.println("OK: created [zero-length] " + this);
			else
				Debug.println("OK: Created " + this);
		}
	}

	public final String toString() {
		return "seg [" + p1 + " - " + p2 + "]";
	}

	public final boolean point_on(Point p) {
		if (z_len) {
			if (p1.cmp(p) == 0)
				return true;

			return false;
		}

		if (NS.fuzzy(a * p.x + b * p.y + c) == 0.0)
			return true;

		return false;
	}

	public final boolean point_to_right(Point p) {
		if (z_len)
			return NS.fuzzy(p.x - p1.x) > 0.0;

		return NS.fuzzy(a * p.x + b * p.y + c) > 0;
	}

	public final boolean point_to_left(Point p) {
		if (z_len)
			return NS.fuzzy(p.x - p1.x) < 0.0;

		return NS.fuzzy(a * p.x + b * p.y + c) < 0;
	}

	public final int orientation(Point p) {
		double x = NS.fuzzy(a * p.x + b * p.y + c);
		if (x < 0)
			return -1;
		else if (x > 0)
			return 1;

		return 0;
	}

	public static final Point intersect(Segment s1, Segment s2) {
		//
		// Degenerate cases first
		//
		if (s2.z_len) {
			if (s1.point_on(s2.p1))
				return new Point(s2.p1.x, s2.p1.y);

			return null;
		}

		//
		// Non-degenerate case
		//

		double sign11 = NS.fuzzy(s1.a * s2.p1.x + s1.b * s2.p1.y + s1.c);
		double sign12 = NS.fuzzy(s1.a * s2.p2.x + s1.b * s2.p2.y + s1.c);
		double sign21 = NS.fuzzy(s2.a * s1.p1.x + s2.b * s1.p1.y + s2.c);
		double sign22 = NS.fuzzy(s2.a * s1.p2.x + s2.b * s1.p2.y + s2.c);

		if ((sign11 * sign12 <= 0.0) && (sign21 * sign22 <= 0.0)) {
			//
			// One more degenerate case
			//
			if ((NS.fuzzy(s1.a - s2.a) == 0.0) && (NS.fuzzy(s1.b - s2.b) == 0.0)) {
				if (NS.fuzzy(s1.c - s2.c) != 0.0)
					return null;

				if (s1.p1.cmp(s2.p1) < 0)
					return new Point((s2.p1.x + s1.p2.x) / 2.0, (s2.p1.y + s1.p2.y) / 2.0);
				else
					return new Point((s1.p1.x + s2.p2.x) / 2.0, (s1.p1.y + s2.p2.y) / 2.0);
			}

			double y = (s2.a * s1.c - s2.c * s1.a) / (s2.b * s1.a - s2.a * s1.b);
			double x = (NS.fuzzy(s1.a) != 0.0) ? -(s1.c + s1.b * y) / s1.a : -(s2.c + s2.b * y) / s2.a;

			return new Point(x, y);
		}

		return null;
	}
}

class SegmentList {
	public Segment seg;
	public SegmentList next;

	public SegmentList(Segment s, SegmentList n) {
		seg = s;
		next = n;
	}

	public static final int length(SegmentList run) {
		int n = 0;

		while (run != null) {
			n++;
			run = run.next;
		}

		return n;
	}

	public final String toString() {
		String x = "seglist[" + seg;

		SegmentList run = next;
		while (run != null) {
			x = x + ", " + run.seg;
			run = run.next;
		}
		return x + "]";
	}
}

class QueueEvent {
	public Point pt;
	public SegmentList U;
	public SegmentList L;

	//
	// This counts how many times Event was inserted in the queue
	//
	public int n_ins;

	public QueueEvent(Point p) {
		pt = p;
		U = null;
		L = null;

		n_ins = 1;
	}

	private final SegmentList add_to_list(Segment s, SegmentList l) {
		if (Debug.debug) {
			SegmentList run = l;

			while (run != null) {
				if (run.seg == s)
					Debug.println("ERROR: inserting dup segment " + s + " in upper list of " + this);

				run = run.next;
			}
		}

		return new SegmentList(s, l);
	}

	public final void add(Segment s) {
		if (Debug.debug) {
			if ((pt.cmp(s.p1) != 0) && (pt.cmp(s.p2) != 0))
				Debug.println("ERROR: Point " + pt + " is not a part of " + s);
		}

		if (pt.cmp(s.p1) == 0)
			U = add_to_list(s, U);
		else
			L = add_to_list(s, L);
	}

	public final String toString() {
		return "evt[" + pt + " / U:" + U + " / L: " + L + "]";
	}
}

class EventTreeNode {
	public EventTreeNode left;
	public EventTreeNode right;
	public EventTreeNode parent;

	public QueueEvent evt;

	public int color;
}

class EventTree {
	private final static int BLACK = 0;
	private final static int RED = 1;

	private EventTreeNode nil;
	private EventTreeNode root;

	//
	// Constructor
	//
	public EventTree() {
		nil = new EventTreeNode();
		nil.color = BLACK;

		root = nil;
	}

	//
	// True if queue is empty
	//
	public final boolean is_empty() {
		return root == nil;
	}

	//
	// insert edge point (with segment s), or intersection point
	// (in which case segment is null). Note the difference between
	// insertion and deleting: while insert inserts segment one by one,
	// delete destroys the entire node.
	//
	public final void insert(Point p) {
		insert(p, null);
	}

	@SuppressWarnings("unused")
	public final void insert(Point p, Segment s) {
		EventTreeNode y = nil;
		EventTreeNode r = root;

		int c = 0;

		while (r != nil) {
			y = r;
			c = r.evt.pt.cmp(p);
			if (c > 0)
				r = r.left;
			else if (c < 0)
				r = r.right;
			else {
				if (s != null)
					r.evt.add(s);

				r.evt.n_ins++;

				if (Debug.report) {
					if (s != null)
						Debug.println("OK: Added segment edge point: " + p + " " + s);
					else
						Debug.println("OK: Duplicate intersection point" + p);
				}

				return;
			}
		}

		if (Debug.report) {
			if (s != null)
				Debug.println("OK: Adding new edge point: " + p + " " + s);
			else
				Debug.println("OK: Adding new intersection point: " + p);
		}

		EventTreeNode x = new EventTreeNode();

		x.left = nil;
		x.right = nil;

		x.evt = new QueueEvent(p);

		if (s != null)
			x.evt.add(s);

		x.parent = y;
		x.color = RED;

		if (y == nil)
			root = x;
		else {
			if (Debug.debug && (c == 0))
				Debug.println("ERROR: Incorrect comparison in insert pt" + p);

			if (c > 0)
				y.left = x;
			else
				y.right = x;
		}

		//
		// Do RB balancing
		//

		while ((x != root) && (x.parent.color == RED)) {
			if (x.parent == x.parent.parent.left) {
				y = x.parent.parent.right;
				if (y.color == RED) {
					x.parent.color = BLACK;
					y.color = BLACK;
					x = x.parent.parent;
					x.color = RED;
				} else {
					if (x == x.parent.right) {
						x = x.parent;
						LeftRotate(x);
					}

					x.parent.color = BLACK;
					x.parent.parent.color = RED;
					RightRotate(x.parent.parent);
				}
			} else {
				y = x.parent.parent.left;
				if (y.color == RED) {
					x.parent.color = BLACK;
					y.color = BLACK;
					x = x.parent.parent;
					x.color = RED;
				} else {
					if (x == x.parent.left) {
						x = x.parent;
						RightRotate(x);
					}

					x.parent.color = BLACK;
					x.parent.parent.color = RED;
					LeftRotate(x.parent.parent);
				}
			}
		}
		root.color = BLACK;
	}

	//
	// Extract minimal node and delete it... Return associated event.
	//
	public final QueueEvent get() {
		EventTreeNode x = root;

		if (x == nil) {
			if (Debug.debug)
				Debug.println("ERROR: No more nodes in the tree");

			return null;
		}

		while (x.left != nil)
			x = x.left;

		return delete(x);
	}

	//
	// Private section
	//
	//

	//
	// Destroy the node and return event associated with it...
	//
	private final QueueEvent delete(EventTreeNode z) {
		QueueEvent res = z.evt;

		EventTreeNode y = ((z.left == nil) || (z.right == nil)) ? z : TreeSuccessor(z);
		EventTreeNode x = (y.left != nil) ? y.left : y.right;

		x.parent = y.parent;

		if (y.parent == nil)
			root = x;
		else {
			if (y == y.parent.left)
				y.parent.left = x;
			else
				y.parent.right = x;
		}

		if (y != z)
			z.evt = y.evt;

		if (y.color != BLACK)
			return res;

		while ((x != root) && (x.color == BLACK)) {
			EventTreeNode w;
			if (x == x.parent.left) {
				w = x.parent.right;

				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					LeftRotate(x.parent);
					w = x.parent.right;
				}
				if ((w.left.color == BLACK) && (w.right.color == BLACK)) {
					w.color = RED;
					x = x.parent;
				} else {
					if (w.right.color == BLACK) {
						w.left.color = BLACK;
						w.color = RED;
						RightRotate(w);
						w = x.parent.right;
					}
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					LeftRotate(x.parent);
					x = root;
				}
			} else {
				w = x.parent.left;

				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					RightRotate(x.parent);
					w = x.parent.left;
				}
				if ((w.left.color == BLACK) && (w.right.color == BLACK)) {
					w.color = RED;
					x = x.parent;
				} else {
					if (w.left.color == BLACK) {
						w.right.color = BLACK;
						w.color = RED;
						LeftRotate(w);
						w = x.parent.left;
					}
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					RightRotate(x.parent);
					x = root;
				}
			}
		}
		x.color = BLACK;

		return res;
	}

	private final void LeftRotate(EventTreeNode x) {
		if (Debug.debug) {
			if (x == nil)
				Debug.println("ERROR: rotating around nil node");

			if (x.right == nil)
				Debug.println("ERROR: nil node goes internal");
		}

		EventTreeNode y = x.right;

		x.right = y.left;

		if (y.left != nil)
			y.left.parent = x;

		y.parent = x.parent;
		if (x.parent == nil)
			root = y;
		else {
			if (x == x.parent.left)
				x.parent.left = y;
			else
				x.parent.right = y;
		}

		y.left = x;
		x.parent = y;
	}

	private final void RightRotate(EventTreeNode y) {
		EventTreeNode x = y.left;

		y.left = x.right;

		if (x.right != nil)
			x.right.parent = y;

		x.parent = y.parent;

		if (x.parent == nil)
			root = x;
		else if (y == y.parent.right)
			y.parent.right = x;
		else
			y.parent.left = x;

		x.right = y;
		y.parent = x;
	}

	private final EventTreeNode TreeSuccessor(EventTreeNode x) {
		if (x.right != nil) {
			x = x.right;

			while (x.left != nil)
				x = x.left;

			return x;
		}

		EventTreeNode y = x.parent;

		while ((y != nil) && (x == y.right)) {
			x = y;
			y = y.parent;
		}

		return y;
	}
}

class IntersectionPoint {
	public Point pt;

	private SegmentList sl;

	IntersectionPoint next;

	public IntersectionPoint(Point p, IntersectionPoint n) {
		pt = p;
		next = n;
	}

	public final void add_seg_list(SegmentList s) {
		while (s != null) {
			sl = new SegmentList(s.seg, sl);
			s = s.next;
		}
	}

	public final void add_segment(Segment s) {
		sl = new SegmentList(s, sl);
	}

	public final void print() {
		Debug.println("Intersection at point " + pt);

		SegmentList run = sl;
		while (run != null) {
			Debug.println("    " + run.seg);
			run = run.next;
		}
	}
}

class StatusTreeNode {
	public StatusTreeNode parent;

	public StatusTreeNode left;
	public StatusTreeNode right;

	public Segment seg_stored;

	public Segment seg_max;
	public Segment seg_min;

	public int color;

	public int id;
	private static int id_counter;

	public StatusTreeNode() {
		id = ++id_counter;
	}

	public String toString() {
		return "Node " + id;
	}
}

class StatusTree {
	private final static int BLACK = 0;
	private final static int RED = 1;

	private StatusTreeNode root;

	public Point p_sweep;

	//
	// This returns the successor to given node; or minimum
	// element if passed null; can return null!
	//
	public final StatusTreeNode succ(StatusTreeNode n) {
		if (n == null) {
			if (root == null)
				return null;

			n = root;

			while (n.seg_stored == null)
				n = n.left;

			return n;
		}

		while ((n != root) && (n == n.parent.right))
			n = n.parent;

		if (n == root)
			return null;

		n = n.parent.right;

		while (n.left != null)
			n = n.left;

		return n;
	}

	//
	// This returns the predecessor to given node, or maximum
	// element if passed null; can return null!
	//
	public final StatusTreeNode pred(StatusTreeNode n) {
		if (n == null) {
			if (root == null)
				return null;

			n = root;

			while (n.seg_stored == null)
				n = n.right;

			return n;
		}

		while ((n != root) && (n == n.parent.left))
			n = n.parent;

		if (n == root)
			return null;

		n = n.parent.left;

		while (n.right != null)
			n = n.right;

		return n;
	}

	//
	// Delete the node; rebalance the tree...
	//
	// NOT_IMPLEMENTED_YET: rebalancing
	//
	public final void delete_node(StatusTreeNode n) {
		if (Debug.report)
			Debug.println("Deleting " + n.seg_stored);

		if (Debug.debug) {
			if ((n.left != null) || (n.right != null))
				Debug.println("Deleting non-child");
		}

		if (n == root) {
			root = null;
			return;
		}

		StatusTreeNode x = (n == n.parent.left) ? n.parent.right : n.parent.left;

		if (n.parent == root) {
			root = x;
			root.color = BLACK;
			root.parent = null;

			return;
		}

		StatusTreeNode y = n.parent.parent;

		x.parent = y;

		if (n.parent == y.left)
			y.left = x;
		else
			y.right = x;

		//
		// Propagate up...
		//
		while (y != null) {
			boolean changed = false;

			if (y.seg_min != y.left.seg_min) {
				changed = true;
				y.seg_min = y.left.seg_min;
			}

			if (y.seg_max != y.right.seg_max) {
				changed = true;
				y.seg_max = y.right.seg_max;
			}

			if (!changed)
				break;

			y = y.parent;
		}

		//
		// Rebalance
		//

		y = n.parent;

		if (y.color == BLACK) {
			while ((x != root) && (x.color == BLACK)) {
				if (x == x.parent.left) {
					StatusTreeNode w = x.parent.right;

					if (w.color == RED) {
						w.color = BLACK;
						x.parent.color = RED;

						LeftRotate(x.parent);
						w = x.parent.right;
					}

					if ((w.left.color == BLACK) && (w.right.color == BLACK)) {
						w.color = RED;
						x = x.parent;
					} else {
						if (w.right.color == BLACK) {
							w.left.color = BLACK;
							w.color = RED;
							RightRotate(w);
							w = x.parent.right;
						}
						w.color = x.parent.color;
						x.parent.color = BLACK;
						w.right.color = BLACK;
						LeftRotate(x.parent);
						x = root;
					}
				} else {
					StatusTreeNode w = x.parent.left;

					if (w.color == RED) {
						w.color = BLACK;
						x.parent.color = RED;

						RightRotate(x.parent);
						w = x.parent.left;
					}

					if ((w.left.color == BLACK) && (w.right.color == BLACK)) {
						w.color = RED;
						x = x.parent;
					} else {
						if (w.left.color == BLACK) {
							w.right.color = BLACK;
							w.color = RED;
							LeftRotate(w);
							w = x.parent.left;
						}
						w.color = x.parent.color;
						x.parent.color = BLACK;
						w.left.color = BLACK;
						RightRotate(x.parent);
						x = root;
					}
				}
			}

			x.color = BLACK;
		}
	}

	private final int cmp_segments(Segment s1, Segment s2) {
		Point p = Segment.intersect(s1, s2);

		if (p != null) {
			if (p_sweep.cmp(p) < 0)
				return -s1.orientation(s2.p1);
			else
				return -s1.orientation(s2.p2);
		}

		//
		// p_sweep is known to be on s2
		//
		return -s1.orientation(p_sweep);
	}

	//
	// Insert segment in the tree
	//
	public final void insert_seg(Segment s) {
		if (Debug.report)
			Debug.println("Inserting " + s);

		StatusTreeNode m = new StatusTreeNode();

		m.seg_stored = s;
		m.seg_max = s;
		m.seg_min = s;
		m.color = BLACK;

		if (root == null) {
			root = m;
			return;
		}

		StatusTreeNode n = root;

		while (n.seg_stored == null) {
			if (cmp_segments(n.right.seg_min, s) > 0)
				n = n.left;
			else
				n = n.right;
		}

		StatusTreeNode np = new StatusTreeNode();
		np.parent = n.parent;
		n.parent = np;
		m.parent = np;

		if (n == root)
			root = np;
		else {
			if (np.parent.left == n)
				np.parent.left = np;
			else
				np.parent.right = np;
		}

		if (cmp_segments(n.seg_stored, s) > 0) {
			np.left = m;
			np.right = n;
		} else {
			np.left = n;
			np.right = m;
		}

		np.seg_min = np.left.seg_min;
		np.seg_max = np.right.seg_max;

		np = np.parent;

		//
		// Propagate the change up...
		//
		while (np != null) {
			boolean changed = false;

			if (np.seg_min != np.left.seg_min) {
				changed = true;
				np.seg_min = np.left.seg_min;
			}

			if (np.seg_max != np.right.seg_max) {
				changed = true;
				np.seg_max = np.right.seg_max;
			}

			if (!changed)
				break;

			np = np.parent;
		}

		//
		// Rebalance the tree
		//
		StatusTreeNode x = m.parent;

		x.color = RED;

		while ((x != root) && (x.parent.color == RED)) {
			if (x.parent == x.parent.parent.left) {
				StatusTreeNode y = x.parent.parent.right;
				if (y.color == RED) {
					x.parent.color = BLACK;
					y.color = BLACK;
					x.parent.parent.color = RED;

					x = x.parent.parent;
				} else {
					if (x == x.parent.right) {
						x = x.parent;
						LeftRotate(x);
					}
					x.parent.color = BLACK;
					x.parent.parent.color = RED;
					RightRotate(x.parent.parent);
				}
			} else {
				StatusTreeNode y = x.parent.parent.left;
				if (y.color == RED) {
					x.parent.color = BLACK;
					y.color = BLACK;
					x.parent.parent.color = RED;

					x = x.parent.parent;
				} else {
					if (x == x.parent.left) {
						x = x.parent;
						RightRotate(x);
					}
					x.parent.color = BLACK;
					x.parent.parent.color = RED;
					LeftRotate(x.parent.parent);
				}
			}
		}

		root.color = BLACK;
	}

	//
	// This returns the node immediately to the left of p,
	// but not including p. can be null!
	//
	public final StatusTreeNode left_node_for_point(Point p) {
		StatusTreeNode n = root;

		if (n == null)
			return null;

		while (n.seg_stored == null) {
			if (!n.right.seg_min.point_to_right(p))
				n = n.left;
			else
				n = n.right;
		}

		if (n.seg_stored.point_to_right(p))
			return n;

		return null;
	}

	//
	// This returns the node immediately to the right of p,
	// but not including p; can be null!
	//
	public final StatusTreeNode right_node_for_point(Point p) {
		StatusTreeNode n = root;

		if (n == null)
			return null;

		while (n.seg_stored == null) {
			if (!n.left.seg_max.point_to_left(p))
				n = n.right;
			else
				n = n.left;
		}

		if (n.seg_stored.point_to_left(p))
			return n;

		return null;
	}

	//
	// Private section
	//
	private final void LeftRotate(StatusTreeNode x) {
		if (Debug.debug) {
			if (x.right == null)
				Debug.println("Incorrect left rotation : left tree null.");
			else if (x.right.right == null)
				Debug.println("Incorrect left rotation : child becomes internaal node.");
		}

		StatusTreeNode y = x.right;

		x.right = y.left;

		if (y.left != null)
			y.left.parent = x;

		y.parent = x.parent;
		if (x.parent == null)
			root = y;
		else {
			if (x == x.parent.left)
				x.parent.left = y;
			else
				x.parent.right = y;
		}

		y.left = x;
		x.parent = y;

		//
		// Restore augmentation (note sequence of restoration)
		//
		x.seg_max = x.right.seg_max;
		x.seg_min = x.left.seg_min;
		y.seg_max = y.right.seg_max;
		y.seg_min = y.left.seg_min;
	}

	private final void RightRotate(StatusTreeNode y) {
		if (Debug.debug) {
			if (y.left == null)
				Debug.println("Incorrect right rotation : left tree null.");
			else if (y.left.left == null)
				Debug.println("Incorrect right rotation : child becomes internaal node.");
		}

		StatusTreeNode x = y.left;

		y.left = x.right;

		if (x.right != null)
			x.right.parent = y;

		x.parent = y.parent;

		if (x.parent == null)
			root = x;
		else if (y == y.parent.right)
			y.parent.right = x;
		else
			y.parent.left = x;

		x.right = y;
		y.parent = x;

		//
		// Restore augmentation (note sequence of restoration)
		//
		y.seg_max = y.right.seg_max;
		y.seg_min = y.left.seg_min;
		x.seg_max = x.right.seg_max;
		x.seg_min = x.left.seg_min;
	}

	//
	// Debug only: Check integrity of the tree
	//
	private void check_node_integrity(StatusTreeNode n) {
		boolean bugs = false;

		if ((n.left == null) && (n.right != null)) {
			Debug.println("Node " + n + " has left child null");
			bugs = true;
		}

		if ((n.right == null) && (n.left != null)) {
			Debug.println("Node " + n + " has left child null");
			bugs = true;
		}

		if (n.left != null) {
			if (n.left.parent != n) {
				Debug.println("Incorrect child/parent linkage at left of " + n);
				bugs = true;
			}
		}

		if (n.right != null) {
			if (n.right.parent != n) {
				Debug.println("Incorrect child/parent linkage at left of " + n);
				bugs = true;
			}
		}

		if (n.seg_stored != null) {
			if (n.seg_min != n.seg_stored)
				Debug.println("Min != stored for child " + n);

			if (n.seg_max != n.seg_stored)
				Debug.println("Max != stored for child " + n);

			if (n.left != null)
				Debug.println("Left not null @ child " + n);

			if (n.right != null)
				Debug.println("Left not null @ child " + n);

			if (n.color != BLACK)
				Debug.println("Color integrity violated @ child " + n);
			return;
		} else if ((n.color == RED) && (!bugs)) {
			if (n.left.color == RED) {
				bugs = true;
				Debug.println("Color integrity violated at left of " + n);
			}

			if (n.right.color == RED) {
				bugs = true;
				Debug.println("Color integrity violated at right of " + n);
			}
		}

		if (!bugs) {
			check_node_integrity(n.left);
			check_node_integrity(n.right);
		}
	}

	public void check_integrity() {
		if (root != null) {
			if (root.color == BLACK)
				check_node_integrity(root);
			else
				Debug.println("ERROR: Color integrity of the tree violated at root.");
		}
	}

}

public class Sweep {
	private static EventTree event_queue;
	private static StatusTree status_tree;

	private static int nsegs;

	private static IntersectionPoint ipt;

	//
	// The Main
	//
	public static final void main(String[] args) {
		if (args.length < 1) {
			Debug.println("Usage:\n\tjava sweep <filename>");
			return;
		}

		event_queue = new EventTree();
		nsegs = 0;

		if (!ReadFile(args[0]))
			return;

		DoProcessing();

		if (ipt != null) {
			Debug.println("Intersections:");

			IntersectionPoint irun = ipt;

			while (irun != null) {
				irun.print();
				irun = irun.next;
			}
		} else
			Debug.println("No intersections...");
	}

	@SuppressWarnings("unused")
	public static final RandomPointList process(RandomSegmentList rsl) {
		FileOutputStream fout = null;
		PrintStream pout = null;

		ipt = null;

		event_queue = new EventTree();
		nsegs = 0;

		if (Debug.dump_in) {
			try {
				fout = new FileOutputStream(Debug.dump_name);
				pout = new PrintStream(fout);
			} catch (IOException e) {
				Debug.println("File system abort.");
			}
		}

		while (rsl != null) {
			Segment ns = new Segment(rsl.x1, rsl.y1, rsl.x2, rsl.y2);

			++nsegs;

			if (Debug.report || Debug.report_inout)
				Debug.println("Read " + ns);

			if (Debug.dump_in && (fout != null))
				pout.println(rsl.x1 + " " + rsl.y1 + " " + rsl.x2 + " " + rsl.y2);

			event_queue.insert(ns.p1, ns);
			event_queue.insert(ns.p2, ns);

			rsl = rsl.next;
		}

		if (Debug.dump_in && (fout != null)) {
			try {
				fout.close();
				pout = null;
				fout = null;
			} catch (IOException e1) {
				pout = null;
				fout = null;
			}
		}

		if (Debug.report || Debug.report_inout)
			Debug.println("Read " + nsegs + " segments");

		if (nsegs <= 0)
			return null;

		DoProcessing();

		if (ipt != null) {
			RandomPointList rpl = null;

			IntersectionPoint irun = ipt;

			while (irun != null) {
				if (Debug.report || Debug.report_inout)
					irun.print();

				rpl = new RandomPointList(irun.pt.x, irun.pt.y, rpl);
				irun = irun.next;
			}

			return rpl;
		}

		return null;
	}

	//
	// Private section
	//

	private static final void DoProcessing() {
		status_tree = new StatusTree();

		while (!event_queue.is_empty()) {
			QueueEvent e = event_queue.get();

			if (Debug.report)
				Debug.println("Extracted event " + e);

			HandleEventPoint(e);
		}
	}

	@SuppressWarnings({ "static-access", "unused", "deprecation" })
	private static final boolean ReadFile(String name) {
		FileInputStream s = null;
		StreamTokenizer t = null;
		try {
			s = new FileInputStream(name);
			t = new StreamTokenizer(s);

			t.eolIsSignificant(true);
			t.parseNumbers();
			t.commentChar(';');

			for (;;) {
				do
					t.nextToken();
				while (t.ttype == t.TT_EOL);

				if (t.ttype == t.TT_EOF) {
					t = null;
					s.close();
					break;
				}

				if (t.ttype != t.TT_NUMBER)
					throw new SyntaxError("Syntax Error (expected x1) line " + t.lineno());

				double x1 = t.nval;

				t.nextToken();

				if (t.ttype != t.TT_NUMBER)
					throw new SyntaxError("Syntax Error (expected y1) line " + t.lineno());

				double y1 = t.nval;

				t.nextToken();

				if (t.ttype != t.TT_NUMBER)
					throw new SyntaxError("Syntax Error (expected x2) line " + t.lineno());

				double x2 = t.nval;

				t.nextToken();

				if (t.ttype != t.TT_NUMBER)
					throw new SyntaxError("Syntax Error (expected y2) line " + t.lineno());

				double y2 = t.nval;

				Segment ns = new Segment(x1, y1, x2, y2);

				++nsegs;

				if (Debug.report || Debug.report_inout)
					Debug.println("Read " + ns);

				event_queue.insert(ns.p1, ns);
				event_queue.insert(ns.p2, ns);

				t.nextToken();

				if (t.ttype == t.TT_EOF) {
					t = null;
					s.close();
					break;
				}

				if (t.ttype != t.TT_EOL)
					throw new SyntaxError("Syntax Error (expected eol) line " + t.lineno());
			}
		} catch (FileNotFoundException e) {
			Debug.println("File " + name + " does not exist...");
			return false;
		} catch (IOException e) {
			Debug.println("I/O fault reading " + name);

			if (t != null) {
				try {
					s.close();
				} catch (IOException x) {
					Debug.println("I/O fault closing file " + name);
				}
			}

			return false;
		} catch (SyntaxError e) {
			Debug.println(e.toString());
			try {
				s.close();
			} catch (IOException x) {
				Debug.println("I/O fault closing file " + name);
			}

			return false;
		}

		if (nsegs == 0) {
			Debug.println("No segments to process.");
			return false;
		}

		if (Debug.report || Debug.report_inout)
			Debug.println("Read " + nsegs + " segments");

		return true;
	}

	//
	// Everything in between ln and rn contains point. We need to sort
	// out segments which have point as one of their ends...
	//
	private static final SegmentList CalculateCrosses(QueueEvent e, StatusTreeNode ln, StatusTreeNode rn) {
		StatusTreeNode run = status_tree.succ(ln);

		SegmentList cross = null;

		while (run != rn) {
			if ((e.pt.cmp(run.seg_stored.p1) != 0) && (e.pt.cmp(run.seg_stored.p2) != 0))
				cross = new SegmentList(run.seg_stored, cross);

			run = status_tree.succ(run);
		}

		return cross;
	}

	//
	// This checks and records intersection
	//
	private static final void CheckIntersection(Point p, SegmentList upper, SegmentList crosses, SegmentList lower) {
		int num_inter = SegmentList.length(upper) + SegmentList.length(crosses) + SegmentList.length(lower);

		if (num_inter <= 1)
			return;

		if (Debug.report)
			Debug.println("Added intersection " + p);

		ipt = new IntersectionPoint(p, ipt);
		ipt.add_seg_list(upper);
		ipt.add_seg_list(crosses);
		ipt.add_seg_list(lower);
	}

	//
	// DeleteNodes
	//

	private static final void DeleteInternalNodes(StatusTreeNode ln, StatusTreeNode rn) {
		StatusTreeNode run = status_tree.succ(ln);

		//
		// list always starts at ln...
		//
		while (run != rn) {
			status_tree.delete_node(run);

			if (Debug.debug)
				status_tree.check_integrity();

			run = status_tree.succ(ln);
		}
	}

	//
	// Main engine of this program
	//
	private static final void HandleEventPoint(QueueEvent e) {
		StatusTreeNode ln = status_tree.left_node_for_point(e.pt);
		StatusTreeNode rn = status_tree.right_node_for_point(e.pt);

		//
		// Cross list...
		//
		SegmentList C = CalculateCrosses(e, ln, rn);

		//
		// is this an intersection?
		//
		CheckIntersection(e.pt, e.U, C, e.L);

		//
		// Delete everything that crosses the point from the tree
		//
		DeleteInternalNodes(ln, rn);

		//
		// Set new order
		//
		status_tree.p_sweep = e.pt;

		//
		// If no crossing, the point is bottom end. Check intersection of new
		// neighbors and
		// just return
		//
		if ((e.U == null) && (C == null)) {
			if ((ln != null) && (rn != null))
				FindNewEvent(ln.seg_stored, rn.seg_stored, e.pt);

			return;
		}

		//
		// Insert these from e.U and reinsert ones from C
		//
		SegmentList run = e.U;

		while (run != null) {
			status_tree.insert_seg(run.seg);

			if (Debug.debug)
				status_tree.check_integrity();

			run = run.next;
		}

		run = C;

		while (run != null) {
			status_tree.insert_seg(run.seg);

			if (Debug.debug)
				status_tree.check_integrity();

			run = run.next;
		}

		//
		// rn and ln do not contain the point, and therefore did not change.
		// The rest of points all does intersect in current point
		//
		if (ln != null)
			FindNewEvent(ln.seg_stored, status_tree.succ(ln).seg_stored, e.pt);
		if (rn != null)
			FindNewEvent(status_tree.pred(rn).seg_stored, rn.seg_stored, e.pt);
	}

	private static final void FindNewEvent(Segment left, Segment right, Point p) {
		Point ip = Segment.intersect(left, right);

		if ((ip != null) && (p.cmp(ip) < 0))
			event_queue.insert(ip);
	}
}
