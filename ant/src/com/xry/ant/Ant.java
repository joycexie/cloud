package com.xry.ant;

import java.util.Random;

/**
 * 蚂蚁类
 * 
 * @author FashionXu
 */
public class Ant {
	/**
	 * 蚂蚁获得的路径
	 */
	public int[] tour;
	// unvisitedcity 取值是0或1，
	// 1表示没有访问过，0表示访问过
	int[] unvisitedcloudlet;
	/**
	 * 蚂蚁获得的路径长度
	 */
	public int tourlength;
	int cloudlets;
	int vms;

	/**
	 * 随机分配蚂蚁到某个城市中 同时完成蚂蚁包含字段的初始化工作
	 * 
	 * @param cloudletcount
	 *            总的城市数量
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
	 * 选择下一个城市
	 * 
	 * @param index
	 *            需要选择第index个城市了
	 * @param tao
	 *            全局的信息素信息
	 * @param distance
	 *            全局的距离矩阵信息
	 */
	public void SelectNextCity(int index, double[][] tao, int[][] distance) {
		double[][] p;
		p = new double[vms][cloudlets];
		double alpha = 1.0;
		double beta = 1.0;
		double sum = 0;
		int currentvm = tour[index - 1];
		// 计算公式中的分母部分
		for (int i = 0; i < cloudlets; i++) {
			if (unvisitedcloudlet[i] == 1) {
				sum += (Math.pow(tao[currentvm][i], alpha) * Math.pow(
						1.0 / distance[currentvm][i], beta));
			}
		}
		System.out.println("sum=" + sum);
		// 计算每个城市被选中的概率
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
		// 轮盘赌选择一个城市；
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
	 * 计算蚂蚁获得的路径的长度
	 * 
	 * @param distance
	 *            全局的距离矩阵信息
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