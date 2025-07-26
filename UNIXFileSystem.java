import java.util.*;

enum FileType {
    FILE, DIRECTORY, SYMLINK
}

abstract class FileEntity {
    protected String name;
    protected Directory parent;
    protected long createdAt;

    public FileEntity(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public abstract FileType getType();

    public String getName() {
        return name;
    }

    public String getPath() {
        if(parent == null) return "/" + name;
        return parent.getPath() + "/" + name;
    }
}

class File extends FileEntity {
    private long size;;
    private String extension;

    public File(String name, Directory parent, long size, String extension) {
        super(name);
        this.size = size;
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }
    public String getExtension() {
        return extension;
    }
    @Override
    public FileType getType() {
        return FileType.FILE;
    }
}

class Directory extends FileEntity {
    private List<FileEntity> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void addEntity(FileEntity entity) {
        entity.parent = this;
        children.add(entity);
    }

    public List<FileEntity> getChildren() {
        return children;
    }
    @Override
    public FileType getType() {
        return FileType.DIRECTORY;
    }
}


class SearchFilter {
    String nameContains;
    FileType type;
    String extension;
    String pathPrefix;

    public SearchFilter setNameContains(String name) {
        this.nameContains = name;
        return this;
    }

    public SearchFilter setExtension(String ext) {
        this.extension = ext;
        return this;    
    }

    public SearchFilter setType(FileType type) {
        this.type = type;
        return this;
    }
    public SearchFilter setPathPrefix(String path) {
        this.pathPrefix = path;
        return this;
    }

}
