package com.fastaccess.ui.modules.repos.code.files.paths;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.RepoFilesModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.adapter.RepoFilePathsAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.modules.repos.code.files.RepoFilesView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 18 Feb 2017, 2:10 AM
 */

public class RepoFilePathView extends BaseFragment<RepoFilePathMvp.View, RepoFilePathPresenter> implements RepoFilePathMvp.View {

    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.toParentFolder) View toParentFolder;

    private RepoFilePathsAdapter adapter;
    private RepoFilesView repoFilesView;

    public static RepoFilePathView newInstance(@NonNull String login, @NonNull String repoId, @Nullable String path) {
        RepoFilePathView view = new RepoFilePathView();
        view.setArguments(Bundler.start()
                .put(BundleConstant.ID, repoId)
                .put(BundleConstant.EXTRA, login)
                .put(BundleConstant.EXTRA_TWO, path)
                .end());
        return view;
    }

    @OnClick(R.id.toParentFolder) void onBackClicked() {
        getPresenter().getPaths().clear();
        onNotifyAdapter();
        getRepoFilesView().onSetData(getPresenter().getLogin(), getPresenter().getRepoId(), null);
    }

    @Override public void onNotifyAdapter() {
        adapter.notifyDataSetChanged();
        onShowHideBackBtn();
    }

    @Override public void onItemClicked(@NonNull RepoFilesModel model, int position) {
        if (getRepoFilesView().isRefreshing()) return; // avoid calling for path while the other still loading...
        if ((adapter.getItemCount() - 1) > position) {
            adapter.subList(position + 1, adapter.getItemCount());
        }
        getRepoFilesView().onSetData(getPresenter().getLogin(), getPresenter().getRepoId(), model.getPath());
    }

    @Override public void onAppendPath(@NonNull RepoFilesModel model) {
        adapter.addItem(model);
        onShowHideBackBtn();
        recycler.scrollToPosition(adapter.getItemCount() - 1); //smoothScrollToPosition(index) hides the recyclerview? WTF GOOGLE.
        getRepoFilesView().onSetData(getPresenter().getLogin(), getPresenter().getRepoId(), model.getPath());
    }

    @Override public void onSendData() {
        getRepoFilesView().onSetData(getPresenter().getLogin(), getPresenter().getRepoId(), getPresenter().getPath());
    }

    @Override protected int fragmentLayout() {
        return R.layout.repo_file_layout;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new RepoFilePathsAdapter(getPresenter().getPaths());
        adapter.setListener(getPresenter());
        recycler.setAdapter(adapter);
        if (savedInstanceState == null) {
            getPresenter().onFragmentCreated(getArguments());
        }
    }

    @NonNull @Override public RepoFilePathPresenter providePresenter() {
        return new RepoFilePathPresenter();
    }

    @NonNull public RepoFilesView getRepoFilesView() {
        if (repoFilesView == null) {
            repoFilesView = (RepoFilesView) getChildFragmentManager().findFragmentById(R.id.filesFragment);
        }
        return repoFilesView;
    }

    private void onShowHideBackBtn() {
        if (adapter.getItemCount() > 0) {
            if (!toParentFolder.isShown()) {
                toParentFolder.setVisibility(View.VISIBLE);
            }
        } else {
            if (toParentFolder.isShown()) {
                toParentFolder.setVisibility(View.GONE);
            }
        }
    }
}
