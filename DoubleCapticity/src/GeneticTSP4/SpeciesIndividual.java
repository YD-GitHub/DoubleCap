package GeneticTSP4;

import java.util.Arrays;
import java.util.Random;

/**
 * 个体类
 * 包含：
 *         1.createByRandomGenes 初始物种基因(随机) 基因直接用城市序列编码
 *         2.calFitness 计算物种适应度
 *         3.printRate 打印路径
 */

public class SpeciesIndividual {

	String[] realGenes;//真实需要的序列
    String[] genes;//基因序列
    float distance;//路程
    float fitness;//适应度
    SpeciesIndividual next;
    float rate;
    void swap(String genes[],int i,int j) {
    	 String tmp;
         tmp=genes[i];
         genes[i]=genes[j];
         genes[j]=tmp;
    }

    SpeciesIndividual() {
        //初始化
        this.genes=new String[TaskOrderImpl.TASK_NUM/2];
        this.realGenes=new String[TaskOrderImpl.TASK_NUM];
        this.fitness=0.0f;
        this.distance=0.0f;
        this.next=null;
        rate=0.0f;
    }

    //初始物种基因(随机)
    void createByRandomGenes() {
        //初始化基因为1-TASK_NUM序列
        for(int i = 0;i < genes.length;i++)
        {
            genes[i]=Integer.toString(2 * i + 1);
        }

        //获取随机种子
        Random rand=new Random();

        for(int j=0;j<genes.length;j++){
            int num= j + rand.nextInt(genes.length -j);

            //交换
            String tmp;
            tmp=genes[num];
            genes[num]=genes[j];
            genes[j]=tmp;
        }
        
    }
    
    //初始物种基因(贪婪)
  /*  void createByGreedyGenes(){
        Random rand=new Random();
        int i= rand.nextInt(TaskOrderImpl.TASK_NUM); //随机产生一个作为起点
        genes[0]=Integer.toString(i+1);
        int j;//终点
        int cityNum=0;
        do {
            cityNum++;

            //选出单源最短
            float minDis=Integer.MAX_VALUE;
            int minCity=0;
            for(j=0;j<TaskOrderImpl.TASK_NUM;j++) {
                if(j != i) {
                    //判是否和已有重复
                    boolean repeat=false;
                    for(int n=0;n<cityNum;n++) {
                        if(Integer.parseInt(genes[n]) == j+1) {
                            repeat=true;//重了
                            break;
                        }
                    }
                    if(repeat == false) {//没重
                    
                        //判长度
                        if(TaskOrderImpl.disMap[i][j] < minDis) {
                            minDis=TaskOrderImpl.disMap[i][j];
                            minCity=j;
                        }
                    }
                }
            }

            //加入到染色体
            genes[cityNum]=Integer.toString(minCity+1);
            i=minCity;
        }while(cityNum < TaskOrderImpl.TASK_NUM-1);
    }*/

    //计算物种适应度
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

    //深拷贝
    public SpeciesIndividual clone() {   
        SpeciesIndividual species=new SpeciesIndividual();

        //复制值
        for(int i=0;i<this.genes.length;i++)
            species.genes[i]=this.genes[i];
        species.distance=this.distance;
        species.fitness=this.fitness;

        return species; 
    }

    //打印路径
    void printRate() {
        System.out.print("最短路线：");
        for(int i=0;i<realGenes.length;i++)
            System.out.print(realGenes[i]+"->");
        
        for(int m = 0; m < TaskOrderImpl.TASK_NUM; m++) {
        	if(Integer.parseInt(realGenes[m]) % 2 == 0) {
        		for(int n = m; n < TaskOrderImpl.TASK_NUM; n++) {
        			if(Integer.parseInt(realGenes[n]) == (Integer.parseInt(realGenes[m]) - 1)) {
        				System.out.println("error");
        				break;  //跳出循环避免出现重复操作
        			}
	        	}
        	}
        	
        }
       // System.out.print(genes[0]+"\n");
        System.out.println("最短长度：" + distance);
    }

}