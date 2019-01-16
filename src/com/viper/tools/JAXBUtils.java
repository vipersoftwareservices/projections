/*
 * -----------------------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * -----------------------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2008/06/15
 *
 * Copyright 1998-2008 by Viper Software Services
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2008 
 *
 * @note 
 *        
 * -----------------------------------------------------------------------------
 */

package com.viper.tools;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class JAXBUtils {

    private static final Hashtable<String, JAXBContext> contextStore = new Hashtable<String, JAXBContext>();

    private JAXBUtils() {
    }

    public static JAXBUtils getInstance() {
        return new JAXBUtils();
    }

    public static Source readSource(String str) throws Exception {
        return new StreamSource(new java.io.StringReader(str));
    }

    // -------------------------------------------------------------------------
    public static <T> T getObject(Class<T> clazz, Source source) throws Exception {
        return getJAXBContext(clazz).createUnmarshaller().unmarshal(source, clazz).getValue();
    }

    public static <T> T getObject(Class<T> clazz, String str) throws Exception {
        return getObject(clazz, readSource(str));
    }

    public static <T> T getObject(Class<T> clazz, File file) throws Exception {
        return getObject(clazz, new StreamSource(file));
    }

    public static <T> T unmarshal(Class<T> clazz, Reader reader) throws Exception {
        return getObject(clazz, new StreamSource(reader));
    }

    public static <T> T unmarshal(Class<T> clazz, InputStream inputstream) throws Exception {
        return getObject(clazz, new StreamSource(inputstream));
    }

    public static <T> T unmarshal(Class<T> clazz, File file) throws Exception {
        return getObject(clazz, new StreamSource(file));
    }

    // -------------------------------------------------------------------------

    public static <T> Source getSource(T bean, Map<String, Object> properties) throws Exception {
        return new StreamSource(new StringReader(getString(bean, properties)));
    }

    public static <T> String getString(T bean, Map<String, Object> properties) throws Exception {
        if (bean == null) {
            throw new Exception("Passed object is null");
        }
        StringWriter out = new StringWriter();
        marshal(out, bean, properties);
        return out.toString();
    }

    public static <T> void marshal(Writer writer, T bean, Map<String, Object> properties) throws Exception {
        if (bean == null) {
            throw new Exception("Passed object is null");
        }
        createMarshaller(bean.getClass(), properties).marshal(bean, writer);
        writer.flush();
    }

    public static <T> void marshal(File file, T bean, Map<String, Object> properties) throws Exception {
        if (bean == null) {
            throw new Exception("Passed object is null");
        }
        if (!file.getParentFile().mkdirs()) {
            throw new Exception("Directories not created: " + file.getParent());
        }
        createMarshaller(bean.getClass(), properties).marshal(bean, file);
    }

   
    // -------------------------------------------------------------------------

    public static <T> Marshaller createMarshaller(Class<T> clazz, Map<String, Object> properties) throws Exception {

        Marshaller m = getJAXBContext(clazz).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        if (properties != null) {
            for (String name : properties.keySet()) {
                m.setProperty(name, properties.get(name));
            }
        }
        return m;
    }

    public static <T> JAXBContext getJAXBContext(Class<T> clazz) throws Exception {
        String packagename = clazz.getPackage().getName();
        JAXBContext context = contextStore.get(packagename);
        if (context != null) {
            return context;
        }
        String classname = clazz.getName();
        context = contextStore.get(classname);
        if (context != null) {
            return context;
        }
        if (classExists(packagename + ".ObjectFactory")) {
            context = JAXBContext.newInstance(packagename);
            contextStore.put(packagename, context);
        } else {
            context = JAXBContext.newInstance(clazz);
            contextStore.put(classname, context);
        }
        if (context == null) {
            throw new Exception("No JAXBContext for package " + classname);
        }
        return context;
    }

    public static boolean classExists(String classname) {
        try {
            return (Class.forName(classname) != null);
        } catch (Exception ex) {
            return false;
        }
    }

    public static void clearFactoryStore() {
        contextStore.clear();
    }
}
