package up.visulog.analyzer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.CommitBuilder;

public class TestCountMergeCommitsPlugin {
	@Test
    public void testMergeCommitsNumber() {
        var log = new ArrayList<Commit>();
        String[] merge = {"test", "test1", null,"test2",null,null,"test3"};
        for (int i = 0; i < merge.length; i++) {
            log.add(new CommitBuilder("").setMergedFrom(merge[i]).createCommit());
        }
        var res = CountMergeCommitsPlugin.processLog(log);
        assertEquals(4, res.getNumberOfMergeCommits());
    }
}
