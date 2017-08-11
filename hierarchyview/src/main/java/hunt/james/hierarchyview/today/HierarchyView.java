package hunt.james.hierarchyview.today;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.HashSet;

import hunt.james.hierarchyview.HierarchyLinearLayout;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyView extends ScrollView implements HierarchyListener.Scroll {

    private HierarchyData hierarchyData;

    //active
    private final int IS_EXPANDING = 1;
    private final int IS_COLLAPSING = 2;


    //states
    private final int NONE = 0;
    private final int EXPAND_NORMAL = 1;
    private final int EXPAND_UP_THEN_DOWN = 2;
    private final int COLLAPSE_NORMAL = 3;

    private int state = NONE;


    private int collapseStartHeight;
    private int expandStopHeight; //height it will be when its done expanding
    private int collapseCurrentHeight, expandCurrentHeight;
    private int initialScrollY;


    private int previousFrameExpandHeight;
    /*private int collapseHeightWhenStoppedScrolling;
    private int scrollYWhenStoppedScrolling;*/
    private boolean scrollWithoutExpand = false;

    private int clickY; //the y value of the element that was clicked
    private int previousClickY;




    private int clickedViewHeight;


    private HashSet<Integer> active;

    public HierarchyView(Context context) {
        super(context);
    }

    public HierarchyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HierarchyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHierarchyData(HierarchyData hierarchyData) {
        this.hierarchyData = hierarchyData;
        init();
    }

    private void init() {
        active = new HashSet<>();

        HierarchyLinearLayout hierarchyLinearLayout = new HierarchyLinearLayout(getContext(), this);
        hierarchyLinearLayout.setHierarchyData(hierarchyData);
        addView(hierarchyLinearLayout);
    }

    public void scrollAndStayInPlace() {

        if (initialScrollY == 0) {
            initialScrollY = getScrollY();
        }

        if (state == NONE) {


            if (active.contains(IS_COLLAPSING)) {
                state = COLLAPSE_NORMAL;

            } else if (active.contains(IS_EXPANDING) && (clickY < previousClickY || previousClickY == 0)) {

                if (clickY + clickedViewHeight + expandStopHeight > initialScrollY + getHeight()) {
                    state = EXPAND_UP_THEN_DOWN;
                }

                if (clickY + clickedViewHeight + expandStopHeight <= getScrollY() + getHeight()) {
                    state = EXPAND_NORMAL;
                }

            }
        }

        animateScroll(state);

    }


    private void animateScroll(int scrollMode) {

        switch (scrollMode) {
            case EXPAND_UP_THEN_DOWN:
                upThenDown();
                break;

            case COLLAPSE_NORMAL:
                collapseNormal();
                break;
        }
    }

    private void collapseNormal() {

        if (active.contains(IS_EXPANDING) && clickY > previousClickY) {

            scrollWithoutExpand = true;


            int left = clickY - collapseStartHeight + collapseCurrentHeight + clickedViewHeight;
            int right = getScrollY();

            //int position = scrollYWhenStoppedScrolling - (collapseHeightWhenStoppedScrolling - collapseCurrentHeight);

            int min = Math.min(expandStopHeight, getHeight());
            min = Math.min(min, expandCurrentHeight);

            //if (left < right && left + Math.min(expandStopHeight, getHeight()) + clickedViewHeight <= getScrollY() + getHeight()) {
            if (left <= right && collapseStartHeight != 0) {

                /*if(collapseHeightWhenStoppedScrolling == 0 && scrollYWhenStoppedScrolling == 0) {
                    collapseHeightWhenStoppedScrolling = collapseCurrentHeight;
                    scrollYWhenStoppedScrolling = getScrollY();
                    //position = scrollYWhenStoppedScrolling - (collapseHeightWhenStoppedScrolling - collapseCurrentHeight);
                }*/

                //scrollTo(0, position);
                scrollBy(0, left - getScrollY());
            } /*else {
                collapseHeightWhenStoppedScrolling = collapseCurrentHeight;
                scrollYWhenStoppedScrolling = getScrollY();
            }*/

        } else if (!active.contains(IS_EXPANDING)) {
            if (scrollWithoutExpand) {
                /*int position = scrollYWhenStoppedScrolling - (collapseHeightWhenStoppedScrolling - collapseCurrentHeight);
                scrollTo(0, position);*/
            }
        }
    }

    private void upThenDown() {

        if (active.contains(IS_COLLAPSING) && (clickY > previousClickY)) {

            state = COLLAPSE_NORMAL;
            animateScroll(state);
            return;
        }

        boolean goUp = false;
        if (clickY + expandCurrentHeight >= getScrollY() + expandStopHeight + clickedViewHeight ) {
            goUp = true;
        }


        if (goUp) {
            int diff = expandCurrentHeight - previousFrameExpandHeight;
            scrollBy(0, diff);
        }

        previousFrameExpandHeight = expandCurrentHeight;
    }

    @Override
    public void setClickY(int y) {
        clickY = y;
    }


    @Override
    public void startExpand(int fullHeight) {
        expandStopHeight = fullHeight;
        active.add(IS_EXPANDING);
    }

    @Override
    public void stopExpand() {
        active.remove(IS_EXPANDING);
        reset();
    }

    @Override
    public void setExpandCurrentHeight(int height) {
        expandCurrentHeight = height;

        if (expandStopHeight != 0)
            scrollAndStayInPlace();
    }

    @Override
    public void startCollapse(int height, int maxTicksCollapse) {
        collapseStartHeight = height;
        active.add(IS_COLLAPSING);
    }

    @Override
    public void stopCollapse() {
        active.remove(IS_COLLAPSING);
        reset();
    }

    @Override
    public void setCollapseCurrentHeight(int height) {
        collapseCurrentHeight = height;
        scrollAndStayInPlace();
    }

    @Override
    public void setHeightOfClickedView(int height) {
        clickedViewHeight = height;
    }

    private void reset() {

        if(active.size() == 0) {

            previousClickY = clickY;

            collapseStartHeight = 0;
            initialScrollY = 0;
            collapseCurrentHeight = 0;
            expandCurrentHeight = 0;
            expandStopHeight = 0;
            clickY = 0;
            clickedViewHeight = 0;

            previousFrameExpandHeight = 0;
            //collapseHeightWhenStoppedScrolling = 0;
            scrollWithoutExpand = false;
           // scrollYWhenStoppedScrolling = 0;

            state = NONE;
        }
    }
}
