package GeneticTSP7;

import java.util.Arrays;

public class TaskOrderImpl implements TaskOrder {
	
	static int TASK_NUM; //������
    static final int SPECIES_NUM=50; //��Ⱥ��
    static final int DEVELOP_NUM=300; //��������
    static final float pcl=0.80f,pch=0.95f;//�������
    static final float pm=0.60f;//�������
    static float[][] disMap; //��ͼ����

	@Override
	public String[] getTaskOrder(int[][] NormalTaskOrder, int[] position) {
		        //·������
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

            //�����Ŵ��㷨��������
            GeneticAlgorithm GA=new GeneticAlgorithm();

            //������ʼ��Ⱥ
            SpeciesPopulation speciesPopulation = new SpeciesPopulation();

            //��ʼ�Ŵ��㷨��ѡ�����ӡ��������ӡ��������ӣ�
            SpeciesIndividual bestRate=GA.run(speciesPopulation,NormalTaskOrder, position);

            //��ӡ·������̾���
            bestRate.printRate();
            if(bestRate.distance < distance) {
            	for(int p = 0;p < TASK_NUM; p++) {
            		best[p] = bestRate.realGenes[p];
            	}
            	distance = bestRate.distance;
            }
            
            long endTime=System.currentTimeMillis();
            long  totalTime = endTime - startTime1;

            System.out.println("��" + i + "��ѭ����ʱ��"+ totalTime +"  ms");
    	}
    	
         System.out.println(Arrays.toString(best) + distance);
         
		return best;
	}

	

}
