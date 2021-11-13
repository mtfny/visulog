package up.visulog.analyzer;

import up.visulog.gitrawdata.CommitBuilder;
import up.visulog.gitrawdata.Commit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;


public class TestCountCommitsPerDayPlugin {
	
	@Test
	public void commitsPerDayTests() {
		checkCommitSum();
		checkCommitNull();
		checkCommitEmpty();
	}
	
    //Test avec différentes dates du plugin (cas standard)
    public static void checkCommitSum() {
        List<Commit> log = new ArrayList<Commit>();
        Date[] dates = new Date[10];
        Calendar c = Calendar.getInstance();
        
        //For Calendar objects, Month value is 0-based. e.g., 0 for January.
        c.set(2017, 0, 20);
        Date d1 = c.getTime();
        c.set(2017, 0, 10);
        Date d2 = c.getTime();
        c.set(2017, 0, 20);
        Date d3 = c.getTime();
        c.set(2017, 0, 5);
        Date d4 = c.getTime();
        c.set(2017, 0, 15);
        Date d5 = c.getTime();
        dates[0] = d1;
        dates[1] = d2;
        dates[2] = d3;
        dates[3] = d4;
        dates[4] = d5;
        
        for (int i = 0; i < dates.length; i++) {
            log.add(new CommitBuilder("0").setDate(dates[i]).createCommit());
        }
        CountCommitsPerDayPlugin.Result res = CountCommitsPerDayPlugin.processLog(log);
        System.out.println(res.getResultAsHtmlDiv());
        System.out.println(res.getResultAsString());
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"module\" hidden>commitActivity</div><div id=\"grid-container\"><div id=\"day-list\"><div id=\"month-space\"></div><div id=\"days-list-name\"><p>Lun</p><p>Mar</p><p>Mer</p><p>Jeu</p><p>Ven</p><p>Sam</p><p>Dim</p></div></div><div id =\"stats-grid\"><p id=\"max-commit\" hidden>10</p><div class=\"months\"><p>Jan 2017</p></div><div class=\"days\"><div class=\"case\" data-commit-number=\"0\" data-date=\"2 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"3 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"4 Janvier\"></div><div class=\"case\" data-commit-number=\"1\" data-date=\"5 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"6 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"7 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"8 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"9 Janvier\"></div><div class=\"case\" data-commit-number=\"1\" data-date=\"10 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"11 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"12 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"13 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"14 Janvier\"></div><div class=\"case\" data-commit-number=\"1\" data-date=\"15 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"16 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"17 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"18 Janvier\"></div><div class=\"case\" data-commit-number=\"0\" data-date=\"19 Janvier\"></div><div class=\"case\" data-commit-number=\"2\" data-date=\"20 Janvier\"></div></div></div></div>");
    }
    
    //Test que l'objet result est "vide" lorsque la liste de commits fournie est null
    public static void checkCommitNull() {
    	
        CountCommitsPerDayPlugin.Result res = CountCommitsPerDayPlugin.processLog(null);
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"module\" hidden>commitActivity</div><div id=\"grid-container\"><div id=\"day-list\"><div id=\"month-space\"></div><div id=\"days-list-name\"><p>Lun</p><p>Mar</p><p>Mer</p><p>Jeu</p><p>Ven</p><p>Sam</p><p>Dim</p></div></div><div id =\"stats-grid\"><p id=\"max-commit\" hidden>10</p><div class=\"months\"></div><div class=\"days\"></div></div></div>");
        assertEquals(res.getResultAsString(), "{}");
    }
    
    //Test que l'objet result est "vide" lorsque la liste de commits fournie est vide
    public static void checkCommitEmpty() {
    	List<Commit> log = new ArrayList<Commit>();
        
        CountCommitsPerDayPlugin.Result res = CountCommitsPerDayPlugin.processLog(log);
        System.out.println(res.getResultAsHtmlDiv());
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"module\" hidden>commitActivity</div><div id=\"grid-container\"><div id=\"day-list\"><div id=\"month-space\"></div><div id=\"days-list-name\"><p>Lun</p><p>Mar</p><p>Mer</p><p>Jeu</p><p>Ven</p><p>Sam</p><p>Dim</p></div></div><div id =\"stats-grid\"><p id=\"max-commit\" hidden>10</p><div class=\"months\"></div><div class=\"days\"></div></div></div>");
        assertEquals(res.getResultAsString(), "{}");
    }
}
