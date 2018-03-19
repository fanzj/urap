package com.jary.eval.problem;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.utils.FileUtils;
import com.jary.eval.utils.MathUtils;

/**
 * 实验数据生成
 */
public class RandomGenerateData {
	
	/**
	 * 行李件数
	 */
	private int N;
	
	/**
	 * 危险物品
	 */
	private int M;
	
	/**
	 * 安检设备数
	 */
	private int K;

	/**
	 * 二层安检设备数
	 */
	private int R;

	/**
	 * 安检人员数
	 */
	private int Q;
	

	private static Random Rand = new Random(System.currentTimeMillis());
	
	public RandomGenerateData(int M,int N,int K,int R,int Q){
		this.M = M;
		this.N = N;
		this.K = K;
		this.R = R;
		this.Q = Q;
	}

	
	/**
	 * 危险品权重Wi
	 * @param path
	 */
	private void genWi(String path){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<M;i++){
			sb.append(String.format("%.8f", Rand.nextDouble()));
			if(i < M-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.wiStr, sb.toString());
	}

	/**
	 * 行李长度
	 * @param path
	 */
	private void genLj(String path){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<N;j++){
			sb.append(String.format("%.3f", MathUtils.doubleAToB(0.1,2)));
			if(j < N-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.ljStr, sb.toString());
	}

	/**
	 * 一层设备生成速度
	 * @param path
	 */
	private void genSk(String path){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<K;j++){
			sb.append(String.format("%.3f", MathUtils.doubleAToB(0.5,3)));
			if(j < K-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.skStr, sb.toString());
	}

	/**
	 * 2层设备生成速度
	 * @param path
	 */
	private void genSr(String path){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<R;j++){
			sb.append(String.format("%.3f", MathUtils.doubleAToB(0.5,3)));
			if(j < R-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.srStr, sb.toString());
	}

	/**
	 * 最早达到时间
	 * @param path
	 */
	private void gent0j(String path){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<N;j++){
			sb.append(String.format("%.3f", MathUtils.doubleAToB(0,50)));
			if(j < N-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.t0jStr, sb.toString());
	}

	/**
	 * 生成每个包裹的体积
	 * @param path
	 */
	private void genVj(String path){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<N;j++){
			sb.append(String.format("%.3f", MathUtils.doubleAToB(0,10)));
			if(j < N-1){
				sb.append(" ");
			}
		}
		FileUtils.writeAsStr(path, URAPConstant.vjStr, sb.toString());
	}

	/**
	 * j中含有i的概率
	 * @param f_str_path
	 */
	private void genAij(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<M;t_aI4_i++){
			for(int t_aI4_j=0;t_aI4_j<N;t_aI4_j++){
				t_aTC_res.append(String.format("%.8f", Rand.nextDouble()));
				if(t_aI4_j < N-1){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < M-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.writeAsStr(f_str_path, URAPConstant.aijStr, t_aTC_res.toString());
	}

	/**
	 * 危险品i被设备k检测到的概率
	 * @param f_str_path
	 */
	private void genBik(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<M;t_aI4_i++){
			for(int t_aI4_k=0;t_aI4_k<K;t_aI4_k++){
				t_aTC_res.append(String.format("%.8f", Rand.nextDouble()));
				if(t_aI4_k < K-1){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < M-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.writeAsStr(f_str_path, URAPConstant.bik, t_aTC_res.toString());
	}

	/**
	 * 危险品i被2层设备r检测到的概率
	 * @param f_str_path
	 */
	private void genBir(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<M;t_aI4_i++){
			for(int t_aI4_r=0;t_aI4_r<R+1;t_aI4_r++){
				if(t_aI4_r == 0){
					t_aTC_res.append("0");
				}else{
					t_aTC_res.append(String.format("%.8f", Rand.nextDouble()));
				}

				if(t_aI4_r < R){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < M-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.writeAsStr(f_str_path, URAPConstant.bir, t_aTC_res.toString());
	}

	/**
	 * 危险品i被人q检测到的概率
	 * @param f_str_path
	 */
	private void genYiq(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_i = 0; t_aI4_i < M; t_aI4_i++) {
			for (int t_aI4_q = 0; t_aI4_q < Q + 1; t_aI4_q++) {
				if (t_aI4_q == 0) {
					t_aTC_res.append("0");
				} else {
					t_aTC_res.append(String.format("%.8f",Rand.nextDouble()));
				}
				if (t_aI4_q < Q) {
					t_aTC_res.append(" ");
				} else if (t_aI4_i < M - 1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.writeAsStr(f_str_path, URAPConstant.yiq, t_aTC_res.toString());
	}

	/**
	 * 计算危险品出现的频率区间
	 * @param problemNum
	 * @return
	 */
	public String calInterval(String path, int problemNum) throws IOException {
		String prefixPath = URAPConstant.PREFIX_PATH + String.format("%02d/", problemNum) + "msiap/";
		double[][] aij = FileUtils.StrTransfer2D(FileUtils.readAsStr(prefixPath + URAPConstant.aijStr));
		double sum[] = new double[M];
		double total = 0;
		int minPos = -1, maxPos = -1;
		double minVal = Double.MAX_VALUE, maxVal = Double.MIN_VALUE;
		for(int i=0;i<M;i++){
			sum[i] = 0;
			for(int j=0;j<N;j++){
				sum[i] += aij[i][j];
			}
			total += sum[i];

			if(Double.compare(sum[i], minVal)<0){
				minVal = sum[i];
				minPos = i;
			}
			if(Double.compare(sum[i], maxVal)>0){
				maxVal = sum[i];
				maxPos = i;
			}
		}
		for(int i=0;i<M;i++){
			sum[i] /= total;
		}
		FileUtils.writeAsStr(path, URAPConstant.frequency, String.format("[%f,%f]", sum[minPos],sum[maxPos]) );
		return String.format("[%f,%f]", sum[minPos],sum[maxPos]);
	}
	


	

	private void genExpData(String path){
		genWi(path);
		genLj(path);
		genVj(path);
		genSk(path);
		genSr(path);
		gent0j(path);

		genAij(path);
		genBik(path);
		genBir(path);
		genYiq(path);
	}
	
	public static void main(String[] args) throws IOException {
		RandomGenerateData t_aTC_genData = new RandomGenerateData(50	,2018,42,23,17);
		String path = "msiap\\data_08";
		//t_aTC_genData.genExpData(path);
		t_aTC_genData.calInterval(path,8);
	}
}
