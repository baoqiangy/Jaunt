package csc445.missouriwestern.edu.jaunt.thirdparty.imagepicker;


import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.features.ReturnMode;

/**
 * Created by byan on 3/3/2018.
 */

public class ImagePickerWrapper {
    public static void pickImage(ImagePicker imagePicker){
        imagePicker
                .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .single()
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select")
                .start(IpCons.RC_IMAGE_PICKER); // image selection title
        }
}
