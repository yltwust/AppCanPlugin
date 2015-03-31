package com.ylt.plugin.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.ylt.plugin.command.AppendFileCommandAction;
import com.ylt.plugin.vo.XmlItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylt on 15/3/29.
 */
public class GenerateCallbackAction extends BaseGenerateAction {

    public GenerateCallbackAction() {
        super(null);
    }

    public GenerateCallbackAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return super.isValidForClass(targetClass);
    }

    @Override
    protected boolean isValidForFile(Project project, Editor editor, PsiFile file) {
        return super.isValidForFile(project, editor, file);
    }


    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        actionPerformedImpl(project, editor);
    }

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        Module module= ModuleUtil.findModuleForFile(file.getVirtualFile(), project);
        List<XmlItem> methods=new ArrayList<XmlItem>();
//        getIDsFromLayout(file,editor,methods);
        new AppendFileCommandAction(project,file,methods,module).execute();

    }


}
