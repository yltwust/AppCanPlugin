package com.ylt.plugin;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.ylt.plugin.action.InitPluginAction;
import com.ylt.plugin.action.PluginHTMLCaseGeneratorAction;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ylt on 15/3/22.
 */
public class YltPluginGroup  extends ActionGroup {
    public YltPluginGroup(){
        setPopup(true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent anActionEvent) {
        AnAction[] actions=new AnAction[2];
        actions[0]=new InitPluginAction();
        Presentation presentation=actions[0].getTemplatePresentation();
        presentation.setText("InitModule");
        actions[1]=new PluginHTMLCaseGeneratorAction();
        Presentation presentation1=actions[1].getTemplatePresentation();
        presentation1.setText("...");
        return actions;
    }
}
