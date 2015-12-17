package com.ylt.plugin.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.ylt.plugin.command.AppendFileCommandAction;
import com.ylt.plugin.command.AppendHTMLCommandAction;
import com.ylt.plugin.vo.XmlItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 不通过handler方法转
 *
 * Created by ylt on 15/3/26.
 */
public class GenerateCodeNoHandlerAction extends GenerateCodeAction {

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        Module module= ModuleUtil.findModuleForFile(file.getVirtualFile(), project);
        List<XmlItem> methods=new ArrayList<XmlItem>();
        getMethodsFromXml(file, editor, methods);
        new AppendFileNoHandlerCommandAction(project,file,methods,module).execute();
        new AppendHTMLCommandAction(project,file,methods,module).execute();

    }

}
