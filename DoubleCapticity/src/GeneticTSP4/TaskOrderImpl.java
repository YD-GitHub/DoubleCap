package GeneticTSP4;

import java.util.Arrays;

public class TaskOrderImpl implements TaskOrder {
	
	static int TASK_NUM; //任务数
    static final int SPECIES_NUM=50; //种群数
    static final int DEVELOP_NUM=300; //进化代数
    static final float pcl=0.80f,pch=0.95f;//交叉概率
    static final float pm=0.60f;//变异概率
    static float[][] disMap; //地图数据

	@Override
	public String[] getTaskOrder(int[][] NormalTaskOrder, int[] position) {
		        //路径集合
		        TASK_NUM=NormalTaskOrder.length;
		        disMap=new float[TASK_NUM][TASK_NUM];
		        for(int i=0;i<TASK_NUM;i++) {
		            for(int j=i;j<TASK_NUM;j++) {
		                float dis=(float)Math.sqrt(Math.pow((NormalTaskOrder[i][0] - NormalTaskOrder[j][0]),2) + Math.pow((NormalTaskOrder[i][1] - NormalTaskOrder[j][1]),2));
		                disMap[i][j]=dis;
		                disMap[j][i]=disMap[i][j];
		            }
		    }
		float distance = Integer.MAX_VALUE;
    	String [] best = new String[TASK_NUM];
    	for (int i = 1; i <= 10; i ++) {
    		long startTime1=System.currentTimeMillis();

            //创建遗传算法驱动对象
            GeneticAlgorithm GA=new GeneticAlgorithm();

            //创建初始种群
            SpeciesPopulation speciesPopulation = new SpeciesPopulation();

            //开始遗传算法（选择算子、交叉算子、变异算子）
            SpeciesIndividual bestRate=GA.run(speciesPopulation,NormalTaskOrder, position);

            //打印路径与最短距离
            bestRate.printRate();
            if(bestRate.distance < distance) {
            	for(int p = 0;p < TASK_NUM; p++) {
            		best[p] = bestRate.realGenes[p];
            	}
            	distance = bestRate.distance;
            }
            
            long endTime=System.currentTimeMillis();
            long  totalTime = endTime - startTime1;

            System.out.println("第" + i + "次循环耗时："+ totalTime +"  ms");
    	}
    	
         System.out.println(Arrays.toString(best) + distance);
         
		return best;
	}

	

}
