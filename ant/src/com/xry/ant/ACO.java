package com.xry.ant;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 蚁群优化算法，用来求解TSP问题
 * 
 * @author FashionXu
 */
public class ACO {
	// 定义蚂蚁群
	Ant[] ants;
	int antcount;// 蚂蚁的数量
	int[][] time = { { 1000, 500, 3500, 2500 }, { 2000, 500, 1500, 4000 } };// 表示城市间距离
	double[][] tao;// 信息素矩阵
	int cloudletcount;// cloudletcount
	int vmcount;// 虚拟机个数
	int[] besttour;// 求解的最佳路径
	int shortestTime;// 求的最优解的长度

	/**
	 * 初始化函数
	 * 
	 * @param filename
	 *            tsp数据文件
	 * @param antnum
	 *            系统用到蚂蚁的数量
	 * @throws 如果文件不存在则抛出异常
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
		// 初始化信息素矩阵
		tao = new double[vmcount][cloudletcount];
		for (int i = 0; i < vmcount; i++) {
			for (int j = 0; j < cloudletcount; j++) {
				tao[i][j] = 0.1;
			}
		}
		shortestTime = Integer.MAX_VALUE;
		besttour = new int[cloudletcount + 1];
		// 随机放置蚂蚁
		for (int i = 0; i < antcount; i++) {
			ants[i] = new Ant();
			ants[i].RandomSelectCloudlet(cloudletcount, vmcount);
		}
	}

	/**
	 * ACO的运行过程
	 * 
	 * @param maxgen
	 *            ACO的最多循环次数
	 * 
	 */
	public void run(int maxgen) {
		for (int runtimes = 0; runtimes < maxgen; runtimes++) {
			// 每一只蚂蚁移动的过程
			for (int i = 0; i < antcount; i++) {
				for (int j = 1; j < cloudletcount; j++) {
					System.out.println("select for cloudlet:" + j);
					ants[i].SelectNextCity(j, tao, time);
				}
				// 计算蚂蚁获得的路径长度
				ants[i].CalTourLength(time);
				if (ants[i].tourlength < shortestTime) {
					// 保留最优路径
					shortestTime = ants[i].tourlength;
					System.out.println("第" + runtimes + "代，发现新的解" + shortestTime);
					for (int j = 0; j < cloudletcount + 1; j++)
						besttour[j] = ants[i].tour[j];
				}
			}
			// 更新信息素矩阵
			UpdateTao();
			// 重新随机设置蚂蚁
			for (int i = 0; i < antcount; i++) {
				ants[i].RandomSelectCloudlet(cloudletcount, vmcount);
			}
		}
	}

	/**
	 * 更新信息素矩阵
	 */
	private void UpdateTao() {
		double rou = 0.2;
		// 信息素挥发
		for (int i = 0; i < vmcount; i++)
			for (int j = 0; j < cloudletcount; j++)
				tao[i][j] = tao[i][j] * (1 - rou);
		// 信息素更新
		for (int k = 0; k < antcount; k++) {
			for (int i = 0; i < antcount; i++) {
				for (int j = 0; j < cloudletcount; j++) {
					tao[ants[k].tour[j]][j] += 1.0 / ants[k].tourlength;
				}
			}
		}
	}

	/**
	 * 输出程序运行结果
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
		System.out.println("最优路径长度是" + shortestTime);
	}
}