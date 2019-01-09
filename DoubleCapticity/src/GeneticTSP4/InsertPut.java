package GeneticTSP4;

import java.util.Iterator;
import java.util.LinkedList;


public class InsertPut {

	public String[] getInsertPut(String[] init) {
		LinkedList<String> put = new LinkedList<>();
		String[] result = new String[init.length * 2];
		int flagCap = -1;
		int MaxCap = 2;
		int CurrCap = 0;
		int CurrBefore = 0;
		for(int i= 0 ,j = 0 ; j < init.length;) {
			if(CurrCap == 0) {
				result[i] = init[j];
				CurrCap ++;
				i++;
				String s = (Integer.parseInt(init[j]) + 1) + "";
				put.add(s);
				CurrBefore = 0;
			}else if(CurrCap == 1 && CurrBefore == 0) {
				int curr = Integer.parseInt(init[j]);
				int currNext = Integer.parseInt(init[(j+1) % init.length]);
				Iterator<String> it = put.iterator();
				String flag = null;
				float min = TaskOrderImpl.disMap[curr - 1][currNext - 1];
				while(it.hasNext()) {
					String ss = it.next();
					int currFlag = Integer.parseInt(ss);
					if(TaskOrderImpl.disMap[curr - 1][currFlag - 1] < min) {
						flag = ss;
						min = TaskOrderImpl.disMap[curr - 1][currFlag - 1];
					}
				}
					if(flag != null) {
						result[i] = flag;
						i++;
						j++;
						CurrCap --;
						put.remove(flag);
						CurrBefore = 1;
						}
						else {
							result[i] = currNext+ "";
							i++;
							j++;
							flagCap = 1;
							CurrCap ++;
							CurrBefore = 1;
						}
			}else if(CurrCap == 1 && CurrBefore == 2) {
				int currPut = Integer.parseInt(result[i - 1]);
				int curr = Integer.parseInt(init[j]);
				Iterator<String> it = put.iterator();
				String flag = null;
				float min = TaskOrderImpl.disMap[currPut - 1][curr - 1];
				while(it.hasNext()) {
					String ss = it.next();
					int currFlag = Integer.parseInt(ss);
					if(TaskOrderImpl.disMap[currPut - 1][currFlag - 1] < min) {
						flag = ss;
						min = TaskOrderImpl.disMap[curr - 1][currFlag - 1];
					}
				}
				if(flag != null) {
					result[i] = flag;
					put.remove(flag);
					i++;
					CurrCap--;
				}else {
					result[i] = curr + "";
					i++;
					j++;
					flagCap = 0;
					CurrCap++;
				}
				
			}else if(CurrCap == 2) {
				CurrBefore = 2;
				String flag = null;
				int curr = Integer.parseInt(result[i - 1]);
				String sflag = (curr + 1) + "";
				put.add(sflag);
				Iterator<String> it = put.iterator();
				float min = 99999.9f;
				while(it.hasNext()) {
					String ss = it.next();
					int currFlag = Integer.parseInt(ss);
					if(TaskOrderImpl.disMap[curr - 1][currFlag - 1] < min) {
						flag = ss;
						min = TaskOrderImpl.disMap[curr - 1][currFlag - 1];
					}
				}
				if(flag != null) {
				result[i] = flag;
				i++;
				CurrCap --;
				if(flagCap == 1)
				j++;
				put.remove(flag);
				}
			
			}
		}
		Iterator<String> it = put.iterator();
		int q = result.length - 1;
		while(it.hasNext()) {
			result[q] = it.next();
			q--;
		}
		for(int l = 0; l < result.length;l++) {
			if(result[l] == null) {
				result[l] = (Integer.parseInt(result[l-1]) + 1) + "";
				break;
			}
		}
		
		
		
		/*for(int m = 0; m < result.length; m++)
			System.out.print(result[m] + ", ");
		
		System.out.println("\r\n--------------------------------");*/
		
		return result;
	}
}
