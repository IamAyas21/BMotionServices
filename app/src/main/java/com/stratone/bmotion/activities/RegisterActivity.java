package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseUser;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.UploadService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.imgCamera)
    ImageView mCamera;

    @BindView(R.id.btnSignUp)
    Button bSignUp;

    @BindView(R.id.edtNIK)
    EditText eNIK;

    @BindView(R.id.edtFullName)
    EditText eFullName;

    @BindView(R.id.edtPassword)
    EditText ePassword;

    @BindView(R.id.edtEmail)
    EditText eEmail;

    @BindView(R.id.edtPhone)
    EditText ePhone;

    @BindView(R.id.edtCity)
    EditText eCity;

    @BindView(R.id.expiredDate)
    EditText expDate;

    @BindView(R.id.uploadFile)
    EditText uploadFile;

    ApiInterface apiService;
    PowerManager.WakeLock wakeLock;

    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_FILE_REQUEST = 1;
    private Uri uri, uriFile;
    private Bitmap imageBitmap;
    private String currentPhotoPath;
    private String selectedFilePath;
    private File image;
    private UploadService uploadService;
    final Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private File arrFile[] = new File[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        /*Get Path PDF*/
        GetPutExtra();

        /*Get Instance API Service*/
        apiService = ApiClient.getClient().create(ApiInterface.class);

        /*Take Picture*/
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        /*SignUp*/
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrFile[0] = image;
                arrFile[1] = new File(selectedFilePath);
                signUp(arrFile);
            }
        });

        /*Datetime NOW*/
        final DatePickerDialog.OnDateSetListener dateNow = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateExpDate();
            }
        };

        /*Pick Date*/
        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, dateNow, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*Pick File*/
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void updateExpDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        expDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void signUp(File[] file)
    {
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), file[0]);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("imagektp",
                file[0].getName(), photoBody);

        RequestBody pdfBody = RequestBody.create(MediaType.parse("application/pdf"), file[1]);
        MultipartBody.Part pdfPart = MultipartBody.Part.createFormData("filepdf",
                file[1].getName(), pdfBody);

        User user = new User();
        user.setNIP(eNIK.getText().toString());
        user.setEmail(eEmail.getText().toString());
        user.setName(eFullName.getText().toString());
        user.setPhone(ePhone.getText().toString());
        user.setKTP(file[0].getName());

        uploadService = new UploadService();
        uploadService.SignUpMultipart(user, photoPart, pdfPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ResponseUser baseResponse = (ResponseUser) response.body();

                if(baseResponse != null) {
                    if(baseResponse.getStatus().equals("success"))
                    {
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(this,
                        "com.bmotion.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            mCamera.setImageURI(uri);
        }
        else if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if (data == null) {
                //no data present
                return;
            }

            uriFile = data.getData();
            Path pathsToFile = Paths.get(uriFile.getPath());
            selectedFilePath = pathsToFile.toString();
            uploadFile.setText(selectedFilePath);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ktp_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showFileChooser() {
        Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
        SetPutExtra(intent);
        startActivity(intent);
    }

    private void SetPutExtra(Intent intent)
    {
        intent.putExtra("image_ktp",image.getAbsolutePath());
        intent.putExtra("nik",eNIK.getText().toString());
        intent.putExtra("full_name",eFullName.getText().toString());
        intent.putExtra("email_address",eEmail.getText().toString());
        intent.putExtra("password",ePassword.getText().toString());
        intent.putExtra("phone_number",ePhone.getText().toString());
        intent.putExtra("city",eCity.getText().toString());
        intent.putExtra("exp_date",expDate.getText().toString());
        intent.putExtra("path_pdf",selectedFilePath);
    }

    private void GetPutExtra()
    {
        if(getIntent().getExtras()!=null){
            selectedFilePath = String.valueOf(getIntent().getStringExtra("path_pdf")) ;
            eNIK.setText(String.valueOf(getIntent().getStringExtra("nik")));
            eFullName.setText(String.valueOf(getIntent().getStringExtra("full_name")));
            eEmail.setText(String.valueOf(getIntent().getStringExtra("email_address")));
            ePassword.setText(String.valueOf(getIntent().getStringExtra("password")));
            ePhone.setText(String.valueOf(getIntent().getStringExtra("phone_number")));
            eCity.setText(String.valueOf(getIntent().getStringExtra("city")));
            expDate.setText(String.valueOf(getIntent().getStringExtra("exp_date")));
            /*uri = Uri.parse(getIntent().getStringExtra("image_ktp"));
            mCamera.setImageURI(uri);*/
            uploadFile.setText(selectedFilePath);
            image = new File(getIntent().getStringExtra("image_ktp"));
            uri = Uri.fromFile(new File(image.getPath()));
            mCamera.setImageURI(uri);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
