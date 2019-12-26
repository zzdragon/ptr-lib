package in.srain.cube.views.ptr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;


public abstract class PtrDefaultHandler implements PtrHandler {

    public static boolean canChildScrollUp(View view) {
        if (view instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) view;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        } else if (view instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) view).getLayoutManager();
            if (manager.getChildCount() == 0) return false;
            if (manager.canScrollVertically()) {
                View firstVisibleView = manager.getChildAt(0);
                if (firstVisibleView == null) return false;
                int firstVisibleViewPosition = manager.getPosition(firstVisibleView);
                int firstVisibleViewTop = manager.getDecoratedTop(firstVisibleView);
                return firstVisibleViewPosition > 0 || firstVisibleViewTop < view.getPaddingTop();
            } else {
                return true;
            }
        } else {
            return view.getScrollY() > 0;
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledDown(frame, content, header);
    }

}