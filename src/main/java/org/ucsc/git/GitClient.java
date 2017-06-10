package org.ucsc.git;

import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lasith on 3/21/17.
 */
public class GitClient {
    GitHub github;

    public GitClient(String authToken) throws IOException {
        GitHubBuilder gitHubBuilder = new GitHubBuilder();
        gitHubBuilder.withOAuthToken(authToken);
        github = gitHubBuilder.build();
    }

    public List<String> getRepositories(String size, String stars, String language) {
        PagedSearchIterable<GHRepository> searchBuilder =
                github.searchRepositories()
                        .size(size)
                        .stars(stars)
                        .language(language).sort(GHRepositorySearchBuilder.Sort.STARS).list();
        return searchBuilder.asList().stream().map(GHRepository::gitHttpTransportUrl).collect(Collectors.toList());
    }
}
