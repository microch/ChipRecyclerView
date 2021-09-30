package org.fengye.chiprecyclerview;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.chip.Chip;

import java.util.LinkedList;
import java.util.List;

/**
 * author : caohao
 * date : 2021-03-07 11:18
 * description :
 */
public class ChipAdapter extends BaseQuickAdapter<IChipBean, BaseViewHolder> {

    private OnItemSelectedListener onItemSelectedListener;
    private OnItemDeSelectedListener onItemDeSelectedListener;

    private boolean enableSelection = true;

    // 是否单选
    private boolean singleSelection = false;

    private boolean lastItemSelection = true;

    private int maxSelection = -1;

    private List<IChipBean> selectedList = new LinkedList<>();
    private boolean selectionRequired = false;
    private int itemTextSize;
    private boolean itemTextBold;

    private ColorStateList itemTextColor;
    private ColorStateList itemStrokeColor;
    private ColorStateList itemBackgroundColor;

    public ChipAdapter() {
        this(false);
    }

    public ChipAdapter(boolean singleSelection) {
        super(R.layout.item_tag_selector);
        this.singleSelection = singleSelection;
    }

    public void setItemColor(ColorStateList itemTextColor, ColorStateList itemStrokeColor, ColorStateList itemBackgroundColor) {
        this.itemTextColor = itemTextColor;
        this.itemStrokeColor = itemStrokeColor;
        this.itemBackgroundColor = itemBackgroundColor;
    }

    public void setLastItemSelection(boolean lastItemSelection) {
        this.lastItemSelection = lastItemSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    @Override
    protected void convert(BaseViewHolder helper, IChipBean chipBean) {


        Chip root = (Chip) helper.itemView;
        root.setOnCheckedChangeListener(null);
        root.setText(chipBean.getTitle());
        root.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);

        if (itemTextColor != null) {
            root.setTextColor(itemTextColor);
        } else {
            AppCompatResources.getColorStateList(getContext(), R.color.selector_tag_text);
        }

        if (itemStrokeColor != null) {
            root.setChipStrokeColor(itemStrokeColor);
        } else {
            AppCompatResources.getColorStateList(getContext(), R.color.selector_tag_text);
        }
        if (itemBackgroundColor != null) {
            root.setChipBackgroundColor(itemBackgroundColor);
        } else {
            AppCompatResources.getColorStateList(getContext(), R.color.selector_tag_bg);
        }

        if (itemTextBold) {
            root.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            root.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        root.setEnabled(chipBean.isEnable());
        root.setCheckable(true);
        root.setChecked(selectedList.contains(chipBean));
        if (enableSelection) {

            int position = getItemPosition(chipBean);
            if (!lastItemSelection && position == getItemCount() - 1) {
                root.setCheckable(false);
            } else {
                root.setCheckable(true);
            }

            root.setOnCheckedChangeListener((compoundButton, b) -> {
                if (singleSelection) {
                    if (b) {
                        if (selectedList.size() > 0) {
                            IChipBean iChipBean = selectedList.get(0);
                            selectedList.clear();
                            if (onItemDeSelectedListener != null) {
                                onItemDeSelectedListener.onItemDeSelected(iChipBean);
                            }

                        }
                        selectedList.add(chipBean);
                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelected(chipBean);
                        }
                        if (getRecyclerView().isComputingLayout()) {
                            getRecyclerView().post(this::notifyDataSetChanged);
                        } else {
                            notifyDataSetChanged();
                        }

                    } else {
                        if (!selectionRequired) {
                            selectedList.remove(chipBean);
                            if (onItemDeSelectedListener != null) {
                                onItemDeSelectedListener.onItemDeSelected(chipBean);
                            }
                        } else {
                            if (selectedList.size() > 1) {
                                selectedList.remove(chipBean);
                                if (onItemDeSelectedListener != null) {
                                    onItemDeSelectedListener.onItemDeSelected(chipBean);
                                }
                            } else {
                                root.setChecked(true);
                            }
                        }
                    }
                } else {
                    if (b) {
                        if (maxSelection == -1 || selectedList.size() < maxSelection) {
                            selectedList.add(chipBean);
                            if (onItemSelectedListener != null) {
                                onItemSelectedListener.onItemSelected(chipBean);
                            }
                        } else {
                            root.setChecked(false);
                            Toast.makeText(getContext(), String.format("最多选择%d个", maxSelection), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!selectionRequired) {
                            selectedList.remove(chipBean);
                            if (onItemDeSelectedListener != null) {
                                onItemDeSelectedListener.onItemDeSelected(chipBean);
                            }
                        } else {
                            if (selectedList.size() > 1) {
                                selectedList.remove(chipBean);
                                if (onItemDeSelectedListener != null) {
                                    onItemDeSelectedListener.onItemDeSelected(chipBean);
                                }
                            } else {
                                root.setChecked(true);
                            }
                        }
                    }
                }
            });

        } else {
            root.setCheckable(false);
            root.setChecked(false);
        }
    }


    public void setEnableSelection(boolean enableSelection) {
        this.enableSelection = enableSelection;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnItemDeSelectedListener(OnItemDeSelectedListener onItemDeSelectedListener) {
        this.onItemDeSelectedListener = onItemDeSelectedListener;
    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
        selectedList.clear();
        notifyDataSetChanged();

    }

    public <T extends IChipBean> List<T> getSelectedList() {
        return (List<T>) selectedList;
    }

    public void reset() {
        selectedList.clear();
        notifyDataSetChanged();
    }

    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
    }

    public void setItemTextSize(int itemTextSize) {

        this.itemTextSize = itemTextSize;
    }

    public void setItemTextBold(boolean itemTextBold) {
        this.itemTextBold = itemTextBold;

    }

    public interface OnItemSelectedListener<T extends IChipBean> {
        void onItemSelected(T chipBean);
    }

    public interface OnItemDeSelectedListener<T extends IChipBean> {
        void onItemDeSelected(T chipBean);
    }

}
