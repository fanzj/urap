package com.jary.eval.heuralg;

import com.jary.eval.entity.Solution;

/**
 * Created by Fantasy on 2018/2/18.
 */
public interface IAlg{

    void Initialize();//初始化种群

    void Evolve();//一次迭代演化

    Solution SolveF(int nfes);//指定最大函数估值次数求解问题

    Solution Solve(int iters);//指定最大迭代次数求解问题
}
