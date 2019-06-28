package smartadapter.viewholder;

/*
 * Created by Manne Öhlund on 2019-06-16.
 * Copyright (c) All rights reserved.
 */

import android.support.annotation.NonNull;

import smartadapter.SmartRecyclerAdapter;

public interface SmartAdapterHolder {
    @NonNull SmartRecyclerAdapter getSmartRecyclerAdapter();
    void setSmartRecyclerAdapter(@NonNull SmartRecyclerAdapter smartRecyclerAdapter);
}
