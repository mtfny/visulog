package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
import java.io.File;
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
        //gitPath takes the current path visulog is located at, which is it's own folder.
        String gitPath = FileSystems.getDefault().getPath(".").toString();
        String configFilePath = FileSystems.getDefault().getPath(".").toString();
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
                        case "--addPlugin":
                            
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits")) plugins.put("countCommits", new PluginConfig() {
                            });

                            break;
                        case "--loadConfigFile":
                            Configuration res = Configuration.loadConfigFile(configFilePath);
                            if(check_directory_exists(res.getGitPath().toString()) == true)
                            	return Optional.of(res);
                            return Optional.empty();
                            
                        case "--justSaveConfigFile":
                            Configuration toSave = new Configuration(gitPath, configFilePath, plugins);
                            toSave.saveConfigFile();
                            return Optional.empty();
                            
                        default:
                            return Optional.empty();
                    }
                }
            }
            else {
                //Checks if we are passing a directory in the parameters. If the directory exists we 
                //change the gitPath to the path we are passing in the parameters.
                String path = arg;
                if (check_directory_exists(path) == true) gitPath = FileSystems.getDefault().getPath(path).toString();
                else return Optional.empty();
            }
        }
        return Optional.of(new Configuration(gitPath, configFilePath, plugins));
    }

    public static boolean check_directory_exists(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return true;
        }
        return false;
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        System.out.println("\nThe correct syntax for using Gradle is: [./gradlew run --args='here are my args']");
        System.out.println("For example:  [./gradlew run --args='. --addPlugin=countCommits']");

        System.out.println("\nHere is the list of arguments you can write: ");
        System.out.println("[--addPlugin]");
        System.out.println("[--loadConfigFile]");
        System.out.println("[--justSaveConfigFile]");

        System.exit(0);
    }
}
