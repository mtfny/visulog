package up.visulog.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

public class TestCLILauncher {
    /*
    TODO: one can also add integration tests here:
    - run the whole program with some valid options and look whether the output has a valid format
    - run the whole program with bad command and see whether something that looks like help is printed
     */
    @Test
    public void testArgumentParser() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var config1 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{".", "--addPlugin=CountCommitsPerDay"});
        assertTrue(config1.isPresent());
        var config2 = CLILauncher.makeConfigFromCommandLineArgs(new String[] {
            "--nonExistingOption"
        });
        assertFalse(config2.isPresent());
    }
}
