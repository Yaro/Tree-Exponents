package TreeLib;

import java.util.Arrays;

import TreeLib.Main.*;

public class Dimensions {

	public static void buildNextLast(int[] a, int[] next, boolean[] last) {
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
		int[] rightNext = new int[maxVal - minVal + 1];
		boolean[] seen = new boolean[maxVal - minVal + 1];
		Arrays.fill(rightNext, -1);
		Arrays.fill(next, -1);
		Arrays.fill(last, false);
		for (int i = a.length - 1; i >= 0; i--) {
			int h = a[i] + shift;
			if (rightNext[h] != -1 && a[i] > a[i + 1]) {
				next[i] = rightNext[h];
			}
			if (!seen[h]) {
				seen[h] = true;
				last[i] = true;
			}
			rightNext[h] = i;
		}
	}

	public static void buildNextPrevLast(int[] a, int[] next, int[] prev, boolean[] last) {
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
		int[] rightNext = new int[maxVal - minVal + 1];
		boolean[] seen = new boolean[maxVal - minVal + 1];
		Arrays.fill(rightNext, -1);
		Arrays.fill(prev, -1);
		Arrays.fill(next, -1);
		Arrays.fill(last, false);
		for (int i = a.length - 1; i >= 0; i--) {
			int h = a[i] + shift;
			if (rightNext[h] != -1 && a[i] > a[i + 1]) {
				next[i] = rightNext[h];
				prev[next[i]] = i;
			}
			if (!seen[h]) {
				seen[h] = true;
				last[i] = true;
			}
			rightNext[h] = i;
		}
	}

	public static void buildNext(int[] a, int[] next) {
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
		int[] rightNext = new int[maxVal - minVal + 1];
		Arrays.fill(rightNext, -1);
		Arrays.fill(next, -1);
		for (int i = a.length - 1; i >= 0; i--) {
			int h = a[i] + shift;
			if (rightNext[h] != -1 && a[i] > a[i + 1]) {
				next[i] = rightNext[h];
			}
			rightNext[h] = i;
		}
	}

	public static void buildNextPrev(int[] a, int[] next, int[] prev) {
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
		int[] rightNext = new int[maxVal - minVal + 1];
		Arrays.fill(rightNext, -1);
		Arrays.fill(prev, -1);
		Arrays.fill(next, -1);
		for (int i = a.length - 1; i >= 0; i--) {
			int h = a[i] + shift;
			if (rightNext[h] != -1 && a[i] > a[i + 1]) {
				next[i] = rightNext[h];
				prev[next[i]] = i;
			}
			rightNext[h] = i;
		}
	}

	public static double calculateWalkDimensionViaBoundary(int iterations, int length) {
		int[][] rw = new int[2][length];
		int[] queue = new int[length];
		int[] dist = new int[length + 1];
		int[][] next = new int[2][length];
		int[][] prev = new int[2][length];
		double avDistance = 0;
		for (int it = 0; it < iterations; it++) {
			for (int i = 0; i < 2; i++) {
				Main.buildRW(rw[i]);
				buildNextPrev(rw[i], next[i], prev[i]);
			}
			Arrays.fill(dist, Integer.MAX_VALUE);
			dist[length] = Integer.MIN_VALUE;
			int start = length / 2, L = 0, R = 0;
			queue[R++] = start;
			dist[start] = 0;
			outer: while (L < R) {
				int x = queue[L++], cdist = dist[x];
				for (int i = 0; i < 2; i++) {
					int nextV = next[i][x];
					int prevV = prev[i][x];
					if ((nextV == -1 && (x == length - 1 || rw[i][x] > rw[i][x + 1])) || (prevV == -1 && (x == 0 || rw[i][x] > rw[i][x - 1]))) {
						avDistance += cdist;
						break outer;
					}
					if (nextV != -1 && cdist + 1 < dist[nextV]) {
						queue[R++] = nextV;
						dist[nextV] = cdist + 1;
					}
					if (prevV != -1 && cdist + 1 < dist[prevV]) {
						queue[R++] = prevV;
						dist[prevV] = cdist + 1;
					}
				}
				if (cdist + 1 < dist[x + 1]) {
					queue[R++] = x + 1;
					dist[x + 1] = cdist + 1;
				}
				if (x > 0 && cdist + 1 < dist[x - 1]) {
					queue[R++] = x - 1;
					dist[x - 1] = cdist + 1;
				}
			}
			// if (it > 0 && iterations > 20 && it % (iterations / 20) == 0) {
			// Main.debug(it, avDistance / it);
			// }
		}
		avDistance /= iterations;
		System.out.print(String.format("{%d, %.3f}, ", length, avDistance));
		return avDistance;
	}

