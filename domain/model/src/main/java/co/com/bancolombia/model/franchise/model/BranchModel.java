package co.com.bancolombia.model.franchise.model;

import java.util.ArrayList;
import java.util.List;

public class BranchModel {
    private String id;
    private String name;
    private String franchiseId;
    private List<ProductModel> products = new ArrayList<>();

    public BranchModel() {}

    public BranchModel(String id, String name, String franchiseId, List<ProductModel> products) {
        this.id = id;
        this.name = name;
        this.franchiseId = franchiseId;
        this.products = products;
    }

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

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
