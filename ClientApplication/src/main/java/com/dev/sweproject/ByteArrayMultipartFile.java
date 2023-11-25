package com.dev.sweproject;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
public class ByteArrayMultipartFile implements MultipartFile{
    private final byte[] content;
    private final String name;
    private final String originalFilename;
    private final String contentType;


    public ByteArrayMultipartFile(byte[] content, String name, String contentType, String originalFilename) {
        this.content = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isEmpty() {
        return content.length == 0;
    }

    public long getSize() {
        return content.length;
    }

    public byte[] getBytes() throws IOException {
        return content;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("TransferTo is not supported");
    }
}
