package co.com.bancolombia.model.franchise.model;

public class ProductModel {
    private String id;
    private String name;
    private Integer stock;
    private String franchiseId;

    public ProductModel(String id, String name, Integer stock, String franchiseId) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.franchiseId = franchiseId;
    }

    public ProductModel () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }
}
