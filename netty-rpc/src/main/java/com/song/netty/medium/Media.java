package com.song.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.song.netty.utill.Request;
import com.song.netty.utill.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Media {
    public static Map<String, BeanMethod> beanMethodMap;
    static {
        beanMethodMap = new HashMap<String, BeanMethod>();
    }
    private static Media m = null;
    private Media(){

    }

    public static Media newInstance(){
        if (m==null){
            m = new Media();
        }
        return m;
    }

    public Response process(Request request){
        Response result = new Response();
        try{
            String command = request.getCommand();
            BeanMethod beanMethod = beanMethodMap.get(command);
            if(beanMethod==null){
                return null;
            }
            Method method = beanMethod.getMethod();
            Object bean = beanMethod.getBean();
            Object content = request.getContext();
            Class paramType = method.getParameterTypes()[0];
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content),paramType);

            result.setContent(method.invoke(bean,args));
            result.setId(request.getId());
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
