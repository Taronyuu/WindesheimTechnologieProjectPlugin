package nl.windesheim.codeparser.plugin.services;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.reporting.Report;
import nl.windesheim.reporting.builders.CodeReportBuilder;
import nl.windesheim.reporting.components.CodeReport;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
     * Current Project object, used to get the current open file.
     */
    private final Project project;

    /**
     * The logger object for the current class.
     */
    private final Logger logger;

    /**
     * Wrapper for the CodeParser package.
     * @param project the current project
     */
    public CodeParser(final Project project) {
        this.project = project;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Get the pattern for the currently openend file.
     * @return CodeReport
     */
    public CodeReport findPatternsForCurrentProject() {
        // Get current openend file
        try {
            String stringPath = getCurrentDirectory() + "/";
            logger.info("Using path: " + stringPath);
            Path path = Paths.get(stringPath);

            FileAnalysisProvider analysis = FileAnalysisProvider.getConfiguredFileAnalysisProvider();

            List<IDesignPattern> patterns = analyzeFiles(path, analysis);

            CodeReport codeReport = generateCodeReport(patterns);
            logger.info("REPORTS: " + codeReport.getReport());
            return codeReport;
        }catch (NullPointerException ex){
            return new CodeReport();
        }catch (IllegalStateException ex){
            return new CodeReport();
        }
    }

    /**
     * Generate the code report for the given patterns.
     * @param patterns List of the paterns.
     * @return CodeReport
     */
    private CodeReport generateCodeReport(final List<IDesignPattern> patterns) {
        CodeReportBuilder codeReportBuilder = Report.create();
        for (IDesignPattern p : patterns) {
            codeReportBuilder.addFoundPatternBuilder(Report.getMapper().getBuilder(p));
        }

        return codeReportBuilder.buildReport();
    }

    /**
     * Analyze the current file.
     * @param file current file.
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
     * @return String
     */
    @NotNull
    private String getCurrentDirectory() {
        VirtualFile file = ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0];
        return file.getPath();
    }
}
