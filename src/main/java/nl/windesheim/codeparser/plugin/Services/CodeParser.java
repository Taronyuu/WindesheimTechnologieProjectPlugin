package nl.windesheim.codeparser.plugin.Services;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.reporting.Report;
import nl.windesheim.reporting.builders.CodeReportBuilder;
import nl.windesheim.reporting.components.CodeReport;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CodeParser class will handle all the boring actions
 * needed to get a Report object from the actual CodeParser.
 */
public class CodeParser {

    /**
     * Current Project object, used to get the current open file.
     */
    private Project project;

    /**
     * Wrapper for the CodeParser package.
     * @param project the current project
     */
    public CodeParser(final Project project) {
        this.project = project;
    }

    /**
     * Get the pattern for the currently openend file.
     * @return CodeReport
     */
    public CodeReport findPatternForCurrentFile() {
        // Get current openend file
        String path = getCurrentFile();

        File file = new File(path);
        FileAnalysisProvider analysis = FileAnalysisProvider.getConfiguredFileAnalysisProvider();

        List<IDesignPattern> patterns = analyzeFiles(file, analysis);

        return generateCodeReport(patterns);
    }

    /**
     * Generate the code report for the given patterns.
     * @param patterns List of the paterns.
     * @return CodeReport
     */
    private CodeReport generateCodeReport(final List<IDesignPattern> patterns) {
        CodeReportBuilder codeReportBuilder = Report.create();
        for (IDesignPattern p : patterns) {
            try {
                codeReportBuilder.addFoundPatternBuilder(Report.getMapper().getBuilder(p));
            } catch (NullPointerException ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
        }

        return codeReportBuilder.buildReport();
    }

    /**
     * Analyze the current file.
     * @param file current file.
     * @param analysis The analysis provider to be used.
     * @return ArrayList<IDesignPattern>
     */
    private List<IDesignPattern> analyzeFiles(final File file, final FileAnalysisProvider analysis) {
        List<IDesignPattern> patterns = new ArrayList<>();

        try {
            patterns = analysis.analyzeFile(file.toURI().toURL());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return patterns;
    }

    /**
     * Get the current file path from the project.
     * @return String
     */
    @NotNull
    private String getCurrentFile() {
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        return currentFile.getPath();
    }
}
