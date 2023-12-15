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
    public void connect() throws IOException {}

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(resource);
    }
}
