package hunt.james.hierarchyview.today;

import java.util.List;

/**
 * Created by James on 7/29/2017.
 */

public interface HierarchyDataHelper {

    interface Data {
        List<Data> getHierarchyData();
        String getText();
    }

}