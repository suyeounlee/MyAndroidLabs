package algonquin.cst2335.lee00823;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.lee00823.databinding.DetailsLayoutBinding;
import data.ChatMessage;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment() {};
    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }

    public View onCreateView( LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        binding.databaseText.setText("Id=" + selected.id);

        return binding.getRoot();
    }
}
