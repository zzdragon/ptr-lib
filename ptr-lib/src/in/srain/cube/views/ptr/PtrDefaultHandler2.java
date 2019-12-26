package in.srain.cube.views.ptr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;


public abstract class PtrDefaultHandler2 extends PtrDefaultHandler implements PtrHandler2 {

    public static boolean canChildScrollDown(View view) {
        if (view instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) view;
            return absListView.getChildCount() > 0
                    && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                    || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
        } else if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            if (scrollView.getChildCount() == 0) {
                return false;
            } else {
                return scrollView.getScrollY() < scrollView.getChildAt(0).getHeight() - scrollView.getHeight();
            }
        } else if (view instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) view).getLayoutManager();
            if (manager.getChildCount() == 0) return false;
            if (manager.canScrollVertically()) {
                View lastVisibleView = manager.getChildAt(((RecyclerView) view).getChildCount() - 1);
                if (lastVisibleView == null) return false;
                int lastVisibleViewPosition = manager.getPosition(lastVisibleView);
                int lastVisibleViewBottom = manager.getDecoratedBottom(lastVisibleView);
                return lastVisibleViewPosition < manager.getChildCount() - 1 || lastVisibleViewBottom > view.getHeight() - view.getPaddingBottom();
            } else {
                return true;
            }
        } else {
            return false;
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
    public static boolean checkContentCanBePulledUp(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollDown(content);
    }

    @Override
    public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return checkContentCanBePulledUp(frame, content, footer);
    }
}