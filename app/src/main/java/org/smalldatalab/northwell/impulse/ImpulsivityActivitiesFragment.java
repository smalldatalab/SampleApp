package org.smalldatalab.northwell.impulse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.file.StorageAccessListener;
import org.researchstack.backbone.task.Task;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.ui.views.DividerItemDecoration;
import org.smalldatalab.northwell.impulse.studyManagement.CTFActivityRun;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

/**
 * Created by jameskizer on 1/19/17.
 */
public abstract class ImpulsivityActivitiesFragment extends Fragment implements StorageAccessListener {

    private static final int REQUEST_TASK = 1492;
    private ActivityAdapter  adapter;
    private RecyclerView recyclerView;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(org.researchstack.skin.R.layout.rss_fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(org.researchstack.skin.R.id.recycler_view);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        unsubscribe();
    }

    private void unsubscribe()
    {
        if(subscription != null)
        {
            subscription.unsubscribe();
        }
    }

    protected abstract List<CTFScheduleItem> getScheduledActivities(Context context, ImpulsivityDataProvider dataProvider);

    private void setUpAdapter() {
        unsubscribe();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL_LIST,
                0,
                false));

        Observable.create(subscriber -> {

            ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();

            List<CTFScheduleItem> scheduledActivities = this.getScheduledActivities(getActivity(), dataProvider);

            subscriber.onNext(scheduledActivities);
        })
                .compose(ObservableUtils.applyDefault())
                .map(o -> (List<CTFScheduleItem>) o)
                .subscribe(items -> {
                    adapter = new ActivityAdapter(items);
                    recyclerView.setAdapter(adapter);

                    subscription = adapter.getPublishSubject().subscribe(item -> {

                        ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();
                        CTFActivityRun activityRun = dataProvider.activityRunForItem(item);
                        Task newTask = dataProvider.loadTask(getContext(), activityRun);

                        if (newTask == null) {
                            Toast.makeText(getActivity(),
                                    org.researchstack.skin.R.string.rss_local_error_load_task,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = ViewTaskActivity.newIntent(getContext(), newTask);

                        startActivityForResult(intent, activityRun.requestId);
                    });
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();
        CTFActivityRun activityRun = dataProvider.popActivityRunForRequestCode(requestCode);

        if (activityRun != null) {

            if(resultCode == Activity.RESULT_OK)
            {
                LogExt.d(getClass(), "Received task result from task activity");

                TaskResult taskResult = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);
                dataProvider.completeActivity(getActivity(), taskResult, activityRun);

                setUpAdapter();
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onDataReady()
    {
        LogExt.i(getClass(), "onDataReady()");

        setUpAdapter();
    }

    @Override
    public void onDataFailed()
    {
        // Ignore
    }

    @Override
    public void onDataAuth()
    {
        // Ignore, activity handles auth
    }

    public static class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

        List<CTFScheduleItem> items;

        PublishSubject<CTFScheduleItem> publishSubject = PublishSubject.create();

        public ActivityAdapter(List<CTFScheduleItem> scheduledActivities)
        {
            super();

            this.items = new ArrayList<>(scheduledActivities);
        }

        public PublishSubject<CTFScheduleItem> getPublishSubject()
        {
            return publishSubject;
        }

        @Override
        public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(org.researchstack.skin.R.layout.rss_item_schedule, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position)
        {
            CTFScheduleItem item = items.get(position);

            Resources res = holder.itemView.getResources();
            int tintColor = res.getColor(org.researchstack.skin.R.color.rss_recurring_color);

            holder.title.setText(Html.fromHtml("<b>" + item.title + "</b>"));
            holder.title.setTextColor(tintColor);

            Drawable drawable = holder.dailyIndicator.getDrawable();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, tintColor);
            holder.dailyIndicator.setImageDrawable(drawable);

            holder.itemView.setOnClickListener(v -> {
                LogExt.d(getClass(), "Item clicked: " + item.guid);
                publishSubject.onNext(item);
            });
        }

        @Override
        public int getItemCount()
        {
            return this.items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView dailyIndicator;
            AppCompatTextView title;

            public ViewHolder(View itemView)
            {
                super(itemView);
                dailyIndicator = (ImageView) itemView.findViewById(org.researchstack.skin.R.id.daily_indicator);
                title = (AppCompatTextView) itemView.findViewById(org.researchstack.skin.R.id.task_title);
            }
        }
    }

}
