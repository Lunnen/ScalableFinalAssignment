package AL.NodeSpring;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

@Repository
public class LegoRepo {

    public HashMap<Integer, LegoObject> legoList;

    public LegoRepo() throws FileNotFoundException {
        this.legoList = new HashMap<>();
        readLegoFileInfo();
    }

    public Collection<LegoObject> getAllLego() {
        return legoList.values();
    }

    public void readLegoFileInfo() throws FileNotFoundException {
        File file = new File("themes.csv");

        Scanner fileRead = new Scanner(file);
        // Skip first row
        String textString = fileRead.nextLine();

        while (fileRead.hasNextLine()) {
            textString = fileRead.nextLine();
            String[] row = textString.split(",");

            LegoObject lego = new LegoObject();
            lego.setId(Integer.parseInt(row[0]));
            lego.setName(row[1]);
            // Check if parentId exists, if not - set 0
            lego.setParentId((row.length == 3) ? Integer.parseInt(row[2]) : 0);

            legoList.put(lego.getId(), lego);
        }
        fileRead.close();
    }
}
