package GeneticTSP7;

import java.util.Arrays;
import java.util.Random;

/**
 * �Ŵ��㷨��
 * ������
 *         1.run ��ʼ���㷨
 *         2.createBeginningSpecies ������Ⱥ
 *         3.calRate ����ÿһ�����ֱ�ѡ�еĸ���
 *      4.select  ���̲��� ѡ����Ӧ�ȸߵ�����
 *      5.crossover Ⱦɫ�彻��
 *      6.mutate Ⱦɫ�����
 *      7.getBest �����Ӧ����������
 */

public class GeneticAlgorithm {

        //��ʼ�Ŵ�
        SpeciesIndividual run(SpeciesPopulation list,int[][] NormalTaskOrder, int[] position) {
            //������ʼ��Ⱥ
        	
            createBeginningSpecies(list);

            for(int i=1;i<=TaskOrderImpl.DEVELOP_NUM;i++) {
                //ѡ��
                select(list,NormalTaskOrder,position);

                //����
                crossover(list);

                //����
                mutate(list);
            }   
           // list.traverse();

            return getBest(list);
        }

        //������ʼ��Ⱥ
        void createBeginningSpecies(SpeciesPopulation list) {
        	
        	InsertPut ip = new InsertPut();
            //100%���
            int randomNum=(int)(TaskOrderImpl.SPECIES_NUM/3);
            for(int i=1;i<=randomNum;i++) {
                SpeciesIndividual species=new SpeciesIndividual();//�������
                species.createByRandomGenes();//��ʼ��Ⱥ����
                species.realGenes = ip.getInsertPut(species.genes);
                list.add(species);//�������
            }

            //40%̰��
            int greedyNum=TaskOrderImpl.SPECIES_NUM-randomNum;
           for(int i=1;i<=greedyNum;i++)
            {
               SpeciesIndividual species=new SpeciesIndividual();//�������
                species.createByGreedyGenes();//��ʼ��Ⱥ����
                species.realGenes = ip.getInsertPut(species.genes);
                list.add(species);//�������
            }
        }

        //����ÿһ���ֱ�ѡ�еĸ���
        void calRate(SpeciesPopulation list, int[][] NormalTaskOrder, int[] position) {
            //��������Ӧ��
            float totalFitness=0.0f;
            list.speciesNum=0;
            SpeciesIndividual point=list.head.next;//�α�
            while(point != null) {//Ѱ�ұ�β���
            
                point.calFitness(NormalTaskOrder,position);//������Ӧ��

                totalFitness += point.fitness;
                list.speciesNum++;

                point=point.next;
            }

            //����ѡ�и���
            point=list.head.next;//�α�
            while(point != null) {//Ѱ�ұ�β���
            
                point.rate=point.fitness/totalFitness;
                point=point.next;
            }
        }

