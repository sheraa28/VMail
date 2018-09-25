package com.example.parmindersingh.vmail;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import com.sun.mail.pop3.POP3Store;

public class InboxActivity extends AppCompatActivity {

    private ListView emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        emailList=(ListView)findViewById(R.id.emailListView);
        final ArrayList<String>inboxMails=new ArrayList<String>();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,inboxMails);
        emailList.setAdapter(arrayAdapter);



    }

        public static void receiveEmail(String pop3Host, String storeType, String user, String password) {
            try {
                Properties properties = new Properties();
                properties.put("mail.pop3.host", pop3Host);
                Session emailSession = Session.getDefaultInstance(properties);

                POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
                emailStore.connect(user, password);

                Folder emailFolder = emailStore.getFolder("INBOX");
                emailFolder.open(Folder.READ_ONLY);

                Message[] messages = emailFolder.getMessages();
                for (int i = 0; i < messages.length; i++) {
                    Message message = messages[i];
                    System.out.println("---------------------------------");
                    System.out.println("Email Number " + (i + 1));
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("From: " + message.getFrom()[0]);
                    System.out.println("Text: " + message.getContent().toString());
                }

                emailFolder.close(false);
                emailStore.close();

            } catch (NoSuchProviderException e) {e.printStackTrace();}
            catch (MessagingException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
        }
    SharedPreferences sp1= InboxActivity.this.getSharedPreferences("Login",0);
    String unm=sp1.getString("Unm", null);
    String pass = sp1.getString("Psw", null);


        public void main(String[] args) {

            String host = "mail.javatpoint.com";//change accordingly
            String mailStoreType = "pop3";
            String username= unm;
            String password= pass;//change accordingly

            receiveEmail(host, mailStoreType, username, password);

        }
}
