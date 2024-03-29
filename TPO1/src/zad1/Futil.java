package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        Path startPath = Paths.get(dirName);
        try (FileChannel fileChannel = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.isRegularFile(file);
                    FileChannel srcChannel = FileChannel.open(file);
                    decodeAndTransfer(srcChannel, fileChannel);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void decodeAndTransfer(FileChannel srcChannel, FileChannel destChannel) throws IOException {
        Charset sourceCharset = Charset.forName("Cp1250");
        Charset destCharset = StandardCharsets.UTF_8;
        ByteBuffer buffer = ByteBuffer.allocate((int) srcChannel.size());
        srcChannel.read(buffer);
        buffer.flip();
        CharBuffer charBuffer = sourceCharset.decode(buffer);
        ByteBuffer destBuffer = destCharset.encode(charBuffer);
        destChannel.write(destBuffer);
        srcChannel.close();
    }
}
