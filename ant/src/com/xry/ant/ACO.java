package com.xry.ant;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ��Ⱥ�Ż��㷨���������TSP����
 * 
 * @author FashionXu
 */
public class ACO {
	// ��������Ⱥ
	Ant[] ants;
	int antcount;// ���ϵ�����
	int[][] time = { { 1000, 500, 3500, 2500 }, { 2000, 500, 1500, 4000 } };// ��ʾ���м����
	double[][] tao;// ��Ϣ�ؾ���
	int cloudletcount;// cloudletcount
	int vmcount;// ���������
	int[] besttour;// �������·��
	int shortestTime;// ������Ž�ĳ���

	/**
	 * ��ʼ������
	 * 
	 * @param filename
	 *            tsp�����ļ�
	 * @param antnum
	 *            ϵͳ�õ����ϵ�����
	 * @throws ����ļ����������׳��쳣
	 */
	public void init(int antnum) throws FileNotFoundException, IOException {
		antcount = antnum;
		ants = new Ant[antcount];

		vmcount = 2;
		cloudletcount = 4;
		// distance = new int[vmcount][cloudletcount];
		// for (int i = 0; i < vmcount; i++) {
		// for (int j = 0; j < cloudletcount; j++) {
		// distance[i][j] = rand.nextInt(20);
		// }
		// }
		// ��ʼ����Ϣ�ؾ���
		tao = new double[vmcount][cloudletcount];
		for (int i = 0; i < vmcount; i++) {
			for (int j = 0; j < cloudletcount; j++) {
				tao[i][j] = 0.1;
			}
		}
		shortestTime = Integer.MAX_VALUE;
		besttour = new int[cloudletcount + 1];
		// �����������
		for (int i = 0; i < antcount; i++) {
			ants[i] = new Ant();
			ants[i].RandomSelectCloudlet(cloudletcount, vmcount);
		}
	}

	/**
	 * ACO�����й���
	 * 
	 * @param maxgen
	 *            ACO�����ѭ������
	 * 
	 */
	public void run(int maxgen) {
		for (int runtimes = 0; runtimes < maxgen; runtimes++) {
			// ÿһֻ�����ƶ��Ĺ���
			for (int i = 0; i < antcount; i++) {
				for (int j = 1; j < cloudletcount; j++) {
					System.out.println("select for cloudlet:" + j);
					ants[i].SelectNextCity(j, tao, time);
				}
				// �������ϻ�õ�·������
				ants[i].CalTourLength(time);
				if (ants[i].tourlength < shortestTime) {
					// ��������·��
					shortestTime = ants[i].tourlength;
					System.out.println("��" + runtimes + "���������µĽ�" + shortestTime);
					for (int j = 0; j < cloudletcount + 1; j++)
						besttour[j] = ants[i].tour[j];
				}
			}
			// ������Ϣ�ؾ���
			UpdateTao();
			// ���������������
			for (int i = 0; i < antcount; i++) {
				ants[i].RandomSelectCloudlet(cloudletcount, vmcount);
			}
		}
	}

	/**
	 * ������Ϣ�ؾ���
	 */
	private void UpdateTao() {
		double rou = 0.2;
		// ��Ϣ�ػӷ�
		for (int i = 0; i < vmcount; i++)
			for (int j = 0; j < cloudletcount; j++)
				tao[i][j] = tao[i][j] * (1 - rou);
		// ��Ϣ�ظ���
		for (int k = 0; k < antcount; k++) {
			for (int i = 0; i < antcount; i++) {
				for (int j = 0; j < cloudletcount; j++) {
					tao[ants[k].tour[j]][j] += 1.0 / ants[k].tourlength;
				}
			}
		}
	}

	/**
	 * ����������н��
	 */
	public void ReportResult() {
		for (int i = 0; i < vmcount; i++) {
			for (int j = 0; j < cloudletcount; j++) {
				System.out.println("distance[" + i + "][" + j + "]="
						+ time[i][j]);
			}
		}
		for (int i = 0; i < cloudletcount; i++) {
			System.out.println("besttour[" + i + "]=" + besttour[i]);
		}
		System.out.println("����·��������" + shortestTime);
	}
}