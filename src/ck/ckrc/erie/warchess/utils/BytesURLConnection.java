package ck.ckrc.erie.warchess.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
