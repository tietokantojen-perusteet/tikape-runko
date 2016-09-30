package tikape.runko.domain;

public class SubCategory {

    public int subCategoryId;
    public int categoryId;
    public String name;
    public String description;

    public SubCategory(int categoryId, int subCategoryId, String name) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public SubCategory setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return subCategoryId + ": " + name + " (" + description + ")";
    }

}
