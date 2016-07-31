package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.hp.sqlite.adapter.PhoneContactAdapter;
import com.example.hp.sqlite.dao.PhoneContactsDAO;
import com.example.hp.sqlite.model.PhoneContact;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPeople extends AppCompatActivity {

    PhoneContactsDAO contactsDAO;
    Activity activity = this;
    ListView allContactListView, chosenContactsListView;
    List<PhoneContact> phoneContactList;
    List<PhoneContact> chosenContactsList;
    PhoneContactAdapter phoneContactAdapter, chosenContactsAdapter;
    ContentResolver cr;
    GetEmpTask task;
    Bundle bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        allContactListView = (ListView) findViewById(R.id.allContactslistView);
        chosenContactsListView = (ListView) findViewById(R.id.choosedContactsListView);
        chosenContactsList = new ArrayList<>();

        bb = getIntent().getExtras().getBundle("eventData");
        if (bb.getSerializable("contacts")!=null) chosenContactsList = (List<PhoneContact>)(bb.getSerializable("contacts"));

        contactsDAO = new PhoneContactsDAO(this);
        cr = getContentResolver();
        task = new GetEmpTask(activity);
        task.execute((Void) null);
        updateView();
        registerForContextMenu(allContactListView);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                PhoneContact phoneContact = phoneContactAdapter.getItem(position);
                if (!chosenContactsList.contains(phoneContact)){
                chosenContactsList.add(phoneContact);
                chosenContactsAdapter.notifyDataSetChanged();
                }

            }
        };
        allContactListView.setOnItemClickListener(listener);

        Button btnAddPeople = (Button) findViewById(R.id.btn_accept_people);
        btnAddPeople.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), AddEvent.class);

                bb.putSerializable("contacts", new ArrayList<>(chosenContactsList));
                intent.putExtra("eventData", bb);
                startActivity(intent);
            }

        });

    }


    public class GetEmpTask extends AsyncTask<Void, Void, Map<String ,List<PhoneContact>>>{

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Map<String ,List<PhoneContact>> doInBackground(Void... arg0) {

            List<PhoneContact> contactsList = contactsDAO.getAllContacts(cr);
            List<PhoneContact> contactsList2 = chosenContactsList;
            Map<String ,List<PhoneContact>> map =new HashMap();
            map.put("allContacts",contactsList);
            map.put("chosenContact",contactsList2);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String,List<PhoneContact>> contacts) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                phoneContactList = contacts.get("allContacts");
                if (contacts != null) {
                    if (contacts.size() != 0) {
                        phoneContactAdapter = new PhoneContactAdapter(activity,
                                contacts.get("allContacts"),0);
                        allContactListView.setAdapter(phoneContactAdapter);
                        phoneContactAdapter.notifyDataSetChanged();

                        chosenContactsAdapter = new PhoneContactAdapter(activity,
                                contacts.get("chosenContact"),1);
                        chosenContactsListView.setAdapter(chosenContactsAdapter);
                        chosenContactsAdapter.notifyDataSetChanged();

                    }
                }
            }
        }
    }

        public void updateView() {
            task = new GetEmpTask(activity);
            task.execute((Void) null);
        }
}

