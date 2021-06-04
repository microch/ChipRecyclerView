package org.fengye.chiprecyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.fengye.chiprecyclerview.IChipBean;
import org.fengye.chiprecyclerviewdemo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String[] fruits = new String[]{
            "西瓜", "葡萄", "芒果", "提子", "柚子", "香蕉", "山楂", "苹果", "柠檬",
            "金桔", "毛桃", "杨桃", "杏子", "橙子", "沃柑", "蓝莓", "荔枝"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        for (int i = 0; i < fruits.length; i += 3) {
            binding.test0.addItem(new TestBean(i, fruits[i]));
        }

        for (int i = 0; i < fruits.length; i += 2) {
            binding.test1.addItem(new TestBean(i, fruits[i]));
        }

        for (int i = 0; i < fruits.length / 3; i++) {
            binding.test2.addItem(new TestBean(i, fruits[i]));
        }
        for (int i = 0; i < fruits.length / 3; i++) {
            binding.test3.addItem(new TestBean(i, fruits[i]));
        }
        for (int i = 0; i < fruits.length / 2; i++) {
            binding.test4.addItem(new TestBean(i, fruits[i]));
        }
        binding.test4.setEnabledIds(new ArrayList<>(Arrays.asList(0, 2, 4, 6)));

        for (int i = 0; i < fruits.length / 3; i++) {
            binding.test5.addItem(new TestBean(i, fruits[i]));
        }
        binding.test5.addItem(new TestBean(-1, "自定义"));
        binding.test5.setOnLastItemClickListener(v -> {
            binding.test5.addLastItem(new TestBean("自定义+"));
        });

        for (int i = 0; i < fruits.length / 3; i++) {
            binding.test6.addItem(new TestBean(i, fruits[i]));
        }
        binding.test6.setEnabledIds(new ArrayList<>(Arrays.asList(0, 1, 3, 4)));


        binding.btn.setOnClickListener(v -> {
            List<TestBean> selectedList = binding.test1.getSelectedList();
            StringBuilder sb = new StringBuilder();
            for (TestBean testBean : selectedList) {
                sb.append(testBean.getId());
                sb.append(":");
                sb.append(testBean.getTitle());
                sb.append("\n");
            }
            binding.result.setText(sb.toString());
        });


    }
}