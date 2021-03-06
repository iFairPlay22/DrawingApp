package fr.uge.andrawid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ShareActionProvider;

import fr.uge.andrawid.controller.EventManager;
import fr.uge.andrawid.model.draw.model.ColorKind;
import fr.uge.andrawid.model.draw.model.ShapeKind;
import fr.uge.andrawid.view.ColorArrayAdapter;
import fr.uge.andrawid.view.DrawingView;
import fr.uge.andrawid.view.ShapeArrayAdapter;


public class MainActivity extends AppCompatActivity {

    private EventManager eventManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMenuMutton:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, eventManager.onShareDrawing());
                new ShareActionProvider(this).setShareIntent(shareIntent);
                startActivity(shareIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DrawingView drawingView = findViewById(R.id.drawingView);
        this.eventManager = new EventManager(this, drawingView);

        drawingView.setOnTouchListener(
                (v, event) -> {
                    switch (event.getActionMasked()) {

                        case MotionEvent.ACTION_DOWN:
                            eventManager.onDown(event.getX(), event.getY());
                            break;

                        case MotionEvent.ACTION_MOVE:
                            eventManager.onMove(event.getX(), event.getY());
                            break;

                        case MotionEvent.ACTION_UP:
                            eventManager.onUp(event.getX(), event.getY());
                            break;

                        default:
                            break;
                    }

                    return true;
                }
        );

        GridView gridView = (GridView) findViewById(R.id.shapeGridView);
        gridView.setNumColumns(2);
        gridView.setAdapter(new ShapeArrayAdapter(this, android.R.layout.simple_list_item_1, ShapeKind.values()));

        gridView.setOnItemClickListener( (adapterView, view, i, l) -> {
            eventManager.onShapeItemSelection((ShapeKind) adapterView.getItemAtPosition(i));
        });


        GridView colorGridView = (GridView) findViewById(R.id.colorGridView);
        colorGridView.setNumColumns(2);
        colorGridView.setAdapter(new ColorArrayAdapter(this, android.R.layout.simple_list_item_1, ColorKind.values()));

        colorGridView.setOnItemClickListener( (adapterView, view, i, l) -> {
            eventManager.onColorItemSelection((ColorKind) adapterView.getItemAtPosition(i));
        });


        Button changeActivityButton = (Button) findViewById(R.id.changeActivityButton);
        changeActivityButton.setOnClickListener((e) -> {
            startActivityForResult(new Intent(this, SelectionActivity.class), 1);
        });
        changeActivityButton.callOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            this.eventManager.onRefresh(data.getStringExtra("drawingName"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventManager.onSave();
    }
}