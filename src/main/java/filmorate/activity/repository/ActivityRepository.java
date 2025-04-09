package filmorate.activity.repository;


import filmorate.activity.model.Activity;

import java.util.List;

public interface ActivityRepository {

    List<Activity> getUserFeed(Long userId);

    void save(Activity activity);

}
