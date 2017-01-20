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

import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.file.StorageAccessListener;
import org.researchstack.backbone.task.Task;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.researchstack.skin.ui.views.DividerItemDecoration;
import org.smalldatalab.northwell.impulse.studyManagement.CTFActivity;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduledActivity;

import java.util.ArrayList;
import java.util.HashMap;
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

    protected abstract List<CTFScheduledActivity> getScheduledActivities(Context context, SampleDataProvider dataProvider);

    private void setUpAdapter()
    {
        unsubscribe();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL_LIST,
                0,
                false));

        Observable.create(subscriber -> {

            SampleDataProvider dataProvider = (SampleDataProvider) DataProvider.getInstance();

            List<CTFScheduledActivity> scheduledActivities = this.getScheduledActivities(getActivity(), dataProvider);

            subscriber.onNext(scheduledActivities);
        })
                .compose(ObservableUtils.applyDefault())
                .map(o -> (List<CTFScheduledActivity>) o)
                .subscribe(scheduledActivities -> {
                    adapter = new ActivityAdapter(scheduledActivities);
                    recyclerView.setAdapter(adapter);

                    subscription = adapter.getPublishSubject().subscribe(scheduledActivity -> {

                        Task newTask = DataProvider.getInstance().loadTask(getContext(), scheduledActivity.getActivity().toTask());

                        if(newTask == null)
                        {
                            Toast.makeText(getActivity(),
                                    org.researchstack.skin.R.string.rss_local_error_load_task,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = ViewTaskActivity.newIntent(getContext(), newTask);
                        intent.putExtra("guid", scheduledActivity.getGuid());

                        startActivityForResult(intent, REQUEST_TASK);
                    });
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_TASK)
        {
            LogExt.d(getClass(), "Received task result from task activity");

            TaskResult taskResult = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);
            StorageAccess.getInstance().getAppDatabase().saveTaskResult(taskResult);
            DataProvider.getInstance().uploadTaskResult(getActivity(), taskResult);

            setUpAdapter();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

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

        List<CTFScheduledActivity> scheduledActivities;

        PublishSubject<CTFScheduledActivity> publishSubject = PublishSubject.create();

        public ActivityAdapter(List<CTFScheduledActivity> scheduledActivities)
        {
            super();

            this.scheduledActivities = new ArrayList<>(scheduledActivities);
        }

        public PublishSubject<CTFScheduledActivity> getPublishSubject()
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
            CTFScheduledActivity scheduledActivity = scheduledActivities.get(position);

            Resources res = holder.itemView.getResources();
            int tintColor = res.getColor(org.researchstack.skin.R.color.rss_recurring_color);

            holder.title.setText(Html.fromHtml("<b>" + scheduledActivity.getTitle() + "</b>"));
            holder.title.append("\n" + scheduledActivity.getTimeEstimate());
            holder.title.setTextColor(tintColor);

            Drawable drawable = holder.dailyIndicator.getDrawable();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, tintColor);
            holder.dailyIndicator.setImageDrawable(drawable);

            holder.itemView.setOnClickListener(v -> {
                LogExt.d(getClass(), "Item clicked: " + scheduledActivity.getGuid());
                publishSubject.onNext(scheduledActivity);
            });
        }

        @Override
        public int getItemCount()
        {
            return this.scheduledActivities.size();
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
