package co.com.bancolombia.model.franchise.model;

import java.util.ArrayList;
import java.util.List;

public class FranchiseModel {

    private String id;
    private String name;
    private List<BranchModel> branches = new ArrayList<>();

    public FranchiseModel(String id, String name, List<BranchModel> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public FranchiseModel() {}

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

    public List<BranchModel> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchModel> branches) {
        this.branches = branches;
    }
}
