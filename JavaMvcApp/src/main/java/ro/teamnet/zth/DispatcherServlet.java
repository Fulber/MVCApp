package ro.teamnet.zth;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.ClassDeserializer;
import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.api.annotations.MyRequestParam;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 14.07.2016.
 */
public class DispatcherServlet extends HttpServlet {

    /*
     * key: urlPath
     * val: method info
     */
    private HashMap<String, MethodAttributes> allowedMethods = new HashMap<>();

    @Override
    public void init() throws ServletException {

        try {
            Iterable<Class> classes = AnnotationScanUtils.getClasses("ro.teamnet.zth.appl");
            for (Class controller : classes) {
                if (controller.isAnnotationPresent(MyController.class)) {
                    MyController myController = (MyController) controller.getAnnotation(MyController.class);
                    String urlPath = myController.urlPath();

                    Method[] methods = controller.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(MyRequestMethod.class)) {
                            MyRequestMethod myRequestMethod = method.getAnnotation(MyRequestMethod.class);
                            String finalUrlPath = urlPath + myRequestMethod.urlPath() + myRequestMethod.methodType();

                            MethodAttributes methodAttributes = new MethodAttributes();
                            methodAttributes.setControllerClass(controller.getName());
                            methodAttributes.setMethodType(myRequestMethod.methodType());
                            methodAttributes.setMethodName(method.getName());
                            methodAttributes.setParameterTypes(method.getParameterTypes());

                            allowedMethods.put(finalUrlPath, methodAttributes);
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("GET", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("POST", req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("PUT", req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("DELETE", req, resp);
    }

    protected void dispatchReply(String type, HttpServletRequest req, HttpServletResponse resp) {

        try {
            Object r = dispatch(type, req, resp);
            reply(r, req, resp);

        } catch (Exception ex) {
            sendExceptionError(ex, req, resp);
        }
    }

    protected Object dispatch(String type, HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {

        String pathInfo = req.getPathInfo() + type;
        MethodAttributes methodAttributes = allowedMethods.get(pathInfo);
        Object controller = Class.forName(methodAttributes.getControllerClass()).newInstance();
        Method method = controller.getClass().getMethod(methodAttributes.getMethodName(), methodAttributes.getParameterTypes());
        List<Object> args = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(MyRequestParam.class)) {
                String parameterName = parameter.getAnnotation(MyRequestParam.class).name();

                if (parameterName.equals("id")) {
                    args.add(new ObjectMapper().readValue(req.getParameter(parameterName), parameter.getType()));
                } else {
                    String jsonString = req.getReader().readLine();
                    args.add(new ObjectMapper().readValue(jsonString, parameter.getType()));
                }
            }
        }
        return method.invoke(controller, args.toArray());
    }

    protected void reply(Object r, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        resp.getWriter().write(objectMapper.writeValueAsString(r));
    }

    protected void sendExceptionError(Exception ex, HttpServletRequest req, HttpServletResponse resp) {

    }
}
