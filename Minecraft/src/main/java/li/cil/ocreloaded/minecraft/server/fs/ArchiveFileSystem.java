package li.cil.ocreloaded.minecraft.server.fs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import li.cil.ocreloaded.core.machine.PathUtil;
import li.cil.ocreloaded.core.machine.fs.FileHandle;
import li.cil.ocreloaded.core.machine.fs.FileSystem;
import li.cil.ocreloaded.minecraft.server.fs.FSStreamHandleTracker.InputStreamSupplier;

public class ArchiveFileSystem implements FileSystem {

    private final FSStreamHandleTracker streamHandleTracker = new FSStreamHandleTracker();

    private final Archive archive;
        
    public ArchiveFileSystem(Archive archive) {
        this.archive = archive;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean exists(String path) {
        return archive.entries.keySet().stream()
            .anyMatch(entry ->
                entry.startsWith(PathUtil.minimizePath(archive.prefix + "/" + path) + "/")
                || entry.equals(PathUtil.minimizePath(archive.prefix + "/" + path)));
    }

    @Override
    public boolean isDirectory(String path) {
        // TODO: A bit of a hack, may not detect empty directories correctly
        String fixedPath = PathUtil.minimizePath(archive.prefix + "/" + path) + "/";
        return archive.entries.keySet().stream()
            .anyMatch(entry -> entry.startsWith(fixedPath) && entry.length() > fixedPath.length());
    }

    @Override
    public List<String> list(String path) {
        String fixedPath = PathUtil.minimizePath(archive.prefix + "/" + path);

        return archive.entries.keySet().stream()
            .filter(entry -> entry.startsWith(fixedPath))
            .map(entry -> entry.substring(fixedPath.length() + 1))
            .map(entry -> entry.split("/")[0])
            .distinct()
            .toList();
    }

    @Override
    public int open(String path, Mode mode) throws IOException {
        String fixedPath = PathUtil.minimizePath(archive.prefix + "/" + path);

        ArchiveEntry entry = archive.entries.get(fixedPath);
        if (entry == null) {
            return -1;
        }

        return streamHandleTracker.openInput(entry.contents(), (int) entry.size());
    }

    @Override
    public FileHandle getHandle(int handle) {
        return streamHandleTracker.getHandle(handle);
    }

    @Override
    public void close() throws IOException {
        streamHandleTracker.close();
    }

    public static ArchiveFileSystem fromZip(InputStream inputStream, String prefix) throws IOException {
        Map<String, ArchiveEntry> entries = new HashMap<>();
        ZipInputStream zip = new ZipInputStream(inputStream);

        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            byte[] data = new byte[(int) entry.getSize()];
            zip.read(data);
            entries.put(entry.getName(), new ArchiveEntry(
                entry.getName(), entry.getSize(),
                () -> new ByteArrayInputStream(data)));
        }

        return new ArchiveFileSystem(new Archive(entries, prefix));
    }

    public static record ArchiveEntry(String name, long size, InputStreamSupplier contents) {}

    public static record Archive(Map<String, ArchiveEntry> entries, String prefix) {}

}
