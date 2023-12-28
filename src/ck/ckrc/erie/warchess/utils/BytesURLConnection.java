package ck.ckrc.erie.warchess.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 这个类用于为字节数组分配url
 */
public class BytesURLConnection extends URLConnection {

    private byte[] resource=null;

    public BytesURLConnection(URL url,byte[] arr){
        super(url);
        resource=arr;
    }

    @Override
    public void connect() {}

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(resource);
    }
}
