package Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name",unique = true,nullable = false)
    private String name;
    @ManyToMany(mappedBy = "listTags", fetch = FetchType.EAGER)
    private List<Task> listTasks ;

    public Tag() {
        this.listTasks = new ArrayList<>();
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
        this.listTasks = new ArrayList<>();
    }
    public Tag(String name){
        this.name=name;
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
