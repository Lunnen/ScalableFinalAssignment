package AL.NodeSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class LegoService {

    @Autowired
    LegoRepo repository;

    public Collection<LegoObject> getAllLego() {
        System.out.println("HEJ JAG VISAR ALLT");

        return repository.getAllLego().stream()
                .sorted(Comparator.comparingInt(LegoObject::getId)).collect(Collectors.toList());

    }

    public Collection<LegoObject> getSearchedLego(String searchedText) {

        System.out.println("HEJ JAG SÖKER");

        return repository.getAllLego()
                .stream()
                .filter(x -> x.getName().toLowerCase().matches(".*" + searchedText.toLowerCase() + ".*") ||
                        searchedText.equals(Integer.toString(x.getId())) ||
                        searchedText.equals(Integer.toString(x.getParentId()))
                )
                .collect(Collectors.toList());
    }
}