        //ѡ���������֣����̶ģ�
        void select(SpeciesPopulation list,int[][] NormalTaskOrder,int[] position) {           
            //������Ӧ��
            calRate(list, NormalTaskOrder,position);

            //�ҳ������Ӧ������
            float talentDis=Float.MAX_VALUE;
            SpeciesIndividual talentSpecies=null;
            SpeciesIndividual point=list.head.next;//�α�

            while(point!=null) {
                if(talentDis > point.distance) {
                    talentDis=point.distance;
                    talentSpecies=point;
                }
                point=point.next;
            }

            //�������Ӧ�����ָ���talentNum��
            SpeciesPopulation newSpeciesPopulation=new SpeciesPopulation();
            int talentNum=(int)(list.speciesNum/3);
            for(int i=1;i<=talentNum;i++) {
                //�����������±�
                SpeciesIndividual newSpecies=talentSpecies.clone();
                newSpeciesPopulation.add(newSpecies);
            }

            //���̶�list.speciesNum-talentNum��
            int roundNum=list.speciesNum-talentNum;
            for(int i=1;i<=roundNum;i++) {
                //����0-1�ĸ���
                float rate=(float)Math.random();

                SpeciesIndividual oldPoint=list.head.next;//�α�
                while(oldPoint != null && oldPoint != talentSpecies) {//Ѱ�ұ�β���
                
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
                    //�������һ��
                    point=list.head;//�α�
                    while(point.next != null)//Ѱ�ұ�β���
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

        //�������
        void crossover(SpeciesPopulation list) {
        	
        	InsertPut ip = new InsertPut();
        	
            //�Ը���pcl~pch����
            float rate=(float)Math.random();
            if(rate > TaskOrderImpl.pcl && rate < TaskOrderImpl.pch) {           
                //SpeciesIndividual point=list.head.next;//�α�
                SpeciesIndividual pointFlag1 = list.head.next;
           	 	SpeciesIndividual pointFlag2 = list.head.next;
                Random rand=new Random();
                int find=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                int findFlag = find;
                //System.out.println(find);
                while(pointFlag1 != null && find != 0)//Ѱ�ұ�β���
                {
                	pointFlag1=pointFlag1.next;
                    find--;
                }
                int find1=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                while(find1 == findFlag) {
                	find1=rand.nextInt(TaskOrderImpl.SPECIES_NUM);
                }
                //System.out.println(findFlag + "---->" + find1);
                while(pointFlag2 != null && find1 != 0)//Ѱ�ұ�β���
                {
                	pointFlag2=pointFlag2.next;
                    find1--;
                }

                if(pointFlag1 != null && pointFlag2 != null){
                	
                    int begin=rand.nextInt(TaskOrderImpl.TASK_NUM/4);
                   

                    //ȡpoint��point.next���н��棬�γ��µ�����Ⱦɫ��
                    for(int i=begin;i<TaskOrderImpl.TASK_NUM/2;i++){
                        //�ҳ�point.genes����point.next.genes[i]��ȵ�λ��fir
                        //�ҳ�point.next.genes����point.genes[i]��ȵ�λ��sec
                    
                        int fir,sec;
                        for(fir=0;!pointFlag1.genes[fir].equals(pointFlag2.genes[i]);fir++);
                        for(sec=0;!pointFlag2.genes[sec].equals(pointFlag1.genes[i]);sec++);
                        //�������򻥻�
                        String tmp;
                        tmp=pointFlag1.genes[i];
                        pointFlag1.genes[i]=pointFlag2.genes[i];
                        pointFlag2.genes[i]=tmp;

                        //��ȥ�������ظ����Ǹ�����
                        pointFlag1.genes[fir]=pointFlag2.genes[i];
                        pointFlag2.genes[sec]=pointFlag1.genes[i];

                    }
                	
                    pointFlag1.realGenes = ip.getInsertPut(pointFlag1.genes);
                    pointFlag2.realGenes = ip.getInsertPut(pointFlag2.genes);
                }
            }
        }

        //�������
        void mutate(SpeciesPopulation list) {   
            //ÿһ���־��б���Ļ���,�Ը���pm����
        	
        	InsertPut ip = new InsertPut();
            SpeciesIndividual point=list.head.next;
            while(point != null) {
                float rate=(float)Math.random();
                if(rate < TaskOrderImpl.pm) {
                    //Ѱ����ת���Ҷ˵�
                    Random rand=new Random();
                   
                    int left=rand.nextInt(TaskOrderImpl.TASK_NUM/2);
                    int right=rand.nextInt(TaskOrderImpl.TASK_NUM/2);
                   
                   if(left > right) {
                        int tmp;
                        tmp=left;
                        left=right;
                        right=tmp;
                    }

                    //��תleft-right�±�Ԫ��
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

        //�����Ӧ����������
        SpeciesIndividual getBest(SpeciesPopulation list) {
            float distance=Float.MAX_VALUE;
            SpeciesIndividual bestSpecies=null;
            SpeciesIndividual point=list.head.next;//�α�
            while(point != null) {//Ѱ�ұ�β���
            
                if(distance > point.distance) {
                    bestSpecies=point;
                    distance=point.distance;
                }

                point=point.next;
            }

            return bestSpecies;
        }

}