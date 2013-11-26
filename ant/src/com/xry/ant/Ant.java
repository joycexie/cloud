package com.xry.ant;

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
	int[] unvisitedcloudlet;
	/**
	 * ���ϻ�õ�·������
	 */
	public int tourlength;
	int cloudlets;
	int vms;

	/**
	 * ����������ϵ�ĳ�������� ͬʱ������ϰ����ֶεĳ�ʼ������
	 * 
	 * @param cloudletcount
	 *            �ܵĳ�������
	 */
	public void RandomSelectCloudlet(int cloudletcount, int vmcount) {
		cloudlets = cloudletcount;
		vms = vmcount;
		unvisitedcloudlet = new int[cloudletcount];
		tour = new int[cloudletcount + 1];
		tourlength = 0;
		for (int i = 0; i < cloudletcount; i++) {
			tour[i] = -1;
			unvisitedcloudlet[i] = 1;
		}
		long r1 = System.currentTimeMillis();
		Random rnd = new Random(r1);
		int firstvm = rnd.nextInt(vms);
		// int firstcloudlet = rnd.nextInt(cloudletcount);
		unvisitedcloudlet[0] = 0;
		tour[0] = firstvm;
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
		double[][] p;
		p = new double[vms][cloudlets];
		double alpha = 1.0;
		double beta = 1.0;
		double sum = 0;
		int currentvm = tour[index - 1];
		// ���㹫ʽ�еķ�ĸ����
		for (int i = 0; i < cloudlets; i++) {
			if (unvisitedcloudlet[i] == 1) {
				sum += (Math.pow(tao[currentvm][i], alpha) * Math.pow(
						1.0 / distance[currentvm][i], beta));
			}
		}
		System.out.println("sum=" + sum);
		// ����ÿ�����б�ѡ�еĸ���
		for (int i = 0; i < vms; i++) {
			for (int j = 0; j < cloudlets; j++) {
				if (unvisitedcloudlet[j] == 0)
					p[i][j] = 0.0;
				else {
					p[i][j] = (Math.pow(tao[currentvm][j], alpha) * Math.pow(
							1.0 / distance[currentvm][j], beta)) / sum;
				}
				System.out.println("p[" + i + "][" + j + "]=" + p[i][j]);
			}
		}
		// double selectp = p[0][index];
		// int selectvm = -1;
		// for (int i = 0; i < vms; i++) {
		// if (selectp < p[i][index]) {
		// continue;
		// }
		// selectp = p[i][index];
		// selectvm = i;
		// }
		//

		long r1 = System.currentTimeMillis();
		Random rnd = new Random(r1);
		double selectp = rnd.nextDouble();
		// ���̶�ѡ��һ�����У�
		double sumselect = 0;
		int selectvm = -1;
		for (int i = 0; i < vms; i++) {
			sumselect += p[i][index];
			System.out.println("sumselect" + sumselect);
			if (sumselect >= selectp) {
				selectvm = i;
				break;
			}
		}
		// System.out.println("select for cloudlet:" + (index - 1));
		System.out.println("selectp:" + selectp);
		if (selectvm == -1) {
			selectvm = rnd.nextInt(vms);
			System.out.println("random select vm:" + selectvm);
		} else

			System.out.println("select vm:" + selectvm);
		tour[index] = selectvm;
		unvisitedcloudlet[selectvm] = 0;
	}

	/**
	 * �������ϻ�õ�·���ĳ���
	 * 
	 * @param distance
	 *            ȫ�ֵľ��������Ϣ
	 */
	public void CalTourLength(int[][] distance) {
		tourlength = 0;
		// tour[cloudlets] = tour[0];
		System.out.println("*************************************");
		for (int i = 0; i < cloudlets; i++) {
			System.out.print("tour[" + i + "]" + tour[i] + ":tourlength"
					+ distance[tour[i]][i] + "-->");
			tourlength += distance[tour[i]][i];
		}
		System.out.println();
	}
}