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

public class NumberOfFiles {
    public final Integer filesNumber;

    public NumberOfFiles(Integer filesNumber) {
        this.filesNumber = filesNumber;
    }

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

    public static  Integer parseNumberOfFilesCommand() {
        Optional<NumberOfFiles> number= parseN(executeLsCommand("-n"));
        if(number.isPresent()) return number.get().filesNumber;
        return null;
    }

    public static Optional<NumberOfFiles> parseN(BufferedReader input) {
        try {
            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            line=line.trim();
            if (!isInteger(line)) parseError();

            NumberOfFiles res=new NumberOfFiles(Integer.parseInt(line));
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
        throw new RuntimeException("Wrong command result format.");
    }
    public String toString() { return "NumberOfFiles{" + "filesNumber=" + filesNumber + '}'; }


}


