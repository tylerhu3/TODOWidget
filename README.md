# TODOWidget
 Latest Revision: 12/21/2017
 This is a implementation of ListView via RemoteViews as a fun project
 Here is the actual link to the App itself on Google Play: 
https://play.google.com/store/apps/details?id=com.hu.tyler.todowidget&hl=en

The .java files are located in with http link below that: 
TODOWidget/app/src/main/java/com/hu/tyler/todowidget/   
https://github.com/tylerhu3/TODOWidget/tree/master/app/src/main/java/com/hu/tyler/todowidget
I'll give a brief explaination of what each file is below:



TodoWidget.java - This is part of the widget, an extension of AppWidgetProvider class which is necessary for creation of any widget.
The onReceive() method is typically call for any updating of the widget itself... as far as I know. Also this constructs each click Intent for each item of the List. So when it's clicked a new Activity comes out allowing the user to edit that particular item.

TodoViewsFactory.java - Also part of the widget, implements RemoteViewsService.RemoteViewsFactory, which helps construct the actual physical look of the widget itself.

TodoService.java - This is a necessary part of the widget, as with the other 2 on top. It's a extension of RemoteViewsService and it returns a TodoViewsFactory from whichever client calls it. I'm not too sure on the technicalities of this item, will require further research in
the future.

All the files below have a xml layout file correlated with them in dir and the link below that: 
TODOWidget/app/src/main/res/layout/
https://github.com/tylerhu3/TODOWidget/tree/master/app/src/main/res/layout

MainActivity.java - This is the main activity of the app, it just contains instructions for how to add a widget on to the home screen
and some general information about myself, nothing too special.

TodoItem.java - For each item in the list, it holds a few attributes other than the title of the object itself. The title attribute is basically whats displayed on the list, the strike attribute decides if a item is completed and thus should have strike through text. There's 2 other attributes: extra and description, both of string types I left in there for future revisions. 

ChangeTodo.java - This is executed which a item on the list is clicked basically opens a small activity window with some buttons and a edittext box for the user to alter whats in that TodoItem.

...More to come in the future.
