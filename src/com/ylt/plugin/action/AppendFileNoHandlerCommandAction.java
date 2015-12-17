package com.ylt.plugin.action;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.ylt.plugin.command.AppendFileCommandAction;
import com.ylt.plugin.util.StringUtil;
import com.ylt.plugin.vo.XmlItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ylt on 15/12/17.
 */
public class AppendFileNoHandlerCommandAction extends AppendFileCommandAction {

    /**
     * Builds a new instance of {@link com.ylt.plugin.command.AppendFileCommandAction}.
     * Takes a {@link java.util.Set} of the rules to add.
     *
     * @param project current project
     * @param file    working file
     * @param content rules set
     * @param module
     */
    public AppendFileNoHandlerCommandAction(@NotNull Project project, @NotNull PsiFile file, @NotNull List<XmlItem> content, @NotNull Module module) {
        super(project, file, content, module);
    }


    @Override
    public void addMethodAndFiled(PsiClass psiClass, XmlItem method, int index) {
        psiClass.addBefore(mFactory.createMethodFromText(createMethod(method), psiClass), psiClass.findMethodsByName("callBackPluginJs", true)[0]);
    }

    protected String createMethod(XmlItem method) {
        String methodName = method.getMethodName();
        StringBuilder stringBuilder = new StringBuilder("public void ");
        stringBuilder.append(methodName)
                .append("(String[] params){\n")
                .append("if (params == null || params.length < 1) {\n" +
                        "            errorCallback(0, 0, \"error params!\");\n" +
                        "            return;\n" +
                        "        }\n" +
                        "        String json=params[0];\n");
        if (method.getType() == 1) {
            //cb回调
            stringBuilder.append("JSONObject jsonResult=new JSONObject();\n" +
                    "        try {\n" +
                    "            jsonResult.put(\"\",\"\");")
                    .append("} catch (JSONException e) {\n" +
                            "        }\n")
                    .append("callBackPluginJs(JsConst.CALLBACK_")
                    .append(StringUtil.getStaticFieldName(methodName))
                    .append(", jsonResult.toString());\n");
        }
        stringBuilder.append("    }");
        return stringBuilder.toString();
    }

}
