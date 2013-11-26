package com.xry.ant.example;

import java.util.Random;

/**
 * ������
 * 
 * @author FashionXu
 */
public class Ant {
	/**
	 * ���ϻ�õ�·��
	 */
	public int[] tour;
	// unvisitedcity ȡֵ��0��1��
	// 1��ʾû�з��ʹ���0��ʾ���ʹ�
	int[] unvisitedcity;
	/**
	 * ���ϻ�õ�·������
	 */
	public int tourlength;
	int citys;

	/**
	 * ����������ϵ�ĳ�������� ͬʱ������ϰ����ֶεĳ�ʼ������
	 * 
	 * @param citycount
	 *            �ܵĳ�������
	 */
	public void RandomSelectCity(int citycount) {
		citys = citycount;
		unvisitedcity = new int[citycount];
		tour = new int[citycount + 1];
		tourlength = 0;
		for (int i = 0; i < citycount; i++) {
			tour[i] = -1;
			unvisitedcity[i] = 1;
		}
		long r1 = System.currentTimeMillis();
		Random rnd = new Random(r1);
		int firstcity = rnd.nextInt(citycount);
		unvisitedcity[firstcity] = 0;
		tour[0] = firstcity;
	}

	/**
	 * ѡ����һ������
	 * 
	 * @param index
	 *            ��Ҫѡ���index��������
	 * @param tao
	 *            ȫ�ֵ���Ϣ����Ϣ
	 * @param distance
	 *            ȫ�ֵľ��������Ϣ
	 */
	public void SelectNextCity(int index, double[][] tao, int[][] distance) {
		double[] p;
		p = new double[citys];
		double alpha = 1.0;
		double beta = 2.0;
		double sum = 0;
		int currentcity = tour[index - 1];
		// ���㹫ʽ�еķ�ĸ����
		for (int i = 0; i < citys; i++) {
			if (unvisitedcity[i] == 1)
				sum += (Math.pow(tao[currentcity][i], alpha) * Math.pow(
						1.0 / distance[currentcity][i], beta));
		}
		System.out.println("sum=" + sum);
		// ����ÿ�����б�ѡ�еĸ���
		for (int i = 0; i < citys; i++) {
			if (unvisitedcity[i] == 0)
				p[i] = 0.0;
			else {
				p[i] = (Math.pow(tao[currentcity][i], alpha) * Math.pow(
						1.0 / distance[currentcity][i], beta)) / sum;
			}
			System.out.println("p[" + i + "]=" + p[i]);
		}
		long r1 = System.currentTimeMillis();
		Random rnd = new Random(r1);
		double selectp = rnd.nextDouble();
		// ���̶�ѡ��һ�����У�
		double sumselect = 0;
		int selectcity = -1;
		for (int i = 0; i < citys; i++) {
			sumselect += p[i];
			if (sumselect >= selectp) {
				selectcity = i;
				break;
			}
		}

		// double selectp = p[0];
		// int selectcity = -1;
		// for (int i = 0; i < citys; i++) {
		// if (selectp < p[i]) {
		// continue;
		// }
		// selectp = p[i];
		// selectcity = i;
		// }
		// System.out.println("sumselect" + sumselect);
		if (selectcity == -1)
			System.out.println();
		System.out.println("selectcity:" + selectcity);
		tour[index] = selectcity;
		unvisitedcity[selectcity] = 0;
	}

	/**
	 * �������ϻ�õ�·���ĳ���
	 * 
	 * @param distance
	 *            ȫ�ֵľ��������Ϣ
	 */
	public void CalTourLength(int[][] distance) {
		tourlength = 0;
		tour[citys] = tour[0];
		for (int i = 0; i < citys; i++) {
			tourlength += distance[tour[i]][tour[i + 1]];
		}
	}
}