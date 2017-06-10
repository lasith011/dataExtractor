package org.ucsc;


import org.apache.maven.model.Model;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, XmlPullParserException {
        Reporting reporting = getModel("/media/data/Code/dataExtractor/src/main/resources/pom.xml").getReporting();
        Model towrite = getModel("/tmp/pom.xml");
        if (towrite.getReporting() == null) {
            towrite.setReporting(reporting);
        } else {
            towrite.getReporting().getPlugins().addAll(reporting.getPlugins());
        }
        writeModel("/tmp/pom.xml", towrite);

    }

    private static Model getModel(String path) throws IOException, XmlPullParserException {
        Reader reader = new FileReader(path);
        MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
        return xpp3Reader.read(reader);
    }

    public static void writeModel(String path, Model model)
            throws IOException {
        Writer writer = null;

        try {
            writer = new FileWriter(path);

            MavenXpp3Writer pomWriter = new MavenXpp3Writer();

            pomWriter.write(writer, model);
        } finally {
            IOUtil.close(writer);
        }
    }

}
