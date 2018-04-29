package nl.windesheim.codeparser.plugin.Services;

import com.intellij.notification.Notification;
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

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class CodeParser {

    private Project project;

    public CodeParser(Project project) {
        this.project = project;
    }

    public CodeReport findPatternForCurrentFile() {
        // Get current openend file
        String path = getCurrentFile();

        File file = new File(path);
        FileAnalysisProvider analysis = FileAnalysisProvider.getConfiguredFileAnalysisProvider();

        ArrayList<IDesignPattern> patterns = analyzeFiles(file, analysis);

        return generateCodeReport(patterns);
    }

    private CodeReport generateCodeReport(ArrayList<IDesignPattern> patterns) {
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

    private ArrayList<IDesignPattern> analyzeFiles(File file, FileAnalysisProvider analysis) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        try {
            patterns = analysis.analyzeFile(file.toURI().toURL());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return patterns;
    }

    @NotNull
    private String getCurrentFile() {
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        return currentFile.getPath();
    }
}
