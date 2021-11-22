package AL.NodeSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@RestController
public class LegoController {

    @Autowired
    LegoService service;

    @GetMapping("/all")
    public Collection<LegoObject> getAllLegos(HttpServletResponse response) {
        return service.getAllLego();
    }

    @PutMapping("/search/{searchValue}")
    public Collection<LegoObject>  getSearch(@PathVariable("searchValue") String searchValue, HttpServletResponse response) {
        return service.getSearchedLego(searchValue);
    }
}
