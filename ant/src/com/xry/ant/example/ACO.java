package com.xry.ant.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ��Ⱥ�Ż��㷨���������TSP����
 * 
 * @author FashionXu
 */
public class ACO {
	// ��������Ⱥ
	Ant[] ants;
	int antcount;// ���ϵ�����
	int[][] distance;// ��ʾ���м����
	double[][] tao;// ��Ϣ�ؾ���
	int citycount;// ��������
	int[] besttour;// �������·��
	int bestlength;// ������Ž�ĳ���

	/**
	 * ��ʼ������
	 * 
	 * @param filename
	 *            tsp�����ļ�
	 * @param antnum
	 *            ϵͳ�õ����ϵ�����
	 * @throws ����ļ����������׳��쳣
	 */
	public void init(String filename, int antnum) throws FileNotFoundException,
			IOException {
		antcount = antnum;
		ants = new Ant[antcount];
		// ��ȡ����
		int[] x;
		int[] y;
		String strbuff;
		BufferedReader tspdata = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		strbuff = tspdata.readLine();
		citycount = Integer.valueOf(strbuff);
		distance = new int[citycount][citycount];
		x = new int[citycount];
		y = new int[citycount];
		for (int citys = 0; citys < citycount; citys++) {
			strbuff = tspdata.readLine();
			String[] strcol = strbuff.split(" ");
			x[citys] = Integer.valueOf(strcol[1]);
			y[citys] = Integer.valueOf(strcol[2]);
		}
		// ����������
		for (int city1 = 0; city1 < citycount - 1; city1++) {
			distance[city1][city1] = 0;
			for (int city2 = city1 + 1; city2 < citycount; city2++) {
				distance[city1][city2] = (int) (Math.sqrt((x[city1] - x[city2])
						* (x[city1] - x[city2]) + (y[city1] - y[city2])
						* (y[city1] - y[city2])) + 0.5);
				distance[city2][city1] = distance[city1][city2];
			}
		}
		distance[citycount - 1][citycount - 1] = 0;
		// ��ʼ����Ϣ�ؾ���
		tao = new double[citycount][citycount];
		for (int i = 0; i < citycount; i++) {
			for (int j = 0; j < citycount; j++) {
				tao[i][j] = 0.1;
			}
		}
		bestlength = Integer.MAX_VALUE;
		besttour = new int[citycount + 1];
		// �����������
		for (int i = 0; i < antcount; i++) {
			ants[i] = new Ant();
			ants[i].RandomSelectCity(citycount);
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
				for (int j = 1; j < citycount; j++) {
					ants[i].SelectNextCity(j, tao, distance);
				}
				// �������ϻ�õ�·������
				ants[i].CalTourLength(distance);
				if (ants[i].tourlength < bestlength) {
					// ��������·��
					bestlength = ants[i].tourlength;
					System.out.println("��" + runtimes + "���������µĽ�" + bestlength);
					for (int j = 0; j < citycount + 1; j++)
						besttour[j] = ants[i].tour[j];
				}
			}
			// ������Ϣ�ؾ���
			UpdateTao();
			// ���������������
			for (int i = 0; i < antcount; i++) {
				ants[i].RandomSelectCity(citycount);
			}
		}
	}

	/**
	 * ������Ϣ�ؾ���
	 */
	private void UpdateTao() {
		double rou = 0.5;
		// ��Ϣ�ػӷ�
		for (int i = 0; i < citycount; i++)
			for (int j = 0; j < citycount; j++)
				tao[i][j] = tao[i][j] * (1 - rou);
		// ��Ϣ�ظ���
		for (int i = 0; i < antcount; i++) {
			for (int j = 0; j < citycount; j++) {
				tao[ants[i].tour[j]][ants[i].tour[j + 1]] += 1.0 / ants[i].tourlength;
			}
		}
	}

	/**
	 * ����������н��
	 */
	public void ReportResult() {
		System.out.println("����·��������" + bestlength);
	}
}