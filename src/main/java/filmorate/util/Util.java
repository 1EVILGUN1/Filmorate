package filmorate.util;

import filmorate.exception.NotFoundException;
import filmorate.pubRepository.Repository;

public class Util {

    public static void checkId(Repository repository, Long... ids) {
        for (Long id : ids) {
            if (repository.findById(id).isEmpty()) {
                throw new NotFoundException("Объекта с ID " + id + " не существует");
            }
        }
    }
}
