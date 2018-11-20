package org.litespring.core.io;

import org.litespring.utils.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private final String filePath;
    private final File file;
    public FileSystemResource(String filePath) {
        Assert.notNull(filePath, "Path must not be null");
        this.filePath = filePath;
        this.file = new File(filePath);
    }

    @Override
    public InputStream getInputStream() throws IOException {
//        return Files.newInputStream(Paths.get(filePath));
        return new FileInputStream(file);
    }

    @Override
    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";

    }
}
