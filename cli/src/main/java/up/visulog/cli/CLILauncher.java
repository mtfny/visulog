package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.webgen.writeHTML;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.Buffer;
import java.nio.file.FileSystems;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Optional;

import java.lang.reflect.*;
import java.net.URISyntaxException;

public class CLILauncher {

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, URISyntaxException {
        //Creates a new configuration with the gitpath and the plugins as arguments from main.
        var config = makeConfigFromCommandLineArgs(args);

        //If the configuration was correctly created :
        if (config.isPresent()) {
            
            //We create a new analyzer object using this configuration as an argument.
            var analyzer = new Analyzer(config.get());
            //The analyzer then runs all the plugins.
            var results = analyzer.computeResults();

            writeHTML writer = new writeHTML(results);
            writer.createhtmlFile();
            System.out.println("Success");
        } 
        else helpexit_txt();

        //displayHelpAndExit();
    }

    //So what this code do is take the command line we entered from the terminal, and make a new configuration.
    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
                        case "--help" :
                            help_txt();
                           // help_command();
                            break;

                        case "--addPlugin":
                            addPlugin(pValue, plugins);
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
                //If we aren't passing a command that exists. We are changing the value of gitpath to the 
                //argument we passed to check if we are passing a directory in our command line.
                gitPath = FileSystems.getDefault().getPath(arg).toString();
            }
        }
        //At the end we verify that everything is working by checking if the path in gitPath is
        //in an existing directory or not.
        if (check_directory_exists(gitPath)) return Optional.of(new Configuration(gitPath, configFilePath, plugins));
        else return Optional.empty();
    }   

    public static void addPlugin(String pluginName, HashMap<String, PluginConfig> plugins) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            @SuppressWarnings("unchecked")

            //We create a new plugin config according to the pluginName we typed.
            Class<?> c = (Class<PluginConfig>)Class.forName("up.visulog.config." + pluginName + "Config");
            Constructor<?>[] configContructor = c.getConstructors();
            
            plugins.put(pluginName, (PluginConfig)configContructor[0].newInstance());
        }
        
        catch (ClassNotFoundException e) {
            System.out.println("ERROR: Plugin doesn't exist");
        }
    }

    public static boolean check_directory_exists(String path) {
        //First we check if there is a git directory using "file.isDirectory()" and then we check it there 
        //is a .git repository in this repository.
        File path_passed = new File(path);

        if (path_passed.isDirectory()) {
            File gitdirectory = new File(path + "/.git");
            if (gitdirectory.isDirectory()) return true;
            return true;
        }
        return false;
    }

    private static void helpexit_txt(){
        try {
            InputStream file = CLILauncher.class.getClassLoader().getResource("helpexit.txt").openStream();
            InputStreamReader reader = new InputStreamReader(file);

            BufferedReader Breader = new BufferedReader(reader);
            String line;

            while( (line = Breader.readLine()) !=null){
                System.out.println(line);
            }
            Breader.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    private static void help_txt() {
        try {
            InputStream file = CLILauncher.class.getClassLoader().getResource("help.txt").openStream();
            InputStreamReader reader = new InputStreamReader(file);

            BufferedReader Breader = new BufferedReader(reader);
            String line;

            while( (line = Breader.readLine()) !=null){
                System.out.println(line);
            }
            Breader.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
/* first planned methods
    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        System.out.println("\nThe correct syntax for using Gradle is: [./gradlew run --args='here are my args']");
        System.out.println("For example:  [./gradlew run --args='. --addPlugin=countCommits']");

        System.out.println("\nHere is the list of commands you can type: ");
        System.out.println("[--help] : Display the list of every command and plugin with every argument possible.");
        System.out.println("[--addPlugin] : Adds a plugin.");
        System.out.println("[--loadConfigFile] : Saves your parameters and quits the program.");
        System.out.println("[--justSaveConfigFile] : Loads your parameters from a file.");

        System.exit(0);
    }   

    private static void help_command() {
        System.out.println("\nTo use gradlew the correct syntax is : [./gradlew run --args='here are my args']");
        System.out.println("You can either pass a command in the arguments or the path to a directory.");
        System.out.println("The path to your directory must contain a .git directory or else you will get an error message.");

        System.out.println("\n\n Here is a list of commands you can type: ");

        System.out.println("\n[--help] : Display this exact list.");

        System.out.println("\n[--addPlugin] : Adds a plugin. Takes a plugin name as a parameter.");
        System.out.println("    - [--addPlugin=countCommits] Counts the number of commits made.");

        System.out.println("\n[--loadConfigFile] : Saves your parameters and quits the program.");

        System.out.println("\n[--justSaveConfigFile] : Loads your parameters from a file.");

        System.out.println("");
        System.exit(0);
    }
    */
}
