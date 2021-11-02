package up.visulog.analyzer;

import up.visulog.gitrawdata.CommitBuilder;
import up.visulog.gitrawdata.Commit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
/*import org.junit.Test;
import static org.junit.Assert.*;*/


public class TestCountCommitsPerDayPlugin {
	/*public static void main(String[] args) {
		checkCommitSum();
		checkCommitNull();
		checkCommitEmpty();
	}
	
    //Test avec différentes dates du plugin (cas standard)
    public static void checkCommitSum() {
        List<Commit> log = new ArrayList<Commit>();
        Date[] dates = new Date[10];
        Calendar c = Calendar.getInstance();
        c.set(2017, 1, 20);
        Date d1 = c.getTime();
        c.set(2017, 2, 25);
        Date d2 = c.getTime();
        c.set(2017, 1, 20);
        Date d3 = c.getTime();
        c.set(2017, 1, 1);
        Date d4 = c.getTime();
        c.set(2016,5,14);
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
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"grid\"><p id=\"max-commit\" hidden></p><div class=\"months\"><p>Mai 2016</p><p>Jan 2017</p><p>Fév 2017</p></div><div id=\"days\"><div class=\"case\" data-commit-number=\"1\" data-date=\"14 Mai\"></div><div class=\"case\" data-commit-number=\"1\" data-date=\"1 Janvier\"></div><div class=\"case\" data-commit-number=\"2\" data-date=\"20 Janvier\"></div><div class=\"case\" data-commit-number=\"1\" data-date=\"25 Février\"></div></div></div>");
    }
    
    //Test que l'objet result est "vide" lorsque la liste de commits fournie est null
    public static void checkCommitNull() {
    	
        CountCommitsPerDayPlugin.Result res = CountCommitsPerDayPlugin.processLog(null);
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"grid\"><p id=\"max-commit\" hidden></p><div class=\"months\"></div><div id=\"days\"></div></div>");
        assertEquals(res.getResultAsString(), "{}");
    }
    
    //Test que l'objet result est "vide" lorsque la liste de commits fournie est vide
    public static void checkCommitEmpty() {
    	List<Commit> log = new ArrayList<Commit>();
        
        CountCommitsPerDayPlugin.Result res = CountCommitsPerDayPlugin.processLog(log);
        assertEquals(res.getResultAsHtmlDiv(), "<div class=\"grid\"><p id=\"max-commit\" hidden></p><div class=\"months\"></div><div id=\"days\"></div></div>");
        assertEquals(res.getResultAsString(), "{}");
    }*/
}
