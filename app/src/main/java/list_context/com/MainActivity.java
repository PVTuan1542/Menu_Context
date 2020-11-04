package list_context.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> arrayMonHoc;
    ArrayAdapter adapterMonHoc;
    int vtri;
    private List<String> UserSelection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(modeListener);

        adapterMonHoc = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayMonHoc);
        lv.setAdapter(adapterMonHoc);

        registerForContextMenu(lv);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
    }

    private void AnhXa(){
        lv = (ListView) findViewById(R.id.listView);
        arrayMonHoc = new ArrayList<>();
        arrayMonHoc.add("Java");
        arrayMonHoc.add("C++");
        arrayMonHoc.add("PHP");
        arrayMonHoc.add("Ruby");
        arrayMonHoc.add("Python");
        arrayMonHoc.add("CSS");
        arrayMonHoc.add("HTML");
        arrayMonHoc.add("Swift");
        arrayMonHoc.add("React");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Them:them();

                break;
            case R.id.Sua: sua(item);

                break;
            case R.id.Xoa: xoa(item);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //Thêm

    private void them(){
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add, null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.et_tenMonHoc);
                action_them(editText.getText().toString());
            }

        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void action_them(String data) {
        arrayMonHoc.add(data);
        adapterMonHoc.notifyDataSetChanged();
    }

    //Sửa

    private void sua(MenuItem item){

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add, null);
        builder.setView(customLayout);

        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
         vtri = info.position;//Lấy vi trí listview item

        // send data from the AlertDialog to the Activity
        EditText editText = customLayout.findViewById(R.id.et_tenMonHoc);
        editText.setText(arrayMonHoc.get(vtri));

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = customLayout.findViewById(R.id.et_tenMonHoc);
                action_sua(editText.getText().toString());
            }

        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void action_sua(String data) {
        arrayMonHoc.set(vtri,data);
        adapterMonHoc.notifyDataSetChanged();
    }

    //Xóa

    public void xoa(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int current_row = info.position;
        arrayMonHoc.remove(current_row);
        adapterMonHoc.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context,menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }


    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            if(UserSelection.contains(arrayMonHoc.get(i))){
                UserSelection.remove(arrayMonHoc.get(i));
            }else{
                UserSelection.add(arrayMonHoc.get(i));
            }
            actionMode.setTitle(UserSelection.size() + " items selected ...");
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_bar,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.delete:
                    removeItems(UserSelection);
                    actionMode.finish();
                    return  true;

                default: return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            UserSelection.clear();
        }
    };

    public void removeItems(List<String> items){
        for(String item : items){
            arrayMonHoc.remove(item);
        }
        adapterMonHoc.notifyDataSetChanged();
    }
}