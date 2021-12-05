package up.visulog.analyzer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import up.visulog.gitrawdata.NumberOfLines;

public class TestCountLinesPerDay {
	
	public void checkmakeADay() {
		NumberOfLines Today = new NumberOfLines(2,3);
		
		for(NumberOfLines n : NumberOfLines.getDays()) {
			assertEquals(n.toString(), Today.toString());
		}
		
		//test update
		Today.Update(3,4);
		assertEquals(Today.getAddDay(), 3);
		assertEquals(Today.getDelDay(), 4);
		
		
	}
	
	public void checkSplit() {
		String str = "added lines: 4123, removed lines: 920, total lines: 3203";
        String split[] = str.split(" ");
        ArrayList<String> splited = new ArrayList<String>();
		int i =0;
		
		for(String s:split) {
			String[] st = s.split(",",0);
			splited.add(st[i]);
		}
		
		List <Integer> datas = NumberOfLines.getData(splited);
		int [] t ={4123,920,3203};
		int []tab = {datas.get(0),datas.get(1),datas.get(2)};
		assertArrayEquals(t, tab);
	}
	
}
