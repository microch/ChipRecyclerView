package org.fengye.chiprecyclerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : caohao
 * date : 2021-03-07 12:14
 * description :
 */
public class ChipRecyclerView extends RecyclerView {

    private final int itemTextSize;
    private final boolean itemTextBold;
    private ChipAdapter adapter;

    private int spanCount;
    private int spacing;
    private int maxSelection = -1;
    private boolean enableSelection = true;

    private boolean singleSelection = false;

    private boolean lastItemSelection = true;

    private boolean selectionRequired = false;

    private ColorStateList itemTextColor;
    private ColorStateList itemStrokeColor;
    private ColorStateList itemBackgroundColor;

    private OnClickListener onLastItemClickListener;

    public ChipRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ChipRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChipRecyclerView);
        enableSelection = typedArray.getBoolean(R.styleable.ChipRecyclerView_enableSelection, true);
        singleSelection = typedArray.getBoolean(R.styleable.ChipRecyclerView_singleSelection, false);
        lastItemSelection = typedArray.getBoolean(R.styleable.ChipRecyclerView_lastItemSelection, true);
        selectionRequired = typedArray.getBoolean(R.styleable.ChipRecyclerView_selectionRequired, false);
        spanCount = typedArray.getInt(R.styleable.ChipRecyclerView_spanCount, 4);
        maxSelection = typedArray.getInt(R.styleable.ChipRecyclerView_maxSelection, -1);
        spacing = (int) typedArray.getDimension(R.styleable.ChipRecyclerView_spacing, Util.dp2px(10));
        itemTextSize = (int) typedArray.getDimension(R.styleable.ChipRecyclerView_itemTextSize, Util.sp2px(12));
        itemTextBold = typedArray.getBoolean(R.styleable.ChipRecyclerView_itemTextBold, false);
        itemTextColor = typedArray.getColorStateList(R.styleable.ChipRecyclerView_itemTextColor);
        itemStrokeColor = typedArray.getColorStateList(R.styleable.ChipRecyclerView_itemStrokeColor);
        itemBackgroundColor = typedArray.getColorStateList(R.styleable.ChipRecyclerView_itemBackgroundColor);
        typedArray.recycle();
        init();
    }


    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
        adapter.setMaxSelection(maxSelection);
    }

    public void setEnableSelection(boolean enableSelection) {
        this.enableSelection = enableSelection;
        adapter.setEnableSelection(enableSelection);
    }

    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
        adapter.setSelectionRequired(selectionRequired);

    }

    private void init() {
        adapter = new ChipAdapter(singleSelection);
        adapter.setLastItemSelection(lastItemSelection);

        adapter.setMaxSelection(maxSelection);
        adapter.setEnableSelection(enableSelection);
        adapter.setSelectionRequired(selectionRequired);
        adapter.setItemTextSize(itemTextSize);
        adapter.setItemTextBold(itemTextBold);

        adapter.setItemColor(itemTextColor, itemStrokeColor, itemBackgroundColor);


        setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        addItemDecoration(new GridSpaceDecoration(spanCount, spacing, false));

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (position == adapter.getData().size() - 1 && !lastItemSelection && onLastItemClickListener != null) {
                onLastItemClickListener.onClick(view);
            }
        });
        adapter.setRecyclerView(this);
        setAdapter(adapter);
    }


    public void setLastItemSelection(boolean lastItemSelection) {
        this.lastItemSelection = lastItemSelection;
    }

    public void setOnLastItemClickListener(OnClickListener onLastItemClickListener) {
        this.onLastItemClickListener = onLastItemClickListener;
    }

    public boolean isSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
        adapter.setSingleSelection(singleSelection);


    }

    public void addItem(IChipBean chipBean) {
        if (adapter == null) {
            return;
        }

        adapter.addData(chipBean);

    }

    public <T extends IChipBean> void addAllItem(List<T> data) {
        if (adapter == null) {
            return;
        }

        adapter.addData(data);
    }


    public void setOnItemSelectedListener(ChipAdapter.OnItemSelectedListener onItemSelectedListener) {
        if (adapter == null) {
            return;
        }
        adapter.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setOnItemDeSelectedListener(ChipAdapter.OnItemDeSelectedListener onItemDeSelectedListener) {
        if (adapter == null) {
            return;
        }
        adapter.setOnItemDeSelectedListener(onItemDeSelectedListener);
    }


    public void addLastItem(IChipBean chipBean) {
        if (adapter == null) {
            return;
        }
        if (lastItemSelection) {
            adapter.addData(chipBean);
        } else {
            adapter.addData(adapter.getItemCount() - 1, chipBean);
        }

    }


    public <T extends IChipBean> void addItem(List<T> chipBeans) {
        if (adapter == null) {
            return;
        }
        adapter.addData(chipBeans);
    }


    public void clear() {
        if (adapter == null) {
            return;
        }
        adapter.setNewInstance(null);
    }

    public <T extends IChipBean> List<T> getSelectedList() {
        if (adapter == null) {
            return null;
        }
        return adapter.getSelectedList();
    }


    public void addSelectedItem(int chipBeanId) {

        List<IChipBean> data = adapter.getData();
        for (IChipBean datum : data) {
            if (datum.getId() == chipBeanId) {
                List<IChipBean> selectedList = adapter.getSelectedList();
                if (!selectedList.contains(datum)) {
                    selectedList.add(datum);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    public void removeSelectedItem(int chipBeanId) {

        List<IChipBean> data = adapter.getData();
        for (IChipBean datum : data) {
            if (datum.getId() == chipBeanId) {
                List<IChipBean> selectedList = adapter.getSelectedList();
                if (selectedList.contains(datum)) {
                    selectedList.remove(datum);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }


    }


    public void removeAllView() {
        if (adapter == null) {
            return;
        }
        adapter.setNewInstance(new ArrayList<>());
    }

    public void reset() {
        if (adapter == null) {
            return;
        }
        adapter.reset();
    }

    @Nullable
    @Override
    public ChipAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    public <T extends IChipBean> T getItem(int pos) {
        return (T) adapter.getItem(pos);
    }


    public void setEnabledIds(List<Integer> ids) {

        adapter.getSelectedList().clear();
        List<IChipBean> data = adapter.getData();
        for (IChipBean datum : data) {
            datum.setEnable(ids.contains(datum.getId()));
        }
        adapter.notifyDataSetChanged();
    }
}
