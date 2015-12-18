package com.ylt.plugin.command;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
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
                    htmlIndexFile =psiFile;
                }
              }
         }

    }

    @Override
    protected void run(Result<PsiFile> result) throws Throwable {
//        getBodyAndHead();
        String html=FileUtil.loadTextAndClose(getClass().getResourceAsStream("/com/ylt/plugin/res/index.template"));
        html=html.replace("\"$FUNCTION$\"",getFunctionString());
        html=html.replace("\"$BODY$\"",getBodyString());
        html=html.replace("\"$CALLBACK_REGISTER$\"",getCallbackRegisterString());
        html=html.replace("\"$CALLBACK\"",getCallbackString());
        html=html.replace("\"$TITLE$\"",module.getName());
        htmlIndexFile.getVirtualFile().setBinaryContent(html.getBytes());
    }

    private String getBodyString(){
        StringBuilder bodyStringBuilder=new StringBuilder();
        for (XmlItem xmlItem:content){
            if (xmlItem.getType()!=2){
                bodyStringBuilder.append("            <input class='btn' type='button' value='").append(xmlItem.getMethodName()).append("' onclick=")
                        .append("'").append(xmlItem.getMethodName()).append("()'>\n\n");
            }
        }
        return bodyStringBuilder.toString();
    }

    private String getCallbackRegisterString(){
        StringBuilder callbackRegister=new StringBuilder();
        for (XmlItem xmlItem:content){
            if (xmlItem.getType()!=0){
                String callbackName = null;
                if (xmlItem.getType()==1){
                    callbackName=xmlItem.getCbMethodName();
                }else if (xmlItem.getType()==2){
                    callbackName=xmlItem.getMethodName();
                }
                callbackRegister.append("            ").append(module.getName()).append(".")
                        .append(callbackName).append(" = ").append(callbackName).append(";\n");
            }
        }
        return callbackRegister.toString();
    }

    private String getFunctionString(){
        StringBuilder functionStringBuilder=new StringBuilder();
        for (XmlItem xmlItem:content){
            if (xmlItem.getType()!=2){
                functionStringBuilder.append("    function\40").append(xmlItem.getMethodName())
                        .append("(){\n        var params = {\n");
                if (xmlItem.getParams()!=null&&xmlItem.getParams().length>0){
                    for (int i = 0; i < xmlItem.getParams().length; i++) {
                        if (i==xmlItem.getParams().length-1){
                            functionStringBuilder.append("            ").append(xmlItem.getParams()[i]).append(":\"\"\n");
                        }else {
                            functionStringBuilder.append("            ").append(xmlItem.getParams()[i]).append(":\"\",\n");
                        }

                    }
                }
                functionStringBuilder.append("        };\n        var data = JSON.stringify(params);\n        ")
                        .append(module.getName()).append(".").append(xmlItem.getMethodName()).append("(data);\n    }\n\n");
            }
        }
        return functionStringBuilder.toString();
    }

    private String getCallbackString(){
        StringBuilder callbackBuilder=new StringBuilder();
        for (XmlItem xmlItem:content) {
            if (xmlItem.getType()!=0) {
                String callbackName = null;
                if (xmlItem.getType()==1){
                    callbackName=xmlItem.getCbMethodName();
                }else if (xmlItem.getType()==2){
                    callbackName=xmlItem.getMethodName();
                }
                callbackBuilder.append("    function\40").append(callbackName).append("(info){\n        ")
                        .append("alert(")
                        .append("'")
                        .append(callbackName)
                        .append(": '+")
                        .append("info);\n    }\n\n");
            }
        }
        return callbackBuilder.toString();
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
