package com.rusandre.passwordlayout;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    private TextView resultTextView;
    private View copyButton;
    private EditText sourceEditText;
    private TextView qualityTextView;
    private ImageView qualityImageView;
    private CompoundButton upperCaseCheckbox;
    private CompoundButton numberCheckbox;
    private CompoundButton specialCharCheckbox;
    private View generateButton;
    private SeekBar charCountSeekBar;
    private TextView generatedPasswordTextView;
    private TextView generatePasswordTitleTextView;
    private View copyGeneratedButton;

    private final int MINIMAL_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.result_text);
        sourceEditText = findViewById(R.id.source_text);
        qualityImageView = findViewById(R.id.image_quality);
        qualityTextView = findViewById(R.id.quality_text);
        copyButton = findViewById(R.id.button_copy);
        upperCaseCheckbox = findViewById(R.id.check_uppercase);
        numberCheckbox = findViewById(R.id.check_numbers);
        specialCharCheckbox = findViewById(R.id.check_special_characters);
        generateButton = findViewById(R.id.button_generate);
        charCountSeekBar = findViewById(R.id.seekbar_char_count);
        generatedPasswordTextView = findViewById(R.id.generated_password_text);
        generatePasswordTitleTextView = findViewById(R.id.textview_generate_password_title);
        copyGeneratedButton = findViewById(R.id.button_copy_generated_password);


        //Генератор паролей инициализируется наборами символов
        final PasswordHelper pwHelper = new PasswordHelper(
                getResources().getStringArray(R.array.russian),
                getResources().getStringArray(R.array.english),
                getResources().getString(R.string.upper_case_characters),
                getResources().getString(R.string.lower_case_characters),
                getResources().getString(R.string.numbers),
                getResources().getString(R.string.special_characters));

        //введенный пароль на русском транскрибируется на английский, качество отображается ниже
        sourceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultTextView.setText(pwHelper.convert(s));
                copyButton.setEnabled(s.length() > 0);
                int quality = pwHelper.getQuality(s);
                qualityTextView.setText(getResources().getStringArray(R.array.quality)[quality]);
                qualityImageView.setImageLevel(quality * 1000);
                if (s.length() == 0)
                    copyButton.setEnabled(false);
                else
                    copyButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //кнопка копирования выключается изначально
        copyButton.setEnabled(false);
        //Копирование в буффер
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_title), resultTextView.getText()));
                Toast.makeText(MainActivity.this, R.string.clipboard_message, Toast.LENGTH_SHORT).show();
            }
        });

        //Начальный заголовок генератора паролей, когда ползунок еще не двигался
        generatePasswordTitleTextView.setText(String.format(getResources().getString(R.string.password_length_title),
                getResources().getQuantityString(R.plurals.symbol_count, 0, 0),
                getResources().getQuantityString(R.plurals.symbol_count, MINIMAL_PASSWORD_LENGTH, MINIMAL_PASSWORD_LENGTH)));

        //Изменена заголовка при движения ползунка
        charCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                generatePasswordTitleTextView.setText(String.format(getResources().getString(R.string.password_length_title),
                        getResources().getQuantityString(R.plurals.symbol_count, progress, progress),
                        getResources().getQuantityString(R.plurals.symbol_count, progress + MINIMAL_PASSWORD_LENGTH, progress + MINIMAL_PASSWORD_LENGTH)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        //Генерация пароля
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedPasswordTextView.setText(
                        pwHelper.generatePassword(upperCaseCheckbox.isChecked(),
                                numberCheckbox.isChecked(),
                                specialCharCheckbox.isChecked(),
                                8 + charCountSeekBar.getProgress()));
                copyGeneratedButton.setEnabled(true);
            }
        });

        //Кнопка копирования изначально выключена
        copyGeneratedButton.setEnabled(false);
        //Копирование в буффер
        copyGeneratedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_title), generatedPasswordTextView.getText()));
                Toast.makeText(MainActivity.this, R.string.clipboard_message, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
