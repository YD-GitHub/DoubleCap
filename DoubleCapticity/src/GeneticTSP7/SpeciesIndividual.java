package GeneticTSP7;

import java.util.Arrays;
import java.util.Random;

/**
 * ������
 * ������
 *         1.createByRandomGenes ��ʼ���ֻ���(���) ����ֱ���ó������б���
 *         2.calFitness ����������Ӧ��
 *         3.printRate ��ӡ·��
 */

public class SpeciesIndividual {

	String[] realGenes;//��ʵ��Ҫ������
    String[] genes;//��������
    float distance;//·��
    float fitness;//��Ӧ��
    SpeciesIndividual next;
    float rate;
    void swap(String genes[],int i,int j) {
    	 String tmp;
         tmp=genes[i];
         genes[i]=genes[j];
         genes[j]=tmp;
    }

    SpeciesIndividual() {
        //��ʼ��
        this.genes=new String[TaskOrderImpl.TASK_NUM/2];
        this.realGenes=new String[TaskOrderImpl.TASK_NUM];
        this.fitness=0.0f;
        this.distance=0.0f;
        this.next=null;
        rate=0.0f;
    }

    //��ʼ���ֻ���(���)
    void createByRandomGenes() {
        //��ʼ������Ϊ1-TASK_NUM����
        for(int i = 0;i < genes.length;i++)
        {
            genes[i]=Integer.toString(2 * i + 1);
        }

        //��ȡ�������
        Random rand=new Random();

        for(int j=0;j<genes.length;j++){
            int num= j + rand.nextInt(genes.length -j);

            //����
            String tmp;
            tmp=genes[num];
            genes[num]=genes[j];
            genes[j]=tmp;
        }
        
    }
    
    //��ʼ���ֻ���(̰��)
    void createByGreedyGenes(){
        Random rand=new Random();
        int i= rand.nextInt(TaskOrderImpl.TASK_NUM/2); //�������һ����Ϊ���
        genes[0]=Integer.toString(2*i+1);
        int j;//�յ�
        int cityNum=0;
        do {
            cityNum++;

            //ѡ����Դ���
            float minDis=Integer.MAX_VALUE;
            int minCity=0;
            for(j=0;j<TaskOrderImpl.TASK_NUM/2;j++) {
                if(j != i) {
                    //���Ƿ�������ظ�
                    boolean repeat=false;
                    for(int n=0;n<cityNum;n++) {
                        if(Integer.parseInt(genes[n]) == 2*j+1) {
                            repeat=true;//����
                            break;
                        }
                    }
                    if(repeat == false) {//û��
                    
                        //�г���
                        if(TaskOrderImpl.disMap[2*i][2*j] < minDis) {
                            minDis=TaskOrderImpl.disMap[2*i][2*j];
                            minCity=j;
                        }
                    }
                }
            }

            //���뵽Ⱦɫ��
            genes[cityNum]=Integer.toString(2 * minCity+1);
            i=minCity;
        }while(cityNum < TaskOrderImpl.TASK_NUM/2-1);
    }

    //����������Ӧ��
    void calFitness(int[][] NormalTaskOrder,int[] position) {
        float totalDis=0.0f;
        
        
        int first = Integer.parseInt(this.realGenes[0])-1;
     
        totalDis += (float)Math.sqrt(Math.pow((NormalTaskOrder[first][0] - position[0]),2) + Math.pow((NormalTaskOrder[first][1] - position[1]),2));
        for(int i = 0;i < TaskOrderImpl.TASK_NUM - 1;i++) {
            int curCity=Integer.parseInt(this.realGenes[i])-1;
            int nextCity=Integer.parseInt(this.realGenes[(i+1)])-1;
            
            totalDis += TaskOrderImpl.disMap[curCity][nextCity];
        }

        this.distance=totalDis;
        this.fitness=1.0f/totalDis;
    }

    //���
    public SpeciesIndividual clone() {   
        SpeciesIndividual species=new SpeciesIndividual();

        //����ֵ
        for(int i=0;i<this.genes.length;i++)
            species.genes[i]=this.genes[i];
        species.distance=this.distance;
        species.fitness=this.fitness;

        return species; 
    }

    //��ӡ·��
    void printRate() {
        System.out.print("���·�ߣ�");
        for(int i=0;i<realGenes.length;i++)
            System.out.print(realGenes[i]+"->");
        
        for(int m = 0; m < TaskOrderImpl.TASK_NUM; m++) {
        	if(Integer.parseInt(realGenes[m]) % 2 == 0) {
        		for(int n = m; n < TaskOrderImpl.TASK_NUM; n++) {
        			if(Integer.parseInt(realGenes[n]) == (Integer.parseInt(realGenes[m]) - 1)) {
        				System.out.println("error");
        				break;  //����ѭ����������ظ�����
        			}
	        	}
        	}
        	
        }
       // System.out.print(genes[0]+"\n");
        System.out.println("��̳��ȣ�" + distance);
    }

}