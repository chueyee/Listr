package com.example.listr;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ItemListFragment extends Fragment{

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItemRecyclerView = view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback((Adapter) mAdapter));
        itemTouchHelper.attachToRecyclerView(mItemRecyclerView);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.new_item:
                Item item = new Item();
                ItemLab.get(getActivity()).addItem(item);
                Intent intent = ListActivity.newIntent(getActivity(), item.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void updateUI() {
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }

    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Item mItem;
        private TextView mTitleTextView, mDetailTextView;
        private CheckBox mCheckBox;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.item_title);
            mDetailTextView = itemView.findViewById(R.id.item_detail);
            mCheckBox = itemView.findViewById(R.id.have_checkbox);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mItem.setHave(isChecked);
                }
            });
        }

        public void bind(Item item) {
            mItem = item;
            mTitleTextView.setText(mItem.getTitle());
            mDetailTextView.setText(mItem.getDetail());
            mCheckBox.setChecked(mItem.isHave());
        }

        @Override
        public void onClick(View view) {
            Intent intent = ListActivity.newIntent(getContext(), mItem.getId());
            startActivity(intent);
        }


    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ItemHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
            Item item = mItems.get(i);
            itemHolder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void setItems(List<Item> items) {
            mItems = items;
        }

        public void deleteItem(int position) {
            mItems.get(position);
            int recentDeleted = position;
            ItemLab.get(getActivity()).deleteItem(mItems.get(position));
            mItems.remove(position);

            notifyItemRemoved(position);


            //showUndoSnackbar();

        }

        /*private void showUndoSnackbar() {
            View view = getActivity().findViewById(R.id.list_item);
            Snackbar
        }*/
    }

    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        public SwipeToDeleteCallback(Adapter adapter) {
            super(0, ItemTouchHelper.RIGHT);
            mAdapter = (ItemAdapter) adapter;

        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int position = viewHolder.getPosition();
            mAdapter.deleteItem(position);
        }

    }


}
