package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgrammingLanguage {
    public final String name;
    public final int size;


    public ProgrammingLanguage(String name) { this.name = name;this.size=0; }
    public ProgrammingLanguage(String name, int size) { this.name = name;this.size = size; }

    public static BufferedReader executeLsCommand(String command) {
        Path gitPath = FileSystems.getDefault().getPath(".");
        ProcessBuilder builder = new ProcessBuilder("./fileScanner",command).directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running ./fileScanner "+command+" .", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader;
    }

    public static List<ProgrammingLanguage> parsePLpercentageCommand() { return parsePLpercentage(executeLsCommand("-s")); }

    public static List<ProgrammingLanguage> parsePLpercentage(BufferedReader reader) {
        var result = new ArrayList<ProgrammingLanguage>();
        Optional<ProgrammingLanguage> PL = parsePL(reader);
        while (PL.isPresent()) {
            result.add(PL.get());
            PL = parsePL(reader);
        }
        return result;
    }


    public static Optional<ProgrammingLanguage> parsePL(BufferedReader input) {
        try {
            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (! isInteger(idChunks[1])) parseError();
            int PLsize= Integer.parseInt(idChunks[1]);
            ProgrammingLanguage res = new ProgrammingLanguage(idChunks[0],PLsize);
            return Optional.of(res);
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    private static boolean isInteger( String input ) {
        try { Integer.parseInt( input );return true; }
        catch( Exception e ) { return false; }
    }
    private static void parseError() {
        throw new RuntimeException("Command didn't give a proper result.");
    }
    public String toString() { return "ProgrammingLanguage{" + "name='" + name + '\'' + ", size=" + size + '}'; }

}
