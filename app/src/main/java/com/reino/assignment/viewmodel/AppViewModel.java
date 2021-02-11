package com.reino.assignment.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.reino.assignment.model.ContactModel;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.repository.ContactRepo;
import com.reino.assignment.repository.UserRepo;
import com.reino.assignment.utils.SyncContacts;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = "PageViewModel";
    private UserRepo userRepo;
    private ContactRepo contactRepo;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<UserModel>> allUsers = new MutableLiveData<List<UserModel>>();
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public AppViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo(application);
        contactRepo = new ContactRepo(application);
    }

    public LiveData<PagedList<UserModel>> usersList;

    private static MutableLiveData<Boolean> isMultiSelectOn = new MutableLiveData<>();

    public void setIsMultiSelect(boolean isMultiSelect) {
        isMultiSelectOn.postValue(isMultiSelect);
    }

    public static LiveData<Boolean> getIsMultiSelectOn() {
        return isMultiSelectOn;
    }
    /**
     * This method is used for fetching Users from Room DB.
     */
    public void getUsersList() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(15)
                .setPageSize(15)
                .build();
        DataSource.Factory<Integer, UserModel> userDataSource = userRepo.fetchAllUsers();
        usersList = new LivePagedListBuilder<>(userDataSource, config).build();
    }

    public void fetchUsersOnQuery(String key) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(15)
                .setPageSize(15)
                .build();
        DataSource.Factory<Integer, UserModel> userDataSource = userRepo.fetchUsersOnQuery(key);
        usersList = new LivePagedListBuilder<>(userDataSource, config).build();
    }

    public void insert(UserModel user){
        userRepo.insert(user)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
                Toast.makeText(getApplication(), "Record has been Inserted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });
    }
    public void update(UserModel user){
        userRepo.update(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplication(), "Record has been Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    public void delete(UserModel user){
        userRepo.delete(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplication(), "Users deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }
    public void deleteAll(){
        userRepo.deleteAllUsers();
    }

    //CONTACTS

    private SyncContacts syncContacts;
    public LiveData<PagedList<ContactModel>> contactList;
    public MutableLiveData<Boolean> isProgress = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsProgress() {
        return isProgress;
    }

    public void setIsProgress(Boolean isFlag) {
        isProgress.setValue(isFlag);
    }

    public void syncContacts() {
        setIsProgress(true);
        syncContacts = new SyncContacts(getApplication());
        syncContacts.fetchContacts().doAfterSuccess(new Consumer<List<ContactModel>>() {
            @Override
            public void accept(List<ContactModel> contactList) throws Exception {
                insertContactToDb(contactList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ContactModel>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<ContactModel> contactModels) {
                        setIsProgress(false);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        setIsProgress(false);
                    }
                });
    }

    private void insertContactToDb(List<ContactModel> contactList) {
        contactRepo.insertContactList(contactList)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    public void queryContactList() {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(15)
                .setPageSize(15)
                .build();

        DataSource.Factory<Integer, ContactModel> dataSource = contactRepo.getAllContacts();
        contactList = new LivePagedListBuilder<>(dataSource, config).build();
    }

    public void queryContactListSearch(String query) {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(15)
                .setPageSize(15)
                .build();

        DataSource.Factory<Integer, ContactModel> dataSource = contactRepo.getQueryContact(query);
        contactList = new LivePagedListBuilder<>(dataSource, config).build();
    }

    public MutableLiveData<String> inputSearchText = new MutableLiveData<>();

    public MutableLiveData<String> getInputSearchText() {
        return inputSearchText;
    }

    public void setInputSearchText(String str) {
        Log.d(TAG, "setInputSearchText: "+str);
        inputSearchText.setValue(str);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }
}