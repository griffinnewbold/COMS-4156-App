package com.dev.sweproject;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * The ByteArrayMultipart class represents a component for encapsulating the properties
 * of a file in memory as a byte array. It implements the MultipartFile interface, providing
 * convenient methods for handling and processing file data. This class is designed to facilitate
 * in-memory manipulation of file-like data.
 *
 * @see <a href="https://www.baeldung.com/java-convert-byte-array-to-multipartfile">
 *   Guide to implementing the MultipartFile interface</a>
 */
public class ByteArrayMultipartFile implements MultipartFile {
    private final byte[] content;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    /**
     * Constructs a new ByteArrayMultipart object representing a part of a multi-part request.
     *
     * @param content           The binary content of the part.
     * @param name              The name of the part.
     * @param originalFilename  The original filename of the associated file.
     * @param contentType       The type of content within the part, specified as a MIME type.
     */
    public ByteArrayMultipartFile(byte[] content, String name, String contentType, String originalFilename) {
        this.content = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    /**
     * Get the name of this ByteArrayMultipart part.
     *
     * @return The name of the part.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the original filename of the associated file in this ByteArrayMultipart part.
     *
     * @return The original filename.
     */
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * Get the content type of this ByteArrayMultipart part.
     *
     * @return The content type, specified as a MIME type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Check if the ByteArrayMultipart part is empty (has no content).
     *
     * @return true if the part is empty, false otherwise.
     */
    public boolean isEmpty() {
        return content.length == 0;
    }

    /**
     * Get the size of the content in this ByteArrayMultipart part.
     *
     * @return The size of the content in bytes.
     */
    public long getSize() {
        return content.length;
    }

    /**
     * Get the binary content of this ByteArrayMultipart part as a byte array.
     *
     * @return The content as a byte array.
     */
    public byte[] getBytes() throws IOException {
        return content;
    }

    /**
     * Get an input stream to read the content of this ByteArrayMultipart part.
     *
     * @return An InputStream for reading the content.
     */
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    /**
     * Transfer the content of this ByteArrayMultipart part to a specified File.
     *
     * @param dest The File to which the content should be transferred.
     * @throws IllegalStateException If the transfer cannot be performed.
     */
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("TransferTo is not supported");
    }
}
