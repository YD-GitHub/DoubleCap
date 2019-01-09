package GeneticTSP4;

import java.util.Arrays;
import java.util.Random;

/**
 * 遗传算法类
 * 包含：
 *         1.run 开始跑算法
 *         2.createBeginningSpecies 创建种群
 *         3.calRate 计算每一种物种被选中的概率
 *      4.select  轮盘策略 选择适应度高的物种
 *      5.crossover 染色体交叉
 *      6.mutate 染色体变异
 *      7.getBest 获得适应度最大的物种
 */

public class GeneticAlgorithm {

        //开始遗传
        SpeciesIndividual run(SpeciesPopulation list,int[][] NormalTaskOrder, int[] position) {
            //创建初始种群
        	
            createBeginningSpecies(list);

            for(int i=1;i<=TaskOrderImpl.DEVELOP_NUM;i++) {
                //选择
                select(list,NormalTaskOrder,position);

                //交叉
                crossover(list);

                //变异
                mutate(list);
            }   
           // list.traverse();

            return getBest(list);
        }

        //创建初始种群
        void createBeginningSpecies(SpeciesPopulation list) {
        	
        	InsertPut ip = new InsertPut();
            //100%随机
            int randomNum=(int)(TaskOrderImpl.SPECIES_NUM);
            for(int i=1;i<=randomNum;i++) {
                SpeciesIndividual species=new SpeciesIndividual();//创建结点
                species.createByRandomGenes();//初始种群基因
                species.realGenes = ip.getInsertPut(species.genes);
                list.add(species);//添加物种
            }

            //40%贪婪
          /*  int greedyNum=TaskOrderImpl.SPECIES_NUM-randomNum;
           for(int i=1;i<=greedyNum;i++)
            {
               SpeciesIndividual species=new SpeciesIndividual();//创建结点
                species.createByGreedyGenes();//初始种群基因
    
                list.add(species);//添加物种
            }*/
        }

        //计算每一物种被选中的概率
        void calRate(SpeciesPopulation list, int[][] NormalTaskOrder, int[] position) {
            //计算总适应度
            float totalFitness=0.0f;
            list.speciesNum=0;
            SpeciesIndividual point=list.head.next;//游标
            while(point != null) {//寻找表尾结点
            
                point.calFitness(NormalTaskOrder,position);//计算适应度

                totalFitness += point.fitness;
                list.speciesNum++;

                point=point.next;
            }

            //计算选中概率
            point=list.head.next;//游标
            while(point != null) {//寻找表尾结点
            
                point.rate=point.fitness/totalFitness;
                point=point.next;
            }
        }

        //选择优秀物种（轮盘赌）
        void select(SpeciesPopulation list,int[][] NormalTaskOrder,int[] position) {           
            //计算适应度
            calRate(list, NormalTaskOrder,position);

            //找出最大适应度物种
            float talentDis=Float.MAX_VALUE;
            SpeciesIndividual talentSpecies=null;
            SpeciesIndividual point=list.head.next;//游标

            while(point!=null) {
                if(talentDis > point.distance) {
                    talentDis=point.distance;
                    talentSpecies=point;
                }
                point=point.next;
            }

            //将最大适应度物种复制talentNum个
            SpeciesPopulation newSpeciesPopulation=new SpeciesPopulation();
            int talentNum=(int)(list.speciesNum/3);
            for(int i=1;i<=talentNum;i++) {
                //复制物种至新表
                SpeciesIndividual newSpecies=talentSpecies.clone();
                newSpeciesPopulation.add(newSpecies);
            }

            //轮盘赌list.speciesNum-talentNum次
            int roundNum=list.speciesNum-talentNum;
            for(int i=1;i<=roundNum;i++) {
                //产生0-1的概率
                float rate=(float)Math.random();

                SpeciesIndividual oldPoint=list.head.next;//游标
                while(oldPoint != null && oldPoint != talentSpecies) {//寻找表尾结点
                
                    if(rate <= oldPoint.rate) {
                        SpeciesIndividual newSpecies=oldPoint.clone();
                        newSpeciesPopulation.add(newSpecies);

                        break;
                    }
                    else {
                        rate=rate-oldPoint.rate;
                    }
                    oldPoint=oldPoint.next;
                }
                if(oldPoint == null || oldPoint == talentSpecies) {
                    //复制最后一个
                    point=list.head;//游标
                    while(point.next != null)//寻找表尾结点
                        point=point.next;
                    SpeciesIndividual newSpecies=point.clone();
                    newSpeciesPopulation.add(newSpecies);
                }

            }
            list.head=newSpeciesPopulation.head;
        }
        
        
        void swap(String genes[],int num,int j) {
       	    String tmp;
            tmp=genes[num];
            genes[num]=genes[j];
            genes[j]=tmp;
       }

