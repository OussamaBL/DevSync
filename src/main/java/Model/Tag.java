package Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name",unique = true,nullable = false)
    private String name;
    @ManyToMany(mappedBy = "listTags")
    private List<Task> listTasks;

    public Tag() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getListTasks() {
        return listTasks;
    }

    public void setListTasks(List<Task> listTasks) {
        this.listTasks = listTasks;
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
        this.listTasks = new ArrayList<>();
    }
    public Tag(int id){
        this.id=id;
        this.listTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", listTasks=" + listTasks +
                '}';
    }
}
