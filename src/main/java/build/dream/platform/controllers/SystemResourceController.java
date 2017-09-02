package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.PackageUtils;
import build.dream.platform.annotations.SystemResource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/systemResource")
public class SystemResourceController extends BasicController {
    @RequestMapping(value = "/listSystemResources")
    @ResponseBody
    public String listSystemResources() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            List<Class<?>> classes = PackageUtils.getAllClasses(this.getClass().getPackage(), true);
            List<Map<String, String>> systemResources = new ArrayList<Map<String, String>>();
            for (Class<?> controllerClass : classes) {
                Controller controllerAnnotation = AnnotationUtils.findAnnotation(controllerClass, Controller.class);
                if (controllerAnnotation != null) {
                    Method[] methods = ReflectionUtils.getAllDeclaredMethods(controllerClass);
                    for (Method method : methods) {
                        SystemResource systemResourceAnnotation = AnnotationUtils.getAnnotation(method, SystemResource.class);
                        if (systemResourceAnnotation != null) {
                            Map<String, String> systemResource = new LinkedHashMap<String, String>();
                            systemResource.put("name", systemResourceAnnotation.name());
                            systemResource.put("code", systemResourceAnnotation.code());
                            systemResource.put("controllerName", systemResourceAnnotation.controllerName());
                            systemResource.put("actionName", systemResourceAnnotation.actionName());
                            systemResource.put("remark", systemResourceAnnotation.remark());
                            systemResources.add(systemResource);
                        }
                    }
                }
            }
            apiRest = new ApiRest();
            apiRest.setData(systemResources);
            apiRest.setMessage("操作成功！");
            apiRest.setSuccessful(true);
        } catch (Exception e) {
            LogUtils.error("获取权限列表失败", controllerSimpleName, "listSystemResources", e, requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }
}
