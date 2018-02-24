package com.jary.eval.utils.excel;
/** 
 * @author Fan Zhengjie 
 * @date 2017��1��29�� ����9:10:47 
 * @version 1.0 
 * @description ��ͬ���۴�������Ӧ��ֵ�����ڽ������ʾ
 */
public class Fitness {
	
	private int m_aI4_nfe;//��m_aI4_nfe������
	private double m_aI8_fitness;//��ǰ����Ӧ��ֵ
	
	public Fitness(){}
	
	public Fitness(int f_aI4_nfe,double f_aI8_fitness){
		this.m_aI4_nfe = f_aI4_nfe;
		this.m_aI8_fitness = f_aI8_fitness;
	}

	public int getM_aI4_nfe() {
		return m_aI4_nfe;
	}

	public void setM_aI4_nfe(int m_aI4_nfe) {
		this.m_aI4_nfe = m_aI4_nfe;
	}

	public double getM_aI8_fitness() {
		return m_aI8_fitness;
	}

	public void setM_aI8_fitness(double m_aI8_fitness) {
		this.m_aI8_fitness = m_aI8_fitness;
	}
	
	

}
