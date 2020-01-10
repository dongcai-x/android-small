package com.dc.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dc.myapplication.LifePagerActivity;
import com.dc.myapplication.R;
import com.dc.myapplication.Utils.WrapContentLinearLayoutManager;
import com.dc.myapplication.model.Life;
import com.dc.myapplication.model.LifeLab;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LifeListFragment extends Fragment {

    private RecyclerView mLifeRecyclerView;
    private LifeAdapter lifeAdapter;
    private static int mLifeIndex;
    private TextView textView;
    private List<Life> mLives;
    private LifeLab mLifeLab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_life_list, container, false);
        mLifeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mLifeRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        textView = view.findViewById(R.id.null_text);
        updateUI();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        mLifeLab = LifeLab.getsLifeLab(getActivity());
        List<Life> lives = mLifeLab.getLives();
        if (lifeAdapter == null){
            lifeAdapter = new LifeAdapter(lives);
            mLifeRecyclerView.setAdapter(lifeAdapter);
        }else {
            lifeAdapter.setCrimes(lives);
            lifeAdapter.notifyItemChanged(mLifeIndex);
        }
        if (lives.size() != 0) {
            textView.setVisibility(View.INVISIBLE);
        } else
            textView.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.new_crime : {
                Life life = new Life();
                LifeLab.getsLifeLab(getActivity()).addLife(life);
                Intent intent = LifePagerActivity
                        .newIntent(getActivity(), life.getId());
                startActivity(intent);
                return true;
            }
            case R.id.show_subtitle:{
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            }

            default:
                    return super.onOptionsItemSelected(item);
            }
    }

    //total
    private void updateSubtitle() {
        //LifeLab lifeLab = LifeLab.getsLifeLab(getActivity());
        int crimeCount = mLifeLab.getLives().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toast.makeText(activity, subtitle, Toast.LENGTH_SHORT).show();
        //activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

//        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
//////        if (mSubtitleVisible) {
//////            subtitleItem.setTitle(R.string.hide_subtitle);
//////        } else {
//////            subtitleItem.setTitle(R.string.show_subtitle);
//////        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mLifeLab = LifeLab.getsLifeLab(getActivity());
    }

    private class LifeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Life mLife;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mStar;

        LifeHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView =  itemView.findViewById(R.id.crime_date);
            mStar = itemView.findViewById(R.id.star_image);
            itemView.setOnClickListener(this);
  //          itemView.setOnClickListener();

        }

        void bind(Life life) {
            mLife = life;
            mTitleTextView.setText(mLife.getTitle());
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.CHINA);
            mDateTextView.setText(dateFormat.format(mLife.getDate()));
            mStar.setVisibility(life.isStar() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {

            Intent intent = LifePagerActivity.newIntent(getActivity(), mLife.getId());
            mLifeIndex = getAdapterPosition();
            startActivity(intent);
        }


//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
//            builder.setMessage("确定删除?");
//            builder.setTitle("提示");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if(mLives.remove(position)!=null){
//                        System.out.println("success");
//                    }else {
//                        System.out.println("failed");
//                    }
//
//                    Toast.makeText(getContext(), "删除列表项", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            //添加AlertDialog.Builder对象的setNegativeButton()方法
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//
//            builder.create().show();
//            return false;
//
//        }
//

    }

    private class LifeAdapter extends RecyclerView.Adapter<LifeHolder> {

        LifeAdapter(List<Life> lives) {
            mLives = lives;
        }

        @NonNull
        @Override
        public LifeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LifeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull LifeHolder holder, final int position){

            Life life = mLives.get(position);
            holder.bind(life);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopMenu(v, position);
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return mLives.size();
        }

        public void setCrimes(List<Life> lives) {
            mLives = lives;
        }

        public void removeItem(int pos){
            mLives.remove(pos);
            notifyItemRemoved(pos);
        }

    }

    private void showPopMenu(View view, final int pos){

        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                mLifeLab.delLife(mLives.get(pos));
                lifeAdapter.removeItem(pos);
                updateUI();
                Toast.makeText(getActivity(), "删除成功~", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }

}

