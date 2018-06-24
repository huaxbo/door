package com.automic.global.util.mvc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MvcAct {

    /**
     * 查询条件注解
     */
    public static final String con_pro = "con_pro";

    /**
     * 查询条件回执hidden
     */
    private Hashtable<String, String> fpros = new Hashtable<String, String>(0);
    /**
     * 全部回执hidden
     */
    private Hashtable<String, String> pros = new Hashtable<String, String>(0);

    /**
     * 绑定properties
     * 
     * @param objs
     * @param pts
     */
    protected void bindProperties(Object[] objs, ModelMap mrlt) {
        if (objs == null || objs.length == 0) {
            return;
        }
        for (int i = 0; i < objs.length; i++) {
            Object obj = objs[i];
            Class<?> c = obj.getClass();
            Field[] fds = c.getDeclaredFields();
            for (int k = 0; k < fds.length; k++) {
                Field fd = fds[k];
                fd.setAccessible(true);
                MvcProAnno mpa = fd.getAnnotation(MvcProAnno.class);
                if (mpa != null) {
                    try {
                        String fv = fetchFiled(fd, obj);
                        if (mpa.value() == null || !mpa.value().equals(con_pro)) {
                            if (fv != null) {
                                fpros.put(fd.getName(), fv);
                            }
                        }
                        if (fv != null) {
                            pros.put(fd.getName(), fv);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        // bind rlt
        mrlt.addAttribute("fpros", fpros);
        mrlt.addAttribute("pros", pros);
    }

    /**
     * 增加个性属性
     * 
     * @param nm
     * @param vl
     */
    protected void addProperties(String nm, String vl) {
        fpros.put(nm, vl);
        pros.put(nm, vl);
    }

    /**
     * 文件上传
     * @param request
     * @return [0]filedName,[1]multipartFile
     * @throws IllegalStateException
     * @throws IOException
     */
    public Object[][] uploadMultipartFile(HttpServletRequest request) throws IllegalStateException, IOException {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            Object[][] arr = new Object[multiRequest.getFileMap().size()][2];
            int idx = 0;
            while (iter.hasNext()) {
                // 取得上传文件
                String nm = iter.next();
                MultipartFile file = multiRequest.getFile(nm);
                arr[idx][0] = nm;
                arr[idx][1] = file;
                idx++;
            }

            return arr;
        }

        return new Object[0][2];
    }

    /**
     * spring多文件转io文件
     * 
     * @param mfile
     * @param nfile
     * @return io文件
     * @throws IllegalStateException
     * @throws IOException
     */
    protected File multi2File(MultipartFile mfile, String nfile) throws IllegalStateException, IOException {
        if (mfile != null) {
            // 取得当前上传文件的文件名称
            String myFileName = mfile.getOriginalFilename();
            // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (myFileName.trim() != "") {
                File localFile = new File(nfile);
                mfile.transferTo(localFile);

                return localFile;
            }
        }

        return null;
    }

    /**
     * @param fd
     * @param obj
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private String fetchFiled(Field fd, Object obj) throws IllegalArgumentException, IllegalAccessException {
        String tp = fd.getType().getSimpleName();
        if (tp.equals("String") || tp.equals("Integer") || tp.equals("int") || tp.equals("Double")
                || tp.equals("double") || tp.equals("Boolean") || tp.equals("boolean") || tp.equals("Long")
                || tp.equals("long") || tp.equals("Float") || tp.equals("float") || tp.equals("Short")
                || tp.equals("short")) {

            Object v = fd.get(obj);

            return v.toString();
        }

        return null;
    }

}
