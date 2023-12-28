package ck.ckrc.erie.warchess.utils;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * 这个类用于为字节数组分配url
 */
public class BytesURLHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) {
        return new BytesURLConnection(u,ResourceSerialization.resourceMap.get(u.getPath().substring(1)));
    }
}
