package com.example.theuser;

import java.util.Arrays;
import java.util.List;

public class Dist3DMLGrid {

	private static final int ROW = 0;		// �б�־
	private static final int COLUMN = 1;	// �б�־

	private static final int XMIN = 0;
	private static final int XMAX = 1;
	private static final int YMIN = 2;
	private static final int YMAX = 3;
	private static final int ZMIN = 4;
	private static final int ZMAX = 5;
	
	private static final int PX = 0;
	private static final int PY = 1;
	private static final int PZ = 2;
	
	private static final double EPSILON = 0.01;	
	private static final int mN = 50;		// ����������
	private static final int stepDi = 10;	//
	
	private static final int DIMENSION = 3;
	
	private static Dist3DMLGrid instance = new Dist3DMLGrid();
	
	// Singleton
	private Dist3DMLGrid() { }
	// ��ȡ��ʵ��
	public static Dist3DMLGrid getInstance() {
		return instance;
	}

	/**
	 * ��ά�����Ȼ��λ���ƣ�������ʽ���������������������������һ����
	 * @param xnode �ű�ڵ�λ��
	 * @param dist �ڵ㵽Ŀ��λ��
	 * @param area Ŀ���������
	 */
	public double[] compute(List<double[]> xnode, final double[] dist,final double[] area) {
		// ��ʼ��
		double[] tPos = null;
		// �쳣���
		if (xnode == null || dist == null || area == null) {
			return null;
		}		
		if (xnode.size() == 0 || dist.length == 0 || area.length == 0) {
			return null;
		}
		int[] dim1 = new int[]{dist.length, 1};
		int[] dim2 = new int[]{xnode.size(), xnode.get(0).length};
		// ����ά�Ȳ��ԣ���ڵ����ʱ�����ֵ����Ϊ�㣬���ؿռ�
		if (dim1[ROW] < 4 || dim1[ROW] != dim2[ROW] || dim2[COLUMN] != DIMENSION) {
			return null;
		}
		int ns = dim1[ROW];
		// �����Ȼ Newton_Raphson
		int mCount = 0;
		
//		List<double[]> deltaD = new ArrayList<>();
//		List<double[]> tmpPos = new ArrayList<>();
		while (true) {
			mCount++;
			double[] delta = new double[3];
			delta[PX] = (area[XMAX] - area[XMIN]) / stepDi;
			delta[PY] = (area[YMAX] - area[YMIN]) / stepDi;
			delta[PZ] = (area[ZMAX] - area[ZMIN]) / stepDi;
			double[][] xyz = new double[DIMENSION][stepDi];		//MATLAB�е�Sxyz

			for (int i = 0; i < stepDi; i++) {
				xyz[PX][i] = (area[XMIN] + delta[PX] / 2) + i * delta[PX];
				xyz[PY][i] = (area[YMIN] + delta[PY] / 2) + i * delta[PY];
				xyz[PZ][i] = (area[ZMIN] + delta[PZ] / 2) + i * delta[PZ];
			}
			// ���㵱ǰλ�õ�����������d
			double[][] lnPVec = new double[stepDi*stepDi*stepDi][DIMENSION+1];
			double[] lnP = new double[stepDi*stepDi*stepDi];
			for (int i = 0; i < stepDi; i++) {
				for (int j = 0; j < stepDi; j++) {
					for (int k = 0; k < stepDi; k++) {
						double[] tmpLoc = new double[] {xyz[PX][i], xyz[PY][j], xyz[PZ][k]};
						double[] d = new double[ns];
						for (int kk = 0; kk < ns; kk++) {
							d[kk] = norm(tmpLoc, xnode.get(kk));
							d[kk] -= dist[kk];			// ��Ӧres = sum((D-dist).^2);�еļ���
							d[kk] *= d[kk];				// ��Ӧres = sum((D-dist).^2);�еĳ˷�
						}
						double resTemp = sum(d);
						double[] tmp = new double[]{resTemp,tmpLoc[PX],tmpLoc[PY],tmpLoc[PZ]};
						lnP[i * 100 + j * 10 + k] = resTemp;
						lnPVec[i * 100 + j * 10 + k] = tmp;
					}
				}
			}
			
			int index = minIndex(lnP);
			area[XMIN] = lnPVec[index][1] - delta[PX];
			area[YMIN] = lnPVec[index][2] - delta[PY];
			area[ZMIN] = lnPVec[index][3] - delta[PZ];
			area[XMAX] = lnPVec[index][1] + delta[PX];
			area[YMAX] = lnPVec[index][2] + delta[PY];
			area[ZMAX] = lnPVec[index][3] + delta[PZ];	

//			deltaD.add(delta);
			if (norm(delta) < EPSILON || mCount > mN) {
				tPos = lnPVec[index];				
				System.out.println("mCount=" + mCount);
				break;
			}
		}
		return tPos;
	}
	// �������
	private double norm(double[] a, double[] b) {
		if (a == null || b == null || a.length != b.length) {
			throw new IllegalArgumentException("invalid input parameter for norm()");
		}
		int length = a.length;
		double sum = 0.0;
		for (int i = 0; i < length; i++) {
			sum += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	private double norm(double[] a) {
		if (a == null) {
			throw new IllegalArgumentException("invalid input parameter for norm()");
		}
		int length = a.length;
		double[] b = new double[length];
		Arrays.fill(b, 0.0);
		return norm(a, b);
	}
	
	private double sum(double[] a) {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		int length = a.length;
		double sum = 0.0;
		for (int i = 0; i < length; i++) {
			sum += a[i];
		}
		return sum;
	}
	
	private int minIndex(double[] a) {
		if (a == null || a.length == 0) {
			return -1;
		}
		int length = a.length;
		int index = 0;
		double min = a[0];
		for (int i = 0; i < length; i++) {
			if (min > a[i]) {
				min = a[i];
				index = i;
			}
		}
		return index;
	}
	
	private int maxIndex(double[] a) {
		if (a == null || a.length == 0) {
			return -1;
		}
		int length = a.length;
		int index = 0;
		double max = a[0];
		for (int i = 0; i < length; i++) {
			if (max < a[i]) {
				max = a[i];
				index = i;
			}
		}
		return index;
	}
	
}