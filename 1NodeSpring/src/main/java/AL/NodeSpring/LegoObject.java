package AL.NodeSpring;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LegoObject {
    int id;
    String name;
    int parentId;

    /*public Lego(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }*/
}