        //交叉操作
        void crossover(SpeciesPopulation list) {
        	
        	InsertPut ip = new InsertPut();
        	
            //以概率pcl~pch进行
            float rate=(float)Math.random();
            if(rate > TaskOrderImpl.pcl && rate < TaskOrderImpl.pch) {           
                //SpeciesIndividual point=list.head.next;//游标
                SpeciesIndividual pointFlag1 = list.head.next;
           	 	SpeciesIndividual pointFlag2 = list.head.next;
                Random rand=new Random();
                int find=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                int findFlag = find;
                //System.out.println(find);
                while(pointFlag1 != null && find != 0)//寻找表尾结点
                {
                	pointFlag1=pointFlag1.next;
                    find--;
                }
                int find1=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                while(find1 == findFlag) {
                	find1=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                }
                //System.out.println(findFlag + "---->" + find1);
                while(pointFlag2 != null && find1 != 0)//寻找表尾结点
                {
                	pointFlag2=pointFlag2.next;
                    find1--;
                }

                if(pointFlag1 != null && pointFlag2 != null){
                	
                    int begin=rand.nextInt(TaskOrderImpl.TASK_NUM/4);
                   

                    //取point和point.next进行交叉，形成新的两个染色体
                    for(int i=begin;i<TaskOrderImpl.TASK_NUM/2;i++){
                        //找出point.genes中与point.next.genes[i]相等的位置fir
                        //找出point.next.genes中与point.genes[i]相等的位置sec
                    
                        int fir,sec;
                        for(fir=0;!pointFlag1.genes[fir].equals(pointFlag2.genes[i]);fir++);
                        for(sec=0;!pointFlag2.genes[sec].equals(pointFlag1.genes[i]);sec++);
                        //两个基因互换
                        String tmp;
                        tmp=pointFlag1.genes[i];
                        pointFlag1.genes[i]=pointFlag2.genes[i];
                        pointFlag2.genes[i]=tmp;

                        //消去互换后重复的那个基因
                        pointFlag1.genes[fir]=pointFlag2.genes[i];
                        pointFlag2.genes[sec]=pointFlag1.genes[i];

                    }
                	
                    pointFlag1.realGenes = ip.getInsertPut(pointFlag1.genes);
                    pointFlag2.realGenes = ip.getInsertPut(pointFlag2.genes);
                }
            }
        }

        //变异操作
        void mutate(SpeciesPopulation list) {   
            //每一物种均有变异的机会,以概率pm进行
        	
        	InsertPut ip = new InsertPut();
            SpeciesIndividual point=list.head.next;
            while(point != null) {
                float rate=(float)Math.random();
                if(rate < TaskOrderImpl.pm) {
                    //寻找逆转左右端点
                    Random rand=new Random();
                   
                    int left=rand.nextInt(TaskOrderImpl.TASK_NUM/2);
                    int right=rand.nextInt(TaskOrderImpl.TASK_NUM/2);
                   
                   if(left > right) {
                        int tmp;
                        tmp=left;
                        left=right;
                        right=tmp;
                    }

                    //逆转left-right下标元素
                    while(left < right) {
                        String tmp;
                        tmp=point.genes[left];
                        point.genes[left]=point.genes[right];
                        point.genes[right]=tmp;

                        left++;
                        right--;
                    }
                }
            point.realGenes = ip.getInsertPut(point.genes);
            point=point.next;
            }
        }

        //获得适应度最大的物种
        SpeciesIndividual getBest(SpeciesPopulation list) {
            float distance=Float.MAX_VALUE;
            SpeciesIndividual bestSpecies=null;
            SpeciesIndividual point=list.head.next;//游标
            while(point != null) {//寻找表尾结点
            
                if(distance > point.distance) {
                    bestSpecies=point;
                    distance=point.distance;
                }

                point=point.next;
            }

            return bestSpecies;
        }

}