	public static double calculateGraphDimensionViaBoundary(int iterations, int length) {
		// length = 9;
		int[][] rw = new int[2][length];
		int[] queueX = new int[4 * length];
		int[] queueW = new int[4 * length];
		boolean[] edir = new boolean[length];
		for (int i = 0; i < edir.length; i++) {
			edir[i] = Main.rnd.nextBoolean();
		}
		int[][] dist = new int[2][length + 1];
		int[][] next = new int[2][length];
		int[][] prev = new int[2][length];
		double avDistance = 0;
		for (int it = 0; it < iterations; it++) {
			for (int i = 0; i < 2; i++) {
				Main.buildRW(rw[i]);
				// rw[i] = new int[] { 1, 0, -1, 0, -1, 0, -1, -2, -1 };
				buildNextPrev(rw[i], next[i], prev[i]);
				Arrays.fill(dist[i], Short.MAX_VALUE);
				dist[i][length] = Short.MIN_VALUE;
			}
			int start = length / 2, startW = 0, L = 2 * length, R = 2 * length;
			queueX[R] = start;
			queueW[R++] = startW;
			dist[startW][start] = 0;
			boolean reachedBoundary = false;
			while (L < R) {
				int x = queueX[L], w = queueW[L], cdist = dist[w][x];
				// Main.debug(x, w, cdist);
				L++;
				int nextV = next[w][x];
				int prevV = prev[w][x];
				if ((nextV == -1 && (x == length - 1 || rw[w][x] > rw[w][x + 1])) || (prevV == -1 && (x == 0 || rw[w][x] > rw[w][x - 1]))) {
					avDistance += cdist;
					reachedBoundary = true;
					break;
				}
				if (nextV != -1 && cdist < dist[w][nextV]) {
					queueX[L - 1] = nextV;
					queueW[(L--) - 1] = w;
					dist[w][nextV] = cdist;
				}
				// backward jumps
				// if (prevV != -1 && cdist < dist[w][prevV]) {
				// queueX[L - 1] = prevV;
				// queueW[(L--) - 1] = w;
				// dist[w][prevV] = cdist;
				// }

				// // can add these conditions, the answer shouldn't change! (not proved, but can be)
				// if (cdist + 1 < dist[w][x + 1]/* && rw[w][x + 1] < rw[w][x] */) {
				// queueX[R] = x + 1;
				// queueW[R++] = w;
				// dist[w][x + 1] = cdist + 1;
				// }
				// if (x > 0 && cdist + 1 < dist[w][x - 1]/* && rw[w][x - 1] > rw[w][x] */) {
				// queueX[R] = x - 1;
				// queueW[R++] = w;
				// dist[w][x - 1] = cdist + 1;
				// }
				boolean walk = (w == 0);
				if ((walk ^ edir[x]) && cdist + 1 < dist[1 - w][x + 1]) {
					queueX[R] = x + 1;
					queueW[R++] = 1 - w;
					dist[1 - w][x + 1] = cdist + 1;
				}
				if ((!walk ^ edir[x - 1]) && cdist + 1 < dist[1 - w][x - 1]) {
					queueX[R] = x - 1;
					queueW[R++] = 1 - w;
					dist[1 - w][x - 1] = cdist + 1;
				}
				if (cdist + 1 < dist[1 - w][x]) {
					queueX[R] = x;
					queueW[R++] = 1 - w;
					dist[1 - w][x] = cdist + 1;
				}
			}
			if (!reachedBoundary)
				throw new AssertionError();
			// if (it > 0 && iterations > 20 && it % (iterations / 20) == 0) {
			// Main.debug(it, avDistance / it);
			// }
		}
		avDistance /= iterations;
		System.out.print(String.format("{%d, %.3f}, ", length, avDistance));
		return avDistance;
	}
}
