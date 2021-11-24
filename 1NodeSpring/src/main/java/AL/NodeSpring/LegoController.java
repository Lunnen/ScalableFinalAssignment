package AL.NodeSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Objects;

@RestController
public class LegoController {

    @Autowired
    LegoService service;

    String allowedHostToConnect = "localhost/<unresolved>:6000";

    @GetMapping("/all")
    public Collection<LegoObject> getAllLegos(@RequestHeader HttpHeaders httpHeader) {

        InetSocketAddress connectedHost = httpHeader.getHost();
        boolean checkIfConnectedIsNonNullAndAllowed = connectedHost != null && Objects.equals(connectedHost.toString(), allowedHostToConnect);

        if (checkIfConnectedIsNonNullAndAllowed){
            return service.getAllLego();
        }
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED!");
    }

    @PutMapping("/search/{searchValue}")
    public Collection<LegoObject> getSearch(@PathVariable("searchValue") String searchValue,
                                             @RequestHeader HttpHeaders httpHeader) {

        InetSocketAddress connectedHost = httpHeader.getHost();
        boolean checkIfConnectedIsNonNullAndAllowed = connectedHost != null && Objects.equals(connectedHost.toString(), allowedHostToConnect);

        if (checkIfConnectedIsNonNullAndAllowed){
            return service.getSearchedLego(searchValue);
        }
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED!");

    }
}
