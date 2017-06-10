package org.ucsc;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.ucsc.builder.Runit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lasith on 3/21/17.
 */
public class Main {
    public static final String JAVA = "java";

    public static void main(String[] args) throws IOException, XmlPullParserException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<String> repos;
        /*
        GitClient gitClient = new GitClient("d372d108770b94f148f66fe5dc4b4dcfb49685da");
        repos = gitClient.getRepositories("<1000", ">2000", JAVA);
        for (String s : repos) {
            System.out.println(s);
        }
        */
        repos = new ArrayList<>(Arrays.asList(
                "https://github.com/lasith011/find-bug-sample.git",
                "https://github.com/expectedbehavior/gauges-android.git",
                "https://github.com/gwtbootstrap/gwt-bootstrap.git",
                "https://github.com/google/guava.git",
                "https://github.com/EnterpriseQualityCoding/FizzBuzzEnterpriseEdition.git",
                "https://github.com/winterbe/java8-tutorial.git",
                "https://github.com/dropwizard/metrics.git"
        ));
        for (String s : repos) {
            Runit runit = new Runit(s);
            executor.execute(runit);
        }
    }
}
