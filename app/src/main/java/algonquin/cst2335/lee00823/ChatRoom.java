package algonquin.cst2335.lee00823;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    private RecyclerView.Adapter myAdapter;

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
    String currentDateandTime  = sdf.format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener( click -> {
            Log.i("ChatRoom", "send button clicked.");
            String message = binding.textInput.getText().toString();
            boolean sentButton = true;
            chat = new ChatMessage(message,currentDateandTime , sentButton );
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

        });

        binding.receiveButton.setOnClickListener( click ->{
            Log.i("ChatRoom", "receive button clicked.");

            String message = binding.textInput.getText().toString();
            boolean sentButton = false;
            chat = new ChatMessage(message,currentDateandTime , sentButton );
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

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

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}