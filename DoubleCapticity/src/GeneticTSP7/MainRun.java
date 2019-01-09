package GeneticTSP7;

import java.util.Arrays;

/**
 * 主函数运行类
 */

public class MainRun {

    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
    	TaskOrder order = new TaskOrderImpl();
    	int[][] taskPosition={
    			{0,0},{3,2},        //1,2
                {0,0},{3,3},        //3,4 
                {0,0},{8,8},        //5,6
                {0,0},{9,9},        //7,8
                {12,12},{0,0},		//9,10
                {13,13},{0,0},		//11,12
                {17,17},{0,0},		//13,14
                {0,0},{15,16},		//15,16
                {0,1},{16,16},		//17,18
                {22,22},{1,1},		//19,20
                {28,28},{0,0},		//21,22
                {19,19},{1,1},		//23,24
                {26,26},{11,11},	//25,26
                {10,10},{0,0},		//27,28
                {23,23},{28,28},	//29,30
                {6,6},{13,13}};		//31,32
    	String[] best = null;
    	int [] position = {1,1};
    	best = order.getTaskOrder(taskPosition, position);
    	System.out.println(Arrays.asList(best));
         long end = System.currentTimeMillis();
         long total = end - start;
         System.out.println(total + "ms");

    }

}