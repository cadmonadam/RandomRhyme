package com.example.android.randomrhyme;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.randomrhyme.data.PoemsContract;

public class PoemsCursorAdapter extends RecyclerView.Adapter<PoemsCursorAdapter.PoemViewHolder> {


    private Cursor cursor;
    private Context context;

    public PoemsCursorAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PoemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.poem_list_item, parent, false);
        ButterKnife.bind(this, itemView);
        return new PoemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PoemViewHolder holder, int position) {

        int idIndex = cursor.getColumnIndex(PoemsContract.PoemEntry.COLUMN_ID);
        int poemBodyTextIndex = cursor.getColumnIndex(PoemsContract.PoemEntry.COLUMN_POEM_BODY_TEXT);
        int authorIndex = cursor.getColumnIndex(PoemsContract.PoemEntry.COLUMN_AUTHOR);
        int titleIndex = cursor.getColumnIndex(PoemsContract.PoemEntry.COLUMN_POEM_TITLE);
        cursor.moveToPosition(position);

        final int id = cursor.getInt(idIndex);
        String poemText = cursor.getString(poemBodyTextIndex);
        String author = cursor.getString(authorIndex);
        String title = cursor.getString(titleIndex);

        holder.itemView.setTag(id);
        holder.poemTextView.setText(poemText);
        holder.authorTextView.setText(author);
        holder.titleTextView.setText(title);

    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    public class PoemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        @BindView(R.id.poem_textView)
        TextView poemTextView;
        @BindView(R.id.author_textView)
        TextView authorTextView;
        @BindView(R.id.title_textView)
        TextView titleTextView;

        public PoemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {

            final PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.popup_share:

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, authorTextView.getText().toString() + "\n\n" + titleTextView.getText().toString() + "\n\n" + poemTextView.getText().toString());
                            intent.setType("text/plain");
                            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_to)));
                            return true;

                        case R.id.add_to_widget:
                            sendToWidget();
                            Toast.makeText(context, R.string.added_to_widget, Toast.LENGTH_SHORT).show();
                            return true;

                        default:
                            return false;


                    }
                }
            });

            popup.show();

            return true;
        }

        void sendToWidget() {

            String poemText = poemTextView.getText().toString();
            String author = authorTextView.getText().toString();
            String title = titleTextView.getText().toString();

            SharedPreferences sharedPref = context.getSharedPreferences("poem_to_widget", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("poem_text", poemText);
            editor.putString("author", author);
            editor.putString("title", title);
            editor.apply();

            Intent intent = new Intent(context, PoemWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int[] ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(new ComponentName(context, PoemWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }


        @Override
        public void onClick(View v) {
            expandCollapseCardview();
        }

        void expandCollapseCardview() {
            if (poemTextView.getVisibility() == View.GONE) {
                poemTextView.setVisibility(View.VISIBLE);
            } else {
                poemTextView.setVisibility(View.GONE);
            }

        }
    }

}


