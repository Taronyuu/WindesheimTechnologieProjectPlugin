package nl.windesheim.codeparser.plugin.services;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.reporting.Report;
import nl.windesheim.reporting.builders.CodeReportBuilder;
import nl.windesheim.reporting.components.CodeReport;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CodeParser class will handle all the boring actions
 * needed to get a Report object from the actual CodeParser.
 */
public class CodeParser {

    /**
     * The logger object for the current class.
     */
    private final Logger logger;

    /**
     * Wrapper for the CodeParser package.
     */
    public CodeParser() {
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Get the pattern for the currently openend file.
     *
     * @param project the project in which to find design patterns
     * @return CodeReport
     */
    public List<IDesignPattern> findPatternsForProject(final Project project) {
        if (project == null) {
            logger.log(Level.SEVERE, "No project in focus, can't get source root");
            return new ArrayList<>();
        }

        // Get current openend file
        try {
            String root = getProjectSourceRoot(project);

            if (root == null) {
                return new ArrayList<>();
            }

            String stringPath = root + "/";
            logger.info("Using path: " + stringPath);
            Path path = Paths.get(stringPath);

            FileAnalysisProvider analysis = FileAnalysisProvider.getConfiguredFileAnalysisProvider();

            return analyzeFiles(path, analysis);
        } catch (IllegalStateException ex) {
            return new ArrayList<>();
        }
    }

    /**
     * Generate the code report for the given patterns.
     *
     * @param patterns List of the paterns.
     * @return CodeReport
     */
    public CodeReport generateCodeReport(final List<IDesignPattern> patterns) {
        CodeReportBuilder codeReportBuilder = Report.create();
        for (IDesignPattern p : patterns) {
            codeReportBuilder.addFoundPatternBuilder(Report.getMapper().getBuilder(p));
        }

        return codeReportBuilder.buildReport();
    }

    /**
     * Analyze the current file.
     *
     * @param path     current path.
     * @param analysis The analysis provider to be used.
     * @return ArrayList<IDesignPattern>
     */
    private List<IDesignPattern> analyzeFiles(final Path path, final FileAnalysisProvider analysis) {
        List<IDesignPattern> patterns = new ArrayList<>();

        try {
            patterns = analysis.analyzeDirectory(path);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Ops!", e);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Ops!", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ops!", e);
        }

        return patterns;
    }

    /**
     * Get the current file path from the project.
     *
     * @param project the project for which to find the source root
     * @return String
     */
    @NotNull
    private String getProjectSourceRoot(final Project project) {
        VirtualFile[] sourceRoots = ProjectRootManager.getInstance(project).getContentSourceRoots();

        if (sourceRoots.length == 0) {
            return null;
        }

        //TODO this may not work with multiple source roots
        VirtualFile file = sourceRoots[0];

        return file.getPath();
    }
}
