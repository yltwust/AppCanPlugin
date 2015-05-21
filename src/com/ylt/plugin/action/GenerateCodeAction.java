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
 * Created by ylt on 15/3/26.
 */
public class GenerateCodeAction extends BaseGenerateAction {

    public GenerateCodeAction() {
        super(null);
    }

    public GenerateCodeAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return true;
    }

    @Override
    protected boolean isValidForFile(Project project, Editor editor, PsiFile file) {
        return true;
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
        getMethodsFromXml(file, editor, methods);
        new AppendFileCommandAction(project,file,methods,module).execute();
        new AppendHTMLCommandAction(project,file,methods,module).execute();

    }

    public List<XmlItem> getMethodsFromXml(final PsiFile file, Editor editor, final List<XmlItem> elements) {

//        int offset = editor.getCaretModel().getOffset();
//        editor.getSelectionModel().getSelectedText();
//        PsiElement candidate = file.findElementAt(offset);
//
//        if (candidate!=null){
//            file.
//            XmlItem xmlItem=new XmlItem();
//            xmlItem.setMethodName(candidate.getText());
//            elements.add(xmlItem);
//            return elements;
//        }

        file.accept(new MyXmlRecursiveElementVisitor(elements));

        return elements;
    }

    public class MyXmlRecursiveElementVisitor extends XmlRecursiveElementVisitor{


        private List<XmlItem> elements;

        public MyXmlRecursiveElementVisitor(List<XmlItem> elements){
            this.elements=elements;
        }

        @Override
        public void visitElement(PsiElement element) {
            super.visitElement(element);

            if (element instanceof XmlTag) {
                XmlTag tag = (XmlTag) element;
                XmlItem item=new XmlItem();
                if (tag.getName().equals("method")){
                    XmlAttribute methodName=tag.getAttribute("name",null);
                    if (methodName!=null){
                        item.setMethodName(methodName.getValue());
                        XmlAttribute typeAttribute=tag.getAttribute("type");
                        if (typeAttribute!=null){
                            item.setType(Integer.parseInt(typeAttribute.getValue()));
                        }
                        XmlAttribute paramsXmlAttribute=tag.getAttribute("params");
                        if (paramsXmlAttribute!=null){
                            item.setParams(paramsXmlAttribute.getValue().split("|"));
                        }
                        elements.add(item);
                    }else{
                        return;
                    }
                }
            }
        }
    }
}
