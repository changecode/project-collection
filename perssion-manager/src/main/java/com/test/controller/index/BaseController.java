package com.test.controller.index;

import com.test.entity.ResFormMap;
import com.test.entity.UserFormMap;
import com.test.mapper.ResourcesMapper;
import com.test.plugin.PageView;
import com.test.util.Common;
import com.test.util.FormMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * base controller
 */
public class BaseController {
    @Inject
    private ResourcesMapper resourcesMapper;

    public PageView pageView = null;

    public PageView getPageView(String pageNow, String pageSize, String orderby) {
        if (Common.isEmpty(pageNow)) {
            pageView = new PageView(1);
        } else {
            pageView = new PageView(Integer.parseInt(pageNow));
        }
        if (Common.isEmpty(pageSize)) {
            pageSize = "10";
        }
        pageView.setPageSize(Integer.parseInt(pageSize));
        pageView.setOrderby(orderby);
        return pageView;
    }

    public <T> T toFormMap(T t, String pageNow, String pageSize, String orderby) {
        @SuppressWarnings("unchecked")
        FormMap<String, Object> formMap = (FormMap<String, Object>) t;
        formMap.put("paging", getPageView(pageNow, pageSize, orderby));
        return t;
    }

    /**
     * 获取返回某一页面的按扭组,
     *
     * @return Class<T>
     * @throws Exception
     */
    public List<ResFormMap> findByRes() {
        String id = getPara("id");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserFormMap userFormMap = (UserFormMap) Common.findUserSession(request);
        int userId = userFormMap.getInt("id");
        ResFormMap resQueryForm = new ResFormMap();
        resQueryForm.put("parentId", id);
        resQueryForm.put("userId", userId);
        List<ResFormMap> rse = resourcesMapper.findRes(resQueryForm);
        //List<ResFormMap> rse = resourcesMapper.findByAttribute("parentId", id, ResFormMap.class);
        for (ResFormMap resFormMap : rse) {
            Object o = resFormMap.get("description");
            if (o != null && !Common.isEmpty(o.toString())) {
                resFormMap.put("description", Common.stringtohtml(o.toString()));
            }
        }
        return rse;
    }

    /**
     * 获取页面传递的某一个参数值,
     */
    public String getPara(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(key);
    }

    /**
     * 获取页面传递的某一个数组值,
     *
     * @return Class<T>
     * @throws Exception
     */
    public String[] getParaValues(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameterValues(key);
    }
    /*
	 * @ModelAttribute
	 * 这个注解作用.每执行controllor前都会先执行这个方法
	 * @param request
	 * @throws Exception 
	 * @throws  
	 */
	/*@ModelAttribute
	public void init(HttpServletRequest request){
		String path = Common.BACKGROUND_PATH;
		Object ep = request.getSession().getAttribute("basePath");
		if(ep!=null){
			if(!path.endsWith(ep.toString())){
				Common.BACKGROUND_PATH = "/WEB-INF/jsp/background"+ep;
			}
		}
		
	}*/

    /**
     * 获取传递的所有参数,
     * 反射实例化对象，再设置属性值
     * 通过泛型回传对象.
     *
     * @return Class<T>
     * @throws Exception
     */
    public <T> T getFormMap(Class<T> clazz) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> en = request.getParameterNames();
        T t = null;
        try {
            t = clazz.newInstance();
            FormMap<String, Object> map = (FormMap<String, Object>) t;
            String order = "", sort = "";
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (nms.endsWith("[]")) {
                    String[] as = request.getParameterValues(nms);
                    if (as != null && as.length != 0 && as.toString() != "[]") {
                        String mname = t.getClass().getSimpleName().toUpperCase();
                        if (nms.toUpperCase().startsWith(mname)) {
                            nms = nms.substring(nms.toUpperCase().indexOf(mname) + 1);
                            map.put(nms, as);
                        }
                    }
                } else {
                    String as = request.getParameter(nms);
                    if (!Common.isEmpty(as)) {
                        String mname = t.getClass().getSimpleName().toUpperCase();
                        if (nms.toUpperCase().startsWith(mname)) {
                            nms = nms.substring(mname.length() + 1);
                            map.put(nms, as);
                        }
                        if (nms.toLowerCase().equals("column")) order = as;
                        if (nms.toLowerCase().equals("sort")) sort = as;
                    }
                }
            }
            if (!Common.isEmpty(order) && !Common.isEmpty(sort))
                map.put("orderby", " order by " + order + " " + sort);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取传递的所有参数,
     * 再设置属性值
     * 通过回传Map对象.
     *
     * @return Class<T>
     * @throws Exception
     */
    public <T> T findHasHMap(Class<T> clazz) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> en = request.getParameterNames();
        T t = null;
        try {
            t = clazz.newInstance();
            FormMap<String, Object> map = (FormMap<String, Object>) t;
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (!"_t".equals(nms)) {
                    if (nms.endsWith("[]")) {
                        String[] as = request.getParameterValues(nms);
                        if (as != null && as.length != 0 && as.toString() != "[]") {
                            map.put(nms, as);
                        }
                    } else {
                        String as = request.getParameter(nms);
                        if (!Common.isEmpty(as)) {
                            map.put(nms, as);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}