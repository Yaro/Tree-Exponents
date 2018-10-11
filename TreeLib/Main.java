package TreeLib;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {

	static Random rnd = new Random();

	public static void buildRW(int[] a) {
		a[0] = 0;
		for (int i = 1; i < a.length; i++) {
			a[i] = a[i - 1] + (rnd.nextBoolean() ? 1 : -1);
		}
	}

	// exponent 2, as expected
	public static int solveDynamicJumps(int[] rw1, int[] rw2) {
		// debug(rw1);
		// debug(rw2);
		int n = rw1.length;
		int[] next1 = buildNext(rw1);
		int[] next2 = buildNext(rw2);
		int ret = 0, pos = 0;
		while (pos < n - 1) {
			// int nxt = next1[pos];
			int nxt = Math.max(next1[pos], next2[pos]);
			// debug(pos, nxt);
			if (nxt == -1)
				pos++;
			else
				pos = nxt;
			ret++;
		}
		return ret;
	}

	public static int[] buildNext(int[] a) {
		int minVal = Integer.MAX_VALUE;
		int maxVal = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			int h = a[i];
			if (h < minVal)
				minVal = h;
			if (h > maxVal)
				maxVal = h;
		}
		int shift = -minVal;
		int[] prev = new int[maxVal - minVal + 1];
		int[] next = new int[a.length];
		Arrays.fill(prev, -1);
		Arrays.fill(next, -1);
		for (int i = 0; i < a.length; i++) {
			int h = a[i] + shift;
			if (prev[h] != -1) {
				next[prev[h]] = i;
			}
			prev[h] = i;
		}
		return next;
	}

	// direction = {1 -> up, -1 -> down}
	public static int[] findLongestGapUpDown(int[] a, int from, int to, int direction) {
		int m = (from + to) / 2;
		int minVal = Integer.MAX_VALUE;
		int maxVal = Integer.MIN_VALUE;
		for (int i = from; i <= to; i++) {
			int h = a[i];
			if (h < minVal)
				minVal = h;
			if (h > maxVal)
				maxVal = h;
		}
		int shift = -minVal;
		int[] prev = new int[maxVal - minVal + 1];
		Arrays.fill(prev, -1);
		int[] ret = new int[] { -1, -1, -1 };
		for (int i = from; i <= to; i++) {
			int h = a[i] + shift;
			if (prev[h] == -1) {
				prev[h] = i;
				continue;
			}
			int gap = i - prev[h];
			if (gap > ret[0] && a[i] - a[i - 1] == direction) {
				ret[0] = gap;
				ret[1] = prev[h];
				ret[2] = i;
			}
			prev[h] = i;
		}
		return ret;
	}

	public static int[] findLongestGap(int[] a, int from, int to) {
		int m = (from + to) / 2;
		int minVal = Integer.MAX_VALUE;
		int maxVal = Integer.MIN_VALUE;
		for (int i = from; i <= to; i++) {
			int h = a[i];
			if (h < minVal)
				minVal = h;
			if (h > maxVal)
				maxVal = h;
		}
		int shift = -minVal;
		int[] prev = new int[maxVal - minVal + 1];
		Arrays.fill(prev, -1);
		int[] ret = new int[] { -1, -1, -1 };
		for (int i = from; i <= to; i++) {
			int h = a[i] + shift;
			if (prev[h] == -1) {
				prev[h] = i;
				continue;
			}
			int gap = i - prev[h];
			if (gap > ret[0]) {
				ret[0] = gap;
				ret[1] = prev[h];
				ret[2] = i;
			}
			prev[h] = i;
		}
		return ret;
	}

	public static int[] findMiddleGap(int[] a, int from, int to) {
		int m = (from + to) / 2;
		int minVal = Integer.MAX_VALUE;
		int maxVal = Integer.MIN_VALUE;
		for (int i = from; i <= to; i++) {
			int h = a[i];
			if (h < minVal)
				minVal = h;
			if (h > maxVal)
				maxVal = h;
		}
		int shift = -minVal;
		int[] prev = new int[maxVal - minVal + 1];
		Arrays.fill(prev, -1);
		int[] ret = new int[] { -1, -1, -1 };
		for (int i = from; i <= to; i++) {
			int h = a[i] + shift;
			if (prev[h] == -1) {
				prev[h] = i;
				continue;
			}
			if (prev[h] < m && i > m) {
				int gap = i - prev[h];
				if (gap > ret[0]) {
					ret[0] = gap;
					ret[1] = prev[h];
					ret[2] = i;
				}
			}
			prev[h] = i;
		}
		return ret;
	}

	public static void testGap(int x) throws Exception {
		int[] rw1 = new int[x + 1];
		int[] rw2 = new int[x + 1];
		double avGap = 0;
		int ITER = 10000;
		ArrayList<Integer> gaps = new ArrayList<>();
		for (int it = 0; it < ITER; it++) {
			buildRW(rw1);
			buildRW(rw2);
			// int gp1 = findMiddleGap(rw1, 0, x)[0];
			// int gp2 = findMiddleGap(rw2, 0, x)[0];
			// int gp1 = findLongestGap(rw1, 0, x)[0];
			int gp1 = findLongestGapUpDown(rw1, 0, x, 1)[0];
			int gp2 = findLongestGapUpDown(rw2, 0, x, -1)[0];
			// debug(gp1);
			// avGap += gp1;
			avGap += Math.max(gp1, gp2);
			gaps.add(gp1);
			gaps.add(gp2);
		}
		avGap /= ITER;
		debug(avGap);
		PrintWriter out = new PrintWriter("gaps");
		for (int v : gaps) {
			out.print(v + " ");
		}
		out.close();
	}

	public static void testGapExponent(int x) {
		int[] rw1 = new int[x + 1];
		int[] rw2 = new int[x + 1];
		int ITER = 10000;
		int[] len = new int[2 * ITER];
		for (int it = 0; it < ITER; it++) {
			buildRW(rw1);
			buildRW(rw2);

			int[] gp1 = findLongestGap(rw1, 0, x);
			len[2 * it] = gp1[1];
			len[2 * it + 1] = x - gp1[2];

			// int[] gp1 = findLongestGapUpDown(rw1, 0, x, 1);
			// int[] gp2 = findLongestGapUpDown(rw2, 0, x, 1);

			// int[] gp1 = findLongestGap(rw1, 0, x);
			// int[] gp2 = findLongestGap(rw2, 0, x);
			// if (gp1[0] > gp2[0]) {
			// len[2 * it] = gp1[1];
			// len[2 * it + 1] = x - gp1[2];
			// } else {
			// len[2 * it] = gp2[1];
			// len[2 * it + 1] = x - gp2[2];
			// }
		}
		double aL = 0, aR = 1;
		for (int it = 0; it < 50; it++) {
			double aM = (aL + aR) / 2;
			double ex = Math.pow(x, aM);
			double ex12 = 0;
			for (int xi : len) {
				ex12 += Math.pow(xi, aM);
			}
			ex12 /= ITER;
			if (ex < ex12)
				aL = aM;
			else
				aR = aM;
		}
		debug(aL);
	}

	public static void testFromAbove(int x) {
		int[] rw1 = new int[x + 1];
		int[] rw2 = new int[x + 1];
		double avJumps = 0;
		int ITER = 100;
		for (int it = 0; it < ITER; it++) {
			buildRW(rw1);
			buildRW(rw2);
			int j = fromAboveDynamics(rw1, rw2);
			debug(j);
			if (it % 10 == 0)
				debug(it / 1000, avJumps / (it + 1));
			avJumps += j;
		}
		avJumps /= ITER;
		debug(String.format("{%d, %.4f}", x, avJumps));
	}

	public static int[] buildAboveJumps(int[] a) {
		ArrayList<Integer> pts = new ArrayList<>();
		pts.add(0);
		int pos = 0;
		while (pos + 1 < a.length) {
			while (pos + 1 < a.length && a[pos] < a[pos + 1])
				pos++;
			int h = a[pos];
			pos++;
			while (pos < a.length && a[pos] != h)
				pos++;
			if (pos < a.length && a[pos] == h)
				pts.add(pos);
		}
		int[] ret = new int[pts.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = pts.get(i);
		}
		return ret;
	}

	public static int fromAboveDynamics(int[] rw1, int[] rw2) {
		int[] p1 = buildAboveJumps(rw1);
		int[] p2 = buildAboveJumps(rw2);
		// debug(rw1);
		// debug(rw2);
		// debug(p1);
		// debug(p2);
		int i1 = 0, i2 = 0, j = 0;
		while (i1 < p1.length - 1 && i2 < p2.length - 1) {
			if (p1[i1 + 1] > p2[i2 + 1]) {
				i1++;
				while (i2 < p2.length - 1 && p2[i2 + 1] < p1[i1])
					i2++;
			} else {
				i2++;
				while (i1 < p1.length - 1 && p1[i1 + 1] < p2[i2])
					i1++;
			}
			j++;
		}
		// debug("j = ", j);
		return j;
	}

	public static void testJumps(int x) {
		int[] rw1 = new int[x + 1];
		int[] rw2 = new int[x + 1];
		double avJumps = 0;
		int ITER = 40000;
		for (int it = 0; it < ITER; it++) {
			buildRW(rw1);
			buildRW(rw2);
			int j = solveJumpsFree(rw1, rw2, 0, x);
			// int j = Math.abs(rw1[x]);
			if (it % 1000 == 0)
				debug(it / 1000, avJumps / (it + 1));
			avJumps += j;
		}
		avJumps /= ITER;
		debug(avJumps);
	}

	public static int solveJumps(int[] rw1, int[] rw2, int from, int to) {
		int[] a1 = findLongestGapUpDown(rw1, from, to, 1);
		int[] a2 = findLongestGapUpDown(rw2, from, to, -1);
		// int[] a1 = findLongestGap(rw1, from, to);
		// int[] a2 = new int[] { -1, -1, -1 };
		int nfrom, nto;
		if (a1[0] > a2[0]) {
			nfrom = a1[1];
			nto = a1[2];
		} else {
			nfrom = a2[1];
			nto = a2[2];
		}
		if (nfrom == -1)
			return to - from;
		// debug(from, to, nfrom, nto);
		return solveJumps(rw1, rw2, from, nfrom) + solveJumps(rw1, rw2, nto, to) + 1;
	}

	public static int solveJumpsFree(int[] rw1, int[] rw2, int from, int to) {
		// int[] a1 = findLongestGapUpDown(rw1, from, to, 1);
		// int[] a2 = findLongestGapUpDown(rw2, from, to, -1);
		// int[] a1 = findLongestGap(rw1, from, to);
		// int[] a2 = new int[] { -1, -1, -1 };
		int[] a1 = findLongestGap(rw1, from, to);
		int[] a2 = findLongestGap(rw2, from, to);
		int nfrom, nto;
		if (a1[0] > a2[0]) {
			nfrom = a1[1];
			nto = a1[2];
		} else {
			nfrom = a2[1];
			nto = a2[2];
		}
		if (nfrom == -1)
			return 0;
		return solveJumpsFree(rw1, rw2, from, nfrom) + solveJumpsFree(rw1, rw2, nto, to) + 1;
	}

	public static void main(String[] args) throws Exception {
		setTime();
		// testJumps(64000);
		// test1Djumps(20000);
		// testGapExponent(20000);
		// testGap(10000);

		// int[] test = new int[] { 1000, 2000, 4000, 8000, 16000, 32000, 64000
		// };
		// int[] test = new int[] { 10_000_000 };
		// for (int v : test) {
		// testFromAbove(v);
		// }

		int ITER = 1000;
		// int[] radii = new int[] { 20, 35, 60, 90, 120, 160, 200, 250 };
		// for (int r : radii) {
		// Dimensions.calculateDimension(ITER, r);
		// }

		ArrayList<Integer> lens = new ArrayList<>();
		for (int i = 20000; i <= 2000000; i = (int) Math.round(i * 1.5)) {
			lens.add(i);
		}
		for (int len : lens) {
			Dimensions.calculateGraphDimensionViaBoundary(ITER, len);
			System.out.println();
		}

		// double d1 = Dimensions.calculateDimensionViaBoundary(10000, 2000000);
		// double d2 = Dimensions.calculateDimensionViaBoundary(10000, 4000000);
		// debug(Math.log(2) / Math.log(d2 / d1));

		printTime();
	}

	public static void debug(Object... arg) {
		System.out.println(Arrays.deepToString(arg));
	}

	static long systemTime;

	static void setTime() {
		systemTime = System.currentTimeMillis();
	}

	static void printTime() {
		System.err.println("Time consumed: " + (System.currentTimeMillis() - systemTime));
	}

	static void printMemory() {
		System.err.println("Memory consumed: "
				+ (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 + "kb");
	}
}
