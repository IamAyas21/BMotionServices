package com.stratone.bmotion.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseUser;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.SessionManager;
import com.stratone.bmotion.utils.UploadService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @BindView(R.id.imgBtnBack)
    ImageButton back;

    ApiInterface apiService;

    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_FILE_REQUEST = 1;
    private int isObject = 0;
    private Uri uri, uriFile;
    private Bitmap imageBitmap;
    private String currentPhotoPath;
    private String selectedFilePath;
    private File image;
    private UploadService uploadService;
    final Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private File arrFile[] = new File[2];
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.isLoggedIn())
        {
            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }

        /*Get Path PDF*/
        GetPutExtra();

        /*Get Instance API Service*/
        apiService = ApiClient.getClient().create(ApiInterface.class);

        /*Take Picture*/
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isObject = 1;
                showImageChooser();
            }
        });

        /*SignUp*/
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidateRegister())
                {
                    arrFile[0] = image;
                    arrFile[1] = new File(selectedFilePath);
                    signUp(arrFile);
                }
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
                isObject = 2;
                SelectMedia();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
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
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage(getResources().getString(R.string.prompt_loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), file[0]);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("imagektp",
                file[0].getName(), photoBody);

        RequestBody pdfBody = RequestBody.create(MediaType.parse("application/pdf"), file[1]);
        MultipartBody.Part pdfPart = MultipartBody.Part.createFormData("filepdf",
                file[1].getName(), pdfBody);

        final User user = new User();
        user.setNIP(eNIK.getText().toString());
        user.setEmail(eEmail.getText().toString());
        user.setName(eFullName.getText().toString());
        user.setPhone(ePhone.getText().toString());
        user.setExpdate(expDate.getText().toString());
        user.setPassword(ePassword.getText().toString());
        user.setKTP(file[0].getName());

        uploadService = new UploadService();
        uploadService.SignUpMultipart(user, photoPart, pdfPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ResponseUser baseResponse = (ResponseUser) response.body();

                if(baseResponse != null) {
                    if(baseResponse.getStatus().equals("success"))
                    {
                        pDialog.dismiss();

                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(user);

                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        pDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(RegisterActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void showFileChooser() {
        Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
        SetPutExtra(intent);
        startActivity(intent);
    }

    private void SetPutExtra(Intent intent)
    {
        if(image != null)
        {
            intent.putExtra("image_ktp",image.getAbsolutePath());
        }

        intent.putExtra("object",isObject);
        intent.putExtra("nik",eNIK.getText().toString());
        intent.putExtra("full_name",eFullName.getText().toString());
        intent.putExtra("email_address",eEmail.getText().toString());
        intent.putExtra("password",ePassword.getText().toString());
        intent.putExtra("phone_number",ePhone.getText().toString());
        intent.putExtra("city",eCity.getText().toString());
        intent.putExtra("exp_date",expDate.getText().toString());
        intent.putExtra("path_file",selectedFilePath);
    }

    private void GetPutExtra()
    {
        if(getIntent().getExtras()!=null){
            isObject = Integer.valueOf(getIntent().getIntExtra("object",0)) ;
            selectedFilePath = String.valueOf(getIntent().getStringExtra("path_file")) ;
            eNIK.setText(String.valueOf(getIntent().getStringExtra("nik")));
            eFullName.setText(String.valueOf(getIntent().getStringExtra("full_name")));
            eEmail.setText(String.valueOf(getIntent().getStringExtra("email_address")));
            ePassword.setText(String.valueOf(getIntent().getStringExtra("password")));
            ePhone.setText(String.valueOf(getIntent().getStringExtra("phone_number")));
            eCity.setText(String.valueOf(getIntent().getStringExtra("city")));
            expDate.setText(String.valueOf(getIntent().getStringExtra("exp_date")));
            uploadFile.setText(selectedFilePath);

            if(getIntent().getStringExtra("image_ktp") != null)
            {
                image = new File(getIntent().getStringExtra("image_ktp"));
                uri = Uri.fromFile(new File(image.getPath()));
                mCamera.setImageURI(uri);
            }
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
        SetPutExtra(intent);
        startActivity(intent);
    }

    private void SelectMedia()
    {
        final CharSequence[] options = {"Browse Image", "Browse PDF", "cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("upload file");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Browse Image"))
                {
                    showImageChooser();
                }
                else if (options[item].equals("Browse PDF"))
                {
                    showFileChooser();
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean ValidateRegister()
    {
        boolean isSuccess = false;
        if(image != null)
        {
            if(!eNIK.getText().toString().equals(""))
            {
                if(!eFullName.getText().toString().equals(""))
                {
                    if(!eEmail.getText().toString().equals(""))
                    {
                        if(!ePassword.getText().toString().equals(""))
                        {
                            if(!ePhone.getText().toString().equals(""))
                            {
                                if(!eCity.getText().toString().equals(""))
                                {
                                    if(!expDate.getText().toString().equals(""))
                                    {
                                        if(!uploadFile.getText().toString().equals(""))
                                        {
                                            isSuccess = true;
                                        }
                                        else
                                        {
                                            uploadFile.setError(getResources().getString(R.string.error_cant_empty));
                                            uploadFile.requestFocus();
                                        }
                                    }
                                    else
                                    {
                                        expDate.setError(getResources().getString(R.string.error_cant_empty));
                                        expDate.requestFocus();
                                    }
                                }
                                else
                                {
                                    eCity.setError(getResources().getString(R.string.error_cant_empty));
                                    eCity.requestFocus();
                                }
                            }
                            else
                            {
                                ePhone.setError(getResources().getString(R.string.error_cant_empty));
                                ePhone.requestFocus();
                            }
                        }
                        else
                        {
                            ePassword.setError(getResources().getString(R.string.error_cant_empty));
                            ePassword.requestFocus();
                        }
                    }
                    else
                    {
                        eEmail.setError(getResources().getString(R.string.error_cant_empty));
                        eEmail.requestFocus();
                    }
                }
                else
                {
                    eFullName.setError(getResources().getString(R.string.error_cant_empty));
                    eFullName.requestFocus();
                }
            }
            else
            {
                eNIK.setError(getResources().getString(R.string.error_cant_empty));
                eNIK.requestFocus();
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error_cant_empty), Toast.LENGTH_SHORT).show();
        }
        return isSuccess;
    }

    private void Back()
    {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Back();
    }
}
