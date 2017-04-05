package com.shawn.microservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Shawn on 2014/5/22.
 */
public class PropertiesTool {
    private PropertiesTool() {
    }

    Properties prop = new Properties();

    public static PropertiesTool newInstance(String fileName) {
        PropertiesTool pt = new PropertiesTool();
        InputStream in = PropertiesTool.class.getClassLoader().getResourceAsStream(
                fileName);
        try {
            pt.prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pt;
    }

    public static PropertiesTool newInstance(InputStream in) {
        PropertiesTool pt = new PropertiesTool();
        try {
            pt.prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pt;
    }

    public String readValue(String key) {
        return prop.getProperty(key);
    }

    /**
     * 读取所有的属性
     * @return
     */
    public Map<String, String> readAll() {
        Map<String, String> resultMap = new HashMap<String, String>();
        Iterator it = prop.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (value != null) {
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }

    
    public Properties getProp() {
		return prop;
	}

	public static void main(String[] args) {
        PropertiesTool pt = PropertiesTool.newInstance("META-INF/solr.properties");
        System.out.println(pt.readValue("solr.server.ip.port"));
    }
}
