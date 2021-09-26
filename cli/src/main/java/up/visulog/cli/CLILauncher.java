package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Optional;

public class CLILauncher {

    public static void main(String[] args) {
        //Creates a new configuration with the gitpath and the plugins as arguments from main.
        var config = makeConfigFromCommandLineArgs(args);

        //If the configuration was correctly created :
        if (config.isPresent()) {
            //We create a new analyzer object using this configuration as an argument.
            var analyzer = new Analyzer(config.get());
            //The analyzer then runs all the plugins.
            var results = analyzer.computeResults();

            System.out.println(results.toHTML());
        } 
        else displayHelpAndExit();
    }

    //So what this code do is take the command line we entered from the terminal, and make a new configuration.
    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        //gitPath takes the current path visulog is located at.
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();

        //This code looks at every argument ('''word''') of the string we entered in the terminal.
        for (var arg : args) {
            //If the argument starts with "--" that means we will be passing program arguments. We are passing what we
            //specifically want the program to do.
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                        //This is the argument that says we will add commit. This is VERY IMPORTANT, as when we will add new
                        //plugins we will need to modify this code to check which plugin we are adding to our gradle.
                        case "--addPlugin":
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits")) plugins.put("countCommits", new PluginConfig() {
                            });

                            break;
                        
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            break;
                        default:
                            return Optional.empty();
                    }
                }
            } 
            
            else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}
