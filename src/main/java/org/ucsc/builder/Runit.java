package org.ucsc.builder;

import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.ucsc.pom.PomReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by lasith on 3/21/17.
 */
public class Runit implements Runnable{
    static String PROJECT_DIR_PATH = "/media/video/EDU/repos";
    private String gitPath = null;

    public Runit(String gitPath) throws IOException, XmlPullParserException {
        this.gitPath = gitPath;
        //String projectPath = cloneGitRepo(gitPath);
        //modifyPom(projectPath);
    }

    private void modifyPom(String projectPath) throws IOException, XmlPullParserException {
        PomReader reader = new PomReader();
        boolean inserted = reader.insertPlugin(projectPath + "/pom.xml");
        if (inserted) {
            buildRepo(projectPath);
        }
    }

    private String cloneGitRepo(String gitPath) {
        String[] gitURLProps = gitPath.split("/");
        String projectName = gitURLProps[gitURLProps.length - 1];
        String projectPath = PROJECT_DIR_PATH + "/" + projectName;

        try {
            Git.cloneRepository().setURI(gitPath).setDirectory(new File(projectPath)).call();
            System.out.println(gitPath + " cloned successfully to : " + projectPath);
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        return projectPath;
    }

    private void buildRepo(String projectPath) {
        System.out.println("Building Repository at : " + projectPath);
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectPath + "/pom.xml"));
        request.setGoals(Arrays.asList("clean", "compile", "site", "-Ddependency.locations.enabled=false"));
        InvocationResult result = null;
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("/usr/share/maven"));
        try {
            result = invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }

        if (result.getExitCode() != 0) {
            if (result.getExecutionException() != null) {
                System.out.println("Failed to build project : " + result.getExecutionException());
                //throw new PublishException(  );
            } else {
                System.out.println("Failed to build project : " + result.getExitCode());
                //throw new PublishException("Failed to publish site. Exit code: ");}
            }
        }
    }

    @Override
    public void run() {
        String projectPath = cloneGitRepo(this.gitPath);
        try {
            modifyPom(projectPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
