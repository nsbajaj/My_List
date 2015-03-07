package com.example.nitish.my_list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {

    String[] colourNames;
    String colorName = "#00000000";
    String tempColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile();

        //tie your array to your xml array like this
        colourNames = getResources().getStringArray(R.array.listArray);

        //create a ListView object pointing to your list view like this
        ListView lv = (ListView) findViewById(R.id.listView);

        //create an array adapter and attach it to your list view like this
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        //register the list view for a Context Menu by adding this line to our activity
        registerForContextMenu(lv);

        backgroundColor();
    }
    private void setColor(){
        readFile();
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        currentLayout.setBackgroundColor(Color.parseColor(getName()));
        TextView msg = (TextView) findViewById(R.id.textView1);
        msg.setText("Setting Background Color: " + getName());
    }

    private void assignColor(){
        if(tempColor != null){
            colorName = tempColor;
        }
    }
    private String getName(){
        return colorName;
    }

    private void writeFile() throws IOException {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "color.txt");

        if (file.exists()) {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            assignColor();
            writer.println(getName());
            TextView msg = (TextView) findViewById(R.id.textView1);
            msg.setText("Writing Background Color to File: " + getName());
            writer.close();
        }
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "color.txt");

        Scanner in;

        TextView msg = (TextView) findViewById(R.id.textView1);
        if (file.exists()) {
            try{
                in = new Scanner(file);
                while(in.hasNext()){
                    String input = in.next();
                    if(input != null){
                        colorName = input;
                        currentLayout.setBackgroundColor(Color.parseColor(getName()));
                        msg.setText("Reading Background Color from File: " + colorName);
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write colour to SDCard");
        menu.add(0, v.getId(), 0, "Read colour from SDCard");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write colour to SDCard") {
            Toast.makeText(getApplicationContext(), "Write colour to SDCard", Toast.LENGTH_LONG).show();
            if(isExternalStorageWritable() == true) {
                try {
                    writeFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (item.getTitle() == "Read colour from SDCard") {
            Toast.makeText(getApplicationContext(), "Read colour from SDCard", Toast.LENGTH_LONG).show();
            setColor();
        } else {
            return false;
        }
        return true;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void backgroundColor(){
        ListView listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout);
                String[] name = getResources().getStringArray(R.array.listArray);
                String[] color = getResources().getStringArray(R.array.listValues);
                String input = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < name.length; i++){
                    if(name[i].equals(input)){
                        String temp = color[i];
                        String hex = temp.substring(2);
                        String hash = "#" + hex;
                        currentLayout.setBackgroundColor(Color.parseColor(hash));
                        TextView msg = (TextView) findViewById(R.id.textView1);
                        msg.setText("Setting Custom Background: " + hash);
                        tempColor = hash;
                        break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
