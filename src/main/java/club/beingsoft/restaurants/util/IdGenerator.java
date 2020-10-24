package club.beingsoft.restaurants.util;

import java.util.UUID;

//https://web.archive.org/web/20170810065759/http://www.onjava.com/pub/a/onjava/2006/09/13/dont-let-hibernate-steal-your-identity.html?page=2
public class IdGenerator {
    public static String createId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
