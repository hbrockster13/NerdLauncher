package com.example.nerdlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLauncherFragment extends Fragment
{
    private static final String TAG = "NerdLauncherFragment";

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public ActivityHolder(View viewItem)
        {
            super(viewItem);
            mNameTextView = (TextView) viewItem;
            mNameTextView.setOnClickListener(this);

        }

        public void bindActivity(ResolveInfo resolveInfo)
        {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);

        }

        @Override
        public void onClick(View view)
        {
            ActivityInfo acitivityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN).setClassName(acitivityInfo.applicationInfo
                    .packageName, acitivityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>
    {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities)
        {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position)
        {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount()
        {
            return mActivities.size();
        }
    }

    public static Fragment newInstance()
    {
        return new NerdLauncherFragment();
    }

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpAdater();

        return v;
    }

    private void setUpAdater()
    {
        Intent startUpIntent = new Intent(Intent.ACTION_MAIN);
        startUpIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = ((PackageManager) pm).queryIntentActivities(startUpIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>()
        {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b)
            {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString());
            }
        });
        Log.i(TAG, "Found: " + activities.size() + " activities.");
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }
}
