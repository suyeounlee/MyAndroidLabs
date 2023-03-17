package algonquin.cst2335.lee00823;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import algonquin.cst2335.lee00823.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.lee00823.databinding.ReceiveMessageBinding;
import algonquin.cst2335.lee00823.databinding.SentMessageBinding;
import data.ChatMessage;
import data.ChatRoomViewModel;


public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    ChatMessage chat = new ChatMessage("","",false);
    ChatMessageDAO mDAO;
    private RecyclerView.Adapter myAdapter;
    int position;

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
    String currentDateandTime  = sdf.format(new Date());

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TextView messageText;
        messageText = binding.recycleView.findViewById(R.id.messageText);

        switch( item.getItemId() ) {

            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setTitle("Question:")
                        .setMessage("Do you want to delete the message?")
                        .setNegativeButton("No",(dialog, cl) ->{})
                        .setPositiveButton("Yes",(dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            ChatMessage m = messages.get(position);
                            thread.execute(() ->
                            {
                                mDAO.deleteMessage(m);
                            });
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(findViewById(R.id.message),"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo",click ->{
                                        messages.add(position, m);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show();
                break;

            case R.id.about:
                Toast.makeText(this, "Version 1.0, created by Su Yeoun Lee", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "ChatMessage_db").build();
        mDAO = db.cmDAO();

        setContentView(binding.getRoot());


        setSupportActionBar(binding.myToolbar);


        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .addToBackStack("")
                    .commit();

//            FragmentManager fMgr = getSupportFragmentManager();
//            FragmentTransaction tx = fMgr.beginTransaction();
//            tx.add(R.id.fragmentLocation.chatFragment);
//            tx.commit();

        });

        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding.sendButton.setOnClickListener( click -> {
            Log.i("ChatRoom", "send button clicked.");
            String message = binding.textInput.getText().toString();
            boolean sentButton = true;
            chat = new ChatMessage(message,currentDateandTime , sentButton );
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(chat);
            });

        });

        binding.receiveButton.setOnClickListener( click ->{
            Log.i("ChatRoom", "receive button clicked.");

            String message = binding.textInput.getText().toString();
            boolean sentButton = false;
            chat = new ChatMessage(message,currentDateandTime , sentButton );
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(chat);
            });

        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //This function creates a ViewHolder object. It represents a single row in the list
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            //This initializes a ViewHolder to go at the row specified by the position parameter.
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() { //This function just returns an int specifying how many items to draw.
                return messages.size();
            }

            public int getItemViewType(int position){
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }


    public class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk ->  {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);



//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//                builder.setTitle("Question:")
//                        .setMessage("Do you want to delete the message: " + messageText.getText())
//                        .setNegativeButton("No", (dialog, cl)-> {})
//                         .setPositiveButton("Yes", (dialog, cl) -> {
//                             Executor thread = Executors.newSingleThreadExecutor();
//                             ChatMessage m = messages.get(position);
//                             thread.execute(() -> {
//                                 mDAO.deleteMessage(m);
//                             });
//
//                             messages.remove(position);
//                             myAdapter.notifyItemRemoved(position);
//                             Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
//                                     .setAction("Undo",click ->{
//                                         messages.add(position, m);
//                                         myAdapter.notifyItemInserted(position);
//                                     })
//                                     .show();
//                           })
//                           .create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

    }



}