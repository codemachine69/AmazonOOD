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
    private long size;
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

class FileSearcher {

    public  List<FileEntity> search(Directory root, SearchFilter filter) {
        List<FileEntity> results = new ArrayList<>();
        dfs(root, filter, results);
        return results;
    }

    private void dfs(FileEntity entity, SearchFilter filter, List<FileEntity> result) {
        if(matches(entity, filter)) {
            result.add(entity);
        }

        if(entity instanceof Directory) {
            for(FileEntity child : ((Directory) entity).getChildren()) {
                dfs(child, filter, result);
            }
        }
    }

    private boolean matches(FileEntity entity, SearchFilter filter) {
        if(filter.nameContains != null && !entity.getName().contains(filter.nameContains)) {
            return false;
        }
        if(filter.pathPrefix != null && !entity.getPath().startsWith(filter.pathPrefix)) {
            return false;
        }
        if(filter.type != null && entity.getType() != filter.type) {
            return false;
        }
        if(filter.extension != null) {
            if(!(entity instanceof File) || !((File) entity).getExtension().equals(filter.extension)) {
                return false;
            }
        }
        return true;
    }
}

class FileSystem {
    private Directory root;

    public FileSystem() {
        this.root = new Directory("root");
    }

    public Directory getRoot() {
        return root;
    }

    public List<FileEntity> search(SearchFilter filter) {
        FileSearcher searcher = new FileSearcher();
        return searcher.search(root, filter);
    }
}
