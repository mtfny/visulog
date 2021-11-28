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
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import java.lang.reflect.*;
import java.net.URISyntaxException;

public class CLILauncher {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //Creates a new configuration with the gitpath and the plugins as arguments from main.
        var config = makeConfigFromCommandLineArgs(args);

        //If the configuration was correctly created :
        if (config.isPresent()) {
            
            //We create a new analyzer object using this configuration as an argument.
            var analyzer = new Analyzer(config.get());
            //The analyzer then runs all the plugins.
            var results = analyzer.computeResults();

            boolean openhtml = config.get().getOpenHtml();

            
            //If we have results that can be opened with an html page and we wrote the command '--o' wich
            //makes openhtml true then we can open the html page.   
            if (results.getSubResults().isEmpty() == false) {
            	writeHTML writer = new writeHTML(results);
                writer.createhtmlFile();
                
                if(openhtml)
                	writer.openHtlmFile();
            }
        }
        else helpexit_txt();

        //displayHelpAndExit();
    }

    //So what this code do is take the command line we entered from the terminal, and make a new configuration.
    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        //gitPath takes the current path visulog is located at, which is it's own folder.
        String gitPath = FileSystems.getDefault().getPath(".").toString();
        String configFilePath = FileSystems.getDefault().getPath(".").toString();
        boolean openHtml = false;
        boolean saveConfigFile = false;

        var plugins = new HashMap<String, PluginConfig>();
        PluginConfig pluginConfig = new PluginConfig();

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
                        case "--o" :
                            openHtml = true;
                            break;

                        case "--help" :
                            help_txt();
                           // help_command();
                            break;

                        case "--addPlugin":
                        	if(plugins.containsKey(pValue)) {
                        		return Optional.empty();
                        	}else {
                        		addPlugin(pValue, plugins);
                        	}
                            break;
                            
                        case "--justSaveConfigFile":
                        	saveConfigFile = true;
                        	configFilePath = "../" + pValue;
                        	break;
                        	
                        case "--loadConfigFile":
                        	configFilePath = "../" + pValue;
                            Configuration res = Configuration.loadConfigFile(configFilePath);
                            if(res != null && check_directory_exists(res.getGitPath().toString()) == true)
                            	return Optional.of(res);
                            
                        default:
                            return Optional.empty();
                    }
                }
            }
            
            else if(arg.startsWith("-")) {
            	String[] parts = arg.split("=");
                if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) 
                	pluginConfig.addSetting(parts[0], parts[1]);
                
                else if(parts.length == 1 && parts[0].length() > 0)
                	pluginConfig.addOption(parts[0]);
            }
            
            else {
                //If we aren't passing a command that exists. We are changing the value of gitpath to the 
                //argument we passed to check if we are passing a directory in our command line.
                gitPath = FileSystems.getDefault().getPath(arg).toString();
            }
        }
        //At the end we verify that everything is working by checking if the path in gitPath is
        //in an existing directory or not.
        if (check_directory_exists(gitPath)) {
        	Configuration res = new Configuration(gitPath, configFilePath, plugins, openHtml);
        	res.setPluginConfig(pluginConfig);
        	
        	if(saveConfigFile)
        		res.saveConfigFile();
        	
        	return Optional.of(res);
        }

        else return Optional.empty();
    }   

    public static void addPlugin(String pluginName, HashMap<String, PluginConfig> plugins) {
        plugins.put(pluginName, new PluginConfig());
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
            e.printStackTrace();
        }
    }
}
