package com.dili.trace.crack;

import com.dili.ss.java.BI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BIImpl implements BI {
    @Override
    public List<String> gif(String f) {
        return null;
    }

    @Override
    public String gif(String f, String k) {
        return (String) this.gif((Object) f, (Object) k);
    }

    @Override
    public void daeif(String f, String k, Environment env) {
        Object[] args = new Object[]{f, k, env};
        if (args[1] == null) {
            List<String> l = (List) this.gif(args[0], null);
            l.stream().forEach(s -> {
                if (StringUtils.isBlank(s)) {
                    return;
                }
                try {
                    if (s.contains("^")) {
                        String cd = s.substring(0, s.indexOf("^"));
                        String[] cds = cd.split("=");
                        if (cds.length < 2) {
                            B.b.dae(s.substring(s.indexOf("^") + 1, s.length()));
                        } else if (cds[1].equals(((Environment) args[2]).getProperty(cds[0], "false"))) {
                            B.b.dae(s.substring(s.indexOf("^") + 1, s.length()));
                        }
                    } else {
                        B.b.dae(s);
                    }
                } catch (Exception e) {
                }
            });
        }
    }

    private Object gif(Object args0, Object args1) {
        List<String> l = this.gfl(args0.toString());
        if (args1 == null) {
            return l;
        }
        for (String s : l) {
            if (s.contains("^")) {
                String n = s.substring(0, s.indexOf("^"));
                if (n.equalsIgnoreCase(args1.toString())) {
                    return s.substring(s.indexOf("^") + 1, s.length());
                }
            }
        }
        return null;
    }

    private List<String> gfl(String fn) {
        BufferedReader br = null;
        InputStream is = null;
        try {
            Enumeration<URL> enumeration = B.class.getClassLoader().getResources(fn);
            List<String> list = new ArrayList<String>();
            while (enumeration.hasMoreElements()) {
                is = (InputStream) enumeration.nextElement().getContent();
                br = new BufferedReader(new InputStreamReader(is));
                //文件内容格式:spring.redis.pool.max-active=8
                String s;
                while ((s = br.readLine()) != null) {
                    list.add(s);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private String gfs(String fn) {
        try {
            InputStream is = (InputStream) B.class.getClassLoader().getResource(fn).getContent();
            byte[] buffer = new byte[is.available()];
            int tmp = is.read(buffer);
            while (tmp != -1) {
                tmp = is.read(buffer);
            }
            return new String(buffer);
        } catch (Exception e) {
            return null;
        }
    }
}
