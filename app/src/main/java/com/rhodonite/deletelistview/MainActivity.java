package com.rhodonite.deletelistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean selectAll = false;
    boolean isClosing = false;
    private ListView listview;
    private Button bt_delete;
    private Button bt_cancel;
    private CheckBox selectAllCheckbox;
    private RelativeLayout relativeLayout;
    private boolean isSelecting = false;
    private List<CustomListView> myList = new ArrayList<>();
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            myList.add(new CustomListView("Test" + i, R.mipmap.img, false));
        }
    }

    private void initView() {
        bt_delete = (Button) findViewById(R.id.bt_delete);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        selectAllCheckbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, R.layout.item_layout, myList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isSelecting) {
                    CheckBox checkBox = view.findViewById(R.id.item_checkbox);
                    if (!checkBox.isChecked() && !myList.get(position).getSelected()) {
                        myList.get(position).setSelected(true);
                        checkBox.setChecked(true);
                    } else if (checkBox.isChecked() && myList.get(position).getSelected()) {
                        myList.get(position).setSelected(false);
                        checkBox.setChecked(false);
                    }

                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isSelecting) {
                    CheckBox checkBox = view.findViewById(R.id.item_checkbox);
                    if (!checkBox.isChecked() && !myList.get(position).getSelected()) {
                        myList.get(position).setSelected(true);
                        checkBox.setChecked(true);
                    } else if (checkBox.isChecked() && myList.get(position).getSelected()) {
                        myList.get(position).setSelected(false);
                        checkBox.setChecked(false);
                    }
                } else {
                    myList.get(position).setSelected(true);
                    isSelecting = true;
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < myList.size(); i++) {
                    if (myList.get(i).getSelected()) {
                        myList.remove(i);
                        i--;
                    }
                }
                isClosing = true;
                adapter.notifyDataSetChanged();
                relativeLayout.setVisibility(View.GONE);
                isSelecting = false;
            }
        });
        selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < myList.size(); i++) {
                        myList.get(i).setSelected(true);
                    }
                    selectAll = true;
                    adapter.notifyDataSetChanged();
                } else {
                    selectAll = false;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0 && isSelecting) {
                cancel();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void cancel() {
        selectAll = false;
        selectAllCheckbox.setChecked(false);
        relativeLayout.setVisibility(View.GONE);
        for (int i = 0; i < myList.size(); i++) {
            myList.get(i).setSelected(false);
        }
        isClosing = true;
        adapter.notifyDataSetChanged();
        isSelecting = false;
    }

    class ListViewAdapter extends ArrayAdapter<CustomListView> {
        private int resourceId;

        public ListViewAdapter(Context context, int textViewResourceId,
                               List<CustomListView> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomListView customlistview = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (isClosing || convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = view.findViewById(R.id.list_image);
                viewHolder.textView = view.findViewById(R.id.list_name);
                viewHolder.checkBox = view.findViewById(R.id.item_checkbox);
                view.setTag(viewHolder);
                if (position == myList.size()) {
                    isClosing = false;
                }
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.imageView.setImageResource(customlistview.getImageId());
            viewHolder.textView.setText(customlistview.getName());
            if (isSelecting) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checkBox.setVisibility(View.GONE);

            }
            if (selectAll)
                viewHolder.checkBox.setChecked(true);
            else {
                if (myList.get(position).getSelected())
                    viewHolder.checkBox.setChecked(true);
                else
                    viewHolder.checkBox.setChecked(false);
            }
            return view;
        }
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
        CheckBox checkBox;
    }
}
