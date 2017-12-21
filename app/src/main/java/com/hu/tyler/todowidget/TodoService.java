package com.hu.tyler.todowidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by tyler on 8/10/2017.
 */

public class TodoService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new TodoViewsFactory(this.getApplicationContext(),
                intent));
    }
}
