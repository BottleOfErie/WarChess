package ck.ckrc.erie.warchess.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

public class BytesURLHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new BytesURLConnection(u,ResourceSerialization.resourceMap.get(u.getPath().substring(1)));
    }
}
