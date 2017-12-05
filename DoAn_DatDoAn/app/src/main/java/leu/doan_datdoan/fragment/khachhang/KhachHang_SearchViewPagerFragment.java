package leu.doan_datdoan.fragment.khachhang;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import leu.doan_datdoan.R;
import leu.doan_datdoan.adapters.khachhang.KhachHang_NearMeViewPagerFrament_RVAdapter;
import leu.doan_datdoan.events.khachhang.KhachHang_OnClickChonCuaHangEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHang_SearchViewPagerFragment extends Fragment implements FragmentLifecycle,KhachHang_NearMeViewPagerFrament_RVAdapter.KhachHang_OnClickCuaHang{

    @BindView(R.id.searchview_khachhang_search_viewpager_frament)
    MaterialSearchView searchView;
    @BindView(R.id.iv_khachhang_search_viewpager_frament)
    ImageView iv;
    @BindView(R.id.rl_khachhang_search_viewpager_frament)
    RelativeLayout rl;
    @BindView(R.id.rotatesearch_khachhang_search_viewpager_frament)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.rv_khachhang_search_viewpager_frament)
    RecyclerView rv;
    @BindView(R.id.rlmain_khachhang_search_viewpager_frament)
    RelativeLayout rlmain;

    private KhachHang_NearMeViewPagerFrament_RVAdapter khachHangNearMeViewPagerFramentRvAdapter;

    private List<CuaHang> cuaHangList ;
    private String searchString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_khach_hang__search_view_pager, container, false);
        cuaHangList = new ArrayList<>();
        setupUI(v);
        addEvent();
        return v;
    }

    private int call = 0;
    private void addEvent() {


        Observable<String> observable = fromSearchView(searchView);
        observable.debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(item->item.length()>0)
                .subscribe(query->{
                    searchString = query;
                    searchCuaHang(query);
                });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic7

                searchView.setContentDescription(searchString);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                iv.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv.setVisibility(View.GONE);
                searchView.showSearch();
            }
        });

//        RetrofitFactory.getInstance().createService(CuaHangService.class).layCuaHangTheoTen("Vịt nướng").enqueue(new Callback<List<CuaHang>>() {
//            @Override
//            public void onResponse(Call<List<CuaHang>> call, Response<List<CuaHang>> response) {
//                    Log.d("abcde",response.body().size()+" " +response.code() + " " + response.message() );
//            }
//
//            @Override
//            public void onFailure(Call<List<CuaHang>> call, Throwable t) {
//
//            }
//        });
    }

    public Observable<String> fromSearchView(@NonNull final MaterialSearchView searchView) {
        final BehaviorSubject<String> subject = BehaviorSubject.create();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("12345","submit");

                searchView.closeSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("123abc","ontechchang" + newText);
                if (!newText.isEmpty()) {
                    Log.d("123abc","ontechchang not empty" + newText);

                    rv.setVisibility(View.INVISIBLE);
                    rl.setVisibility(View.VISIBLE);
                    loadingIndicatorView.show();
                        subject.onNext(newText);
                }
                return true;
            }
        });

        return subject;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);

        loadingIndicatorView.hide();
        rl.setVisibility(View.GONE);
        khachHangNearMeViewPagerFramentRvAdapter = new KhachHang_NearMeViewPagerFrament_RVAdapter(cuaHangList, this.getContext());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false)
//        {
//            @Override
//            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//                super.onLayoutChildren(recycler, state);
//
//                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
//                final int lastVisibleItemPosition = findLastVisibleItemPosition();
//                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
//
//                if(khachHangNearMeViewPagerFramentRvAdapter.getItemCount() > itemsShown){
//                    loadingIndicatorView.hide();
//                    rl.setVisibility(View.GONE);
//                    rv.setVisibility(View.VISIBLE);
//                }
//            }}
                ;
        khachHangNearMeViewPagerFramentRvAdapter.setOnItemClickListener(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(khachHangNearMeViewPagerFramentRvAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                linearLayoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
    }

    private void searchCuaHang(String ten) {

        RetrofitFactory.getInstance().createService(CuaHangService.class).layCuaHangTheoTen(ten).enqueue(new Callback<List<CuaHang>>() {
            @Override
            public void onResponse(Call<List<CuaHang>> call, Response<List<CuaHang>> response) {


                if (response.isSuccessful()) {
                    cuaHangList = response.body();

                    if(cuaHangList.size() > 0){

                        khachHangNearMeViewPagerFramentRvAdapter.setNewList(cuaHangList);
                        rl.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }else{
                        rl.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CuaHang>> call, Throwable t) {
                rl.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onClick(int postion) {
        Log.d("abc",postion+"");
        EventBus.getDefault().postSticky(new KhachHang_OnClickChonCuaHangEvent(khachHangNearMeViewPagerFramentRvAdapter.getCuaHangList().get(postion)));
    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }
}
