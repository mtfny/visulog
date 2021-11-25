package up.visulog.gitrawdata;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestNumberOfLinesPerAuthor {
    @Test
    public void testparseNumberOfLinesPerAuthor() throws IOException, URISyntaxException {
        var expected = "NumberOfLinesPerAuthor{name=Lyrdinn date=Fri Sep 11 20:39:40 2020 +0200 lines_added=7 lines_removed=1 total=6}";
        var uri = TestCommit.class.getClassLoader().getResource("git.numstat").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var NLPauthor = NumberOfLinesPerAuthor.parseNumberOfLinesPerAuthor(reader);
            assertTrue(NLPauthor.isPresent());
            assertEquals(expected, NLPauthor.get().toString());
        }
    }

    @Test
    public void testparseNumberOfLines() throws IOException, URISyntaxException {
        var expected = "[NumberOfLinesPerAuthor{name=Lyrdinn lines_added=7 lines_removed=1 total=6}, NumberOfLinesPerAuthor{name=Xavier Denis lines_added=22 lines_removed=0 total=22}, NumberOfLinesPerAuthor{name=Aldric Degorre lines_added=4 lines_removed=5 total=-1}]";
        var uri = TestCommit.class.getClassLoader().getResource("git.numstat").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var NLPauthor = NumberOfLinesPerAuthor.parseNumberOfLines(reader);
            assertEquals(expected, NLPauthor.toString());
        }
    }


    @Test
    public void testgetAllLinesPerAuthor() throws IOException, URISyntaxException {
        var expected="[NumberOfLinesPerAuthor{name=Lyrdinn date=Fri Sep 11 20:39:40 2020 +0200 lines_added=7 lines_removed=1 total=6}, NumberOfLinesPerAuthor{name=Xavier Denis date=Wed Sep 9 15:11:35 2020 +0200 lines_added=22 lines_removed=0 total=22}, NumberOfLinesPerAuthor{name=Aldric Degorre date=Mon Aug 31 11:28:28 2020 +0200 lines_added=2 lines_removed=1 total=1}, NumberOfLinesPerAuthor{name=Aldric Degorre date=Thu Aug 27 00:35:19 2020 +0200 lines_added=1 lines_removed=3 total=-2}, NumberOfLinesPerAuthor{name=Aldric Degorre date=Thu Aug 27 00:33:46 2020 +0200 lines_added=1 lines_removed=1 total=0}]";
        var uri = TestCommit.class.getClassLoader().getResource("git.numstat").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var NLPauthor = NumberOfLinesPerAuthor.getAllLinesPerAuthor(reader);
            assertEquals(expected, NLPauthor.toString());
        }
    }

}
