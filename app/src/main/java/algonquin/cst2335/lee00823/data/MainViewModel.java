package algonquin.cst2335.lee00823.data;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    //public String editString;
   // public String editTextContents;

    public MutableLiveData<String> editString = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSelected = new MutableLiveData<>();


}
