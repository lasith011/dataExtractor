package org.ucsc.pom;

import org.apache.maven.model.Model;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;

/**
 * Created by lasith on 3/21/17.
 */
public class PomReader {
    Reporting reporting;

    public PomReader() throws IOException, XmlPullParserException {
        reporting = getModel("/media/data/Code/dataExtractor/src/main/resources/pom.xml").getReporting();
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

    public boolean insertPlugin(String pomToModify) {
        try {
            Model toWrite = getModel(pomToModify);
            if (toWrite.getReporting() == null) {
                toWrite.setReporting(reporting);
            } else {
                toWrite.getReporting().getPlugins().addAll(reporting.getPlugins());
            }
            writeModel(pomToModify, toWrite);
        } catch (IOException e) {
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return true;
    }
}
