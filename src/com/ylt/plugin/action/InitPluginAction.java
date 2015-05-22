package com.ylt.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.IOException;

/**
 * Created by ylt on 15/3/22.
 */
public class InitPluginAction extends AnAction {

    private VirtualFile moduleBaseDir;
    private String moduleName;
    VirtualFile euexClass = null;
    VirtualFile jsConstFile=null;
    Module module;

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
//        e.getData(CommonData
        module = ModuleUtil.findModuleForFile(e.getData(CommonDataKeys.VIRTUAL_FILE), project);
        if (module == null || project == null) {
            return;
        }
        moduleName = module.getName();
        String path = module.getModuleFile().getParent().getPath();
        System.out.println(path);
        moduleBaseDir = module.getModuleFile().getParent();
        VirtualFile[] virtualFiles = moduleBaseDir.getChildren();
        try {
            VirtualFile resDir = moduleBaseDir.findChild("res");
            if (resDir == null) {
                resDir = moduleBaseDir.createChildDirectory(null, "res");
            }
            VirtualFile xmlFile = resDir.findChild("xml");
            if (xmlFile == null) {
                xmlFile = resDir.createChildDirectory(null, "xml");
            }
            final VirtualFile finalXmlFile = xmlFile;
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    try {
                        //生成plugin.xml
                        VirtualFile pluginFile = finalXmlFile.findOrCreateChildData(this, "plugin.xml");
                        pluginFile.setBinaryContent(getPluginXml(moduleName).getBytes());
                        //生成config.xml
                        VirtualFile configFile = finalXmlFile.findOrCreateChildData(this, "config.xml");
                        configFile.setBinaryContent(getPluginXml(moduleName).getBytes());
                        //生成java类
                        createMainClass(moduleBaseDir, module.getName());
                        euexClass.setBinaryContent(getMainClassCode(moduleName).getBytes());
                        jsConstFile.setBinaryContent(getJsConstClassCode(moduleName).getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (IOException e1) {
            System.out.println(e1.toString());
        }

    }


    private void createMainClass(VirtualFile baseDir, String moduleName) {
        try {
            VirtualFile srcFile = baseDir.findChild("src");
            if (srcFile == null) {
                srcFile = baseDir.createChildDirectory(null, "src");
            }
            VirtualFile orgFile = srcFile.findChild("org");
            if (orgFile == null) {
                orgFile = srcFile.createChildDirectory(null, "org");
            }
            VirtualFile zywxFile = orgFile.findChild("zywx");
            if (zywxFile == null) {
                zywxFile = orgFile.createChildDirectory(null, "zywx");
            }
            VirtualFile wbpalmstarFile = zywxFile.findChild("wbpalmstar");
            if (wbpalmstarFile == null) {
                wbpalmstarFile = zywxFile.createChildDirectory(null, "wbpalmstar");
            }
            VirtualFile pluginFile = wbpalmstarFile.findChild("plugin");
            if (pluginFile == null) {
                pluginFile = wbpalmstarFile.createChildDirectory(null, "plugin");
            }

            String mainClassName = getMainClassByProjectName(moduleName);
            VirtualFile projectFile = pluginFile.findChild(moduleName.toLowerCase());
            if (projectFile == null) {
                projectFile = pluginFile.createChildDirectory(null, moduleName.toLowerCase());
            }

            euexClass=projectFile.findOrCreateChildData(null, mainClassName + ".java");
            jsConstFile=projectFile.findOrCreateChildData(null,"JsConst.java");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMainClassByProjectName(String projectName) {
        return projectName.replace("uex", "EUEx");
    }

    private String getPluginXml(String projectName) {
        StringBuilder content = new StringBuilder();
        content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<uexplugins>\n" +
                "    <plugin\n" +
                "        className=")
                .append("\"org.zywx.wbpalmstar.plugin.")
                .append(projectName.toLowerCase())
                .append(".")
                .append(getMainClassByProjectName(projectName))
                .append("\" uexName=\"")
                .append(projectName)
                .append("\" >\n" +
                        "        <method name=\"\" type=\"012\" params=\"param1|param2\"/>\n" +
                        "</plugin>\n" +
                        "</uexplugins>");

        return content.toString();
    }

    private String getJsConstClassCode(String moduleName){
        StringBuilder content=new StringBuilder();
        content.append("package org.zywx.wbpalmstar.plugin.")
                .append(moduleName.toLowerCase())
                .append(";\n\n")
                .append("public class JsConst {\n}\n");
        return content.toString();
    }

    private String getMainClassCode(String moduleName) {
        StringBuilder content = new StringBuilder();
        content.append("package org.zywx.wbpalmstar.plugin.")
                .append(moduleName.toLowerCase())
                .append(";\n\n")
                .append("import android.content.Context;\n" +
                        "import android.content.Intent;\n" +
                        "import android.os.Bundle;\n" +
                        "import android.os.Message;\n" +
                        "import android.text.TextUtils;\n" +
                        "\n" +
                        "import org.json.JSONException;\n" +
                        "import org.json.JSONObject;\n" +
                        "import org.zywx.wbpalmstar.engine.EBrowserView;\n" +
                        "import org.zywx.wbpalmstar.engine.universalex.EUExBase;\n\n")
                .append("public class ").append(getMainClassByProjectName(moduleName)).append(" extends EUExBase {\n\n")
                .append("    private static final String BUNDLE_DATA = \"data\";\n\n")
                .append("    public ").append(getMainClassByProjectName(moduleName))
                .append("(Context context, EBrowserView eBrowserView) {\n" +
                        "        super(context, eBrowserView);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    protected boolean clean() {\n" +
                        "        return false;\n" +
                        "    }\n" +
                        "    \n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void onHandleMessage(Message message) {\n" +
                        "        if(message == null){\n" +
                        "            return;\n" +
                        "        }\n" +
                        "        Bundle bundle=message.getData();\n" +
                        "        switch (message.what) {\n\n" +
                        "        default:\n" +
                        "                super.onHandleMessage(message);\n" +
                        "        }\n" +
                        "    }\n\n" +
                        "    private void callBackPluginJs(String methodName, String jsonData){\n" +
                        "        String js = SCRIPT_HEADER + \"if(\" + methodName + \"){\"\n" +
                        "                + methodName + \"('\" + jsonData + \"');}\";\n" +
                        "        onCallback(js);\n" +
                        "    }\n\n"+
                        "}\n");

        return content.toString();
    }
}
