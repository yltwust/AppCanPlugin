package com.ylt.plugin.command;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.ylt.plugin.vo.XmlItem;

import java.util.List;

/**
 * Created by ylt on 15/5/21.
 */
public class AppendHTMLCommandAction extends WriteCommandAction<PsiFile> {

    /**
     * Rules set to add.
     */
    private final List<XmlItem> content;
    private final Module module;
    private PsiFile htmlIndexFile;
    private HtmlTag bodyHtmlTag;
    private HtmlTag headHtmlTag;
    public AppendHTMLCommandAction(Project project, PsiFile file, List<XmlItem> content, Module module) {
        super(project, file);
        this.content = content;
        this.module = module;
        String indexPath="index.html";
        PsiFile[] files = FilenameIndex.getFilesByName(project,indexPath , new EverythingGlobalScope(project));
        if (files != null && files.length > 0) {
            for (PsiFile psiFile:files){
                if (ModuleUtil.findModuleForFile(psiFile.getVirtualFile(), project).getModuleFilePath().equals(module.getModuleFilePath())) {
                    htmlIndexFile = files[0];
                }
              }
         }

    }

    @Override
    protected void run(Result<PsiFile> result) throws Throwable {
        getBodyAndHead();
    }

    private void generateBody(){

    }



    private void getBodyAndHead() {
        PsiElement[] rootElement=htmlIndexFile.getChildren();
        PsiElement[] rootChildren=rootElement[0].getChildren();
        for (PsiElement psiElement:rootChildren){
            if (psiElement instanceof HtmlTag){
                HtmlTag htmlTag= (HtmlTag) psiElement;
                PsiElement[] htmlChildren=htmlTag.getChildren();
                for (PsiElement htmlChild:htmlChildren){
                    //<html>标签里面的内容
                    if (htmlChild instanceof HtmlTag){
                        HtmlTag htmlChildTag= (HtmlTag) htmlChild;
                        if (htmlChildTag.getText().equals("body")){
                            bodyHtmlTag=htmlChildTag;
                        }else if (htmlChildTag.getText().equals("head")){
                            headHtmlTag=htmlChildTag;
                        }
                    }
                }
            }
        }
    }

}
