package dev.nosehad.varos.utils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YMLConfiguration {

    public static YMLConfiguration getYmlConfiguration (String file) {
        return new YMLConfiguration ( file );
    }

    private final String file;
    public YMLConfiguration ( String file) {
        this.file = file;
    }

    public String getString (String target) throws IOException, ExecutionException {
        String content = null;
        List<String> lines = Files.readAllLines( Paths .get ( file ), Charset.defaultCharset());

        for (String line : lines) {
            if( line.contains ( target ) ) {
                if(line.contains ( "\"" )) {
                    content = StringUtils.substringBetween ( line, "\"","\"" );
                }
                else {
                    content = line.replaceAll ( target,"" ).replaceAll ( " ", "" ).replaceAll ( ":", "" ).replaceAll ( "=", "" );
                }
            }
        }
        if(content != null)
            return content;
        else
            throw new ExecutionException ( "Zeile " + target + " wurde nicht gefunden!", new ExecutionException ( new Throwable ( "Bitte überprüfe, ob du die Config beschädigt hast." ) ) );
    }
}
